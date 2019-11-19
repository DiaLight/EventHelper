package dialight.patch.asm;

import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class ClassNodeEx extends ClassNode {

    public ClassNodeEx() {
        super(Opcodes.ASM7);
    }

    public static ClassNodeEx deserialize(byte[] data) {
        ClassReader cr = new ClassReader(data);
        ClassNodeEx cn = new ClassNodeEx();
        try {
            cr.accept(cn, ClassReader.EXPAND_FRAMES);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            cr = null;
        }
        return cn;
    }

    public byte[] serialize() {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        this.accept(cw);
        return cw.toByteArray();
    }

    @Nullable public MethodNode findMethod(String name, String desc) {
        for (MethodNode method : this.methods) {
            if(method.name.equals(name) && method.desc.equals(desc)) {
                return method;
            }
        }
        return null;
    }

}
