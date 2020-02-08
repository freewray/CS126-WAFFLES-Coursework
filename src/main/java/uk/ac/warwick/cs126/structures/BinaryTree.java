package uk.ac.warwick.cs126.structures;


public class BinaryTree<E extends Comparable<E>> {
    BinaryTreeNode<E> root;

    public BinaryTree() {
        root = null;
    }

    private void addToSubTree(BinaryTreeNode<E> n, E v) {
        if (n != null) // sanity check!
        {
            E nValue = n.getValue();
            if (v.compareTo(nValue) <= 0) {
                System.out.println("Adding " + v + " to left sub-tree of " + nValue);
                if (n.getLeft() == null)
                    n.setLeft(new BinaryTreeNode<>(v));
                else
                    addToSubTree(n.getLeft(), v);
            } else {
                System.out.println("Adding " + v + " to right sub-tree of " + nValue);
                if (n.getRight() == null)
                    n.setRight(new BinaryTreeNode<>(v));
                else
                    addToSubTree(n.getRight(), v);
            }
        }
    }

    public void add(E v) {
        if (root == null) {
            System.out.println("Adding " + v + " to root.");
            root = new BinaryTreeNode<>(v);
        } else
            addToSubTree(root, v);
    }

    private void inOrder(BinaryTreeNode<E> n) {
        if (n != null) {

            inOrder(n.getRight());
            System.out.print(((Integer) n.getValue()).intValue() + " ");
            inOrder(n.getLeft());
        }
    }

    // INCOMPLETE.
    private void preOrder(BinaryTreeNode<E> n) {
        // this method is to be completed...
        if (n != null) {
            System.out.print(((Integer) n.getValue()).intValue() + " ");
            preOrder(n.getLeft());
            preOrder(n.getRight());
        }
    }// 2 1 4 3 5

    // INCOMPLETE.
    private void postOrder(BinaryTreeNode<E> n) {
        // this method is to be completed...
        if (n != null) {
            postOrder(n.getLeft());
            postOrder(n.getRight());
            System.out.print(((Integer) n.getValue()).intValue() + " ");
        }
    }

    public void traversal() {
        System.out.print("Inorder traversal: ");

        preOrder(root);

        System.out.println();
    }
}