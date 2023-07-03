package sirup.cli.inputs;

import java.io.Console;

public class ConsoleReader implements InputReader {

    private Console console;

    public ConsoleReader() {
        console = System.console();
    }

    @Override
    public String readLine() {
        return console.readLine();
    }

    @Override
    public String readPassword() {
        return new String(console.readPassword());
    }
}
