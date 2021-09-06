package bg.sofia.uni.fmi.mjt.todoist.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class TodoistClient {
    // private static final int SERVER_PORT = 7777;
    private static final String SERVER_HOST = "localhost";
    private static final ByteBuffer buffer = ByteBuffer.allocateDirect(2048);

    public TodoistClient() {
    }

    public void ConnectToServer(int serverPort) {

        try (SocketChannel socketChannel = SocketChannel.open();
             Scanner scanner = new Scanner(System.in)) {

            socketChannel.connect(new InetSocketAddress(SERVER_HOST, serverPort));

            System.out.println("Connected to the server.");

            while (true) {
                System.out.print("Enter message: ");
                String message = scanner.nextLine(); // read a line from the console

                if ("disconnect".equals(message)) {
                    sendMessage(message, socketChannel);
                    receiveMessage(socketChannel);
                    break;
                }

                sendMessage(message, socketChannel);
                receiveMessage(socketChannel);
            }

        } catch (IOException e) {
            System.out.println("There is a problem with the network communication");
            e.printStackTrace();
        }
    }

    private void sendMessage(String message, SocketChannel socketChannel) {
        buffer.clear(); // switch to writing mode
        buffer.put(message.getBytes()); // buffer fill
        buffer.flip(); // switch to reading mode
        try {
            socketChannel.write(buffer); // buffer drain
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receiveMessage(SocketChannel socketChannel) {
        buffer.clear(); // switch to writing mode
        try {
            socketChannel.read(buffer); // buffer fill
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Cannot read message from server");
        }
        buffer.flip(); // switch to reading mode

        byte[] byteArray = new byte[buffer.remaining()];
        buffer.get(byteArray); // buffer drain
        try {
            String reply = new String(byteArray, "UTF-8");
            System.out.println(reply);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException("Cannot encode received message");
        }

    }

    public static void main(String[] args) {

        TodoistClient client = new TodoistClient();

        client.ConnectToServer(7777);

//        try (SocketChannel socketChannel = SocketChannel.open();
//             Scanner scanner = new Scanner(System.in)) {
//
//            socketChannel.connect(new InetSocketAddress(SERVER_HOST, SERVER_PORT));
//
//            System.out.println("Connected to the server.");
//
//            while (true) {
//                System.out.print("Enter message: ");
//                String message = scanner.nextLine(); // read a line from the console
//
//                if ("disconnect".equals(message)) {
//                    buffer.clear(); // switch to writing mode
//                    buffer.put(message.getBytes()); // buffer fill
//                    buffer.flip(); // switch to reading mode
//                    socketChannel.write(buffer); // buffer drain
//
//                    buffer.clear(); // switch to writing mode
//                    socketChannel.read(buffer); // buffer fill
//                    buffer.flip(); // switch to reading mode
//
//                    byte[] byteArray = new byte[buffer.remaining()];
//                    buffer.get(byteArray); // buffer drain
//                    String reply = new String(byteArray, "UTF-8");
//
//                    System.out.println(reply);
//                    break;
//                }
//
//                buffer.clear(); // switch to writing mode
//                buffer.put(message.getBytes()); // buffer fill
//                buffer.flip(); // switch to reading mode
//                socketChannel.write(buffer); // buffer drain
//
//                buffer.clear(); // switch to writing mode
//                socketChannel.read(buffer); // buffer fill
//                buffer.flip(); // switch to reading mode
//
//                byte[] byteArray = new byte[buffer.remaining()];
//                buffer.get(byteArray); // buffer drain
//                String reply = new String(byteArray, "UTF-8");
//
//                // if buffer is a non-direct one, is has a wrapped array and we can get it
//                // String reply = new String(buffer.array(), 0, buffer.position(), "UTF-8"); // buffer drain
//
//                System.out.println(reply);
//            }
//
//        } catch (IOException e) {
//            System.out.println("There is a problem with the network communication");
//            e.printStackTrace();
//        }
    }
}
