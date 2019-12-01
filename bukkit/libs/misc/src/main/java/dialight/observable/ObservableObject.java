package dialight.observable;

import java.util.*;
import java.util.function.BiConsumer;

public class ObservableObject<V> {

    protected V value;
    private final Map<Object, BiConsumer<V, V>> onChange = new LinkedHashMap<>();

    public ObservableObject() {
        this.value = null;
    }
    public ObservableObject(V value) {
        this.value = value;
    }

    protected void fireChange(V fr, V to) {
        for(BiConsumer<V, V> op : onChange.values()) op.accept(fr, to);
    }

    public ObservableObject<V> onChange(Object key, BiConsumer<V, V> op) {
        onChange.put(key, op);
        return this;
    }
    public ObservableObject<V> removeListeners(Object key) {
        onChange.remove(key);
        return this;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        V oldValue = this.value;
        if(Objects.equals(value, oldValue)) return;
        this.value = value;
        fireChange(oldValue, value);
    }

    public ObservableObject<V> asImmutable() {
        return new ImmutableObservableObject<>(this);
    }

    @Override public String toString() {
        return this.value.toString();
    }

}
