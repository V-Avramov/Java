package bg.sofia.uni.fmi.mjt.spellchecker.comparator;

import bg.sofia.uni.fmi.mjt.spellchecker.pair.Pair;

import java.util.Comparator;

public class CompareByPercentage implements Comparator<Pair> {
    @Override
    public int compare(Pair o1, Pair o2) {

        final double perc1 = o1.getPercentage();
        final double perc2 = o2.getPercentage();

        if (Double.compare(perc1, perc2) > 0) {
            return -1;
        } else if (Double.compare(perc1, perc2) < 0) {
            return 1;
        }
        return 0;
    }
}
