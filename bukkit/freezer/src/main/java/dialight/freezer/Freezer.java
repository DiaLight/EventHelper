package dialight.freezer;

import dialight.eventhelper.EventHelper;
import dialight.eventhelper.project.Project;
import dialight.eventhelper.project.ProjectApi;
import dialight.extensions.PlayerEx;
import dialight.freezer.gui.FreezerGui;
import dialight.guilib.GuiLibApi;
import dialight.maingui.MainGuiApi;
import dialight.observable.collection.ObservableCollection;
import dialight.observable.map.ObservableMap;
import dialight.observable.map.ObservableMapWrapper;
import dialight.offlinelib.OfflineLibApi;
import dialight.offlinelib.UuidPlayer;
import dialight.toollib.ToolLibApi;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.UUID;

public class Freezer extends Project {

    private MainGuiApi maingui;
    private ToolLibApi toollib;
    private OfflineLibApi offlinelib;
    private GuiLibApi guilib;


    private FreezerTool tool;
    private FreezerGui gui;
    private final ObservableMap<UuidPlayer, Frozen> frozenMap = new ObservableMapWrapper<>();
    private final ObservableCollection<Frozen> frozens = frozenMap.asCollectionObaervable(Frozen::getTarget);

    private FreezerListener freezerListener;
    private FreezeFlayers freezeFlayers;

    public Freezer(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public void enable(EventHelper eh) {
        maingui = eh.require("MainGui");
        toollib = eh.require("ToolLib");
        offlinelib = eh.require("OfflineLib");
        guilib = eh.require("GuiLib");

        tool = new FreezerTool(this);
        gui = new FreezerGui(this);

        maingui.registerToolItem(FreezerTool.ID, new FreezerSlot(this));
        toollib.register(tool);

        freezerListener = new FreezerListener(this);
        freezeFlayers = new FreezeFlayers(getPlugin());

        if(frozenMap.size() != 0) {
            freezerListener.enable();
            freezeFlayers.enable();
        }

        frozenMap.onPut(this, (uuid, frozen) -> {
            Player frozenPlayer = frozen.getTarget().getPlayer();
            if(frozenPlayer != null) {
                PlayerEx.of(frozenPlayer).teleport(frozen.getLocation());
            }
            if (frozen.isSelf()) {
                frozen.sendMessage(FreezerMessages.selfFreeze);
            } else {
                frozen.getInvoker().sendMessage(FreezerMessages.youFreezed(frozen.getTarget()));
                OfflinePlayer player = frozen.getInvoker().getPlayer();
                if(player != null) {
                    frozen.sendMessage(FreezerMessages.youHbFreezed(player));
                } else {
                    Plugin plugin = frozen.getInvoker().getPlugin();
                    if (plugin != null) {
                        frozen.sendMessage(FreezerMessages.youHbFreezed(plugin));
                    } else {
                        ConsoleCommandSender console = frozen.getInvoker().getConsole();
                        frozen.sendMessage(FreezerMessages.youHbFreezed(console));
                    }
                }
            }
            freezeFlayers.setFly(frozen.getTarget());
            if(frozenMap.size() == 1) {
                freezerListener.enable();
                freezeFlayers.enable();
            }
        });
        frozenMap.onRemove(this, (uuid, frozen) -> {
            if (frozen.isSelf()) {
                frozen.sendMessage(FreezerMessages.selfUnfreeze);
            } else {
                frozen.getInvoker().sendMessage(FreezerMessages.youUnfreezed(frozen.getTarget()));
                OfflinePlayer player = frozen.getInvoker().getPlayer();
                if(player != null) {
                    frozen.sendMessage(FreezerMessages.youHbUnfreezed(player));
                } else {
                    Plugin plugin = frozen.getInvoker().getPlugin();
                    if (plugin != null) {
                        frozen.sendMessage(FreezerMessages.youHbUnfreezed(plugin));
                    } else {
                        ConsoleCommandSender console = frozen.getInvoker().getConsole();
                        frozen.sendMessage(FreezerMessages.youHbUnfreezed(console));
                    }
                }
            }
            freezeFlayers.removeFly(frozen.getTarget());
            if(frozenMap.size() == 0) {
                freezerListener.disable();
                freezeFlayers.disable();
            }
        });
    }

    @Override public void disable() {
        frozenMap.removeListeners(this);
        freezerListener.disable();
        freezeFlayers.disable();
    }

    @Override
    public ProjectApi getApi() {
        return new FreezerApi(this);
    }

    public MainGuiApi getMaingui() {
        return maingui;
    }

    public ToolLibApi getToollib() {
        return toollib;
    }

    public OfflineLibApi getOfflinelib() {
        return offlinelib;
    }

    public GuiLibApi getGuilib() {
        return guilib;
    }

    public FreezerTool getTool() {
        return tool;
    }

    public void register(Frozen frozen) {
        frozenMap.put(frozen.getTarget(), frozen);
    }

    public Frozen unregister(UUID uuid) {
        return unregister(offlinelib.getUuidPlayer(uuid));
    }
    public Frozen unregister(UuidPlayer player) {
        Frozen frozen = frozenMap.remove(player);
        Vector velocity = frozen.getVelocity();
        if(velocity != null) player.setVelocity(velocity);
        return frozen;
    }

    public Frozen get(UUID uuid) {
        return get(offlinelib.getUuidPlayer(uuid));
    }
    public Frozen get(UuidPlayer player) {
        return frozenMap.get(player);
    }

    public ObservableCollection<Frozen> getFrozens() {
        return frozens;
    }

    public FreezerGui getGui() {
        return gui;
    }
}
