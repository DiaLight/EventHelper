package dialight.teleporter;

import dialight.extensions.Colorizer;
import dialight.extensions.ItemStackBuilder;
import dialight.extensions.OfflinePlayerEx;
import dialight.extensions.Utils;
import dialight.guilib.layout.NamedLayout;
import dialight.maingui.MainGuiTool;
import dialight.offlinelib.OfflineEntity;
import dialight.toollib.Tool;
import dialight.toollib.events.ToolInteractEvent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;

import java.util.*;

public class TeleporterTool extends Tool {

    public static final String ID = "teleporter";
    private final Teleporter proj;

    public TeleporterTool(Teleporter proj) {
        super(ID);
        this.proj = proj;
    }

    private void toggleSelect(Player player, OfflinePlayer op) {
        SelectedPlayers selected = proj.getSelectedPlayers(player.getUniqueId());
        if (selected.contains(op.getUniqueId())) {
            selected.remove(op.getUniqueId());
            player.sendMessage(TeleporterMessages.unselected(Collections.singletonList(op)));
        } else {
            selected.add(op.getUniqueId());
            player.sendMessage(TeleporterMessages.selected(Collections.singletonList(op)));
        }
        selected.sendStatus(player);
    }

    @Override public void onClick(ToolInteractEvent event) {
        switch (event.getAction()) {
            case LEFT_CLICK: if(!event.isSneaking()) {
                SelectedPlayers selected = proj.getSelectedPlayers(event.getPlayer().getUniqueId());
                if(selected.isEmpty()) {
                    event.getPlayer().sendMessage(TeleporterMessages.noPlayersSelected);
                } else {
                    Location loc = event.lookingAtLoc();
                    List<OfflinePlayer> offline = new ArrayList<>();
                    List<Player> online = new ArrayList<>();
                    for (OfflinePlayer op : selected.toOfflinePlayers()) {
                        proj.teleport(op, loc);
                        if(op.isOnline()) {
                            Player trg = (Player) op;
                            online.add(trg);
                            if(!trg.getUniqueId().equals(event.getPlayer().getUniqueId())) {
                                trg.sendMessage(TeleporterMessages.YouHBTp(event.getPlayer().getName()));
                            }
                        } else {
                            offline.add(op);
                        }
                    }
                    event.getPlayer().sendMessage(TeleporterMessages.YouTp(online, offline));
                }
            } else {

            } break;
            case RIGHT_CLICK: if(!event.isSneaking()) {
                Server srv = proj.getPlugin().getServer();
                if (event.getTarget() == ToolInteractEvent.Target.ENTITY) {
                    ToolInteractEvent.Entity eevent = (ToolInteractEvent.Entity) event;
                    if (eevent.getEntity().getType() == EntityType.PLAYER) {
                        OfflinePlayer op = srv.getOfflinePlayer(((Player) eevent.getEntity()).getUniqueId());
                        toggleSelect(event.getPlayer(), op);
                    }
                } else {
                    Entity p2 = Utils.getEnByDirection(event.getPlayer(), 20.0, 1.5, EntityType.PLAYER, OfflineEntity.TYPE);
                    if (p2 == null) {
                        proj.getGuilib().openGui(event.getPlayer(), proj.getGui());
                    } else {
                        if(p2.getType() == EntityType.PLAYER) {
                            OfflinePlayer op = srv.getOfflinePlayer(((Player) p2).getUniqueId());
                            toggleSelect(event.getPlayer(), op);
                        } else if(p2.getType() == OfflineEntity.TYPE) {
                            OfflinePlayerEx opex = proj.getOfflinelib().getOfflinePlayerExByEntity(p2);
                            if(opex != null) {
                                OfflinePlayer op = srv.getOfflinePlayer(opex.getUniqueId());
                                toggleSelect(event.getPlayer(), op);
                            }
                        }
                    }
                }
            } else {
                SelectedPlayers selected = proj.getSelectedPlayers(event.getPlayer().getUniqueId());
                selected.clear();
                NamedLayout<OfflinePlayer> layout = proj.getGui().getOrCreateLayout(event.getPlayer()).getSelectedLayout();
                if(layout.getWidth() != 0) throw new RuntimeException("Somethings wrong");
                event.getPlayer().sendMessage(TeleporterMessages.AllPlayersRemoved);
            } break;
            case DROP: {
                if(proj.getMaingui() != null) {
                    proj.getToollib().giveTool(event.getPlayer(), MainGuiTool.ID);
                }
                proj.getGuilib().clearStory(event.getPlayer());
            } break;
        }
    }

    @Override public ItemStack createItem() {
        PluginDescriptionFile desc = proj.getPlugin().getDescription();
        return new ItemStackBuilder(Material.STICK)
                .displayName(Colorizer.apply("|a|Телепорт игроков"))
                .lore(Colorizer.asList(
                        "|w|Телепортация",
                        "|a|ЛКМ|y|: телепортировать выбранных игроков",
                        "|w|Выбор игроков",
                        "|a|ПКМ|y|: открыть редактор выбранных игроков",
                        "|a|ПКМ по игроку*|y|: выбрать игрока",
                        "|y|*Игрока можно пометить на расстоянии.",
                        "|a|Shift|y|+|a|ПКМ|y|: очистить список выбранных",
                        "",
                        "|g|Плагин: |y|Телепорт",
                        "|g|Версия: |y|v" + desc.getVersion()
                ))
                .build();
    }

}
