package dialight.maingui;

import dialight.guilib.gui.Gui;
import dialight.guilib.layout.HorizontalMultiLayout;
import dialight.guilib.view.View;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class MainGui extends Gui {

    private final MainGuiProject proj;

    private final ToolsLayout toolsDataFlow;
    private final ModulesLayout modulesDataFlow;
    private final HorizontalMultiLayout dataFlow;

    private final MainGuiView view;

    public MainGui(MainGuiProject proj) {
        this.proj = proj;

        toolsDataFlow = new ToolsLayout(proj);
        modulesDataFlow = new ModulesLayout(proj);
        dataFlow = new HorizontalMultiLayout(Arrays.asList(toolsDataFlow, modulesDataFlow));

        this.view = new MainGuiView(this, dataFlow, "Event Helper");
    }

    @Override
    public View createView(Player player) {
        return view;
    }

}
