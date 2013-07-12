package mobi.monaca.framework.bootloader;

public class AbortException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public AbortException(Exception e) {
        super(e);
    }
}
