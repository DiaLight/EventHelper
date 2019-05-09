package dialight.nms;

import java.lang.reflect.Method;

public class NBTTagListNms {

    public static final Class<?> NMS_class = ReflectionUtils.getNMSClass("NBTTagList");

    // Reflection cache
    private static Method m_NBTTagList_getFloat;
    private static Method m_NBTTagList_set;
    private static Method m_NBTTagList_getDouble;

    private final Object nbt;

    public NBTTagListNms(Object nbt) {
        if(nbt == null) throw new NullPointerException();
        this.nbt = nbt;
    }

    public Object set(int index, Object value) {
        if(m_NBTTagList_set == null) m_NBTTagList_set = ReflectionUtils.getMethod(NMS_class, new String[]{"set", "a"}, int.class, NBTTagCompoundNms.NBTBase_class);
        return ReflectionUtils.invokeMethod(m_NBTTagList_set, nbt, index, value);
    }

    public double getDouble(int index) {
        if(m_NBTTagList_getDouble == null) m_NBTTagList_getDouble = ReflectionUtils.findSingleMethod(NMS_class, double.class, new String[]{"getDoubleAt"}, int.class);
        return (double) ReflectionUtils.invokeMethod(m_NBTTagList_getDouble, nbt, index);
    }

    public float getFloat(int index) {
        if(m_NBTTagList_getFloat == null) m_NBTTagList_getFloat = ReflectionUtils.findSingleMethod(NMS_class, float.class, new String[]{"getFloatAt"}, int.class);
        return (float) ReflectionUtils.invokeMethod(m_NBTTagList_getFloat, nbt, index);
    }

    public boolean setDouble(int index, double value) {
        Object obj = NBTTagCompoundNms.createDouble(value);
        Object oldValue = set(index, obj);
        return oldValue != null;
    }

    public boolean setFloat(int index, float value) {
        Object obj = NBTTagCompoundNms.createFloat(value);
        Object oldValue = set(index, obj);
        return oldValue != null;
    }

    public Object getInternal() {
        return nbt;
    }

    @Override
    public String toString() {
        return nbt.toString();
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
