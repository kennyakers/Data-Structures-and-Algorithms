
/**
 * Kenny Akers
 * Mr. Paige
 * Homework #4
 * 9/15/17
 */
public class Homework_4 {

    private static enum Operation {
        CAPACITY,
        ENQUEUE,
        DEQUEUE,
        EMPTY,
        HEAD,
        PRINT
    }

    public static void main(String[] args) {
        int capacity = 4;
        Operation operation = Operation.ENQUEUE;
        Queue<String> queue = new ArrayQueue<String>(capacity);

        for (String arg : args) {
            switch (arg) {
                case "capacity":
                    operation = Operation.CAPACITY;
                    continue;

                case "+":
                case "enq":
                case "enqueue":
                    operation = Operation.ENQUEUE;
                    continue;

                case "-":
                case "deq":
                case "dequeue":
                    operation = Operation.DEQUEUE;
                    break;

                case "head":
                    operation = Operation.HEAD;
                    break;

                case "empty":
                    operation = Operation.EMPTY;
                    break;

                case "print":
                    operation = Operation.PRINT;
                    break;

                default:
                    break;
            }

            try {
                switch (operation) {
                    case CAPACITY:
                        try {
                            capacity = Integer.parseInt(arg);
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid capacity: " + e.getMessage());
                            continue;
                        }
                        queue = new ArrayQueue<String>(capacity);
                        break;

                    case ENQUEUE:
                        queue.enqueue(arg);
                        break;

                    case DEQUEUE:
                        System.out.println(queue.dequeue());
                        break;

                    case EMPTY:
                        System.out.println(queue.isEmpty());
                        break;

                    case HEAD:
                        System.out.println(queue.head());
                        break;

                    case PRINT:
                        System.out.println(queue);
                        break;

                    default:
                        break;
                }

            } catch (Queue.EmptyException | Queue.UnderflowException | Queue.OverflowException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
