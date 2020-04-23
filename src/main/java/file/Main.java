package file;

import java.io.File;
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
        if (args.length != 3) {
            throw new IllegalArgumentException("Probably some arguments are missing: directory, pattern, file type message");
        }

        File directory = new File(args[0]);
        String pattern = args[1];
        String fileType = args[2];

        if (!(directory.exists() && directory.isDirectory())) {
            throw new IllegalArgumentException(args[0] + " does not exist or is not a directory");
        }

        long startTime = System.nanoTime();
        List<File> files = new ArrayList<>();
        Files.walk(directory.toPath()).forEach(path -> {
            if (Files.isRegularFile(path)) {
                files.add(path.toFile());
            }
        });

        byte[] patternBytes = pattern.getBytes();
        PatternSearchEngine searchEngine = new PatternSearchEngine();
        ExecutorService pool = Executors.newFixedThreadPool(4);

        List<Future<String>> results = new ArrayList<>();
        for (File file : files) {
            CheckFileTask task = new CheckFileTask(file, searchEngine, patternBytes, fileType);
            Future<String> result = pool.submit(task);
            results.add(result);
        }

        for (Future<String> result : results) {
            try {
                System.out.println(result.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        long elapsedTime = System.nanoTime() - startTime;
        System.out.println(formatTime(elapsedTime));
        pool.shutdown();
    }

    private static String formatTime(long nanos) {
        float milliseconds = (float) nanos / 1_000_000;
        return String.format("It took %.3f milliseconds", milliseconds);
    }
}
