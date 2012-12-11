package mobi.monaca.framework.template;

/** CLI intarfaece for this template engine */
public class Cli {

    public static final String STUB_ENV_NAME = "MONACA_VIRTUAL_DEVICE";

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("usage :");
            System.out.println("    export " + STUB_ENV_NAME + "=android");
            System.out.println("    # or ");
            System.out.println("    export " + STUB_ENV_NAME + "=ios");
            System.out
                    .println("    java -cp monaca_templating.jar mobi.monaca.framework.template.Cli path/to/template.html");
            return;
        }

        String templatePath = args[0];

        if (templatePath != null) {
            System.out.print(new TemplateEngine(createStubType(),
                    new FileSystemResourceLoader(), null).execute(templatePath));
        }
    }

    static protected TemplateEngine.StubType createStubType() {
        String stub = System.getenv(STUB_ENV_NAME);
        stub = stub == null ? "" : stub;

        if (stub.equals("android")) {
            return TemplateEngine.StubType.Android;
        }

        if (stub.equals("ios")) {
            return TemplateEngine.StubType.IOS;
        }

        return TemplateEngine.StubType.Android;
    }

}