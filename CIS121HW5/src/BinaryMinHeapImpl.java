import java.util.ArrayList;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 *
 * @param <V> {@inheritDoc}
 * @param <Key> {@inheritDoc}
 *
 */
public class BinaryMinHeapImpl<Key extends Comparable<Key>, V> implements BinaryMinHeap<Key, V> {
    private int size;
    private ArrayList<Entry<Key, V>> heap;
    
    
    public BinaryMinHeapImpl() {
        size = 0;
        heap = new ArrayList<Entry<Key, V>>();
        heap.add(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsValue(V value) {
        if (isEmpty()) {
            return false;
        }
        
        Set<V> heapValue = values();
        return heapValue.contains(value);
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(Key key, V value) {
        if (key == (null) || containsValue(value)) {
            throw new IllegalArgumentException();
        }
        
        heap.add(new Entry<Key, V>(key, value));
        size++;
        int i = size();
        
        while (i != 1 && heap.get(i).getKey().compareTo(heap.get(parent(i)).getKey()) < 0) {
            swap(i, parent(i));
            i = parent(i);
        }
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public V peek() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return heap.get(1).getValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public V extractMin() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        if (size() == 1) {
            size--;
            return heap.remove(1).getValue();
        }

        BinaryMinHeapImpl<Key, V>.Entry<Key, V> last = heap.remove(size);
        BinaryMinHeapImpl<Key, V>.Entry<Key, V> min = heap.get(1);
        size--;
        heap.set(1, last);
        minHeapify(1);
        return min.getValue();
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<V> values() {
        Set<V> value = new HashSet<V>();
        for (int i = 1; i <= size(); i++) {
            value.add(heap.get(i).getValue());
        }
        return value;
    }
    
    int parent(int i) {
        return i / 2;
    }
    
    int leftChild(int i) {
        return 2 * i;
    }
    
    int rightChild(int i) {
        return 2 * i + 1;
    }
    
    void swap(int i, int j) {
        BinaryMinHeapImpl<Key, V>.Entry<Key, V> temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }
    
    void minHeapify(int i) {
        int left = leftChild(i);
        int right = rightChild(i);
        int min = 0;
        
        if (left <= size() && heap.get(left).getKey().compareTo(heap.get(i).getKey()) < 0) {
            min = left;
        } else {
            min = i;
        }
        
        if (right <= size() && heap.get(right).getKey().compareTo(heap.get(min).getKey()) < 0) {
            min = right;
        }
        
        if (min != i) {
            swap(i, min);
            minHeapify(min);
        }
    }
    
    /**
     * Helper entry class for maintaining value-key pairs.
     * The underlying indexed list for your heap will contain
     * these entries.
     *
     * You are not required to use this, but we recommend it.
     */
    class Entry<A, B> {

        private A key;
        private B value;

        public Entry(A key, B value) {
            this.value = value;
            this.key = key;
        }

        /**
         * @return  the value stored in the entry
         */
        public B getValue() {
            return this.value;
        }

        /**
         * @return  the key stored in the entry
         */
        public A getKey() {
            return this.key;
        }


    }

}