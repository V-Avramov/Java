package bg.sofia.uni.fmi.mjt.spellchecker;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.io.FileWriter;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NaiveSpellChekerTest {

    @Test
    public void compilationTest() throws IOException {
        Reader dictionaryReader = new StringReader(String.join(System.lineSeparator(), List.of("cat", "dog", "bird")));
        Reader soptwordsReader = new StringReader(String.join(System.lineSeparator(), List.of("a", "am", "me")));

        // 1. constructor
        SpellChecker spellChecker = new NaiveSpellChecker(dictionaryReader, soptwordsReader);

        // 2. findClosestWords()
        // List<String> closestWords = spellChecker.findClosestWords("hello", 2);

        // 3. metadata()
        Reader catTextReader = new StringReader("hello, i am a cat!");
        Metadata metadata = spellChecker.metadata(catTextReader);
        metadata.characters();
        metadata.words();
        metadata.mistakes();

        // 4. analyze()
        Reader dogTextReader = new StringReader("hello, i am a dog!");
        Writer output = new FileWriter("output.txt");
        spellChecker.analyze(dogTextReader, output, 2);

    }

    private NaiveSpellChecker spellChecker;
    private List<String> dictionaryList;

    @Before
    public void setUp() {

        dictionaryList = List.of("Aalst", "Aalto",
                "Aandahl", "Aani", "Aandahl", "Aani", "AAO", "AAP", "AAPSS", "aLLiance", "almost",
                "cat", "car", "correctly", "dog", "done", "do", "bird", "hope", "HoMeWoRk", "Yoshi", "Yoshihito",
                "iotized", "iotizing", "IOU", "you", " jam-up ", "jam", "jum", "leap", "(lol)", "obstetrist", "play-+",
                "shpicar", "!@##$yeowomen!!!)", "!!!yep&*^*", "yepeleic", "yepely", "Ieper", "really", "rally",
                "rallying");

        Reader dictionaryReader = new StringReader(String.join(System.lineSeparator(), dictionaryList));

        Reader stopwordsReader = new StringReader(String.join(System.lineSeparator(), List.of("a",
                "am", "me", "can't", "cannot", "could", "couldn't", "did", "didn't", "do",
                "does", "doesn't", "doing", "don't", "down", "I", "is", "not", "you're", "or")));

        spellChecker = new NaiveSpellChecker(dictionaryReader, stopwordsReader);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testMetadataWithNullText() {

        spellChecker.metadata(null);

    }

    @Test
    public void testMetadataWithEmptyText() {
        Reader emptyText = new StringReader("");

        Metadata expectedOutput = new Metadata(0, 0, 0);

        assertEquals("Empty text metadata test failed", expectedOutput, spellChecker.metadata(emptyText));
    }

    @Test
    public void testMetadataWithOneStopWordText() {
        Reader oneWordText = new StringReader("#");

        Metadata expectedOutput = new Metadata(1, 0, 0);

        assertEquals("Only One word stop word text metadata test failed",
                expectedOutput, spellChecker.metadata(oneWordText));
    }

    @Test
    public void testMetadataWithOneNonDictionaryWordText() {
        final String oneWordInput = "doggo";
        Reader oneWordText = new StringReader(oneWordInput);

        Metadata expectedOutput = new Metadata(5, 1, 1);

        assertEquals("Only One word stop word text metadata test failed",
                expectedOutput, spellChecker.metadata(oneWordText));
    }

    @Test
    public void testMetadataWithOneDictionaryWordText() {
        final String oneWordInput = "aap";
        Reader oneWordText = new StringReader(oneWordInput);

        Metadata expectedOutput = new Metadata(3, 1, 0);

        assertEquals("Only One word stop word text metadata test failed",
                expectedOutput, spellChecker.metadata(oneWordText));
    }

    @Test
    public void testMetadataWithWholeText() {
        final String inputText = "Alast jen's ~-?kat# Yoshi does not Pl!Ay tetris" + System.lineSeparator()
                + "yep o'Lol." + System.lineSeparator()
                + "you're #@NoT&&&&" + System.lineSeparator()
                + "YOU'RE" + System.lineSeparator()
                + "am I me doing a jem-op" + System.lineSeparator()
                + "i relly hpe this ho-me-wrk is done korrektly oR atleast almost";

        Reader input = new StringReader(inputText.toLowerCase());

        Metadata expectedOutput = new Metadata(139, 17, 13);

        assertEquals("Only One word stop word text metadata test failed",
                expectedOutput, spellChecker.metadata(input));
    }

    @Test (expected = IllegalArgumentException.class)
    public void testFindClosestWordsOfNull() {

        spellChecker.findClosestWords(null, 5);

    }

    @Test
    public void testFindThreeClosestWordsOfEmptyWord() {

        List<String> expectedOutput = dictionaryList.stream()
                .map(String::toLowerCase)
                .limit(3)
                .collect(Collectors.toList());

        assertEquals("Find three closest words of empty word problem",
                expectedOutput.size(), spellChecker.findClosestWords("", 3).size());

    }

    @Test
    public void testFindFiveClosestWordsOfNonalphanumbericsymbol() {

        List<String> expectedOutput = dictionaryList.stream()
                .map(String::toLowerCase)
                .limit(5)
                .collect(Collectors.toList());

        assertEquals("Find three closest words of empty word problem",
                expectedOutput.size(), spellChecker.findClosestWords("##$%!@!", 5).size());

    }

    @Test
    public void testFindFourClosestWordsOfWord() {

        List<String> expectedOutput = List.of("really", "jum", "play", "rally");

        assertEquals("Find three closest words of empty word problem",
                expectedOutput, spellChecker.findClosestWords("umbrella", 4));
    }

    @Test (expected = IllegalArgumentException.class)
    public void testAnalyzeWithNullReader() {

        Writer outputWriter = new StringWriter();

        spellChecker.analyze(null, outputWriter, 5);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testAnalyzeWithNullWriter() {

        Reader emptyText = new StringReader("");

        spellChecker.analyze(emptyText, null, 5);
    }

    @Test
    public void testAnalyzeWithEmptyText() {
        Reader emptyText = new StringReader("");
        Writer outputWriter = new StringWriter();

        spellChecker.analyze(emptyText, outputWriter, 10);
        String expectedOutput = "= = = Metadata = = =" + System.lineSeparator()
                + new Metadata(0, 0, 0) + System.lineSeparator()
                + "= = = Findings = = =" + System.lineSeparator();

        assertTrue("The output of empty input is incorrect", outputWriter.toString().equals(expectedOutput));
    }

    @Test
    public void testAnalyzeWithOneStopWordText() {
        Reader oneWordText = new StringReader("#");
        Writer outputWriter = new StringWriter();

        spellChecker.analyze(oneWordText, outputWriter, 10);
        String expectedOutput = "#" + System.lineSeparator()
                + "= = = Metadata = = =" + System.lineSeparator()
                + new Metadata(1, 0, 0) + System.lineSeparator()
                + "= = = Findings = = =" + System.lineSeparator();

        assertTrue("The output of one word input is incorrect", outputWriter.toString().equals(expectedOutput));
    }

    @Test
    public void testAnalyzeWithOneNonDictionaryWordText() {
        final String oneWordInput = "doggo";
        Reader oneWordText = new StringReader(oneWordInput);
        Writer outputWriter = new StringWriter();

        spellChecker.analyze(oneWordText, outputWriter, 3);

        String expectedOutput = oneWordInput + System.lineSeparator()
                + "= = = Metadata = = =" + System.lineSeparator()
                + new Metadata(5, 1, 1) + System.lineSeparator()
                + "= = = Findings = = =" + System.lineSeparator()
                + "Line #1, {" + oneWordInput + "} - Possible suggestions are {"
                + spellChecker.findClosestWords(oneWordInput, 3) + "}";

        assertTrue("The output of one word input is incorrect",
                outputWriter.toString().equals(expectedOutput));
    }

    @Test
    public void testAnalyzeWithOneDictionaryWordText() {
        final String oneWordInput = "aAp";
        Reader oneWordText = new StringReader(oneWordInput);
        Writer outputWriter = new StringWriter();

        spellChecker.analyze(oneWordText, outputWriter, 3);
        String expectedOutput = oneWordInput + System.lineSeparator()
                + "= = = Metadata = = =" + System.lineSeparator()
                + new Metadata(3, 1, 0) + System.lineSeparator()
                + "= = = Findings = = =" + System.lineSeparator();

        assertTrue("The output of one word input is incorrect",
                outputWriter.toString().equals(expectedOutput));
    }

    @Test
    public void testAnalyzeWithWholeText() {
        final String inputText = "Alast jen's ~-?kat# Yoshi does not Pl!Ay tetris" + System.lineSeparator()
                + "yep o'Lol." + System.lineSeparator()
                + "you're #@NoT&&&&" + System.lineSeparator()
                + "YOU'RE" + System.lineSeparator()
                + "am I me doing a jem-op" + System.lineSeparator()
                + "i relly hpe this ho-me-wrk is done korrektly5 oR atleast almost";

        final String expectedOutput = "Alast jen's ~-?kat# Yoshi does not Pl!Ay tetris" + System.lineSeparator()
                + "yep o'Lol." + System.lineSeparator()
                + "you're #@NoT&&&&" + System.lineSeparator()
                + "YOU'RE" + System.lineSeparator()
                + "am I me doing a jem-op" + System.lineSeparator()
                + "i relly hpe this ho-me-wrk is done korrektly5 oR atleast almost" + System.lineSeparator()
                + "= = = Metadata = = =" + System.lineSeparator()
                + new Metadata(140, 17, 13) + System.lineSeparator()
                + "= = = Findings = = =" + System.lineSeparator()
                + "Line #1, {Alast} - Possible suggestions are {"
                + spellChecker.findClosestWords("alast", 3) + "}" + System.lineSeparator()
                + "Line #1, {jen's} - Possible suggestions are {"
                + spellChecker.findClosestWords("jen's", 3) + "}" + System.lineSeparator()
                + "Line #1, {~-?kat#} - Possible suggestions are {"
                + spellChecker.findClosestWords("~-?kat#", 3) + "}" + System.lineSeparator()
                + "Line #1, {Pl!Ay} - Possible suggestions are {"
                + spellChecker.findClosestWords("pl!ay", 3) + "}" + System.lineSeparator()
                + "Line #1, {tetris} - Possible suggestions are {"
                + spellChecker.findClosestWords("tetris", 3) + "}" + System.lineSeparator()
                + "Line #2, {o'Lol.} - Possible suggestions are {"
                + spellChecker.findClosestWords("o'lol.", 3) + "}" + System.lineSeparator()
                + "Line #5, {jem-op} - Possible suggestions are {"
                + spellChecker.findClosestWords("jem-op", 3) + "}" + System.lineSeparator()
                + "Line #6, {relly} - Possible suggestions are {"
                + spellChecker.findClosestWords("relly", 3) + "}" + System.lineSeparator()
                + "Line #6, {hpe} - Possible suggestions are {"
                + spellChecker.findClosestWords("hpe", 3) + "}" + System.lineSeparator()
                + "Line #6, {this} - Possible suggestions are {"
                + spellChecker.findClosestWords("this", 3) + "}" + System.lineSeparator()
                + "Line #6, {ho-me-wrk} - Possible suggestions are {"
                + spellChecker.findClosestWords("ho-me-wrk", 3) + "}" + System.lineSeparator()
                + "Line #6, {korrektly5} - Possible suggestions are {"
                + spellChecker.findClosestWords("korrektly5", 3) + "}" + System.lineSeparator()
                + "Line #6, {atleast} - Possible suggestions are {"
                + spellChecker.findClosestWords("atleast", 3) + "}";

        Reader input = new StringReader(inputText);
        Writer outputWriter = new StringWriter();

        spellChecker.analyze(input, outputWriter, 3);

        System.out.println(outputWriter.toString());


        assertTrue("Whole text input should output correctly", outputWriter.toString().equals(expectedOutput));
    }
}
