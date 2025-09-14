package com.kongzue.dialogx.dialogs;

import static android.view.View.OVER_SCROLL_NEVER;
import static android.view.View.VISIBLE;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Outline;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
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
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.R;
import com.kongzue.dialogx.interfaces.BaseDialog;
import com.kongzue.dialogx.interfaces.BlurViewType;
import com.kongzue.dialogx.interfaces.DialogConvertViewInterface;
import com.kongzue.dialogx.interfaces.DialogLifecycleCallback;
import com.kongzue.dialogx.interfaces.DialogXAnimInterface;
import com.kongzue.dialogx.interfaces.DialogXRunnable;
import com.kongzue.dialogx.interfaces.DialogXStyle;
import com.kongzue.dialogx.interfaces.MenuItemLayoutRefreshCallback;
import com.kongzue.dialogx.interfaces.OnBackPressedListener;
import com.kongzue.dialogx.interfaces.OnBackgroundMaskClickListener;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.kongzue.dialogx.interfaces.OnIconChangeCallBack;
import com.kongzue.dialogx.interfaces.OnMenuItemClickListener;
import com.kongzue.dialogx.util.DialogXViewLoc;
import com.kongzue.dialogx.util.ItemDivider;
import com.kongzue.dialogx.util.PopMenuArrayAdapter;
import com.kongzue.dialogx.util.TextInfo;
import com.kongzue.dialogx.util.views.DialogXBaseRelativeLayout;
import com.kongzue.dialogx.util.views.MaxRelativeLayout;
import com.kongzue.dialogx.util.views.PopMenuListView;

import java.lang.ref.WeakReference;
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
 * @createTime: 2021/8/18 14:27
 */
public class PopMenu extends BaseDialog {

    public static long overrideEnterDuration = -1;
    public static long overrideExitDuration = -1;

    protected PopMenu me = this;
    protected boolean bkgInterceptTouch = true;
    protected OnBindView<PopMenu> onBindView;                               // 自定义布局
    protected DialogLifecycleCallback<PopMenu> dialogLifecycleCallback;     // 对话框生命周期
    protected OnBackgroundMaskClickListener<PopMenu> onBackgroundMaskClickListener;
    protected List<CharSequence> menuList;
    protected List<Integer> iconResIds;
    protected boolean autoTintIconInLightOrDarkMode = true;
    protected DialogImpl dialogImpl;
    protected WeakReference<View> baseViewWeakReference;
    protected boolean overlayBaseView = true;                               // 允许菜单覆盖在 baseView 上
    protected OnMenuItemClickListener<PopMenu> onMenuItemClickListener;
    protected OnIconChangeCallBack<PopMenu> onIconChangeCallBack;           // 设置图标
    protected int width = -1;                                               // 指定菜单宽度
    protected int height = -1;                                              // 指定菜单高度
    protected TextInfo menuTextInfo;
    protected boolean offScreen = false;                                    // 超出屏幕
    protected float backgroundRadius = DialogX.defaultPopMenuBackgroundRadius;
    protected DialogXAnimInterface<PopMenu> dialogXAnimImpl;
    protected OnBackPressedListener<PopMenu> onBackPressedListener;
    protected MenuItemLayoutRefreshCallback<PopMenu> menuMenuItemLayoutRefreshCallback;
    protected int pressedIndex = -1;
    protected Map<Integer, Boolean> menuUsability = new HashMap<Integer, Boolean>();
    protected ItemDivider itemDivider;

    protected int alignGravity = -1;                                        // 指定菜单相对 baseView 的位置

    // 记录 baseView 位置
    protected DialogXViewLoc baseViewLoc = new DialogXViewLoc();
    private int selectIndex;
    public boolean notCheckHash = false;
    public int lastHash = -1;

    public PopMenu() {
        super();
    }

    public PopMenu(View baseView, List<CharSequence> menuList) {
        this.menuList = new ArrayList<>();
        this.menuList.addAll(menuList);
        this.baseView(baseView);
    }

    public PopMenu(View baseView, CharSequence[] menuList) {
        this.menuList = new ArrayList<>();
        this.menuList.addAll(Arrays.asList(menuList));
        this.baseView(baseView);
    }

    public PopMenu(List<CharSequence> menuList) {
        this.menuList = new ArrayList<>();
        this.menuList.addAll(menuList);
    }

    public PopMenu(CharSequence... menuList) {
        this.menuList = new ArrayList<>();
        this.menuList.addAll(Arrays.asList(menuList));
    }

    public PopMenu(String... menuList) {
        this.menuList = new ArrayList<>();
        this.menuList.addAll(Arrays.asList(menuList));
    }

    public PopMenu(OnBindView<PopMenu> onBindView) {
        this.onBindView = onBindView;
    }

    public PopMenu(View baseView, OnBindView<PopMenu> onBindView) {
        this.baseView(baseView);
        this.onBindView = onBindView;
    }

    public PopMenu(View baseView, List<CharSequence> menuList, OnBindView<PopMenu> onBindView) {
        this.baseView(baseView);
        this.menuList = new ArrayList<>();
        this.menuList.addAll(menuList);
        this.onBindView = onBindView;
    }

    public PopMenu(View baseView, CharSequence[] menuList, OnBindView<PopMenu> onBindView) {
        this.baseView(baseView);
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

    public static PopMenu show(CharSequence... menus) {
        PopMenu popMenu = new PopMenu(menus);
        popMenu.show();
        return popMenu;
    }

    public static PopMenu show(String... menus) {
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

    private ViewTreeObserver viewTreeObserver;
    private ViewTreeObserver.OnDrawListener baseViewDrawListener;

    public PopMenu show() {
        if (isHide && getDialogView() != null && isShow) {
            if (hideWithExitAnim && getDialogImpl() != null) {
                getDialogImpl().boxBody.clearAnimation();
                getDialogView().setVisibility(View.VISIBLE);
                getDialogImpl().boxRoot.animate().alpha(1f);
                getDialogImpl().getDialogXAnimImpl().doShowAnim(me, getDialogImpl().boxBody);
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
            layoutId = getCustomDialogLayoutResId(isLightTheme()) != 0 ? getCustomDialogLayoutResId(isLightTheme()) : layoutId;

            View dialogView = createView(layoutId);
            dialogImpl = new DialogImpl(dialogView);
            if (dialogView != null) {
                dialogView.setTag(me);
            }
            show(dialogView);
        } else {
            show(getDialogView());
        }
        if (baseView() != null) {
            viewTreeObserver = baseView().getViewTreeObserver();
            viewTreeObserver.addOnDrawListener(baseViewDrawListener = new ViewTreeObserver.OnDrawListener() {
                @Override
                public void onDraw() {
                    int[] baseViewLocCache = new int[2];
                    if (baseView() != null) {
                        baseView().getLocationInWindow(baseViewLocCache);
                        if (getDialogImpl() != null && !baseViewLoc.isSameLoc(baseViewLocCache) && baseView().getVisibility() == VISIBLE) {
                            baseViewLoc.set(baseViewLocCache);
                            refreshMenuLoc();
                        }
                    } else {
                        if (viewTreeObserver != null) {
                            removeDrawListener(viewTreeObserver, this);
                            viewTreeObserver = null;
                            baseViewDrawListener = null;
                        }
                    }
                }
            });
        }
        return this;
    }

    public PopMenu show(Activity activity) {
        if (isHide && getDialogView() != null && isShow) {
            if (hideWithExitAnim && getDialogImpl() != null) {
                getDialogImpl().boxBody.clearAnimation();
                getDialogView().setVisibility(View.VISIBLE);
                getDialogImpl().boxRoot.animate().alpha(1f);
                getDialogImpl().getDialogXAnimImpl().doShowAnim(me, getDialogImpl().boxBody);
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
            layoutId = getCustomDialogLayoutResId(isLightTheme()) != 0 ? getCustomDialogLayoutResId(isLightTheme()) : layoutId;

            View dialogView = createView(layoutId);
            dialogImpl = new DialogImpl(dialogView);
            if (dialogView != null) {
                dialogView.setTag(me);
            }
            show(activity, dialogView);
        } else {
            show(activity, getDialogView());
        }
        if (baseView() != null) {
            viewTreeObserver = baseView().getViewTreeObserver();
            viewTreeObserver.addOnDrawListener(baseViewDrawListener = new ViewTreeObserver.OnDrawListener() {
                @Override
                public void onDraw() {
                    int[] baseViewLocCache = new int[2];
                    if (baseView() != null) {
                        baseView().getLocationInWindow(baseViewLocCache);
                        if (getDialogImpl() != null && !baseViewLoc.isSameLoc(baseViewLocCache) && baseView().getVisibility() == VISIBLE) {
                            baseViewLoc.set(baseViewLocCache);
                            refreshMenuLoc();
                        }
                    } else {
                        if (viewTreeObserver != null) {
                            removeDrawListener(viewTreeObserver, this);
                            viewTreeObserver = null;
                            baseViewDrawListener = null;
                        }
                    }
                }
            });
        }
        return this;
    }

    private void refreshMenuLoc() {
        if (getDialogImpl() == null || getDialogImpl().boxRoot == null || baseView() == null) {
            return;
        }
        getDialogImpl().boxBody.setTag(null);
        DialogXViewLoc loc = getMenuLoc();
        getDialogImpl().boxBody.setTag(loc);
        if (!isEnterAnimRunning) {
            if (loc.getX() != getDialogImpl().boxBody.getX()) {
                getDialogImpl().boxBody.setX(loc.getX());
            }
            if (loc.getY() != getDialogImpl().boxBody.getY()) {
                getDialogImpl().boxBody.setY(loc.getY());
            }
        }
        if (getDialogImpl().boxBody.getWidth() != loc.getW()) {
            RelativeLayout.LayoutParams rLp = new RelativeLayout.LayoutParams((int) loc.getW(), ViewGroup.LayoutParams.WRAP_CONTENT);
            getDialogImpl().boxBody.setLayoutParams(rLp);
        }
    }

    protected DialogXViewLoc getMenuLoc() {
        if (getDialogImpl().boxBody.getTag() instanceof DialogXViewLoc) {
            return (DialogXViewLoc) getDialogImpl().boxBody.getTag();
        }
        DialogXViewLoc result = new DialogXViewLoc();

        MaxRelativeLayout boxBody = getDialogImpl().boxBody;
        DialogXBaseRelativeLayout boxRoot = getDialogImpl().boxRoot;
        // 菜单位置计算逻辑
        int baseViewLeft = (int) baseViewLoc.getX();
        int baseViewTop = (int) baseViewLoc.getY();
        int calX = 0, calY = 0;
        if (alignGravity != -1) {
            if (isAlignGravity(Gravity.CENTER_VERTICAL)) {
                calY = (Math.max(0, baseViewTop + baseView().getMeasuredHeight() / 2 - boxBody.getHeight() / 2));
            }
            if (isAlignGravity(Gravity.CENTER_HORIZONTAL)) {
                calX = (Math.max(0, baseViewLeft + (getWidth() > 0 ? baseView().getMeasuredWidth() / 2 - getWidth() / 2 : 0)));
            }
            if (isAlignGravity(Gravity.CENTER)) {
                calX = (Math.max(0, baseViewLeft + (getWidth() > 0 ? (baseView().getMeasuredWidth() / 2 - getWidth() / 2) : 0)));
                calY = (Math.max(0, baseViewTop + baseView().getMeasuredHeight() / 2 - boxBody.getHeight() / 2));
            }
            if (overlayBaseView) {
                // 菜单覆盖在 baseView 上时
                if (isAlignGravity(Gravity.TOP)) {
                    calY = (baseViewTop + baseView().getMeasuredHeight() - boxBody.getHeight());
                    if (calX == 0) {
                        calX = (Math.max(0, baseViewLeft + (getWidth() > 0 ? baseView().getMeasuredWidth() / 2 - getWidth() / 2 : 0)));
                    }
                }
                if (isAlignGravity(Gravity.LEFT)) {
                    calX = Math.max(0, (baseViewLeft + baseView().getMeasuredWidth() - boxBody.getWidth()));
                    if (calY == 0) {
                        calY = (Math.max(0, baseViewTop + baseView().getMeasuredHeight() / 2 - boxBody.getHeight() / 2));
                    }
                }
                if (isAlignGravity(Gravity.RIGHT)) {
                    calX = baseViewLeft;
                    if (calY == 0) {
                        calY = (Math.max(0, baseViewTop + baseView().getMeasuredHeight() / 2 - boxBody.getHeight() / 2));
                    }
                }
                if (isAlignGravity(Gravity.BOTTOM)) {
                    calY = baseViewTop;
                    if (calX == 0) {
                        calX = (Math.max(0, baseViewLeft + (getWidth() > 0 ? baseView().getMeasuredWidth() / 2 - getWidth() / 2 : 0)));
                    }
                }
            } else {
                if (isAlignGravity(Gravity.TOP)) {
                    calY = (Math.max(0, baseViewTop - boxBody.getHeight()));
                    if (calX == 0) {
                        calX = (Math.max(0, baseViewLeft + (getWidth() > 0 ? baseView().getMeasuredWidth() / 2 - getWidth() / 2 : 0)));
                    }
                }
                if (isAlignGravity(Gravity.LEFT)) {
                    calX = Math.max(0, (baseViewLeft - boxBody.getWidth()));
                    if (calY == 0) {
                        calY = (Math.max(0, baseViewTop + baseView().getMeasuredHeight() / 2 - boxBody.getHeight() / 2));
                    }
                }
                if (isAlignGravity(Gravity.RIGHT)) {
                    calX = (Math.max(0, baseViewLeft + baseView().getWidth()));
                    if (calY == 0) {
                        calY = (Math.max(0, baseViewTop + baseView().getMeasuredHeight() / 2 - boxBody.getHeight() / 2));
                    }
                }
                if (isAlignGravity(Gravity.BOTTOM)) {
                    calY = (Math.max(0, baseViewTop + baseView().getHeight()));
                    if (calX == 0) {
                        calX = (Math.max(0, baseViewLeft + (getWidth() > 0 ? baseView().getMeasuredWidth() / 2 - getWidth() / 2 : 0)));
                    }
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
            result.setX(calX).setY(calY);
        } else {
            int mHeight = PopMenu.this.height == -1 ? baseView().getHeight() : PopMenu.this.height;
            int left = (int) baseViewLoc.getX();
            int top = (int) (baseViewLoc.getY() + (overlayBaseView ? 0 : mHeight) + selectItemYDeviation);

            if (!offScreen) {
                if (left < 0) {
                    left = 0;
                }
                if ((left + boxBody.getWidth()) > boxRoot.getUseAreaWidth()) {
                    left = boxRoot.getUseAreaWidth() - boxBody.getWidth();
                }
                if (top < 0) {
                    top = 0;
                }
                if ((top + boxBody.getHeight()) > boxRoot.getUseAreaHeight()) {
                    top = boxRoot.getUseAreaHeight() - boxBody.getHeight();
                }
            }
            result.setX(left).setY(top);
        }

        int mWidth = PopMenu.this.width == -1 ? baseView().getWidth() : PopMenu.this.width;
        int mHeight = PopMenu.this.height == -1 ? baseView().getHeight() : PopMenu.this.height;
        result.setW(mWidth).setH(mHeight);
        return result;
    }

    protected PopMenuArrayAdapter menuListAdapter;
    protected int selectItemYDeviation; // 如果找到了选中菜单，这里记录的是其位置的 Y 偏差值
    protected boolean isEnterAnimRunning;

    public class DialogImpl implements DialogConvertViewInterface {

        private List<View> blurViews;

        public DialogXBaseRelativeLayout boxRoot;
        public MaxRelativeLayout boxBody;
        public RelativeLayout boxCustom;
        public PopMenuListView listMenu;

        public DialogImpl(View convertView) {
            if (convertView == null) return;
            setDialogView(convertView);
            boxRoot = convertView.findViewById(R.id.box_root);
            boxBody = convertView.findViewById(R.id.box_body);
            boxCustom = convertView.findViewById(R.id.box_custom);
            listMenu = convertView.findViewById(R.id.listMenu);
            boxBody.setVisibility(View.INVISIBLE);

            blurViews = findAllBlurView(convertView);

            // 先设置为 -1 表示未初始化位置
            boxBody.setX(-1);
            boxBody.setY(-1);
            init();
        }

        @Override
        public void init() {
            closing = false;
            if (menuListAdapter == null) {
                menuListAdapter = new PopMenuArrayAdapter(me, getOwnActivity(), menuList);
            }

            boxRoot.setParentDialog(me);
            boxRoot.setOnLifecycleCallBack(new DialogXBaseRelativeLayout.OnLifecycleCallBack() {
                @Override
                public void onShow() {
                    isShow = true;
                    preShow = false;
                    setLifecycleState(Lifecycle.State.CREATED);
                    onDialogShow();
                    getDialogLifecycleCallback().onShow(me);
                    PopMenu.this.onShow(me);
                    refreshUI();
                }

                @Override
                public void onDismiss() {
                    isShow = false;
                    getDialogLifecycleCallback().onDismiss(me);
                    PopMenu.this.onDismiss(me);
                    setLifecycleState(Lifecycle.State.DESTROYED);
                    menuListAdapter = null;
                    dialogImpl = null;
                    baseView(null);
                    dialogLifecycleCallback = null;
                    BaseDialog.gc();
                }
            });

            boxRoot.setOnBackPressedListener(new DialogXBaseRelativeLayout.PrivateBackPressedListener() {
                @Override
                public boolean onBackPressed() {
                    if (onBackPressedListener != null) {
                        if (onBackPressedListener.onBackPressed(me)) {
                            dismiss();
                        }
                    } else {
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
                    getDialogXAnimImpl().doShowAnim(me, boxBody);
                    setLifecycleState(Lifecycle.State.RESUMED);

                    Integer blurFrontColor = null;
                    Float dialogXRadius = null;
                    if (style.popMenuSettings() != null && style.popMenuSettings().blurBackgroundSettings() != null) {
                        blurFrontColor = getColorNullable(getIntStyleAttr(style.popMenuSettings().blurBackgroundSettings().blurForwardColorRes(isLightTheme())));
                        dialogXRadius = getFloatStyleAttr((float) style.popMenuSettings().blurBackgroundSettings().blurBackgroundRoundRadiusPx());
                    }

                    if (blurViews != null) {
                        for (View blurView : blurViews) {
                            ((BlurViewType) blurView).setOverlayColor(backgroundColor == null ? blurFrontColor : backgroundColor);
                            ((BlurViewType) blurView).setRadiusPx(dialogXRadius);
                        }
                    }
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
                    if (!isMenuItemEnable(position)) {
                        return;
                    }
                    haptic(view);
                    selectIndex = position;
                    if (!closing) {
                        lastHash = menuList.hashCode();
                        boolean callBack = getOnMenuItemClickListener().onClick(me, menuList.get(position), position);
                        if (!notCheckHash) {
                            if (lastHash == menuList.hashCode()) {
                                if (callBack) {
                                    callBack = false;
                                }
                            }
                        }
                        if (!callBack) {
                            dismiss();
                        }
                    }
                }
            });
            onDialogInit();
        }

        @Override
        public void refreshView() {
            if (boxRoot == null || getOwnActivity() == null) {
                return;
            }
            boxRoot.setAutoUnsafePlacePadding(isEnableImmersiveMode());
            boxRoot.setRootPadding(screenPaddings[0], screenPaddings[1], screenPaddings[2], screenPaddings[3]);
            if (listMenu.getAdapter() == null) {
                listMenu.setAdapter(menuListAdapter);
            } else {
                if (menuListAdapter.getMenuList() != menuList) {
                    menuListAdapter = new PopMenuArrayAdapter(me, getOwnActivity(), menuList);
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
            if (backgroundColor != null) {
                tintColor(boxBody, backgroundColor);
            }

            if (backgroundRadius > -1) {
                if (boxBody.getBackground() instanceof GradientDrawable) {
                    GradientDrawable gradientDrawable = (GradientDrawable) boxBody.getBackground();
                    if (gradientDrawable != null)
                        gradientDrawable.setCornerRadius(backgroundRadius);
                }
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    boxBody.setOutlineProvider(new ViewOutlineProvider() {
                        @Override
                        public void getOutline(View view, Outline outline) {
                            outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), backgroundRadius);
                        }
                    });
                    boxBody.setClipToOutline(true);
                }

                if (blurViews != null) {
                    for (View blurView : blurViews) {
                        ((BlurViewType) blurView).setRadiusPx(backgroundRadius);
                    }
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

            if (backgroundColor != null) {
                tintColor(boxBody, backgroundColor);

                if (blurViews != null) {
                    for (View blurView : blurViews) {
                        ((BlurViewType) blurView).setOverlayColor(backgroundColor);
                    }
                }
            }

            if (itemDivider != null) {
                listMenu.setDivider(itemDivider.createDividerDrawable(getOwnActivity(), isLightTheme()));
                listMenu.setDividerHeight(itemDivider.getWidth());
            }

            onDialogRefreshUI();
        }

        @Override
        public void doDismiss(View v) {
            if (preDismiss(PopMenu.this)) {
                return;
            }
            if (v != null) {
                v.setEnabled(false);
            }

            if (!dismissAnimFlag && boxRoot != null) {
                dismissAnimFlag = true;
                boxRoot.post(new Runnable() {
                    @Override
                    public void run() {
                        getDialogXAnimImpl().doExitAnim(me, boxBody);

                        runOnMainDelay(new Runnable() {
                            @Override
                            public void run() {
                                if (baseViewDrawListener != null) {
                                    if (viewTreeObserver != null) {
                                        removeDrawListener(viewTreeObserver, baseViewDrawListener);
                                    } else {
                                        if (baseView() != null) {
                                            removeDrawListener(baseView().getViewTreeObserver(), baseViewDrawListener);
                                        }
                                    }
                                    baseViewDrawListener = null;
                                    viewTreeObserver = null;
                                }
                                dismiss(getDialogView());
                            }
                        }, getExitAnimationDuration(null));
                    }
                });
            }
        }

        protected DialogXAnimInterface<PopMenu> getDialogXAnimImpl() {
            if (dialogXAnimImpl == null) {
                dialogXAnimImpl = new DialogXAnimInterface<PopMenu>() {

                    int selectMenuIndex = -1;

                    @Override
                    public void doShowAnim(PopMenu dialog, ViewGroup dialogBodyView) {
                        long enterAnimDurationTemp = getEnterAnimationDuration(null);

                        if (baseView() != null) {
                            // 有绑定按钮的情况下
                            int targetHeight = getBodyRealHeight();
                            boxBody.getLayoutParams().height = 1;

                            if (overlayBaseView && !listMenu.isCanScroll()) {
                                if (baseView() instanceof TextView) {
                                    String baseText = ((TextView) baseView()).getText().toString();
                                    for (CharSequence c : menuList) {
                                        if (TextUtils.equals(c.toString(), baseText)) {
                                            selectMenuIndex = menuList.indexOf(c);
                                            break;
                                        }
                                    }
                                }
                                // 找到已选中的项目
                                if (selectMenuIndex != -1) {
                                    int[] viewLoc = new int[2];
                                    if (listMenu.getChildAt(selectMenuIndex) != null) {
                                        int itemHeight = listMenu.getChildAt(selectMenuIndex).getMeasuredHeight();
                                        listMenu.getChildAt(selectMenuIndex).getLocationInWindow(viewLoc);
                                        selectItemYDeviation = (int) ((baseView().getMeasuredHeight() / 2f) - (viewLoc[1] - boxBody.getY()) - (itemHeight / 2f));
                                    }
                                }
                            }

                            refreshMenuLoc();
                            selectItemYDeviation = (int) (getMenuLoc().getY() - baseViewLoc.getY());

                            // 展开动画
                            ValueAnimator enterAnim = ValueAnimator.ofFloat(0f, 1f);
                            enterAnim.setInterpolator(new DecelerateInterpolator());
                            enterAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator animation) {
                                    if (!isShow || getDialogImpl() == null || getDialogImpl().boxBody == null)
                                        return;
                                    float animatedValue = (float) animation.getAnimatedValue();
                                    isEnterAnimRunning = !(animatedValue == 1f);
                                    DialogXViewLoc loc = getMenuLoc();

                                    int aimHeight = animatedValue == 1f ? ViewGroup.LayoutParams.WRAP_CONTENT : (int) (targetHeight * animatedValue);
                                    boxBody.getLayoutParams().height = aimHeight;
                                    boxBody.getLayoutParams().width = getWidth() == -1 ? baseView().getWidth() : getWidth();
                                    if ((boxBody.getY() + aimHeight) > boxRoot.getSafeHeight()) {
                                        boxBody.setY(boxRoot.getSafeHeight() - aimHeight);
                                    }
                                    float calX = loc.getX() != -1 ? loc.getX() : baseViewLoc.getX();
                                    float calY = baseViewLoc.getY() + selectItemYDeviation * animatedValue;

                                    if (!offScreen) {
                                        if (calX < 0) {
                                            calX = 0;
                                        }
                                        if (calY < 0) {
                                            calY = 0;
                                        }
                                        if ((calX + boxBody.getWidth()) > boxRoot.getUseAreaWidth()) {
                                            calX = boxRoot.getUseAreaWidth() - boxBody.getWidth();
                                        }
                                        if ((calY + boxBody.getHeight()) > boxRoot.getUseAreaHeight()) {
                                            calY = boxRoot.getUseAreaHeight() - boxBody.getHeight();
                                        }
                                    }
                                    boxBody.setX(calX);
                                    boxBody.setY(calY);

                                    boxBody.requestLayout();
                                    if (boxBody.getVisibility() != VISIBLE) {
                                        boxBody.setVisibility(View.VISIBLE);
                                    }

                                    if (isUseBlurBackground()) {
                                        boxRoot.setBkgAlpha((Float) animation.getAnimatedValue());
                                    }
                                }
                            });
                            enterAnim.setInterpolator(new DecelerateInterpolator(2f));
                            enterAnim.setDuration(enterAnimDurationTemp);
                            enterAnim.start();
                        } else {
                            // 无绑定按钮的情况下
                            RelativeLayout.LayoutParams rLp = (RelativeLayout.LayoutParams) boxBody.getLayoutParams();
                            rLp.addRule(RelativeLayout.CENTER_IN_PARENT);
                            rLp.width = getWidth() == -1 ? RelativeLayout.LayoutParams.MATCH_PARENT : getWidth();
                            rLp.leftMargin = dip2px(50);
                            rLp.rightMargin = dip2px(50);
                            boxBody.setLayoutParams(rLp);
                            boxBody.setAlpha(0f);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !isUseBlurBackground()) {
                                boxBody.setElevation(dip2px(20));
                            }
                            boxBody.setVisibility(View.VISIBLE);
                            boxBody.animate().alpha(1f).setDuration(enterAnimDurationTemp);

                            ValueAnimator bkgAlpha = ValueAnimator.ofFloat(0f, 1f);
                            bkgAlpha.setDuration(enterAnimDurationTemp);
                            bkgAlpha.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator animation) {
                                    boxRoot.setBkgAlpha((Float) animation.getAnimatedValue());
                                }
                            });
                            bkgAlpha.start();
                        }
                    }

                    @Override
                    public void doExitAnim(PopMenu dialog, ViewGroup dialogBodyView) {
                        Animation exitAnim = AnimationUtils.loadAnimation(getOwnActivity() == null ? boxRoot.getContext() : getOwnActivity(), R.anim.anim_dialogx_default_exit);
                        long exitAnimDuration = getExitAnimationDuration(exitAnim);
                        exitAnim.setDuration(exitAnimDuration);
                        boxBody.startAnimation(exitAnim);

                        boxRoot.animate().alpha(0f).setInterpolator(new AccelerateInterpolator()).setDuration(exitAnimDuration);

                        ValueAnimator bkgAlpha = ValueAnimator.ofFloat(1, 0f);
                        bkgAlpha.setDuration(exitAnimDuration);
                        bkgAlpha.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                if (boxRoot != null && baseView() == null) {
                                    boxRoot.setBkgAlpha((Float) animation.getAnimatedValue());
                                }
                            }
                        });
                        bkgAlpha.start();
                    }
                };
            }
            return dialogXAnimImpl;
        }

        private boolean isUseBlurBackground() {
            return style.popMenuSettings() != null && style.popMenuSettings().blurBackgroundSettings() != null && style.popMenuSettings().blurBackgroundSettings().blurBackground();
        }

        public long getExitAnimationDuration(@Nullable Animation defaultExitAnim) {
            if (defaultExitAnim == null && boxBody.getAnimation() != null) {
                defaultExitAnim = boxBody.getAnimation();
            }
            long exitAnimDurationTemp = (defaultExitAnim == null || defaultExitAnim.getDuration() == 0) ? 150 : defaultExitAnim.getDuration();
            if (overrideExitDuration >= 0) {
                exitAnimDurationTemp = overrideExitDuration;
            }
            if (exitAnimDuration != -1) {
                exitAnimDurationTemp = exitAnimDuration;
            }
            return exitAnimDurationTemp;
        }

        public long getEnterAnimationDuration(@Nullable Animation defaultEnterAnim) {
            if (defaultEnterAnim == null && boxBody.getAnimation() != null) {
                defaultEnterAnim = boxBody.getAnimation();
            }
            long enterAnimDurationTemp = (defaultEnterAnim == null || defaultEnterAnim.getDuration() == 0) ? 150 : defaultEnterAnim.getDuration();
            if (overrideEnterDuration >= 0) {
                enterAnimDurationTemp = overrideEnterDuration;
            }
            if (enterAnimDuration >= 0) {
                enterAnimDurationTemp = enterAnimDuration;
            }
            return enterAnimDurationTemp;
        }
    }

    private void removeDrawListener(ViewTreeObserver viewTreeObserver, ViewTreeObserver.OnDrawListener listener) {
        if (viewTreeObserver == null || listener == null || !viewTreeObserver.isAlive()) {
            return;
        }
        try {
            viewTreeObserver.removeOnDrawListener(listener);
        } catch (Exception e) {
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
        if (getDialogView() != null) {
            if (baseViewDrawListener != null) {
                if (viewTreeObserver != null) {
                    removeDrawListener(viewTreeObserver, baseViewDrawListener);
                } else {
                    if (baseView() != null) {
                        removeDrawListener(baseView().getViewTreeObserver(), baseViewDrawListener);
                    }
                }
                baseViewDrawListener = null;
            }
            dismiss(getDialogView());
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

    public PopMenu setMenus(String... menuList) {
        this.menuList = new ArrayList<>();
        this.menuList.addAll(Arrays.asList(menuList));
        refreshUI();
        return this;
    }

    public PopMenu setMenus(CharSequence... menuList) {
        this.menuList = new ArrayList<>();
        this.menuList.addAll(Arrays.asList(menuList));
        refreshUI();
        return this;
    }

    public PopMenu setMenus(int... menuListResId) {
        this.menuList = Arrays.asList(getTextArray(menuListResId));
        this.menuListAdapter = null;
        refreshUI();
        return this;
    }

    private String[] getTextArray(int[] menuListResId) {
        String[] result = new String[menuListResId == null ? 0 : menuListResId.length];
        for (int i = 0; i < (menuListResId == null ? 0 : menuListResId.length); i++) {
            result[i] = getString(menuListResId[i]);
        }
        return result;
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
        refreshMenuLoc();
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
        refreshUI();
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

    public PopMenu setHapticFeedbackEnabled(boolean isHapticFeedbackEnabled) {
        this.isHapticFeedbackEnabled = isHapticFeedbackEnabled ? 1 : 0;
        return this;
    }

    public float getRadius() {
        return backgroundRadius;
    }

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
            getDialogImpl().getDialogXAnimImpl().doExitAnim(me, getDialogImpl().boxBody);
            runOnMainDelay(new Runnable() {
                @Override
                public void run() {
                    if (getDialogView() != null) {
                        getDialogView().setVisibility(View.GONE);
                    }
                }
            }, getDialogImpl().getExitAnimationDuration(null));
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
        return baseView();
    }

    public PopMenu setBaseView(View baseView) {
        baseView(baseView);
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

    // 设置已选择的菜单项（菜单背景会有选中状态的显示）
    public PopMenu setPressedIndex(int pressedIndex) {
        this.pressedIndex = pressedIndex;
        refreshUI();
        return this;
    }

    public int getSelectIndex() {
        return selectIndex;
    }

    public CharSequence getSelectMenuText() {
        if (menuList == null) return "";
        return menuList.get(selectIndex);
    }

    /**
     * 用于使用 new 构建实例时，override 的生命周期事件
     * 例如：
     * new PopMenu() {
     *
     * @param dialog self
     * @Override public void onShow(PopMenu dialog) {
     * //...
     * }
     * }
     */
    protected void onShow(PopMenu dialog) {

    }

    /**
     * 用于使用 new 构建实例时，override 的生命周期事件
     * 例如：
     * new PopMenu() {
     *
     * @param dialog self
     * @Override public boolean onDismiss(PopMenu dialog) {
     * WaitDialog.show("Please Wait...");
     * if (dialog.getButtonSelectResult() == BUTTON_SELECT_RESULT.BUTTON_OK) {
     * //点击了OK的情况
     * //...
     * } else {
     * //其他按钮点击、对话框dismiss的情况
     * //...
     * }
     * return false;
     * }
     * }
     */
    // 用于使用 new 构建实例时，override 的生命周期事件
    protected void onDismiss(PopMenu dialog) {

    }

    public MenuItemLayoutRefreshCallback<PopMenu> getMenuMenuItemLayoutRefreshCallback() {
        return menuMenuItemLayoutRefreshCallback;
    }

    public PopMenu setMenuMenuItemLayoutRefreshCallback(MenuItemLayoutRefreshCallback<PopMenu> menuMenuItemLayoutRefreshCallback) {
        this.menuMenuItemLayoutRefreshCallback = menuMenuItemLayoutRefreshCallback;
        return this;
    }

    protected PopMenu baseView(View view) {
        if (view == null && baseViewWeakReference != null) {
            baseViewWeakReference.clear();
            baseViewWeakReference = null;
        } else {
            baseViewWeakReference = new WeakReference<>(view);
        }
        return this;
    }

    protected View baseView() {
        return baseViewWeakReference == null ? null : baseViewWeakReference.get();
    }

    public PopMenu setData(String key, Object obj) {
        if (data == null) data = new HashMap<>();
        data.put(key, obj);
        return this;
    }

    public PopMenu onShow(DialogXRunnable<PopMenu> dialogXRunnable) {
        onShowRunnable = dialogXRunnable;
        if (isShow() && onShowRunnable != null) {
            onShowRunnable.run(this);
        }
        return this;
    }

    public PopMenu onDismiss(DialogXRunnable<PopMenu> dialogXRunnable) {
        onDismissRunnable = dialogXRunnable;
        return this;
    }

    public PopMenu setEnableImmersiveMode(boolean enableImmersiveMode) {
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

    public PopMenu setIconResIds(int... resIds) {
        if (iconResIds == null) {
            iconResIds = new ArrayList<>();
        }
        for (int id : resIds) {
            iconResIds.add(id);
        }
        refreshUI();
        return this;
    }

    public PopMenu setIconResIds(List<Integer> iconResIds) {
        this.iconResIds = iconResIds;
        refreshUI();
        return this;
    }

    public boolean isAutoTintIconInLightOrDarkMode() {
        return autoTintIconInLightOrDarkMode;
    }

    public PopMenu setAutoTintIconInLightOrDarkMode(boolean autoTintIconInLightOrDarkMode) {
        this.autoTintIconInLightOrDarkMode = autoTintIconInLightOrDarkMode;
        return this;
    }

    public PopMenu setThisOrderIndex(int orderIndex) {
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

    public PopMenu bringToFront() {
        setThisOrderIndex(getHighestOrderIndex());
        return this;
    }

    public PopMenu enableMenu(int... menuIndex) {
        for (int i : menuIndex) {
            menuUsability.put(i, true);
        }
        return this;
    }

    public PopMenu enableMenu(CharSequence... menuText) {
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

    public PopMenu enableMenu(String... menuText) {
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

    public PopMenu enableAllMenu() {
        menuUsability.clear();
        return this;
    }

    public PopMenu disableMenu(int... menuIndex) {
        for (int i : menuIndex) {
            menuUsability.put(i, false);
        }
        return this;
    }

    public PopMenu disableMenu(CharSequence... menuText) {
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

    public PopMenu disableMenu(String... menuText) {
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

    public PopMenu disableAllMenu() {
        if (menuList != null && !menuList.isEmpty()) {
            for (int i = 0; i < menuList.size(); i++) {
                menuUsability.put(i, false);
            }
        } else {
            error("DialogX: " + dialogKey() + " .disableAllMenu()执行失败，请先初始化菜单项 menuList");
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

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public PopMenu setBackgroundColor(@ColorInt int backgroundColor) {
        this.backgroundColor = backgroundColor;
        refreshUI();
        return this;
    }

    public PopMenu setBackgroundColorRes(@ColorRes int backgroundColorResId) {
        this.backgroundColor = getColor(backgroundColorResId);
        refreshUI();
        return this;
    }

    public PopMenu setActionRunnable(int actionId, DialogXRunnable<PopMenu> runnable) {
        dialogActionRunnableMap.put(actionId, runnable);
        return this;
    }

    public PopMenu cleanAction(int actionId) {
        dialogActionRunnableMap.remove(actionId);
        return this;
    }

    public PopMenu cleanAllAction() {
        dialogActionRunnableMap.clear();
        return this;
    }

    // for BaseDialog use
    public void callDialogDismiss() {
        dismiss();
    }

    public PopMenu bindDismissWithLifecycleOwner(LifecycleOwner owner) {
        super.bindDismissWithLifecycleOwnerPrivate(owner);
        return this;
    }

    public ItemDivider getItemDivider() {
        return itemDivider;
    }

    public PopMenu setItemDivider(ItemDivider itemDivider) {
        this.itemDivider = itemDivider;
        refreshUI();
        return this;
    }

    public PopMenu setCustomDialogLayoutResId(int customDialogLayoutId) {
        this.customDialogLayoutResId[0] = customDialogLayoutId;
        this.customDialogLayoutResId[1] = customDialogLayoutId;
        return this;
    }

    public PopMenu setCustomDialogLayoutResId(int customDialogLayoutId, boolean isLightTheme) {
        this.customDialogLayoutResId[isLightTheme ? 0 : 1] = customDialogLayoutId;
        return this;
    }
}
