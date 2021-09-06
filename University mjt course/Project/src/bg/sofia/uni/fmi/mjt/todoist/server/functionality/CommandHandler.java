package bg.sofia.uni.fmi.mjt.todoist.server.functionality;

import bg.sofia.uni.fmi.mjt.todoist.account.Account;
import bg.sofia.uni.fmi.mjt.todoist.commands.Commands;
import bg.sofia.uni.fmi.mjt.todoist.server.serverdatabase.Database;

import java.util.Map;

public class CommandHandler {
    protected String wholeCommand;
    protected String[] commandInDetails;
    Map<String, Account> accounts;
    Map<Integer, Boolean> userLoginStatus;
    int currentUserPort;
    protected boolean isTesting;

    public CommandHandler(String wholeCommand, Map<String, Account> accounts,
                          Map<Integer, Boolean> userLoginStatus, int currentUserPort,
                          boolean isTesting) {
        this.wholeCommand = wholeCommand;
        try {
            this.commandInDetails = wholeCommand.split(" ");
        } catch (NullPointerException e) {
            wholeCommand = "";
        }
        this.accounts = accounts;
        this.userLoginStatus = userLoginStatus;
        this.currentUserPort = currentUserPort;
        this.isTesting = isTesting;
    }

    public String execute() {
        if (wholeCommand == null || wholeCommand.length() == 0) {
            return "[ Incorrect input ]" + System.lineSeparator();
        }

        String[] commandInDetails = wholeCommand.split(" ");
        if (commandInDetails[0].equals("disconnect")) {
            if (userLoginStatus.get(currentUserPort)) {
                for (String accountName : accounts.keySet()) {
                    if (accounts.get(accountName).getLoggedUsers().contains(currentUserPort)) {
                        accounts.get(accountName).getLoggedUsers().remove((Object) currentUserPort);
                    }
                }
            }
            userLoginStatus.remove(currentUserPort);
            if (!isTesting) {
                Database.updateDatabase(accounts);
            }
            return "[ Disconnected from server ]" + System.lineSeparator();
        }

        if (commandInDetails[0].equals("help")) {
            return Commands.listOutCommands();
        }

        if (!userLoginStatus.get(currentUserPort) || commandInDetails[0].equals("logout")) {
            TodoistAuthenticator authenticator = new TodoistAuthenticator(wholeCommand, accounts,
                    userLoginStatus, currentUserPort, isTesting);

            return authenticator.init();
        }

        MainFunctionality mainFunctionality = new MainFunctionality(wholeCommand, accounts, userLoginStatus,
                currentUserPort, isTesting);

        return mainFunctionality.init();
    }
}
