package mobi.monaca.framework.template.ast;

public class IfStatementNode extends Node {

    protected Node expression, elseStatement;
    protected StatementsNode statements;

    public IfStatementNode(Node expression, StatementsNode statements,
            Node elseStatement) {
        this.expression = expression;
        this.statements = statements;
        this.elseStatement = elseStatement;
    }

    public <T> void accept(
            mobi.monaca.framework.template.ASTNodeVisitor<T> visitor, T v) {
        visitor.visit(this, v);
    }

    public Node getExpression() {
        return expression;
    }

    public StatementsNode getStatements() {
        return statements;
    }

    public Node getElseStatement() {
        return elseStatement;
    }

}
