package dialight.toollib;

import dialight.eventhelper.project.ProjectApi;
import dialight.observable.collection.ObservableCollection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ToolLibApi implements ProjectApi {

    private final ToolLib proj;

    public ToolLibApi(ToolLib proj) {
        this.proj = proj;
    }

    public ObservableCollection<Tool> getTools() {
        return proj.getTools();
    }

    public void giveTool(Player player, String id) {
        this.proj.giveTool(player, id);
    }

    @Nullable public ItemStack buildItem(String id) {
        return proj.buildItem(id);
    }
}
