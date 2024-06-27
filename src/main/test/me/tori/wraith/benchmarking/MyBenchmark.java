/*
 * Copyright (c) 2021-2024 7orivorian.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package me.tori.wraith.benchmarking;

import me.tori.wraith.bus.EventBus;
import me.tori.wraith.listener.EventListener;
import me.tori.wraith.subscriber.Subscriber;
import org.openjdk.jmh.Main;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="https://github.com/7orivorian">7orivorian</a>
 * @since 3.2.0
 */
public class MyBenchmark {

    public static void main(String[] args) throws IOException {
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