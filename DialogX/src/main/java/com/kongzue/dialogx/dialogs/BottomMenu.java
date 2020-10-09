package com.kongzue.dialogx.dialogs;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;

import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.R;
import com.kongzue.dialogx.interfaces.DialogLifecycleCallback;
import com.kongzue.dialogx.interfaces.DialogXStyle;
import com.kongzue.dialogx.interfaces.OnBackPressedListener;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.kongzue.dialogx.interfaces.OnIconChangeCallBack;
import com.kongzue.dialogx.util.NormalMenuArrayAdapter;
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
    
    protected BottomMenu me = this;
    
    /**
     * 此值用于，当禁用滑动时（style.overrideBottomDialogRes.touchSlide = false时）的最大显示高度。
     * 0：不限制，最大显示到屏幕可用高度。
     */
    protected float bottomDialogMaxHeight = 0.6f;
    
    public static BottomMenu build() {
        return new BottomMenu();
    }
    
    protected BottomMenu() {
        super();
        if (bottomDialogMaxHeight <= 1 && bottomDialogMaxHeight > 0f) {
            bottomDialogMaxHeight = (int) (getRootFrameLayout().getMeasuredHeight() * bottomDialogMaxHeight);
        }
    }
    
    private OnIconChangeCallBack onIconChangeCallBack;
    private BottomDialogListView listView;
    private BaseAdapter menuArrayAdapter;
    private List<CharSequence> menuList;
    
    public static BottomMenu show(List<CharSequence> menuList) {
        BottomMenu bottomMenu = new BottomMenu();
        bottomMenu.setMenuList(menuList);
        bottomMenu.show();
        return bottomMenu;
    }
    
    public static BottomMenu showStringList(List<String> menuList) {
        BottomMenu bottomMenu = new BottomMenu();
        bottomMenu.setMenuStringList(menuList);
        bottomMenu.show();
        return bottomMenu;
    }
    
    public static BottomMenu show(String[] menuList) {
        BottomMenu bottomMenu = new BottomMenu();
        bottomMenu.setMenuList(menuList);
        bottomMenu.show();
        return bottomMenu;
    }
    
    public static BottomMenu show(CharSequence[] menuList) {
        BottomMenu bottomMenu = new BottomMenu();
        bottomMenu.setMenuList(menuList);
        bottomMenu.show();
        return bottomMenu;
    }
    
    @Override
    protected void onDialogInit(final DialogImpl dialog) {
        if (dialog != null) {
            
            if (!isAllowInterceptTouch()) {
                dialog.bkg.setMaxHeight((int) bottomDialogMaxHeight);
                if (bottomDialogMaxHeight != 0) {
                    dialogImpl.scrollView.setEnabled(false);
                }
            }
            
            int dividerDrawableResId = isLightTheme() ? R.drawable.rect_dialogx_material_menu_split_divider : R.drawable.rect_dialogx_material_menu_split_divider_night;
            int dividerHeight = 1;
            if (style.overrideBottomDialogRes() != null) {
                dividerDrawableResId = style.overrideBottomDialogRes().overrideMenuDividerDrawableRes(isLightTheme());
                dividerHeight = style.overrideBottomDialogRes().overrideMenuDividerHeight(isLightTheme());
            }
            
            listView = new BottomDialogListView(getContext());
            listView.setOverScrollMode(OVER_SCROLL_NEVER);
            listView.setDivider(getResources().getDrawable(dividerDrawableResId));
            listView.setDividerHeight(dividerHeight);
            
            RelativeLayout.LayoutParams listViewLp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.boxCustom.addView(listView, listViewLp);
            
            refreshUI();
        }
    }
    
    @Override
    public void refreshUI() {
        super.refreshUI();
        if (listView != null) {
            if (menuArrayAdapter == null) {
                menuArrayAdapter = new NormalMenuArrayAdapter(me, getContext(), R.layout.item_dialogx_material_bottom_menu_normal_text, menuList);
            }
            if (listView.getAdapter() == null) {
                listView.setAdapter(menuArrayAdapter);
            } else {
                menuArrayAdapter.notifyDataSetChanged();
            }
        }
    }
    
    public List<CharSequence> getMenuList() {
        return menuList;
    }
    
    public BottomMenu setMenuList(List<CharSequence> menuList) {
        this.menuList = menuList;
        refreshUI();
        return this;
    }
    
    public BottomMenu setMenuStringList(List<String> menuList) {
        this.menuList = new ArrayList<>();
        this.menuList.addAll(menuList);
        refreshUI();
        return this;
    }
    
    public BottomMenu setMenuList(String[] menuList) {
        this.menuList = new ArrayList<>();
        this.menuList.addAll(Arrays.asList(menuList));
        refreshUI();
        return this;
    }
    
    public BottomMenu setMenuList(CharSequence[] menuList) {
        this.menuList = Arrays.asList(menuList);
        refreshUI();
        return this;
    }
    
    public OnIconChangeCallBack getOnIconChangeCallBack() {
        return onIconChangeCallBack;
    }
    
    public BottomMenu setOnIconChangeCallBack(OnIconChangeCallBack onIconChangeCallBack) {
        this.onIconChangeCallBack = onIconChangeCallBack;
        return this;
    }
    
    public OnBackPressedListener getOnBackPressedListener() {
        return onBackPressedListener;
    }
    
    public BottomMenu setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
        refreshUI();
        return this;
    }
    
    public BottomMenu setDialogLifecycleCallback(DialogLifecycleCallback<BottomDialog> dialogLifecycleCallback) {
        this.dialogLifecycleCallback = dialogLifecycleCallback;
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
        return cancelable;
    }
    
    public BottomMenu setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
        refreshUI();
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
        refreshUI();
        return this;
    }
    
    public CharSequence getMessage() {
        return message;
    }
    
    public BottomMenu setMessage(CharSequence message) {
        this.message = message;
        refreshUI();
        return this;
    }
    
    public CharSequence getCancelText() {
        return cancelText;
    }
    
    public BottomMenu setCancelText(CharSequence cancelText) {
        this.cancelText = cancelText;
        refreshUI();
        return this;
    }
    
    public BottomMenu setCustomView(OnBindView<BottomDialog> onBindView) {
        this.onBindView = onBindView;
        refreshUI();
        return this;
    }
    
    public View getCustomView() {
        if (onBindView == null) return null;
        return onBindView.getCustomView();
    }
    
    public BottomMenu removeCustomView() {
        this.onBindView.clean();
        refreshUI();
        return this;
    }
    
    public boolean isAllowInterceptTouch() {
        return super.isAllowInterceptTouch();
    }
    
    public BottomMenu setAllowInterceptTouch(boolean allowInterceptTouch) {
        this.allowInterceptTouch = allowInterceptTouch;
        refreshUI();
        return this;
    }
    
    public BottomMenu setDialogImpl(DialogImpl dialogImpl) {
        this.dialogImpl = dialogImpl;
        return this;
    }
    
    public float getBottomDialogMaxHeight() {
        return bottomDialogMaxHeight;
    }
    
    public BottomMenu setBottomDialogMaxHeight(float bottomDialogMaxHeight) {
        this.bottomDialogMaxHeight = bottomDialogMaxHeight;
        return this;
    }
}
