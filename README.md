# 一个关于三方功能的类库
# 淘宝api使用方法
## step1:在全局build.gradle中添加(注：记住添加淘宝的安全图片)
```
allprojects {
		repositories {
			...
		maven {
                url "http://repo.baichuan-android.taobao.com/content/groups/BaichuanRepositories/"
            }
	}
}
```

## step2:在工程中添加依赖
```
dependencies {
	   implementation 'com.github.sl2413:MyTreeSdk:目标版本号'
}
```

## step3:在application中或者在MainActivity的onCreate中进行淘宝api初始化
```
TaoBaoInit.Init(getApplication());
```

## step4:在需要的地方调用相应的api方法，例如：授权登录
```
TaoBaoLogin.Login(new TaoBaoLogin.TaoBaoLoginCallBack() {
    @Override
    public void onSuccess(int code, String openId, String nickName) {
        Log.e("TAG","获取淘宝用户信息: " + TaoBaoLogin.getTaoBaoUserInfo());
    }
});
```

