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
import com.kongzue.dialogx.dialogs.MessageMenu;
import com.kongzue.dialogx.interfaces.SELECT_MODE;

import java.util.List;

public class MessageMenuArrayAdapter extends BaseAdapter {
    private MessageMenu messageMenu;
    public List<CharSequence> objects;
    public Context context;

    public MessageMenuArrayAdapter(MessageMenu messageMenu, Context context, List<CharSequence> objects) {
        this.objects = objects;
        this.context = context;
        this.messageMenu = messageMenu;
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
        MessageMenuArrayAdapter.ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new MessageMenuArrayAdapter.ViewHolder();
            LayoutInflater mInflater = LayoutInflater.from(context);

            int resourceId = R.layout.item_dialogx_material_bottom_menu_normal_text;
            if (messageMenu.getStyle().overrideBottomDialogRes() != null) {
                resourceId = messageMenu.getStyle().overrideBottomDialogRes().overrideMenuItemLayout(messageMenu.isLightTheme(), position, getCount(), false);
                if (resourceId == 0) {
                    resourceId = R.layout.item_dialogx_material_bottom_menu_normal_text;
                } else {
                    if (!isNull(messageMenu.getTitle()) || !isNull(messageMenu.getMessage()) ||
                            messageMenu.getCustomView() != null) {
                        if (position == 0) {
                            resourceId = messageMenu.getStyle().overrideBottomDialogRes().overrideMenuItemLayout(messageMenu.isLightTheme(), position, getCount(), true);
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
            viewHolder = (MessageMenuArrayAdapter.ViewHolder) convertView.getTag();
        }
        if (!messageMenu.isMenuItemEnable(position)) {
            convertView.setAlpha(0.4f);
        } else {
            convertView.setAlpha(1f);
        }
        if (messageMenu.getSelectMode() == SELECT_MODE.SINGLE) {
            if (viewHolder.imgDialogxMenuSelection != null) {
                if (messageMenu.getSelection() == position) {
                    viewHolder.imgDialogxMenuSelection.setVisibility(View.VISIBLE);
                    int overrideSelectionImageResId = messageMenu.getStyle().overrideBottomDialogRes().overrideSelectionImage(messageMenu.isLightTheme(), true);
                    if (overrideSelectionImageResId != 0) {
                        viewHolder.imgDialogxMenuSelection.setImageResource(overrideSelectionImageResId);
                    }
                } else {
                    int overrideSelectionImageResId = messageMenu.getStyle().overrideBottomDialogRes().overrideSelectionImage(messageMenu.isLightTheme(), false);
                    if (overrideSelectionImageResId != 0) {
                        viewHolder.imgDialogxMenuSelection.setVisibility(View.VISIBLE);
                        viewHolder.imgDialogxMenuSelection.setImageResource(overrideSelectionImageResId);
                    } else {
                        viewHolder.imgDialogxMenuSelection.setVisibility(View.INVISIBLE);
                    }
                }
            }
        } else if (messageMenu.getSelectMode() == SELECT_MODE.MULTIPLE) {
            if (viewHolder.imgDialogxMenuSelection != null) {
                if (messageMenu.getSelectionList().contains(position)) {
                    viewHolder.imgDialogxMenuSelection.setVisibility(View.VISIBLE);
                    int overrideSelectionImageResId = messageMenu.getStyle().overrideBottomDialogRes().overrideMultiSelectionImage(messageMenu.isLightTheme(), true);
                    if (overrideSelectionImageResId != 0) {
                        viewHolder.imgDialogxMenuSelection.setImageResource(overrideSelectionImageResId);
                    }
                } else {
                    int overrideSelectionImageResId = messageMenu.getStyle().overrideBottomDialogRes().overrideMultiSelectionImage(messageMenu.isLightTheme(), false);
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
        if (messageMenu.getStyle().overrideBottomDialogRes() != null) {
            overrideSelectionBackgroundColorRes = messageMenu.getStyle().overrideBottomDialogRes().overrideSelectionMenuBackgroundColor(messageMenu.isLightTheme());
        }
        if (messageMenu.getSelection() == position) {
            //选中的背景变色
            if (overrideSelectionBackgroundColorRes != 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    convertView.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(overrideSelectionBackgroundColorRes)));
                }
            }
        }
        CharSequence text = objects.get(position);

        int textColor = messageMenu.isLightTheme() ? R.color.black90 : R.color.white90;
        if (messageMenu.getStyle().overrideBottomDialogRes() != null) {
            if (messageMenu.getStyle().overrideBottomDialogRes().overrideMenuTextColor(messageMenu.isLightTheme()) != 0) {
                textColor = messageMenu.getStyle().overrideBottomDialogRes().overrideMenuTextColor(messageMenu.isLightTheme());
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
            if (messageMenu.getMenuItemTextInfoInterceptor() != null) {
                TextInfo textInfo = messageMenu.getMenuItemTextInfoInterceptor().menuItemTextInfo(messageMenu, position, text.toString());
                if (textInfo != null) {
                    useTextInfo(viewHolder.txtDialogxMenuText, textInfo);
                } else {
                    if (messageMenu.getMenuTextInfo() != null) {
                        useTextInfo(viewHolder.txtDialogxMenuText, messageMenu.getMenuTextInfo());
                    } else {
                        useTextInfo(viewHolder.txtDialogxMenuText, defaultMenuTextInfo);
                    }
                }
            } else {
                if (messageMenu.getMenuTextInfo() != null) {
                    useTextInfo(viewHolder.txtDialogxMenuText, messageMenu.getMenuTextInfo());
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (viewHolder.imgDialogxMenuSelection != null) {
                    if (messageMenu.getStyle().overrideBottomDialogRes() != null && messageMenu.getStyle().overrideBottomDialogRes().selectionImageTint(messageMenu.isLightTheme())) {
                        viewHolder.imgDialogxMenuSelection.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(textColor)));
                    } else {
                        viewHolder.imgDialogxMenuSelection.setImageTintList(null);
                    }
                }
            }

            if (messageMenu.getOnIconChangeCallBack() != null) {
                int resId = messageMenu.getOnIconChangeCallBack().getIcon(messageMenu, position, text.toString());
                boolean autoTintIconInLightOrDarkMode = messageMenu.getOnIconChangeCallBack().isAutoTintIconInLightOrDarkMode() == null ? messageMenu.isAutoTintIconInLightOrDarkMode() : messageMenu.getOnIconChangeCallBack().isAutoTintIconInLightOrDarkMode();

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
                if (messageMenu.getIconResIds() != null) {
                    int resId = messageMenu.getIconResIds(position);
                    boolean autoTintIconInLightOrDarkMode = messageMenu.isAutoTintIconInLightOrDarkMode();

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
        }
        if (messageMenu.getMenuMenuItemLayoutRefreshCallback() != null) {
            messageMenu.getMenuMenuItemLayoutRefreshCallback().getView(messageMenu, position, convertView, parent);
        }
        return convertView;
    }

    private int px2dip(float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}