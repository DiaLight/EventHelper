package dialight.maingui;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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

        public void press(int button) {
            if(button == button1) {
                reset();
                button1 = button;
                return;
            }
            if(button == button2) {
                count++;
            }
            button2 = button1;
            button1 = button;
        }

        public void reset() {
            button1 = -1;
            button2 = -1;
            count = 0;
        }

        public int min() {
            if(button1 < button2) return button1;
            return button2;
        }

        public boolean complete() {
            return count >= 3;
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
        if(!player.isOp()) return;
        int button = e.getNewSlot();
        GetToolStatus status = getOrCreate(player.getUniqueId());
        if(button != 0 && button != 2) {
            status.reset();
            return;
        }

        long current = System.currentTimeMillis();
        long delta = current - status.lastTime;
        status.lastTime = current;
        if(delta > 500) {
            status.reset();
            status.press(button);
            return;
        }
        status.press(button);

        if(status.complete()) {
            MainGuiTool tool = proj.getToollib().get(MainGuiTool.class);
            if(tool != null) {
                player.sendMessage(MainGuiMessages.toolSummoned);
                player.getInventory().setItem(status.min(), tool.createItem());
            }
            status.reset();
        }
    }

}
