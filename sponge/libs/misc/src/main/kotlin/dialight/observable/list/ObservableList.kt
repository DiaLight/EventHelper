package dialight.observable.list

import java.util.*

abstract class ObservableList<E> {

    private val onAdd: MutableCollection<(E, Int) -> Unit> = LinkedList()
    private val onRemove: MutableCollection<(E, Int) -> Unit> = LinkedList()
    private val onChange: MutableCollection<(E, E, Int) -> Unit> = LinkedList()
    private val onMove: MutableCollection<(E, Int, Int) -> Unit> = LinkedList()


    protected fun fireAdd(e: E, i: Int) {
        for(op in onAdd) op(e, i)
    }
    protected fun fireRemove(e: E, i: Int) {
        for(op in onRemove) op(e, i)
    }
    protected fun fireChange(old: E, new: E, i: Int) {
        for(op in onChange) op(old, new, i)
    }
    protected fun fireMove(e: E, old: Int, new: Int) {
        for(op in onMove) op(e, old, new)
    }


    fun onAdd(op: (E, Int) -> Unit): ObservableList<E> {
        onAdd += op
        return this
    }
    fun onRemove(op: (E, Int) -> Unit): ObservableList<E> {
        onRemove += op
        return this
    }
    fun onChange(op: (E, E, Int) -> Unit): ObservableList<E> {
        onChange += op
        return this
    }
    fun onMove(op: (E, Int, Int) -> Unit): ObservableList<E> {
        onMove += op
        return this
    }


    abstract fun add(element: E): Boolean
    abstract fun remove(element: E): Boolean

    abstract operator fun set(index: Int, element: E): E
    abstract fun removeAt(index: Int): E

    abstract fun move(fromIndex: Int, toIndex: Int)
    abstract fun replace(list: List<E>)
    abstract fun updateAt(index: Int)
    abstract fun update(element: E): Boolean

}
