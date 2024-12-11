package ru.otus.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.annotations.Log;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ProxyTestLogging {
    private static final Logger logger = LoggerFactory.getLogger(ProxyTestLogging.class);

    private ProxyTestLogging() {}

    public static TestLoggingInterface createMyClass(TestLoggingInterface classInst) {
        InvocationHandler handler = new TestLoggingInvocationHandler(classInst);
        return (TestLoggingInterface) Proxy.newProxyInstance(classInst.getClass().getClassLoader(), new Class<?>[]{TestLoggingInterface.class}, handler);
    }

    static class TestLoggingInvocationHandler implements InvocationHandler {
        private final TestLoggingInterface myClass;
        private final Map<String, Method> methods;

        TestLoggingInvocationHandler(TestLoggingInterface myClass) {
            this.myClass = myClass;
            this.methods = getLogAnnotatedMethods();
        }

        private Map<String, Method> getLogAnnotatedMethods() {
            Map<String, Method> map = new HashMap<>();
            for (Method method : myClass.getClass().getDeclaredMethods()) {
                if (method.isAnnotationPresent(Log.class)) {
                    map.put(getMethodSignature(method), method);
                }
            }
            return map;
        }
        private String getMethodSignature(Method method) {
            return method.getName() + (method.getParameters() != null ? Arrays.toString(method.getParameters()) : "");
        }

        private boolean isMethodsIdentical(Method method1, Method method2) {
            Parameter[] params1 = method1.getParameters();
            Parameter[] params2 = method2.getParameters();

            if (params1.length != params2.length) { return false; }

            for (int i = 0; i < params1.length; i++) {
                if (!params1[i].getType().equals(params2[i].getType())) { return false; }
            }
            return true;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Method annotatedMethod = methods.get(getMethodSignature(method));
            if (annotatedMethod !=null && isMethodsIdentical(annotatedMethod, method)) {
                logger.info("executed method: {}, param: {}", method.getName(), args != null ? Arrays.toString(args) : "null");
            }
            return method.invoke(myClass, args);
        }
    }
}
