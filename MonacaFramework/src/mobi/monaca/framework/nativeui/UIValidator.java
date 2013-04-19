package mobi.monaca.framework.nativeui;

import static mobi.monaca.framework.nativeui.UIUtil.buildColor;

import java.util.Iterator;

import mobi.monaca.framework.nativeui.exception.ConversionException;
import mobi.monaca.framework.nativeui.exception.KeyNotValidException;
import mobi.monaca.framework.nativeui.exception.NativeUIException;
import mobi.monaca.framework.nativeui.exception.ValueNotInRangeException;
import mobi.monaca.framework.util.MyLog;
import mobi.monaca.utils.TimeStamp;
import mobi.monaca.utils.log.LogItem;
import mobi.monaca.utils.log.LogItem.LogLevel;
import mobi.monaca.utils.log.LogItem.Source;

import org.json.JSONObject;

import android.content.Context;

public class UIValidator {
	public static void validateKey(Context context, String componentName, JSONObject componentJSON, String[] validKeys) throws KeyNotValidException {
		Iterator<String> keys = componentJSON.keys();

		while (keys.hasNext()) {
			boolean valid = false;
			String userSpecifiedKey = (String) keys.next();
			for (int i = 0; i < validKeys.length; i++) {
				String validKey = validKeys[i];
				if (userSpecifiedKey.equalsIgnoreCase(validKey)) {
					valid = true;
				}
			}
			if (valid == false) {
				KeyNotValidException exception = new KeyNotValidException(componentName, userSpecifiedKey, validKeys);
				throw exception;
			}
		}
    }
	
	public static void reportException(Context context, NativeUIException e){
		e.printStackTrace();
		LogItem logItem = new LogItem(TimeStamp.getCurrentTimeStamp(), Source.SYSTEM, LogLevel.ERROR, "NativeComponent:" + e.getMessage(), "", 0);
		MyLog.sendBroadcastDebugLog(context, logItem);
	}
	
	public static int parseAndValidateColor(Context context, String componentName, String keyName, String defaultValue, JSONObject componentJSON) throws ConversionException{
		String backgroundColorString = componentJSON.optString(keyName, defaultValue);
		try{
			int backgroundColor = buildColor(backgroundColorString);
			return backgroundColor;
		}catch (IllegalArgumentException e) {
			throw new ConversionException( componentName, keyName, backgroundColorString, "Color");
		}
	}
	
	public static float parseAndValidateFloat(Context context, String componentName, String keyName, String defaultValue, JSONObject componentJSON, float min, float max) throws ValueNotInRangeException, ConversionException{
		String floatString = defaultValue;
		if(componentJSON.has(keyName)){
			floatString = componentJSON.optString(keyName);
		}
        float floatValue; 
        try {
        	floatValue = Float.parseFloat(floatString);
        	if(floatValue < min || floatValue > max){
        		throw new ValueNotInRangeException(componentName, keyName, floatString, "[" + min + "-" + max + "]");
        	}
        	return floatValue;
        }catch (IllegalArgumentException e) {
        	throw new ConversionException(componentName, keyName, floatString, "Float");
		}
	}
	
	public static int parseAndValidateInt(Context context, String componentName, String keyName, String defaultValue, JSONObject componentJSON, int min, int max) throws ValueNotInRangeException, ConversionException{
		String integerString = defaultValue;
		if(componentJSON.has(keyName)){
			integerString = componentJSON.optString(keyName);
		}
        int intValue; 
        try {
        	intValue = Integer.parseInt(integerString);
        	if(intValue < min || intValue > max){
        		throw new ValueNotInRangeException(componentName, keyName, integerString, "[" + min + "-" + max + "]");
        	}
        	return intValue;
        }catch (IllegalArgumentException e) {
        	throw new ConversionException(componentName, keyName, integerString, "Integer");
		}
	}
}
