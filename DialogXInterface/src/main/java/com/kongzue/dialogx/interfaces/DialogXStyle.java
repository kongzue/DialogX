package com.kongzue.dialogx.interfaces;

import android.content.Context;

public abstract class DialogXStyle {
    
    /**
     * DialogXStyle 版本
     * 相关文档请参阅：https://github.com/kongzue/DialogX/wiki/%E8%87%AA%E5%AE%9A%E4%B9%89-DialogX-%E4%B8%BB%E9%A2%98
     */
    public static final int styleVer = 5;
    
    /**
     * 按钮类型常量
     */
    public static final int BUTTON_OK = 1;
    public static final int BUTTON_CANCEL = 2;
    public static final int BUTTON_OTHER = 3;
    public static final int SPACE = 4;
    public static final int SPLIT = 5;
    
    /**
     * 重写基础对话框布局资源
     * <p>
     * 重写 layout 方法用于设置默认的消息对话框的 layout 布局文件，其中 light 参数用于判断是否为亮色模式，
     * 你可以在这里查看亮色默认主题布局文件的实现 和 暗色默认主题布局文件的实现，
     * 请参照 Demo 布局的格式进行布局设计，不建议修改或去掉布局中的含有 id 的组件。
     *
     * @param light 亮暗色
     * @return 布局资源 id
     */
    public int layout(boolean light) {
        return 0;
    }
    
    /**
     * 修改默认对话框启动动画效果
     * <p>
     * 你可以自定义消息对话框的启动动画效果，当你return 0时则采用默认实现，
     * 要自定义动画文件，可以参考：默认对话框启动动画文件 https://github.com/kongzue/DialogX/blob/master/DialogX/src/main/res/anim/anim_dialogx_default_enter.xml
     *
     * @return 布局资源 id
     */
    public int enterAnimResId() {
        return 0;
    }
    
    /**
     * 修改默认对话框关闭动画效果
     * <p>
     * 你可以自定义消息对话框的关闭动画效果，当你return 0时则采用默认实现，
     * 要自定义动画文件，可以参考：默认对话框启动动画文件 https://github.com/kongzue/DialogX/blob/master/DialogX/src/main/res/anim/anim_dialogx_default_exit.xml
     *
     * @return 布局资源 id
     */
    public int exitAnimResId() {
        return 0;
    }
    
    /**
     * 修改默认按钮排序(纵向)
     * <p>
     * 在特殊情况下，你可以调整对话框的按钮布局顺序，例如，在横向时，我们一般采用“其他、间隔、取消、确定”的逻辑，要修改按钮排序顺序，你可以重写以下接口。
     * <p>
     * eg. new int[]{BUTTON_OTHER, SPLIT, BUTTON_CANCEL, BUTTON_OK}
     *
     * @return 排序数组
     */
    public int[] verticalButtonOrder() {
        return new int[]{BUTTON_OTHER, SPLIT, BUTTON_CANCEL, BUTTON_OK};
    }
    
    /**
     * 修改默认按钮排序（横向）
     * <p>
     * 在特殊情况下，你可以调整对话框的按钮布局顺序，例如，在纵向时，我们可以调整为“确定、其他、取消”的顺序，要修改按钮排序顺序，你可以重写以下接口。
     * <p>
     * eg. new int[]{BUTTON_OTHER, BUTTON_CANCEL, BUTTON_OK}
     *
     * @return 排序数组
     */
    public int[] horizontalButtonOrder() {
        return new int[]{BUTTON_OTHER, BUTTON_CANCEL, BUTTON_OK};
    }
    
    /**
     * 按钮分割线宽度
     * <p>
     * 需要调整分隔线的宽度，可以重写以下接口进行设置，这些设置也可以使用 return 0不进行设置。
     *
     * @return 像素值
     */
    public int splitWidthPx() {
        return 1;
    }
    
    ;
    
    /**
     * 按钮分割线颜色
     * <p>
     * 需要调整分隔线的颜色，可以重写以下接口进行设置，这些设置也可以使用 return 0不进行设置。
     *
     * @return 颜色资源 id
     */
    public int splitColorRes(boolean light) {
        return 0x1A000000;
    }
    
    ;
    
    /**
     * 模糊背景
     * <p>
     * 默认对话框支持模糊化背景，如果需要模糊的背景效果，你可以重写以下接口：
     *
     * @return BlurBackgroundSetting
     */
    public BlurBackgroundSetting messageDialogBlurSettings() {
        return null;
    }
    
    /**
     * 自定义按钮样式
     * <p>
     * 一些主题要求我们在显示按钮时提供不同的样式，你可以通过两个接口来进行自定义：当对话框按钮处于横向/纵向时，
     * 或者当按钮显示数量为1个、2个或3个时，呈现不同的样式。
     * <p>
     * 例如 iOS 主题，横向且只显示一个按钮（OkButton）时，需要左下角和右下角都为圆角的按钮样式，
     * 当显示多个按钮时则为仅右下角为圆角的按钮样式，如果需要基于不同的情况进行相应按钮样式调整，你可以重写以下接口：
     *
     * @return HorizontalButtonRes
     */
    public HorizontalButtonRes overrideHorizontalButtonRes() {
        return null;
    }
    
    public VerticalButtonRes overrideVerticalButtonRes() {
        return null;
    }
    
    /**
     * 修改等待/提示框的样式
     * <p>
     * 如果需要修改等待/提示框的样式效果，你可以重写这个接口：
     *
     * @return WaitTipRes
     */
    public WaitTipRes overrideWaitTipRes() {
        return null;
    }
    
    /**
     * 自定义底部对话框/菜单样式
     * <p>
     * 底部对话框是 DialogX 中第二个具有丰富功能的组件，您可以实现overrideBottomDialogRes()方法以自定义底部对话框的样式细节。
     * 同样的，当return null 时使用默认样式（Material 主题）。
     *
     * @return BottomDialogRes
     */
    public BottomDialogRes overrideBottomDialogRes() {
        return null;
    }
    
    /**
     * 自定义 PopTip 样式
     *
     * @return PopTipSettings
     */
    public PopTipSettings popTipSettings() {
        return null;
    }
    
    /**
     * 自定义 PopNotificationSettings 样式
     *
     * @return PopNotificationSettings
     */
    public PopNotificationSettings popNotificationSettings() {
        return null;
    }
    
    /**
     * 自定义 PopMenu 样式
     *
     * @return PopMenuSettings
     */
    public PopMenuSettings popMenuSettings() {
        return null;
    }
    
    /**
     * 模糊背景设置
     * <p>
     * 如果不需要可以return null进行默认处理。
     * <p>
     * 此接口需要重写一个BlurBackgroundSetting，其中包含三个子接口，
     * 分别是blurBackground()用于判断是否开启模糊，
     * blurForwardColorRes(boolean light)用于处理前景色（建议设置一定的透明度保证可以看到背后的模糊效果），
     * blurBackgroundRoundRadiusPx()用于给定模糊效果的圆角半径，单位为像素（Px）。
     */
    public abstract class BlurBackgroundSetting {
        
        public boolean blurBackground() {
            return false;
        }
        
        public int blurForwardColorRes(boolean light) {
            return 0;
        }
        
        public int blurBackgroundRoundRadiusPx() {
            return 0;
        }
    }
    
    /**
     * 自定义按钮样式设置
     * <p>
     * 其中 visibleButtonCount 参数为当前显示按钮的数量，light参数为当前对话框的亮/暗色模式。
     * <p>
     * drawable 只接受 xml 配置，可以定义不同状态时的按钮样式效果，如有需要请参考 iOS drawable 样式
     * <p>
     * overrideHorizontalButtonRes和overrideVerticalButtonRes返回值return null时默认不进行样式修改设置。
     */
    public abstract class HorizontalButtonRes {
        
        public int overrideHorizontalOkButtonBackgroundRes(int visibleButtonCount, boolean light) {
            return 0;
        }
        
        public int overrideHorizontalCancelButtonBackgroundRes(int visibleButtonCount, boolean light) {
            return 0;
        }
        
        public int overrideHorizontalOtherButtonBackgroundRes(int visibleButtonCount, boolean light) {
            return 0;
        }
    }
    
    public abstract class VerticalButtonRes {
        
        public int overrideVerticalOkButtonBackgroundRes(int visibleButtonCount, boolean light) {
            return 0;
        }
        
        public int overrideVerticalCancelButtonBackgroundRes(int visibleButtonCount, boolean light) {
            return 0;
        }
        
        public int overrideVerticalOtherButtonBackgroundRes(int visibleButtonCount, boolean light) {
            return 0;
        }
    }
    
    /**
     * 修改等待/提示框的样式设置
     * <p>
     * 此接口return null时采用默认样式。
     */
    public abstract class WaitTipRes {
        
        //重写布局资源
        public int overrideWaitLayout(boolean light) {
            return 0;
        }
        
        //设置圆角像素
        public int overrideRadiusPx() {
            return 0;
        }
        
        //判断是否需要模糊背景效果
        public boolean blurBackground() {
            return false;
        }
        
        //重新设置前景色
        public int overrideBackgroundColorRes(boolean light) {
            return 0;
        }
        
        //设置在亮/暗色时的文字颜色，注意，此颜色也将修改进度动画的颜色。
        public int overrideTextColorRes(boolean light) {
            return 0;
        }
        
        /**
         * 自定义等待提示动画组件接口 ProgressViewInterface
         * <p>
         * 需要在自定义的 View 中实现这些方法，以实现在等待提示框处于不同状态时调整显示的样式。
         * 需要注意的是，默认等待提示框在加载中到完成/警告/错误等状态时，会有衔接过渡的过程，这个过程导致目标状态可能会延后几十到几百毫秒后才真正切换，因此有一个whenShowTick(Runnable runnable)接口需要实现，在衔接过程结束后执行runnable.run()即可，若无衔接过程，直接在重写后的方法中执行runnable.run()即可。
         * 在亮暗色模式切换后，等待提示框组件应该遵循颜色的变化调整颜色，这个颜色会从setColor(int color)接口中给出，注意此处的参数color是色值，并非资源值，建议直接赋值给画笔 Paint 进行绘制操作。
         * 如需参照 Demo，您可以查看 iOS 样式的 ProgressView 实现：ProgressView.java
         * <p>
         * //停止加载动画
         * noLoading();
         * <p>
         * //切换至完成状态
         * success();
         * <p>
         * //切换至警告状态
         * warning();
         * <p>
         * //切换至错误状态
         * error();
         * <p>
         * //切换至进度（取值0f-1f）
         * progress(floatprogress);
         * <p>
         * //切换至加载状态
         * loading();
         * <p>
         * //不同状态切换时，衔接动画完成后执行
         * ProgressViewInterface whenShowTick(Runnablerunnable);
         * <p>
         * //设置颜色
         * ProgressViewInterface setColor(intcolor);
         *
         * @param context 上下文
         * @param light   是否为亮色模式
         * @return ProgressViewInterface
         */
        public ProgressViewInterface overrideWaitView(Context context, boolean light) {
            return null;
        }
    }
    
    /**
     * 自定义底部对话框/菜单样式设置
     * <p>
     * <p>
     * overrideSelectionMenuBackgroundColor(boolean light)接口用于
     * <p>
     * selectionImageTint(boolean light)接口
     * <p>
     * overrideSelectionImage(boolean light, boolean isSelected)用于
     */
    public abstract class BottomDialogRes {
        
        //定义是否支持滑动关闭操作。
        public boolean touchSlide() {
            return false;
        }
        
        //用于设置底部对话框的布局，如需修改布局样式，请参照 底部对话框默认亮色布局 和 底部对话框默认暗色布局，请参照 Demo 布局的格式进行布局设计，不建议修改或去掉布局中的含有 id 的组件，当return 0时使用默认实现。
        public int overrideDialogLayout(boolean light) {
            return 0;
        }
        
        //用于修改默认分隔线的粗细，单位像素。
        public int overrideMenuDividerDrawableRes(boolean light) {
            return 0;
        }
        
        //修改默认分隔线的粗细，单位像素。
        public int overrideMenuDividerHeight(boolean light) {
            return 0;
        }
        
        //修改默认菜单文字的颜色，值采用为 color 的资源 ID。
        public int overrideMenuTextColor(boolean light) {
            return 0;
        }
        
        //设置默认情况下，当底部对话框内容大于屏幕可显示高度时，
        //默认启动后显示的高度比例，值为浮点型，例如设置为 0.6f 时，则当内容大于可显示高度时，启动后对话框只从屏幕底部弹出 0.6×屏幕高度的大小，需要再次向上拖拽才能展开全部对话框，
        //此功能需要和 touchSlide() 配合使用。
        public float overrideBottomDialogMaxHeight() {
            return 0;
        }
        
        /**
         * 定义菜单条目的布局样式。
         * 例如当使用 iOS 样式时，第一条菜单默认采用左上角和右上角都为圆角的样式，当显示菜单标题、正文或自定义布局时，
         * 则第一条菜单使用无圆角样式。当index == count - 1时为最后一个菜单，使用 iOS 样式时，
         * 最后一个菜单应该采用左下角和右下角都为圆角的样式。菜单的布局请参照 底部菜单亮色样式参考布局 和 底部菜单暗色样式参考布局 。
         * <p>
         * 此接口return 0时使用默认实现。
         *
         * @param light               判断亮/暗色模式
         * @param index               当前菜单项的索引值
         * @param count               菜单数量
         * @param isContentVisibility 确认当菜单显示时，是否还有其他内容显示（例如对话框标题、正文或自定义布局）
         * @return 条目布局资源
         */
        public int overrideMenuItemLayout(boolean light, int index, int count, boolean isContentVisibility) {
            return 0;
        }
        
        //定义已选中的菜单默认背景颜色，
        // 例如在使用 MIUI 主题样式且开启了单选模式时，默认打开菜单后会选中上次已选择的条目，此接口用预设定已选中菜单的背景颜色。
        public int overrideSelectionMenuBackgroundColor(boolean light) {
            return 0;
        }
        
        //用于确定使用此主题时，默认会不会重定义图标的颜色，
        // 若开启，那么所有菜单图标会根据主题的亮/暗色的文字颜色重新覆盖颜色，若关闭，则使用图标原本的颜色。
        public boolean selectionImageTint(boolean light) {
            return false;
        }
        
        //设置默认单选菜单已选择/未选择时的图标资源，可使用 mipmap 图像或者 drawable 资源。
        public int overrideSelectionImage(boolean light, boolean isSelected) {
            return 0;
        }
        
        //设置默认多选菜单已选择/未选择时的图标资源，可使用 mipmap 图像或者 drawable 资源。
        public int overrideMultiSelectionImage(boolean light, boolean isSelected) {
            return 0;
        }
    }
    
    /**
     * 自定义 PopTip 样式设置
     * <p>
     * 当return null 时使用默认样式。
     */
    public abstract static class PopTipSettings {
        
        //PopTip 的默认布局样式，请参考具体布局实现：PopTip 默认亮色布局 和 PopTip 默认暗色布局
        public int layout(boolean light) {
            return 0;
        }
        
        /**
         * align()接口用于判断 PopTip 的弹出规则，支持的值如下：
         */
        public ALIGN align() {
            return ALIGN.CENTER;
        }
        
        public enum ALIGN {
            CENTER,         //屏幕中央弹出
            TOP,            //屏幕顶端弹出（非安全区）
            BOTTOM,         //屏幕底部弹出（非安全区）
            TOP_INSIDE,     //屏幕顶端安全区内弹出
            BOTTOM_INSIDE   //屏幕底部安全区内弹出
        }
        
        //设置启动动画效果。
        public int enterAnimResId(boolean light) {
            return 0;
        }
        
        //设置关闭动画效果。
        public int exitAnimResId(boolean light) {
            return 0;
        }
        
        //使图标颜色和文字保持一致
        public boolean tintIcon(){
            return true;
        }
        
        //默认完成图标
        public int defaultIconSuccess(){
            return 0;
        }
    
        //默认警告图标
        public int defaultIconWarning(){
            return 0;
        }
    
        //默认错误图标
        public int defaultIconError(){
            return 0;
        }
    }
    
    public abstract class PopMenuSettings {
        
        //PopMenu 的默认布局样式，请参考具体布局实现：PopMenu 默认亮色布局 和 PopMenu 默认暗色布局
        public int layout(boolean light) {
            return 0;
        }
        
        public BlurBackgroundSetting blurBackgroundSettings() {
            return null;
        }
        
        public int backgroundMaskColorRes() {
            return 0;
        }
        
        //用于修改默认分隔线的粗细，单位像素。
        public int overrideMenuDividerDrawableRes(boolean light) {
            return 0;
        }
        
        //修改默认分隔线的粗细，单位像素。
        public int overrideMenuDividerHeight(boolean light) {
            return 0;
        }
        
        //修改默认菜单文字的颜色，值采用为 color 的资源 ID。
        public int overrideMenuTextColor(boolean light) {
            return 0;
        }
        
        //自定义菜单的布局资源 ID。
        public int overrideMenuItemLayoutRes(boolean light) {
            return 0;
        }
        
        /**
         * 定义菜单条目的布局背景资源。
         * 例如当使用 iOS 样式时，第一条菜单默认采用左上角和右上角都为圆角的样式，当显示菜单标题、正文或自定义布局时，
         * 则第一条菜单背景使用无圆角样式。当index == count - 1时为最后一个菜单，使用 iOS 样式时，
         * 最后一个菜单应该采用左下角和右下角都为圆角的背景样式。菜单的布局请参照 底部菜单亮色样式参考布局 和 底部菜单暗色样式参考布局 。
         * <p>
         * 此接口return 0时使用默认实现。
         *
         * @param light               判断亮/暗色模式
         * @param index               当前菜单项的索引值
         * @param count               菜单数量
         * @param isContentVisibility 确认当菜单显示时，是否还有其他内容显示（例如对话框标题、正文或自定义布局）
         * @return 条目布局背景资源
         */
        public int overrideMenuItemBackgroundRes(boolean light, int index, int count, boolean isContentVisibility) {
            return 0;
        }
        
        //定义已选中的菜单默认背景颜色，
        // 例如在使用 MIUI 主题样式且开启了单选模式时，默认打开菜单后会选中上次已选择的条目，此接口用预设定已选中菜单的背景颜色。
        public int overrideSelectionMenuBackgroundColor(boolean light) {
            return 0;
        }
        
        //用于确定使用此主题时，默认会不会重定义图标的颜色，
        // 若开启，那么所有菜单图标会根据主题的亮/暗色的文字颜色重新覆盖颜色，若关闭，则使用图标原本的颜色。
        public boolean selectionImageTint(boolean light) {
            return false;
        }
        
        //PopMenu 的顶部和底部的额外 padding
        public int paddingVertical() {
            return 0;
        }
    }
    
    /**
     * 自定义 PopNotificationSettings 样式设置
     * <p>
     * 当return null 时使用默认样式。
     */
    public abstract static class PopNotificationSettings {
        
        //PopNotificationSettings 的默认布局样式，请参考具体布局实现：PopNotificationSettings 默认亮色布局 和 PopNotificationSettings 默认暗色布局
        public int layout(boolean light) {
            return 0;
        }
        
        /**
         * align()接口用于判断 PopNotificationSettings 的弹出规则，支持的值如下：
         */
        public ALIGN align() {
            return ALIGN.CENTER;
        }
        
        public enum ALIGN {
            CENTER,         //屏幕中央弹出
            TOP,            //屏幕顶端弹出（非安全区）
            BOTTOM,         //屏幕底部弹出（非安全区）
            TOP_INSIDE,     //屏幕顶端安全区内弹出
            BOTTOM_INSIDE   //屏幕底部安全区内弹出
        }
        
        //设置启动动画效果。
        public int enterAnimResId(boolean light) {
            return 0;
        }
        
        //设置关闭动画效果。
        public int exitAnimResId(boolean light) {
            return 0;
        }
    
        //使图标颜色和文字保持一致
        public boolean tintIcon(){
            return true;
        }
    
        //默认完成图标
        public int defaultIconSuccess(){
            return 0;
        }
    
        //默认警告图标
        public int defaultIconWarning(){
            return 0;
        }
    
        //默认错误图标
        public int defaultIconError(){
            return 0;
        }
    
        public BlurBackgroundSetting blurBackgroundSettings() {
            return null;
        }
    }
}