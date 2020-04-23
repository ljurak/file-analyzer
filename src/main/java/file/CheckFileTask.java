package file;

import java.io.File;
import java.nio.file.Files;
import java.util.concurrent.Callable;

public class CheckFileTask implements Callable<String> {

    private final File file;

    private final PatternSearchEngine searchEngine;

    private final byte[] pattern;

    private final String message;

    public CheckFileTask(File file, PatternSearchEngine searchEngine, byte[] pattern, String message) {
        this.file = file;
        this.searchEngine = searchEngine;
        this.pattern = pattern;
        this.message = message;
    }

    @Override
    public String call() throws Exception {
        byte[] fileBytes = Files.readAllBytes(file.toPath());
        boolean searchResult = searchEngine.kmpSearch(fileBytes, pattern);
        return file.getName() + ": " + (searchResult ? message : "Unknown file type");
    }
}
