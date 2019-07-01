package dialight.guilib.layout;

import dialight.guilib.indexcache.IndexCache;
import dialight.guilib.slot.Slot;
import dialight.guilib.slot.SlotUsage;
import dialight.guilib.slot.Vec2i;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CachedPageLayout<T> extends DataLayout<T> {

    private static final class BackedSlot<T> implements SlotUsage {

        private final CachedPageLayout<T> layout;
        public final Slot slot;
        public final T data;

        public BackedSlot(CachedPageLayout<T> layout, Slot slot, T data) {
            this.layout = layout;
            this.slot = slot;
            this.data = data;
        }

        @Override public void update() {
            layout.update(this.data);
        }

    }

    private final int width;
    private final int height;
    private final List<BackedSlot<T>> slots = new ArrayList<>();
    private final IndexCache cache;

    public CachedPageLayout(IndexCache cache) {
        this.width = cache.getWidth();
        this.height = cache.getHeight();
        this.cache = cache;
    }

    @Override public int getWidth() {
        int page = width * height;
        int pages = (slots.size() + page - 1) / page;
        return pages * width;
    }
    @Override public int getHeight() {
        return height;
    }

    @Override public boolean add(@NotNull T data) {
        int index = slots.size();
        Vec2i loc = getLoc(index);
        Slot slot = slotFunction.apply(data);
        BackedSlot<T> backedSlot = new BackedSlot<>(this, slot, data);
        slots.add(backedSlot);
        slot.attached(backedSlot);
        fireUpdateSlot(loc.x, loc.y, slot);
        if(slots.size() % (width * height) == 1) {
            fireUpdateDataBounds(getWidth(), getHeight());
        }
        return true;
    }

    public int size() {
        return slots.size();
    }

    private BackedSlot<T> removeAtIndex(int index) {
        BackedSlot<T> backedSlot = slots.remove(index);
        for (int i = index; i < slots.size(); i++) {
            BackedSlot<T> cur = slots.get(i);
            Vec2i loc = getLoc(i);
            fireUpdateSlot(loc.x, loc.y, cur.slot);
        }
        Vec2i loc = getLoc(slots.size());
        fireUpdateSlot(loc.x, loc.y, null);
        backedSlot.slot.detached(backedSlot);
        if(slots.size() % (width * height) == 0) {
            fireUpdateDataBounds(getWidth(), getHeight());
        }
        return backedSlot;
    }

    @Override public boolean remove(@NotNull T data) {
        for (int i = 0; i < slots.size(); i++) {
            BackedSlot<T> backedSlot = slots.get(i);
            if(!data.equals(backedSlot.data)) continue;
            removeAtIndex(i);
            return true;
        }
        return false;
    }

    @Override
    public boolean update(@NotNull T data) {
        for (int i = 0; i < slots.size(); i++) {
            BackedSlot<T> backedSlot = slots.get(i);
            if(!data.equals(backedSlot.data)) continue;
            Vec2i loc = getLoc(i);
            fireUpdateSlot(loc.x, loc.y, backedSlot.slot);
            return true;
        }
        return false;
    }

    @Override public void clear() {
        for (int i = 0; i < slots.size(); i++) {
            Vec2i loc = getLoc(i);
            fireUpdateSlot(loc.x, loc.y, null);
        }
        slots.clear();
        fireUpdateDataBounds(getWidth(), getHeight());
    }

    @NotNull private Vec2i getLoc(int index) {
        int page = index / (width * height);
        int pindex = index % (width * height);
        Vec2i res = cache.getLoc(pindex);
        if(res == null) throw new RuntimeException("Can't resolve location for index " + index);
        return new Vec2i(res.x + page * width, res.y);
    }
    private int getBackedSlotIndex(int x, int y) {
        int page = x / width;
        int rx = x % width;
        int index = cache.getIndex(rx, y);
        if(index < 0) return -1;
        index += page * width * height;
        return index;
    }
    @Override public Slot getSlot(int x, int y) {
        int index = getBackedSlotIndex(x, y);
        if(index < 0) return null;
        if(index >= slots.size()) return null;
        BackedSlot<T> backedSlot = slots.get(index);
        if(backedSlot == null) return null;
        return backedSlot.slot;
    }
    public Slot getSlot(@NotNull T data) {
        for (BackedSlot<T> backedSlot : slots) {
            if (!data.equals(backedSlot.data)) continue;
            return backedSlot.slot;
        }
        return null;
    }

    @Override public T getData(int x, int y) {
        int index = getBackedSlotIndex(x, y);
        if(index < 0) return null;
        if(index >= slots.size()) return null;
        BackedSlot<T> backedSlot = slots.get(index);
        if(backedSlot == null) return null;
        return backedSlot.data;
    }

    @Nullable @Override public T remove(int x, int y) {
        int index = getBackedSlotIndex(x, y);
        if(index < 0) return null;
        if(index >= slots.size()) return null;
        BackedSlot<T> backedSlot = removeAtIndex(index);
        if(backedSlot == null) return null;
        return backedSlot.data;
    }

}
