/*
 * Copyright (c) 2025 7orivorian.
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

package dev.tori.wraith.listener;

import dev.tori.wraith.bus.EventBus;
import dev.tori.wraith.subscriber.Subscriber;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author <a href="https://github.com/7orivorian">7orivorian</a>
 * @since 4.1.0
 */
public class AnnotationTypeTest {

    private EventBus bus;

    @BeforeEach
    void setUp() {
        bus = new EventBus();
    }

    @Test
    void testBooleanType() {
        bus.subscribe(new BooleanListener());
        Assertions.assertDoesNotThrow(() -> bus.dispatch(true), "Dispatching a boolean event should not throw an exception.");
        Assertions.assertDoesNotThrow(() -> bus.dispatch(false), "Dispatching a boolean event should not throw an exception.");
    }

    @Test
    void testByteType() {
        bus.subscribe(new ByteListener());
        Assertions.assertDoesNotThrow(() -> bus.dispatch((byte) 0), "Dispatching a byte event should not throw an exception.");
    }

    @Test
    void testShortType() {
        bus.subscribe(new ShortListener());
        Assertions.assertDoesNotThrow(() -> bus.dispatch((short) 0), "Dispatching a short event should not throw an exception.");
    }

    @Test
    void testIntType() {
        bus.subscribe(new IntListener());
        Assertions.assertDoesNotThrow(() -> bus.dispatch(0), "Dispatching an int event should not throw an exception.");
    }

    @Test
    void testLongType() {
        bus.subscribe(new LongListener());
        Assertions.assertDoesNotThrow(() -> bus.dispatch(0L), "Dispatching a long event should not throw an exception.");
    }

    @Test
    void testFloatType() {
        bus.subscribe(new FloatListener());
        Assertions.assertDoesNotThrow(() -> bus.dispatch(0.0f), "Dispatching a float event should not throw an exception.");
    }

    @Test
    void testDoubleType() {
        bus.subscribe(new DoubleListener());
        Assertions.assertDoesNotThrow(() -> bus.dispatch(0.0), "Dispatching a double event should not throw an exception.");
    }

    @Test
    void testCharType() {
        bus.subscribe(new CharListener());
        Assertions.assertDoesNotThrow(() -> bus.dispatch('a'), "Dispatching a char event should not throw an exception.");
    }

    public static class BooleanListener extends Subscriber {

        @Listen
        public void listener(boolean event) {

        }
    }

    public static class ByteListener extends Subscriber {

        @Listen
        public void listener(byte event) {

        }
    }

    public static class ShortListener extends Subscriber {

        @Listen
        public void listener(short event) {

        }
    }

    public static class IntListener extends Subscriber {

        @Listen
        public void listener(int event) {

        }
    }

    public static class LongListener extends Subscriber {

        @Listen
        public void listener(long event) {

        }
    }

    public static class FloatListener extends Subscriber {

        @Listen
        public void listener(float event) {

        }
    }

    public static class DoubleListener extends Subscriber {

        @Listen
        public void listener(double event) {

        }
    }

    public static class CharListener extends Subscriber {

        @Listen
        public void listener(char event) {

        }
    }
}