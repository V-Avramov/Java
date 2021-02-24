package bg.sofia.uni.fmi.mjt.socialmedia;

import bg.sofia.uni.fmi.mjt.socialmedia.content.Content;
import bg.sofia.uni.fmi.mjt.socialmedia.content.ContentInator;
import bg.sofia.uni.fmi.mjt.socialmedia.comparators.CompareContentByPopularity;
import bg.sofia.uni.fmi.mjt.socialmedia.content.enums.ContentType;
import bg.sofia.uni.fmi.mjt.socialmedia.exceptions.ContentNotFoundException;
import bg.sofia.uni.fmi.mjt.socialmedia.exceptions.NoUsersException;
import bg.sofia.uni.fmi.mjt.socialmedia.exceptions.UsernameAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.socialmedia.exceptions.UsernameNotFoundException;
import bg.sofia.uni.fmi.mjt.socialmedia.user.User;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class EvilSocialInator implements SocialMediaInator {

    private Map<String, User> accounts;
    private Map<String, ContentInator> allContent;
    private List<ContentInator> activeContent;

    public EvilSocialInator() {
        accounts = new HashMap<>();
        allContent = new HashMap<>();
        activeContent = new ArrayList<>();
    }

    @Override
    public void register(String username) {
        if (username == null) {
            throw new IllegalArgumentException("Invalid input");
        }
        if (accounts.containsKey(username)) {
            throw new UsernameAlreadyExistsException("Username is taken!");
        }
        accounts.put(username, new User());
    }

    @Override
    public String publishPost(String username, LocalDateTime publishedOn, String description) {

        if (username == null || publishedOn == null || description == null) {
            throw new IllegalArgumentException("Cannot publish post with a null parameter");
        }

        if (!accounts.containsKey(username)) {
            throw new UsernameNotFoundException("User " + username + " not found");
        }

        ContentInator postContent = new ContentInator(username, publishedOn, description);
        Collection<String> allMentions = postContent.getMentions();

        for (String currentMention : allMentions) {
            if (accounts.containsKey(currentMention.substring(1))) { //substring(1) is used because we extract
                                                                    //the first element being "@"
                accounts.get(currentMention.substring(1)).increasePopularity();
            }
        }

        if (LocalDateTime.now().minusDays(30).isBefore(publishedOn)) {
            activeContent.add(postContent);
        }
        accounts.get(username).addContent(publishedOn, description, ContentType.Post, postContent);
        allContent.put(postContent.getId(), postContent);

        return postContent.getId();
    }

    @Override
    public String publishStory(String username, LocalDateTime publishedOn, String description) {

        if (username == null || publishedOn == null || description == null) {
            throw new IllegalArgumentException("Cannot publish story with a null parameter");
        }
        ContentInator storyContent = new ContentInator(username, publishedOn, description);
        Collection<String> allMentions = storyContent.getMentions();

        for (String currentMention : allMentions) {
            if (accounts.containsKey(currentMention.substring(1))) { //substring(1) is used because we extract
                                                                    //the first element being "@"
                accounts.get(currentMention.substring(1)).increasePopularity();
            }
        }

        if (LocalDateTime.now().minusHours(24).isBefore(publishedOn)) {
            activeContent.add(storyContent);
        }
        accounts.get(username).addContent(publishedOn, description, ContentType.Story, storyContent);
        allContent.put(storyContent.getId(), storyContent);

        return storyContent.getId();
    }

    private String publish(String username, LocalDateTime publishedOn, String description, ContentType type) {
        if (username == null || publishedOn == null || description == null) {
            throw new IllegalArgumentException("Cannot publish story with a null parameter");
        }
        ContentInator content = new ContentInator(username, publishedOn, description);
        Collection<String> allMentions = content.getMentions();

        for (String currentMention : allMentions) {
            if (accounts.containsKey(currentMention.substring(1))) { //substring(1) is used because we extract
                                                                    //the first element being "@"
                accounts.get(currentMention.substring(1)).increasePopularity();
            }
        }

        if (type == ContentType.Post) {
            if (LocalDateTime.now().minusDays(30).isBefore(publishedOn)) {
                activeContent.add(content);
            }
        }

        else if (type == ContentType.Story) {
            if (LocalDateTime.now().minusHours(24).isBefore(publishedOn)) {
                activeContent.add(content);
            }
        }

        accounts.get(username).addContent(publishedOn, description, type, content);
        allContent.put(content.getId(), content);

        return content.getId();
    }

    @Override
    public void like(String username, String id) {
        if (username == null || id == null) {
            throw new IllegalArgumentException("Invalid username or content id input");
        }

        if (!accounts.containsKey(username)) {
            throw new UsernameNotFoundException("User " + username + " not found");
        }

        if (!allContent.containsKey(id)) {
            throw new ContentNotFoundException("Content " + id + " does not exist");
        }

        allContent.get(id).like(); //adds a like to the given content
        accounts.get(username).like(allContent.get(id)); //given user likes the content
    }

    @Override
    public void comment(String username, String text, String id) {

        if (username == null || id == null) {
            throw new IllegalArgumentException("Invalid username or content id input");
        }

        if (!accounts.containsKey(username)) {
            throw new UsernameNotFoundException("User " + username + " not found");
        }

        if (!allContent.containsKey(id)) {
            throw new ContentNotFoundException("Content " + id + " does not exist");
        }

        allContent.get(id).comment(); //adds a comment to the given content
        accounts.get(username).comment(allContent.get(id), text);
    }

    @Override
    public Collection<Content> getNMostPopularContent(int n) {

        if (n < 0) {
            throw new IllegalArgumentException("Input should be a positive integer");
        }

        if (n > activeContent.size()) {
            n = activeContent.size();
        }

        List<Content> nmostPopularContent = new ArrayList<>();

        CompareContentByPopularity comparator = new CompareContentByPopularity();

        Collections.sort(activeContent, comparator);

        for (int i = 0; i < n; i++) {
            nmostPopularContent.add(activeContent.get(i));
        }

        return nmostPopularContent;
    }

    @Override
    public Collection<Content> getNMostRecentContent(String username, int n) {

        if (username == null || n < 0) {
            throw new IllegalArgumentException("Inator cannot get most recent content with given input");
        }

        if (!accounts.containsKey(username)) {
            throw new UsernameNotFoundException("User " + username + " does not exist!");
        }

        return accounts.get(username).getNMostRecent(n);
    }

    @Override
    public String getMostPopularUser() {

        if (accounts.isEmpty()) {
            throw new NoUsersException("There are no users in the platform! Curse you Perry the Platypus!");
        }

        String name = null;
        int currPopularity = 0;
        for (String currentUser : accounts.keySet()) {
            if (currPopularity < accounts.get(currentUser).getPopularity()) {
                name = currentUser;
                currPopularity = accounts.get(currentUser).getPopularity();
            }
        }

        return name;
    }

    @Override
    public Collection<Content> findContentByTag(String tag) {
        if (tag == null) {
            throw new IllegalArgumentException("Given tag is null");
        }

        List<Content> containingTag = new ArrayList<>();
        for (ContentInator currentContent : activeContent) {
            if (currentContent.getTags().contains(tag)) {
                containingTag.add(currentContent);
            }
        }

        return containingTag;
    }

    @Override
    public List<String> getActivityLog(String username) {
        if (username == null) {
            throw new IllegalArgumentException("Given username is null!");
        }
        if (!accounts.containsKey(username)) {
            throw new UsernameNotFoundException("User " + username + " does not exist!");
        }
        return accounts.get(username).getActivity();
    }
}
