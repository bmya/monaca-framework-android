package mobi.monaca.framework.template.ast;

import java.util.ArrayList;
import java.util.List;

import mobi.monaca.framework.template.ASTNodeVisitor;
import mobi.monaca.framework.template.Token;

/** This class represents path refer constant value. */
public class ConstantValueNode extends Node {

    protected Token<String> id;

    protected ArrayList<String> nameList;

    protected ConstantValueNode left = null;

    protected String name;

    public ConstantValueNode(Token<String> id) {
        this.id = id;
        name = id.getVal();
        nameList = new ArrayList<String>();
        nameList.add(name);
    }

    public ConstantValueNode(ConstantValueNode left, Token<String> id) {
        this.id = id;
        name = id.getVal();
        nameList = left.nameList;
        nameList.add(name);
    }

    public List<String> getNames() {
        return nameList;
    }

    @Override
    public <T> void accept(ASTNodeVisitor<T> visitor, T v) {
        visitor.visit(this, v);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (String name : getNames()) {
            builder.append(name + ".");
        }

        String result = builder.toString();
        return result.substring(0, result.length() - 1);
    }

}
