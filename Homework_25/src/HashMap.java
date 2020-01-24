
import java.io.Console;
import java.math.BigInteger;

public class HashMap<Key, Value> {

    private boolean quadratic = false;  // Use quadratic probing
    private boolean exactSize = false;  // Don't round capacity up to next prime
    private double maxLoad = 0.5;    // Load factor at which table is resized
    private int compares = 0;      // Number of equality tests
    private int searches = 0;      // Number of search operations

    private int capacity;
    private int size;
    private Key[] keys;
    private Value[] values;

    private int nextPrime(int n) {  // Returns next prime number following n
        return BigInteger.valueOf(n).nextProbablePrime().intValue();
    }

    public HashMap(int capacity, boolean quadratic, boolean exactSize) {
        if (!exactSize) {
            capacity = nextPrime(capacity);
        }
        this.quadratic = quadratic;
        this.exactSize = exactSize;

        this.keys = (Key[]) new Object[capacity];
        this.values = (Value[]) new Object[capacity];
        this.capacity = capacity;
        this.size = size;
    }

    public HashMap(int capacity) {
        this(capacity, false, false);
    }

    public HashMap() {
        this(16, false, false);
    }

    public int capacity() {
        return this.capacity;
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    private int hash(Key key) {
        return (key.hashCode() & 0x7FFFFFFF) % this.capacity;
    }

    private void resize(int capacity) {
        Key[] oldKeys = this.keys;
        Value[] oldValues = this.values;

        this.keys = (Key[]) new Object[capacity];
        this.values = (Value[]) new Object[capacity];
        this.capacity = capacity;
        this.size = 0;

        for (int i = 0; i < oldKeys.length; i++) {
            if (oldKeys[i] != null) {
                add(oldKeys[i], oldValues[i]);
            }
        }
    }

    private void resize() {
        if (this.exactSize) {
            resize(2 * this.capacity);
        } else {
            resize(nextPrime(2 * this.capacity));
        }
    }

    private int locate(Key key) {
        int index = hash(key);
        int step = 1;

        while (this.keys[index] != null && !this.keys[index].equals(key)) {
            index = Math.abs(index + step) % this.capacity;
            if (this.quadratic) {
                step *= 2;
            }
            this.compares++;
        }

        this.compares++;
        this.searches++;

        return index;
    }

    public boolean contains(Key key) {
        int index = locate(key);
        return this.keys[index] != null;
    }

    public Value find(Key key) {
        int index = locate(key);
        return this.values[index];
    }

    public void add(Key key, Value value) {
        if (loadFactor() > this.maxLoad) {
            resize();
        }

        int index = locate(key);
        if (this.keys[index] == null) {
            this.keys[index] = key;
        }
        this.values[index] = value;
        this.size++;
    }

    public void remove(Key key) {
        int index = hash(key);
        int step = 1;

        while (keys[index] != null & !keys[index].equals(key)) {
            index = (index + step) % this.capacity;
            if (this.quadratic) {
                step *= 2;
            }
        }

        keys[index] = null;
        values[index] = null;
        this.size--;

        index = (index + step) % this.capacity;
        if (this.quadratic) {
            step *= 2;
        }

        while (keys[index] != null) {
            Key savedKey = keys[index];
            Value savedValue = values[index];
            keys[index] = null;
            values[index] = null;
            this.size--;

            index = (index + step) % this.capacity;
            if (this.quadratic) {
                step *= 2;
            }

            add(savedKey, savedValue);
        }
    }

    public void print() {
        for (int i = 0; i < this.capacity; i++) {
            System.out.print(i);
            System.out.print(": ");
            if (keys[i] != null) {
                System.out.print(keys[i]);
                System.out.print(" => ");
                System.out.print(values[i]);
            }
            System.out.println();
        }
    }

    private int next(int index) {
        return (++index < this.capacity) ? index : 0;
    }

    private int nextCluster(int index) {
        while (this.keys[index] != null) {
            index = next(index);
        }
        while (this.keys[index] == null) {
            index = next(index);
        }
        return index;
    }

    private int clusterLength(int start) {
        int index = start;
        while (this.keys[index] != null) {
            index = next(index);
        }
        if (index >= start) {
            return index - start;
        } else {
            return index + this.capacity - start;
        }
    }

    public int numberClusters() {
        int count = 0;

        if (this.size > 0) {
            int start = nextCluster(0);
            int index = start;

            do {
                index = nextCluster(index);
                count++;
            } while (index != start);
        }

        return count;
    }

    public int largestClusterSize() {
        int max = 0;

        if (this.size > 0) {
            int start = nextCluster(0);
            int index = start;

            do {
                int length = clusterLength(index);
                if (length > max) {
                    max = length;
                }
                index = nextCluster(index);
            } while (index != start);
        }
        return max;
    }

    public double averageClusterSize() {
        int total = 0;
        int count = 0;

        if (this.size > 0) {
            int start = nextCluster(0);
            int index = start;

            do {
                total += clusterLength(index);
                index = nextCluster(index);
                count++;
            } while (index != start);
        }

        if (count > 0) {
            return (double) total / (double) count;
        } else {
            return 0.0;
        }
    }

    public double loadFactor() {
        if (this.capacity > 0) {
            return (double) this.size / (double) this.capacity;
        } else {
            return 0.0;
        }
    }

    public int searches() {
        return this.searches;
    }

    public int compares() {
        return this.compares;
    }

    public double averageCompares() {
        if (this.searches > 0) {
            return (double) this.compares / (double) this.searches;
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
        System.out.println("Load factor = " + this.loadFactor());
        System.out.println("Number clusters = " + this.numberClusters());
        System.out.println("Average cluster size = " + this.averageClusterSize());
        System.out.println("Largest cluster size = " + this.largestClusterSize());
        System.out.println("Average compares per search = " + this.averageCompares());
    }

    public static void main(String[] args) {
        HashMap<String, String> map = null;
        Console console = System.console();
        boolean statistics = false;
        boolean quadratic = false;
        boolean exact = false;
        int capacity = 16;
        double load = 0.5;
        String option = "";

        if (console == null) {
            System.err.println("No console");
            return;
        }

        // Command line options:
        //
        //     -prime		  Round capacity up to the next prime number
        //	   -exact		  Don't round capacity up to the next prime
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
                    exact = false;
                    continue;

                case "-exact":
                    exact = true;
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
                        map = new HashMap<String, String>(capacity, quadratic, exact);
                        map.maxLoad = load;
                    }
                    map.add(arg, "");
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
            map = new HashMap<String, String>(capacity, quadratic, exact);
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

                case "contains":
                    System.out.println(map.contains(getArgument(line, 1)));
                    break;

                case "find":
                    System.out.println(map.find(getArgument(line, 1)));
                    break;

                case "add":
                case "insert":
                    map.add(getArgument(line, 1), getArgument(line, 2));
                    break;

                case "delete":
                case "remove":
                    map.remove(getArgument(line, 1));
                    break;

                case "print":
                    map.print();
                    break;

                case "clear":
                    map = new HashMap<>(capacity, quadratic, exact);
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
                    exact = false;
                    break;

                case "exact":
                    exact = true;
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
