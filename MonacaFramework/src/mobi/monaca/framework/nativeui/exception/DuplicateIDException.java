package mobi.monaca.framework.nativeui.exception;

import java.util.Arrays;

public class DuplicateIDException extends NativeUIException{
	private String[] components;
	private String id;

	public DuplicateIDException(String id, String[] components) {
		super(components.toString());
		this.id = id;
		this.components = components;
	}

	@Override
	public String getMessage() {
		return "Duplicate id '" + id + "' in " + Arrays.toString(components);
	}

}
