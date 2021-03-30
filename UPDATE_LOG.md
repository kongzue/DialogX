# 0.0.37.beta2
更新日志：

### 新增

几乎所有组件增加了单独重设置的操作，例如你可以直接使用：

```
WaitDialog.overrideEnterDuration = 100;
WaitDialog.overrideExitDuration = 100;
WaitDialog.overrideEnterAnimRes = R.anim.anim_dialogx_top_enter;
WaitDialog.overrideExitAnimRes = R.anim.anim_dialogx_top_exit;
```

这样的方法单独对某一个组件进行进出场动画的重写设置。

注意这些方法不会影响全局其他组件，只是单独设置单一组件的动画效果。

优先级为：实例使用方法设置 > 组件override设置 > 全局设置。

另外 FullScreenDialog 只提供动画时长设置，不提供动画资源设置（此组件只允许上下进出场动画）；

CustomDialog 额外新增了overrideMaskEnterAnimRes和overrideMaskExitAnimRes，可以覆盖背景遮罩的动画效果，设置为0时取消动画。

### 修复

BottomDialog 在 IOS 样式下“取消”按钮背景不模糊的问题；

CustomDialog Align 方法无效的问题；

OnBindView 绑定父布局时设置的 LayoutParams 存在不生效的问题；

