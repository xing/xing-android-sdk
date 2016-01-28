# XING API Client for Java/Android
---

[![Build Status](https://travis-ci.org/xing/xing-android-sdk.svg?branch=master)](https://travis-ci.org/xing/xing-android-sdk)

Add all the power of the XING platform to your app.

This library provides a java implementation of a network layer infrastructure required to access the XING Web Services. 
It's center class the `XingApi` allows users to access resources that reflect the XING API public, documentation for 
which can be found [here](https://dev.xing.com/docs/resources).

## DISCLAIMER

__This library is an evolution of so called "XING Android SDK". The library was rewritten to adapt modern 
approaches of software development and bring a simple and straight forward api. The previous implementation was 
DEPRECATED and is no longer under support. If you are in need for a changes or a bug fix, and for some reason can not 
switch to the new api, feel free to fork this repo and checkout the [1.x branch](https://github.com/xing/xing-android-sdk/tree/1.x).__

How to get Started
==========

### 1.1. For Java projects

Add the XING API Client as a dependency:

Either through Gradle:

```gradle
compile 'com.xing.api:api-client:2.0.0-alpha2'
```
or by using Maven:

```xml
<dependency>
  <groupId>com.xing.api</groupId>
  <artifactId>api-client</artifactId>
  <version>2.0.0-alpha2</version>
</dependency>
```

### 1.2. For Android projects

For Android we made an additional step and implemented an oauth helper library, to simplify **Login with XING** 
implementation in your app. To benefit from this library just add the following dependency in you gradle build file:

```gradle
compile 'com.xing.api:android-oauth:2.0.0-alpha2'
```

Snapshots of the development version are available in [Sonatype's `snapshots` repository](https://oss.sonatype.org/content/repositories/snapshots/com/xing/api/).

### 2. Add OAuth credentials to your project

Add your **OAuth Consumer Key** and **OAuth Consumer Secret** to your project.

The OAuth information can be obtained after registering a new app on <https://dev.xing.com/applications>

#### For Android projects you can add the keys to your *local.properties* file: 

```
	oauth_consumer_secret=xxxxxxxxxxxxxxxxxxxx
	oauth_consumer_key=yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy
	sdk.dir=/path/to/sdk
```

You are also free to provide this information in any other way. For example using *Preferences* or a helper class 
containing the API credentials as CONSTANTS.

### 3. Build your first app (Android Example)

To build your Android app with the XING API Client check out the [Get started page](GETTING_STARTED_ANDROID.md)

Based on Okio
==========

The XING API Client is fully based on the [Okio](https://github.com/square/okio) stack. We use 
[OkHttp](https://github.com/square/okhttp) for networking, [Moshi](https://github.com/square/moshi) for Json parsing,
and the request building/execution system is a fork of [Retrofit](https://github.com/square/retrofit).

Borrows from Retrofit
==========

If you are using [Retrofit](https://github.com/square/retrofit) as an HTTP client, you will find this api to be very 
familiar. <br>
Here are somme key differences and similarities are:

* `XingApi` - This class acts as a key entry point and *resource* provider (Similar to `Retrofit` in Retrofit 2.)
* `Resource` - Namespace/Api provider class. We try to reflect the public [api documentation](https://dev.xing.com/docs/resources)
in such a way that every *resource* section will have it's own `Resource` class implementation.
* `CallSpec<RT, ET>` - Similar to `Call<T>` in Retrofit, but with a few key deference's:
  - `CallSpec` is aware of both return types `RT` for success and `ET` for error responses. And will parse them out 
  of the box.
  - `CallSpec` allows users to alter the underlying request to a certain degree (you can add query or form params, as
   well as completely change the request body. This will become useful for those who want to benefit form optional 
   request parameters accepted by XING WEB Services.
  - In addition to `#execute()` and `#enqueue()` methods `CallSpec` provides `#stream()` and `#rawStream()` if you are
   into RxJava (**WARNING:** usage of these methods will require you to have RxJava in your classpath).

Contributing
==========

## How to contribute
If you have an idea how to improve this library, feel free to either suggest your idea using the issues, 
or send us a Pull Request. If you do the latter please make sure you observe the rules for contributing below.

1. Fork [this](https://github.com/xing/xing-android-sdk) repository.
2. Create your feature branch (git checkout -b my-new-feature).
3. Commit your changes (git commit -am 'Add some feature').
4. Make sure to cover your new feature with sufficient unit tests.
6. Create a new Pull Request.
7. Wait for us to review your feature, discuss internal and eventually accept your Pull Request.
8. Enjoy using your feature inside the XING API Client.

## Contact
If you have any problems or feedback, feel free to contact us:

* [@xingdevs](https://twitter.com/xingdevs)
* <https://dev.xing.com>

Dependencies
==========
* [OkHttp](https://github.com/square/okhttp)
* [Moshi](https://github.com/square/okhttp)
* [RxJava](https://github.com/ReactiveX/RxJava) (Required if you use `#stream()` or `#rawStream()`).
* [Signpost](https://github.com/mttkay/signpost) (Required by `android-oauth` artifact)

License
==========


  	Copyright (C) 2015 XING AG (http://xing.com/)
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
       http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.



