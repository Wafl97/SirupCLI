package sirup.cli.base;

import org.reflections.Reflections;
import sirup.cli.annotations.*;
import sirup.cli.inputs.Input;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class SirupCli {

    private final Map<String, CallPair> methodMap;
    private record CallPair(Method method, CommandClass comClass) {}
    private final List<CliObject> cliObjects;
    private final String pack;
    private final Input input;
    private boolean skipHeader = false;
    private static PrintCallback goodbyeMessage = () -> System.out.println("Bye");
    private final Arguments arguments = new Arguments();

    //Security
    private LoginHandler loginHandler;
    private PrintCallback welcomeMessage = () -> System.out.println("Welcome");
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
        input = new Input();
        methodMap = new HashMap<>();
        cliObjects = new ArrayList<>();
        this.pack = pack;
    }

    public SirupCli addWelcomeMessage(PrintCallback printCallback) {
        this.welcomeMessage = printCallback;
        return this;
    }

    public SirupCli addWelcomeMessage(String message) {
        this.welcomeMessage = () -> System.out.println(message);
        return this;
    }

    public SirupCli addGoodbyeMessage(PrintCallback printCallback) {
        this.goodbyeMessage = printCallback;
        return this;
    }

    public SirupCli addGoodbyeMessage(String message) {
        this.goodbyeMessage = () -> System.out.println(message);
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

    private final String DIR = "sirup.cli";
    private void parseDefaultCommand() {
        Reflections reflections = new Reflections(DIR);
        _parseCommands(reflections, ActionsClass.class);
    }

    private void parseCliCommands() {
        Reflections reflections = new Reflections(pack);
        _parseCommands(reflections, ActionsClass.class);
    }

    private void parseDefaultSecureCommand() {
        Reflections reflections = new Reflections(DIR);
        _parseCommands(reflections, SecureActionsClass.class);
    }

    private void parseCliSecureCommands() {
        Reflections reflections = new Reflections(pack);
        _parseCommands(reflections, SecureActionsClass.class);
    }

    private void _parseCommands(Reflections reflections, Class<? extends Annotation> classAnnotation) {
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(classAnnotation);
        classes.forEach(clazz -> {
            try {
                CommandClass comClass = (CommandClass) clazz.getConstructors()[0].newInstance(null);
                comClass.setInput(input);
                comClass.setArguments(arguments);
                Method[] methods = clazz.getDeclaredMethods();
                for (Method method : methods) {
                    if (method.isAnnotationPresent(Command.class)) {
                        Command a = method.getAnnotation(Command.class);
                        Set<CliObject.CliArg> cliArgs = new HashSet<>();
                        methodMap.put(a.command(), new CallPair(method, comClass));
                        if (!a.alias().isEmpty()) {
                            methodMap.put(a.alias(), new CallPair(method, comClass));
                        }
                        if (method.isAnnotationPresent(Args.class)) {
                            for (Arg arg : method.getAnnotation(Args.class).value()) {
                                cliArgs.add(new CliObject.CliArg(arg.flag(), arg.arg(), arg.description()));
                            }
                        }
                        cliObjects.add(new CliObject(a.command(), a.alias(), a.description(), cliArgs));
                    }
                }
            } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
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

    public record CliObject(String command, String alias, String description, Set<CliArg> args) {
        public record CliArg(String flag, String arg, String description) {}
    }

    public interface LoginHandler {
        boolean login(Input input);
    }

    public interface PrintCallback {
        void print();
    }
}
