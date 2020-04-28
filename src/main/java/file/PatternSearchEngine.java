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

    public static boolean rabinKarpSearch(byte[] bytes, byte[] pattern) {
        int t = bytes.length;
        int p = pattern.length;

        int a = 53;
        long m = 1_000_000_000 + 9;

        long patternHash = 0;
        long currentHash = 0;
        long pow = 1;

        for (int i = 0; i < p; i++) {
            patternHash += pattern[i] * pow;
            patternHash %= m;

            currentHash += bytes[i] * pow;
            currentHash %= m;

            if (i != p - 1) {
                pow = pow * a % m;
            }
        }

        for (int i = 0; i <= t - p; i++) {
            if (patternHash == currentHash) {
                boolean patternIsFound = true;
                for (int j = 0; j < p; j++) {
                    if (bytes[i + j] != pattern[j]) {
                        patternIsFound = false;
                        break;
                    }
                }

                if (patternIsFound) {
                    return true;
                }
            }

            if (i < t - p) {
                currentHash = (currentHash - bytes[i] + m) % m / a;
                currentHash = (currentHash + bytes[i + p] * pow) % m;
            }
        }

        return false;
    }

    public static boolean rabinKarpSearch(String text, String pattern) {
        int t = text.length();
        int p = pattern.length();

        int patternHash = polynomialHash(pattern, 0, p);
        int currentHash = polynomialHash(text, t - p, t);

        for (int i = t - p; i >= 0; i--) {
            if (currentHash == patternHash) {
                boolean foundPattern = true;
                for (int j = 0; j < pattern.length(); j++) {
                    if (text.charAt(i + j) != pattern.charAt(j)) {
                        foundPattern = false;
                        break;
                    }
                }

                if (foundPattern) {
                    return true;
                }
            }

            if (i > 0) {
                currentHash = rollingHash(text, i - 1, i - 1 + p, currentHash);
            }
        }

        return false;
    }

    private static int polynomialHash(String s, int from, int to) { // from inclusive, to exclusive
        if (from >= to) {
            throw new IllegalArgumentException("To must be greater than from");
        }

        int a = 53;
        int m = 1_000_000_000 + 9;

        int power = 1;
        int hash = s.charAt(from);

        for (int i = from; i < to; i++) {
            hash += s.charAt(i) * power;
            hash %= m;
            power = power * a % m;
        }

        return hash;
    }

    private static int rollingHash(String s, int from, int to, int previousHash) { // from inclusive to exclusive
        int a = 53;
        int m = 1_000_000_000 + 9;

        int hash = previousHash - s.charAt(to) * (int) Math.pow(a, to - from - 1) % m + m;
        hash = hash * a % m;
        hash = (hash + s.charAt(from)) % m;
        return hash;
    }
}
