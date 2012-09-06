package mobi.monaca.framework.template;

import java.util.Stack;

import mobi.monaca.framework.template.ast.BinaryOperationNode;
import mobi.monaca.framework.template.ast.BlockStatementNode;
import mobi.monaca.framework.template.ast.ConstantValueNode;
import mobi.monaca.framework.template.ast.EchoStatementNode;
import mobi.monaca.framework.template.ast.ExtendsStatementNode;
import mobi.monaca.framework.template.ast.IfStatementNode;
import mobi.monaca.framework.template.ast.IncludeStatementNode;
import mobi.monaca.framework.template.ast.Node;
import mobi.monaca.framework.template.ast.ParentStatementNode;
import mobi.monaca.framework.template.ast.RawStatementNode;
import mobi.monaca.framework.template.ast.ShortCircuitBinaryOperationNode;
import mobi.monaca.framework.template.ast.StatementsNode;
import mobi.monaca.framework.template.ast.StringValueNode;
import mobi.monaca.framework.template.ast.TopStatementsNode;
import mobi.monaca.framework.template.ast.UnaryOperationNode;
import mobi.monaca.framework.template.model.MonacaApplicationInfo;
import mobi.monaca.framework.template.value.DictValue;
import mobi.monaca.framework.template.value.FalseValue;
import mobi.monaca.framework.template.value.StringValue;
import mobi.monaca.framework.template.value.SymbolValue;
import mobi.monaca.framework.template.value.TrueValue;
import mobi.monaca.framework.template.value.Value;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

/** This class provide to execute template object. */
public class TemplateExecuter {

    public interface Printer {
        public void print(String str);
    }

	private static final String TAG = TemplateExecuter.class.getSimpleName();

    protected DictValue constants;

    protected TemplateResource templateResource;
    
    protected MonacaApplicationInfo applicationInfo;

    protected TemplateExecuter(DictValue constants,
            TemplateResource templateResrouce) {
        this.constants = constants;
        this.templateResource = templateResrouce;
    }

    public static TemplateExecuter buildForAndroidStubEnv(
            TemplateResource resource) {
        return new TemplateExecuter(
                buildAndroidStubConstants(buildPresetConstants()), resource);
    }

    public static TemplateExecuter buildForIOSStubEnv(TemplateResource resource) {
        return new TemplateExecuter(
                buildIOSStubConstants(buildPresetConstants()), resource);
    }

    public static TemplateExecuter build(DictValue constants,
            TemplateResource resource, MonacaApplicationInfo applicationInfo) {
        return new TemplateExecuter(constants, resource);
    }

    public static TemplateExecuter build(Context context,
            TemplateResource resource, MonacaApplicationInfo applicationInfo) {
        return new TemplateExecuter(buildNormalConstants(context,
                buildPresetConstants(), applicationInfo), resource);
    }

    /**
     * Execute template object.
     * 
     * @param writer
     *            Writer object which the executer output.
     */
    public void execute(Template template, Printer printer) {
        new ExecuterVisitor(constants, template).execute(printer);
    }

    /** Execute template object and return its output as string. */
    public String execute(Template template) {
//    	Log.v(TAG, "execute()");
        final StringBuilder builder = new StringBuilder();
        execute(template, new Printer() {
            public void print(String str) {
                builder.append(str);
            }
        });

        return builder.toString();
    }

    static public DictValue buildPresetConstants() {
        DictValue consts = new DictValue();

        // boolean
        consts.put("true", TrueValue.getInstance());
        consts.put("false", FalseValue.getInstance());

        // Symbols
        consts.put("Android", SymbolValue.get("Android"));
        consts.put("IOS", SymbolValue.get("IOS"));

        return consts;
    }

    static protected DictValue buildAndroidStubConstants(DictValue consts) {
//    	Log.v(TAG,"buildAndroidStubConstants");
        // Device
        DictValue device = new DictValue();
        device.put("Platform", consts.get("Android"));
        device.put("Name", new StringValue("stub"));
        device.put("UUID", new StringValue("stub"));
        consts.put("Device", device);

        // Network
        DictValue network = new DictValue();
        network.put("IsReachable", TrueValue.getInstance());
        network.put("Hostname", new StringValue("stub"));
        consts.put("Network", network);
        
     // Application
        DictValue application = new DictValue();
        application.put("WWWDir", new StringValue("stub"));  
        consts.put("App", application);

        return consts;
    }

    static protected DictValue buildIOSStubConstants(DictValue consts) {
//    	Log.v(TAG,"buildIOSStubConstants");
        // Device
        DictValue device = new DictValue();
        device.put("Platform", consts.get("IOS"));
        device.put("Name", new StringValue("stub"));
        device.put("UUID", new StringValue("stub"));
        consts.put("Device", device);

        // Network
        DictValue network = new DictValue();
        network.put("IsReachable", TrueValue.getInstance());
        network.put("Hostname", new StringValue("stub"));
        consts.put("Network", network);
        
        // Application
        DictValue application = new DictValue();
        application.put("WWWDir", new StringValue("stub"));  
        consts.put("App", application);

        return consts;
    }

    static protected DictValue buildNormalConstants(Context context,
            DictValue consts, MonacaApplicationInfo applicationInfo) {
//    	Log.v(TAG,"buildNormalConstants");
    	
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo();
        boolean networkReachable = info != null ? info.isConnected() : false;

        // Device
        DictValue device = new DictValue();
        device.put("Platform", consts.get("Android"));
        device.put("Name", new StringValue(Build.DEVICE));
        device.put("UUID", new StringValue(Build.ID));
        consts.put("Device", device);

        // Network
        DictValue network = new DictValue();
        network.put("IsReachable", networkReachable ? TrueValue.getInstance()
                : FalseValue.getInstance());
        network.put("Hostname", new StringValue(Build.HOST));
        consts.put("Network", network);
        
        // Application
        DictValue application = new DictValue();
        String appRoot = "Undefined";
        if(applicationInfo != null){
        	appRoot = applicationInfo.getWWWDir().replaceFirst("file://", "");
        }
        application.put("WWWDir", new StringValue(appRoot));  
        consts.put("App", application);

        return consts;
    }

    /** Actual template object renderer. */
    class ExecuterVisitor extends StatementNodeVisitor<Printer> {

        protected Stack<String> blockNameStack = new Stack<String>();
        protected Stack<Template> templateStack = new Stack<Template>();
        protected ExpressionEvaluator evaluator;

        public ExecuterVisitor(DictValue constants, Template template) {
            this(constants, template, new Stack<String>(),
                    new Stack<Template>());
        }

        public ExecuterVisitor(DictValue constants, Template template,
                Stack<String> blockNameStack, Stack<Template> templateStack) {
            evaluator = new ExpressionEvaluator(constants);
            // Push executing template.
            templateStack.push(template);

            this.templateStack = templateStack;
            this.blockNameStack = blockNameStack;
        }

        public void execute(Printer printer) {
            currentTemplate().getRootTopStatementsNode().accept(this, printer);
        }

        protected Template currentTemplate() {
            return templateStack.peek();
        }

        /** Execute "echo" statement. */
        @Override
        public void visit(EchoStatementNode node, Printer v) {
            if (node.hasRawModifier()) {
                v.print(evaluator.eval(node.getExpression()).toString());
            } else {
                v.print(escape(evaluator.eval(node.getExpression()).toString()));
            }
        }

        public String escape(String val) {
            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < val.length(); i++) {
                switch (val.charAt(i)) {
                case '<':
                    builder.append("&lt;");
                    break;
                case '>':
                    builder.append("&gt;");
                    break;
                case '"':
                    builder.append("&quot;");
                    break;
                case '\'':
                    builder.append("&rsquo;");
                    break;
                default:
                    builder.append(val.charAt(i));
                    break;
                }
            }

            return builder.toString();
        }

        @Override
        public void visit(ExtendsStatementNode node, Printer v) {
        }

        /** Execute "if" statements. */
        @Override
        public void visit(IfStatementNode node, Printer v) {
            Value condition = evaluator.eval(node.getExpression());

            if (EvaluateUtil.canCastAsTrue(condition)) {
                node.getStatements().accept(this, v);
            } else {
                node.getElseStatement().accept(this, v);
            }
        }

        /** Execute "block" statement. */
        @Override
        public void visit(BlockStatementNode node, Printer v) {
            blockNameStack.push(node.getBlockName());

            if (currentTemplate().getBlock(node.getBlockName()) == null) {
                throw new ExecuterError("no such block '" + node.getBlockName()
                        + "'\n" + templateStack.toString(),
                        currentTemplate().path);
            }

            // Visit statements node in current template.
            currentTemplate().getBlock(node.getBlockName()).getStatementsNode()
                    .accept(this, v);

            blockNameStack.pop();
        }

        /** Execute "include" statement. */
        @Override
        public void visit(IncludeStatementNode node, Printer v) {
            Template template = currentTemplate().getDependency(
                    templateResource.resolve(node.getPath(), currentTemplate()
                            .getResourcePath()));
            templateStack.push(template);

            template.getRootTopStatementsNode().accept(this, v);

            templateStack.pop();
        }

        /** Execute "parent" statement. */
        @Override
        public void visit(ParentStatementNode node, Printer v) {
            Template parent = currentTemplate().getParent();
            templateStack.push(parent);

            Node child = parent.getBlock(blockNameStack.peek())
                    .getStatementsNode();
            child.accept(this, v);

            templateStack.pop();
        }

        /** Execute "raw" statement. */
        @Override
        public void visit(RawStatementNode node, Printer v) {
            v.print(node.getRawString());
        }

        /** Execute statements. */
        @Override
        public void visit(StatementsNode node, Printer v) {
            for (Node child : node) {
                child.accept(this, v);
            }
        }

        /** Execute statements. */
        @Override
        public void visit(TopStatementsNode node, Printer v) {
            visit((StatementsNode) node, v);
        }
    }

    class ExpressionEvaluator extends ExpressionNodeVisitor<Stack<Value>> {

        protected DictValue constantDict;

        public ExpressionEvaluator(DictValue constantDict) {
            this.constantDict = constantDict;
        }

        public Value eval(Node expression) {
            Stack<Value> stack = new Stack<Value>();
            expression.accept(this, stack);
            return stack.pop();
        }

        @Override
        public void visit(ConstantValueNode node, Stack<Value> v) {
            Value val = constantDict;
            for (String id : node.getNames()) {
                val = ((DictValue) val).get(id);
            }
            v.push(val);
        }

        @Override
        public void visit(StringValueNode node, Stack<Value> v) {
            v.push(node.getValue());
        }

        @Override
        public void visit(BinaryOperationNode node, Stack<Value> v) {
            node.getLeft().accept(this, v);
            node.getRight().accept(this, v);
            v.push(node.operate(v.pop(), v.pop()));
        }

        /** Evaluate short circuit binary operator node. */
        @Override
        public void visit(ShortCircuitBinaryOperationNode node, Stack<Value> v) {
            node.getLeft().accept(this, v);
            Value left = v.pop();

            if (node.willEvaluateRight(left)) {
                node.getRight().accept(this, v);
                v.push(node.operate(left, v.pop()));
            } else {
                // case for short circuit.
                v.push(left);
            }
        }

        @Override
        public void visit(UnaryOperationNode node, Stack<Value> v) {
            node.getValueNode().accept(this, v);
            v.push(node.operate(v.pop()));
        }

    }
}
