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
import com.kongzue.dialogx.util.MessageMenuArrayAdapter;
import com.kongzue.dialogx.util.TextInfo;
import com.kongzue.dialogx.util.views.DialogListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageMenu extends MessageDialog {

    protected MessageMenu me = this;
    protected boolean allowInterceptTouch = true;
    protected int selectionIndex = -1;
    protected SELECT_MODE selectMode = SELECT_MODE.NONE;
    protected ArrayList<Integer> selectionItems;

    protected boolean showSelectedBackgroundTips = false;
    protected MenuItemLayoutRefreshCallback<MessageMenu> menuMenuItemLayoutRefreshCallback;
    protected Map<Integer, Boolean> menuUsability = new HashMap<Integer, Boolean>();
    protected ItemDivider itemDivider;

    protected OnMenuItemClickListener<MessageMenu> onMenuItemClickListener;

    public static MessageMenu build() {
        return new MessageMenu();
    }

    public static MessageMenu build(DialogXStyle style) {
        return new MessageMenu().setStyle(style);
    }

    public static MessageMenu build(OnBindView<MessageDialog> onBindView) {
        return new MessageMenu().setCustomView(onBindView);
    }

    protected MessageMenu() {
        super();
    }

    protected OnIconChangeCallBack<MessageMenu> onIconChangeCallBack;
    protected MenuItemTextInfoInterceptor<MessageMenu> menuItemTextInfoInterceptor;
    protected DialogListView listView;
    protected TextInfo menuTextInfo;
    protected BaseAdapter menuListAdapter;
    protected List<CharSequence> menuList;
    protected List<Integer> iconResIds;
    protected boolean autoTintIconInLightOrDarkMode = true;

    public static MessageMenu show(List<CharSequence> menuList) {
        MessageMenu messageMenu = new MessageMenu();
        messageMenu.setMenuList(menuList);
        messageMenu.show();
        return messageMenu;
    }

    public static MessageMenu show(List<CharSequence> menuList, OnMenuItemClickListener<MessageMenu> onMenuItemClickListener) {
        MessageMenu messageMenu = new MessageMenu();
        messageMenu.setMenuList(menuList);
        messageMenu.setOnMenuItemClickListener(onMenuItemClickListener);
        messageMenu.show();
        return messageMenu;
    }

    public static MessageMenu showStringList(List<String> menuList) {
        MessageMenu messageMenu = new MessageMenu();
        messageMenu.setMenuStringList(menuList);
        messageMenu.show();
        return messageMenu;
    }

    public static MessageMenu showStringList(List<String> menuList, OnMenuItemClickListener<MessageMenu> onMenuItemClickListener) {
        MessageMenu messageMenu = new MessageMenu();
        messageMenu.setMenuStringList(menuList);
        messageMenu.setOnMenuItemClickListener(onMenuItemClickListener);
        messageMenu.show();
        return messageMenu;
    }

    public static MessageMenu show(String... menuList) {
        MessageMenu messageMenu = new MessageMenu();
        messageMenu.setMenuList(menuList);
        messageMenu.show();
        return messageMenu;
    }

    public static MessageMenu show(String[] menuList, OnMenuItemClickListener<MessageMenu> onMenuItemClickListener) {
        MessageMenu messageMenu = new MessageMenu();
        messageMenu.setMenuList(menuList);
        messageMenu.setOnMenuItemClickListener(onMenuItemClickListener);
        messageMenu.show();
        return messageMenu;
    }

    public static MessageMenu show(CharSequence[] menuList) {
        MessageMenu messageMenu = new MessageMenu();
        messageMenu.setMenuList(menuList);
        messageMenu.show();
        return messageMenu;
    }

    public static MessageMenu show(CharSequence[] menuList, OnMenuItemClickListener<MessageMenu> onMenuItemClickListener) {
        MessageMenu messageMenu = new MessageMenu();
        messageMenu.setMenuList(menuList);
        messageMenu.setOnMenuItemClickListener(onMenuItemClickListener);
        messageMenu.show();
        return messageMenu;
    }

    public static MessageMenu show(CharSequence title, CharSequence message, List<CharSequence> menuList) {
        MessageMenu messageMenu = new MessageMenu();
        messageMenu.title = title;
        messageMenu.message = message;
        messageMenu.setMenuList(menuList);
        messageMenu.show();
        return messageMenu;
    }

    public static MessageMenu show(CharSequence title, CharSequence message, List<CharSequence> menuList, OnMenuItemClickListener<MessageMenu> onMenuItemClickListener) {
        MessageMenu messageMenu = new MessageMenu();
        messageMenu.title = title;
        messageMenu.message = message;
        messageMenu.setMenuList(menuList);
        messageMenu.setOnMenuItemClickListener(onMenuItemClickListener);
        messageMenu.show();
        return messageMenu;
    }

    public static MessageMenu show(CharSequence title, List<CharSequence> menuList) {
        MessageMenu messageMenu = new MessageMenu();
        messageMenu.title = title;
        messageMenu.setMenuList(menuList);
        messageMenu.show();
        return messageMenu;
    }

    public static MessageMenu show(CharSequence title, List<CharSequence> menuList, OnMenuItemClickListener<MessageMenu> onMenuItemClickListener) {
        MessageMenu messageMenu = new MessageMenu();
        messageMenu.title = title;
        messageMenu.setMenuList(menuList);
        messageMenu.setOnMenuItemClickListener(onMenuItemClickListener);
        messageMenu.show();
        return messageMenu;
    }

    public static MessageMenu showStringList(CharSequence title, CharSequence message, List<String> menuList) {
        MessageMenu messageMenu = new MessageMenu();
        messageMenu.title = title;
        messageMenu.message = message;
        messageMenu.setMenuStringList(menuList);
        messageMenu.show();
        return messageMenu;
    }

    public static MessageMenu showStringList(CharSequence title, CharSequence message, List<String> menuList, OnMenuItemClickListener<MessageMenu> onMenuItemClickListener) {
        MessageMenu messageMenu = new MessageMenu();
        messageMenu.title = title;
        messageMenu.message = message;
        messageMenu.setMenuStringList(menuList);
        messageMenu.setOnMenuItemClickListener(onMenuItemClickListener);
        messageMenu.show();
        return messageMenu;
    }

    public static MessageMenu show(CharSequence title, CharSequence message, String[] menuList) {
        MessageMenu messageMenu = new MessageMenu();
        messageMenu.title = title;
        messageMenu.message = message;
        messageMenu.setMenuList(menuList);
        messageMenu.show();
        return messageMenu;
    }

    public static MessageMenu show(CharSequence title, CharSequence message, String[] menuList, OnMenuItemClickListener<MessageMenu> onMenuItemClickListener) {
        MessageMenu messageMenu = new MessageMenu();
        messageMenu.title = title;
        messageMenu.message = message;
        messageMenu.setMenuList(menuList);
        messageMenu.setOnMenuItemClickListener(onMenuItemClickListener);
        messageMenu.show();
        return messageMenu;
    }

    public static MessageMenu show(CharSequence title, CharSequence message, CharSequence[] menuList) {
        MessageMenu messageMenu = new MessageMenu();
        messageMenu.title = title;
        messageMenu.message = message;
        messageMenu.setMenuList(menuList);
        messageMenu.show();
        return messageMenu;
    }

    public static MessageMenu show(CharSequence title, CharSequence message, CharSequence[] menuList, OnMenuItemClickListener<MessageMenu> onMenuItemClickListener) {
        MessageMenu messageMenu = new MessageMenu();
        messageMenu.title = title;
        messageMenu.message = message;
        messageMenu.setMenuList(menuList);
        messageMenu.setOnMenuItemClickListener(onMenuItemClickListener);
        messageMenu.show();
        return messageMenu;
    }

    public static MessageMenu show(String title, String message, List<CharSequence> menuList) {
        MessageMenu messageMenu = new MessageMenu();
        messageMenu.title = title;
        messageMenu.message = message;
        messageMenu.setMenuList(menuList);
        messageMenu.show();
        return messageMenu;
    }

    public static MessageMenu show(String title, String message, List<CharSequence> menuList, OnMenuItemClickListener<MessageMenu> onMenuItemClickListener) {
        MessageMenu messageMenu = new MessageMenu();
        messageMenu.title = title;
        messageMenu.message = message;
        messageMenu.setMenuList(menuList);
        messageMenu.setOnMenuItemClickListener(onMenuItemClickListener);
        messageMenu.show();
        return messageMenu;
    }

    public static MessageMenu showStringList(String title, String message, List<String> menuList) {
        MessageMenu messageMenu = new MessageMenu();
        messageMenu.title = title;
        messageMenu.message = message;
        messageMenu.setMenuStringList(menuList);
        messageMenu.show();
        return messageMenu;
    }

    public static MessageMenu showStringList(String title, String message, List<String> menuList, OnMenuItemClickListener<MessageMenu> onMenuItemClickListener) {
        MessageMenu messageMenu = new MessageMenu();
        messageMenu.title = title;
        messageMenu.message = message;
        messageMenu.setMenuStringList(menuList);
        messageMenu.setOnMenuItemClickListener(onMenuItemClickListener);
        messageMenu.show();
        return messageMenu;
    }

    public static MessageMenu show(String title, String message, String[] menuList) {
        MessageMenu messageMenu = new MessageMenu();
        messageMenu.title = title;
        messageMenu.message = message;
        messageMenu.setMenuList(menuList);
        messageMenu.show();
        return messageMenu;
    }

    public static MessageMenu show(String title, String message, String[] menuList, OnMenuItemClickListener<MessageMenu> onMenuItemClickListener) {
        MessageMenu messageMenu = new MessageMenu();
        messageMenu.title = title;
        messageMenu.message = message;
        messageMenu.setMenuList(menuList);
        messageMenu.setOnMenuItemClickListener(onMenuItemClickListener);
        messageMenu.show();
        return messageMenu;
    }

    public static MessageMenu show(String title, String message, CharSequence[] menuList) {
        MessageMenu messageMenu = new MessageMenu();
        messageMenu.title = title;
        messageMenu.message = message;
        messageMenu.setMenuList(menuList);
        messageMenu.show();
        return messageMenu;
    }

    public static MessageMenu show(String title, String message, CharSequence[] menuList, OnMenuItemClickListener<MessageMenu> onMenuItemClickListener) {
        MessageMenu messageMenu = new MessageMenu();
        messageMenu.title = title;
        messageMenu.message = message;
        messageMenu.setMenuList(menuList);
        messageMenu.setOnMenuItemClickListener(onMenuItemClickListener);
        messageMenu.show();
        return messageMenu;
    }

    public static MessageMenu show(int titleResId, int messageResId, List<CharSequence> menuList) {
        MessageMenu messageMenu = new MessageMenu();
        messageMenu.title = messageMenu.getString(titleResId);
        messageMenu.message = messageMenu.getString(messageResId);
        messageMenu.setMenuList(menuList);
        messageMenu.show();
        return messageMenu;
    }

    public static MessageMenu show(int titleResId, List<CharSequence> menuList) {
        MessageMenu messageMenu = new MessageMenu();
        messageMenu.title = messageMenu.getString(titleResId);
        messageMenu.setMenuList(menuList);
        messageMenu.show();
        return messageMenu;
    }

    public static MessageMenu showStringList(int titleResId, int messageResId, List<String> menuList) {
        MessageMenu messageMenu = new MessageMenu();
        messageMenu.title = messageMenu.getString(titleResId);
        messageMenu.message = messageMenu.getString(messageResId);
        messageMenu.setMenuStringList(menuList);
        messageMenu.show();
        return messageMenu;
    }

    public static MessageMenu show(int titleResId, int messageResId, String[] menuList) {
        MessageMenu messageMenu = new MessageMenu();
        messageMenu.title = messageMenu.getString(titleResId);
        messageMenu.message = messageMenu.getString(messageResId);
        messageMenu.setMenuList(menuList);
        messageMenu.show();
        return messageMenu;
    }

    public static MessageMenu show(int titleResId, int messageResId, CharSequence[] menuList) {
        MessageMenu messageMenu = new MessageMenu();
        messageMenu.title = messageMenu.getString(titleResId);
        messageMenu.message = messageMenu.getString(messageResId);
        messageMenu.setMenuList(menuList);
        messageMenu.show();
        return messageMenu;
    }

    public static MessageMenu show(int titleResId, int messageResId, List<CharSequence> menuList, OnMenuItemClickListener<MessageMenu> onMenuItemClickListener) {
        MessageMenu messageMenu = new MessageMenu();
        messageMenu.title = messageMenu.getString(titleResId);
        messageMenu.message = messageMenu.getString(messageResId);
        messageMenu.setOnMenuItemClickListener(onMenuItemClickListener);
        messageMenu.setMenuList(menuList);
        messageMenu.show();
        return messageMenu;
    }

    public static MessageMenu show(int titleResId, List<CharSequence> menuList, OnMenuItemClickListener<MessageMenu> onMenuItemClickListener) {
        MessageMenu messageMenu = new MessageMenu();
        messageMenu.title = messageMenu.getString(titleResId);
        messageMenu.setOnMenuItemClickListener(onMenuItemClickListener);
        messageMenu.setMenuList(menuList);
        messageMenu.show();
        return messageMenu;
    }

    public static MessageMenu showStringList(int titleResId, int messageResId, List<String> menuList, OnMenuItemClickListener<MessageMenu> onMenuItemClickListener) {
        MessageMenu messageMenu = new MessageMenu();
        messageMenu.title = messageMenu.getString(titleResId);
        messageMenu.message = messageMenu.getString(messageResId);
        messageMenu.setMenuStringList(menuList);
        messageMenu.setOnMenuItemClickListener(onMenuItemClickListener);
        messageMenu.show();
        return messageMenu;
    }

    public static MessageMenu show(int titleResId, int messageResId, String[] menuList, OnMenuItemClickListener<MessageMenu> onMenuItemClickListener) {
        MessageMenu messageMenu = new MessageMenu();
        messageMenu.title = messageMenu.getString(titleResId);
        messageMenu.message = messageMenu.getString(messageResId);
        messageMenu.setMenuList(menuList);
        messageMenu.setOnMenuItemClickListener(onMenuItemClickListener);
        messageMenu.show();
        return messageMenu;
    }

    public static MessageMenu show(int titleResId, int messageResId, CharSequence[] menuList, OnMenuItemClickListener<MessageMenu> onMenuItemClickListener) {
        MessageMenu messageMenu = new MessageMenu();
        messageMenu.title = messageMenu.getString(titleResId);
        messageMenu.message = messageMenu.getString(messageResId);
        messageMenu.setMenuList(menuList);
        messageMenu.setOnMenuItemClickListener(onMenuItemClickListener);
        messageMenu.show();
        return messageMenu;
    }

    public static MessageMenu show(CharSequence title, CharSequence[] menuList) {
        MessageMenu messageMenu = new MessageMenu();
        messageMenu.title = title;
        messageMenu.setMenuList(menuList);
        messageMenu.show();
        return messageMenu;
    }

    public static MessageMenu show(CharSequence title, String[] menuList) {
        MessageMenu messageMenu = new MessageMenu();
        messageMenu.title = title;
        messageMenu.setMenuList(menuList);
        messageMenu.show();
        return messageMenu;
    }

    public static MessageMenu show(CharSequence title, CharSequence[] menuList, OnMenuItemClickListener<MessageMenu> onMenuItemClickListener) {
        MessageMenu messageMenu = new MessageMenu();
        messageMenu.title = title;
        messageMenu.setMenuList(menuList);
        messageMenu.setOnMenuItemClickListener(onMenuItemClickListener);
        messageMenu.show();
        return messageMenu;
    }

    public static MessageMenu show(CharSequence title, String[] menuList, OnMenuItemClickListener<MessageMenu> onMenuItemClickListener) {
        MessageMenu messageMenu = new MessageMenu();
        messageMenu.title = title;
        messageMenu.setMenuList(menuList);
        messageMenu.setOnMenuItemClickListener(onMenuItemClickListener);
        messageMenu.show();
        return messageMenu;
    }

    public static MessageMenu show(int titleResId, CharSequence[] menuList) {
        MessageMenu messageMenu = new MessageMenu();
        messageMenu.title = messageMenu.getString(titleResId);
        messageMenu.setMenuList(menuList);
        messageMenu.show();
        return messageMenu;
    }

    public static MessageMenu show(int titleResId, String[] menuList) {
        MessageMenu messageMenu = new MessageMenu();
        messageMenu.title = messageMenu.getString(titleResId);
        messageMenu.setMenuList(menuList);
        messageMenu.show();
        return messageMenu;
    }

    public static MessageMenu show(int titleResId, CharSequence[] menuList, OnMenuItemClickListener<MessageMenu> onMenuItemClickListener) {
        MessageMenu messageMenu = new MessageMenu();
        messageMenu.title = messageMenu.getString(titleResId);
        messageMenu.setMenuList(menuList);
        messageMenu.setOnMenuItemClickListener(onMenuItemClickListener);
        messageMenu.show();
        return messageMenu;
    }

    public static MessageMenu show(int titleResId, String[] menuList, OnMenuItemClickListener<MessageMenu> onMenuItemClickListener) {
        MessageMenu messageMenu = new MessageMenu();
        messageMenu.title = messageMenu.getString(titleResId);
        messageMenu.setMenuList(menuList);
        messageMenu.setOnMenuItemClickListener(onMenuItemClickListener);
        messageMenu.show();
        return messageMenu;
    }


    private float touchDownY;

    public static final int ITEM_CLICK_DELAY = 100;
    private long lastClickTime = 0;
    private int[] resultArray;
    private CharSequence[] selectTextArray;

    @Override
    protected void onDialogShow() {
        if (getDialogImpl() != null && getDialogImpl().boxList != null) {
            getDialogImpl().boxList.setVisibility(View.VISIBLE);

            if (!isAllowInterceptTouch()) {
                getDialogImpl().bkg.setMaxHeight(maxHeight);
                if (maxHeight != 0) {
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
                    if (!isMenuItemEnable(position)) {
                        return;
                    }
                    haptic(view);
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
                                    OnMenuItemSelectListener<MessageMenu> onMenuItemSelectListener = (OnMenuItemSelectListener<MessageMenu>) onMenuItemClickListener;
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
                                    OnMenuItemSelectListener<MessageMenu> onMenuItemSelectListener = (OnMenuItemSelectListener<MessageMenu>) onMenuItemClickListener;
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
                menuListAdapter = new MessageMenuArrayAdapter(me, getOwnActivity(), menuList);
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

        if (itemDivider != null && listView != null) {
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

    public MessageMenu setMenuList(List<CharSequence> menuList) {
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

    public MessageMenu setMenuStringList(List<String> menuList) {
        this.menuList = new ArrayList<>();
        this.menuList.addAll(menuList);
        this.menuListAdapter = null;
        preRefreshUI();
        return this;
    }

    public MessageMenu setMenuList(String[] menuList) {
        this.menuList = new ArrayList<>();
        this.menuList.addAll(Arrays.asList(menuList));
        this.menuListAdapter = null;
        preRefreshUI();
        return this;
    }

    public MessageMenu setMenuList(CharSequence[] menuList) {
        this.menuList = Arrays.asList(menuList);
        this.menuListAdapter = null;
        preRefreshUI();
        return this;
    }

    public MessageMenu setMenus(CharSequence... menuList) {
        this.menuList = Arrays.asList(menuList);
        this.menuListAdapter = null;
        preRefreshUI();
        return this;
    }

    public MessageMenu setMenus(String... menuList) {
        this.menuList = Arrays.asList(menuList);
        this.menuListAdapter = null;
        preRefreshUI();
        return this;
    }

    public OnIconChangeCallBack<MessageMenu> getOnIconChangeCallBack() {
        return onIconChangeCallBack;
    }

    public MessageMenu setOnIconChangeCallBack(OnIconChangeCallBack<MessageMenu> onIconChangeCallBack) {
        this.onIconChangeCallBack = onIconChangeCallBack;
        return this;
    }

    public OnBackPressedListener<MessageDialog> getOnBackPressedListener() {
        return (OnBackPressedListener<MessageDialog>) onBackPressedListener;
    }

    public MessageMenu setOnBackPressedListener(OnBackPressedListener<MessageDialog> onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
        preRefreshUI();
        return this;
    }

    public MessageMenu setDialogLifecycleCallback(DialogLifecycleCallback<MessageDialog> dialogLifecycleCallback) {
        this.dialogLifecycleCallback = dialogLifecycleCallback;
        if (isShow) dialogLifecycleCallback.onShow(me);
        return this;
    }

    public MessageMenu setStyle(DialogXStyle style) {
        this.style = style;
        return this;
    }

    public MessageMenu setTheme(DialogX.THEME theme) {
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

    public MessageMenu setCancelable(boolean cancelable) {
        this.privateCancelable = cancelable ? BOOLEAN.TRUE : BOOLEAN.FALSE;
        preRefreshUI();
        return this;
    }

    public MessageDialog.DialogImpl getDialogImpl() {
        return dialogImpl;
    }

    public CharSequence getTitle() {
        return title;
    }

    public MessageMenu setTitle(CharSequence title) {
        this.title = title;
        preRefreshUI();
        return this;
    }

    public MessageMenu setTitle(int titleResId) {
        this.title = getString(titleResId);
        preRefreshUI();
        return this;
    }

    public CharSequence getMessage() {
        return message;
    }

    public MessageMenu setMessage(CharSequence message) {
        this.message = message;
        preRefreshUI();
        return this;
    }

    public MessageMenu setMessage(int messageResId) {
        this.message = getString(messageResId);
        preRefreshUI();
        return this;
    }

    public CharSequence getCancelButton() {
        return cancelText;
    }

    public MessageMenu setCancelButton(CharSequence cancelText) {
        this.cancelText = cancelText;
        preRefreshUI();
        return this;
    }

    public MessageMenu setCancelButton(int cancelTextResId) {
        this.cancelText = getString(cancelTextResId);
        preRefreshUI();
        return this;
    }

    public MessageMenu setCancelButton(OnMenuButtonClickListener<MessageMenu> cancelButtonClickListener) {
        this.cancelButtonClickListener = cancelButtonClickListener;
        return this;
    }

    public MessageMenu setCancelButton(CharSequence cancelText, OnMenuButtonClickListener<MessageMenu> cancelButtonClickListener) {
        this.cancelText = cancelText;
        this.cancelButtonClickListener = cancelButtonClickListener;
        preRefreshUI();
        return this;
    }

    public MessageMenu setCancelButton(int cancelTextResId, OnMenuButtonClickListener<MessageMenu> cancelButtonClickListener) {
        this.cancelText = getString(cancelTextResId);
        this.cancelButtonClickListener = cancelButtonClickListener;
        preRefreshUI();
        return this;
    }

    /**
     * 建议使用 {@link com.kongzue.dialogx.dialogs.MessageMenu#setCancelButton(OnMenuButtonClickListener<MessageMenu>)}
     */
    @Deprecated
    public MessageMenu setCancelButton(OnDialogButtonClickListener<MessageDialog> cancelButtonClickListener) {
        this.cancelButtonClickListener = cancelButtonClickListener;
        return this;
    }

    /**
     * 建议使用 {@link com.kongzue.dialogx.dialogs.MessageMenu#setCancelButton(CharSequence cancelText, OnMenuButtonClickListener<MessageMenu>)}
     */
    @Deprecated
    public MessageMenu setCancelButton(CharSequence cancelText, OnDialogButtonClickListener<MessageDialog> cancelButtonClickListener) {
        this.cancelText = cancelText;
        this.cancelButtonClickListener = cancelButtonClickListener;
        preRefreshUI();
        return this;
    }

    /**
     * 建议使用 {@link com.kongzue.dialogx.dialogs.MessageMenu#setCancelButton(int cancelTextResId, OnMenuButtonClickListener<MessageMenu>)}
     */
    @Deprecated
    public MessageMenu setCancelButton(int cancelTextResId, OnDialogButtonClickListener<MessageDialog> cancelButtonClickListener) {
        this.cancelText = getString(cancelTextResId);
        this.cancelButtonClickListener = cancelButtonClickListener;
        preRefreshUI();
        return this;
    }

    public MessageMenu setCustomView(OnBindView<MessageDialog> onBindView) {
        this.onBindView = onBindView;
        preRefreshUI();
        return this;
    }

    public View getCustomView() {
        if (onBindView == null) return null;
        return onBindView.getCustomView();
    }

    public MessageMenu removeCustomView() {
        this.onBindView.clean();
        preRefreshUI();
        return this;
    }

    public boolean isAllowInterceptTouch() {
        if (style.overrideBottomDialogRes() == null) {
            return false;
        } else {
            return allowInterceptTouch && style.overrideBottomDialogRes().touchSlide();
        }
    }

    public MessageMenu setAllowInterceptTouch(boolean allowInterceptTouch) {
        this.allowInterceptTouch = allowInterceptTouch;
        preRefreshUI();
        return this;
    }

    public float getMessageDialogMaxHeight() {
        return maxHeight;
    }

    public MessageMenu setMessageDialogMaxHeight(float dialogMaxHeight) {
        this.maxHeight = (int) dialogMaxHeight;
        return this;
    }

    public OnMenuItemClickListener<MessageMenu> getOnMenuItemClickListener() {
        return onMenuItemClickListener;
    }

    public MessageMenu setOnMenuItemClickListener(OnMenuItemClickListener<MessageMenu> onMenuItemClickListener) {
        this.onMenuItemClickListener = onMenuItemClickListener;
        return this;
    }

    public BaseAdapter getMenuListAdapter() {
        return menuListAdapter;
    }

    public MessageMenu setMenuListAdapter(BaseAdapter menuListAdapter) {
        this.menuListAdapter = menuListAdapter;
        return this;
    }

    public OnMenuButtonClickListener<MessageMenu> getMessageMenuCancelButtonClickListener() {
        return (OnMenuButtonClickListener<MessageMenu>) cancelButtonClickListener;
    }

    public MessageMenu setCancelButtonClickListener(OnMenuButtonClickListener<MessageMenu> cancelButtonClickListener) {
        this.cancelButtonClickListener = cancelButtonClickListener;
        return this;
    }

    public TextInfo getTitleTextInfo() {
        return titleTextInfo;
    }

    public MessageMenu setTitleTextInfo(TextInfo titleTextInfo) {
        this.titleTextInfo = titleTextInfo;
        preRefreshUI();
        return this;
    }

    public TextInfo getMessageTextInfo() {
        return messageTextInfo;
    }

    public MessageMenu setMessageTextInfo(TextInfo messageTextInfo) {
        this.messageTextInfo = messageTextInfo;
        preRefreshUI();
        return this;
    }

    public TextInfo getCancelTextInfo() {
        return cancelTextInfo;
    }

    public MessageMenu setCancelTextInfo(TextInfo cancelTextInfo) {
        this.cancelTextInfo = cancelTextInfo;
        preRefreshUI();
        return this;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public MessageMenu setBackgroundColor(@ColorInt int backgroundColor) {
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

    public MessageMenu setSelection(int selectionIndex) {
        this.selectMode = SELECT_MODE.SINGLE;
        this.selectionIndex = selectionIndex;
        this.selectionItems = null;
        menuListAdapter = null;
        preRefreshUI();
        return this;
    }

    public MessageMenu setSingleSelection() {
        this.selectMode = SELECT_MODE.SINGLE;
        this.selectionIndex = -1;
        this.selectionItems = null;
        menuListAdapter = null;
        preRefreshUI();
        return this;
    }

    public MessageMenu setSelection(int[] selectionItems) {
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

    public MessageMenu setMultiSelection() {
        this.selectMode = SELECT_MODE.MULTIPLE;
        this.selectionIndex = -1;
        this.selectionItems = new ArrayList<>();
        menuListAdapter = null;
        preRefreshUI();
        return this;
    }

    public MessageMenu setSelection(List<Integer> selectionItems) {
        this.selectMode = SELECT_MODE.MULTIPLE;
        this.selectionIndex = -1;
        this.selectionItems = new ArrayList<>(selectionItems);
        menuListAdapter = null;
        preRefreshUI();
        return this;
    }

    public MessageMenu setNoSelect() {
        this.selectMode = SELECT_MODE.NONE;
        this.selectionIndex = -1;
        this.selectionItems = null;
        menuListAdapter = null;
        preRefreshUI();
        return this;
    }

    public MessageMenu setBackgroundColorRes(@ColorRes int backgroundRes) {
        this.backgroundColor = getColor(backgroundRes);
        preRefreshUI();
        return this;
    }

    public CharSequence getOkButton() {
        return okText;
    }

    public MessageMenu setOkButton(CharSequence okText) {
        this.okText = okText;
        preRefreshUI();
        return this;
    }

    public MessageMenu setOkButton(int OkTextResId) {
        this.okText = getString(OkTextResId);
        preRefreshUI();
        return this;
    }

    public MessageMenu setOkButton(OnMenuButtonClickListener<MessageMenu> okButtonClickListener) {
        this.okButtonClickListener = okButtonClickListener;
        return this;
    }

    public MessageMenu setOkButton(CharSequence okText, OnMenuButtonClickListener<MessageMenu> okButtonClickListener) {
        this.okText = okText;
        this.okButtonClickListener = okButtonClickListener;
        return this;
    }

    public MessageMenu setOkButton(int okTextResId, OnMenuButtonClickListener<MessageMenu> okButtonClickListener) {
        this.okText = getString(okTextResId);
        this.okButtonClickListener = okButtonClickListener;
        return this;
    }

    public MessageMenu setHapticFeedbackEnabled(boolean isHapticFeedbackEnabled) {
        this.isHapticFeedbackEnabled = isHapticFeedbackEnabled ? 1 : 0;
        return this;
    }

    /**
     * 建议使用 {@link com.kongzue.dialogx.dialogs.MessageMenu#setOkButton(OnMenuButtonClickListener<MessageMenu>)}
     */
    @Deprecated
    public MessageMenu setOkButton(OnDialogButtonClickListener<MessageDialog> okButtonClickListener) {
        this.okButtonClickListener = okButtonClickListener;
        return this;
    }

    /**
     * 建议使用 {@link com.kongzue.dialogx.dialogs.MessageMenu#setOkButton(CharSequence okText, OnMenuButtonClickListener<MessageMenu>)}
     */
    @Deprecated
    public MessageMenu setOkButton(CharSequence okText, OnDialogButtonClickListener<MessageDialog> okButtonClickListener) {
        this.okText = okText;
        this.okButtonClickListener = okButtonClickListener;
        return this;
    }

    /**
     * 建议使用 {@link com.kongzue.dialogx.dialogs.MessageMenu#setOkButton(int okTextResId, OnMenuButtonClickListener<MessageMenu>)}
     */
    @Deprecated
    public MessageMenu setOkButton(int okTextResId, OnDialogButtonClickListener<MessageDialog> okButtonClickListener) {
        this.okText = getString(okTextResId);
        this.okButtonClickListener = okButtonClickListener;
        return this;
    }

    public CharSequence getOtherButton() {
        return otherText;
    }

    public MessageMenu setOtherButton(CharSequence otherText) {
        this.otherText = otherText;
        preRefreshUI();
        return this;
    }

    public MessageMenu setOtherButton(int OtherTextResId) {
        this.otherText = getString(OtherTextResId);
        preRefreshUI();
        return this;
    }

    public MessageMenu setOtherButton(OnMenuButtonClickListener<MessageMenu> otherButtonClickListener) {
        this.otherButtonClickListener = otherButtonClickListener;
        return this;
    }

    public MessageMenu setOtherButton(CharSequence otherText, OnMenuButtonClickListener<MessageMenu> otherButtonClickListener) {
        this.otherText = otherText;
        this.otherButtonClickListener = otherButtonClickListener;
        return this;
    }

    public MessageMenu setOtherButton(int otherTextResId, OnMenuButtonClickListener<MessageMenu> otherButtonClickListener) {
        this.otherText = getString(otherTextResId);
        this.otherButtonClickListener = otherButtonClickListener;
        return this;
    }

    /**
     * 建议使用 {@link com.kongzue.dialogx.dialogs.MessageMenu#setOtherButton(OnMenuButtonClickListener<MessageMenu>)}
     */
    @Deprecated
    public MessageMenu setOtherButton(OnDialogButtonClickListener<MessageDialog> otherButtonClickListener) {
        this.otherButtonClickListener = otherButtonClickListener;
        return this;
    }

    /**
     * 建议使用 {@link com.kongzue.dialogx.dialogs.MessageMenu#setOtherButton(CharSequence otherText, OnMenuButtonClickListener<MessageMenu>)}
     */
    @Deprecated
    public MessageMenu setOtherButton(CharSequence otherText, OnDialogButtonClickListener<MessageDialog> otherButtonClickListener) {
        this.otherText = otherText;
        this.otherButtonClickListener = otherButtonClickListener;
        return this;
    }

    /**
     * 建议使用 {@link com.kongzue.dialogx.dialogs.MessageMenu#setOtherButton(int otherTextResId, OnMenuButtonClickListener<MessageMenu>)}
     */
    @Deprecated
    public MessageMenu setOtherButton(int otherTextResId, OnDialogButtonClickListener<MessageDialog> otherButtonClickListener) {
        this.otherText = getString(otherTextResId);
        this.otherButtonClickListener = otherButtonClickListener;
        return this;
    }

    public MessageMenu setMaskColor(@ColorInt int maskColor) {
        this.maskColor = maskColor;
        preRefreshUI();
        return this;
    }

    public long getEnterAnimDuration() {
        return enterAnimDuration;
    }

    public MessageMenu setEnterAnimDuration(long enterAnimDuration) {
        this.enterAnimDuration = enterAnimDuration;
        return this;
    }

    public long getExitAnimDuration() {
        return exitAnimDuration;
    }

    public MessageMenu setExitAnimDuration(long exitAnimDuration) {
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

    public MessageMenu setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
        refreshUI();
        return this;
    }

    public MessageMenu setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
        refreshUI();
        return this;
    }

    public MessageMenu setMinHeight(int minHeight) {
        this.minHeight = minHeight;
        refreshUI();
        return this;
    }

    public MessageMenu setMinWidth(int minWidth) {
        this.minWidth = minWidth;
        refreshUI();
        return this;
    }

    public MessageMenu setDialogImplMode(DialogX.IMPL_MODE dialogImplMode) {
        this.dialogImplMode = dialogImplMode;
        return this;
    }

    public TextInfo getMenuTextInfo() {
        if (menuTextInfo == null) return DialogX.menuTextInfo;
        return menuTextInfo;
    }

    public MessageMenu setMenuTextInfo(TextInfo menuTextInfo) {
        this.menuTextInfo = menuTextInfo;
        return this;
    }

    public MenuItemTextInfoInterceptor<MessageMenu> getMenuItemTextInfoInterceptor() {
        return menuItemTextInfoInterceptor;
    }

    public MessageMenu setMenuItemTextInfoInterceptor(MenuItemTextInfoInterceptor<MessageMenu> menuItemTextInfoInterceptor) {
        this.menuItemTextInfoInterceptor = menuItemTextInfoInterceptor;
        return this;
    }

    public boolean isBkgInterceptTouch() {
        return bkgInterceptTouch;
    }

    public MessageMenu setBkgInterceptTouch(boolean bkgInterceptTouch) {
        this.bkgInterceptTouch = bkgInterceptTouch;
        return this;
    }

    public OnBackgroundMaskClickListener<MessageDialog> getOnBackgroundMaskClickListener() {
        return onBackgroundMaskClickListener;
    }

    public MessageMenu setOnBackgroundMaskClickListener(OnBackgroundMaskClickListener<MessageDialog> onBackgroundMaskClickListener) {
        this.onBackgroundMaskClickListener = onBackgroundMaskClickListener;
        return this;
    }

    public MessageMenu setRadius(float radiusPx) {
        backgroundRadius = radiusPx;
        refreshUI();
        return this;
    }

    public float getRadius() {
        return backgroundRadius;
    }

    public MessageMenu setTitleIcon(Bitmap titleIcon) {
        this.titleIcon = new BitmapDrawable(getResources(), titleIcon);
        refreshUI();
        return this;
    }

    public MessageMenu setTitleIcon(int titleIconResId) {
        this.titleIcon = getResources().getDrawable(titleIconResId);
        refreshUI();
        return this;
    }

    public MessageMenu setTitleIcon(Drawable titleIcon) {
        this.titleIcon = titleIcon;
        refreshUI();
        return this;
    }

    public DialogXAnimInterface<MessageDialog> getDialogXAnimImpl() {
        return dialogXAnimImpl;
    }

    public MessageMenu setDialogXAnimImpl(DialogXAnimInterface<MessageDialog> dialogXAnimImpl) {
        this.dialogXAnimImpl = dialogXAnimImpl;
        return this;
    }

    public MessageMenu setRootPadding(int padding) {
        this.screenPaddings = new int[]{padding, padding, padding, padding};
        refreshUI();
        return this;
    }

    public MessageMenu setRootPadding(int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {
        this.screenPaddings = new int[]{paddingLeft, paddingTop, paddingRight, paddingBottom};
        refreshUI();
        return this;
    }

    public boolean isShowSelectedBackgroundTips() {
        return showSelectedBackgroundTips;
    }

    public MessageMenu setShowSelectedBackgroundTips(boolean showSelectedBackgroundTips) {
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

    public MenuItemLayoutRefreshCallback<MessageMenu> getMenuMenuItemLayoutRefreshCallback() {
        return menuMenuItemLayoutRefreshCallback;
    }

    public MessageMenu setMenuMenuItemLayoutRefreshCallback(MenuItemLayoutRefreshCallback<MessageMenu> menuMenuItemLayoutRefreshCallback) {
        this.menuMenuItemLayoutRefreshCallback = menuMenuItemLayoutRefreshCallback;
        return this;
    }

    public TextInfo getOkTextInfo() {
        return okTextInfo;
    }

    public MessageMenu setOkTextInfo(TextInfo okTextInfo) {
        this.okTextInfo = okTextInfo;
        return this;
    }

    public TextInfo getOtherTextInfo() {
        return otherTextInfo;
    }

    public MessageMenu setOtherTextInfo(TextInfo otherTextInfo) {
        this.otherTextInfo = otherTextInfo;
        return this;
    }

    public MessageMenu setData(String key, Object obj) {
        if (data == null) data = new HashMap<>();
        data.put(key, obj);
        return this;
    }

    public MessageMenu onShow(DialogXRunnable<MessageDialog> dialogXRunnable) {
        onShowRunnable = dialogXRunnable;
        if (isShow() && onShowRunnable != null) {
            onShowRunnable.run(this);
        }
        return this;
    }

    public MessageMenu onDismiss(DialogXRunnable<MessageDialog> dialogXRunnable) {
        onDismissRunnable = dialogXRunnable;
        return this;
    }

    public MessageMenu setEnableImmersiveMode(boolean enableImmersiveMode) {
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

    public MessageMenu setIconResIds(List<Integer> iconResIds) {
        this.iconResIds = iconResIds;
        refreshUI();
        return this;
    }

    public MessageMenu setIconResIds(int... resIds) {
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

    public MessageMenu setAutoTintIconInLightOrDarkMode(boolean autoTintIconInLightOrDarkMode) {
        this.autoTintIconInLightOrDarkMode = autoTintIconInLightOrDarkMode;
        refreshUI();
        return this;
    }

    public MessageMenu appendMessage(CharSequence message) {
        this.message = TextUtils.concat(this.message, message);
        refreshUI();
        return this;
    }

    public MessageMenu setThisOrderIndex(int orderIndex) {
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

    public MessageMenu bringToFront() {
        setThisOrderIndex(getHighestOrderIndex());
        return this;
    }

    public MessageMenu enableMenu(int... menuIndex) {
        for (int i : menuIndex) {
            menuUsability.put(i, true);
        }
        return this;
    }

    public MessageMenu enableMenu(CharSequence... menuText) {
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

    public MessageMenu enableMenu(String... menuText) {
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

    public MessageMenu disableMenu(int... menuIndex) {
        for (int i : menuIndex) {
            menuUsability.put(i, false);
        }
        return this;
    }

    public MessageMenu disableMenu(CharSequence... menuText) {
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

    public MessageMenu disableMenu(String... menuText) {
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

    public MessageMenu setActionRunnable(int actionId, DialogXRunnable<MessageDialog> runnable) {
        dialogActionRunnableMap.put(actionId, runnable);
        return this;
    }

    public MessageMenu cleanAction(int actionId) {
        dialogActionRunnableMap.remove(actionId);
        return this;
    }

    public MessageMenu cleanAllAction() {
        dialogActionRunnableMap.clear();
        return this;
    }

    // for BaseDialog use
    public void callDialogDismiss() {
        dismiss();
    }

    public MessageMenu bindDismissWithLifecycleOwner(LifecycleOwner owner) {
        super.bindDismissWithLifecycleOwnerPrivate(owner);
        return this;
    }

    public ItemDivider getItemDivider() {
        return itemDivider;
    }

    public MessageMenu setItemDivider(ItemDivider itemDivider) {
        this.itemDivider = itemDivider;
        refreshUI();
        return this;
    }

    public MessageMenu setCustomDialogLayoutResId(int customDialogLayoutId) {
        this.customDialogLayoutResId[0] = customDialogLayoutId;
        this.customDialogLayoutResId[1] = customDialogLayoutId;
        return this;
    }

    public MessageMenu setCustomDialogLayoutResId(int customDialogLayoutId, boolean isLightTheme) {
        this.customDialogLayoutResId[isLightTheme ? 0 : 1] = customDialogLayoutId;
        return this;
    }
}
