
[中文说明](#user-content-zh) | [English Doc](#user-content-en)


<h3 id="user-content-zh">中文说明</h3>

## FBB大指纹

* [1.FBB大指纹开发包说明](#GAA大指纹开发包说明)
* [2.二次开发说明](#二次开发说明)
  * [2.1 AndroidStudio工程配置说明](#AndroidStudio工程配置说明)
  * [2.2 AndroidManifest.xml配置说明](#AndroidManifest配置说明)
  * [2.3 接口说明](#接口说明)
  * [2.4 接口调用流程](#接口调用流程)
  * [2.5 接口调用案例](#接口调用案例)
* [3.二次开发问题汇总](#二次开发问题汇总)

### FBB大指纹开发包说明

   1.1 支持FBB指纹模块;

   1.2 FBB指纹功能占用了机器唯一的USB口

   1.3 FBB指纹开发包兼容机器: u3,u8,新370

   1.4 [FBB指纹开发包下载地址,请下载指定源码，当中有开发包](https://github.com/CoreWise/FBBFingerDemo/releases)

### 二次开发说明

   由于本机器的高通CPU只支持一个USB口,所以在使用GAA大指纹模块时，需要先调用USB管理类将USB切换到指纹模组,此时USB正常情况下不能用来充电、数据线通信等操作。
   在这样的情况下，USB数据线调试不能使用，建议网络adb调试;

   网络adb调试推荐Android Studio安装Android Wifi ADB插件;

#### AndroidStudio工程配置说明

- 1.添加开发包aar到项目libs目录下

- 2.配置Moudle的build.gradle,参考如下:


```
...
 android {
     ...
     defaultConfig {
         ...
         targetSdkVersion 22 //身份证功能必须降22，其他无所谓
         ...
     }
     ...
 }
 //2.必须2
 repositories {
     flatDir {
         dirs 'libs'   // aar目录
     }
 }

 dependencies {
     ...
    //FBB大指纹开发包(新固件开发包)
    //FBB FingerPrint SDK
    compile(name: 'fp_fbb_sdk_20190620', ext: 'aar')

 }

```

#### AndroidManifest配置说明


```xml

<uses-feature android:name="android.hardware.usb.host"
android:required="true" />

<uses-permission
android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>

<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>

```

#### 接口说明


**USB管理类**

| USBFingerManager API接口 | 接口说明 |
| :----- | :---- |
| USBFingerManager | USB管理类,单例模式 |
| USBFingerManager.getInstance(this) | 获取USB管理类的实例，单例模式 |
| USBFingerManager.getInstance(this).setDelayMs(ms) | 设置切到指纹模组后延时，默认1000ms |
| USBFingerManager.getInstance(this).openUSB(OnUSBFingerListener()) | 将USB切到指纹模块 |
| USBFingerManager.getInstance(this).closeUSB(); | 将USB切到正常模式 |

OnUSBFingerListener回调接口说明:
- onOpenUSBFingerSuccess(String device):

    切换USB到指纹模组成功，返回当前指纹模组的名称

- onOpenUSBFingerFailure(String error):

    切换USB到指纹模组失败，返回错误码



---

**FBB指纹类**

- ##### 打开并初始化指纹模块

![bigfingerusb.png](https://i.loli.net/2019/05/08/5cd24e0add367.png)

- ##### 指纹模组API文档说明

中英文开发包在源码根目录doc路径下。



### 二次开发问题汇总


---



<h3 id="user-content-en">English Doc</h3>

## FBB FingerPrint


* [1.FBB big fingerprint development kit description](#111)
* [2.Secondary development instructions](#112)
  * [2.1 AndroidStudio project configuration instructions](#113)
  * [2.2 AndroidManifest configuration instructions](#114)
  * [2.3 Interface Description](#115)
* [3.Summary of secondary development issues](#117)

<a name="111"></a>
### FBB big fingerprint development kit description

   1.1 Support FBB fingerprint module;

   1.2 FBB fingerprint function occupies the machine's unique USB port

   1.3 [FBB fingerprint development kit download address](https://github.com/CoreWise/FBBFingerDemo/releases)

<a name="112"></a>
### Secondary development instructions

   Since the Qualcomm CPU of this machine only supports one USB port, when using the GAA large fingerprint module, it is necessary to first call the USB management class to switch the USB to the fingerprint module. At this time, the USB cannot be used for charging, data line communication, etc. under normal circumstances. operating.
    In this case, USB data line debugging can not be used, it is recommended to debug the network adb;

   Network adb debugging recommended Android Studio to install Android Wifi ADB plugin;

<a name="113"></a>
#### AndroidStudio project configuration instructions

- 1.Add the development package aar to the project libs directory

- 2.Configure Moudle's build.gradle, as follows:


```
...
 android {
     ...
     defaultConfig {
         ...
         targetSdkVersion 22 //身份证功能必须降22，其他无所谓
         ...
     }
     ...
 }
 //2.必须2
 repositories {
     flatDir {
         dirs 'libs'   // aar目录
     }
 }

 dependencies {
     ...
    //FBB大指纹开发包(新固件开发包)
    //FBB FingerPrint SDK
    compile(name: 'fp_fbb_sdk_20190620', ext: 'aar')

 }

```
<a name="114"></a>
#### AndroidManifest configuration instructions


```xml

<uses-feature android:name="android.hardware.usb.host"
android:required="true" />

<uses-permission
android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>

<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>

```

<a name="115"></a>
#### Interface Description


**USB management class**

| USBFingerManager API | API Description |
| :----- | :---- |
| USBFingerManager | USB management class, singleton mode |
| USBFingerManager.getInstance(this) | Get an instance of the USB management class, singleton mode |
| USBFingerManager.getInstance(this).setDelayMs(ms) | Set the delay after cutting to the fingerprint module, the default is 1000ms |
| USBFingerManager.getInstance(this).openUSB(OnUSBFingerListener()) | Cut USB to fingerprint module |
| USBFingerManager.getInstance(this).closeUSB(); | Cut USB to normal mode |

OnUSBFingerListener Callback interface description:
- onOpenUSBFingerSuccess(String device):

    Switch USB to fingerprint module successfully, return the name of the current fingerprint module

- onOpenUSBFingerFailure(String error):

    Failed to switch USB to fingerprint module, return error code



---


**FBB FingerPrint**

- ##### Open and initialize the fingerprint module


![bigfingerusb.png](https://i.loli.net/2019/05/08/5cd24e0add367.png)


- ##### Fingerprint Module API Documentation Description

The Chinese and English development kits are in the source directory doc path.

<a name="117"></a>
### Summary of secondary development issues