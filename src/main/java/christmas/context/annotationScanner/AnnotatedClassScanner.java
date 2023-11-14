package christmas.context.annotationScanner;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.net.URL;
import java.util.*;

public class AnnotatedClassScanner {
    private static final String PACKAGE_DELIMITER = ".";
    private static final String DIRECTORY_DELIMITER = "/";
    private static final String CLASS_EXTENSION = ".class";
    private static final Set<Annotation> cache = new HashSet<>();

    public static List<Class<?>> scan(String basePackage, Class<? extends Annotation> annotation) {
        List<Class<?>> scannedClasses = new ArrayList<>();
        String path = basePackage.replace(PACKAGE_DELIMITER, DIRECTORY_DELIMITER);
        try {
            Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(path);

            while (resources.hasMoreElements()) {
                File packageDirectory = new File(resources.nextElement().getFile());
                scanPackage(scannedClasses, basePackage, packageDirectory, annotation);
            }

        } catch (ClassNotFoundException | IOException exception) {
            exception.printStackTrace();
        }
        cache.clear();
        return scannedClasses;
    }

    private static void scanPackage(
            List<Class<?>> scannedClasses,
            String basePackage,
            File packageDirectory,
            Class<? extends Annotation> annotation
    ) throws ClassNotFoundException {
        File[] files = packageDirectory.listFiles();
        if (files != null) {
            for (File file : files) {
                scanFile(scannedClasses, basePackage, file, annotation);
            }
        }
    }

    private static void scanFile(
            List<Class<?>> scannedClasses, String basePackage, File file, Class<? extends Annotation> annotation
    ) throws ClassNotFoundException {
        if (file.isDirectory()) {
            scanPackage(scannedClasses, basePackage + PACKAGE_DELIMITER + file.getName(), file, annotation);
            return;
        }
        if (file.isFile() && file.getName().endsWith(CLASS_EXTENSION)) {
            Class<?> clazz = Class.forName(basePackage + PACKAGE_DELIMITER + file.getName().replace(CLASS_EXTENSION, ""));
            if (clazz.isAnnotation()) {
                return;
            }
            if (hasAnnotation(clazz, annotation)) {
                scannedClasses.add(clazz);
            }
        }
    }

    private static boolean hasAnnotation(Class<?> target, Class<? extends Annotation> annotation) {
        for (Annotation declaredAnnotation : target.getDeclaredAnnotations()) {
            if (cache.contains(declaredAnnotation)) {
                return true;
            }
            return hasAnnotation(declaredAnnotation, annotation);
        }

        return false;
    }

    private static boolean hasAnnotation(Annotation target, Class<? extends Annotation> annotation) {
        if (target.annotationType().isAnnotationPresent(annotation)) {
            cache.add(target);
            return true;
        }

        for (Annotation declaredAnnotation : target.annotationType().getDeclaredAnnotations()) {
            if (hasAnnotation(declaredAnnotation, annotation)) {
                cache.add(declaredAnnotation);
                return true;
            }
        }

        return false;
    }
}
