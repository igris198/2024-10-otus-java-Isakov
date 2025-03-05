package ru.otus.appcontainer;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@SuppressWarnings("squid:S1068")
public class AppComponentsContainerImpl implements AppComponentsContainer {
    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?>... initialConfigClasses) {
        Arrays.stream(initialConfigClasses)
                .filter(clazz -> clazz.isAnnotationPresent(AppComponentsContainerConfig.class))
                .sorted(Comparator.comparingInt(clazz -> clazz.getAnnotation(AppComponentsContainerConfig.class).order()))
                .forEach(this::processConfig);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);
        Object configClassObj = configClassObjCreate(configClass);

        List<Method> methods = Arrays.stream(configClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(AppComponent.class))
                .sorted(Comparator.comparingInt(method -> method.getDeclaredAnnotation(AppComponent.class).order())).toList();

        for (Method method : methods) {
            String componentName = getComponentName(method);
            if (appComponentsByName.containsKey(componentName)) {
                throw new RuntimeException("В списке не должно быть компонентов с одинаковым именем!");
            }
            Object componentObj = createComponentObject(configClassObj, method, componentName);
            if (componentObj != null) {
                appComponents.add(componentObj);
                appComponentsByName.put(componentName, componentObj);
            }
        }
    }

    private Object configClassObjCreate(Class<?> configClass) {
        try {
            return configClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException e) {
            throw new RuntimeException("configClass.newInstance() : " + e);
        }
    }

    private Object createComponentObject(Object configClassObj, Method method, String componentName) {
        try {
            return method.getParameterCount() == 0 ?
                    method.invoke(configClassObj) :
                    method.invoke(configClassObj, getMethodParamsObjects(method));
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Invoke exception " + e);
        }
    }

    private String getComponentName(Method method) {
        var annotation = method.getDeclaredAnnotation(AppComponent.class);
        return annotation.name();
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    private Object[] getMethodParamsObjects(Method method) {
        List<Object> methodParamsObjects = new ArrayList<>();
        for (Class<?> parameterClass : method.getParameterTypes()) {
            var paramClassObject = getAppComponent(parameterClass);
            if (paramClassObject == null) {
                throw new RuntimeException("Не найден класс параметра метода или конструктора!");
            }
            methodParamsObjects.add(paramClassObject);
        }
        return methodParamsObjects.toArray();
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        List<Object> objects = appComponents.stream()
                .filter(componentClass::isInstance)
                .toList();
        if (objects.isEmpty()) {
            throw new RuntimeException("Не найден класс компонента!");
        }
        if (objects.size() > 1) {
            throw new RuntimeException("Дублирующиеся компоненты в списке!");
        }
        return componentClass.cast(objects.getFirst());
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        return Optional.ofNullable(appComponentsByName.get(componentName)).map(component -> (C) component)
                .orElseThrow(() -> new RuntimeException("Не найден компонент по названию " + componentName));
    }
}
