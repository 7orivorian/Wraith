/*
 * Copyright (c) 2024 7orivorian.
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

package dev.tori.wraith.event;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Represents a class target with a specified targeting strategy.
 * This class is used to determine if a given class matches the target class according to the targeting strategy.
 *
 * @author <a href="https://github.com/7orivorian">7orivorian</a>
 * @since 4.0.0
 */
public class Target {

    @Nullable
    private final Class<?> clazz;
    @NotNull
    private final TargetingRule rule;

    /**
     * Constructs a new {@link Target}.
     *
     * @param clazz the target class.
     * @param rule  the targeting rule.
     */
    @Contract(pure = true)
    private Target(@Nullable Class<?> clazz, @NotNull TargetingRule rule) {
        this.clazz = clazz;
        this.rule = rule;
    }

    /**
     * Returns a {@code ClassTarget} with no specific target.
     *
     * @return a {@code ClassTarget} with no specific target.
     */
    @NotNull
    public static Target none() {
        return new Target(null, TargetingRule.FINE);
    }

    /**
     * Returns a {@code ClassTarget} with the specified target and {@link TargetingRule#FINE FINE} targeting.
     *
     * @param target the target class.
     * @return a {@code ClassTarget} with the specified target and {@link TargetingRule#FINE FINE} targeting.
     */
    @NotNull
    public static Target fine(@Nullable Class<?> target) {
        return new Target(target, TargetingRule.FINE);
    }

    /**
     * Returns a {@code ClassTarget} with the specified target and {@link TargetingRule#CASCADE CASCADE} targeting.
     *
     * @param target the target class.
     * @return a {@code ClassTarget} with the specified target and {@link TargetingRule#CASCADE CASCADE} targeting.
     */
    @NotNull
    public static Target cascade(@Nullable Class<?> target) {
        return new Target(target, TargetingRule.CASCADE);
    }

    /**
     * Returns a {@code ClassTarget} with the specified target and {@link TargetingRule#REVERSE_CASCADE REVERSE_CASCADE} targeting.
     *
     * @param target the target class.
     * @return a {@code ClassTarget} with the specified target and {@link TargetingRule#REVERSE_CASCADE REVERSE_CASCADE} targeting.
     */
    @NotNull
    public static Target reverseCascade(@Nullable Class<?> target) {
        return new Target(target, TargetingRule.REVERSE_CASCADE);
    }

    /**
     * Checks if the target class matches the given class according to the targeting rule.
     *
     * @param clazz the class to check.
     * @return {@code true} if the class matches the target, {@code false} otherwise.
     */
    public boolean targets(@NotNull Class<?> clazz) {
        if (this.clazz == null) {
            return true; // Null target class will target anything
        }
        return rule.isMatch(clazz, this.clazz);
    }

    /**
     * Returns the target class.
     *
     * @return the target class, {@code null} if no target is set.
     */
    @Nullable
    public Class<?> clazz() {
        return clazz;
    }

    /**
     * Returns the targeting rule.
     *
     * @return the targeting rule.
     */
    @NotNull
    public TargetingRule rule() {
        return rule;
    }

    @Override
    public String toString() {
        return "ClassTarget{" +
                "target=" + clazz +
                ", matching=" + rule +
                '}';
    }

    /**
     * Enumeration of targeting rules.
     */
    public enum TargetingRule {
        /**
         * The class must exactly equal the target class.
         */
        FINE {
            @Override
            public boolean isMatch(Class<?> clazz, Class<?> target) {
                return Objects.equals(clazz, target);
            }
        },
        /**
         * The class must be a subclass or implementation of the target class.
         */
        CASCADE {
            @Override
            public boolean isMatch(Class<?> clazz, Class<?> target) {
                return target.isAssignableFrom(clazz);
            }
        },
        /**
         * The target class must be a subclass or implementation of the given class.
         */
        REVERSE_CASCADE {
            @Override
            public boolean isMatch(Class<?> clazz, Class<?> target) {
                return clazz.isAssignableFrom(target);
            }
        };

        public abstract boolean isMatch(Class<?> clazz, Class<?> target);
    }
}