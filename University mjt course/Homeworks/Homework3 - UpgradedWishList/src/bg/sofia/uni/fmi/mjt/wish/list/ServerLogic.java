package bg.sofia.uni.fmi.mjt.wish.list;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Random;

public class ServerLogic {

    private final String letter;
    private final Map<String, List<String>> mapOfPresents;
    private final Map<String, Account> accounts;
    /*private final int currentUserPort;*/
    private final Map<Integer, Boolean> userLoginStaus;

    public ServerLogic(String letter, Map<String, List<String>> mapOfPresents, Map<String, Account> accounts,
                       Map<Integer, Boolean> userLoginStatus) {
        this.letter = letter;

        this.mapOfPresents = mapOfPresents;
        this.accounts = accounts;
        /*this.currentUserPort = currentUserPort;*/
        this.userLoginStaus = userLoginStatus;
    }

    public String serverMainLogic(int currentUserPort) {

        String[] command = letter.split(" ", 3);
        // The structure of this Array of Strings is
        // 0:command,
        // 1:first argument of command,
        // 2:second argument of command

        if (command[0].equals("disconnect")) {
            userLoginStaus.remove(currentUserPort);
            return "[ Disconnected from server ]" + System.lineSeparator();
        } else if (!userLoginStaus.get(currentUserPort)) {

            if (command[0].equals("register")) {
                String message;
                if (command.length < 3) {
                    message = "[ Unable to register ]"
                            + System.lineSeparator();
                } else if (command[1].matches("^.*[^a-zA-Z0-9._-].*$")) {
                    message = "[ Username " + command[1] + " is invalid, select a valid one ]"
                            + System.lineSeparator();
                } else if (accounts.containsKey(command[1])) {
                    message = "[ Username " + command[1]
                            + " is already taken, select another one ]"
                            + System.lineSeparator();
                } else {
                    Account addedAccount = new Account(command[1], command[2]);
                    accounts.put(command[1], addedAccount);
                    message = "[ Username " + command[1] + " successfully registered ]"
                            + System.lineSeparator();
                }
                return message;
            }

            if (command[0].equals("login")) {
                String message;
                if (command.length < 3) {
                    message = "[ Unable to login ]"
                            + System.lineSeparator();
                } else if (accounts.containsValue(new Account(command[1], command[2]))) {
                    message = "[ User " + command[1] + " successfully logged in ]"
                            + System.lineSeparator();
                    accounts.get(command[1]).addLoggedUsers(currentUserPort);
                    userLoginStaus.put(currentUserPort, true);
                } else {
                    message = "[ Invalid username/password combination ]"
                            + System.lineSeparator();
                }
                return message;
            }

            if (command[0].equals("logout") || command[0].equals("post-wish")
                    || command[0].equals("get-wish")) {

                return "[ You are not logged in ]"
                        + System.lineSeparator();
            }
        } else {

            String message;
            if (command[0].equals("post-wish")) {

                message = postWish(command) + System.lineSeparator();

            } else if (command[0].equals("get-wish")) {
                String currentUser = "";
                for (String account : accounts.keySet()) {
                    if (accounts.get(account).getLoggedUsers().contains(currentUserPort)) {
                        currentUser = account;
                        break;
                    }
                }
                message = getWish(currentUser) + System.lineSeparator();

            } else if (command[0].equals("logout")) {
                message = "[ Successfully logged out ]" + System.lineSeparator();
                for (String account : accounts.keySet()) {
                    if (accounts.get(account).getLoggedUsers().contains(currentUserPort)) {
                        accounts.get(account).removeLoggedUsers(currentUserPort);
                        userLoginStaus.put(currentUserPort, false);
                    }
                }
            } else {
                message = "[ Unknown command ]" + System.lineSeparator();
            }
            return message;
        }

        return "[ Unknown command ]" + System.lineSeparator();
    }

    private String getWish(String currentUser) {
        List<String> students = new ArrayList<>(mapOfPresents.keySet());
        int studentsSize = students.size();
        if (studentsSize == 1 && currentUser.equals(students.iterator().next())) {
            return "[ There are no students present in the wish list ]";
        }

        if (studentsSize <= 0) {
            return "[ There are no students present in the wish list ]";
        }

        int randomStudent = new Random().nextInt(studentsSize);
        while (currentUser.equals(students.get(randomStudent))) {
            randomStudent = new Random().nextInt(studentsSize);
        }
        String resultMessage = "[ " + students.get(randomStudent) + ": "
                + mapOfPresents.get(students.get(randomStudent)).toString() + " ]";
        mapOfPresents.remove(students.get(randomStudent));
        return resultMessage;
    }

    private String postWish(String[] letterInDetails) {

        if (!accounts.containsKey(letterInDetails[1])) {
            return "[ Student with username " + letterInDetails[1] + " is not registered ]";
        } else if (!mapOfPresents.containsKey(letterInDetails[1])) {
            mapOfPresents.put(letterInDetails[1], List.of(letterInDetails[2]));

            return "[ Gift " + letterInDetails[2]
                    + " for student " + letterInDetails[1] + " submitted successfully ]";
        } else {
            if (mapOfPresents.get(letterInDetails[1]).contains(letterInDetails[2])) {
                return "[ The same gift for student "
                        + letterInDetails[1] + " was already submitted ]";
            } else {
                List<String> currentListOfWishes = new ArrayList<>();
                currentListOfWishes.addAll(mapOfPresents.get(letterInDetails[1]));
                currentListOfWishes.add(letterInDetails[2]);
                mapOfPresents.put(letterInDetails[1], currentListOfWishes);

                return "[ Gift " + letterInDetails[2]
                        + " for student " + letterInDetails[1] + " submitted successfully ]";
            }
        }
    }
}
