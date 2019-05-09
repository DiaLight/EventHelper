package dialight.guilib.layout;

import dialight.guilib.slot.Slot;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class HorizontalMultiLayout extends SlotLayout {

    private final class CachedLayout implements LayoutListener {

        private final SlotLayout layout;
        private int width;
        private int x;

        public CachedLayout(SlotLayout layout) {
            this.layout = layout;
            this.x = 0;
            this.width = layout.getWidth();
        }

        @Override
        public void updateSlot(int x, int y, @Nullable Slot slot) {
            fireUpdateSlot(this.x + x, y, slot);
        }

        @Override
        public void updateDataBounds(int width, int height) {
            this.width = width;
            recalcDataBounds();
            fireUpdateDataBounds(getWidth(), getHeight());
        }

        @Override
        public void refresh() {
            fireRefresh();
        }

    }

    private final List<CachedLayout> layouts = new ArrayList<>();
    private int height;

    public HorizontalMultiLayout(List<SlotLayout> layouts) {
        for (SlotLayout layout : layouts) {
            this.layouts.add(new CachedLayout(layout));
        }
        recalcDataBounds();
    }

    private void recalcDataBounds() {
        int height = 0;
        int x = 0;
        for (CachedLayout cache : layouts) {
            cache.x = x;
            cache.width = cache.layout.getWidth();
            x += cache.width;
            int cur = cache.layout.getHeight();
            if(cur > height) height = cur;
        }
        this.height = height;
    }

    @Nullable @Override public Slot getSlot(int x, int y) {
        for (CachedLayout cache : layouts) {
            int width = cache.width;
            if(x < width) return cache.layout.getSlot(x, y);
            x -= width;
        }
        return null;
    }

    @Override public int getWidth() {
        int width = 0;
        for (CachedLayout cache : layouts) {
            width += cache.width;
        }
        return width;
    }

    @Override public int getHeight() {
        return height;
    }

    @Override public void subscribe(LayoutListener listener) {
        if(this.subscribed.isEmpty()) {
            for (CachedLayout cache : layouts) {
                cache.layout.subscribe(cache);
            }
            recalcDataBounds();
        }
        super.subscribe(listener);
    }

    @Override public void unsubscribe(LayoutListener listener) {
        super.unsubscribe(listener);
        if(this.subscribed.isEmpty()) {
            for (CachedLayout cache : layouts) {
                cache.layout.unsubscribe(cache);
            }
        }
    }

    public int getLayoutIndex(int x) {
        int i = 0;
        for (CachedLayout cache : layouts) {
            if(x >= cache.x && x < (cache.x + cache.width)) return i;
            i++;
        }
        return i - 1;
    }

}
