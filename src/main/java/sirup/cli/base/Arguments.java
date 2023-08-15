package sirup.cli.base;

import sirup.cli.exceptions.BadStringException;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Arguments {

    private final Map<String,String> argsMap;

    public Arguments() {
        this.argsMap = new HashMap<>();
        //buildArgMap(args);
    }

    public Chain ifContains(String flag, ArgsCallback callback) {
        if (argsMap.containsKey(flag)) {
            callback.exec();
            return new Chain(true, this.argsMap);
        }
        return new Chain(false, this.argsMap);
    }

    public Chain ifContainsAll(String[] flags, ArgsCallback callback) {
        boolean containsAll = true;
        for (String flag : flags) {
            containsAll &= argsMap.containsKey(flag);
        }
        if (containsAll) {
            callback.exec();
        }
        return new Chain(containsAll, this.argsMap);
    }

    public Chain ifContainsGet(String flag, ArgsValueCallback callback) {
        if (argsMap.containsKey(flag)) {
            callback.exec(argsMap.get(flag));
            return new Chain(true, this.argsMap);
        }
        return new Chain(false, this.argsMap);
    }

    public Chain ifContainsAllGet(String[] flags, ArgsValuesCallback callback) {
        boolean containsAll = argsMap.keySet().containsAll(List.of(flags));
        if (containsAll) {
            String[] values = new String[flags.length];
            for (int i = 0; i < flags.length; i++) {
                values[i] = argsMap.get(flags[i]);
            }
            callback.exec(values);
        }
        return new Chain(containsAll, this.argsMap);
    }

    public boolean contains(String flag) {
        return argsMap.containsKey("-" + flag);
    }

    public boolean containsAll(String ...flags) {
        for (String flag : flags) {
            if (!contains(flag))
                return false;
        }
        return true;
    }

    public String get(String flag) {
        return this.argsMap.get("-" + flag);
    }

    //Only for debugging
    public void printArgs() {
        argsMap.forEach((k,v) -> {
            System.out.println(k + " : " + v);
        });
    }

    public void rebuild(String[] inputArgs) {
        argsMap.clear();
        buildArgMap(inputArgs);
    }

    private void buildArgMap(String[] inputArgs) {
        for (int i = 1; i < inputArgs.length; i++) {
            if (inputArgs[i].startsWith("-")) {
                int j = i + 1;
                if (j < inputArgs.length && !inputArgs[j].startsWith("-")) {
                    if (inputArgs[j].startsWith("\"")) {
                        try {
                            getStringArg(inputArgs, inputArgs[i], j, "\"");
                        } catch (BadStringException e) {
                            System.out.println(e.getMessage() + " ... skipping arg");
                        }
                    }
                    else if (inputArgs[j].startsWith("'")) {
                        try {
                            getStringArg(inputArgs, inputArgs[i], j, "'");
                        } catch (BadStringException e) {
                            System.out.println(e.getMessage() + " ... skipping arg");
                        }
                    }
                    else {
                        this.argsMap.put(inputArgs[i], inputArgs[j]);
                    }
                    i++;
                    continue;
                }
                this.argsMap.put(inputArgs[i], "");
            }
        }
    }

    private void getStringArg(String[] inputArgs, String inputArg, int j, String q) {
        if (inputArgs[j].endsWith(q)) {
            this.argsMap.put(inputArg, inputArgs[j].replace(q,""));
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        try {
            do {
                stringBuilder.append(inputArgs[j]).append(" ");
                j++;
            } while (!inputArgs[j].endsWith(q));
            stringBuilder.append(inputArgs[j]);
            this.argsMap.put(inputArg, stringBuilder.toString().replace(q,""));
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new BadStringException("Could not parse argument for " + inputArg);
        }
    }

    public static class Chain {

        private boolean hasExecuted;
        private final Map<String,String> argsMap;

        private Chain(boolean hasExecuted, Map<String,String> argsMap) {
            this.hasExecuted = hasExecuted;
            this.argsMap = argsMap;
        }

        public Chain elseIfContains(String flag, ArgsCallback callback) {
            if (!hasExecuted && argsMap.containsKey(flag)) {
                callback.exec();
                hasExecuted = true;
            }
            return this;
        }

        public Chain elseIfContainsGet(String flag, ArgsValueCallback callback) {
            if (!hasExecuted && argsMap.containsKey(flag)) {
                callback.exec(argsMap.get(flag));
                hasExecuted = true;
            }
            return this;
        }

        public void elseDo(ArgsCallback callback) {
            if (!hasExecuted) {
                callback.exec();
                hasExecuted = true;
            }
        }
    }

    public interface ArgsCallback {
        void exec();
    }

    public interface ArgsValueCallback {
        void exec(String value);
    }

    public interface ArgsValuesCallback {
        void exec(String... values);
    }
}
