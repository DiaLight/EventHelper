package dialight.maingui;

import dialight.extensions.Colorizer;
import dialight.extensions.ItemStackBuilder;
import dialight.guilib.gui.Gui;
import dialight.guilib.indexcache.SparkIndexCache;
import dialight.guilib.layout.CachedPageLayout;
import dialight.guilib.layout.HorizontalMultiLayout;
import dialight.guilib.slot.Slot;
import dialight.guilib.slot.SlotClickEvent;
import dialight.guilib.slot.StaticSlot;
import dialight.guilib.view.View;
import dialight.modulelib.Module;
import dialight.observable.collection.ObservableCollection;
import dialight.toollib.Tool;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class MainGui implements Gui {

    private final MainGuiProject proj;
    private final CachedPageLayout<Tool> toolsDataFlow = new CachedPageLayout<>(new SparkIndexCache(7, 6));
    private final CachedPageLayout<Module> modulesDataFlow = new CachedPageLayout<>(new SparkIndexCache(7, 6));
    private final HorizontalMultiLayout dataFlow = new HorizontalMultiLayout(Arrays.asList(toolsDataFlow, modulesDataFlow));

    public MainGui(MainGuiProject proj) {
        this.proj = proj;

        ObservableCollection<Tool> tools = proj.getToollib().getTools();
        tools.onAdd(toolsDataFlow::add);
        tools.onRemove(toolsDataFlow::remove);
        tools.forEach(toolsDataFlow::add);

        ObservableCollection<Module> modules = proj.getModulelib().getModules();
        modules.onAdd(modulesDataFlow::add);
        modules.onRemove(modulesDataFlow::remove);
        modules.forEach(modulesDataFlow::add);

        toolsDataFlow.setSlotFunction(tool -> {
            Slot slot = proj.getToolSlot(tool.getId());
            if(slot != null) return slot;
            return new StaticSlot(new ItemStackBuilder(Material.ANVIL)
                    .displayName(tool.getId())
                    .lore(Colorizer.asList(
                            "|a|ЛКМ|y|: Получить инструмент"
                    ))
                    .build()) {
                @Override
                public void onClick(SlotClickEvent e) {
                    switch (e.getEvent().getClick()) {
                        case LEFT: {
                            e.getPlayer().closeInventory();
                            proj.getToollib().giveTool(e.getPlayer(), tool.getId());
                        } break;
                    }
                }
            };
        });
        modulesDataFlow.setSlotFunction(module -> {
            Slot slot = proj.getModuleSlot(module.getId());
            if(slot != null) return slot;
            return new StaticSlot(new ItemStackBuilder(Material.ANVIL)
                    .displayName(module.getId())
                    .lore(Colorizer.asList(
                            "|a|ЛКМ|y|: Вкл модуль"
                    ))
                    .build()) {
                @Override
                public void onClick(SlotClickEvent e) {
                    switch (e.getEvent().getClick()) {
                        case LEFT: {

                        } break;
                    }
                }
            };
        });
    }

    @Override
    public View createView(Player player) {
        return new MainGuiView(this, dataFlow, "Event Helper");
    }

}
