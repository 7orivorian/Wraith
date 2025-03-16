package dev.tori.wraith.util;

import dev.tori.wraith.listener.Invokable;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for handling reflection-related operations efficiently.
 *
 * @author <b><a href=https://github.com/CadenCCC>Caden</a></b>
 * @since 4.1.0
 */
public final class ReflectionUtil {

    private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();
    /**
     * A cache for storing computed MethodHandles to optimize method invocations.
     */
    private static final Map<Method, MethodHandle> METHOD_HANDLE_MAP = new HashMap<>();

    /**
     * Determines whether a given method can be accessed by a specified caller class.
     * <p>
     * This method checks Java module access rules and determines whether the caller has permissions
     * to invoke the given method reflectively. If the package is exported or open to the caller module
     * access is granted. Additionally, private lookup is attempted as a fallback.
     * </p>
     *
     * @param callerClass The class attempting to access the method.
     * @param method      The method being accessed.
     * @return {@code true} if the method can be accessed, otherwise {@code false}.
     */
    public static boolean canAccessMethod(Class<?> callerClass, Method method) {
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
    public static Invokable<Object> createInvokable(Method declaredMethod, Object calledObject, boolean hasParameter) {
        MethodHandle handle = METHOD_HANDLE_MAP.computeIfAbsent(declaredMethod, m -> {
            try {
                return LOOKUP.unreflect(m).bindTo(calledObject);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to reflect and bind: " + declaredMethod, e);
            }
        });

        return (event -> {
            try {
                if (hasParameter) {
                    handle.invoke(event);
                } else {
                    handle.invoke();
                }
            } catch (Throwable throwable) {
                throw new RuntimeException("Error invoking method: " + declaredMethod.getName(), throwable);
            }
        });
    }
}