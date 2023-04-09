package com.kongzue.dialogx.dialogs;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;

import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.R;
import com.kongzue.dialogx.interfaces.BottomMenuListViewTouchEvent;
import com.kongzue.dialogx.interfaces.DialogLifecycleCallback;
import com.kongzue.dialogx.interfaces.DialogXAnimInterface;
import com.kongzue.dialogx.interfaces.DialogXStyle;
import com.kongzue.dialogx.interfaces.MenuItemLayoutRefreshCallback;
import com.kongzue.dialogx.interfaces.MenuItemTextInfoInterceptor;
import com.kongzue.dialogx.interfaces.OnBackPressedListener;
import com.kongzue.dialogx.interfaces.OnBackgroundMaskClickListener;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.kongzue.dialogx.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialogx.interfaces.OnIconChangeCallBack;
import com.kongzue.dialogx.interfaces.OnMenuItemClickListener;
import com.kongzue.dialogx.interfaces.OnMenuItemSelectListener;
import com.kongzue.dialogx.util.BottomMenuArrayAdapter;
import com.kongzue.dialogx.util.TextInfo;
import com.kongzue.dialogx.util.views.BottomDialogListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.view.View.OVER_SCROLL_NEVER;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2020/10/6 23:48
 */
public class BottomMenu extends BottomDialog {

    public enum SELECT_MODE {
        NONE,
        SINGLE,
        MULTIPLE
    }

    protected BottomMenu me = this;
    protected int selectionIndex = -1;
    protected SELECT_MODE selectMode = SELECT_MODE.NONE;
    protected ArrayList<Integer> selectionItems;
    protected boolean showSelectedBackgroundTips = false;
    protected MenuItemLayoutRefreshCallback<BottomMenu> menuMenuItemLayoutRefreshCallback;

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

    private OnIconChangeCallBack<BottomMenu> onIconChangeCallBack;
    private MenuItemTextInfoInterceptor<BottomMenu> menuItemTextInfoInterceptor;
    private BottomDialogListView listView;
    private BaseAdapter menuListAdapter;
    private List<CharSequence> menuList;

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

    public static BottomMenu show(String[] menuList) {
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
                listView = new BottomDialogListView(getDialogImpl(), getOwnActivity(), R.style.DialogXCompatThemeDark);
            } else {
                listView = new BottomDialogListView(getDialogImpl(), getOwnActivity());
            }
            listView.setOverScrollMode(OVER_SCROLL_NEVER);
            listView.setDivider(getResources().getDrawable(dividerDrawableResId));
            listView.setDividerHeight(dividerHeight);

            listView.setBottomMenuListViewTouchEvent(new BottomMenuListViewTouchEvent() {
                @Override
                public void down(MotionEvent event) {
                    touchDownY = getDialogImpl().bkg.getY();
                }
            });

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - lastClickTime > ITEM_CLICK_DELAY) {
                        lastClickTime = currentTime;
                        float deltaY = Math.abs(touchDownY - getDialogImpl().bkg.getY());
                        if (deltaY > dip2px(15)) {
                            return;
                        }
                        selectionIndex = position;
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
                                        dismiss();
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
                                        dismiss();
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

            RelativeLayout.LayoutParams listViewLp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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

        //部分主题下选中项默认按下效果
        if (showSelectedBackgroundTips) {
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

    public BottomMenu setCancelButton(OnDialogButtonClickListener cancelButtonClickListener) {
        this.cancelButtonClickListener = cancelButtonClickListener;
        return this;
    }

    public BottomMenu setCancelButton(CharSequence cancelText, OnDialogButtonClickListener cancelButtonClickListener) {
        this.cancelText = cancelText;
        this.cancelButtonClickListener = cancelButtonClickListener;
        preRefreshUI();
        return this;
    }

    public BottomMenu setCancelButton(int cancelTextResId, OnDialogButtonClickListener cancelButtonClickListener) {
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

    public OnDialogButtonClickListener getCancelButtonClickListener() {
        return cancelButtonClickListener;
    }

    public BottomMenu setCancelButtonClickListener(OnDialogButtonClickListener cancelButtonClickListener) {
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

    public BottomMenu setOkButton(OnDialogButtonClickListener<BottomDialog> OkButtonClickListener) {
        this.okButtonClickListener = OkButtonClickListener;
        return this;
    }

    public BottomMenu setOkButton(CharSequence OkText, OnDialogButtonClickListener<BottomDialog> OkButtonClickListener) {
        this.okText = OkText;
        this.okButtonClickListener = OkButtonClickListener;
        return this;
    }

    public BottomMenu setOkButton(int OkTextResId, OnDialogButtonClickListener<BottomDialog> OkButtonClickListener) {
        this.okText = getString(OkTextResId);
        this.okButtonClickListener = OkButtonClickListener;
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

    public BottomMenu setOtherButton(OnDialogButtonClickListener<BottomDialog> OtherButtonClickListener) {
        this.otherButtonClickListener = OtherButtonClickListener;
        return this;
    }

    public BottomMenu setOtherButton(CharSequence OtherText, OnDialogButtonClickListener<BottomDialog> OtherButtonClickListener) {
        this.otherText = OtherText;
        this.otherButtonClickListener = OtherButtonClickListener;
        return this;
    }

    public BottomMenu setOtherButton(int OtherTextResId, OnDialogButtonClickListener<BottomDialog> OtherButtonClickListener) {
        this.otherText = getString(OtherTextResId);
        this.otherButtonClickListener = OtherButtonClickListener;
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

    //返回点击的菜单索引
    public int getSelectionIndex() {
        return selectionIndex;
    }

    //返回多选时，选择的菜单索引集合
    public int[] getSelectionIndexArray() {
        return resultArray;
    }

    //返回多选时，选择的菜单文本集合
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
}
