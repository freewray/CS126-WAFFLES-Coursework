package uk.ac.warwick.cs126.structures;

public class AVLIDCounter extends AVLTreeCom<IDCounter> {

    /** 
     * Find id in tree
     * @param node
     * @param keyword
     * @return AVLTreeNode<IDCounter>
     */
    private AVLTreeNode<IDCounter> searchByID(AVLTreeNode<IDCounter> node, Long id) {
        if (node == null) {
            return null; // missing from tree
        } else if (id.compareTo(node.getKey().getIdentifier()) < 0) {
            return searchByID(node.getLeft(), id);
        } else if (id.compareTo(node.getKey().getIdentifier()) > 0) {
            return searchByID(node.getRight(), id);
        } else {
            return node; // found it
        }
    }

    public AVLTreeNode<IDCounter> searchByID(Long id) {
        return searchByID(root, id);
    }

    /** 
    * @param tree
    * @param key to be insert into the tree
    * @return AVLTreeNode<IDCounter>
    */
    private AVLTreeNode<IDCounter> insertByID(AVLTreeNode<IDCounter> tree, IDCounter key) {
        if (tree == null) {
            // if the tree is empty, create a new node as root
            tree = new AVLTreeNode<IDCounter>(key, null, null);
        } else {
            if (key.getIdentifier().compareTo(tree.getKey().getIdentifier()) < 0) { // insert the new node to left subtree
                tree.setLeft(insertByID(tree.getLeft(), key));
                // rebalance the tree after the insertion
                if (height(tree.getLeft()) - height(tree.getRight()) == 2) {
                    if (key.getIdentifier().compareTo(tree.getLeft().getKey().getIdentifier()) < 0)
                        tree = leftLeftRotation(tree);
                    else
                        tree = leftRightRotation(tree);
                }
            } else if (key.getIdentifier().compareTo(tree.getKey().getIdentifier()) > 0) { // insert the new node to right subtree
                tree.setRight(insertByID(tree.getRight(), key));
                // rebalance the tree after the insertion
                if (height(tree.getRight()) - height(tree.getLeft()) == 2) {
                    if (key.getIdentifier().compareTo(tree.getRight().getKey().getIdentifier()) > 0)
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

    public void insertByID(IDCounter key) {
        root = insertByID(root, key);
    }
}