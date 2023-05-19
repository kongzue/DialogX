package com.kongzue.dialogx.util;

import static com.kongzue.dialogx.interfaces.BaseDialog.isNull;
import static com.kongzue.dialogx.interfaces.BaseDialog.useTextInfo;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Space;
import android.widget.TextView;

import com.kongzue.dialogx.R;
import com.kongzue.dialogx.dialogs.BottomMenu;

import java.util.List;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2020/10/7 0:00
 */
public class BottomMenuArrayAdapter extends BaseAdapter {
    
    private BottomMenu bottomMenu;
    public List<CharSequence> objects;
    public Context context;
    
    public BottomMenuArrayAdapter(BottomMenu bottomMenu, Context context, List<CharSequence> objects) {
        this.objects = objects;
        this.context = context;
        this.bottomMenu = bottomMenu;
    }
    
    class ViewHolder {
        ImageView imgDialogxMenuIcon;
        ImageView imgDialogxMenuSelection;
        TextView txtDialogxMenuText;
        Space spaceDialogxRightPadding;
    }
    
    @Override
    public int getCount() {
        return objects.size();
    }
    
    @Override
    public CharSequence getItem(int position) {
        return objects.get(position);
    }
    
    @Override
    public long getItemId(int position) {
        return position;
    }
    
    TextInfo defaultMenuTextInfo;
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater mInflater = LayoutInflater.from(context);
            
            int resourceId = R.layout.item_dialogx_material_bottom_menu_normal_text;
            if (bottomMenu.getStyle().overrideBottomDialogRes() != null) {
                resourceId = bottomMenu.getStyle().overrideBottomDialogRes().overrideMenuItemLayout(bottomMenu.isLightTheme(), position, getCount(), false);
                if (resourceId == 0) {
                    resourceId = R.layout.item_dialogx_material_bottom_menu_normal_text;
                } else {
                    if (!isNull(bottomMenu.getTitle()) || !isNull(bottomMenu.getMessage()) ||
                            bottomMenu.getCustomView() != null) {
                        if (position == 0) {
                            resourceId = bottomMenu.getStyle().overrideBottomDialogRes().overrideMenuItemLayout(bottomMenu.isLightTheme(), position, getCount(), true);
                        }
                    }
                }
            }
            convertView = mInflater.inflate(resourceId, null);
            
            viewHolder.imgDialogxMenuIcon = convertView.findViewById(R.id.img_dialogx_menu_icon);
            viewHolder.imgDialogxMenuSelection = convertView.findViewById(R.id.img_dialogx_menu_selection);
            viewHolder.txtDialogxMenuText = convertView.findViewById(R.id.txt_dialogx_menu_text);
            viewHolder.spaceDialogxRightPadding = convertView.findViewById(R.id.space_dialogx_right_padding);
            
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (bottomMenu.getSelectMode() == BottomMenu.SELECT_MODE.SINGLE) {
            if (viewHolder.imgDialogxMenuSelection != null) {
                if (bottomMenu.getSelection() == position) {
                    viewHolder.imgDialogxMenuSelection.setVisibility(View.VISIBLE);
                    int overrideSelectionImageResId = bottomMenu.getStyle().overrideBottomDialogRes().overrideSelectionImage(bottomMenu.isLightTheme(), true);
                    if (overrideSelectionImageResId != 0) {
                        viewHolder.imgDialogxMenuSelection.setImageResource(overrideSelectionImageResId);
                    }
                } else {
                    int overrideSelectionImageResId = bottomMenu.getStyle().overrideBottomDialogRes().overrideSelectionImage(bottomMenu.isLightTheme(), false);
                    if (overrideSelectionImageResId != 0) {
                        viewHolder.imgDialogxMenuSelection.setVisibility(View.VISIBLE);
                        viewHolder.imgDialogxMenuSelection.setImageResource(overrideSelectionImageResId);
                    } else {
                        viewHolder.imgDialogxMenuSelection.setVisibility(View.INVISIBLE);
                    }
                }
            }
        } else if (bottomMenu.getSelectMode() == BottomMenu.SELECT_MODE.MULTIPLE) {
            if (viewHolder.imgDialogxMenuSelection != null) {
                if (bottomMenu.getSelectionList().contains(position)) {
                    viewHolder.imgDialogxMenuSelection.setVisibility(View.VISIBLE);
                    int overrideSelectionImageResId = bottomMenu.getStyle().overrideBottomDialogRes().overrideMultiSelectionImage(bottomMenu.isLightTheme(), true);
                    if (overrideSelectionImageResId != 0) {
                        viewHolder.imgDialogxMenuSelection.setImageResource(overrideSelectionImageResId);
                    }
                } else {
                    int overrideSelectionImageResId = bottomMenu.getStyle().overrideBottomDialogRes().overrideMultiSelectionImage(bottomMenu.isLightTheme(), false);
                    if (overrideSelectionImageResId != 0) {
                        viewHolder.imgDialogxMenuSelection.setVisibility(View.VISIBLE);
                        viewHolder.imgDialogxMenuSelection.setImageResource(overrideSelectionImageResId);
                    } else {
                        viewHolder.imgDialogxMenuSelection.setVisibility(View.INVISIBLE);
                    }
                }
            }
        } else {
            viewHolder.imgDialogxMenuSelection.setVisibility(View.GONE);
        }
        int overrideSelectionBackgroundColorRes = 0;
        if (bottomMenu.getStyle().overrideBottomDialogRes() != null) {
            overrideSelectionBackgroundColorRes = bottomMenu.getStyle().overrideBottomDialogRes().overrideSelectionMenuBackgroundColor(bottomMenu.isLightTheme());
        }
        if (bottomMenu.getSelection() == position) {
            //选中的背景变色
            if (overrideSelectionBackgroundColorRes != 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    convertView.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(overrideSelectionBackgroundColorRes)));
                }
            }
        }
        CharSequence text = objects.get(position);
        
        int textColor = bottomMenu.isLightTheme() ? R.color.black90 : R.color.white90;
        if (bottomMenu.getStyle().overrideBottomDialogRes() != null) {
            if (bottomMenu.getStyle().overrideBottomDialogRes().overrideMenuTextColor(bottomMenu.isLightTheme()) != 0) {
                textColor = bottomMenu.getStyle().overrideBottomDialogRes().overrideMenuTextColor(bottomMenu.isLightTheme());
            }
        }
        
        if (null != text) {
            if (defaultMenuTextInfo == null) {
                defaultMenuTextInfo = new TextInfo()
                        .setShowEllipsis(viewHolder.txtDialogxMenuText.getEllipsize() == TextUtils.TruncateAt.END)
                        .setFontColor(viewHolder.txtDialogxMenuText.getTextColors().getDefaultColor())
                        .setBold(viewHolder.txtDialogxMenuText.getPaint().isFakeBoldText())
                        .setFontSize(px2dip(viewHolder.txtDialogxMenuText.getTextSize()))
                        .setGravity(viewHolder.txtDialogxMenuText.getGravity())
                        .setMaxLines(viewHolder.txtDialogxMenuText.getMaxLines());
            }
            viewHolder.txtDialogxMenuText.setText(text);
            viewHolder.txtDialogxMenuText.setTextColor(context.getResources().getColor(textColor));
            if (bottomMenu.getMenuItemTextInfoInterceptor() != null) {
                TextInfo textInfo = bottomMenu.getMenuItemTextInfoInterceptor().menuItemTextInfo(bottomMenu, position, text.toString());
                if (textInfo != null) {
                    useTextInfo(viewHolder.txtDialogxMenuText, textInfo);
                } else {
                    if (bottomMenu.getMenuTextInfo() != null) {
                        useTextInfo(viewHolder.txtDialogxMenuText, bottomMenu.getMenuTextInfo());
                    } else {
                        useTextInfo(viewHolder.txtDialogxMenuText, defaultMenuTextInfo);
                    }
                }
            } else {
                if (bottomMenu.getMenuTextInfo() != null) {
                    useTextInfo(viewHolder.txtDialogxMenuText, bottomMenu.getMenuTextInfo());
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (viewHolder.imgDialogxMenuSelection != null) {
                    if (bottomMenu.getStyle().overrideBottomDialogRes() != null && bottomMenu.getStyle().overrideBottomDialogRes().selectionImageTint(bottomMenu.isLightTheme())) {
                        viewHolder.imgDialogxMenuSelection.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(textColor)));
                    } else {
                        viewHolder.imgDialogxMenuSelection.setImageTintList(null);
                    }
                }
            }
            
            if (bottomMenu.getOnIconChangeCallBack() != null) {
                int resId = bottomMenu.getOnIconChangeCallBack().getIcon(bottomMenu, position, text.toString());
                boolean autoTintIconInLightOrDarkMode = bottomMenu.getOnIconChangeCallBack().isAutoTintIconInLightOrDarkMode();
                
                if (resId != 0) {
                    viewHolder.imgDialogxMenuIcon.setVisibility(View.VISIBLE);
                    viewHolder.imgDialogxMenuIcon.setImageResource(resId);
                    if (viewHolder.spaceDialogxRightPadding != null) {
                        viewHolder.spaceDialogxRightPadding.setVisibility(View.VISIBLE);
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        if (autoTintIconInLightOrDarkMode) {
                            viewHolder.imgDialogxMenuIcon.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(textColor)));
                        }
                    }
                } else {
                    viewHolder.imgDialogxMenuIcon.setVisibility(View.GONE);
                    if (viewHolder.spaceDialogxRightPadding != null) {
                        viewHolder.spaceDialogxRightPadding.setVisibility(View.GONE);
                    }
                }
            } else {
                viewHolder.imgDialogxMenuIcon.setVisibility(View.GONE);
                if (viewHolder.spaceDialogxRightPadding != null) {
                    viewHolder.spaceDialogxRightPadding.setVisibility(View.GONE);
                }
            }
        }
        if (bottomMenu.getMenuMenuItemLayoutRefreshCallback() != null) {
            bottomMenu.getMenuMenuItemLayoutRefreshCallback().getView(bottomMenu, position, convertView, parent);
        }
        return convertView;
    }
    
    private int px2dip(float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}