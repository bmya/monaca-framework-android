package mobi.monaca.framework.nativeui.exception;

import java.util.Arrays;

public class InvalidValueException extends NativeUIException {
	protected String componentKey;
	protected String userSpecifiedValue;
	protected String[] validValues;

	public InvalidValueException(String componentName, String componentKey, String userSpecifiedValue, String[] validValues) {
		super(componentName);
		this.componentKey = componentKey;
		this.userSpecifiedValue = userSpecifiedValue;
		this.validValues = validValues;
	}

	@Override
	public String getMessage() {
		return componentName + " " + componentKey + ": " + " is not one of " + Arrays.toString(validValues);
	}
}
