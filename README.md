## 前言：
本文是小编根据一位前辈封装的mvvm框架加入了自己的理解提炼的mvvm的base基类，单纯的为了方便，不需要再花时间手写base类，如有不足，欢迎大家留言，督促我改进代码。废话不多说，进入正文。
## 正文：
### 导入依赖：
application的build.gradle
```
 	maven { url 'https://jitpack.io' }
```
module的build.gradle

```
	android{
		dataBinding {
	    	    enabled = true
	    	}
	    }
```
别着急，还有

```
	    implementation 'com.github.xiangshu957:mvvm:0.0.7'
	
	    //okhttp、retrofit、rxjava
	    implementation 'com.squareup.okhttp3:okhttp:3.10.0'
	    implementation 'com.squareup.retrofit2:retrofit:2.3.0'
	    implementation 'com.squareup.retrofit2:converter-gson:2.3.0'
	    implementation 'com.squareup.retrofit2:adapter-rxjava2:2.3.0'
	    implementation 'io.reactivex.rxjava2:rxjava:2.1.7'
	
	    //解决RxJava内存泄漏，但在联网请求的时候使用了liveData，其实已经解决了。观察事件不能取消。
	    //同时在单独使用rxJava的时候还是要用。比如在启动页，还未去主页面，手动点击返回键的时候有效
	    implementation 'com.trello.rxlifecycle2:rxlifecycle-components:2.1.0'
	    implementation 'androidx.lifecycle:lifecycle-extensions:2.0.0'
```
行了，赶紧锤一下你的项目吧，锤完咱们继续
### application中初始化

```
		//context，sp文件的名字
        PreferenceUtil.getInstance().init(this, "test_sp");
        //baseUrl,api接口
        RetrofitManager.getInstance(SysCommon.BASE_URL, RetrofitApi.class);
```
### 实体类模板
这是一个实体类的模板，ResponseMode中已经有data、errorCode、errorMsg三个属性，分别对应返回的实体类对象、结果码、结果信息，因为不同的接口可能使用的字段不一样，所以需要重写setData()、setErrorCode()、setErrorMsg()以及isSuccess()方法，这样就可以适配各种网络请求可能会出现的场景了，切记一定要重写，不然可能会导致取不到值。
```java
		public class ResponseModelCustom<T> extends ResponseModel<T> {
		
		    private int ret;
		    private T dataInfo;
		    private int errorType;
		    private String remark;
		
		    public void setDataInfo(T dataInfo) {
		        this.dataInfo = dataInfo;
		    }
		
		    public T getDataInfo() {
		        return getData();
		    }
		
		    public int getRet() {
		        return ret;
		    }
		
		    public void setRet(int ret) {
		        this.ret = ret;
		    }
		
		    public int getErrorType() {
		        return errorType;
		    }
		
		    public void setErrorType(int errorType) {
		        this.errorType = errorType;
		    }
		
		    public String getRemark() {
		        return remark;
		    }
		
		    public void setRemark(String remark) {
		        this.remark = remark;
		    }
		
		    @Override
		    public boolean isSuccess() {
		        return super.isSuccess();
		    }
		
		    @Override
		    public T getData() {
		        if (dataInfo != null) {
		            return dataInfo;
		        }
		        return super.getData();
		    }
		}

```
### RepositoryImpl网络请求都写到这里

```java
		public class RepositoryImpl extends BaseModel {
		
		    //这里放的是所有的网络请求操作
		
		    public MutableLiveData<Resource<List<TestBean>>> getData(){
		        MutableLiveData<Resource<List<TestBean>>> liveData = new MutableLiveData<>();
		        //observeGo()是BaseModel中封装的方法，大家可以点进去看看
		        return observeGo(((RetrofitApi)getApiService()).getData(),liveData);
		    }
		
		}
```
### viewModel获取数据
这里写的就是单独的activity或者fragment所需要的获取数据的方法
```java
		public class MainViewModel extends BaseViewModel<RepositoryImpl> {
		
		    public MainViewModel(@NonNull Application application) {
		        super(application);
		    }
		
		    @Override
		    protected RepositoryImpl createRepository(RepositoryImpl repository) {
		        if (repository == null) {
		            repository = new RepositoryImpl();
		        }
		        return repository;
		    }
		
		    public MutableLiveData<Resource<List<TestBean>>> getData() {
		        return ((RepositoryImpl) getRepository()).getData();
		    }
		}
```
### Activity中的业务逻辑
activity继承BaseActivity并通过泛型传入对应的ViewModel和Binding，然后通过mViewModel获取到的liveData监测数据的改变，从而更新Ui。
```java
		public class MainActivity extends BaseActivity<MainViewModel, ActivityMainBinding> {
		
		
		    @Override
		    protected int getContentViewId() {
		    	//获取布局文件
		        return R.layout.activity_main;
		    }
		
		    @Override
		    protected void processLogic() {
		    	//业务逻辑
		        mViewModel.getData().observe(this, listResource -> {
		            listResource.handler(new OnCallback<List<TestBean>>() {
		
		                @Override
		                public void onSuccess(List<TestBean> data) {
		                    String ser = GsonUtils.ser(data);
		                    LogUtils.e("zrx", ser);
		                }
		
		                @Override
		                public void onFail(String msg) {
		                    super.onFail(msg);
		                    LogUtils.e("zrx", msg);
		                }
		            });
		
		        });
		    }
		
		    @Override
		    protected void setListener() {
				//设置监听
		    }
		
		    @Override
		    protected void showDialog(String msg) {
				//展示弹框提示
		    }
		
		    @Override
		    protected void hideDialog() {
				//隐藏弹框
		    }
		
		    @Override
		    protected void registerBroadCast() {
				//注册广播
		    }
		
		    @Override
		    protected void unRegisterBroadCast() {
				//注销广播
		    }
		}
```

[源码地址](https://github.com/xiangshu957/mvvm.git)

## 结语
本次分享到此就结束了，有些地方还在考虑如何修改中，后续会陆续跟大家见面的，如有建议，请留言