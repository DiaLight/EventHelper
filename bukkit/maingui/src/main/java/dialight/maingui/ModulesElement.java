package dialight.maingui;

import dialight.extensions.Colorizer;
import dialight.extensions.ItemStackBuilder;
import dialight.guilib.indexcache.SparkIndexCache;
import dialight.guilib.elements.CachedPageElement;
import dialight.guilib.slot.Slot;
import dialight.guilib.slot.SlotClickEvent;
import dialight.guilib.slot.StaticSlot;
import dialight.modulelib.Module;
import dialight.observable.collection.ObservableCollection;
import org.bukkit.Material;

public class ModulesElement extends CachedPageElement<Module> {

    private final MainGuiProject proj;

    public ModulesElement(MainGuiProject proj) {
        super(new SparkIndexCache(7, 6));
        this.proj = proj;

        setSlotFunction(module -> {
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

    @Override public void onViewersNotEmpty() {
//        System.out.println("ModulesLayout.onViewersNotEmpty");
        ObservableCollection<Module> modules = proj.getModulelib().getModules();
        modules.onAdd(this, this::add);
        modules.onRemove(this, this::remove);

        modules.forEach(this::add);
    }

    @Override public void onViewersEmpty() {
//        System.out.println("ModulesLayout.onViewersEmpty");
        ObservableCollection<Module> modules = proj.getModulelib().getModules();
        modules.removeListeners(this);

        proj.runTask(this::clear);
    }
}
