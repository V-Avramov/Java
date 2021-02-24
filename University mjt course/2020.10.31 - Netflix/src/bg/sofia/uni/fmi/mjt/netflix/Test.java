package bg.sofia.uni.fmi.mjt.netflix;

import bg.sofia.uni.fmi.mjt.netflix.account.Account;
import bg.sofia.uni.fmi.mjt.netflix.content.Episode;
import bg.sofia.uni.fmi.mjt.netflix.content.Movie;
import bg.sofia.uni.fmi.mjt.netflix.content.Series;
import bg.sofia.uni.fmi.mjt.netflix.content.Streamable;
import bg.sofia.uni.fmi.mjt.netflix.content.enums.Genre;
import bg.sofia.uni.fmi.mjt.netflix.content.enums.PgRating;
import bg.sofia.uni.fmi.mjt.netflix.exceptions.ContentUnavailableException;
import bg.sofia.uni.fmi.mjt.netflix.platform.Netflix;
import bg.sofia.uni.fmi.mjt.netflix.platform.StreamingService;

import java.time.LocalDateTime;

public class Test {

    public static void main(String[] args) {
        Episode gotEp1 = new Episode("Winter is Coming", 50);
        Episode gotEp2 = new Episode("The Kingsroad", 50);
        Episode[] gotEps = new Episode[2];
        gotEps[0] = gotEp1;
        gotEps[1] = gotEp2;
        Series got = new Series("Got", Genre.ACTION, PgRating.NC17, gotEps);

        Movie it=new Movie("It", Genre.HORROR, PgRating.PG13, 100);

        Movie comedy=new Movie("Comedy", Genre.COMEDY, PgRating.G, 100);

        Streamable[] s=new Streamable[3];

        s[0]=got;
        s[1]=it;
        s[2]=comedy;

        Account myAcc= new Account("user", LocalDateTime.of(1999, 5, 14, 1,1,1));
        Account other= new Account("a", LocalDateTime.of(2009, 1, 1, 1, 1, 1));

        Account[] a= new Account[2];

        a[0]=myAcc;
        a[1]=other;

        Netflix n = new Netflix(a, s);
        try {
            n.watch(myAcc, "Got");
        } catch (ContentUnavailableException e) {
            e.printStackTrace();
        }
        try {
            n.watch(other, "It");
        } catch (ContentUnavailableException e) {
            e.printStackTrace();
        }
        try {
            n.watch(myAcc, "It");
        } catch (ContentUnavailableException e) {
            e.printStackTrace();
        }
        try {
            n.watch(myAcc, "It");
        } catch (ContentUnavailableException e) {
            e.printStackTrace();
        }
        try {
            n.watch(myAcc, null);
        } catch (ContentUnavailableException e) {
            e.printStackTrace();
        }

        System.out.println(n.totalWatchedTimeByUsers());

        System.out.println(n.mostViewed().getTitle());
    }
}