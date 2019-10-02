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
            QuadTreeNodeImpl root = new QuadTreeNodeImpl(image.length);
            root.setQuad(getQuadFromArray(image, 0, 0, image.length / 2), QuadName.TOP_LEFT);
            root.setQuad(getQuadFromArray(image, 0, image.length / 2, image.length / 2), QuadName.TOP_RIGHT);
            root.setQuad(getQuadFromArray(image, image.length / 2, 0, image.length / 2), QuadName.BOTTOM_LEFT);
            root.setQuad(getQuadFromArray(image, image.length / 2, image.length / 2, image.length / 2), QuadName.BOTTOM_RIGHT);
            return root.mergeLeaf();
        }
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
     * @return QuadTreeNodeImpl representing a quadrant of image
     */
    static QuadTreeNodeImpl getQuadFromArray(int[][] image, int x, int y, int side) {
        if (side == 2) {
            QuadTreeNodeImpl root = new QuadTreeNodeImpl(2);
            root.setQuad(new QuadTreeNodeImpl(image[x][y], 1), QuadName.TOP_LEFT);
            root.setQuad(new QuadTreeNodeImpl(image[x + 1][y], 1), QuadName.BOTTOM_LEFT);
            root.setQuad(new QuadTreeNodeImpl(image[x][y + 1], 1), QuadName.TOP_RIGHT);
            root.setQuad(new QuadTreeNodeImpl(image[x + 1][y + 1], 1), QuadName.BOTTOM_RIGHT);
            System.out.println("merge" + root.getQuadrant(QuadName.TOP_LEFT).getColor(0, 0));
            return root.mergeLeaf();
                    
        } else {
            QuadTreeNodeImpl root = new QuadTreeNodeImpl(side);
            root.setQuad(getQuadFromArray(image, x, y, side / 2), QuadName.TOP_LEFT);
            root.setQuad(getQuadFromArray(image, x + side / 2, y, side / 2), QuadName.BOTTOM_LEFT);
            root.setQuad(getQuadFromArray(image, x, y + side / 2, side / 2), QuadName.TOP_RIGHT);
            root.setQuad(getQuadFromArray(image, x + side / 2, y + side / 2, side / 2), QuadName.BOTTOM_RIGHT);
            return root.mergeLeaf();
        }
    }
    
    int getLeafColor() {
        return color;
    }
    
    /**
     * Checks if the leaves of a given node needs to be merged, that is if all leaves hold same
     * color value
     * 
     * @return Single leaf node to replace original root node + four leaves
     */
    QuadTreeNodeImpl mergeLeaf() {
        //System.out.println("merge run");
        if (getQuadrant(QuadName.TOP_LEFT).isLeaf() && 
                getQuadrant(QuadName.TOP_RIGHT).isLeaf() &&
                getQuadrant(QuadName.BOTTOM_LEFT).isLeaf() && 
                getQuadrant(QuadName.BOTTOM_RIGHT).isLeaf()) {
            //System.out.println("out");
            if (leafSameColor()) {
                //System.out.println("in");
                return new QuadTreeNodeImpl(getQuadrant(QuadName.TOP_LEFT).getColor(0, 0), 
                        getDimension());
                //System.out.println(q.getQuadrant(QuadName.TOP_RIGHT));
            }
        }
        return this;
    }
    
    /**
     * Checks if leaves of current node are all same color
     * 
     * @return true is leaves are same color
     */
    boolean leafSameColor() {
        int rgb = getQuadrant(QuadName.TOP_LEFT).getColor(0, 0);
        return rgb == getQuadrant(QuadName.TOP_RIGHT).getColor(0, 0) &&
                rgb == getQuadrant(QuadName.BOTTOM_LEFT).getColor(0, 0) &&
                rgb == getQuadrant(QuadName.BOTTOM_RIGHT).getColor(0, 0);
    }


    @Override
    public int getColor(int x, int y) {
        if (x < 0 || y < 0 || x > size || y > size) {
            throw new IllegalArgumentException();
        }
        if (isLeaf()) {
            return this.color;
        }
        QuadName location = findQuadrant(x, y);
        QuadTreeNode childNode = getQuadrant(location);
        int newX = convertX(x, location);
        int newY = convertY(y, location);
        return childNode.getColor(newX, newY);
    }
    
    /**
     * Finds the quadrant the given coordinate is in
     * 
     * @param row row value of coordinate
     * @param col col value of coordinate
     * @return QuadName of quadrant
     */
    QuadName findQuadrant(int row, int col) {
        if (row < 0 || col < 0 || row > size || col > size) {
            throw new IllegalArgumentException();
        }
        if (row < size / 2 && col < size / 2) {
            return QuadName.TOP_LEFT;
        } else if (row < size / 2 && col >= size / 2) {
            return QuadName.TOP_RIGHT;
        } else if (row >= size / 2 && col < size / 2) {
            return QuadName.BOTTOM_LEFT;
        } else if (row >= size / 2 && col >= size / 2) {
            return QuadName.BOTTOM_RIGHT;
        } else {
            throw new IllegalArgumentException();
        }
    }
    /**
     * Converts y coordinate to a corresponding y coordinate relative to the given quadrant
     * 
     * @param y original y (col) value to be converted
     * @param quadrant new quadrant
     * @return converted y coordinate
     */
    int convertY(int y, QuadName quadrant) {
        switch (quadrant) {
            case TOP_LEFT: return y;
            case TOP_RIGHT: return y - size / 2;
            case BOTTOM_LEFT: return y;
            case BOTTOM_RIGHT: return y - size / 2;
        }
        throw new IllegalArgumentException();
    }
    
    /**
     * Converts x coordinate to a corresponding x relative to given quadrant
     * 
     * @param x original x (row) value
     * @param quadrant new quadrant
     * @return converted x coordinate
     */
    int convertX(int x, QuadName quadrant) {
        switch (quadrant) {
            case TOP_LEFT: return x;
            case TOP_RIGHT: return x;
            case BOTTOM_LEFT: return x - size / 2;
            case BOTTOM_RIGHT: return x - size / 2;
        }
        throw new IllegalArgumentException();
    }

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
    
    /**
     * equals method for a QuadTreeNode
     */
    @Override
    public boolean equals(Object other) {
        if (other == null || this == null) {
            return this == other;
        } else if (this == other) {
            return true;
        } else if (!(other instanceof QuadTreeNode)) {
            return false;
        }
        QuadTreeNode q = (QuadTreeNode) other;
        if (q.getSize() != this.getSize() || q.getDimension() != this.getDimension()) {
            return false;
        }
        
        if (this.isLeaf() && q.isLeaf()) {
            return this.getColor(0, 0) == q.getColor(0, 0);
        }
        
        if (upperLeft != null) {
            if (q.getQuadrant(QuadName.TOP_LEFT) != null) {
                return upperLeft.equals(q.getQuadrant(QuadName.TOP_LEFT));
            } else {
                return false;
            }
        }
        
        if (upperRight != null) {
            if (q.getQuadrant(QuadName.TOP_RIGHT) != null) {
                return upperRight.equals(q.getQuadrant(QuadName.TOP_RIGHT));
            } else {
                return false;
            }
        }
        
        if (lowerLeft != null) {
            if (q.getQuadrant(QuadName.BOTTOM_LEFT) != null) {
                return lowerLeft.equals(q.getQuadrant(QuadName.BOTTOM_LEFT));
            } else {
                return false;
            }
        }
        
        if (lowerRight != null) {
            if (q.getQuadrant(QuadName.BOTTOM_RIGHT) != null) {
                return lowerRight.equals(q.getQuadrant(QuadName.BOTTOM_RIGHT));
            } else {
                return false;
            }
        }
        
        return true;
        
    }
}
