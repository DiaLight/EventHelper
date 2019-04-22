package dialight.observable;

import java.util.*;
import java.util.function.BiConsumer;

class ObservableObject<V> {

    private V value;
    private final Collection<BiConsumer<V, V>> onChange = new LinkedList();

    public ObservableObject(V value) {
        this.value = value;
    }

    protected void fireChange(V fr, V to) {
        for(BiConsumer<V, V> op : onChange) op.accept(fr, to);
    }

    public ObservableObject<V> onChange(BiConsumer<V, V> op) {
        onChange.add(op);
        return this;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        V oldValue = this.value;
        this.value = value;
        fireChange(oldValue, value);
    }

}
