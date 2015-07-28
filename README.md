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

# 3. Build your first app

To build your first app with the XING Android SDK check out the [Get started page](GETTINGSTARTED.md)

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



