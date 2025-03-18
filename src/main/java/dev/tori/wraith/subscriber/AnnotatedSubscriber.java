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

package dev.tori.wraith.subscriber;

import dev.tori.wraith.event.Target;
import dev.tori.wraith.event.Target.TargetingRule;
import dev.tori.wraith.listener.ListenerBuilder;
import dev.tori.wraith.listener.annotation.Listener;
import dev.tori.wraith.util.ReflectionUtil;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InaccessibleObjectException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.Objects;

import static dev.tori.wraith.util.ReflectionUtil.canAccessMethod;
import static dev.tori.wraith.util.ReflectionUtil.createInvokable;

/**
 * An implementation of {@link Subscriber} that registers methods as listeners if they meet certain requirements.
 *
 * @author <b><a href=https://github.com/CadenCCC>Caden</a></b>
 * @see Subscriber
 * @see Listener
 * @see TargetingRule
 * @see Target
 * @see ReflectionUtil
 * @since 4.1.0
 */
public class AnnotatedSubscriber extends Subscriber {

    public AnnotatedSubscriber(@NotNull Object calledObject) {
        super();

        if (calledObject instanceof Class<?>) {
            return;
        }

        findPossibleMethods(calledObject, calledObject.getClass());
    }

    /**
     * Identifies and registers methods within the specified class that are annotated with {@linkplain Listener}.
     * <p>
     * Methods must meet the following criteria to be registered:
     * <ui>
     * <li>Must be annotated with {@linkplain Listener}</li>
     * <li>Must have at most one parameter. (if none it will result in an Object)</li>
     * <li>Cannot be abstract.</li>
     * <li>Must be accessible. (or made accessible via reflection)</li>
     * </ui>
     * </p>
     *
     * @param called The instance containing the methods to be scanned.
     * @param clazz  The class to be inspected for annotated methods.
     */
    private void findPossibleMethods(Object called, Class<?> clazz) {
        for (Method declaredMethod : clazz.getDeclaredMethods()) {
            int modifiers = declaredMethod.getModifiers();

            if (declaredMethod.getParameterCount() > 1 || Modifier.isAbstract(modifiers)) {
                continue;
            }

            Listener annotation = declaredMethod.getAnnotation(Listener.class);
            if (!declaredMethod.isAnnotationPresent(Listener.class)) {
                continue;
            }

            if (!canAccessMethod(clazz, declaredMethod)) {
                continue;
            }

            Parameter[] parameters = declaredMethod.getParameters();
            Class<?> parameterType = getParameterType(parameters);

            if (!Modifier.isPublic(modifiers)) {
                setAccessible(declaredMethod);
            }

            ListenerBuilder<Object> builder = new ListenerBuilder<>()
                    .priority(annotation.priority())
                    .persists(annotation.persists())
                    .invokable(createInvokable(declaredMethod, called, parameters.length == 1));


            TargetingRule targetingRule = annotation.rule();
            Class<?> target = annotation.target();
            switch (targetingRule) {
                case CASCADE -> builder.target(Target.cascade(target == Object.class ? parameterType : target));
                case FINE -> builder.target(Target.fine(target == Object.class ? parameterType : target));
                case REVERSE_CASCADE ->
                        builder.target(Target.reverseCascade(target == Object.class ? parameterType : target));
            }

            registerListener(builder.build());
        }
    }

    private void setAccessible(Method method) {
        try {
            method.setAccessible(true);
        } catch (SecurityException | InaccessibleObjectException exception) {
            throw new RuntimeException("Cannot access method: " + method + " of class " + method.getDeclaringClass(), exception);
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