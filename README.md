# Kongzue DialogX
DialogX 是全新的 Dialog 实现方式，**不**依赖 AlertDialog、Window 或 Fragment 实现，更加轻便快捷。

DialogX 无需依赖 context 启动对话框，使用更加方便。

DialogX 采用分离设计，默认自带 Material 主题，可选引入 IOS、Kongzue 等其他风格主题，大大减小 App 体积。

更低的耦合度，无论对话框是否正在显示，请肆意执行的 Activity 关闭逻辑，而无需担心引发 WindowLeaked 错误。

目前开发进度：53% [========== · · · · · · · · · · ]

## 开发计划

DialogX开发计划{
    MessageDialog                           √
    InputDialog(extends MessageDialog)      √
    WaitDialog                              √
    TipDialog(extends WaitDialog)           √
    BottomDialog                            ×
    FullScreenDialog                        ×
    CustomDialog                            ×
    PopTip                                  ×
}

即将被抛弃{
    ShareDialog
    Notification                            -
}

开发计划变化依据：DialogX不再提供具体功能实现的组件，因此ShareDialog将被抛弃，今后如果需要制作分享对话框则需要自行使用自定义布局实现。另外因为Toast的反射被限制，因此Notification可能也会被抛弃，PopTip则是新增的一个组件，用于非阻断式提示，类似于Toast但不可以跨界面显示，但提供了丰富的提示方式

# 开源协议
```
Copyright Kongzue DialogX

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```