package mobi.monaca.framework.nativeui.exception;

public class ConversionException extends NativeUIException{
	private String keyName;
	private String userSpecifiedValue;
	private String targetType;

	public ConversionException(String componentName, String keyName, String userSpecifiedValue, String targetType) {
		super(componentName);
		this.keyName = keyName;
		this.userSpecifiedValue = userSpecifiedValue;
		this.targetType = targetType;
	}

	@Override
	public String getMessage() {
		return componentName + " " + keyName + " " + userSpecifiedValue + " cannot be parsed to " + targetType;
	}

}
