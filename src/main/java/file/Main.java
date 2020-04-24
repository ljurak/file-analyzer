package file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            throw new IllegalArgumentException("Probably some arguments are missing: pattern file or files directory");
        }

        String patternFile = args[0];
        File directory = new File(args[1]);

        List<Pattern> patterns = null;
        try {
            patterns = readPatterns(patternFile);
        } catch (IOException e) {
            System.out.println("There was a problem reading pattern file.");
            System.exit(1);
        }

        if (!(directory.exists() && directory.isDirectory())) {
            throw new IllegalArgumentException(args[1] + " does not exist or is not a directory");
        }

        long startTime = System.nanoTime();
        List<File> files = new ArrayList<>();
        Files.walk(directory.toPath()).forEach(path -> {
            if (Files.isRegularFile(path)) {
                files.add(path.toFile());
            }
        });

        PatternSearchEngine searchEngine = new PatternSearchEngine(patterns);
        ExecutorService pool = Executors.newFixedThreadPool(4);

        List<Future<String>> results = new ArrayList<>();
        for (File file : files) {
            CheckFileTask task = new CheckFileTask(file, searchEngine);
            Future<String> result = pool.submit(task);
            results.add(result);
        }

        for (Future<String> result : results) {
            try {
                System.out.println(result.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        long elapsedTime = System.nanoTime() - startTime;
        System.out.println(formatTime(elapsedTime));
        pool.shutdown();
    }

    private static List<Pattern> readPatterns(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            List<Pattern> patterns = new ArrayList<>();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                Pattern pattern = new Pattern(
                        Integer.parseInt(parts[0]),
                        parts[1].replace("\"", ""),
                        parts[2].replace("\"", "")
                );
                patterns.add(pattern);
            }

            return patterns;
        }
    }

    private static String formatTime(long nanos) {
        float milliseconds = (float) nanos / 1_000_000;
        return String.format("It took %.3f milliseconds", milliseconds);
    }
}
