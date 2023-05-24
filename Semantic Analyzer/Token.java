abstract class Token {
    protected static SymbolTable table;
    protected String getTabs(int t)
    {
        String tabs = "";
        for (int i = 0; i < t; i++)
            tabs = tabs + "\t";
        return tabs;
    }
  
    public String toString(int t)
    {
        return "";
    }

    protected SymbolTable.Type getType(String type) {
        return 
        type.equals("void") ? SymbolTable.Type.VOID :
        type.equals("bool") ? SymbolTable.Type.BOOL :
        type.equals("int") ? SymbolTable.Type.INT :
        type.equals("float") ? SymbolTable.Type.FLOAT :
        type.equals("char") ? SymbolTable.Type.CHAR :
        type.equals("string") ? SymbolTable.Type.STRING : null;
    }

    protected boolean conversion(SymbolTable.Type rhs, SymbolTable.Type lhs) {
        return rhs.equals(SymbolTable.Type.INT) ? lhs.equals(SymbolTable.Type.BOOL) || 
        lhs.equals(SymbolTable.Type.FLOAT) || lhs.equals(SymbolTable.Type.INT)
        : rhs.equals(lhs);
    }

    protected boolean assignment(Checkable lhs, Checkable rhs) throws Exception {
        return lhs.isArray() && rhs.isArray() && lhs.typeCheck().equals(rhs.typeCheck()) ||
        !lhs.isArray() && !rhs.isArray() && conversion(lhs.typeCheck(), rhs.typeCheck());
    }

    protected boolean isFloat(Checkable expr) throws Exception {
        return !expr.isArray() && ( expr.typeCheck().equals(SymbolTable.Type.FLOAT) || expr.typeCheck().equals(SymbolTable.Type.INT) );
    }  

    protected boolean isBool(Checkable expr) throws Exception {    
        return !expr.isArray() && ( expr.typeCheck().equals(SymbolTable.Type.BOOL) || expr.typeCheck().equals(SymbolTable.Type.INT) );
    }

    class RedefinedVariable extends Exception {
        public RedefinedVariable(String variable) {
            super("Cannot redefine variable:{"+variable+"} in the existing scope");
        }
    }

    class UndefinedFunction extends Exception {
        public UndefinedFunction(String s) {
            super("function {"+s+"} is undefined");
        }
    }

    class BinaryTypeExpression extends Exception {
        public BinaryTypeExpression(String binaryOp, SymbolTable.Type type, boolean boArray) {
            super("binary operator:{"+binaryOp+"} does not accept type:{"+type+(boArray?"[]":"")+"}");
        }
    }

    class AssignmentTypeInconsistency extends Exception {
        public AssignmentTypeInconsistency(SymbolTable.Type lhs, boolean isLhsArray, SymbolTable.Type rhs, boolean isRhsArray) {
            super("lhs type:{"+lhs+(isLhsArray?"[]":"")+"} is inconsistent with rhs type:{"+rhs+(isRhsArray?"[]":"")+"}");
        }
        public AssignmentTypeInconsistency(SymbolTable.Type lhs, SymbolTable.Type rhs) {
            this(lhs, false, rhs, false);
        }
    }
    
    class ReturnTypeInconsistency extends Exception {
        public ReturnTypeInconsistency(String function, SymbolTable.Type actualType, SymbolTable.Type expectedType) {
            super("return type:{"+actualType+"} is inconsistent with expected type:{"+expectedType+"} for function:{"+function+"}");
        }
    }
    
    class ConditionExpected extends Exception {
        public ConditionExpected(SymbolTable.Type type, boolean isArray) {
            super("condition type:{"+type+(isArray?"[]":"")+"} caanot be coerced to type:{"+SymbolTable.Type.BOOL+"}");
        }
    }

    class ArgumentListInconsistency extends Exception {
        public ArgumentListInconsistency(String function) {
            super("incorrect no. of arguments in function {"+function+"}");
        }
        public ArgumentListInconsistency(String function, int index, SymbolTable.Type expectedType, boolean expectedArray, SymbolTable.Type actualType, boolean actualArray) {
            super("function:{"+function+"} with argument index["+index+"] expected type:{"+expectedType+(expectedArray?"[]":"")+"}, but got type:{"+actualType+(actualArray?"[]":"")+"}");
        }
        public ArgumentListInconsistency(String function, int index, SymbolTable.Type expectedType, SymbolTable.Type actualType) {
            this(function, index, expectedType, false, actualType, false);
        }
    }
}