# ORCommon
A common library in some cases useful behavior for Android App.

# What can?
Read code in [Sample module](Sample) for part of the sample how to use.
### [BitmapCache](libraries/ORCommon/src/main/java/me/b0ne/android/orcommon/BitmapCache.java)
This class is implements ImageLoader.ImageCache that you should use with ImageLoader.  
You can see sample in [ImageListAdapter](Sample/src/main/java/me/b0ne/android/orcommonsample/ImageListAdapter.java) and [ImageUtils](libraries/ORCommon/src/main/java/me/b0ne/android/orcommon/ImageUtils.java#L310).

### [CDialogFragment](libraries/ORCommon/src/main/java/me/b0ne/android/orcommon/CDialogFragment.java)
This is simple extends DialogFragment class.

### [KVStorage](libraries/ORCommon/src/main/java/me/b0ne/android/orcommon/KVStorage.java)
The simple key-value storage it use with SharedPreferences.  
See sample for use this class in [UtilsFragment](Sample/src/main/java/me/b0ne/android/orcommonsample/UtilsFragment.java#L32).

### [SimpleRequest](libraries/ORCommon/src/main/java/me/b0ne/android/orcommon/SimpleRequest.java)
This is class volley.Request extends. You can do easy and simple request http.

### [Utils](libraries/ORCommon/src/main/java/me/b0ne/android/orcommon/Utils.java)
Read source code you know what it can and see sample in [UtilsFragment](Sample/src/main/java/me/b0ne/android/orcommonsample/UtilsFragment.java).

# Build .jar file
Build .jar file by gradle. You can see setting in [build.gradle of library](libraries/ORCommon/build.gradle)

```sh
$ git clone https://github.com/ng-b0ne/MaterialSample MaterialSample
$ cd MaterialSample/libraries/ORCommon
$ ../../gradlew makeJar
$ ll jar-file
```

# License

    Copyright 2015 bone

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

