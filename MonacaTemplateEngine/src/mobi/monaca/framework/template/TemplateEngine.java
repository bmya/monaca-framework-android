package mobi.monaca.framework.template;

import java.io.FileNotFoundException;

import mobi.monaca.framework.template.TemplateCompiler.CompilerContext;
import mobi.monaca.framework.template.model.MonacaApplicationInfo;
import android.content.Context;
import android.util.Log;

public class TemplateEngine {

    public static final String VERSION = "0.0.1";

	private static final String TAG = TemplateEngine.class.getSimpleName();

    protected TemplateCompiler compiler;

    protected CompilerContext compilerContext;

    protected TemplateExecuter executer;
    
    protected MonacaApplicationInfo applicationInfo;
    
    protected TemplateResource templateResource;

    enum StubType {
        Android, IOS
    }

    public TemplateEngine(Context context, MonacaApplicationInfo applicationInfo) {
        this(context, new AssetTemplateResource(context), applicationInfo);
    }

    public TemplateEngine(Context context, TemplateResource templateResource, MonacaApplicationInfo applicationInfo) {
    	this.applicationInfo = applicationInfo;
        this.compilerContext = new TemplateCompiler.CompilerContext();
        this.compiler = new TemplateCompiler(templateResource);
        this.executer = TemplateExecuter.build(context, templateResource, applicationInfo);
        this.templateResource = templateResource;
    }

    public TemplateEngine(StubType stub, TemplateResource templateResource, MonacaApplicationInfo applicationInfo) {
    	this.applicationInfo = applicationInfo;
        this.compilerContext = new TemplateCompiler.CompilerContext();
        this.compiler = new TemplateCompiler(templateResource);
        this.executer = stub == StubType.IOS ? TemplateExecuter
                .buildForIOSStubEnv(templateResource) : TemplateExecuter
                .buildForAndroidStubEnv(templateResource);
        this.templateResource = templateResource;
    }

    public String execute(String path) {
//    	Log.v(TAG, "execute. path:" + path);
    	try {
            Template template = compiler.compileFrom(path, compilerContext);
            return executer.execute(template);
        } catch (TemplateEngineError e) {
            return e.toString();
        } catch (RuntimeException e) {
        	if(e.getCause()!= null && e.getCause() instanceof FileNotFoundException){
        		throw e;
        	}else{
        		StringBuilder builder = new StringBuilder();

                builder.append(e.toString() + "\n");
                for (StackTraceElement elt : e.getStackTrace()) {
                    builder.append("    " + elt.toString() + "\n");
                }

                return builder.toString();
        	}
            
        }
    }
}
