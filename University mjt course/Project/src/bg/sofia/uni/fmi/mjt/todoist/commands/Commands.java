package bg.sofia.uni.fmi.mjt.todoist.commands;

public class Commands {

    public static final String ADDTASK = "add-task --name=<name> --date=<date> "
            + "--due-date=<due-date> --description=<description> - "
            + "adds a task with a name, date(optional), due date(optional) and description";
    public static final String UPDATETASK = "update-task --name=<name> --date=<date>"
            + " --due-date=<due-date> --description=<description> - "
            + "changes the attributes of the tasks(without the name)";
    public static final String DELETETASK = "delete-task --name=<task_name> --date=<date>(otpional) - deletes a task";
    public static final String GETTASK = "get-task --name=<task_name> --date=<date>(optional)";
    public static final String LISTTASKS = "for tasks:"
            + System.lineSeparator()
            + "list-tasks --completed=true(optional) --date=<date>(optional)"
            + System.lineSeparator()
            + "for collaborations:"
            + System.lineSeparator()
            + " list-tasks --collaboration=<name> - lists out the tasks of the given collaboration";
    public static final String LISTDASHBOARD = "list-dashboard - gives information for all the tasks for this day";
    public static final String FINISHTASK = "finish-task --name=<name> - marks task as completed";
    public static final String ADDCOLLABORATION = "add-collaboration --name=<name>";
    public static final String DELETECOLLABORATION = "delete-collaboration --name=<name>";
    public static final String LISTCOLLABORATIONS = "list-collaborations - lists the collaborations"
            + " of the current user";
    public static final String ADDUSER = "add-user --collaboration=<name> --user=<name>";
    public static final String ASSIGNTASK = "assign-task --collaboration=<name> --user=<username> --task=<name>";
    public static final String LISTUSERS = "list-users --collaboration=<name>";

    public static String listOutCommands() {
        final String commands = "help - lists out commands" + System.lineSeparator()
                + ADDTASK + System.lineSeparator()
                + UPDATETASK + System.lineSeparator()
                + DELETETASK + System.lineSeparator()
                + GETTASK + System.lineSeparator()
                + LISTTASKS + System.lineSeparator()
                + LISTDASHBOARD + System.lineSeparator()
                + FINISHTASK + System.lineSeparator()
                + ADDCOLLABORATION + System.lineSeparator()
                + DELETECOLLABORATION + System.lineSeparator()
                + LISTCOLLABORATIONS + System.lineSeparator()
                + ADDUSER + System.lineSeparator()
                + ASSIGNTASK + System.lineSeparator()
                + LISTUSERS + System.lineSeparator()
                + "----------------------------------"
                + "<something> is just a placeholder, ignore the '<' and '>'"
                + System.lineSeparator();

        return commands;
    }
}
