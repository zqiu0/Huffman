package quadTree;
// CIS 121, HW4 QuadTree

public class QuadTreeNodeImpl implements QuadTreeNode {
    private int color;
    private int size; //side length of image
    private boolean hasColor; //whether the node has a color value
    private QuadTreeNode upperLeft;
    private QuadTreeNode upperRight;
    private QuadTreeNode lowerLeft;
    private QuadTreeNode lowerRight;
    
    /** leaf */
    QuadTreeNodeImpl(int color, int size) {
        this.color = color;
        this.size = size;
        hasColor = true;
        /*upperLeft = null;
        upperRight = null;
        lowerLeft = null;
        lowerRight = null;*/
    }
    
    /** non-leaf */
    QuadTreeNodeImpl(int size) {
        this.size = size;
        hasColor = false;
    }
    
    /**
     * ! Do not delete this method !
     * Please implement your logic inside this method without modifying the signature
     * of this method, or else your code won't compile.
     * <p/>
     * Be careful that if you were to create another method, make sure it is not public.
     *
     * @param image image to put into the tree
     * @return the newly build QuadTreeNode instance which stores the compressed image
     * @throws IllegalArgumentException if image is null
     * @throws IllegalArgumentException if image is empty
     * @throws IllegalArgumentException if image.length is not a power of 2
     * @throws IllegalArgumentException if image, the 2d-array, is not a perfect square
     */
    public static QuadTreeNode buildFromIntArray(int[][] image) {
        if (image == null || image.length == 0 || image[0].length == 0 
                || !isPowerOfTwo(image.length) || !isSquare(image)) {
            throw new IllegalArgumentException();
        }
        
        if (image.length == 1) {
            return new QuadTreeNodeImpl(image[0][0], 1);
        } else {
            QuadTreeNode root = new QuadTreeNodeImpl(image.length);
            
        }
        return null;
        
    }
    
    /** checks if an int is a power of 2 */
    static boolean isPowerOfTwo(int n) {
        if (n == 0) {
            return false;
        }
        
        while (n % 2 == 0) {
            n = n / 2;
        }
        return n == 1;
    }
    
    /** checks if a 2-D array is perfect square */
    static boolean isSquare(int[][] arr) {
        int row = arr.length;
        for (int i = 0; i < row; i++) {
            if (arr[i].length != row) {
                return false;
            }
        }
        return true;
    }
    
    /** Setter method for four quadrants of a root
     * 
     *  @param root the root node
     *  @param q the node to be inserted to a quadrant
     *  @param quadrant the quadrant to be set 
     *  @throws IllegalArgumentException if node or quadrant is null
     */
    void setQuad(QuadTreeNode q, QuadName quadrant) {
        if (q == null || quadrant == null) {
            throw new IllegalArgumentException();
        }
        switch (quadrant) {
            case TOP_LEFT: upperLeft = q; break;
            case TOP_RIGHT: upperRight = q; break;
            case BOTTOM_LEFT: lowerLeft = q; break;
            case BOTTOM_RIGHT: lowerRight = q; break;
        }
    }
    
    /** Recursively divides the image array into quadrants based on the color values, helper
     *  function for buildFromIntArray 
     * 
     * @param image image of size at least 2 to put into tree
     * @param x row coordinate of upper left coordinate of quadrant
     * @param y col coordinate of upper left coordinate of quadrant
     * @param side side size of quadrant
     * @return QuadTreeNode representing a quadrant of image
     */
    QuadTreeNode getQuadFromArray(int[][] image, int x, int y, int side) {
        if (side == 2) {
            QuadTreeNode root = new QuadTreeNodeImpl(2);
            for (int row = x; row < x + side; row++) {
                for (int col = y; col < y + side; col++) {
                    if (image[row][col] != image[x][y]) {
                        setQuad(new QuadTreeNodeImpl(image[x][y], 1), QuadName.TOP_LEFT);
                        setQuad(new QuadTreeNodeImpl(image[x + 1][y], 1), QuadName.BOTTOM_LEFT);
                        setQuad(new QuadTreeNodeImpl(image[x][y + 1], 1), QuadName.TOP_RIGHT);
                        setQuad(new QuadTreeNodeImpl(image[x + 1][y + 1], 1), QuadName.BOTTOM_RIGHT);
                        return root;
                    }
                }
            }
            return new QuadTreeNodeImpl(image[x][y], 2);
        } else {
            QuadTreeNode root = new QuadTreeNodeImpl(side);
            setQuad(getQuadFromArray(image, x, y, side / 2), QuadName.TOP_LEFT);
            setQuad(getQuadFromArray(image, x + side / 2, y, side / 2), QuadName.BOTTOM_LEFT);
            setQuad(getQuadFromArray(image, x, y + side / 2, side / 2), QuadName.TOP_RIGHT);
            setQuad(getQuadFromArray(image, x + side / 2, y + side / 2, side / 2), QuadName.BOTTOM_RIGHT);
            return root;
        }
    }
    
    int getLeafColor() {
        return color;
    }
    
    /**
     * Checks if the leaves of a given node needs to be merged, that is if all leaves hold same
     * color value
     * @param root node to check if leaves need to be merged
     * @return Single leaf node to replace original root node + four leaves
     */
    QuadTreeNode mergeLeaf(QuadTreeNode root) {
        if (root.getQuadrant(QuadName.TOP_LEFT).isLeaf() && 
                root.getQuadrant(QuadName.TOP_RIGHT).isLeaf() &&
                root.getQuadrant(QuadName.BOTTOM_LEFT).isLeaf() && 
                root.getQuadrant(QuadName.BOTTOM_RIGHT).isLeaf()) {
            int rgb = root.getQuadrant(QuadName.TOP_LEFT).getColor();
        }
    }


    @Override
    public int getColor(int x, int y) {
        throw new UnsupportedOperationException("TODO: implement");
    }
    
    //QuadName findQuadrant(int x, int y, )

    @Override
    public void setColor(int x, int y, int c) {
        throw new UnsupportedOperationException("TODO: implement");
    }

    @Override
    public QuadTreeNode getQuadrant(QuadName quadrant) {
        switch (quadrant) {
            case TOP_LEFT: return upperLeft;
            case TOP_RIGHT: return upperRight;
            case BOTTOM_LEFT: return lowerLeft;
            case BOTTOM_RIGHT: return lowerRight;
        }
        throw new IllegalArgumentException();
    }

    @Override
    public int getDimension() {
        return size;
    }

    @Override
    public int getSize() {
        int size = 1;
        if (upperLeft != null) {
            size += upperLeft.getSize();
        } else if (upperRight != null) {
            size += upperRight.getSize();
        } else if (lowerLeft != null) {
            size += lowerLeft.getSize();
        } else if (lowerRight != null) {
            size += lowerRight.getSize();
        }
        return size;
    }

    @Override
    public boolean isLeaf() {
        return hasColor;
    }

    @Override
    public int[][] decompress() {
        throw new UnsupportedOperationException("TODO: implement");
    }

    @Override
    public double getCompressionRatio() {
        throw new UnsupportedOperationException("TODO: implement");
    }
}
