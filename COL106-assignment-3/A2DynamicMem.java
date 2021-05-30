// import java.io.BufferedWriter;
// import java.io.File;
// import java.io.FileWriter;
// import java.io.IOException;
// import java.util.Scanner;

// Class: A2DynamicMem
// Implements Degragment in A2. No other changes should be needed for other functions.

public class A2DynamicMem extends A1DynamicMem {

    public A2DynamicMem() {
        super();
    }

    public A2DynamicMem(int size) {
        super(size);
    }

    public A2DynamicMem(int size, int dict_type) {
        super(size, dict_type);
    }

    // In A2, you need to test your implementation using BSTrees and AVLTrees.
    // No changes should be required in the A1DynamicMem functions.
    // They should work seamlessly with the newly supplied implementation of BSTrees
    // and AVLTrees
    // For A2, implement the Defragment function for the class A2DynamicMem and test
    // using BSTrees and AVLTrees.
    // Your BST (and AVL tree) implementations should obey the property that keys in
    // the left subtree <= root.key < keys in the right subtree. How is this total
    // order between blocks defined? It shouldn't be a problem when using
    // key=address since those are unique (this is an important invariant for the
    // entire assignment123 module). When using key=size, use address to break ties
    // i.e. if there are multiple blocks of the same size, order them by address.
    // Now think outside the scope of the allocation problem and think of handling
    // tiebreaking in blocks, in case key is neither of the two.

    public int Allocate(int blockSize) {
        if(blockSize <= 0) return -1;
        // freeBlk, allocBlk are always head sentinels
        Dictionary target = freeBlk.Find(blockSize, false);// target memory to split
        // target is not one of the sentinels
        if(target == null) return -1;// memory not found

        if(target.size == blockSize) {
          
          allocBlk.Insert(target.address, blockSize, target.address);
          freeBlk.Delete(target);
          return target.address;
        }else {
          
          freeBlk.Insert(target.address + blockSize, target.size - blockSize, target.size - blockSize);
          allocBlk.Insert(target.address, blockSize, target.address);
          freeBlk.Delete(target);
          return target.address;
        }
    }

    public void Defragment() {
        if(type == 1) return;
        BSTree defrag;
        if(type == 2) {
            defrag = new BSTree();
        }else {
            defrag = new AVLTree();
        }
        Dictionary cur = freeBlk.getFirst();
        while (cur != null) {
            defrag.Insert(cur.address, cur.size, cur.address);
            cur = cur.getNext();
        }
        BSTree trav = defrag;// sentinel
        BSTree next1 = trav.getFirst();
        // assert next1 != trav;
        if (next1 == null) {
            //empty
            defrag = null;
            trav = null;
            return;
        }
        BSTree next2 = next1.getNext();
        while (next2 != null) {
            if (next1.address + next1.size == next2.address) {
                // merge next1 and next2
                int address1 = next1.address, address2 = next2.address, size1 = next1.size, size2 = next2.size;
                BSTree next1_for_freeBlk = new BSTree(address1, size1, size1);
                BSTree next2_for_freeBlk = new BSTree(address2, size2, size2);
                freeBlk.Delete(next1_for_freeBlk);
                freeBlk.Delete(next2_for_freeBlk);
               
                freeBlk.Insert(address1, size1 + size2, size1 + size2);
               

                next2.address = address1;
                next2.size = size1 + size2;
                next2.key = address1;

            }
            next1 = next2;
            next2 = next2.getNext();

        }

        defrag = null;
        trav = null;
        next1 = null;
        // System.out.println("DONE!");
        return;
    }

    // public static void main(String[] args) throws IOException {
    //     File file = new File("unofficial_tests/test3.txt");
    //     File file1 = new File("unofficial_tests/out3.txt");
    //     FileWriter fw = new FileWriter(file1.getAbsoluteFile());
    //     BufferedWriter bw = new BufferedWriter(fw);
    //     Scanner in;
    //     try {
    //         in = new Scanner(file);
    //     }catch(Exception e) {
    //         in = new Scanner(System.in);
    //     }
    //     int q = in.nextInt();
    //     while(--q >= 0) {
    //         int sz = in.nextInt();
    //         int n = in.nextInt();

    //         DynamicMem mem = new A2DynamicMem(sz, 2);
    //         while(--n >= 0) {
    //             String s = in.next();
    //             if(s.equals("Allocate")) {
    //                 int blockSize = in.nextInt();
    //                 int output = mem.Allocate(blockSize); 
    //                 bw.write(output + "\n");
    //                 // System.out.print(output + "\n");
    //             }else if (s.equals("Free")) {
    //                 int startAddr = in.nextInt();
    //                 int output = mem.Free(startAddr);
    //                 bw.write(output + "\n");
    //                 // System.out.print(output + "\n");
    //             }else if(s.equals("Defragment")) {
    //                 int dummy_token = in.nextInt();
    //                 // assert (dummy_token == 0);
    //                 mem.Defragment();
    //                 // System.out.println("null");
    //             }
    //             // System.out.print(n + " ");
    //             // Dictionary trav = mem.freeBlk.getFirst();
    //             // while(trav != null) {
    //             //     System.out.print(trav.address + " " + trav.size + " " + trav.key + " ; ");
    //             //     trav = trav.getNext();
    //             // }System.out.println("");
    //         }
    //     }
    //     in.close();
    //     bw.close();
    // }

}