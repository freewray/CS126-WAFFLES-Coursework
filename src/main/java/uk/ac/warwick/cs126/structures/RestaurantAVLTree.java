package uk.ac.warwick.cs126.structures;

import uk.ac.warwick.cs126.models.Restaurant;

public class RestaurantAVLTree extends AVLTree<Restaurant> {
    private String sortBy;

    public RestaurantAVLTree() {
        sortBy = "id";
    }

    public RestaurantAVLTree(String sortBy) {
        this.sortBy = sortBy;
    }

    private int idCompare(Restaurant r1, Restaurant r2) {
        return r1.getID().compareTo(r2.getID());
    }

    private int nameCompare(Restaurant r1, Restaurant r2) {
        int nameCompare = r1.getName().compareToIgnoreCase(r2.getName());
        if (nameCompare == 0)
            return idCompare(r1, r2);
        else
            return nameCompare;
    }

    /**
     * sorted by Date Established, from oldest to most recent. If they have the same
     * Date Established, then it is sorted alphabetically by the restaurant Name. If
     * they have the same restaurant Name, then it is sorted in ascending order of
     * their ID.
     */
    private int dateCompare(Restaurant r1, Restaurant r2) {
        int dateCompare = r1.getDateEstablished().compareTo(r2.getDateEstablished());
        if (dateCompare == 0)
            return nameCompare(r1, r2);
        else
            return dateCompare;
    }

    /**
     * sorted in descending order of Warwick Stars. If they have the same Warwick
     * Stars, then it is sorted alphabetically by the restaurant Name. If they have
     * the same restaurant Name, then it is sorted in ascending order of their ID.
     */
    private int warwickStarCompare(Restaurant r1, Restaurant r2) {
        if (r1.getWarwickStars() == r2.getWarwickStars())
            return nameCompare(r1, r2);
        else
            return r2.getWarwickStars() - r1.getWarwickStars();
    }

    /**
     * sorted in descending order of Rating. If they have the same Rating, then it
     * is sorted alphabetically by the restaurant Name. If they have the same
     * restaurant Name, then it is sorted in ascending order of their ID.
     */
    private int ratingCompare(Restaurant r1, Restaurant r2) {
        if (r1.getCustomerRating() == r2.getCustomerRating())
            return nameCompare(r1, r2);
        else
            return r2.getCustomerRating() < r1.getCustomerRating() ? -1 : 1;
    }

    private int compare(Restaurant r1, Restaurant r2){
        if (this.sortBy.equalsIgnoreCase("id"))
            return this.idCompare(r1, r2);
        else if (this.sortBy.equalsIgnoreCase("name"))
            return this.nameCompare(r1, r2);
        else if (this.sortBy.equalsIgnoreCase("date"))
            return this.dateCompare(r1, r2);
        else if (this.sortBy.equalsIgnoreCase("warwickStar"))
            return this.warwickStarCompare(r1, r2);
        else if (this.sortBy.equalsIgnoreCase("rating"))
            return this.ratingCompare(r1, r2);
        else
            return 0;
    }

    /*
     * 将结点插入到AVL树中，并返回根节点
     *
     * 参数说明：
     *     tree AVL树的根结点
     *     key 插入的结点的键值
     * 返回值：
     *     根节点
     */
    private AVLTreeNode<Restaurant> insert(AVLTreeNode<Restaurant> tree, Restaurant key) {
        if (tree == null) {
            // 新建节点
            tree = new AVLTreeNode<>(key, null, null);
            if (tree == null) {
                System.out.println("ERROR: create avltree node failed!");
                return null;
            }
        } else {
            int cmp = compare(key, tree.getKey());
            if (cmp < 0) {    // 应该将key插入到"tree的左子树"的情况
                tree.setLeft(insert(tree.getLeft(), key));
                // 插入节点后，若AVL树失去平衡，则进行相应的调节。
                if (super.height(tree.getLeft()) - height(tree.getRight()) == 2) {
                    cmp = compare(key, tree.getLeft().getKey());
                    if (cmp < 0)
                        tree = super.leftLeftRotation(tree);
                    else
                        tree = super.leftRightRotation(tree);
                }
            } else if (cmp > 0) {    // 应该将key插入到"tree的右子树"的情况
                tree.setRight(insert(tree.getRight(), key));
                // 插入节点后，若AVL树失去平衡，则进行相应的调节。
                if (height(tree.getRight()) - height(tree.getLeft()) == 2) {
                    cmp = compare(key, tree.getRight().getKey());
                    if (cmp > 0)
                        tree = rightRightRotation(tree);
                    else
                        tree = rightLeftRotation(tree);
                }
            } else {    // cmp==0
                System.out.println("添加失败：不允许添加相同的节点！");
            }
        }

        tree.setHeight(super.max(height(tree.getLeft()), height(tree.getRight())) + 1);
        return tree;
    }

    public void insert(Restaurant key) {
        root = insert(root, key);
    }

    /*
     * 删除结点(z)，返回根节点
     *
     * 参数说明：
     *     tree AVL树的根结点
     *     z 待删除的结点
     * 返回值：
     *     根节点
     */
    private AVLTreeNode<Restaurant> remove(AVLTreeNode<Restaurant> tree, AVLTreeNode<Restaurant> rm) {
        // 根为空 或者 没有要删除的节点，直接返回null。
        if (tree == null || rm == null)
            return null;

        int cmp = rm.getKey().getID().compareTo(tree.getKey().getID());
        if (cmp < 0) {        // 待删除的节点在"tree的左子树"中
            tree.setLeft(remove(tree.getLeft(), rm));
            // 删除节点后，若AVL树失去平衡，则进行相应的调节。
            if (height(tree.getRight()) - height(tree.getLeft()) == 2) {
                AVLTreeNode<Restaurant> r = tree.getRight();
                if (height(r.getLeft()) > height(r.getRight()))
                    tree = rightLeftRotation(tree);
                else
                    tree = rightRightRotation(tree);
            }
        } else if (cmp > 0) {    // 待删除的节点在"tree的右子树"中
            tree.setRight(remove(tree.getRight(), rm));
            // 删除节点后，若AVL树失去平衡，则进行相应的调节。
            if (height(tree.getLeft()) - height(tree.getRight()) == 2) {
                AVLTreeNode<Restaurant> l = tree.getLeft();
                if (height(l.getRight()) > height(l.getLeft()))
                    tree = leftRightRotation(tree);
                else
                    tree = leftLeftRotation(tree);
            }
        } else {    // tree是对应要删除的节点。
            // tree的左右孩子都非空
            if ((tree.getLeft() != null) && (tree.getRight() != null)) {
                if (height(tree.getLeft()) > height(tree.getRight())) {
                    // 如果tree的左子树比右子树高；
                    // 则(01)找出tree的左子树中的最大节点
                    //   (02)将该最大节点的值赋值给tree。
                    //   (03)删除该最大节点。
                    // 这类似于用"tree的左子树中最大节点"做"tree"的替身；
                    // 采用这种方式的好处是：删除"tree的左子树中最大节点"之后，AVL树仍然是平衡的。
                    AVLTreeNode<Restaurant> max = maximum(tree.getLeft());
                    tree.setKey(max.getKey());
                    tree.setLeft(remove(tree.getLeft(), max));
                } else {
                    // 如果tree的左子树不比右子树高(即它们相等，或右子树比左子树高1)
                    // 则(01)找出tree的右子树中的最小节点
                    //   (02)将该最小节点的值赋值给tree。
                    //   (03)删除该最小节点。
                    // 这类似于用"tree的右子树中最小节点"做"tree"的替身；
                    // 采用这种方式的好处是：删除"tree的右子树中最小节点"之后，AVL树仍然是平衡的。
                    AVLTreeNode<Restaurant> min = minimum(tree.getRight());
                    tree.setKey(min.getKey());
                    tree.setRight(remove(tree.getRight(), min));
                }
            } else {
                AVLTreeNode<Restaurant> tmp = tree;
                tree = (tree.getLeft() != null) ? tree.getLeft() : tree.getRight();
                tmp = null;
            }
        }
        return tree;
    }

    public void remove(Restaurant key) {
        AVLTreeNode<Restaurant> tmp;

        if ((tmp = search(root, key)) != null)
            root = remove(root, tmp);
    }

    public AVLTreeNode<Restaurant> search(AVLTreeNode<Restaurant> node, Restaurant key) {

        if (node == null) {
            return null;  // missing from tree
        } else if (key.getID().compareTo(node.getKey().getID()) < 0) {
            return search(node.getLeft(), key);
        } else if (key.getID().compareTo(node.getKey().getID()) > 0) {
            return search(node.getRight(), key);
        } else {
            return node;  // found it
        }
    }

    public AVLTreeNode<Restaurant> search(Restaurant key) {
        return search(root, key);
    }

    private Restaurant searchByID(AVLTreeNode<Restaurant> node, Long id) {

        if (node == null) {
            return null;  // missing from tree
        } else if (id.compareTo(node.getKey().getID()) < 0) {
            return searchByID(node.getLeft(), id);
        } else if (id.compareTo(node.getKey().getID()) > 0) {
            return searchByID(node.getRight(), id);
        } else {
            return node.getKey();  // found it
        }
    }

    public Restaurant searchByID(Long id) {
        return searchByID(root, id);
    }

    public void inOrder(AVLTreeNode<Restaurant> tree, MyArrayList<Restaurant> arr) {
        if (tree != null) {
            inOrder(tree.getLeft(), arr);
            arr.add(tree.getKey());
            inOrder(tree.getRight(), arr);
        }
    }

    public void inOrder(MyArrayList<Restaurant> arr) {
        inOrder(root, arr);
    }

    public MyArrayList<Restaurant> allNodes() {
        MyArrayList<Restaurant> list = new MyArrayList<>();
        inOrder(root, list);
        return list;
    }
}

