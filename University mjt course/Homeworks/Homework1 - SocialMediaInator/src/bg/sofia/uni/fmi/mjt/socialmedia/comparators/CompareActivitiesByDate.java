package bg.sofia.uni.fmi.mjt.socialmedia.comparators;

import bg.sofia.uni.fmi.mjt.socialmedia.user.useractivity.Activity;

import java.util.Comparator;

public class CompareActivitiesByDate implements Comparator<Activity> {
    @Override
    public int compare(Activity activity1, Activity activity2) {
        if (activity1.publishedOn().isBefore(activity2.publishedOn())) {
            return 1;
        } else if (activity1.publishedOn().isAfter(activity2.publishedOn())) {
            return -1;
        }
        return 0;
    }
}
