package com.github.tatercertified.oxidizium_tester.utils;

import com.github.tatercertified.oxidizium_tester.test.ParityManager;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import org.spongepowered.asm.mixin.Overwrite;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MixinTargetParser {

    public static Optional<Method> getTargetMethodFromMixin(Method mixin, Class targetClass, boolean[] requiresOperation) {
        String targetMethodName = null;
        Class[] targetMethodParams = null;
        outer: for (Annotation annotation : mixin.getDeclaredAnnotations()) {
            switch (annotation) {
                case Overwrite _ -> {
                    targetMethodName = mixin.getName();
                    targetMethodParams = mixin.getParameterTypes();
                    break outer;
                }
                case WrapMethod wrapMethod -> {
                    String method = wrapMethod.method()[0];
                    MethodSignature signature = parse(method);
                    targetMethodName = signature.methodName;
                    targetMethodParams = signature.parameterTypes.toArray(new Class[0]);
                    requiresOperation[0] = true;
                }
                default -> {}
            }
        }

        if (targetMethodName == null) {
            return Optional.empty();
        } else {
            return ParityManager.getMethodFromTargetClass(targetMethodName, targetMethodParams, targetClass);
        }
    }

    public record MethodSignature(String methodName, List<Class<?>> parameterTypes) {}

    private static MethodSignature parse(String target) {
        int parenIndex = target.indexOf('(');
        String methodName = target.substring(0, parenIndex);
        String paramsPart = target.substring(parenIndex + 1, target.indexOf(')'));

        List<Class<?>> params = new ArrayList<>();
        int i = 0;
        while (i < paramsPart.length()) {
            i = parseType(paramsPart, i, params);
        }
        return new MethodSignature(methodName, params);
    }

    private static int parseType(String descriptor, int index, List<Class<?>> params) {
        int arrayDimensions = 0;
        while (descriptor.charAt(index) == '[') {
            arrayDimensions++;
            index++;
        }

        char c = descriptor.charAt(index);
        Class<?> type;

        if (c == 'L') {
            int end = descriptor.indexOf(';', index);
            String className = descriptor.substring(index + 1, end).replace('/', '.');
            try {
                type = Class.forName(className);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Could not find class: " + className);
            }
            index = end + 1;
        } else {
            type = mapPrimitive(c);
            index++;
        }

        for (int i = 0; i < arrayDimensions; i++) {
            type = java.lang.reflect.Array.newInstance(type, 0).getClass();
        }

        params.add(type);
        return index;
    }

    private static Class<?> mapPrimitive(char c) {
        return switch (c) {
            case 'B' -> byte.class;
            case 'C' -> char.class;
            case 'D' -> double.class;
            case 'F' -> float.class;
            case 'I' -> int.class;
            case 'J' -> long.class;
            case 'S' -> short.class;
            case 'Z' -> boolean.class;
            case 'V' -> void.class;
            default -> throw new IllegalArgumentException("Invalid descriptor char: " + c);
        };
    }
}
