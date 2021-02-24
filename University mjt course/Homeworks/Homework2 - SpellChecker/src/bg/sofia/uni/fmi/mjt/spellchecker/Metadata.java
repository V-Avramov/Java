package bg.sofia.uni.fmi.mjt.spellchecker;

public record Metadata(int characters, int words, int mistakes) {

    @Override
    public String toString() {
        return characters() + " characters, " + words() + " words, " + mistakes() + " spelling issue(s) found";
    }
}
