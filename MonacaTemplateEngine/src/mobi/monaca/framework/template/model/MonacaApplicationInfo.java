package mobi.monaca.framework.template.model;

public class MonacaApplicationInfo {
	private String wwwDir;


	public MonacaApplicationInfo(String wwwDir) {
		super();
		this.wwwDir = wwwDir;
	}


	public String getWWWDir() {
		return wwwDir;
	}

	public void setWWWDir(String rootDirectory) {
		this.wwwDir = rootDirectory;
	}
}
