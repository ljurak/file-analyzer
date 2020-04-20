package file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            throw new IllegalArgumentException("Probably some arguments are missing: filename, pattern, file type");
        }

        String filename = args[0];
        String pattern = args[1];
        String fileType = args[2];

        byte[] patternBytes = pattern.getBytes();
        byte[] bytes = Files.readAllBytes(Paths.get(filename));

        if (findPattern(bytes, patternBytes)) {
            System.out.println(fileType);
        } else {
            System.out.println("Unknown file type");
        }
    }

    private static boolean findPattern(byte[] bytes, byte[] pattern) {
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
