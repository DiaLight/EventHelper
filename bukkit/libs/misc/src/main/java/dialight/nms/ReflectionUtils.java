package dialight.nms;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ReflectionUtils {

    public static final String SERVER_VERSION;
    public static final int MAJOR_VERSION;
    public static final int MINOR_VERSION;
    public static final int PATCH_VERSION;

    static {
        String pkg = Bukkit.getServer().getClass().getName();
        String afterCB = pkg.substring(pkg.indexOf("craftbukkit.") + "craftbukkit.".length());
        SERVER_VERSION = afterCB.substring(0, afterCB.indexOf("."));

        String version = Bukkit.getVersion();
        String subver = version.substring(version.indexOf("MC: ") + "MC: ".length());
        subver = subver.replace(")", "");
        List<Integer> versions = Arrays.stream(subver.split("\\.")).map(Integer::parseInt).collect(Collectors.toList());
        MAJOR_VERSION = versions.get(0);
        MINOR_VERSION = versions.get(1);
        PATCH_VERSION = versions.size() > 2 ? versions.get(2) : 0;
    }

    public static int getMajorVersion() {
        String name = Bukkit.getVersion();

        name = name.substring(name.indexOf("MC: ") + "MC: ".length());
        name = name.replace(")", "");

        return Integer.parseInt(name.split("\\.")[0]);
    }

    @Nullable public static Class<?> getNMSClass(String name) {
        try {
            return Class.forName("net.minecraft.server." + SERVER_VERSION + "." + name);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Nullable public static Class<?> getCraftbukkitClass(String name) {
        try {
            return Class.forName("org.bukkit.craftbukkit." + SERVER_VERSION + "." + name);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Nullable public static Object invokeMethod(Object obj, String methodName, Class[] parameterClasses, Object... args) {
        return invokeMethod(obj.getClass(), obj, methodName, parameterClasses, args);
    }

    @Nullable public static Object invokeMethod(Object obj, String[] methodNames, Class[] parameterClasses, Object... args) {
        Class<?> clazz = obj.getClass();
        Method method = getMethod(clazz, methodNames, parameterClasses);
        return invokeMethod(method, obj, args);
    }

    @Nullable public static Object invokeMethod(Class<?> clazz, Object obj, String methodName, Class[] parameterClasses, Object... args) {
        Method method = getMethod(clazz, methodName, parameterClasses);
        try {
            return method.invoke(obj, args);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static Object invokeMethod(Method method, Object obj, Object... args) {
        try {
            return method.invoke(obj, args);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @SuppressWarnings("unused")
    public static void setFieldValue(Object obj, String name, Object value) {
        Class<?> clazz = obj.getClass();
        Field field = getField(clazz, name);
        if (!field.isAccessible()) field.setAccessible(true);
        try {
            field.set(obj, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Nullable public static Object getFieldValue(Object obj, String name) {
        Class<?> clazz = obj.getClass();
        Field field = getField(clazz, name);
        if (!field.isAccessible()) field.setAccessible(true);
        try {
            return field.get(obj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull public static <T> T newInstance(Constructor<? extends T> constructor, Object... arguments) {
        try {
            return constructor.newInstance(arguments);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull public static Object newInstance(Class<?> clazz, Class[] args, Object... params) {
        Constructor<?> constructor = getConstructor(clazz, args);
        if(!constructor.isAccessible()) constructor.setAccessible(true);
        return newInstance(constructor, params);
    }

    @NotNull public static Constructor<?> getConstructor(Class<?> clazz, Class[] args) {
        try {
            return clazz.getConstructor(args);
        } catch (NoSuchMethodException ignored) {}
        try {
            return clazz.getDeclaredConstructor(args);
        } catch (NoSuchMethodException ignored) {}
        String argsStr = Arrays.stream(args).map(Class::getSimpleName).collect(Collectors.joining(", ", "(", ")"));
        throw new NoSuchMethodError(clazz.getSimpleName() + argsStr);
    }

    @Nullable public static Method getMethodOrNull(Class<?> clazz, String name, Class<?>... params) {
        try {
            return clazz.getMethod(name, params);
        } catch (NoSuchMethodException ignored) {}
        try {
            return clazz.getDeclaredMethod(name, params);
        } catch (NoSuchMethodException ignored) {}
        return null;
    }
    @Nullable public static Method getMethodOrNull(Class<?> clazz, String[] methodNames, Class<?>... params) {
        for (String methodName : methodNames) {
            Method method = getMethodOrNull(clazz, methodName, params);
            if(method == null) continue;
            return method;
        }
        return null;
    }
    @NotNull public static Method getMethod(Class<?> clazz, String[] names, Class<?>... params) {
        Method method = getMethodOrNull(clazz, names, params);
        if(method != null) return method;
        return throwMethodNotFound(clazz, names, params);
    }
    @NotNull public static Method getMethod(Class<?> clazz, String name, Class<?>... params) {
        Method method = getMethodOrNull(clazz, name, params);
        if(method != null) return method;
        return throwMethodNotFound(clazz, name, params);
    }

    private static boolean arrayContentsEq(Object[] a1, Object[] a2) {
        if (a1 == null) return a2 == null || a2.length == 0;
        if (a2 == null) return a1.length == 0;
        if (a1.length != a2.length) return false;
        for (int i = 0; i < a1.length; i++) {
            if (a1[i] != a2[i]) return false;
        }
        return true;
    }
    public static List<Method> findMethods(Class<?> clazz, Class<?> ret, Class<?>... params) {
        ArrayList<Method> result = new ArrayList<>();
        for (Method method : clazz.getMethods()) {
            if(method.getReturnType() != ret) continue;
            if(!arrayContentsEq(method.getParameterTypes(), params)) continue;
            result.add(method);
        }
        return result;
    }

    @Nullable public static Method findFirstMethodOrNull(Class<?> clazz, Class<?> ret, Class<?>... params) {
        List<Method> methods = findMethods(clazz, ret, params);
        if(methods.isEmpty()) return null;
        return methods.get(0);
    }
    @NotNull public static Method findFirstMethod(Class<?> clazz, Class<?> ret, Class<?>... params) {
        List<Method> methods = findMethods(clazz, ret, params);
        if(methods.isEmpty()) throw new NoSuchMethodError(ret.getSimpleName() + " " + clazz.getSimpleName() + ".?" + Arrays.stream(params).map(Class::getSimpleName).collect(Collectors.joining(",", "(", ")")));
        return methods.get(0);
    }
    @NotNull public static Method findFirstMethod(Class<?> clazz, Class<?> ret, String[] names, Class<?>... params) {
        Method method = getMethodOrNull(clazz, names, params);
        if(method != null) return method;
        method = findFirstMethodOrNull(clazz, ret, params);
        if(method != null) return method;
        return throwMethodNotFound(clazz, ret, names, params);
    }

    @Nullable public static Method findSingleMethodOrNull(Class<?> clazz, Class<?> ret, Class<?>... params) {
        List<Method> methods = findMethods(clazz, ret, params);
        if(methods.isEmpty()) return null;
        if(methods.size() != 1) return null;
        return methods.get(0);
    }
    @NotNull public static Method findSingleMethod(Class<?> clazz, Class<?> ret, Class<?>... params) {
        List<Method> methods = findMethods(clazz, ret, params);
        if(methods.isEmpty()) throw new NoSuchMethodError(ret.getSimpleName() + " " + clazz.getSimpleName() + ".?" + Arrays.stream(params).map(Class::getSimpleName).collect(Collectors.joining(",", "(", ")")));
        if(methods.size() != 1) throw new NoSuchMethodError("there couple of matched methods: " + methods);
        return methods.get(0);
    }
    @NotNull public static Method findSingleMethod(Class<?> clazz, Class<?> ret, String[] names, Class<?>... params) {
        Method method = getMethodOrNull(clazz, names, params);
        if(method != null) return method;
        method = findSingleMethodOrNull(clazz, ret, params);
        if(method != null) return method;
        return throwMethodNotFound(clazz, ret, names, params);
    }

    public static Method findSingleDeclaredMethod(Class<?> clazz, Class<?> ret, Class<?>... params) {
        List<Method> methods = findDeclaredMethod(clazz, ret, params);
        if(methods.isEmpty()) throw new NoSuchMethodError();
        if(methods.size() != 1) throw new NoSuchMethodError("there couple of matched methods: " + methods);
        return methods.get(0);
    }
    public static List<Method> findDeclaredMethod(Class<?> clazz, Class<?> ret, Class<?>... params) {
        ArrayList<Method> result = new ArrayList<>();
        for (Method method : clazz.getDeclaredMethods()) {
            if(method.getReturnType() != ret) continue;
            if(!arrayContentsEq(method.getParameterTypes(), params)) continue;
            result.add(method);
        }
        return result;
    }

    @NotNull private static Field getField(Class<?> clazz, String name) {
        try {
            return clazz.getField(name);
        } catch (NoSuchFieldException ignored) {}
        try {
            return clazz.getDeclaredField(name);
        } catch (NoSuchFieldException ignored) {}
        throw new NoSuchFieldError("? " + clazz.getSimpleName() + "." + name);
    }

    public static String formatMethods(Class<?> clazz) {
        StringBuilder sb = new StringBuilder();
        sb.append(clazz.getName());sb.append(":\n");
        for (Method method : clazz.getMethods()) {
            sb.append("  ");
            sb.append(method.getReturnType().getSimpleName());
            sb.append(" ");
            sb.append(method.getName());
            sb.append(Arrays.stream(method.getParameterTypes()).map(Class::getSimpleName).collect(Collectors.joining(", ", "(", ")")));
            sb.append("\n");
        }
        return sb.toString();
    }

    private static Method throwMethodNotFound(Class<?> clazz, String name, Class<?>[] params) {
        String args = Arrays.stream(params).map(Class::getSimpleName).collect(Collectors.joining(", ", "(", ")"));
        String method = "? " + clazz.getSimpleName() + "." + name + args;
        System.err.println("NoSuchMethodError: " + method + "\n" + formatMethods(clazz));
        throw new NoSuchMethodError(method);
    }

    private static Method throwMethodNotFound(Class<?> clazz, String[] methodNames, Class<?>[] params) {
        String args = Arrays.stream(params).map(Class::getSimpleName).collect(Collectors.joining(", ", "(", ")"));
        String names = Arrays.stream(methodNames).collect(Collectors.joining("|", "[", "]"));
        String method = "? " + clazz.getSimpleName() + "." + names + args;
        System.err.println("NoSuchMethodError: " + method + "\n" + formatMethods(clazz));
        throw new NoSuchMethodError(method);
    }

    private static Method throwMethodNotFound(Class<?> clazz, Class<?> ret, String[] methodNames, Class<?>[] params) {
        String args = Arrays.stream(params).map(Class::getSimpleName).collect(Collectors.joining(", ", "(", ")"));
        String names = Arrays.stream(methodNames).collect(Collectors.joining("|", "[", "]"));
        String method = ret.getSimpleName() + " " + clazz.getSimpleName() + "." + names + args;
        System.err.println("NoSuchMethodError: " + method + "\n" + formatMethods(clazz));
        throw new NoSuchMethodError(method);
    }

}
