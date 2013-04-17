package mobi.monaca.framework.nativeui.exception;

import java.util.Arrays;

public class KeyNotValidException extends NativeUIException {
	protected String componentKey;
	protected String userSpecifiedKey;
	protected String[] validKeys;

	public KeyNotValidException(String componentName, String userSpecifiedKey, String[] validKeys) {
		super(componentName);
		this.componentKey = componentKey;
		this.userSpecifiedKey = userSpecifiedKey;
		this.validKeys = validKeys;
	}

	@Override
	public String getMessage() {
		return componentName + " '" + userSpecifiedKey + "' is not a valid key. Did you mean one of these " + Arrays.toString(validKeys) + " ?";
	}
}
