package sirup.cli.base;

//import org.reflections.Reflections;
import sirup.cli.annotations.*;
import sirup.cli.inputs.Input;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class SirupCli {

    //Internal
    private final Loader loader;

    private final Map<String, CallPair> methodMap;
    private record CallPair(Method method, CommandClass comClass) {}
    private final Set<CliObject> cliObjects;
    private final String pack;
    private final Input input;
    private boolean skipHeader = false;
    private static PrintCallback goodbyeMessage = () -> System.out.println("Bye");
    private final Arguments arguments = new Arguments();

    //Security
    private LoginHandler loginHandler;
    private static PrintCallback welcomeMessage = () -> System.out.println("Welcome");
    private static boolean loggedIn = false;
    public static void logout() {
        SirupCli.loggedIn = false;
    }

    public static void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows"))
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            else
                new ProcessBuilder("clear").inheritIO().start().waitFor();
        }
        catch (IOException | InterruptedException ignored) {}
    }

    public static void stop() {
        goodbyeMessage.print();
        System.exit(0);
    }

    public SirupCli(final String pack) {
        loader = new Loader();
        input = new Input();
        methodMap = new HashMap<>();
        cliObjects = new HashSet<>();
        this.pack = pack;
    }

    public SirupCli addWelcomeMessage(PrintCallback printCallback) {
        welcomeMessage = printCallback;
        return this;
    }

    public SirupCli addWelcomeMessage(String message) {
        welcomeMessage = () -> System.out.println(message);
        return this;
    }

    public SirupCli addGoodbyeMessage(PrintCallback printCallback) {
        goodbyeMessage = printCallback;
        return this;
    }

    public SirupCli addGoodbyeMessage(String message) {
        goodbyeMessage = () -> System.out.println(message);
        return this;
    }

    public SirupCli addLoginHandler(LoginHandler loginHandler) {
        this.loginHandler = loginHandler;
        return this;
    }

    public SirupCli skipHeader() {
        this.skipHeader = true;
        return this;
    }

    public void start() {
        if (!skipHeader) {
            printHeader();
        }
        parseDefaultCommand();
        parseCliCommands();
        if (this.methodMap.isEmpty()) {
            System.out.println("Found no commands, exiting program");
            return;
        }
        DefaultActions.setCliObjects(this.cliObjects);
        loggedIn = loginHandler == null;
        if (loginHandler != null) {
            runSecure();
        }
        else {
            runUnsecure();
        }
    }

    private void runSecure() {
        parseDefaultSecureCommand();
        parseCliSecureCommands();
        loggedIn = loginHandler.login(this.input);
        while (true) {
            while (!loggedIn) {
                loggedIn = loginHandler.login(this.input);
                if (!loggedIn) {
                    clearScreen();
                    System.out.println("Please try again");
                }
            }
            clearScreen();
            welcomeMessage.print();
            while (loggedIn) {
                runLoop();
            }
        }
    }

    private void runUnsecure() {
        while (true) {
            runLoop();
        }
    }

    private void runLoop() {
        System.out.print("Enter command: ");
        String inputString = input.readLine();
        String[] inputArgs = inputString.split(" ");
        if (inputArgs.length == 0) {
            return;
        }
        String command = inputArgs[0];
        CallPair pair = methodMap.get(command);
        if (pair == null || pair.method() == null || pair.comClass() == null) {
            System.out.println("Unknown command");
            return;
        }
        Method method = pair.method();
        CommandClass clazz = pair.comClass();
        try {
            arguments.rebuild(inputArgs);
            //Arguments arguments = new Arguments(inputArgs);
            //Object[] params = { input, arguments };
            method.invoke(clazz);
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void printHeader() {
        String header = """
                 _____ ___________ _   _______\s
                /  ___|_   _| ___ \\ | | | ___ \\
                \\ `--.  | | | |_/ / | | | |_/ /
                 `--. \\ | | |    /| | | |  __/\s
                /\\__/ /_| |_| |\\ \\| |_| | |   \s
                \\____/ \\___/\\_| \\_|\\___/\\_|   \s
                                              \s
                 by https://github.com/Wafl97""";
        System.out.println(header);
    }

    private final String DIR = SirupCli.class.getPackageName();
    private void parseDefaultCommand() {
        //Reflections reflections = new Reflections(DIR);
        _parseCommands(DIR, Commands.class);
    }

    private void parseCliCommands() {
        //Reflections reflections = new Reflections(pack);
        _parseCommands(pack, Commands.class);
    }

    private void parseDefaultSecureCommand() {
        //Reflections reflections = new Reflections(DIR);
        _parseCommands(DIR, SecureActionsClass.class);
    }

    private void parseCliSecureCommands() {
        //Reflections reflections = new Reflections(pack);
        _parseCommands(pack, SecureActionsClass.class);
    }

    private void _parseCommands(String dir, Class<? extends Annotation> classAnnotation) {
        System.out.println("Loading " + dir + ", checking for " + classAnnotation);
        Set<Class<?>> classes = loader.load(dir, classAnnotation); //reflections.getTypesAnnotatedWith(classAnnotation);
        if (classes.isEmpty()) {
            System.out.println("No classes found in " + dir + " with " + classAnnotation);
            return;
        }
        classes.forEach(clazz -> {
            System.out.println(clazz);
            try {
                CommandClass comClass = (CommandClass) clazz.getConstructors()[0].newInstance( null);
                comClass.setInput(input);
                comClass.setArguments(arguments);
                Method[] methods = clazz.getDeclaredMethods();
                for (Method method : methods) {
                    if (method.isAnnotationPresent(Command.class)) {
                        Command a = method.getAnnotation(Command.class);
                        Set<CliObject.CliArg> cliArgs = new HashSet<>();
                        Set<String> examples = new HashSet<>();
                        methodMap.put(a.command(), new CallPair(method, comClass));
                        if (!a.alias().isEmpty()) {
                            methodMap.put(a.alias(), new CallPair(method, comClass));
                        }
                        if (method.isAnnotationPresent(Args.class)) {
                            for (Arg arg : method.getAnnotation(Args.class).value()) {
                                cliArgs.add(new CliObject.CliArg(arg.flag(), arg.arg(), arg.description()));
                            }
                        }
                        if (method.isAnnotationPresent(Examples.class)) {
                            Example[] exampleArr = method.getAnnotation(Examples.class).value();
                            for (int i = exampleArr.length - 1; i >= 0; i--) {
                                examples.add(exampleArr[i].value());
                            }
                        }
                        else if (method.isAnnotationPresent(Example.class)) {
                            examples.add(method.getAnnotation(Example.class).value());
                        }
                        cliObjects.add(new CliObject(a.command(), a.alias(), a.description(), examples, cliArgs));
                    }
                }
            } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
                // TODO: make better logging
                e.printStackTrace();
            } catch (ClassCastException e) {
                System.err.println(clazz.getName() +  " must extend " + CommandClass.class.getName());
                System.err.println("Commands found in this class will not be available");
            } catch (IllegalArgumentException e) {
                System.err.println(clazz.getName() + " must have its first constructor take no arguments");
                System.err.println("Commands found in this class will not be available");
            } catch (ArrayIndexOutOfBoundsException e) {
                System.err.println(clazz.getName() + " must have a public constructor");
                System.err.println("Commands found in this class will not be available");
            }
        });
    }

    public record CliObject(String command, String alias, String description, Set<String> example, Set<CliArg> args) {
        public record CliArg(String flag, String arg, String description) {}
    }

    public interface LoginHandler {
        boolean login(Input input);
    }

    public interface PrintCallback {
        void print();
    }
}
