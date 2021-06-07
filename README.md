<div align=center>    
    <img src="https://github.com/kongzue/DialogX/raw/master/readme/img_logo_dialogx.jpg" width="150">    
    <center><h1>Kongzue DialogX</h1></center> 
</div>



经过三代的更迭，全新的 Kongzue DialogX 已经到来，不仅仅保留了以往的优势，更带来了更灵活的扩展性和全新的流畅体验。

<a href="https://github.com/kongzue/dialogX/">
<img src="https://img.shields.io/badge/Kongzue%20DialogX-Beta-green.svg" alt="Kongzue Dialog">
</a> 
<a href="https://bintray.com/myzchh/maven/dialogX/">
<img src="https://img.shields.io/badge/jCenter-Beta-blue.svg" alt="Maven">
</a> 
<a href="https://jitpack.io/#kongzue/DialogX">
<img src="https://jitpack.io/v/kongzue/DialogX.svg" alt="Jitpack.io">
</a> 
<a href="http://www.apache.org/licenses/LICENSE-2.0">
<img src="https://img.shields.io/badge/License-Apache%202.0-red.svg" alt="License">
</a> 
<a href="http://www.kongzue.com">
<img src="https://img.shields.io/badge/Homepage-Kongzue.com-brightgreen.svg" alt="Homepage">
</a>

![DialogX](https://repository-images.githubusercontent.com/298764877/37386d80-8986-11eb-8ff2-1206da84cd8d)

# DialogX优势

对话框是一个软件对用户操作进行响应、反馈的重要组件，而 DialogX 将可以协助开发者快速完成这些事务。

我们力求减少开发者所需要担心的，所需要顾虑的，而打造一款可以在任意时间，任意情况都能轻松使用的对话框组件。

在轻松使用的前提下，DialogX 提供了更多的个性接口方便开发者进行扩展，包括在对话框内插入自定义布局，亮暗色模式的切换，甚至自定义更符合 App UI 的自定义主题。

### ✅DialogX的特性：

- DialogX 采用全新的实现方式，**不依赖** AlertDialog、Window 或 Fragment 实现，更加轻便快捷。
- DialogX 的启动与线程无关，你可以**在任意线程**启动 DialogX 而它都将自动在 UI 线程运行。
- DialogX 的启动**无需 context 参数**，默认提供静态方法一句代码实现对话框的启动，使用更加方便。
- 更自由，开发者可以轻松定制对话框中任何组件的样式，包括文本样式、按钮文字样式、菜单文本样式、输入文本样式，大到标题，小到提示消息都可以根据需要随意修改。
- DialogX 采用**主题分离设计**，默认自带 Material 主题，可选引入 IOS、Kongzue、MIUI 等其他风格主题，大大减小 App 体积，同时提供了主题接口，如有定制需求完全可以自行实现一套私有主题。
- 更低的耦合度，更少的问题，DialogX 可以在对话框正在**运行的过程中随意关闭 Activity** ，而无需担心以往 AlertDialog 等组件会引发的 WindowLeaked 错误。
- 更流畅的体验，DialogX 的动画效果更加丰富，对话框启动动画采用**非线性动画**实现，更自带连贯的等待提示到完成错误动画过渡效果，让你的 APP 更具动感。
- 所有主题默认支持亮暗色两种模式，只需一键配置即可实现亮暗色的对话框主题切换，更有自由的布局内容满足定制化需求，DialogX 也支持自动适应系统亮暗色模式切换，能够根据系统设置自动判断亮暗色显示效果的切换。
- 轻松的实现对话框的生命周期管控以及沉浸式适配。

# DialogX对话框

DialogX 包含以下对话框组件：

- [基础对话框 MessageDialog和 输入对话框 InputDialog](https://github.com/kongzue/DialogX/wiki/%E5%9F%BA%E7%A1%80%E5%AF%B9%E8%AF%9D%E6%A1%86-MessageDialog-%E5%92%8C-%E8%BE%93%E5%85%A5%E5%AF%B9%E8%AF%9D%E6%A1%86-InputDialog)

  ![基础对话框 MessageDialog和 输入对话框 InputDialog](https://github.com/kongzue/DialogX/raw/master/readme/messagedialog.png)

  基础对话框组件可以实现基本的对话框业务逻辑，包含标题、消息文本、单/双/三按钮的提醒功能，三个按钮可以按照纵向/横向进行显示，满足绝大部分日常阻断式提醒需求。

  输入对话框 InputDialog 是基础对话框的扩展组件，除了包含基础的功能外还提供了输入框，可自定义输入提示文本、输入文字样式和点击按钮后的输入内容回调等。

- [等待框 WaitDialog 和提示框 TipDialog](https://github.com/kongzue/DialogX/wiki/%E7%AD%89%E5%BE%85%E6%A1%86-WaitDialog-%E5%92%8C%E6%8F%90%E7%A4%BA%E6%A1%86-TipDialog)

  ![等待框 WaitDialog 和提示框 TipDialog](https://github.com/kongzue/DialogX/raw/master/readme/waitdialog.png)

  阻断式等待提示框，会显示基础的环形等待动画以及进度展示动画，它是单例的，这就意味着从等待状态 WaitDialog 切换到提示状态 TipDialog 是无缝的，你可以自由的选择在等待结束后显示成功/警告/错误三种状态的消息提示，动画的切换也会无缝衔接。

- [底部对话框 BottomDialog 和底部菜单 BottomMenu](https://github.com/kongzue/DialogX/wiki/%E5%BA%95%E9%83%A8%E5%AF%B9%E8%AF%9D%E6%A1%86-BottomDialog-%E5%92%8C%E5%BA%95%E9%83%A8%E8%8F%9C%E5%8D%95-BottomMenu)

  ![底部对话框 BottomDialog 和底部菜单 BottomMenu](https://github.com/kongzue/DialogX/raw/master/readme/bottomdialog.png)

  底部对话框 BottomDialog 提供从底部弹出显示的对话框样式，可设置标题、提示文本和自定义布局，使用 Material 主题时还会提供向下滑动关闭和向上滑动展开的功能。

  底部菜单 BottomMenu 则是底部对话框 BottomDialog 的扩展组件，在底部对话框的基础上额外提供了菜单功能，菜单可设置菜单内容/菜单图标/单选功能，在不同的主题下还可以提供“取消”关闭按钮

- [简单提示 PopTip](https://github.com/kongzue/DialogX/wiki/%E7%AE%80%E5%8D%95%E6%8F%90%E7%A4%BA-PopTip)

  ![简单提示 PopTip](https://github.com/kongzue/DialogX/raw/master/readme/poptip.png)

  提供一个类似 Toast 的文本提示功能，但它拥有更强大的自定义属性。你可以设置文本提示、图标、以及一个控制按钮，并可以设置持续显示或定义自动消失的时长。PopTip 是非阻断式提示，也就是说，在 PopTip 显示时用户依然可以操作界面。

- [全屏对话框 FullScreenDialog](https://github.com/kongzue/DialogX/wiki/%E5%85%A8%E5%B1%8F%E5%AF%B9%E8%AF%9D%E6%A1%86-FullScreenDialog)

  ![全屏对话框 FullScreenDialog](https://github.com/kongzue/DialogX/raw/master/readme/fullscreendialog.png)

  全屏对话框 FullScreenDialog 提供从底部弹出的对话框效果，类似 BottomDialog 但相比 BottomDialog 的定制化自由度更高。全屏对话框 FullScreenDialog 将不提供任何基础实现，开发者可以自定义实现布局。默认只提供一个默认的下划关闭逻辑和 Activity 背景下沉的显示效果。

- [自定义对话框 CustomDialog](https://github.com/kongzue/DialogX/wiki/%E8%87%AA%E5%AE%9A%E4%B9%89%E5%AF%B9%E8%AF%9D%E6%A1%86-CustomDialog)

  ![自定义对话框 CustomDialog](https://github.com/kongzue/DialogX/raw/master/readme/customdialog.png)

  根据定制化自由度的对话框组件，完全由用户自行实现布局内容。CustomDialog 提供了 ALIGN 选项可以轻松定制对话框弹出的方式，默认支持屏幕中央、屏幕底部、屏幕顶部、屏幕左侧、屏幕右侧多种弹出模式，也会提供相应的弹出动画效果，当然用户也可以自定义动画效果。

# DialogX主题

![DialogX主题](https://github.com/kongzue/DialogX/raw/master/readme/allstyle.png)

DialogX 采用了主题分离结构，主框架仅包含 Material 设计风格的对话框组件，您可以通过额外引入主题包来实现主题的扩展。

额外的，每套主题都包含亮色/暗色两种显示风格，您可以通过 DialogX 的设置自由切换对话框的显示效果。

主题设计开发者也可以通过使用 DialogX 提供的主题定制接口来实现自定义主题，或者对现有主题进行样式调整和修改。

你还可以更深入的 [了解如何使用 DialogX 主题](https://github.com/kongzue/DialogX/wiki/%E4%BD%BF%E7%94%A8%E5%85%B6%E4%BB%96-DialogX%E4%B8%BB%E9%A2%98)

你还可以更深入的 [了解如何开发 DialogX 主题](https://github.com/kongzue/DialogX/wiki/%E8%87%AA%E5%AE%9A%E4%B9%89-DialogX-%E4%B8%BB%E9%A2%98)

# Demo

您可以先下载 Demo 进行尝试：http://beta.kongzue.com/DialogXDemo

<div align=center><img src="https://github.com/kongzue/DialogX/raw/master/readme/download_demo_qrcode.png" alt="下载 DialogX Demo" width="250" height="250" /></div>

# 开始使用 DialogX

因为依赖的关系，DialogX 目前仅支持 AndroidX 作为基础进行开发，若您正在使用最新版本的 Android Studio，那么默认创建的项目就是使用 AndroidX 作为底层框架的，老版本 Android Support 兼容库将在后续更新。

**以下四种引入方式请任选其一。**

### 📥引入

#### 方式一：Gradle 引入  jitPack 源

<a href="https://jitpack.io/#kongzue/DialogX">
<img src="https://jitpack.io/v/kongzue/DialogX.svg" alt="Jitpack.io">
</a> 

1) 在 project 的 build.gradle 文件中找到 `allprojects{}` 代码块添加以下代码：

```
allprojects {
    repositories {
        google()
        jcenter()
        maven { url 'https://jitpack.io' }      //增加 jitPack Maven 仓库
    }
}
```

2) 在 app 的 build.gradle 文件中找到 `dependencies{}` 代码块，并在其中加入以下语句：

```
implementation 'com.github.kongzue.DialogX:DialogX:0.0.39'
```

#### 方式二：Gradle 引入 jCenter 源

⚠️警告：jCenter已停止运行！

想要在您的项目引入 DialogX，您需要在 app 的 build.gradle 文件中找到 `dependencies{}` 代码块，并在其中加入以下语句：

```
implementation 'com.kongzue.dialogx:DialogX:0.0.37'
```

#### 方式三：直接引入 AAR 包文件

请前往 [Release](https://github.com/kongzue/DialogX/releases) 页面根据需要版本的下载 AAR 包文件。

1) 将 AAR 放入 libs 目录。

2) 在 Module 的 build.gradle 里加入以下代码：

```
build.gradle
repositories{
    flatDir {
        dirs 'libs'
    }
}

implementation(name: 'AAR文件名', ext: 'aar')
```

#### 方式四：手动配置 Maven

```xml
<dependency>
  <groupId>com.kongzue.dialogx</groupId>
  <artifactId>DialogX</artifactId>
  <version>0.0.37</version>
  <type>pom</type>
</dependency>
```

### ▶️使用

具体的使用说明，请参阅 [DialogX Wiki](https://github.com/kongzue/DialogX/wiki/)

### ℹ️使用过程遇到问题？

查看 [常见问题](https://github.com/kongzue/DialogX/wiki/%E5%B8%B8%E8%A7%81%E9%97%AE%E9%A2%98)

技术支持和反馈建议可以加讨论群：590498789

<div align=center><img src="https://github.com/kongzue/DialogX/raw/master/readme/feedback_qq_qrcode.png" alt="反馈 DialogX" width="250" height="250" /></div>


### 🔁如何从 DialogV3 迁移至 DialogX

请参考文章 [从 DialogV3 迁移至 DialogX](https://github.com/kongzue/DialogX/wiki/%E4%BB%8E-DialogV3-%E8%BF%81%E7%A7%BB%E8%87%B3-DialogX)

# 开源协议

DialogX 遵循 Apache License 2.0 开源协议。

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