package bg.sofia.uni.fmi.mjt.spellchecker.pair;

public class Pair {

    private double percentage;
    private String givenWord;

    public Pair(double percentage, String givenWord) {
        this.percentage = percentage;
        this.givenWord = givenWord;
    }

    public double getPercentage() {
        return percentage;
    }

    public String getGivenWord() {
        return givenWord;
    }
}
