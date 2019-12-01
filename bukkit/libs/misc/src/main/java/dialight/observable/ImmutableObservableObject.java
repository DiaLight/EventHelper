package dialight.observable;

public class ImmutableObservableObject<V> extends ObservableObject<V> {

    public ImmutableObservableObject(ObservableObject<V> oobj) {
        super();
        oobj.onChange(this, this::fireOrigChange);
        this.value = oobj.getValue();
    }

    private void fireOrigChange(V fr, V to) {
        this.value = to;
        fireChange(fr, to);
    }

    public void setValue(V value) {
        throw new UnsupportedOperationException();
    }

}
