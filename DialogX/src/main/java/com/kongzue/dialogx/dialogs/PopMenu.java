package com.kongzue.dialogx.dialogs;

import static android.view.View.OVER_SCROLL_NEVER;

import android.animation.ValueAnimator;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kongzue.dialogx.R;
import com.kongzue.dialogx.interfaces.BaseDialog;
import com.kongzue.dialogx.interfaces.DialogConvertViewInterface;
import com.kongzue.dialogx.interfaces.DialogLifecycleCallback;
import com.kongzue.dialogx.interfaces.OnBackPressedListener;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.kongzue.dialogx.interfaces.OnMenuItemClickListener;
import com.kongzue.dialogx.util.PopMenuArrayAdapter;
import com.kongzue.dialogx.util.views.DialogXBaseRelativeLayout;
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
    protected OnBindView<PopMenu> onBindView;
    protected DialogLifecycleCallback<PopMenu> dialogLifecycleCallback;
    private View dialogView;
    private List<CharSequence> menuList;
    protected DialogImpl dialogImpl;
    protected View baseView;
    protected boolean overlayBaseView = true;
    protected OnMenuItemClickListener<PopMenu> onMenuItemClickListener;
    
    public PopMenu() {
        super();
    }
    
    public static PopMenu build() {
        return new PopMenu();
    }
    
    public static PopMenu show(CharSequence[] menus) {
        PopMenu popMenu = new PopMenu();
        popMenu.setMenuList(menus);
        popMenu.show();
        return popMenu;
    }
    
    int[] baseViewLoc = new int[2];
    
    public static PopMenu show(View baseView, CharSequence[] menus) {
        PopMenu popMenu = new PopMenu();
        popMenu.setMenuList(menus);
        popMenu.show();
        popMenu.baseView = baseView;
        baseView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (popMenu.getDialogImpl() != null) {
                    popMenu.baseViewLoc = new int[2];
                    baseView.getLocationOnScreen(popMenu.baseViewLoc);
                    
                    int width = baseView.getWidth();
                    int height = baseView.getHeight();
                    
                    popMenu.getDialogImpl().boxBody.setX(popMenu.baseViewLoc[0]);
                    popMenu.getDialogImpl().boxBody.setY(popMenu.baseViewLoc[1] + (popMenu.overlayBaseView ? 0 : height));
                    
                    if (width != 0 && popMenu.getDialogImpl().boxBody.getWidth() != width) {
                        RelativeLayout.LayoutParams rLp = new RelativeLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
                        popMenu.getDialogImpl().boxBody.setLayoutParams(rLp);
                    }
                    
                    baseView.getViewTreeObserver().removeOnPreDrawListener(this);
                }
                return true;
            }
        });
        return popMenu;
    }
    
    public void show() {
        super.beforeShow();
        if (getDialogView() == null) {
            int layoutId = R.layout.layout_dialogx_popmenu_material;

//            if (style.overrideBottomDialogRes() != null) {
//                layoutId = style.overrideBottomDialogRes().overrideDialogLayout(isLightTheme());
//            }
            
            dialogView = createView(layoutId);
            dialogImpl = new DialogImpl(dialogView);
            if (dialogView != null) dialogView.setTag(me);
        }
        show(dialogView);
    }
    
    protected PopMenuArrayAdapter menuListAdapter;
    
    public class DialogImpl implements DialogConvertViewInterface {
        
        private DialogXBaseRelativeLayout boxRoot;
        private LinearLayout boxBody;
        private RelativeLayout boxCustom;
        private PopMenuListView listMenu;
        
        public DialogImpl(View convertView) {
            boxRoot = convertView.findViewById(R.id.box_root);
            boxBody = convertView.findViewById(R.id.box_body);
            boxCustom = convertView.findViewById(R.id.box_custom);
            listMenu = convertView.findViewById(R.id.listMenu);
            init();
        }
        
        @Override
        public void init() {
            if (menuListAdapter == null) {
                menuListAdapter = new PopMenuArrayAdapter(getContext(), menuList);
            }
            
            boxRoot.setParentDialog(me);
            boxRoot.setOnLifecycleCallBack(new DialogXBaseRelativeLayout.OnLifecycleCallBack() {
                @Override
                public void onShow() {
                    isShow = true;
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
                    System.gc();
                }
            });
            
            boxRoot.setOnBackPressedListener(new OnBackPressedListener() {
                @Override
                public boolean onBackPressed() {
                    if (onBackPressedListener != null && onBackPressedListener.onBackPressed()) {
                        dismiss();
                        return false;
                    }
                    if (isCancelable()) {
                        dismiss();
                    }
                    return false;
                }
            });
            listMenu.setMaxHeight(getRootFrameLayout() == null ? dip2px(500) : getRootFrameLayout().getMeasuredHeight() - dip2px(150));
            
            boxBody.setVisibility(View.INVISIBLE);
            boxBody.post(new Runnable() {
                
                int selectMenuIndex = -1;
                
                @Override
                public void run() {
                    long enterAnimDurationTemp = enterAnimDuration != -1 ? enterAnimDuration : (overrideEnterDuration == -1 ? 150 : overrideEnterDuration);
                    
                    if (baseView != null) {
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
                            if (selectMenuIndex != -1) {
                                int[] viewLoc = new int[2];
                                if (listMenu.getChildAt(selectMenuIndex) != null) {
                                    int itemHeight = listMenu.getChildAt(selectMenuIndex).getMeasuredHeight();
                                    listMenu.getChildAt(selectMenuIndex).getLocationOnScreen(viewLoc);
                                    boxBody.setY(baseViewLoc[1] + (baseView.getMeasuredHeight() / 2f) - (viewLoc[1] - boxBody.getY()) - (itemHeight / 2f));
                                }
                            }
                        }
                        
                        Animation enterAnim = new Animation() {
                            @Override
                            protected void applyTransformation(float interpolatedTime, Transformation t) {
                                int aimHeight = interpolatedTime == 1 ? ViewGroup.LayoutParams.WRAP_CONTENT : (int) (targetHeight * interpolatedTime);
                                boxBody.getLayoutParams().height = aimHeight;
                                if ((boxBody.getY() + aimHeight) > boxRoot.getSafeHeight()) {
                                    boxBody.setY(boxRoot.getSafeHeight() - aimHeight);
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
                    } else {
                        RelativeLayout.LayoutParams rLp = (RelativeLayout.LayoutParams) boxBody.getLayoutParams();
                        rLp.addRule(RelativeLayout.CENTER_IN_PARENT);
                        rLp.width = RelativeLayout.LayoutParams.MATCH_PARENT;
                        rLp.leftMargin = dip2px(50);
                        rLp.rightMargin = dip2px(50);
                        boxBody.setAlpha(0f);
                        boxBody.setElevation(dip2px(20));
                        boxBody.setVisibility(View.VISIBLE);
                        boxBody.animate().alpha(1f).setDuration(enterAnimDurationTemp);
                        
                        ValueAnimator bkgAlpha = ValueAnimator.ofFloat(0f, 1f);
                        bkgAlpha.setDuration(enterAnimDurationTemp);
                        bkgAlpha.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                float value = (float) animation.getAnimatedValue();
                                boxRoot.setBkgAlpha(value);
                            }
                        });
                        bkgAlpha.start();
                    }
                }
            });
            
            int dividerDrawableResId = 0;
            int dividerHeight = 1;
            if (style.overrideBottomDialogRes() != null) {
                dividerDrawableResId = style.overrideBottomDialogRes().overrideMenuDividerDrawableRes(isLightTheme());
                dividerHeight = style.overrideBottomDialogRes().overrideMenuDividerHeight(isLightTheme());
            }
            if (dividerDrawableResId == 0) {
                dividerDrawableResId = isLightTheme() ? R.drawable.rect_dialogx_material_menu_split_divider : R.drawable.rect_dialogx_material_menu_split_divider_night;
            }
            
            listMenu.setOverScrollMode(OVER_SCROLL_NEVER);
            listMenu.setVerticalScrollBarEnabled(false);
            listMenu.setDivider(getResources().getDrawable(dividerDrawableResId));
            listMenu.setDividerHeight(0);
            
            listMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (!getOnMenuItemClickListener().onClick(me, menuList.get(position), position)) {
                        dismiss();
                    }
                }
            });
        }
        
        @Override
        public void refreshView() {
            if (listMenu.getAdapter() == null) {
                listMenu.setAdapter(menuListAdapter);
            } else {
                menuListAdapter.notifyDataSetChanged();
            }
            if (isCancelable()) {
                boxRoot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        doDismiss(v);
                    }
                });
            } else {
                boxRoot.setOnClickListener(null);
            }
        }
        
        @Override
        public void doDismiss(View v) {
            boxRoot.post(new Runnable() {
                @Override
                public void run() {
                    if (v != null) v.setEnabled(false);
                    
                    Animation exitAnim = AnimationUtils.loadAnimation(getContext() == null ? boxRoot.getContext() : getContext(), R.anim.anim_dialogx_default_exit);
                    if (exitAnimDuration != -1) {
                        exitAnim.setDuration(exitAnimDuration);
                    }
                    boxBody.startAnimation(exitAnim);
                    
                    boxRoot.animate()
                            .alpha(0f)
                            .setInterpolator(new AccelerateInterpolator())
                            .setDuration(exitAnimDuration == -1 ? exitAnim.getDuration() : exitAnimDuration);
                    
                    if (baseView == null) {
                        ValueAnimator bkgAlpha = ValueAnimator.ofFloat(1, 0f);
                        bkgAlpha.setDuration(exitAnimDuration == -1 ? exitAnim.getDuration() : exitAnimDuration);
                        bkgAlpha.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                float value = (float) animation.getAnimatedValue();
                                boxRoot.setBkgAlpha(value);
                            }
                        });
                        bkgAlpha.start();
                    }
                    
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dismiss(dialogView);
                        }
                    }, exitAnimDuration == -1 ? exitAnim.getDuration() : exitAnimDuration);
                }
            });
        }
    }
    
    private int getBodyRealHeight() {
        if (getDialogImpl() == null) return 0;
        
        int matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) getDialogImpl().boxBody.getParent()).getWidth(), View.MeasureSpec.EXACTLY);
        int wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) getDialogImpl().boxBody.getParent()).getHeight(), View.MeasureSpec.AT_MOST);
        getDialogImpl().boxBody.measure(matchParentMeasureSpec, wrapContentMeasureSpec);
        
        return getDialogImpl().boxBody.getMeasuredHeight();
    }
    
    public void dismiss() {
        if (dialogImpl == null) return;
        dialogImpl.doDismiss(null);
    }
    
    @Override
    public void onUIModeChange(Configuration newConfig) {
    
    }
    
    @Override
    protected void shutdown() {
    
    }
    
    public View getDialogView() {
        if (dialogView == null) return null;
        return dialogView;
    }
    
    public List<CharSequence> getMenuList() {
        return menuList;
    }
    
    public PopMenu setMenuList(List<CharSequence> menuList) {
        this.menuList = menuList;
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
        if (getDialogImpl() == null) return;
        runOnMain(new Runnable() {
            @Override
            public void run() {
                if (dialogImpl != null) dialogImpl.refreshView();
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
        if (isShow) dialogLifecycleCallback.onShow(me);
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
}
