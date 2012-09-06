package mobi.monaca.framework.template.value;

public class TrueValue implements Value {

    final private static TrueValue instance = new TrueValue();

    private TrueValue() {
    }

    @Override
    public boolean eq(Value val) {
        return val instanceof TrueValue;
    }

    @Override
    public String toString() {
        return "<true>";
    }

    static public TrueValue getInstance() {
        return instance;
    }

}
