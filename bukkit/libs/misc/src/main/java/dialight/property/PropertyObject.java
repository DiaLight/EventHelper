package dialight.property;

import dialight.misc.ActionInvoker;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class PropertyObject<V> {

    public interface ChangeListener<V> {
        void accept(ActionInvoker invoker, V fr, V to);
    }

    protected V value;
    private final Map<Object, ChangeListener<V>> onChange = new LinkedHashMap<>();

    public PropertyObject() {
        this.value = null;
    }
    public PropertyObject(V value) {
        this.value = value;
    }

    protected void fireChange(ActionInvoker invoker, V fr, V to) {
        for(ChangeListener<V> op : onChange.values()) op.accept(invoker, fr, to);
    }

    public PropertyObject<V> onChange(Object key, ChangeListener<V> op) {
        onChange.put(key, op);
        return this;
    }
    public PropertyObject<V> removeListeners(Object key) {
        onChange.remove(key);
        return this;
    }

    public V getValue() {
        return value;
    }

    public void setValue(ActionInvoker invoker, V value) {
        V oldValue = this.value;
        if(Objects.equals(value, oldValue)) return;
        this.value = value;
        fireChange(invoker, oldValue, value);
    }

    public void silentSetValue(V value) {
        this.value = value;
    }

//    public ObservableObject<V> asImmutable() {
//        return new ImmutableObservableObject<>(this);
//    }

    @Override public String toString() {
        return this.value.toString();
    }

}
