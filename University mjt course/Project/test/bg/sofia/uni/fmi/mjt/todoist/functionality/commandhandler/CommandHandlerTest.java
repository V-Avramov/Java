package bg.sofia.uni.fmi.mjt.todoist.functionality.commandhandler;

import bg.sofia.uni.fmi.mjt.todoist.account.Account;
import bg.sofia.uni.fmi.mjt.todoist.commands.Commands;
import bg.sofia.uni.fmi.mjt.todoist.server.functionality.CommandHandler;
import bg.sofia.uni.fmi.mjt.todoist.server.functionality.MainFunctionality;
import bg.sofia.uni.fmi.mjt.todoist.server.functionality.TodoistAuthenticator;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

public class CommandHandlerTest {

    private static Account account;
    //account.addLoggedUser(1);
    private static Map<String, Account> accounts;
    private static Map<Integer, Boolean> userLoginStatus;
    //userLoginStatus.put(1, true);
    //accounts.put("user", account);
    private static int currentPort = 1;
    private final boolean isTesting = true;

    @Before
    public void setUp() {

        account = new Account("user", "pass");
        accounts = new HashMap<>();

        userLoginStatus = new HashMap<>();
        account.addLoggedUser(1);
        userLoginStatus.put(1, true);
        accounts.put("user", account);
    }

    @BeforeClass
    public static void setup() {
        //tasks = new HashMap<>();
        //collaborations =  new HashMap<>();
    }

    @Test
    public void testAddTask() {
        String wholeCommand = "add-task --name=asdf";

        CommandHandler mainFunctionality = new CommandHandler(wholeCommand, accounts, userLoginStatus,
                currentPort, isTesting);

        String expectedResponse = "[ Task with name asdf added to inbox successfully ]" + System.lineSeparator();
        String actualResponse = mainFunctionality.execute();

        assertEquals("Incorrect add task output", expectedResponse, actualResponse);
        assertTrue("Should add task", account.getInboxTasks().size() == 1);
    }

    @Test
    public void testLogin() {
        String setupCommand = "register user pass";
        userLoginStatus.put(1, false);
        CommandHandler authenticate = new CommandHandler(setupCommand, accounts,
                userLoginStatus, currentPort, isTesting);
        authenticate.execute();

        String wholeCommand = "login user pass";

        CommandHandler mainFunctionality = new CommandHandler(wholeCommand, accounts, userLoginStatus,
                currentPort, isTesting);

        String expectedResponse = "[ User user successfully logged in ]" + System.lineSeparator();
        String actualResponse = mainFunctionality.execute();

        assertEquals("Incorrect login output", expectedResponse, actualResponse);
    }

    @Test
    public void testIncorrectInput() {
        String wholeCommand = null;

        CommandHandler mainFunctionality = new CommandHandler(wholeCommand, accounts, userLoginStatus,
                currentPort, isTesting);

        String expectedResponse = "[ Incorrect input ]" + System.lineSeparator();
        String actualResponse = mainFunctionality.execute();

        assertEquals("Incorrect test with incorrect parameters output", expectedResponse, actualResponse);
    }

    @Test
    public void testDisconnect() {
        String wholeCommand = "disconnect";

        CommandHandler mainFunctionality = new CommandHandler(wholeCommand, accounts, userLoginStatus,
                currentPort, isTesting);

        String expectedResponse = "[ Disconnected from server ]" + System.lineSeparator();
        String actualResponse = mainFunctionality.execute();

        assertEquals("Incorrect login output", expectedResponse, actualResponse);
    }

    @Test
    public void testHelp() {
        String wholeCommand = "help";

        CommandHandler mainFunctionality = new CommandHandler(wholeCommand, accounts, userLoginStatus,
                currentPort, isTesting);

        String expectedResponse = "help - lists out commands" + System.lineSeparator()
                + Commands.ADDTASK + System.lineSeparator()
                + Commands.UPDATETASK + System.lineSeparator()
                + Commands.DELETETASK + System.lineSeparator()
                + Commands.GETTASK + System.lineSeparator()
                + Commands.LISTTASKS + System.lineSeparator()
                + Commands.LISTDASHBOARD + System.lineSeparator()
                + Commands.FINISHTASK + System.lineSeparator()
                + Commands.ADDCOLLABORATION + System.lineSeparator()
                + Commands.DELETECOLLABORATION + System.lineSeparator()
                + Commands.LISTCOLLABORATIONS + System.lineSeparator()
                + Commands.ADDUSER + System.lineSeparator()
                + Commands.ASSIGNTASK + System.lineSeparator()
                + Commands.LISTUSERS + System.lineSeparator()
                + "----------------------------------"
                + "<something> is just a placeholder, ignore the '<' and '>'"
                + System.lineSeparator();
        String actualResponse = mainFunctionality.execute();

        assertEquals("Incorrect login output", expectedResponse, actualResponse);
    }
}
