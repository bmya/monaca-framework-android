package mobi.monaca.framework.template.value;

public class FalseValue implements Value {

    final private static FalseValue instance = new FalseValue();

    private FalseValue() {
    }

    @Override
    public boolean eq(Value val) {
        return val instanceof FalseValue;
    }

    @Override
    public String toString() {
        return "<false>";
    }

    static public FalseValue getInstance() {
        return instance;
    }

}
