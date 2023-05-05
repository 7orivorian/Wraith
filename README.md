![logo](wraith_logo.png)

# Wraith
![GitHub all releases](https://img.shields.io/github/downloads/7orivorian/Wraith/total?style=flat-square)
![GitHub release (latest SemVer)](https://img.shields.io/github/v/release/7orivorian/Wraith?style=flat-square)

Lightweight Java event library created and maintained by [7orivorian](https://github.com/7orivorian)

# Importing

## Maven

* Include JitPack in your maven build file

```xml

<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

* Add Wraith as a dependency

```xml

<dependency>
    <groupId>com.github.7orivorian</groupId>
    <artifactId>Wraith</artifactId>
    <version>2.0.0</version>
</dependency>
```

## Gradle

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
    implementation 'com.github.7orivorian:Wraith:2.0.0'
}
```

## Other

Use a `.jar` file from [releases](https://github.com/7orivorian/Wraith/releases/tag/2.0.0)

# Building

* Clone this repository
* Run `mvn package`

Packaged file can be found in the `target/` directory.

# Usage

### Subscribers

A subscriber can be defined by

* Extending the `Subscriber` class

```java
public class ExampleSubscriber extends Subscriber {
}
```

* Implementing the `ISubscriber` interface

```java
public class ExampleSubscriber implements ISubscriber {
}
```

The subscriber must then be subscribed to an event bus, which can be done within the subsciber itself.

```java
public class ExampleSubscriber extends Subscriber {

    private static final IEventBus EVENT_BUS = new EventBus();

    public ExampleSubscriber() {
        EVENT_BUS.subscribe(this);
    }
}
```

This can also be done externally.

```java
public class Example {

    private static final IEventBus EVENT_BUS = new EventBus();

    public static void main(String[] args) {
        EVENT_BUS.subscribe(new ExampleSubscriber());
    }
}

// OR

public class Example {

    private static final IEventBus EVENT_BUS = new EventBus();

    private ExampleSubscriber exampleSubscriber = new ExampleSubscriber();

    public void sub() {
        EVENT_BUS.subscribe(exampleSubscriber);
    }
}
```

### Defining an event

Any class can be passed as an event.

```java
public class ExampleEvent {

    private String string;

    public ExampleEvent(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }
}
```

A cancelable event can be defined by

* Extending the `Cancelable` class

```java
public class CancelableEvent extends Cancelable {
}
```

* Implementing the `ICancelable` interface

```java
public class CancelableEvent implements ICancelable {
}
```

### Listeners

Class event listener.

```java
public class ExampleListener extends EventListener<ExampleEvent> {

    public ExampleListener() {
        super(ExampleEvent.class);
    }

    @Override
    public void invoke(ExampleEvent event) {
        if (event.getStage() == EventStage.POST) {
            event.setString("I feel wonderful!");
        }
    }
}

public class ExampleSubscriber extends Subscriber {

    public ExampleSubscriber() {
        // Register the listener
        registerListener(new ExampleListener());
    }
}
```

Lambda event listener.

```java
public class ExampleSubscriber extends Subscriber {

    public ExampleSubscriber() {
        // Register the listener
        registerListener(
                new LambdaEventListener<>(ExampleEvent.class, event -> {
                    if (event.getStage() == EventStage.PRE) {
                        event.setString("Hello world!");
                    }
                })
        );
    }
}
```

### Posting events

To post an event to an event bus, call one of the "post" methods defined in `IEventBus`, passing your event as a
parameter.

```java
import me.tori.wraith.event.EventStage;

public class Example {

    private static final IEventBus EVENT_BUS = new EventBus();

    public static void main(String[] args) {

        ExampleEvent event = new ExampleEvent(EventStage.PRE);

        EVENT_BUS.post(event);

        if (!event.isCanceled()) {
            System.out.println(event.getString());
        }
    }
}
```

[Click here](src/example/java/me/tori/example) to view a small program with a simple Wraith implementation.

# License

[This project is licensed under MIT](src/main/resources/LICENSE.md)
