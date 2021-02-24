package bg.sofia.uni.fmi.mjt.netflix.content;

import bg.sofia.uni.fmi.mjt.netflix.content.enums.Genre;
import bg.sofia.uni.fmi.mjt.netflix.content.enums.PgRating;

public non-sealed class Movie implements Streamable {

    private String name;
    private Genre genre;
    private PgRating rating;
    private int duration;

    public Movie(String name, Genre genre, PgRating rating, int duration){
        this.name=name;
        this.genre=genre;
        this.rating=rating;
        this.duration=duration;
    }

    @Override
    public String getTitle() {
        return name;
    }

    @Override
    public int getDuration() {
        return duration;
    }

    @Override
    public PgRating getRating() {
        return rating;
    }
}
