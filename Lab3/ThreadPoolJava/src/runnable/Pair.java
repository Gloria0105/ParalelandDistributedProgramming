package runnable;

public class Pair<Key, Value> {
    public Key t;
    public Value t1;

    public Pair(Key t, Value t1) {
        this.t = t;
        this.t1 = t1;
    }

    public Key getKey() {
        return t;
    }

    public void setKey(Key t) {
        this.t = t;
    }

    public Value getValue() {
        return t1;
    }

    public void setValue(Value t1) {
        this.t1 = t1;
    }
}
