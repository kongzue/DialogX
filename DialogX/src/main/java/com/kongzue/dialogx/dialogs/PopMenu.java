package com.kongzue.dialogx.dialogs;

import static android.view.View.OVER_SCROLL_NEVER;

import android.animation.ValueAnimator;
import android.graphics.Outline;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;

import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.R;
import com.kongzue.dialogx.interfaces.BaseDialog;
import com.kongzue.dialogx.interfaces.DialogConvertViewInterface;
import com.kongzue.dialogx.interfaces.DialogLifecycleCallback;
import com.kongzue.dialogx.interfaces.DialogXAnimInterface;
import com.kongzue.dialogx.interfaces.DialogXStyle;
import com.kongzue.dialogx.interfaces.OnBackPressedListener;
import com.kongzue.dialogx.interfaces.OnBackgroundMaskClickListener;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.kongzue.dialogx.interfaces.OnIconChangeCallBack;
import com.kongzue.dialogx.interfaces.OnMenuItemClickListener;
import com.kongzue.dialogx.util.BottomMenuArrayAdapter;
import com.kongzue.dialogx.util.ObjectRunnable;
import com.kongzue.dialogx.util.PopMenuArrayAdapter;
import com.kongzue.dialogx.util.TextInfo;
import com.kongzue.dialogx.util.views.BlurView;
import com.kongzue.dialogx.util.views.DialogXBaseRelativeLayout;
import com.kongzue.dialogx.util.views.MaxRelativeLayout;
import com.kongzue.dialogx.util.views.PopMenuListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2021/8/18 14:27
 */
public class PopMenu extends BaseDialog {
    
    public static long overrideEnterDuration = -1;
    public static long overrideExitDuration = -1;
    
    protected PopMenu me = this;
    protected boolean bkgInterceptTouch = true;
    protected OnBindView<PopMenu> onBindView;                               //自定义布局
    protected DialogLifecycleCallback<PopMenu> dialogLifecycleCallback;     //对话框生命周期
    protected OnBackgroundMaskClickListener<PopMenu> onBackgroundMaskClickListener;
    protected View dialogView;
    protected List<CharSequence> menuList;
    protected DialogImpl dialogImpl;
    protected View baseView;
    protected boolean overlayBaseView = true;                               //允许菜单覆盖在 baseView 上
    protected OnMenuItemClickListener<PopMenu> onMenuItemClickListener;
    protected OnIconChangeCallBack<PopMenu> onIconChangeCallBack;           //设置图标
    protected int width = -1;                                               //指定菜单宽度
    protected int height = -1;                                              //指定菜单高度
    protected TextInfo menuTextInfo;
    protected boolean offScreen = false;                                    //超出屏幕
    protected float backgroundRadius = -1;
    protected DialogXAnimInterface<PopMenu> dialogXAnimImpl;
    protected OnBackPressedListener<PopMenu> onBackPressedListener;
    protected int pressedIndex = -1;
    
    protected int alignGravity = -1;                                        //指定菜单相对 baseView 的位置
    
    //记录 baseView 位置
    protected int[] baseViewLoc = new int[2];
    
    public PopMenu() {
        super();
    }
    
    public PopMenu(View baseView, List<CharSequence> menuList) {
        this.menuList = new ArrayList<>();
        this.menuList.addAll(menuList);
        this.baseView = baseView;
    }
    
    public PopMenu(View baseView, CharSequence[] menuList) {
        this.menuList = new ArrayList<>();
        this.menuList.addAll(Arrays.asList(menuList));
        this.baseView = baseView;
    }
    
    public PopMenu(List<CharSequence> menuList) {
        this.menuList = new ArrayList<>();
        this.menuList.addAll(menuList);
    }
    
    public PopMenu(CharSequence[] menuList) {
        this.menuList = new ArrayList<>();
        this.menuList.addAll(Arrays.asList(menuList));
    }
    
    public PopMenu(OnBindView<PopMenu> onBindView) {
        this.onBindView = onBindView;
    }
    
    public PopMenu(View baseView, OnBindView<PopMenu> onBindView) {
        this.baseView = baseView;
        this.onBindView = onBindView;
    }
    
    public PopMenu(View baseView, List<CharSequence> menuList, OnBindView<PopMenu> onBindView) {
        this.baseView = baseView;
        this.menuList = new ArrayList<>();
        this.menuList.addAll(menuList);
        this.onBindView = onBindView;
    }
    
    public PopMenu(View baseView, CharSequence[] menuList, OnBindView<PopMenu> onBindView) {
        this.baseView = baseView;
        this.menuList = new ArrayList<>();
        this.menuList.addAll(Arrays.asList(menuList));
        this.onBindView = onBindView;
    }
    
    public PopMenu(List<CharSequence> menuList, OnBindView<PopMenu> onBindView) {
        this.menuList = new ArrayList<>();
        this.menuList.addAll(menuList);
        this.onBindView = onBindView;
    }
    
    public PopMenu(CharSequence[] menuList, OnBindView<PopMenu> onBindView) {
        this.menuList = new ArrayList<>();
        this.menuList.addAll(Arrays.asList(menuList));
        this.onBindView = onBindView;
    }
    
    public static PopMenu build() {
        return new PopMenu();
    }
    
    public static PopMenu build(DialogXStyle style) {
        return new PopMenu().setStyle(style);
    }
    
    public static PopMenu show(CharSequence[] menus) {
        PopMenu popMenu = new PopMenu(menus);
        popMenu.show();
        return popMenu;
    }
    
    public static PopMenu show(List<CharSequence> menuList) {
        PopMenu popMenu = new PopMenu(menuList);
        popMenu.show();
        return popMenu;
    }
    
    public static PopMenu show(View baseView, CharSequence[] menus) {
        PopMenu popMenu = new PopMenu(baseView, menus);
        popMenu.show();
        return popMenu;
    }
    
    public static PopMenu show(View baseView, List<CharSequence> menuList) {
        PopMenu popMenu = new PopMenu(baseView, menuList);
        popMenu.show();
        return popMenu;
    }
    
    public static PopMenu show(View baseView, CharSequence[] menus, OnBindView<PopMenu> onBindView) {
        PopMenu popMenu = new PopMenu(baseView, menus, onBindView);
        popMenu.show();
        return popMenu;
    }
    
    public static PopMenu show(View baseView, List<CharSequence> menuList, OnBindView<PopMenu> onBindView) {
        PopMenu popMenu = new PopMenu(baseView, menuList, onBindView);
        popMenu.show();
        return popMenu;
    }
    
    public static PopMenu show(CharSequence[] menus, OnBindView<PopMenu> onBindView) {
        PopMenu popMenu = new PopMenu(menus, onBindView);
        popMenu.show();
        return popMenu;
    }
    
    public static PopMenu show(List<CharSequence> menuList, OnBindView<PopMenu> onBindView) {
        PopMenu popMenu = new PopMenu(menuList, onBindView);
        popMenu.show();
        return popMenu;
    }
    
    public PopMenu show() {
        if (isHide && getDialogView() != null && isShow) {
            if (hideWithExitAnim && getDialogImpl() != null) {
                getDialogView().setVisibility(View.VISIBLE);
                getDialogImpl().getDialogXAnimImpl().doShowAnim(me, new ObjectRunnable<Float>() {
                    @Override
                    public void run(Float value) {
                        getDialogImpl().boxRoot.setBkgAlpha(value);
                    }
                });
            } else {
                getDialogView().setVisibility(View.VISIBLE);
            }
            return this;
        }
        
        super.beforeShow();
        if (getDialogView() == null) {
            int layoutId = isLightTheme() ? R.layout.layout_dialogx_popmenu_material : R.layout.layout_dialogx_popmenu_material_dark;
            if (getStyle().popMenuSettings() != null) {
                if (getStyle().popMenuSettings().layout(isLightTheme()) != 0) {
                    layoutId = getStyle().popMenuSettings().layout(isLightTheme());
                }
            }
            
            dialogView = createView(layoutId);
            dialogImpl = new DialogImpl(dialogView);
            if (dialogView != null) {
                dialogView.setTag(me);
            }
        }
        show(dialogView);
        if (baseView != null) {
            baseView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    if (getDialogImpl() != null) {
                        baseViewLoc = new int[2];
                        baseView.getLocationOnScreen(baseViewLoc);
                        
                        int width = baseView.getWidth();
                        int height = baseView.getHeight();
                        
                        int left = baseViewLoc[0];
                        int top = baseViewLoc[1] + (overlayBaseView ? 0 : height);
                        
                        getDialogImpl().boxBody.setX(left);
                        getDialogImpl().boxBody.setY(top);
                        
                        if (width != 0 && getDialogImpl().boxBody.getWidth() != width) {
                            RelativeLayout.LayoutParams rLp = new RelativeLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
                            getDialogImpl().boxBody.setLayoutParams(rLp);
                        }
                        
                        baseView.getViewTreeObserver().removeOnPreDrawListener(this);
                    }
                    return true;
                }
            });
        }
        return this;
    }
    
    protected PopMenuArrayAdapter menuListAdapter;
    
    public class DialogImpl implements DialogConvertViewInterface {
        
        public DialogXBaseRelativeLayout boxRoot;
        public MaxRelativeLayout boxBody;
        public RelativeLayout boxCustom;
        public PopMenuListView listMenu;
        public BlurView blurView;
        
        public DialogImpl(View convertView) {
            boxRoot = convertView.findViewById(R.id.box_root);
            boxBody = convertView.findViewById(R.id.box_body);
            boxCustom = convertView.findViewById(R.id.box_custom);
            listMenu = convertView.findViewById(R.id.listMenu);
            init();
        }
        
        @Override
        public void init() {
            closing = false;
            if (menuListAdapter == null) {
                menuListAdapter = new PopMenuArrayAdapter(me, getTopActivity(), menuList);
            }
            
            boxRoot.setParentDialog(me);
            boxRoot.setOnLifecycleCallBack(new DialogXBaseRelativeLayout.OnLifecycleCallBack() {
                @Override
                public void onShow() {
                    isShow = true;
                    preShow = false;
                    lifecycle.setCurrentState(Lifecycle.State.CREATED);
                    onDialogShow();
                    getDialogLifecycleCallback().onShow(me);
                    refreshUI();
                }
                
                @Override
                public void onDismiss() {
                    isShow = false;
                    getDialogLifecycleCallback().onDismiss(me);
                    menuListAdapter = null;
                    dialogImpl = null;
                    baseView = null;
                    dialogLifecycleCallback = null;
                    lifecycle.setCurrentState(Lifecycle.State.DESTROYED);
                    System.gc();
                }
            });
            
            boxRoot.setOnBackPressedListener(new DialogXBaseRelativeLayout.PrivateBackPressedListener() {
                @Override
                public boolean onBackPressed() {
                    if (onBackPressedListener != null) {
                        if (onBackPressedListener.onBackPressed(me)) {
                            dismiss();
                        }
                    }else{
                        if (isCancelable()) {
                            dismiss();
                        }
                    }
                    return true;
                }
            });
            listMenu.setMaxHeight(getRootFrameLayout() == null ? dip2px(500) : getRootFrameLayout().getMeasuredHeight() - dip2px(150));
            
            boxBody.setVisibility(View.INVISIBLE);
            boxBody.post(new Runnable() {
                
                @Override
                public void run() {
                    getDialogXAnimImpl().doShowAnim(me, new ObjectRunnable<Float>() {
                        @Override
                        public void run(Float value) {
                            boxRoot.setBkgAlpha(value);
                        }
                    });
                    lifecycle.setCurrentState(Lifecycle.State.RESUMED);
                }
            });
            
            int dividerDrawableResId = 0;
            int dividerHeight = 0;
            if (style.popMenuSettings() != null) {
                dividerDrawableResId = style.popMenuSettings().overrideMenuDividerDrawableRes(isLightTheme());
                dividerHeight = style.popMenuSettings().overrideMenuDividerHeight(isLightTheme());
            }
            if (dividerDrawableResId == 0) {
                dividerDrawableResId = isLightTheme() ? R.drawable.rect_dialogx_material_menu_split_divider : R.drawable.rect_dialogx_material_menu_split_divider_night;
            }
            
            listMenu.setOverScrollMode(OVER_SCROLL_NEVER);
            listMenu.setVerticalScrollBarEnabled(false);
            listMenu.setDivider(getResources().getDrawable(dividerDrawableResId));
            listMenu.setDividerHeight(dividerHeight);
            
            listMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (!closing) {
                        if (!getOnMenuItemClickListener().onClick(me, menuList.get(position), position)) {
                            dismiss();
                        }
                    }
                }
            });
            onDialogInit();
        }
        
        @Override
        public void refreshView() {
            if (boxRoot == null || getTopActivity() == null) {
                return;
            }
            boxRoot.setRootPadding(screenPaddings[0],screenPaddings[1],screenPaddings[2],screenPaddings[3]);
            if (listMenu.getAdapter() == null) {
                listMenu.setAdapter(menuListAdapter);
            } else {
                if (menuListAdapter.getMenuList() != menuList) {
                    menuListAdapter = new PopMenuArrayAdapter(me, getTopActivity(), menuList);
                    listMenu.setAdapter(menuListAdapter);
                } else {
                    menuListAdapter.notifyDataSetChanged();
                }
            }
            
            if (bkgInterceptTouch) {
                if (isCancelable()) {
                    boxRoot.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onBackgroundMaskClickListener == null || !onBackgroundMaskClickListener.onClick(me, v)) {
                                doDismiss(v);
                            }
                        }
                    });
                } else {
                    boxRoot.setOnClickListener(null);
                }
            } else {
                boxRoot.setClickable(false);
            }
            
            if (backgroundRadius > -1) {
                GradientDrawable gradientDrawable = (GradientDrawable) boxBody.getBackground();
                if (gradientDrawable != null) gradientDrawable.setCornerRadius(backgroundRadius);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    boxBody.setOutlineProvider(new ViewOutlineProvider() {
                        @Override
                        public void getOutline(View view, Outline outline) {
                            outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), backgroundRadius);
                        }
                    });
                    boxBody.setClipToOutline(true);
                }
            }
            
            if (onBindView != null && onBindView.getCustomView() != null) {
                onBindView.bindParent(boxCustom, me);
                boxCustom.setVisibility(View.VISIBLE);
            } else {
                boxCustom.setVisibility(View.GONE);
            }
            
            if (width != -1) {
                boxBody.setMaxWidth(width);
                boxBody.setMinimumWidth(width);
            }
            
            if (height != -1) {
                boxBody.setMaxHeight(height);
                boxBody.setMinimumHeight(height);
            }
            
            onDialogRefreshUI();
        }
        
        @Override
        public void doDismiss(View v) {
            if (v != null) {
                v.setEnabled(false);
            }
            
            if (!dismissAnimFlag) {
                dismissAnimFlag = true;
                boxRoot.post(new Runnable() {
                    @Override
                    public void run() {
                        getDialogXAnimImpl().doExitAnim(me, new ObjectRunnable<Float>() {
                            @Override
                            public void run(Float value) {
                                if (boxRoot != null && baseView == null) {
                                    boxRoot.setBkgAlpha(value);
                                }
                                if (value == 0f) {
                                    dismiss(dialogView);
                                }
                            }
                        });
                    }
                });
            }
        }
        
        protected DialogXAnimInterface<PopMenu> getDialogXAnimImpl() {
            if (dialogXAnimImpl == null) {
                dialogXAnimImpl = new DialogXAnimInterface<PopMenu>() {
                    
                    int selectMenuIndex = -1;
                    
                    @Override
                    public void doShowAnim(PopMenu dialog, ObjectRunnable<Float> animProgress) {
                        long enterAnimDurationTemp = enterAnimDuration != -1 ? enterAnimDuration : (overrideEnterDuration == -1 ? 150 : overrideEnterDuration);
                        
                        if (baseView != null) {
                            //有绑定按钮的情况下
                            int targetHeight = getBodyRealHeight();
                            boxBody.getLayoutParams().height = 1;
                            
                            if (overlayBaseView && !listMenu.isCanScroll()) {
                                if (baseView instanceof TextView) {
                                    String baseText = ((TextView) baseView).getText().toString();
                                    for (CharSequence c : menuList) {
                                        if (TextUtils.equals(c.toString(), baseText)) {
                                            selectMenuIndex = menuList.indexOf(c);
                                            break;
                                        }
                                    }
                                }
                                //找到已选中的项目
                                if (selectMenuIndex != -1) {
                                    int[] viewLoc = new int[2];
                                    if (listMenu.getChildAt(selectMenuIndex) != null) {
                                        int itemHeight = listMenu.getChildAt(selectMenuIndex).getMeasuredHeight();
                                        listMenu.getChildAt(selectMenuIndex).getLocationOnScreen(viewLoc);
                                        boxBody.setY(baseViewLoc[1] + (baseView.getMeasuredHeight() / 2f) - (viewLoc[1] - boxBody.getY()) - (itemHeight / 2f));
                                    }
                                }
                            }
                            
                            //菜单位置计算逻辑
                            int baseViewLeft = baseViewLoc[0];
                            int baseViewTop = baseViewLoc[1];
                            int calX = 0, calY = 0;
                            if (alignGravity != -1) {
                                if (isAlignGravity(Gravity.CENTER_VERTICAL)) {
                                    calY = (Math.max(0, baseViewTop + baseView.getMeasuredHeight() / 2 - boxBody.getHeight() / 2));
                                }
                                if (isAlignGravity(Gravity.CENTER_HORIZONTAL)) {
                                    calX = (Math.max(0, baseViewLeft + (
                                            getWidth() > 0 ? baseView.getMeasuredWidth() / 2 - getWidth() / 2 : 0
                                    )));
                                }
                                if (isAlignGravity(Gravity.CENTER)) {
                                    calX = (Math.max(0, baseViewLeft + (
                                            getWidth() > 0 ? (baseView.getMeasuredWidth() / 2 - getWidth() / 2) : 0
                                    )));
                                    calY = (Math.max(0, baseViewTop + baseView.getMeasuredHeight() / 2 - boxBody.getHeight() / 2));
                                }
                                if (overlayBaseView) {
                                    //菜单覆盖在 baseView 上时
                                    if (isAlignGravity(Gravity.TOP)) {
                                        calY = (baseViewTop - boxBody.getHeight() + baseView.getHeight());
                                    }
                                    if (isAlignGravity(Gravity.LEFT)) {
                                        calX = (baseViewLeft);
                                    }
                                    if (isAlignGravity(Gravity.RIGHT)) {
                                        calX = (baseViewLeft + (getWidth() > 0 ? baseView.getMeasuredWidth() - width : 0));
                                    }
                                    if (isAlignGravity(Gravity.BOTTOM)) {
                                        calY = (baseViewTop);
                                    }
                                } else {
                                    if (isAlignGravity(Gravity.TOP)) {
                                        calY = (Math.max(0, baseViewTop - boxBody.getHeight()));
                                    }
                                    if (isAlignGravity(Gravity.LEFT)) {
                                        calX = (Math.max(0, baseViewLeft - boxBody.getWidth()));
                                    }
                                    if (isAlignGravity(Gravity.RIGHT)) {
                                        calX = (Math.max(0, baseViewLeft + baseView.getWidth()));
                                    }
                                    if (isAlignGravity(Gravity.BOTTOM)) {
                                        calY = (Math.max(0, baseViewTop + baseView.getHeight()));
                                    }
                                }
                                if (!offScreen) {
                                    if (calX < 0) {
                                        calX = 0;
                                    }
                                    if ((calX + boxBody.getWidth()) > boxRoot.getUseAreaWidth()) {
                                        calX = boxRoot.getUseAreaWidth() - boxBody.getWidth();
                                    }
                                    if (calY < 0) {
                                        calY = 0;
                                    }
                                    if ((calY + boxBody.getHeight()) > boxRoot.getUseAreaHeight()) {
                                        calY = boxRoot.getUseAreaHeight() - boxBody.getHeight();
                                    }
                                }
                                
                                if (calX != 0) boxBody.setX(calX);
                                if (calY != 0) boxBody.setY(calY);
                            }
                            
                            //展开动画
                            Animation enterAnim = new Animation() {
                                @Override
                                protected void applyTransformation(float interpolatedTime, Transformation t) {
                                    int aimHeight = interpolatedTime == 1 ? ViewGroup.LayoutParams.WRAP_CONTENT : (int) (targetHeight * interpolatedTime);
                                    boxBody.getLayoutParams().height = aimHeight;
                                    boxBody.getLayoutParams().width = getWidth() == -1 ? baseView.getWidth() : getWidth();
                                    if ((boxBody.getY() + aimHeight) > boxRoot.getSafeHeight()) {
                                        boxBody.setY(boxRoot.getSafeHeight() - aimHeight);
                                    }
                                    
                                    if (!offScreen) {
                                        float calX = boxBody.getX();
                                        float calY = boxBody.getY();
                                        if (calX < 0) {
                                            calX = 0;
                                        }
                                        if ((calX + boxBody.getWidth()) > boxRoot.getUseAreaWidth()) {
                                            calX = boxRoot.getUseAreaWidth() - boxBody.getWidth();
                                        }
                                        if (calY < 0) {
                                            calY = 0;
                                        }
                                        if ((calY + boxBody.getHeight()) > boxRoot.getUseAreaHeight()) {
                                            calY = boxRoot.getUseAreaHeight() - boxBody.getHeight();
                                        }
                                        boxBody.setX(calX);
                                        boxBody.setY(calY);
                                    }
                                    
                                    boxBody.requestLayout();
                                }
                                
                                @Override
                                public boolean willChangeBounds() {
                                    return true;
                                }
                            };
                            enterAnim.setInterpolator(new DecelerateInterpolator(2f));
                            enterAnim.setDuration(enterAnimDurationTemp);
                            boxBody.startAnimation(enterAnim);
                            boxBody.setVisibility(View.VISIBLE);
                            
                            //模糊背景
                            if (getStyle().popMenuSettings() != null &&
                                    getStyle().popMenuSettings().blurBackgroundSettings() != null &&
                                    getStyle().popMenuSettings().blurBackgroundSettings().blurBackground()
                            ) {
                                int blurFrontColor = getResources().getColor(getStyle().popMenuSettings().blurBackgroundSettings().blurForwardColorRes(isLightTheme()));
                                blurView = new BlurView(getOwnActivity(), null);
                                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(boxBody.getWidth(), targetHeight);
                                blurView.setOverlayColor(backgroundColor == -1 ? blurFrontColor : backgroundColor);
                                blurView.setTag("blurView");
                                blurView.setRadiusPx(getStyle().popMenuSettings().blurBackgroundSettings().blurBackgroundRoundRadiusPx());
                                boxBody.addView(blurView, 0, params);
                            }
                        } else {
                            //无绑定按钮的情况下
                            RelativeLayout.LayoutParams rLp = (RelativeLayout.LayoutParams) boxBody.getLayoutParams();
                            rLp.addRule(RelativeLayout.CENTER_IN_PARENT);
                            rLp.width = getWidth() == -1 ? RelativeLayout.LayoutParams.MATCH_PARENT : getWidth();
                            rLp.leftMargin = dip2px(50);
                            rLp.rightMargin = dip2px(50);
                            boxBody.setLayoutParams(rLp);
                            boxBody.setAlpha(0f);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                boxBody.setElevation(dip2px(20));
                            }
                            boxBody.setVisibility(View.VISIBLE);
                            boxBody.animate().alpha(1f).setDuration(enterAnimDurationTemp);
                            boxBody.post(new Runnable() {
                                @Override
                                public void run() {
                                    //模糊背景
                                    if (getStyle().popMenuSettings() != null &&
                                            getStyle().popMenuSettings().blurBackgroundSettings() != null &&
                                            getStyle().popMenuSettings().blurBackgroundSettings().blurBackground()
                                    ) {
                                        int blurFrontColor = getResources().getColor(getStyle().popMenuSettings().blurBackgroundSettings().blurForwardColorRes(isLightTheme()));
                                        blurView = new BlurView(getOwnActivity(), null);
                                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(boxBody.getWidth(), boxBody.getHeight());
                                        blurView.setOverlayColor(backgroundColor == -1 ? blurFrontColor : backgroundColor);
                                        blurView.setTag("blurView");
                                        blurView.setRadiusPx(getStyle().popMenuSettings().blurBackgroundSettings().blurBackgroundRoundRadiusPx());
                                        boxBody.addView(blurView, 0, params);
                                    }
                                }
                            });
                            
                            ValueAnimator bkgAlpha = ValueAnimator.ofFloat(0f, 1f);
                            bkgAlpha.setDuration(enterAnimDurationTemp);
                            bkgAlpha.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator animation) {
                                    animProgress.run((Float) animation.getAnimatedValue());
                                }
                            });
                            bkgAlpha.start();
                        }
                    }
                    
                    @Override
                    public void doExitAnim(PopMenu dialog, ObjectRunnable<Float> animProgress) {
                        if (overrideExitDuration != -1) {
                            exitAnimDuration = overrideExitDuration;
                        }
                        Animation exitAnim = AnimationUtils.loadAnimation(getTopActivity() == null ? boxRoot.getContext() : getTopActivity(), R.anim.anim_dialogx_default_exit);
                        if (exitAnimDuration != -1) {
                            exitAnim.setDuration(exitAnimDuration);
                        }
                        boxBody.startAnimation(exitAnim);
                        
                        boxRoot.animate()
                                .alpha(0f)
                                .setInterpolator(new AccelerateInterpolator())
                                .setDuration(exitAnimDuration == -1 ? exitAnim.getDuration() : exitAnimDuration);
                        
                        ValueAnimator bkgAlpha = ValueAnimator.ofFloat(1, 0f);
                        bkgAlpha.setDuration(exitAnimDuration == -1 ? exitAnim.getDuration() : exitAnimDuration);
                        bkgAlpha.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                animProgress.run((Float) animation.getAnimatedValue());
                            }
                        });
                        bkgAlpha.start();
                    }
                };
            }
            return dialogXAnimImpl;
        }
    }
    
    private int getBodyRealHeight() {
        if (getDialogImpl() == null) {
            return 0;
        }
        
        int matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) getDialogImpl().boxBody.getParent()).getWidth(), View.MeasureSpec.EXACTLY);
        int wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) getDialogImpl().boxBody.getParent()).getHeight(), View.MeasureSpec.AT_MOST);
        getDialogImpl().boxBody.measure(matchParentMeasureSpec, wrapContentMeasureSpec);
        
        return getDialogImpl().boxBody.getMeasuredHeight();
    }
    
    private boolean closing;
    
    public void dismiss() {
        closing = true;
        runOnMain(new Runnable() {
            @Override
            public void run() {
                if (dialogImpl == null) {
                    return;
                }
                dialogImpl.doDismiss(null);
            }
        });
    }
    
    @Override
    public void restartDialog() {
        if (dialogView != null) {
            dismiss(dialogView);
            isShow = false;
        }
        if (getDialogImpl().boxCustom != null) {
            getDialogImpl().boxCustom.removeAllViews();
        }
        show();
    }
    
    @Override
    protected void shutdown() {
    
    }
    
    @Override
    public View getDialogView() {
        if (dialogView == null) {
            return null;
        }
        return dialogView;
    }
    
    public List<CharSequence> getMenuList() {
        return menuList;
    }
    
    public PopMenu setMenuList(List<CharSequence> menuList) {
        this.menuList = new ArrayList<>();
        this.menuList.addAll(menuList);
        refreshUI();
        return this;
    }
    
    public PopMenu setMenuList(String[] menuList) {
        this.menuList = new ArrayList<>();
        this.menuList.addAll(Arrays.asList(menuList));
        refreshUI();
        return this;
    }
    
    public PopMenu setMenuList(CharSequence[] menuList) {
        this.menuList = new ArrayList<>();
        this.menuList.addAll(Arrays.asList(menuList));
        refreshUI();
        return this;
    }
    
    public void refreshUI() {
        if (getDialogImpl() == null) {
            return;
        }
        runOnMain(new Runnable() {
            @Override
            public void run() {
                if (dialogImpl != null) {
                    dialogImpl.refreshView();
                }
            }
        });
    }
    
    @Override
    public boolean isCancelable() {
        return true;
    }
    
    public DialogImpl getDialogImpl() {
        return dialogImpl;
    }
    
    @Override
    public String dialogKey() {
        return getClass().getSimpleName() + "(" + Integer.toHexString(hashCode()) + ")";
    }
    
    public DialogLifecycleCallback<PopMenu> getDialogLifecycleCallback() {
        return dialogLifecycleCallback == null ? new DialogLifecycleCallback<PopMenu>() {
        } : dialogLifecycleCallback;
    }
    
    public PopMenu setDialogLifecycleCallback(DialogLifecycleCallback<PopMenu> dialogLifecycleCallback) {
        this.dialogLifecycleCallback = dialogLifecycleCallback;
        if (isShow) {
            dialogLifecycleCallback.onShow(me);
        }
        return this;
    }
    
    public boolean isOverlayBaseView() {
        return overlayBaseView;
    }
    
    public PopMenu setOverlayBaseView(boolean overlayBaseView) {
        this.overlayBaseView = overlayBaseView;
        refreshUI();
        return this;
    }
    
    public OnMenuItemClickListener<PopMenu> getOnMenuItemClickListener() {
        return onMenuItemClickListener == null ? new OnMenuItemClickListener<PopMenu>() {
            @Override
            public boolean onClick(PopMenu dialog, CharSequence text, int index) {
                return false;
            }
        } : onMenuItemClickListener;
    }
    
    public PopMenu setOnMenuItemClickListener(OnMenuItemClickListener<PopMenu> onMenuItemClickListener) {
        this.onMenuItemClickListener = onMenuItemClickListener;
        return this;
    }
    
    public OnIconChangeCallBack<PopMenu> getOnIconChangeCallBack() {
        return onIconChangeCallBack;
    }
    
    public PopMenu setOnIconChangeCallBack(OnIconChangeCallBack<PopMenu> onIconChangeCallBack) {
        this.onIconChangeCallBack = onIconChangeCallBack;
        return this;
    }
    
    public PopMenu setCustomView(OnBindView<PopMenu> onBindView) {
        this.onBindView = onBindView;
        refreshUI();
        return this;
    }
    
    public View getCustomView() {
        if (onBindView == null) {
            return null;
        }
        return onBindView.getCustomView();
    }
    
    public PopMenu removeCustomView() {
        this.onBindView.clean();
        refreshUI();
        return this;
    }
    
    public int getWidth() {
        return width;
    }
    
    /**
     * 设置菜单 UI 宽度（单位：像素）
     *
     * @param width 宽度（像素）
     * @return PopMenu实例
     */
    public PopMenu setWidth(int width) {
        this.width = width;
        refreshUI();
        return this;
    }
    
    public int getHeight() {
        return height;
    }
    
    /**
     * 设置菜单 UI 高度（单位：像素）
     *
     * @param height 高度（像素）
     * @return PopMenu实例
     */
    public PopMenu setHeight(int height) {
        this.height = height;
        refreshUI();
        return this;
    }
    
    public int getAlignGravity() {
        return alignGravity;
    }
    
    /**
     * 判断是否有设置对应的位置关系
     *
     * @param gravity 位置关系
     * @return 是否具备位置关系
     */
    public boolean isAlignGravity(int gravity) {
        return (alignGravity & gravity) == gravity;
    }
    
    public PopMenu setAlignGravity(int alignGravity) {
        this.alignGravity = alignGravity;
        return this;
    }
    
    public PopMenu setDialogImplMode(DialogX.IMPL_MODE dialogImplMode) {
        this.dialogImplMode = dialogImplMode;
        return this;
    }
    
    public TextInfo getMenuTextInfo() {
        if (menuTextInfo == null) {
            return DialogX.menuTextInfo;
        }
        return menuTextInfo;
    }
    
    public PopMenu setMenuTextInfo(TextInfo menuTextInfo) {
        this.menuTextInfo = menuTextInfo;
        return this;
    }
    
    public boolean isOffScreen() {
        return offScreen;
    }
    
    /**
     * 是否允许超出屏幕显示
     * PopMenu 默认位置显示在屏幕内，开启后将无视屏幕范围限制。
     *
     * @param offScreen 超出屏幕
     * @return PopMenu实例
     */
    public PopMenu setOffScreen(boolean offScreen) {
        this.offScreen = offScreen;
        return this;
    }
    
    public boolean isBkgInterceptTouch() {
        return bkgInterceptTouch;
    }
    
    public PopMenu setBkgInterceptTouch(boolean bkgInterceptTouch) {
        this.bkgInterceptTouch = bkgInterceptTouch;
        return this;
    }
    
    public OnBackgroundMaskClickListener<PopMenu> getOnBackgroundMaskClickListener() {
        return onBackgroundMaskClickListener;
    }
    
    public PopMenu setOnBackgroundMaskClickListener(OnBackgroundMaskClickListener<PopMenu> onBackgroundMaskClickListener) {
        this.onBackgroundMaskClickListener = onBackgroundMaskClickListener;
        return this;
    }
    
    public PopMenu setStyle(DialogXStyle style) {
        this.style = style;
        return this;
    }
    
    public PopMenu setTheme(DialogX.THEME theme) {
        this.theme = theme;
        return this;
    }
    
    public PopMenu setRadius(float radiusPx) {
        backgroundRadius = radiusPx;
        refreshUI();
        return this;
    }
    
    public float getRadius() {
        return backgroundRadius;
    }
    
    private boolean isHide;
    
    public void hide() {
        isHide = true;
        hideWithExitAnim = false;
        if (getDialogView() != null) {
            getDialogView().setVisibility(View.GONE);
        }
    }
    
    protected boolean hideWithExitAnim;
    
    public void hideWithExitAnim() {
        hideWithExitAnim = true;
        isHide = true;
        if (getDialogImpl() != null) {
            getDialogImpl().getDialogXAnimImpl().doExitAnim(me, new ObjectRunnable<Float>() {
                @Override
                public void run(Float value) {
                    getDialogImpl().boxRoot.setBkgAlpha(value);
                    if (value == 0 && getDialogView() != null) {
                        getDialogView().setVisibility(View.GONE);
                    }
                }
            });
        }
    }
    
    public DialogXAnimInterface<PopMenu> getDialogXAnimImpl() {
        return dialogXAnimImpl;
    }
    
    public PopMenu setDialogXAnimImpl(DialogXAnimInterface<PopMenu> dialogXAnimImpl) {
        this.dialogXAnimImpl = dialogXAnimImpl;
        return this;
    }
    
    public OnBackPressedListener<PopMenu> getOnBackPressedListener() {
        return onBackPressedListener;
    }
    
    public PopMenu setOnBackPressedListener(OnBackPressedListener<PopMenu> onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
        return this;
    }
    
    public View getBaseView() {
        return baseView;
    }
    
    public PopMenu setBaseView(View baseView) {
        this.baseView = baseView;
        refreshUI();
        return this;
    }
    
    public PopMenu setRootPadding(int padding) {
        this.screenPaddings = new int[]{padding, padding, padding, padding};
        refreshUI();
        return this;
    }
    
    public PopMenu setRootPadding(int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {
        this.screenPaddings = new int[]{paddingLeft, paddingTop, paddingRight, paddingBottom};
        refreshUI();
        return this;
    }
    
    public int getPressedIndex() {
        return pressedIndex;
    }
    
    public PopMenu setPressedIndex(int pressedIndex) {
        this.pressedIndex = pressedIndex;
        refreshUI();
        return this;
    }
}
