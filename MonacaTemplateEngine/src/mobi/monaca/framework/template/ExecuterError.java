package mobi.monaca.framework.template;

@SuppressWarnings("serial")
public class ExecuterError extends TemplateEngineError {

    public ExecuterError(String message, String templatePath) {
        super(message, templatePath);
    }

    public ExecuterError(String message, String templatePath, Integer line) {
        super(message, templatePath, line);
    }

}
