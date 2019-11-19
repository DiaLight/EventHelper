package dialight.maingui;

import dialight.guilib.gui.Gui;
import dialight.guilib.elements.HorizontalMultiElement;
import dialight.guilib.view.View;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class MainGui extends Gui {

    private final MainGuiProject proj;

    private final ToolsElement toolsDataFlow;
    private final ModulesElement modulesDataFlow;
    private final HorizontalMultiElement dataFlow;

    private final MainGuiView view;

    public MainGui(MainGuiProject proj) {
        this.proj = proj;

        toolsDataFlow = new ToolsElement(proj);
        modulesDataFlow = new ModulesElement(proj);
        dataFlow = new HorizontalMultiElement(Arrays.asList(toolsDataFlow, modulesDataFlow));

        this.view = new MainGuiView(this, dataFlow, "Event Helper");
    }

    @Override
    public View createView(Player player) {
        return view;
    }

}
