package uk.ac.warwick.cs126.structures;

public class AVLTree<E> {
    protected AVLTreeNode<E> root; // 根结点

    /**
     * @return height of the tree
     */
    protected int height(AVLTreeNode<E> tree) {
        if (tree != null)
            return tree.getHeight();
        return 0;
    }

    public int height() {
        return height(root);
    }

    /**
     * compare the value of 2 integers
     * @param a
     * @param b
     * @return larger integer
     */
    protected int max(int a, int b) {
        return Math.max(a, b);
    }

    /**
     * Left-Left case
     * @param k2
     * @return root after rotation
     */
    protected AVLTreeNode<E> leftLeftRotation(AVLTreeNode<E> k2) {
        AVLTreeNode<E> k1;

        k1 = k2.getLeft();
        k2.setLeft(k1.getRight());
        k1.setRight(k2);

        k2.setHeight(max(height(k2.getLeft()), height(k2.getRight())) + 1);
        k1.setHeight(max(height(k1.getLeft()), k2.getHeight()) + 1);

        return k1;
    }

    /**
     * right right case
     * @param k1
     * @return root after rotation
     */
    protected AVLTreeNode<E> rightRightRotation(AVLTreeNode<E> k1) {
        AVLTreeNode<E> k2;

        k2 = k1.getRight();
        k1.setRight(k2.getLeft());
        k2.setLeft(k1);

        k1.setHeight(max(height(k1.getLeft()), height(k1.getRight())) + 1);
        k2.setHeight(max(height(k2.getRight()), k1.getHeight()) + 1);

        return k2;
    }

    /**
     * left-right rotation
     * @param k3
     * @return root after rotation
     */
    protected AVLTreeNode<E> leftRightRotation(AVLTreeNode<E> k3) {
        k3.setLeft(rightRightRotation(k3.getLeft()));

        return leftLeftRotation(k3);
    }

    /**
     * right-left rotation
     * @param k1
     * @return root after rotation
     */
    protected AVLTreeNode<E> rightLeftRotation(AVLTreeNode<E> k1) {
        k1.setRight(leftLeftRotation(k1.getRight()));

        return rightRightRotation(k1);
    }

    /**
     * @param node to start with
     * @return the maximum node in current tree
     */
    protected AVLTreeNode<E> maximum(AVLTreeNode<E> node) {
        AVLTreeNode<E> tmp = node;
        while (tmp.getRight() != null)
            tmp = tmp.getRight();

        return tmp;
    }

    /**
     * @param node to start with
     * @return the minimum node in current tree
     */
    protected AVLTreeNode<E> minimum(AVLTreeNode<E> node) {
        AVLTreeNode<E> tmp = node;
        while (tmp.getLeft() != null)
            tmp = tmp.getLeft();

        return tmp;
    }

    /**
     * traversal the tree in order
     * @param tree
     */
    public void inOrder(AVLTreeNode<E> tree) {
        if (tree != null) {
            inOrder(tree.getLeft());
            // arr.add(tree.getKey());
            System.out.println(tree.getKey() + "---");
            inOrder(tree.getRight());
        }
    }

    public void inOrder() {
        inOrder(root);
    }

    public void clear() {
        root = null;
    }
}
