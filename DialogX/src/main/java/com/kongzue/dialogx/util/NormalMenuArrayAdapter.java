package com.kongzue.dialogx.util;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
public class NormalMenuArrayAdapter extends ArrayAdapter {
    
    private BottomMenu.OnIconChangeCallBack onIconChangeCallBack;
    public int resoureId;
    public List<CharSequence> objects;
    public Context context;
    
    public NormalMenuArrayAdapter(Context context, int resourceId, List<CharSequence> objects, BottomMenu.OnIconChangeCallBack onIconChangeCallBack) {
        super(context, resourceId, objects);
        this.objects = objects;
        this.resoureId = resourceId;
        this.context = context;
        this.onIconChangeCallBack = onIconChangeCallBack;
    }
    
    public class ViewHolder {
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
            convertView = mInflater.inflate(resoureId, null);
            
            viewHolder.imgDialogxMenuIcon = convertView.findViewById(R.id.img_dialogx_menu_icon);
            viewHolder.txtDialogxMenuText = convertView.findViewById(R.id.txt_dialogx_menu_text);
            
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        CharSequence text = objects.get(position);
        if (null != text) {
            viewHolder.txtDialogxMenuText.setText(text);
            if (onIconChangeCallBack != null) {
                int resId = onIconChangeCallBack.getIcon(position, text.toString());
                if (resId != 0) {
                    viewHolder.imgDialogxMenuIcon.setVisibility(View.VISIBLE);
                    viewHolder.imgDialogxMenuIcon.setImageResource(resId);
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