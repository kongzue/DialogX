package com.kongzue.dialogx.util;

import static com.kongzue.dialogx.interfaces.BaseDialog.useTextInfo;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import com.kongzue.dialogx.R;
import com.kongzue.dialogx.dialogs.PopMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2021/8/18 15:25
 */
public class PopMenuArrayAdapter extends BaseAdapter {
    
    public List<CharSequence> menuList;
    public Context context;
    private PopMenu popMenu;
    LayoutInflater mInflater;
    
    public PopMenuArrayAdapter(PopMenu popMenu, Context context, List<CharSequence> menuList) {
        this.popMenu = popMenu;
        this.menuList = menuList;
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }
    
    public List<CharSequence> getMenuList() {
        return menuList;
    }
    
    @Override
    public int getCount() {
        if (menuList == null) {
            menuList = new ArrayList<>();
        }
        return menuList.size();
    }
    
    @Override
    public Object getItem(int position) {
        return menuList.get(position);
    }
    
    @Override
    public long getItemId(int position) {
        return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            int resourceId = R.layout.item_dialogx_material_context_menu_normal_text;
            if (popMenu.getStyle().popMenuSettings() != null) {
                int customItemLayout = popMenu.getStyle().popMenuSettings().overrideMenuItemLayoutRes(popMenu.isLightTheme());
                if (customItemLayout != 0) {
                    resourceId = customItemLayout;
                }
            }
            
            convertView = mInflater.inflate(resourceId, null);
            
            viewHolder.boxItem = convertView.findViewById(R.id.box_item);
            viewHolder.imgDialogxMenuIcon = convertView.findViewById(R.id.img_dialogx_menu_icon);
            viewHolder.txtDialogxMenuText = convertView.findViewById(R.id.txt_dialogx_menu_text);
            viewHolder.spaceDialogxRightPadding = convertView.findViewById(R.id.space_dialogx_right_padding);
            
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        int customBackgroundRes = popMenu.getStyle().popMenuSettings() == null ? 0 : popMenu.getStyle().popMenuSettings().overrideMenuItemBackgroundRes(popMenu.isLightTheme(), position, getCount(), false);
        if (customBackgroundRes != 0) {
            convertView.setBackgroundResource(customBackgroundRes);
        }
        if (viewHolder.boxItem != null) {
            if (popMenu.getPressedIndex() == position) {
                viewHolder.boxItem.setBackgroundResource(popMenu.isLightTheme() ? R.color.black5 : R.color.white5);
            } else {
                viewHolder.boxItem.setBackgroundResource(R.color.empty);
            }
        }
        viewHolder.imgDialogxMenuIcon.setVisibility(View.GONE);
        viewHolder.txtDialogxMenuText.setText(menuList.get(position));
        if (popMenu.getStyle().popMenuSettings() != null && popMenu.getStyle().popMenuSettings().paddingVertical() != 0) {
            if (position == 0) {
                convertView.setPadding(0, popMenu.getStyle().popMenuSettings().paddingVertical(), 0, 0);
            } else if (position == getCount() - 1) {
                convertView.setPadding(0, 0, 0, popMenu.getStyle().popMenuSettings().paddingVertical());
            } else {
                convertView.setPadding(0, 0, 0, 0);
            }
        }
        
        if (popMenu.getMenuTextInfo() != null) {
            useTextInfo(viewHolder.txtDialogxMenuText, popMenu.getMenuTextInfo());
        }
        
        int textColor = popMenu.isLightTheme() ? R.color.black90 : R.color.white90;
        viewHolder.txtDialogxMenuText.setTextColor(context.getResources().getColor(textColor));
        
        if (popMenu.getOnIconChangeCallBack() != null) {
            int resId = popMenu.getOnIconChangeCallBack().getIcon(popMenu, position, menuList.get(position).toString());
            boolean autoTintIconInLightOrDarkMode = popMenu.getOnIconChangeCallBack().isAutoTintIconInLightOrDarkMode();
            
            if (resId != 0) {
                viewHolder.imgDialogxMenuIcon.setVisibility(View.VISIBLE);
                viewHolder.imgDialogxMenuIcon.setImageResource(resId);
                if (isHaveProperties(viewHolder.txtDialogxMenuText.getGravity(), Gravity.CENTER) || isHaveProperties(viewHolder.txtDialogxMenuText.getGravity(), Gravity.CENTER_HORIZONTAL)) {
                    if (viewHolder.spaceDialogxRightPadding != null) {
                        viewHolder.spaceDialogxRightPadding.setVisibility(View.VISIBLE);
                    }
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
        
        if (popMenu.getMenuMenuItemLayoutRefreshCallback() != null) {
            popMenu.getMenuMenuItemLayoutRefreshCallback().getView(popMenu, position, convertView, parent);
        }
        
        return convertView;
    }
    
    private boolean isHaveProperties(int gravity, int property) {
        return (gravity & property) == property;
    }
    
    class ViewHolder {
        LinearLayout boxItem;
        ImageView imgDialogxMenuIcon;
        TextView txtDialogxMenuText;
        Space spaceDialogxRightPadding;
    }
}
