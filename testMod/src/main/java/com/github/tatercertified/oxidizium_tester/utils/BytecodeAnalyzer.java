package com.github.tatercertified.oxidizium_tester.utils;

import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.Type;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BytecodeAnalyzer {

    public static class ClassBytecodeRegistry {
        public static final Map<String, byte[]> CLONED_CLASSES = new ConcurrentHashMap<>();
    }

    /**
     * Gets the method inside the provided Method's body
     * @param method Method that contains the body
     * @return The Method contained inside the body
     * @throws Exception If an error occurs (I.E there is probably more than one line)
     */
    public static @Nullable Method inspectMethodBody(Method method) throws Exception {
        Class<?> clazz = method.getDeclaringClass();
        String className = clazz.getName().replace('.', '/');

        // 1. Get the bytes from your registry (for cloned classes) or disk (for normal classes)
        byte[] classBytes = ClassBytecodeRegistry.CLONED_CLASSES.get(clazz.getName());

        if (classBytes == null) {
            String classResource = "/" + className + ".class";
            try (InputStream is = clazz.getClassLoader().getResourceAsStream(classResource)) {
                if (is == null) throw new IOException("Could not find class file: " + className);
                classBytes = is.readAllBytes();
            }
        }

        // 2. Parse the bytecode
        ClassReader cr = new ClassReader(classBytes);
        ClassNode cn = new ClassNode();
        cr.accept(cn, 0);

        // 3. Find the method and resolve the body instruction
        for (MethodNode mn : cn.methods) {
            if (mn.name.equals(method.getName()) && mn.desc.equals(Type.getMethodDescriptor(method))) {
                for (AbstractInsnNode insn : mn.instructions) {
                    if (insn instanceof MethodInsnNode mInsn) {
                        // This now returns the reflection Method object directly
                        return resolveMethod(mInsn);
                    }
                }
            }
        }
        return null;
    }

    private static Method resolveMethod(MethodInsnNode mInsn) throws Exception {
        String className = mInsn.owner.replace('/', '.');
        Class<?> clazz = Class.forName(className, false, Thread.currentThread().getContextClassLoader());

        Type[] argTypes = Type.getArgumentTypes(mInsn.desc);
        Class<?>[] paramClasses = new Class<?>[argTypes.length];
        for (int i = 0; i < argTypes.length; i++) {
            paramClasses[i] = getClassFromType(argTypes[i]);
        }

        Method foundMethod = clazz.getDeclaredMethod(mInsn.name, paramClasses);
        foundMethod.setAccessible(true);
        return foundMethod;
    }

    private static Class<?> getClassFromType(Type type) throws ClassNotFoundException {
        return switch (type.getSort()) {
            case Type.BOOLEAN -> boolean.class;
            case Type.BYTE -> byte.class;
            case Type.CHAR -> char.class;
            case Type.SHORT -> short.class;
            case Type.INT -> int.class;
            case Type.DOUBLE -> double.class;
            case Type.FLOAT -> float.class;
            case Type.LONG -> long.class;
            case Type.OBJECT -> Class.forName(type.getClassName());
            default -> Object.class;
        };
    }
}
