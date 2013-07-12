package mobi.monaca.framework.nativeui.exception;

public abstract class NativeUIException extends Exception{
	protected String componentName;

	public NativeUIException(String componentName) {
		this.componentName = componentName;
	}

	public abstract String getMessage();
}
