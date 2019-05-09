package dialight.guilib.layout;

import dialight.guilib.slot.Vec2i;
import dialight.tuple.Tuple3t;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class NamedSetLayout<T, H> extends NamedLayout<T> {

    private final HashMap<H, Tuple3t<NamedBlock<T>, Vec2i, BackedSlot<T>>> hashMap = new HashMap<>();

    private final Function<T, H> hashObjFunction;

    public NamedSetLayout(int height, Function<T, H> hashObjFunction) {
        super(height);
        this.hashObjFunction = hashObjFunction;
    }

    @Override
    public void dump() {
        for (Map.Entry<Character, NamedBlock<T>> entry : sorted.entrySet()) {
            StringBuilder sb = new StringBuilder();
            for (BackedSlot<T> slot : entry.getValue().relative) {
                sb.append(nameFunction.apply(slot.data));
                sb.append(":");
                H key = hashObjFunction.apply(slot.data);
                sb.append(key);
                sb.append(", ");
            }
            System.out.println("" + entry.getKey() + ": " + sb);
        }
        System.out.println(hashMap.keySet());
    }

    @Override
    protected void updateBackedSlot(NamedBlock<T> block, Vec2i pos, @Nullable BackedSlot<T> backedSlot) {
        if(backedSlot != null) {
            H key = hashObjFunction.apply(backedSlot.data);
            hashMap.put(key, new Tuple3t<>(block, pos, backedSlot));
        }
        super.updateBackedSlot(block, pos, backedSlot);
    }

    @Nullable @Override protected Tuple3t<NamedBlock<T>, Vec2i, BackedSlot<T>> findByData(@NotNull T data) {
        H key = hashObjFunction.apply(data);
        Tuple3t<NamedBlock<T>, Vec2i, BackedSlot<T>> tuple = hashMap.get(key);
        return tuple;
//        return super.findByData(data);
    }

    @Override
    public boolean add(@NotNull T data) {
        if(hashMap.containsKey(hashObjFunction.apply(data))) return false;
        return super.add(data);
    }

    @Override
    public boolean remove(@NotNull T data) {
        boolean result = super.remove(data);
        if(result) {
            H key = hashObjFunction.apply(data);
            hashMap.remove(key);
        }
        return result;
    }
}
