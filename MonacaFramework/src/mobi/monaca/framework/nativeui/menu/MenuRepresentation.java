package mobi.monaca.framework.nativeui.menu;

import java.io.Serializable;
import java.util.List;

import mobi.monaca.framework.nativeui.UIContext;

import android.view.Menu;

/** This class represents Menu component in UIFramework. */
public class MenuRepresentation implements Serializable {

    private static final long serialVersionUID = 1L;

    protected List<MenuItemRepresentation> menuItemList;

    public MenuRepresentation(List<MenuItemRepresentation> menuItemList) {
        this.menuItemList = menuItemList;
    }

    public void configureMenu(UIContext context, Menu menu) {
        for (MenuItemRepresentation menuItem : menuItemList) {
            menuItem.addMenuItemToMenu(context, menu);
        }
    }
}
