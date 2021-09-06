package bg.sofia.uni.fmi.mjt.todoist.server.functionality;

import bg.sofia.uni.fmi.mjt.todoist.account.Account;
import bg.sofia.uni.fmi.mjt.todoist.server.serverdatabase.Database;

import java.util.Map;

public class TodoistAuthenticator extends CommandHandler {

    public TodoistAuthenticator(String wholeCommand, Map<String, Account> accounts,
                                Map<Integer, Boolean> userLoginStatus, int currentUserPort,
                                boolean isTesting) {
        super(wholeCommand, accounts, userLoginStatus, currentUserPort, isTesting);
    }

    public String init() {

        String message;

        switch (commandInDetails[0]) {
            case "register" -> {
                return register();
            }
            case "login" -> {
                return login();
            }
            case "logout" -> {
                return logout();
            }
        }

        return "[ Unknown command ]"
                + System.lineSeparator();
    }

    private String register() {
        if (commandInDetails.length < 3) {
            return "[ Unable to register ]"
                    + System.lineSeparator();
        } else if (accounts.containsKey(commandInDetails[1])) {
            return "[ Username " + commandInDetails[1]
                    + " is already taken, select another one ]"
                    + System.lineSeparator();
        }

        final String username = commandInDetails[1];
        final String password = commandInDetails[2];

        Account addedAccount = new Account(username, password);
        accounts.put(username, addedAccount);

        if (!isTesting) {
            Database.updateDatabase(accounts);
        }

        return "[ Username " + username + " successfully registered ]"
                + System.lineSeparator();
    }

    private String login() {
        if (commandInDetails.length < 3) {
            return "[ Unable to login ]"
                    + System.lineSeparator();
        }
        final String username = commandInDetails[1];
        final String password = commandInDetails[2];
        if (accounts.containsValue(new Account(username, password))) {
            userLoginStatus.put(currentUserPort, true);
            accounts.get(username).addLoggedUser(currentUserPort);

            if (!isTesting) {
                Database.updateDatabase(accounts);
            }

            return "[ User " + username + " successfully logged in ]"
                    + System.lineSeparator();
        }

        return "[ Invalid username/password combination ]"
                + System.lineSeparator();
    }

    private String logout() {
        if (!userLoginStatus.get(currentUserPort)) {
            return "[ You are not logged in ]"
                    + System.lineSeparator();
        }

        for (String account : accounts.keySet()) {
            if (accounts.get(account).getLoggedUsers().contains(currentUserPort)) {
                accounts.get(account).removeLoggedUser(currentUserPort);
                userLoginStatus.put(currentUserPort, false);
            }
        }

        if (!isTesting) {
            Database.updateDatabase(accounts);
        }

        return "[ Successfully logged out ]" + System.lineSeparator();
    }
}

