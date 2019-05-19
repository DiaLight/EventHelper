package dialight.extensions;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by DiaLight on 16.03.2016.
 */
public class Colorizer_old {

    public static String apply(String format) {
        format = format.replaceAll("\\|r\\|", ChatColor.RED.toString());
        format = format.replaceAll("\\|g\\|", ChatColor.GREEN.toString());
        format = format.replaceAll("\\|b\\|", ChatColor.BLUE.toString());
        format = format.replaceAll("\\|a\\|", ChatColor.AQUA.toString());
        format = format.replaceAll("\\|y\\|", ChatColor.YELLOW.toString());
        format = format.replaceAll("\\|go\\|", ChatColor.GOLD.toString());
        format = format.replaceAll("\\|gr\\|", ChatColor.GRAY.toString());
        format = format.replaceAll("\\|dgr\\|", ChatColor.DARK_GRAY.toString());
        format = format.replaceAll("\\|w\\|", ChatColor.WHITE.toString());
        format = format.replaceAll("\\|_\\|", ChatColor.UNDERLINE.toString());
        format = format.replaceAll("\\|~\\|", ChatColor.STRIKETHROUGH.toString());
        format = format.replaceAll("\\|`\\|", ChatColor.RESET.toString());
        if (format.indexOf('|') != -1) {
            throw new IllegalStateException("Bad format: " + format);
        }
        return format;
    }

    public static List<String> asList(String... args) {
        ArrayList<String> ret = new ArrayList<>(args.length);
        for (String arg : args) {
            ret.add(apply(arg));
        }
        return ret;
    }

    public static String[] apply(String... args) {
        for (int i = 0; i < args.length; i++) {
            args[i] = apply(args[i]);
        }
        return args;
    }

    public static List<String> apply(List<String> lore) {
        ListIterator<String> iterator = lore.listIterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            iterator.set(apply(next));
        }
        return lore;
    }
}
