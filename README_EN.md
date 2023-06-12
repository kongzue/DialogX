🌐 View [简体中文文档](https://github.com/kongzue/DialogX/blob/master/README.md) | [繁體中文文檔](https://github.com/kongzue/DialogX/blob/master/README_TC.md)

<div align=center>    
    <img src="https://github.com/kongzue/DialogX/raw/master/readme/img_logo_dialogx.jpg" width="150">    
    <center><h1>Kongzue DialogX</h1></center> 
</div>

An easy-to-use dialog box component with better experience than native dialog box, more customizable, more scalable, easy to achieve a variety of dialog boxes, menus and hint effects, more iOS, MIUI, Material You and other theme extensions are available.

<div align=center>    
  <a href="https://github.com/kongzue/dialogX/">
    <img src="https://img.shields.io/badge/Kongzue%20DialogX-Release-green.svg" alt="Kongzue Dialog">
  </a> 
  <a href="https://github.com/kongzue/DialogX/releases">
    <img src="https://img.shields.io/github/v/release/kongzue/DialogX?color=green" alt="Maven">
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
</div>

![DialogX](https://user-images.githubusercontent.com/10115359/229279713-79c43a63-1a0a-4f70-851f-0a0783b94b45.jpg)

# Advantages of DialogX

DialogX is an important component of software that responds and gives feedback to user actions, and DialogX will help developers to do this quickly.

We have tried to reduce the amount of worries and concerns developers have, and create a dialog component that can be used easily at any time and in any situation.

On the premise of ease of use, DialogX provides more personalized interfaces for developers to extend, including inserting custom layouts inside dialogs, switching between light and dark color modes, and even customizing custom themes to better fit the App UI.

### ✅Features of DialogX

- DialogX adopts a new implementation, the default View implementation is lighter, and you can also choose Window or DialogFragment implementation for freedom and flexibility.
- DialogX is thread-independent, you can **start DialogX in any thread** and it will automatically run in the UI thread.
- DialogX can be started **without context parameter** and provides a static method to start the dialog by default, which is more convenient to use.
- More freedom, developers can easily customize the style of any component in the dialog box, including text style, button text style, menu text style, input text style, from large to title, small to prompt message can be modified as needed.
- DialogX adopts **theme separation design**, which comes with Material theme by default and can optionally introduce other style themes such as IOS, Kongzue, MIUI, etc., greatly reducing the size of the App, while providing a theme interface, so you can fully implement a set of private themes if you have customization needs.
- Lower coupling, less problems, DialogX allows you to **close the Activity while the dialog is running** without worrying about the WindowLeaked error that components like AlertDialog used to raise.
- DialogX is richer in animation effects, the dialog start animation is realized by **non-linear animation**, and it comes with a coherent transition effect from waiting prompt to completion error animation, making your APP more dynamic.
- All themes support both light and dark modes by default, just one click configuration can realize light and dark dialog box theme switching, more free layout content to meet the customization needs, DialogX also supports automatic adaptation to the system light and dark color mode switching, can automatically determine the light and dark color display effect switching according to the system settings.
- It is easy to realize the lifecycle control and immersive adaptation of dialogs.

# DialogX

DialogX contains the following dialog components：

- [MessageDialog & InputDialog](https://github.com/kongzue/DialogX/wiki/%E5%9F%BA%E7%A1%80%E5%AF%B9%E8%AF%9D%E6%A1%86-MessageDialog-%E5%92%8C-%E8%BE%93%E5%85%A5%E5%AF%B9%E8%AF%9D%E6%A1%86-InputDialog)

  ![基础对话框 MessageDialog和 输入对话框 InputDialog](https://github.com/kongzue/DialogX/raw/master/readme/messagedialog.png)

  The basic dialog box component can realize the basic dialog box business logic, including the title, message text, single/double/triple button reminder function, and the three buttons can be displayed vertically/horizontally to meet most of the daily blocking reminder requirements.

  InputDialog is an extension component of the basic dialog box, which not only contains the basic functions but also provides input box, customizable input prompt text, input text style and callback of input content after clicking the button.

- [WaitDialog & TipDialog](https://github.com/kongzue/DialogX/wiki/%E7%AD%89%E5%BE%85%E6%A1%86-WaitDialog-%E5%92%8C%E6%8F%90%E7%A4%BA%E6%A1%86-TipDialog)

  ![等待框 WaitDialog 和提示框 TipDialog](https://github.com/kongzue/DialogX/raw/master/readme/waitdialog.png)

  The blocking wait dialog, which displays the basic circular wait animation as well as the progress display animation, is single-instance, which means that switching from the wait state (WaitDialog) to the tip state (TipDialog) is seamless, and you can freely choose to display three states of success/warning/error message alerts at the end of the wait, and the animation switching will be seamless.

- [BottomDialog & BottomMenu](https://github.com/kongzue/DialogX/wiki/%E5%BA%95%E9%83%A8%E5%AF%B9%E8%AF%9D%E6%A1%86-BottomDialog-%E5%92%8C%E5%BA%95%E9%83%A8%E8%8F%9C%E5%8D%95-BottomMenu)

  ![底部对话框 BottomDialog 和底部菜单 BottomMenu](https://github.com/kongzue/DialogX/raw/master/readme/bottomdialog.png)

  BottomDialog provides a pop-up dialog style from the bottom, with title, prompt text and custom layout.

  BottomMenu is an extension of BottomDialog, which provides additional menu function on top of BottomDialog, the menu can set menu content/menu icon/select function, and can also provide "Cancel" close button under different themes.

- [PopTip](https://github.com/kongzue/DialogX/wiki/%E7%AE%80%E5%8D%95%E6%8F%90%E7%A4%BA-PopTip)

  ![简单提示 PopTip](https://github.com/kongzue/DialogX/raw/master/readme/poptip.png)

  Provides a Toast-like text prompt feature, but with more powerful customization properties. You can set text tips, icons, and a control button, and you can set a continuous display or define the length of time it will automatically disappear. popTip is a non-blocking tip, which means that the user can still operate the interface while popTip is displayed.
  
- [PopNotification](https://github.com/kongzue/DialogX/wiki/%E7%AE%80%E5%8D%95%E9%80%9A%E7%9F%A5%E6%8F%90%E7%A4%BA-PopNotification)

  ![简单通知提示 PopNotification](https://github.com/kongzue/DialogX/raw/master/readme/popnotification.png)

  This component is not a replacement for Notification, does not support cross-platform display by default (you can use the hover permission to allow it), and is only used for in-app notification alerts, with more powerful customization properties. PopNotification is a non-blocking alert, meaning that the user can still operate the interface while PopNotification is displayed.

- [FullScreenDialog](https://github.com/kongzue/DialogX/wiki/%E5%85%A8%E5%B1%8F%E5%AF%B9%E8%AF%9D%E6%A1%86-FullScreenDialog)

  ![全屏对话框 FullScreenDialog](https://github.com/kongzue/DialogX/raw/master/readme/fullscreendialog.png)

  FullScreenDialog provides a pop-up effect from the bottom, similar to BottomDialog but with more freedom of customization than BottomDialog. FullScreenDialog will not provide any base implementation, developers can customize the layout. By default, it will only provide a default down-close logic and a sunken Activity background display effect.

- [CustomDialog](https://github.com/kongzue/DialogX/wiki/%E8%87%AA%E5%AE%9A%E4%B9%89%E5%AF%B9%E8%AF%9D%E6%A1%86-CustomDialog)

  ![自定义对话框 CustomDialog](https://github.com/kongzue/DialogX/raw/master/readme/customdialog.png)

  CustomDialog provides ALIGN option to easily customize the pop-up mode of the dialog box, which supports multiple pop-up modes of screen center, screen bottom, screen top, screen left and screen right by default, and also provides corresponding pop-up animation effect, of course, users can also customize the animation effect.

- [GuideDialog](https://github.com/kongzue/DialogX/wiki/%E5%BC%95%E5%AF%BC%E5%AF%B9%E8%AF%9D%E6%A1%86-GuideDialog(Beta))
  
  ![引導對話框 GuideDialog](https://github.com/kongzue/DialogX/raw/master/readme/guidedialog.png)

A mask can be implemented to show the operation guide diagram, or to guide the operation tips for buttons. GuideDialog can be displayed around a component on the interface and realize the effect of stage light. Stage light can be selected from round (outer, inner), square (outer, inner) and rectangle modes, and square and rectangle can be set with rounded corners.

# Themes

![DialogX主题](https://github.com/kongzue/DialogX/raw/master/readme/allstyle.png)

DialogX adopts a theme separation structure, the main framework contains only Material design style dialog components, you can extend the themes by introducing additional theme packages.

In addition, each set of themes contains two display styles: light/dark, and you can freely switch the display of dialogs through DialogX's settings.

Theme design developers can also use the theme customization interface provided by DialogX to implement custom themes, or to style and modify existing themes.

You can also go deeper [Learn how to use the DialogX theme](https://github.com/kongzue/DialogX/wiki/%E4%BD%BF%E7%94%A8%E5%85%B6%E4%BB%96-DialogX%E4%B8%BB%E9%A2%98)

You can also go deeper [Learn how to make a DialogX theme](https://github.com/kongzue/DialogX/wiki/%E8%87%AA%E5%AE%9A%E4%B9%89-DialogX-%E4%B8%BB%E9%A2%98)

# Demo

You can try it by downloading the demo first: http://beta.kongzue.com/DialogXDemo

[![下载Demo](https://github.com/kongzue/DialogX/raw/master/readme/download_demo_img.png)](http://beta.kongzue.com/DialogXDemo)

# Start using DialogX

Because of dependencies, DialogX currently only supports AndroidX as the base for development. If you are using the latest version of Android Studio, the default project will be created using AndroidX as the underlying framework.

### 📥Introduce

Please choose one of the following two sources to bring in your project.

#### MavenCentral 

<div>
Latest version: 
<a href="https://central.sonatype.dev/artifact/com.kongzue.dialogx/DialogX/0.0.48"><img src="https://img.shields.io/maven-central/v/com.kongzue.dialogx/DialogX" alt="DialogX Release"></a></div>

1) In the project's build.gradle file, find the `allprojects{}` block and add the following code.

```
allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()      //增加 mavenCentral 仓库
    }
}
```

⚠️ Please note that projects created with the Arctic Fox version of Android Studio will require you to go to settings.gradle and add the above jitpack repository configuration.

2) Find the `dependencies{}` code block in the app's build.gradle file and add the following statement to it.

```
def dialogx_version = "0.0.48"
implementation "com.kongzue.dialogx:DialogX:${dialogx_version}"
```

#### Jitpack

<div>
<b>Latest Version：</b>
<a href="https://jitpack.io/#kongzue/DialogX">
<img src="https://jitpack.io/v/kongzue/DialogX.svg" alt="Jitpack.io">
</a> 
</div>

<b>Latest Version: </b>
<a href="https://jitpack.io/#kongzue/DialogX"><img src="https://jitpack.io/v/kongzue/DialogX.svg" alt="Jitpack.io"></a> <a href="https://github.com/kongzue/DialogX/releases"><img src="https://img.shields.io/github/v/release/kongzue/DialogX?color=green" alt="Latest Version"></a>
</div>

1) In the project's build.gradle file, find the `allprojects{}` block and add the following code.

```
allprojects {
    repositories {
        google()
        jcenter()
        maven { url 'https://jitpack.io' }      //add jitPack Maven
    }
}
```

⚠️Please note that projects created with the Arctic Fox version of Android Studio require you to go to settings.gradle to add the above jitpack repository configuration.

2) Find the `dependencies{}` block in the app's build.gradle file, and add the following statement to it：

```
def dialogx_version = "0.0.48"
implementation "com.github.kongzue.DialogX:DialogX:${dialogx_version}"
```

### ▶️How to use

For specific instructions, see [DialogX Wiki](https://github.com/kongzue/DialogX/wiki/)

### 🧩 Expansion Pack

Currently DialogX still provides only the most basic dialog implementation and no advanced functional modules, in order to avoid bloating your application.

However, in order to ensure that some common features, such as address selection, date selection and "share to" dialogs, are simpler to use, we provide extensions to meet these needs.

Each module is introduced separately in the extension package, so you don't need to worry about introducing unnecessary features and resources.

The extension package is currently in the preliminary development stage, to preview or make your suggestions, please visit: [DialogXSample](https://github.com/kongzue/DialogXSample)

[![DialogXSample](https://github.com/kongzue/DialogXSample/raw/master/img_sample.png)](https://github.com/kongzue/DialogXSample)

### ℹ️Having trouble using it?

View [FAQ](https://github.com/kongzue/DialogX/wiki/%E5%B8%B8%E8%A7%81%E9%97%AE%E9%A2%98)

### ❤️Powered By DialogX

[![Powered By DialogX](https://github.com/kongzue/DialogX/raw/master/readme/img_powered_by_dialogx.jpg)](https://github.com/kongzue/DialogX/wiki/%E2%9D%A4%EF%B8%8FPowered-By-DialogX)

🚀 [More >](https://github.com/kongzue/DialogX/wiki/❤️Powered-By-DialogX)

### 🔁How to Migrate from DialogV3 to DialogX

Please refer to the article [Migrating from DialogV3 to DialogX](https://github.com/kongzue/DialogX/wiki/%E4%BB%8E-DialogV3-%E8%BF%81%E7%A7%BB%E8%87%B3-DialogX)


# ⭐Stargazer

[![Stargazers over time](https://starchart.cc/kongzue/DialogX.svg?b=2)](https://github.com/kongzue/DialogX/stargazers)


# License

DialogX follows the Apache License 2.0 open source license.

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

# Contributors
Thank you to all who have contributed to DialogX!

If DialogX has helped you build your software better, please give DialogX a Star, every click you make is a great support for DialogX!

[![Stargazers repo roster for @kongzue/DialogX](https://reporoster.com/stars/kongzue/DialogX)](https://github.com/kongzue/DialogX/stargazers)

### Assist in development

<a href="https://github.com/kongzue/DialogX/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=kongzue/DialogX" />
</a>
