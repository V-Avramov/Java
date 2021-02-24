package bg.sofia.uni.fmi.mjt.socialmedia;

import java.time.LocalDateTime;

public class Inate {
    public static void main(String[] args) {
        EvilSocialInator doofs = new EvilSocialInator();

        doofs.register("Doof");
        doofs.register("Perry");

        doofs.publishPost("Doof", LocalDateTime.now(), "asdf @Perry #inator");

        System.out.println(doofs.getNMostRecentContent("Doof", 5));

        switch('a') {
            case 'a' -> System.out.println(123);
            case 'b' -> System.out.println(456);
        }
    }
}
