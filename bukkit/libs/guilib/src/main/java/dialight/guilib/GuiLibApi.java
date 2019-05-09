package dialight.guilib;

import dialight.eventhelper.project.ProjectApi;
import dialight.guilib.gui.Gui;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GuiLibApi implements ProjectApi {

    private final GuiLib proj;

    public GuiLibApi(GuiLib proj) {
        this.proj = proj;
    }

    public void openGui(Player player, Gui gui) {
        proj.openGui(player, gui);
    }
    @Nullable public Gui openLast(Player player) {
        return proj.openLast(player);
    }
    public void clearStory(Player player) {
        proj.clearStory(player);
    }
    public List<Player> getViewers(Gui gui) {
        return proj.getViewers(gui);
    }

}
