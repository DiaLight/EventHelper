package dialight.guilib.view.extensions;

import com.google.common.collect.ImmutableSet;
import dialight.guilib.gui.Gui;
import dialight.guilib.elements.NamedElement;
import dialight.guilib.elements.SlotElement;
import dialight.guilib.view.Scroll9x5View;
import dialight.nms.InventoryNms;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public abstract class NamedElementScroll9X5View<G extends Gui, L extends SlotElement> extends Scroll9x5View<G, L> {

    @Nullable private String emptyTitleReplace = null;

    public NamedElementScroll9X5View(G gui, L layout) {
        super(gui, layout, "");
    }

    @Override protected int calcLimit() {
        int limit = getLayout().getWidth() - getWidth();
        if(limit < 0) limit = 0;
        return limit;
    }

    public abstract NamedElement getNamedLayout();

    public void setEmptyTitleReplace(@Nullable String emptyTitleReplace) {
        this.emptyTitleReplace = emptyTitleReplace;
    }

    @Override protected void updateTitle() {
        NamedElement<?> layout = getNamedLayout();
        this.setTitle(layout.buildColumnsHeader(getOffset(), getWidth()));
    }

    @Override
    public void updateDataBounds(int width, int height) {
        super.updateDataBounds(width, height);
        updateTitle();
    }

    public String getUnicodeTitle() {
        boolean isEmpty = true;
        StringBuilder unicodeTitleBuilder = new StringBuilder();
        for (int i = 0; i < title.length(); i++) {
            char c = title.charAt(i);
            if(c != ' ') isEmpty = false;
            unicodeTitleBuilder.append("  ").append(c).append((i % 2 == 0) ? " " : "  ");
        }
        if(isEmpty) {
            if(this.emptyTitleReplace != null) return this.emptyTitleReplace;
            return "";
        }
        return unicodeTitleBuilder.toString();
    }
    public String getAsciiTitle() {
        boolean isEmpty = true;
        StringBuilder asciiTitleBuilder = new StringBuilder();
        for (int i = 0; i < title.length(); i++) {
            char c = title.charAt(i);
            if(c != ' ') isEmpty = false;
            asciiTitleBuilder.append((i % 4 == 3) ? "  " : " ").append(c).append("  ");
        }
        if(isEmpty) {
            if(this.emptyTitleReplace != null) return this.emptyTitleReplace;
            return "";
        }
        return asciiTitleBuilder.toString();
    }

    private static final Set<String> unicodeLocales = new ImmutableSet.Builder<String>()
            .add(
                    "ar_SA", "bg_BG", "el_GR", "en_CA",
                    "he_IL", "hy_AM", "ja_JP", "ka_GE",
                    "ko_KR", "ru_RU", "sr_SP", "th_TH",
                    "uk_UA", "zh_CN", "zh_TW"
            ).build();

    @Override public void setTitle(@NotNull String title) {
        if(this.title.equals(title)) return;
        this.title = title;
        String unicodeTitle = getUnicodeTitle();
        String asciiTitle = getAsciiTitle();
        for (Player viewer : getViewers()) {
            String playerTitle;
            if(unicodeLocales.contains(viewer.spigot().getLocale())) {
                playerTitle = unicodeTitle;
            } else {
                playerTitle = asciiTitle;
            }
            InventoryNms.sendInventoryTitle(viewer, getInventory(), playerTitle);
        }
    }

    @NotNull @Override public String getTitle(Player player) {
        if(unicodeLocales.contains(player.spigot().getLocale())) {
            return getUnicodeTitle();
        }
        return getAsciiTitle();
    }

}
