package quadTree;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class QuadTreeNodeImplTest {
    /** isSquare test */
    @Test
    public void testIsSquareTrue() {
        int[][] arr = new int[][]{
            {2, 1, 2},
            {1, 0, 1},
            {2, 1, 2},
        };
        assertTrue(QuadTreeNodeImpl.isSquare(arr));
    }
        
    @Test
    public void testIsSquareFalse() {
        int[][] arr = new int[][]{
            {2, 1, 2},
            {1, 0, 1},
            {2, 1, 2},
            {1, 0, 1},
            {2, 1, 2}
        };
        assertFalse(QuadTreeNodeImpl.isSquare(arr));
    }
        
    @Test
    public void testIsSquareJagged() {
        int[][] arr = new int[][]{
            {2, 1, 2},
            {1, 0},
            {2, 1, 2},
            {1, 0, 1, 3},
            {2, 1, 2}
        };
        assertFalse(QuadTreeNodeImpl.isSquare(arr));
    }
        
    /** isPowerOfTwo test */
    @Test
    public void testIsPowerOfTwoTrue() {
        assertTrue(QuadTreeNodeImpl.isPowerOfTwo(16));
    }
        
    @Test
    public void testIsPowerOfTwoFalse() {
        assertFalse(QuadTreeNodeImpl.isPowerOfTwo(5));
    }
        
    @Test
    public void testGetQuadFromArray() {
        int[][] arr = new int[][]{
            {2, 1},
            {1, 0},
        };
        QuadTreeNode q = new QuadTreeNodeImpl(2);
        ((QuadTreeNodeImpl) q).getQuadFromArray(arr, 0, 0, 2);
        QuadTreeNode quad = q.getQuadrant(QuadTreeNode.QuadName.TOP_LEFT);
        assertEquals(1, quad.getDimension());
        assertEquals(2, ((QuadTreeNodeImpl) quad).getLeafColor());
    }
    
    @Test
    public void testGetQuadFromArrayAllSameColor() {
        int[][] arr = new int[][]{
            {0, 0},
            {0, 0},
        };
        QuadTreeNode q = new QuadTreeNodeImpl(arr.length);
        ((QuadTreeNodeImpl) q).getQuadFromArray(arr, 0, 0, arr.length);
        assertEquals(1, q.getSize());
        assertNull(q.getQuadrant(QuadTreeNode.QuadName.TOP_LEFT));
    }
    
    @Test
    public void testSetQuad() {
        QuadTreeNode root = new QuadTreeNodeImpl(2);
        ((QuadTreeNodeImpl) root).setQuad(new QuadTreeNodeImpl(1, 1), QuadTreeNode.QuadName.BOTTOM_LEFT);
        assertEquals(1, ((QuadTreeNodeImpl) root.getQuadrant(QuadTreeNode.QuadName.BOTTOM_LEFT)).getLeafColor());
    }
    
    @Test
    public void testFindQuadrant2By2() {
        int[][] arr = new int[][]{
            {0, 1},
            {1, 0}, 
        };
        
        QuadTreeNode q = new QuadTreeNodeImpl(arr.length);
        ((QuadTreeNodeImpl) q).setQuad(new QuadTreeNodeImpl(0, 1), QuadTreeNode.QuadName.TOP_LEFT);
        ((QuadTreeNodeImpl) q).setQuad(new QuadTreeNodeImpl(1, 1), QuadTreeNode.QuadName.TOP_RIGHT);
        ((QuadTreeNodeImpl) q).setQuad(new QuadTreeNodeImpl(1, 1), QuadTreeNode.QuadName.BOTTOM_LEFT);
        ((QuadTreeNodeImpl) q).setQuad(new QuadTreeNodeImpl(0, 1), QuadTreeNode.QuadName.BOTTOM_RIGHT);
        
        assertEquals(QuadTreeNode.QuadName.TOP_LEFT, ((QuadTreeNodeImpl) q).findQuadrant(0, 0));
        assertEquals(QuadTreeNode.QuadName.TOP_RIGHT, ((QuadTreeNodeImpl) q).findQuadrant(0, 1));
        assertEquals(QuadTreeNode.QuadName.BOTTOM_LEFT, ((QuadTreeNodeImpl) q).findQuadrant(1, 0));
        assertEquals(QuadTreeNode.QuadName.BOTTOM_RIGHT, ((QuadTreeNodeImpl) q).findQuadrant(1, 1));
    }
    
    @Test
    public void testGetColor2By2() {
        int[][] arr = new int[][]{
            {0, 1},
            {1, 0}, 
        };
        
        QuadTreeNode q = new QuadTreeNodeImpl(arr.length);
        ((QuadTreeNodeImpl) q).setQuad(new QuadTreeNodeImpl(0, 1), QuadTreeNode.QuadName.TOP_LEFT);
        ((QuadTreeNodeImpl) q).setQuad(new QuadTreeNodeImpl(1, 1), QuadTreeNode.QuadName.TOP_RIGHT);
        ((QuadTreeNodeImpl) q).setQuad(new QuadTreeNodeImpl(1, 1), QuadTreeNode.QuadName.BOTTOM_LEFT);
        ((QuadTreeNodeImpl) q).setQuad(new QuadTreeNodeImpl(0, 1), QuadTreeNode.QuadName.BOTTOM_RIGHT);
        
        assertFalse(q.isLeaf());
        assertEquals(1, q.getColor(1, 0));
    }
}

