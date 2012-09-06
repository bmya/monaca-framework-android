package mobi.monaca.framework.template.ast;

import java.util.ArrayList;
import java.util.List;

import mobi.monaca.framework.template.Token;

public class PipeModifiers extends Node {

    protected ArrayList<String> modifierIds = new ArrayList<String>();

    public PipeModifiers() {
    }

    public PipeModifiers(Token<String> left, PipeModifiers right) {
        modifierIds.add(left.getVal());
        for (String modifier : right.modifierIds) {
            modifierIds.add(modifier);
        }
    }

    public List<String> getModifierIds() {
        return modifierIds;
    }

    public <T> void accept(
            mobi.monaca.framework.template.ASTNodeVisitor<T> visitor, T v) {
        throw new RuntimeException(
                "This node is unneeded for AST node visitor.");
    }

}
