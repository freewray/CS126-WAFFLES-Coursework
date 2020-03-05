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
}