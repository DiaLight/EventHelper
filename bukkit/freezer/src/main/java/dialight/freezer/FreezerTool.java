package dialight.freezer;

import dialight.compatibility.PlayerInventoryBc;
import dialight.extensions.*;
import dialight.maingui.MainGuiTool;
import dialight.misc.ActionInvoker;
import dialight.misc.Colorizer;
import dialight.misc.ItemStackBuilder;
import dialight.offlinelib.OfflineEntity;
import dialight.misc.player.UuidPlayer;
import dialight.toollib.Tool;
import dialight.toollib.events.ToolInteractEvent;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;

public class FreezerTool extends Tool {

    public static final String ID = "freezer";
    private final Freezer proj;
    private final ItemStack item;

    public FreezerTool(Freezer proj) {
        super(ID);
        this.proj = proj;
        PluginDescriptionFile desc = proj.getPlugin().getDescription();
        this.item = new ItemStackBuilder(Material.ICE)
                .displayName(Colorizer.apply("|a|Замораживатель игроков"))
                .lore(Colorizer.asList(
                        "|w|Выбор игроков",
                        "|a|ПКМ по игроку*|y|: заморозить/разморозить игрока",
                        "|y|*Игрока можно пометить на расстоянии.",
                        "|a|ПКМ|y|: открыть замораживатель",
//                        "|a|Shift|y|+|a|ПКМ|y|: добавить замороженных",
//                        "|y| в список телепортируемых",
                        "",
                        "|g|Плагин: |y|Замораживатель",
                        "|g|Версия: |y|v" + desc.getVersion()
                ))
                .let(this::applyId)
                .build();
    }

    private void toggleFreeze(ActionInvoker invoker, UuidPlayer target) {
        Frozen frozen = proj.get(target);
        if(frozen == null) {
            proj.register(new Frozen(target, target.getLocation(), invoker, "freezer tool action"));
        } else {
            proj.unregister(invoker, target);
        }
    }

    @Override
    public void onClick(ToolInteractEvent e) {
        switch (e.getAction()) {
            case LEFT_CLICK: if(!e.isSneaking()) {

            } else {

            } break;
            case RIGHT_CLICK: if(!e.isSneaking()) {
                ActionInvoker invoker = new ActionInvoker(proj.getOfflineLib().getUuidPlayer(e.getPlayer()));
                if (e.getTarget() == ToolInteractEvent.Target.ENTITY) {
                    ToolInteractEvent.Entity eevent = (ToolInteractEvent.Entity) e;
                    if (eevent.getEntity().getType() == EntityType.PLAYER) {
                        Player target = (Player) eevent.getEntity();
                        toggleFreeze(invoker, proj.getOfflineLib().getUuidPlayer(target));
                    }
                } else {
                    Entity p2 = LocationEx.of(e.getPlayer().getEyeLocation()).getEnByDirection(20.0, 1.5, EntityType.PLAYER, OfflineEntity.TYPE);
                    if (p2 == null) {
                        proj.getGuilib().openGui(e.getPlayer(), proj.getGui());
                    } else {
                        if(p2.getType() == EntityType.PLAYER) {
                            Player target = (Player) p2;
                            toggleFreeze(invoker, proj.getOfflineLib().getUuidPlayer(target));
                        } else if(p2.getType() == OfflineEntity.TYPE) {
                            UuidPlayer up = proj.getOfflineLib().getUuidPlayerExByEntity(p2);
                            if(up != null) {
                                toggleFreeze(invoker, up);
                            }
                        }
                    }
                }
            } else {

            } break;
            case DROP: {
                if(proj.getMaingui() != null) {
                    MainGuiTool tool = proj.getToollib().get(MainGuiTool.class);
                    if(tool != null) {
                        PlayerInventoryBc.of(e.getPlayer().getInventory()).setItemInMainHand(tool.createItem());
                    }
                }
                proj.getGuilib().clearStory(e.getPlayer());
            } break;
        }
    }

    @Override public ItemStack createItem() {
        return item;
    }

}
