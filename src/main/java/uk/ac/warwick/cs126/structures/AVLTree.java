package uk.ac.warwick.cs126.structures;

public class AVLTree<E> {
    private AVLTreeNode<E> root;    // 根结点

    protected int height(AVLTreeNode<E> tree) {
        if (tree != null)
            return tree.getHeight();
        return 0;
    }

    public int height() {
        return height(root);
    }

    /*
     * 比较两个值的大小
     */
    protected int max(int a, int b) {
        return a > b ? a : b;
    }

    /*
     * LL：左左对应的情况(左单旋转)。
     *
     * 返回值：旋转后的根节点
     */
    protected AVLTreeNode<E> leftLeftRotation(AVLTreeNode<E> k2) {
        AVLTreeNode<E> k1;

        k1 = k2.getLeft();
        k2.setLeft(k1.getRight());
        k1.setRight(k2);

        k2.setHeight(max( height(k2.getLeft()), height(k2.getRight())) + 1);
        k1.setHeight(max( height(k1.getLeft()), k2.getHeight()) + 1);

        return k1;
    }

    /*
     * RR：右右对应的情况(右单旋转)。
     *
     * 返回值：旋转后的根节点
     */
    protected AVLTreeNode<E> rightRightRotation(AVLTreeNode<E> k1) {
        AVLTreeNode<E> k2;

        k2 = k1.getRight();
        k1.setRight(k2.getLeft());
        k2.setLeft(k1);

        k1.setHeight(max( height(k1.getLeft()), height(k1.getRight())) + 1);
        k2.setHeight(max( height(k2.getRight()), k1.getHeight()) + 1);

        return k2;
    }

    /*
     * LR：左右对应的情况(左双旋转)。
     *
     * 返回值：旋转后的根节点
     */
    protected AVLTreeNode<E> leftRightRotation(AVLTreeNode<E> k3) {
        k3.setLeft(rightRightRotation(k3.getLeft()));

        return leftLeftRotation(k3);
    }

    /*
     * RL：右左对应的情况(右双旋转)。
     *
     * 返回值：旋转后的根节点
     */
    protected AVLTreeNode<E> rightLeftRotation(AVLTreeNode<E> k1) {
        k1.setRight(leftLeftRotation(k1.getRight()));

        return rightRightRotation(k1);
    }

    protected AVLTreeNode<E> maximum(AVLTreeNode<E> node){
        AVLTreeNode<E> tmp = node;
        while (tmp.getRight() != null)
            tmp = tmp.getRight();

        return tmp;
    }

    protected AVLTreeNode<E> minimum(AVLTreeNode<E> node){
        AVLTreeNode<E> tmp = node;
        while (tmp.getLeft() != null)
            tmp = tmp.getLeft();

        return tmp;
    }

     /*
      * 中序遍历"AVL树"
      */
    public void inOrder(AVLTreeNode<E> tree) {
        if(tree != null)
        {
            inOrder(tree.getLeft());
//            arr.add(tree.getKey());
            System.out.println(tree.getKey()+"---");
            inOrder(tree.getRight());
        }
    }

    public void inOrder() {
        inOrder(root);
    }
}
