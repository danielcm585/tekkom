/** 
 * @class: HMachine
 * This class provides the constant variables needed
 * in the code generation process.
 *
 * @author: DAJI Group (Dalton E. Pelawi & Jimmy)
 */

class HMachine
{
    public static final int memorySize = 2048,
              displaySize = 20,
              undefined = -32768;

    public static int[] memory = new int[memorySize];
    public static int[] display = new int[displaySize];

    // program counter and memory top
    public static int pc, mt;

    public static final int NAME = 0,
                            LOAD = 1,
                            STORE = 2,
                            PUSH = 3,
                            PUSHMT = 4,
                            SETD = 5,
                            POP = 6,
                            DUP = 7,
                            BR = 8,
                            BF = 9,
                            ADD = 10,
                            SUB =11,
                            MUL =12,
                            DIVI =13,
                            EQ =14,
                            LT =15,
                            ORI =16,
                            FLIP =17,
                            READC =18,
                            PRINTC =19,
                            READI =20,
                            PRINTI =21,
                            HALT =22,
                            CALL = 23,
                            RETURN = 24;

    public static final String[] operation = 
       { "NAME", "LOAD", "STORE", "PUSH", "PUSHMT", "SETD",
         "POP", "DUP", "BR", "BF", "ADD", "SUB", "MUL",
         "DIVI", "EQ", "LT", "ORI", "FLIP", "READC", "PRINTC",
         "READI", "PRINTI", "HALT", "CALL", "RETURN" };

    // Method to determine the index of a certain operation
    public static int toOpNumber(String op)
    {
        for (int i = 0; i < operation.length; i++)
        {
            if (op.equals(operation[i]))
            {
                return i;
            }
        }

        return -1;
    }

    public void run() {
        while (pc < memory.length) {
            int opcode = memory[pc];
            switch (opcode) {
                // ... existing opcodes ...
                case CALL:
                    int addr = memory[++pc];
                    // Save current pc and activation record
                    // Adjust stack and display as needed
                    pc = addr;
                    break;
                case RETURN:
                    // Restore saved pc and activation record
                    // Adjust stack and display as needed
                    // pc = return address
                    break;
                // ... rest of the opcodes ...
                default:
                    System.err.println("Unknown opcode: " + opcode);
                    System.exit(1);
            }
            pc++;
        }
    }

}