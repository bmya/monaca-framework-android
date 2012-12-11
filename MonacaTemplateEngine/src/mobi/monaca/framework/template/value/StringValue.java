package mobi.monaca.framework.template.value;

public class StringValue implements Value {

    protected String str;

    public StringValue(String str) {
        this.str = str;
    }

    public boolean eq(Value val) {
        return val instanceof StringValue
                && ((StringValue) val).str.equals(str);
    }

    @Override
    public String toString() {
        return str;
    }

}
