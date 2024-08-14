![logo](wraith_logo.png)

# Wraith

![GitHub all releases](https://img.shields.io/github/downloads/7orivorian/Wraith/total?style=flat-square)
![GitHub release (latest SemVer)](https://img.shields.io/github/v/release/7orivorian/Wraith?style=flat-square)
[![](https://jitci.com/gh/7orivorian/Wraith/svg)](https://jitci.com/gh/7orivorian/Wraith)

Lightweight Java event library created and maintained by [7orivorian](https://github.com/7orivorian)

# Importing

### Maven

* Include JitPack in your maven build file

```xml

<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```

* Add Wraith as a dependency

```xml

<dependency>
    <groupId>dev.7ori</groupId>
    <artifactId>Wraith</artifactId>
    <version>4.0.0</version>
</dependency>
```

### Gradle

* Add JitPack to your root `build.gradle` at the end of repositories

```gradle
repositories {
    maven {
        url 'https://jitpack.io'
    }
}
```

* Add the dependency

```gradle
dependencies {
    implementation 'dev.7ori:Wraith:4.0.0'
}
```

### Other

Download a `.jar` file from [releases](https://github.com/7orivorian/Wraith/releases/tag/4.0.0)

# Building

* Clone this repository
* Run `mvn package`

Packaged file can be found in the `target/` directory.

# Quick-Start Guide

While the code itself is thoroughly documented, here's a simple guide to help you get started with the latest features.

### Subscribers

<details>
<summary><i>Details...</i></summary>

To define a subscriber, you have multiple options:

Extending the Subscriber class:

```java
public class ExampleSubscriber extends Subscriber {
// ...
}
```

Implementing the ISubscriber interface:

```java
public class ExampleSubscriber implements ISubscriber {
// ...
}
```

Once you've defined your subscriber, you can subscribe it to an event bus directly within the subscriber's constructor:

```java
public class Consts {

    private static final IEventBus EVENT_BUS = new EventBus();
}

public class ExampleSubscriber extends Subscriber {

    public ExampleSubscriber() {
        Consts.EVENT_BUS.subscribe(this);
    }
}
```

Alternatively, you can subscribe a subscriber externally:

```java
public class Example {

    private static final IEventBus EVENT_BUS = new EventBus();

    public static void main(String[] args) {
        EVENT_BUS.subscribe(new ExampleSubscriber());
    }
}
```

</details>

### Defining Events

<details>
<summary><i>Details...</i></summary>

Any class can be used as an event. For instance:

```java
public class ExampleEvent {

    private String message;

    public ExampleEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
```

</details>

### Listeners

<details>
<summary><i>Details...</i></summary>

For class event listeners, you can define your listeners as follows:

```java
public class ExampleListener extends EventListener<ExampleEvent> {

    public ExampleListener() {
        super(ExampleEvent.class);
    }

    @Override
    public void invoke(ExampleEvent event) {
        event.setMessage("Hello world!");
    }
}
```

```java
public class ExampleSubscriber extends Subscriber {

    public ExampleSubscriber() {
        // Register the listener
        registerListener(new ExampleListener());
    }
}
```

Lambda event listeners provide a more concise way to achieve the same functionality:

```java
public class ExampleSubscriber extends Subscriber {

    public ExampleSubscriber() {
        // Register the listener
        registerListener(
                new LambdaEventListener<>(ExampleEvent.class, event -> event.setMessage("Hello world!"))
        );
    }
}
```

</details>

### Dispatching Events

<details>
<summary><i>Details...</i></summary>

To dispatch an event, call one of the `dispatch` methods defined in `EventBus`, passing your event as a parameter:

```java
public class Example {

    private static final IEventBus EVENT_BUS = new EventBus();

    public static void main(String[] args) {

        ExampleEvent event = new ExampleEvent("world greetings");

        EVENT_BUS.dispatch(event);

        System.out.println(event.getMessage());
    }
}
```

</details>

Please explore the [example folder](./examples/java/dev/tori/example) for _even more_ Wraith implementations!

# Contributing

Contributions are welcome! Feel free to open a pull request.

### Guidelines

* Utilize similar [formatting](.editorconfig) and practises to the rest of the codebase
* Do not include workspace files (such as an `.idea/` or `target/` directory) in your pull request
* Include unit tests for any features you add

### How to contribute

To make a contribution, follow these steps:

1. Fork and clone this repository
2. Make the changes to your fork
3. Submit a pull request

# License

[Wraith is licensed under MIT](./LICENSE)

### MIT License Summary:

The MIT License is a permissive open-source license that allows you to use, modify, and distribute the software for both
personal and commercial purposes. You are not required to share your changes, but you must include the original
copyright notice and disclaimer in your distribution. The software is provided "as is," without any warranties or
conditions.