package bg.sofia.uni.fmi.mjt.tagger;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;


public class TaggerTest {

    private Path file = Path.of("resources" + File.separator + "world-cities.csv");

    @Test
    public void testCityTagsShort() {

        try (BufferedReader reader = Files.newBufferedReader(file)) {
            Tagger tagger = new Tagger(reader);
            Path inFile = Path.of("inputForShortTest.txt");
            Path outFile = Path.of("output.txt");

            try (BufferedReader in = Files.newBufferedReader(inFile)) {
                Writer writer = Files.newBufferedWriter(outFile);
                tagger.tagCities(in, writer);
            } catch (IOException e) {
                throw new RuntimeException("Cannot read from file" + file);
            }

            Path outTestFile = Path.of("outputForShortTest.txt");

            try (BufferedReader testResult = Files.newBufferedReader(outTestFile)) {
                BufferedReader actualResult = Files.newBufferedReader(outFile);
                String file1Line;
                String file2Line;

                while ((file1Line = testResult.readLine()) != null && (file2Line = actualResult.readLine()) != null) {
                    assertTrue(file1Line.equals(file2Line));
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Cannot read from file" + file);
        }
    }

    @Test
    public void testCityTagsLong() {

        try (BufferedReader reader = Files.newBufferedReader(file)) {
            Tagger tagger = new Tagger(reader);
            Path inFile = Path.of("inputForLongTest.txt");
            Path outFile = Path.of("output.txt");

            try (BufferedReader in = Files.newBufferedReader(inFile)) {
                Writer writer = Files.newBufferedWriter(outFile);
                tagger.tagCities(in, writer);
            } catch (IOException e) {
                throw new RuntimeException("Cannot read from file" + file);
            }

            Path outTestFile = Path.of("outputForLongTest.txt");

            try (BufferedReader testResult = Files.newBufferedReader(outTestFile)) {
                BufferedReader actualResult = Files.newBufferedReader(outFile);
                String file1Line;
                String file2Line;

                while ((file1Line = testResult.readLine()) != null && (file2Line = actualResult.readLine()) != null) {
                    assertTrue(file1Line.equals(file2Line));
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Cannot read from file" + file);
        }
    }

    @Test
    public void testGetAllTaggedCitiesWithZeroInput() {
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            Tagger tagger = new Tagger(reader);

            assertEquals(0, tagger.getAllTaggedCities().size());
        } catch (IOException e) {
            throw new RuntimeException("Cannot read from file" + file);
        }
    }

    @Test
    public void testGetAllTaggedCities() {
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            Tagger tagger = new Tagger(reader);
            Path inFile = Path.of("inputForShortTest.txt");
            Path outFile = Path.of("output.txt");

            try (BufferedReader in = Files.newBufferedReader(inFile)) {
                Writer writer = Files.newBufferedWriter(outFile);
                tagger.tagCities(in, writer);
            } catch (IOException e) {
                throw new RuntimeException("Cannot read from file" + file);
            }

            List<String> expected = new ArrayList<>();
            expected.add("Sofia");
            expected.add("Plovdiv");
            expected.add("Saitama");

            List<String> actual;
            actual = (List<String>) tagger.getAllTaggedCities();
            long sizeOfTagged = expected.size();

            for (int i = 0; i < sizeOfTagged; i++) {
                assertTrue(expected.get(i).equals(actual.get(i)));
            }

        } catch (IOException e) {
            throw new RuntimeException("Cannot read from file" + file);
        }
    }

    @Test
    public void testGetNMostTaggedCitiesWithZeroTaggedCities() {
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            Tagger tagger = new Tagger(reader);

            List<String> expected;
            expected = (List<String>) tagger.getAllTaggedCities();
            List<String> actual;
            actual = (List<String>) tagger.getNMostTaggedCities(5);
            long sizeOfTagged = expected.size();

            for (int i = 0; i < sizeOfTagged; i++) {
                assertFalse(expected.get(i).equals(actual.get(i)));
            }

        } catch (IOException e) {
            throw new RuntimeException("Cannot read from file" + file);
        }
    }

    @Test
    public void testGetNMostTaggedCities() {
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            Tagger tagger = new Tagger(reader);
            Path inFile = Path.of("inputForShortTest.txt");
            Path outFile = Path.of("output.txt");

            try (BufferedReader in = Files.newBufferedReader(inFile)) {
                Writer writer = Files.newBufferedWriter(outFile);
                tagger.tagCities(in, writer);
            } catch (IOException e) {
                throw new RuntimeException("Cannot read from file" + file);
            }

            List<String> expected;
            expected = (List<String>) tagger.getAllTaggedCities();
            List<String> actual;
            actual = (List<String>) tagger.getNMostTaggedCities(5);
            long sizeOfTagged = expected.size();

            for (int i = 0; i < sizeOfTagged; i++) {
                assertTrue(expected.get(i).equals(actual.get(i)));
            }

        } catch (IOException e) {
            throw new RuntimeException("Cannot read from file" + file);
        }
    }

    @Test
    public void testAllTagsCountWithZeroTaggedCities() {
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            Tagger tagger = new Tagger(reader);

            assertEquals("Wrong total number of tags", 0, tagger.getAllTagsCount());

        } catch (IOException e) {
            throw new RuntimeException("Cannot read from file" + file);
        }
    }

    @Test
    public void testAllTagsCount() {
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            Tagger tagger = new Tagger(reader);
            Path inFile = Path.of("inputForShortTest.txt");
            Path outFile = Path.of("output.txt");

            try (BufferedReader in = Files.newBufferedReader(inFile)) {
                Writer writer = Files.newBufferedWriter(outFile);
                tagger.tagCities(in, writer);
            } catch (IOException e) {
                throw new RuntimeException("Cannot read from file" + file);
            }
            try (BufferedReader in = Files.newBufferedReader(inFile)) {
                Writer writer = Files.newBufferedWriter(outFile);
                tagger.tagCities(in, writer);
            } catch (IOException e) {
                throw new RuntimeException("Cannot read from file" + file);
            }

            assertEquals("Wrong total number of tags", 3, tagger.getAllTagsCount());

        } catch (IOException e) {
            throw new RuntimeException("Cannot read from file" + file);
        }
    }
}
