/*
 * Copyright (c) 2021-2025 7orivorian.
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

import dev.tori.wraith.event.Target.TargetingRule;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static dev.tori.wraith.bus.IEventBus.DEFAULT_PRIORITY;

/**
 * An annotation representation of {@link Listener}.
 * <p>
 * This annotation must only be present on methods with <b>0</b> or <b>1</b> parameter(s).
 *
 * <p><b>Usage Example:</b>
 *
 * <pre>
 * {@code
 * @Listen
 * public void method(Object event) {
 *     System.out.println(event);
 * }
 * }</pre>
 *
 * @author <a href="https://github.com/7orivorian">7orivorian</a>
 * @author <a href=https://github.com/CadenCCC>Caden</a>
 * @see Listener
 * @see TargetingRule TargetingRule
 * @since 4.1.0
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Listen {

    /**
     * Gets the priority level of the listener.
     *
     * @return The priority level of the listener.
     */
    int priority() default DEFAULT_PRIORITY;

    /**
     * @return The number of events this listener should handle before being removed
     */
    int persists() default 0;

    /**
     * Specifies the {@link TargetingRule TargetingRule} for this listener.
     *
     * @return This listener's {@link TargetingRule TargetingRule}.
     */
    TargetingRule rule() default TargetingRule.FINE;

    /**
     * Specifies the event class type that this listener should explicitly listen to.
     * The default value is {@code Object.class}, which means the listener will attempt to infer
     * the event type based on parameter type.
     *
     * @return The class type that this listener targets. Defaults to {@code Object.class}.
     */
    Class<?> targetClass() default Object.class;
}