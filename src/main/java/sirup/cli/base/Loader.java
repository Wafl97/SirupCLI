package sirup.cli.base;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.stream.Collectors;

public class Loader {

    private ClassLoader classLoader;

    public Loader() {
        checkRuntime();
        //isRuntimeJAR();
    }

    public Set<Class<?>> load(String packageName) {
        //isRuntimeJAR(packageName);
        return loadClasses(loadClassPaths(packageName));
    }

    public Set<Class<?>> load(String packageName, Class annotation) {
        //isRuntimeJAR(packageName);
        if (inJar) {
            return loadJar(packageName)
                        .stream()
                        .filter(c -> c.isAnnotationPresent(annotation))
                        .collect(Collectors.toSet());
        }
        return load(packageName).stream()
                .filter(c -> c.isAnnotationPresent(annotation))
                .collect(Collectors.toSet());
    }

    private Set<String> loadClassPaths(String packageName) {
        InputStream inputStream = classLoader.getResourceAsStream(packageName.replaceAll("[.]","/"));
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        Set<String> classPaths = bufferedReader.lines()
                .filter(line -> !line.endsWith(".class"))
                .map(line -> packageName + "." + line)
                .collect(Collectors.toSet());
        classPaths.addAll(classPaths.stream()
                .map(this::loadClassPaths)
                .flatMap(Set::stream)
                .collect(Collectors.toSet()));
        classPaths.add(packageName);

        return classPaths;
    }

    private Set<Class<?>> loadClasses(Set<String> packages) {
        return packages.stream()
                .map(this::loadPackage)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }

    private Set<Class<?>> loadPackage(String packageName) {
        InputStream inputStream = classLoader.getResourceAsStream(packageName.replaceAll("[.]","/"));
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        return bufferedReader.lines()
                .filter(line -> line.endsWith(".class"))
                .map(line -> loadClass(packageName + "." + line))
                .collect(Collectors.toSet());
    }

    private Class<?> loadClass(String className) {
        try {
            return Class.forName(className.substring(0, className.lastIndexOf('.')), false, classLoader);
        } catch (ClassNotFoundException e) {
            System.out.println("Could not load class [" + className + "]");
        }
        return null;
    }

    private boolean inJar = false;

    /**
     * @deprecated this functionality is no longer needed and will be handled internally by the {@link Loader} class
     */
    @Deprecated(since = "2.1.2")
    private boolean isRuntimeJAR(String path) {
        File file = new File(path);
        System.out.println("path is dir : " + file.isDirectory());
        String loaderDir = Loader.class.getResource("Loader.class").toString();
        try {
            System.out.println(SirupCli.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return loaderDir.startsWith("jar");
    }

    private void checkRuntime() {
        String loaderDir = Loader.class.getResource("Loader.class").toString();
        try {
            System.out.println(SirupCli.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        if (loaderDir.startsWith("jar")) {
            inJar = true;
            System.out.println("Running in JAR");

            try {
                classLoader = makeJarClassLoader(SirupCli.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }

            return;
        }
        classLoader = ClassLoader.getSystemClassLoader();
    }

    private Set<Class<?>> jarClasses;

    private Set<Class<?>> loadJar(String packageName) {
        if (jarClasses != null) {
            return jarClasses.stream()
                    .filter(c -> c.getPackageName().startsWith(packageName))
                    .collect(Collectors.toSet());
        }
        jarClasses = new HashSet<>();
        for (String classPath : classPaths) {
            try {
                Class<?> c = Class.forName(classPath.replaceAll("/","."), false, classLoader);
                jarClasses.add(c);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return jarClasses.stream()
                .filter(c -> c.getPackageName().startsWith(packageName))
                .collect(Collectors.toSet());
    }

    private List<String> classPaths;

    private ClassLoader makeJarClassLoader(String jarPath) throws IOException {
        classPaths = new ArrayList<>();
        JarInputStream jarInputStream = new JarInputStream(new FileInputStream(jarPath));
        JarEntry jarEntry;
        while (true) {
            jarEntry = jarInputStream.getNextJarEntry();
            if (jarEntry == null) {
                break;
            }
            if (jarEntry.getName().endsWith(".class")) {
                String className = jarEntry.getName();
                className = className.substring(0, className.lastIndexOf("."));
                classPaths.add(className);
            }
        }
        List<File> files = classPaths.stream()
                .map(c -> new File("jar:file:" + jarPath + "!/" + c))
                .collect(Collectors.toList());

        URL[] urls = new URL[files.size()];
        for (int i = 0; i < urls.length; i++) {
            try {
                urls[i] = files.get(i).toURI().toURL();
            } catch (MalformedURLException e) {
                System.out.println("Failed to make URL " + files.get(i));
            }
        }
        return new URLClassLoader(urls, ClassLoader.getSystemClassLoader());
    }
}
