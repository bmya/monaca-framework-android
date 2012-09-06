package mobi.monaca.framework.template.ast;

import mobi.monaca.framework.template.ASTNodeVisitor;

public abstract class Node {
    abstract public <T> void accept(ASTNodeVisitor<T> visitor, T v);
}
