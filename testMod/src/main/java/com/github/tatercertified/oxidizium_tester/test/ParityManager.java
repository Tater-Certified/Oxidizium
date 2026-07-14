package com.github.tatercertified.oxidizium_tester.test;

import com.github.tatercertified.oxidizium.utils.annotation.Max;
import com.github.tatercertified.oxidizium.utils.annotation.Min;
import com.github.tatercertified.oxidizium.utils.annotation.NonZero;
import com.github.tatercertified.oxidizium.utils.annotation.PositiveOnly;
import com.github.tatercertified.oxidizium_tester.OxidiziumTester;
import com.github.tatercertified.oxidizium_tester.utils.BytecodeAnalyzer;
import com.github.tatercertified.oxidizium_tester.utils.MixinCloner;
import com.github.tatercertified.oxidizium_tester.utils.MixinTargetParser;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import org.apache.commons.lang3.math.Fraction;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class ParityManager {
    private record MixinInfo(String targetClass, String mixinPath){}

    private static final Set<MixinInfo> OXIDIZIUM_MIXINS = new HashSet<>();
    private static final Operation OPERATION = (_) -> null;
    private static final double TOLERANCE = 0.0001;

    public static void collectMixins(String target, String mixinPath) {
        if (mixinPath.startsWith("com.github.tatercertified.oxidizium")) {
            OXIDIZIUM_MIXINS.add(new MixinInfo(target, mixinPath));
        }
    }

    public static void runParityTests() {
        TestingGUI.setCurrentTestName("Parity Testing...");
        int totalMethods = 0;
        for (MixinInfo info : OXIDIZIUM_MIXINS) {
            String clone = "com/github/tatercertified/oxidizium/Cloned" + getSimpleName(info.mixinPath);
            String mixinPath = info.mixinPath.replace('.', '/');
            Class<?> mixinClass;
            try {
                mixinClass = MixinCloner.cloneStaticMethods(mixinPath, clone);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Class targetClass = getClassFromClassPath(info.targetClass);

            totalMethods += mixinClass.getMethods().length;
            TestingGUI.setTotalTests(totalMethods);

            TestingGUI.setCurrentClass(targetClass.getSimpleName());

            for (Method mixinMethod : mixinClass.getMethods()) {
                boolean[] requiresOperation = new boolean[]{false};
                Optional<Method> possibleTargetMethod = MixinTargetParser.getTargetMethodFromMixin(mixinMethod, targetClass, requiresOperation);
                possibleTargetMethod.ifPresent(targetMethod -> runTest(mixinMethod, targetMethod, requiresOperation[0]));
            }
        }
    }

    private static String getSimpleName(String dotNotation) {
        int lastDotIndex = dotNotation.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return dotNotation;
        }
        return dotNotation.substring(lastDotIndex + 1);
    }

    private static void runTest(Method nativeMethod, Method javaMethod, boolean requiresOperationObject) {
        OxidiziumTester.TEST_LOGGER.info("Testing {}.{}", javaMethod.getDeclaringClass().getName(), javaMethod.getName());
        TestingGUI.setCurrentMethod(javaMethod.getName());
        Object[] testValues = selectTestValues(javaMethod.getParameterTypes(), getAllParameterAnnotations(nativeMethod));
        Object javaOutput;
        try {
            javaOutput = javaMethod.invoke(null, testValues);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        if (requiresOperationObject) {
            Object[] extendedValues = Arrays.copyOf(testValues, testValues.length + 1);
            extendedValues[extendedValues.length - 1] = OPERATION;
            testValues = extendedValues;
        }
        Object nativeOutput;
        try {
            nativeOutput = nativeMethod.invoke(null, testValues);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        withinReason((Number) javaOutput, (Number) nativeOutput, javaMethod.getName());
    }

    private static void withinReason(Number javaOutput, Number nativeOutput, String methodname) {
        if (javaOutput.doubleValue() == nativeOutput.doubleValue()) {
            OxidiziumTester.TEST_LOGGER.info("[Native] {} | [Java] {}", nativeOutput, javaOutput);
        } else {
            TestingGUI.addError(methodname, Math.abs(javaOutput.doubleValue() - nativeOutput.doubleValue()) < TOLERANCE, "[Native] " + nativeOutput + " != [Java] " + javaOutput);
        }
    }

    private static Annotation[][] getAllParameterAnnotations(Method mixinMethod) {
        Method nativeMethod;
        try {
            nativeMethod = BytecodeAnalyzer.inspectMethodBody(mixinMethod);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (nativeMethod == null) {
            return new Annotation[][]{};
        }

        Parameter[] parameters = nativeMethod.getParameters();
        Annotation[][] allAnnotations = new Annotation[parameters.length][];

        for (int i = 0; i < parameters.length; i++) {
            allAnnotations[i] = parameters[i].getAnnotations();
        }

        return allAnnotations;
    }

    private static Object[] selectTestValues(Class[] types, Annotation[][] requirements) {
        Class futureMinClass = null;
        Annotation[] futureMinAnnotations = null;
        int minIndex = 0;
        double[] smallestValue = new double[]{Double.MAX_VALUE};

        Class futureMaxClass = null;
        Annotation[] futureMaxAnnotations = null;
        int maxIndex = 0;
        double[] largestValue = new double[]{Double.MIN_VALUE};

        List<Object> testVals = new ArrayList<>();

        outer: for (int i = 0; i < types.length; i++) {
            Class type = types[i];
            Annotation[] annotations = requirements[i];

            // Check for min and max
            for (Annotation a : annotations) {
                if (a instanceof Min) {
                    futureMinAnnotations = annotations;
                    futureMinClass = type;
                    minIndex = i;
                    continue outer;
                } else if (a instanceof Max) {
                    futureMaxAnnotations = annotations;
                    futureMaxClass = type;
                    maxIndex = i;
                    continue outer;
                }
            }

            if (type == boolean.class || type == Boolean.class) {
                testVals.set(i, ThreadLocalRandom.current().nextBoolean());
                continue;
            }

            testVals.set(i, getTestValue(type, annotations, largestValue, smallestValue));
        }

        // Check for Min
        if (futureMinClass != null) {
            testVals.set(minIndex, getTestValue(futureMinClass, futureMinAnnotations, largestValue, smallestValue));
        }

        // Check for Max
        if (futureMaxClass != null) {
            testVals.set(maxIndex, getTestValue(futureMaxClass, futureMaxAnnotations, largestValue, smallestValue));
        }

        return testVals.toArray();
    }

    private static Number getTestValue(Class type, Annotation[] annotations, double[] largestValue, double[] smallestValue) {
        // Begin value selection
        Number val = ThreadLocalRandom.current().nextInt(-1000, 1000);

        for (Annotation requirement : annotations) {
            switch (requirement) {
                case PositiveOnly _ -> val = Math.abs(val.intValue());
                case NonZero _ -> {
                    if (val.intValue() == 0) {
                        val = val.intValue() + 1;
                    }
                }
                case Max _ -> val = largestValue[0] + Math.abs(val.intValue()) + 1;
                case Min _ -> val = smallestValue[0] - Math.abs(val.intValue()) - 1;
                default -> {}
            }
        }

        if (type == float.class || type == Float.class) {
            float finalVal = val.floatValue() / 1000.0F;
            if (finalVal > largestValue[0]) {
                largestValue[0] = finalVal;
            }
            if (finalVal < smallestValue[0]) {
                smallestValue[0] = finalVal;
            }

            return finalVal;
        } else if (type == byte.class || type == Byte.class) {
            byte finalVal = (byte) Math.clamp(val.longValue(), -126, 126);
            if (finalVal > largestValue[0]) {
                largestValue[0] = finalVal;
            }
            if (finalVal < smallestValue[0]) {
                smallestValue[0] = finalVal;
            }

            return finalVal;
        } else if (type == Fraction.class) {
            Fraction finalVal = Fraction.getFraction(val.intValue(), ThreadLocalRandom.current().nextInt(1, 1000));
            double doubleVal = finalVal.doubleValue();

            if (doubleVal > largestValue[0]) {
                largestValue[0] = doubleVal;
            }
            if (doubleVal < smallestValue[0]) {
                smallestValue[0] = doubleVal;
            }

            return finalVal;
        } else {
            int finalVal = val.intValue();

            if (finalVal > largestValue[0]) {
                largestValue[0] = finalVal;
            }
            if (finalVal < smallestValue[0]) {
                smallestValue[0] = finalVal;
            }

            return finalVal;
        }
    }

    private static Class<?> getClassFromClassPath(String classPath) {
        try {
            OxidiziumTester.TEST_LOGGER.info("Trying to load class: {}", classPath);
            return ParityManager.class.getClassLoader().loadClass(classPath);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Optional<Method> getMethodFromTargetClass(String methodName, Class[] parameters, Class targetClass) {
        try {
            return Optional.of(targetClass.getMethod(methodName, parameters));
        } catch (NoSuchMethodException e) {
            return Optional.empty();
        }
    }
}
