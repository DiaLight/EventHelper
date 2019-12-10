package dialight.stateengine;

import dialight.misc.ActionInvoker;
import dialight.observable.ObservableObject;
import dialight.teams.captain.CaptainMessages;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class StateEngine<T extends Enum> {

    private final JavaPlugin plugin;
    private final T noneState;
    private final T entryState;
    private final Map<Object, Runnable> onStart = new LinkedHashMap<>();
    private final Map<Object, Runnable> onStop = new LinkedHashMap<>();
    private final Map<Object, Consumer<ActionInvoker>> onKill = new LinkedHashMap<>();
    private final Map<Object, Consumer<Throwable>> onError = new LinkedHashMap<>();
    private final Map<Object, Runnable> onCleanup = new LinkedHashMap<>();
    private final ObservableObject<StateEngineHandler<T>> handler;
    private final ObservableObject<StateEngineHandler<T>> handler_api;
    private final Map<T, StateEngineHandler<T>> handlers = new HashMap<>();

    private int tick = 0;
    private Runnable tickHandler = () -> {};
    private ActionInvoker invoker = null;
    private BukkitTask bukkitTask;

    public StateEngine(JavaPlugin plugin, StateEngineHandler<T> noneHandler, T entryState) {
        this.plugin = plugin;
        this.noneState = noneHandler.getState();
        this.entryState = entryState;
        this.handler = new ObservableObject<>(noneHandler);
        this.handler_api = handler.asImmutable();
    }

    public void addHandler(StateEngineHandler<T> handler) {
        if(this.handler.getValue() != null && !isNone()) throw new IllegalStateException("Can't add state while StateEngine is running");
        handlers.put(handler.getState(), handler);
        if(handler.getState() == noneState) {
            this.handler.setValue(handler);
        }
    }

    public void setHandler(T state) {
        StateEngineHandler<T> oldHandler = this.handler.getValue();
//        if(oldHandler.getState() == state) throw new IllegalStateException("state refers to self. " + oldHandler.getState() + " -> " + state);
        if(oldHandler.getState() == state) return;
        try {
            oldHandler.leave();
            if(state == noneState) {
                setNone();
                fireStop();
            } else {
                StateEngineHandler<T> handler = handlers.get(state);
                this.tick = 0;
                this.handler.setValue(handler);
                this.tickHandler = this::enterTick;
            }
        } catch (Throwable e) {
            e.printStackTrace();
            fireError(e);
            if(state != noneState) setNone();
        }
    }

    private void setNone() {
        try {
            StateEngineHandler<T> handler = handlers.get(noneState);
            this.handler.setValue(handler);
            handler.enter();
            this.invoker = null;
            fireCleanup();
            for (StateEngineHandler<T> hs : handlers.values()) {
                hs.clear();
            }
        } catch (Throwable e1) {
            e1.printStackTrace();
        }
        bukkitTask.cancel();
    }

    private void enterTick() {
        this.tickHandler = this::handleTick;
        handler.getValue().enter();  // can change this.tickHandler
    }
    private void handleTick() {
        this.handler.getValue().tick(tick);
        tick++;
    }
    private void tick() {
        try {
            this.tickHandler.run();
        } catch (Throwable e) {
            e.printStackTrace();
            fireError(e);
            if(handler.getValue().getState() != noneState) setNone();
        }
    }

    public boolean isNone() {
        return handler.getValue().getState() == noneState;
    }

    public void start(ActionInvoker invoker) {
        if(!isNone()) {
            invoker.sendMessage(CaptainMessages.alreadyStarted(handler.getValue().getState()));
            return;
        }
        this.invoker = invoker;

        fireStart();
        setHandler(entryState);
        bukkitTask = this.plugin.getServer().getScheduler().runTaskTimer(this.plugin, this::tick, 1, 1);
    }

    public void kill(ActionInvoker invoker) {
        if(isNone()) {
            invoker.sendMessage(CaptainMessages.notStarted);
            return;
        }
        fireKill(invoker);
        setNone();
    }

    public ObservableObject<StateEngineHandler<T>> getHandler() {
        return handler_api;
    }

    @Nullable
    public ActionInvoker getInvoker() {
        return invoker;
    }

    protected void fireStart() {
        for(Runnable op : onStart.values()) op.run();
    }
    public void onStart(Object key, Runnable op) {
        onStart.put(key, op);
    }

    protected void fireStop() {
        for(Runnable op : onStop.values()) op.run();
    }
    public void onStop(Object key, Runnable op) {
        onStop.put(key, op);
    }

    protected void fireKill(ActionInvoker invoker) {
        for(Consumer<ActionInvoker> op : onKill.values()) op.accept(invoker);
    }
    public void onKill(Object key, Consumer<ActionInvoker> op) {
        onKill.put(key, op);
    }

    protected void fireError(Throwable e) {
        for(Consumer<Throwable> op : onError.values()) op.accept(e);
    }
    public void onError(Object key, Consumer<Throwable> op) {
        onError.put(key, op);
    }

    protected void fireCleanup() {
        for(Runnable op : onCleanup.values()) op.run();
    }
    public void onCleanup(Object key, Runnable op) {
        onCleanup.put(key, op);
    }

    public void removeListeners(Object key) {
        onStart.remove(key);
        onStop.remove(key);
        onKill.remove(key);
        onError.remove(key);
    }

}
