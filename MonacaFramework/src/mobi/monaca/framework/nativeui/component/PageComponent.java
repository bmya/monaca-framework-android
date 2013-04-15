package mobi.monaca.framework.nativeui.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import mobi.monaca.framework.MonacaApplication;
import mobi.monaca.framework.bootloader.LocalFileBootloader;
import mobi.monaca.framework.nativeui.ComponentEventer;
import mobi.monaca.framework.nativeui.DefaultStyleJSON;
import mobi.monaca.framework.nativeui.NonScaleBitmapDrawable;
import mobi.monaca.framework.nativeui.UIContext;
import mobi.monaca.framework.nativeui.UIEventer;
import mobi.monaca.framework.nativeui.UIGravity;
import mobi.monaca.framework.nativeui.UIUtil;
import mobi.monaca.framework.nativeui.container.TabbarContainer;
import mobi.monaca.framework.nativeui.container.ToolbarContainer;
import mobi.monaca.framework.nativeui.exception.InvalidValueException;
import mobi.monaca.framework.nativeui.exception.MenuNameNotDefinedInAppMenuFileException;
import mobi.monaca.framework.nativeui.exception.NativeUIException;
import mobi.monaca.framework.nativeui.exception.RequiredKeyNotFoundException;
import mobi.monaca.framework.nativeui.menu.MenuRepresentation;
import mobi.monaca.framework.util.MyLog;
import mobi.monaca.utils.TimeStamp;
import mobi.monaca.utils.log.LogItem;
import mobi.monaca.utils.log.LogItem.LogLevel;
import mobi.monaca.utils.log.LogItem.Source;

import org.apache.http.conn.scheme.LayeredSocketFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.text.TextUtils;
import android.util.FloatMath;
import android.view.Gravity;
import android.view.View;

/**
 * Used for manipulating MonacaPageActivity background style
 * 
 */
public class PageComponent extends Component {

	private static final String TAG = PageComponent.class.getSimpleName();
	private UIContext uiContext;
	private LayerDrawable mLayeredBackgroundDrawable;
	private PageOrientation mScreenOrientation;
	protected Component topComponent;
	protected Component bottomComponent;
	public static ComponentEventer BACK_BUTTON_EVENTER;
	public UIEventer eventer;
	public String menuName;

	protected static String[] validKeys = {
		"top",
		"bottom",
		"event",
		"style",
		"menu"
	};

	@Override
	public String[] getValidKeys() {
		return validKeys;
	}

	public PageComponent(UIContext uiContext, JSONObject pageJSON) throws NativeUIException {
		super(pageJSON);
		this.uiContext = uiContext;
		JSONObject event = getComponentJSON().optJSONObject("event");
		if(event != null) {
			eventer = new UIEventer(uiContext, getComponentJSON().optJSONObject("event"));
		}
		menuName = getComponentJSON().optString("menu");
		if( !TextUtils.isEmpty(menuName) ) {
			MonacaApplication app = (MonacaApplication) uiContext.getPageActivity().getApplication();
			MenuRepresentation menuRepresentation = app.findMenuRepresentation(menuName);
			if(menuRepresentation == null){
				throw new MenuNameNotDefinedInAppMenuFileException(getComponentName(), menuName);
			}
		}
		style();

		buildChildren();
	}

	private void buildChildren() throws NativeUIException {
		JSONObject topJSON = getComponentJSON().optJSONObject("top");
		if (topJSON != null) {
			// right now top is always toolbar
			topComponent = new ToolbarContainer(uiContext, topJSON, true);
		}

		JSONObject bottomJSON = getComponentJSON().optJSONObject("bottom");
		if (bottomJSON != null) {
			String containerType;
			try {
				containerType = bottomJSON.getString("container");
				if (containerType.equalsIgnoreCase("toolbar")) {
					bottomComponent = new ToolbarContainer(uiContext, bottomJSON, false);
				}
				if (containerType.equalsIgnoreCase("tabbar")) {
					bottomComponent = new TabbarContainer(uiContext, bottomJSON);
				}
			} catch (JSONException e) {
				throw new RequiredKeyNotFoundException("top", "container");
			}
		}
	}

	@Override
	public View getView() {
		return null;
	}
	
	public Component getTopComponent() {
		return topComponent;
	}

	public Component getBottomComponent() {
		return bottomComponent;
	}

	public PageOrientation getScreenOrientation() {
		return mScreenOrientation;
	}

	public Drawable getBackgroundDrawable() {
		return mLayeredBackgroundDrawable;
	}

	@Override
	public void updateStyle(JSONObject update) {
		UIUtil.updateJSONObject(style, update);
		style();
		uiContext.getPageActivity().setupBackground(mLayeredBackgroundDrawable);
	}

	private void style() {
		ArrayList<Drawable> layerList = new ArrayList<Drawable>();
		processScreenOrientation();
		processPageStyleBackgroundColor(style, layerList);
		processPageStyleBackgroundImage(style, layerList);
		processPageStyleBackgroundRepeat(layerList);

		Drawable[] layers = new Drawable[layerList.size()];
		mLayeredBackgroundDrawable = new LayerDrawable(layerList.toArray(layers));
	}

	private void processScreenOrientation() {
		String screenOrientationString = style.optString("screenOrientation").trim();
		if (TextUtils.isEmpty(screenOrientationString)) {
			mScreenOrientation = PageOrientation.INHERIT;
		} else if (screenOrientationString.equalsIgnoreCase("portrait")) {
			mScreenOrientation = PageOrientation.PORTRAIT;
		} else if (screenOrientationString.equalsIgnoreCase("landscape")) {
			mScreenOrientation = PageOrientation.LANDSCAPE;
		} else {
			mScreenOrientation = PageOrientation.SENSOR;
		}
	}

	private void processPageStyleBackgroundRepeat(ArrayList<Drawable> layerList) {
		String backgroundRepeatString = style.optString("backgroundRepeat");
		String backgroundImageString = style.optString("backgroundImage");
		if (!backgroundRepeatString.equalsIgnoreCase("") && !backgroundImageString.equalsIgnoreCase("")) {

			Drawable drawable = getTopDrawable(layerList);
			if ((drawable instanceof BitmapDrawable) == false) {
				return;
			}

			BitmapDrawable bitmapDrawable = (BitmapDrawable) getTopDrawable(layerList);
			MyLog.e(TAG, "background repeat:" + backgroundRepeatString);
			if (backgroundRepeatString.equalsIgnoreCase("repeat-x")) {
				bitmapDrawable.setTileModeX(TileMode.REPEAT);
			}

			if (backgroundRepeatString.equalsIgnoreCase("repeat-y")) {
				bitmapDrawable.setTileModeY(TileMode.REPEAT);
			}

			if (backgroundRepeatString.equalsIgnoreCase("repeat")) {
				bitmapDrawable.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
			}

			if (backgroundRepeatString.equalsIgnoreCase("no-repeat")) {
				bitmapDrawable.setTileModeXY(null, null);
			}
		}
	}

	private Drawable getTopDrawable(ArrayList<Drawable> layerList) {
		return layerList.get(layerList.size() - 1);
	}

	private void processPageStyleBackgroundColor(JSONObject pageStyle, ArrayList<Drawable> layerList) {
		// // background color
		String backgroundColorString = pageStyle.optString("backgroundColor").trim();

		// default
		int color = Color.WHITE;
		if (!backgroundColorString.equalsIgnoreCase("")) {
			// #xxxxxx format
			if (backgroundColorString.startsWith("#")) {
				try {
					color = Color.parseColor(backgroundColorString);
				} catch (NumberFormatException e) {
					e.printStackTrace();
					LogItem logItem = new LogItem(TimeStamp.getCurrentTimeStamp(), Source.SYSTEM, LogLevel.ERROR,
							"NativeComponent:InvalidColorValue: Cannot parse backgroundColor " + backgroundColorString, "", 0);
					MyLog.sendBloadcastDebugLog(uiContext, logItem);
				}
			} else {
				LogItem logItem = new LogItem(TimeStamp.getCurrentTimeStamp(), Source.SYSTEM, LogLevel.ERROR,
						"NativeComponent:InvalidColorValue: Cannot parse backgroundColor " + backgroundColorString, "", 0);
				MyLog.sendBloadcastDebugLog(uiContext, logItem);
			}
		}

		ColorDrawable colorDrawable = new ColorDrawable(color);
		layerList.add(colorDrawable);
	}

	private void processPageStyleBackgroundImage(JSONObject pageStyle, ArrayList<Drawable> layerList) {
		// // background image
		String backgroundImageFile = pageStyle.optString("backgroundImage").trim();
		boolean shouldSkipBackgroundPosition = false;
		if (!backgroundImageFile.equalsIgnoreCase("")) {
			try {
				Bitmap bitmap = uiContext.readScaledBitmap(backgroundImageFile);
				BitmapDrawable backgroundImage = new BitmapDrawable(uiContext.getResources(), bitmap);
				layerList.add(backgroundImage);
				float bitmapWidth = bitmap.getWidth();
				float bitmapHeight = bitmap.getHeight();
				float deviceWidth = uiContext.getDisplayMetrics().widthPixels;
				float deviceHeight = uiContext.getDisplayMetrics().heightPixels;

				// // Background Size
				String backgroundSize = pageStyle.optString("backgroundSize").trim();
				if (backgroundSize.equalsIgnoreCase("")) {
					backgroundSize = "cover";
				}

				// cover
				if (backgroundSize.equalsIgnoreCase("cover")) {
					backgroundImage.setGravity(Gravity.FILL);
					shouldSkipBackgroundPosition = true;
					// contain
				} else if (backgroundSize.equalsIgnoreCase("contain")) {
					float widthRatio = deviceWidth / bitmapWidth;
					float heightRatio = deviceHeight / bitmapHeight;

					float safeRatio = widthRatio < heightRatio ? widthRatio : heightRatio;
					int scaledHeight = (int) FloatMath.ceil(bitmapHeight * safeRatio);

					bitmap = UIUtil.resizeBitmap(bitmap, scaledHeight);
					BitmapDrawable drawable = new BitmapDrawable(uiContext.getResources(), bitmap);
					layerList.remove(layerList.size() - 1);
					layerList.add(drawable);
				}

				// pixel or percentage or dip
				else {
					int width = 0;
					int height = 0;
					// both width and height specified
					if (backgroundSize.trim().contains(",")) {
						JSONArray sizes = pageStyle.optJSONArray("backgroundSize");
						if (sizes != null) {
							// width
							String widthString = sizes.optString(0);
							if (widthString.endsWith("%")) {
								String percentageString = widthString.replace("%", "");
								int percentage = Integer.parseInt(percentageString);
								width = (int) (deviceWidth * percentage / 100);
							} else if (widthString.endsWith("px")) {
								widthString = widthString.replace("px", "");
								width = Integer.parseInt(widthString);
							} else {
								widthString = widthString.replace("dip", "");
								width = Integer.parseInt(widthString);
								width = UIUtil.dip2px(uiContext, width);
							}

							// height
							String heightString = sizes.optString(1);
							if (heightString.endsWith("%")) {
								String percentageString = heightString.replace("%", "");
								int percentage = Integer.parseInt(percentageString);
								height = (int) (deviceHeight * percentage / 100);
							} else if (widthString.endsWith("px")) {
								heightString = heightString.replace("px", "");
								height = Integer.parseInt(heightString);
							} else {
								heightString = widthString.replace("dip", "");
								height = Integer.parseInt(widthString);
								height = UIUtil.dip2px(uiContext, width);
							}
						} else {
							LogItem logItem = new LogItem(TimeStamp.getCurrentTimeStamp(), Source.SYSTEM, LogLevel.ERROR,
									"NativeComponent:InvalidColorValue: Cannot parse backgroundSize " + backgroundSize, "", 0);
							MyLog.sendBloadcastDebugLog(uiContext, logItem);
						}

					} else {
						// only width specified
						String widthString = backgroundSize;
						if (widthString.endsWith("%")) {
							String percentageString = widthString.replace("%", "");
							int percentage = Integer.parseInt(percentageString);
							width = (int) (deviceWidth * percentage / 100);
						} else if (widthString.endsWith("px")) {
							widthString = widthString.replace("px", "");
							width = Integer.parseInt(widthString);
						} else {
							widthString = widthString.replace("dip", "");
							width = Integer.parseInt(widthString);
							width = UIUtil.dip2px(uiContext, width);
						}

						float ratio = bitmapHeight / bitmapWidth;
						height = (int) (width * ratio);
						MyLog.v(TAG, "scaled height:" + height);
					}

					// finished calculating width and height
					bitmap = UIUtil.resizeBitmap(bitmap, width, height);
					BitmapDrawable drawable = new BitmapDrawable(uiContext.getResources(), bitmap);
					layerList.remove(layerList.size() - 1);
					layerList.add(drawable);
				}

				BitmapDrawable finalDrawable = (BitmapDrawable) getTopDrawable(layerList);
				processBackgroundPosition(pageStyle, finalDrawable, shouldSkipBackgroundPosition);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void processBackgroundPosition(JSONObject pageStyle, BitmapDrawable backgroundImage, boolean shouldSkipBackgroundPosition) {
		if (shouldSkipBackgroundPosition) {
			return;
		}

		// // position
		String horizontalPositionString = "center";
		String verticalPositionString = "center";
		String backgroundPosition = pageStyle.optString("backgroundPosition").trim();

		// get horizontal and vertical
		int horizontalGravity = Gravity.CENTER;
		int verticalGravity = Gravity.CENTER;
		if (!backgroundPosition.equalsIgnoreCase("")) {
			// default
			horizontalPositionString = "center";
			verticalPositionString = "center";

			if (backgroundPosition.contains(" ")) {
				// user specified both horizontal and vertial
				MyLog.w(TAG, backgroundPosition + " contains a space!");
				String[] positions = backgroundPosition.split(" ");
				horizontalPositionString = positions[0];
				verticalPositionString = positions[1];
			} else {
				// only horizontal position
				horizontalPositionString = backgroundPosition;
			}

			// check horizontal
			// left, center, or right
			if (UIGravity.hasHorizontalGravity(horizontalPositionString)) {
				horizontalGravity = UIGravity.getHorizontalGravity(horizontalPositionString);
			}

			// check vertical
			// top, center, or bottom
			if (UIGravity.hasVerticalGravity(verticalPositionString)) {
				verticalGravity = UIGravity.getVerticalGravity(verticalPositionString);
			}
		}
		backgroundImage.setGravity(horizontalGravity | verticalGravity);
	}

	@Override
	public String getComponentName() {
		return "Page";
	}

	@Override
	public JSONObject getDefaultStyle() {
		return DefaultStyleJSON.page();
	}
}
