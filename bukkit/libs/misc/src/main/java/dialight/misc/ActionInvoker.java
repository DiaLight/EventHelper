package dialight.misc;

import dialight.misc.player.UuidPlayer;
import org.bukkit.Location;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

public class ActionInvoker {

    private final InvokerType invokerType;
    private final Object invoker;

    public ActionInvoker(UuidPlayer invoker) {
        this.invokerType = InvokerType.PLAYER;
        this.invoker = invoker;
    }
    public ActionInvoker(Plugin invoker) {
        this.invokerType = InvokerType.PLUGIN;
        this.invoker = invoker;
    }
    public ActionInvoker(ConsoleCommandSender invoker) {
        this.invokerType = InvokerType.CONSOLE;
        this.invoker = invoker;
    }

    public void sendMessage(String msg) {
        switch (this.invokerType) {
            case PLAYER:
                UuidPlayer up = (UuidPlayer) this.invoker;
                Player player = up.getPlayer();
                if(player != null) {
                    player.sendMessage(msg);
                }
                break;
            case CONSOLE:
                ConsoleCommandSender ccs = (ConsoleCommandSender) this.invoker;
                ccs.sendMessage(msg);
                break;
            case PLUGIN:
                Plugin plugin = (Plugin) this.invoker;
                plugin.getServer().getConsoleSender().sendMessage(plugin.getName() + ": " + msg);
                break;
        }
    }

    @Nullable public Location getLocation() {
        if(invokerType != InvokerType.PLAYER) return null;
        return ((UuidPlayer) invoker).getLocation();
    }

    @Nullable public UuidPlayer getPlayer() {
        if(invokerType != InvokerType.PLAYER) return null;
        return (UuidPlayer) invoker;
    }

    @Nullable public Plugin getPlugin() {
        if(invokerType != InvokerType.PLUGIN) return null;
        return (Plugin) invoker;
    }

    @Nullable public ConsoleCommandSender getConsole() {
        if(invokerType != InvokerType.CONSOLE) return null;
        return (ConsoleCommandSender) invoker;
    }

    public InvokerType getType() {
        return invokerType;
    }

    public String getName() {
        switch (this.invokerType) {
            case PLAYER:
                UuidPlayer up = (UuidPlayer) this.invoker;
                return up.getName();
            case CONSOLE:
                ConsoleCommandSender ccs = (ConsoleCommandSender) this.invoker;
                return ccs.getName();
            case PLUGIN:
                Plugin plugin = (Plugin) this.invoker;
                return plugin.getName();
        }
        return invoker.toString();
    }

    @Override
    public String toString() {
        switch (this.invokerType) {
            case PLAYER:
                UuidPlayer up = (UuidPlayer) this.invoker;
                return "Player{" + up.getName() + "}";
            case CONSOLE:
                ConsoleCommandSender ccs = (ConsoleCommandSender) this.invoker;
                return "Console{" + ccs.getName() + "}";
            case PLUGIN:
                Plugin plugin = (Plugin) this.invoker;
                return "Plugin{" + plugin.getName() + "}";
        }
        return this.invokerType + "{" + invoker.toString() + "}";
    }
}
