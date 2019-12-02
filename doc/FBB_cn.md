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

    //SerialPort SDK,需要串口管理类中的USB管理类
    compile(name: 'serialport_sdk_20190702', ext: 'aar')

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

- onOpenUSBFingerSuccess(String device, UsbManager mUsbManager, UsbDevice mDevice):

    切换USB到指纹模组成功，返回当前指纹模组的名称
    device:返回当前指纹模组型号
    mUsbManager:返回UsbManager管理实例
    mDevice:返回UsbDevice实例

- onOpenUSBFingerFailure(String error):

    切换USB到指纹模组失败，返回错误码



---

**FBB指纹类**

- TrustFinger类

| FBB FingerPrint API接口 | 接口说明 |
| :----- | :---- |
| getInstance(Context context) | 获取TrustFinger 类的单实例对象 |
|initialize()|初始化SDK 运行环境，通常在应用程序进程开始时调用此API，并且只需在释
放设备之前调用一次|
|release()|释放初始化时申请的资源|
|openDevice(int, DeviceOpenListener)||
|getSdkVersion()||
|getDeviceList()||
|setDeviceListenner(DeviceListener)||




- TrustFingerDevice类

| FBB FingerPrint API接口 | 接口说明 |
| :----- | :---- |
| getImageInfo() |  |
|getDeviceDescription()||
|captureRawData()||
|captureRawData(long)||
|captureRawDataLfd(int [])||
|captureBmpData()||
|captureBmpDataLfd(int[])||
|captureISOData(FingerPosition,ImgCompressAlg,int[])||
|captureANSIData(FingerPosition,ImgCompressAlg,int[])||
|captureANSIData(FingerPosition,ImgCompressAlg)||
|captureANSIDataLfd(FingerPosition,ImgCompressAlg,int[])||
|captureWSQData()||
|captureWSQDataLfd(int[])||
|extractFeature(byte[], FingerPosition)||
|extractANSIFeature(byte[], FingerPosition)||
|extractISOFeature(byte[], FingerPosition)||
|generalizeTemplate(byte[], byte[], byte[])||
|verify(SecurityLevel, byte[], byte[])||
|rawToBmp(byte[], int, int, int)||
|bmpToRaw(byte[])||
|rawToWsq(byte[], int, int, int)||
|rawToANSI(byte[], int, int, int, int, int)||
|rawToISO(byte[], int, int, int, int, int)||
|rawDataQuality(byte[])||
|bmpDataQuality(byte[])||
|close()||



- VerifyResult类

| 属性 | 说明 |
| :----- | :---- |
| int error | 错误代码。0 表示操作成功，其它表示操作失败 |
|int similarity|相似度|
|boolean isMatched|是否匹配|


- ScannerImageInfo类

| 属性 | 说明 |
| :----- | :---- |
| int width | 图像宽度 |
|int height|图像高度|
|int resolution|图像分辨率|

- DeviceDesciption 类



- DeviceOpenListener 接口



- DeviceListener 接口

- ImgComCompressAlg枚举类型

- LedIndex枚举类型

- LedStatus枚举类型

- SecurityLevel枚举类型

- LfdLevel枚举类型

- LfdStatus枚举类型

- FingerPosition枚举类型

- TrustFingerException.Type枚举类型

#### 接口调用案例

参考Demo源码

### 二次开发问题汇总

- 指纹类的应用在横竖屏切换时,如果重复初始化模块导致出现的一系列问题？

    ```
    在清单文件里相应的Activity标签下添加一下内容:
    android:configChanges="orientation|keyboardHidden|screenSize"


    ```