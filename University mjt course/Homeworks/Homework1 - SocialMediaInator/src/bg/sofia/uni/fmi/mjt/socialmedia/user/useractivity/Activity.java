package bg.sofia.uni.fmi.mjt.socialmedia.user.useractivity;

import java.time.LocalDateTime;

public record Activity(LocalDateTime publishedOn, String log) { }
