package mobi.monaca.framework.template.test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.content.res.AssetManager;


public class TestUtil {
	
	/** 
	 * Get the all file path of test case representation. 
	 * 
	 * A test case representation file is located in "assets/testcases".
	 * And its file name has ".testcase" suffix.
	 */
	static public ArrayList<String> getTestCaseFiles(String dir, AssetManager assetManager) {
		ArrayList<String> result = new ArrayList<String>();
		
		try {
			for (String path : assetManager.list(dir)) {
				if (path.endsWith(".testcase")) {
					result.add(dir + "/" + path);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return result;
	}
	
	/** Convert input stream to string. */
	static public String stream2String(InputStream stream) {
		StringWriter writer = new StringWriter();
		
		try {
            char[] buffer = new char[1024];
            int n;
            Reader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return writer.toString();
	}
	
	/** Convert test case representation to map. */
	static public Map<String, String> testCaseRepresentation2Map(String testcaseString) {
		HashMap<String, String> result = new HashMap<String, String>();
		
		String key = "";
		StringBuilder segmentBuilder = new StringBuilder();
		
		for (String line : testcaseString.split("\r\n|\n")) {
			if (line.length() > 6 && line.startsWith("---") && line.endsWith("--")) {
				if (segmentBuilder.length() > 0 && !key.equals("")) {
					result.put(key, segmentBuilder.toString().substring(0, segmentBuilder.length() - 1));
				}
				
				segmentBuilder = new StringBuilder();
				key = line.substring(3, line.length() - 3);
			} else {
				segmentBuilder.append(line + "\n");
			}
		}
		
		if (segmentBuilder.length() > 0 && !key.equals("")) {
			result.put(key, segmentBuilder.toString().substring(0, segmentBuilder.length() - 1));
		}
		
		return result;
	}

}
