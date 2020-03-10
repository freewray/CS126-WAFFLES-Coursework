package uk.ac.warwick.cs126.structures;

public class AVLTreeIDCounter extends AVLTree<IDCounter, Long> {

    
    /**
     * default comparison decleared by the tree
     *
     * @param o1 object 1 to be compared with
     * @param o2 object 2 to be compared with
     * @return nagative number if o1 is smaller
     * positive number if o1 is bigger
     */
    public int customCompare(IDCounter o1, IDCounter o2) {
        return o1.compareTo(o2);
    }

    
    /**
     * id comparison
     *
     * @param o1 object 1s'id to be compared with
     * @param o2 object 2s'id to be compared with
     * @return nagative number if o1s'id is smaller
     * positive number if o1s'id is bigger
     */
    public int idOnlyCompare(IDCounter o1, IDCounter o2) {
        return o1.getIdentifier().compareTo(o2.getIdentifier());
    }

    
    /**
     * @param id identifier to be compared (id or keyword)
     * @param o  object to be compared with
     * @return nagative number if o1s'id is smaller
     * positive number if o1s'id is bigger
     */
    public int idOnlyCompare(Long id, IDCounter o) {
        return id.compareTo(o.getIdentifier());
    }


    /**
     * Insert the node into the tree with only its Identifier order
     *
     * @param tree to insert the node into
     * @param key  to be insert into the tree
     * @return AVLTreeNode<E> root node
     */
    private AVLTreeNode<IDCounter> insertByID(AVLTreeNode<IDCounter> tree, IDCounter key) {
        if (tree == null) {
            // if the tree is empty, create a new node as root
            tree = new AVLTreeNode<>(key, null, null);
        } else {
            if (key.getIdentifier().compareTo(tree.getKey().getIdentifier()) < 0) { // insert the new node to left subtree
                tree.setLeft(insertByID(tree.getLeft(), key));
                // rebalance the tree after the insertion
                if (height(tree.getLeft()) - height(tree.getRight()) == 2) {
                    if (idOnlyCompare(key, tree.getLeft().getKey()) < 0)
                        tree = leftLeftRotation(tree);
                    else
                        tree = leftRightRotation(tree);
                }
            } else if (key.getIdentifier().compareTo(tree.getKey().getIdentifier()) > 0) { // insert the new node to right subtree
                tree.setRight(insertByID(tree.getRight(), key));
                // rebalance the tree after the insertion
                if (height(tree.getRight()) - height(tree.getLeft()) == 2) {
                    if (idOnlyCompare(key, tree.getRight().getKey()) > 0)
                        tree = rightRightRotation(tree);
                    else
                        tree = rightLeftRotation(tree);
                }
            } else { // key.compareTo(tree.getKey()) == 0
                tree.getKey().addCount();
                if (tree.getKey().getLatestReviewDate().before(key.getLatestReviewDate()))
                    tree.getKey().setLatestReviewDate(key.getLatestReviewDate());
            }
        }
        // set the new height of the tree
        tree.setHeight(max(height(tree.getLeft()), height(tree.getRight())) + 1);
        return tree;
    }

    public void insertByID(IDCounter key) {
        root = insertByID(root, key);
    }

}