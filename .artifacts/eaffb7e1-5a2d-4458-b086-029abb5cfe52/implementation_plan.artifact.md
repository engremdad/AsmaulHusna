# Fix Unresolved Reference 'choose_language'

The build error `Unresolved reference 'choose_language'` in `FirstRunLanguageScreen.kt` indicates that the string resource `R.string.choose_language` is missing from the project's resource files. I will add this string to the default `strings.xml` and all supported language variants.

## Proposed Changes

### [Android Resources]

I will add `<string name="choose_language">...</string>` to the following files:

#### [MODIFY] [strings.xml](file:///Users/mac/AndroidStudioProjects/MyApplication/app/src/main/res/values/strings.xml)
- Value: `Choose Language`

#### [MODIFY] [strings.xml](file:///Users/mac/AndroidStudioProjects/MyApplication/app/src/main/res/values-ar/strings.xml)
- Value: `اختر اللغة`

#### [MODIFY] [strings.xml](file:///Users/mac/AndroidStudioProjects/MyApplication/app/src/main/res/values-bn/strings.xml)
- Value: `ভাষা নির্বাচন করুন`

#### [MODIFY] [strings.xml](file:///Users/mac/AndroidStudioProjects/MyApplication/app/src/main/res/values-hi/strings.xml)
- Value: `भाषा चुनें`

#### [MODIFY] [strings.xml](file:///Users/mac/AndroidStudioProjects/MyApplication/app/src/main/res/values-in/strings.xml)
- Value: `Pilih Bahasa`

#### [MODIFY] [strings.xml](file:///Users/mac/AndroidStudioProjects/MyApplication/app/src/main/res/values-tr/strings.xml)
- Value: `Dil Seçin`

#### [MODIFY] [strings.xml](file:///Users/mac/AndroidStudioProjects/MyApplication/app/src/main/res/values-ur/strings.xml)
- Value: `زبان منتخب کریں`

## Verification Plan

### Automated Tests
- Run `./gradlew :app:compileDebugKotlin` to verify the build error is resolved.
