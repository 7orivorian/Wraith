package me.tori.wraith.benchmarking;

import me.tori.wraith.bus.EventBus;
import me.tori.wraith.listener.EventListener;
import me.tori.wraith.subscriber.Subscriber;
import org.openjdk.jmh.Main;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

/**
 * @author <a href="https://github.com/7orivorian">7orivorian</a>
 * @since 3.2.0
 */
public class MyBenchmark {

    public static void main(String[] args) throws Exception {
        Main.main(args);
    }

    @State(Scope.Thread)
    public static class BenchmarkState {
        MyListener myListener;
        MySubscriber mySubscriber;
        EventBus bus;

        @Setup(Level.Trial)
        public void setup() {
            bus = new EventBus();
            mySubscriber = new MySubscriber();
            myListener = new MyListener();
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 1, time = 2)
    @Measurement(iterations = 2, time = 2)
    @Fork(5)
    public void testMethod(BenchmarkState state) {
        for (int i = 0; i < 1_000; i++) {
            state.bus.register(state.myListener);
        }
    }

    public static class MySubscriber extends Subscriber {

        public MySubscriber() {
            registerListener(
                    new MyListener()
            );
        }
    }

    public static class MyListener extends EventListener<MyEvent> {

        public MyListener() {
            super(MyEvent.class);
        }

        @Override
        public void invoke(MyEvent event) {
            System.out.println(event.content());
        }

        @Override
        public boolean shouldPersist() {
            return false;
        }
    }

    public static final class MyEvent {

        private String content;

        public MyEvent(String content) {
            this.content = content;
        }

        public String content() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        @Override
        public String toString() {
            return "MyEvent[" +
                    "content=" + content + ']';
        }
    }
}