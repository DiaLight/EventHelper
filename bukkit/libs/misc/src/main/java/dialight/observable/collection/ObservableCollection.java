package dialight.observable.collection;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class ObservableCollection<E> implements Collection<E> {

    private final Map<Object, Consumer<E>> onAdd = new LinkedHashMap<>();
    private final Map<Object, Consumer<E>> onRemove = new LinkedHashMap<>();

    protected void fireAdd(E e) {
        for(Consumer<E> op : onAdd.values()) {
            try {
                op.accept(e);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    protected void fireRemove(E e) {
        for(Consumer<E> op : onRemove.values()) {
            try {
                op.accept(e);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    public ObservableCollection<E> onAdd(Object key, Consumer<E> op) {
        onAdd.put(key, op);
        return this;
    }
    public ObservableCollection<E> onRemove(Object key, Consumer<E> op) {
        onRemove.put(key, op);
        return this;
    }

    public ObservableCollection<E> removeListeners(Object key) {
        onAdd.remove(key);
        onRemove.remove(key);
        return this;
    }

    public ObservableCollection<E> asImmutable() {
        return new ImmutableObservableCollection<>(this);
    }

}
