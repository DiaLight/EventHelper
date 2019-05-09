package dialight.maingui;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MainGuiCommand implements CommandExecutor {

    private final MainGuiProject proj;

    public MainGuiCommand(MainGuiProject proj) {
        this.proj = proj;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            this.proj.getGuilib().openGui(player, this.proj.getGui());
        }
        return false;
    }

}
