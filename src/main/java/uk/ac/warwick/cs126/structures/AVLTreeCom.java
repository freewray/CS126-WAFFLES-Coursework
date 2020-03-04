package uk.ac.warwick.cs126.structures;

public class AVLTreeCom<E extends Comparable> extends AVLTree<E> {
    private AVLTreeNode<E> root;

    private AVLTreeNode<E> insert(AVLTreeNode<E> tree, E key) {
        if (tree == null) {
             // 新建节点
             tree = new AVLTreeNode<E>(key, null, null);
             if (tree==null) {
                 System.out.println("ERROR: create AVL Tree node failed!");
                 return null;
             }
         } else {
             int cmp = key.compareTo(tree.getKey());

                if (cmp < 0) {    // 应该将key插入到"tree的左子树"的情况
                 tree.setLeft(insert(tree.getLeft(), key));
                 // 插入节点后，若AVL树失去平衡，则进行相应的调节。
                 if (height(tree.getLeft()) - height(tree.getRight()) == 2) {
                     if (key.compareTo(tree.getLeft().getKey()) < 0)
                         tree = leftLeftRotation(tree);
                     else
                         tree = leftRightRotation(tree);
                 }
             } else if (cmp > 0) {    // 应该将key插入到"tree的右子树"的情况
                 tree.setRight(insert(tree.getRight(), key));
                 // 插入节点后，若AVL树失去平衡，则进行相应的调节。
                 if (height(tree.getRight()) - height(tree.getLeft()) == 2) {
                     if (key.compareTo(tree.getRight().getKey()) > 0)
                         tree = rightRightRotation(tree);
                     else
                         tree = rightLeftRotation(tree);
                 }
             } else {    // cmp==0
                 System.out.println("添加失败：不允许添加相同的节点！");
             }
         }

         tree.setHeight(max( height(tree.getLeft()), height(tree.getRight())) + 1);

         return tree;
    }

    public void insert(E key) {
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
    private AVLTreeNode<E> remove(AVLTreeNode<E> tree, AVLTreeNode<E> rm) {
        // 根为空 或者 没有要删除的节点，直接返回null。
        if (tree==null || rm==null)
            return null;

        int cmp = rm.getKey().compareTo(tree.getKey());
        if (cmp < 0) {        // 待删除的节点在"tree的左子树"中
            tree.setLeft(remove(tree.getLeft(), rm));
            // 删除节点后，若AVL树失去平衡，则进行相应的调节。
            if (height(tree.getRight()) - height(tree.getLeft()) == 2) {
                AVLTreeNode<E> r =  tree.getRight();
                if (height(r.getLeft()) > height(r.getRight()))
                    tree = rightLeftRotation(tree);
                else
                    tree = rightRightRotation(tree);
            }
        } else if (cmp > 0) {    // 待删除的节点在"tree的右子树"中
            tree.setRight(remove(tree.getRight(), rm));
            // 删除节点后，若AVL树失去平衡，则进行相应的调节。
            if (height(tree.getLeft()) - height(tree.getRight()) == 2) {
                AVLTreeNode<E> l =  tree.getLeft();
                if (height(l.getRight()) > height(l.getLeft()))
                    tree = leftRightRotation(tree);
                else
                    tree = leftLeftRotation(tree);
            }
        } else {    // tree是对应要删除的节点。
            // tree的左右孩子都非空
            if ((tree.getLeft()!=null) && (tree.getRight()!=null)) {
                if (height(tree.getLeft()) > height(tree.getRight())) {
                    // 如果tree的左子树比右子树高；
                    // 则(01)找出tree的左子树中的最大节点
                    //   (02)将该最大节点的值赋值给tree。
                    //   (03)删除该最大节点。
                    // 这类似于用"tree的左子树中最大节点"做"tree"的替身；
                    // 采用这种方式的好处是：删除"tree的左子树中最大节点"之后，AVL树仍然是平衡的。
                    AVLTreeNode<E> max = maximum(tree.getLeft());
                    tree.setKey(max.getKey());
                    tree.setLeft(remove(tree.getLeft(), max));
                } else {
                    // 如果tree的左子树不比右子树高(即它们相等，或右子树比左子树高1)
                    // 则(01)找出tree的右子树中的最小节点
                    //   (02)将该最小节点的值赋值给tree。
                    //   (03)删除该最小节点。
                    // 这类似于用"tree的右子树中最小节点"做"tree"的替身；
                    // 采用这种方式的好处是：删除"tree的右子树中最小节点"之后，AVL树仍然是平衡的。
                    AVLTreeNode<E> min = minimum(tree.getRight());
                    tree.setKey(min.getKey());
                    tree.setRight(remove(tree.getRight(), min));
                }
            } else {
                AVLTreeNode<E> tmp = tree;
                tree = (tree.getLeft()!=null) ? tree.getLeft() : tree.getRight();
                tmp = null;
            }
        }
        return tree;
    }

    public void remove(E key) {
        AVLTreeNode<E> tmp;

        if ((tmp = search(root, key)) != null)
            root = remove(root, tmp);
    }

    public AVLTreeNode<E> search(AVLTreeNode<E> node, E key) {

        if (node == null) {
             return null;  // missing from tree
        } else if (key.compareTo(node.getKey()) < 0) {
             return search(node.getLeft(), key);
        } else if (key.compareTo(node.getKey()) > 0) {
             return search(node.getRight(), key);
        } else {
             return node;  // found it
        }
    }

    public AVLTreeNode<E> search(E key) {
        return search(root, key);
    }

    private void print(AVLTreeNode<E> tree, E key, int direction) {
        if(tree != null) {
            if(direction==0)    // tree是根节点
                System.out.println(tree.getKey());
            else                // tree是分支节点
                System.out.println(tree.getKey() + (direction==1?"right" : "left"));

            print(tree.getLeft(), tree.getKey(), -1);
            print(tree.getRight(),tree.getKey(),  1);
        }
    }

    public void print() {
        if (root != null)
            print(root, root.getKey(), 0);
    }

    public void inOrder(AVLTreeNode<E> tree, MyArrayList<E> arr) {
        if(tree != null)
        {
            inOrder(tree.getLeft(), arr);
            arr.add(tree.getKey());
            inOrder(tree.getRight(), arr);
        }
    }

    public void inOrder(MyArrayList<E> arr) {
        inOrder(root, arr);
    }
}
