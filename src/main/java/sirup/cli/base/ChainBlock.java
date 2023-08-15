package sirup.cli.base;

public class ChainBlock {

    private Arguments arguments;
    private boolean hasExecuted;

    public ChainBlock(Arguments arguments, boolean hasExecuted) {
        this.arguments = arguments;
        this.hasExecuted = hasExecuted;
    }

    public ChainBlock elseWith(String flag, ValueCallback callback) {
        if (!hasExecuted && arguments.contains(flag)) {
            callback.call(arguments.get(flag));
            hasExecuted = true;
        }
        return this;
    }

    public ChainBlock elseWhen(String flag, Callback callback) {
        if (!hasExecuted && arguments.contains(flag)) {
            callback.call();
            hasExecuted = true;
        }
        return this;
    }

    public void elseDo(Callback callback) {
        if (!hasExecuted) {
            callback.call();
            hasExecuted = true;
        }
    }
}
