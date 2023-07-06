package sirup.cli.inputs;

public class Input {

    private final InputReader reader;

    public Input() {
        if (System.console() != null) {
            reader = new ConsoleReader();
        }
        else {
            reader = new SystemInReader();
        }
    }

    public Input(InputReader reader) {
        this.reader = reader;
    }

    public String readLine() {
        return reader.readLine();
    }

    public String readPassword() {
        return reader.readPassword();
    }
}
