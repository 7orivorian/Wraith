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

package dev.tori.wraith.subscriber;

import dev.tori.wraith.event.Target;
import dev.tori.wraith.event.Target.TargetingRule;
import dev.tori.wraith.listener.ListenerBuilder;
import dev.tori.wraith.listener.annotation.Listener;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.*;
import java.util.Objects;

/**
 * An implementation of {@link Subscriber} that registers methods as listeners if they meet certain requirements.
 * <p>
 * Accepted methods have to meet a specific requirements, which are:
 * <ul>
 *     <li>Has to have the {@link Listener} annotation.</li>
 *     <li>Cannot have more than 1 parameter.</li>
 *     <li>Methods cannot be abstract.</li>
 * </ul>
 *
 * @author <b><a href=https://github.com/CadenCCC>Caden</a></b>
 * @see Subscriber
 * @see Listener
 * @see TargetingRule
 * @see Target
 * @since 4.1.0
 */
public class AnnotatedSubscriber extends Subscriber {

    public AnnotatedSubscriber(@NotNull Object calledObject) {
        super();
        Objects.requireNonNull(calledObject);

        if (calledObject instanceof Class<?>) {
            return;
        }

        findPossibleMethods(calledObject, calledObject.getClass());
    }

    /**
     * Finds and processes methods in the specified class that are annotated with {@link Listener}.
     * The identified methods are registered as event listeners using a {@link ListenerBuilder}.
     *
     * @param called object called by the constructor
     * @param clazz  class that is going to be searched for methods with annotation {@link Listener}
     */
    private void findPossibleMethods(Object called, Class<?> clazz) {
        for (Method declaredMethod : clazz.getDeclaredMethods()) {
            Parameter[] parameters = declaredMethod.getParameters();
            int modifiers = declaredMethod.getModifiers();

            if (parameters.length > 1) {
                continue;
            }

            if (Modifier.isAbstract(modifiers)) {
                continue;
            }

            if (!declaredMethod.isAnnotationPresent(Listener.class)) {
                continue;
            }

            Class<?> parameterType = getParameterType(parameters);
            boolean requiresAccessible = !Modifier.isPublic(modifiers);
            boolean hasParameter = parameters.length == 1;

            Listener annotation = declaredMethod.getAnnotation(Listener.class);
            TargetingRule rule = annotation.rule();

            ListenerBuilder<Object> builder = new ListenerBuilder<>()
                    .priority(annotation.priority())
                    .persists(annotation.persists())
                    .invokable((obj) -> {
                        if (requiresAccessible) {
                            setAccessible(declaredMethod);
                        }

                        try {
                            if (hasParameter) {
                                declaredMethod.invoke(called, obj);
                            } else {
                                declaredMethod.invoke(called);
                            }
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            throw new RuntimeException("Error invoking method: " + declaredMethod.getName(), e);
                        }
                    });

            switch (rule) {
                case CASCADE -> builder.target(Target.cascade(parameterType));
                case FINE -> builder.target(Target.fine(parameterType));
                case REVERSE_CASCADE -> builder.target(Target.reverseCascade(parameterType));
            }

            registerListener(builder.build());
        }
    }

    private void setAccessible(Method method) {
        try {
            method.setAccessible(true);
        } catch (SecurityException | InaccessibleObjectException ignored) {
        }
    }

    /**
     * Retrieves the parameter type of method. If the method has a single parameter,
     * its type is returned. If the method has no parameters, {@link Object} is returned.
     * Converts primitive parameter types to their corresponding wrapper classes.
     *
     * @param parameters an array of parameters for a method.
     * @return the {@link Class} of the parameter type, or an {@link Object} class if no parameters are present.
     */
    private @NotNull Class<?> getParameterType(Parameter[] parameters) {
        Class<?> parameterType = parameters.length == 1 ? parameters[0].getType() : Object.class;
        if (parameterType.isPrimitive()) {
            switch (parameterType.getName()) {
                case "int" -> parameterType = Integer.class;
                case "long" -> parameterType = Long.class;
                case "boolean" -> parameterType = Boolean.class;
                case "double" -> parameterType = Double.class;
                case "float" -> parameterType = Float.class;
                case "byte" -> parameterType = Byte.class;
                case "short" -> parameterType = Short.class;
                case "char" -> parameterType = Character.class;
            }
        }
        return parameterType;
    }
}