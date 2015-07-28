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

