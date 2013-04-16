package mobi.monaca.framework.nativeui.exception;

public class NativeUIIOException extends NativeUIException{
	private String keyName;
	private String keyValue;
	private String platformErrorMessage;
	
	public NativeUIIOException(String componentName, String keyName, String keyValue, String platformErrorMessage) {
		super(componentName);
		this.keyName = keyName;
		this.keyValue = keyValue;
		this.platformErrorMessage = platformErrorMessage;
	}

	@Override
	public String getMessage() {
		return componentName + "'s " + keyName + ": " + keyValue + " read failed. " + platformErrorMessage;
	}

}
