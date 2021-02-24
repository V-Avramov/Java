package bg.sofia.uni.fmi.mjt.tagger.comparator;

import bg.sofia.uni.fmi.mjt.tagger.city.City;

import java.util.Comparator;

public class CompareCityTags implements Comparator<City> {

    @Override
    public int compare(City city1, City city2) {
        return (-1) * (city1.getTags() - city2.getTags());
    }
}
