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

import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.R;
import com.kongzue.dialogx.dialogs.PopMenu;

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
    
    public PopMenuArrayAdapter(PopMenu popMenu, Context context, List<CharSequence> menuList) {
        this.popMenu = popMenu;
        this.menuList = menuList;
        this.context = context;
    }
    
    @Override
    public int getCount() {
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
            LayoutInflater mInflater = LayoutInflater.from(context);
            int resourceId = R.layout.item_dialogx_material_context_menu_normal_text;
            
            convertView = mInflater.inflate(resourceId, null);
            
            viewHolder.imgDialogxMenuIcon = convertView.findViewById(R.id.img_dialogx_menu_icon);
            viewHolder.txtDialogxMenuText = convertView.findViewById(R.id.txt_dialogx_menu_text);
            viewHolder.spaceRightPadding = convertView.findViewById(R.id.space_dialogx_right_padding);
            
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.imgDialogxMenuIcon.setVisibility(View.GONE);
        viewHolder.txtDialogxMenuText.setText(menuList.get(position));
        
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
                    viewHolder.spaceRightPadding.setVisibility(View.VISIBLE);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (autoTintIconInLightOrDarkMode) {
                        viewHolder.imgDialogxMenuIcon.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(textColor)));
                    }
                }
            } else {
                viewHolder.imgDialogxMenuIcon.setVisibility(View.GONE);
                viewHolder.spaceRightPadding.setVisibility(View.GONE);
            }
        } else {
            viewHolder.imgDialogxMenuIcon.setVisibility(View.GONE);
            viewHolder.spaceRightPadding.setVisibility(View.GONE);
        }
        return convertView;
    }
    
    private boolean isHaveProperties(int gravity, int property) {
        return (gravity & property) == property;
    }
    
    class ViewHolder {
        ImageView imgDialogxMenuIcon;
        TextView txtDialogxMenuText;
        Space spaceRightPadding;
    }
}
