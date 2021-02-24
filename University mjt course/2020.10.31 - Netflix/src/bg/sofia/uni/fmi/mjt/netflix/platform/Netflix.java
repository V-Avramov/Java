package bg.sofia.uni.fmi.mjt.netflix.platform;

import bg.sofia.uni.fmi.mjt.netflix.account.Account;
import bg.sofia.uni.fmi.mjt.netflix.content.Streamable;
import bg.sofia.uni.fmi.mjt.netflix.content.enums.PgRating;
import bg.sofia.uni.fmi.mjt.netflix.exceptions.*;

import java.time.LocalDateTime;
import java.util.Arrays;

public class Netflix implements StreamingService{

    private Account[] accounts;
    private Streamable[] streamableContent;
    private int[] viewed;

    public Netflix(Account[] accounts, Streamable[] streamableContent){
        this.accounts=accounts;
        this.streamableContent=streamableContent;
        this.viewed=new int[this.streamableContent.length];
        Arrays.fill(viewed, 0);
    }

    @Override
    public void watch(Account user, String videoContentName) throws ContentUnavailableException {

        for (int i=0; i < accounts.length; i++) {
            if(accounts[i]==user){
                break;
            }
            if(i==accounts.length-1){
                throw new UserNotFoundException();
            }
        }

        Streamable searchedContent = null;

        int i=0;
        for (; i < streamableContent.length; i++) {
            if(streamableContent[i].getTitle().equals(videoContentName)){
                searchedContent=streamableContent[i];
                break;
            }
        }

        if(i==streamableContent.length){
            throw new ContentNotFoundException();
        }
        int userAge=(LocalDateTime.now().getYear())-user.birthdayDate().getYear();

        if(searchedContent.getRating().equals(PgRating.G)){
            viewed[i]++;
            return;
        }
        else if(searchedContent.getRating().equals(PgRating.PG13)&&userAge<=13){
            throw new ContentUnavailableException();
        }
        else if(searchedContent.getRating().equals(PgRating.NC17)&&userAge<=17){
            throw new ContentUnavailableException();
        }
        viewed[i]++;
    }

    @Override
    public Streamable findByName(String videoContentName) {
        for (int i = 0; i < streamableContent.length; i++) {
            if(streamableContent[i].getTitle().equals(videoContentName)){
                return streamableContent[i];
            }
        }
        return null;
    }

    @Override
    public Streamable mostViewed() {
        int i=0;
        int max=0;
        Streamable currMax=null;
        for (; i < viewed.length; i++) {
            if(viewed[i]>max){
                max=viewed[i];
                currMax=streamableContent[i];
            }
        }
        return currMax;
    }

    @Override
    public int totalWatchedTimeByUsers() {
        int total=0;
        for (int i = 0; i < viewed.length; i++) {
            if(viewed[i]>0){
                int timesWatched=viewed[i];
                while(timesWatched>0){
                    total+=streamableContent[i].getDuration();
                    timesWatched--;
                }
            }
        }
        return total;
    }
}
