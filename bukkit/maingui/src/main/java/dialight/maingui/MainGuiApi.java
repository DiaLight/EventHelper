package dialight.maingui;

import dialight.eventhelper.project.ProjectApi;
import dialight.guilib.slot.Slot;

public class MainGuiApi implements ProjectApi {

    private final MainGuiProject proj;

    public MainGuiApi(MainGuiProject proj) {
        this.proj = proj;
    }


    public void registerToolItem(String id, Slot item) {
        proj.registerToolSlot(id, item);
    }

    public void registerModuleItem(String id, Slot item) {
        proj.registerModuleSlot(id, item);
    }

}
