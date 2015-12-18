# XING Android SDK
---

[![Build Status](https://travis-ci.org/xing/xing-android-sdk.svg?branch=master)](https://travis-ci.org/xing/xing-android-sdk)

Add all the power of the XING platform to your Android app using the XING Android SDK.

This library provides an Android implementation of the network layer infrastructure required to access
the XING Web Services. Basically this SDK provides a standalone RequestExecutor, Module, Tasks and requests, which reflect the public XING API which can be found [here](https://dev.xing.com/docs/resources).

## DISCLAIMER

__Curently version 2.0 is under heavy development. This version may change/deprecate some API's currently present in the library. For more information please checkout the [github issues page](https://github.com/xing/xing-android-sdk/issues).__

#### Currently the following resources are available as Tasks in the XING Android SDK:

- [User Profiles](https://dev.xing.com/docs/resources#user-profiles)
- [Contacts](https://dev.xing.com/docs/resources#contacts)
- [Profile Visits](https://dev.xing.com/docs/resources#profile-visits)
- [Contact Requests](https://dev.xing.com/docs/resources#contact-requests)
- [Contact Path](https://dev.xing.com/docs/resources#contact-path)


How to get Started
==========

## 1. Add the SDK to your project

Add the SDK as a dependency:

Either through Gradle:

```gradle
compile 'com.xing.android:sdk:1.0.1'
```
or by using Maven:

```xml
<dependency>
  <groupId>com.xing.android</groupId>
  <artifactId>sdk</artifactId>
  <version>1.0.1</version>
</dependency>
```

Snapshots of the development version are available in [Sonatype's `snapshots` repository](https://oss.sonatype.org/content/repositories/snapshots/com/xing/android/sdk/).

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

# 3. Build your first app

To build your first app with the XING Android SDK check out the [Get started page](GETTINGSTARTED.md)

Contributing
==========

## How to contribute
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
* [OkHttp](https://github.com/square/okhttp) by Square
* [Signpost](https://github.com/mttkay/signpost) by Matthias Käppler


## License


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



