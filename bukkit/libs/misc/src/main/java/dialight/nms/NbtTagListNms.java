package dialight.nms;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class NbtTagListNms implements NbtBaseNms {

    private static final Constructor<? extends NbtTagListNms> constructor;
    private static Method m_create;

    static {
        Class<? extends NbtTagListNms> clazz = ReflectionUtils.findCompatibleClass(NbtTagListNms.class);
        try {
            constructor = clazz.getConstructor(Object.class);
            m_create = clazz.getDeclaredMethod("create");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected final Object nbt;

    public NbtTagListNms(Object nbt) {
        if(nbt == null) throw new NullPointerException();
        this.nbt = nbt;
    }

    @Override public Object getNms() {
        return nbt;
    }

    public abstract int size();
    public abstract Object set(int index, NbtBaseNms value);
    public abstract boolean add(NbtBaseNms value);
    public abstract double getDouble(int index);
    public abstract float getFloat(int index);
    public abstract boolean setDouble(int index, double value);
    public abstract boolean setFloat(int index, float value);
    public abstract NbtTagCompoundNms getCompound(int i);

    @Override
    public String toString() {
        return nbt.toString();
    }

    public static NbtTagListNms of(Object nbt) {
        return ReflectionUtils.newInstance(constructor, nbt);
    }

    public static NbtTagListNms create() {
        try {
            return (NbtTagListNms) m_create.invoke(null);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

}

/*
net.minecraft.server.v1_13_R2.NBTTagList:
  boolean add(Object)
  boolean add(NBTBase)
  Object remove(int)
  NBTBase remove(int)
  Object get(int)
  NBTBase get(int)
  boolean equals(Object)
  String toString()
  int hashCode()
  NBTTagList clone()
  Object clone()
  NBTBase clone()
  boolean isEmpty()
  int size()
  void load(DataInput, int, NBTReadLimiter)
  void write(DataOutput)
  Object set(int, Object)
  NBTBase set(int, NBTBase)
  double getDoubleAt(int)
  NBTBase c(int)
  void b(int)
  String getString(int)
  IChatBaseComponent a(String, int)
  void a(int, NBTBase)
  int[] i(int)
  float l(int)
  double k(int)
  NBTTagList f(int)
  short g(int)
  int h(int)
  int d()
  NBTTagCompound getCompound(int)
  byte getTypeId()
  void add(int, Object)
  int indexOf(Object)
  void clear()
  Iterator iterator()
  int lastIndexOf(Object)
  List subList(int, int)
  boolean addAll(int, Collection)
  ListIterator listIterator(int)
  ListIterator listIterator()
  boolean remove(Object)
  boolean contains(Object)
  Object[] toArray(Object[])
  Object[] toArray()
  boolean addAll(Collection)
  boolean containsAll(Collection)
  boolean removeAll(Collection)
  boolean retainAll(Collection)
  void wait()
  void wait(long, int)
  void wait(long)
  Class getClass()
  void notify()
  void notifyAll()
  Stream stream()
  boolean removeIf(Predicate)
  Stream parallelStream()
  void forEach(Consumer)
  void replaceAll(UnaryOperator)
  Spliterator spliterator()
  void sort(Comparator)
  IChatBaseComponent k()
  String asString()
*/
