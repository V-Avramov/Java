package bg.sofia.uni.fmi.mjt.tagger;

import bg.sofia.uni.fmi.mjt.tagger.city.City;
import bg.sofia.uni.fmi.mjt.tagger.comparator.CompareCityTags;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;


public class Tagger {

    private Map<String, City> citiesCountries;
    private boolean isTagCitiesCalled;

    /**
     * Creates a new instance of Tagger for a given list of city/country pairs
     *
     * @param citiesReader a java.io.Reader input stream containing list of cities and countries
     *                     in the specified CSV format
     */
    public Tagger(Reader citiesReader) {
        citiesCountries = new HashMap<>();
        BufferedReader bufCitiesReader = new BufferedReader(citiesReader);
        this.isTagCitiesCalled = false;

        String currentLine;

        try {
            while ((currentLine = bufCitiesReader.readLine()) != null) {
                int getComma = currentLine.indexOf(',');
                String city = currentLine.substring(0, getComma);
                String country = currentLine.substring(getComma + 1); //in order to get the name of the country

                citiesCountries.put(city.toLowerCase(), new City(city, country));
            }
        } catch (IOException e) {
            throw new RuntimeException("Cannot get reader contents");
        }
    }

    /**
     * Processes an input stream of a text file, tags any cities and outputs result
     * to a text output stream.
     *
     * @param text   a java.io.Reader input stream containing text to be processed
     * @param output a java.io.Writer output stream containing the result of tagging
     */
    public void tagCities(Reader text, Writer output) {

        isTagCitiesCalled = true;

        for (String city : citiesCountries.keySet()) {
            citiesCountries.get(city).zeroOutTags();
        }

        try (BufferedReader bufText = new BufferedReader(text)) {
            String line;
            boolean firstLine = true;

            while ((line = bufText.readLine()) != null) {
                String currentWord = "";
                String wholeText = "";
                if (!firstLine) {
                    wholeText += System.lineSeparator();
                }
                firstLine = false;
                boolean endOfLine = false; //flagging the end of a line!
                long lineLen = line.length();

                for (int i = 0; i < lineLen; i++) {
                    char currentChar = line.charAt(i);
                    if (!(currentChar >= 'A' && currentChar <= 'Z')
                            && !(currentChar >= 'a' && currentChar <= 'z')) {
                        if (citiesCountries.containsKey(currentWord.toLowerCase())) {
                            wholeText += "<city country=" + '"'
                                    + citiesCountries.get(currentWord.toLowerCase()).getCountry()
                                    + '"' + ">" + currentWord + "</city>" + currentChar;
                            citiesCountries.get(currentWord.toLowerCase()).increaseTags();
                        } else {
                            wholeText += currentWord + currentChar;
                        }
                        currentWord = "";
                        continue;
                    }
                    if (i + 1 == lineLen) {
                        endOfLine = true;
                    }
                    if (endOfLine) {
                        currentWord += currentChar;
                        if (citiesCountries.containsKey(currentWord.toLowerCase())) {
                            wholeText += "<city country=" + '"'
                                    + citiesCountries.get(currentWord.toLowerCase()).getCountry()
                                    + '"' + ">" + currentWord + "</city>";
                            citiesCountries.get(currentWord.toLowerCase()).increaseTags();
                        } else {
                            wholeText += currentWord;
                        }
                        currentWord = "";
                        continue;
                    }
                    currentWord += currentChar;
                }

                try {
                    output.write(wholeText);
                    output.flush();
                } catch (IOException e) {
                    throw new RuntimeException("Cannot write to given file");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Cannot get text");
        }
    }

    /**
     * Returns a collection the top @n most tagged cities' unique names
     * from the last tagCities() invocation. Note that if a particular city has been tagged
     * more than once in the text, just one occurrence of its name should appear in the result.
     * If @n exceeds the total number of cities tagged, return as many as available
     * If tagCities() has not been invoked at all, return an empty collection.
     *
     * @param n the maximum number of top tagged cities to return
     * @return a collection the top @n most tagged cities' unique names
     * from the last tagCities() invocation.
     */
    public Collection<String> getNMostTaggedCities(int n) {
        if (!isTagCitiesCalled) {
            return new ArrayList<>();
        }

        List<City> cities = new ArrayList<>();
        for (String currentCity : citiesCountries.keySet()) {
            cities.add(citiesCountries.get(currentCity));
        }

        CompareCityTags comparator = new CompareCityTags();
        Collections.sort(cities, comparator);

        List<String> nmost = new ArrayList<>();
        int index = 0;
        for (City currentCity : cities) {
            if (index == n) {
                break;
            }
            nmost.add(currentCity.getCityName());
            index++;
        }

        return nmost;
    }

    /**
     * Returns a collection of all tagged cities' unique names
     * from the last tagCities() invocation. Note that if a particular city has been tagged
     * more than once in the text, just one occurrence of its name should appear in the result.
     * If tagCities() has not been invoked at all, return an empty collection.
     *
     * @return a collection of all tagged cities' unique names
     * from the last tagCities() invocation.
     */
    public Collection<String> getAllTaggedCities() {
        if (!isTagCitiesCalled) {
            return new ArrayList<>();
        }

        List<String> cities = new ArrayList<>();
        for (String city : citiesCountries.keySet()) {
            if (citiesCountries.get(city).getTags() != 0) {
                String originalCityName = city.substring(0, 1).toUpperCase() + city.substring(1);
                cities.add(originalCityName);
            }
        }
        return cities;
    }

    /**
     * Returns the total number of tagged cities in the input text
     * from the last tagCities() invocation
     * In case a particular city has been taged in several occurences, all must be counted.
     * If tagCities() has not been invoked at all, return 0.
     *
     * @return the total number of tagged cities in the input text
     */
    public long getAllTagsCount() {
        if (!isTagCitiesCalled) {
            return 0;
        }

        long totalTags = 0;

        for (String city : citiesCountries.keySet()) {
            if (citiesCountries.get(city).getTags() != 0) {
                totalTags += citiesCountries.get(city).getTags();
            }
        }

        return totalTags;
    }
}
