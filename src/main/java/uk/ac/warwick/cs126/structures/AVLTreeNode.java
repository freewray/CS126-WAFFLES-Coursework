package uk.ac.warwick.cs126.structures;

// AVL树的节点(内部类)
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

    public E getKey() {
        return key;
    }

    public void setKey(E key) {
        this.key = key;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public AVLTreeNode<E> getLeft() {
        return left;
    }

    public void setLeft(AVLTreeNode<E> left) {
        this.left = left;
    }

    public AVLTreeNode<E> getRight() {
        return right;
    }

    public void setRight(AVLTreeNode<E> right) {
        this.right = right;
    }
}