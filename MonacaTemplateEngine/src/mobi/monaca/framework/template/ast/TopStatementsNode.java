package mobi.monaca.framework.template.ast;

public class TopStatementsNode extends StatementsNode {

    public TopStatementsNode() {
    }

    public TopStatementsNode(TopStatementsNode left, Node right) {
        super(left, right);
    }
}
