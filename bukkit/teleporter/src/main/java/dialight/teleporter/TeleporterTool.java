package dialight.teleporter;

import dialight.compatibility.PlayerInventoryBc;
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
    private final ItemStack item;

    public TeleporterTool(Teleporter proj) {
        super(ID);
        this.proj = proj;
        PluginDescriptionFile desc = proj.getPlugin().getDescription();
        this.item = new ItemStackBuilder(Material.STICK)
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
                .let(this::applyId)
                .build();
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

    @Override public void onClick(ToolInteractEvent e) {
        switch (e.getAction()) {
            case LEFT_CLICK: if(!e.isSneaking()) {
                SelectedPlayers selected = proj.getSelectedPlayers(e.getPlayer().getUniqueId());
                if(selected.isEmpty()) {
                    e.getPlayer().sendMessage(TeleporterMessages.noPlayersSelected);
                } else {
                    Location loc = e.lookingAtLoc();
                    List<OfflinePlayer> offline = new ArrayList<>();
                    List<Player> online = new ArrayList<>();
                    for (OfflinePlayer op : selected.toOfflinePlayers()) {
                        proj.teleport(op, loc);
                        if(op.isOnline()) {
                            Player trg = (Player) op;
                            online.add(trg);
                            if(!trg.getUniqueId().equals(e.getPlayer().getUniqueId())) {
                                trg.sendMessage(TeleporterMessages.YouHBTp(e.getPlayer().getName()));
                            }
                        } else {
                            offline.add(op);
                        }
                    }
                    e.getPlayer().sendMessage(TeleporterMessages.YouTp(online, offline));
                }
            } else {

            } break;
            case RIGHT_CLICK: if(!e.isSneaking()) {
                Server srv = proj.getPlugin().getServer();
                if (e.getTarget() == ToolInteractEvent.Target.ENTITY) {
                    ToolInteractEvent.Entity eevent = (ToolInteractEvent.Entity) e;
                    if (eevent.getEntity().getType() == EntityType.PLAYER) {
                        OfflinePlayer op = srv.getOfflinePlayer(((Player) eevent.getEntity()).getUniqueId());
                        toggleSelect(e.getPlayer(), op);
                    }
                } else {
                    Entity p2 = Utils.getEnByDirection(e.getPlayer(), 20.0, 1.5, EntityType.PLAYER, OfflineEntity.TYPE);
                    if (p2 == null) {
                        proj.getGuilib().openGui(e.getPlayer(), proj.getGui());
                    } else {
                        if(p2.getType() == EntityType.PLAYER) {
                            OfflinePlayer op = srv.getOfflinePlayer(((Player) p2).getUniqueId());
                            toggleSelect(e.getPlayer(), op);
                        } else if(p2.getType() == OfflineEntity.TYPE) {
                            OfflinePlayerEx opex = proj.getOfflinelib().getOfflinePlayerExByEntity(p2);
                            if(opex != null) {
                                OfflinePlayer op = srv.getOfflinePlayer(opex.getUniqueId());
                                toggleSelect(e.getPlayer(), op);
                            }
                        }
                    }
                }
            } else {
                SelectedPlayers selected = proj.getSelectedPlayers(e.getPlayer().getUniqueId());
                selected.clear();
                NamedLayout<OfflinePlayer> layout = proj.getGui().getOrCreateLayout(e.getPlayer()).getSelectedLayout();
                if(layout.getWidth() != 0) throw new RuntimeException("Somethings wrong");
                e.getPlayer().sendMessage(TeleporterMessages.AllPlayersRemoved);
            } break;
            case DROP: {
                MainGuiTool tool = proj.getToollib().get(MainGuiTool.class);
                if(tool != null) {
                    PlayerInventoryBc.of(e.getPlayer().getInventory()).setItemInMainHand(tool.createItem());
                }
                proj.getGuilib().clearStory(e.getPlayer());
            } break;
        }
    }

    public ItemStack createItem() {
        return item;
    }

}
