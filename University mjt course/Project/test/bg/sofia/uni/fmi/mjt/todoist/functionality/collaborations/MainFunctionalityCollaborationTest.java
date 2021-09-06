package bg.sofia.uni.fmi.mjt.todoist.functionality.collaborations;

import bg.sofia.uni.fmi.mjt.todoist.account.Account;
import bg.sofia.uni.fmi.mjt.todoist.commands.Commands;
import bg.sofia.uni.fmi.mjt.todoist.server.functionality.MainFunctionality;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;

public class MainFunctionalityCollaborationTest {
    private static Account account;
    private static Map<String, Account> accounts;
    private static Map<Integer, Boolean> userLoginStatus;
    private static int currentPort;
    private final boolean isTesting = true;

    @Before
    public void setUp() {

        account = new Account("user", "pass");
        accounts = new HashMap<>();

        userLoginStatus = new HashMap<>();
        currentPort = 1;
        account.addLoggedUser(currentPort);
        userLoginStatus.put(currentPort, true);
        accounts.put("user", account);
    }

    @Test
    public void testAddCollaboration() {
        String wholeCommand = "add-collaboration --name=XML project";

        MainFunctionality mainFunctionality = new MainFunctionality(wholeCommand, accounts, userLoginStatus,
                currentPort, isTesting);

        String expectedResponse = "[ Collaboration with name XML project added successfully ]"
                + System.lineSeparator();
        String actualResponse = mainFunctionality.init();

        assertEquals("Incorrect add add collaboration output", expectedResponse, actualResponse);
        Assert.assertEquals("Should add collaboration", 1, account.getCollaborations().size());
    }

    @Test
    public void testAddCollaborationNoNameGiven() {
        String wholeCommand = "add-collaboration --name=";

        MainFunctionality mainFunctionality = new MainFunctionality(wholeCommand, accounts, userLoginStatus,
                currentPort, isTesting);

        String expectedResponse = "[ Cannot add anonymous collaboration ]"
                + System.lineSeparator();
        String actualResponse = mainFunctionality.init();

        assertEquals("Incorrect add add collaboration output", expectedResponse, actualResponse);
        Assert.assertEquals("Should add collaboration", 0, account.getCollaborations().size());
    }

    @Test
    public void testAddExistingCollaboration() {
        String setupCommand = "add-collaboration --name=Front-end project";

        MainFunctionality setupFunctionality = new MainFunctionality(setupCommand, accounts, userLoginStatus,
                currentPort, isTesting);
        setupFunctionality.init();

        String wholeCommand = "add-collaboration --name=Front-end project";

        MainFunctionality mainFunctionality = new MainFunctionality(wholeCommand, accounts, userLoginStatus,
                currentPort, isTesting);

        String expectedResponse = "[ You are already in a collaboration with the same name ]"
                + System.lineSeparator();
        String actualResponse = mainFunctionality.init();

        assertEquals("Incorrect add add collaboration output", expectedResponse, actualResponse);
        Assert.assertEquals("Should add collaboration", 1, account.getCollaborations().size());
    }

    @Test
    public void testDeleteCollaboration() {
        String setupCommand = "add-collaboration --name=Front-end project";

        MainFunctionality setupFunctionality = new MainFunctionality(setupCommand, accounts, userLoginStatus,
                currentPort, isTesting);
        setupFunctionality.init();

        String wholeCommand = "delete-collaboration --name=Front-end project";

        MainFunctionality mainFunctionality = new MainFunctionality(wholeCommand, accounts, userLoginStatus,
                currentPort, isTesting);

        String expectedResponse = "[ Collaboration Front-end project removed successfully ]"
                + System.lineSeparator();
        String actualResponse = mainFunctionality.init();

        assertEquals("Incorrect add add collaboration output", expectedResponse, actualResponse);
        Assert.assertEquals("Should delete collaboration", 0, account.getCollaborations().size());
    }

    @Test
    public void testDeleteCollaborationWithoutStatingaName() {
        String setupCommand = "add-collaboration --name=Front-end project";

        MainFunctionality setupFunctionality = new MainFunctionality(setupCommand, accounts, userLoginStatus,
                currentPort, isTesting);
        setupFunctionality.init();

        String wholeCommand = "delete-collaboration";

        MainFunctionality mainFunctionality = new MainFunctionality(wholeCommand, accounts, userLoginStatus,
                currentPort, isTesting);

        String expectedResponse = "[ Cannot delete anonymous collaboration ]"
                + System.lineSeparator();
        String actualResponse = mainFunctionality.init();

        assertEquals("Incorrect add add collaboration output", expectedResponse, actualResponse);
        Assert.assertEquals("Should not delete collaboration", 1, account.getCollaborations().size());
    }

    @Test
    public void testDeleteCollaborationFromNonOwnerUser() {
        final int nonCurrentUserPort = 2;

        Account accountOfSecondUser = new Account("project initiate", "pass123");

        accountOfSecondUser.addLoggedUser(nonCurrentUserPort);
        userLoginStatus.put(nonCurrentUserPort, true);
        accounts.put(accountOfSecondUser.getUsername(), accountOfSecondUser);
        String setupCommand = "add-collaboration --name=Front-end project";

        MainFunctionality setupFunctionality = new MainFunctionality(setupCommand, accounts, userLoginStatus,
                nonCurrentUserPort, isTesting);
        setupFunctionality.init();

        String addUserToCollaborationCommand = "add-user --collaboration=Front-end project --user=user";

        MainFunctionality addUserToCollaboration = new MainFunctionality(addUserToCollaborationCommand,
                accounts, userLoginStatus, nonCurrentUserPort, isTesting);
        addUserToCollaboration.init();

        String wholeCommand = "delete-collaboration --name=Front-end project";

        MainFunctionality mainFunctionality = new MainFunctionality(wholeCommand, accounts, userLoginStatus,
                currentPort, isTesting);

        String expectedResponse = "[ You must be the owner of the collaboration in order to delete it ]"
                + System.lineSeparator();
        String actualResponse = mainFunctionality.init();

        assertEquals("Incorrect add add collaboration output", expectedResponse, actualResponse);
        Assert.assertEquals("Should not delete collaboration", 1, account.getCollaborations().size());
    }

    @Test
    public void testDeleteNonexistentCollaboration() {
        String wholeCommand = "delete-collaboration --name=Front-end project";

        MainFunctionality mainFunctionality = new MainFunctionality(wholeCommand, accounts, userLoginStatus,
                currentPort, isTesting);

        String expectedResponse = "[ Collaboration is nonexistent ]"
                + System.lineSeparator();
        String actualResponse = mainFunctionality.init();

        assertEquals("Incorrect add add collaboration output", expectedResponse, actualResponse);
        Assert.assertEquals("Should delete collaboration", 0, account.getCollaborations().size());
    }

    @Test
    public void testListCollaborations() {
        final int nonCurrentUserPort = 2;

        Account accountOfSecondUser = new Account("project initiate", "pass123");

        accountOfSecondUser.addLoggedUser(nonCurrentUserPort);
        userLoginStatus.put(nonCurrentUserPort, true);
        accounts.put(accountOfSecondUser.getUsername(), accountOfSecondUser);
        String setupCommand = "add-collaboration --name=Front-end project";

        MainFunctionality setupFunctionality = new MainFunctionality(setupCommand, accounts, userLoginStatus,
                nonCurrentUserPort, isTesting);
        setupFunctionality.init();

        //accounts.put(accountOfSecondUser.getUsername(), accountOfSecondUser);
        String setupSecondCollabCommand = "add-collaboration --name=Back-end project";

        MainFunctionality setupFunctionalitySecondCollab = new MainFunctionality(setupSecondCollabCommand,
                accounts, userLoginStatus, nonCurrentUserPort, isTesting);
        setupFunctionalitySecondCollab.init();

        String addUserToCollaborationCommand = "add-user --collaboration=Back-end project --user=user";

        MainFunctionality addUserToCollaboration = new MainFunctionality(addUserToCollaborationCommand,
                accounts, userLoginStatus, nonCurrentUserPort, isTesting);
        addUserToCollaboration.init();

        String assignTaskCommand = "assign-task --user=user --collaboration=Back-end project"
                + " --task=Do the tests";

        MainFunctionality assignTask = new MainFunctionality(assignTaskCommand,
                accounts, userLoginStatus, nonCurrentUserPort, isTesting);
        assignTask.init();

        String listCommand = "list-collaborations";

        MainFunctionality mainFunctionality = new MainFunctionality(listCommand,
                accounts, userLoginStatus, nonCurrentUserPort, isTesting);

        String expectedResponse = "Collaboration name: Back-end project"
                + System.lineSeparator()
                + "users:"
                + System.lineSeparator()
                + "project initiate"
                + System.lineSeparator()
                + "user"
                + System.lineSeparator()
                + System.lineSeparator()
                + "tasks:"
                + System.lineSeparator()
                + "name: Do the tests"
                + System.lineSeparator()
                + " assigned to user"
                + System.lineSeparator()
                + "Collaboration name: Front-end project"
                + System.lineSeparator()
                + "users:"
                + System.lineSeparator()
                + "project initiate"
                + System.lineSeparator()
                + System.lineSeparator()
                + "tasks:"
                + System.lineSeparator()
                + "[ No tasks present in the collaboration ]"
                + System.lineSeparator();
        String actualResponse = mainFunctionality.init();

        assertEquals("Incorrect add add collaboration output", expectedResponse, actualResponse);
        Assert.assertEquals("Should not add the user to collaboration Back-end project", 2,
                accountOfSecondUser.getCollaborations().size());
    }

    @Test
    public void testAddUserToCollaborationWithoutGivingNameToTheCollaboration() {
        final int nonCurrentUserPort = 2;

        Account accountOfSecondUser = new Account("project initiate", "pass123");

        accountOfSecondUser.addLoggedUser(nonCurrentUserPort);
        userLoginStatus.put(nonCurrentUserPort, true);
        accounts.put(accountOfSecondUser.getUsername(), accountOfSecondUser);
        String setupCommand = "add-collaboration --name=Front-end project";

        MainFunctionality setupFunctionality = new MainFunctionality(setupCommand, accounts, userLoginStatus,
                nonCurrentUserPort, isTesting);
        setupFunctionality.init();

        String addUserToCollaborationCommand = "add-user --collaboration= --user=user";

        MainFunctionality addUserToCollaboration = new MainFunctionality(addUserToCollaborationCommand,
                accounts, userLoginStatus, nonCurrentUserPort, isTesting);
        addUserToCollaboration.init();

        String expectedResponse = "[ Cannot add to anonymous collaboration ]"
                + System.lineSeparator();
        String actualResponse = addUserToCollaboration.init();

        assertEquals("Incorrect add add collaboration output", expectedResponse, actualResponse);
        Assert.assertEquals("Should not add the user to collaboration Front-end project", 1,
                accountOfSecondUser.getCollaborations().get("Front-end project").getUsers().size());
    }

    @Test
    public void testAddUserToCollaborationWithoutGivingNameToTheUser() {
        final int nonCurrentUserPort = 2;

        Account accountOfSecondUser = new Account("project initiate", "pass123");

        accountOfSecondUser.addLoggedUser(nonCurrentUserPort);
        userLoginStatus.put(nonCurrentUserPort, true);
        accounts.put(accountOfSecondUser.getUsername(), accountOfSecondUser);
        String setupCommand = "add-collaboration --name=Front-end project";

        MainFunctionality setupFunctionality = new MainFunctionality(setupCommand, accounts, userLoginStatus,
                nonCurrentUserPort, isTesting);
        setupFunctionality.init();

        String addUserToCollaborationCommand = "add-user --collaboration=Front-end project --user=";

        MainFunctionality addUserToCollaboration = new MainFunctionality(addUserToCollaborationCommand,
                accounts, userLoginStatus, nonCurrentUserPort, isTesting);
        addUserToCollaboration.init();

        String expectedResponse = "[ Cannot add anonymous user to collaboration ]"
                + System.lineSeparator();
        String actualResponse = addUserToCollaboration.init();

        assertEquals("Incorrect add add collaboration output", expectedResponse, actualResponse);
        Assert.assertEquals("Should not add the user to collaboration Front-end project", 1,
                accountOfSecondUser.getCollaborations().get("Front-end project").getUsers().size());
    }

    @Test
    public void testListCollaborationsWhenTheCollaborationsAreNone() {
        final int nonCurrentUserPort = 2;

        Account accountOfSecondUser = new Account("project initiate", "pass123");

        accountOfSecondUser.addLoggedUser(nonCurrentUserPort);
        userLoginStatus.put(nonCurrentUserPort, true);
        accounts.put(accountOfSecondUser.getUsername(), accountOfSecondUser);

        String listCommand = "list-collaborations";

        MainFunctionality mainFunctionality = new MainFunctionality(listCommand,
                accounts, userLoginStatus, nonCurrentUserPort, isTesting);

        String expectedResponse = "[ No collaborations ]"
                + System.lineSeparator();
        String actualResponse = mainFunctionality.init();

        assertEquals("Incorrect add add collaboration output", expectedResponse, actualResponse);
        Assert.assertEquals("Should not add the user to collaboration Back-end project", 0,
                accountOfSecondUser.getCollaborations().size());
    }

    @Test
    public void testAssignTask() {
        final int nonCurrentUserPort = 2;

        Account accountOfSecondUser = new Account("project initiate", "pass123");

        accountOfSecondUser.addLoggedUser(nonCurrentUserPort);
        userLoginStatus.put(nonCurrentUserPort, true);
        accounts.put(accountOfSecondUser.getUsername(), accountOfSecondUser);
        String setupCommand = "add-collaboration --name=Back-end project";

        MainFunctionality setupFunctionality = new MainFunctionality(setupCommand, accounts, userLoginStatus,
                nonCurrentUserPort, isTesting);
        setupFunctionality.init();

        String addUserToCollaborationCommand = "add-user --collaboration=Back-end project --user=user";

        MainFunctionality addUserToCollaboration = new MainFunctionality(addUserToCollaborationCommand, accounts,
                userLoginStatus, nonCurrentUserPort, isTesting);

        addUserToCollaboration.init();

        String assignTaskCommand = "assign-task --collaboration=Back-end project --user=user"
                + " --task=Do the tests";

        MainFunctionality mainFunctionality = new MainFunctionality(assignTaskCommand,
                accounts, userLoginStatus, nonCurrentUserPort, isTesting);
        //addUserToCollaboration.init();

        String expectedResponse = "[ Task with name Do the tests assigned to user user successfully ]"
                + System.lineSeparator();
        String actualResponse = mainFunctionality.init();

        assertEquals("Incorrect add add collaboration output", expectedResponse, actualResponse);
        Assert.assertEquals("Should not add the user to collaboration Back-end project", 2,
                accountOfSecondUser.getCollaborations()
                        .get("Back-end project")
                        .listTasks()
                        .split(System.lineSeparator()).length);
    }

    @Test
    public void testAssignTaskWithEmptyParameters() {
        final int nonCurrentUserPort = 2;

        Account accountOfSecondUser = new Account("project initiate", "pass123");

        accountOfSecondUser.addLoggedUser(nonCurrentUserPort);
        userLoginStatus.put(nonCurrentUserPort, true);
        accounts.put(accountOfSecondUser.getUsername(), accountOfSecondUser);
        String setupCommand = "add-collaboration --name=Back-end project";

        MainFunctionality setupFunctionality = new MainFunctionality(setupCommand, accounts, userLoginStatus,
                nonCurrentUserPort, isTesting);
        setupFunctionality.init();

        String addUserToCollaborationCommand = "add-user --collaboration=Back-end project --user=user";

        MainFunctionality addUserToCollaboration = new MainFunctionality(addUserToCollaborationCommand, accounts,
                userLoginStatus, nonCurrentUserPort, isTesting);

        addUserToCollaboration.init();

        String assignTaskCommand = "assign-task --user="
                + " --task=Do the tests";

        MainFunctionality mainFunctionality = new MainFunctionality(assignTaskCommand,
                accounts, userLoginStatus, nonCurrentUserPort, isTesting);
        //addUserToCollaboration.init();

        String expectedResponse = "[ command 'assign-task' cannot contain empty values of parameters"
                + " consider using it as follows ]"
                + System.lineSeparator()
                + Commands.ASSIGNTASK
                + System.lineSeparator();
        String actualResponse = mainFunctionality.init();

        assertEquals("Incorrect add add collaboration output", expectedResponse, actualResponse);
        Assert.assertEquals("Should not add the user to collaboration Back-end project", 1,
                accountOfSecondUser.getCollaborations()
                        .get("Back-end project")
                        .listTasks()
                        .split(System.lineSeparator()).length);
    }

    @Test
    public void testAssignTaskWithNonexistentCollaboration() {
        final int nonCurrentUserPort = 2;

        Account accountOfSecondUser = new Account("project initiate", "pass123");

        accountOfSecondUser.addLoggedUser(nonCurrentUserPort);
        userLoginStatus.put(nonCurrentUserPort, true);
        accounts.put(accountOfSecondUser.getUsername(), accountOfSecondUser);
        String setupCommand = "add-collaboration --name=Back-end project";

        MainFunctionality setupFunctionality = new MainFunctionality(setupCommand, accounts, userLoginStatus,
                nonCurrentUserPort, isTesting);
        setupFunctionality.init();

        String addUserToCollaborationCommand = "add-user --collaboration=Back-end project --user=user";

        MainFunctionality addUserToCollaboration = new MainFunctionality(addUserToCollaborationCommand, accounts,
                userLoginStatus, nonCurrentUserPort, isTesting);

        addUserToCollaboration.init();

        String assignTaskCommand = "assign-task --user= --collaboration=Front-end project"
                + " --task=Do the tests";

        MainFunctionality mainFunctionality = new MainFunctionality(assignTaskCommand,
                accounts, userLoginStatus, nonCurrentUserPort, isTesting);
        //addUserToCollaboration.init();

        String expectedResponse = "[ Collaboration is nonexistent ]"
                + System.lineSeparator();
        String actualResponse = mainFunctionality.init();

        assertEquals("Incorrect add add collaboration output", expectedResponse, actualResponse);
        Assert.assertEquals("Should not add the user to collaboration Back-end project", 1,
                accountOfSecondUser.getCollaborations()
                        .get("Back-end project")
                        .listTasks()
                        .split(System.lineSeparator()).length);
    }

    @Test
    public void testAssignTaskWithAlreadyExistingTask() {
        final int nonCurrentUserPort = 2;

        Account accountOfSecondUser = new Account("project initiate", "pass123");

        accountOfSecondUser.addLoggedUser(nonCurrentUserPort);
        userLoginStatus.put(nonCurrentUserPort, true);
        accounts.put(accountOfSecondUser.getUsername(), accountOfSecondUser);
        String setupCommand = "add-collaboration --name=Back-end project";

        MainFunctionality setupFunctionality = new MainFunctionality(setupCommand, accounts, userLoginStatus,
                nonCurrentUserPort, isTesting);
        setupFunctionality.init();

        String addUserToCollaborationCommand = "add-user --collaboration=Back-end project --user=user";

        MainFunctionality addUserToCollaboration = new MainFunctionality(addUserToCollaborationCommand, accounts,
                userLoginStatus, nonCurrentUserPort, isTesting);

        addUserToCollaboration.init();

        String assignTaskCommand = "assign-task --user=user --collaboration=Back-end project"
                + " --task=Do the tests";

        MainFunctionality addTask = new MainFunctionality(assignTaskCommand,
                accounts, userLoginStatus, nonCurrentUserPort, isTesting);
        addTask.init();

        String assignTaskCommandSecondTime = "assign-task --user=user --collaboration=Back-end project"
                + " --task=Do the tests";

        MainFunctionality mainFunctionality = new MainFunctionality(assignTaskCommandSecondTime,
                accounts, userLoginStatus, nonCurrentUserPort, isTesting);

        String expectedResponse = "[ Task with this name already exists ]"
                + System.lineSeparator();
        String actualResponse = mainFunctionality.init();

        assertEquals("Incorrect add add collaboration output", expectedResponse, actualResponse);
        Assert.assertEquals("Should not add the user to collaboration Back-end project", 2,
                accountOfSecondUser.getCollaborations()
                        .get("Back-end project")
                        .listTasks()
                        .split(System.lineSeparator()).length);
    }

    @Test
    public void testAssignTaskWithNonAddedUserToTheCollaboration() {
        final int nonCurrentUserPort = 2;

        Account accountOfSecondUser = new Account("project initiate", "pass123");

        accountOfSecondUser.addLoggedUser(nonCurrentUserPort);
        userLoginStatus.put(nonCurrentUserPort, true);
        accounts.put(accountOfSecondUser.getUsername(), accountOfSecondUser);
        String setupCommand = "add-collaboration --name=Back-end project";

        MainFunctionality setupFunctionality = new MainFunctionality(setupCommand, accounts, userLoginStatus,
                nonCurrentUserPort, isTesting);
        setupFunctionality.init();

        String assignTaskCommand = "assign-task --user=user --collaboration=Back-end project"
                + " --task=Do the tests";

        MainFunctionality addTask = new MainFunctionality(assignTaskCommand,
                accounts, userLoginStatus, nonCurrentUserPort, isTesting);
        addTask.init();

        String assignTaskCommandSecondTime = "assign-task --user=user --collaboration=Back-end project"
                + " --task=Do the tests";

        MainFunctionality mainFunctionality = new MainFunctionality(assignTaskCommandSecondTime,
                accounts, userLoginStatus, nonCurrentUserPort, isTesting);

        String expectedResponse = "[ User that is given is not a member of the collaboration, consider adding them ]"
                + System.lineSeparator();
        String actualResponse = mainFunctionality.init();

        assertEquals("Incorrect add add collaboration output", expectedResponse, actualResponse);
        Assert.assertEquals("Should not add the user to collaboration Back-end project", 1,
                accountOfSecondUser.getCollaborations()
                        .get("Back-end project")
                        .listTasks()
                        .split(System.lineSeparator()).length);
    }

    @Test
    public void testListTasks() {
        final int nonCurrentUserPort = 2;

        Account accountOfSecondUser = new Account("project initiate", "pass123");

        accountOfSecondUser.addLoggedUser(nonCurrentUserPort);
        userLoginStatus.put(nonCurrentUserPort, true);
        accounts.put(accountOfSecondUser.getUsername(), accountOfSecondUser);
        String setupCommand = "add-collaboration --name=Back-end project";

        MainFunctionality setupFunctionality = new MainFunctionality(setupCommand, accounts, userLoginStatus,
                nonCurrentUserPort, isTesting);
        setupFunctionality.init();

        String addUserToCollaborationCommand = "add-user --collaboration=Back-end project --user=user";

        MainFunctionality addUserToCollaboration = new MainFunctionality(addUserToCollaborationCommand,
                accounts, userLoginStatus, nonCurrentUserPort, isTesting);
        addUserToCollaboration.init();

        String assignTaskCommand = "assign-task --user=user --collaboration=Back-end project"
                + " --task=Do the tests";

        MainFunctionality assignTask = new MainFunctionality(assignTaskCommand,
                accounts, userLoginStatus, nonCurrentUserPort, isTesting);
        assignTask.init();

        String listTasksCommand = "list-tasks --collaboration=Back-end project";

        MainFunctionality mainFunctionality = new MainFunctionality(listTasksCommand,
                accounts, userLoginStatus, nonCurrentUserPort, isTesting);

        String expectedResponse = "name: Do the tests"
                + System.lineSeparator()
                + " assigned to user"
                + System.lineSeparator();
        String actualResponse = mainFunctionality.init();

        assertEquals("Incorrect add add collaboration output", expectedResponse, actualResponse);
        Assert.assertEquals("Should not add the user to collaboration Back-end project", 2,
                accountOfSecondUser.getCollaborations()
                        .get("Back-end project")
                        .listTasks()
                        .split(System.lineSeparator()).length);
    }

    @Test
    public void testListTasksWithNoParameterValue() {
        final int nonCurrentUserPort = 2;

        String listTasksCommand = "list-tasks --collaboration=";

        MainFunctionality mainFunctionality = new MainFunctionality(listTasksCommand,
                accounts, userLoginStatus, nonCurrentUserPort, isTesting);

        String expectedResponse = "[ Command list-tasks cannot receive empty parameters, consider the following ]"
                + System.lineSeparator()
                + Commands.LISTTASKS
                + System.lineSeparator();
        String actualResponse = mainFunctionality.init();

        assertEquals("Incorrect add add collaboration output", expectedResponse, actualResponse);
    }

    @Test
    public void testListTasksWithNonexistingCollaboration() {
        final int nonCurrentUserPort = 2;

        String listTasksCommand = "list-tasks --collaboration=Back-end project";

        MainFunctionality mainFunctionality = new MainFunctionality(listTasksCommand,
                accounts, userLoginStatus, nonCurrentUserPort, isTesting);

        String expectedResponse = "[ Collaboration is nonexistent ]"
                + System.lineSeparator();
        String actualResponse = mainFunctionality.init();

        assertEquals("Incorrect add add collaboration output", expectedResponse, actualResponse);
    }

    @Test
    public void testListTasksWithNonexistingTasks() {
        final int nonCurrentUserPort = 2;

        Account accountOfSecondUser = new Account("project initiate", "pass123");

        accountOfSecondUser.addLoggedUser(nonCurrentUserPort);
        userLoginStatus.put(nonCurrentUserPort, true);
        accounts.put(accountOfSecondUser.getUsername(), accountOfSecondUser);
        String setupCommand = "add-collaboration --name=Back-end project";

        MainFunctionality setupFunctionality = new MainFunctionality(setupCommand, accounts, userLoginStatus,
                nonCurrentUserPort, isTesting);
        setupFunctionality.init();

        String addUserToCollaborationCommand = "add-user --collaboration=Back-end project --user=user";

        MainFunctionality addUserToCollaboration = new MainFunctionality(addUserToCollaborationCommand,
                accounts, userLoginStatus, nonCurrentUserPort, isTesting);
        addUserToCollaboration.init();

        String listTasksCommand = "list-tasks --collaboration=Back-end project";

        MainFunctionality mainFunctionality = new MainFunctionality(listTasksCommand,
                accounts, userLoginStatus, nonCurrentUserPort, isTesting);

        String expectedResponse = "[ No tasks present in the collaboration ]"
                + System.lineSeparator();
        String actualResponse = mainFunctionality.init();

        assertEquals("Incorrect add add collaboration output", expectedResponse, actualResponse);
        Assert.assertEquals("Should not add the user to collaboration Back-end project", 1,
                accountOfSecondUser.getCollaborations()
                        .get("Back-end project")
                        .listTasks()
                        .split(System.lineSeparator()).length);
    }

    @Test
    public void testListUsers() {
        final int nonCurrentUserPort = 2;

        Account accountOfSecondUser = new Account("project initiate", "pass123");

        accountOfSecondUser.addLoggedUser(nonCurrentUserPort);
        userLoginStatus.put(nonCurrentUserPort, true);
        accounts.put(accountOfSecondUser.getUsername(), accountOfSecondUser);
        String setupCommand = "add-collaboration --name=Back-end project";

        MainFunctionality setupFunctionality = new MainFunctionality(setupCommand, accounts, userLoginStatus,
                nonCurrentUserPort, isTesting);
        setupFunctionality.init();

        String addUserToCollaborationCommand = "add-user --collaboration=Back-end project --user=user";

        MainFunctionality addUserToCollaboration = new MainFunctionality(addUserToCollaborationCommand,
                accounts, userLoginStatus, nonCurrentUserPort, isTesting);
        addUserToCollaboration.init();

        String listUsersCommand = "list-users --collaboration=Back-end project";

        MainFunctionality mainFunctionality = new MainFunctionality(listUsersCommand,
                accounts, userLoginStatus, nonCurrentUserPort, isTesting);

        String expectedResponse = "project initiate"
                + System.lineSeparator()
                + "user"
                + System.lineSeparator();
        String actualResponse = mainFunctionality.init();

        assertEquals("Incorrect add add collaboration output", expectedResponse, actualResponse);
        Assert.assertEquals("Should not add the user to collaboration Back-end project", 2,
                accountOfSecondUser.getCollaborations()
                        .get("Back-end project")
                        .listUsers()
                        .split(System.lineSeparator()).length);
    }
}
