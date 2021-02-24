package bg.sofia.uni.fmi.mjt.netflix.content;

import bg.sofia.uni.fmi.mjt.netflix.content.enums.Genre;
import bg.sofia.uni.fmi.mjt.netflix.content.enums.PgRating;

public non-sealed class Series implements Streamable{

    private String name;
    private Genre genre;
    private PgRating rating;
    private Episode[] episodes;

    public Series(String name, Genre genre, PgRating rating, Episode[] episodes){
        this.name=name;
        this.genre=genre;
        this.rating=rating;
        this.episodes=episodes;
    }

    @Override
    public String getTitle() {
        return name;
    }

    @Override
    public int getDuration() {
        int totalMinutes=0;
        for (int i = 0; i < episodes.length; i++) {
            totalMinutes+=episodes[i].duration();
        }
        return totalMinutes;
    }

    @Override
    public PgRating getRating() {
        return rating;
    }
}
