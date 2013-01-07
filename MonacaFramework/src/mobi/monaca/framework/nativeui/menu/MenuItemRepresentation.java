package mobi.monaca.framework.nativeui.menu;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Field;

import mobi.monaca.framework.bootloader.LocalFileBootloader;
import mobi.monaca.framework.nativeui.UIContext;
import mobi.monaca.framework.util.MyLog;

import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

/** This class represents MenuItem component in UIFramework. */
public class MenuItemRepresentation implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final String TAG = MenuItemRepresentation.class.getSimpleName();

	protected String title;
	protected String iconImagePath = "";
	protected String action = "";

	public MenuItemRepresentation(String title) {
		this.title = title;
	}

	public void setIconImagePath(String iconImagePath) {
		this.iconImagePath = iconImagePath;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public void addMenuItemToMenu(final UIContext context, Menu menu) {
		MenuItem menuItem = menu.add(title);

		if (!iconImagePath.equals("")) {
			MyLog.v(TAG, "iconImagePath:" + iconImagePath);
			if (iconImagePath.startsWith("@")) {
				try {
					Field field = android.R.drawable.class.getField("ic_menu_" + iconImagePath.substring(1));
					int id = (Integer) field.get(null);
					menuItem.setIcon(context.getResources().getDrawable(id));
				} catch (Exception e) {
					MyLog.d(getClass().getSimpleName(), "can't locate system icon: android.R.drawable.ic_menu_" + iconImagePath.substring(1));
				}
			} else {
				try {
					InputStream stream = null;
					if (iconImagePath.startsWith("/data/")) {
						stream = new FileInputStream(iconImagePath);
					}else{
						stream = LocalFileBootloader.openAsset(context, "www/" + iconImagePath);
					}
					menuItem.setIcon(new BitmapDrawable(context.getResources(), BitmapFactory.decodeStream(stream)));
					stream.close();
				} catch (IOException e) {
					MyLog.d(getClass().getSimpleName(), "cant open icon image: file:///android_asset/www/" + iconImagePath);
				}
			}
		}

		menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				if (!action.equals("")) {
					context.react("javascript:" + action);
					return true;
				}
				return false;
			}
		});
	}

}