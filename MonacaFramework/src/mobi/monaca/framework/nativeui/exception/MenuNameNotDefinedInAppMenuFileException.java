package mobi.monaca.framework.nativeui.exception;

public class MenuNameNotDefinedInAppMenuFileException extends NativeUIException{
	private String menuName;

	public MenuNameNotDefinedInAppMenuFileException(String componentName, String menuName) {
		super(componentName);
		this.menuName = menuName;
	}


	@Override
	public String getMessage() {
		return "menu '" + menuName + "' is not defined in 'app.menu' file";
	}

}
