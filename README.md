# BX-Image-Cropper-Android - Image Cropping Library for Android

#### This project aims to provide an ultimate and flexible image cropping experience. Made in [BrainxTech](https://brainxtech.com/)

# Usage 
1.Include the library

##### You can depend on the library through Gradle
Add image-croppe to the dependencies block in your app level build.gradle:
```groovy
compile 'com.brainx.android:image-cropper:1.0'
```

##### Or through Maven
```gradle
<dependency> 
    <groupId>com.brainx.android</groupId> 
    <artifactId>image-cropper</artifactId> 
    <version>1.0</version> 
    <type>pom</type> 
</dependency>
```


 2.Add permissions to manifest

```
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />

```

3.Add Activity into your AndroidManifest.xml

```
<activity android:name="com.brainx.piccroplibrary.BXActivity.BXCropActivity"
          android:theme="@style/BXTheme.NoActionBar"/>
<activity android:name="com.theartofdev.edmodo.cropper.CropImageActivity"
          android:screenOrientation="portrait"
          android:theme="@style/Base.Theme.AppCompat" />
```
you can customize the UI colors by override the BXTheme

4.The BXCrop configuration.

```
BXCrop.getInstance()
    .initlization(this)
    .setUri(sourceUri,destinationUri)
    .withAspectRatio(1, 2)
    .crop(CROP_REQUEST_CODE);
```

5.Override `onActivityResult` method and handle BXCropResult.

```
@Override
public void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);
       if (requestCode == CROP_REQUEST_CODE) {
            if (resultCode == BXCropResult.RESULT_OK) {
                if (data != null) {
                  String cropImagePath=  BXCropResult.getPath(data);
                }
            }
        }
    }
```

# Features
```
1 Built-in BxCropActivity.
2 Image rotation during cropping.
3 Set  Aspect Ratio of Croped Image 
4 API Level 15.
   More..
```


# Changelog

#### Version: 1.0

```
Initial Build
```

### Let us know!

```
We’d be really happy if you sent us links to your 
projects where you use our component. Just send an
email to library@brainxtech.com And do let us know
if you have any questions or suggestion regarding
the library. 
```

### License

    Copyright 2018, BrainxTech

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
