package dialight.observable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class WriteProxyObservableObject<V> extends ObservableObject<V> {

    private final Map<Object, BiConsumer<V, V>> proxyOnChange = new LinkedHashMap<>();

    public WriteProxyObservableObject(ObservableObject<V> oobj) {
        super();
        oobj.onChange(this, this::fireOrigChange);
        this.value = oobj.getValue();
    }

    private void fireOrigChange(V fr, V to) {
        this.value = to;
        fireChange(fr, to);
    }

    protected void fireProxyChange(V fr, V to) {
        for(BiConsumer<V, V> op : proxyOnChange.values()) op.accept(fr, to);
    }

    public ObservableObject<V> onProxyChange(Object key, BiConsumer<V, V> op) {
        proxyOnChange.put(key, op);
        return this;
    }
    public ObservableObject<V> removeProxyListeners(Object key) {
        proxyOnChange.remove(key);
        return this;
    }

    public void setValue(V value) {
        fireProxyChange(this.value, value);
    }

}
