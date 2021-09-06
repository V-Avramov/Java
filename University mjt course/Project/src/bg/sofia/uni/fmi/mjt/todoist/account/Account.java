package bg.sofia.uni.fmi.mjt.todoist.account;

import bg.sofia.uni.fmi.mjt.todoist.server.functionality.type.Collaboration;
import bg.sofia.uni.fmi.mjt.todoist.server.functionality.type.Task;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Account {

    private final String username;
    private final String password;
    private final List<Integer> loggedUsers;
    private final Map<String, Task> inboxTasks;
    private final Map<LocalDateTime, List<Task>> datedTasks;
    private final Map<String, Collaboration> collaborations;

    public Account(String username, String password) {
        this.username = username;
        this.password = password;

        this.loggedUsers = new CopyOnWriteArrayList<>();
        this.inboxTasks = new ConcurrentHashMap<>();
        this.datedTasks = new ConcurrentHashMap<>();
        this.collaborations = new ConcurrentHashMap<>();
    }

    public String getUsername() {
        return username;
    }

    public void addLoggedUser(int userPort) {
        loggedUsers.add(userPort);
    }

    public void removeLoggedUser(int userPort) {
        int loggedUsersSize = loggedUsers.size();
        for (int i = 0; i < loggedUsersSize; i++) {
            if (loggedUsers.get(i) == userPort) {
                loggedUsers.remove(i);
                return;
            }
        }
    }

    public Map<String, Task> getInboxTasks() {
        return inboxTasks;
    }

    public void addInboxTask(Task task) {
        inboxTasks.put(task.name(), task);
    }

    public Map<LocalDateTime, List<Task>> getDatedTasks() {
        return datedTasks;  // might need a fix
    }

    public void addDatedTask(Task task) {
        datedTasks.putIfAbsent(task.getDate(), new LinkedList<>());
        datedTasks.get(task.getDate()).add(task);
    }

    public List<Integer> getLoggedUsers() {
        return loggedUsers;
    }

    public Map<String, Collaboration> getCollaborations() {
        return collaborations;
    }

    @Override
    public int hashCode() {
        return 31 * username.hashCode() + password.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Account)) {
            return false;
        }

        Account acc = (Account) obj;
        return username.equals(acc.username) && password.equals(acc.password);
    }
}
