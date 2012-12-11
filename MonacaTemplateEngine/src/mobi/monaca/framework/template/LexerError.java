package mobi.monaca.framework.template;

@SuppressWarnings("serial")
public class LexerError extends TemplateEngineError {

    public LexerError() {
        super();
    }

    public LexerError(String message, String templatePath) {
        super(message, templatePath);
    }

    public LexerError(String message, String templatePath, Integer line) {
        super(message, templatePath, line);
    }

}
