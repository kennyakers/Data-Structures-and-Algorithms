/**
 * Kenny Akers
 * Mr. Paige
 * Homework #3
 * 9/8/17
 */

import java.util.Iterator;

public class Homework_3 {

    private static enum Operation {
        APPEND,
        PREPEND,
        REMOVE,
        REVERSE,
        CONTAINS,
        GET,
        SIZE,
        PRINT,
        TNIRP
    }

    public static void main(String[] args) {
        DoublyLinkedList<String> list = new DoublyLinkedList<>();
        Operation operation = Operation.APPEND;
        
        for (String arg : args) {
            switch (arg) {
                case "size":
                    operation = Operation.SIZE;
                    break;

                case "get":
                    operation = Operation.GET;
                    continue;

                case "contains":
                    operation = Operation.CONTAINS;
                    continue;

                case "append":
                    operation = Operation.APPEND;
                    continue;

                case "prepend":
                    operation = Operation.PREPEND;
                    continue;

                case "remove":
                    operation = Operation.REMOVE;
                    continue;

                case "reverse":
                    operation = Operation.REVERSE;
                    break;

                case "print":
                    operation = Operation.PRINT;
                    break;

                case "tnirp":
                    operation = Operation.TNIRP;
                    break;

                default:
                    break;
            }

            switch (operation) {
                case SIZE:
                    System.out.println(list.size());
                    break;

                case GET:
                    try {
                        int index = Integer.parseInt(arg);
                        System.out.println(list.get(index));
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid index: " + arg);
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case CONTAINS:
                    System.out.println(list.contains(arg));
                    break;

                case APPEND:
                    list.append(arg);
                    break;

                case PREPEND:
                    list.prepend(arg);
                    break;

                case REMOVE:
                    list.remove(arg);
                    break;

                case REVERSE:
                    list.reverse();
                    break;

                case PRINT:
                    for (String s : list) {
                        System.out.println(s);
                    }
                    break;

                case TNIRP:
                    Iterator<String> iterator = list.reverseIterator();
                    while (iterator.hasNext()) {
                        System.out.println(iterator.next());
                    }
                    break;
            }
        }
    }
}
