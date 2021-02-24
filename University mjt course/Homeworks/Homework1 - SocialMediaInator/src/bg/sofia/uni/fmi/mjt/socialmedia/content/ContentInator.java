package bg.sofia.uni.fmi.mjt.socialmedia.content;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ContentInator implements Content {

    private String username;
    private LocalDateTime publishedOn;
    private String description;
    private int contentNumber;

    private int likes = 0;
    private int comments = 0;
    private static int totalContents = 0;

    public ContentInator(String username, LocalDateTime publishedOn, String description) {
        this.username = username;
        this.publishedOn = publishedOn;
        this.description = description;
        contentNumber = totalContents;
        totalContents++;
    }

    public void like() {
        likes++;
    }

    public void comment() {
        comments++;
    }

    public LocalDateTime getPublishedOn() {
        return publishedOn;
    }

    @Override
    public int getNumberOfLikes() {
        return likes;
    }

    @Override
    public int getNumberOfComments() {
        return comments;
    }

    @Override
    public String getId() {
        return username + "-" + contentNumber;
    }

    @Override
    public Collection<String> getTags() {
        List<String> tags = new ArrayList<>();
        String wholeTag = "";   //keeps track of the current tag
        boolean isTag = false;    //to check if we are in a "tag"

        for (int i = 0; i < description.length(); i++) {
            char currentChar = description.charAt(i);

            if (currentChar == ' ' && !wholeTag.isEmpty()) {
                tags.add(wholeTag);
                wholeTag = "";
                isTag = false;
                continue;
            }

            if (isTag || (currentChar == '#' && (i == 0 || description.charAt(i - 1) == ' '))) {
                isTag = true;
                wholeTag += currentChar;
            }
        }
        //In order to add the last element (if there is any)
        if (!wholeTag.isEmpty()) {
            tags.add(wholeTag);
        }

        return tags;
    }

    @Override
    public Collection<String> getMentions() {
        List<String> mentions = new ArrayList<>();
        String wholeMention = "";   //keeps track of the current mention
        boolean isMention = false;    //to check if we are in a "mention"

        for (int i = 0; i < description.length(); i++) {
            char currentChar = description.charAt(i);

            if (currentChar == ' ' && !wholeMention.isEmpty()) {
                mentions.add(wholeMention);
                wholeMention = "";
                isMention = false;
                continue;
            }

            if (isMention || (currentChar == '@' && (i == 0 || description.charAt(i - 1) == ' '))) {
                isMention = true;
                wholeMention += currentChar;
            }
        }
        //In order to add the last element (if there is any)
        if (!wholeMention.isEmpty()) {
            mentions.add(wholeMention);
        }

        return mentions;
    }
}
