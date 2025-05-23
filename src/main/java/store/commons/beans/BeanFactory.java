package store.commons.beans;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.concurrent.ConcurrentHashMap;

public class BeanFactory {

    private BeanFactory() {}

    private static final ConcurrentHashMap<Class<?>, Object> beanContextHolder = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> requiredType) {
        return (T) beanContextHolder.computeIfAbsent(requiredType, BeanFactory::newInstance);
    }

    private static <T> T newInstance(Class<T> clazz) {
        try {
            return createNewInstance(clazz);
        } catch (Exception e) {
            throw new IllegalStateException(clazz.getName() + "에 해당하는 bean 을 생성하는 과정에서 오류가 발생하였습니다.", e);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T createNewInstance(Class<T> clazz) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Method getInstanceMethod = findGetInstanceMethod(clazz);
        if (getInstanceMethod != null) return (T) getInstanceMethod.invoke(null);
        Constructor<?> constructor = findConstructors(clazz)[0];
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        if (parameterTypes.length == 0) return (T) constructor.newInstance();
        return (T) constructor.newInstance(findParameters(parameterTypes));
    }

    private static <T> Constructor<?>[] findConstructors(Class<T> clazz) {
        Constructor<?>[] constructors = clazz.getConstructors();
        validateConstructorsEmpty(constructors, clazz);
        return constructors;
    }

    private static <T> void validateConstructorsEmpty(Constructor<?>[] constructors, Class<T> clazz) {
        if (constructors.length == 0) {
            throw new IllegalStateException(clazz.getName() +"에 해당하는 생성자를 찾을 수 없습니다.");
        }
    }

    private static Object[] findParameters(Class<?>[] parameterTypes) {
        Object[] parameters = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            parameters[i] = BeanFactory.newInstance(parameterTypes[i]);
        }
        return parameters;
    }

    private static <T> Method findGetInstanceMethod(Class<T> clazz) {
        try {
            Method getInstanceMethod = clazz.getDeclaredMethod("getInstance");
            if (isStatic(getInstanceMethod)) {
                return getInstanceMethod;
            }
        } catch (NoSuchMethodException ignored) {}
        return null;
    }

    private static boolean isStatic(Method method) {
        return (method.getModifiers() & Modifier.STATIC) != 0;
    }
}
