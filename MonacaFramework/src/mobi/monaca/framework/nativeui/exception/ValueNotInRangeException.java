package mobi.monaca.framework.nativeui.exception;

public class ValueNotInRangeException extends NativeUIException{
	private String keyName;
	private String userSpecifiedValue;
	private String validRange;

	public ValueNotInRangeException(String componentName, String keyName, String userSpecifiedValue, String validRange) {
		super(componentName);
		this.keyName = keyName;
		this.userSpecifiedValue = userSpecifiedValue;
		this.validRange = validRange;
	}

	@Override
	public String getMessage() {
		return componentName + " " + keyName + " " + userSpecifiedValue + " is not in range " + validRange;
	}

}
