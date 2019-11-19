package dialight.extensions;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

public class ActionInvoker {

    private final InvokerType invokerType;
    private final Object invoker;

    public ActionInvoker(OfflinePlayer invoker) {
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
                OfflinePlayer op = (OfflinePlayer) this.invoker;
                Player player = op.getPlayer();
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

                break;
        }
    }

    @Nullable public OfflinePlayer getPlayer() {
        if(invokerType != InvokerType.PLAYER) return null;
        return (OfflinePlayer) invoker;
    }

    @Nullable public Plugin getPlugin() {
        if(invokerType != InvokerType.PLUGIN) return null;
        return (Plugin) invoker;
    }

    @Nullable public ConsoleCommandSender getConsole() {
        if(invokerType != InvokerType.CONSOLE) return null;
        return (ConsoleCommandSender) invoker;
    }



}
