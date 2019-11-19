package dialight.guilib;

import dialight.guilib.gui.Gui;
import dialight.guilib.view.View;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by DiaLight on 28.03.2016.
 */
public class GuiStory {

    private static final boolean DEBUG = false;
    private final Map<UUID, Story> guiStory = new HashMap<>();

    @Nullable public Gui getCurrentGui(@NotNull Player player) {
        InventoryHolder holder = player.getOpenInventory().getTopInventory().getHolder();
        if(!(holder instanceof View)) return null;
        View view = (View) holder;
        return view.getGui();
    }

    public void addGui(@NotNull Player player, @NotNull Gui gui) {
        Story story = getOrCreate(player);
        Gui current = getCurrentGui(player);
        if(current == null) {
            story.stripTailTo(gui);
        } else {
            story.stripTailTo(current);
        }
        if(story.queue.peekLast() != gui) story.queue.addLast(gui);
        if(DEBUG) Bukkit.broadcastMessage("opened: " + toString(player));
    }

    public void stripTailTo(@NotNull Player player, @NotNull Gui gui) {
        Story story = guiStory.get(player.getUniqueId());
        if (story == null) return;
        story.stripTailTo(gui);
    }
    @Nullable public Gui getPrev(@NotNull Player player) {
        Story story = guiStory.get(player.getUniqueId());
        if (story == null) return null;
        Iterator<Gui> iterator = story.queue.descendingIterator();

        if (!iterator.hasNext()) return null;
        Gui current = iterator.next();

        if (!iterator.hasNext()) return null;
        Gui prev = iterator.next();

        return prev;
    }

    @Nullable public Gui getLast(@NotNull Player player) {
        Story story = guiStory.get(player.getUniqueId());
        if (story == null) return null;
        return story.queue.peekLast();
    }

    private String toString(@NotNull Player player) {
        Story story = guiStory.get(player.getUniqueId());
        return story == null ? "null" : toString(story.queue);
    }

    private String toString(Gui gui) {
        return gui.getClass().getSimpleName();
    }

    private String toString(Collection<Gui> story) {
        return story.stream().map(this::toString).collect(Collectors.joining(", ", "[", "]"));
    }

    public void clearStory(Player player) {
        Story story = guiStory.remove(player.getUniqueId());
    }

    private Story getOrCreate(Player player) {
        Story story = guiStory.get(player.getUniqueId());
        if (story == null) guiStory.put(player.getUniqueId(), (story = new Story()));
        return story;
    }

    private static class Story {

        public final Deque<Gui> queue = new LinkedList<>();

        public void stripTailTo(Gui gui) {
            Iterator<Gui> iterator = queue.descendingIterator();
            while (iterator.hasNext()) {
                Gui next = iterator.next();
                if (gui == next) break;
                iterator.remove();
            }
        }
    }
}
