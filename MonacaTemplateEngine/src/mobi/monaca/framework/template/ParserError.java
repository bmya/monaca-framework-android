package mobi.monaca.framework.template;

@SuppressWarnings("serial")
public class ParserError extends TemplateEngineError {

    public ParserError() {
        super();
    }

    public ParserError(String message, String templatePath) {
        super(message, templatePath);
    }

    public ParserError(String message, String templatePath, Integer line) {
        super(message, templatePath, line);
    }

}