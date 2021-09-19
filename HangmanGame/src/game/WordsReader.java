package game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class WordsReader {
    private Map<Integer, String> allWords;

    public WordsReader(Path file) {
        allWords = new HashMap<>();

        try (BufferedReader reader = Files.newBufferedReader(file)) {
            generateWords(reader);
        } catch (IOException e) {
            System.out.println("Unable to open file " + file);
        }
    }

    private void generateWords(Reader wordsReader) throws IOException { // throw exception or else we would
                                                                        // need to try catch the while loop
        String currentWord;
        int index = 1;
        BufferedReader reader = new BufferedReader(wordsReader);
        while ((currentWord = reader.readLine()) != null) {
            if (currentWord.length() < 3) {
                continue;
            }
            allWords.put(index, currentWord);
            index++;
        }
    }

    public Map<Integer, String> getWords() {
        return allWords;
    }
}
