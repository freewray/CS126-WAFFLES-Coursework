package uk.ac.warwick.cs126.structures;

public abstract class AVLTreeStore<E> extends AVLTree<E> {
    protected final String sortBy;

    public AVLTreeStore() {
        this.sortBy = "id";
    }

    public AVLTreeStore(String sortBy) {
        this.sortBy = sortBy;
    }

    protected abstract int customCompare(E o1, E o2);

    private AVLTreeNode<E> insert(AVLTreeNode<E> tree, E key) {
        if (tree == null) {
            tree = new AVLTreeNode<>(key, null, null);
        } else {
            if (customCompare(key, tree.getKey()) < 0) {
                tree.setLeft(insert(tree.getLeft(), key));
                if (super.height(tree.getLeft()) - height(tree.getRight()) == 2) {
                    if (customCompare(key, tree.getLeft().getKey()) < 0)
                        tree = super.leftLeftRotation(tree);
                    else
                        tree = super.leftRightRotation(tree);
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

        tree.setHeight(super.max(height(tree.getLeft()), height(tree.getRight())) + 1);
        return tree;
    }

    public void insert(E key) {
        root = insert(root, key);
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

//    public void inOrder(AVLTreeNode<E> tree, MyArrayList<E> arr) {
//        if (tree != null) {
//            inOrder(tree.getLeft(), arr);
//            arr.add(tree.getKey());
//            inOrder(tree.getRight(), arr);
//        }
//    }
//
//    public void inOrder(MyArrayList<E> arr) {
//        inOrder(root, arr);
//    }
//
//    public MyArrayList<E> toArrayList() {
//        MyArrayList<E> list = new MyArrayList<>();
//        inOrder(root, list);
//        return list;
//    }
}
