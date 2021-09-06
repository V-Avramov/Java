package bg.sofia.uni.fmi.mjt.todoist.functionality.authentication;

import bg.sofia.uni.fmi.mjt.todoist.account.Account;
import bg.sofia.uni.fmi.mjt.todoist.server.functionality.TodoistAuthenticator;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;

public class TodoistAuthenticatorTest {
    //account.addLoggedUser(1);
    private static Map<String, Account> accounts;
    private static Map<Integer, Boolean> userLoginStatus;
    //userLoginStatus.put(1, true);
    //accounts.put("user", account);
    private static int currentPort = 1;
    private final boolean isTesting = true;

    @Before
    public void setUp() {

        accounts = new HashMap<>();

        userLoginStatus = new HashMap<>();
    }

    @BeforeClass
    public static void setup() {
        //tasks = new HashMap<>();
        //collaborations =  new HashMap<>();
    }

    @Test
    public void testRegister() {
        String wholeCommand = "register user pass";
        TodoistAuthenticator authenticate = new TodoistAuthenticator(wholeCommand, accounts,
                userLoginStatus, currentPort, isTesting);
        String expected = "[ Username user successfully registered ]"
                + System.lineSeparator();
        String actual = authenticate.init();

        assertEquals("Incorrect register output", expected, actual);
    }

    @Test
    public void testRegisterWithIncorrectInput() {
        String wholeCommand = "register user";
        TodoistAuthenticator authenticate = new TodoistAuthenticator(wholeCommand, accounts,
                userLoginStatus, currentPort, isTesting);
        String expected = "[ Unable to register ]"
                + System.lineSeparator();
        String actual = authenticate.init();

        assertEquals("Incorrect register output", expected, actual);
    }

    @Test
    public void testRegisterWithTakenUsername() {
        String setupCommand = "register user pass";
        TodoistAuthenticator setup = new TodoistAuthenticator(setupCommand, accounts,
                userLoginStatus, currentPort, isTesting);
        setup.init();
        String wholeCommand = "register user pass";
        TodoistAuthenticator authenticate = new TodoistAuthenticator(wholeCommand, accounts,
                userLoginStatus, currentPort, isTesting);
        String expected = "[ Username user is already taken, select another one ]"
                + System.lineSeparator();
        String actual = authenticate.init();

        assertEquals("Incorrect register output", expected, actual);
    }

    @Test
    public void testLogin() {
        String setupCommand = "register user pass";
        TodoistAuthenticator setup = new TodoistAuthenticator(setupCommand, accounts,
                userLoginStatus, currentPort, isTesting);
        setup.init();
        String wholeCommand = "login user pass";
        TodoistAuthenticator authenticate = new TodoistAuthenticator(wholeCommand, accounts,
                userLoginStatus, currentPort, isTesting);
        String expected = "[ User user successfully logged in ]"
                + System.lineSeparator();
        String actual = authenticate.init();

        assertEquals("Incorrect login output", expected, actual);
    }

    @Test
    public void testLoginNoParameters() {
        String wholeCommand = "login";
        TodoistAuthenticator authenticate = new TodoistAuthenticator(wholeCommand, accounts,
                userLoginStatus, currentPort, isTesting);
        String expected = "[ Unable to login ]"
                + System.lineSeparator();
        String actual = authenticate.init();

        assertEquals("Incorrect login output", expected, actual);
    }

    @Test
    public void testLoginWithNonexistentAccount() {
        String wholeCommand = "login user pass";
        TodoistAuthenticator authenticate = new TodoistAuthenticator(wholeCommand, accounts,
                userLoginStatus, currentPort, isTesting);
        String expected = "[ Invalid username/password combination ]"
                + System.lineSeparator();
        String actual = authenticate.init();

        assertEquals("Incorrect login with nonexistent username and password output", expected, actual);
    }

    @Test
    public void testLogout() {
        String setupCommand = "register user pass";
        TodoistAuthenticator setup = new TodoistAuthenticator(setupCommand, accounts,
                userLoginStatus, currentPort, isTesting);
        setup.init();
        String loginCommand = "login user pass";
        TodoistAuthenticator login = new TodoistAuthenticator(loginCommand, accounts,
                userLoginStatus, currentPort, isTesting);
        login.init();
        String wholeCommand = "logout";
        TodoistAuthenticator authenticate = new TodoistAuthenticator(wholeCommand, accounts,
                userLoginStatus, currentPort, isTesting);
        String expected = "[ Successfully logged out ]"
                + System.lineSeparator();
        String actual = authenticate.init();

        assertEquals("Incorrect login with nonexistent username and password output", expected, actual);
    }

    @Test
    public void testLogoutWhileNotLoggedIn() {
        String wholeCommand = "logout";
        userLoginStatus.put(1, false);
        TodoistAuthenticator authenticate = new TodoistAuthenticator(wholeCommand, accounts,
                userLoginStatus, currentPort, isTesting);
        String expected = "[ You are not logged in ]"
                + System.lineSeparator();
        String actual = authenticate.init();

        assertEquals("Incorrect login with nonexistent username and password output", expected, actual);
    }

    @Test
    public void testUnknownCommand() {
        String wholeCommand = "insert unknown command here";
        TodoistAuthenticator authenticate = new TodoistAuthenticator(wholeCommand, accounts,
                userLoginStatus, currentPort, isTesting);
        String expected = "[ Unknown command ]"
                + System.lineSeparator();
        String actual = authenticate.init();

        assertEquals("Incorrect login with nonexistent username and password output", expected, actual);
    }
}
