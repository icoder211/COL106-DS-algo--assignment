import java.util.HashSet;

// Class: Height balanced AVL Tree
// Binary Search Tree

public class AVLTree extends BSTree {
    
    private AVLTree left, right;     // Children. 
    private AVLTree parent;          // Parent pointer. 
    private int height;  // The height of the subtree
        
    public AVLTree() { 
        super();
        // This acts as a sentinel root node
        // How to identify a sentinel node: A node with parent == null is SENTINEL NODE
        // The actual tree starts from one of the child of the sentinel node !.
        // CONVENTION: Assume right child of the sentinel node holds the actual root! and left child will always be null.
        
    }

    public AVLTree(int address, int size, int key) { 
        super(address, size, key);
        this.height = 0;
    }

    // Implement the following functions for AVL Trees.
    // You need not implement all the functions. 
    // Some of the functions may be directly inherited from the AVLTree class and nothing needs to be done for those.
    // Remove the functions, to not override the inherited functions.
    
    public AVLTree Insert(int address, int size, int key) 
    { 
        // assumes sanity
        AVLTree cur = this;
        while(cur.parent != null) cur = cur.parent;
        return cur.insert(address, size, key);
    }
    private AVLTree insert(int add, int sz, int k) {
        if(this.parent == null) {
            if(this.right == null) {
                this.right = new AVLTree(add, sz, k);
                this.right.parent = this;
                this.right.height = 1;
                this.height = 2;
                return this.right;
            }
            return this.right.insert(add, sz, k);
        }
        if(this.key < k) {
            if(right == null) {
                right = new AVLTree(add, sz, k);
                right.parent = this;
                // right.height = 1;
                // this.height = 2;
                AVLTree to_return = this.right;
                chkBalance(right);
                return to_return;
            }
            return right.insert(add, sz, k);
        }
        if(this.key > k) {
            if(left == null) {
                left  = new AVLTree(add, sz, k);
                left .parent = this;
                // left .height = 1;
                // this.height = 2;
                AVLTree to_return = this. left;
                chkBalance( left);
                return to_return;
            }
            return left .insert(add, sz, k);
        }
        if(this.address < add) {
            if(right == null) {
                right = new AVLTree(add, sz, k);
                right.parent = this;
                // right.height = 1;
                // this.height = 2;

                AVLTree to_return = this.right;
                chkBalance(right);
                return to_return;
            }
            return right.insert(add, sz, k);
        }
        if(this.address > add) {
            if(left == null) {
                left  = new AVLTree(add, sz, k);
                left .parent = this;
                // left .height = 1;
                // this.height = 2;

                AVLTree to_return = this.left;
                chkBalance(left );
                return to_return;
            }
            return left .insert(add, sz, k);
        }
        return null;
    } 

    // O(1)
    private static int getHeight(AVLTree node) {
        // assumes children are valid AVLtree's
        if(node == null) return 0;
        int left_height  = node. left == null ? 0 : node. left.height;
        int right_height = node.right == null ? 0 : node.right.height;
        return 1 + Math.max(left_height, right_height);
    }
    private static int height(AVLTree node) {
        if(node == null) return 0;
        return node.height;
    }

    private static void chkBalance(AVLTree node) {
        if(node == null) return;
        rebalance(node);
        chkBalance(node.parent);
    }
    private static void rebalance(AVLTree gpar) {
        if(gpar == null) {
            return;
        }
        if(gpar.parent == null) {
            // gpar is the sentinel
            gpar.height = 1 + height(gpar.right);
            return;
        }
        int left_height  = height(gpar.left);
        int right_height = height(gpar.right);
        gpar.height = 1 + Math.max(left_height, right_height);

        // gpar.height = 1 + Math.max(left_height, right_height);
        if(Math.abs(left_height - right_height) <= 1) {
            return;
        }
        // gpar to be rebalanced!
        AVLTree par = (left_height > right_height ? gpar.left : gpar.right);
        int left_height_par  = height(par.left);
        int right_height_par = height(par.right);
        // if(par.height != 1 + Math.max(left_height_par, right_height_par)) {
        //     System.out.println("ABORT: height != 1 + max(left, right)");
        // }
        AVLTree node;
        if(left_height_par > right_height_par) {
            node = par.left;
        }
        else if(left_height_par < right_height_par) {
            node = par.right;
        }
        else {
            if(par == gpar.left) node = par.left;
            else node = par.right;
        }
        if(par == gpar.left) {
            if(node == par.left) {
                /*
                 *      .
                 *     /
                 *    .
                 *   /
                 *  .
                */
                rightRotate(par, gpar);
                return;
            }else {
                /*
                 *      .
                 *     /
                 *    .
                 *     \
                 *      .
                */
                leftRotate(node, par);
                rightRotate(node, gpar);
                return;
            }
        }else {
            if(node == par.left) {
                /*
                 *    .
                 *     \
                 *      .
                 *     /
                 *    .
                */
                rightRotate(node, par);
                leftRotate(node, gpar);
                return;

            }else {
                /*
                 *      .
                 *       \
                 *        .
                 *         \ 
                 *          .
                */
                leftRotate(par, gpar);
                return;
            }
        }
         
    }
    
 
    private static void rightRotate(AVLTree node, AVLTree par) {
        if(node == null || par == null) {
            return;
        }
        // par.parent is NOT null
        node.parent = par.parent; 
        if(par == par.parent.right) par.parent.right = node;
        else par.parent.left = node;
        par.left = node.right;
        if(node.right != null) node.right.parent = par;
        node.right = par;
        par.parent = node;
        par.height = getHeight(par);
        node.height = getHeight(node);
    }
    private static void leftRotate(AVLTree node, AVLTree par) {
        if(node == null || par == null) {
            return;
        }
        // par.parent is NOT null
        node.parent = par.parent;
        if(par == par.parent.right) par.parent.right = node;
        else par.parent.left = node;
        par.right = node.left;
        if(node. left != null) node.left.parent = par;
        node.left = par;
        par.parent = node;
        par.height = getHeight(par);
        node.height = getHeight(node);
    }


    private static int comp(Dictionary d, Dictionary e) {
        if(d.key == e.key) {
            if(d.address > e.address) {
                return 1;
            } else if(d.address == e.address) {
                return 0;
            } else {
                return -1;
            }
        }else if(d.key > e.key) {
            return 1;
        }else {
            return -1;
        }
    }
    private AVLTree max() {
        AVLTree cur = this;
        while(cur.right != null) {
            cur = cur.right;
        }
        return cur;
    }
    private static boolean del(AVLTree root) {
        if(root == null) {
            return false;
        }
        if(root.parent == null) {
            // do nothing, thats a sentinel
            return false;
        }
        // find max in left subtree
        AVLTree temp;
        AVLTree to_be_rebalanced;
        if(root.left == null) {
            temp = root;
            to_be_rebalanced = temp.parent;
            if(temp == temp.parent.right) temp.parent.right = temp.right;
            else temp.parent.left = temp.right;
            if(temp.right != null) temp.right.parent = temp.parent;
            temp.parent = null;
            temp.right = null;
            chkBalance(to_be_rebalanced);
            return true;
        }
        
        temp = root.left.max();
        // print(temp);
        
        
        if(temp.parent==root) {
            to_be_rebalanced = temp;
        }else to_be_rebalanced = temp.parent;

        if(root == root.parent.left) root.parent.left = temp;
        else root.parent.right = temp;
        if(root.right != null) root.right.parent = temp;
        if(root.left != null) root.left.parent = temp;

        if(temp == temp.parent.right) temp.parent.right = temp.left;// temp.left could be null
        else temp.parent.left = temp.left;
        if(temp.left != null) temp.left.parent = temp.parent;

        if(temp != root.left) temp.left  = root.left ;
        if(temp != root.right) temp.right = root.right;
        temp.parent = root.parent;
        root.parent = null;
        root.left = null;
        root.right = null;
        chkBalance(to_be_rebalanced);

        return true;
    }

    private static boolean Delete_given_root(AVLTree root, Dictionary e) {
        int token = comp(root, e);
        if(token > 0) {
            return root.left == null ? false : Delete_given_root(root.left, e);
        }else if(token < 0) {
            return root.right == null ? false : Delete_given_root(root.right, e);
        }else {
            if(root.size == e.size) {
                // delete this element
                return del(root);
            }else {
                // do nothing, element not found
                return false;
            }
        }
    }
    public boolean Delete(Dictionary e)
    {
        // assumes sanity
        if(e == null) return false;
        AVLTree cur = this;
        while(cur.parent != null) cur = cur.parent;
        cur = cur.right;// convention
        if(cur == null) {
            //empty
            return false;
        }
        return Delete_given_root(cur, e);
    }

    private static int find_key;
    private static int find_address;
    private static AVLTree find_AVLTree;

    private void find(int k, boolean exact) {
        // finds the one with the lowest address
        if(this.key < k) {
            if(this.right != null) this.right.find(k, exact);
        }
        else if(this.key == k) {
            if(find_key > k) {
                find_key = k;
                find_address = this.address;
                find_AVLTree = this;
                if(this.left != null) this.left.find(k, exact);
            } else {
                // find.key = k
                if(this.address < find_address) {
                    find_address = this.address;
                    find_AVLTree = this;
                }
                if(this.left != null) this.left.find(k, exact);
            }
        }
        else if(this.key > k) {
            if(this.key > find_key) {
                if(this.left != null) this.left.find(k, exact);
            } else if (this.key == find_key) {
                if(this.address < find_address) {
                    find_address = this.address;
                    find_AVLTree = this;
                }
                if(this.left != null) this.left.find(k, exact);
            }else {
                find_key = this.key;
                find_address = this.address;
                find_AVLTree = this;     
                if(this.left != null) this.left.find(k, exact);
            }
        }
    }
    public AVLTree Find(int key, boolean exact)
    { 
        // assumes sanity
        find_key =  Integer.MAX_VALUE;
        find_address = Integer.MAX_VALUE;
        find_AVLTree = null;
        AVLTree temp = this;
        while(temp.parent != null) temp = temp.parent;
        temp = temp.right;
        if(temp == null) return null;
        temp.find(key, exact);
        
        if(exact && find_key > key) return null;
        AVLTree res = find_AVLTree;

        find_key =  Integer.MAX_VALUE;
        find_address = Integer.MAX_VALUE;
        find_AVLTree = null;
        return res;
    }

    public AVLTree getFirst()
    {
        AVLTree temp = this;
        while(temp.parent != null) {
            if(temp != null && temp == temp.parent) return null;
            temp = temp.parent;
        }
        temp = temp.right;
        if(temp == null) {
            // empty dictionary
            return null;
        }
        while(temp.left != null) {
            if(temp == temp .left) return null;
            temp = temp.left;
        }
        return temp;
    }

    public AVLTree getNext()
    {
        
        if(this.parent == null) return null;
        AVLTree temp = this;
        if(temp.right != null) {
            temp = temp.right;
            while(temp.left != null) {
                if(temp==temp.left) return null;
                temp = temp.left;
            }
            return temp;
        }
        while(temp.parent != null && temp.parent.right == temp) {
            if(temp != null && temp.parent == temp) {
                return null;
            }
            temp = temp.parent;
        }
        return temp.parent;
    }

    private static boolean chkBST(AVLTree root, int k_min, int k_max, int add_min, int add_max) {
        if(root == null) return true;
        
        AVLTree dummy_max = new AVLTree(add_max, root.size, k_max);
        AVLTree dummy_min = new AVLTree(add_min, root.size, k_min);

        if(root. left != null) {
            if(root.address == root. left.address && root.key == root. left.key) {
                // repetition
                // System.out.println("SANITY: repetition of node");
                return false;
            }
        }
        if(root.right != null) {
            if(root.address == root.right.address && root.key == root.right.key) {
                // repetition
                // System.out.println("SANITY: repetition of node");
                return false;
            }
        }
        if(comp(root, dummy_max) > 0) {
            return false;
        }
        if(comp(root, dummy_min) < 0) {
            return false;
        }
        boolean ok = chkBST(root.left, k_min, root.key, add_min, root.address);
        if(!ok) return false;
        ok = chkBST(root.right, root.key, k_max, root.address, add_max);
        if(!ok) return false;

        return true;
    }


    private static HashSet<AVLTree> vis;
    /*
     * returns FALSE if:
     * 1. loop detected (self loop or bigger), or
     * 2. root.left == root or root.right == root, or
     * 3. root.left == root.right
    */
    private static boolean chkLoop(AVLTree root) {
        if(root == null) return true;
        if(vis.contains(root)) return false;
        vis.add(root);
        if(root == root.parent || root == root.left || root == root.right) return false; // self loop
        if(root.left != null && root. left == root.right)  return false;
        if(root.left != null && root. left == root.parent) return false;
        if(root.right != null && root.right == root.parent) return false;
        if(root. left != null) if(root. left.parent != root) return false;
        if(root.right != null) if(root.right.parent != root) return false;

        if(
            !chkLoop(root.left)
        ) return false;
        if(
            !chkLoop(root.right)
        ) return false;
        return true;
    }
    private static boolean sentinelChk(AVLTree sentinel) {
        if(sentinel == null) return false; // CANNOT BE NULL
        if(sentinel.parent != null) return false;
        if(sentinel.left != null) return false; // violates CONVENTION
        if(sentinel.right != null) if(sentinel.right.parent != sentinel || sentinel == sentinel.right) return false;
        if(sentinel.key != -1) return false;
        if(sentinel.address != -1) return false;
        if(sentinel.size != -1) return false;
        return true; // sentinel is definitely the root sentinel
    }

    private static boolean chkAVL(AVLTree root) {
        if(root == null) return true;
        
        
        if(!chkAVL(root.left )) return false;
        if(!chkAVL(root.right)) return false;
        int left_height  = root. left == null ? 0 : root. left.height;
        int right_height = root.right == null ? 0 : root.right.height;
        if(root.height != 1 + Math.max(left_height, right_height)) {
            // root.height = 1 + Math.max(left_height, right_height);
            return false;
        }
        if(Math.abs(left_height - right_height) > 1) {
            return false;
        }
        return true;

    }

    public boolean sanity()
    { 
        // return true;
        // CONVENTION: Assume right child of the sentinel node holds the actual root! and left child will always be null.
        AVLTree cur = this;
        AVLTree cur_par = this.parent;
        while(cur_par != null) {
            if(cur == cur_par) {
                // System.out.println("SANITY: cur = cur_par");
                return false; // required to prevent infinite loop
            }
            if(cur != cur_par. left
            && cur != cur_par.right) {
                // System.out.println("SANITY: cur is not child of cur_par");
                return false; // NOT redundant
            }
            cur = cur_par;
            cur_par = cur_par.parent;
        }
        
        // cur is(should be) root sentinel
        if(!sentinelChk(cur)) return false;
        AVLTree sentinel = cur; // sentinel is surely the sentinel

        if(cur.right == null) return true;// empty

        vis = new HashSet<>(); // start with empty set of visited nodes
        if(
            ! chkLoop(sentinel)
        ) {
            // System.out.println("SANITY: loop / right.parent != parent / right = left ");
            return false;
        }
        vis = new HashSet<>(); // make vis empty again as this maybe used by further ops
        // Now, the graph "this" node is a part of, is definitely a tree

        // search property
        if(
            ! chkBST(sentinel.right, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE)
        ) {
            // System.out.println("SANITY: search property");
            return false;
        }
        // Now the tree is definitely a BST

        // Now check for duplicates
        cur = sentinel.getFirst();// NOT null
        
        AVLTree cur1 = cur.getNext();
        while(cur1 != null) {
            if(cur.key == cur1.key && cur.address == cur1.address) {
                // duplicate!
                // System.out.println("SANITY: Duplicate!");
                return false;
            }
            cur = cur1;
            cur1 = cur1.getNext();
        }
        // Now this is a BST with no repetitions of (key, address)

        //check AVL
        if(
            !chkAVL(sentinel.right)
        ) {
            // System.out.println("chkAVL");
            return false;
        }

        return true;
    }
    
    // private static void print(AVLTree e) {
    //     if(e == null) {
    //         System.out.println("null");
    //         return;
    //     }
    //     System.out.println(e.address + " " + e.size + " " + e.key + " ; ");
    // }

    // private static void println(AVLTree e) {
    //     println_(e);
    //     System.out.println("");
    // }
    // private static void println_(AVLTree e) {
    //     if(e == null) {
    //         System.out.print("null ; ");
    //         return;
    //     }
    //     println_(e.left);
        
    //     System.out.print(e.key + " " + height(e.left) + " " + height(e.right) + " ; ");
    //     println_(e.right);
    // }

    // public static void main(String[] args) {
    //     AVLTree t = new AVLTree();
    //     for(int i = 0;i < 10;i++) {
    //         t.Insert(0, 0, i);
    //     }
    //     System.out.println(t.sanity());
    //     println(t);
    //     AVLTree cur = t.getFirst();
    //     while(cur != null) {
    //         println(cur);
    //         cur = cur.getNext();
    //     }
    //     for(int i  =0;i < 1;i++){
    //         t.Delete(new AVLTree(0, 0, 2*i+1));
    //     }
    //     println(t);
    //     System.out.println(t.sanity());

    // }
}


