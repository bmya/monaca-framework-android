package mobi.monaca.framework.template.value;

import java.util.HashMap;
import java.util.List;

/** This class represent symbol table for constant. */
public class DictValue implements Value {

    protected HashMap<String, Value> map = new HashMap<String, Value>();

    @SuppressWarnings("serial")
    public class AlreadyDefinedException extends RuntimeException {
    }

    public void put(String key, Value val) {
        map.put(key, val);
    }

    public boolean has(String key) {
        return map.containsKey(key);
    }

    public Value get(String key) {
        if (!map.containsKey(key)) {
            return NullValue.getInstance();
        }
        return map.get(key);
    }

    @Override
    public boolean eq(Value val) {
        return this == val;
    }

    public Value get(List<String> keys) {
        Value v = this;
        for (String name : keys) {
            if (!(v instanceof DictValue)) {
                // TODO: fix this.
                throw new RuntimeException();
            }
            v = ((DictValue) v).get(name);
        }
        return v;
    }

    @Override
    public String toString() {
        return "<dict>";
    }

}
