package bg.sofia.uni.fmi.mjt.tagger.city;

public class City {

    private String cityName;
    private String country;
    private int tags;

    public City(String cityName, String country) {
        this.cityName = cityName;
        this.country = country;
        this.tags = 0;
    }

    public String getCityName() {
        return cityName;
    }

    public String getCountry() {
        return country;
    }

    public int getTags() {
        return tags;
    }

    public void increaseTags() {
        tags++;
    }

    public void zeroOutTags() {
        tags = 0;
    }
}