package mobi.monaca.framework.nativeui;

import java.util.HashMap;

import android.view.Gravity;

public class UIGravity {
	private static HashMap<String, Integer> horizontalGravityMap;
	private static HashMap<String, Integer> verticalGravityMap;
	public static final String HORIZONTAL_POSITIONS[] = {"left", "center", "right"}; 
	public static final String VERTICAL_POSITIONS[] = {"top", "center", "bottom"}; 

	private static final int horizontalGravities[] = {Gravity.LEFT, Gravity.CENTER_HORIZONTAL, Gravity.RIGHT};
	private static final int verticalGravities[] = {Gravity.TOP, Gravity.CENTER_VERTICAL, Gravity.BOTTOM};
	
	private static void initHorizontalMap(){
		if(horizontalGravityMap == null){
			horizontalGravityMap = new HashMap<String, Integer>();
			for (int i = 0; i < HORIZONTAL_POSITIONS.length; i++) {
				horizontalGravityMap.put(HORIZONTAL_POSITIONS[i], horizontalGravities[i]);
			}
				
		}
	}
	
	private static void initVerticalMap(){
		if(verticalGravityMap == null){
			verticalGravityMap = new HashMap<String, Integer>();
			for (int i = 0; i < VERTICAL_POSITIONS.length; i++) {
				verticalGravityMap.put(VERTICAL_POSITIONS[i], verticalGravities[i]);
			}
				
		}
	}
	
	public static Integer getHorizontalGravity(String position){
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
