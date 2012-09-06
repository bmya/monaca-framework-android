package mobi.monaca.framework.template;

import java.util.HashMap;

import mobi.monaca.framework.template.ast.BlockStatementNode;
import mobi.monaca.framework.template.ast.TopStatementsNode;

/** This class represent compiled template. */
public class Template {

    /** A parent template */
    protected Template parent = null;

    protected HashMap<String, BlockStatementNode> blockMap;

    protected HashMap<String, Template> dependency;

    /** An implicit block which wrap template whole. */
    protected TopStatementsNode rootNode;

    protected String path;

    public Template(String path, TopStatementsNode rootNode,
            HashMap<String, BlockStatementNode> blockMap, Template parent,
            HashMap<String, Template> dependency) {
        this.path = path;
        this.rootNode = parent != null ? parent.rootNode : rootNode;
        this.parent = parent;
        this.blockMap = blockMap;
        this.dependency = new HashMap<String, Template>(dependency);
    }

    public Template(String path, TopStatementsNode rootNode,
            HashMap<String, BlockStatementNode> blockMap) {
        this(path, rootNode, blockMap, null, new HashMap<String, Template>());
    }

    public Template(String path, TopStatementsNode rootNode,
            HashMap<String, BlockStatementNode> blockMap,
            HashMap<String, Template> templateMap) {
        this(path, rootNode, blockMap, null, templateMap);
    }

    /** Get whether this template has parent template or not. */
    public boolean hasParent() {
        return parent != null;
    }

    /** Get a parent template. */
    public Template getParent() {
        return parent;
    }

    /** Search a block node from block name. */
    public BlockStatementNode getBlock(String blockName) {
        return blockMap.containsKey(blockName) ? blockMap.get(blockName)
                : parent != null ? parent.getBlock(blockName) : null;
    }

    /**
     * Get a root block node. If this template has parent, this method return
     * parent's root node.
     */
    public TopStatementsNode getRootTopStatementsNode() {
        return rootNode;
    }

    /** Get a template which this template depend on. */
    public Template getDependency(String path) {
        return dependency.containsKey(path) ? dependency.get(path) : null;
    }

    /** Get a template resource path. */
    public String getResourcePath() {
        return path;
    }

    @Override
    public String toString() {
        return path + (parent != null ? " < " + parent.toString() : "");
    }
}
