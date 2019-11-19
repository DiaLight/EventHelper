package dialight.tuple;

public class Tuple2t<T1, T2> {

    private final T1 t1;
    private final T2 t2;

    public Tuple2t(T1 t1, T2 t2) {
        this.t1 = t1;
        this.t2 = t2;
    }

    public T1 getT1() {
        return t1;
    }

    public T2 getT2() {
        return t2;
    }

}
