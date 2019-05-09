package dialight.guilib.slot;

public class Vec2i {

    public int x;
    public int y;
    public Vec2i(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vec2i locI = (Vec2i) o;
        if (x != locI.x) return false;
        return y == locI.y;

    }

    @Override
    public int hashCode() {
        return 31 * x + y;
    }

    @Override
    public String toString() {
        return "{" + x + ", " + y + "}";
    }
}
