package dialight.observable.collection

open class ObservableCollectionWrapper<E>(
    val collection: MutableCollection<E> = mutableListOf()
) : ObservableCollection<E>(), MutableCollection<E> {

    override val size get() = collection.size
    override fun contains(element: E) = collection.contains(element)
    override fun containsAll(elements: Collection<E>) = collection.containsAll(elements)
    override fun isEmpty() = collection.isEmpty()

    override fun add(element: E): Boolean {
        val result = collection.add(element)
        if (result) {
            fireAdd(element)
        }
        return result
    }

    override fun addAll(elements: Collection<E>): Boolean {
        var index = collection.size
        for (e in elements) {
            collection.add(e)
            fireAdd(e)
            index++
        }
        return true
    }

    override fun clear() {
        for (e in collection) {
            fireRemove(e)
        }
    }


    override fun iterator(): MutableIterator<E> = object : MutableIterator<E> {
        val inner = collection.iterator()
        var lastElement: E? = null
        override fun hasNext(): Boolean = inner.hasNext()
        override fun next(): E {
            val element = inner.next()
            lastElement = element
            return element
        }

        override fun remove() {
            inner.remove()
            fireRemove(lastElement!!)
        }

    }

    override fun remove(element: E): Boolean {
        if(collection.remove(element)) {
            fireRemove(element)
            return true
        }
        return false
    }

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
}