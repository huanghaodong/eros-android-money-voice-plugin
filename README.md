

**集成方式**

* 进入Android目录`工程目录/platforms/android/WeexFrameworkWrapper/` 目录下 clone 对应的插件。

``` java
git clone https://github.com/huanghaodong/eros-android-money-voice-plugin.git "voiceplugin"
```



* 打开Android目录`工程目录/platforms/android/WeexFrameworkWrapper/`,编辑`settings.gradle`,添加引入。
在`settings.gradle` 中 添加如下代码。

``` java
//这里只需要在最后添加 , ':voiceplugin'
include ':app',':sdk',':nexus', ':wxframework', ':voiceplugin'  

// chooseCity
project(':voiceplugin').projectDir = new File(settingsDir,'/voiceplugin')

```

* 打开Android目录`工程目录/platforms/android/WeexFrameworkWrapper/app`,编辑app目录下`build.gradle` 文件 `dependencies` 下添对应 插件引用。

``` java
	dependencies {
		....
		compile project(':voiceplugin')
	}
```
* 最后记得点击右上角sync now




## 使用

* 引用Module

	```js
	const Voice = weex.requireModule('voice');
	```
	
* api
##### 参数说明

| name | type | required | Description |
| ------ | ------ | ------ | ------ |
| money | String | true | 金额 |
| payType | String | true | 收款商,'alipay,wxpay,fdpay' |




	```js
  	Voice.play({
        money: '5.2',
        payType: 'wxpay'
      })
  ```


## 更新日志
 
 ## 如果觉得有用，请给个start，谢谢
