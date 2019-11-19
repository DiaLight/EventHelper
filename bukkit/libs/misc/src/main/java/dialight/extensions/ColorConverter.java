package dialight.extensions;

import com.google.common.collect.ImmutableMap;
import dialight.compatibility.DyeColorBc;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;

import java.util.Map;

/**
 * Created by DiaLight on 28.02.2016.
 */
public class ColorConverter {

    public static final Color[] CHAT_COLORS = {
            Color.fromRGB(0, 0, 0),
            Color.fromRGB(0, 0, 170),
            Color.fromRGB(0, 170, 0),
            Color.fromRGB(0, 170, 170),
            Color.fromRGB(170, 0, 0),
            Color.fromRGB(170, 0, 170),
            Color.fromRGB(255, 170, 0),
            Color.fromRGB(170, 170, 170),
            Color.fromRGB(85, 85, 85),
            Color.fromRGB(85, 85, 255),
            Color.fromRGB(85, 255, 85),
            Color.fromRGB(85, 255, 255),
            Color.fromRGB(255, 85, 85),
            Color.fromRGB(255, 85, 255),
            Color.fromRGB(255, 255, 85),
            Color.fromRGB(255, 255, 255),
    };

    public static final Map<ChatColor, Color> CHAT_TO_COLOR = ImmutableMap.<ChatColor, Color>builder()
            .put(ChatColor.BLACK,       Color.fromRGB(0, 0, 0))
            .put(ChatColor.DARK_BLUE,   Color.fromRGB(0, 0, 170))
            .put(ChatColor.DARK_GREEN,  Color.fromRGB(0, 170, 0))
            .put(ChatColor.DARK_AQUA,   Color.fromRGB(0, 170, 170))
            .put(ChatColor.DARK_RED,    Color.fromRGB(170, 0, 0))
            .put(ChatColor.DARK_PURPLE, Color.fromRGB(170, 0, 170))
            .put(ChatColor.GOLD,        Color.fromRGB(255, 170, 0))
            .put(ChatColor.GRAY,        Color.fromRGB(170, 170, 170))
            .put(ChatColor.DARK_GRAY,   Color.fromRGB(85, 85, 85))
            .put(ChatColor.BLUE,        Color.fromRGB(85, 85, 255))
            .put(ChatColor.GREEN,       Color.fromRGB(85, 255, 85))
            .put(ChatColor.AQUA,        Color.fromRGB(85, 255, 255))
            .put(ChatColor.RED,         Color.fromRGB(255, 85, 85))
            .put(ChatColor.LIGHT_PURPLE, Color.fromRGB(255, 85, 255))
            .put(ChatColor.YELLOW,      Color.fromRGB(255, 255, 85))
            .put(ChatColor.WHITE,       Color.fromRGB(255, 255, 255))
            .build();

    @Deprecated public static Map<DyeColor, ChatColor> DYE_TO_CHAT = ImmutableMap.<DyeColor, ChatColor>builder()
            .put(DyeColor.WHITE,    ChatColor.WHITE)
            .put(DyeColor.BLACK,    ChatColor.BLACK)
            .put(DyeColor.BLUE,     ChatColor.DARK_BLUE)
            .put(DyeColor.BROWN,    ChatColor.DARK_GRAY)
            .put(DyeColor.CYAN,     ChatColor.AQUA)
            .put(DyeColor.GRAY,     ChatColor.GRAY)
            .put(DyeColor.GREEN,    ChatColor.DARK_GREEN)
            .put(DyeColor.LIGHT_BLUE, ChatColor.BLUE)
            .put(DyeColor.LIME,     ChatColor.GREEN)
            .put(DyeColor.MAGENTA,  ChatColor.RED)
            .put(DyeColor.ORANGE,   ChatColor.GOLD)
            .put(DyeColor.PINK,     ChatColor.LIGHT_PURPLE)
            .put(DyeColor.PURPLE,   ChatColor.DARK_PURPLE)
            .put(DyeColor.RED,      ChatColor.DARK_RED)
            .put(DyeColorBc.LIGHT_GRAY,   ChatColor.GRAY)
            .put(DyeColor.YELLOW,   ChatColor.YELLOW)
            .build();

    @Deprecated public static final Map<ChatColor, DyeColor> CHAT_TO_DYE = ImmutableMap.<ChatColor, DyeColor>builder()
            .put(ChatColor.WHITE,       DyeColor.WHITE)
            .put(ChatColor.BLACK,       DyeColor.BLACK)
            .put(ChatColor.DARK_BLUE,   DyeColor.BLUE)
            .put(ChatColor.DARK_GRAY,   DyeColor.GRAY)
            .put(ChatColor.GRAY,        DyeColorBc.LIGHT_GRAY)
            .put(ChatColor.DARK_RED,    DyeColor.BROWN)
            .put(ChatColor.RED,         DyeColor.RED)
            .put(ChatColor.GREEN,       DyeColor.LIME)
            .put(ChatColor.DARK_GREEN,  DyeColor.GREEN)
            .put(ChatColor.BLUE,        DyeColor.LIGHT_BLUE)
            .put(ChatColor.YELLOW,      DyeColor.YELLOW)
            .put(ChatColor.GOLD,        DyeColor.ORANGE)
            .put(ChatColor.AQUA,        DyeColor.CYAN)
            .put(ChatColor.DARK_AQUA,   DyeColor.CYAN)
            .put(ChatColor.LIGHT_PURPLE, DyeColor.MAGENTA)
            .put(ChatColor.DARK_PURPLE, DyeColor.PURPLE)
            .build();

    public static Color toLeatherColor(ChatColor chatColor) {
        if(chatColor == null) return Bukkit.getItemFactory().getDefaultLeatherColor();
        Color rgb = CHAT_TO_COLOR.get(chatColor);
        if(rgb == null) return Bukkit.getItemFactory().getDefaultLeatherColor();
        return rgb;
    }

    /**
     * @deprecated colors not matches
     */
    @Deprecated public static DyeColor toWoolColor(ChatColor chatColor) {
        if(chatColor == null) return DyeColor.WHITE;
        DyeColor dye = CHAT_TO_DYE.get(chatColor);
        if(dye == null) return DyeColor.WHITE;
        return dye;
    }

}
