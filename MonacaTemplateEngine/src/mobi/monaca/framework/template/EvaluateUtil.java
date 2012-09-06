package mobi.monaca.framework.template;

import mobi.monaca.framework.template.value.FalseValue;
import mobi.monaca.framework.template.value.TrueValue;
import mobi.monaca.framework.template.value.Value;

public class EvaluateUtil {

    static public Value castToBoolean(Value val) {
        return val == FalseValue.getInstance() ? val : TrueValue.getInstance();
    }

    static public boolean canCastAsTrue(Value val) {
        return val != FalseValue.getInstance();
    }

}
