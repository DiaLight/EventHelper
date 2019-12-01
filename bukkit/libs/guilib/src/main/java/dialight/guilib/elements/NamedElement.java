package dialight.guilib.elements;

import dialight.guilib.slot.DataSlotUsage;
import dialight.guilib.slot.Slot;
import dialight.guilib.slot.Vec2i;
import dialight.tuple.Tuple2t;
import dialight.tuple.Tuple3t;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class NamedElement<T> extends DataElement<T> {

    protected static final class BackedSlot<T> {

        public char c = '_';
        public Slot slot = null;
        public DataSlotUsage usage = null;

        public void clear() {
            c = '_';
            slot = null;
            usage = null;
        }

        public void moveTo(BackedSlot<T> that) {
            that.c = this.c;
            that.slot = this.slot;
            that.usage = this.usage;
        }
    }

    protected static final class NamedBlock<T> {

        private final int height;
        public final List<BackedSlot<T>> relative = new ArrayList<>();
        public int x;

        public NamedBlock(int height, int x) {
            this.height = height;
            this.x = x;
        }

        public Vec2i tail() {
            return indexToPos(relative.size());
        }

        private int posToIndex(Vec2i pos) {
            return pos.y + (pos.x - this.x) * height;
        }

        private Vec2i indexToPos(int index) {
            return new Vec2i(this.x + index / height, index % height);
        }

        @Nullable public Tuple2t<Vec2i, BackedSlot<T>> findByData(@NotNull T data) {
            for (int i = 0; i < relative.size(); i++) {
                BackedSlot<T> backedSlot = relative.get(i);
                if(data.equals(backedSlot.usage.getData())) {
                    Vec2i pos = indexToPos(i);
                    return new Tuple2t<>(pos, backedSlot);
                }
            }
            return null;
        }

        @Nullable public BackedSlot<T> findByPos(Vec2i pos) {
            return relative.get(posToIndex(pos));
        }

        /**
         * @deprecated not implemented
         */
        @Deprecated public void insert(Vec2i pos, BackedSlot<T> backedSlot) {
            Vec2i tail = tail();
            if(pos.x > tail.x) throw new IllegalStateException();
            if(pos.x == tail.x && pos.y > tail.y) throw new IllegalStateException();
            else if(pos.y >= height) throw new IllegalStateException();
            relative.add(posToIndex(pos), backedSlot);
            throw new NotImplementedException();
        }

        public Vec2i add(BackedSlot<T> backedSlot) {
            int index = relative.size();
            relative.add(backedSlot);
            return indexToPos(index);
        }

        @NotNull public BackedSlot<T> removeAt(Vec2i pos) {
            int index = posToIndex(pos);
            for (int i = index; i < relative.size() - 1; i++) {
                BackedSlot<T> backedSlot = relative.get(i);
                BackedSlot<T> nextBackedSlot = relative.get(i + 1);
                nextBackedSlot.moveTo(backedSlot);
            }
            int lastIndex = relative.size() - 1;
            BackedSlot<T> removed = relative.remove(lastIndex);
            if(removed == null) throw new NullPointerException();
            return removed;
        }

        public void forEachFrom(Vec2i pos, BiConsumer<Vec2i, BackedSlot<T>> op) {
            forEachFrom(posToIndex(pos), op);
        }
        public void forEachFrom(int index, BiConsumer<Vec2i, BackedSlot<T>> op) {
            for (int i = index; i < relative.size(); i++) {
                BackedSlot<T> backedSlot = relative.get(i);
                op.accept(indexToPos(i), backedSlot);
            }
        }

        public int width() {
            return (relative.size() + height - 1) / height;
        }
    }

    private static final List<Character> VALID_CHARS = Arrays.asList(
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й',
            'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф',
            'х', 'ц', 'ч', 'ш', 'щ', 'ь', 'ы', 'ъ', 'э', 'ю', 'я'
    );

    private final int height;
    protected final TreeMap<Character, NamedBlock<T>> sorted = new TreeMap<>();
    private final List<BackedSlot<T>[]> backed = new ArrayList<>();

    public void dump() {
        for (Map.Entry<Character, NamedBlock<T>> entry : sorted.entrySet()) {
            String collect = entry.getValue().relative.stream().map(o -> nameFunction.apply((T) o.usage.getData())).collect(Collectors.joining(", "));
            System.out.println("" + entry.getKey() + ": " + collect);
        }
    }

    public NamedElement(int height) {
        this.height = height;
    }

    private char extractChar(@Nullable String name) {
        if(name == null) return '_';
        String nameLower = name.toLowerCase();
        for (int i = 0; i < nameLower.length(); i++) {
            char c = nameLower.charAt(i);
            if(VALID_CHARS.contains(c)) return c;
        }
        return '_';
    }

    @Override public int getWidth() {
        return backed.size();
    }
    @Override public int getHeight() {
        return height;
    }

    protected void updateBackedSlot(NamedBlock<T> block, Vec2i pos, @Nullable BackedSlot<T> backedSlot) {
        if(backedSlot == null) {
            fireUpdateSlot(pos.x, pos.y, null);
        } else {
            fireUpdateSlot(pos.x, pos.y, backedSlot.slot);
        }
    }

    @Nullable protected Tuple3t<NamedBlock<T>, Vec2i, BackedSlot<T>> findByData(@NotNull T data) {
        char c = extractChar(nameFunction.apply(data));
        NamedBlock<T> block = sorted.get(c);
        if(block == null) return null;
        Tuple2t<Vec2i, BackedSlot<T>> tuple = block.findByData(data);
        if(tuple == null) return null;
        return new Tuple3t<>(block, tuple);
    }

    private BackedSlot<T>[] insertCacheColumn(int x, char c) {
        BackedSlot<T>[] column = new BackedSlot[height];
        for (int i = 0; i < height; i++) {
            column[i] = new BackedSlot<>();
        }
        backed.add(x, column);
        for (Map.Entry<Character, NamedBlock<T>> entry : sorted.tailMap(c, false).entrySet()) {
            NamedBlock<T> tailBlock = entry.getValue();
            tailBlock.x++;
        }
        return column;
    }
    private BackedSlot<T>[] removeCacheColumn(int x, char c) {
        BackedSlot<T>[] removed = backed.get(x);
        backed.remove(x);
        for (Map.Entry<Character, NamedBlock<T>> entry : sorted.tailMap(c, false).entrySet()) {
            NamedBlock<T> tailBlock = entry.getValue();
            tailBlock.x--;
        }
        return removed;
    }

    @Override public boolean add(@NotNull T data) {
        Slot slot = slotFunction.apply(data);
        if(slot == null) return false;
        char c = extractChar(nameFunction.apply(data));
        NamedBlock<T> block = sorted.get(c);
        if(block == null) {
            int x = 0;
            Map.Entry<Character, NamedBlock<T>> entry = sorted.floorEntry(c);
            if (entry != null) {
                NamedBlock<T> prev = entry.getValue();
                x = prev.x + prev.width();
            }
            block = new NamedBlock<>(height, x);
            sorted.put(c, block);
        }
        Vec2i tail = block.tail();
        BackedSlot<T> backedSlot;
        if(tail.y == 0) {
            BackedSlot<T>[] column = insertCacheColumn(tail.x, c);

            backedSlot = column[tail.y];
            backedSlot.c = c;
            backedSlot.slot = slot;
            backedSlot.usage = new DataSlotUsage(this, data);
            backedSlot.slot.attached(backedSlot.usage);
            block.add(backedSlot);

            refreshAfter(c);
            fireUpdateDataBounds(getWidth(), getHeight());
        } else {
            BackedSlot<T>[] column = backed.get(tail.x);

            backedSlot = column[tail.y];
            backedSlot.c = c;
            backedSlot.slot = slot;
            backedSlot.usage = new DataSlotUsage(this, data);
            backedSlot.slot.attached(backedSlot.usage);
            block.add(backedSlot);

            updateBackedSlot(block, tail, backedSlot);
        }
        return true;
    }

    private void removeSlotAt(NamedBlock<T> block, Vec2i pos) {
        BackedSlot<T> removed = block.removeAt(pos);
        Vec2i tail = block.tail();
        block.forEachFrom(pos, (p, backedSlot) -> {
            updateBackedSlot(block, p, backedSlot);
        });
        updateBackedSlot(block, tail, null);
        removed.slot.detached(removed.usage);
        char c = removed.c;
        removed.clear();
        if(tail.y == 0) {
            removeCacheColumn(tail.x, c);
            refreshAfter(c);
            refreshTail();
            fireUpdateDataBounds(getWidth(), getHeight());
        }
    }

    @Nullable private BackedSlot<T> getBackedSlot(int x, int y) {
        if(x < 0 || y < 0) return null;
        if(x >= backed.size()) return null;
        BackedSlot<T>[] column = backed.get(x);
        if(y >= column.length) return null;
        return column[y];
    }

    @Override public boolean remove(@NotNull T data) {
        Tuple3t<NamedBlock<T>, Vec2i, BackedSlot<T>> tuple = findByData(data);
        if(tuple == null) return false;
        removeSlotAt(tuple.getT1(), tuple.getT2());
        return true;
    }

    @Override public boolean update(@NotNull T data) {
        Tuple3t<NamedBlock<T>, Vec2i, BackedSlot<T>> tuple = findByData(data);
        if(tuple == null) return false;
        Vec2i pos = tuple.getT2();
        BackedSlot<T> backedSlot = tuple.getT3();
        fireUpdateSlot(pos.x, pos.y, backedSlot.slot);
        return true;
    }

    @Override public void clear() {
        for (NamedBlock<T> namedBlock : sorted.values()) {
            for (int i = 0; i < namedBlock.relative.size(); i++) {
                Vec2i pos = namedBlock.indexToPos(i);
                fireUpdateSlot(pos.x, pos.y, null);
            }
            namedBlock.relative.clear();
        }
        sorted.clear();
        for (BackedSlot<T>[] backedSlots : backed) {
            for (BackedSlot<T> backedSlot : backedSlots) {
                if(backedSlot.slot != null) {
                    backedSlot.slot.detached(backedSlot.usage);
                }
                backedSlot.clear();
            }
        }
        backed.clear();
        fireUpdateDataBounds(getWidth(), getHeight());
    }

    private void refreshTail() {
        int x = backed.size();
        for (int y = 0; y < height; y++) {
            updateBackedSlot(null, new Vec2i(x, y), null);
        }
    }
    private void refreshAfter(char c) {
        for (Map.Entry<Character, NamedBlock<T>> entry : sorted.tailMap(c, true).entrySet()) {
            NamedBlock<T> namedBlock = entry.getValue();
            namedBlock.forEachFrom(0, (pos, backedSlot) -> {
                updateBackedSlot(namedBlock, pos, backedSlot);
            });
            Vec2i tail = namedBlock.tail();
            for (int y = tail.y; y < height; y++) {
                updateBackedSlot(namedBlock, new Vec2i(tail.x, y), null);
            }
        }
    }

    @Nullable @Override public Slot getSlot(int x, int y) {
        BackedSlot<T> backedSlot = getBackedSlot(x, y);
        if(backedSlot == null) return null;
        return backedSlot.slot;
    }

    @Nullable @Override public T getData(int x, int y) {
        BackedSlot<T> backedSlot = getBackedSlot(x, y);
        if(backedSlot == null) return null;
        return (T) backedSlot.usage.getData();
    }

    @Override public T remove(int x, int y) {
        BackedSlot<T> backedSlot = getBackedSlot(x, y);
        if(backedSlot == null) return null;
        T data = (T) backedSlot.usage.getData();
        if(data == null) return null;
        NamedBlock<T> block = sorted.get(backedSlot.c);
        Vec2i pos = new Vec2i(x, y);
        removeSlotAt(block, pos);
        return data;
    }

    @Override public int getSize() {
        int size = 0;
        for (NamedBlock<T> namedBlock : sorted.values()) {
            size += namedBlock.relative.size();
        }
        return size;
    }

    @NotNull public String buildColumnsHeader(int x, int width) {
        if(x < 0) return empty(width);
        if(x >= backed.size()) return empty(width);
        char c = 0;
        int end = x + width;
        StringBuilder sb = new StringBuilder();
        for (int i = x; i < end; i++) {
            if(i >= backed.size()) {
                sb.append(' ');
                continue;
            }
            BackedSlot<T>[] column = backed.get(i);
            char cur = column[0].c;
            if(c != cur) {
                c = cur;
                sb.append(c);
            } else {
                sb.append(' ');
            }
        }
        return sb.toString();
    }

    private String empty(int width) {
        char[] chars = new char[width];
        Arrays.fill(chars, ' ');
        return new String(chars);
    }

}
