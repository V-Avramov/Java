package bg.sofia.uni.fmi.mjt.todoist.server.functionality;

import bg.sofia.uni.fmi.mjt.todoist.account.Account;
import bg.sofia.uni.fmi.mjt.todoist.commands.Commands;
import bg.sofia.uni.fmi.mjt.todoist.server.functionality.type.Collaboration;
import bg.sofia.uni.fmi.mjt.todoist.server.functionality.type.Task;
import bg.sofia.uni.fmi.mjt.todoist.server.serverdatabase.Database;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class MainFunctionality extends CommandHandler {

    private final Account currentAccount;

    public MainFunctionality(String wholeCommand, Map<String, Account> accounts,
                             Map<Integer, Boolean> userLoginStatus, int currentUserPort,
                             boolean isTesting) {
        super(wholeCommand, accounts, userLoginStatus, currentUserPort, isTesting);
        this.isTesting = isTesting;
        currentAccount = getCurrentAccount();
    }

    public String init() {

        final String commandName = commandInDetails[0];
        if (commandName.equals("add-task")) {
            return addTask();
        } else if (commandName.equals("update-task")) {
            return updateTask();
        } else if (commandName.equals("delete-task")) {
            return deleteTask();
        } else if (commandName.equals("get-task")) {
            return getTask();
        } else if (commandName.equals("list-tasks")) {
            return listTasks();
        } else if (commandName.equals("list-dashboard")) {
            return listDashboard();
        } else if (commandName.equals("finish-task")) {
            return finishTask();
        } else if (commandName.equals("add-collaboration")) {
            return addCollaboration();
        } else if (commandName.equals("delete-collaboration")) {
            return deleteCollaboration();
        } else if (commandName.equals("list-collaborations")) {
            return listCollaborations();
        } else if (commandName.equals("add-user")) {
            return addUser();
        } else if (commandName.equals("assign-task")) {
            return assignTask();
        } else if (commandName.equals("list-users")) {
            return listUsers();
        }

        return "[ Unknown command ]" + System.lineSeparator();
    }

    private String addTask() {

        if (commandInDetails.length == 1) {
            return "[ Cannot add anonymous task ]"
                    + System.lineSeparator();
        }

        // the following are arrays because we need to change their values in the parameterizeTask method

        final String[] name = new String[1];
        name[0] = null;
        LocalDateTime[] date = new LocalDateTime[1];
        date[0] = null;
        LocalDateTime[] dueDate = new LocalDateTime[1];
        String[] description = new String[1];

        try {
            parameterizeTask(name, date, dueDate, description);
        } catch (RuntimeException e) {
            return "[ Date parameters must be of type yyyy-MM-dd,"
                    + " in case of February 1st 2021 write 2021-02-01 ]"
                    + System.lineSeparator();
        }

        if (name[0] == null) {
            return "[ Cannot add anonymous task ]"
                    + System.lineSeparator();
        }

        String message;

        if (date[0] == null) {
            if (currentAccount.getInboxTasks().containsKey(name[0])) {
                return "[ Task with the same name already exists ]" + System.lineSeparator();
            }
            Task task = new Task(name[0], date[0], dueDate[0], description[0]);
            currentAccount.addInboxTask(task);

            message = "[ Task with name " + name[0] + " added to inbox successfully ]"
                    + System.lineSeparator();
        } else {
            for (LocalDateTime datedTask : currentAccount.getDatedTasks().keySet()) {
                for (int i = 0; i < currentAccount.getDatedTasks().get(datedTask).size(); i++) {
                    if (currentAccount.getDatedTasks().get(datedTask).get(i).name().equals(name[0])) {
                        return "[ Task with the same name already exists ]" + System.lineSeparator();
                    }
                }
            }
            Task task = new Task(name[0], date[0], dueDate[0], description[0]);
            currentAccount.addDatedTask(task);
            message = "[ Task with name " + name[0] + " and date " + date[0] + " added successfully ]"
                    + System.lineSeparator();
        }

        if (!isTesting) {
            Database.updateDatabase(accounts);
        }
        return message;
    }

    private String getTask() {
        /*if (commandInDetails.length == 1) {
            return "[ Cannot add anonymous task ]"
                    + System.lineSeparator();
        }

        if (!commandInDetails[1].split("=")[0].equals("--name")) {
            return "[ Cannot add anonymous task ]"
                    + System.lineSeparator();
        }*/

        String[] name = new String[1];
        name[0] = null;
        LocalDateTime[] date = new LocalDateTime[1];
        date[0] = null;
        try {
            parameterizeTask(name, date, null, null);
        } catch (RuntimeException e) {
            return "[ Date parameters must be of type yyyy-MM-dd,"
                    + " in case of February 1st 2021 write 2021-02-01 ]"
                    + System.lineSeparator();
        }

        if (name[0] == null) {
            return "[ Cannot get anonymous task ]"
                    + System.lineSeparator();
        }

        if (date[0] == null) {
            if (!currentAccount.getInboxTasks().containsKey(name[0])) {
                return "[ Task with name " + name[0] + " does not exist in inbox ]"
                        + System.lineSeparator();
            }
            return currentAccount.getInboxTasks().get(name[0]).toString();
        } else {

            if (!currentAccount.getDatedTasks().containsKey(date[0])) {
                return "[ Task with name " + name[0] + " and date " + date[0] + " does not exist ]"
                        + System.lineSeparator();
            }

            for (int i = 0; i < currentAccount.getDatedTasks().get(date[0]).size(); i++) {
                Task currentTask = currentAccount.getDatedTasks().get(date[0]).get(i);
                if (currentTask.name().equals(name[0])) {
                    return currentTask.toString();
                }
            }
        }
        return "[ Task with name " + name[0] + " and date " + date[0] + " does not exist ]"
                + System.lineSeparator();
        //final String name = commandInDetails[1].split("=")[1];

    }

    private String updateTask() {
        final String[] name = new String[1];
        name[0] = null;
        LocalDateTime[] date = new LocalDateTime[1];
        date[0] = null;
        LocalDateTime[] dueDate = new LocalDateTime[1];
        dueDate[0] = null;
        String[] description = new String[1];
        description[0] = null;

        try {
            parameterizeTask(name, date, dueDate, description);
        } catch (RuntimeException e) {
            return "[ Date parameters must be of type yyyy-MM-dd,"
                    + " in case of February 1st 2021 write 2021-02-01 ]"
                    + System.lineSeparator();
        }

        if (name[0] == null) {
            return "[ Cannot update anonymous task ]"
                    + System.lineSeparator();
        }

        if (date[0] == null) {

            if (!currentAccount.getInboxTasks().containsKey(name[0])) {
                return "[ Task named " + name[0] + " does not exist ]" + System.lineSeparator();
            }

            //currentAccount.getTasks().get(name[0]).setDate(date[0]);
            currentAccount.getInboxTasks().get(name[0]).updateDueDate(dueDate[0]);
            currentAccount.getInboxTasks().get(name[0]).updateDescription(description[0]);

            if (!isTesting) {
                Database.updateDatabase(accounts);
            }

            return "[ Task " + name[0] + " updated successfully ]" + System.lineSeparator();
        } else {

            if (!currentAccount.getDatedTasks().containsKey(date[0])) {
                return "[ Task named " + name[0] + " with date " + date[0] + " does not exist ]"
                        + System.lineSeparator();
            }

            for (int i = 0; i < currentAccount.getDatedTasks().get(date[0]).size(); i++) {
                Task currentTask = currentAccount.getDatedTasks().get(date[0]).get(i);
                if (currentTask.name().equals(name[0])) {
                    // many tasks with the same name but on different dates may exist
                    // in that case the date is not being updated
                    currentAccount.getDatedTasks().get(date[0]).get(i).updateDueDate(dueDate[0]);
                    currentAccount.getDatedTasks().get(date[0]).get(i).updateDescription(description[0]);
                    if (!isTesting) {
                        Database.updateDatabase(accounts);
                    }
                    return "[ Task " + name[0] + " with date " + date[0] +  " updated successfully ]"
                            + System.lineSeparator();
                }
            }
            return "[ Task named " + name[0] + " with date " + date[0] + " does not exist ]"
                    + System.lineSeparator();
        }
    }

    private String deleteTask() {

        String message;

        final String[] name = new String[1];
        LocalDateTime[] date = new LocalDateTime[1];
        date[0] = null;

        try {
            parameterizeTask(name, date, null, null);
        } catch (RuntimeException e) {
            return "[ Date parameters must be of type yyyy-MM-dd,"
                    + " in case of February 1st 2021 write 2021-02-01 ]"
                    + System.lineSeparator();
        }

        if (name[0] == null) {
            return "[ Cannot delete anonymous task ]"
                    + System.lineSeparator();
        }

        if (date[0] == null) {

            if (!currentAccount.getInboxTasks().containsKey(name[0])) {
                return "[ Requested task for deletion is nonexistent ]" + System.lineSeparator();
            }

            currentAccount.getInboxTasks().remove(name[0]);

            message =  "[ Task " + name[0] + " removed successfully ]" + System.lineSeparator();
        } else {
            if (!currentAccount.getDatedTasks().containsKey(date[0])) {
                return "[ Task named " + name[0] + " with date " + date[0] + " does not exist ]"
                        + System.lineSeparator();
            }

            int index = -1;
            for (int i = 0; i < currentAccount.getDatedTasks().get(date[0]).size(); i++) {
                if (currentAccount.getDatedTasks().get(date[0]).get(i).name().equals(name[0])) {
                    index = i;
                }
            }
            if (index < 0) {
                return "[ Task named " + name[0] + " with date " + date[0] + " does not exist ]"
                        + System.lineSeparator();
            } else {
                currentAccount.getDatedTasks().get(date[0]).remove(index);
                message = "[ Task " + name[0] + " and date " + date[0] + " removed successfully ]"
                        + System.lineSeparator();
            }
        }
        if (!isTesting) {
            Database.updateDatabase(accounts);
        }
        return message;
    }

    private String listDashboard() {
        String dashboard = "";
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime asDay = LocalDateTime.of(today.getYear(), today.getMonthValue(),
                today.getDayOfMonth(), 0, 0);
        if (currentAccount.getDatedTasks().isEmpty()) {
            return "[ Dashboard is empty for today ]" + System.lineSeparator();
        }
        for (int i = 0; i < currentAccount.getDatedTasks().get(asDay).size(); i++) {
            /*LocalDateTime currentTaskDate = currentAccount.getTasks().get(task).getDate();
            if (currentTaskDate == null) {
                continue;
            }*/
            dashboard += currentAccount.getDatedTasks().get(asDay).get(i);
        }
        return dashboard;
    }

    private String listTasks() {

        if (commandInDetails.length > 1 && commandInDetails[1].contains("--collaboration")) {
            return listCollaborationTasks();
        }

        String tasksToReceive = "";
        boolean isCompletedCalled = false;

        LocalDateTime[] date = new LocalDateTime[1];
        date[0] = null;

        try {
            parameterizeTask(null, date, null, null);
        } catch (RuntimeException e) {
            return "[ Date parameters must be of type yyyy-MM-dd,"
                    + " in case of February 1st write 02-01 ]"
                    + System.lineSeparator();
        }

        if (date[0] == null) {
            if (commandInDetails.length > 1 && commandInDetails[1].equals("--completed=true")) {
                isCompletedCalled = true;
            }

            for (String taskName : currentAccount.getInboxTasks().keySet()) {
                Task currentTask = currentAccount.getInboxTasks().get(taskName);
                if (isCompletedCalled && currentTask.isCompleted()) {
                    tasksToReceive += currentTask.toString();
                } else if (!isCompletedCalled) {
                    tasksToReceive += currentTask.toString();
                }
            }

            if (tasksToReceive.length() == 0) {
                tasksToReceive = "[ No tasks present ]" + System.lineSeparator();
            }
            return tasksToReceive;
        } else {

            if (!currentAccount.getDatedTasks().containsKey(date[0])) {
                tasksToReceive = "[ No tasks present ]" + System.lineSeparator();
                return tasksToReceive;
            }

            for (int i = 0; i < currentAccount.getDatedTasks().get(date[0]).size(); i++) {
                tasksToReceive += currentAccount.getDatedTasks().get(date[0]).get(i).toString();
            }

            if (tasksToReceive.length() == 0) {
                tasksToReceive = "[ No tasks present ]" + System.lineSeparator();
            }
            return tasksToReceive;
        }
    }

    private String finishTask() {

        final String[] name = new String[1];
        name[0] = null;
        LocalDateTime[] date = new LocalDateTime[1];
        date[0] = null;
        LocalDateTime[] dueDate = new LocalDateTime[1];
        String[] description = new String[1];

        try {
            parameterizeTask(name, date, dueDate, description);
        } catch (RuntimeException e) {
            return "[ Date parameters must be of type yyyy-MM-dd,"
                    + " in case of February 1st 2021 write 2021-02-01 ]"
                    + System.lineSeparator();
        }

        if (commandInDetails.length == 1) {
            return "[ Cannot complete an anonymous task ]"
                    + System.lineSeparator();
        }
        final String[] commandParameter = commandInDetails[1].split("=", 2);
        if (!commandParameter[0].equals("--name")) {
            return "[ Wrong parameter given, try the following ]" + System.lineSeparator() + Commands.FINISHTASK
                    + System.lineSeparator();
        } else if (!currentAccount.getInboxTasks().containsKey(name[0])) {
            return "[ Task with name " + name[0] + " does not exist ]" + System.lineSeparator();
        } else if (currentAccount.getInboxTasks().get(name[0]).isCompleted()) {
            return "[ Task with name " + name[0] + " has already been completed ]"
                    + System.lineSeparator();
        }

        currentAccount.getInboxTasks().get(name[0]).complete();
        if (!isTesting) {
            Database.updateDatabase(accounts);
        }
        return "[ Task marked as completed ]" + System.lineSeparator();
    }

    private void parameterizeTask(String[] name, LocalDateTime[] date, LocalDateTime[] dueDate,
                                  String[] description) throws RuntimeException {
        for (int i = 1; i < commandInDetails.length; i++) {
            //System.out.println(commandInDetails[i]);
            final String[] optionalParameter = commandInDetails[i].split("=", 2);
            //System.out.println(optionalParameter[0]);
            if (optionalParameter[0].equals("--name")) {
                name[0] = optionalParameter[1];
                // because the command is split with whitespace
                // we add all the text from the description
                i++;
                while (i < commandInDetails.length && !commandInDetails[i].contains("--")) {
                    if (commandInDetails[i].contains(System.lineSeparator())) {
                        name[0] += " " + commandInDetails[i].trim();
                        break;
                    }
                    name[0] += " " + commandInDetails[i];
                    i++;
                }
                if (i < commandInDetails.length) {
                    i--;
                }
            } else if (optionalParameter[0].equals("--date")) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                try {
                    date[0] = LocalDateTime.parse(optionalParameter[1] + " 00:00", formatter);
                } catch (Exception e) {
                    throw new RuntimeException("[ Date parameters must be of type yyyy-MM-dd,"
                            + " in case of February 1st 2021 write 2021-02-01 ]"
                            + System.lineSeparator());
                }
            } else if (optionalParameter[0].equals("--due-date")) {
                //String[] splitDueDate = optionalParameter[1].split("-");
                //System.out.println(splitDueDate[0] + " " + splitDueDate[1] + " " + splitDueDate[2]);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                try {
                    dueDate[0] = LocalDateTime.parse(optionalParameter[1] + " 00:00", formatter);
                } catch (Exception e) {
                    throw new RuntimeException("[ Date parameters must be of type yyyy-MM-dd,"
                            + " in case of February 1st 2021 write 2021-02-01 ]"
                            + System.lineSeparator());
                }
            } else if (optionalParameter[0].equals("--description")) {
                description[0] = optionalParameter[1];
                // because the command is split with whitespace
                // we add all the text from the description
                i++;
                while (i < commandInDetails.length && !commandInDetails[i].contains("--")) {
                    description[0] += " " + commandInDetails[i];
                    i++;
                }
                if (i < commandInDetails.length) {
                    i--;
                }
            }
        }
    }

    private String addCollaboration() {
        final String[] collabName = new String[1];

        parameterizeCollaboration(collabName, null, null);

        if (collabName[0] == null || collabName[0].isEmpty()) {
            return "[ Cannot add anonymous collaboration ]" + System.lineSeparator();
        }

        if (currentAccount.getCollaborations().containsKey(collabName[0])) {
            return "[ You are already in a collaboration with the same name ]" + System.lineSeparator();
        }

        Collaboration collab = new Collaboration(collabName[0], currentAccount.getUsername());
        currentAccount.getCollaborations().put(collabName[0], collab);

        if (!isTesting) {
            Database.updateDatabase(accounts);
        }

        return "[ Collaboration with name " + collabName[0] + " added successfully ]" + System.lineSeparator();
    }

    private String deleteCollaboration() {
        final String[] collabName = new String[1];

        parameterizeCollaboration(collabName, null, null);

        if (collabName[0] == null || collabName[0].isEmpty()) {
            return "[ Cannot delete anonymous collaboration ]" + System.lineSeparator();
        }
        String[] collabUsers;
        try {
            collabUsers = currentAccount.getCollaborations().
                    get(collabName[0]).
                    listUsers().
                    split(System.lineSeparator());
        } catch (Exception e) { // should always catch if the collaboration does not exist
            return "[ Collaboration is nonexistent ]" + System.lineSeparator();
        }
        final String collabOwner = currentAccount.getCollaborations().get(collabName[0]).getCollabOwner();
        if (!currentAccount.getUsername().equals(collabOwner)) {
            return "[ You must be the owner of the collaboration in order to delete it ]" + System.lineSeparator();
        }

        for (int i = 0; i < collabUsers.length; i++) {
            accounts.get(collabUsers[i]).getCollaborations().remove(collabName[0]);
        }

        if (!isTesting) {
            Database.updateDatabase(accounts);
        }

        return "[ Collaboration " + collabName[0] + " removed successfully ]" + System.lineSeparator();
    }

    private String listCollaborations() {
        String allCollaborations = "";
        for (String currentCollab : currentAccount.getCollaborations().keySet()) {
            allCollaborations += currentAccount.getCollaborations().get(currentCollab).toString();
        }
        if (allCollaborations.isEmpty()) {
            allCollaborations += "[ No collaborations ]" + System.lineSeparator();
        }
        return allCollaborations;
    }

    private String addUser() {
        final String[] collabName = new String[1];
        final String[] uname = new String[1];

        parameterizeCollaboration(collabName, uname, null);

        if (collabName[0] == null || collabName[0].isEmpty()) {
            return "[ Cannot add to anonymous collaboration ]" + System.lineSeparator();
        }
        if (uname[0] == null || uname[0].isEmpty()) {
            return "[ Cannot add anonymous user to collaboration ]" + System.lineSeparator();
        }
        if (!accounts.containsKey(uname[0])) {
            return "[ User does not exist in the system ]" + System.lineSeparator();
        }

        Collaboration currentCollaboration;
        try {
            currentCollaboration = currentAccount.getCollaborations().get(collabName[0]);
        } catch (Exception e) {
            return "[ Collaboration is nonexistent ]" + System.lineSeparator();
        }


        if (!currentCollaboration.addUser(uname[0])) {
            return "[ User with username " + uname[0] + " is already in the collaboration ]" + System.lineSeparator();
        }
        accounts.get(uname[0]).getCollaborations().put(collabName[0], currentCollaboration);

        if (!isTesting) {
            Database.updateDatabase(accounts);
        }

        return "[ User with username" + uname[0] + " added to the collaboration ]" + System.lineSeparator();
    }

    private String assignTask() {
        final String[] collabName = new String[1];
        final String[] uname = new String[1];
        final String[] taskName = new String[1];

        parameterizeCollaboration(collabName, uname, taskName);

        if (collabName[0] == null || collabName[0].isEmpty()
                || taskName[0] == null || taskName[0].isEmpty()) {
            return "[ command 'assign-task' cannot contain empty values of parameters consider using it as follows ]"
                    + System.lineSeparator()
                    + Commands.ASSIGNTASK
                    + System.lineSeparator();
        }
        Collaboration currentCollab;

        try {
            currentCollab = currentAccount.getCollaborations().get(collabName[0]);
        } catch (Exception e) {
            return "[ Collaboration is nonexistent ]" + System.lineSeparator();
        }

        if (currentCollab == null) {
            return "[ Collaboration is nonexistent ]" + System.lineSeparator();
        }

        String message;
        message = currentCollab.assignTask(taskName[0], uname[0]) + System.lineSeparator();

        if (message.contains("successfully") && !isTesting) {
            Database.updateDatabase(accounts);
        }

        return message;
    }

    private String listCollaborationTasks() {
        final String[] collabName = new String[1];

        parameterizeCollaboration(collabName, null, null);

        if (collabName[0] == null || collabName[0].isEmpty()) {
            return "[ Command list-tasks cannot receive empty parameters, consider the following ]"
                    + System.lineSeparator()
                    + Commands.LISTTASKS
                    + System.lineSeparator();
        }
        Collaboration currentCollab;

        try {
            currentCollab = currentAccount.getCollaborations().get(collabName[0]);
        } catch (Exception e) {
            return "[ Collaboration is nonexistent ]" + System.lineSeparator();
        }

        if (currentCollab == null) {
            return "[ Collaboration is nonexistent ]" + System.lineSeparator();
        }

        return currentCollab.listTasks();
    }

    private String listUsers() {
        final String[] collabName = new String[1];

        parameterizeCollaboration(collabName, null, null);

        if (collabName[0] == null || collabName[0].isEmpty()) {
            return "[ Command list-tasks cannot receive empty parameters, consider the following ]"
                    + System.lineSeparator()
                    + Commands.LISTTASKS
                    + System.lineSeparator();
        }
        Collaboration currentCollab;

        try {
            currentCollab = currentAccount.getCollaborations().get(collabName[0]);
        } catch (Exception e) {
            return "[ Collaboration is nonexistent ]" + System.lineSeparator();
        }

        if (currentCollab == null) {
            return "[ Collaboration is nonexistent ]" + System.lineSeparator();
        }

        return currentCollab.listUsers();
    }

    private void parameterizeCollaboration(String[] collabName, String[] uname, String[] taskName) {
        for (int i = 1; i < commandInDetails.length; i++) {
            //System.out.println(commandInDetails[i]);
            final String[] optionalParameter = commandInDetails[i].split("=", 2);
            //System.out.println(optionalParameter[0]);
            if (optionalParameter[0].equals("--name") || optionalParameter[0].equals("--collaboration")) {
                i = getWholeText(collabName, optionalParameter[1], i);
            } else if (optionalParameter[0].equals("--user")) {
                i = getWholeText(uname, optionalParameter[1], i);
            } else if (optionalParameter[0].equals("--task")) {
                i = getWholeText(taskName, optionalParameter[1], i);
            }
        }
    }

    private int getWholeText(String[] input, String optionalParameter, int i) {
        input[0] = optionalParameter;
        // because the command is split with whitespace
        // we add all the text from the input
        i++;
        while (i < commandInDetails.length && !commandInDetails[i].contains("--")) {
            if (commandInDetails[i].contains(System.lineSeparator())) {
                input[0] += " " + commandInDetails[i].trim();
                break;
            }
            input[0] += " " + commandInDetails[i];
            i++;
        }
        if (i < commandInDetails.length) {
            i--;
        }
        return i;
    }

    private Account getCurrentAccount() {
        for (String accountName : accounts.keySet()) {
            if (accounts.get(accountName).getLoggedUsers().contains(currentUserPort)) {
                return accounts.get(accountName);
            }
        }
        return null; // should not happen
    }
}
