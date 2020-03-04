package uk.ac.warwick.cs126.structures;

import uk.ac.warwick.cs126.models.Customer;

public class CustomerAVLTree extends AVLTree<Customer> {
    // private AVLTreeNode<Customer> root;
    private String sortBy;

    public CustomerAVLTree() {
        this.sortBy = "id";
    }

    public CustomerAVLTree(String sortBy) {
        this.sortBy = sortBy;
    }

    private int idCompare(Customer c1, Customer c2) {
        return c1.getID().compareTo(c2.getID());
    }

    private int nameCompare(Customer c1, Customer c2) {
        int firstNameCompare = c1.getFirstName().compareToIgnoreCase(c2.getFirstName());
        int lastNameCompare = c1.getLastName().compareToIgnoreCase((c2.getLastName()));
        if (firstNameCompare == 0 && lastNameCompare == 0)
            return idCompare(c1, c2);
        else if (lastNameCompare == 0)
            return firstNameCompare;
        else
            return lastNameCompare;
    }

    private int compare(Customer r1, Customer r2) {
        if (this.sortBy.equalsIgnoreCase("id"))
            return this.idCompare(r1, r2);
        else if (this.sortBy.equalsIgnoreCase("name"))
            return this.nameCompare(r1, r2);
        else
            return 0;
    }

    /*
     * 将结点插入到AVL树中，并返回根节点
     *
     * 参数说明： tree AVL树的根结点 key 插入的结点的键值 返回值： 根节点
     */
    private AVLTreeNode<Customer> insert(AVLTreeNode<Customer> tree, Customer key) {
        if (tree == null) {
            // 新建节点
            tree = new AVLTreeNode<>(key, null, null);
        } else {
            if (compare(key, tree.getKey()) < 0) { // 应该将key插入到"tree的左子树"的情况
                tree.setLeft(insert(tree.getLeft(), key));
                // 插入节点后，若AVL树失去平衡，则进行相应的调节。
                if (super.height(tree.getLeft()) - height(tree.getRight()) == 2) {
                    if (compare(key, tree.getLeft().getKey()) < 0)
                        tree = super.leftLeftRotation(tree);
                    else
                        tree = super.leftRightRotation(tree);
                }
            } else if (compare(key, tree.getKey()) > 0) { // 应该将key插入到"tree的右子树"的情况
                tree.setRight(insert(tree.getRight(), key));
                // 插入节点后，若AVL树失去平衡，则进行相应的调节。
                if (height(tree.getRight()) - height(tree.getLeft()) == 2) {
                    if (compare(key, tree.getRight().getKey()) > 0)
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

    public void insert(Customer key) {
        root = insert(root, key);
    }

    /*
     * 删除结点(z)，返回根节点
     *
     * 参数说明： tree AVL树的根结点 z 待删除的结点 返回值： 根节点
     */
    private AVLTreeNode<Customer> remove(AVLTreeNode<Customer> tree, AVLTreeNode<Customer> rm) {
        // 根为空 或者 没有要删除的节点，直接返回null。
        if (tree == null || rm == null)
            return null;

        int cmp = rm.getKey().getID().compareTo(tree.getKey().getID());
        if (cmp < 0) { // 待删除的节点在"tree的左子树"中
            tree.setLeft(remove(tree.getLeft(), rm));
            // 删除节点后，若AVL树失去平衡，则进行相应的调节。
            if (height(tree.getRight()) - height(tree.getLeft()) == 2) {
                AVLTreeNode<Customer> r = tree.getRight();
                if (height(r.getLeft()) > height(r.getRight()))
                    tree = rightLeftRotation(tree);
                else
                    tree = rightRightRotation(tree);
            }
        } else if (cmp > 0) { // 待删除的节点在"tree的右子树"中
            tree.setRight(remove(tree.getRight(), rm));
            // 删除节点后，若AVL树失去平衡，则进行相应的调节。
            if (height(tree.getLeft()) - height(tree.getRight()) == 2) {
                AVLTreeNode<Customer> l = tree.getLeft();
                if (height(l.getRight()) > height(l.getLeft()))
                    tree = leftRightRotation(tree);
                else
                    tree = leftLeftRotation(tree);
            }
        } else { // tree是对应要删除的节点。
            // tree的左右孩子都非空
            if ((tree.getLeft() != null) && (tree.getRight() != null)) {
                if (height(tree.getLeft()) > height(tree.getRight())) {
                    // 如果tree的左子树比右子树高；
                    // 则(01)找出tree的左子树中的最大节点
                    // (02)将该最大节点的值赋值给tree。
                    // (03)删除该最大节点。
                    // 这类似于用"tree的左子树中最大节点"做"tree"的替身；
                    // 采用这种方式的好处是：删除"tree的左子树中最大节点"之后，AVL树仍然是平衡的。
                    AVLTreeNode<Customer> max = maximum(tree.getLeft());
                    tree.setKey(max.getKey());
                    tree.setLeft(remove(tree.getLeft(), max));
                } else {
                    // 如果tree的左子树不比右子树高(即它们相等，或右子树比左子树高1)
                    // 则(01)找出tree的右子树中的最小节点
                    // (02)将该最小节点的值赋值给tree。
                    // (03)删除该最小节点。
                    // 这类似于用"tree的右子树中最小节点"做"tree"的替身；
                    // 采用这种方式的好处是：删除"tree的右子树中最小节点"之后，AVL树仍然是平衡的。
                    AVLTreeNode<Customer> min = minimum(tree.getRight());
                    tree.setKey(min.getKey());
                    tree.setRight(remove(tree.getRight(), min));
                }
            } else {
                tree = (tree.getLeft() != null) ? tree.getLeft() : tree.getRight();
            }
        }
        return tree;
    }

    public void remove(Customer key) {
        AVLTreeNode<Customer> tmp;

        if ((tmp = search(root, key)) != null)
            root = remove(root, tmp);
    }

    public AVLTreeNode<Customer> search(AVLTreeNode<Customer> node, Customer key) {

        if (node == null) {
            return null; // missing from tree
        } else if (key.getID().compareTo(node.getKey().getID()) < 0) {
            return search(node.getLeft(), key);
        } else if (key.getID().compareTo(node.getKey().getID()) > 0) {
            return search(node.getRight(), key);
        } else {
            return node; // found it
        }
    }

    private Customer searchByID(AVLTreeNode<Customer> node, Long id) {

        if (node == null) {
            return null; // missing from tree
        } else if (id.compareTo(node.getKey().getID()) < 0) {
            return searchByID(node.getLeft(), id);
        } else if (id.compareTo(node.getKey().getID()) > 0) {
            return searchByID(node.getRight(), id);
        } else {
            return node.getKey(); // found it
        }
    }

    public Customer searchByID(Long id) {
        return searchByID(root, id);
    }

    public void inOrder(AVLTreeNode<Customer> tree, MyArrayList<Customer> arr) {
        if (tree != null) {
            inOrder(tree.getLeft(), arr);
            arr.add(tree.getKey());
            inOrder(tree.getRight(), arr);
        }
    }

    public void inOrder(MyArrayList<Customer> arr) {
        inOrder(root, arr);
    }
}
