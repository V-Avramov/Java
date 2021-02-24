package bg.sofia.uni.fmi.mjt.socialmedia.comparators;

import bg.sofia.uni.fmi.mjt.socialmedia.content.ContentInator;

import java.util.Comparator;

public class CompareContentByPopularity implements Comparator<ContentInator> {

    @Override
    public int compare(ContentInator content1, ContentInator content2) {
        final int content1Popularity = content1.getNumberOfLikes() + content1.getNumberOfComments();
        final int content2Popularity = content2.getNumberOfLikes() + content2.getNumberOfComments();
        //reversed
        return (-1) * (content1Popularity - content2Popularity);
    }
}
