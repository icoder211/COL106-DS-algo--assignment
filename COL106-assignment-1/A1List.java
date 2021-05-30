// Implements Dictionary using Doubly Linked List (DLL)
// Implement the following functions using the specifications provided in the class List

public class A1List extends List {

    private A1List  next; // Next Node
    private A1List prev;  // Previous Node

    public A1List(int address, int size, int key) {
        super(address, size, key);
    }

    public A1List(){
        super(-1,-1,-1);
        // This acts as a head Sentinel

        A1List tailSentinel = new A1List(-1,-1,-1); // Intiate the tail sentinel

        this.next = tailSentinel;
        tailSentinel.prev = this;
    }

    public A1List Insert(int address, int size, int key)
    {
        if(this.next == null) {
          // cannot insert at tail sentinel
          return null;
        }
        A1List node = new A1List(address, size, key);
        node.next = this.next;
        this.next.prev = node;
        node.prev = this;
        this.next = node;
        return node;
    }

    private boolean comp (Dictionary a, Dictionary b) {
      if(a.key == b.key) {
        if(a.address == b.address && a.size == b.size) {
          return true;
        }
      }
      return false;
    }

    /*
     * returns true iff something was deleted
    */
    private boolean del(A1List a) {
      if(a.prev == null || a.next == null) {
        // do nothing, this is a sentinel
        return false;
      }
      a.prev.next = a.next;
      a.next.prev = a.prev;
      a.next = null;
      a.prev = null;
      return true;
    }

    public boolean Delete(Dictionary d)
    {
      if(d == null) {
        return false;
      }
      if(!this.sanity()) {
        return false;
      }

      // A1List cur = this.getFirst();
      // while(cur != null) {
      //   if(cur.key == d.key) {
      //     if(cur.address == d.address && cur.size == d.size) {
      //       // delete this node and end function
      //       if(cur.prev == null || cur.next == null) {
      //         // this is impossible because DLL is wrapped by head and tail sentinels
      //         // some problem... will be caught by sanity
      //         return false;
      //       }
      //       cur.prev.next = cur.next;
      //       cur.next.prev = cur.prev;
      //       cur.next = null;
      //       cur.prev = null;
      //       return true;
      //     }
          
      //   }
      //   cur = cur.getNext();
      // }
      // return false;

        // here assumption is that DLL is sane
        A1List cur1 = this, cur2 = this;
        if(comp(cur1, d)) {
          del(cur1);
          return true;
        }
        cur1 = this.prev;
        cur2 = this.next;
        while(cur1 != null || cur2 != null) {
          // if these are sentinels, these won't be deleted
          if(cur1 != null) {
            if(comp(cur1, d)) {
              return del(cur1);
            }
            cur1 = cur1.prev;
          }
          if(cur2 != null) {
            if(comp(cur2, d)) {
              return del(cur2);
            }
            cur2 = cur2.next;
          }
        }
        return false;
    }

    public A1List Find(int k, boolean exact)
    {
        // here assumption is that DLL is sane
        A1List cur = this.getFirst();
        // cur CAN be null
        while(cur != null) {
          if(cur.key >= k) {
            if(!exact || cur.key == k) {
              return cur;
            }
          }
          cur = cur.getNext();
        }
        return null;
    }

    /*
     *returns null iff (empty DLL [or] loop detected while going towards the beginnning [or] head sentinel found is not "sane")
     */
    public A1List getFirst()
    {
        if(this.prev == null) {
          // "this" is/should be head sentinel
          if(!chkHead(this)) {
            return null;
          }
          if(this.next.next == null) {
            // thsis.next is/should be tail sentinel
            // empty DLL
            return null;
          }
          return this.next;
        }
        // performing an O(n) check if the DLL has a beginning by hare - tortoise
        // doesn't exhaustively check for a loop - that is done in sanity, here we just need a "beginning", with prev = null
        A1List a, b;
        a = this;
        b = this.prev;
        // a and b are not null now
        while (true) {
          a = a.prev;
          if(b.prev == null) {
            // b is the head sentinel
            if(!chkHead(b)) {
              return null;
            }
            // b.next can't be null as it started as a.prev, and moves twice as fast as "a"
            if(b.next.next == null) {
              // empty DLL
              // b is the head, b.next is the tail
              return null;
            }
            return b.next;// return first element of DLL
          }
          b = b.prev;
          if(b.prev == null) {
            // b is the head sentinel
            if(!chkHead(b)) {
              return null;
            }
            // b.next can't be null as it started as a.prev, and moves twice as fast as "a"
            if(b.next.next == null) {
              // empty DLL
              // b is the head, b.next is the tail
              return null;
            }
            return b.next;// return first element of DLL
          }
          b = b.prev;
          if(a == b) {
            // no beginning - loop detected
            return null;
          }
        }
        // if it has a beginning and not empty and head sentinel is "sane", then first element returned, else null
    }

    /*
     * returns next element of DLL
     * Does NOT return any sentinel
     * will return first element if Head.getNext() is called
    */
    public A1List getNext()
    {
        if(this.next == null || this.next.next==null) return null;
        return this.next;
    }

    private boolean chkHead(A1List head) {
      if(head == null) return false;
      if(head.prev != null) return false;
      if(head.address != -1 || head.key != -1 || head.size != -1) return false;
      if(head.next == null) return false;
      if(head.next.prev != head) return false;
      return true;
    }
    private boolean chkTail(A1List tail) {
      if(tail == null) return false;
      if(tail.next != null) return false;
      if(tail.address != -1 || tail.key != -1 || tail.size != -1) return false;
      if(tail.prev == null) return false;
      if(tail.prev.next != tail) return false;
      return true;
    }
    public boolean sanity()
    {
        // we will go forwards and backwards from "this"

        // go backwards
        A1List a = this;
        A1List b = this.prev;
        A1List head, tail;// to identify head and tail sentinels
        if(b == null) {
          // a is head sentinel
          if(!chkHead(a)) return false;
          head = a;
        }
        else {
          if(a == b) {
            // that's wierd, self loop
            return false;
          }
          if(b.next != a) {
            return false;
          }
          while(true) {
            // here invariant is a != b and from "this" till b.next, node.prev.next = node is satified, and no self loops and a.prev != null
            if(a.prev.next != a || a == a.prev) {
              // this is redundant
              return false;
            }
            if(b.prev == null) {
              // b is the head sentinel
              if(!chkHead(b)) return false;
              head = b;
              break;
            }
            if(b.prev.next != b || b == b.prev) return false;
            b = b.prev;
            if(b.prev == null) {
              // b is the head sentinel
              if(!chkHead(b)) return false;
              head = b;
              break;
            }
            if(b.prev.next != b || b == b.prev) return false;
            b = b.prev;

            if(a == b) {
              // loop detected
              return false;
            }
            
          }
        }

        // go forwards
        a = this;
        b = this.next;
        if(b == null) {
          // a is tail sentinel
          if(!chkTail(a)) return false;
          tail = a;
        }
        else {
          if(a == b) {
            // that's wierd, self loop
            return false;
          }
          if(b.prev != a) {
            return false;
          }
          while(true) {
            // here invariant is a != b and from "this" till b.prev, node.next.prev = node is satified, and no self loops and a.next != null
            if(a.next.prev != a || a == a.next) {
              // this is redundant
              return false;
            }
            if(b.next == null) {
              // b is the tail sentinel
              if(!chkTail(b)) return false;
              tail = b;
              break;
            }
            if(b.next.prev != b || b == b.next) return false;
            b = b.next;
            if(b.next == null) {
              // b is the tail sentinel
              if(!chkTail(b)) return false;
              tail = b;
              break;
            }
            if(b.next.prev != b || b == b.next) return false;
            b = b.next;

            if(a == b) {
              // loop detected
              return false;
            }
          }
        }
        if(!chkHead(head)) return false;// redundant
        if(!chkTail(tail)) return false;// redundant

        return true;
      }

}
