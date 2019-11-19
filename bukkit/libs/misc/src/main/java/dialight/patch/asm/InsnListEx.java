package dialight.patch.asm;

import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;

import java.util.Iterator;

public class InsnListEx implements Iterable<AbstractInsnNode> {

    private final InsnList list;

    public InsnListEx(InsnList list) {
        this.list = list;
    }

    @NotNull @Override public Iterator<AbstractInsnNode> iterator() {
        return list.iterator();
    }

}
