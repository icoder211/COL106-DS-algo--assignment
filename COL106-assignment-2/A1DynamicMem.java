
// Class: A1DynamicMem
// Implements DynamicMem
// Does not implement defragment (which is for A2).

public class A1DynamicMem extends DynamicMem {

    public A1DynamicMem() {
        super();
    }

    public A1DynamicMem(int size) {
        super(size);
    }

    public A1DynamicMem(int size, int dict_type) {
        super(size, dict_type);
    }

    public void Defragment() {
        return ;
    }

    // In A1, you need to implement the Allocate and Free functions for the class A1DynamicMem
    // Test your memory allocator thoroughly using Doubly Linked lists only (A1List.java).

    public int Allocate(int blockSize) {
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

    public int Free(int startAddr) {
        Dictionary target = allocBlk.Find(startAddr, true);//target address to free, with this address
        // target is not one of the sentinels
        if(target == null) return -1;// nothing found
        freeBlk.Insert(target.address, target.size, target.size);
        allocBlk.Delete(target);
        return 0;
    }
}
