package file;

public class Pattern {

    private final int priority;

    private final String pattern;

    private final String message;

    public Pattern(int priority, String pattern, String message) {
        this.priority = priority;
        this.pattern = pattern;
        this.message = message;
    }

    public int getPriority() {
        return priority;
    }

    public String getPattern() {
        return pattern;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return message;
    }
}
