package com.kongzue.dialogx.dialogs;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.kongzue.dialogx.R;
import com.kongzue.dialogx.interfaces.OnBackPressedListener;
import com.kongzue.dialogx.util.NormalMenuArrayAdapter;
import com.kongzue.dialogx.util.views.BottomDialogListView;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.OVER_SCROLL_NEVER;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2020/10/6 23:48
 */
public class BottomMenu extends BottomDialog {
    
    protected BottomMenu() {
        me = this;
    }
    
    public static BottomMenu build() {
        return new BottomMenu();
    }
    
    private OnIconChangeCallBack onIconChangeCallBack;
    private BottomDialogListView listView;
    private BaseAdapter menuArrayAdapter;
    private List<CharSequence> menuList;
    
    @Override
    protected void onDialogInit(final DialogImpl dialog) {
        if (dialog != null) {
            listView = new BottomDialogListView(getContext());
            listView.setOverScrollMode(OVER_SCROLL_NEVER);
            listView.setDivider(getResources().getDrawable(R.drawable.rect_dialogx_material_menu_split_divider));
            listView.setDividerHeight(1);
            RelativeLayout.LayoutParams listViewLp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.boxCustom.addView(listView, listViewLp);
            test();
        }
    }
    
    private void test() {
        menuList = new ArrayList<>();
        menuList.add("添加");
        menuList.add("查看");
        menuList.add("编辑");
        menuList.add("删除");
        menuList.add("分享");
        menuList.add("评论");
        menuList.add("下载");
        menuList.add("收藏");
        menuList.add("赞！");
        menuList.add("不喜欢");
        menuList.add("所属专辑");
        menuList.add("复制链接");
        menuList.add("类似推荐");
        menuList.add("添加");
        menuList.add("查看");
        menuList.add("编辑");
        menuList.add("删除");
        menuList.add("分享");
        menuList.add("评论");
        menuList.add("下载");
        menuList.add("收藏");
        menuList.add("赞！");
        menuList.add("不喜欢");
        menuList.add("所属专辑");
        menuList.add("复制链接");
        menuList.add("类似推荐");
        
        menuArrayAdapter = new NormalMenuArrayAdapter(getContext(), R.layout.item_dialogx_material_bottom_menu_normal_text, menuList, getOnIconChangeCallBack());
        listView.setAdapter(menuArrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), menuList.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    public OnIconChangeCallBack getOnIconChangeCallBack() {
        return onIconChangeCallBack;
    }
    
    public BottomMenu setOnIconChangeCallBack(OnIconChangeCallBack onIconChangeCallBack) {
        this.onIconChangeCallBack = onIconChangeCallBack;
        return this;
    }
    
    public interface OnIconChangeCallBack {
        int getIcon(int index, String menuText);
    }
    
    public OnBackPressedListener getOnBackPressedListener() {
        return onBackPressedListener;
    }
    
    public BottomMenu setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
        refreshUI();
        return this;
    }
}
