package com.github.tatercertified.oxidizium.utils;

import com.github.tatercertified.oxidizium.utils.asm.Strip;
import org.joml.Vector3f;

public final class Cleanup {
    @Strip(className = "net.minecraft.class_3532")
    static final String[][] MATH_HELPER = {
            {"field_15727", double[].class.descriptorString()},
            {"field_15722", double[].class.descriptorString()},
            {"field_15725", float[].class.descriptorString()},
            {"field_15723", int[].class.descriptorString(), ">1.17.1"},
            {"field_29852", long.class.descriptorString()},
            {"field_29853", long.class.descriptorString()},
            {"field_29854", long.class.descriptorString()},
            {"field_29855", long.class.descriptorString()},
            {"field_29844", float.class.descriptorString()},
            {"field_29845", float.class.descriptorString()},
            {"field_29846", float.class.descriptorString()},
            {"field_29847", float.class.descriptorString()},
            {"field_29848", float.class.descriptorString()},
            {"field_29849", float.class.descriptorString()},
            {"field_29856", float.class.descriptorString()},
            {"field_46243", Vector3f.class.descriptorString()},
            {"field_46244", Vector3f.class.descriptorString()},
            {"field_29857", double.class.descriptorString()},
            {"field_29858", int.class.descriptorString()},
            {"field_29859", int.class.descriptorString()},
            {"field_15728", double.class.descriptorString()}
    };
}
