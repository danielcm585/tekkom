/** 
 * @class: Context
 * This class constructs Context object that has attributes : 
 * 1. lexicalLevel    : current lexical level
 * 2. orderNumber     : current order number
 * 3. symbolHash      : hash table of symbols
 * 4. symbolStack     : stack to keep symbol's name
 * 4. typeStack       : stack to keep symbol's type
 * 4. printSymbols    : choice of printing symbols
 * 4. errorCount      : error counter of context checking
 *
 * @author: DAJI Group (Dalton E. Pelawi & Jimmy)
 */

import java.util.Stack;

class Context
{
    public Context()
    {
        lexicalLevel = -1;
        orderNumber = 0;
        symbolHash = new Hash(HASH_SIZE);
        symbolStack = new Stack<String>();
        typeStack = new Stack<Integer>();
        orderNumberStack = new Stack<Integer>();
        argCountStack = new Stack<Integer>();
        printSymbols = false;
        errorCount = 0;
        isProcOrFunc = false;
    }

    /**
     * This method chooses which action to be taken
     * @input : ruleNo(type:int)
     * @output: -(type:void)
     */
    public void C(int ruleNo)
    {
        boolean error = false;

        //System.out.println("C" + ruleNo);
        switch(ruleNo)
        {
            case 0:
                lexicalLevel++;
                orderNumber = 0;
                // TODO: 
                if (isProcOrFunc) {
                    orderNumberStack.push(orderNumber);
                }
                break;
            case 1:
                if (printSymbols)
                    symbolHash.print(lexicalLevel);
                break;
            case 2:
                symbolHash.delete(lexicalLevel);
                lexicalLevel--;
                // TODO: 
                if (isProcOrFunc) {
                    orderNumber = orderNumberStack.pop();
                }
                break;
            case 3:
                if (symbolHash.isExist(currentStr, lexicalLevel))
                {
                    System.out.println("Variable declared at line " + currentLine + ": " + currentStr);
                    errorCount++;
                    System.err.println("\nProcess terminated.\nAt least " + (errorCount + parser.yylex.num_error)
                                       + " error(s) detected.");
                    System.exit(1);
                }
                else
                {
                    symbolHash.insert(new Bucket(currentStr));
                }
                symbolStack.push(currentStr);
                break;
            case 4:
                symbolHash.find(currentStr).setLLON(lexicalLevel, orderNumber);
                break;
            case 5:
                symbolHash.find(currentStr).setIdType(typeStack.peek());
                break;
            case 6:
                if (!symbolHash.isExist(currentStr))
                {
                    System.out.println("Variable undeclared at line " + currentLine + ": " + currentStr);
                    errorCount++;
                    System.err.println("\nProcess terminated.\nAt least " + (errorCount + parser.yylex.num_error)
                                       + " error(s) detected.");
                    System.exit(1);
                }
                else
                {
                    symbolStack.push(currentStr);
                }
                break;
            case 7:
                symbolStack.pop();
                break;
            case 8:
                typeStack.push(symbolHash.find(currentStr).getIdType());
                break;
            case 9:
                typeStack.push(Bucket.INTEGER);
                break;
            case 10:
                typeStack.push(Bucket.BOOLEAN);
                break;
            case 11:
                typeStack.pop();
                break;
            case 12:
                switch (typeStack.peek())
                {
                    case Bucket.BOOLEAN:
                        System.out.println("Type of integer expected at line " + currentLine + ": " + currentStr);
                        errorCount++;
                        break;
                    case Bucket.UNDEFINED:
                        System.out.println("Undefined type at line " + currentLine + ": " + currentStr);
                        errorCount++;
                        break;
                }
                break;
            case 13:
                switch (typeStack.peek())
                {
                    case Bucket.INTEGER:
                        System.out.println("Type of boolean expected at line " + currentLine + ": " + currentStr);
                        errorCount++;
                        break;
                    case Bucket.UNDEFINED:
                        System.out.println("Undefined type at line " + currentLine + ": " + currentStr);
                        errorCount++;
                        break;
                }
                break;
            case 14:
                int temp = typeStack.pop();
                if (temp != typeStack.peek())
                {
                    System.out.println("Unmatched type at line " + currentLine + ": " + currentStr);
                    errorCount++;
                }
                typeStack.push(temp);
                break;
            case 15:
                temp = typeStack.pop();
                if ((temp != Bucket.INTEGER) && typeStack.peek() != Bucket.INTEGER)
                {
                    System.out.println("Unmatched type at line " + currentLine + ": " + currentStr);
                    errorCount++;
                }
                typeStack.push(temp);
                break;
            case 16:
                temp = symbolHash.find((String)symbolStack.peek()).getIdType();
                if (temp != typeStack.peek())
                {
                    System.out.println("Unmatched type at line " + currentLine + ": " + currentStr);
                    errorCount++;
                }
                break;
            case 17:
                temp = symbolHash.find((String)symbolStack.peek()).getIdType();
                if (temp != Bucket.INTEGER)
                {
                    System.out.println("Type of integer expected at line " + currentLine + ": " + currentStr);
                    errorCount++;
                }
                break;
            case 18:
                symbolHash.find(currentStr).setIdKind(Bucket.SCALAR);
                orderNumber++;
                break;
            case 19:
                symbolHash.find(currentStr).setIdKind(Bucket.ARRAY);
                orderNumber += 3;
                break;
            case 20:
                switch (symbolHash.find((String)symbolStack.peek()).getIdKind())
                {
                    case Bucket.UNDEFINED:
                        System.out.println("Variable not fully defined at line " + currentLine + ": " + currentStr);
                        errorCount++;
                        break;
                    case Bucket.ARRAY:
                        System.out.println("Scalar variable expected at line " + currentLine + ": " + currentStr);
                        errorCount++;
                        break;
                }
                break;
            case 21:
                switch (symbolHash.find((String)symbolStack.peek()).getIdKind())
                {
                    case Bucket.UNDEFINED:
                        System.out.println("Variable not fully defined at line " + currentLine + ": " + currentStr);
                        errorCount++;
                        break;
                    case Bucket.SCALAR:
                        System.out.println("Array variable expected at line " + currentLine + ": " + currentStr);
                        errorCount++;
                        break;
                }
                break;
            case 22:
                // TODO: 
                Bucket currentBucket = symbolHash.find(currentStr);
                if (currentBucket != null) {
                    currentBucket.setLLON(lexicalLevel, orderNumber);
                } else {
                    System.out.println("Error: Symbol not found for LLON assignment at line " + currentLine + ": " + currentStr);
                    errorCount++;
                    System.err.println("\nProcess terminated.\nAt least " + errorCount + " error(s) detected.");
                    System.exit(1);
                }
                break;
            case 23:
                // TODO: 
                Bucket bucket23 = symbolHash.find(currentStr);
                if (bucket23 != null) {
                    if (!typeStack.isEmpty()) {
                        int type = typeStack.peek(); // Get the type from the type stack
                        if (type == Bucket.INTEGER || type == Bucket.BOOLEAN) {
                            bucket23.setIdType(type); // Set the type in the symbol table
                        } else {
                            System.out.println("Invalid type for symbol at line " + currentLine + ": " + currentStr);
                            errorCount++;
                        }
                    } else {
                        System.out.println("Type stack is empty when setting type for symbol at line " + currentLine + ": " + currentStr);
                        errorCount++;
                    }
                } else {
                    handleError("Symbol not found for setting type in C23");
                }
                break;
            case 24:
                // TODO: 
                Bucket procBucket = symbolHash.find(currentStr);
                if (procBucket != null) {
                    System.out.println("Procedure already declared at line " + currentLine + ": " + currentStr);
                    errorCount++;
                    System.err.println("\nProcess terminated.\nAt least " + errorCount + " error(s) detected.");
                    System.exit(1);
                } else {
                    Bucket newProc = new Bucket(currentStr);
                    newProc.setIdKind(Bucket.PROCEDURE);
                    newProc.setLLON(lexicalLevel, orderNumber);
                    symbolHash.insert(newProc);
                    orderNumber++;
                    symbolStack.push(currentStr);
                    isProcOrFunc = true;
                }
                break;
            case 27:
                // TODO: 
                symbolHash.delete(lexicalLevel);
                lexicalLevel--;
                if (isProcOrFunc && !orderNumberStack.isEmpty()) {
                    orderNumber = orderNumberStack.pop();
                }
                if (isProcOrFunc && lexicalLevel == 0) {
                    isProcOrFunc = false;
                }
                break;
            case 28:
                // TODO: 
                Bucket procCheck = symbolHash.find(currentStr);
                if (procCheck != null) {
                    if (procCheck.getIdKind() != Bucket.PROCEDURE) {
                        System.out.println("Semantic Error: '" + currentStr + "' is not a procedure at line " + currentLine);
                        errorCount++;
                    }
                } else {
                    System.out.println("Semantic Error: Procedure '" + currentStr + "' not found in symbol table at line " + currentLine);
                    errorCount++;
                }
                break;
            case 29:
                // TODO: 
                Bucket procFuncBucket = symbolHash.find(currentStr);
                if (procFuncBucket != null) {
                    if (procFuncBucket.getIdKind() == Bucket.PROCEDURE || procFuncBucket.getIdKind() == Bucket.FUNCTION) {
                        int paramCount = procFuncBucket.getParameterCount();
                        if (paramCount > 0) {
                            System.out.println("Semantic Error: Procedure or function '" + currentStr + "' should not have parameters at line " + currentLine);
                            errorCount++;
                        }
                    } else {
                        System.out.println("Semantic Error: '" + currentStr + "' is not a procedure or function at line " + currentLine);
                        errorCount++;
                    }
                } else {
                    System.out.println("Semantic Error: Procedure or function '" + currentStr + "' not found in symbol table at line " + currentLine);
                    errorCount++;
                }
                break;
            case 30:
                // TODO: 
                argCountStack.push(0);
                break;
            case 32:
                // TODO: 
                if (!argCountStack.isEmpty()) {
                    int providedArgs = argCountStack.pop();
                    // Retrieve expected number of parameters from symbol table
                    Bucket procFunc = symbolHash.find(currentStr);
                    if (procFunc != null) {
                        if (procFunc.getIdKind() == Bucket.PROCEDURE || procFunc.getIdKind() == Bucket.FUNCTION) {
                            int expectedArgs = procFunc.getParameterCount();                            ;
                            if (providedArgs != expectedArgs) {
                                System.out.println("Semantic Error: Procedure/Function '" + currentStr + "' expects " 
                                                   + expectedArgs + " arguments but " + providedArgs 
                                                   + " were provided at line " + currentLine);
                                errorCount++;
                            }
                        } else {
                            System.out.println("Semantic Error: '" + currentStr + "' is not a procedure or function at line " 
                                               + currentLine);
                            errorCount++;
                        }
                    } else {
                        System.out.println("Semantic Error: Procedure or function '" + currentStr 
                                           + "' not found in symbol table at line " + currentLine);
                        errorCount++;
                    }
                } else {
                    System.out.println("Semantic Error: Argument count stack is empty when validating arguments for '" 
                                       + currentStr + "' at line " + currentLine);
                    errorCount++;
                }            
                break;
            case 33:
                // TODO: 
                Bucket funcCheck = symbolHash.find(currentStr);
                if (funcCheck != null) {
                    if (funcCheck.getIdKind() != Bucket.FUNCTION) {
                        System.out.println("Semantic Error: '" + currentStr + "' is not a function at line " + currentLine);
                        errorCount++;
                    }
                } else {
                    System.out.println("Semantic Error: Function '" + currentStr + "' not found in symbol table at line " + currentLine);
                    errorCount++;
                }
                break;
            case 34:
                if (!argCountStack.isEmpty()) {
                    int argCount = argCountStack.pop();
                    argCount++;
                    argCountStack.push(argCount);
                } else {
                    System.out.println("Semantic Error: Argument count stack is empty when incrementing argument count at line " + currentLine);
                    errorCount++;
                }
                break;
            case 35:
                // TODO: 
                if (!argCountStack.isEmpty()) {
                    int argCount = argCountStack.pop();
                    Bucket procFunc = symbolHash.find(currentStr);
                    if (procFunc != null) {
                        if (procFunc.getIdKind() == Bucket.PROCEDURE || procFunc.getIdKind() == Bucket.FUNCTION) {
                            procFunc.setParameterCount(argCount); // Assuming setParameterCount method exists in Bucket
                        } else {
                            System.out.println("Semantic Error: '" + currentStr + "' is not a procedure or function at line " + currentLine);
                            errorCount++;
                        }
                    } else {
                        System.out.println("Semantic Error: Procedure or function '" + currentStr + "' not found in symbol table at line " + currentLine);
                        errorCount++;
                    }
                } else {
                    System.out.println("Semantic Error: Argument count stack is empty when setting parameter count for '" + currentStr + "' at line " + currentLine);
                    errorCount++;
                }            
                break;
            case 36:
                // TODO: 
                Bucket functionBucket = symbolHash.find(currentStr);
                if (functionBucket != null) {
                    if (functionBucket.getIdKind() != Bucket.FUNCTION) {
                        System.out.println("Semantic Error: " + currentStr + " is not a function at line " + currentLine);
                        errorCount++;
                    } else {
                        if (!typeStack.isEmpty()) {
                            int exprType = typeStack.pop(); // Type of the expression being returned
                            int funcType = functionBucket.getIdType(); // Function's declared return type
                            if (exprType != funcType) {
                                String funcTypeStr = (funcType == Bucket.INTEGER) ? "int" :
                                                    (funcType == Bucket.BOOLEAN) ? "bool" : "unknown";
                                String exprTypeStr = (exprType == Bucket.INTEGER) ? "int" :
                                                    (exprType == Bucket.BOOLEAN) ? "bool" : "unknown";
                                
                                System.out.println("Type Mismatch: Function '" + currentStr + "' expects return type '"
                                                + funcTypeStr + "' but expression has type '" + exprTypeStr
                                                + "' at line " + currentLine);
                                errorCount++;
                            }
                        } else {
                            System.out.println("Semantic Error: Type stack is empty when checking return type for function '" 
                                            + currentStr + "' at line " + currentLine);
                            errorCount++;
                        }
                    }
                } else {
                    System.out.println("Semantic Error: Function '" + currentStr + "' not found in symbol table at line " + currentLine);
                    errorCount++;
                }
                break;
            case 37:
                // TODO: 
                Bucket bucket37 = symbolHash.find(currentStr);
                if (bucket37 != null && bucket37.getIdKind() == Bucket.FUNCTION) {
                    // Execute C33: Check that the identifier is the name of a function
                    System.out.println("Semantic Error: '" + currentStr + "' is not a function at line " + currentLine);
                    errorCount++;
                } else {
                    // Execute C20: Check if variable is SCALAR
                    if (!symbolStack.isEmpty()) {
                        String symbolName20 = symbolStack.peek();
                        Bucket bucket20 = symbolHash.find(symbolName20);
                        if (bucket20 != null) {
                            switch (bucket20.getIdKind()) {
                                case Bucket.UNDEFINED:
                                    System.out.println("Variable not fully defined at line " + currentLine + ": " + currentStr);
                                    errorCount++;
                                    break;
                                case Bucket.ARRAY:
                                    System.out.println("Scalar variable expected at line " + currentLine + ": " + currentStr);
                                    errorCount++;
                                    break;
                                default:
                                    // If it's neither UNDEFINED nor ARRAY, no action needed
                                    break;
                            }
                        } else {
                            handleError("Symbol not found during SCALAR variable check");
                        }
                    } else {
                        handleError("Symbol stack is empty during SCALAR variable check");
                    }
                }
                break;
            case 40:
                // TODO: 
                Bucket bucket40 = symbolHash.find(currentStr);
                if (bucket40 != null) {
                    if (!typeStack.isEmpty()) {
                        int funcType = typeStack.peek(); // Assuming typeStack has the function's return type
                        if (funcType == Bucket.INTEGER || funcType == Bucket.BOOLEAN) {
                            bucket40.setIdType(funcType);
                        } else {
                            System.out.println("Invalid function return type at line " + currentLine + ": " + currentStr);
                            errorCount++;
                        }
                    } else {
                        System.out.println("Type stack is empty when setting function return type at line " + currentLine + ": " + currentStr);
                        errorCount++;
                    }
                } else {
                    handleError("Symbol not found for setting function type in C40");
                }
                break;
        }
    }

    /**
     * This method sets the current token and line
     * @input : str(type:int), line(type:int)
     * @output: -(type:void)
     */
    public void setCurrent(String str, int line)
    {
        currentStr = str;
        currentLine = line;
    }

    /**
     * This method sets symbol printing option
     * @input : bool(type:boolean)
     * @output: -(type:void)
     */
    public void setPrint(boolean bool)
    {
        printSymbols = bool;
    }

    private void handleError(String message) 
    {
        System.out.println(message);
        errorCount++;
        System.err.println("\nProcess terminated.\nAt least " + errorCount + " error(s) detected.");
        System.exit(1);
    }

    private final int HASH_SIZE = 211;

    public static int lexicalLevel;
    public static int orderNumber;
    public static Hash symbolHash;
    private Stack<String> symbolStack;
    private Stack<Integer> typeStack;
    private Stack<Integer> orderNumberStack;
    private Stack<Integer> argCountStack;
    public static String currentStr;
    public static int currentLine;
    private boolean printSymbols;
    public int errorCount;
    private boolean isProcOrFunc;

}