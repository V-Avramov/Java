package bg.sofia.uni.fmi.mjt.wish.list;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;
import java.util.Random;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class WishListServer {
    private static final String SERVER_HOST = "localhost";
    private static int SERVER_PORT; // = 7777;
    private static final int BUFFER_SIZE = 1024;

    private boolean isServerOn;

    public WishListServer(int port) {
        SERVER_PORT = port;
        isServerOn = true;
    }

    public void start() {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {

            serverSocketChannel.bind(new InetSocketAddress(SERVER_HOST, SERVER_PORT));
            serverSocketChannel.configureBlocking(false);

            Selector selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
            Map<String, List<String>> mapOfPresents = new ConcurrentHashMap<>();

            ByteBuffer reply = ByteBuffer.allocate(BUFFER_SIZE * 2);

            while (isServerOn) {
                int readyChannels = selector.selectNow();
                if (readyChannels == 0) {
                    // select() is blocking but may still return with 0, check javadoc
                    continue;
                }

                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    if (key.isReadable()) {
                        SocketChannel sc = (SocketChannel) key.channel();

                        buffer.clear();
                        int r = sc.read(buffer);

                        buffer.flip();

                        byte[] byteArray = new byte[r];
                        buffer.get(byteArray); // buffer drain
                        String letter = new String(byteArray, "UTF-8").trim();

                        String[] letterInDetails = letter.split(" ", 3);

                        if (letterInDetails[0].equals("disconnect")) {
                            String message = "[ Disconnected from server ]" + System.lineSeparator();

                            reply.clear();

                            reply.put(message.getBytes());

                            reply.flip();
                            sc.write(reply);
                            sc.close();
                            continue;
                        }

                        if (letterInDetails[0].equals("post-wish")) {

                            String message = postWish(mapOfPresents, letterInDetails) + System.lineSeparator();

                            reply.clear();

                            reply.put(message.getBytes());

                            reply.flip();
                            sc.write(reply);

                        } else if (letterInDetails[0].equals("get-wish")) {
                            String message = getWish(mapOfPresents) + System.lineSeparator();

                            reply.clear();

                            reply.put(message.getBytes());

                            reply.flip();
                            sc.write(reply);

                        } else {
                            String message = "[ Unknown command ]" + System.lineSeparator();

                            reply.clear();

                            reply.put(message.getBytes());

                            reply.flip();
                            sc.write(reply);

                        }

                        //sc.write(buffer);

                    } else if (key.isAcceptable()) {
                        ServerSocketChannel sockChannel = (ServerSocketChannel) key.channel();
                        SocketChannel accept = sockChannel.accept();
                        accept.configureBlocking(false);
                        accept.register(selector, SelectionKey.OP_READ);
                    }

                    keyIterator.remove();
                }

            }

        } catch (IOException e) {
            System.out.println("There is a problem with the server socket");
            e.printStackTrace();
        }
    }

    public void stop() {

        isServerOn = false;
    }

    private String getWish(Map<String, List<String>> mapOfPresents) {
        Set<String> studentSet = mapOfPresents.keySet();
        int studentSetSize = studentSet.size();

        if (studentSetSize <= 0) {
            return "[ There are no students present in the wish list ]";
        }

        int randomStudent = new Random().nextInt(studentSetSize);
        int i = 0;

        for (String student : studentSet) {
            if (i == randomStudent) {
                String resultMessage = "[ " + student + ": " + mapOfPresents.get(student).toString() + " ]";
                mapOfPresents.remove(student);
                return resultMessage;
            }
            i++;
        }
        return "[ There are no students present in the wish list ]";
    }

    private String postWish(Map<String, List<String>> mapOfPresents, String[] letterInDetails) {

        if (!mapOfPresents.containsKey(letterInDetails[1])) {
            mapOfPresents.put(letterInDetails[1], List.of(letterInDetails[2]));

            return "[ Gift " + letterInDetails[2]
                    + " for student " + letterInDetails[1] + " submitted successfully ]";
        } else {
            if (mapOfPresents.get(letterInDetails[1]).contains(letterInDetails[2])) {
                return "[ The same gift for student "
                        + letterInDetails[1] + " was already submitted ]";
            } else {
                List<String> currentListOfWishes = new ArrayList<>();
                currentListOfWishes.addAll(mapOfPresents.get(letterInDetails[1]));
                currentListOfWishes.add(letterInDetails[2]);
                mapOfPresents.put(letterInDetails[1], currentListOfWishes);

                return "[ Gift " + letterInDetails[2]
                        + " for student " + letterInDetails[1] + " submitted successfully ]";
            }
        }
    }
}