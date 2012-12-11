package mobi.monaca.framework.template;

import java.io.Reader;

import java.util.HashMap;
import java.util.HashSet;

import mobi.monaca.framework.template.ast.BlockStatementNode;
import mobi.monaca.framework.template.ast.EchoStatementNode;
import mobi.monaca.framework.template.ast.ExtendsStatementNode;
import mobi.monaca.framework.template.ast.IfStatementNode;
import mobi.monaca.framework.template.ast.IncludeStatementNode;
import mobi.monaca.framework.template.ast.Node;
import mobi.monaca.framework.template.ast.ParentStatementNode;
import mobi.monaca.framework.template.ast.RawStatementNode;
import mobi.monaca.framework.template.ast.StatementsNode;
import mobi.monaca.framework.template.ast.TopStatementsNode;

import mobi.monaca.framework.template.TemplateLexer;
import mobi.monaca.framework.template.TemplateParser;

/** This class represent template compiler from AST. */
public class TemplateCompiler {

    protected TemplateResource templateResourceMap;

    public TemplateCompiler(TemplateResource templateResourceMap) {
        this.templateResourceMap = templateResourceMap;
    }

    /** Compile a template from template resource path. */
    public Template compileFrom(String path, CompilerContext compilerContext) {

        if (compilerContext.templateMap.containsKey(path)) {
            return compilerContext.templateMap.get(path);
        }

        if (compilerContext.compilingMap.get(path) != null) {
            throw new CompilerError("Recursive dependency is detected.", path);
        }
        compilerContext.compilingMap.put(path, true);

        TopStatementsNode rootNode = parse(path, templateResourceMap.get(path));
        TemplateContext unit = new TemplateContext(path);

        // iterate AST.
        rootNode.accept(new CompilerVisitor(), unit);

        if (unit.hasParentStatement && !unit.hasExtendsStatement) {
            throw new CompilerError(
                    "You can't use parent statement in a template has no parent.",
                    path);
        }

        for (String dependOn : unit.dependency) {
            compilerContext.templateMap.put(dependOn,
                    compileFrom(dependOn, compilerContext));
        }

        compilerContext.templateMap.put(
                path,
                unit.parentPath == null ? new Template(path, rootNode,
                        unit.blockMap, compilerContext.templateMap)
                        : new Template(path, rootNode, unit.blockMap,
                                compilerContext.templateMap
                                        .get(unit.parentPath),
                                compilerContext.templateMap));

        compilerContext.compilingMap.put(path, null);

        return compilerContext.templateMap.get(path);
    }

    /** Compile to template object from template resource path. */
    public Template compileFrom(String path) {
        CompilerContext compilerContext = new CompilerContext();
        return compileFrom(path, compilerContext);
    }

    /** Create a compiler context object. */
    public CompilerContext createCompilerContext() {
        return new CompilerContext();
    }

    protected TopStatementsNode parse(String templatePath, Reader template) {
        try {
            return (TopStatementsNode) new TemplateParser(new TemplateLexer(
                    template, templatePath)).parse().value;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static public class CompilerContext {
        final protected HashMap<String, Boolean> compilingMap = new HashMap<String, Boolean>();
        final protected HashMap<String, Template> templateMap = new HashMap<String, Template>();
    }

    protected class TemplateContext {

        protected String templatePath;
        final protected HashMap<String, BlockStatementNode> blockMap = new HashMap<String, BlockStatementNode>();
        final protected HashSet<String> dependency = new HashSet<String>();
        protected String parentPath = null;

        // checks for template which has no parent using parent statement.
        protected boolean hasExtendsStatement = false;
        protected boolean hasParentStatement = false;

        /**
         * @param templatePath
         *            A compiling template's path.
         */
        public TemplateContext(String templatePath) {
            this.templatePath = templatePath;
        }

        /** Put a block node. */
        public void put(String name, BlockStatementNode block) {
            if (blockMap.containsKey(name)) {
                throw new CompilerError(
                        "Block name must be unique in a template: " + name,
                        templatePath);
            }

            blockMap.put(name, block);
        }

        /** Add a template which depend on. */
        public void addDependency(String path) {
            String resolvedPath = templateResourceMap.resolve(path,
                    templatePath);
            dependency.add(resolvedPath);
            if (!templateResourceMap.exists(resolvedPath)) {
                throw new CompilerError("The template resource not found: "
                        + path + ", resolved path: " + resolvedPath,
                        templatePath);
            }
        }

        /** Set a parent template path. */
        public void setParentPath(String path) {
            if (parentPath != null) {
                throw new CompilerError(
                        "Using parent statement must be one time.",
                        templatePath);
            }

            if (this.templatePath.equals(path)) {
                throw new CompilerError(
                        "Parent template must be other template.", templatePath);
            }

            addDependency(path);
            parentPath = templateResourceMap.resolve(path, templatePath);
        }

        public void setHasParentStatement() {
            hasParentStatement = true;
        }

        public void setHasExtendsStatement() {
            hasExtendsStatement = true;
        }
    }

    class CompilerVisitor extends StatementNodeVisitor<TemplateContext> {

        @Override
        public void visit(RawStatementNode node, TemplateContext v) {
        }

        @Override
        public void visit(EchoStatementNode node, TemplateContext v) {
        }

        @Override
        public void visit(IfStatementNode node, TemplateContext v) {
        }

        /** Treat "block" statement node. */
        @Override
        public void visit(BlockStatementNode node, TemplateContext v) {
            v.put(node.getBlockName(), node);
            node.getStatementsNode().accept(this, v);
        }

        /** Iterate statements. */
        @Override
        public void visit(StatementsNode node, TemplateContext v) {
            for (Node child : node) {
                child.accept(this, v);
            }
        }

        /** Iterate statements. */
        @Override
        public void visit(TopStatementsNode node, TemplateContext v) {
            visit((StatementsNode) node, v);
        }

        /** Compile "extends" statement. */
        @Override
        public void visit(ExtendsStatementNode node, TemplateContext v) {
            v.setHasExtendsStatement();
            v.addDependency(node.getPath());
            v.setParentPath(node.getPath());
        }

        @Override
        public void visit(IncludeStatementNode node, TemplateContext v) {
            v.addDependency(node.getPath());
        }

        @Override
        public void visit(ParentStatementNode node, TemplateContext v) {
            v.setHasParentStatement();
        }
    }

}
