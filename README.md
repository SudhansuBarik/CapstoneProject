# MoviesCentral
This is the repository for Popular Movies Stage 2 project for Google India Scholarship 2018.
This App uses The Movie Database (TMDB) API to display Popular and Top Rated dbMovies.

# Setup Instructions
1. Fork Repository
2. Create `TMDB API Key` following the guidelines [here](https://developers.themoviedb.org/3/getting-started/introduction)
3. Open `.gradle` folder in your File Explorer. Usually it can be found at:
* Windows: C:\Users\<Your Username>\.gradle
* Mac: /Users/<Your Username>/.gradle
* Linux: /home/<Your Username>/.gradle
Inside it there would be a file named gradle.properties (just create it if there isn’t any).
4. Open that file in Text Editor and add `MoviesCentral_ApiKey="YOUR API KEY"`
5. Open module-level `build.gradle` file in Android Studio (the one where you define dependencies)
6. Add the property in each of your build types
* For accessing in Java/Kotlin add it as `buildConfigField`
* For accessing in XML add it as `resValue`
7. After addition it might look like:
```
  buildTypes {
        debug {
            buildConfigField 'String', "ApiKey", MoviesCentral_ApiKey
            resValue 'string', "api_key", MoviesCentral_ApiKey
        }
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
            buildConfigField 'String', "ApiKey", MoviesCentral_ApiKey
            resValue 'string', "api_key", MoviesCentral_ApiKey
        }
    }
  ```
And then you are good to go!
  
# Creator
[Sudhansu Barik](https://www.linkedin.com/in/sudhansu-barik-007/)
