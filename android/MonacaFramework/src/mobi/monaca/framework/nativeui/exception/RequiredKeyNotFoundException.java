package mobi.monaca.framework.nativeui.exception;

public class RequiredKeyNotFoundException extends NativeUIException{
	protected String keyName;

	public RequiredKeyNotFoundException(String componentName, String keyName) {
		super(componentName);
		this.keyName = keyName;
	}

	@Override
	public String getMessage() {
		return "Missing required key:'" + keyName + "' in " + componentName;
	}

}
