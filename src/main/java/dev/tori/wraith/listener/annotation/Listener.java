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

package dev.tori.wraith.listener.annotation;

import dev.tori.wraith.event.Target;
import dev.tori.wraith.event.Target.TargetingRule;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static dev.tori.wraith.bus.IEventBus.DEFAULT_PRIORITY;

/**
 * An annotation representation of {@link dev.tori.wraith.listener.Listener}.
 * <p>
 * Usage of this interface is only supposed to be on methods with only one/zero parameters.
 *
 * <h4>Usage:</h4>
 *
 * <pre><code>
 * {@code @Listener}
 * public void method(Object event) {
 *     System.out.println(event);
 * }
 * </code></pre>
 *
 * @see dev.tori.wraith.listener.Listener
 * @see TargetingRule
 *
 * @author <b><a href=https://github.com/CadenCCC>Caden</a></b>
 * @since 4.1.0
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Listener {

    /**
     * The priority level of this listener for event handling.
     *
     * @return The priority level of this listener.
     */
    int priority() default DEFAULT_PRIORITY;

    /**
     * @return The number of events this listener should handle before being removed
     */
    int persists() default 0;

    /**
     * Sets the {@link Target} for this listener.
     *
     * @return This listener's {@link Target}.
     */
    TargetingRule rule() default TargetingRule.FINE;

}