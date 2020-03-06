package uk.ac.warwick.cs126.structures;

public abstract class AVLTree<E, F extends Comparable<F>> {
    protected AVLTreeNode<E> root;
    protected final String sortBy;

    public AVLTree() {
        this.sortBy = "id";
    }

    public AVLTree(String sortBy) {
        this.sortBy = sortBy;
    }

    protected abstract int customCompare(E o1, E o2);

    protected abstract int idOnlyCompare(E o1, E o2);

    protected abstract int idOnlyCompare(F id, E o);

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

    private AVLTreeNode<E> insert(AVLTreeNode<E> tree, E key) {
        if (tree == null) {
            tree = new AVLTreeNode<>(key, null, null);
        } else {
            if (customCompare(key, tree.getKey()) < 0) {
                tree.setLeft(insert(tree.getLeft(), key));
                if (height(tree.getLeft()) - height(tree.getRight()) == 2) {
                    if (customCompare(key, tree.getLeft().getKey()) < 0)
                        tree = leftLeftRotation(tree);
                    else
                        tree = leftRightRotation(tree);
                }
            } else if (customCompare(key, tree.getKey()) > 0) {
                tree.setRight(insert(tree.getRight(), key));
                if (height(tree.getRight()) - height(tree.getLeft()) == 2) {
                    if (customCompare(key, tree.getRight().getKey()) > 0)
                        tree = rightRightRotation(tree);
                    else
                        tree = rightLeftRotation(tree);
                }
            } else { // cmp==0
                System.out.println("FAILED: Same nodes are not allowed in AVL Tree");
            }
        }

        tree.setHeight(max(height(tree.getLeft()), height(tree.getRight())) + 1);
        return tree;
    }

    public void insert(E key) {
        root = insert(root, key);
    }

    /**
     * @param tree
     * @param key  to be insert into the tree
     * @return AVLTreeNode<E>
     */
    private AVLTreeNode<E> insertByID(AVLTreeNode<E> tree, E key) {
        if (tree == null) {
            // if the tree is empty, create a new node as root
            tree = new AVLTreeNode<>(key, null, null);
        } else {
            if (idOnlyCompare(key, tree.getKey()) < 0) { // insert the new node to left subtree
                tree.setLeft(insertByID(tree.getLeft(), key));
                // rebalance the tree after the insertion
                if (height(tree.getLeft()) - height(tree.getRight()) == 2) {
                    if (idOnlyCompare(key, tree.getLeft().getKey()) < 0)
                        tree = leftLeftRotation(tree);
                    else
                        tree = leftRightRotation(tree);
                }
            } else if (idOnlyCompare(key, tree.getKey()) > 0) { // insert the new node to right subtree
                tree.setRight(insertByID(tree.getRight(), key));
                // rebalance the tree after the insertion
                if (height(tree.getRight()) - height(tree.getLeft()) == 2) {
                    if (idOnlyCompare(key, tree.getRight().getKey()) > 0)
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

    public void insertByID(E key) {
        root = insertByID(root, key);
    }

    protected AVLTreeNode<E> remove(AVLTreeNode<E> tree, AVLTreeNode<E> rm) {
        if (tree == null || rm == null)
            return null;

        int cmp = customCompare(rm.getKey(), tree.getKey());
        if (cmp < 0) {
            tree.setLeft(remove(tree.getLeft(), rm));
            if (height(tree.getRight()) - height(tree.getLeft()) == 2) {
                AVLTreeNode<E> r = tree.getRight();
                if (height(r.getLeft()) > height(r.getRight()))
                    tree = rightLeftRotation(tree);
                else
                    tree = rightRightRotation(tree);
            }
        } else if (cmp > 0) {
            tree.setRight(remove(tree.getRight(), rm));
            if (height(tree.getLeft()) - height(tree.getRight()) == 2) {
                AVLTreeNode<E> l = tree.getLeft();
                if (height(l.getRight()) > height(l.getLeft()))
                    tree = leftRightRotation(tree);
                else
                    tree = leftLeftRotation(tree);
            }
        } else {
            if ((tree.getLeft() != null) && (tree.getRight() != null)) {
                if (height(tree.getLeft()) > height(tree.getRight())) {
                    AVLTreeNode<E> max = maximum(tree.getLeft());
                    tree.setKey(max.getKey());
                    tree.setLeft(remove(tree.getLeft(), max));
                } else {
                    AVLTreeNode<E> min = minimum(tree.getRight());
                    tree.setKey(min.getKey());
                    tree.setRight(remove(tree.getRight(), min));
                }
            } else {
                tree = (tree.getLeft() != null) ? tree.getLeft() : tree.getRight();
            }
        }
        return tree;
    }

    public void remove(E key) {
        AVLTreeNode<E> tmp;

        if ((tmp = search(root, key)) != null)
            root = remove(root, tmp);
    }

    private AVLTreeNode<E> search(AVLTreeNode<E> node, E key) {

        if (node == null) {
            return null; // missing from tree
        } else if (idOnlyCompare(key, node.getKey()) < 0) {
            return search(node.getLeft(), key);
        } else if (idOnlyCompare(key, node.getKey()) > 0) {
            return search(node.getRight(), key);
        } else {
            return node; // found it
        }
    }

    public AVLTreeNode<E> search(E key) {
        return search(root, key);
    }

    private E searchByID(AVLTreeNode<E> node, F id) {

        if (node == null) {
            return null; // missing from tree
        } else if (idOnlyCompare(id, node.getKey()) < 0) {
            return searchByID(node.getLeft(), id);
        } else if (idOnlyCompare(id, node.getKey()) > 0) {
            return searchByID(node.getRight(), id);
        } else {
            return node.getKey(); // found it
        }
    }

    public E searchByID(F id) {
        return searchByID(root, id);
    }

    private void inOrder(AVLTreeNode<E> tree, MyArrayList<E> arr) {
        if (tree != null) {
            inOrder(tree.getLeft(), arr);
            arr.add(tree.getKey());
            inOrder(tree.getRight(), arr);
        }
    }

    public void inOrder(MyArrayList<E> arr) {
        inOrder(root, arr);
    }

    public MyArrayList<E> toArrayList() {
        MyArrayList<E> list = new MyArrayList<>();
        inOrder(root, list);
        return list;
    }
}
