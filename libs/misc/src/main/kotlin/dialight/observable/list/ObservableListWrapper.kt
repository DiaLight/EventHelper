package dialight.observable.list


class ObservableListWrapper<E>(
    val list: MutableList<E> = mutableListOf()
) : ObservableList<E>(), MutableList<E> {


    override fun set(index: Int, element: E): E {
        val old = list[index]
        list[index] = element
        fireChange(old, element, index)
        return element
    }

    override fun add(element: E): Boolean {
        val result = list.add(element)
        val index = list.size - 1
        if (result) {
            fireAdd(element, index)
        }
        return result
    }

    override fun add(index: Int, element: E) {
        list.add(index, element)
        fireAdd(element, index)
    }

    override fun addAll(elements: Collection<E>): Boolean {
        var index = list.size
        for (e in elements) {
            list.add(e)
            fireAdd(e, index)
            index++
        }
        return true
    }

    override fun addAll(index: Int, elements: Collection<E>): Boolean {
        var currentIndex = index
        for (e in elements) {
            list.add(currentIndex, e)
            fireAdd(e, currentIndex)
            currentIndex++
        }
        return true
    }

    @Suppress("UNCHECKED_CAST")
    override fun remove(element: E): Boolean {
        val index = indexOf(element)
        if (index == -1) return false
        list.removeAt(index)
        fireRemove(element, index)
        return true
    }

    override fun removeAt(index: Int): E {
        val element = list.removeAt(index)
        fireRemove(element, index)
        return element
    }

    @Suppress("UNCHECKED_CAST")
    override fun removeAll(elements: Collection<E>): Boolean {
        var anyRemoved = false
        for (e in elements) {
            if(remove(e)) {
                anyRemoved = true
            }
        }
        return anyRemoved
    }

    override fun retainAll(elements: Collection<E>): Boolean {
        throw UnsupportedOperationException()
    }

    override fun clear() {
        for (e in list) {
            fireRemove(e, 0)
        }
        list.clear()
    }

    override fun isEmpty(): Boolean = list.isEmpty()
    override fun contains(element: E): Boolean = list.contains(element)
    override fun containsAll(elements: Collection<E>): Boolean = list.containsAll(elements)
    override fun listIterator(): MutableListIterator<E> = throw UnsupportedOperationException()
    override fun listIterator(index: Int): MutableListIterator<E> = throw UnsupportedOperationException()
    /**
     * WARNING:
     * This iterator MAY have issues when it removes things, because there is no way to know if the iterator is done with its work.
     * It will not call onUpdate, and calls onRemove while iterating.
     *
     * YOU HAVE BEEN WARNED.
     */
    override fun iterator(): MutableIterator<E> = object : MutableIterator<E> {
        val inner = list.iterator()
        var lastIndex: Int = -1
        var lastElement: E? = null
        override fun hasNext(): Boolean = inner.hasNext()
        override fun next(): E {
            val element = inner.next()
            lastElement = element
            lastIndex++
            return element
        }

        override fun remove() {
            inner.remove()
            fireRemove(lastElement!!, lastIndex)
            lastIndex--
        }

    }
    override fun subList(fromIndex: Int, toIndex: Int): MutableList<E> = list.subList(fromIndex, toIndex)
    override fun get(index: Int): E = list[index]
    override fun indexOf(element: E): Int = list.indexOf(element)
    override fun lastIndexOf(element: E): Int = list.lastIndexOf(element)
    override val size: Int get() = list.size

    override fun replace(list: List<E>) {
        this.list.clear()
        this.list.addAll(list)
    }

    override fun move(fromIndex: Int, toIndex: Int) {
        val item = list.removeAt(fromIndex)
        list.add(toIndex, item)
        fireMove(item, fromIndex, toIndex)
    }

    override fun updateAt(index: Int) {
        this[index] = this[index]
    }
    override fun update(element: E): Boolean {
        val index = indexOf(element)
        if (index != -1)
            updateAt(index)
        return index != -1
    }
}

fun <E> observableListOf(vararg items: E) = ObservableListWrapper(items.toMutableList())
