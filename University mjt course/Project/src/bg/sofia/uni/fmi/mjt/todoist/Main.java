package bg.sofia.uni.fmi.mjt.todoist;

import bg.sofia.uni.fmi.mjt.todoist.server.TodoistServer;

public class Main {
    private static final int SERVER_PORT = 7777;
    public static void main(String[] args) {
        TodoistServer todoistServer = new TodoistServer(SERVER_PORT);
        todoistServer.start();
    }
}
