import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.lang.Integer.max;
import static java.lang.Integer.min;

/**
 * For complexity analysis, let m be this.max and n be the number of elements in this set.
 */

public class nSet {
    // this class implements the set operations over sets of natural numbers.
    public int Max;        // maximal natural number in any set
    private int n_long;    // the number of long integers: 64*n_long > Max
    private long[] store;  // the store has n_long longs
    private int size;      // remember the size of the current set

    // Constructor: runtime = O(m), with a small constant
    public nSet(int n) {
        this.Max = n;
        if (n <= 0) { this.Max = 1; }
        this.n_long = (n >> 6) + 1; // n_long = n/64+1, number of longs
        this.store = new long[n_long];
        for (int i = 0; i<this.n_long; i++) this.store[i] = 0;
        this.size = 0;   // the empty set:
    }

    //Constant runtime
    public void add(int x) {
        // add x into the set
        if (x < 0 || x > this.Max) return;
        int i = (x>>6);     // i = x/64
        int j = x - (i<<6); // j = x % 64, i.e., 64i + j = x
        long y = this.store[i];
        if (((y>>>j) & 1) == 1) return;   // if x is present, do nothing
        this.store[i] |= ((long) 1<<j);  // "|" is the bitwise OR operation
        this.size++;
    }


    //Constant runtime
    public boolean find(int x) {
        // decide if x is in the set
        if (x < 0 || x > this.Max) return false;
        int i = (x>>6);     // i = x/64
        int j = x - (i<<6); // j = x % 64, i.e., 64i + j = x
        long y = this.store[i];
        return ((y>>>j) & 1) == 1; // "&" is the bitwise OR operation
    }


    //O(m) linear time, with a small constant
    public void clear () {
        for(int i = 0; i<this.n_long; i++) store[i]=0;
        this.size = 0;
    }


    // Constant runtime
    public int size () {
        return this.size;
    }


    // O(m) linear time
    private void set_size () {
        int counter = 0;
        for(int i = 0; i<this.n_long; i++) {
            for (int j = 0; j < 64; j++) {
                if(((this.store[i]>>>j) & 1) == 1)
                    counter++;
            }
        }
        this.size = counter;
    }

    // Constant runtime
    public void print () {
        // print up to 30 numbers in the current nSet
        System.out.print("{ ");
        int count = 0;
        for(int i=0; i<this.n_long; i++)
            for(int j=0; j<64 ; j++)
                if (((this.store[i] >>> j) & 1) == 1) {
                    System.out.print(((i << 6) + j)+", ");
                    if (++count >= 30) {
                        System.out.println("... }");
                        return;
                    }
                }
        System.out.println("}");
    }


    // O(m) runtime
    public nSet union (nSet X) {
        int maximum = max(this.Max, X.Max);
        nSet A = new nSet(maximum);

        for(int i=0 ;i < n_long; i++) {
            A.store[i] = this.store[i] | X.store[i];
        }

        A.set_size();
        return A;
    }


    // You need to complete the implementation of the following operations:

    public boolean isEmpty () {
        // return true iff the current nSet is empty
        if(store.length == 0){
            return true;
        }
        return false;

    }

    public boolean delete (int x) {
        // return false if x isn't in the set;
        // delete the number x from the current set and return true.
        if (x < 0 || x > this.Max) return false;
        int i = (x>>6);     // i = x/64
        int j = x - (i<<6); // j = x % 64, i.e., 64i + j = x
        long y = this.store[i];
        if (((y>>>j) & 1) == 1) {// if x is present, do nothing
            this.store[i] = 0;
            return true;
        }
        return false;
    }


	public nSet intersect (nSet X) {
	   // return a new nSet which is the intersection of the current nSet and X
       if(X.isEmpty()|| this.isEmpty()){return null;}
       int maximum = max(this.Max, X.Max);
       nSet Y = new nSet(maximum);
       for(int i= 0; i< n_long; i++){
           Y.store[i] = this.store[i] & X.store[i];
       }
       Y.set_size();
       return Y;

	}

    public nSet subtract (nSet X) {
	   // return a new nSet which is the subtraction of the current nSet by X
       if(X.isEmpty() || this.isEmpty()) {return null;}
       int maximum = max(this.Max, X.Max);
       nSet Y = new nSet(maximum);
       for(int i= 0; i< n_long; i++){
           Y.store[i] = this.store[i] ^ X.store[i];
       }
       Y.set_size();
       return Y;
	}

    public nSet complement() {
	   // return a new nSet which is the complement of the current nSet
        nSet Y = new nSet(Max);
        for(int i = 0; i< n_long; i++){
            Y.store[i] = ~this.store[i];
        }
        Y.set_size();
        return Y;
	}

	public boolean equal(nSet X) {
        // return true iff X and the current nSet contain the same set of numbers
        //int maximum = max(this.Max, X.Max);
        nSet Y = this.intersect(X);
        Y.set_size();
        Y.print();
        this.print();
        if (this.size == Y.size) {
            return true;
        }
        return false;
    }


	public boolean isSubset(nSet X) {
	   // return true iff X is a subset of the current nSet
       if(intersect(X) != null){
           return true;
       }
       else return false;
	}

	public int[] toArray () {
	   // return an int array which contains all the numbers in the current nSet
        int[] newArray = new int[size];
        for(int i=0; i<this.n_long; i++)
            for(int j=0; j<64 ; j++)
                if (((this.store[i] >>> j) & 1) == 1) {
                    newArray[i] = ((i << 6)+ j);
                    }
        return newArray;
    }

	public void enumerate() {
	   // Enumerate all subsets of the current nSet and print them out.
	   // You may assume the current nSet contains less than 30 numbers.
        for(int i=0; i<this.n_long; i++)
            for(int j=0; j<64 ; j++)
                if (((this.store[i] >>> j) & 1) == 1) {
                    System.out.print(((i << 6) + j)+", ");

                    }

	}



    public static void main(String[] args) {
        // testing
        nSet A = new nSet(1000);

        for (int i = 1; i<A.Max; i += i) {
            A.add(i-1);
            A.add(i);
            A.add(i+1);
        }

        for (int i = 0; i<=A.Max; i++) {
            if (A.find(i)) System.out.println("found " + i + " in A");
        }


        // more testing code
        nSet B = new nSet(1000); // all natural numbers <= 1000, is a power of 2
        for(int j = 0; j<= 1000;j++){
            if(j >0 && ((j & (j-1)) == 0)){
                B.add(j);
            }
        }

        nSet C = new nSet(1000); // all odd natural numbers <= 1000
        for(int q = 0; q<=1000;q++){
            if(q % 2 == 1){
                C.add(q);
            }


        }
        nSet D = C.complement(); //was C complement

        nSet E = D.union(B);

        if (D.equal(E))
            System.out.println("D is equal to E");
        else
            System.out.println("D is not equal to E");

        nSet F = A.intersect(B);

        if (B.equal(F))
            System.out.println("B is equal to F");
        else
            System.out.println("B is not equal to F");

        nSet G = new nSet(1000); // all natural numbers <= 1000, and divisible by 8
        for(int z= 0; z<= 1000; z++){
            if(z % 8 == 0){
                G.add(z);
            }
        }

        nSet H = A.intersect(G);
        if (G.equal(H))
            System.out.println("G is equal to H");
        else
            System.out.println("G is not equal to H");

        nSet I = G.subtract(D);
        if (I.isEmpty())
            System.out.println("I is empty");
        else
            System.out.println("I is not empty");

        nSet J = H.intersect(E);

        nSet K = H.complement();

        // print out the sizes and members of A, B, C, D, E, F, G, H, I, J, K
        System.out.print(A.size() + " is the size of A = "); A.print();
        System.out.print(B.size() + " is the size of B = "); B.print();
        System.out.print(C.size() + " is the size of C = "); C.print();
        System.out.print(D.size() + " is the size of D = "); D.print();
        System.out.print(E.size() + " is the size of E = "); E.print();
        System.out.print(F.size() + " is the size of F = "); F.print();
        System.out.print(G.size() + " is the size of G = "); G.print();
        System.out.print(H.size() + " is the size of H = "); H.print();
        System.out.print(I.size() + " is the size of I = "); I.print();
        System.out.print(J.size() + " is the size of J = "); J.print();
        System.out.print(K.size() + " is the size of K = "); K.print();

        System.out.println("All subsets of H:");
        H.enumerate();


        System.out.print("My own tests");


        nSet Z = new nSet(5);
        Z.add(1);
        Z.add(2);
        Z.add(3);
        Z.add(4);
        Z.add(5);
        System.out.print("this is Z");
        Z.print();

        nSet P = new nSet(5);
        P.add(1);
        P.add(2);
        P.add(3);
        P.add(4);
        System.out.print("this is P");
        P.print();
        nSet V = Z.intersect(P);
        System.out.print("this is V");
        V.print();
        System.out.print("results of Z = P");
        System.out.print(Z.equal(P));

    }
}
