package dialight.stateengine;

import dialight.teams.captain.SortByCaptain;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class StateEngineHandler<T extends Enum> {

    protected final SortByCaptain proj;
    private final T state;
    private final Map<Object, Runnable> onDone = new LinkedHashMap<>();

    public StateEngineHandler(SortByCaptain proj, T state) {
        this.proj = proj;
        this.state = state;
    }

    public T getState() {
        return state;
    }

    public abstract void enter();
    public abstract void tick(int tick);
    public abstract void leave();
    public abstract void clear();

    protected void fireDone() {
        for(Runnable op : onDone.values()) op.run();
    }

    public void onDone(Object key, Runnable op) {
        onDone.put(key, op);
    }
    public void removeListeners(Object key) {
        onDone.remove(key);
    }

}
