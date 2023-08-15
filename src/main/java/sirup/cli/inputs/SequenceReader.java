package sirup.cli.inputs;

import java.util.ArrayList;
import java.util.List;

public class SequenceReader implements InputReader {

    private final ArrayList<String> sequence;
    private int index = 0;

    public SequenceReader() {
        sequence = new ArrayList<>();
    }

    public SequenceReader(String ...lines) {
        sequence = new ArrayList<>();
        sequence.addAll(List.of(lines));
    }

    public void then(String line) {
        sequence.add(line);
    }

    @Override
    public String readLine() {
        return sequence.get(index++);
    }

    @Override
    public String readPassword() {
        return sequence.get(index++);
    }
}
