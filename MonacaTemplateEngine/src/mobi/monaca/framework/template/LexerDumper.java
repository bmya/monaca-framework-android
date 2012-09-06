package mobi.monaca.framework.template;

import java.io.IOException;

import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.HashMap;

import java_cup.runtime.Symbol;
import mobi.monaca.framework.template.Symbols;
import mobi.monaca.framework.template.TemplateLexer;

/** This class dump template lexer's result. */
public class LexerDumper {

    protected static HashMap<Integer, String> dict = buildDict();

    protected static HashMap<Integer, String> buildDict() {
        HashMap<Integer, String> map = new HashMap<Integer, String>();

        for (Field field : Symbols.class.getFields()) {
            try {
                map.put(field.getInt(null), field.getName());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return map;
    }

    /** Get a symbol name. */
    public static String getSymbolName(Integer tokenSymbol) {
        if (!dict.containsKey(tokenSymbol)) {
            throw new RuntimeException("no such symbol: "
                    + tokenSymbol.toString());
        }
        return dict.get(tokenSymbol);
    }

    /** Get a symbol name. */
    public static String getSymbolName(Symbol symbol) {
        return dict.get(symbol.sym);
    }

    public static String dump(String template) throws IOException {
        TemplateLexer lexer = new TemplateLexer(new StringReader(template));
        StringBuilder builder = new StringBuilder();

        Symbol symbol;
        while ((symbol = lexer.next_token()).sym != Symbols.EOF) {
            builder.append(getSymbolName(symbol) + "\n");
        }

        return builder.length() > 0 ? builder
                .substring(0, builder.length() - 1) : "";
    }

}
