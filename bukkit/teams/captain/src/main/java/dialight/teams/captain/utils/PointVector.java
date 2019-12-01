package dialight.teams.captain.utils;

import org.bukkit.util.Vector;

public class PointVector<T> {

    private int index = 0;
    private Vector loc;
    private Vector forward;
    private Vector right;
    private Vector up;
    private T value;

    public PointVector(int index, Vector loc, Vector forward, Vector right, Vector up, T value) {
        this.index = index;
        this.loc = loc;
        this.forward = forward;
        this.right = right;
        this.up = up;
        this.value = value;
    }
    public PointVector(int index, Vector loc, Vector forward, Vector right, T value) {
        this(index, loc, forward, right, new Vector(0, 1, 0), value);
    }
    public PointVector(T value) {
        this(0, new Vector(), new Vector(), new Vector(), new Vector(0, 1, 0), value);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Vector getLoc() {
        return loc;
    }

    public void setLoc(Vector loc) {
        this.loc = loc;
    }

    public Vector getForward() {
        return forward;
    }

    public void setForward(Vector forward) {
        this.forward = forward;
    }

    public Vector getRight() {
        return right;
    }

    public void setRight(Vector right) {
        this.right = right;
    }

    public Vector getUp() {
        return up;
    }

    public void setUp(Vector up) {
        this.up = up;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
