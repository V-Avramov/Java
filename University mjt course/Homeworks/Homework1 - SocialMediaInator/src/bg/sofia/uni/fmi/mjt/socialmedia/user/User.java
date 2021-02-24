package bg.sofia.uni.fmi.mjt.socialmedia.user;

import bg.sofia.uni.fmi.mjt.socialmedia.content.Content;
import bg.sofia.uni.fmi.mjt.socialmedia.content.ContentInator;
import bg.sofia.uni.fmi.mjt.socialmedia.comparators.CompareActivitiesByDate;
import bg.sofia.uni.fmi.mjt.socialmedia.comparators.CompareContentByPublishDate;
import bg.sofia.uni.fmi.mjt.socialmedia.content.enums.ContentType;
import bg.sofia.uni.fmi.mjt.socialmedia.user.useractivity.Activity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class User {

    private List<ContentInator> allContent;
    private List<ContentInator> activeContent;
    private int popularity; //sum of the mentions of the current user

    //The user activity is kept in an activity priority
    //with a custom comparable comparing the time of the activities
    private List<Activity> userActivity;

    public User() {
        allContent = new ArrayList<>();
        activeContent = new ArrayList<>();
        userActivity = new ArrayList<>();
        popularity = 0;
    }

    public void addContent(LocalDateTime publishedOn, String description, ContentType type, ContentInator content) {
        allContent.add(content);
        String log = getLogDateTimeInfo(publishedOn);

        if (type == ContentType.Post) {
            if (LocalDateTime.now().minusDays(30).isBefore(publishedOn)) {
                activeContent.add(content);
            }
            //Log information--
            log += ": Created a post with id " + content.getId();
            //-----------------
        } else if (type == ContentType.Story) {
            if (LocalDateTime.now().minusHours(24).isBefore(publishedOn)) {
                activeContent.add(content);
            }
            //Log information --
            log += ": Created a story with id " + content.getId();
            //------------------
        }

        /*if (LocalDateTime.now().minusDays(30).isBefore(publishedOn) && type == ContentType.Post) {

            activeContent.add(content);
        } else if (LocalDateTime.now().minusHours(24).isBefore(publishedOn) && type == ContentType.Story) {

            activeContent.add(content);
        }
        if (type == ContentType.Post) {
            //Log information--
            log += ": Created a post with id " + content.getId();
            //-----------------
        } else if (type == ContentType.Story) {
            //Log information --
            log += ": Created a story with id " + content.getId();
            //------------------
        }*/
        userActivity.add(new Activity(publishedOn, log));
    }

    public void increasePopularity() {
        popularity++;
    }

    public int getPopularity() {
        return popularity;
    }

    public void like(Content content) {
        String log = getLogDateTimeInfo(LocalDateTime.now());
        log += ": Liked a content with id " + content.getId();
        userActivity.add(new Activity(LocalDateTime.now(), log));
    }

    public void comment(Content content, String text) {
        String log = getLogDateTimeInfo(LocalDateTime.now());
        log += ": Commented " + '"' + text + '"' + " on a content with id " + content.getId();
        userActivity.add(new Activity(LocalDateTime.now(), log));
    }

    public Collection<Content> getNMostRecent(int n) {
        CompareContentByPublishDate comparator = new CompareContentByPublishDate();
        Collections.sort(activeContent, comparator);

        List<Content> nthMostRecent = new ArrayList<>();


        if (n > activeContent.size()) {
            n = activeContent.size();
        }

        for (int i = 0; i < n; i++) {
            nthMostRecent.add(activeContent.get(i));
        }

        return nthMostRecent;
    }

    public List<String> getActivity() {

        CompareActivitiesByDate comparator = new CompareActivitiesByDate();
        Collections.sort(userActivity, comparator);

        List<String> log = new ArrayList<>();

        for (int i = 0; i < userActivity.size(); i++) {
            log.add(userActivity.get(i).log());
        }

        return log;
    }

    private static String getLogDateTimeInfo(LocalDateTime date) {

        String returnedDate = date.getHour() + ":" + date.getMinute() + ":" + date.getSecond();
        returnedDate += " " + date.getDayOfMonth() + "." + date.getMonthValue() + "." + date.getYear();

        return returnedDate;
    }
}
