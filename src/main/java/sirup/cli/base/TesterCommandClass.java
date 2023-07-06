package sirup.cli.base;

import sirup.cli.inputs.Input;

public abstract class TesterCommandClass extends CommandClass {

    public TesterCommandClass(Input input, Arguments arguments) {
        setArguments(arguments);
        setInput(input);
    }
}
