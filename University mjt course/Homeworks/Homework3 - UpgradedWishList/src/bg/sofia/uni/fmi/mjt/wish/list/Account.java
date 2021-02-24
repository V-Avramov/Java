package bg.sofia.uni.fmi.mjt.wish.list;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Account {

    private final String username;
    private final String password;
    private final List<Integer> loggedUsers;

    public Account(String username, String password) {
        this.username = username;
        this.password = password;

        this.loggedUsers = new CopyOnWriteArrayList<>();
    }

    public void addLoggedUsers(int userPort) {
        loggedUsers.add(userPort);
    }

    public void removeLoggedUsers(int userPort) {
        int loggedUsersSize = loggedUsers.size();
        for (int i = 0; i < loggedUsersSize; i++) {
            if (loggedUsers.get(i) == userPort) {
                loggedUsers.remove(i);
                return;
            }
        }
    }

    public List<Integer> getLoggedUsers() {
        return loggedUsers;
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
