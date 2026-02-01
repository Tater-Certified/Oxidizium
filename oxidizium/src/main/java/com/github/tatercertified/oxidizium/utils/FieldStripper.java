package com.github.tatercertified.oxidizium.utils;

import com.github.tatercertified.oxidizium.Config;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * This class is so unbelievably cursed... just don't worry about it :)
 */
public class FieldStripper {
    public static final Logger STRIPPER_LOGGER = LoggerFactory.getLogger("FieldStripper");

    public static void stripFieldsWithClassNode(String className, ClassNode node, String[][] fieldsToStrip) {
        if (Config.getInstance().debug()) {
            STRIPPER_LOGGER.info("Beginning Stripping Process");
        }
        List<String> fields = new ArrayList<>();

        for (String[] field : fieldsToStrip) {
            fields.add(MappingTranslator.remapFieldName(className, field[0], field[1]));
        }

        node.fields.removeIf(field -> {
            if (fields.contains(field.name)) {
                if (Config.getInstance().debug()) {
                    STRIPPER_LOGGER.info("Stripped field: {}", field.name);
                }
                return true;
            }
            return false;
        });

        String internalName = node.name.replace('.', '/');
        stripStaticFieldInitializers(node, fields, internalName);
        removeStaticFieldUsagesInClinit(node, fields, internalName);

        if (Config.getInstance().debug()) {
            STRIPPER_LOGGER.info("Finished Stripping Process");
        }
    }

    private static void stripStaticFieldInitializers(ClassNode classNode, List<String> strippedFieldNames, String internalClassName) {
        for (MethodNode method : classNode.methods) {
            if (!method.name.equals("<clinit>")) continue;

            InsnList instructions = method.instructions;
            List<AbstractInsnNode> toRemove = new ArrayList<>();

            for (AbstractInsnNode insn : instructions) {
                if (insn instanceof FieldInsnNode fieldInsn &&
                        fieldInsn.getOpcode() == Opcodes.PUTSTATIC &&
                        fieldInsn.owner.equals(internalClassName) &&
                        strippedFieldNames.contains(fieldInsn.name)) {

                    if (Config.getInstance().debug()) {
                        STRIPPER_LOGGER.info("Stripping static field initializer: {}", fieldInsn.name);
                    }

                    AbstractInsnNode start = insn.getPrevious();

                    int stackDepth = 1;
                    int insnLimit = 32;
                    while (start != null && insnLimit-- > 0) {
                        if (start instanceof InsnNode || start instanceof MethodInsnNode ||
                                start instanceof LdcInsnNode || start instanceof IntInsnNode ||
                                start instanceof VarInsnNode || start instanceof TypeInsnNode) {
                            // Heuristic: this might push to the stack
                            stackDepth--;
                        }

                        if (stackDepth == 0) {
                            break;
                        }

                        start = start.getPrevious();
                    }

                    // Collect all instructions from start to end
                    AbstractInsnNode cur = (start != null) ? start.getNext() : instructions.getFirst();
                    while (cur != null && cur != insn.getNext()) {
                        toRemove.add(cur);
                        cur = cur.getNext();
                    }
                }
            }

            for (AbstractInsnNode insn : toRemove) {
                instructions.remove(insn);
            }

            // Ensure <clinit> ends correctly
            AbstractInsnNode last = instructions.getLast();
            if (last == null || last.getOpcode() != Opcodes.RETURN) {
                instructions.add(new InsnNode(Opcodes.RETURN));
            }
        }
    }

    private static void removeStaticFieldUsagesInClinit(ClassNode classNode, List<String> strippedFields, String internalName) {
        for (MethodNode method : classNode.methods) {
            if (!method.name.equals("<clinit>")) continue;

            InsnList insns = method.instructions;
            ListIterator<AbstractInsnNode> it = insns.iterator();

            while (it.hasNext()) {
                AbstractInsnNode insn = it.next();

                if (insn instanceof FieldInsnNode fieldInsn &&
                        fieldInsn.owner.equals(internalName) &&
                        strippedFields.contains(fieldInsn.name)) {

                    List<AbstractInsnNode> toRemove = new ArrayList<>();

                    if (fieldInsn.getOpcode() == Opcodes.GETSTATIC) {
                        // Possible read and use — try to find an array store like IASTORE, DASTORE, etc.
                        AbstractInsnNode current = insn;
                        toRemove.add(current);
                        for (int j = 0; j < 6 && current != null; j++) {
                            current = current.getNext();
                            if (current != null) toRemove.add(current);
                            if (current instanceof InsnNode) {
                                int op = current.getOpcode();
                                if (op >= Opcodes.IASTORE && op <= Opcodes.SASTORE) {
                                    break;
                                }
                            }
                        }
                    } else if (fieldInsn.getOpcode() == Opcodes.PUTSTATIC) {
                        // Assigning to the field, remove backward up to 3 or so instructions, and the PUTSTATIC
                        toRemove.add(fieldInsn);
                        AbstractInsnNode prev = fieldInsn.getPrevious();
                        for (int j = 0; j < 3 && prev != null; j++) {
                            toRemove.add(0, prev);
                            prev = prev.getPrevious();
                        }
                    }

                    if (Config.getInstance().debug()) {
                        STRIPPER_LOGGER.info("Removing usage of static field '{}'", fieldInsn.name);
                    }
                    for (AbstractInsnNode toDel : toRemove) {
                        insns.remove(toDel);
                    }

                    // Reset iterator since we mutated the list
                    it = insns.iterator();
                }
            }
        }
    }
}
