
/**
 * Kenny Akers
 * Mr. Paige
 * Homework #16
 * 2/2/18
 */
import java.io.Console;
import java.math.BigInteger;

public class HashMap<Key, Value> {

    private class Pair {

        public Key key;
        public Value value;

        public Pair(Key key, Value value) {
            this.key = key;
            this.value = value;
        }
    }

    private boolean quadratic = false;  // Use quadratic probing
    private boolean primeSize = false;  // Don't round capacity up to next prime
    private double maxLoad = 0.5;    // Load factor at which table is resized
    private int numValues = 0;
    private double loadFactor = 0;
    private int numSearches = 0;
    private int numCompares = 0;

    private Pair[] hashMap;

    private int nextPrime(int n) {  // Returns next prime number following n
        return BigInteger.valueOf(n).nextProbablePrime().intValue();
    }

    public HashMap(int capacity, boolean quadratic, boolean primeSize) {
        if (primeSize) {
            capacity = nextPrime(capacity);
        }
        this.quadratic = quadratic;
        this.primeSize = primeSize;
        hashMap = new HashMap.Pair[capacity]; // To get around generic array creation error.
    }

    public HashMap(int capacity) {
        this(capacity, false, false);
    }

    public HashMap() {
        this(16, false, false);
    }

    public int capacity() {
        // Size of the hash table.
        return this.hashMap.length;
    }

    public int size() {
        return this.numValues;
    }

    public boolean isEmpty() {
        return this.numValues == 0;
    }

    public int hash(Key key) {
        // Returns the index of the key in the table
        // (provided that there are no collisions).
        return (key.hashCode() & 0x7FFFFFFF) % hashMap.length;
    }

    public boolean contains(Key key) {
        // Does this hash map contain this key?
        this.numSearches++;
        int index = this.hash(key); // Place to start looking.
        int start = index;
        int step = 1;
        do {
            if (this.hashMap[index] == null) { // If the current slot is null.
                index = (index + step) % this.hashMap.length; // Increment, wrap around if needed.
                if (this.quadratic) { // Quadratic probing seems to yield negative values for "index". Which means that: 
                                      // 1: key hashes to a negative value (unlikely).
                                      // 2: Index grows obscenely large and overflows (more likely).
                                      // I think this growth could be due to the condition problem described on line 100.
                    step *= 2;
                }
            } else {
                this.numCompares++; // Accounts for the following .equals() statement. However this does yield massive compare numbers.
                if (this.hashMap[index].key.equals(key)) {
                    return true; // Found it.
                }
                index = (index + step) % this.hashMap.length; // Increment, wrap around if needed.
                if (this.quadratic) {
                    step *= 2;
                }
            }
        } while (index != start); // While not back at where we started
        // ^^^ This condition only accounts for linear probing, because quadratic probing allows skipping over the original
        // start point (as I understand it). How do we detect when we've completed a full pass and thus know when to
        // stop looping?

        return false;
    }

    private Pair findPair(Key key) {
        // The value associated with this key.
        // Returns null if not in the map.

        if (!this.contains(key)) { // If key is not in the map...
            return null;
        }

        int hash = this.hash(key);
        int start = hash;
        int step = 1;
        this.numSearches++;
        this.numCompares++; // Accounts for first compare.

        while (hash != start && !this.hashMap[hash].key.equals(key)) { // While we haven't wrapped back to the start and we haven't found the key
            this.numCompares++; // For subsequent compares.
            hash = ++hash % this.hashMap.length; // Go to the next spot, wrapping around if needed.
            // Need to do quadratic probing
        }
        return this.hashMap[hash];
    }

    public Value find(Key key) {
        return this.findPair(key).value;
    }

    public void add(Key key, Value value, boolean resizing) {
        // Adds a key/value pair to the map.
        // Resizes the map if the load factor exceeds 50%
        this.numSearches++;
        int hash = this.hash(key);
        int step = 1;
        if (this.contains(key)) { // If key is in the map, it updates the value.
            this.hashMap[hash] = new Pair(key, value);
            this.numValues--;
        } else { // Otherwise, the key is not in the map
            while (this.hashMap[hash] != null) {
                hash = (hash + step) % this.hashMap.length; // Increment, wrap around if needed.
                if (this.quadratic) {
                    step *= 2;
                }
            }
            this.hashMap[hash] = new Pair(key, value); // Add the pair.
        }

        this.loadFactor = ((double) ++this.numValues) / ((double) this.hashMap.length); // Recalculate the load factor.
        // Need to resize (the !resizing ensures that the call to add() 
        // in resize() doesn't end up calling itself (which would happen 
        // when the load factor was still over the threshold)).
        if (this.loadFactor > this.maxLoad && !resizing) {
            this.resize();
        }

    }

    private void resize() {
        int newCapacity = this.primeSize ? this.nextPrime(this.hashMap.length * 2) : this.hashMap.length * 2;
        // Save the current contents of the array and
        // allocate a new one with the desired capacity.
        Pair[] copy = this.hashMap;
        this.hashMap = new HashMap.Pair[newCapacity];

        for (int i = 0; i < copy.length; i++) {
            if (copy[i] != null) {
                this.numValues--; // Since we're not adding anything, negate the ++this.numValues in add()
                this.add(copy[i].key, copy[i].value, true);
            }
        }
        this.loadFactor = ((double) this.numValues) / ((double) this.hashMap.length);
    }

    public void remove(Key key) {
        // Removes a key/value pair from the map.
        // Does nothing if the key is not in the map.
        Pair pairToRemove = this.findPair(key);
        if (pairToRemove == null) {
            return;
        }

        int indexOfPair = this.hash(key); // Place to start.
        while (!this.hashMap[indexOfPair].key.equals(key)) { // Loop to find its actual index.
            indexOfPair++;
        }

        this.hashMap[indexOfPair] = null; // Remove it from the table.

        for (int i = 0; i < indexOfPair; i++) { // Rehash above items.
            if (this.hashMap[i] != null) {
                this.add(this.hashMap[i].key, this.hashMap[i].value, false);
            }
        }
    }

    public void print() {
        // Prints the contents of the hash map.
        // Format should be nnn: kkk => xxx
        // Where nnn is the slot number,
        // kkk is the key and xxx is the value.
        for (int i = 0; i < this.hashMap.length; i++) {
            System.out.print(i + ": ");
            if (this.hashMap[i] == null) {
                System.out.println("");
                continue;
            }
            System.out.println(hashMap[i].key + " => " + hashMap[i].value);
        }
    }

    public int numberClusters() {
        // The number of clusters in the hash map.
        // A cluster is a set of consecutive non-null
        // entries in the hash map.  Be careful about 
        // wrap around.
        int count = 0;
        for (int i = 0; i < this.hashMap.length - 1; i++) { // Iterate over table
            if (this.hashMap[i] != null && this.hashMap[i + 1] == null) { // Count clusters
                count++;
            }
        }
        // If there is one lone cluster at the end, but nothing in the first slot (wrap around), account for it.
        if (this.hashMap[this.hashMap.length - 1] != null && this.hashMap[0] == null) {
            count++;
        }

        return count;
    }

    public int largestClusterSize() {
        // This method is intended to find the first non-null slot, mark it, and start iterating over the cluster
        // until we find its end (the next null occurance). However, I believe that my wrap around statement 
        // conflicts with the i++ in the for loop.
        int begin = 0;
        int maxSize = 0;
        for (int i = 0; i < this.hashMap.length; i++) {
            if (this.hashMap[i] != null) {
                begin = i; // Mark where the cluster began.
                while (this.hashMap[i] != null) { // This while loop sometimes loops infinitely, which shouldn't be possible given the load factor should never be 1.
                    i = ++i % this.hashMap.length; // Go to the next null, with wrap around
                }
                maxSize = Math.max(i - begin, maxSize); // Record the biggest cluster using the distance between the start and finish points of the cluster.
            }
        }
        return maxSize; 
    }

    public double averageClusterSize() {
        // The average number of slots in a cluster.
        
        // This method is the same as largestClusterSize() except that it sums the distances instead of maximizing them.
        int begin = 0;
        int sum = 0;
        for (int i = 0; i < this.hashMap.length; i++) {
            if (this.hashMap[i] != null) {
                begin = i; // Mark where the cluster began.
                while (this.hashMap[i] != null) {
                    i = ++i % this.hashMap.length; // Go to the next null, with wrap around
                }
                sum += (i - begin); // Record the biggest cluster.
            }
        }
        return ((double) sum) / ((double) this.numberClusters());
    }

    public double loadFactor() {
        return this.loadFactor;
    }

    public int searches() {
        // Return the number of searches (hash table lookups).
        // Includes searches for contains, find, add and remove.
        return this.numSearches;
    }

    public int compares() {
        // Return the total number of equality tests on keys performed.
        return this.numCompares;
    }

    public double averageCompares() {
        // Average number of equality tests per search.
        int searches = this.searches();
        int compares = this.compares();
        if (searches > 0) {
            return (double) compares / (double) searches;
        } else {
            return 0.0;
        }
    }

    private static String getArgument(String line, int index) {
        String[] words = line.split("\\s");
        return words.length > index ? words[index] : "";
    }

    private static String getCommand(String line) {
        return getArgument(line, 0);
    }

    private void printStatistics() {
        System.out.println("Size = " + this.size());
        System.out.println("Capacity = " + this.capacity());
        System.out.println("Searches = " + this.searches());
        System.out.println("Compares = " + this.compares());
        System.out.print("Load factor = ");
        System.out.printf("%.2f", this.loadFactor);
        System.out.println("\nNumber clusters = " + this.numberClusters());
        System.out.print("Average cluster size = ");
        System.out.printf("%.2f", this.averageClusterSize());
        System.out.println("\nLargest cluster size = " + this.largestClusterSize());
        System.out.print("Average compares per search = ");
        System.out.printf("%.2f", this.averageCompares());
        System.out.println("");
    }

    public static void main(String[] args) {
        HashMap<String, String> map = null;
        Console console = System.console();
        boolean statistics = false;
        boolean quadratic = false;
        boolean prime = true;
        int capacity = 16;
        double load = 0.5;
        String option = "";

        if (console == null) {
            System.err.println("No console");
            return;
        }

        // Command line options:
        //
        //     -prime         Round capacity up to the next prime number
        //     -exact         Don't round capacity up to the next prime
        //     -linear        Use linear probing
        //     -quadratic     Use quadric probing
        //     -capacity <n>  Initial capacity of the hash table
        //     -load <x>      Load factor at which table is resized
        //     -statistics    Print statistics when done
        //     key			  (key, "") is added to the map
        //
        for (String arg : args) {
            switch (arg) {
                case "-prime":
                    prime = true;
                    continue;

                case "-exact":
                    prime = false;
                    continue;

                case "-linear":
                    quadratic = false;
                    continue;

                case "-quadratic":
                    quadratic = true;
                    continue;

                case "-capacity":
                    option = arg;
                    continue;

                case "-load":
                    option = arg;
                    continue;

                case "-statistics":
                    statistics = true;
                    continue;

                default:
                    break;
            }

            switch (option) {
                case "-capacity":
                    try {
                        capacity = Integer.parseInt(arg);
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid capacity: " + arg);
                        continue;
                    }
                    break;

                case "-load":
                    try {
                        load = Double.parseDouble(arg);
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid load factor: " + arg);
                        continue;
                    }
                    break;

                default:
                    if (map == null) {
                        map = new HashMap<String, String>(capacity, quadratic, prime);
                        map.maxLoad = load;
                    }
                    map.add(arg, "", false);
                    break;
            }
            option = "";
        }

        // Allow the user to enter commands on standard input:
        //
        //   hash <key>        prints the hash index for a given key
        //   contains <key>    prints true if a key is in the map; false if not
        //   find <key>        prints the value associated with the key
        //   add <key> <value> adds an item to the tree
        //   remove <key>      removes an item from the tree (if present)
        //   clear             removes all items from the tree
        //   print             prints the contents of the hash table
        //   clusters		   prints the number of clusters in the map
        //   average		   prints the average cluster size
        //   largest           prints the largest cluster size
        //   load              prints the load factor
        //   linear            use linear probing**
        //   quadratic         use quadratic probing**
        //   prime			   round up capacity to a prime**
        //   exact			   use exact capacity (don't round up to a prime)**
        //   exit              quit the program
        //
        //   ** takes effect on the next clear command.
        if (map == null) {
            map = new HashMap<String, String>(capacity, quadratic, prime);
            map.maxLoad = load;
        }

        String line = console.readLine("Command: ").trim();
        while (line != null) {
            String command = getCommand(line);

            switch (command) {
                case "hash":
                    System.out.println(map.hash(getArgument(line, 1)));
                    break;

                case "size":
                    System.out.println(map.size());
                    break;

                case "capacity":
                    System.out.println(map.capacity());
                    break;

                case "clusters":
                    System.out.println(map.numberClusters());
                    break;

                case "contains":
                    System.out.println(map.contains(getArgument(line, 1)));
                    break;

                case "load":
                    System.out.printf("%.2f", map.loadFactor);
                    System.out.println("");
                    break;

                case "find":
                    System.out.println(map.find(getArgument(line, 1)));
                    break;

                case "add":
                case "insert":
                    map.add(getArgument(line, 1), getArgument(line, 2), false);
                    break;

                case "delete":
                case "remove":
                    map.remove(getArgument(line, 1));
                    break;

                case "print":
                    map.print();
                    break;

                case "clear":
                    map = new HashMap<>(capacity, quadratic, prime);
                    break;

                case "stats":
                case "statistics":
                    map.printStatistics();
                    break;

                case "linear":
                    quadratic = false;
                    break;

                case "quadratic":
                    quadratic = true;
                    break;

                case "prime":
                    prime = true;
                    break;

                case "exact":
                    prime = false;
                    break;

                case "end":
                case "exit":
                case "quit":
                    if (statistics) {
                        map.printStatistics();
                    }
                    return;

                default:
                    System.out.println("Invalid command: " + command);
                    break;
            }

            line = console.readLine("Command: ").trim();
        }
    }
}
