![logo](wraith_logo.png)

# Wraith

![GitHub all releases](https://img.shields.io/github/downloads/7orivorian/Wraith/total?style=flat)
![GitHub Release](https://img.shields.io/github/v/release/7orivorian/Wraith?sort=semver&style=flat&link=https%3A%2F%2Fgithub.com%2F7orivorian%2FWraith%2Freleases%2Flatest)
![JitPack Release](https://jitpack.io/v/dev.7ori/Wraith.svg?style=flat)
[![JitCI](https://jitci.com/gh/7orivorian/Wraith/svg)](https://jitci.com/gh/7orivorian/Wraith)
![GitHub License](https://img.shields.io/github/license/7orivorian/Wraith?color=blue)

Capable, versatile, and easy to use Java event library.

## Key Features

- Easily define your own events, listeners, & event buses.
- Out-of-the-box support for any scenario.
- Performant.
- Fully documented.

## Usage

Click [here](https://docs.7ori.dev/wraith/importing) to learn how to import Wraith into your own project.
For more information, please see the Wraith [documentation](https://docs.7ori.dev/wraith/).

## Import with Build Automation

<details>
<summary>Maven</summary>    
Include JitPack in your maven build file.

```xml

<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

Add the dependency.

```xml

<dependency>
    <groupId>dev.7ori</groupId>
    <artifactId>Wraith</artifactId>
    <version>4.0.0</version>
</dependency>
```

</details>
<details>
<summary>Gradle</summary>
Add JitPack to your root `build.gradle` at the end of repositories.

```groovy
repositories {
    //...
    maven {
        url 'https://jitpack.io'
    }
}
```

Add the dependency.

```groovy
dependencies {
    implementation 'dev.7ori:Wraith:4.0.0'
}
```

</details>
<details>
<summary>Gradle (Kotlin)</summary>
Add JitPack to your root `build.gradle.kts` at the end of repositories.

```groovy
repositories {
    //...
    maven {
        url = uri("https://jitpack.io")
    }
}
```

Add the dependency.

```groovy
dependencies {
    implementation("dev.7ori:Wraith:4.0.0")
}
```

</details>

## Download a Jar

Wraith and its sources can be downloaded
[here](https://github.com/7orivorian/Wraith/releases/latest).

## License

[Wraith is licensed under MIT.](./LICENSE)

### MIT License Summary:

The MIT License is a permissive open-source license that allows you to use, modify, and distribute the software for both
personal and commercial purposes. You are not required to share your changes, but you must include the original
copyright notice and disclaimer in your distribution. The software is provided "as is," without any warranties or
conditions.