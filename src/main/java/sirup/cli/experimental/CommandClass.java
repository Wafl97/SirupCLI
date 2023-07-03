package sirup.cli.experimental;

import sirup.cli.base.Arguments;
import sirup.cli.inputs.Input;

public abstract class CommandClass {

    private Input input;
    private Arguments arguments;

    protected ChainBlock when(String flag, Callback callback) {
        if (arguments.contains(flag)) {
            callback.call();
            return new ChainBlock(arguments, true);
        }
        return new ChainBlock(arguments, false);
    }

    protected ChainBlock use(String flag, ValueCallback callback) {
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
