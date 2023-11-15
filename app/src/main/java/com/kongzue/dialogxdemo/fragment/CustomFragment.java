package com.kongzue.dialogxdemo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.kongzue.dialogxdemo.R;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2021/8/17 13:50
 */
public class CustomFragment extends Fragment {
    
    private static int index = 0;
    private View.OnClickListener addButtonClickListener;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    private TextView txtInfo;
    private MaterialButton btnAddDialog;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_custom, null);
        txtInfo = view.findViewById(R.id.txt_info);
        btnAddDialog = view.findViewById(R.id.btn_addDialog);
        txtInfo.setText("这是第：" + (index++) + " 个 Fragment");
        btnAddDialog.setOnClickListener(addButtonClickListener);
        return view;
    }
    
    @Override
    public void onPause() {
        super.onPause();
    }
    
    public View.OnClickListener getAddButtonClickListener() {
        return addButtonClickListener;
    }
    
    public CustomFragment setAddButtonClickListener(View.OnClickListener addButtonClickListener) {
        this.addButtonClickListener = addButtonClickListener;
        return this;
    }
}
