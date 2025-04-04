package dev.tori.wraith.util;

import dev.tori.wraith.listener.Invokable;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.InaccessibleObjectException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for handling reflection-related operations efficiently.
 *
 * @author <a href="https://github.com/7orivorian">7orivorian</a>
 * @author <a href=https://github.com/CadenCCC>Caden</a>
 * @since 4.1.0
 */
public final class ReflectionUtil {

    private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();
    /**
     * A cache for storing computed MethodHandles to optimize method invocations.
     */
    private static final Map<Method, MethodHandle> METHOD_HANDLE_MAP = new HashMap<>();

    /**
     * Creates an {@linkplain Invokable} wrapper around a method, using {@linkplain MethodHandle} for optimized invocation.
     * <p>
     * This method caches method handles to avoid redundant lookups and provides a functional
     * interface for invocation. The resulting invokable can be used to invoke the method effectively.
     * </p>
     *
     * @param declaredMethod The method to be wrapped for invocation.
     * @param calledObject   The instance on which the method should be invoked.
     * @param hasParameter   {@code true} if the method accepts a parameter, otherwise {@code false}.
     * @return An {@link Invokable} object that allows invoking the method.
     */
    @NotNull
    public static Invokable<Object> createInvokable(@NotNull Method declaredMethod, Object calledObject, boolean hasParameter) {
        MethodHandle handle = METHOD_HANDLE_MAP.computeIfAbsent(declaredMethod, (Method m) -> {
            try {
                return LOOKUP.unreflect(m).bindTo(calledObject);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to reflect and bind: " + declaredMethod, e);
            }
        });

        return ((Object event) -> {
            try {
                if (hasParameter) {
                    handle.invoke(event);
                } else {
                    handle.invoke();
                }
            } catch (Throwable t) {
                throw new RuntimeException("Error invoking method: " + declaredMethod.getName(), t);
            }
        });
    }

    /**
     * Determines whether a given method can be accessed by a specified caller class.
     * <p>
     * This method checks Java module access rules and determines whether the caller has permissions
     * to invoke the given method reflectively. If the package is exported or open to the caller module
     * access is granted. Additionally, private lookup is attempted as a fallback.
     * </p>
     *
     * @param callerClass The class attempting to access the method.
     * @return {@code true} if the method can be accessed, otherwise {@code false}.
     */
    public static boolean canAccessMethod(@NotNull Class<?> callerClass) {
        Module classModule = callerClass.getModule();
        String packageName = callerClass.getPackageName();

        if (!classModule.isNamed()) {
            return true;
        }

        if (classModule.isExported(packageName)) {
            return true;
        }

        if (classModule.isOpen(packageName, ReflectionUtil.class.getModule())) {
            return true;
        }

        try {
            MethodHandles.privateLookupIn(callerClass, LOOKUP);
            return true;
        } catch (IllegalAccessException e) {
            return false;
        }
    }

    /**
     * Sets the specified method to be accessible, bypassing Java access control checks.
     * <p>
     * This enables reflective access to private, protected, or package-private methods.
     * <p>
     * If access cannot be granted due to security restrictions or module access limitations,
     * a runtime exception is thrown with an explanation.
     *
     * @param method The method whose accessibility is to be modified. Must not be {@code null}.
     * @throws RuntimeException If the method cannot be made accessible due to security or module access restrictions.
     */
    public static void setAccessible(@NotNull Method method) {
        try {
            method.setAccessible(true);
        } catch (SecurityException | InaccessibleObjectException e) {
            throw new RuntimeException("Cannot access method: " + method + " of class " + method.getDeclaringClass(), e);
        }
    }

    /**
     * Determines the type of the parameter for a given method. If the method has exactly one parameter,
     * the type of that parameter is returned. If the method has no parameters or more than one parameter,
     * {@code Object.class} is returned. Additionally, this method converts primitive parameter types
     * to their corresponding wrapper classes (e.g., {@code int} to {@code Integer}).
     *
     * @param method The method whose parameter type is to be determined. Must not be {@code null}.
     * @return The {@code Class} object representing the type of the parameter, or {@code Object.class}
     * if the method does not take exactly one parameter.
     */
    @NotNull
    public static Class<?> getParameterType(@NotNull Method method) {
        Parameter[] parameters = method.getParameters();
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