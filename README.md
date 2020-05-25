# flutter_huawei_push
flutter华为推送 集成2.6版本

## Getting Started

### 导入到flutter的yaml文件
```
  flutter_huawei_push:
    git:
      url: https://github.com/a47781402/flutter_huawei_push.git
```
### 在android目录添加华为推送配置
#### 配置权限
```
<!-- 接收PUSH TOKEN的广播以及PUSH消息需要定义该权限 com.example.flutter_huawei_push_example 要替换上您应用的包名 -->
    <permission
        android:name="com.example.flutter_huawei_push_example.permission.PROCESS_PUSH_MSG"
        android:protectionLevel="signatureOrSystem"/>

    <!--接收PUSH TOKEN的广播以及PUSH消息需要定义该权限 com.example.flutter_huawei_push_example 要替换上您应用的包名 -->
    <uses-permission android:name="com.example.flutter_huawei_push_example.permission.PROCESS_PUSH_MSG" />
```
#### 配置application
```
<!--102178463要替换上您应用的appid -->
<meta-data
            android:name="com.huawei.hms.client.appid"
            android:value="appid=102178463" />
        <!-- 接入HMSSDK 需要注册的provider，authorities 一定不能与其他应用一样，所以这边 com.example.flutter_huawei_push_example 要替换上您应用的包名
            Access HMSSDK need to register provider,authorities must not be the same as other applications, so this side ${package_name} to replace the package name you applied-->
        <provider
            android:name="com.huawei.hms.update.provider.UpdateProvider"
            android:authorities="com.example.flutter_huawei_push_example.hms.update.provider"
            android:exported="false"
            android:grantUriPermissions="true"/>

        <!-- 接入HMSSDK 需要注册的provider，authorities 一定不能与其他应用一样，所以这边 com.example.flutter_huawei_push_example 要替换上您应用的包名
            Access HMSSDK need to register provider,authorities must not be the same as other applications, so this side ${package_name} to replace the package name you applied-->
        <provider
            android:name="com.huawei.updatesdk.fileprovider.UpdateSdkFileProvider"
            android:authorities="com.example.flutter_huawei_push_example.updateSdk.fileProvider"
            android:exported="false"
            android:grantUriPermissions="true">
        </provider>
```
### 初始化华为推送
打开android目录下的MainActivity,在onCreate方法里使用
java
```
        HMSAgent.init(this);

```
