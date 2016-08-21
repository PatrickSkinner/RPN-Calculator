import java.util.*;
import java.io.*;
 
/**
 *A calculator that uses Reverse Polish Notation for calculations.
 *
 * Cosc241 assignment 2015.
 *
 * @author Patrick Skinner, Cassidy Mowat
 */
public class RPNApp {
     
    /** A stack used for holding operators and operands.*/
    private static Stack<Long> rpnStack;
   
    /** Set true if an error is found.*/
    private static boolean errorFlag;
   
    /** Create a new stack and set the error flag.*/
    private static void createStack(){
        rpnStack = new Stack<Long>();
        errorFlag = false;
    }
 
    /**
     * A method to take input from the user and push it to the stack.
     * Or call the performOperation method to calculate the result.
     *
     * @param inputExpression the string given by the user.
     */
    private static void parseInput(String inputExpression){
        StringTokenizer st = new StringTokenizer(inputExpression);
       
        while(st.hasMoreTokens()){
            String token = st.nextToken();
            try{
                // If number found push to the stack.
                rpnStack.push(Long.parseLong(token)); 
            } catch (NumberFormatException ex){
                if(token.equals("(")){ 
                    // If open bracket check for too few operands error.
                    if(rpnStack.empty()){
                        System.out.println("Error: too few operands");
                        errorFlag = true;
                    }
                    // Call to method for handling brackets.
                    parseBrackets(st);
                    return;
                }else{ // If operator find appropriate method in switch.
                    performOperation(token);
                }
            }
        }
    }
     
    /**
     * A method to check the token given and perform the needed calculation.
     *
     * @param token that will be used for calculation.
     */
    private static void performOperation(String token){
        long op1 = 0;
        long op2 = 0;
        switch (token){
            case "+": // Addition of top two operands in the stack.
                if(rpnStack.size() < 2){
                    System.out.println("Error: too few operands");
                    errorFlag = true;
                    break;
                }
                op2 = rpnStack.pop();
                op1 = rpnStack.pop();
                rpnStack.push(op1+op2);
                break;
               
            case "-": // Subtraction of top two operands in the stack.
                if(rpnStack.size() < 2){
                    System.out.println("Error: too few operands");
                    errorFlag = true;
                    break;
                }
                op2 = rpnStack.pop();
                op1 = rpnStack.pop();
                rpnStack.push(op1-op2);
                break;
               
            case "*": // Multiplication of top two operands in the stack.
                if(rpnStack.size() < 2){
                    System.out.println("Error: too few operands");
                    errorFlag = true;
                    break;
                }
                op2 = rpnStack.pop();
                op1 = rpnStack.pop();
                rpnStack.push(op1*op2);
                break;
               
            case "/": // Division of top two operands in the stack.
                if(rpnStack.size() < 2){
                    System.out.println("Error: too few operands");
                    errorFlag = true;
                    break;
                }
                op2 = rpnStack.pop();
                if(op2 == 0){
                    System.out.println("Error: division by 0");
                    errorFlag = true;
                    break;
                }
                op1 = rpnStack.pop();
                rpnStack.push(op1/op2);
                break;
               
            case "%": // Remainder of top two operands in the stack.
                if(rpnStack.size() < 2){
                    System.out.println("Error: too few operands");
                    errorFlag = true;
                    break;
                }
                op2 = rpnStack.pop();
                if(op2 == 0){
                    System.out.println("Error: division by 0");
                    errorFlag = true;
                    break;
                }
                op1 = rpnStack.pop();
                rpnStack.push(op1%op2);
                break;
               
            case "+!": // Repeating addition of operands in the stack.
                if(rpnStack.size() < 2){
                    System.out.println("Error: too few operands");
                    errorFlag = true;
                    break;
                }
                while(rpnStack.size() > 1){
                    op2 = rpnStack.pop();
                    op1 = rpnStack.pop();
                    rpnStack.push(op1+op2);
                }
                break;
               
            case "*!": // Repeating multiplication of operands in the stack.
                if(rpnStack.size() < 2){
                    System.out.println("Error: too few operands");
                    errorFlag = true;
                    break;
                }
                while(rpnStack.size() > 1){
                    op2 = rpnStack.pop();
                    op1 = rpnStack.pop();
                    rpnStack.push(op1*op2);
                }
                break;
               
            case "-!": // Repeating subtraction of operands in the stack.
                if(rpnStack.size() < 2){
                    System.out.println("Error: too few operands");
                    errorFlag = true;
                    break;
                }
                while(rpnStack.size() > 1){
                    op2 = rpnStack.pop();
                    op1 = rpnStack.pop();
                    rpnStack.push(op1-op2);
                }
                break;
               
            case "d": // Duplicates the top operand on the stack.
                if(rpnStack.empty()){
                    System.out.println("Error: too few operands");
                    errorFlag = true;
                    break;
                }
                rpnStack.push(rpnStack.peek());
                break;
               
            case "o": // Prints the top operand on the stack.
                if(rpnStack.empty()){
                    System.out.println("Error: too few operands");
                    errorFlag = true;
                    break;
                }
                System.out.print(rpnStack.peek() + " ");
                break;
               
            case "c": // Copies an item in the stack a given number of times.
                if(rpnStack.size() < 2){
                    System.out.println("Error: too few operands");
                    errorFlag = true;
                    break;
                }
                op2 = rpnStack.pop();
                if(op2 < 0){
                    System.out.println("Error: negative copy");
                    errorFlag = true;
                    break;
                }
                op1 = rpnStack.pop();
                for(long i = 0; i < op2; i++){
                    rpnStack.push(op1);
                }
                break;
            
            // Rotate the top value of the stack a given number of times.    
            case "r":
                if(rpnStack.size() < 2){
                    System.out.println("Error: too few operands");
                    errorFlag = true;
                    break;
                }
                op2 = rpnStack.pop(); //rotate by op2
                if(op2 < 0){
                    System.out.println("Negative roll not allowed.");
                    errorFlag = true;
                    break;
                }
                op1 = rpnStack.pop(); //the value to be rotated
                long[] tempArray = new long[(int) op2-1];
                for(int i = 0; i < op2-1; i++){
                    tempArray[i] = rpnStack.pop();
                }
                rpnStack.push(op1);
                for(int i = (int) op2-2; i >= 0; i--){
                    rpnStack.push(tempArray[i]);
                }
                break;
               
            case ")": // Case to catch closed bracket token before open.
                System.out.println("Error: unmatched parentheses");
                errorFlag = true;
                break;
                   
            default: // Catches bad tokens.
                System.out.println("Error: bad token '" + token + "'");
                errorFlag = true;
                break;
        }        
    }
   
    /**
     * A method to handle brackets given as input.
     * 
     * Takes the input from the brackets.
     * Adds it to the original string the correct number of times.
     *
     * @param st the StringTokenizer.
     */
    public static void parseBrackets(StringTokenizer st){
        if(rpnStack.empty()){
            return;
        }
        long lastInt = rpnStack.pop();
        String token = st.nextToken();
        String bracketString = "";
        while(!token.equals(")")){
            bracketString += " " + token;
            if(st.hasMoreTokens()){
                token = st.nextToken();
            }else{
                System.out.println("Error: unmatched parentheses");
                errorFlag = true;
                return;
            }
        }
        String tempString = bracketString;
        for(int i = 0; i < (int) lastInt - 1; i++){
            bracketString += tempString;
        }
        parseInput(bracketString);
    }
   
    /**
     * The main method, start point of the program.
     *
     * @param args not required.
     */
    public static void main(String[] args){
        Scanner scan = new Scanner(System.in);
        while(scan.hasNextLine()){
            createStack();
            String input = scan.nextLine();
            parseInput(input);
            if(!errorFlag){
                System.out.println(rpnStack.toString());
            }
        }
    }
    
}