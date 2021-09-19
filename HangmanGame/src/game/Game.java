package game;

import java.io.File;
import java.nio.file.Path;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class Game {
    private String word;
    private int mistakes;
    private int wordLen;
    private Map<Integer, String> allWords;

    public Game() {
        loadWords();
        this.word = chooseWord();
        mistakes = 0;
        this.wordLen = this.word.length();

        startGame();
    }
    public void startGame() {
        String encryptedWord = encryptWord();

        while (encryptedWord.contains("_") && mistakes < 6) {
            System.out.println(encryptedWord);
            System.out.println(Hangman.hangman[mistakes]);

            System.out.println("Make a guess: ");
            Scanner sc = new Scanner(System.in);
            char input = sc.next().toLowerCase().charAt(0);
            if (word.contains(String.valueOf(input))) {
                encryptedWord = updateEncryptedWord(encryptedWord, input);
            } else {
                mistakes++;
            }
        }
        if (mistakes == 6) {
            System.out.println(Hangman.hangman[mistakes]);
            System.out.println("You lost");
            System.out.println("The word was " + word);
        } else {
            System.out.println(word);
            System.out.println("Congratulations, you've won");
        }
    }

    private String encryptWord() {
        String result = String.valueOf(word.charAt(0));

        for (int i = 1; i < wordLen - 1; i++) {
            if (word.charAt(i) == word.charAt(0)) {
                result += word.charAt(0);
            } else if (word.charAt(i) == word.charAt(wordLen - 1)) {
                result += word.charAt(wordLen - 1);
            } else {
                result += '_';
            }
        }
        result += word.charAt(wordLen - 1);
        return result;
    }

    private String updateEncryptedWord(String encryptedWord, char letter) {
        String updated = "";
        int updIndex = word.indexOf(letter);
        int prevIndex = -1;
        for (int i = 0; i < wordLen; i++) {
            if (i == updIndex) {
                updated += word.charAt(i);
                if (prevIndex == -1) {
                    prevIndex = updIndex;
                }
                updIndex = word.indexOf(letter, prevIndex + 1);
                continue;
            }
            updated += encryptedWord.charAt(i);
        }
        return updated;
    }

    private void loadWords() {
        Path file = Path.of("resources" + File.separator + "words.txt");
        WordsReader wr = new WordsReader(file);
        allWords = wr.getWords();
    }

    private String chooseWord() {
        Random rand = new Random();

        int chosen = rand.nextInt(allWords.size());

        return allWords.get(chosen);
    }
}
