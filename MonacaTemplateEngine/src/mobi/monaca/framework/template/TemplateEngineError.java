package mobi.monaca.framework.template;

/** This interface represents template engine's external error. */
@SuppressWarnings("serial")
public abstract class TemplateEngineError extends RuntimeException {

    protected String message;
    protected Integer line;
    protected String templatePath;

    public TemplateEngineError() {
        this("", "", 0);
    }

    public TemplateEngineError(String message, String templatePath) {
        this(message, templatePath, 0);
    }

    public TemplateEngineError(String message, String templatePath, Integer line) {
        super(message);
        this.message = message;
        this.templatePath = templatePath;
        this.line = line;
    }

    public String getErrorMessage() {
        return message;
    }

    public String getTemplatePath() {
        return templatePath;
    }

    public Integer getLine() {
        return line;
    }

    @Override
    public String toString() {
        if (line <= 0 && templatePath.equals("")) {
            return this.getClass().getSimpleName() + ": " + message;
        }

        if (line <= 0) {
            return this.getClass().getSimpleName() + ": " + message + " in "
                    + templatePath;
        }

        return this.getClass().getSimpleName() + ": " + message + " in "
                + templatePath + " on line " + line;
    }

}
