package bg.sofia.uni.fmi.mjt.spotify;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class SpotifyExplorerTest {
    private Path resourceFile = Path.of("resources" + File.separator + "spotify-data.csv");

    private String shortListOfTracks = "dummyline \n"
            + "3ftBPsC5vPBKxYSee08FDH,['Frank Parker'],"
            + "Danny Boy,1921,3,210000,100.109,-9.316,0.165,0.967,0.275,0.309,0.381,0.0354,0\n"

            + "60a0Rd6pjrkxjPbaKzXjfq,['Linkin Park'],"
            + "In the End,2000,84,216880,105.143,-5.87,0.4,0.00958,0.556,0.864,0.209,0.0584,0\n"

            + "3K4HG9evC7dg3N0R9cYqk4,['Linkin Park'],"
            + "One Step Closer,2000,76,157333,95.136,-4.419,0.538,0.0014,0.492,0.969,0.0787,0.0491,0\n"

            + "3xXBsjrbG1xQIm1xv1cKOt,['Linkin Park'],"
            + "One More Light,2017,73,255067,83.966,-11.063,0.17,0.904,0.593,0.2,0.0897,0.0311,0\n"

            + "18oWEPapjNt32E6sCM6VLb,['Yeah Yeah Yeahs'],"
            + "Heads Will Roll,2009,58,221000,132.009,-4.46,0.791,0.000187,0.562,0.903,0.298,0.0296,0\n"

            + "5SiZJoLXp3WOl3J4C8IK0d,['Eminem'],"
            + "Darkness,2020,70,337147,75.055,-7.161,0.195,0.00998,0.671,0.623,0.643,0.308,1\n";

    @Test
    public void testGetAllSpotifyTracks() {
        var actReader = new StringReader(shortListOfTracks);

        SpotifyExplorer actual = new SpotifyExplorer(actReader);

        var expStringreader = new StringReader(shortListOfTracks);
        try (BufferedReader expReader = new BufferedReader(expStringreader)) {
            expReader.readLine();   //removes the first line of the given file
            List<SpotifyTrack> expected;
            expected = expReader.lines()
                    .map(SpotifyTrack::of).collect(Collectors.toList());

            assertTrue(Collections.unmodifiableCollection(expected).containsAll(actual.getAllSpotifyTracks())
            && Collections.unmodifiableCollection(expected).size() == actual.getAllSpotifyTracks().size());

        } catch (IOException e) {
            throw new IllegalStateException("Cannot read from given file", e);
        }
    }

    @Test
    public void testGetExplicitSpotifyTracks() {
        var actReader = new StringReader(shortListOfTracks);

        SpotifyExplorer actual = new SpotifyExplorer(actReader);

        var expStringreader = new StringReader(shortListOfTracks);

        try (BufferedReader expReader = new BufferedReader(expStringreader)) {

            expReader.readLine();   //removes the first line of the given file

            List<SpotifyTrack> expected;
            expected = expReader.lines()
                    .map(SpotifyTrack::of)
                    .filter(SpotifyTrack::explicit)
                    .collect(Collectors.toList());

            assertTrue(expected.containsAll(actual.getExplicitSpotifyTracks()));

        } catch (IOException e) {
            throw new IllegalStateException("Cannot read from given file", e);
        }
    }

    @Test
    public void testGroupSpotifyTracksByYear() {
        var actReader = new StringReader(shortListOfTracks);

        SpotifyExplorer actual = new SpotifyExplorer(actReader);

        var expStringreader = new StringReader(shortListOfTracks);

        try (BufferedReader expReader = new BufferedReader(expStringreader)) {

            expReader.readLine();   //removes the first line of the given file

            Map<Integer, Set<SpotifyTrack>> expected;
            expected = expReader.lines()
                    .map(SpotifyTrack::of)
                    .collect(Collectors.groupingBy(SpotifyTrack::year, Collectors.toSet()));

            assertTrue(expected.equals(actual.groupSpotifyTracksByYear()));

        } catch (IOException e) {
            throw new IllegalStateException("Cannot read from given file", e);
        }
    }

    @Test
    public void testGetArtistActiveYears() {
        var actReader = new StringReader(shortListOfTracks);

        SpotifyExplorer actual = new SpotifyExplorer(actReader);

        assertEquals(18, actual.getArtistActiveYears("Linkin Park"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTopNHighestValenceTracksFromThe80sWithNegativeNumber() {
        var actReader = new StringReader(shortListOfTracks);

        SpotifyExplorer actual = new SpotifyExplorer(actReader);

        var expStringreader = new StringReader(shortListOfTracks);

        try (BufferedReader expReader = new BufferedReader(expStringreader)) {

            expReader.readLine();   //removes the first line of the given file

            List<SpotifyTrack> expected;
            expected = expReader.lines()
                    .map(SpotifyTrack::of)
                    .filter(song -> song.year() >= 1980 && song.year() <= 1989)
                    .sorted(Comparator.comparing(SpotifyTrack::valence).reversed())
                    .limit(5)
                    .collect(Collectors.toList());
            assertTrue(expected.equals(actual.getTopNHighestValenceTracksFromThe80s(-5)));

        } catch (IOException e) {
            throw new IllegalStateException("Cannot read from given file", e);
        }
    }

    @Test
    public void testGetTopNHighestValenceTracksFromThe80s() {

        shortListOfTracks += "7GhIk7Il098yCjg4BQjzvb,['Rick Astley'],Never Gonna Give You Up,"
                + "1987,68,212827,113.33,-11.855,0.916,0.135,0.727,0.939,0.151,0.0369,0\n"

                + "4fnGw1e3iYLlRqzJjhXUQI,['Guns N' Roses'],Welcome To The Jungle,"
                + "1989,39,273067,123.302,-8.081,0.237,0.00975,0.424,0.974,0.252,0.0913,0\n";

        var actReader = new StringReader(shortListOfTracks);

        SpotifyExplorer actual = new SpotifyExplorer(actReader);

        var expStringreader = new StringReader(shortListOfTracks);

        try (BufferedReader expReader = new BufferedReader(expStringreader)) {

            expReader.readLine();   //removes the first line of the given file

            List<SpotifyTrack> expected;
            expected = expReader.lines()
                    .map(SpotifyTrack::of)
                    .filter(song -> song.year() >= 1980 && song.year() <= 1989)
                    .sorted(Comparator.comparing(SpotifyTrack::valence).reversed())
                    .limit(5)
                    .collect(Collectors.toList());
            assertTrue(expected.equals(actual.getTopNHighestValenceTracksFromThe80s(5)));

        } catch (IOException e) {
            throw new IllegalStateException("Cannot read from given file", e);
        }
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetMostPopularTrackFromThe90sNonexistent() {
        var actReader = new StringReader(shortListOfTracks);

        SpotifyExplorer actual = new SpotifyExplorer(actReader);

        var expStringreader = new StringReader(shortListOfTracks);

        try (BufferedReader expReader = new BufferedReader(expStringreader)) {

            expReader.readLine();   //removes the first line of the given file

            SpotifyTrack expected;
            expected = expReader.lines()
                    .map(SpotifyTrack::of)
                    .filter(song -> song.year() >= 1990 && song.year() <= 1999)
                    .max(Comparator.comparing(SpotifyTrack::popularity))
                    .orElseThrow(NoSuchElementException::new);
            assertTrue(expected.equals(actual.getMostPopularTrackFromThe90s()));

        } catch (IOException e) {
            throw new IllegalStateException("Cannot read from given file", e);
        }
    }

    @Test
    public void testGetMostPopularTrackFromThe90s() {

        shortListOfTracks += "0bYg9bo50gSsH3LtXe2SQn,['Mariah Carey'],All I Want for Christmas Is You,"
                + "1994,88,241107,150.273,-7.463,0.35,0.164,0.336,0.627,0.0708,0.0384,0\n"

                + "2IHaGyfxNoFPLJnaEg4GTs,['Haddaway'],What Is Love - 7' Mix,"
                + "1993,67,270373,123.871,-7.907,0.737,0.0222,0.683,0.772,0.203,0.0311,0\n";

        var actReader = new StringReader(shortListOfTracks);

        SpotifyExplorer actual = new SpotifyExplorer(actReader);

        var expStringreader = new StringReader(shortListOfTracks);

        try (BufferedReader expReader = new BufferedReader(expStringreader)) {

            expReader.readLine();   //removes the first line of the given file

            SpotifyTrack expected;
            expected = expReader.lines()
                    .map(SpotifyTrack::of)
                    .filter(song -> song.year() >= 1990 && song.year() <= 1999)
                    .max(Comparator.comparing(SpotifyTrack::popularity))
                    .orElseThrow(NoSuchElementException::new);
            assertTrue(expected.equals(actual.getMostPopularTrackFromThe90s()));

        } catch (IOException e) {
            throw new IllegalStateException("Cannot read from given file", e);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNumberOfLongerTracksBeforeYearWithNegativeYear() {
        var actReader = new StringReader(shortListOfTracks);

        SpotifyExplorer actual = new SpotifyExplorer(actReader);

        assertTrue("Incorrect number of longer tracks", actual.getNumberOfLongerTracksBeforeYear(55, -1) == 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNumberOfLongerTracksBeforeYearWithNegativeMinutes() {
        var actReader = new StringReader(shortListOfTracks);

        SpotifyExplorer actual = new SpotifyExplorer(actReader);

        assertTrue("Incorrect number of longer tracks", actual.getNumberOfLongerTracksBeforeYear(-55, 2003) == 3);
    }

    @Test
    public void testGetNumberOfLongerTracksBeforeYear() {
        var actReader = new StringReader(shortListOfTracks);

        SpotifyExplorer actual = new SpotifyExplorer(actReader);

        assertTrue("Incorrect number of longer tracks", actual.getNumberOfLongerTracksBeforeYear(3, 2005) == 2);
        //of which being "Danny Boy" and "In The End"
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTheLoudestTrackInYearWithNegativeYear() {
        var actReader = new StringReader(shortListOfTracks);

        SpotifyExplorer actual = new SpotifyExplorer(actReader);

        var expStringreader = new StringReader(shortListOfTracks);

        try (BufferedReader expReader = new BufferedReader(expStringreader)) {

            expReader.readLine();   //removes the first line of the given file

            Optional expected;
            expected = expReader.lines()
                    .map(SpotifyTrack::of)
                    .filter(song -> song.year() == 2000)
                    .max(Comparator.comparingDouble(SpotifyTrack::loudness));

            assertTrue(expected.equals(actual.getTheLoudestTrackInYear(-11)));

        } catch (IOException e) {
            throw new IllegalStateException("Cannot read from given file", e);
        }
    }

    @Test
    public void testGetTheLoudestTrackInYear() {
        var actReader = new StringReader(shortListOfTracks);

        SpotifyExplorer actual = new SpotifyExplorer(actReader);

        var expStringreader = new StringReader(shortListOfTracks);

        try (BufferedReader expReader = new BufferedReader(expStringreader)) {

            expReader.readLine();   //removes the first line of the given file

            Optional expected;
            expected = expReader.lines()
                    .map(SpotifyTrack::of)
                    .filter(song -> song.year() == 2000)
                    .max(Comparator.comparingDouble(SpotifyTrack::loudness));

            assertTrue(expected.equals(actual.getTheLoudestTrackInYear(2000)));

        } catch (IOException e) {
            throw new IllegalStateException("Cannot read from given file", e);
        }
    }
}
