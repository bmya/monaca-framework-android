package mobi.monaca.framework.nativeui;

import java.util.HashMap;

import android.view.Gravity;

public class UIGravity {
	private static HashMap<String, Integer> horizontalGravityMap;
	private static HashMap<String, Integer> verticalGravityMap;
	private static String horizontalPositions[] = {"left", "center", "right"}; 
	private static String verticalPositions[] = {"top", "center", "bottom"}; 

	private static int horizontalGravities[] = {Gravity.LEFT, Gravity.CENTER_HORIZONTAL, Gravity.RIGHT};
	private static int verticalGravities[] = {Gravity.TOP, Gravity.CENTER_VERTICAL, Gravity.BOTTOM};
	
	private static void initHorizontalMap(){
		if(horizontalGravityMap == null){
			horizontalGravityMap = new HashMap<String, Integer>();
			for (int i = 0; i < horizontalPositions.length; i++) {
				horizontalGravityMap.put(horizontalPositions[i], horizontalGravities[i]);
			}
				
		}
	}
	
	private static void initVerticalMap(){
		if(verticalGravityMap == null){
			verticalGravityMap = new HashMap<String, Integer>();
			for (int i = 0; i < verticalPositions.length; i++) {
				verticalGravityMap.put(verticalPositions[i], verticalGravities[i]);
			}
				
		}
	}
	
	public static int getHorizontalGravity(String position){
		initHorizontalMap();
		return horizontalGravityMap.get(position);
	}
	
	public static boolean hasHorizontalGravity(String position){
		initHorizontalMap();
		return horizontalGravityMap.containsKey(position);
	}
	
	public static int getVerticalGravity(String position){
		initVerticalMap();
		return verticalGravityMap.get(position);
	}
	
	public static boolean hasVerticalGravity(String position){
		initVerticalMap();
		return verticalGravityMap.containsKey(position);
	}
	
}
