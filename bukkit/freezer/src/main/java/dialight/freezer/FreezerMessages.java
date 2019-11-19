package dialight.freezer;

import dialight.offlinelib.UuidPlayer;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.plugin.Plugin;

/**
 * Created by DiaLight on 29.04.2016.
 */
public final class FreezerMessages {

    public static String selfFreeze = "ты заморозил сам себя";
    public static String selfUnfreeze = "ты разморозил сам себя";
    public static String unfreezeByReload = "Тебя разморозило из-за перезагрузки плагина";
    public static String youFrozen = "Ты находишься в замороженном состоянии";

    public static void unfreeze(HumanEntity invoker, HumanEntity trg) {
        if(invoker.getEntityId() == trg.getEntityId()) {
            invoker.sendMessage("ты разморозил сам себя");
        } else {
            trg.sendMessage("тебя разморозил " + invoker.getName());
            invoker.sendMessage("ты разморозил: " + trg.getName());
        }
    }

    public static String youFreezed(UuidPlayer target) {
        return "ты заморозил: " + target.getName();
    }
    public static String youUnfreezed(UuidPlayer target) {
        return "ты разморозил: " + target.getName();
    }

    public static String youHbFreezed(OfflinePlayer invoker) {
        return "тебя заморозил " + invoker.getName();
    }
    public static String youHbUnfreezed(OfflinePlayer invoker) {
        return "тебя разморозил " + invoker.getName();
    }

    public static String youHbFreezed(Plugin invoker) {
        return "Тебя заморозило плагином " + invoker.getName();
    }
    public static String youHbUnfreezed(Plugin invoker) {
        return "Тебя разморозило плагином " + invoker.getName();
    }

    public static String youHbFreezed(ConsoleCommandSender invoker) {
        return "Тебя заморозили из консоли";
    }
    public static String youHbUnfreezed(ConsoleCommandSender invoker) {
        return "Тебя разморозили из консоли";
    }

}
