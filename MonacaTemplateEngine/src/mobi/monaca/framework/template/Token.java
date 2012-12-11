package mobi.monaca.framework.template;

public class Token<T> {
    protected int symbol, line;
    protected T val;

    public Token(int symbol, int line, T val) {
        this.symbol = symbol;
        this.line = line;
        this.val = val;
    }

    public T getVal() {
        return val;
    }

    public int getLine() {
        return line;
    }
}
