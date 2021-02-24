package bg.sofia.uni.fmi.mjt.spellchecker;

import bg.sofia.uni.fmi.mjt.spellchecker.comparator.CompareByPercentage;
import bg.sofia.uni.fmi.mjt.spellchecker.pair.Pair;

import java.io.Reader;
import java.io.Writer;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.StringReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.Set;
import java.util.HashSet;

public class NaiveSpellChecker implements SpellChecker {

    private final Set<String> dictionaryWords;
    private final Set<String> stopwords;

    public NaiveSpellChecker(Reader dictionaryReader, Reader stopwordsReader) {

        try (BufferedReader bufDict = new BufferedReader(dictionaryReader)) {

            dictionaryWords = bufDict.lines()
                    .filter(word -> word.length() != 1)
                    .map(String::toLowerCase)   // to lower case so that it is easier to be made case insensitive
                    .map(word -> word = word.replaceAll("^[^a-zA-Z0-9]+", ""))
                    .map(word -> word = word.replaceAll("[^a-zA-Z0-9]+$", ""))
                    .collect(Collectors.toCollection(HashSet::new));

        } catch (IOException e) {
            throw new IllegalStateException("An error occurred while reading from dictionary file");
        }

        try (BufferedReader bufStop = new BufferedReader(stopwordsReader)) {

            stopwords = bufStop.lines()
                    .map(String::toLowerCase)
                    .map(word -> word.replaceAll("[ ]+", ""))
                    .collect(Collectors.toCollection(HashSet::new));

        } catch (IOException e) {
            throw new IllegalStateException("An error occurred while reading from stopwords file");
        }
    }

    @Override
    public void analyze(Reader textReader, Writer output, int suggestionsCount) {

        if (textReader == null) {
            throw new IllegalArgumentException("No argument given for text reader");
        }

        if (output == null) {
            throw new IllegalArgumentException("No argument given for output");
        }

        String findings = "= = = Findings = = =" + System.lineSeparator();

        try {
            BufferedReader reader = new BufferedReader(textReader);
            BufferedWriter writer = new BufferedWriter(output);

            String line;
            String wholeText = "";
            int lineNumber = 0;
            boolean isFirstLineInFindings = true;

            while ((line = reader.readLine()) != null) {

                lineNumber++;

                String[] words = line.split("\\s+");
                for (String word : words) {

                    if (stopwords.contains(word)) { // just in case
                        continue;
                    }

                    String strippedWord = word.replaceAll("^[^a-zA-Z0-9]+", "");
                    strippedWord = strippedWord.replaceAll("[^a-zA-Z0-9]+$", "");
                    strippedWord = strippedWord.toLowerCase();

                    if (!strippedWord.isEmpty()) {
                        if (stopwords.contains(strippedWord)) {
                            continue;
                        }

                        if (!dictionaryWords.contains(strippedWord)) {

                            if (!isFirstLineInFindings) {
                                findings += System.lineSeparator();
                            }

                            List<String> closestWords = findClosestWords(strippedWord, suggestionsCount);

                            isFirstLineInFindings = false;
                            findings += "Line #" + lineNumber + ", {" + word + "} - Possible suggestions are {"
                                    + closestWords + "}";
                        }
                    }
                }
                wholeText += line + System.lineSeparator();
                writer.write(line);
                writer.newLine();
                writer.flush();
            }

            String metadataFirstLine = "= = = Metadata = = =";
            Reader metadataReader = new StringReader(wholeText);
            Metadata meta = metadata(metadataReader);

            String metadataOutput = metadataFirstLine + System.lineSeparator() + meta + System.lineSeparator();
            writer.write(metadataOutput);
            writer.write(findings);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException("Something happened while analysing the input");
        }
    }

    @Override
    public Metadata metadata(Reader textReader) {

        if (textReader == null) {
            throw new IllegalArgumentException("Cannot find metadata of null text reader");
        }

        int charsNumber = 0;
        int wordsNumber = 0;
        int issuesNumber = 0;

        try {
            BufferedReader reader = new BufferedReader(textReader);

            String line;

            while ((line = reader.readLine()) != null) {

                String[] words = line.split("\\s+");
                for (String word : words) {
                    charsNumber += word.length();

                    if (stopwords.contains(word)) {
                        continue;
                    }

                    String strippedWord = word.replaceAll("^[^a-zA-Z0-9]+", "");
                    strippedWord = strippedWord.replaceAll("[^a-zA-Z0-9]+$", "");
                    strippedWord = strippedWord.toLowerCase();

                    if (!strippedWord.isEmpty()) {
                        if (stopwords.contains(strippedWord)) {
                            continue;
                        }
                        wordsNumber++;
                        if (!dictionaryWords.contains(strippedWord)) {
                            issuesNumber++;
                        }
                    }
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Something happened while analysing the input metadata");
        }

        return new Metadata(charsNumber, wordsNumber, issuesNumber);
    }

    @Override
    public List<String> findClosestWords(String word, int n) {

        if (word == null) {
            throw new IllegalArgumentException("Cannot find closest words to null");
        }


        Set<String> wordToGram = convertToTwoGram(word).keySet();

        // Only the dictionary words that contain all of
        // the grams of the parameter word are added to a list
        List<String> dictWordsThatContainGivenWordGrams = dictionaryWords.stream()
                .filter(currentWord -> wordToGram.stream().anyMatch(currentWord::contains))
                .collect(Collectors.toList());

        int containedLen = dictWordsThatContainGivenWordGrams.size();

        // a really important check to see if the size of the previously created
        // list is smaller than the number of suggestions we want
        if (containedLen < n) {
            // add some words just to fill out the empty space
            for (String dictWord : dictionaryWords) {
                if (containedLen >= n) {
                    break;
                }
                dictWordsThatContainGivenWordGrams.add(dictWord);
                containedLen++;
            }
        }

        List<Pair> percentGivenWord = new ArrayList<>();

        // created class of pairs
        for (String currentDictWord : dictWordsThatContainGivenWordGrams) {
            if (currentDictWord.length() == 1) {
                continue;
            }
            // get the current dictionary word's percentage similarity to the main word
            double percSimilarity = cosineSimilarity(word, currentDictWord);
            // place the current word and its percentage similarity in a list
            // of custom created class "Pair"
            percentGivenWord.add(new Pair(percSimilarity, currentDictWord));
        }

        CompareByPercentage comparator = new CompareByPercentage();
        Collections.sort(percentGivenWord, comparator);

        List<String> result = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            result.add(percentGivenWord.get(i).getGivenWord());
        }

        return result;
    }

    /*
    Following methods are private because they are not
    directly connected with the functionality of the class
     */

    private double cosineSimilarity(String mainWord, String givenWord) {
        Map<String, Integer> mainTwoGram = convertToTwoGram(mainWord);
        Map<String, Integer> givenTwoGram = convertToTwoGram(givenWord);

        List<Integer> mainOccur = new ArrayList<>();    // stores the number of times a 2grams are contained
        List<Integer> givenOccur = new ArrayList<>();   // stores the number of times a 2grams are contained
        double mainSumOfSquares = 0;
        for (String currentMainGram : mainTwoGram.keySet()) {
            mainSumOfSquares += (mainTwoGram.get(currentMainGram) * mainTwoGram.get(currentMainGram));
        }
        final double mainVectorLen = Math.sqrt(mainSumOfSquares);

        double givenSumOfSquares = 0;
        for (String currentMainGram : givenTwoGram.keySet()) {
            givenSumOfSquares += (givenTwoGram.get(currentMainGram) * givenTwoGram.get(currentMainGram));
        }
        double givenVectorLen = Math.sqrt(givenSumOfSquares);

        for (String currentMainGram : mainTwoGram.keySet()) {
            if (givenTwoGram.containsKey(currentMainGram)) {
                mainOccur.add(mainTwoGram.get(currentMainGram));
                givenOccur.add(givenTwoGram.get(currentMainGram));
            }
        }

        double sumOfEqualGrams = 0;

        for (int i = 0; i < mainOccur.size(); i++) {
            sumOfEqualGrams += (mainOccur.get(i) * givenOccur.get(i));
        }

        return sumOfEqualGrams / (mainVectorLen * givenVectorLen);
    }

    private Map<String, Integer> convertToTwoGram(String str) {
        Map<String, Integer> ngrams = new HashMap<>();
        for (int i = 0; i < str.length() - 1; i++) {
            String currentSubStr = str.substring(i, i + 2);

            // Add the substring or size n
            if (ngrams.containsKey(currentSubStr)) {
                ngrams.put(currentSubStr, ngrams.get(currentSubStr) + 1);
            } else {
                ngrams.put(currentSubStr, 1);
            }
        }
        // In each iteration, the window moves one step forward
        // Hence, each n-gram is added to the map

        return ngrams;
    }
}
