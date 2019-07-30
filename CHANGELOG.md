Change Log
==========

## Version 3.1.0 *(2019-07-30)*

### Breaking‼️:

* [Remove XingApi's resource function](https://github.com/xing/xing-android-sdk/pull/245)
  * See `GETTING_STARTED_ANDROID` for usage. The reasons for this change are:
    * `XingApi` is a dependency of any `Resource`, therefore it doesn't make sense that the former provides or holds a
    Resource in any way.
    * `XingApi` also used to hold a cache of resources, this could potentially cause problems if an implementation would
    contain any mutable state inside, as it would be propagated to further calls to the `resource` function.
    * Finally, this function used to sometimes rely on reflection, which is not mentioned anywhere and leads to runtime
    crashes when the target implementation Proguard's strips out the resources.

## Version 3.0.7 *(2019-01-31)*

* Update moshi to 1.8.0
* Update minSdk to 21
* Update targetSdk to 28
* Update supportLib to 28.0.0
* Update build tools to 28.0.3
* Update kotlin version to 1.3.20

## Version 3.0.6 *(2019-01-10)*

* [Allow setting a hostname verifier](https://github.com/xing/xing-android-sdk/pull/242)

## Version 2.0.1 *(2016-11-16)*

 * Fix internal parsing mechanism to work with moshi 1.2.0 and higher.
 * Rename `XingApi#converter()` to `XingApi#moshi()`

## Version 2.0.0 *(2016-09-27)*

 * Initial Release of the XING API Client Version 2.0.0 