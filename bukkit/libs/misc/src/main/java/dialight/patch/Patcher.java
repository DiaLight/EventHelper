package dialight.patch;

import dialight.patch.asm.ClassNodeEx;
import org.bukkit.Bukkit;
import sun.misc.IOUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSource;
import java.security.SecureClassLoader;
import java.security.cert.Certificate;
import java.util.function.Predicate;

public class Patcher {

    public static boolean loadPatchClass(String className, Predicate<ClassNodeEx> patcher) {
        URLClassLoader classLoader = (URLClassLoader) Bukkit.getServer().getClass().getClassLoader();
        try {
            Method defineClass = SecureClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class, CodeSource.class);
            defineClass.setAccessible(true);

            URL resource = classLoader.findResource(className.replace('.', '/') + ".class");
            byte[] bytes = IOUtils.readFully(resource.openStream(), -1, true);
            ClassNodeEx node = ClassNodeEx.deserialize(bytes);
            if(patcher.test(node)) {
                bytes = node.serialize();
            }
            defineClass.invoke(classLoader, className, bytes, 0, bytes.length, new CodeSource(resource, (Certificate[]) null));
            return true;
        } catch (NoSuchMethodException | IOException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return false;
    }

}
