package mobi.monaca.utils.log;

import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;



public class LogItem {
	
	public enum LogLevel {
		DEBUG, LOG, WARNING, ERROR
	}
	
	public enum Source {
		JAVASCRIPT, SYSTEM
	}

	private String currentTimeStamp;
	private Source source;
	private LogLevel logLevel;
	private String message;
	private String url;
	private int lineNumber;
	
	public LogItem(String currentTimeStamp, Source source, LogLevel logLevel, String message, String url, int lineNumber) {
		super();
		this.currentTimeStamp = currentTimeStamp;
		this.source = source;
		this.logLevel = logLevel;
		this.message = message;
		this.url = url;
		this.lineNumber = lineNumber;
	}
	
	public Source getSource() {
		return source;
	}
	
	public int getLineNumber() {
		return lineNumber;
	}
	
	public LogLevel getLogLevel() {
		return logLevel;
	}
	
	public String getMessage() {
		return message;
	}
	
	public String getUrl() {
		return url;
	}
	
	public String getCurrentTimeStamp() {
		return currentTimeStamp;
	}
	
	public JSONObject createJsonObject() throws JSONException{
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("source", source.toString().toLowerCase(Locale.ENGLISH));
		jsonObject.put("type", logLevel.toString().toLowerCase(Locale.ENGLISH));
		jsonObject.put("message", message);
		jsonObject.put("url", url);
		jsonObject.put("line", lineNumber);
		
		return jsonObject;
	}
	
	@Override
	public String toString() {
		return message;
	}
}
