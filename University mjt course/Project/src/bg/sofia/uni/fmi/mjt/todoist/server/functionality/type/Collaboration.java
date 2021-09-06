package bg.sofia.uni.fmi.mjt.todoist.server.functionality.type;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Collaboration {
    private String name;
    private List<String> users;
    private Map<String, Task> tasks;
    private String collabOwner;

    public Collaboration(String name,
                          String collabOwner) {
        this.name = name;
        this.users = new CopyOnWriteArrayList<>();
        this.tasks = new ConcurrentHashMap<>();
        this.collabOwner = collabOwner;
        this.users.add(collabOwner);
    }

    public String getName() {
        return name;
    }

    public String getCollabOwner() {
        return collabOwner;
    }

    public List<String> getUsers() {
        return users;
    }

    public boolean addUser(String username) {
        if (users.contains(username)) {
            return false;
        }
        users.add(username);
        return true;
    }

    public String assignTask(String taskName, String username) {
        if (tasks.containsKey(taskName)) {
            return "[ Task with this name already exists ]";
        }

        if (!users.contains(username)) {
            return "[ User that is given is not a member of the collaboration, consider adding them ]";
        }
        Task task = new Task(taskName, null, null, null);
        task.setAsignee(username);
        tasks.put(taskName, task);
        return "[ Task with name " + taskName + " assigned to user " + task.getAsignee() + " successfully ]";
    }

    public String listTasks() {
        String allTasks = "";
        for (String taskName : tasks.keySet()) {
            allTasks += tasks.get(taskName) + " assigned to " + tasks.get(taskName).getAsignee()
                    + System.lineSeparator();
        }

        if (allTasks.length() == 0) {
            allTasks = "[ No tasks present in the collaboration ]" + System.lineSeparator();
        }

        return allTasks;
    }

    public String listUsers() {
        String allUsers = "";

        for (int i = 0; i < users.size(); i++) {
            allUsers += users.get(i) + System.lineSeparator();
        }
        return allUsers;
    }

    @Override
    public String toString() {
        return "Collaboration name: " + getName()
                + System.lineSeparator()
                + "users:"
                + System.lineSeparator()
                + listUsers()
                + System.lineSeparator()
                + "tasks:"
                + System.lineSeparator()
                + listTasks();
    }
}
