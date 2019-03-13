package dialight.observable.collection

import java.util.*

abstract class ObservableCollection<E> {

    private val onAdd: MutableCollection<(E) -> Unit> = LinkedList()
    private val onRemove: MutableCollection<(E) -> Unit> = LinkedList()


    protected fun fireAdd(e: E) {
        for(op in onAdd) op(e)
    }
    protected fun fireRemove(e: E) {
        for(op in onRemove) op(e)
    }


    fun onAdd(op: (E) -> Unit): ObservableCollection<E> {
        onAdd += op
        return this
    }
    fun onRemove(op: (E) -> Unit): ObservableCollection<E> {
        onRemove += op
        return this
    }


    abstract fun add(element: E): Boolean
    abstract fun remove(element: E): Boolean

}