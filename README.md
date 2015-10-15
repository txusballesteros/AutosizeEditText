AutoscaleEditText
=================

![](assets/demo.gif)

AutosizeEditText for Android is an extension of native EditText that offer a smooth text size auto scale.

## Latest Version

![](https://img.shields.io/badge/platform-android-green.svg)

## How to use

### Configuring your project dependencies

Add the library dependency to your build.gradle file.

```groovy
dependencies {
    ...
    compile 'com.txusballesteros:AutoscaleEditText:1.0'
}
```

### Adding the view to your layout

Add the view to your xml layout file.

```xml
<com.txusballesteros.AutoscaleEditText
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:textSize="30sp" 
        ...        
        app:animationDuration="300"
        app:textScale="0.7"
        app:linesLimit="2" />
```

## License

Copyright Txus Ballesteros 2015 (@txusballesteros)

This file is part of some open source application.

Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.

Contact: Txus Ballesteros <txus.ballesteros@gmail.com>