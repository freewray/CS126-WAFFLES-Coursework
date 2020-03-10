package uk.ac.warwick.cs126.structures;

public class AVLTreeNode<E> {

    private E key;
    private int height;
    private AVLTreeNode<E> left;
    private AVLTreeNode<E> right;

    public AVLTreeNode(E key, AVLTreeNode<E> left, AVLTreeNode<E> right) {
        this.key = key;
        this.left = left;
        this.right = right;
        this.height = 0;
    }

    
    /** 
     * @return E key of the node
     */
    public E getKey() {
        return key;
    }

    
    /** 
     * @param key to set on to the node
     */
    public void setKey(E key) {
        this.key = key;
    }

    
    /** 
     * @return height of the node
     */
    public int getHeight() {
        return height;
    }

    
    /** 
     * @param height of the node to set to
     */
    public void setHeight(int height) {
        this.height = height;
    }

    
    /** 
     * @return left AVLTreeNode<E> of the node
     */
    public AVLTreeNode<E> getLeft() {
        return left;
    }

    
    /** 
     * @param left AVLTreeNode<E> to set to
     */
    public void setLeft(AVLTreeNode<E> left) {
        this.left = left;
    }

    
    /** 
     * @return right AVLTreeNode<E> of the node
     */
    public AVLTreeNode<E> getRight() {
        return right;
    }

    
    /** 
     * @param right AVLTreeNode<E> to set to
     */
    public void setRight(AVLTreeNode<E> right) {
        this.right = right;
    }
}