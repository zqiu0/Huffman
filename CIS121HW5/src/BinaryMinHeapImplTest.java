import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

public class BinaryMinHeapImplTest {
    @Test
    public void heapSizeOneElement() {
        BinaryMinHeap heap = new BinaryMinHeapImpl();
        heap.add(1, 2);
        assertEquals(1, heap.size());
    }
    
    @Test
    public void testHeapSizeTwoElement() {
        BinaryMinHeap heap = new BinaryMinHeapImpl();
        heap.add(1, 2);
        heap.add(2, 3);
        assertEquals(2, heap.size());
    }
    
    @Test
    public void testHeapSizeTwoAddSmaller() {
        BinaryMinHeap heap = new BinaryMinHeapImpl();
        heap.add(1, 2);
        heap.add(-1, 5);
        assertEquals(2, heap.size());
    }
    
    @Test
    public void testHeapSizeThreeElement() {
        BinaryMinHeap heap = new BinaryMinHeapImpl();
        heap.add(1, 2);
        heap.add(2, 3);
        heap.add(0, 5);
        assertEquals(3, heap.size());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddNullKey() {
        BinaryMinHeap heap = new BinaryMinHeapImpl();
        heap.add(null, 5);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddExistingValue() {
        BinaryMinHeap heap = new BinaryMinHeapImpl();
        heap.add(1, 2);
        heap.add(2, 2);
    }
    
    @Test
    public void testContainsEmptyHeap() {
        BinaryMinHeap heap = new BinaryMinHeapImpl();
        assertFalse(heap.containsValue(4));
    }
    
    @Test
    public void testContainsNullValue() {
        BinaryMinHeap heap = new BinaryMinHeapImpl();
        assertFalse(heap.containsValue(null));
    }
    
    @Test
    public void testContainsValueTrue() {
        BinaryMinHeap heap = new BinaryMinHeapImpl();
        heap.add(1, 2);
        heap.add(2, 3);
        heap.add(0, 5);
        assertTrue(heap.containsValue(2));
    }
    
    @Test
    public void testContainsValueFalse() {
        BinaryMinHeap heap = new BinaryMinHeapImpl();
        heap.add(1, 2);
        heap.add(2, 3);
        heap.add(0, 5);
        assertFalse(heap.containsValue(6));
    }
    
    @Test(expected = NoSuchElementException.class)
    public void testPeekEmptyHeap() {
        BinaryMinHeap heap = new BinaryMinHeapImpl();
        heap.peek();
    }
    
    @Test
    public void testPeekOneElement() {
        BinaryMinHeap heap = new BinaryMinHeapImpl();
        heap.add(1, 2);
        assertEquals(2, heap.peek());
    }
    
    @Test
    public void testPeekTwoElements() {
        BinaryMinHeap heap = new BinaryMinHeapImpl();
        heap.add(1, 2);
        heap.add(2, 3);
        assertEquals(2, heap.peek());
    }
    
    @Test
    public void testPeekThreeElements() {
        BinaryMinHeap heap = new BinaryMinHeapImpl();
        heap.add(1, 2);
        heap.add(2, 3);
        heap.add(0, 5);
        assertEquals(5, heap.peek());
    }
    
    @Test(expected = NoSuchElementException.class)
    public void testExtractMinEmpty() {
        BinaryMinHeap heap = new BinaryMinHeapImpl();
        heap.extractMin();
    }
    
    @Test
    public void testExtractMinOneElement() {
        BinaryMinHeap heap = new BinaryMinHeapImpl();
        heap.add(1, 2);
        assertEquals(2, heap.extractMin());
        assertTrue(heap.isEmpty());
    }
    
    @Test(expected = NoSuchElementException.class)
    public void testExtractMinOneElementAndPeek() {
        BinaryMinHeap heap = new BinaryMinHeapImpl();
        heap.add(1, 2);
        heap.extractMin();
        heap.peek();
    }
    
    @Test
    public void testExtractMinTwoElement() {
        BinaryMinHeap heap = new BinaryMinHeapImpl();
        heap.add(1, 2);
        heap.add(2, 3);
        assertEquals(2, heap.extractMin());
        assertEquals(3, heap.peek());
        assertEquals(1, heap.size());
    }
    
    @Test
    public void testExtractMinThreeElement() {
        BinaryMinHeap heap = new BinaryMinHeapImpl();
        heap.add(2, 'c');
        heap.add(3, 'a');
        heap.add(3, 'b');
        assertEquals('c', heap.extractMin());
        assertEquals('b', heap.extractMin());
        assertEquals('a', heap.extractMin());
    }
    
    @Test(expected = NoSuchElementException.class)
    public void testExtractMinTooMany() {
        BinaryMinHeap heap = new BinaryMinHeapImpl();
        heap.add(2, 'c');
        heap.add(3, 'a');
        heap.add(3, 'b');
        assertEquals('c', heap.extractMin());
        assertEquals('b', heap.extractMin());
        assertEquals('a', heap.extractMin());
        heap.extractMin();
    }
    
    @Test
    public void testValuesEmpty() {
        BinaryMinHeap heap = new BinaryMinHeapImpl();
        Set v = heap.values();
        assertTrue(v.isEmpty());
    }
    
    @Test
    public void testValuesOne() {
        BinaryMinHeap heap = new BinaryMinHeapImpl();
        heap.add(1, 2);
        Set v = heap.values();
        Set expected = new TreeSet<>();
        expected.add(2);
        assertEquals(expected, v);
    }
    
    @Test
    public void testValuesTwo() {
        BinaryMinHeap heap = new BinaryMinHeapImpl();
        heap.add(1, 2);
        heap.add(2, 3);
        Set v = heap.values();
        Set expected = new TreeSet<>();
        expected.add(2);
        expected.add(3);
        assertEquals(expected, v);
    }
    
    @Test
    public void testSwap() {
        BinaryMinHeap heap = new BinaryMinHeapImpl();
        heap.add(1, 2);
        heap.add(2, 3);
        ((BinaryMinHeapImpl) heap).swap(1, 2);
        assertEquals(3, heap.peek());
    }
    
    @Test
    public void testMinHeapify() {
        BinaryMinHeap heap = new BinaryMinHeapImpl();
        heap.add(1, 2);
        heap.add(2, 3);
        heap.add(3, 5);
        ((BinaryMinHeapImpl) heap).swap(1, 3);
        assertEquals(5, heap.peek());
        ((BinaryMinHeapImpl) heap).minHeapify(1);
        assertEquals(2, heap.peek());
    }
    
}
