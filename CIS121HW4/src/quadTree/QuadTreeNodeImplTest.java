package quadTree;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class QuadTreeNodeImplTest {
    private int[][] fourByFour;
    private QuadTreeNode lengthFour;
    
    @Before
    public void setFourByFour() {
        fourByFour = new int[][]{
            {0, 1, 0, 0},
            {1, 0, 0, 0},
            {0, 0, 0 ,0},
            {1, 0, 0 ,0},
        };
        
        QuadTreeNode uL = new QuadTreeNodeImpl(2);
        ((QuadTreeNodeImpl) uL).setQuad(new QuadTreeNodeImpl(0, 1), QuadTreeNode.QuadName.TOP_LEFT);
        ((QuadTreeNodeImpl) uL).setQuad(new QuadTreeNodeImpl(1, 1), QuadTreeNode.QuadName.TOP_RIGHT);
        ((QuadTreeNodeImpl) uL).setQuad(new QuadTreeNodeImpl(1, 1), QuadTreeNode.QuadName.BOTTOM_LEFT);
        ((QuadTreeNodeImpl) uL).setQuad(new QuadTreeNodeImpl(0, 1), QuadTreeNode.QuadName.BOTTOM_RIGHT);
        
        QuadTreeNode bL = new QuadTreeNodeImpl(2);
        ((QuadTreeNodeImpl) bL).setQuad(new QuadTreeNodeImpl(0, 1), QuadTreeNode.QuadName.TOP_LEFT);
        ((QuadTreeNodeImpl) bL).setQuad(new QuadTreeNodeImpl(0, 1), QuadTreeNode.QuadName.TOP_RIGHT);
        ((QuadTreeNodeImpl) bL).setQuad(new QuadTreeNodeImpl(1, 1), QuadTreeNode.QuadName.BOTTOM_LEFT);
        ((QuadTreeNodeImpl) bL).setQuad(new QuadTreeNodeImpl(0, 1), QuadTreeNode.QuadName.BOTTOM_RIGHT);
        
        QuadTreeNode uR = new QuadTreeNodeImpl(0, 2);
        
        QuadTreeNode bR = new QuadTreeNodeImpl(0, 2);
        
        lengthFour = new QuadTreeNodeImpl(4);
        ((QuadTreeNodeImpl) lengthFour).setQuad(uL, QuadTreeNode.QuadName.TOP_LEFT);
        ((QuadTreeNodeImpl) lengthFour).setQuad(uR, QuadTreeNode.QuadName.TOP_RIGHT);
        ((QuadTreeNodeImpl) lengthFour).setQuad(bL, QuadTreeNode.QuadName.BOTTOM_LEFT);
        ((QuadTreeNodeImpl) lengthFour).setQuad(bR, QuadTreeNode.QuadName.BOTTOM_RIGHT);
        
    }
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
        QuadTreeNode q = QuadTreeNodeImpl.getQuadFromArray(arr, 0, 0, 2);
        QuadTreeNode quad = q.getQuadrant(QuadTreeNode.QuadName.TOP_LEFT);
        assertEquals(1, quad.getDimension());
        assertEquals(2, ((QuadTreeNodeImpl) quad).getLeafColor());
    }
    
    /** getQuadFromArray test */
    @Test
    public void testGetQuadFromArrayAllSameColor() {
        int[][] arr = new int[][]{
            {0, 0},
            {0, 0},
        };
        QuadTreeNode q = QuadTreeNodeImpl.getQuadFromArray(arr, 0, 0, arr.length);
        assertNull(q.getQuadrant(QuadTreeNode.QuadName.TOP_LEFT));
        assertNull(q.getQuadrant(QuadTreeNode.QuadName.TOP_RIGHT));
        assertNull(q.getQuadrant(QuadTreeNode.QuadName.BOTTOM_LEFT));
        assertNull(q.getQuadrant(QuadTreeNode.QuadName.BOTTOM_RIGHT));
        assertEquals(1, q.getSize());
        assertTrue(q.isLeaf());
    }
    
    /** setQuad test */
    @Test
    public void testSetQuad() {
        QuadTreeNode root = new QuadTreeNodeImpl(2);
        ((QuadTreeNodeImpl) root).setQuad(new QuadTreeNodeImpl(1, 1), QuadTreeNode.QuadName.BOTTOM_LEFT);
        assertEquals(1, ((QuadTreeNodeImpl) root.getQuadrant(QuadTreeNode.QuadName.BOTTOM_LEFT)).getLeafColor());
    }
    
    /** findQuadrant test */
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
    
    /** getColor test */
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
    
    @Test
    public void testGetColor4By4() {
        assertEquals(1, lengthFour.getColor(1, 0));
    }
    
    @Test
    public void testGetColor4By4UR() {
        assertEquals(0, lengthFour.getColor(1, 2));
    }
    
    /** convertX and convertY test */
    @Test
    public void testConvertX() {
        QuadTreeNode q = new QuadTreeNodeImpl(fourByFour.length);
        assertEquals(1, ((QuadTreeNodeImpl) q).convertX(3, QuadTreeNode.QuadName.BOTTOM_LEFT));
    }
    
    @Test
    public void testConvertY() {
        QuadTreeNode q = new QuadTreeNodeImpl(fourByFour.length);
        assertEquals(0, ((QuadTreeNodeImpl) q).convertY(2, QuadTreeNode.QuadName.TOP_RIGHT));
    }
    
    /** getSize test */
    @Test
    public void testGetSize2By2() {
        int[][] arr = new int[][]{
            {0, 1},
            {1, 0}, 
        };
        QuadTreeNode tree = QuadTreeNodeImpl.buildFromIntArray(arr);
        assertEquals(5, tree.getSize());
    }
    
    /** exception testing buildFromIntArray */
    @Test(expected = IllegalArgumentException.class)
    public void testBuildFromNullArray() {
        int [][] arr = null;
        QuadTreeNodeImpl.buildFromIntArray(arr);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBuildFromEmptyArray() {
        int[][] arr = new int[0][0];
        QuadTreeNodeImpl.buildFromIntArray(arr);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBuildFromNonSquare() {
        int[][] arr = new int[][]{
            {2, 1, 2},
            {1, 0, 1},
            {2, 1, 2},
            {1, 0, 1},
            {2, 1, 2}
        };
        QuadTreeNodeImpl.buildFromIntArray(arr);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBuildFromNonSquare2() {
        int[][] arr = new int[][]{
            {2, 1, 2, 1},
            {1, 0, 1, 1},
            {2, 1, 2, 3},
        };
        QuadTreeNodeImpl.buildFromIntArray(arr);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBuildFromJagged() {
        int[][] arr = new int[][]{
            {2, 1, 2, 3},
            {1, 0, 1},
            {2, 1},
            {1, 0, 1},
            {2, 1, 2}
        };
        QuadTreeNodeImpl.buildFromIntArray(arr);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBuildFromNonPowerOfTwoAndSquare() {
        int[][] arr = new int[5][5];
        QuadTreeNodeImpl.buildFromIntArray(arr);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBuildFromNonPowerOfTwo() {
        int[][] arr = new int[6][5];
        QuadTreeNodeImpl.buildFromIntArray(arr);
    }
    
    /** buildFromIntArray test */
    @Test
    public void testFourByFourAllSame() {
        int[][] image = new int[4][4];
        QuadTreeNode tree = QuadTreeNodeImpl.buildFromIntArray(image);
        QuadTreeNode expected = new QuadTreeNodeImpl(0, 4);
        assertTrue(tree.equals(expected));
    }
    
    @Test
    public void testFourByFourSomeDifferent() {
        int[][] image = new int[][]{
            {0, 1, 0, 0},
            {1, 0, 0, 0},
            {0, 0, 0 ,0},
            {1, 0, 0 ,0},
        };
        
        QuadTreeNode tree = QuadTreeNodeImpl.buildFromIntArray(image);
        
        assertTrue(tree.equals(lengthFour));
    }
    
    @Test
    public void testTwoByTwoAllSame() {
        int[][] image = new int[2][2];
        QuadTreeNode tree = QuadTreeNodeImpl.buildFromIntArray(image);
        QuadTreeNode expected = new QuadTreeNodeImpl(0, 2);
        assertTrue(tree.equals(expected));
    }
    
    @Test
    public void testTwoByTwoDifferent() {
        int[][] image = new int[][]{
            {0, 1},
            {1, 0}
        };
        QuadTreeNode tree = QuadTreeNodeImpl.buildFromIntArray(image);
        System.out.println(tree.getSize());
        
        QuadTreeNode expected = new QuadTreeNodeImpl(image.length);
        ((QuadTreeNodeImpl) expected).setQuad(new QuadTreeNodeImpl(0, 1), QuadTreeNode.QuadName.TOP_LEFT);
        ((QuadTreeNodeImpl) expected).setQuad(new QuadTreeNodeImpl(1, 1), QuadTreeNode.QuadName.TOP_RIGHT);
        ((QuadTreeNodeImpl) expected).setQuad(new QuadTreeNodeImpl(1, 1), QuadTreeNode.QuadName.BOTTOM_LEFT);
        ((QuadTreeNodeImpl) expected).setQuad(new QuadTreeNodeImpl(0, 1), QuadTreeNode.QuadName.BOTTOM_RIGHT);
        System.out.println(expected.getSize());
        assertTrue(tree.equals(expected));
    }
    
    /** equals test */
    @Test
    public void testTwoLeavesEqual() {
        QuadTreeNode q1 = new QuadTreeNodeImpl(0, 1);
        QuadTreeNode q2 = new QuadTreeNodeImpl(0, 1);
        assertTrue(q1.equals(q2));
    }
    
    @Test
    public void testNullNotEqual() {
        QuadTreeNode q1 = new QuadTreeNodeImpl(0, 1);
        QuadTreeNode q2 = null;
        assertFalse(q1.equals(q2));
    }
    
    /** getCompressionRatio test */
    @Test
    public void test2By2() {
        int[][] image = new int[][]{
            {0, 1},
            {1, 0}
        };
        QuadTreeNode tree = QuadTreeNodeImpl.buildFromIntArray(image);
        assertTrue((double) 5 / 4 == tree.getCompressionRatio());
    }
    
    @Test
    public void test4By4() {
        assertTrue((double) 13 / 16 == lengthFour.getCompressionRatio());
    }
    
    /** decompress test */
    @Test
    public void testDecompress2By2() {
        int[][] image = new int[][]{
            {2, 1},
            {1, 2}
        };
        QuadTreeNode tree = QuadTreeNodeImpl.buildFromIntArray(image);
        int [][] a = tree.decompress();
        /*for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                System.out.println(a[i][j]);
            }
        }*/
        assertArrayEquals(image, tree.decompress());
    }
    
    @Test
    public void testDecompress1By1() {
        int[][] image = new int[][]{
            {2}
        };
        QuadTreeNode tree = QuadTreeNodeImpl.buildFromIntArray(image);
        assertArrayEquals(image, tree.decompress());
    }
    
    @Test
    public void testDecompressLeaf2By2() {
        int[][] image = new int[][]{
            {2, 2},
            {2, 2}
        };
        QuadTreeNode tree = QuadTreeNodeImpl.buildFromIntArray(image);
        assertArrayEquals(image, tree.decompress());
    }
    
    @Test
    public void testDecompress4By4() {
        assertArrayEquals(fourByFour, lengthFour.decompress());
    }
    
    @Test
    public void testDecompress4By4Complicated() {
        int[][] image = new int[][]{
            {2, 1, 3, 4},
            {2, 3, 1, 4},
            {1, 2, 3, 4},
            {4, 3, 2, 1}
        };
        QuadTreeNode tree = QuadTreeNodeImpl.buildFromIntArray(image);
        assertArrayEquals(image, tree.decompress());
    }
}

