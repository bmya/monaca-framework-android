package mobi.monaca.framework.template.value;

/** This interface represents value used in template engine. */
public interface Value {

    /** Used for checking equality in executing template. */
    public boolean eq(Value val);

}
