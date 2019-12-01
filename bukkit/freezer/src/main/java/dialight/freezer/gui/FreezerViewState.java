package dialight.freezer.gui;

import dialight.freezer.Freezer;
import dialight.guilib.elements.NamedElement;
import dialight.guilib.elements.ReplaceableElement;
import dialight.misc.player.UuidPlayer;

public class FreezerViewState extends ReplaceableElement<NamedElement<UuidPlayer>> {

    public static void dumpThrow(UuidPlayer up, NamedElement<UuidPlayer> layout) {
        layout.dump();
        throw new RuntimeException("Somethings wrong");
    }

    private final FreezerSelectedElement selectedLayout;
    private final FreezerUnselectedElement unselectedLayout;
    private final FreezerAllElement allLayout;

    public FreezerViewState(Freezer proj) {
        selectedLayout = new FreezerSelectedElement(proj);
        unselectedLayout = new FreezerUnselectedElement(proj);
        allLayout = new FreezerAllElement(proj);

        setCurrent(allLayout);
    }

    public NamedElement<UuidPlayer> getAllLayout() {
        return allLayout;
    }

    public NamedElement<UuidPlayer> getUnselectedLayout() {
        return unselectedLayout;
    }

    public NamedElement<UuidPlayer> getSelectedLayout() {
        return selectedLayout;
    }

}
