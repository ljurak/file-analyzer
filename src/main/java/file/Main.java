package file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws IOException {
        if (args.length != 4) {
            throw new IllegalArgumentException("Probably some arguments are missing: program mode, filename, pattern, file type message");
        }

        String mode = args[0];
        String filename = args[1];
        String pattern = args[2];
        String fileType = args[3];

        PatternSearchEngine searchEngine = new PatternSearchEngine();
        byte[] patternBytes = pattern.getBytes();
        byte[] fileBytes = Files.readAllBytes(Paths.get(filename));

        long startTime = System.nanoTime();
        boolean searchResult;
        switch (mode) {
            case "--naive":
                searchResult = searchEngine.naiveSearch(fileBytes, patternBytes);
                break;
            case "--KMP":
                searchResult = searchEngine.kmpSearch(fileBytes, patternBytes);
                break;
            default:
                throw new IllegalArgumentException("Unsupported program mode");
        }

        long elapsedTime = System.nanoTime() - startTime;
        if (searchResult) {
            System.out.println(fileType);
        } else {
            System.out.println("Unknown file type");
        }
        System.out.println(formatTime(elapsedTime));
    }

    private static String formatTime(long nanos) {
        float milliseconds = (float) nanos / 1_000_000;
        return String.format("It took %.3f milliseconds", milliseconds);
    }
}
