package dialight.patch;

import dialight.nms.ReflectionUtils;
import dialight.patch.asm.ClassNodeEx;
import dialight.patch.asm.InsnListEx;
import org.bukkit.Bukkit;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.net.URLClassLoader;
import java.util.NoSuchElementException;

public class ScoreboardTeamPatch8 extends ScoreboardTeamPatch {

    public static String CLASS_NAME = "net.minecraft.server." + ReflectionUtils.SERVER_VERSION + ".ScoreboardTeam";

    private void patch_setDisplayName(ClassNodeEx node) throws NoSuchMethodException {
        node.fields.add(new FieldNode(Opcodes.ACC_PUBLIC, "eh_setDisplayName", "Ljava/util/function/Consumer;", "Ljava/util/function/Consumer<Ljava/lang/String;>;", null));

        String name = "setDisplayName";
        MethodNode method = node.findMethod(name, "(Ljava/lang/String;)V");
        if(method == null) throw new NoSuchMethodException("can't find " + name + " method");
        MethodNode orig = new MethodNode(Opcodes.ACC_PUBLIC, name + "_orig", method.desc, method.signature, null);
        orig.exceptions = method.exceptions;
        method.instructions.accept(orig);
        node.methods.add(orig);

        AbstractInsnNode point = null;
        for (AbstractInsnNode insn : new InsnListEx(method.instructions)) {
            if(insn.getOpcode() != Opcodes.ATHROW) continue;
            insn = insn.getNext();
            if(insn.getType() != AbstractInsnNode.LABEL) continue;
            insn = insn.getNext();
            if(insn.getType() != AbstractInsnNode.LINE) continue;
            insn = insn.getNext();
            if(insn.getType() != AbstractInsnNode.FRAME) continue;
            point = insn.getNext();
        }
        if(point == null) throw new NoSuchElementException("can't find target instruction");

        InsnList insnList = new InsnList();
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
        insnList.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/server/v1_8_R3/ScoreboardTeam", "eh_setDisplayName", "Ljava/util/function/Consumer;"));
        LabelNode label1 = new LabelNode();
        insnList.add(new JumpInsnNode(Opcodes.IFNULL, label1));
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
        insnList.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/server/v1_8_R3/ScoreboardTeam", "eh_setDisplayName", "Ljava/util/function/Consumer;"));
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 1));
        insnList.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "java/util/function/Consumer", "accept", "(Ljava/lang/Object;)V", true));
        insnList.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
        insnList.add(label1);

        method.instructions.insert(point, insnList);
    }

    private void patch_setPrefix(ClassNodeEx node) throws NoSuchMethodException {
        node.fields.add(new FieldNode(Opcodes.ACC_PUBLIC, "eh_setPrefix", "Ljava/util/function/Consumer;", "Ljava/util/function/Consumer<Ljava/lang/String;>;", null));

        String name = "setPrefix";
        MethodNode method = node.findMethod(name, "(Ljava/lang/String;)V");
        if(method == null) throw new NoSuchMethodException("can't find " + name + " method");
        MethodNode orig = new MethodNode(Opcodes.ACC_PUBLIC, name + "_orig", method.desc, method.signature, null);
        orig.exceptions = method.exceptions;
        method.instructions.accept(orig);
        node.methods.add(orig);

        AbstractInsnNode point = null;
        for (AbstractInsnNode insn : new InsnListEx(method.instructions)) {
            if(insn.getOpcode() != Opcodes.ATHROW) continue;
            insn = insn.getNext();
            if(insn.getType() != AbstractInsnNode.LABEL) continue;
            insn = insn.getNext();
            if(insn.getType() != AbstractInsnNode.LINE) continue;
            insn = insn.getNext();
            if(insn.getType() != AbstractInsnNode.FRAME) continue;
            point = insn.getNext();
        }
        if(point == null) throw new NoSuchElementException("can't find target instruction");

        InsnList insnList = new InsnList();
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
        insnList.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/server/v1_8_R3/ScoreboardTeam", "eh_setPrefix", "Ljava/util/function/Consumer;"));
        LabelNode label1 = new LabelNode();
        insnList.add(new JumpInsnNode(Opcodes.IFNULL, label1));
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
        insnList.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/server/v1_8_R3/ScoreboardTeam", "eh_setPrefix", "Ljava/util/function/Consumer;"));
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 1));
        insnList.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "java/util/function/Consumer", "accept", "(Ljava/lang/Object;)V", true));
        insnList.add(label1);
        insnList.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));

        method.instructions.insert(point, insnList);
    }

    private void patch_setSuffix(ClassNodeEx node) throws NoSuchMethodException {
        node.fields.add(new FieldNode(Opcodes.ACC_PUBLIC, "eh_setSuffix", "Ljava/util/function/Consumer;", "Ljava/util/function/Consumer<Ljava/lang/String;>;", null));

        String name = "setSuffix";
        MethodNode method = node.findMethod(name, "(Ljava/lang/String;)V");
        if(method == null) throw new NoSuchMethodException("can't find " + name + " method");
        MethodNode orig = new MethodNode(Opcodes.ACC_PUBLIC, name + "_orig", method.desc, method.signature, null);
        orig.exceptions = method.exceptions;
        method.instructions.accept(orig);
        node.methods.add(orig);

        InsnList insnList = new InsnList();
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
        insnList.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/server/v1_8_R3/ScoreboardTeam", "eh_setSuffix", "Ljava/util/function/Consumer;"));
        LabelNode label0 = new LabelNode();
        insnList.add(new JumpInsnNode(Opcodes.IFNULL, label0));
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
        insnList.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/server/v1_8_R3/ScoreboardTeam", "eh_setSuffix", "Ljava/util/function/Consumer;"));
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 1));
        insnList.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "java/util/function/Consumer", "accept", "(Ljava/lang/Object;)V", true));
        insnList.add(label0);
        insnList.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));

        method.instructions.insert(insnList);
    }

    private void patch_setAllowFriendlyFire(ClassNodeEx node) throws NoSuchMethodException {
        node.fields.add(new FieldNode(Opcodes.ACC_PUBLIC, "eh_setAllowFriendlyFire", "Ljava/util/function/Consumer;", "Ljava/util/function/Consumer<Ljava/lang/Boolean;>;", null));

        String name = "setAllowFriendlyFire";
        MethodNode method = node.findMethod(name, "(Z)V");
        if(method == null) throw new NoSuchMethodException("can't find " + name + " method");
        MethodNode orig = new MethodNode(Opcodes.ACC_PUBLIC, name + "_orig", method.desc, method.signature, null);
        orig.exceptions = method.exceptions;
        method.instructions.accept(orig);
        node.methods.add(orig);

        InsnList insnList = new InsnList();
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
        insnList.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/server/v1_8_R3/ScoreboardTeam", "eh_setAllowFriendlyFire", "Ljava/util/function/Consumer;"));
        LabelNode label0 = new LabelNode();
        insnList.add(new JumpInsnNode(Opcodes.IFNULL, label0));
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
        insnList.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/server/v1_8_R3/ScoreboardTeam", "eh_setAllowFriendlyFire", "Ljava/util/function/Consumer;"));
        insnList.add(new VarInsnNode(Opcodes.ILOAD, 1));
        insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;", false));
        insnList.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "java/util/function/Consumer", "accept", "(Ljava/lang/Object;)V", true));
        insnList.add(label0);
        insnList.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));

        method.instructions.insert(insnList);
    }

    private void patch_setCanSeeFriendlyInvisibles(ClassNodeEx node) throws NoSuchMethodException {
        node.fields.add(new FieldNode(Opcodes.ACC_PUBLIC, "eh_setCanSeeFriendlyInvisibles", "Ljava/util/function/Consumer;", "Ljava/util/function/Consumer<Ljava/lang/Boolean;>;", null));

        String name = "setCanSeeFriendlyInvisibles";
        MethodNode method = node.findMethod(name, "(Z)V");
        if(method == null) throw new NoSuchMethodException("can't find " + name + " method");
        MethodNode orig = new MethodNode(Opcodes.ACC_PUBLIC, name + "_orig", method.desc, method.signature, null);
        orig.exceptions = method.exceptions;
        method.instructions.accept(orig);
        node.methods.add(orig);

        InsnList insnList = new InsnList();
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
        insnList.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/server/v1_8_R3/ScoreboardTeam", "eh_setCanSeeFriendlyInvisibles", "Ljava/util/function/Consumer;"));
        LabelNode label0 = new LabelNode();
        insnList.add(new JumpInsnNode(Opcodes.IFNULL, label0));
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
        insnList.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/server/v1_8_R3/ScoreboardTeam", "eh_setCanSeeFriendlyInvisibles", "Ljava/util/function/Consumer;"));
        insnList.add(new VarInsnNode(Opcodes.ILOAD, 1));
        insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;", false));
        insnList.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "java/util/function/Consumer", "accept", "(Ljava/lang/Object;)V", true));
        insnList.add(label0);
        insnList.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));

        method.instructions.insert(insnList);
    }

    private void patch_setNameTagVisibility(ClassNodeEx node) throws NoSuchMethodException {
        node.fields.add(new FieldNode(Opcodes.ACC_PUBLIC, "eh_setNameTagVisibility", "Ljava/util/function/Consumer;", "Ljava/util/function/Consumer<Lnet/minecraft/server/v1_8_R3/ScoreboardTeamBase$EnumNameTagVisibility;>;", null));

        String name = "setNameTagVisibility";
        MethodNode method = node.findMethod(name, "(Lnet/minecraft/server/v1_8_R3/ScoreboardTeamBase$EnumNameTagVisibility;)V");
        if(method == null) throw new NoSuchMethodException("can't find " + name + " method");
        MethodNode orig = new MethodNode(Opcodes.ACC_PUBLIC, name + "_orig", method.desc, method.signature, null);
        orig.exceptions = method.exceptions;
        method.instructions.accept(orig);
        node.methods.add(orig);

        InsnList insnList = new InsnList();
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
        insnList.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/server/v1_8_R3/ScoreboardTeam", "eh_setNameTagVisibility", "Ljava/util/function/Consumer;"));
        LabelNode label0 = new LabelNode();
        insnList.add(new JumpInsnNode(Opcodes.IFNULL, label0));
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
        insnList.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/server/v1_8_R3/ScoreboardTeam", "eh_setNameTagVisibility", "Ljava/util/function/Consumer;"));
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 1));
        insnList.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "java/util/function/Consumer", "accept", "(Ljava/lang/Object;)V", true));
        insnList.add(label0);
        insnList.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));

        method.instructions.insert(insnList);
    }

    private void patch_setCollisionRule(ClassNodeEx node) throws NoSuchMethodException {
        node.fields.add(new FieldNode(Opcodes.ACC_PUBLIC, "eh_setCollisionRule", "Ljava/util/function/Consumer;", "Ljava/util/function/Consumer<Lnet/minecraft/server/v1_8_R3/ScoreboardTeamBase$EnumNameTagVisibility;>;", null));

        String name = "b";
        MethodNode method = node.findMethod(name, "(Lnet/minecraft/server/v1_8_R3/ScoreboardTeamBase$EnumNameTagVisibility;)V");
        if(method == null) throw new NoSuchMethodException("can't find " + name + " method");
        MethodNode orig = new MethodNode(Opcodes.ACC_PUBLIC, name + "_orig", method.desc, method.signature, null);
        orig.exceptions = method.exceptions;
        method.instructions.accept(orig);
        node.methods.add(orig);

        InsnList insnList = new InsnList();
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
        insnList.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/server/v1_8_R3/ScoreboardTeam", "eh_setCollisionRule", "Ljava/util/function/Consumer;"));
        LabelNode label0 = new LabelNode();
        insnList.add(new JumpInsnNode(Opcodes.IFNULL, label0));
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
        insnList.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/server/v1_8_R3/ScoreboardTeam", "eh_setCollisionRule", "Ljava/util/function/Consumer;"));
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 1));
        insnList.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "java/util/function/Consumer", "accept", "(Ljava/lang/Object;)V", true));
        insnList.add(label0);
        insnList.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));

        method.instructions.insert(insnList);
    }

    private boolean patch(ClassNodeEx node) {
        try {
            patch_setDisplayName(node);
            patch_setPrefix(node);
            patch_setSuffix(node);
            patch_setAllowFriendlyFire(node);
            patch_setCanSeeFriendlyInvisibles(node);
            patch_setNameTagVisibility(node);
            patch_setCollisionRule(node);
            node.fields.add(new FieldNode(Opcodes.ACC_PUBLIC, "eh_patched", "Ljava/lang/Object;", null, null));
            return true;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override public boolean patch() {
        boolean success = Patcher.loadPatchClass(CLASS_NAME, this::patch);
        if(!success) {
            URLClassLoader classLoader = (URLClassLoader) Bukkit.getServer().getClass().getClassLoader();
            try {
                Class<?> cl_ScoreboardTeam = Class.forName(CLASS_NAME, false, classLoader);
                cl_ScoreboardTeam.getDeclaredField("eh_patched");
                success = true;
            } catch (NoSuchFieldException ignore) {
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return success;
    }

}
