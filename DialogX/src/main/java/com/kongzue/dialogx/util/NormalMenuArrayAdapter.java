package com.kongzue.dialogx.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
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
public class NormalMenuArrayAdapter extends BaseAdapter {
    
    private BottomMenu bottomMenu;
    public List<CharSequence> objects;
    public Context context;
    
    public NormalMenuArrayAdapter(BottomMenu bottomMenu, Context context, List<CharSequence> objects) {
        this.objects = objects;
        this.context = context;
        this.bottomMenu = bottomMenu;
    }
    
    class ViewHolder {
        ImageView imgDialogxMenuIcon;
        TextView txtDialogxMenuText;
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
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater mInflater = LayoutInflater.from(context);
            
            int resourceId = R.layout.item_dialogx_material_bottom_menu_normal_text;
            if (bottomMenu.getStyle().overrideBottomDialogRes() != null) {
                resourceId = bottomMenu.getStyle().overrideBottomDialogRes().overrideMenuItemLayout(bottomMenu.isLightTheme(), position, getCount());
                if (resourceId == 0) {
                    resourceId = R.layout.item_dialogx_material_bottom_menu_normal_text;
                } else {
                    if (bottomMenu.getDialogImpl().txtDialogTitle.getVisibility() == View.VISIBLE ||
                            bottomMenu.getDialogImpl().txtDialogTip.getVisibility() == View.VISIBLE||
                            bottomMenu.getCustomView()!=null) {
                        if (position == 0) {
                            //有显示 title、tip 或自定义布局时第一个 item 按钮显示为 center 部分的样式
                            resourceId = bottomMenu.getStyle().overrideBottomDialogRes().overrideMenuItemLayout(bottomMenu.isLightTheme(), 1, getCount() + 1);
                        }
                    }
                }
            }
            convertView = mInflater.inflate(resourceId, null);
            
            viewHolder.imgDialogxMenuIcon = convertView.findViewById(R.id.img_dialogx_menu_icon);
            viewHolder.txtDialogxMenuText = convertView.findViewById(R.id.txt_dialogx_menu_text);
            
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        CharSequence text = objects.get(position);
        
        int textColor = bottomMenu.isLightTheme() ? R.color.black90 : R.color.white90;
        if (bottomMenu.getStyle().overrideBottomDialogRes() != null) {
            if (bottomMenu.getStyle().overrideBottomDialogRes().overrideMenuTextColor(bottomMenu.isLightTheme()) != 0) {
                textColor = bottomMenu.getStyle().overrideBottomDialogRes().overrideMenuTextColor(bottomMenu.isLightTheme());
            }
        }
        
        if (null != text) {
            viewHolder.txtDialogxMenuText.setText(text);
            viewHolder.txtDialogxMenuText.setTextColor(context.getResources().getColor(textColor));
            
            if (bottomMenu.getOnIconChangeCallBack() != null) {
                int resId = bottomMenu.getOnIconChangeCallBack().getIcon(bottomMenu, position, text.toString());
                boolean autoTintIconInLightOrDarkMode = bottomMenu.getOnIconChangeCallBack().isAutoTintIconInLightOrDarkMode();
                
                if (resId != 0) {
                    viewHolder.imgDialogxMenuIcon.setVisibility(View.VISIBLE);
                    viewHolder.imgDialogxMenuIcon.setImageResource(resId);
                    
                    if (autoTintIconInLightOrDarkMode) {
                        viewHolder.imgDialogxMenuIcon.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(textColor)));
                    }
                } else {
                    viewHolder.imgDialogxMenuIcon.setVisibility(View.GONE);
                }
            } else {
                viewHolder.imgDialogxMenuIcon.setVisibility(View.GONE);
            }
        }
        
        return convertView;
    }
}