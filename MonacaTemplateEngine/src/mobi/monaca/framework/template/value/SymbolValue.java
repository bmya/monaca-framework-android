package mobi.monaca.framework.template.value;

import java.util.HashMap;

/** This class represents symbol value in template engine. */
public class SymbolValue implements Value {

    protected String name;

    static private HashMap<String, SymbolValue> map = new HashMap<String, SymbolValue>();

    private SymbolValue(String name) {
        this.name = name;
    }

    public boolean eq(Value val) {
        return val == this;
    }

    @Override
    public String toString() {
        return "<symbol: " + name + ">";
    }

    static public SymbolValue get(String name) {
        if (!map.containsKey(name)) {
            map.put(name, new SymbolValue(name));
        }

        return map.get(name);
    }
}
