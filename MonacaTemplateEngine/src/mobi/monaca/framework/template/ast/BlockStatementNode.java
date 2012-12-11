package mobi.monaca.framework.template.ast;

import mobi.monaca.framework.template.Token;

public class BlockStatementNode extends Node {
    protected Token<String> id;
    protected StatementsNode statements;

    public BlockStatementNode(Token<String> id, StatementsNode statements) {
        this.id = id;
        this.statements = statements;
    }

    public String getBlockName() {
        return id.getVal();
    }

    public <T> void accept(
            mobi.monaca.framework.template.ASTNodeVisitor<T> visitor, T v) {
        visitor.visit(this, v);
    }

    public StatementsNode getStatementsNode() {
        return statements;
    }

    @Override
    public String toString() {
        return statements.length() > 0 ? "block:\n"
                + indent(statements.toString()) : "block:";
    }

    static public String indent(String str) {
        if (str == "") {
            return "";
        }

        StringBuilder builder = new StringBuilder();

        for (String line : str.split("\r\n|\n")) {
            builder.append("  " + line + "\n");
        }

        return builder.length() > 0 ? builder
                .substring(0, builder.length() - 1) : "";
    }
}
