# 项目描述  
一个简易的扫码签到系统。在教师端生成二维码，二维码每五秒钟刷新一次，防止截图代签。学生在手机中扫描二维码并向后端发送请求，并在数据库中保存签到信息。
# 实现方法
使用了YXING库生成二维码以及调用摄像头权限，使用OKHttp库发送请求处理响应。当用户扫描二维码时向后端发送请求，后端处理请求。
## YXING
[YXING](https://github.com/amggg/YXing)
### 使用前准备
在setting.gradle中的dependencyResolutionManagement中添加jitpack依赖。
```
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```
在build.gradle中的dependencies中添加YXing库。
```
implementation 'com.github.amggg:YXing:V2.0.1'
```
在AndroidManifast.xml中向系统申请摄像头权限。
```
<uses-permission android:name="android.permission.CAMERA" />
<uses-feature android:name="android.hardware.camera" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.MANAGE_EXTERNAL_STORAGE" />
```
### 如何使用
**向用户申请使用摄像头的权限**
新建Permission.java
```
package com.example.signapp.ui.scan;
 
import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
 
 
 
 
//用于授权相机权限
public class Permission {
    public static final int REQUEST_CODE = 5;
    //定义三个权限
    private static final String[] permission = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    //每个权限是否已授
    public static boolean isPermissionGranted(Activity activity){
        if(Build.VERSION.SDK_INT >= 23){
            for(int i = 0; i < permission.length;i++) {
                int checkPermission = ContextCompat.checkSelfPermission(activity,permission[i]);
                /***
                 * checkPermission返回两个值
                 * 有权限: PackageManager.PERMISSION_GRANTED
                 * 无权限: PackageManager.PERMISSION_DENIED
                 */
                if(checkPermission != PackageManager.PERMISSION_GRANTED){
                    return false;
                }
            }
            return true;
        }else{
            return true;
        }
    }
 
    public static boolean checkPermission(Activity activity){
        if(isPermissionGranted(activity)) {
            return true;
        } else {
            //如果没有设置过权限许可，则弹出系统的授权窗口
            ActivityCompat.requestPermissions(activity,permission,REQUEST_CODE);
            return false;
        }
    }
}
```
之后直接在onCreate或onCreateView中调用checkPermission(Activity activity)方法即可。
```
@Override
public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan, container, false);
        
 
        appCompatActivity = (AppCompatActivity) getActivity();
 
        Permission.checkPermission(appCompatActivity);
 
}
 
 
//在授权的activity或fragment中可以重写这个方法，如果用户拒绝授权就退出应用
@Override
public void onRequestPermissionsResult(
        int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == Permission.REQUEST_CODE) {
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                Log.e("Permission","授权失败！");
                // 授权失败，退出应用
                appCompatActivity.finish();
                return;
            }
        }
    }
}
```
**打开扫码界面**
接着在对应的点击事件中打开扫码界面，一些具体的参数可以参考文档：[Yxing集成文档](http://18390826440.3vkj.club/#/integraedpage)
```
appCompatActivity = (AppCompatActivity) getActivity();
ScanFragment = getParentFragment();
 
@Override
            public void onClick(View v) {
              
                    //如果是在activity中调用就不用加fragment参数了
                    ScanCodeConfig.create(appCompatActivity,ScanFragment )
                            .setStyle(ScanStyle.WECHAT )
                            .setPlayAudio(false)
                            .setShowFrame(true)
                            .buidler()
                            .start(ScanCodeActivity.class);
                    
}
```
**接收二维码中带的数据**
接受数据很大一部分都是为了去发送Http请求，但我这里的需求比较简单，只需要获取码里的信息就好了。
如果是在fragment中打开的扫码界面，信息最终还是会返回到fragment所在的activity中去，所以在activity中重写onActivityResult。
```
 @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            
 
            //接收扫码结果
            Bundle extras = data.getExtras();
            //扫到码的类型
            int codeType = extras.getInt(ScanCodeConfig.CODE_TYPE);
            //码中的数据
            QRCodeValue = extras.getString(ScanCodeConfig.CODE_KEY);
}
}
```
### 生成二维码
```
bitmap = ScanCodeConfig.createQRCode(dateString);
imageView.setImageBitmap(bitmap);
```
**这里要说的是一个比较有意思的需求：二维码要求每五秒刷新一次，同时还要携带时间等信息。**
思路比较简单就是每五秒获取一次时间戳，转化为字符串生成二维码。我在做的时候遇到的问题是关于UI线程的问题，正常一个activity只有一个线程就是UI线程，如果你直接在UI线程中做这个生成二维码的操作就会导致UI不能刷新，甚至app也会崩溃，所以需要新建线程来执行这些费时操作，同时更新二维码图标的操作通过handler用post方法给Runnable推到UI线程中去。
```
//在onCreate中获取主线程
handler = new Handler(); 
 
 
 
 
//当点击开始生成二维码时
        @Override
        public void onClick(View v) {
            
        //通过thread_stop来决定循环什么时候结束，如果直接使用thread的stop方法的话很不安全
            thread_stop = true;
            starDate = new Date();
 
            //5秒刷新一次
            final long timeInterval = 5000;
 
            imageView.setVisibility(View.VISIBLE);
            
 
            //新建线程用于生成二维码并更新UI
            Thread t  = new Thread() {
 
 
                @Override
                public void run() {
 
                    while (thread_stop ){
 
 
                        Date date = new Date();
                        
 
                        String dateString = date.toString();
                        bitmap = ScanCodeConfig.createQRCode(dateString);
                        handler.post(runnable);
 
                        try {
                            sleep(timeInterval);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
 
                    }
 
                    if(!thread_stop){
                        handler.post(UIrun);
                    }
 
                }
 
            };
            t.start();
 
 
 
 //手动结束线程的方法
        public void stopThread() {
            thread_stop = false;
            Log.d("Thread","ThreadStop");
        }
 
 
 
//更新二维码
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            imageView.setImageBitmap(bitmap);
        }
    };
```
