package dialight.maingui;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MainGuiListener implements Listener {

    private static final class GetToolStatus {
        public int button1 = -1;
        public int button2 = -1;
        public int count = 0;
        public long lastTime = 0;

        public boolean add(int button) {
            if(button2 == -1) {
                button2 = button1;
                button1 = button;
                return true;
            }
            if(button == button2) return true;
            if(button == button1) return true;
            button2 = button1;
            button1 = button;
            return false;
        }

        public void clear() {
            button1 = -1;
            button2 = -1;
            count = 0;
        }

        public int min() {
            if(button1 < button2) return button1;
            return button2;
        }
    }

    private final MainGuiProject proj;
    private final Map<UUID, GetToolStatus> statusMap = new HashMap<>();

    public MainGuiListener(MainGuiProject proj) {
        this.proj = proj;
    }

    @NotNull private GetToolStatus getOrCreate(UUID uuid) {
        GetToolStatus status = statusMap.get(uuid);
        if(status != null) return status;
        status = new GetToolStatus();
        statusMap.put(uuid, status);
        return status;
    }

    @EventHandler
    public void onItemHeld(PlayerItemHeldEvent e) {
        Player player = e.getPlayer();
        if(player.getGameMode() != GameMode.CREATIVE) return;
        int button = e.getNewSlot();
        GetToolStatus status = getOrCreate(player.getUniqueId());

        long current = System.currentTimeMillis();
        long delta = current - status.lastTime;
        status.lastTime = current;
        if(delta > 500) {
            status.clear();
            status.add(button);
            return;
        }

        if(!status.add(button)) {
            status.clear();
            status.add(button);
            return;
        }

        status.count++;
        if(status.count >= 3) {
            status.count = 0;
            player.getInventory().setItem(status.min(), proj.getToollib().buildItem(MainGuiTool.ID));
        }
    }

}
