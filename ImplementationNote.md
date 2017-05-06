# SecurityGuard

#### 本项目来自“传智播客”教程项目，实现了一个简易版本的“手机安全卫士”，主要包含以下几个功能模块：欢迎界面模块、手机防盗模块、通讯卫士模块、软件管家模块、手机杀毒模块、缓存清理模块、进程管理模块、流量统计模块、高级工具模块和设置中心模块。

本文档主要介绍本项目中出现的一些知识点和关键技术。

## 一、 欢迎界面模块

欢迎界面模块主要用于程序Logo以及版本信息的显示，如果服务器的版本号与本地版本号一致，则直接进入主界面，否则会弹出提示信息询问是否升级。如果点击“立即升级”，则会从服务器下载最新版本的APP，下载完成后直接进行安装，否则直接进入主界面。

相关包：欢迎界面splash和主界面home

#### 1. JSON

JSON(JavaScript Object Notation)是一种轻量级的数据交换格式，它的数据格式非常简单，易于阅读和编写，同时也易于机器解析和生成，既可以用JSON传输一个简单的String、Number、Boolean类型数据，也可以传输一个数组，或者一个复杂的Object对象。

JSON有两种结构：

- 值得有序列表：在大部分语言中，它可以被理解为数组。
- “名称/值”的集合：在不同的语言中，它可以被理解为对象、记录、结构、字典、哈希表等。

**(1) 数组**

使用JSON表示数组时，数组以“[”开始，以“]”结束，每个元素之间使用“,”(逗号)分隔，元素可以是任意的值。例如，一个数组包含了String、Number、Boolean、null类型数据，JSON的表示形式如下：
	
	["abc", 12345, false, null]

**(2) 对象**

使用JSON表示Object类型数据时，Object对象以“{”开始，以“}”结束，每个“名称”后跟一个“:”(冒号)；“名称/值”对之间使用“,”(逗号)分隔。例如，一个address对象包含城市、街道、邮编等信息，JSON的表示形式如下：

	{"city":"Beijing", "street":"Chaoyang Road", "postcode":100025}

当时用JSON存储Object时，其中的Value值既可以是一个Object，也可以是数组，因此，复杂的Object可以嵌套表示。例如一个Person对象包含name和address对象，其表示格式如下：
	
	{
		"name":"Michael",
		"address":{
			"city":"Beijing",
			"street":"Chaoyang Road",
			"postcode":100025
		}
	}

假设Value值是一个数组，例如一个Person对象包含name和hobby信息，其表示格式如下：
	
	{
		"name":"Michael",
		"hobby":["Basketball", "Baseball", "Swimming"]
	}

需要注意的是，如果使用JSON存储单个数据，如"abc"，一定要使用数组形式，不要使用Object形式，因为Object形式必须是“名称/值”形式。

#### 2. 回调函数

#### 3. ProgressBar、GridView、TextView指定文字阴影效果的属性

ProgressBar：layout/activity_splash.xml

GridView：layout/activity_home.xml

TextView中有几个属性用于指定文字阴影效果，其中android:shadowColor属性用于指定阴影颜色，android:shadowDx、android:shadowDy和android:shadowRadius属性分别用于指定阴影在X轴和Y轴上的偏移量以及阴影的半径。

#### 4. xUtils开源框架：splash.apkutils.ApkDownloader和splash.apkutils.VersionUpdateUtils

使用HttpUtils发送HTTP请求、httpUtils.download(url, localTargetFile, RequestCallBack<File> callback)方法用来下载文件，

#### 5. 获取本地软件信息：splash.apkutils.ApkUtils

	PackageManager manager=context.getPackageManager();
	PackageInfo packageInfo=manager.getPackageInfo(context.getPackageName(), 0);
    return packageInfo.versionName;

#### 6. 安装APK：splash.apkutils.ApkUtils

	Intent intent=new Intent("android.intent.action.VIEW");
    //添加默认分类
    intent.addCategory("android.intent.category.DEFAULT");
    //设置数据和类型
    intent.setDataAndType(Uri.fromFile(new File(Constants.APK_LOCAL_FILE)), "application/vnd.android.package-archive");
    activity.startActivityForResult(intent, 0);

#### 7. Handler与线程间通信:splash.apkutils.VersionUpdateUtils

Handler可以用于主线程和子线程之间通信，让子线程可以及时通知主线程更新UI

#### 8. APK服务器、访问网络权限和写SD卡权限

## 二、 手机防盗模块

手机防盗模块主要用于SIM卡变更提醒、GPS追踪、远程锁屏和远程删除数据等。

### (一) 为防盗模块设置密码和输入密码

#### 1. 自定义形状样式：drawable\coner\_bg\_white.xml

	<shape xmlns:android="http://schemas.android.com/apk/res/android">
	    <corners android:radius="6.0dp"/> <!--指定圆角半径-->
	    <solid android:color="#ffffff"/> <!--指定填充颜色-->
	</shape>

#### 2. 自定义对话框样式：values-v21/styles.xml

	<style name="dialog_custom" parent="android:style/Theme.Dialog">
        <item name="android:windowFrame">@null</item> <!--去掉边框-->
        <item name="android:windowNoTitle">true</item> <!--无标题栏-->
        <item name="android:background">#00000000</item> <!--背景颜色为黑色-->
        <item name="android:windowBackground">@android:color/transparent</item> <!--对话框背景透明-->
    </style>

	在safe.dialog.SetupPasswordDialog和safe.dialog.EnterPasswordDialog中通过super(context, R.style.dialog_custom);引入自定义对话框样式。

#### 3. MD5加密算法保护防盗模块的密码

### (二) 防盗模块设置向导

#### 1. RadioGroup和RadioButton：layout/setup\_radiogroup.xml

#### 2. Android Selector选择器：

drawable/circle\_purple\_bg\_selector.xml

	<selector xmlns:android="http://schemas.android.com/apk/res/android">
	    <item android:state_checked="true" android:drawable="@drawable/circle_purple"/>
	    <item android:state_checked="false" android:drawable="@drawable/circle_white"/>
	</selector>

	当按钮处于选中状态时，为按钮指定背景颜色circle_purple；当按钮未被选中时，为按钮指定背景颜色circle_white

drawable/sim\_bind\_selector.xml

	<selector xmlns:android="http://schemas.android.com/apk/res/android">
	    <item android:drawable="@drawable/sim_bind_p" android:state_pressed="true"/>
	    <item android:drawable="@drawable/sim_bind_e" android:state_enabled="false"/>
	    <item android:drawable="@drawable/sim_bind_n"/>
	</selector>

	默认情况下，按钮显示sim_bind_n；当按钮按下时，显示sim_bind_p；当按钮弹起时，显示sim_bind_e

drawable/toggle\_btn\_bg\_selector.xml

ToggoleButton：layout/activity\_lostfind.xml，该控件用于展示可切换的按钮，控制条目的开启和关闭，android:textOn属性表示选中时按钮的文本，android:textOff属性用于表示未选中时按钮的文本。

	<selector xmlns:android="http://schemas.android.com/apk/res/android">
	    <item android:state_checked="true" android:drawable="@drawable/switch_btn_on"/>
	    <item android:state_checked="false" android:drawable="@drawable/switch_btn_off"/>
	</selector>

	当按钮处于选中状态时，为按钮指定背景图片switch_btn_on；当按钮未被选中时，为按钮指定背景图片switch_btn_off


#### 3. 自定义形状样式：drawable/round_purple\_tv\_bg.xml

	<shape xmlns:android="http://schemas.android.com/apk/res/android">
	    <solid android:color="@color/purple"/>
	    <corners 
			android:topLeftRadius="5dp" 
			android:topRightRadius="5dp" 
			android:bottomLeftRadius="0dp" 
			android:bottomRightRadius="0dp"/>
	</shape>

	<shape>标签用于设置形状样式，其中有六个子标签，分别为<corners>、<solid>、<gradient>、<padding>、<size>和<stroke>。
	<solid>标签指定填充颜色。<corners>标签指定圆角样式。
	android:topLeftRadius属性用于设置左上圆角的半径，android:topRightRadius属性用于设置右上圆角的半径，
	android:bottomLeftRadius属性用于设置左下圆角的半径，android:bottomRightRadius属性用于设置右下圆角的半径。

#### 4. 标题栏 layout/titlebar.xml

标题栏放置了一个TextView控件和两个ImageView控件，其中TextView控件用于展示标题，ImageView控件用于在标题文字左侧或右侧放置返回按钮。

#### 5. 平移动画和手势识别器GestureDetector

在Android系统中，通过手势识别切换页面时，通常会在页面切换时加入动画，以提高用户的体验效果，这种动画一般采用平移动画，当下一个页面进入时，上一个页面移出屏幕。

动画效果文件：anim/next\_in.xml、anim/next\_out.xml、anim/pre\_in.xml和anim/pre\_out.xml

手势识别：safe/BaseSetupActivity.java

### (三) 防盗功能的实现：SIM卡变更提醒、GPS追踪、远程锁屏和远程删除数据等

#### 1. 检测SIM卡变化

Application类是Android框架的一个系统组件，当Android程序启动时，系统会创建Application对象，正是由于它的这种特性，可以将检测SIM卡是否变更的方法放在Application的onCreate()方法中，当程序启动时就会检测SIM卡是否变更。

safe.App.java：当防盗保护开启时，获取绑定的SIM卡串号，然后获取当前手机SIM卡串号进行比对，如果一致则代表SIM未发生变化，否则表示SIM卡发生了变化，此时向安全号码发送短信，提示手机SIM卡已更换。

获取手机SIM卡号

	TelephonyManager telephonyManager= (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
    String realSim=telephonyManager.getSimSerialNumber();

发送短信

	SmsManager smsManager=SmsManager.getDefault();
    smsManager.sendTextMessage(safephone, null, "您的亲友手机的SIM卡已经被更换! ", null, null);

在Android系统中，有些手机SIM卡更换后，需要重新启动手机识别新的SIM卡，因此，为了最大限度地知道SIM卡变化，需要创建一个开机启动的广播接收者safe.receiver.BootCompleteReceiver监听手机开机事件，并检查SIM是否更换。

	public class BootCompleteReceiver extends BroadcastReceiver {
	    private final String TAG=BootCompleteReceiver.class.getSimpleName();
	
	    @Override
	    public void onReceive(Context context, Intent intent) {
	        ((App)context.getApplicationContext()).checkSim();
	    }
	}

注意注册开启启动的广播接收者以及配置权限信息。

	<uses-permission android:name="android.permission.SEND_SMS" />
    <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED"/>

#### 2. 远程锁屏和远程删除数据、GPS追踪

为了监听安全号码发送的防盗指令，创建一个广播接收者safe.receiver.SmsLostFindReceiver，根据接收的防盗指令(GPS追踪、远程锁屏和远程删除数据)执行不同的操作。

(1) 远程锁屏和远程删除数据

获取超级管理员权限，并利用超级管理员完成远程清除数据和远程锁屏功能，超级管理员权限需要在清单文件中配置。

	// 获取超级管理员
    DevicePolicyManager dpm= (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
	// 远程清楚数据
	dpm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
	// 远程锁屏
	dpm.lockNow();

获取超级管理员权限

	/**
	 * 1. 定义特殊的广播接收者，系统超级管理员的广播接收者
	 */
	public class MyDeviceAdminReceiver extends DeviceAdminReceiver {
	    @Override
	    public void onReceive(Context context, Intent intent) {
	    }
	}

	<!-- 2. 配置设备超级管理员广播接收者 -->
    <receiver
        android:name=".safe.receiver.MyDeviceAdminReceiver"
        android:description="@string/sample_device_admin_description"
        android:label="@string/sample_device_admin"
        android:permission="android.permission.BIND_DEVICE_ADMIN">
        <meta-data
            android:name="android.app.device_admin"
            android:resource="@xml/device_admin_sample" />
        <intent-filter>
            <action android:name="android.app.action.DEVICE_ADMIN_ENABLED" />
        </intent-filter>
    </receiver>

	<device-admin xmlns:android="http://schemas.android.com/apk/res/android">
	    <!-- 3. 声明安全策略，锁屏、清除数据、重置密码-->
	    <uses-policies>
	        <force-lock/>     <!-- 锁屏 -->
	        <wipe-data/>      <!-- 清除数据 -->
	        <reset-password/> <!-- 重置密码 -->
	    </uses-policies>
	</device-admin>

(2) GPS追踪：safe.service.GPSLocationService

获取系统的位置管理器，通过Criteria对象返回可用的位置提供者，利用最好位置提供者获取手机的位置，然后将手机位置发送给安全号码。

	locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

	//查询条件
    Criteria criteria = new Criteria();
    criteria.setAccuracy(Criteria.ACCURACY_FINE);
    criteria.setCostAllowed(true);
    String name = locationManager.getBestProvider(criteria, true);

	locationManager.requestLocationUpdates(name, 0, 0, listener);

## 三、 通讯卫士模块

通讯卫士模块用于实现黑名单拦截功能，对黑名单中的号码进行短信或电话拦截。

## 四、 软件管家模块

软件管家模块主要用于管理软件的启动、卸载、分享和设置等。

## 五、 手机杀毒模块

手机杀毒模块主要用于全盘扫描，并显示当前正在扫描的病毒以及查杀进度。

## 六、 缓存清理模块

缓存清理模块主要用于查看所有程序的缓存，并可以一键清理所有程序的缓存。

## 七、 进程管理模块

进程管理模块主要用于查看手机中正在运行的进程信息，以及清理进程等。

## 八、 流量统计模块

流量统计模块主要用于显示运营商信息设置、流量监控，在流量监控界面中可以看见本日、本月使用流量以及本月的总流量。

## 九、 高级工具模块

高级工具模块主要包括号码归属地查询、短信备份、短信还原和程序锁四个功能。

## 十、 设置中心模块

设置中心模块主要用于设置黑名单拦截是否开启、程序锁是否开启。