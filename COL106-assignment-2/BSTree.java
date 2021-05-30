import java.util.HashSet;

// Class: Implementation of BST in A2
// Implement the following functions according to the specifications provided in Tree.java

public class BSTree extends Tree {

    private BSTree left, right;     // Children.
    private BSTree parent;          // Parent pointer.

    public BSTree(){
        super();
        // This acts as a sentinel root node
        // How to identify a sentinel node: A node with parent == null is SENTINEL NODE
        // The actual tree starts from one of the child of the sentinel node!.
        // CONVENTION: Assume right child of the sentinel node holds the actual root! and left child will always be null.
    }

    public BSTree(int address, int size, int key){
        super(address, size, key);
    }


    public BSTree Insert(int address, int size, int key)
    {
        // assumes sanity
        if(!sanity()) {
            System.out.println("sanity: INSERT");
        }

        BSTree cur = this;
        while(cur.parent != null) cur = cur.parent;
        return cur.insert(address, size, key);
    }
    private BSTree insert(int add, int sz, int k)
    {

        if(this.parent == null) {
            // this is sentinel
            if(this.right == null) {
                this.right = new BSTree(add, sz, k);
                this.right.parent = this;
                return this.right;
            }
            return this.right.insert(add, sz, k);
        }
        if(this.key < k) {
            if(this.right != null) return this.right.insert(add, sz, k);
            this.right = new BSTree(add, sz, k);
            this.right.parent = this;
            return this.right;
        }
        if(this.key > k) {
            if(this.left != null) return this.left.insert(add, sz, k);
            this.left = new BSTree(add, sz, k);
            this.left.parent = this;
            return this.left;
        }
        if(this.address < add) {
            if(this.right != null) return this.right.insert(add, sz, k);
            this.right = new BSTree(add, sz, k);
            this.right.parent = this;
            return this.right;
        }
        if(this.address > add) {

            if(this.left != null) return this.left.insert(add, sz, k);
            this.left = new BSTree(add, sz, k);
            this.left.parent = this;
            return this.left;
        }
        // System.out.println("DUPLICATE!");
        return null;
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

    private BSTree max() {
        BSTree temp = this;
        while(temp.right != null) {
            temp = temp.right;
        }
        return temp;
    }

    private static boolean del(BSTree root) {
        if(root == null) {
            return false;
        }
        if(root.parent == null) {
            // do nothing, thats a sentinel
            return false;
        }
        // find max in left subtree
        BSTree temp;
        if(root.left == null) {
            temp = root;
            if(temp == temp.parent.right) temp.parent.right = temp.right;
            else temp.parent.left = temp.right;
            if(temp.right != null) temp.right.parent = temp.parent;
            temp.parent = null;
            temp.right = null;
            return true;
        }


        temp = root.left.max();

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

        return true;
    }

    private static boolean Delete_given_root(BSTree root, Dictionary e) {
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

    /*
     * returns false if and only if:
     * deleting a sentinel, or
     * node with same key, address, size not found
     * sanity problem: having a right element in max node
    */
    public boolean Delete(Dictionary e)
    {
        // assumes sanity
        if(!sanity()) {
            System.out.println("sanity: DELETE");
        }

        if(e == null) return false;
        BSTree temp = this;
        while(temp.parent != null) temp = temp.parent;
        temp = temp.right; // CONVENTION
        if(temp == null) {
            return false;
        }
        return Delete_given_root(temp, e);


    }

    private static int find_key;
    private static int find_address;
    private static BSTree find_BSTree;

    private void find(int k, boolean exact) {
        // finds the one with the lowest address
        if(this.key < k) {
            if(this.right != null) this.right.find(k, exact);
        }
        else if(this.key == k) {
            if(find_key > k) {
                find_key = k;
                find_address = this.address;
                find_BSTree = this;
                if(this.left != null) this.left.find(k, exact);
            } else {
                // find.key = k
                if(this.address < find_address) {
                    find_address = this.address;
                    find_BSTree = this;
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
                    find_BSTree = this;
                }
                if(this.left != null) this.left.find(k, exact);
            }else {
                find_key = this.key;
                find_address = this.address;
                find_BSTree = this;
                if(this.left != null) this.left.find(k, exact);
            }
        }
    }
    public BSTree Find(int key, boolean exact)
    {
        // assumes sanity
        if(!sanity()) {
            System.out.println("sanity: FIND");
        }
        find_key =  Integer.MAX_VALUE;
        find_address = Integer.MAX_VALUE;
        find_BSTree = null;
        BSTree temp = this;
        while(temp.parent != null) temp = temp.parent;
        temp = temp.right;
        if(temp == null) return null;
        temp.find(key, exact);

        if(exact && find_key > key) return null;
        BSTree res = find_BSTree;

        find_key =  Integer.MAX_VALUE;
        find_address = Integer.MAX_VALUE;
        find_BSTree = null;
        return res;
    }

    public BSTree getFirst()
    {
        BSTree temp = this;
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

    public BSTree getNext()
    {
        
        if(this.parent == null) return null;
        BSTree temp = this;
        if(temp.right != null) {
            temp = temp.right;
            while(temp.left != null) {
                temp = temp.left;
                if(temp!= null && temp==temp.left) return null;
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

    private static boolean chkBST(BSTree root, int k_min, int k_max, int add_min, int add_max) {
        if(root == null) return true;

        BSTree dummy_max = new BSTree(add_max, root.size, k_max);
        BSTree dummy_min = new BSTree(add_min, root.size, k_min);

        if(root. left != null) {
            if(root.address == root. left.address && root.key == root. left.key) {
                // repetition
                return false;
            }
        }
        if(root.right != null) {
            if(root.address == root.right.address && root.key == root.right.key) {
                // repetition
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

    private static HashSet<BSTree> vis;
    /*
     * returns FALSE if:
     * 1. loop detected (self loop or bigger), or
     * 2. root.left == root or root.right == root, or
     * 3. root.left == root.right
    */
    private static boolean chkLoop(BSTree root) {
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
    private static boolean sentinelChk(BSTree sentinel) {
        if(sentinel == null) return false; // CANNOT BE NULL
        if(sentinel.parent != null) return false;
        if(sentinel.left != null) return false; // violates CONVENTION
        if(sentinel.right != null) if(sentinel.right.parent != sentinel || sentinel == sentinel.right) return false;
        if(sentinel.key != -1) return false;
        if(sentinel.address != -1) return false;
        if(sentinel.size != -1) return false;
        return true; // sentinel is definitely the root sentinel
    }

    public boolean sanity()
    {
        // return true;
        // CONVENTION: Assume right child of the sentinel node holds the actual root! and left child will always be null.
        BSTree cur = this;
        BSTree cur_par = this.parent;
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
        BSTree sentinel = cur; // sentinel is surely the sentinel

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

        BSTree cur1 = cur.getNext();
        while(cur1 != null) {
            if(cur.key == cur1.key && cur.address == cur1.address) {
                // duplicate!
                // System.out.println("SANITY: Duplicate!");
                return false;
            }
            cur = cur1;
            cur1 = cur1.getNext();
        }
        // this is all!

        return true;
    }
}
