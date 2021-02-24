package bg.sofia.uni.fmi.mjt.socialmedia.comparators;

import bg.sofia.uni.fmi.mjt.socialmedia.content.ContentInator;

import java.util.Comparator;

public class CompareContentByPublishDate implements Comparator<ContentInator> {
    @Override
    public int compare(ContentInator content1, ContentInator content2) {
        //reversed
        if (content1.getPublishedOn().isAfter(content2.getPublishedOn())) {
            return -1;
        } else if (content1.getPublishedOn().isBefore(content2.getPublishedOn())) {
            return 1;
        }
        return 0;
    }
}
