package mobi.monaca.framework.template.value;

public class NullValue implements Value {

    private static NullValue instance = new NullValue();

    private NullValue() {
    }

    @Override
    public boolean eq(Value val) {
        return val instanceof NullValue;
    }

    static public NullValue getInstance() {
        return instance;
    }

    @Override
    public String toString() {
        return "<null>";
    }

}
