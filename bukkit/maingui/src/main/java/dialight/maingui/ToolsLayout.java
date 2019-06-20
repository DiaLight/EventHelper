package dialight.maingui;

import dialight.compatibility.PlayerInventoryBc;
import dialight.extensions.Colorizer;
import dialight.extensions.ItemStackBuilder;
import dialight.guilib.indexcache.SparkIndexCache;
import dialight.guilib.layout.CachedPageLayout;
import dialight.guilib.slot.Slot;
import dialight.guilib.slot.SlotClickEvent;
import dialight.guilib.slot.StaticSlot;
import dialight.observable.collection.ObservableCollection;
import dialight.toollib.Tool;
import org.bukkit.Material;

public class ToolsLayout extends CachedPageLayout<Tool> {

    private final MainGuiProject proj;

    public ToolsLayout(MainGuiProject proj) {
        super(new SparkIndexCache(7, 6));
        this.proj = proj;

        setSlotFunction(tool -> {
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
                            PlayerInventoryBc.of(e.getPlayer().getInventory()).setItemInMainHand(tool.createItem());
                        } break;
                    }
                }
            };
        });
    }

    @Override public void onViewersNotEmpty() {
//        System.out.println("ToolsLayout.onViewersNotEmpty");
        ObservableCollection<Tool> tools = proj.getToollib().getTools();
        tools.onAdd(this::add);
        tools.onRemove(this::remove);

        tools.forEach(this::add);
    }

    @Override public void onViewersEmpty() {
//        System.out.println("ToolsLayout.onViewersEmpty");
        ObservableCollection<Tool> tools = proj.getToollib().getTools();
        tools.unregisterOnAdd(this::add);
        tools.unregisterOnRemove(this::remove);

        proj.runTask(this::clear);
    }
}
