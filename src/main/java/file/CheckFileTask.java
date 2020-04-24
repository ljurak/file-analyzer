package file;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.Callable;

public class CheckFileTask implements Callable<String> {

    private final File file;

    private final PatternSearchEngine searchEngine;

    public CheckFileTask(File file, PatternSearchEngine searchEngine) {
        this.file = file;
        this.searchEngine = searchEngine;
    }

    @Override
    public String call() throws Exception {
        byte[] fileBytes = Files.readAllBytes(file.toPath());
        List<Pattern> searchResult = searchEngine.searchPatterns(fileBytes);
        return file.getName() + ": " + (!searchResult.isEmpty() ? searchResult : "Unknown file type");
    }
}
