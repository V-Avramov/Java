package bg.sofia.uni.fmi.mjt.wish.list;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
            Map<String, Account> accounts = new ConcurrentHashMap<>();
            //List<Account> accounts = new CopyOnWriteArrayList<>();
            Map<Integer, Boolean> userLoginStatus = new ConcurrentHashMap<>();

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

                        int currentUserPort = sc.socket().getPort();

                        userLoginStatus.putIfAbsent(currentUserPort, false);

                        //System.out.println(sc.getLocalAddress() + "Local address");
                        //System.out.println(sc.getRemoteAddress() + "Remote address");
                        //System.out.println(sc.socket().getPort());
                        buffer.clear();
                        int r = sc.read(buffer);
                        if (r < 0) {
                            System.out.println("Connection lost with user");
                            sc.close();
                            continue;
                        }
                        buffer.flip();

                        byte[] byteArray = new byte[r];
                        buffer.get(byteArray); // buffer drain
                        String letter = new String(byteArray, "UTF-8").trim();

                        ServerLogic serverLogic = new ServerLogic(letter, mapOfPresents, accounts,
                                userLoginStatus);

                        String message = serverLogic.serverMainLogic(currentUserPort);
                        sendReply(message, reply, sc);

                        if (message.equals("[ Disconnected from server ]" + System.lineSeparator())) {
                            sc.close();
                        }

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

    private void sendReply(String message, ByteBuffer reply, SocketChannel sc) {
        reply.clear();

        reply.put(message.getBytes());

        reply.flip();
        try {
            sc.write(reply);
        } catch (IOException e) {
            System.out.println("A problem appeared while replying");
            e.printStackTrace();
        }
    }
}