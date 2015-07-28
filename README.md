# XING Android SDK
---
Add all the power of the XING platform to your Android app using the XING Android SDK.

This library provides an Android implementation of the network layer infrastructure required to access
the XING Web Services. Basically this SDK provides a standalone RequestExecutor, Module, Tasks and requests, which reflect the public XING API which can be found [here](https://dev.xing.com/docs/resources).

#### Currently the following resources are available as Tasks in the XING Android SDK:

- [User Profiles](<https://dev.xing.com/docs/resources#user-profiles>)
- [Contacts](<https://dev.xing.com/docs/resources#contacts>)
- [Profile Visits](<https://dev.xing.com/docs/resources#profile-visits>)
- [Contact Requests](<https://dev.xing.com/docs/resources#contact-requests>)
- [Contact Path](<https://dev.xing.com/docs/resources#contact-path>)


How to get Started
==========

## 1. Add the SDK to your project

Add the SDK as a dependency:

Either through Gradle:

```gradle
compile 'com.xing.android:sdk:0.9.0'
```
or by using Maven:

```xml
<dependency>
  <groupId>com.xing.android</groupId>
  <artifactId>sdk</artifactId>
  <version>0.9.0</version>
</dependency>
```

## 2. Add OAuth credentials to your project

Add your **OAuth Consumer Key** and **OAuth Consumer Secret** to your *local.properties* file.

The OAuth information can be obtained after registering a new app on <https://dev.xing.com/applications>

The file should then look something like this:

**local.properties**

```java
	oauth_consumer_secret=xxxxxxxxxxxxxxxxxxxx
	oauth_consumer_key=yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy
	sdk.dir=zzz
```

You are also free to provide this information in any other way. For example using *Preferences* or a helper class containing the OAuth credentials in CONSTANTS.

## 3. Build your first app


### How to log in:

In order to be able to request data from the SDK the user needs to log in using his XING credentials.

1. Create a **LoginActivity** where your user can login and authenticate by using his XING credentials. Preferably after pressing a "Login with XING" button.

2. Use the **LoginHelper** class from the SDK, which provides a static `login()` method that takes the OAuth Consumer Key and Secret and passes it to the **LoginHelper** which authenticates the user with the XING Services.

3. Override the `onActivityResult()` method inside your **LoginActivity** and handle the request code: `OauthCallbackActivity.REQUEST_CODE`.

4. If the result code for this request is `Activity.RESULT_OK` you can unwrap the data inside the intent object to get the `OauthAuthenticatorHelper.TOKEN` and the `OauthAuthenticatorHelper.TOKEN_SECRET`.

5. Be sure to afterwards initialize the XingController using the received  token and the  token secret inside your Application class <br>
NOTE: This should be executed after every login, or when the application is started and the XingController has not been initialized using the `setup()` method.

### Initializing the XingController:

Initialization of the **XingController** should be done in the Application class of your application. It can be done after a user logs in or when starting your application. For example when the user has logged in before and you saved the  token and the according secret.

**SDKSampleApplication.java**

 ```java
@Override
public void onCreate() {
	app = this;
    prefs = Prefs.getInstance(app);
    if (Utils.isLoggedIn(this) && !xingRequestControllerInitiated) {
    	initializeXingRequestController();
    }
    super.onCreate();
}
```

This shows the initialization of the XingController if a user is already logged in and the XingController has not been initialized yet.
This usually is executed when the application is started. The **Utils** class has the helper method `isLoggedIn()` which just checks if there is an  token existing inside the user's preferences. Since this value should only be set after logging in successfully this can be seen as a *logged-in state*.


Below you can see how the initialization of the XingController is handled. So, basically the `setup()` method is called and all of the available  information is set. Finally the `init()` method must be executed to start the initialization process.

**SDKSampleApplication.java**

```java
    /**
     * Initializing the XingController using the  Token and the Secret
     */
    private void initializeXingRequestController() {
        XingController.setup()
                .setConsumerKey(BuildConfig.OAUTH_CONSUMER_KEY)
                .setConsumerSecret(BuildConfig.OAUTH_CONSUMER_SECRET)
                .setToken(prefs.getOauthToken())
                .setTokenSecret(prefs.getOauthSecret())
                .init();
        xingRequestControllerInitiated = true;
    }
```

In order to log the user in after he successfully authenticated using his XING credentials we need a callback that starts the process of initializing the XingController. The full OAuth information is only available after the user has logged in sucessfully.
So, after saving the OAuth token and the OAuth token secret inside the Login Activity, the sample application uses a static method of the Application class notifying the application that the user has logged in and the XingController can now be initialized.

**LoginActivity.java - onLoginActivityResult()**

```java
	SDKSampleApplication.getInstance().onUserLoggedIn();
```

**SDKSampleApplication.java**

```java
    /**
     * Handle the initialization process after the user logged in
     * */
    public void onUserLoggedIn() {
        if (!xingRequestControllerInitiated) {
            initializeXingController();
        }
    }
```

As you can see in the code example above the callback just executes the same method for initalizing the XingController like the app does in the `onCreate()` method of the Application class.

### Execute Requests using the XingController:

After the user logs in to your app and the XingController has been initialized successfully you are ready to request data. In your application you would request the data as follows:

**SomeActivity.java**

```java
	// Create the Task you want to execute
	TASK_TO_BE_EXECUTED myTask = new TASK_TO_BE_EXECUTED([REQUIRED ARGUMENTS], OnTaskFinishedListener<T>);
	XingController.getInstance().executeAsync(myTask)
```

The Activity that wants to execute a certain task to get data should implement the appropriate **OnTaskFinishedListener** that you need for your request. Alternatively, you can also specify to use a new **OnTaskFinishedListener** directly when executing the request as an *inner anonymous class*.

### Example: Requesting the user's profile

When the **ProfileActivity** is started we want to load the user’s profile and display it.

**ProfileActivity.java - onCreate()**

```java
	MeTask meTask = new MeTask(null, this, this);
    XingController.getInstance().executeAsync(meTask);
```

Remember to cancel the execution of your requests when the activity is stopped.

**ProfileActivity.java - onStop()**

```java
 @Override
    protected void onStop() {
        super.onStop();
        XingController.getInstance().cancelExecution(this);
    }
```

In this example the ProfileActivity implements the **OnTaskFinishedListener<XingUser>** interface. This forces you to implement the two methods shown below:

**ProfileActivity.java - onSuccess()**

```java
	@Override
    public void onSuccess(@Nullable XingUser resultUser) {
        if (resultUser != null) {
            //Do Stuff with the User Object e.g. save the ID into the preferences, update the views to display some info etc.
          }
            //After the request was succesfully executed maybe display some of the gathered data to the user
            ...
        }
```

**ProfileActivity.java - onError()**

```java
    @Override
    public void onError(Exception exception) {
        //Do what ever needs to be done to handle the error
    }
```


## Contributing


If you have an idea how to improve the XING Android SDK, feel free to either suggest your idea using the issues, or send us a Pull Request. If you do the latter please make sure you observe the rules for contributing below.

1. Fork [this](https://github.com/xing/xing-android-sdk) repository
2. Create your feature branch (git checkout -b my-new-feature)
3. Commit your changes (git commit -am 'Add some feature')
4. Make sure to cover your new feature with sufficient unit tests
5. Push to the branch (git push origin my-new-feature)
6. Create new Pull Request
7. Wait for us to review your feature, discuss internal and eventually accept your Pull Request
8. Enjoy using your feature inside the XING Android SDK


## Contact
If you have any problems or feedback, feel free to contact us:

* [@xingdevs](https://twitter.com/xingdevs)
* <https://dev.xing.com>

## Libraries used


* OkHttp <https://github.com/square/okhttp>
* Signpost <https://github.com/mttkay/signpost>


## License


  	Copyright (c) 2015 XING AG (http://xing.com/)

	Permission is hereby granted, free of charge, to any person obtaining a copy
   	of this software and associated documentation files (the "Software"), to deal
   	in the Software without restriction, including without limitation the rights
   	to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
   	copies of the Software, and to permit persons to whom the Software is
   	furnished to do so, subject to the following conditions:

  	The above copyright notice and this permission notice shall be included in
  	all copies or substantial portions of the Software.

  	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
  	THE SOFTWARE.



