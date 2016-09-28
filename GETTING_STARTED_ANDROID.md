## 3. Build your Android app with XING API Client

### User login:

In order to be able to request data from the SDK the user needs to login using his(her) XING credentials.

1. Create a **LoginActivity** where your user can login and authenticate by using his XING credentials. Preferably after pressing a "Login with XING" button.

2. Use the `XingOauthActivity` class from the `android-oauth` artifact, which provides a static `#startOauthProcess()` 
method that takes the OAuth Consumer Key and Secret and starts a new activity which authenticates the user with the 
XING Services.

3. Override the `onActivityResult()` method inside your **LoginActivity** and handle the request code: 
`XingOauthActivity.REQUEST_CODE`.

4. If the result code for this request is `Activity.RESULT_OK` you can unwrap the data inside the intent object to get 
the `XingOauthActivity.TOKEN` and the `XingOauthActivity.TOKEN_SECRET`.

5. Be sure to afterwards initialize the `XingApi` using the received token and the token secret inside your Application 
or any other container class.<br>
**NOTE:** This should be executed after every login, or when the application is started and the `XingApi` has not 
been initialized.

### Initializing XingApi:

Initialization of the `XingApi` should be done in the Application or any other container class of your application. 
It can be done after a user logs in or when starting your application.

```java
    XingApi api = new XingApi.Builder()
        .consumerKey("YOUR_CONSUMER_KEY")
        .consumerSecret("YOUR_CONSUMER_SECRET")
        .accessToken("USERS_ACCESS_TOKEN")
        .accessSecret("USERS_ACCESS_SECRET")
        .build();
```
*We advice you not to store the users credentials and pass the user through the login flow each time the token expires.* 

**IMPORTANT** The XING API Client does NOT store your api credentials or the users access token/secret. So 
this data will be lost when the `XingApi` instance is garbage collected. You should manage the object as a singleton
and try to avoid initializing it multiple times during your applications lifecycle. We recommend to keep the instance
inside your custom `Application` class (see android sample). Or use a Dependency Injection framework to do the heavy 
lifting for you (see [Dagger](https://github.com/square/dagger) and [Dagger 2](https://github.com/google/dagger)).

### Access Resources/Execute Requests using XingApi:

After the user is logged in in to your app and the `XingApi` has been initialized successfully you are ready to request 
data. For example, let's assume that you need to access the users profile data:


```java

    // First you need to select the resource you want to access.
    UserProfileResource resource = xingApi.resource(UserProfileResource.class);
      
    // Get the spec for the user profile request.
    CallSpec<XingUser, HttpError> spec = resource.getOwnProfile();
```

Depending on your use case, you can execute the underlying request in several ways:

*Synchronously*

```java
    Response<XingUser, HttpError> response = spec.execute();
```

*Asynchronously*

```java
    
    spec.enqueue(new Callback<XingUser, HttpError>() {
        @Override
        void onResponse(Response<XingUser, HttpError> response) {
            // Process response here.
        }
        
        @Override    
        void onFailure(Throwable t) {
            // Called on network or parsing failure.
        }
    }
```

*Reactive (Requires RxJava as a dependency)*

```java

    spec.stream()
        .subscribe(user -> {
            // This will be only invoked in case of a success response.
        }, throwable -> {
            // All errors will land here.
        });
```

For more information visit the `XingApi` documentation [page](http://xing.github.io/xing-android-sdk/javadocs/2.0.0/).
