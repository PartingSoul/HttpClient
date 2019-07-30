[TOC]

### 1. 特性

- 支持Get与Post同步和异步请求
- 支持文件上传，提交Json，提交表单数据
- 线程池管理线程
- 错误重置机制
- 支持取消请求任务



### 2. 使用

#### 2.1 导入依赖

```groovy
 implementation 'com.parting_soul.http:httpClient:+'//推荐使用具体版本号，release中可以看到
```

#### 2.2 Get异步请求

```java
HttpClient httpClient = new HttpClient();
httpClient.get(request, new OnHttpCallback() {

    @Override
    public void onStart(Disposable disposable) {
        // 在主线程中调用，保存任务的引用，在界面销毁取消任务，避免内存泄漏引发的空指针问题
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void onSuccess(Response response) {
        //请求成功
    }

    @Override
    public void onFailed(int code, String error) {
        //请求失败
    }
});
```

#### 2.3 Get同步请求

```java
HttpClient httpClient = new HttpClient();
Response response = httpClient.get(request);
```

#### 2.4 Post异步提交表单

```java
final FormRequest request = new FormRequest("xxxxxxx url");
HttpClient httpClient = new HttpClient();
httpClient.post(request, new OnHttpCallback() {

    @Override
    public void onStart(Disposable disposable) {
        // 在主线程中调用，保存任务的引用，在界面销毁取消任务，避免内存泄漏引发的空指针问题
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void onSuccess(Response response) {
        //请求成功
    }

    @Override
    public void onFailed(int code, String error) {
        //请求失败
    }
});
```

#### 2.5 Post异步提交Json

```java
JsonRequest request = new JsonRequest("xxxx url", json);
HttpClient httpClient = new HttpClient();
httpClient.post(request, new OnHttpCallback() {

    @Override
    public void onStart(Disposable disposable) {
        // 在主线程中调用，保存任务的引用，在界面销毁取消任务，避免内存泄漏引发的空指针问题
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void onSuccess(Response response) {
        //请求成功
    }

    @Override
    public void onFailed(int code, String error) {
        //请求失败
    }
});
```

#### 2.6 Post异步上传文件

```java
MultipartRequest request = new MultipartRequest("xxxx url");
        request.addFilePair("avatarFile", new FilePair(PIC_PATH))
                .addParam("nickname", "nickname" + Math.random())
                .addParam("sex", 0);

HttpClient httpClient = new HttpClient();
httpClient.post(request, new OnHttpCallback() {

    @Override
    public void onStart(Disposable disposable) {
        // 在主线程中调用，保存任务的引用，在界面销毁取消任务，避免内存泄漏引发的空指针问题
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void onSuccess(Response response) {
        //请求成功
    }

    @Override
    public void onFailed(int code, String error) {
        //请求失败
    }
});
```

#### 2.7 添加Header

全局配置

```java
Map<String, String> headers = new HashMap<>();
headers.put("machine", "xxxx");
headers.put("device", Build.MODEL);
headers.put("terminal", "0");
headers.put("app", App.getContext().getPackageName());
headers.put("version", "1.0");
sHttpClient.setHeaders(headers);
```

单次请求配置(注：单次请求配置的优先级会高于全局配置)

```java
final FormRequest request = new FormRequest("xxxx url");
        request.addParam("ip", "xxxx")
                .addHeader("app", "app")
                .addHeader("machine", "machine");
```

#### 2.8 设置请求配置

- 支持设置连接超时时间与读取超时时间
- 支持设置错误重试次数以及重试延迟时间

```java
 // 可自行配置，不配置则使用默认配置
HttpConfig httpConfig = new HttpConfig();
httpConfig.setRetryCount(2);
httpConfig.setRetryDelayTime(2000);
httpConfig.setConnectTimeout(8 * 1000);
httpConfig.setReadTimeout(8 * 1000);
sHttpClient.setHttpConfig(httpConfig);
```

