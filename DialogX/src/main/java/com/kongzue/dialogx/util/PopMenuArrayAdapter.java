package com.kongzue.dialogx.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kongzue.dialogx.R;

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
    
    public PopMenuArrayAdapter(Context context, List<CharSequence> menuList) {
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
            viewHolder.imgDialogxMenuSelection = convertView.findViewById(R.id.img_dialogx_menu_selection);
    
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.imgDialogxMenuIcon.setVisibility(View.GONE);
        viewHolder.txtDialogxMenuText.setText(menuList.get(position));
        return convertView;
    }
    
    class ViewHolder {
        ImageView imgDialogxMenuIcon;
        TextView txtDialogxMenuText;
        ImageView imgDialogxMenuSelection;
    }
}
