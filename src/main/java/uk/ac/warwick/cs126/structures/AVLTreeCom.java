package uk.ac.warwick.cs126.structures;

/**
 * AVL tree structure for comparable elements
 */
public class AVLTreeCom<E extends Comparable<E>> extends AVLTree<E> {
    protected AVLTreeNode<E> root;

    /** 
     * @param tree
     * @param key to be insert into the tree
     * @return AVLTreeNode<E>
     */
    private AVLTreeNode<E> insert(AVLTreeNode<E> tree, E key) {
        if (tree == null) {
            // if the tree is empty, create a new node as root
            tree = new AVLTreeNode<>(key, null, null);
        } else {
            if (key.compareTo(tree.getKey()) < 0) { // insert the new node to left subtree
                tree.setLeft(insert(tree.getLeft(), key));
                // rebalance the tree after the insertion
                if (height(tree.getLeft()) - height(tree.getRight()) == 2) {
                    if (key.compareTo(tree.getLeft().getKey()) < 0)
                        tree = leftLeftRotation(tree);
                    else
                        tree = leftRightRotation(tree);
                }
            } else if (key.compareTo(tree.getKey()) > 0) { // insert the new node to right subtree
                tree.setRight(insert(tree.getRight(), key));
                // rebalance the tree after the insertion
                if (height(tree.getRight()) - height(tree.getLeft()) == 2) {
                    if (key.compareTo(tree.getRight().getKey()) > 0)
                        tree = rightRightRotation(tree);
                    else
                        tree = rightLeftRotation(tree);
                }
            } else { // key.compareTo(tree.getKey()) == 0
                System.out.println("FAILED: Same nodes are not allowed in AVL Tree");
            }
        }
        // set the new height of the tree
        tree.setHeight(max(height(tree.getLeft()), height(tree.getRight())) + 1);
        return tree;
    }

    public void insert(E key) {
        root = insert(root, key);
    }

    /** 
     * @param tree
     * @param rm
     * @return AVLTreeNode<E>
     */
    private AVLTreeNode<E> remove(AVLTreeNode<E> tree, AVLTreeNode<E> rm) {
        // the tree is empty, or the param is invalid . nothing to remove
        if (tree == null || rm == null)
            return null;
        if (rm.getKey().compareTo(tree.getKey()) < 0) { // 'rm' is in the left subtree
            tree.setLeft(remove(tree.getLeft(), rm));
            // rebalance the tree after the deletion
            if (height(tree.getRight()) - height(tree.getLeft()) == 2) {
                AVLTreeNode<E> r = tree.getRight();
                if (height(r.getLeft()) > height(r.getRight()))
                    tree = rightLeftRotation(tree);
                else
                    tree = rightRightRotation(tree);
            }
        } else if (rm.getKey().compareTo(tree.getKey()) > 0) { // 'rm' is in the right subtree
            tree.setRight(remove(tree.getRight(), rm));
            // rebalance the tree after the deletion
            if (height(tree.getLeft()) - height(tree.getRight()) == 2) {
                AVLTreeNode<E> l = tree.getLeft();
                if (height(l.getRight()) > height(l.getLeft()))
                    tree = leftRightRotation(tree);
                else
                    tree = leftLeftRotation(tree);
            }
        } else {
            if ((tree.getLeft() != null) && (tree.getRight() != null)) {
                // 'rm' has 2 children
                if (height(tree.getLeft()) > height(tree.getRight())) {
                    /**
                     * if 'rm's left subtree is higher than the right subtree
                     * 1. find the maximum in the right subtree
                     * 2. set the maximum onto the 'rm' place
                     * 3. delete the maximum
                     */
                    AVLTreeNode<E> max = maximum(tree.getLeft());
                    tree.setKey(max.getKey());
                    tree.setLeft(remove(tree.getLeft(), max));
                } else {
                    /**
                     * if 'rm's right subtree is higher than or equal to the left subtree
                     * 1. find the minimum in the right subtree
                     * 2. set the minimum onto the 'rm' place
                     * 3. delete the minimum
                     */
                    AVLTreeNode<E> min = minimum(tree.getRight());
                    tree.setKey(min.getKey());
                    tree.setRight(remove(tree.getRight(), min));
                }
            } else {
                // 'rm' has 1 children, delete rm by treating it child as itself
                tree = (tree.getLeft() != null) ? tree.getLeft() : tree.getRight();
            }
        }
        return tree; // 'rm's successor
    }

    public void remove(E key) {
        AVLTreeNode<E> tmp;
        if ((tmp = search(root, key)) != null)
            root = remove(root, tmp);
    }

    /** 
     * @param node
     * @param key
     * @return AVLTreeNode<E>
     */
    private AVLTreeNode<E> search(AVLTreeNode<E> node, E key) {
        if (node == null) {
            return null; // missing from tree
        } else if (key.compareTo(node.getKey()) < 0) {
            return search(node.getLeft(), key);
        } else if (key.compareTo(node.getKey()) > 0) {
            return search(node.getRight(), key);
        } else {
            return node; // found it
        }
    }

    public AVLTreeNode<E> search(E key) {
        return search(root, key);
    }

    /**
     * Traverse the tree in order and add each element in to the passed in array
     * 
     * @param tree
     * @param arr
     */
    public void inOrder(AVLTreeNode<E> tree, MyArrayList<E> arr) {
        if (tree != null) {
            inOrder(tree.getLeft(), arr);
            arr.add(tree.getKey());
            inOrder(tree.getRight(), arr);
        }
    }

    public void inOrder(MyArrayList<E> arr) {
        inOrder(root, arr);
    }

    /** 
     * @return MyArrayList<E>
     */
    public MyArrayList<E> toArrayList() {
        MyArrayList<E> res = new MyArrayList<>();
        inOrder(res);
        return res;
    }
}
