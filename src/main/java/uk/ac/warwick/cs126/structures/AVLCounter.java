package uk.ac.warwick.cs126.structures;

public class AVLCounter extends AVLTreeCom<Counter<String>> {

    /** 
     * Find keyword in tree
     * @param node
     * @param keyword
     * @return AVLTreeNode<Counter<String>>
     */
    private AVLTreeNode<Counter<String>> searchKeyword(AVLTreeNode<Counter<String>> node, String keyword) {
        if (node == null) {
            return null; // missing from tree
        } else if (keyword.toLowerCase().compareTo(node.getKey().getIdentifier().toLowerCase()) < 0) {
            return searchKeyword(node.getLeft(), keyword);
        } else if (keyword.toLowerCase().compareTo(node.getKey().getIdentifier().toLowerCase()) > 0) {
            return searchKeyword(node.getRight(), keyword);
        } else {
            return node; // found it
        }
    }

    public AVLTreeNode<Counter<String>> searchKeyword(String keyword) {
        return searchKeyword(root, keyword);
    }

    /**
     * @param tree
     * @param key to be insert into the tree
     * @return AVLTreeNode<IDCounter>
     */
    private AVLTreeNode<Counter<String>> insertByWord(AVLTreeNode<Counter<String>> tree, Counter<String> key) {
        if (tree == null) {
            // if the tree is empty, create a new node as root
            tree = new AVLTreeNode<>(key, null, null);
        } else {
            if (key.getIdentifier().compareTo(tree.getKey().getIdentifier()) < 0) { // insert the new node to left subtree
                tree.setLeft(insertByWord(tree.getLeft(), key));
                // rebalance the tree after the insertion
                if (height(tree.getLeft()) - height(tree.getRight()) == 2) {
                    if (key.getIdentifier().compareTo(tree.getLeft().getKey().getIdentifier()) < 0)
                        tree = leftLeftRotation(tree);
                    else
                        tree = leftRightRotation(tree);
                }
            } else if (key.getIdentifier().compareTo(tree.getKey().getIdentifier()) > 0) { // insert the new node to right subtree
                tree.setRight(insertByWord(tree.getRight(), key));
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

    public void insertByWord(Counter<String> key) {
        root = insertByWord(root, key);
    }
}