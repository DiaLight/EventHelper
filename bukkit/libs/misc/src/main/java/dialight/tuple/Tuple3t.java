package dialight.tuple;

public class Tuple3t<T1, T2, T3> {

    private final T1 t1;
    private final T2 t2;
    private final T3 t3;

    public Tuple3t(T1 t1, T2 t2, T3 t3) {
        this.t1 = t1;
        this.t2 = t2;
        this.t3 = t3;
    }

    public Tuple3t(T1 t1, Tuple2t<T2, T3> tuple) {
        this.t1 = t1;
        this.t2 = tuple.getT1();
        this.t3 = tuple.getT2();
    }

    public Tuple3t(Tuple2t<T1, T2> tuple, T3 t3) {
        this.t1 = tuple.getT1();
        this.t2 = tuple.getT2();
        this.t3 = t3;
    }

    public T1 getT1() {
        return t1;
    }

    public T2 getT2() {
        return t2;
    }

    public T3 getT3() {
        return t3;
    }

}
