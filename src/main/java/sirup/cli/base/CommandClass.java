package sirup.cli.base;

import sirup.cli.inputs.Input;

public abstract class CommandClass {

    private Input input;
    private Arguments arguments;

    void setInput(Input input) {
         this.input = input;
    }

    void setArguments(Arguments arguments) {
         this.arguments = arguments;
    }

    protected ChainBlock when(String flag, Callback callback) {
        if (arguments.contains(flag)) {
            callback.call();
            return new ChainBlock(arguments, true);
        }
        return new ChainBlock(arguments, false);
    }

    protected ChainBlock with(String flag, ValueCallback callback) {
        if (arguments.contains(flag)) {
            callback.call(arguments.get(flag));
            return new ChainBlock(arguments, true);
        }
        return new ChainBlock(arguments, false);
    }

    protected String readLine() {
        return input.readLine();
    }

    protected String readPassword() {
        return input.readPassword();
    }
}
