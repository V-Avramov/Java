package bg.sofia.uni.fmi.mjt.todoist.functionality.tasks;

import bg.sofia.uni.fmi.mjt.todoist.account.Account;
import bg.sofia.uni.fmi.mjt.todoist.server.functionality.MainFunctionality;
import bg.sofia.uni.fmi.mjt.todoist.server.functionality.type.Collaboration;
import bg.sofia.uni.fmi.mjt.todoist.server.functionality.type.Task;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

public class MainFunctionalityTest {

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

        MainFunctionality mainFunctionality = new MainFunctionality(wholeCommand, accounts, userLoginStatus,
                currentPort, isTesting);

        String expectedResponse = "[ Task with name asdf added to inbox successfully ]" + System.lineSeparator();
        String actualResponse = mainFunctionality.init();

        assertEquals("Incorrect add task output", expectedResponse, actualResponse);
        assertTrue("Should add task", account.getInboxTasks().size() == 1);
    }

    @Test
    public void testAddTaskWithoutParameters() {
        String wholeCommand = "add-task";

        MainFunctionality mainFunctionality = new MainFunctionality(wholeCommand, accounts, userLoginStatus,
                currentPort, isTesting);

        String expectedResponse = "[ Cannot add anonymous task ]" + System.lineSeparator();
        String actualResponse = mainFunctionality.init();

        assertEquals("Incorrect add task output", expectedResponse, actualResponse);
        assertTrue("Should not add task", account.getInboxTasks().size() == 0);
    }

    @Test
    public void testAddTaskWithWrongDateInput() {
        String wholeCommand = "add-task --name=wrong date input --date=111-22-3";

        MainFunctionality mainFunctionality = new MainFunctionality(wholeCommand, accounts, userLoginStatus,
                currentPort, isTesting);

        String expectedResponse = "[ Date parameters must be of type yyyy-MM-dd,"
                + " in case of February 1st 2021 write 2021-02-01 ]" + System.lineSeparator();
        String actualResponse = mainFunctionality.init();

        assertEquals("Incorrect add task output", expectedResponse, actualResponse);
        assertTrue("Should not add task", account.getInboxTasks().size() == 0);
    }


    @Test
    public void testAddTaskWithNoNameGiven() {
        String wholeCommand = "add-task --date=2021-02-15";

        MainFunctionality mainFunctionality = new MainFunctionality(wholeCommand, accounts, userLoginStatus,
                currentPort, isTesting);

        String expectedResponse = "[ Cannot add anonymous task ]" + System.lineSeparator();
        String actualResponse = mainFunctionality.init();

        assertEquals("Incorrect add task output", expectedResponse, actualResponse);
        assertTrue("Should not add task", account.getInboxTasks().size() == 0);
    }

    @Test
    public void testAddTaskToDatedTasks() {
        String wholeCommand = "add-task --name=asdf --date=2021-02-17";

        MainFunctionality mainFunctionality = new MainFunctionality(wholeCommand, accounts, userLoginStatus,
                currentPort, isTesting);

        String expectedResponse = "[ Task with name asdf and date 2021-02-17T00:00 added successfully ]"
                + System.lineSeparator();
        String actualResponse = mainFunctionality.init();

        assertEquals("Incorrect add task output", expectedResponse, actualResponse);
        assertTrue("Should not add task", account.getDatedTasks().size() == 1);
    }

    @Test
    public void testAddWithExistingNameTask() {
        String wholeCommand = "add-task --name=asdf";

        MainFunctionality mainFunctionality = new MainFunctionality(wholeCommand, accounts, userLoginStatus,
                currentPort, isTesting);
        mainFunctionality.init();

        MainFunctionality addSameTask = new MainFunctionality(wholeCommand, accounts, userLoginStatus,
                currentPort, isTesting);

        String expectedResponse = "[ Task with the same name already exists ]" + System.lineSeparator();
        String actualResponse = addSameTask.init();

        assertEquals("Incorrect add task output", expectedResponse, actualResponse);
        assertTrue("Should not add task", account.getInboxTasks().size() == 1);
    }

    @Test
    public void testAddTaskWithExistingNameToDatedTasks() {
        String wholeCommand = "add-task --name=asdf --date=2021-02-17";


        MainFunctionality mainFunctionality = new MainFunctionality(wholeCommand, accounts, userLoginStatus,
                currentPort, isTesting);
        mainFunctionality.init();

        MainFunctionality addSameTask = new MainFunctionality(wholeCommand, accounts, userLoginStatus,
                currentPort, isTesting);

        String expectedResponse = "[ Task with the same name already exists ]" + System.lineSeparator();
        String actualResponse = addSameTask.init();

        assertEquals("Incorrect add task output", expectedResponse, actualResponse);
        assertTrue("Should not add task", account.getDatedTasks().size() == 1);
    }

    @Test
    public void testGetTask() {
        String setUpCommand = "add-task --name=asdf";
        MainFunctionality setUpFunctionality = new MainFunctionality(setUpCommand, accounts, userLoginStatus,
                currentPort, isTesting);
        setUpFunctionality.init();
        String wholeCommand = "get-task --name=asdf";

        MainFunctionality getTaskFunctionality = new MainFunctionality(wholeCommand, accounts, userLoginStatus,
                currentPort, isTesting);

        String expectedResponse = "name: asdf" + System.lineSeparator();
        String actualResponse = getTaskFunctionality.init();

        assertEquals("Incorrect get task output", expectedResponse, actualResponse);
    }

    @Test
    public void testGetTaskWithDate() {
        LocalDateTime taskDate = LocalDateTime.of(2021, 02, 17, 0, 0);
        String setUpCommand = "add-task --name=asdf --date=" + taskDate.getYear() + "-0" + taskDate.getMonthValue()
                + "-" + taskDate.getDayOfMonth();
        MainFunctionality setUpFunctionality = new MainFunctionality(setUpCommand, accounts, userLoginStatus,
                currentPort, isTesting);
        setUpFunctionality.init();
        String wholeCommand = "get-task --name=asdf --date=" + taskDate.getYear() + "-0" + taskDate.getMonthValue()
                + "-" + taskDate.getDayOfMonth();

        MainFunctionality getTaskFunctionality = new MainFunctionality(wholeCommand, accounts, userLoginStatus,
                currentPort, isTesting);

        String expectedResponse = "name: asdf date: " + taskDate.toString() + System.lineSeparator();
        String actualResponse = getTaskFunctionality.init();

        assertEquals("Incorrect get task output", expectedResponse, actualResponse);
    }

    @Test
    public void testGetTaskWithDescription() {
        String setUpCommand = "add-task --name=asdf --description=I hope to pass this class";
        MainFunctionality setUpFunctionality = new MainFunctionality(setUpCommand, accounts, userLoginStatus,
                currentPort, isTesting);
        setUpFunctionality.init();
        String wholeCommand = "get-task --name=asdf";

        MainFunctionality getTaskFunctionality = new MainFunctionality(wholeCommand, accounts, userLoginStatus,
                currentPort, isTesting);

        String expectedResponse = "name: asdf" + System.lineSeparator()
                + "description: I hope to pass this class" + System.lineSeparator();
        String actualResponse = getTaskFunctionality.init();

        assertEquals("Incorrect get task output", expectedResponse, actualResponse);
    }

    @Test
    public void testGetTaskWithDateAndDescription() {
        LocalDateTime taskDate = LocalDateTime.of(2021, 02, 17, 0, 0);
        String setUpCommand = "add-task --name=asdf --date=" + taskDate.getYear() + "-0" + taskDate.getMonthValue()
                + "-" + taskDate.getDayOfMonth() + " --description=I hope to pass this class";
        MainFunctionality setUpFunctionality = new MainFunctionality(setUpCommand, accounts, userLoginStatus,
                currentPort, isTesting);
        setUpFunctionality.init();
        String wholeCommand = "get-task --name=asdf --date=" + taskDate.getYear() + "-0" + taskDate.getMonthValue()
                + "-" + taskDate.getDayOfMonth();

        MainFunctionality getTaskFunctionality = new MainFunctionality(wholeCommand, accounts, userLoginStatus,
                currentPort, isTesting);

        String expectedResponse = "name: asdf date: " + taskDate.toString() + System.lineSeparator()
                + "description: I hope to pass this class" + System.lineSeparator();
        String actualResponse = getTaskFunctionality.init();

        assertEquals("Incorrect get task output", expectedResponse, actualResponse);
    }

    @Test
    public void testGetTaskWithDescriptionAndDate() {
        LocalDateTime taskDate = LocalDateTime.of(2021, 02, 17, 0, 0);
        String setUpCommand = "add-task --name=asdf --description=I hope to pass this class"
                + " --date=" + taskDate.getYear() + "-0" + taskDate.getMonthValue()
                + "-" + taskDate.getDayOfMonth() + "";
        MainFunctionality setUpFunctionality = new MainFunctionality(setUpCommand, accounts, userLoginStatus,
                currentPort, isTesting);
        setUpFunctionality.init();
        String wholeCommand = "get-task --name=asdf --date=" + taskDate.getYear() + "-0" + taskDate.getMonthValue()
                + "-" + taskDate.getDayOfMonth() + "";

        MainFunctionality getTaskFunctionality = new MainFunctionality(wholeCommand, accounts, userLoginStatus,
                currentPort, isTesting);

        String expectedResponse = "name: asdf date: " + taskDate.toString() + System.lineSeparator()
                + "description: I hope to pass this class" + System.lineSeparator();
        String actualResponse = getTaskFunctionality.init();

        assertEquals("Incorrect get task output", expectedResponse, actualResponse);
    }

    @Test
    public void testAddTaskWithIncorrectDateAndDescription() {
        LocalDateTime taskDate = LocalDateTime.of(2021, 02, 17, 0, 0);
        String setUpCommand = "add-task --name=asdf --date=" + taskDate.getYear() + "-" + taskDate.getMonthValue()
                + "-" + taskDate.getDayOfMonth() + " --description=I hope to pass this class";
        MainFunctionality setUpFunctionality = new MainFunctionality(setUpCommand, accounts, userLoginStatus,
                currentPort, isTesting);

        String expectedResponse = "[ Date parameters must be of type yyyy-MM-dd,"
                + " in case of February 1st 2021 write 2021-02-01 ]"
                + System.lineSeparator();
        String actualResponse = setUpFunctionality.init();;

        assertEquals("Incorrect add task with incorrect date output", expectedResponse, actualResponse);
    }

    @Test
    public void testGetTaskWithNoParameters() {
        LocalDateTime taskDate = LocalDateTime.of(2021, 02, 17, 0, 0);
        String setUpCommand = "add-task --name=asdf --description=I hope to pass this class"
                + " --date=" + taskDate.getYear() + "-0" + taskDate.getMonthValue()
                + "-" + taskDate.getDayOfMonth() + "";
        MainFunctionality setUpFunctionality = new MainFunctionality(setUpCommand, accounts, userLoginStatus,
                currentPort, isTesting);
        setUpFunctionality.init();
        String wholeCommand = "get-task";

        MainFunctionality getTaskFunctionality = new MainFunctionality(wholeCommand, accounts, userLoginStatus,
                currentPort, isTesting);

        String expectedResponse = "[ Cannot get anonymous task ]" + System.lineSeparator();
        String actualResponse = getTaskFunctionality.init();

        assertEquals("Incorrect get task output", expectedResponse, actualResponse);
    }

    @Test
    public void testGetTaskWithNoNameInput() {
        LocalDateTime taskDate = LocalDateTime.of(2021, 02, 17, 0, 0);
        String setUpCommand = "add-task --name=asdf --description=I hope to pass this class"
                + " --date=" + taskDate.getYear() + "-0" + taskDate.getMonthValue()
                + "-" + taskDate.getDayOfMonth() + "";
        MainFunctionality setUpFunctionality = new MainFunctionality(setUpCommand, accounts, userLoginStatus,
                currentPort, isTesting);
        setUpFunctionality.init();
        String wholeCommand = "get-task --date=" + taskDate.getYear() + "-0" + taskDate.getMonthValue()
                + "-" + taskDate.getDayOfMonth() + "";

        MainFunctionality getTaskFunctionality = new MainFunctionality(wholeCommand, accounts, userLoginStatus,
                currentPort, isTesting);

        String expectedResponse = "[ Cannot get anonymous task ]" + System.lineSeparator();
        String actualResponse = getTaskFunctionality.init();

        assertEquals("Incorrect get task output", expectedResponse, actualResponse);
    }

    @Test
    public void testGetNonexistentTaskFromInbox() {
        LocalDateTime taskDate = LocalDateTime.of(2021, 02, 17, 0, 0);
        String setUpCommand = "add-task --name=asdf --description=I hope to pass this class"
                + " --date=" + taskDate.getYear() + "-0" + taskDate.getMonthValue()
                + "-" + taskDate.getDayOfMonth() + "";
        MainFunctionality setUpFunctionality = new MainFunctionality(setUpCommand, accounts, userLoginStatus,
                currentPort, isTesting);
        setUpFunctionality.init();
        String wholeCommand = "get-task --name=asdf";

        MainFunctionality getTaskFunctionality = new MainFunctionality(wholeCommand, accounts, userLoginStatus,
                currentPort, isTesting);

        String expectedResponse = "[ Task with name asdf does not exist in inbox ]" + System.lineSeparator();
        String actualResponse = getTaskFunctionality.init();

        assertEquals("Incorrect get task output", expectedResponse, actualResponse);
    }

    @Test
    public void testGetNonexistentTaskFromDatedTasks() {
        LocalDateTime taskDate = LocalDateTime.of(2021, 02, 17, 0, 0);
        String setUpCommand = "add-task --name=asdf --description=I hope to pass this class";
        MainFunctionality setUpFunctionality = new MainFunctionality(setUpCommand, accounts, userLoginStatus,
                currentPort, isTesting);
        setUpFunctionality.init();
        String wholeCommand = "get-task --name=asdf"
                + " --date=" + taskDate.getYear() + "-0" + taskDate.getMonthValue()
                + "-" + taskDate.getDayOfMonth() + "";

        MainFunctionality getTaskFunctionality = new MainFunctionality(wholeCommand, accounts, userLoginStatus,
                currentPort, isTesting);

        String expectedResponse = "[ Task with name asdf and date 2021-02-17T00:00 does not exist ]"
                + System.lineSeparator();
        String actualResponse = getTaskFunctionality.init();

        assertEquals("Incorrect get task output", expectedResponse, actualResponse);
    }

    @Test
    public void testGetNonexistentTaskFromDatedTasksWithExistingDate() {
        LocalDateTime taskDate = LocalDateTime.of(2021, 02, 17, 0, 0);
        String setUpCommand = "add-task --name=asdf --description=I hope to pass this class"
                + " --date=" + taskDate.getYear() + "-0" + taskDate.getMonthValue()
                + "-" + taskDate.getDayOfMonth() + "";
        MainFunctionality setUpFunctionality = new MainFunctionality(setUpCommand, accounts, userLoginStatus,
                currentPort, isTesting);
        setUpFunctionality.init();
        String wholeCommand = "get-task --name=find this task"
                + " --date=" + taskDate.getYear() + "-0" + taskDate.getMonthValue()
                + "-" + taskDate.getDayOfMonth() + "";

        MainFunctionality getTaskFunctionality = new MainFunctionality(wholeCommand, accounts, userLoginStatus,
                currentPort, isTesting);

        String expectedResponse = "[ Task with name find this task and date 2021-02-17T00:00 does not exist ]"
                + System.lineSeparator();
        String actualResponse = getTaskFunctionality.init();

        assertEquals("Incorrect get task output", expectedResponse, actualResponse);
    }

    @Test
    public void testUpdateInboxTask() {
        LocalDateTime taskDate = LocalDateTime.of(2021, 02, 17, 0, 0);
        String setUpCommand = "add-task --name=asdf --description=am I going to pass this class?";
        MainFunctionality setUpFunctionality = new MainFunctionality(setUpCommand, accounts, userLoginStatus,
                currentPort, isTesting);
        setUpFunctionality.init();

        String updateCommand = "update-task --name=asdf --due-date=" + taskDate.getYear() + "-0" + taskDate.getMonthValue()
                + "-" + taskDate.getDayOfMonth() + " --description=I sure hope so";
        MainFunctionality updateFunctionality = new MainFunctionality(updateCommand, accounts, userLoginStatus,
                currentPort, isTesting);

        String expectedResponse = "[ Task asdf updated successfully ]"
                + System.lineSeparator();
        String actualResponse = updateFunctionality.init();

        String wholeCommand = "get-task --name=asdf";
        MainFunctionality getTaskFunctionality = new MainFunctionality(wholeCommand, accounts, userLoginStatus,
                currentPort, isTesting);

        String expectedGetResponse = "name: asdf due date: " + taskDate.toString() + System.lineSeparator()
                + "description: I sure hope so" + System.lineSeparator();
        String actualGetResponse = getTaskFunctionality.init();

        assertEquals("Incorrect update task output", expectedResponse, actualResponse);
        assertEquals("The updated task has not actually been updated",
                expectedGetResponse, actualGetResponse);
    }

    @Test
    public void testUpdateDatedTask() {
        LocalDateTime taskDate = LocalDateTime.of(2021, 02, 17, 0, 0);
        String setUpCommand = "add-task --name=asdf --date=" + taskDate.getYear() + "-0" + taskDate.getMonthValue()
                + "-" + taskDate.getDayOfMonth();
        MainFunctionality setUpFunctionality = new MainFunctionality(setUpCommand, accounts, userLoginStatus,
                currentPort, isTesting);
        setUpFunctionality.init();

        String updateCommand = "update-task --name=asdf --date=" + taskDate.getYear() + "-0" + taskDate.getMonthValue()
                + "-" + taskDate.getDayOfMonth() + " --description=I hope to pass this class";
        MainFunctionality updateFunctionality = new MainFunctionality(updateCommand, accounts, userLoginStatus,
                currentPort, isTesting);

        String expectedResponse = "[ Task asdf with date 2021-02-17T00:00 updated successfully ]"
                + System.lineSeparator();
        String actualResponse = updateFunctionality.init();

        String wholeCommand = "get-task --name=asdf --date=" + taskDate.getYear() + "-0" + taskDate.getMonthValue()
                + "-" + taskDate.getDayOfMonth();
        MainFunctionality getTaskFunctionality = new MainFunctionality(wholeCommand, accounts, userLoginStatus,
                currentPort, isTesting);

        String expectedGetResponse = "name: asdf date: " + taskDate.toString() + System.lineSeparator()
                + "description: I hope to pass this class" + System.lineSeparator();
        String actualGetResponse = getTaskFunctionality.init();

        assertEquals("Incorrect update task output", expectedResponse, actualResponse);
        assertEquals("The updated task has not actually been updated",
                expectedGetResponse, actualGetResponse);
    }

    @Test
    public void testUpdateWithNoName() {
        String updateCommand = "update-task --description=I sure hope so";
        MainFunctionality updateFunctionality = new MainFunctionality(updateCommand, accounts, userLoginStatus,
                currentPort, isTesting);

        String expectedResponse = "[ Cannot update anonymous task ]"
                + System.lineSeparator();
        String actualResponse = updateFunctionality.init();

        assertEquals("Incorrect update task output", expectedResponse, actualResponse);
    }

    @Test
    public void testUpdateNotExistingInboxTask() {
        LocalDateTime taskDate = LocalDateTime.of(2021, 02, 17, 0, 0);
        String setUpCommand = "add-task --name=asdf --description=am I going to pass this class?";
        MainFunctionality setUpFunctionality = new MainFunctionality(setUpCommand, accounts, userLoginStatus,
                currentPort, isTesting);
        setUpFunctionality.init();

        String updateCommand = "update-task --name=a --description=I sure hope so";
        MainFunctionality updateFunctionality = new MainFunctionality(updateCommand, accounts, userLoginStatus,
                currentPort, isTesting);

        String expectedResponse = "[ Task named a does not exist ]"
                + System.lineSeparator();
        String actualResponse = updateFunctionality.init();

        assertEquals("Incorrect update task output", expectedResponse, actualResponse);
    }

    @Test
    public void testUpdateNotExistingDatedTask() {
        LocalDateTime taskDate = LocalDateTime.of(2021, 02, 17, 0, 0);

        String updateCommand = "update-task --name=a --date=" + taskDate.getYear() + "-0" + taskDate.getMonthValue()
                + "-" + taskDate.getDayOfMonth() + " --description=I sure hope so";
        MainFunctionality updateFunctionality = new MainFunctionality(updateCommand, accounts, userLoginStatus,
                currentPort, isTesting);

        String expectedResponse = "[ Task named a with date 2021-02-17T00:00 does not exist ]"
                + System.lineSeparator();
        String actualResponse = updateFunctionality.init();

        assertEquals("Incorrect update task output", expectedResponse, actualResponse);
    }

    @Test
    public void testUpdateNotExistingDatedTaskWithExistingDate() {
        LocalDateTime taskDate = LocalDateTime.of(2021, 02, 17, 0, 0);
        String setUpCommand = "add-task --name=asdf"
                + " --date=" + taskDate.getYear() + "-0" + taskDate.getMonthValue()
                + "-" + taskDate.getDayOfMonth() + " --description=am I going to pass this class?";
        MainFunctionality setUpFunctionality = new MainFunctionality(setUpCommand, accounts, userLoginStatus,
                currentPort, isTesting);
        setUpFunctionality.init();

        String updateCommand = "update-task --name=a --date=" + taskDate.getYear() + "-0" + taskDate.getMonthValue()
                + "-" + taskDate.getDayOfMonth() + " --description=I sure hope so";
        MainFunctionality updateFunctionality = new MainFunctionality(updateCommand, accounts, userLoginStatus,
                currentPort, isTesting);

        String expectedResponse = "[ Task named a with date 2021-02-17T00:00 does not exist ]"
                + System.lineSeparator();
        String actualResponse = updateFunctionality.init();

        assertEquals("Incorrect update task output", expectedResponse, actualResponse);
    }

    @Test
    public void testDeleteTask() {
        String addTaskCommand = "add-task --name=task set for deletion"
                + " --description=Let's see if this task will be deleted" + System.lineSeparator();
        MainFunctionality setUpFunctionality = new MainFunctionality(addTaskCommand, accounts, userLoginStatus,
                currentPort, isTesting);
        setUpFunctionality.init();

        String command = "delete-task --name=task set for deletion"
                + System.lineSeparator();
        MainFunctionality deleteTask = new MainFunctionality(command, accounts, userLoginStatus,
                currentPort, isTesting);

        String expectedResponse = "[ Task task set for deletion removed successfully ]" + System.lineSeparator();
        String actualResponse = deleteTask.init();

        assertEquals("Incorrect task deletion response", expectedResponse, actualResponse);
        int expectedTasksSize = 0;
        Assert.assertEquals("Task was not removed", expectedTasksSize, account.getInboxTasks().size());
    }

    @Test
    public void testDeleteDatedTask() {
        String addTaskCommand = "add-task --name=task set for deletion --date=2021-02-17"
                + " --description=Let's see if this task will be deleted";
        MainFunctionality setUpFunctionality = new MainFunctionality(addTaskCommand, accounts, userLoginStatus,
                currentPort, isTesting);
        setUpFunctionality.init();

        String command = "delete-task --name=task set for deletion --date=2021-02-17";
        MainFunctionality deleteTask = new MainFunctionality(command, accounts, userLoginStatus,
                currentPort, isTesting);

        String expectedResponse = "[ Task task set for deletion and date 2021-02-17T00:00 removed successfully ]"
                + System.lineSeparator();
        String actualResponse = deleteTask.init();

        assertEquals("Incorrect task deletion response", expectedResponse, actualResponse);
        int expectedTasksSize = 0;
        Assert.assertEquals("Task was not removed", expectedTasksSize, account.getInboxTasks().size());
    }

    @Test
    public void testDeleteTaskNoParameters() {
        String addTaskCommand = "add-task --name=task set for deletion --date=2021-02-17"
                + " --description=Let's see if this task will be deleted";
        MainFunctionality setUpFunctionality = new MainFunctionality(addTaskCommand, accounts, userLoginStatus,
                currentPort, isTesting);
        setUpFunctionality.init();

        String command = "delete-task";
        MainFunctionality deleteTask = new MainFunctionality(command, accounts, userLoginStatus,
                currentPort, isTesting);

        String expectedResponse = "[ Cannot delete anonymous task ]"
                + System.lineSeparator();
        String actualResponse = deleteTask.init();

        assertEquals("Incorrect task deletion response", expectedResponse, actualResponse);
        int expectedTasksSize = 0;
        Assert.assertEquals("Task was not removed", expectedTasksSize, account.getInboxTasks().size());
    }

    @Test
    public void testDeleteNonexistentTask() {
        String command = "delete-task --name=task set for deletion"
                + System.lineSeparator();
        MainFunctionality deleteTask = new MainFunctionality(command, accounts, userLoginStatus,
                currentPort, isTesting);

        String expectedResponse = "[ Requested task for deletion is nonexistent ]" + System.lineSeparator();
        String actualResponse = deleteTask.init();

        assertEquals("Incorrect task deletion response", expectedResponse, actualResponse);
    }

    @Test
    public void testDeleteNonexistentDatedTask() {
        String command = "delete-task --name=task set for deletion --date=2021-02-15";
        MainFunctionality deleteTask = new MainFunctionality(command, accounts, userLoginStatus,
                currentPort, isTesting);

        String expectedResponse = "[ Task named task set for deletion with date 2021-02-15T00:00 does not exist ]"
                + System.lineSeparator();
        String actualResponse = deleteTask.init();

        assertEquals("Incorrect task deletion response", expectedResponse, actualResponse);
    }

    @Test
    public void testDeleteNonexistentDatedTaskWithExistingDate() {
        String addTaskCommand = "add-task --name=task set for deletion --date=2021-02-15";
        MainFunctionality setUpFunctionality = new MainFunctionality(addTaskCommand, accounts, userLoginStatus,
                currentPort, isTesting);
        setUpFunctionality.init();

        String command = "delete-task --name=task1 --date=2021-02-15";
        MainFunctionality deleteTask = new MainFunctionality(command, accounts, userLoginStatus,
                currentPort, isTesting);

        String expectedResponse = "[ Task named task1 with date 2021-02-15T00:00 does not exist ]"
                + System.lineSeparator();
        String actualResponse = deleteTask.init();

        assertEquals("Incorrect task deletion response", expectedResponse, actualResponse);
    }

    @Test
    public void testListDashboard() {
        LocalDateTime today = LocalDateTime.now();
        String addTaskCommand = "add-task --name=task set for deletion --date="
                + today.getYear() + "-0" + today.getMonthValue() + "-" + today.getDayOfMonth();
        MainFunctionality setUpFunctionality = new MainFunctionality(addTaskCommand, accounts, userLoginStatus,
                currentPort, isTesting);
        setUpFunctionality.init();

        String addSecondTaskCommand = "add-task --name=Do the dishes --date="
                + today.getYear() + "-0" + today.getMonthValue() + "-" + today.getDayOfMonth();
        MainFunctionality setUpSecond = new MainFunctionality(addSecondTaskCommand, accounts, userLoginStatus,
                currentPort, isTesting);
        setUpSecond.init();

        String command = "list-dashboard";
        MainFunctionality listDashboardTask = new MainFunctionality(command, accounts, userLoginStatus,
                currentPort, isTesting);

        String expectedResponse = "name: task set for deletion date: 2021-02-16T00:00"
                + System.lineSeparator()
                + "name: Do the dishes date: 2021-02-16T00:00"
                + System.lineSeparator();
        String actualResponse = listDashboardTask.init();

        assertEquals("Incorrect list dashboard response", expectedResponse, actualResponse);
    }

    @Test
    public void testListDashboardWithNoTasksForToday() {
        String command = "list-dashboard";
        MainFunctionality listDashboardTask = new MainFunctionality(command, accounts, userLoginStatus,
                currentPort, isTesting);

        String expectedResponse = "[ Dashboard is empty for today ]"
                + System.lineSeparator();
        String actualResponse = listDashboardTask.init();

        assertEquals("Incorrect list dashboard response", expectedResponse, actualResponse);
    }

    @Test
    public void testListTasks() {
        LocalDateTime today = LocalDateTime.now();
        String addTaskCommand = "add-task --name=task set for deletion";
        MainFunctionality setUpFunctionality = new MainFunctionality(addTaskCommand, accounts, userLoginStatus,
                currentPort, isTesting);
        setUpFunctionality.init();

        String addSecondTaskCommand = "add-task --name=Do the dishes";
        MainFunctionality setUpSecond = new MainFunctionality(addSecondTaskCommand, accounts, userLoginStatus,
                currentPort, isTesting);
        setUpSecond.init();

        String command = "list-tasks";
        MainFunctionality listTasks = new MainFunctionality(command, accounts, userLoginStatus,
                currentPort, isTesting);

        String expectedResponse = "name: Do the dishes"
                + System.lineSeparator()
                +"name: task set for deletion"
                + System.lineSeparator();
        String actualResponse = listTasks.init();

        assertEquals("Incorrect list tasks response", expectedResponse, actualResponse);
    }

    @Test
    public void testListDatedTasks() {
        LocalDateTime today = LocalDateTime.now();
        String addTaskCommand = "add-task --name=task set for deletion --date=" + today.getYear()
                + "-0" + today.getMonthValue() + "-" + today.getDayOfMonth();
        MainFunctionality setUpFunctionality = new MainFunctionality(addTaskCommand, accounts, userLoginStatus,
                currentPort, isTesting);
        setUpFunctionality.init();

        String addSecondTaskCommand = "add-task --name=Do the dishes --date="
                + today.getYear() + "-0" + today.getMonthValue() + "-" + today.getDayOfMonth();
        MainFunctionality setUpSecond = new MainFunctionality(addSecondTaskCommand, accounts, userLoginStatus,
                currentPort, isTesting);
        setUpSecond.init();

        String command = "list-tasks --date=" + today.getYear()
                + "-0" + today.getMonthValue() + "-" + today.getDayOfMonth();
        MainFunctionality listTasks = new MainFunctionality(command, accounts, userLoginStatus,
                currentPort, isTesting);

        String expectedResponse = "name: task set for deletion date: 2021-02-16T00:00"
                + System.lineSeparator()
                + "name: Do the dishes date: 2021-02-16T00:00"
                + System.lineSeparator();
        String actualResponse = listTasks.init();

        assertEquals("Incorrect list tasks response", expectedResponse, actualResponse);
    }

    @Test
    public void testListCompletedTasks() {
        LocalDateTime today = LocalDateTime.now();
        String addTaskCommand = "add-task --name=task set for deletion";
        MainFunctionality setUpFunctionality = new MainFunctionality(addTaskCommand, accounts, userLoginStatus,
                currentPort, isTesting);
        setUpFunctionality.init();

        String addSecondTaskCommand = "add-task --name=Do the dishes";
        MainFunctionality setUpSecond = new MainFunctionality(addSecondTaskCommand, accounts, userLoginStatus,
                currentPort, isTesting);
        setUpSecond.init();

        String finishTaskCommand = "finish-task --name=Do the dishes";
        MainFunctionality setUpFinishTask = new MainFunctionality(finishTaskCommand, accounts, userLoginStatus,
                currentPort, isTesting);
        setUpFinishTask.init();

        String command = "list-tasks --completed=true";
        MainFunctionality listTasks = new MainFunctionality(command, accounts, userLoginStatus,
                currentPort, isTesting);

        String expectedResponse = "name: Do the dishes"
                + System.lineSeparator();
        String actualResponse = listTasks.init();

        assertEquals("Incorrect list tasks response", expectedResponse, actualResponse);
    }

    @Test
    public void testListCompletedTasksWithNoCompleted() {
        LocalDateTime today = LocalDateTime.now();
        String addTaskCommand = "add-task --name=task set for deletion";
        MainFunctionality setUpFunctionality = new MainFunctionality(addTaskCommand, accounts, userLoginStatus,
                currentPort, isTesting);
        setUpFunctionality.init();

        String addSecondTaskCommand = "add-task --name=Do the dishes";
        MainFunctionality setUpSecond = new MainFunctionality(addSecondTaskCommand, accounts, userLoginStatus,
                currentPort, isTesting);
        setUpSecond.init();

        String command = "list-tasks --completed=true";
        MainFunctionality listTasks = new MainFunctionality(command, accounts, userLoginStatus,
                currentPort, isTesting);

        String expectedResponse = "[ No tasks present ]"
                + System.lineSeparator();
        String actualResponse = listTasks.init();

        assertEquals("Incorrect list tasks response", expectedResponse, actualResponse);
    }

    @Test
    public void testFinishTaskWithNoNameGiven() {
        String finishTaskCommand = "finish-task";
        MainFunctionality setUpFinishTask = new MainFunctionality(finishTaskCommand, accounts, userLoginStatus,
                currentPort, isTesting);

        String expectedResponse = "[ Cannot complete an anonymous task ]"
                + System.lineSeparator();
        String actualResponse = setUpFinishTask.init();

        assertEquals("Incorrect list tasks response", expectedResponse, actualResponse);
    }

    @Test
    public void testFinishNonexistentTask() {
        String finishTaskCommand = "finish-task --name=Do the dishes";
        MainFunctionality setUpFinishTask = new MainFunctionality(finishTaskCommand, accounts, userLoginStatus,
                currentPort, isTesting);

        String expectedResponse = "[ Task with name Do the dishes does not exist ]"
                + System.lineSeparator();
        String actualResponse = setUpFinishTask.init();

        assertEquals("Incorrect list tasks response", expectedResponse, actualResponse);
    }

    @Test
    public void testFinishAlreadyFinishedTask() {
        LocalDateTime today = LocalDateTime.now();
        String addTaskCommand = "add-task --name=task set for deletion";
        MainFunctionality setUpFunctionality = new MainFunctionality(addTaskCommand, accounts, userLoginStatus,
                currentPort, isTesting);
        setUpFunctionality.init();

        String addSecondTaskCommand = "add-task --name=Do the dishes";
        MainFunctionality setUpSecond = new MainFunctionality(addSecondTaskCommand, accounts, userLoginStatus,
                currentPort, isTesting);
        setUpSecond.init();

        String finishTaskCommand = "finish-task --name=Do the dishes";
        MainFunctionality setUpFinishTask = new MainFunctionality(finishTaskCommand, accounts, userLoginStatus,
                currentPort, isTesting);
        setUpFinishTask.init();

        String expectedResponse = "[ Task with name Do the dishes has already been completed ]"
                + System.lineSeparator();
        String actualResponse = setUpFinishTask.init();

        assertEquals("Incorrect list tasks response", expectedResponse, actualResponse);
    }
}
