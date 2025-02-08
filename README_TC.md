🌐 View [简体中文文档](https://github.com/kongzue/DialogX/blob/master/README.md) | [English Document](https://github.com/kongzue/DialogX/blob/master/README_EN.md)

<div align=center>    
    <img src="https://github.com/kongzue/DialogX/raw/master/readme/img_logo_dialogx.jpg" width="150">    
    <center><h1>Kongzue DialogX</h1></center> 
</div>

一款簡單易用的對話框組件，相比原生對話框使用體驗更佳，可自訂程度更高，擴展性更強，輕鬆實現各種對話框、菜單和提示效果，更有iOS、MIUI、Material You等主題擴展可選。

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

# DialogX優勢

對話框是軟件與用戶互動的關鍵部分，DialogX 讓開發者輕鬆搞定這一切，我們致力於打造一款無論何時何地都能便捷使用的對話框組件，讓開發者少操心、多做事，DialogX 不僅易用，還提供了豐富的個性化接口，比如自定義佈局、日夜模式切換和專屬 App 主題的創建

### ✅ DialogX 亮點

- **多樣實現**：DialogX 默認使用更輕便的 View 實現方式，也可選 Window 或 DialogFragment 等其他實現模式，靈活自由。
- **線程無憂**：在任何線程啟動DialogX，它都會自動在 UI 線程裏運行，無需擔心線程問題。
- **一鍵啟動**：無需 context 參數，簡單一行代碼就能啟動對話框，超級方便。
- **樣式自定義**：從標題到提示，所有組件樣式都可自定義，包括文本、按鈕、菜單和輸入框或者插入任何自定義佈局。
- **主題多樣**：自帶Material主題，可選額外添加 Material You、iOS、Kongzue、MIUI 等主題風格，還支持自定義主題，減小 App 體積。
- **低耦合度**：運行時關閉 Activity 也不怕，DialogX 能更自動銷毀避免 WindowLeaked 等內存洩漏錯誤。
- **動畫豐富**：採用非線性動畫，還有流暢的等待到錯誤動畫過渡，讓APP更生動。
- **亮暗模式**：一鍵切換亮暗主題，自動適應系統亮暗模式，滿足訂製化需求。
- **生命週期與沉浸式**：輕鬆管理對話框生命週期，完美適配沉浸式體驗。
- **穿透能力**：“臨時儲物櫃”數據穿透和“快捷功能鍵”動作穿透兩大幫手助你輕鬆實現數據傳遞和靈活控制，安全又放心。

# DialogX對話框

DialogX 包含以下對話框組件：

- [基礎對話框 MessageDialog和 輸入對話框 InputDialog](https://github.com/kongzue/DialogX/wiki/MessageDialog&InputDialog_tc)

  ![基礎對話框 MessageDialog和 輸入對話框 InputDialog](https://github.com/kongzue/DialogX/raw/master/readme/messagedialog.png)

  基礎對話框組件可以實現基本的對話框業務邏輯，包含標題、消息文本、單/雙/三按鈕的提醒功能，三個按鈕可以按照縱向/橫向進行顯示，滿足絕大部分日常阻斷式提醒需求。

  輸入對話框 InputDialog 是基礎對話框的擴展組件，除了包含基礎的功能外還提供了輸入框，可自訂輸入提示文本、輸入文字樣式和點擊按鈕後的輸入內容回調等。

- [等待框 WaitDialog 和提示框 TipDialog](https://github.com/kongzue/DialogX/wiki/WaitDialog&TipDialog_tc)

  ![等待框 WaitDialog 和提示框 TipDialog](https://github.com/kongzue/DialogX/raw/master/readme/waitdialog.png)

  阻斷式等待提示框，會顯示基礎的環形等待動畫以及進度展示動畫，它是單例的，這就意味著從等待狀態 WaitDialog 切換到提示狀態 TipDialog 是無縫的，你可以自由的選擇在等待結束後顯示成功/警告/錯誤三種狀態的消息提示，動畫的切換也會無縫銜接。

- [底部對話框 BottomDialog 和底部選單 BottomMenu](https://github.com/kongzue/DialogX/wiki/BottomDialog&BottomMenu_tc)

  ![底部對話框 BottomDialog 和底部選單 BottomMenu](https://github.com/kongzue/DialogX/raw/master/readme/bottomdialog.png)

  底部對話框 BottomDialog 提供從底部彈出顯示的對話框樣式，可設置標題、提示文本和自訂布局，使用 Material 主題時還會提供向下滑動關閉和向上滑動展開的功能。

  底部選單 BottomMenu 則是底部對話框 BottomDialog 的擴展組件，在底部對話框的基礎上額外提供了菜單功能，菜單可設置菜單內容/菜單圖示/單選功能，在不同的主題下還可以提供“取消”關閉按鈕

- [簡單提示 PopTip](https://github.com/kongzue/DialogX/wiki/PopTip_tc)

  ![簡單提示 PopTip](https://github.com/kongzue/DialogX/raw/master/readme/poptip.png)

  提供一個類似 Toast 的文本提示功能，但它擁有更強大的自訂屬性。你可以設置文本提示、圖示、以及一個控制按鈕，並可以設置持續顯示或定義自動消失的時長。PopTip 是非阻斷式提示，也就是說，在 PopTip 顯示時用戶依然可以操作界面。
  
- [簡單通知提示 PopNotification](https://github.com/kongzue/DialogX/wiki/PopNotification_tc)

  ![簡單通知提示 PopNotification](https://github.com/kongzue/DialogX/raw/master/readme/popnotification.png)

  提供一個類似 Notification 的通知樣式提示功能，請注意，此組件並不能取代 Notification，默認不支持不能跨界面顯示（可使用懸浮窗權限設定允許），僅用於應用內通知提示，擁有更強大的自訂屬性。你可以設置文本提示、圖示、以及一個控制按鈕，並可以設置持續顯示或定義自動消失的時長。PopNotification 是非阻斷式提示，也就是說，在 PopNotification 顯示時用戶依然可以操作界面。

- [全螢幕對話框 FullScreenDialog](https://github.com/kongzue/DialogX/wiki/FullScreenDialog_tc)

  ![全螢幕對話框 FullScreenDialog](https://github.com/kongzue/DialogX/raw/master/readme/fullscreendialog.png)

  全螢幕對話框 FullScreenDialog 提供從底部彈出的對話框效果，類似 BottomDialog 但相比 BottomDialog 的訂製化自由度更高。全螢幕對話框 FullScreenDialog 將不提供任何基礎實現，開發者可以自訂實現布局。默認只提供一個預設的下劃關閉邏輯和 Activity 背景下沉的顯示效果。

- [自訂對話框 CustomDialog](https://github.com/kongzue/DialogX/wiki/CustomDialog_tc)

  ![自訂對話框 CustomDialog](https://github.com/kongzue/DialogX/raw/master/readme/customdialog.png)

  根據訂製化自由度的對話框組件，完全由用戶自行實現布局內容。CustomDialog 提供了 ALIGN 選項可以輕鬆訂製對話框彈出的方式，默認支持螢幕中央、螢幕底部、螢幕頂部、螢幕左側、螢幕右側多種彈出模式，也會提供相應的彈出動畫效果，當然用戶也可以自訂動畫效果。

- [引導對話框 GuideDialog](https://github.com/kongzue/DialogX/wiki/GuideDialog_tc)
  
  ![引導對話框 GuideDialog](https://github.com/kongzue/DialogX/raw/master/readme/guidedialog.png)

可以實現一個遮罩展示操作引導圖，或者對按鈕進行操作提示指引。GuideDialog 可以圍繞一個界面上的組件顯示，並實現舞台光的效果，舞台光可選圓形（外圍、內側）、方形（外圍、內側）和矩形模式，方形和矩形可設置圓角。

# DialogX主題

![DialogX主題](https://github.com/kongzue/DialogX/raw/master/readme/allstyle.png)

DialogX 採用了主題分離結構，主框架僅包含 Material 設計風格的對話框組件，您可以透過額外引入主題包來實現主題的擴展。

額外的，每套主題都包含亮色/暗色兩種顯示風格，您可以通過 DialogX 的設置自由切換對話框的顯示效果。

主題設計開發者也可以透過使用 DialogX 提供的主題訂製介面來實現自訂主題，或者對現有主題進行樣式調整和修改。

你還可以更深入的 [了解如何使用 DialogX 主題](https://github.com/kongzue/DialogX/wiki/Styles_tc)

你還可以更深入的 [了解如何開發 DialogX 主題](https://github.com/kongzue/DialogX/wiki/CustomThemeStyle_tc)

# Demo

您可以先下載 Demo 進行嘗試：http://beta.kongzue.com/DialogXDemo

[![下載Demo](https://github.com/kongzue/DialogX/raw/master/readme/download_demo_img.png)](http://beta.kongzue.com/DialogXDemo)

# 開始使用 DialogX

因為依賴的關係，DialogX 目前僅支持 AndroidX 作為基礎進行開發，若您正在使用最新版本的 Android Studio，那麼默認創建的項目就是使用 AndroidX 作為底層框架的，老版本 Android Support 相容庫將在後續更新。

### 📥引入

請從以下兩個源二選一引入項目。

#### MavenCentral 源

<div>
最新版本：
<a href="https://central.sonatype.dev/artifact/com.kongzue.dialogx/DialogX/0.0.49"><img src="https://img.shields.io/maven-central/v/com.kongzue.dialogx/DialogX" alt="DialogX Release"></a></div>

1) 在 project 的 build.gradle 文件中找到 `allprojects{}` 代碼塊添加以下代碼：

```
allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()      //增加 mavenCentral 倉庫
    }
}
```

⚠️請注意，使用 Android Studio 北極狐版本（Arctic Fox）創建的項目，需要您前往 settings.gradle 添加上述 jitpack 倉庫配置。

2) 在 app 的 build.gradle 文件中找到 `dependencies{}` 代碼塊，並在其中加入以下語句：

```
def dialogx_version = "0.0.49"
implementation "com.kongzue.dialogx:DialogX:${dialogx_version}"
```

#### Jitpack 源

<b>最新版本：</b>
<a href="https://jitpack.io/#kongzue/DialogX"><img src="https://jitpack.io/v/kongzue/DialogX.svg" alt="Jitpack.io"></a> <a href="https://github.com/kongzue/DialogX/releases"><img src="https://img.shields.io/github/v/release/kongzue/DialogX?color=green" alt="查看最新编译版本"></a>
</div>

1) 在 project 的 build.gradle 文件中找到 `allprojects{}` 代碼塊添加以下代碼：

```
allprojects {
    repositories {
        google()
        jcenter()
        maven { url 'https://jitpack.io' }      //增加 jitPack Maven 倉庫
    }
}
```

⚠️請注意，使用 Android Studio 北極狐版本（Arctic Fox）創建的項目，需要您前往 settings.gradle 添加上述 jitpack 倉庫配置。

2) 在 app 的 build.gradle 文件中找到 `dependencies{}` 代碼塊，並在其中加入以下語句：

```
def dialogx_version = "0.0.49"
implementation "com.github.kongzue.DialogX:DialogX:${dialogx_version}"
```

### ▶️使用

<a href="https://github.com/kongzue/DialogX/wiki/"><img src="https://github.com/kongzue/DialogX/raw/master/readme/img_how_to_use_tip.png" alt="如何使用" width="450" height="280" /></a>

具體的使用說明，請參閱 [DialogX Wiki](https://github.com/kongzue/DialogX/wiki/home_tc)

### 🧩 擴展包

目前 DialogX 依然僅提供最基礎的對話框實現，不提供進階的功能模組，這是為了避免是您的應用變得臃腫。

但為了保證一些常用功能，例如 地址選擇、日期選擇以及“分享到”對話框等較為通用且常見的功能更為簡單的能夠使用，我們提供了擴展包以滿足這些需求。

擴展包中，各個模組是單獨引入的，您無需擔心引入不必要的功能和資源。

擴展包目前尚處於初步開發階段，要預覽或提出你的建議，請訪問：[DialogXSample](https://github.com/kongzue/DialogXSample)

[![DialogXSample](https://github.com/kongzue/DialogXSample/raw/master/img_sample.png)](https://github.com/kongzue/DialogXSample)

### ℹ️使用過程遇到問題？

查看 [常見問題](https://github.com/kongzue/DialogX/wiki/FAQ_tc)

技術支援和回饋建議可以加討論群：590498789

<div align=center><img src="https://github.com/kongzue/DialogX/raw/master/readme/feedback_qq_qrcode.png" alt="回饋 DialogX" width="250" height="250" /></div>

### ❤️Powered By DialogX

[![Powered By DialogX](https://github.com/kongzue/DialogX/raw/master/readme/img_powered_by_dialogx.jpg)](https://github.com/kongzue/DialogX/wiki/%E2%9D%A4%EF%B8%8FPowered-By-DialogX)
 
🚀 [更多 >](https://github.com/kongzue/DialogX/wiki/❤️Powered-By-DialogX)

### 🔁如何從 DialogV3 遷移至 DialogX

請參考文章 [從 DialogV3 遷移至 DialogX](https://github.com/kongzue/DialogX/wiki/%E4%BB%8E-DialogV3-%E8%BF%81%E7%A7%BB%E8%87%B3-DialogX)


# ⭐觀星者

[![Stargazers over time](https://starchart.cc/kongzue/DialogX.svg?c=3)](https://github.com/kongzue/DialogX/stargazers)


# 開源協議

DialogX 遵循 Apache License 2.0 開源協議。

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

# 貢獻者
感謝所有為 DialogX 做出貢獻的人！

如果 DialogX 幫助您更好的構建了您的軟體，請為 DialogX 點一個小小的 Star，您的每一次點擊對 DialogX 都是最大的支持！

[![Stargazers repo roster for @kongzue/DialogX](https://reporoster.com/stars/kongzue/DialogX)](https://github.com/kongzue/DialogX/stargazers)

### 協助開發

<a href="https://github.com/kongzue/DialogX/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=kongzue/DialogX" />
</a>
