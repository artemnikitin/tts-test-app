# Text-to-speech test app for Android  
[![Codacy Badge](https://api.codacy.com/project/badge/grade/d58f2b3e3b494417884cb66dd5dcf551)](https://www.codacy.com/app/artemnikitin/tts-test-app)   [![Build Status](https://travis-ci.org/artemnikitin/tts-test-app.svg?branch=master)](https://travis-ci.org/artemnikitin/tts-test-app)         
Text-to-speech test app for Android.   

It's recommended to use it on devices with Android 5 or higher, because in Android 5 (SDK 21) new API for obtaining list of supported languages by TTS engine was introduced.

App is available on Google Play    
<a href='https://play.google.com/store/apps/details?id=com.artemnikitin.tts&utm_source=global_co&utm_medium=prtnr&utm_content=Mar2515&utm_campaign=PartBadge&pcampaignid=MKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png' width="220" height="85"/></a>

### Build
```
./gradlew clean build
```

### Test
```
./gradlew clean testDebugUnitTest
```
