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

import java.util.Objects;

/**
 * Represents a class target with a specified targeting strategy.
 * This class is used to determine if a given class matches the target class according to the targeting strategy.
 *
 * @author <a href="https://github.com/7orivorian">7orivorian</a>
 * @since 4.0.0
 */
public class Target {

    @NotNull
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
    private Target(@NotNull Class<?> clazz, @NotNull TargetingRule rule) {
        this.clazz = clazz;
        this.rule = rule;
    }

    /**
     * Returns a {@link Target} that matches any class.
     *
     * @return a {@link Target} that matches any class.
     */
    @NotNull
    public static Target all() {
        return new Target(Object.class, TargetingRule.CASCADE);
    }

    /**
     * Returns a {@link Target} with the specified target and {@link TargetingRule#FINE FINE} targeting.
     *
     * @param clazz the target class.
     * @return a {@link Target} with the specified target and {@link TargetingRule#FINE FINE} targeting.
     */
    @NotNull
    public static Target fine(@NotNull Class<?> clazz) {
        return new Target(clazz, TargetingRule.FINE);
    }

    /**
     * Returns a {@link Target} with the specified target and {@link TargetingRule#CASCADE CASCADE} targeting.
     *
     * @param clazz the target class.
     * @return a {@link Target} with the specified target and {@link TargetingRule#CASCADE CASCADE} targeting.
     */
    @NotNull
    public static Target cascade(@NotNull Class<?> clazz) {
        return new Target(clazz, TargetingRule.CASCADE);
    }

    /**
     * Returns a {@link Target} with the specified target and {@link TargetingRule#REVERSE_CASCADE REVERSE_CASCADE} targeting.
     *
     * @param clazz the target class.
     * @return a {@link Target} with the specified target and {@link TargetingRule#REVERSE_CASCADE REVERSE_CASCADE} targeting.
     */
    @NotNull
    public static Target reverseCascade(@NotNull Class<?> clazz) {
        return new Target(clazz, TargetingRule.REVERSE_CASCADE);
    }

    /**
     * Checks if the target class matches the given class according to the targeting rule.
     *
     * @param clazz the class to check.
     * @return {@code true} if the class matches the target, {@code false} otherwise.
     */
    public boolean targets(@NotNull Class<?> clazz) {
        if (this.clazz == Object.class) {
            //System.out.println(clazz);
            return true;
        }
        //System.out.println(clazz);
        return rule.classesMatch(clazz, this.clazz);
    }

    /**
     * Returns the {@linkplain #clazz target class}.
     *
     * @return the {@linkplain #clazz target class}.
     */
    @NotNull
    public Class<?> clazz() {
        return clazz;
    }

    /**
     * Returns the {@linkplain #rule targeting rule}.
     *
     * @return the {@linkplain #rule targeting rule}.
     */
    @NotNull
    public TargetingRule rule() {
        return rule;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }

        Target target = (Target) obj;
        return clazz.equals(target.clazz)
                && (rule == target.rule);
    }

    @Override
    public int hashCode() {
        int result = clazz.hashCode();
        result = (31 * result) + rule.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Target{" +
                "clazz=" + clazz +
                ", rule=" + rule +
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
            public boolean classesMatch(Class<?> clazz, Class<?> target) {
                return Objects.equals(clazz, target);
            }
        },
        /**
         * The class must be a subclass or implementation of the target class.
         */
        CASCADE {
            @Override
            public boolean classesMatch(Class<?> clazz, Class<?> target) {
                return target.isAssignableFrom(clazz);
            }
        },
        /**
         * The target class must be a subclass or implementation of the given class.
         */
        REVERSE_CASCADE {
            @Override
            public boolean classesMatch(Class<?> clazz, Class<?> target) {
                return clazz.isAssignableFrom(target);
            }
        };

        public abstract boolean classesMatch(Class<?> clazz, Class<?> target);
    }
}