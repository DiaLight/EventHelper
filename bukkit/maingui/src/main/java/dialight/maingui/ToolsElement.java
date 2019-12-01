package dialight.maingui;

import dialight.guilib.elements.CachedPageElement;
import dialight.guilib.indexcache.SparkIndexCache;
import dialight.observable.collection.ObservableCollection;
import dialight.toollib.Tool;

public class ToolsElement extends CachedPageElement<Tool> {

    private final MainGuiProject proj;

    public ToolsElement(MainGuiProject proj) {
        super(new SparkIndexCache(7, 6));
        this.proj = proj;
        setSlotFunction(tool -> proj.getToolSlot(tool.getId()));
    }

    @Override public void onViewersNotEmpty() {
//        System.out.println("ToolsLayout.onViewersNotEmpty");
        ObservableCollection<Tool> tools = proj.getToollib().getTools();
        tools.onAdd(this, this::add);
        tools.onRemove(this, this::remove);

        tools.forEach(this::add);
    }

    @Override public void onViewersEmpty() {
//        System.out.println("ToolsLayout.onViewersEmpty");
        ObservableCollection<Tool> tools = proj.getToollib().getTools();
        tools.removeListeners(this);

        proj.runTask(this::clear);
    }
}
