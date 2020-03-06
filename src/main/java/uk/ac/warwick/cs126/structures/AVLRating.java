package uk.ac.warwick.cs126.structures;

public class AVLRating extends AVLTreeCom<Rating> {

    /**
     * Find id in tree
     * @param node
     * @param id
     * @return AVLTreeNode<Rating>
     */
    private AVLTreeNode<Rating> searchByID(AVLTreeNode<Rating> node, Long id) {
        if (node == null) {
            return null; // missing from tree
        } else if (id.compareTo(node.getKey().getId()) < 0) {
            return searchByID(node.getLeft(), id);
        } else if (id.compareTo(node.getKey().getId()) > 0) {
            return searchByID(node.getRight(), id);
        } else {
            return node; // found it
        }
    }

    public AVLTreeNode<Rating> searchByID(Long id) {
        return searchByID(root, id);
    }

    /**
     * @param tree
     * @param key to be insert into the tree
     * @return AVLTreeNode<Rating>
     */
    private AVLTreeNode<Rating> insertByID(AVLTreeNode<Rating> tree, Rating key) {
        if (tree == null) {
            // if the tree is empty, create a new node as root
            tree = new AVLTreeNode<>(key, null, null);
        } else {
            if (key.getId().compareTo(tree.getKey().getId()) < 0) { // insert the new node to left subtree
                tree.setLeft(insertByID(tree.getLeft(), key));
                // rebalance the tree after the insertion
                if (height(tree.getLeft()) - height(tree.getRight()) == 2) {
                    if (key.getId().compareTo(tree.getLeft().getKey().getId()) < 0)
                        tree = leftLeftRotation(tree);
                    else
                        tree = leftRightRotation(tree);
                }
            } else if (key.getId().compareTo(tree.getKey().getId()) > 0) { // insert the new node to right subtree
                tree.setRight(insertByID(tree.getRight(), key));
                // rebalance the tree after the insertion
                if (height(tree.getRight()) - height(tree.getLeft()) == 2) {
                    if (key.getId().compareTo(tree.getRight().getKey().getId()) > 0)
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

    public void insertByID(Rating key) {
        root = insertByID(root, key);
    }
}