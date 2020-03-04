package uk.ac.warwick.cs126.structures;

import uk.ac.warwick.cs126.models.RestaurantDistance;

public class RestaurantDistanceAVLTree extends AVLTree<RestaurantDistance> {

    /**
     * sorted in ascending order of distance If they have the same Distance, then it
     * is sorted in ascending order of their ID.
     */
    private int distanceCompare(RestaurantDistance r1, RestaurantDistance r2) {
        if (r1.getDistance() == r2.getDistance())
            return r1.getRestaurant().getID().compareTo(r2.getRestaurant().getID());
        else
            return r1.getDistance() < r2.getDistance() ? -1 : 1;
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
    private AVLTreeNode<RestaurantDistance> insert(AVLTreeNode<RestaurantDistance> tree, RestaurantDistance key) {
        if (tree == null) {
            // 新建节点
            tree = new AVLTreeNode<>(key, null, null);
            if (tree == null) {
                System.out.println("ERROR: create avltree node failed!");
                return null;
            }
        } else {
            int cmp = distanceCompare(key, tree.getKey());

            if (cmp < 0) {    // 应该将key插入到"tree的左子树"的情况
                tree.setLeft(insert(tree.getLeft(), key));
                // 插入节点后，若AVL树失去平衡，则进行相应的调节。
                if (super.height(tree.getLeft()) - height(tree.getRight()) == 2) {
                    cmp = distanceCompare(key, tree.getLeft().getKey());
                    if (cmp < 0)
                        tree = super.leftLeftRotation(tree);
                    else
                        tree = super.leftRightRotation(tree);
                }
            } else if (cmp > 0) {    // 应该将key插入到"tree的右子树"的情况
                tree.setRight(insert(tree.getRight(), key));
                // 插入节点后，若AVL树失去平衡，则进行相应的调节。
                if (height(tree.getRight()) - height(tree.getLeft()) == 2) {
                    cmp = distanceCompare(key, tree.getRight().getKey());
                    if (cmp > 0)
                        tree = rightRightRotation(tree);
                    else
                        tree = rightLeftRotation(tree);
                }
            } else {    // cmp==0
                System.out.println("Failed：Same nodes are not allowed！");
            }
        }

        tree.setHeight(super.max(height(tree.getLeft()), height(tree.getRight())) + 1);
        return tree;
    }

    public void insert(RestaurantDistance key) {
        root = insert(root, key);
    }

    public void inOrder(AVLTreeNode<RestaurantDistance> tree, MyArrayList<RestaurantDistance> arr) {
        if (tree != null) {
            inOrder(tree.getLeft(), arr);
            arr.add(tree.getKey());
            inOrder(tree.getRight(), arr);
        }
    }

    public void inOrder(MyArrayList<RestaurantDistance> arr) {
        inOrder(root, arr);
    }

    public MyArrayList<RestaurantDistance> allNodes() {
        MyArrayList<RestaurantDistance> list = new MyArrayList<>();
        inOrder(root, list);
        return list;
    }
}
