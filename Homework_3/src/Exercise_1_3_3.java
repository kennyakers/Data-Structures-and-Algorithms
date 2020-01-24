
import java.util.Stack;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author kakers
 */
public class Exercise_1_3_3 {

    public static boolean isValid(int[] values) {
        Stack<Integer> stack = new Stack<Integer>();
        
        int index = 0; // Index variable for @param values.
        int i = 0; // Number/counter to put on stack if stack is either empty (all have been popped) or the value in @param values is not the next thing on the stack.

        System.out.print("\nActually printed: ");
        
        while (index < values.length && i <= values.length) {
            if (!stack.isEmpty() && stack.peek() == values[index]) { // If the top thing on the stack is the same value as the one at that place in @param values...
                System.out.print(stack.pop() + " "); // Pop it off the stack.
                index++; // Go to the next spot.
            } else { // The next value in @param values was not the one on the stack or the stack is empty
                if (i < values.length) {
                    stack.push(i++); // Put i on the stack
                }
            }
        }
        System.out.println("");

        return index == values.length && stack.isEmpty(); // Return true (valid sequence) only if 
    }

    public static void main(String[] args) {
        
        int[] values = new int[args.length];
        
        System.out.print("Original sequence: ");
        
        int i = 0;
        for (String arg : args) {
            values[i] = Integer.parseInt(arg);
            System.out.print(values[i++] + " ");
        }
        System.out.println(isValid(values));
    }

}
