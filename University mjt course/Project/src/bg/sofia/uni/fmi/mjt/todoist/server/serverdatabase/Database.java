package bg.sofia.uni.fmi.mjt.todoist.server.serverdatabase;

import bg.sofia.uni.fmi.mjt.todoist.account.Account;
import bg.sofia.uni.fmi.mjt.todoist.localdatetimeinstructions.LocalDateTimeDeserializer;
import bg.sofia.uni.fmi.mjt.todoist.localdatetimeinstructions.LocalDateTimeSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Database {

    public static ConcurrentHashMap<String, Account> loadAccounts()
            throws IOException {

        Map<String, Account> accounts;

        Path serverDatabase = Path.of("Resources" + File.separator + "accountsLog.txt");

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());

        Gson accountsGson = gsonBuilder.setPrettyPrinting().create();
        try (BufferedReader reader = Files.newBufferedReader(serverDatabase)) {
            String line;
            String wholeDocument = "";
            while ((line = reader.readLine()) != null) {
                wholeDocument += line;
            }

            Type type = new TypeToken<ConcurrentHashMap<String, Account>>() { }.getType();
            accounts = accountsGson.fromJson(wholeDocument, type);

        } catch (IOException e) {
            throw new RuntimeException("Cannot read from file" + serverDatabase);
        }

        return (ConcurrentHashMap<String, Account>) accounts;
    }

    public static void updateDatabase(Map<String, Account> accounts) {

        Path serverDatabase = Path.of("Resources" + File.separator + "accountsLog.txt");

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());

        Gson accountsGson = gsonBuilder.setPrettyPrinting().create();

        try (BufferedWriter writer = Files.newBufferedWriter(serverDatabase)) {
            String accountsToString = accountsGson.toJson(accounts);
            writer.write(accountsToString);

        } catch (IOException e) {
            throw new RuntimeException("Cannot write to file" + serverDatabase);
        }
    }
}
