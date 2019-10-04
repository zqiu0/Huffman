// CIS 121, HW4 QuadTree

public class QuadTreeNodeImpl implements QuadTreeNode {
    private int color;
    private int dimension; //side length of image
    private boolean hasColor; //whether the node has a color value
    private QuadTreeNodeImpl upperLeft;
    private QuadTreeNodeImpl upperRight;
    private QuadTreeNodeImpl lowerLeft;
    private QuadTreeNodeImpl lowerRight;
    int[][] decompressed; //stores decompressed image
    
    /** leaf */
    QuadTreeNodeImpl(int color, int dimension) {
        this.color = color;
        this.dimension = dimension;
        hasColor = true;
    }
    
    /** non-leaf */
    QuadTreeNodeImpl(int dimension) {
        this.dimension = dimension;
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
        } else if (image.length == 2) {
            return getQuadFromArray(image, 0, 0, image.length);
        }
        else {
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
    
    /** Setter method for four quadrants of a root, if called on a leaf, change boolean hasColor to
     *  false before setting quadrants
     * 
     *  @param root the root node
     *  @param q the node to be inserted to a quadrant
     *  @param quadrant the quadrant to be set 
     *  @throws IllegalArgumentException if node or quadrant is null
     */
    void setQuad(QuadTreeNodeImpl q, QuadName quadrant) {
        if (q == null || quadrant == null) {
            throw new IllegalArgumentException();
        }
        //System.out.println("before");
        //System.out.println(upperLeft == null);
        if (isLeaf()) {
            leafToNode(this);
        }
        switch (quadrant) {
            case TOP_LEFT: 
                upperLeft = q;
                break;
            case TOP_RIGHT: 
                upperRight = q; 
                break;
            case BOTTOM_LEFT: 
                lowerLeft = q; 
                break;
            case BOTTOM_RIGHT: 
                lowerRight = q; 
                break;
        }
        //System.out.println(upperLeft == null);
        
    }
    
    /**
     * Setter method for boolean hasColor, changes a node from leaf to non-leaf
     * 
     * @param q node to be changed
     */
    void leafToNode(QuadTreeNodeImpl q) {
        q.hasColor = false;
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
            //System.out.println("merge" + root.getQuadrant(QuadName.TOP_LEFT).getColor(0, 0));
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
        if (x < 0 || y < 0 || x >= dimension || y >= dimension) {
            throw new IllegalArgumentException();
        }
        if (isLeaf()) {
            return this.color;
        }
        QuadName location = findQuadrant(y, x);
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
     * @return QuadName of quadrant, or null if quadrant doesn't exist
     * @throws IllegalArgumentException if input is invalid or called on a leaf node
     */
    QuadName findQuadrant(int row, int col) {
        //System.out.println("dim = " + dimension);
        if (row < 0 || col < 0 || row >= dimension || col >= dimension) {
            throw new IllegalArgumentException();
        }
        if (row < dimension / 2 && col < dimension / 2) {
            return QuadName.TOP_LEFT;
        } else if (row < dimension / 2 && col >= dimension / 2) {
            return QuadName.TOP_RIGHT;
        } else if (row >= dimension / 2 && col < dimension / 2) {
            return QuadName.BOTTOM_LEFT;
        } else if (row >= dimension / 2 && col >= dimension / 2) {
            return QuadName.BOTTOM_RIGHT;
        } else {
            return null;
        }
    }
    /**
     * Converts x coordinate to a corresponding x coordinate relative to the top left position of 
     * given quadrant
     * 
     * @param x original x (row) value to be converted
     * @param quadrant new quadrant
     * @return converted x coordinate
     */
    int convertX(int x, QuadName quadrant) {
        switch (quadrant) {
            case TOP_LEFT: return x;
            case TOP_RIGHT: return x - dimension / 2;
            case BOTTOM_LEFT: return x;
            case BOTTOM_RIGHT: return x - dimension / 2;
        }
        throw new IllegalArgumentException();
    }
    
    /**
     * Converts y coordinate to a corresponding y relative to top left position of
     *  given quadrant
     * 
     * @param y original y (col) value
     * @param quadrant new quadrant
     * @return converted y coordinate
     */
    int convertY(int y, QuadName quadrant) {
        switch (quadrant) {
            case TOP_LEFT: return y;
            case TOP_RIGHT: return y;
            case BOTTOM_LEFT: return y - dimension / 2;
            case BOTTOM_RIGHT: return y - dimension / 2;
        }
        throw new IllegalArgumentException();
    }
    //assume always called on root node of tree
    @Override
    public void setColor(int x, int y, int c) {
        if (x < 0 || y < 0 || x >= dimension || y >= dimension) {
            throw new IllegalArgumentException();
        }
        
        if (isLeaf()) {
            setColorHelper(x, y, c, getDimension(), this);
        } else {
            QuadName location = findQuadrant(y, x);
            QuadTreeNodeImpl childNode = getQuad(location);
            System.out.println(childNode.getDimension());
            System.out.println("out " + quadNameToString(location));
            int newX = convertX(x, location);
            int newY = convertY(y, location);
            QuadTreeNodeImpl newQuadrant = setColorHelper(newX, newY, c, getDimension() / 2, childNode);
            this.setQuad(newQuadrant, location);
            mergeAtRoot(this);
            System.out.println(this.isLeaf());
            //System.out.println("color" + this.getQuadrant(QuadName.TOP_LEFT).getColor(0, 0));
        }
    }
    
    QuadTreeNodeImpl setColorHelper(int x, int y, int c, int side, QuadTreeNodeImpl root) {
        int oldColor = 0;
        if (side == 1) {
            System.out.println("side 1");
            return new QuadTreeNodeImpl(c, 1);
        } else if (side == 2) {
            System.out.println("side 2");
            QuadName location = root.findQuadrant(y, x);
            System.out.println(quadNameToString(location));
            if (root.isLeaf()) {
                System.out.println("2 is leaf");
                oldColor = root.getColor(0, 0);
                root.setLeaves(location, oldColor, 1, root);
                root.setQuad(new QuadTreeNodeImpl(c, 1), location);
            } else {
                root.setQuad(new QuadTreeNodeImpl(c, 1), location);
                root = root.mergeLeaf();
            }
        }
        
        else {
            System.out.println(root.isLeaf());
            System.out.println("side = "  +side);
            System.out.println(x + "," + y + "!");
            QuadName location = root.findQuadrant(y, x);
            System.out.println(quadNameToString(location));
            QuadTreeNodeImpl childNode = root.getQuad(location);
            System.out.println(root.getDimension());
            int newX = root.convertX(x, location);
            int newY = root.convertY(y, location);
            System.out.println(x + "," + y);
            System.out.println(newX + "," + newY);
            QuadTreeNodeImpl newChild = null;
            if (root.isLeaf()) {
                System.out.println("is leaf");
                oldColor = root.getColor(0, 0);
                QuadTreeNodeImpl quadrantNewColor = new QuadTreeNodeImpl(oldColor,
                        root.getDimension() / 2); //create quadrant for new color
                System.out.println("side div" + side / 2);
                newChild = setColorHelper(newX, newY, c, side / 2, quadrantNewColor);
            } else {
                newChild = setColorHelper(newX, newY, c, side / 2, childNode);
            }
            //System.out.println(newChild.getColor(0, 0));
            //System.out.println(location == QuadName.TOP_RIGHT);
            //oldColor = root.getColor(0, 0); //not always true!
            root.setQuad(newChild, location);
            root.setLeaves(location, oldColor, side / 2, root); //set other 3 leaves
            
            root = root.mergeLeaf();
            //System.out.println(root.getQuad(QuadName.BOTTOM_LEFT) == null);
        }
        return root;
    }

    void mergeAtRoot(QuadTreeNodeImpl root) {
        if (root.getQuadrant(QuadName.TOP_LEFT).isLeaf() && 
                root.getQuadrant(QuadName.TOP_RIGHT).isLeaf() &&
                root.getQuadrant(QuadName.BOTTOM_LEFT).isLeaf() && 
                root.getQuadrant(QuadName.BOTTOM_RIGHT).isLeaf()) {
            //System.out.println("out");
            if (root.leafSameColor()) {
               // System.out.println("in");
                root = new QuadTreeNodeImpl(getQuadrant(QuadName.TOP_LEFT).getColor(0, 0), 
                        getDimension());
                //System.out.println(root.isLeaf());
            }
        }
    }
    @Override
    public QuadTreeNode getQuadrant(QuadName quadrant) {
        if (isLeaf()) {
            System.out.println("leaf");
            return null;
        }
        switch (quadrant) {
            case TOP_LEFT: return upperLeft; 
            case TOP_RIGHT: return upperRight;
            case BOTTOM_LEFT: return lowerLeft;
            case BOTTOM_RIGHT: return lowerRight;
        }
        throw new IllegalArgumentException();
    }
    
    QuadTreeNodeImpl getQuad(QuadName quadrant) {
        if (isLeaf()) {
            return null;
        }
        switch (quadrant) {
            case TOP_LEFT: return upperLeft;
            case TOP_RIGHT: return upperRight;
            case BOTTOM_LEFT: return lowerLeft;
            case BOTTOM_RIGHT: return lowerRight;
        }
        throw new IllegalArgumentException();
    }
    
    void setLeaves(QuadName filledQuad, int c, int leaveSize, QuadTreeNodeImpl root) {
        if (filledQuad == QuadName.TOP_LEFT) {
            root.setQuad(new QuadTreeNodeImpl(c, leaveSize), QuadName.TOP_RIGHT);
            root.setQuad(new QuadTreeNodeImpl(c, leaveSize), QuadName.BOTTOM_LEFT);
            root.setQuad(new QuadTreeNodeImpl(c, leaveSize), QuadName.BOTTOM_RIGHT);
        } else if (filledQuad == QuadName.TOP_RIGHT) {
            //System.out.println("set leaves tr");
            //System.out.println(new QuadTreeNodeImpl(c, leaveSize).getColor(0, 0) + "!!");
            //System.out.println("root" + root.getColor(0, 0));
            //System.out.println(root.getQuad(QuadName.TOP_LEFT) == null); //true, root is leaf
            root.setQuad(new QuadTreeNodeImpl(c, leaveSize), QuadName.TOP_LEFT);
            //System.out.println("is null");
            //System.out.println(root.getQuad(QuadName.TOP_LEFT) == null);
            //System.out.println(root.getQuad(QuadName.TOP_LEFT).getColor(0, 0));
            root.setQuad(new QuadTreeNodeImpl(c, leaveSize), QuadName.BOTTOM_LEFT);
            root.setQuad(new QuadTreeNodeImpl(c, leaveSize), QuadName.BOTTOM_RIGHT);
        } else if (filledQuad == QuadName.BOTTOM_LEFT) {
            root.setQuad(new QuadTreeNodeImpl(c, leaveSize), QuadName.TOP_RIGHT);
            root.setQuad(new QuadTreeNodeImpl(c, leaveSize), QuadName.TOP_LEFT);
            root.setQuad(new QuadTreeNodeImpl(c, leaveSize), QuadName.BOTTOM_RIGHT);
        } else if (filledQuad == QuadName.BOTTOM_RIGHT) {
            root.setQuad(new QuadTreeNodeImpl(c, leaveSize), QuadName.TOP_RIGHT);
            root.setQuad(new QuadTreeNodeImpl(c, leaveSize), QuadName.TOP_LEFT);
            root.setQuad(new QuadTreeNodeImpl(c, leaveSize), QuadName.BOTTOM_LEFT);
        }
        //return root;
    }
    

    @Override
    public int getDimension() {
        return dimension;
    }

    @Override
    public int getSize() {
        int num = 1;
        if (upperLeft != null) {
            num += upperLeft.getSize();
        } 
        if (upperRight != null) {
            num += upperRight.getSize();
        } 
        if (lowerLeft != null) {
            num += lowerLeft.getSize();
        }
        if (lowerRight != null) {
            num += lowerRight.getSize();
        }
        return num;
    }

    @Override
    public boolean isLeaf() {
        return hasColor;
    }

    @Override
    public int[][] decompress() {
        decompressed = new int[getDimension()][getDimension()];
        if (isLeaf()) {
            System.out.println("leaf");
            for (int i = 0; i < getDimension(); i++) {
                for (int j = 0; j < getDimension(); j++) {
                    decompressed[i][j] = this.getColor(0, 0);
                }
            }
            return decompressed;
        } else {
            getArrayFromTree(0, 0, getDimension() / 2, upperLeft);
            getArrayFromTree(0, getDimension() / 2, getDimension() / 2, upperRight);
            getArrayFromTree(getDimension() / 2, 0, getDimension() / 2, lowerLeft);
            getArrayFromTree(getDimension() / 2, getDimension() / 2, getDimension() / 2, lowerRight);
            return decompressed; 
            
        }
    }
    
    /*int getUpperLeftX(int oldX, QuadName quadrant) {
        switch (quadrant) {
            case TOP_LEFT: return oldX;
            case TOP_RIGHT: return oldX + dimension / 2;
            case BOTTOM_LEFT: return oldX;
            case BOTTOM_RIGHT: return oldX + dimension / 2;
        }
        throw new IllegalArgumentException();
    }
    
    int getUpperLeftY(int oldY, QuadName quadrant) {
        switch (quadrant) {
            case TOP_LEFT: return oldY;
            case TOP_RIGHT: return oldY;
            case BOTTOM_LEFT: return oldY + dimension / 2;
            case BOTTOM_RIGHT: return oldY + dimension / 2;
        }
        throw new IllegalArgumentException();
    }*/
    
    /**
     * Recursive helper for decompress(), fills array by quadrants
     * 
     * @param x upper left x coordinate of current quadrant
     * @param y upper left y coordinate of current quadrant
     * @param side side length (dimension) of current quadrant
     * @param root root of current quadrant
     */
    void getArrayFromTree(int x, int y, int side, QuadTreeNode root) {
        if (root.isLeaf()) {
            //System.out.println("help leaf");
            for (int i = x; i < x + side; i++) {
                for (int j = y; j < y + side; j++) {
                    decompressed[i][j] = root.getColor(0, 0);
                    //System.out.println(i + ","+ j + " "+ root.getColor(0, 0));
                }
            }
        } else {
            getArrayFromTree(x, y, side / 2, root.getQuadrant(QuadName.TOP_LEFT));
            getArrayFromTree(x, y + side / 2, side / 2, root.getQuadrant(QuadName.TOP_RIGHT));
            getArrayFromTree(x + side / 2, y, side / 2, root.getQuadrant(QuadName.BOTTOM_LEFT));
            getArrayFromTree(x + side / 2, y + side / 2, side / 2, root.getQuadrant(QuadName.BOTTOM_RIGHT));
        }
    }

    @Override
    public double getCompressionRatio() {
        return (double) getSize() / (double) (getDimension() * getDimension());
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
        
        boolean isEqual = true;
        
        if (upperLeft != null && q.getQuadrant(QuadName.TOP_LEFT) != null) {
            //System.out.println("1");
            isEqual = isEqual && upperLeft.equals(q.getQuadrant(QuadName.TOP_LEFT));
        } else {
            System.out.println("1 false");
            isEqual = false;
        }
        
        if (upperRight != null && q.getQuadrant(QuadName.TOP_RIGHT) != null) {
            //System.out.println("2");
            isEqual = isEqual && upperRight.equals(q.getQuadrant(QuadName.TOP_RIGHT));
        } else {
            System.out.println("2 false");
            isEqual = false;
        }
        
        if (lowerLeft != null && q.getQuadrant(QuadName.BOTTOM_LEFT) != null) {
            //System.out.println("3");
            isEqual = isEqual && lowerLeft.equals(q.getQuadrant(QuadName.BOTTOM_LEFT));
        } else {
            System.out.println("3 false");
            isEqual = false;
        }
        
        if (lowerRight != null && q.getQuadrant(QuadName.BOTTOM_RIGHT) != null) {
            //System.out.println("4");
            isEqual = isEqual && lowerRight.equals(q.getQuadrant(QuadName.BOTTOM_RIGHT));
        } else {
            System.out.println("4 false");
            isEqual = false;
        }
        
        return isEqual;
    }
    
    String quadNameToString(QuadName quadrant) {
        switch (quadrant) {
            case TOP_LEFT:
                return "top left";
            case TOP_RIGHT:
                return "top right";
            case BOTTOM_LEFT:
                return "bottom left";
            case BOTTOM_RIGHT:
                return "bottom right";
        }
        throw new IllegalArgumentException();
    }
}
