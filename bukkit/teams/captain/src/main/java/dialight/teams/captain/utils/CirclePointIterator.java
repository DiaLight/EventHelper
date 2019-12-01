package dialight.teams.captain.utils;

import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;

public class CirclePointIterator<T> implements Iterable<PointVector<T>>, Iterator<PointVector<T>> {

    private final double dist;
    private final double r;
    private final Iterator<T> it;

    private double angle = .0;
    private int index = 0;
    private boolean mirror = false;
    private PointVector last = new PointVector(null);
    private boolean middle = true;

    public CirclePointIterator(Collection<T> objs, double dist, double minRadius) {
        this.dist = dist;
        this.r = Math.max(Math.ceil(objs.size() / Math.PI) * dist, minRadius);
        it = objs.iterator();
    }

    @NotNull @Override public Iterator<PointVector<T>> iterator() {
        return this;
    }

    private Vector Vector_align(Vector vec) {
        return new Vector(
                vec.getBlockX() + 0.5,
                vec.getBlockY(),
                vec.getBlockZ() + 0.5
        );
    }

    private PointVector<T> mirror(PointVector<T> st) {
        return new PointVector<T>(
                st.getIndex(),
                st.getLoc().clone().multiply(new Vector(1.0, 1.0, -1.0)).add(new Vector(.0, .0, 1.0)),
                st.getForward().clone().multiply(new Vector(1.0, 1.0, -1.0)),
                st.getRight(),
                st.getValue()
        );
    }

    @Override public boolean hasNext() {
        return it.hasNext();
    }

    @Override public PointVector<T> next() {
        if(mirror) {
            mirror = false;
            if(!middle) {
                PointVector mirror = mirror(last);
                mirror.setValue(it.next());
                return mirror;
            }
            middle = false;
        }

        double sideAngle = angle + (Math.PI / 2);
        Vector side = new Vector(
                Math.cos(sideAngle),
                0.0,
                Math.sin(sideAngle)
        );
        Vector forward = new Vector(
                Math.cos(angle),
                0.0,
                Math.sin(angle)
        );

        Vector pos = forward.clone().multiply(r);
        pos = Vector_align(pos);

        if (!middle) {
            if(index == 0) {
                pos = pos.clone().add(side.multiply(dist / 2));
            } else {
                pos = pos.clone().add(side.multiply(dist));
            }
            pos = Vector_align(pos);
        }

//        println("$index  $pos  ${angle.fmt}")
        PointVector<T> point = new PointVector<T>(index++, pos, forward, side, null);
        last = point;

        angle = Math.atan(pos.getZ() / pos.getX());
        mirror = true;
        point.setValue(it.next());
        return point;
    }

}
