# SecurityGuard

#### 本项目来自“传智播客”教程项目，实现了一个简易版本的“手机安全卫士”，主要包含以下几个功能模块：欢迎界面模块、手机防盗模块、通讯卫士模块、软件管家模块、手机杀毒模块、缓存清理模块、进程管理模块、流量统计模块、高级工具模块和设置中心模块。

本文档主要介绍本项目中出现的一些知识点和关键技术。

## 一、 欢迎界面模块：org/android/securityguard/splash

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

主要包：org/android/securityguard/black

### (一) 黑名单数据库

#### 1. 使用Android自带数据库SQLite存储黑名单信息

#### 2. JUnit测试框架

### (二) 通讯卫士主界面

#### 1. FrameLayout布局：layout/activity_securityphone.xml

(1) 通过两个FrameLayout的显示与隐藏，来分别显示有黑名单和无黑名单的界面。

(2) black/adapter/BlackContactAdapter.java：当数据库中数据完全删除完后，调用回调函数，将含有ListView的FrameLayout布局隐藏

	if(dao.getTotalNumber()==0){
        callback.dataSizeChanged();
    }

#### 2. ListView的滑动监听事件：org/android/securityguard/black/SecurityPhoneActivity.java

onScrollStateChanged()方法主要功能是获取数据库中的数据并分页显示界面上，每页显示多少数据可以自定义，当ListView向下滑动时再次加载同样条目的数据。

	mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int state) {
                switch (state){
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE://没有滑动的状态
                        int lastVisiblePosition=mListView.getLastVisiblePosition();
                        if(lastVisiblePosition==contacts.size()-1){
                            pagenumber++;
                            if(pagenumber*pagesize>=totalNumber){
                                Toast.makeText(SecurityPhoneActivity.this, R.string.security_no_more_data, Toast.LENGTH_SHORT).show();
                            }else{
                                contacts.addAll(dao.getPageBlackNumber(pagenumber, pagesize));
                                adapter.notifyDataSetChanged();
                            }
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });

### (三) 添加黑名单

#### 1. layout/activity_add_black_number.xml中CheckBox和Button都使用了背景选择器

### (四) 黑名单拦截

在Android系统中，当电话或短信到来时都会产生广播，因此可以利用广播接收者将广播终止，实现黑名单拦截功能，然后将电话和短信记录删除不让其在界面中显示。

#### 1. 短信拦截 (没有成功)

在进行短信拦截时，需要在广播中获取到电话号码以及短信内容，然后查询该号码是否在黑名单数据库中，如果在黑名单中，则判断是哪种拦截模式，并进行拦截。

拦截短信的广播接收者：org/android/securityguard/black/receiver/InterceptSmsReceiver.java

	public class InterceptSmsReceiver extends BroadcastReceiver {
	    @Override
	    public void onReceive(Context context, Intent intent) {
	        //黑名单功能是否开启
	        SharedPreferences mSharedPreferences=context.getSharedPreferences("config", Context.MODE_PRIVATE);
	        boolean BlackNumStatus=mSharedPreferences.getBoolean("BlackNumStatus", true);
	        if(!BlackNumStatus){//黑名单拦截关闭
	            return;
	        }
	
	        BlackNumberDao dao=new BlackNumberDao(context);
	        Object[] objs= (Object[]) intent.getExtras().get("pdus"); //获取接收到的短信
	        for(Object obj:objs){
	            SmsMessage msg=SmsMessage.createFromPdu((byte[]) obj);
	            String sender=msg.getOriginatingAddress();
	            String body=msg.getMessageBody();
	            if(sender.startsWith("+86")){
	                sender=sender.substring(3, sender.length());
	            }
	            int mode=dao.getBlackContactMode(sender);//查询拦截模式
	            if(mode==2 || mode==3){
	                abortBroadcast();//需要拦截，拦截广播
	            }
	        }
	    }
	}

注册拦截短信的广播接收者：

	<uses-permission android:name="android.permission.CALL_PHONE" />
    <uses-permission android:name="android.permission.WRITE_CALL_LOG" />
    <uses-permission android:name="android.permission.READ_CALL_LOG" />
	
	<receiver android:name=".black.receiver.InterceptSmsReceiver">
        <!--定义了接收短信的广播，将广播的优先级设置为最高，这样当有新短信到来时会优先被该广播接收者接收-->
        <intent-filter android:priority="214783647">
            <action android:name="android.provider.Telephony.SMS_RECEIVED" />
        </intent-filter>
    </receiver>

#### 2. 电话拦截

当电话铃响时需要自动挂断电话并且不让该记录显示在界面上，Google工程师为了手机的安全性隐藏了挂断电话的服务方法，因此要实现挂断电话操作只能通过反射获取底层服务。

注意：与电话相关的操作一般都使用TelephonyManager类，但是由于挂断电话的方法在ITelephony接口中，而这个接口是隐藏的，在开发时看不到，因此需要使用ITelephony.aidl。在使用ITelephony.aidl时，需要创建一个与其包名一致的包com.android.internal.telephony，然后把系统的ITelephony.aidl文件复制进来。同时，由于ITelephony.aidl关联了NeighboringCellInfo.aidl，也需要一并复制进来。不过NeighboringCellInfo.aidl所在的包名为android.telephony，因此需要创建一个android.telephony包，然后把NeighboringCellInfo.aidl放到该包中。

拦截电话的广播接收者：org/android/securityguard/black/receiver/InterceptCallReceiver.java

	public class InterceptCallReceiver extends BroadcastReceiver {
	    @Override
	    public void onReceive(Context context, Intent intent) {
	        SharedPreferences mSharedPreferences=context.getSharedPreferences("config", Context.MODE_PRIVATE);
	        boolean BlackNumStatus=mSharedPreferences.getBoolean("BlackNumStatus", true);
	        if(!BlackNumStatus){//黑名单拦截关闭
	            return;
	        }
	
	        BlackNumberDao dao=new BlackNumberDao(context);
	        if(!intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)){
	            String mIncomingNumber="";
	            TelephonyManager manager= (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
	            switch (manager.getCallState()){
	                case TelephonyManager.CALL_STATE_RINGING://来电话
	                    mIncomingNumber=intent.getStringExtra("incoming_number");
	                    int blackContactMode=dao.getBlackContactMode(mIncomingNumber);
	                    if(blackContactMode==1 || blackContactMode==3){
	                        //观察呼叫记录的变化，如果呼叫记录生成了，就把呼叫记录给删除掉
	                        Uri uri=Uri.parse("content://call_log/calls");
	                        context.getContentResolver().registerContentObserver(uri, true, new CallLogObserver(new Handler(), mIncomingNumber, context));
	                        endCall(context);
	                    }
	                    break;
	            }
	        }
	    }
	
	    /**
	     * 清除呼叫记录
		 * 黑名单电话呼入时，手机系统通话记录中会显示该记录，因此需要把通话记录中的黑名单通话记录删除。
		 * 手机上拨打电话、接听电话等产生的记录都在系统联系人应用下的contacts2.db数据库中，使用ContentResolver对象查询并删除数据库中黑名单号码产生的记录
	     * @param incomingNumber
	     * @param context
	     */
	    public void deleteCallLog(String incomingNumber, Context context){
	        ContentResolver resolver=context.getContentResolver();
	        Uri uri=Uri.parse("content://call_log/calls");
	        Cursor cursor=resolver.query(uri, new String[]{"_id"}, "number=?", new String[]{incomingNumber}, "_id desc limit 1");
	        if(cursor.moveToNext()){
	            String id=cursor.getString(0);
	            resolver.delete(uri, "_id=?", new String[]{id});
	        }
	    }
	
	    /**
	     * 挂断电话，需要复制两个AIDL
	     * @param context
	     */
	    public void endCall(Context context){
	        try {
	            Class clazz=context.getClassLoader().loadClass("android.os.ServiceManager");//通过反射获得ServiceManager字节码
	            Method method=clazz.getDeclaredMethod("getService", String.class);//通过该字节码获得getService方法
	            IBinder iBinder= (IBinder) method.invoke(null, Context.TELEPHONY_SERVICE);//执行该方法，获得远程服务的代理
	            ITelephony iTelephony=ITelephony.Stub.asInterface(iBinder);
	            iTelephony.endCall();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	
	    /**
	     * 通过内容观察者观察系统联系人数据库变化
	     */
	    private class CallLogObserver extends ContentObserver{
	        private String incomingNumber;
	        private Context context;
	
	        public CallLogObserver(Handler handler, String incomingNumber, Context context){
	            super(handler);
	            this.incomingNumber=incomingNumber;
	            this.context=context;
	        }
	
	        /**
	         * 观察到数据库内容变化时调用该方法
	         * @param selfChange
	         * @param uri
	         */
	        @Override
	        public void onChange(boolean selfChange, Uri uri) {
	            Log.i("CallLogObserver", "呼叫记录数据库的内容变化了.");
	            context.getContentResolver().unregisterContentObserver(this);
	            deleteCallLog(incomingNumber, context);
	            super.onChange(selfChange, uri);
	        }
	    }
	}

注册拦截电话的广播接收者并添加与电话相关的权限：

	<uses-permission android:name="android.permission.CALL_PHONE" />
    <uses-permission android:name="android.permission.WRITE_CALL_LOG" />
    <uses-permission android:name="android.permission.READ_CALL_LOG" />

	<receiver android:name=".black.receiver.InterceptCallReceiver">
        <intent-filter android:priority="214783647">
            <action android:name="android.intent.action.PHONE_STATE" />
            <action android:name="android.intent.action.NEW_OUTGOING_CALL" />
        </intent-filter>
    </receiver>

## 四、 软件管家模块

软件管家模块主要用于管理软件的启动、卸载、分享和设置等。

#### 1. 获取手机中安装的应用程序的包信息：org/android/securityguard/app/utils/AppInfoParser.java

	// 获取包管理器，通过包管理器获取手机中已安装的应用程序的包信息
	PackageManager packageManager=context.getPackageManager();
    List<PackageInfo> packageInfos=packageManager.getInstalledPackages(0);

	// 获取每个应用程序的包名、图标、应用程序名称、应用程序路径、应用程序大小、安装位置、系统应用还是用户应用
	for(PackageInfo packageInfo:packageInfos){
        String packageName=packageInfo.packageName;
        Drawable icon=packageInfo.applicationInfo.loadIcon(packageManager);
        String appName=packageInfo.applicationInfo.loadLabel(packageManager).toString();
        String apkPath=packageInfo.applicationInfo.sourceDir;
        Long appSize=new File(apkPath).length();

        boolean isInRoom=false;
        boolean isUserApp=false;
        int flag=packageInfo.applicationInfo.flags;
        if((ApplicationInfo.FLAG_EXTERNAL_STORAGE & flag) !=0){
            isInRoom=false; //外部存储
        }else{
            isInRoom=true;  //手机内存
        }
        if((ApplicationInfo.FLAG_SYSTEM & flag) !=0){
            isUserApp=false;//系统应用
        }else{
            isUserApp=true; //用户应用
        }

		...
	}

#### 2. dip和px相互转换：org/android/securityguard/app/utils/DensityUtils.java

#### 3. ListView的使用

适配器：org/android/securityguard/app/adapter/AppManagerAdapter.java

(1) 注意：多包含两个条目：一个TextView用于显示系统程序标签，一个TextView用于显示用户程序标签

	if(position==0){
        TextView tv=getTextView();
        tv.setText("用户程序: "+userAppInfos.size()+"个");
        return tv;
    }else if(position==userAppInfos.size()+1){
        TextView tv=getTextView();
        tv.setText("系统程序: "+sysAppInfos.size()+"个");
        return tv;
    }

(2) 条目点击事件和条目滚动事件

	mListView= (ListView) findViewById(R.id.lv_appmanager);
    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
            if(adapter!=null){
                new Thread(){
                    @Override
                    public void run() {
                        AppInfo mAppInfo= (AppInfo) adapter.getItem(position);
                        boolean flag=mAppInfo.isSelected;
                        for(AppInfo appInfo:userAppInfos){
                            appInfo.isSelected=false;
                        }
                        for(AppInfo appInfo:sysAppInfos){
                            appInfo.isSelected=false;
                        }
                        if(mAppInfo!=null){
                            //如果已经选中，则变为未选中
                            if(flag){
                                mAppInfo.isSelected=false;
                            }else{
                                mAppInfo.isSelected=true;
                            }
                            mHandler.sendEmptyMessage(15);
                        }
                    }
                }.start();
            }
        }
    });
    mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if(firstVisibleItem>=userAppInfos.size()+1){
                mAppNumTV.setText("系统程序: "+sysAppInfos.size()+"个");
            }else{
                mAppNumTV.setText("用户程序: "+userAppInfos.size()+"个");
            }
        }
    });

(3) 更新界面

定义一个广播接收者，用于接收程序卸载的广播，当程序卸载完后，调用initData()方法，清除数据重新加载应用列表，更新列表

	receiver=new UninstallReceiver();
    IntentFilter intentFilter=new IntentFilter(Intent.ACTION_PACKAGE_REMOVED);
    intentFilter.addDataScheme("package");
    registerReceiver(receiver, intentFilter);

	// 更新界面
	adapter.notifyDataSetChanged();

#### 4. 获取手机本身和SD卡剩余内存

    private void getMemoryFromPhone(){
        long avail_sd= Environment.getExternalStorageDirectory().getFreeSpace();
        long avail_rom=Environment.getDataDirectory().getFreeSpace();

        //由于上面的数值为long比较长，因此对其进行格式化使其返回单位为KB或MB的String
        String str_avail_sd= Formatter.formatFileSize(this, avail_sd);
        String str_avail_rom=Formatter.formatFileSize(this, avail_rom);
        mPhoneMemoryTV.setText("剩余手机内存: "+str_avail_rom);
        mSDMemoryTV.setText("剩余SD卡内存"+str_avail_sd);
    }

#### 5. 分享、启动、设置、卸载程序：org/android/securityguard/app/utils/EngineUtils.java（图标不显示问题，不知道为什么）

卸载应用程序时，首先判断程序是否为用户程序，如果是则通过隐式意图直接删除。如果是系统应用程序，需要Root权限才能卸载。在Android系统中获取Root权限需要两步，首先通过RootTools.isRootAvailable()方法(需要导入RootTools包)判断是否有Root权限，如果无，则通过RootTools.isAccessGiven()方法获取Root权限。获取到权限后，通过Linux命令即可删除。

 	/**
     * 分享应用(隐式意图)
     * @param context
     * @param appInfo
     */
    public static void shareApplication(Context context, AppInfo appInfo){
        Intent intent=new Intent("android.intent.action.SEND");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "推荐您使用一款软件，名称叫: "+appInfo.appName+", 下载路径: https://play.google.com/store/apps/details?id="+appInfo.packageName);
        context.startActivity(intent);
    }

    /**
     * 开启应用程序(隐式意图)
     * @param context
     * @param appInfo
     */
    public static void startApplication(Context context, AppInfo appInfo){
        PackageManager packageManager=context.getPackageManager();
        Intent intent=packageManager.getLaunchIntentForPackage(appInfo.packageName);
        if(intent!=null){
            context.startActivity(intent);
        }else{
            Toast.makeText(context, R.string.app_manager_app_no_launch, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 开启应用设置页面(隐式意图)
     * @param context
     * @param appInfo
     */
    public static void setAppDetail(Context context, AppInfo appInfo){
        Intent intent=new Intent();
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse("package:"+appInfo.packageName));
        context.startActivity(intent);
    }

	 /**
     * 卸载应用
     * @param context
     * @param appInfo
     */
    public static void uninstallApplication(Context context, AppInfo appInfo){
        if(appInfo.isUserApp){
            Intent intent=new Intent();
            intent.setAction(Intent.ACTION_DELETE);
            intent.setData(Uri.parse("package: "+appInfo.packageName));
            context.startActivity(intent);
        }
        else{//系统应用需要root权限，利用linux命令删除文件
            if(!RootTools.isRootAvailable()){
                Toast.makeText(context, R.string.app_manager_app_uninstall_need_root, Toast.LENGTH_SHORT).show();
                return;
            }
            try{
                if(RootTools.isAccessGiven()){
                    Toast.makeText(context, R.string.app_manager_app_uninstall_permission_grant, Toast.LENGTH_SHORT).show();
                    return;
                }
                RootTools.sendShell("mount -o remount, rw /system", 3000);
                RootTools.sendShell("rm -r "+appInfo.apkPath, 30000);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

导入RootTools包

	dependencies {   
	    compile files('libs/RootTools-2.4.jar')
	}


## 五、 手机杀毒模块：org/android/securityguard/virus

手机杀毒模块主要用于全盘扫描，并显示当前正在扫描的病毒以及查杀进度。

#### 1. 判断病毒应用程序：org/android/securityguard/virus/utils/MD5Utils.java

每一个应用程序都有对应的特征码，这个特征码称为MD5码。MD5码是程序的唯一标识，手机杀毒厂商搜集了大量收集病毒应用的MD5码存入数据库中，当杀毒软件对手机应用进行扫描时，会先得到被扫描应用的MD5码，并将该MD5码与病毒数据库中存储的MD5码进行比对，如果发现应用程序的MD5码存在于数据库中，则说明这个应用程序是病毒，可以直接清楚或手动卸载应用。

#### 2. ListView自动向上滚动效果：org/android/securityguard/virus/VirusScanSpeedActivity.java

Handler接收扫描的应用信息并将其显示在界面上，同时不断在mScanAppInfos集合中添加数据，因此集合长度不断增加，使用mScanListView.setSelection(mScanAppInfos.size())方法是ListView一直选择最后一条数据，这样当不断扫描应用时，就会看到ListView自动向上滚动的效果。

#### 3. 子线程扫描病毒，并将结果发送给Handler进行处理：org/android/securityguard/virus/VirusScanSpeedActivity.java

#### 4. 旋转动画：org/android/securityguard/virus/VirusScanSpeedActivity.java

	private void startAnim(){
        if(rani==null){
            rani=new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        }
        rani.setRepeatCount(Animation.INFINITE);
        rani.setDuration(2000);
        mScanningIcon.startAnimation(rani);
    }

## 六、 缓存清理模块：org/android/securityguard/cache

缓存清理模块主要用于查看所有程序的缓存，并可以一键清理所有程序的缓存。

### (一) 扫描缓存：org.android.securityguard.cache.CacheScanActivity

手机中的大部分程序都有缓存信息，这些缓存信息是通过AIDL接口调用底层方法获取的。

#### 1. 帧动画：anim/broom_animation.xml

ImageView使用一个帧动画，通过一系列Drawable依次显示来模拟动画的效果，每张图片显示0.2秒进行切换。

	<animation-list xmlns:android="http://schemas.android.com/apk/res/android" >
	    <item android:drawable="@drawable/broom_left" android:duration="200"></item>
	    <item android:drawable="@drawable/broom_center" android:duration="200"></item>
	    <item android:drawable="@drawable/broom_right" android:duration="200"></item>
	</animation-list>

#### 2. 获取应用程序的缓存大小

由于getPackageSizeInfo(String packageName, IPackageStatsObserver observer)方法是隐藏的，因此需要通过反射机制获取该方法，该方法参数中的packageName表示包名，observer是一个远程服务的AIDL接口。

 	/**
     * 获取某个包名对应的应用程序的缓存大小
     * @param info
     */
    public void getCacheSize(PackageInfo info){
        try {
            Method method=PackageManager.class.getDeclaredMethod("getPackageSizeInfo", String.class, IPackageStatsObserver.class);
            method.invoke(packageManager, info.packageName, new MyPackageObserver(info));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

MyPackageObserver类是一个AIDL的实现类，它继承自android.content.pm.IPackageStatsObserver.Stub，需要实现该类中的onGetStatsCompleted方法，然后通过pStats.cacheSize就可以获取到缓存信息。

 	private class MyPackageObserver extends android.content.pm.IPackageStatsObserver.Stub{
        private PackageInfo info;

        public MyPackageObserver(PackageInfo info){
            this.info=info;
        }

        @Override
        public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {
            Long cacheSize=pStats.cacheSize;
            if(cacheSize>=0){
                CacheInfo cacheInfo=new CacheInfo();
                cacheInfo.cacheSize=cacheSize;
                cacheInfo.packageName=info.packageName;
                cacheInfo.appName=info.applicationInfo.loadLabel(packageManager).toString();
                cacheInfo.appIcon=info.applicationInfo.loadIcon(packageManager);
                cacheInfos.add(cacheInfo);
                cacheMemory+=cacheSize;
            }
        }
    }

需要注意的是，获取缓存时需要用到IPackageStatsObserver接口，因此需要创建一个android.content.pm包，将IPackageStatsObserver.aidl复制到工程中。由于该接口还依赖于PackageStats.aidl接口，因此也需要将该文件复制到android.content.pm包中。

在获取程序缓存时，需要在AndroidManifest.xml文件中配置相关权限。

	<uses-permission android:name="android.permission.GET_PACKAGE_SIZE" />

### (二) 缓存清理

#### 1. 帧布局：layout/activity_cache_clean.xml

(1) 动画选择器

该选择器是一个帧动画，用于控制垃圾桶图标400毫秒切换一次。

	<animation-list xmlns:android="http://schemas.android.com/apk/res/android">
	    <item android:drawable="@drawable/cacheclean_trashbin_close_icon" android:duration="400"/>
	    <item android:drawable="@drawable/cacheclean_trashbin_open_icon" android:duration="400"/>
	</animation-list>

#### 2. 清理缓存

通过反射的形式获取到freeStorageAndNotify(long freeStorageSize, IPackageDataObserver observer)方法，该方法接收两个参数，第一个参数表示要释放的缓存大小，第二个参数是远程服务接口。

	private void cleanAll(){
        //清除全部缓存利用Android系统的一个漏洞:freeStorageAndNotify
        Method[] methods=PackageManager.class.getMethods();
        for(Method method:methods){
            if("freeStorageAndNotify".equals(method.getName())){
                try {
                    method.invoke(packageManager, Long.MAX_VALUE, new ClearCacheObserver());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                return;
            }
        }
        Toast.makeText(CacheCleanActivity.this, R.string.cacheclean_clean_finish, Toast.LENGTH_SHORT).show();
    }

	class ClearCacheObserver extends android.content.pm.IPackageDataObserver.Stub{
        @Override
        public void onRemoveCompleted(String packageName, boolean succeeded) throws RemoteException {

        }
    }

需要注意的是，在清除缓存时使用了IPackageDataObserver.aidl接口，因此需要将其复制到android.content.pm包中。另外，还要在AndroidManifest.xml文件中配置清除缓存相关权限。

	<uses-permission android:name="android.permission.CLEAR_APP_CACHE" />

## 七、 进程管理模块：org/android/securityguard/process

进程管理模块主要用于查看手机中正在运行的进程信息，以及清理进程等。

### (一) 进程管理

#### 1. 背景选择器

drawable/cleanprocess_btn_selector.xml

drawable/select_all_btn_selector.xml

drawable/inverse_btn_selector.xml

drawable/green_checkbox_selector.xml

#### 2. ListView

(1) 条目点击事件和条目滚动事件:org/android/securityguard/process/ProcessManagerActivity.java

	mListView= (ListView) findViewById(R.id.lv_runningapps);
    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            Object object=mListView.getItemAtPosition(position);
            if(object!=null && object instanceof TaskInfo){
                TaskInfo taskInfo= (TaskInfo) object;
                if(taskInfo.packageName.equals(getPackageName())){//当前点击的条目是本应用程序
                    return;
                }
                taskInfo.isChecked=!taskInfo.isChecked;
                adapter.notifyDataSetChanged();
            }
        }
    });
    mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView absListView, int i) {
        }

        @Override
        public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if(firstVisibleItem>=userTaskInfos.size()+1){
                mProcessNumTV.setText("系统进程: "+sysTaskInfos.size()+"个");
            }else{
                mProcessNumTV.setText("用户进程: "+userTaskInfos.size()+"个");
            }
        }
    });

(2) Adapter:org/android/securityguard/process/adapter/ProcessManagerAdapter.java

调用Adapter的notifyDataSetChanged()方法可以更新ListView的数据列表。

#### 3. 清理进程：org/android/securityguard/process/ProcessManagerActivity.java

private void cleanProcess()利用ActivityManager的killBackgroundProcesses(String packageName)杀死进程。

	ActivityManager manager=(ActivityManager) getSystemService(ACTIVITY_SERVICE);
	manager.killBackgroundProcesses(taskInfo.packageName);

#### 4. 获取系统信息：org/android/securityguard/process/utils/SystemInfoUtils.java

(1) 判断一个服务是否处于运行状态

	 public static boolean isServiceRunning(Context context, String className){
        ActivityManager activityManager= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> infos=activityManager.getRunningServices(200);
        for(ActivityManager.RunningServiceInfo info:infos){
            String serviceClassName=info.service.getClassName();
            if(className.equals(serviceClassName)){
                return true;
            }
        }
        return false;
    }

(2) 获取手机的总内存大小(单位byte)

 	public static long getTotalMem(){
        try {
            StringBuffer sb=new StringBuffer();

            FileInputStream fis=new FileInputStream(new File("/proc/meminfo"));
            BufferedReader br=new BufferedReader(new InputStreamReader(fis));
            String totalInfo=br.readLine();
            for(char c:totalInfo.toCharArray()){
                if(c>='0' && c<='9'){
                    sb.append(c);
                }
            }
            long bytesize=Long.parseLong(sb.toString())*1024;
            return bytesize;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

(3) 获取手机可用内存大小

	public static long getAvailMem(Context context){
        ActivityManager activityManager= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo=new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        long availMem=memoryInfo.availMem;
        return availMem;
    }

(4) 获取手机正在运行的进程的数量

	public static int getRunningProcessCount(Context context){
        ActivityManager activityManager= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfos=activityManager.getRunningAppProcesses();
        return runningAppProcessInfos.size();
    }

#### 5. 获取进程信息：org/android/securityguard/process/utils/TaskInfoParser.java

通过ActivityManager获取正在运行的进程，根据进程获得进程名称、占用内存大小，然后再通过PackageManager获取进程对应程序的图标、名称、是否为用户程序

	public static List<TaskInfo> getRunningTaskInfos(Context context){
        List<TaskInfo> taskInfos=new ArrayList<TaskInfo>();

        PackageManager packageManager=context.getPackageManager();

        ActivityManager activityManager= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos=activityManager.getRunningAppProcesses();
        for(ActivityManager.RunningAppProcessInfo processInfo:processInfos){
            TaskInfo taskInfo=new TaskInfo();

            String packageName=processInfo.processName;
            taskInfo.packageName=packageName; //进程名称

            Debug.MemoryInfo[] memoryInfos=activityManager.getProcessMemoryInfo(new int[]{processInfo.pid});
            long mensize=memoryInfos[0].getTotalPrivateDirty()*1024;
            taskInfo.appMemory=mensize;       //程序占用的内存空间

            try {
                PackageInfo packageInfo=packageManager.getPackageInfo(packageName, 0);
                Drawable icon=packageInfo.applicationInfo.loadIcon(packageManager);
                taskInfo.appIcon=icon;        //程序图标

                String appName=packageInfo.applicationInfo.loadLabel(packageManager).toString();
                taskInfo.appName=appName;     //程序名称

                //是否为用户进程
                if((ApplicationInfo.FLAG_SYSTEM & packageInfo.applicationInfo.flags)!=0){
                    taskInfo.isUserTask=false;
                }else{
                    taskInfo.isUserTask=true;
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                taskInfo.appName=packageName;
                taskInfo.appIcon=context.getResources().getDrawable(R.drawable.ic_default);
            }
            taskInfos.add(taskInfo);
        }
        return taskInfos;
    }

### (二) 设置进程

在清理进程时，通常不希望手动进行清理，有时希望在手机睡眠时自动清理进程，因此可以利用服务和广播接收者在锁屏时自动清理进程。

#### 1. 背景选择器

drawable/toggle\_btn\_green_selector.xml

#### 2. ToggleButton按钮：layout/activity_process_manager_setting.xml

#### 3. 锁屏清理进程服务：org/android/securityguard/process/service/AutoKillProcessService.java

当设置界面中的锁屏清理进程按钮开启时，就会打开进程清理的服务，在该服务中注册监听屏幕锁屏的广播接收者，当屏幕锁屏时该广播接收者就收到锁屏的消息后悔自动清理进程。注意，在AndroidManifest.xml中配置清理进程服务。

## 八、 流量统计模块

流量统计模块主要用于显示运营商信息设置、流量监控，在流量监控界面中可以看见本日、本月使用流量以及本月的总流量。

#### 1. 流量数据库

在Android系统中，存储每日流量可以使用SQLite数据库。由于向运营商发送短信只能获取到本月使用总流量和本月已用流量，而无法得到每日使用流量，因此需要自己实时进行计算，并根据日期将使用的流量存储到数据库中，然后不断更新数据库。

#### 2. 查询流量信息

使用系统的短信管理器向运营商发送查询流量信息的短信

	SmsManager smsManager=SmsManager.getDefault();
	smsManager.sendTextMessage("10010",null, "LLCX", null, null);
	
自定义广播接收者CorrectFlowReceiver，当有短信到来时执行onReceiver()方法并对流量短信进行拦截，获取短信内容中本月已使用流量、剩余流量和套餐外流量等。

	class CorrectFlowReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            Object[] objs= (Object[]) intent.getExtras().get("pdus");
            for(Object obj:objs){
                SmsMessage smsMessage=SmsMessage.createFromPdu((byte[]) obj);
                String body=smsMessage.getMessageBody();
                String address=smsMessage.getOriginatingAddress();
                if(!address.equals("10010")){
                    return;
                }

                String[] split=body.split(",");
                long left=0;   //本月剩余流量
                long used=0;   //本月已用流量
                long beyond=0; //本月超出流量
                for(int i=0; i<split.length; i++){
                    if(split[i].contains("本月流量已使用")){
                        String usedFlow=split[i].substring(7, split[i].length());
                        used=getStringToFloat(usedFlow);
                    }else if(split[i].contains("剩余流量")){
                        String leftFlow=split[i].substring(4, split[i].length());
                        left=getStringToFloat(leftFlow);
                    }else if(split[i].contains("套餐外流量")){
                        String beyondFlow=split[i].substring(5, split[i].length());
                        beyond=getStringToFloat(beyondFlow);
                    }
                }

                SharedPreferences.Editor editor=mSharedPreferences.edit();
                editor.putLong("totalFlow", used+left);
                editor.putLong("usedFlow", used+beyond);
                editor.commit();

                mTotalTV.setText("本月流量: "+ Formatter.formatFileSize(context, (used+left)));
                mUsedTV.setText("本月已用: "+Formatter.formatFileSize(context, (used+beyond)));
            }
        }
    }

#### 3. 计算实时流量

定义一个Service，获取应用程序的实时流量信息：org/android/securityguard/traffic/service/TrafficMonitoringService.java

使用以下方法可以统计从本次开机到当前所使用的总流量

	long mobileRxBytes=TrafficStats.getMobileRxBytes(); //发送流量
    long mobileTxBytes=TrafficStats.getMobileTxBytes(); //接收流量

记录使用上面方法获取的流量信息，使用一个子线程，每隔一段时间，再次使用上述方法获取新的流量信息，减去前一次获取的流量信息，即为这段时间内使用的流量。

为了保证流量实时更新，在流量统计模块打开时就需要开启上面的Service。

另外，在手机刚开机时也需要开启该服务，由于手机开机时会发送一条广播消息，因此可以刘勇广播接收者来开启服务。

	public class BootCompleteReceiver extends BroadcastReceiver {
	    @Override
	    public void onReceive(Context context, Intent intent) {
	        if(!SystemInfoUtils.isServiceRunning(context, "org.android.securityguard.traffic.service.TrafficMonitoringService.class")){
	            context.startActivity(new Intent(context, TrafficMonitoringService.class));
	        }
	    }
	}

## 九、 高级工具模块

高级工具模块主要包括号码归属地查询、短信备份、短信还原和程序锁四个功能。

### (一) 主界面 - 自定义组合控件

#### 1. 条目布局：layout/ui\_advancedtools\_view.xml

#### 2. 自定义属性：values/attrs.xml

在res/values目录下建立一个attrs.xml的文件(如果存在，则直接使用即可)，在该文件中添加控件的自定义属性，这样R文件就可以生成对应的资源id，就能在自定义控件中引用。
	
	<resources>
	    <declare-styleable name="AdvancedToolsView">
	        <attr name="desc" format="string"/>
	        <attr name="android:src"/>
	    </declare-styleable>
	</resources>

#### 3. 创建自定义控件类：org.android.securityguard.advance.widget.AdvancedToolsView

自定义控件类需要继承系统布局或者控件，并使用带AttributeSet参数的类的构造方法，在构造方法中将自定义控件类中的变量和attrs.xml中属性关联起来。

	/**
     * 通过继承一个布局文件实现自定义控件，一般来说做组合控件时可以使用这种方式实现
     * 由于此处是通过布局文件来实现自定义控件，因此使用这种构造方法
     * @param context
     * @param attrs
     */
    public AdvancedToolsView(Context context, AttributeSet attrs){
        super(context, attrs);

		//获取到属性对象
        TypedArray mTypeArray=context.obtainStyledAttributes(attrs, R.styleable.AdvancedToolsView); 
        //获取到desc属性，与attrs.xml中定义的desc属性绑定
		desc=mTypeArray.getString(R.styleable.AdvancedToolsView_desc); 
		//获取到android:src属性，与attrs.xml中定义的android:src属性绑定       
        drawable=mTypeArray.getDrawable(R.styleable.AdvancedToolsView_android_src); 
		//回收属性对象 
        mTypeArray.recycle(); 

        init(context);
    }

    /**
     * 控件初始化
     * @param context
     */
    private void init(Context context){
        //将资源转化为View对象显示在自己身上
        View view=View.inflate(context, R.layout.ui_advancedtools_view, null);
        this.addView(view);

        mLeftImgV= (ImageView) findViewById(R.id.imgv_left);
        if(drawable!=null){
            mLeftImgV.setImageDrawable(drawable);
        }

        mDescriptionTV= (TextView) findViewById(R.id.tv_description);
        mDescriptionTV.setText(desc);
    }

#### 4. 在布局中使用自定义控件：layout/activity_advanced_tools.xml

	<!--使用自定义控件时必须使用XML命名空间xmlns:custom_android="http://schemas.android.com/apk/res-auto"将自定义空间引入到布局-->
	<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
	    xmlns:custom_android="http://schemas.android.com/apk/res-auto"
	    android:layout_width="match_parent"
	    android:layout_height="match_parent"
	    android:orientation="vertical">
	    <include layout="@layout/titlebar"/>
	    <!--在使用自定义控件时，每个控件的开始节点和结束节点都为自定义控件类的全路径，其中custom_android:desc和android:src是自定义属性-->
	    <org.android.securityguard.advance.widget.AdvancedToolsView
	        android:id="@+id/atv_num_belongto"
	        android:layout_width="match_parent"
	        android:layout_height="55dp"
	        custom_android:desc="@string/advanced_number_belongto"
	        android:gravity="center_vertical"
	        android:src="@drawable/num_belongto"/>
	    <org.android.securityguard.advance.widget.AdvancedToolsView
	        android:id="@+id/atv_sms_backup"
	        android:layout_width="match_parent"
	        android:layout_height="55dp"
	        android:gravity="center_vertical"
	        android:layout_marginTop="3dp"
	        custom_android:desc="@string/advanced_sms_backup"
	        android:src="@drawable/sms_backup"/>
	    <org.android.securityguard.advance.widget.AdvancedToolsView
	        android:id="@+id/atv_sms_restore"
	        android:layout_width="match_parent"
	        android:layout_height="55dp"
	        android:gravity="center_vertical"
	        android:layout_marginTop="3dp"
	        custom_android:desc="@string/advanced_sms_restore"
	        android:src="@drawable/sms_restore"/>
	    <org.android.securityguard.advance.widget.AdvancedToolsView
	        android:id="@+id/atv_app_lock"
	        android:layout_width="match_parent"
	        android:layout_height="55dp"
	        android:gravity="center_vertical"
	        android:layout_marginTop="3dp"
	        custom_android:desc="@string/advanced_app_lock"
	        android:src="@drawable/app_lock"/>
	</LinearLayout>

### (二) 号码归属地查询：org/android/securityguard/advance/NumBelongtoActivity.java

#### 1. 按钮的背景选择器

#### 2. 号码归属地数据库操作：org/android/securityguard/advance/db/dao/NumBelongtoDao.java

#### 3. EditText的内容监听器

在文本框输入完成后判断如果为空，将归属地信息清空

	mNumET= (EditText) findViewById(R.id.et_num_num_belongto);
    mNumET.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
        }

        /**
         * 文本变化之后
         * @param editable
         */
        @Override
        public void afterTextChanged(Editable editable) {
            String string=editable.toString().trim();
            if(string.length()==0){
                mResultTV.setText("");
            }
        }
    });

### (三) 短信备份

Android手机中的短信是在data/data/com.android.provider.telephony应用databases目录下的mmssms.db数据库的sms表中。
其中address代表短信地址；type代表短信类型：2代表发送，1代表接收；date代表短信时间；body代表短信内容。

#### 1. 获取短信内容

使用ContentResolver可以得到短信数据库中的短信内容

	ContentResolver resolver=context.getContentResolver();
    Uri uri= Uri.parse("content://sms/");
    Cursor cursor=resolver.query(uri, new String[]{"address", "body", "type", "date"}, null, null, null);

#### 2. 短信加密和解密：org/android/securityguard/advance/utils/Cryptor.java

为防止到处在本地的短信内容被别人窃取和查看，可以使用base64对象将短信内容进行加密；当还原或导入其他设备时同样需要使用相同密码将短信进行解密，以保证短信的私密性和安全性。

#### 3. 短信备份界面 - 自定义控件

(1) 自定义属性：values/attrs.xml
	
	<resources>
		<declare-styleable name="MyCircleProgress">
	        <attr name="progress" format="integer"/>
	        <attr name="max" format="integer"/>
	        <attr name="circleColor" format="color"/>
	        <attr name="progressColor" format="color"/>
	        <attr name="android:background"/>
	    </declare-styleable>
	</resources>

自定义控件的根节点，它有5个属性，分别是设置进度的progress，设置进度最大值的max，设置原形颜色的circleColo，设置进度条颜色的progressColor，设置背景的android:background。

(2) 创建自定义控件类：org/android/securityguard/advance/widget/MyCircleProgress.java

自定义按钮控件

(3) 在布局中使用自定义控件:layout/activity_smsbackup.xml

#### 4. 短信备份：org/android/securityguard/advance/SMSBackupActivity.java

(1) 短信备份帮助类：org/android/securityguard/advance/utils/SMSBackupUtils.java

从短信数据库中读出短信内容，然后将其写入XML文件中保存在本地(内存或SD卡)

### (四) 短信还原

android为了防止第三方软件拦截短信和乱写入短信记录，在4.4之后，设置了只有默认的短信应用才会有权限操作短信数据库，因此本节不成功

#### 1. 短信还原帮助类：org/android/securityguard/advance/utils/SMSRestoreUtils.java

#### 2. 使用子线程读XML内容恢复短信

### (五) 程序锁

程序锁功能主要是将加锁的程序信息存入数据库，当程序锁服务打开时，后台会运行一个服务检查当前打开的程序，如果程序在数据库中说明是加锁的，此时会弹出输入密码界面，只有输入密码正确才能进入应用(这个密码和手机防盗模块中的防盗密码相同)。

#### 1. 数据库及数据库操作类

org.android.securityguard.advance.db.dao.AppLockDao

org.android.securityguard.advance.db.AppLockOpenHelper

#### 2. Fragment和ViewPager

#### 3. ListView

#### 4. 未加锁界面：org/android/securityguard/advance/fragments/AppUnlockFragment.java

当进入程序锁模块时默认显示的是未加锁模块，如果是第一次打开该界面就显示手机上安装的所有应用，当在该界面上将某个应用加锁时，该应用条目将向左滑动消失，并将该应用添加到数据库中(表示该应用已加锁)重新刷新界面。

使用URI注册一个内容观察者，如果数据库内容发生了改变，会执行onChange方法，在该方法中执行fillData()方法刷新界面。

	private Uri uri= Uri.parse("content://org.android.securityguard.applock");
	getActivity().getContentResolver().registerContentObserver(uri, true, new ContentObserver(new Handler()) {
	    @Override
	    public void onChange(boolean selfChange) {
	        fillData();
	    }
	});

#### 5. 加锁界面：org/android/securityguard/advance/fragments/AppLockFragment.java

#### 6. 程序锁服务

通过该服务来获取任务栈的信息来判断当前开启的哪个应用，是否需要弹出密码锁界面。

(1) 开启子线程，实时监视被打开的程序

程序将通过监听任务栈获得的包名与数据库中存储的包名进行对比，如果该包名在数据库中存在，则表示该应用已加锁，开启密码锁界面并将当前打开的应用包名传递过去。

	//创建Intent实例，用来打开输入密码界面
    intent=new Intent(AppLockService.this, EnterPasswordActivity.class);

	private void startAppLockService(){
        new Thread(){
            @Override
            public void run() {
                flag=true;
                while(flag){
                    //监视任务栈的情况，最近打开的任务栈在集合的最前面
                    taskInfos=activityManager.getRunningTasks(1);
                    taskInfo=taskInfos.get(0);  //最近使用的任务栈
                    packageName=taskInfo.topActivity.getPackageName();
                    //判断这个包名是否需要被保护
                    if(packageNames.contains(packageName)){
                        //判断当前应用程序是否需要临时停止保护(输入了正确的密码)
                        if(!packageName.equals(tempStopProtectPackage)){
                            //需要保护，弹出一个输入密码的界面
                            intent.putExtra("packageName", packageName);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

注意，当打开已加锁应用的密码锁界面输入密码后，由于程序锁服务还在监听任务栈信息，会再次弹出密码锁界面。

为了防止这种情况，需要将输入正确密码的应用进行临时取消保护。当在密码锁界面输入完成后会发送一条自定义的广播，传输一个action为org.android.securityguard.applock。

自定义广播接收者，当广播接收到这个action时获取已解锁应用的包名，并在代码中进行判断，如果当前应用包名不是已解锁应用时才打开密码锁界面。这样就防止密码锁界面重复打开。

	Intent intent=new Intent();
    intent.setAction("org.android.securityguard.applock");
    intent.putExtra("packageName", packageName);
    sendBroadcast(intent);

自定义广播接收者中判断屏幕是否锁屏或解屏，当锁屏时停止监控任务栈信息，解锁时再重新监控，这样会节省手机电量，使应用消耗少。

	receiver=new AppLockReceiver();
    IntentFilter filter=new IntentFilter("org.android.securityguard.applock");
    filter.addAction(Intent.ACTION_SCREEN_ON);
    filter.addAction(Intent.ACTION_SCREEN_OFF);
    registerReceiver(receiver, filter);

	class AppLockReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if("org.android.securityguard.applock".equals(intent.getAction())){
                tempStopProtectPackage=intent.getStringExtra("packageName");
            }else if(Intent.ACTION_SCREEN_OFF.equals(intent.getAction())){
                tempStopProtectPackage=null;
                flag=false; //停止监控程序
            }else if(Intent.ACTION_SCREEN_ON.equals(intent.getAction())){
                if(flag==false){
                    startAppLockService();
                }
            }
        }
    }

## 十、 设置中心模块

设置中心模块主要用于设置黑名单拦截是否开启、程序锁是否开启。