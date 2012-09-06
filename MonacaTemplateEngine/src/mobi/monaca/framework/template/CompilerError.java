package mobi.monaca.framework.template;

@SuppressWarnings("serial")
public class CompilerError extends TemplateEngineError {

    public CompilerError() {
        super();
    }

    public CompilerError(String message, String templatePath) {
        super(message, templatePath);
    }

    public CompilerError(String message, String templatePath, Integer line) {
        super(message, templatePath, line);
    }

}
