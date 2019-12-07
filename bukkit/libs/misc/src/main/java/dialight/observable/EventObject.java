package dialight.observable;

import java.util.LinkedHashMap;
import java.util.Map;

public class EventObject {

    private final Map<Object, Runnable> onAction = new LinkedHashMap<>();

    protected void fireAction() {
        for(Runnable op : onAction.values()) op.run();
    }

    public EventObject onAction(Object key, Runnable op) {
        onAction.put(key, op);
        return this;
    }
    public EventObject removeListeners(Object key) {
        onAction.remove(key);
        return this;
    }

}
