# 🎨 Compose Mock Preview

A Kotlin library to automatically generate mock data in Jetpack Compose @Previews using KSP.

---

## 🚀 Feature

- ✅ Automatic generation `PreviewParameterProvider`
- ✅ Nested data class support
- ✅ Custom annotations: `@LongMessagePreview`, `@ShortMessagePreview`, etc.
- ✅ Centralized configuration via `ComposePreviewMockConfig`
- ✅ Compatible with KSP + KotlinPoet

---

## 📦 Installation ( In progress )

add KSP plugin in your `build.gradle.kts` :

```kotlin
plugins {
    id("com.google.devtools.ksp") version "1.9.22-1.0.16" // update with your kotlin version
}
```

and add to dependencies :

```kotlin
dependencies {
    ksp("com.recisio.compose-mock-preview:1.0.0")
    implementation("com.recisio.compose-mock-preview:1.0.0")
}
```

---

## 🛠️ Using

### 1. Data class Annotation:

```kotlin
@ComposeMockPreviewProvider
data class TestComposable(
    val title: String,
    @LongMessagePreview val description: String,
    val imageRes: Int,
    val nested: NestedData
)
```

### 2. Add a configuration :

```kotlin
@ComposeMockPreviewConfiguration
class AppMockConfiguration : ComposeMockPreviewConfig {
    override val longMessageList: List<String> = listOf("Very very long message")
    override val shortMessageList: List<String> = listOf("short message")
    override val resMessageList: List<Int> = listOf(R.string.app_name)
    override val resDrawableList: List<Int> = listOf(R.drawable.ic_launcher_background)
    override val resColorList: List<Int> = listOf(R.color.purple_700)
}
```

---

## 👀 Preview example

```kotlin
@PreviewLightDark
@Composable
fun TestComposablePreview(@PreviewParameter(TestComposablePreviewProvider::class) parameter: TestComposable) {
    TestCompose(parameter)
}
```

---

## 🧩 Module

| Module                | Description                                  |
|----------------------|----------------------------------------------|
| `com.recisio.compose-mock-preview` | Annotations KSP (`@ComposeMockPreviewProvider`) + KSP PreviewParameterProvider auto generate file|

---

## 📝 Licence

MIT © [Recisio](https://github.com/recisio/)
