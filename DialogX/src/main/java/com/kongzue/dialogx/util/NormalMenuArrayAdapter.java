package com.kongzue.dialogx.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.StateListDrawable;
import android.util.Log;
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
        ImageView imgDialogxMenuSelection;
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
            int overrideSelectionBackgroundColorRes = 0;
            if (bottomMenu.getStyle().overrideBottomDialogRes() != null) {
                resourceId = bottomMenu.getStyle().overrideBottomDialogRes().overrideMenuItemLayout(bottomMenu.isLightTheme(), position, getCount(), false);
                if (resourceId == 0) {
                    resourceId = R.layout.item_dialogx_material_bottom_menu_normal_text;
                } else {
                    if (bottomMenu.getDialogImpl().txtDialogTitle.getVisibility() == View.VISIBLE ||
                            bottomMenu.getDialogImpl().txtDialogTip.getVisibility() == View.VISIBLE ||
                            bottomMenu.getCustomView() != null) {
                        if (position == 0) {
                            resourceId = bottomMenu.getStyle().overrideBottomDialogRes().overrideMenuItemLayout(bottomMenu.isLightTheme(), position, getCount(), true);
                        }
                    }
                }
                
                overrideSelectionBackgroundColorRes = bottomMenu.getStyle().overrideBottomDialogRes().overrideSelectionMenuBackgroundColor(bottomMenu.isLightTheme());
            }
            convertView = mInflater.inflate(resourceId, null);
            
            viewHolder.imgDialogxMenuIcon = convertView.findViewById(R.id.img_dialogx_menu_icon);
            viewHolder.txtDialogxMenuText = convertView.findViewById(R.id.txt_dialogx_menu_text);
            viewHolder.imgDialogxMenuSelection = convertView.findViewById(R.id.img_dialogx_menu_selection);
            
            if (bottomMenu.getSelection() >= 0) {
                if (viewHolder.imgDialogxMenuSelection != null) {
                    if (bottomMenu.getSelection() == position) {
                        viewHolder.imgDialogxMenuSelection.setVisibility(View.VISIBLE);
                    } else {
                        viewHolder.imgDialogxMenuSelection.setVisibility(View.INVISIBLE);
                    }
                }
            } else {
                viewHolder.imgDialogxMenuSelection.setVisibility(View.GONE);
            }
            if (bottomMenu.getSelection() == position) {
                if (overrideSelectionBackgroundColorRes != 0) {
                    convertView.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(overrideSelectionBackgroundColorRes)));
                    final View finalRootView = convertView;
                    convertView.post(new Runnable() {
                        @Override
                        public void run() {
                            finalRootView.setPressed(true);
                        }
                    });
                }
            } else {
                convertView.setBackgroundTintList(null);
            }
            
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
            if (viewHolder.imgDialogxMenuSelection != null) {
                if (bottomMenu.getStyle().overrideBottomDialogRes()!=null && bottomMenu.getStyle().overrideBottomDialogRes().selectionImageTint(bottomMenu.isLightTheme())) {
                    viewHolder.imgDialogxMenuSelection.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(textColor)));
                }else{
                    viewHolder.imgDialogxMenuSelection.setImageTintList(null);
                }
            }
            
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