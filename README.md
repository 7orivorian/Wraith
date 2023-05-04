# Wraith

Lightweight java event library created and maintained by [7orivorian](https://github.com/7orivorian)

_Latest version: [1.4.0](https://github.com/7orivorian/WraithLib/releases/tag/v1.4.0)_

# Usage

### Subscribers

A subscriber can be defined by either

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

the subscriber must then be subscribed to an event bus. This can be done within the subsciber itself

```java
public class ExampleSubscriber extends Subscriber {

    private static final IEventBus EVENT_BUS = new EventBus();

    public ExampleSubscriber() {
        EVENT_BUS.subscribe(this);
    }
}
```

or externally

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

Any class can be passed as an event

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

For an event to be cancelable, it must either

* Extend the `Cancelable` class

```java
public class CancelableEvent extends Cancelable {
}
```

* Implement the `ICancelable` interface

```java
public class CancelableEvent implements ICancelable {
}
```

### Listeners

Class event listener

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
        registerListener(new ExampleListener());
    }
}
```

Lambda event listener

```java
public class ExampleSubscriber extends Subscriber {

    public ExampleSubscriber() {
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

To post an event to an event bus, call one of the "post" methods defined in `IEventBus`, passing your event as a parameter.

```java
public class Example {

    private static final IEventBus EVENT_BUS = new EventBus();

    public static void main(String[] args) {
        
        ExampleEvent event = new ExampleEvent("hello");
        
        EVENT_BUS.post(event);
        
        if (!event.isCanceled()) {
            System.out.println(event.getString());
        }
    }
}
```

[Click here](url) to view a small program with a simple Wraith implementation