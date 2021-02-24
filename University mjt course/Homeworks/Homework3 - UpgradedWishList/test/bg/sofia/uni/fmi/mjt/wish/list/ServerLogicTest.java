package bg.sofia.uni.fmi.mjt.wish.list;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertEquals;

public class ServerLogicTest {

    private ServerLogic serverLogic;
    private final Map<String, List<String>> mapOfPresents = new ConcurrentHashMap<>();
    private final Map<String, Account> accounts = new ConcurrentHashMap<>();
    private final Map<Integer, Boolean> userLoginStatus = new ConcurrentHashMap<>();
    int currentUserPort;

    @Before
    public void setUp() {
        currentUserPort = 1;
        userLoginStatus.put(1, false);
    }

    @Test
    public void testRegisterWithEmptyInitials() {
        serverLogic = new ServerLogic("register", mapOfPresents, accounts, userLoginStatus);

        String expected = "[ Unable to register ]" + System.lineSeparator();

        assertEquals("Incorrect register with empty initials output",
                expected, serverLogic.serverMainLogic(currentUserPort));
    }

    @Test
    public void testRegisterWithOneParameter() {
        serverLogic = new ServerLogic("register a", mapOfPresents, accounts, userLoginStatus);

        String expected = "[ Unable to register ]" + System.lineSeparator();

        assertEquals("Incorrect register with one parameter output",
                expected, serverLogic.serverMainLogic(currentUserPort));
    }

    @Test
    public void testRegisterWithInvalidInput() {
        serverLogic = new ServerLogic("register Ko$io Abcd1234", mapOfPresents, accounts,
                userLoginStatus);

        String expected = "[ Username Ko$io is invalid, select a valid one ]" + System.lineSeparator();

        assertEquals("Incorrect register with invalid username test",
                expected, serverLogic.serverMainLogic(currentUserPort));
    }

    @Test
    public void testRegisterWithTakenUsername() {
        Account zdravko = new Account("Zdravko", "Abcd1234");

        accounts.put("Zdravko", zdravko);

        serverLogic = new ServerLogic("register Zdravko Abcd1234", mapOfPresents, accounts,
                userLoginStatus);

        String expected = "[ Username Zdravko is already taken, select another one ]" + System.lineSeparator();

        assertEquals("Incorrect register already taken username output",
                expected, serverLogic.serverMainLogic(currentUserPort));
    }

    @Test
    public void testRegisterWithValidInput() {
        serverLogic = new ServerLogic("register Zdravko Abcd1234", mapOfPresents, accounts,
                userLoginStatus);

        String expected = "[ Username Zdravko successfully registered ]" + System.lineSeparator();

        assertEquals("Incorrect register output",
                expected, serverLogic.serverMainLogic(currentUserPort));
    }

    @Test
    public void testLoginWithoutInput() {
        serverLogic = new ServerLogic("login", mapOfPresents, accounts,
                userLoginStatus);

        String expected = "[ Unable to login ]" + System.lineSeparator();

        assertEquals("Incorrect login without input output",
                expected, serverLogic.serverMainLogic(currentUserPort));
    }

    @Test
    public void testLoginWithInvalidUsernamePasswordCombination() {
        Account zdravko = new Account("Zdravko", "Abcd1234");

        accounts.put("Zdravko", zdravko);
        serverLogic = new ServerLogic("login Zdravko Abcd", mapOfPresents, accounts,
                userLoginStatus);

        String expected = "[ Invalid username/password combination ]" + System.lineSeparator();

        assertEquals("Incorrect login with invalid username/password test output",
                expected, serverLogic.serverMainLogic(currentUserPort));
    }

    @Test
    public void testLoginWithValidInput() {
        Account zdravko = new Account("Zdravko", "Abcd1234");

        accounts.put("Zdravko", zdravko);
        serverLogic = new ServerLogic("login Zdravko Abcd1234", mapOfPresents, accounts,
                userLoginStatus);

        String expected = "[ User Zdravko successfully logged in ]" + System.lineSeparator();

        assertEquals("Incorrect login with valid username/password test output",
                expected, serverLogic.serverMainLogic(currentUserPort));
    }

    @Test
    public void testLogoutWhileNotLoggedIn() {
        serverLogic = new ServerLogic("logout", mapOfPresents, accounts,
                userLoginStatus);

        String expected = "[ You are not logged in ]" + System.lineSeparator();

        assertEquals("Incorrect logout while not logged in",
                expected, serverLogic.serverMainLogic(currentUserPort));
    }

    @Test
    public void testPostWishWhileNotLoggedIn() {
        serverLogic = new ServerLogic("post-wish", mapOfPresents, accounts,
                userLoginStatus);

        String expected = "[ You are not logged in ]" + System.lineSeparator();

        assertEquals("Incorrect post-wish while not logged in",
                expected, serverLogic.serverMainLogic(currentUserPort));
    }

    @Test
    public void testGetWishWhileNotLoggedIn() {
        serverLogic = new ServerLogic("get-wish", mapOfPresents, accounts,
                userLoginStatus);

        String expected = "[ You are not logged in ]" + System.lineSeparator();

        assertEquals("Incorrect get-wish while not logged in",
                expected, serverLogic.serverMainLogic(currentUserPort));
    }

    @Test
    public void testDisconnectWhileLoggedOut() {
        serverLogic = new ServerLogic("disconnect", mapOfPresents, accounts,
                userLoginStatus);

        String expected = "[ Disconnected from server ]" + System.lineSeparator();

        assertEquals("Incorrect disconnect while logged out",
                expected, serverLogic.serverMainLogic(currentUserPort));
    }

    @Test
    public void testDisconnectWhileLoggedIn() {
        userLoginStatus.put(currentUserPort, true);
        serverLogic = new ServerLogic("disconnect", mapOfPresents, accounts,
                userLoginStatus);

        String expected = "[ Disconnected from server ]" + System.lineSeparator();

        assertEquals("Incorrect disconnect while logged in",
                expected, serverLogic.serverMainLogic(currentUserPort));
    }

    @Test
    public void testUnknownCommandWhileNotLoggedIn() {
        serverLogic = new ServerLogic("asdf", mapOfPresents, accounts,
                userLoginStatus);

        String expected = "[ Unknown command ]" + System.lineSeparator();

        assertEquals("Incorrect unknown command output while not logged in",
                expected, serverLogic.serverMainLogic(currentUserPort));
    }

    @Test
    public void testUnknownCommandWhileLoggedIn() {
        userLoginStatus.put(currentUserPort, true);
        Account tester = new Account("Tester", "qwerty");
        accounts.put("Tester", tester);

        serverLogic = new ServerLogic("asdfasdf a g d qer post-wish", mapOfPresents, accounts,
                userLoginStatus);

        String expected = "[ Unknown command ]" + System.lineSeparator();

        assertEquals("Unknown command test was incorrect while testing with a logged in user",
                expected, serverLogic.serverMainLogic(currentUserPort));
    }

    @Test
    public void testPostWishWithUnregisteredUserWhileLoggedIn() {
        userLoginStatus.put(currentUserPort, true);
        Account tester = new Account("Tester", "qwerty");
        accounts.put("Tester", tester);

        serverLogic = new ServerLogic("post-wish Zdravko kolelo", mapOfPresents, accounts,
                userLoginStatus);

        String expected = "[ Student with username Zdravko is not registered ]" + System.lineSeparator();

        assertEquals("Incorrect post-wish with an unregistered user test",
                expected, serverLogic.serverMainLogic(currentUserPort));
    }

    @Test
    public void testPostWishWhileLoggedIn() {
        userLoginStatus.put(currentUserPort, true);
        Account zdravko = new Account("Zdravko", "Abcd1234");
        Account tester = new Account("Tester", "qwerty");
        accounts.put("Zdravko", zdravko);
        accounts.put("Tester", tester);
        accounts.get("Tester").addLoggedUsers(currentUserPort);

        serverLogic = new ServerLogic("post-wish Zdravko kolelo", mapOfPresents, accounts,
                userLoginStatus);

        String expected = "[ Gift kolelo for student Zdravko submitted successfully ]" + System.lineSeparator();

        assertEquals("Incorrect test for the command post-wish while logged in",
                expected, serverLogic.serverMainLogic(currentUserPort));
    }

    @Test
    public void testPostWishWithExistingWishWhileLoggedIn() {
        userLoginStatus.put(currentUserPort, true);
        Account zdravko = new Account("Zdravko", "Abcd1234");
        Account tester = new Account("Tester", "qwerty");
        accounts.put("Zdravko", zdravko);
        accounts.put("Tester", tester);

        serverLogic = new ServerLogic("post-wish Zdravko kolelo", mapOfPresents, accounts,
                userLoginStatus);
        serverLogic.serverMainLogic(currentUserPort);

        serverLogic = new ServerLogic("post-wish Zdravko kolelo", mapOfPresents, accounts,
                userLoginStatus);

        String expected = "[ The same gift for student Zdravko was already submitted ]" + System.lineSeparator();

        assertEquals("Incorrect test for the command post-wish with existing wish while logged in",
                expected, serverLogic.serverMainLogic(currentUserPort));
    }

    @Test
    public void testPostTwoDifferentWishesToTheSameStudentWhileLoggedIn() {
        userLoginStatus.put(currentUserPort, true);
        Account zdravko = new Account("Zdravko", "Abcd1234");
        Account tester = new Account("Tester", "qwerty");
        accounts.put("Zdravko", zdravko);
        accounts.put("Tester", tester);

        serverLogic = new ServerLogic("post-wish Zdravko kolelo", mapOfPresents, accounts,
                userLoginStatus);
        serverLogic.serverMainLogic(currentUserPort);

        serverLogic = new ServerLogic("post-wish Zdravko topka", mapOfPresents, accounts,
                userLoginStatus);

        String expected = "[ Gift topka for student Zdravko submitted successfully ]" + System.lineSeparator();

        assertEquals("Incorrect test for the command post-wish with two different presents"
                        + "to the same student while logged in",
                expected, serverLogic.serverMainLogic(currentUserPort));
    }

    @Test
    public void testGetWishWithNoPresentsInTheWishListWhileLoggedIn() {
        userLoginStatus.put(currentUserPort, true);
        Account tester = new Account("Tester", "qwerty");
        accounts.put("Tester", tester);

        serverLogic = new ServerLogic("get-wish", mapOfPresents, accounts,
                userLoginStatus);

        String expected = "[ There are no students present in the wish list ]" + System.lineSeparator();

        assertEquals("Incorrect test for the command get-wish with no presents in the wish list"
                        + " while logged in",
                expected, serverLogic.serverMainLogic(currentUserPort));
    }

    @Test
    public void testGetWishWithUserWhenOnlyPresentsForThemAreInTheWishListWhileLoggedIn() {

        final Account tester = new Account("Tester", "qwerty");
        accounts.put("Tester", tester);
        userLoginStatus.put(currentUserPort, true);

        final Account zdravko = new Account("Zdravko", "Abcd1234");
        accounts.put("Zdravko", zdravko);
        final int zdravkoUserPort = 2;
        userLoginStatus.put(2, true);

        final Account doofenshmirtz = new Account("Doof", "Inator");
        accounts.put("Doof", doofenshmirtz);
        final int doofUserPort = 3;
        userLoginStatus.put(3, true);

        serverLogic = new ServerLogic("post-wish Tester no more tests", mapOfPresents, accounts,
                userLoginStatus);
        serverLogic.serverMainLogic(zdravkoUserPort);
        serverLogic = new ServerLogic("post-wish Tester passing the class", mapOfPresents, accounts,
                userLoginStatus);
        serverLogic.serverMainLogic(doofUserPort);
        serverLogic = new ServerLogic("get-wish", mapOfPresents, accounts,
                userLoginStatus);
        serverLogic.serverMainLogic(currentUserPort);

        String expected = "[ There are no students present in the wish list ]" + System.lineSeparator();

        assertEquals("Incorrect test for the command get-wish with no presents in the wish list"
                        + " while logged in",
                expected, serverLogic.serverMainLogic(currentUserPort));
    }

    @Test
    public void testGetWishWhileLoggedIn() {
        Account zdravko = new Account("Zdravko", "Abcd1234");
        Account tester = new Account("Tester", "qwerty");
        accounts.put("Zdravko", zdravko);
        userLoginStatus.put(currentUserPort, true);

        accounts.put("Tester", tester);
        userLoginStatus.put(2, true);

        serverLogic = new ServerLogic("post-wish Zdravko kolelo", mapOfPresents, accounts,
                userLoginStatus);
        serverLogic.serverMainLogic(currentUserPort);

        serverLogic = new ServerLogic("get-wish", mapOfPresents, accounts,
                userLoginStatus);

        String expected = "[ Zdravko: [kolelo] ]" + System.lineSeparator();

        assertEquals("Incorrect test for the command get-wish while logged in",
                expected, serverLogic.serverMainLogic(currentUserPort));
    }

    @Test
    public void testLogout() {
        Account tester = new Account("Tester", "qwerty");
        accounts.put("Tester", tester);
        userLoginStatus.put(currentUserPort, true);

        serverLogic = new ServerLogic("logout", mapOfPresents, accounts,
                userLoginStatus);

        String expected = "[ Successfully logged out ]" + System.lineSeparator();

        assertEquals("Incorrect test for the command post-wish while logged in",
                expected, serverLogic.serverMainLogic(currentUserPort));
    }
}
