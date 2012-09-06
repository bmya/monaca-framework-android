package mobi.monaca.framework.template.ast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mobi.monaca.framework.template.Token;

public class StatementsNode extends Node implements Iterable<Node> {
    protected List<Node> nodes;

    public StatementsNode() {
        nodes = new ArrayList<Node>();
    }

    public StatementsNode(StatementsNode left, Node right) {
        this.nodes = left.nodes;
        this.nodes.add(right);
    }

    /** Build a node has RawStatement object from string literal token. */
    public static StatementsNode buildFrom(Token<String> strToken) {
        return new StatementsNode(new StatementsNode(), new RawStatementNode(
                strToken));
    }

    public <T> void accept(
            mobi.monaca.framework.template.ASTNodeVisitor<T> visitor, T v) {
        visitor.visit(this, v);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (int i = 0, len = nodes.size(); i < len; i++) {
            if (i != 0) {
                builder.append("\n");
            }
            builder.append(nodes.get(i).toString());
        }

        return builder.toString();
    }

    public int length() {
        return nodes.size();
    }

    @Override
    public Iterator<Node> iterator() {
        return nodes.iterator();
    }
}
