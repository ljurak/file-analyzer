package file;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PatternSearchEngine {

    private final List<Pattern> patterns;

    public PatternSearchEngine(List<Pattern> patterns) {
        this.patterns = patterns;
    }

    public List<Pattern> searchPatterns(byte[] bytes) {
        List<Pattern> foundPatterns = new ArrayList<>();
        for (Pattern pattern : patterns) {
            if (kmpSearch(bytes, pattern.getPattern().getBytes())) {
                foundPatterns.add(pattern);
            }
        }
        return foundPatterns;
    }

    public boolean kmpSearch(byte[] bytes, byte[] pattern) {
        int[] prefix = prefixFunction(pattern);

        int j = 0;
        for (int i = 0; i < bytes.length; i++) {
            while (j > 0 && bytes[i] != pattern[j]) {
                j = prefix[j - 1];
            }

            if (bytes[i] == pattern[j]) {
                j++;
            }

            if (j == pattern.length) {
                return true;
            }
        }

        return false;
    }

    public boolean kmpSearch(String text, String pattern) {
        int[] prefix = prefixFunction(pattern);

        int j = 0;
        for (int i = 0; i < text.length(); i++) {
            while (j > 0 && text.charAt(i) != pattern.charAt(j)) {
                j = prefix[j - 1];
            }

            if (text.charAt(i) == pattern.charAt(j)) {
                j++;
            }

            if (j == pattern.length()) {
                return true;
            }
        }

        return false;
    }

    private int[] prefixFunction(byte[] bytes) {
        int[] prefix = new int[bytes.length];

        for (int i = 1; i < prefix.length; i++) {
            int j = prefix[i - 1];

            while (j > 0 && bytes[i] != bytes[j]) {
                j = prefix[j - 1];
            }

            if (bytes[i] == bytes[j]) {
                j++;
            }

            prefix[i] = j;
        }

        return prefix;
    }

    private int[] prefixFunction(String s) {
        int[] prefix = new int[s.length()];

        for (int i = 1; i < prefix.length; i++) {
            int j = prefix[i - 1];

            while (j > 0 && s.charAt(i) != s.charAt(j)) {
                j = prefix[j - 1];
            }

            if (s.charAt(i) == s.charAt(j)) {
                j++;
            }

            prefix[i] = j;
        }

        return prefix;
    }

    private boolean naiveSearch(byte[] bytes, byte[] pattern) {
        if (bytes.length < pattern.length) {
            return false;
        }

        for (int i = 0; i < bytes.length - pattern.length + 1; i++) {
            if (bytes[i] == pattern[0]) {
                if (Arrays.equals(bytes, i, i + pattern.length, pattern, 0, pattern.length)) {
                    return true;
                }
            }
        }
        return false;
    }
}
