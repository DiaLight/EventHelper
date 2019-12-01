package dialight.observable;

public class SilentWriteObservableObject<V> extends ObservableObject<V> {

    public SilentWriteObservableObject(V value) {
        super(value);
    }

    public SilentWriteObservableObject() {
        super();
    }

    public void silentSetValue(V value) {
        this.value = value;
    }

}
