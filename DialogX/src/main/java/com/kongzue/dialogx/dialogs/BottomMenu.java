package com.kongzue.dialogx.dialogs;

import static android.view.View.OVER_SCROLL_NEVER;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.lifecycle.LifecycleOwner;

import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.R;
import com.kongzue.dialogx.interfaces.BottomMenuListViewTouchEvent;
import com.kongzue.dialogx.interfaces.DialogLifecycleCallback;
import com.kongzue.dialogx.interfaces.DialogXAnimInterface;
import com.kongzue.dialogx.interfaces.DialogXRunnable;
import com.kongzue.dialogx.interfaces.DialogXStyle;
import com.kongzue.dialogx.interfaces.MenuItemLayoutRefreshCallback;
import com.kongzue.dialogx.interfaces.MenuItemTextInfoInterceptor;
import com.kongzue.dialogx.interfaces.OnBackPressedListener;
import com.kongzue.dialogx.interfaces.OnBackgroundMaskClickListener;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.kongzue.dialogx.interfaces.OnMenuButtonClickListener;
import com.kongzue.dialogx.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialogx.interfaces.OnIconChangeCallBack;
import com.kongzue.dialogx.interfaces.OnMenuItemClickListener;
import com.kongzue.dialogx.interfaces.OnMenuItemSelectListener;
import com.kongzue.dialogx.interfaces.SELECT_MODE;
import com.kongzue.dialogx.util.BottomMenuArrayAdapter;
import com.kongzue.dialogx.util.ItemDivider;
import com.kongzue.dialogx.util.TextInfo;
import com.kongzue.dialogx.util.views.DialogListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2020/10/6 23:48
 */
public class BottomMenu extends BottomDialog {

    protected BottomMenu me = this;
    protected int selectionIndex = -1;
    protected SELECT_MODE selectMode = SELECT_MODE.NONE;
    protected ArrayList<Integer> selectionItems;
    protected boolean showSelectedBackgroundTips = false;
    protected MenuItemLayoutRefreshCallback<BottomMenu> menuMenuItemLayoutRefreshCallback;
    protected Map<Integer, Boolean> menuUsability = new HashMap<Integer, Boolean>();
    protected ItemDivider itemDivider;

    protected OnMenuItemClickListener<BottomMenu> onMenuItemClickListener;

    public static BottomMenu build() {
        return new BottomMenu();
    }

    public static BottomMenu build(DialogXStyle style) {
        return new BottomMenu().setStyle(style);
    }

    public static BottomMenu build(OnBindView<BottomDialog> onBindView) {
        return new BottomMenu().setCustomView(onBindView);
    }

    protected BottomMenu() {
        super();
    }

    protected OnIconChangeCallBack<BottomMenu> onIconChangeCallBack;
    protected MenuItemTextInfoInterceptor<BottomMenu> menuItemTextInfoInterceptor;
    protected DialogListView listView;
    protected BaseAdapter menuListAdapter;
    protected List<CharSequence> menuList;
    protected List<Integer> iconResIds;
    protected boolean autoTintIconInLightOrDarkMode = true;

    public static BottomMenu show(List<CharSequence> menuList) {
        BottomMenu bottomMenu = new BottomMenu();
        bottomMenu.setMenuList(menuList);
        bottomMenu.show();
        return bottomMenu;
    }

    public static BottomMenu show(List<CharSequence> menuList, OnMenuItemClickListener<BottomMenu> onMenuItemClickListener) {
        BottomMenu bottomMenu = new BottomMenu();
        bottomMenu.setMenuList(menuList);
        bottomMenu.setOnMenuItemClickListener(onMenuItemClickListener);
        bottomMenu.show();
        return bottomMenu;
    }

    public static BottomMenu showStringList(List<String> menuList) {
        BottomMenu bottomMenu = new BottomMenu();
        bottomMenu.setMenuStringList(menuList);
        bottomMenu.show();
        return bottomMenu;
    }

    public static BottomMenu showStringList(List<String> menuList, OnMenuItemClickListener<BottomMenu> onMenuItemClickListener) {
        BottomMenu bottomMenu = new BottomMenu();
        bottomMenu.setMenuStringList(menuList);
        bottomMenu.setOnMenuItemClickListener(onMenuItemClickListener);
        bottomMenu.show();
        return bottomMenu;
    }

    public static BottomMenu show(String... menuList) {
        BottomMenu bottomMenu = new BottomMenu();
        bottomMenu.setMenuList(menuList);
        bottomMenu.show();
        return bottomMenu;
    }

    public static BottomMenu show(String[] menuList, OnMenuItemClickListener<BottomMenu> onMenuItemClickListener) {
        BottomMenu bottomMenu = new BottomMenu();
        bottomMenu.setMenuList(menuList);
        bottomMenu.setOnMenuItemClickListener(onMenuItemClickListener);
        bottomMenu.show();
        return bottomMenu;
    }

    public static BottomMenu show(CharSequence[] menuList) {
        BottomMenu bottomMenu = new BottomMenu();
        bottomMenu.setMenuList(menuList);
        bottomMenu.show();
        return bottomMenu;
    }

    public static BottomMenu show(CharSequence[] menuList, OnMenuItemClickListener<BottomMenu> onMenuItemClickListener) {
        BottomMenu bottomMenu = new BottomMenu();
        bottomMenu.setMenuList(menuList);
        bottomMenu.setOnMenuItemClickListener(onMenuItemClickListener);
        bottomMenu.show();
        return bottomMenu;
    }

    public static BottomMenu show(CharSequence title, CharSequence message, List<CharSequence> menuList) {
        BottomMenu bottomMenu = new BottomMenu();
        bottomMenu.title = title;
        bottomMenu.message = message;
        bottomMenu.setMenuList(menuList);
        bottomMenu.show();
        return bottomMenu;
    }

    public static BottomMenu show(CharSequence title, CharSequence message, List<CharSequence> menuList, OnMenuItemClickListener<BottomMenu> onMenuItemClickListener) {
        BottomMenu bottomMenu = new BottomMenu();
        bottomMenu.title = title;
        bottomMenu.message = message;
        bottomMenu.setMenuList(menuList);
        bottomMenu.setOnMenuItemClickListener(onMenuItemClickListener);
        bottomMenu.show();
        return bottomMenu;
    }

    public static BottomMenu show(CharSequence title, List<CharSequence> menuList) {
        BottomMenu bottomMenu = new BottomMenu();
        bottomMenu.title = title;
        bottomMenu.setMenuList(menuList);
        bottomMenu.show();
        return bottomMenu;
    }

    public static BottomMenu show(CharSequence title, List<CharSequence> menuList, OnMenuItemClickListener<BottomMenu> onMenuItemClickListener) {
        BottomMenu bottomMenu = new BottomMenu();
        bottomMenu.title = title;
        bottomMenu.setMenuList(menuList);
        bottomMenu.setOnMenuItemClickListener(onMenuItemClickListener);
        bottomMenu.show();
        return bottomMenu;
    }

    public static BottomMenu showStringList(CharSequence title, CharSequence message, List<String> menuList) {
        BottomMenu bottomMenu = new BottomMenu();
        bottomMenu.title = title;
        bottomMenu.message = message;
        bottomMenu.setMenuStringList(menuList);
        bottomMenu.show();
        return bottomMenu;
    }

    public static BottomMenu showStringList(CharSequence title, CharSequence message, List<String> menuList, OnMenuItemClickListener<BottomMenu> onMenuItemClickListener) {
        BottomMenu bottomMenu = new BottomMenu();
        bottomMenu.title = title;
        bottomMenu.message = message;
        bottomMenu.setMenuStringList(menuList);
        bottomMenu.setOnMenuItemClickListener(onMenuItemClickListener);
        bottomMenu.show();
        return bottomMenu;
    }

    public static BottomMenu show(CharSequence title, CharSequence message, String[] menuList) {
        BottomMenu bottomMenu = new BottomMenu();
        bottomMenu.title = title;
        bottomMenu.message = message;
        bottomMenu.setMenuList(menuList);
        bottomMenu.show();
        return bottomMenu;
    }

    public static BottomMenu show(CharSequence title, CharSequence message, String[] menuList, OnMenuItemClickListener<BottomMenu> onMenuItemClickListener) {
        BottomMenu bottomMenu = new BottomMenu();
        bottomMenu.title = title;
        bottomMenu.message = message;
        bottomMenu.setMenuList(menuList);
        bottomMenu.setOnMenuItemClickListener(onMenuItemClickListener);
        bottomMenu.show();
        return bottomMenu;
    }

    public static BottomMenu show(CharSequence title, CharSequence message, CharSequence[] menuList) {
        BottomMenu bottomMenu = new BottomMenu();
        bottomMenu.title = title;
        bottomMenu.message = message;
        bottomMenu.setMenuList(menuList);
        bottomMenu.show();
        return bottomMenu;
    }

    public static BottomMenu show(CharSequence title, CharSequence message, CharSequence[] menuList, OnMenuItemClickListener<BottomMenu> onMenuItemClickListener) {
        BottomMenu bottomMenu = new BottomMenu();
        bottomMenu.title = title;
        bottomMenu.message = message;
        bottomMenu.setMenuList(menuList);
        bottomMenu.setOnMenuItemClickListener(onMenuItemClickListener);
        bottomMenu.show();
        return bottomMenu;
    }

    public static BottomMenu show(String title, String message, List<CharSequence> menuList) {
        BottomMenu bottomMenu = new BottomMenu();
        bottomMenu.title = title;
        bottomMenu.message = message;
        bottomMenu.setMenuList(menuList);
        bottomMenu.show();
        return bottomMenu;
    }

    public static BottomMenu show(String title, String message, List<CharSequence> menuList, OnMenuItemClickListener<BottomMenu> onMenuItemClickListener) {
        BottomMenu bottomMenu = new BottomMenu();
        bottomMenu.title = title;
        bottomMenu.message = message;
        bottomMenu.setMenuList(menuList);
        bottomMenu.setOnMenuItemClickListener(onMenuItemClickListener);
        bottomMenu.show();
        return bottomMenu;
    }

    public static BottomMenu showStringList(String title, String message, List<String> menuList) {
        BottomMenu bottomMenu = new BottomMenu();
        bottomMenu.title = title;
        bottomMenu.message = message;
        bottomMenu.setMenuStringList(menuList);
        bottomMenu.show();
        return bottomMenu;
    }

    public static BottomMenu showStringList(String title, String message, List<String> menuList, OnMenuItemClickListener<BottomMenu> onMenuItemClickListener) {
        BottomMenu bottomMenu = new BottomMenu();
        bottomMenu.title = title;
        bottomMenu.message = message;
        bottomMenu.setMenuStringList(menuList);
        bottomMenu.setOnMenuItemClickListener(onMenuItemClickListener);
        bottomMenu.show();
        return bottomMenu;
    }

    public static BottomMenu show(String title, String message, String[] menuList) {
        BottomMenu bottomMenu = new BottomMenu();
        bottomMenu.title = title;
        bottomMenu.message = message;
        bottomMenu.setMenuList(menuList);
        bottomMenu.show();
        return bottomMenu;
    }

    public static BottomMenu show(String title, String message, String[] menuList, OnMenuItemClickListener<BottomMenu> onMenuItemClickListener) {
        BottomMenu bottomMenu = new BottomMenu();
        bottomMenu.title = title;
        bottomMenu.message = message;
        bottomMenu.setMenuList(menuList);
        bottomMenu.setOnMenuItemClickListener(onMenuItemClickListener);
        bottomMenu.show();
        return bottomMenu;
    }

    public static BottomMenu show(String title, String message, CharSequence[] menuList) {
        BottomMenu bottomMenu = new BottomMenu();
        bottomMenu.title = title;
        bottomMenu.message = message;
        bottomMenu.setMenuList(menuList);
        bottomMenu.show();
        return bottomMenu;
    }

    public static BottomMenu show(String title, String message, CharSequence[] menuList, OnMenuItemClickListener<BottomMenu> onMenuItemClickListener) {
        BottomMenu bottomMenu = new BottomMenu();
        bottomMenu.title = title;
        bottomMenu.message = message;
        bottomMenu.setMenuList(menuList);
        bottomMenu.setOnMenuItemClickListener(onMenuItemClickListener);
        bottomMenu.show();
        return bottomMenu;
    }

    public static BottomMenu show(int titleResId, int messageResId, List<CharSequence> menuList) {
        BottomMenu bottomMenu = new BottomMenu();
        bottomMenu.title = bottomMenu.getString(titleResId);
        bottomMenu.message = bottomMenu.getString(messageResId);
        bottomMenu.setMenuList(menuList);
        bottomMenu.show();
        return bottomMenu;
    }

    public static BottomMenu show(int titleResId, List<CharSequence> menuList) {
        BottomMenu bottomMenu = new BottomMenu();
        bottomMenu.title = bottomMenu.getString(titleResId);
        bottomMenu.setMenuList(menuList);
        bottomMenu.show();
        return bottomMenu;
    }

    public static BottomMenu showStringList(int titleResId, int messageResId, List<String> menuList) {
        BottomMenu bottomMenu = new BottomMenu();
        bottomMenu.title = bottomMenu.getString(titleResId);
        bottomMenu.message = bottomMenu.getString(messageResId);
        bottomMenu.setMenuStringList(menuList);
        bottomMenu.show();
        return bottomMenu;
    }

    public static BottomMenu show(int titleResId, int messageResId, String[] menuList) {
        BottomMenu bottomMenu = new BottomMenu();
        bottomMenu.title = bottomMenu.getString(titleResId);
        bottomMenu.message = bottomMenu.getString(messageResId);
        bottomMenu.setMenuList(menuList);
        bottomMenu.show();
        return bottomMenu;
    }

    public static BottomMenu show(int titleResId, int messageResId, CharSequence[] menuList) {
        BottomMenu bottomMenu = new BottomMenu();
        bottomMenu.title = bottomMenu.getString(titleResId);
        bottomMenu.message = bottomMenu.getString(messageResId);
        bottomMenu.setMenuList(menuList);
        bottomMenu.show();
        return bottomMenu;
    }

    public static BottomMenu show(int titleResId, int messageResId, List<CharSequence> menuList, OnMenuItemClickListener<BottomMenu> onMenuItemClickListener) {
        BottomMenu bottomMenu = new BottomMenu();
        bottomMenu.title = bottomMenu.getString(titleResId);
        bottomMenu.message = bottomMenu.getString(messageResId);
        bottomMenu.setOnMenuItemClickListener(onMenuItemClickListener);
        bottomMenu.setMenuList(menuList);
        bottomMenu.show();
        return bottomMenu;
    }

    public static BottomMenu show(int titleResId, List<CharSequence> menuList, OnMenuItemClickListener<BottomMenu> onMenuItemClickListener) {
        BottomMenu bottomMenu = new BottomMenu();
        bottomMenu.title = bottomMenu.getString(titleResId);
        bottomMenu.setOnMenuItemClickListener(onMenuItemClickListener);
        bottomMenu.setMenuList(menuList);
        bottomMenu.show();
        return bottomMenu;
    }

    public static BottomMenu showStringList(int titleResId, int messageResId, List<String> menuList, OnMenuItemClickListener<BottomMenu> onMenuItemClickListener) {
        BottomMenu bottomMenu = new BottomMenu();
        bottomMenu.title = bottomMenu.getString(titleResId);
        bottomMenu.message = bottomMenu.getString(messageResId);
        bottomMenu.setMenuStringList(menuList);
        bottomMenu.setOnMenuItemClickListener(onMenuItemClickListener);
        bottomMenu.show();
        return bottomMenu;
    }

    public static BottomMenu show(int titleResId, int messageResId, String[] menuList, OnMenuItemClickListener<BottomMenu> onMenuItemClickListener) {
        BottomMenu bottomMenu = new BottomMenu();
        bottomMenu.title = bottomMenu.getString(titleResId);
        bottomMenu.message = bottomMenu.getString(messageResId);
        bottomMenu.setMenuList(menuList);
        bottomMenu.setOnMenuItemClickListener(onMenuItemClickListener);
        bottomMenu.show();
        return bottomMenu;
    }

    public static BottomMenu show(int titleResId, int messageResId, CharSequence[] menuList, OnMenuItemClickListener<BottomMenu> onMenuItemClickListener) {
        BottomMenu bottomMenu = new BottomMenu();
        bottomMenu.title = bottomMenu.getString(titleResId);
        bottomMenu.message = bottomMenu.getString(messageResId);
        bottomMenu.setMenuList(menuList);
        bottomMenu.setOnMenuItemClickListener(onMenuItemClickListener);
        bottomMenu.show();
        return bottomMenu;
    }

    public static BottomMenu show(CharSequence title, CharSequence[] menuList) {
        BottomMenu bottomMenu = new BottomMenu();
        bottomMenu.title = title;
        bottomMenu.setMenuList(menuList);
        bottomMenu.show();
        return bottomMenu;
    }

    public static BottomMenu show(CharSequence title, String[] menuList) {
        BottomMenu bottomMenu = new BottomMenu();
        bottomMenu.title = title;
        bottomMenu.setMenuList(menuList);
        bottomMenu.show();
        return bottomMenu;
    }

    public static BottomMenu show(CharSequence title, CharSequence[] menuList, OnMenuItemClickListener<BottomMenu> onMenuItemClickListener) {
        BottomMenu bottomMenu = new BottomMenu();
        bottomMenu.title = title;
        bottomMenu.setMenuList(menuList);
        bottomMenu.setOnMenuItemClickListener(onMenuItemClickListener);
        bottomMenu.show();
        return bottomMenu;
    }

    public static BottomMenu show(CharSequence title, String[] menuList, OnMenuItemClickListener<BottomMenu> onMenuItemClickListener) {
        BottomMenu bottomMenu = new BottomMenu();
        bottomMenu.title = title;
        bottomMenu.setMenuList(menuList);
        bottomMenu.setOnMenuItemClickListener(onMenuItemClickListener);
        bottomMenu.show();
        return bottomMenu;
    }

    public static BottomMenu show(int titleResId, CharSequence[] menuList) {
        BottomMenu bottomMenu = new BottomMenu();
        bottomMenu.title = bottomMenu.getString(titleResId);
        bottomMenu.setMenuList(menuList);
        bottomMenu.show();
        return bottomMenu;
    }

    public static BottomMenu show(int titleResId, String[] menuList) {
        BottomMenu bottomMenu = new BottomMenu();
        bottomMenu.title = bottomMenu.getString(titleResId);
        bottomMenu.setMenuList(menuList);
        bottomMenu.show();
        return bottomMenu;
    }

    public static BottomMenu show(int titleResId, CharSequence[] menuList, OnMenuItemClickListener<BottomMenu> onMenuItemClickListener) {
        BottomMenu bottomMenu = new BottomMenu();
        bottomMenu.title = bottomMenu.getString(titleResId);
        bottomMenu.setMenuList(menuList);
        bottomMenu.setOnMenuItemClickListener(onMenuItemClickListener);
        bottomMenu.show();
        return bottomMenu;
    }

    public static BottomMenu show(int titleResId, String[] menuList, OnMenuItemClickListener<BottomMenu> onMenuItemClickListener) {
        BottomMenu bottomMenu = new BottomMenu();
        bottomMenu.title = bottomMenu.getString(titleResId);
        bottomMenu.setMenuList(menuList);
        bottomMenu.setOnMenuItemClickListener(onMenuItemClickListener);
        bottomMenu.show();
        return bottomMenu;
    }

    private float touchDownY;

    public static final int ITEM_CLICK_DELAY = 100;
    private long lastClickTime = 0;
    private int[] resultArray;
    private CharSequence[] selectTextArray;

    @Override
    protected void onDialogShow() {
        if (getDialogImpl() != null) {
            getDialogImpl().boxList.setVisibility(View.VISIBLE);

            if (!isAllowInterceptTouch()) {
                getDialogImpl().bkg.setMaxHeight((int) bottomDialogMaxHeight);
                if (bottomDialogMaxHeight != 0) {
                    dialogImpl.scrollView.lockScroll(true);
                }
            }

            int dividerDrawableResId = 0;
            int dividerHeight = 1;
            if (style.overrideBottomDialogRes() != null) {
                dividerDrawableResId = style.overrideBottomDialogRes().overrideMenuDividerDrawableRes(isLightTheme());
                dividerHeight = style.overrideBottomDialogRes().overrideMenuDividerHeight(isLightTheme());
            }
            if (dividerDrawableResId == 0) {
                dividerDrawableResId = isLightTheme() ? R.drawable.rect_dialogx_material_menu_split_divider : R.drawable.rect_dialogx_material_menu_split_divider_night;
            }


            if (!isLightTheme()) {
                listView = new DialogListView(getDialogImpl(), getOwnActivity(), R.style.DialogXCompatThemeDark);
            } else {
                listView = new DialogListView(getDialogImpl(), getOwnActivity());
            }
            listView.setTag("ScrollController");
            listView.setOverScrollMode(OVER_SCROLL_NEVER);
            listView.setDivider(getResources().getDrawable(dividerDrawableResId));
            listView.setDividerHeight(dividerHeight);
            getDialogImpl().scrollView = listView;

            listView.setBottomMenuListViewTouchEvent(new BottomMenuListViewTouchEvent() {
                @Override
                public void down(MotionEvent event) {
                    touchDownY = getDialogImpl().boxBkg.getY();
                    log("#TouchDown: " + touchDownY);
                }
            });

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (!isMenuItemEnable(position)) {
                        return;
                    }
                    haptic(view);
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - lastClickTime > ITEM_CLICK_DELAY) {
                        lastClickTime = currentTime;
                        float deltaY = Math.abs(touchDownY - getDialogImpl().boxBkg.getY());
                        log("#Click:deltaY= " + deltaY);
                        if (deltaY > dip2px(15)) {
                            return;
                        }
                        selectionIndex = position;
                        log("### onMenuItemClickListener=" + onMenuItemClickListener);
                        switch (selectMode) {
                            case NONE:
                                if (onMenuItemClickListener != null) {
                                    if (!onMenuItemClickListener.onClick(me, menuList.get(position), position)) {
                                        dismiss();
                                    }
                                } else {
                                    dismiss();
                                }
                                break;
                            case SINGLE:
                                if (onMenuItemClickListener instanceof OnMenuItemSelectListener) {
                                    OnMenuItemSelectListener<BottomMenu> onMenuItemSelectListener = (OnMenuItemSelectListener<BottomMenu>) onMenuItemClickListener;
                                    if (!onMenuItemSelectListener.onClick(me, menuList.get(position), position)) {
                                        dismiss();
                                    } else {
                                        menuListAdapter.notifyDataSetInvalidated();
                                        onMenuItemSelectListener.onOneItemSelect(me, menuList.get(position), position, true);
                                    }
                                } else {
                                    if (onMenuItemClickListener != null) {
                                        if (!onMenuItemClickListener.onClick(me, menuList.get(position), position)) {
                                            dismiss();
                                        }
                                    } else {
                                        menuListAdapter.notifyDataSetInvalidated();
                                    }
                                }
                                break;
                            case MULTIPLE:
                                if (onMenuItemClickListener instanceof OnMenuItemSelectListener) {
                                    OnMenuItemSelectListener<BottomMenu> onMenuItemSelectListener = (OnMenuItemSelectListener<BottomMenu>) onMenuItemClickListener;
                                    if (!onMenuItemSelectListener.onClick(me, menuList.get(position), position)) {
                                        dismiss();
                                    } else {
                                        if (selectionItems.contains(position)) {
                                            selectionItems.remove(new Integer(position));
                                        } else {
                                            selectionItems.add(position);
                                        }
                                        menuListAdapter.notifyDataSetInvalidated();
                                        resultArray = new int[selectionItems.size()];
                                        selectTextArray = new CharSequence[selectionItems.size()];
                                        for (int i = 0; i < selectionItems.size(); i++) {
                                            resultArray[i] = selectionItems.get(i);
                                            selectTextArray[i] = menuList.get(resultArray[i]);
                                        }
                                        onMenuItemSelectListener.onMultiItemSelect(me, selectTextArray, resultArray);
                                    }
                                } else {
                                    if (onMenuItemClickListener != null) {
                                        if (!onMenuItemClickListener.onClick(me, menuList.get(position), position)) {
                                            dismiss();
                                        }
                                    } else {
                                        if (selectionItems.contains(position)) {
                                            selectionItems.remove(new Integer(position));
                                        } else {
                                            selectionItems.add(position);
                                        }
                                        menuListAdapter.notifyDataSetInvalidated();
                                        resultArray = new int[selectionItems.size()];
                                        selectTextArray = new CharSequence[selectionItems.size()];
                                        for (int i = 0; i < selectionItems.size(); i++) {
                                            resultArray[i] = selectionItems.get(i);
                                            selectTextArray[i] = menuList.get(resultArray[i]);
                                        }
                                    }
                                }
                                break;
                        }
                    }
                }
            });
            if (style.overrideBottomDialogRes() != null) {
                if (style.overrideBottomDialogRes().overrideMenuItemLayout(true, 0, 0, false) != 0) {
                    listView.setSelector(R.color.empty);
                }
            }

            ViewGroup.LayoutParams listViewLp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            getDialogImpl().boxList.addView(listView, listViewLp);
            refreshUI();
        }
    }

    @Override
    public void refreshUI() {
        if (getDialogImpl() == null) return;
        if (listView != null) {
            if (menuListAdapter == null) {
                menuListAdapter = new BottomMenuArrayAdapter(me, getOwnActivity(), menuList);
            }
            if (listView.getAdapter() == null) {
                listView.setAdapter(menuListAdapter);
            } else {
                if (listView.getAdapter() != menuListAdapter) {
                    listView.setAdapter(menuListAdapter);
                } else {
                    menuListAdapter.notifyDataSetChanged();
                }
            }
        }

        // 部分主题下选中项默认按下效果
        if (showSelectedBackgroundTips) {
            if (listView != null) {
                listView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (menuListAdapter instanceof BottomMenuArrayAdapter && showSelectedBackgroundTips) {
                            BottomMenuArrayAdapter bottomMenuArrayAdapter = ((BottomMenuArrayAdapter) menuListAdapter);

                            View selectItemView = listView.getChildAt(getSelection());
                            if (selectItemView != null) {
                                selectItemView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        selectItemView.setPressed(true);
                                    }
                                });
                            }
                        }
                    }
                });
            }

        }

        if (itemDivider != null) {
            listView.setDivider(itemDivider.createDividerDrawable(getOwnActivity(), isLightTheme()));
            listView.setDividerHeight(itemDivider.getWidth());
        }
        super.refreshUI();
    }

    public void preRefreshUI() {
        if (getDialogImpl() == null) return;
        runOnMain(new Runnable() {
            @Override
            public void run() {
                refreshUI();
            }
        });
    }

    @Override
    public String dialogKey() {
        return getClass().getSimpleName() + "(" + Integer.toHexString(hashCode()) + ")";
    }

    public List<CharSequence> getMenuList() {
        return menuList;
    }

    public BottomMenu setMenuList(List<CharSequence> menuList) {
        this.menuList = menuList;
        this.menuListAdapter = null;
        preRefreshUI();
        return this;
    }

    private boolean isSameSize(int menuListSize) {
        if (this.menuList == null || this.menuList.size() == 0) {
            return true;
        }
        return this.menuList.size() == menuListSize;
    }

    public BottomMenu setMenuStringList(List<String> menuList) {
        this.menuList = new ArrayList<>();
        this.menuList.addAll(menuList);
        this.menuListAdapter = null;
        preRefreshUI();
        return this;
    }

    public BottomMenu setMenuList(String[] menuList) {
        this.menuList = new ArrayList<>();
        this.menuList.addAll(Arrays.asList(menuList));
        this.menuListAdapter = null;
        preRefreshUI();
        return this;
    }

    public BottomMenu setMenuList(CharSequence[] menuList) {
        this.menuList = Arrays.asList(menuList);
        this.menuListAdapter = null;
        preRefreshUI();
        return this;
    }

    public BottomMenu setMenus(CharSequence... menuList) {
        this.menuList = Arrays.asList(menuList);
        this.menuListAdapter = null;
        preRefreshUI();
        return this;
    }

    public BottomMenu setMenus(String... menuList) {
        this.menuList = Arrays.asList(menuList);
        this.menuListAdapter = null;
        preRefreshUI();
        return this;
    }

    public BottomMenu setMenus(int... menuListResId) {
        this.menuList = Arrays.asList(getTextArray(menuListResId));
        this.menuListAdapter = null;
        preRefreshUI();
        return this;
    }

    private String[] getTextArray(int[] menuListResId) {
        String[] result = new String[menuListResId == null ? 0 : menuListResId.length];
        for (int i = 0; i < (menuListResId == null ? 0 : menuListResId.length); i++) {
            result[i] = getString(menuListResId[i]);
        }
        return result;
    }

    public OnIconChangeCallBack<BottomMenu> getOnIconChangeCallBack() {
        return onIconChangeCallBack;
    }

    public BottomMenu setOnIconChangeCallBack(OnIconChangeCallBack<BottomMenu> onIconChangeCallBack) {
        this.onIconChangeCallBack = onIconChangeCallBack;
        return this;
    }

    public OnBackPressedListener<BottomDialog> getOnBackPressedListener() {
        return (OnBackPressedListener<BottomDialog>) onBackPressedListener;
    }

    public BottomMenu setOnBackPressedListener(OnBackPressedListener<BottomDialog> onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
        preRefreshUI();
        return this;
    }

    public BottomMenu setDialogLifecycleCallback(DialogLifecycleCallback<BottomDialog> dialogLifecycleCallback) {
        this.dialogLifecycleCallback = dialogLifecycleCallback;
        if (isShow) dialogLifecycleCallback.onShow(me);
        return this;
    }

    public BottomMenu setStyle(DialogXStyle style) {
        this.style = style;
        return this;
    }

    public BottomMenu setTheme(DialogX.THEME theme) {
        this.theme = theme;
        return this;
    }

    public boolean isCancelable() {
        if (privateCancelable != null) {
            return privateCancelable == BOOLEAN.TRUE;
        }
        if (overrideCancelable != null) {
            return overrideCancelable == BOOLEAN.TRUE;
        }
        return cancelable;
    }

    public BottomMenu setCancelable(boolean cancelable) {
        this.privateCancelable = cancelable ? BOOLEAN.TRUE : BOOLEAN.FALSE;
        preRefreshUI();
        return this;
    }

    public DialogImpl getDialogImpl() {
        return dialogImpl;
    }

    public CharSequence getTitle() {
        return title;
    }

    public BottomMenu setTitle(CharSequence title) {
        this.title = title;
        preRefreshUI();
        return this;
    }

    public BottomMenu setTitle(int titleResId) {
        this.title = getString(titleResId);
        preRefreshUI();
        return this;
    }

    public CharSequence getMessage() {
        return message;
    }

    public BottomMenu setMessage(CharSequence message) {
        this.message = message;
        preRefreshUI();
        return this;
    }

    public BottomMenu setMessage(int messageResId) {
        this.message = getString(messageResId);
        preRefreshUI();
        return this;
    }

    public CharSequence getCancelButton() {
        return cancelText;
    }

    public BottomMenu setCancelButton(CharSequence cancelText) {
        this.cancelText = cancelText;
        preRefreshUI();
        return this;
    }

    public BottomMenu setCancelButton(int cancelTextResId) {
        this.cancelText = getString(cancelTextResId);
        preRefreshUI();
        return this;
    }

    public BottomMenu setCancelButton(OnMenuButtonClickListener<BottomMenu> cancelButtonClickListener) {
        this.cancelButtonClickListener = cancelButtonClickListener;
        return this;
    }

    public BottomMenu setCancelButton(CharSequence cancelText, OnMenuButtonClickListener<BottomMenu> cancelButtonClickListener) {
        this.cancelText = cancelText;
        this.cancelButtonClickListener = cancelButtonClickListener;
        preRefreshUI();
        return this;
    }

    public BottomMenu setCancelButton(int cancelTextResId, OnMenuButtonClickListener<BottomMenu> cancelButtonClickListener) {
        this.cancelText = getString(cancelTextResId);
        this.cancelButtonClickListener = cancelButtonClickListener;
        preRefreshUI();
        return this;
    }

    /**
     * 建议使用 {@link com.kongzue.dialogx.dialogs.BottomMenu#setCancelButton(OnMenuButtonClickListener <BottomMenu>)}
     */
    @Deprecated
    public BottomMenu setCancelButton(OnDialogButtonClickListener<BottomDialog> cancelButtonClickListener) {
        this.cancelButtonClickListener = cancelButtonClickListener;
        return this;
    }

    /**
     * 建议使用 {@link com.kongzue.dialogx.dialogs.BottomMenu#setCancelButton(CharSequence cancelText, OnMenuButtonClickListener <BottomMenu>)}
     */
    @Deprecated
    public BottomMenu setCancelButton(CharSequence cancelText, OnDialogButtonClickListener<BottomDialog> cancelButtonClickListener) {
        this.cancelText = cancelText;
        this.cancelButtonClickListener = cancelButtonClickListener;
        preRefreshUI();
        return this;
    }

    /**
     * 建议使用 {@link com.kongzue.dialogx.dialogs.BottomMenu#setCancelButton(int cancelTextResId, OnMenuButtonClickListener <BottomMenu>)}
     */
    @Deprecated
    public BottomMenu setCancelButton(int cancelTextResId, OnDialogButtonClickListener<BottomDialog> cancelButtonClickListener) {
        this.cancelText = getString(cancelTextResId);
        this.cancelButtonClickListener = cancelButtonClickListener;
        preRefreshUI();
        return this;
    }

    public BottomMenu setCustomView(OnBindView<BottomDialog> onBindView) {
        this.onBindView = onBindView;
        preRefreshUI();
        return this;
    }

    public View getCustomView() {
        if (onBindView == null) return null;
        return onBindView.getCustomView();
    }

    public BottomMenu removeCustomView() {
        this.onBindView.clean();
        preRefreshUI();
        return this;
    }

    public boolean isAllowInterceptTouch() {
        return super.isAllowInterceptTouch();
    }

    public BottomMenu setAllowInterceptTouch(boolean allowInterceptTouch) {
        this.allowInterceptTouch = allowInterceptTouch;
        preRefreshUI();
        return this;
    }

    public float getBottomDialogMaxHeight() {
        return bottomDialogMaxHeight;
    }

    public BottomMenu setBottomDialogMaxHeight(float bottomDialogMaxHeight) {
        this.bottomDialogMaxHeight = bottomDialogMaxHeight;
        return this;
    }

    public OnMenuItemClickListener<BottomMenu> getOnMenuItemClickListener() {
        return onMenuItemClickListener;
    }

    public BottomMenu setOnMenuItemClickListener(OnMenuItemClickListener<BottomMenu> onMenuItemClickListener) {
        this.onMenuItemClickListener = onMenuItemClickListener;
        return this;
    }

    public BaseAdapter getMenuListAdapter() {
        return menuListAdapter;
    }

    public BottomMenu setMenuListAdapter(BaseAdapter menuListAdapter) {
        this.menuListAdapter = menuListAdapter;
        return this;
    }

    public OnMenuButtonClickListener<BottomMenu> getBottomMenuCancelButtonClickListener() {
        return (OnMenuButtonClickListener<BottomMenu>) cancelButtonClickListener;
    }

    public BottomMenu setCancelButtonClickListener(OnMenuButtonClickListener<BottomMenu> cancelButtonClickListener) {
        this.cancelButtonClickListener = cancelButtonClickListener;
        return this;
    }

    public TextInfo getTitleTextInfo() {
        return titleTextInfo;
    }

    public BottomMenu setTitleTextInfo(TextInfo titleTextInfo) {
        this.titleTextInfo = titleTextInfo;
        preRefreshUI();
        return this;
    }

    public TextInfo getMessageTextInfo() {
        return messageTextInfo;
    }

    public BottomMenu setMessageTextInfo(TextInfo messageTextInfo) {
        this.messageTextInfo = messageTextInfo;
        preRefreshUI();
        return this;
    }

    public TextInfo getCancelTextInfo() {
        return cancelTextInfo;
    }

    public BottomMenu setCancelTextInfo(TextInfo cancelTextInfo) {
        this.cancelTextInfo = cancelTextInfo;
        preRefreshUI();
        return this;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public BottomMenu setBackgroundColor(@ColorInt int backgroundColor) {
        this.backgroundColor = backgroundColor;
        preRefreshUI();
        return this;
    }

    public int getSelection() {
        return selectionIndex;
    }

    public ArrayList<Integer> getSelectionList() {
        return selectionItems;
    }

    public BottomMenu setSelection(int selectionIndex) {
        this.selectMode = SELECT_MODE.SINGLE;
        this.selectionIndex = selectionIndex;
        this.selectionItems = null;
        menuListAdapter = null;
        preRefreshUI();
        return this;
    }

    public BottomMenu setSingleSelection() {
        this.selectMode = SELECT_MODE.SINGLE;
        this.selectionIndex = -1;
        this.selectionItems = null;
        menuListAdapter = null;
        preRefreshUI();
        return this;
    }

    public BottomMenu setSelection(int[] selectionItems) {
        this.selectMode = SELECT_MODE.MULTIPLE;
        this.selectionIndex = -1;
        this.selectionItems = new ArrayList<>();
        if (selectionItems != null) {
            for (int itemIndex : selectionItems) {
                this.selectionItems.add(itemIndex);
            }
        }
        menuListAdapter = null;
        preRefreshUI();
        return this;
    }

    public BottomMenu setMultiSelection() {
        this.selectMode = SELECT_MODE.MULTIPLE;
        this.selectionIndex = -1;
        this.selectionItems = new ArrayList<>();
        menuListAdapter = null;
        preRefreshUI();
        return this;
    }

    public BottomMenu setSelection(List<Integer> selectionItems) {
        this.selectMode = SELECT_MODE.MULTIPLE;
        this.selectionIndex = -1;
        this.selectionItems = new ArrayList<>(selectionItems);
        menuListAdapter = null;
        preRefreshUI();
        return this;
    }

    public BottomMenu setNoSelect() {
        this.selectMode = SELECT_MODE.NONE;
        this.selectionIndex = -1;
        this.selectionItems = null;
        menuListAdapter = null;
        preRefreshUI();
        return this;
    }

    public BottomMenu setBackgroundColorRes(@ColorRes int backgroundRes) {
        this.backgroundColor = getColor(backgroundRes);
        preRefreshUI();
        return this;
    }

    public CharSequence getOkButton() {
        return okText;
    }

    public BottomMenu setOkButton(CharSequence okText) {
        this.okText = okText;
        preRefreshUI();
        return this;
    }

    public BottomMenu setOkButton(int OkTextResId) {
        this.okText = getString(OkTextResId);
        preRefreshUI();
        return this;
    }

    public BottomMenu setOkButton(OnMenuButtonClickListener<BottomMenu> okButtonClickListener) {
        this.okButtonClickListener = okButtonClickListener;
        return this;
    }

    public BottomMenu setOkButton(CharSequence okText, OnMenuButtonClickListener<BottomMenu> okButtonClickListener) {
        this.okText = okText;
        this.okButtonClickListener = okButtonClickListener;
        return this;
    }

    public BottomMenu setOkButton(int okTextResId, OnMenuButtonClickListener<BottomMenu> okButtonClickListener) {
        this.okText = getString(okTextResId);
        this.okButtonClickListener = okButtonClickListener;
        return this;
    }

    public BottomMenu setHapticFeedbackEnabled(boolean isHapticFeedbackEnabled) {
        this.isHapticFeedbackEnabled = isHapticFeedbackEnabled ? 1 : 0;
        return this;
    }

    /**
     * 建议使用 {@link com.kongzue.dialogx.dialogs.BottomMenu#setOkButton(OnMenuButtonClickListener <BottomMenu>)}
     */
    @Deprecated
    public BottomMenu setOkButton(OnDialogButtonClickListener<BottomDialog> okButtonClickListener) {
        this.okButtonClickListener = okButtonClickListener;
        return this;
    }

    /**
     * 建议使用 {@link com.kongzue.dialogx.dialogs.BottomMenu#setOkButton(CharSequence okText, OnMenuButtonClickListener <BottomMenu>)}
     */
    @Deprecated
    public BottomMenu setOkButton(CharSequence okText, OnDialogButtonClickListener<BottomDialog> okButtonClickListener) {
        this.okText = okText;
        this.okButtonClickListener = okButtonClickListener;
        return this;
    }

    /**
     * 建议使用 {@link com.kongzue.dialogx.dialogs.BottomMenu#setOkButton(int okTextResId, OnMenuButtonClickListener <BottomMenu>)}
     */
    @Deprecated
    public BottomMenu setOkButton(int okTextResId, OnDialogButtonClickListener<BottomDialog> okButtonClickListener) {
        this.okText = getString(okTextResId);
        this.okButtonClickListener = okButtonClickListener;
        return this;
    }

    public CharSequence getOtherButton() {
        return otherText;
    }

    public BottomMenu setOtherButton(CharSequence otherText) {
        this.otherText = otherText;
        preRefreshUI();
        return this;
    }

    public BottomMenu setOtherButton(int OtherTextResId) {
        this.otherText = getString(OtherTextResId);
        preRefreshUI();
        return this;
    }

    public BottomMenu setOtherButton(OnMenuButtonClickListener<BottomMenu> otherButtonClickListener) {
        this.otherButtonClickListener = otherButtonClickListener;
        return this;
    }

    public BottomMenu setOtherButton(CharSequence otherText, OnMenuButtonClickListener<BottomMenu> otherButtonClickListener) {
        this.otherText = otherText;
        this.otherButtonClickListener = otherButtonClickListener;
        return this;
    }

    public BottomMenu setOtherButton(int otherTextResId, OnMenuButtonClickListener<BottomMenu> otherButtonClickListener) {
        this.otherText = getString(otherTextResId);
        this.otherButtonClickListener = otherButtonClickListener;
        return this;
    }

    /**
     * 建议使用 {@link com.kongzue.dialogx.dialogs.BottomMenu#setOtherButton(OnMenuButtonClickListener <BottomMenu>)}
     */
    @Deprecated
    public BottomMenu setOtherButton(OnDialogButtonClickListener<BottomDialog> otherButtonClickListener) {
        this.otherButtonClickListener = otherButtonClickListener;
        return this;
    }

    /**
     * 建议使用 {@link com.kongzue.dialogx.dialogs.BottomMenu#setOtherButton(CharSequence otherText, OnMenuButtonClickListener <BottomMenu>)}
     */
    @Deprecated
    public BottomMenu setOtherButton(CharSequence otherText, OnDialogButtonClickListener<BottomDialog> otherButtonClickListener) {
        this.otherText = otherText;
        this.otherButtonClickListener = otherButtonClickListener;
        return this;
    }

    /**
     * 建议使用 {@link com.kongzue.dialogx.dialogs.BottomMenu#setOtherButton(int otherTextResId, OnMenuButtonClickListener <BottomMenu>)}
     */
    @Deprecated
    public BottomMenu setOtherButton(int otherTextResId, OnDialogButtonClickListener<BottomDialog> otherButtonClickListener) {
        this.otherText = getString(otherTextResId);
        this.otherButtonClickListener = otherButtonClickListener;
        return this;
    }

    public BottomMenu setMaskColor(@ColorInt int maskColor) {
        this.maskColor = maskColor;
        preRefreshUI();
        return this;
    }

    public long getEnterAnimDuration() {
        return enterAnimDuration;
    }

    public BottomMenu setEnterAnimDuration(long enterAnimDuration) {
        this.enterAnimDuration = enterAnimDuration;
        return this;
    }

    public long getExitAnimDuration() {
        return exitAnimDuration;
    }

    public BottomMenu setExitAnimDuration(long exitAnimDuration) {
        this.exitAnimDuration = exitAnimDuration;
        return this;
    }

    public SELECT_MODE getSelectMode() {
        return selectMode;
    }

    @Override
    protected void shutdown() {
        dismiss();
    }

    public BottomMenu setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
        refreshUI();
        return this;
    }

    public BottomMenu setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
        refreshUI();
        return this;
    }

    public BottomMenu setMinHeight(int minHeight) {
        this.minHeight = minHeight;
        refreshUI();
        return this;
    }

    public BottomMenu setMinWidth(int minWidth) {
        this.minWidth = minWidth;
        refreshUI();
        return this;
    }

    public BottomMenu setDialogImplMode(DialogX.IMPL_MODE dialogImplMode) {
        this.dialogImplMode = dialogImplMode;
        return this;
    }

    public TextInfo getMenuTextInfo() {
        if (menuTextInfo == null) return DialogX.menuTextInfo;
        return menuTextInfo;
    }

    public BottomMenu setMenuTextInfo(TextInfo menuTextInfo) {
        this.menuTextInfo = menuTextInfo;
        return this;
    }

    public MenuItemTextInfoInterceptor<BottomMenu> getMenuItemTextInfoInterceptor() {
        return menuItemTextInfoInterceptor;
    }

    public BottomMenu setMenuItemTextInfoInterceptor(MenuItemTextInfoInterceptor<BottomMenu> menuItemTextInfoInterceptor) {
        this.menuItemTextInfoInterceptor = menuItemTextInfoInterceptor;
        return this;
    }

    public boolean isBkgInterceptTouch() {
        return bkgInterceptTouch;
    }

    public BottomMenu setBkgInterceptTouch(boolean bkgInterceptTouch) {
        this.bkgInterceptTouch = bkgInterceptTouch;
        return this;
    }

    public OnBackgroundMaskClickListener<BottomDialog> getOnBackgroundMaskClickListener() {
        return onBackgroundMaskClickListener;
    }

    public BottomMenu setOnBackgroundMaskClickListener(OnBackgroundMaskClickListener<BottomDialog> onBackgroundMaskClickListener) {
        this.onBackgroundMaskClickListener = onBackgroundMaskClickListener;
        return this;
    }

    public BottomMenu setRadius(float radiusPx) {
        backgroundRadius = radiusPx;
        refreshUI();
        return this;
    }

    public float getRadius() {
        return backgroundRadius;
    }

    public BottomMenu setTitleIcon(Bitmap titleIcon) {
        this.titleIcon = new BitmapDrawable(getResources(), titleIcon);
        refreshUI();
        return this;
    }

    public BottomMenu setTitleIcon(int titleIconResId) {
        this.titleIcon = getResources().getDrawable(titleIconResId);
        refreshUI();
        return this;
    }

    public BottomMenu setTitleIcon(Drawable titleIcon) {
        this.titleIcon = titleIcon;
        refreshUI();
        return this;
    }

    public DialogXAnimInterface<BottomDialog> getDialogXAnimImpl() {
        return dialogXAnimImpl;
    }

    public BottomMenu setDialogXAnimImpl(DialogXAnimInterface<BottomDialog> dialogXAnimImpl) {
        this.dialogXAnimImpl = dialogXAnimImpl;
        return this;
    }

    public BottomMenu setRootPadding(int padding) {
        this.screenPaddings = new int[]{padding, padding, padding, padding};
        refreshUI();
        return this;
    }

    public BottomMenu setRootPadding(int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {
        this.screenPaddings = new int[]{paddingLeft, paddingTop, paddingRight, paddingBottom};
        refreshUI();
        return this;
    }

    public boolean isShowSelectedBackgroundTips() {
        return showSelectedBackgroundTips;
    }

    public BottomMenu setShowSelectedBackgroundTips(boolean showSelectedBackgroundTips) {
        this.showSelectedBackgroundTips = showSelectedBackgroundTips;
        refreshUI();
        return this;
    }

    // 返回点击的菜单索引
    public int getSelectionIndex() {
        return selectionIndex;
    }

    // 返回多选时，选择的菜单索引集合
    public int[] getSelectionIndexArray() {
        return resultArray;
    }

    // 返回多选时，选择的菜单文本集合
    public CharSequence[] getSelectTextArray() {
        return selectTextArray;
    }

    public MenuItemLayoutRefreshCallback<BottomMenu> getMenuMenuItemLayoutRefreshCallback() {
        return menuMenuItemLayoutRefreshCallback;
    }

    public BottomMenu setMenuMenuItemLayoutRefreshCallback(MenuItemLayoutRefreshCallback<BottomMenu> menuMenuItemLayoutRefreshCallback) {
        this.menuMenuItemLayoutRefreshCallback = menuMenuItemLayoutRefreshCallback;
        return this;
    }

    public TextInfo getOkTextInfo() {
        return okTextInfo;
    }

    public BottomMenu setOkTextInfo(TextInfo okTextInfo) {
        this.okTextInfo = okTextInfo;
        return this;
    }

    public TextInfo getOtherTextInfo() {
        return otherTextInfo;
    }

    public BottomMenu setOtherTextInfo(TextInfo otherTextInfo) {
        this.otherTextInfo = otherTextInfo;
        return this;
    }

    public BottomMenu setScrollableWhenContentLargeThanVisibleRange(boolean scrollableWhenContentLargeThanVisibleRange) {
        this.scrollableWhenContentLargeThanVisibleRange = scrollableWhenContentLargeThanVisibleRange;
        return this;
    }

    public BottomMenu setData(String key, Object obj) {
        if (data == null) data = new HashMap<>();
        data.put(key, obj);
        return this;
    }

    public BottomMenu onShow(DialogXRunnable<BottomDialog> dialogXRunnable) {
        onShowRunnable = dialogXRunnable;
        if (isShow() && onShowRunnable != null) {
            onShowRunnable.run(this);
        }
        return this;
    }

    public BottomMenu onDismiss(DialogXRunnable<BottomDialog> dialogXRunnable) {
        onDismissRunnable = dialogXRunnable;
        return this;
    }

    public BottomMenu setEnableImmersiveMode(boolean enableImmersiveMode) {
        this.enableImmersiveMode = enableImmersiveMode;
        refreshUI();
        return this;
    }

    public List<Integer> getIconResIds() {
        return iconResIds;
    }

    public int getIconResIds(int position) {
        if (iconResIds != null && position >= 0 && position < iconResIds.size()) {
            return iconResIds.get(position);
        }
        return 0;
    }

    public BottomMenu setIconResIds(List<Integer> iconResIds) {
        this.iconResIds = iconResIds;
        refreshUI();
        return this;
    }

    public BottomMenu setIconResIds(int... resIds) {
        if (iconResIds == null) {
            iconResIds = new ArrayList<>();
        }
        for (int id : resIds) {
            iconResIds.add(id);
        }
        refreshUI();
        return this;
    }

    public boolean isAutoTintIconInLightOrDarkMode() {
        return autoTintIconInLightOrDarkMode;
    }

    public BottomMenu setAutoTintIconInLightOrDarkMode(boolean autoTintIconInLightOrDarkMode) {
        this.autoTintIconInLightOrDarkMode = autoTintIconInLightOrDarkMode;
        refreshUI();
        return this;
    }

    public BottomMenu appendMessage(CharSequence message) {
        this.message = TextUtils.concat(this.message, message);
        refreshUI();
        return this;
    }

    public BottomMenu setThisOrderIndex(int orderIndex) {
        this.thisOrderIndex = orderIndex;
        if (getDialogView() != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getDialogView().setTranslationZ(orderIndex);
            } else {
                error("DialogX: " + dialogKey() + " 执行 .setThisOrderIndex(" + orderIndex + ") 失败：系统不支持此方法，SDK-API 版本必须大于 21（LOLLIPOP）");
            }
        }
        return this;
    }

    public BottomMenu bringToFront() {
        setThisOrderIndex(getHighestOrderIndex());
        return this;
    }

    public BottomMenu enableMenu(int... menuIndex) {
        for (int i : menuIndex) {
            menuUsability.put(i, true);
        }
        return this;
    }

    public BottomMenu enableMenu(CharSequence... menuText) {
        if (menuList != null && !menuList.isEmpty()) {
            for (CharSequence c : menuText) {
                int index = menuList.indexOf(c);
                menuUsability.put(index, true);
            }
        } else {
            error("DialogX: " + dialogKey() + " .enableMenu(" + menuText + ")执行失败，请先初始化菜单项 menuList");
        }
        return this;
    }

    public BottomMenu enableMenu(String... menuText) {
        if (menuList != null && !menuList.isEmpty()) {
            for (String c : menuText) {
                int index = menuList.indexOf(c);
                menuUsability.put(index, true);
            }
        } else {
            error("DialogX: " + dialogKey() + " .enableMenu(" + menuText + ")执行失败，请先初始化菜单项 menuList");
        }
        return this;
    }

    public BottomMenu disableMenu(int... menuIndex) {
        for (int i : menuIndex) {
            menuUsability.put(i, false);
        }
        return this;
    }

    public BottomMenu disableMenu(CharSequence... menuText) {
        if (menuList != null && !menuList.isEmpty()) {
            for (CharSequence c : menuText) {
                int index = menuList.indexOf(c);
                menuUsability.put(index, false);
            }
        } else {
            error("DialogX: " + dialogKey() + " .disableMenu(" + menuText + ")执行失败，请先初始化菜单项 menuList");
        }
        return this;
    }

    public BottomMenu disableMenu(String... menuText) {
        if (menuList != null && !menuList.isEmpty()) {
            for (String c : menuText) {
                int index = menuList.indexOf(c);
                menuUsability.put(index, false);
            }
        } else {
            error("DialogX: " + dialogKey() + " .disableMenu(" + menuText + ")执行失败，请先初始化菜单项 menuList");
        }
        return this;
    }

    public boolean isMenuItemEnable(int index) {
        Boolean enabled = menuUsability.get(index);
        if (enabled == null) {
            return true;
        }
        return enabled;
    }

    public BottomMenu setActionRunnable(int actionId, DialogXRunnable<BottomDialog> runnable) {
        dialogActionRunnableMap.put(actionId, runnable);
        return this;
    }

    public BottomMenu cleanAction(int actionId) {
        dialogActionRunnableMap.remove(actionId);
        return this;
    }

    public BottomMenu cleanAllAction() {
        dialogActionRunnableMap.clear();
        return this;
    }

    // for BaseDialog use
    protected void callDialogDismissPrivate() {
        dismiss();
    }

    public BottomMenu bindDismissWithLifecycleOwner(LifecycleOwner owner) {
        super.bindDismissWithLifecycleOwnerPrivate(owner);
        return this;
    }

    public BottomMenu setItemDivider(ItemDivider itemDivider) {
        this.itemDivider = itemDivider;
        refreshUI();
        return this;
    }
}
