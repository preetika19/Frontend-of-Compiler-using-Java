public class BinaryOperator extends Token {
    String binaryOp;

    public BinaryOperator(String binaryOp) {
        this.binaryOp = binaryOp;
    }
    
    public String toString() {
        return binaryOp;
    }

    public boolean isLogical() {
        return binaryOp.equals("||") || binaryOp.equals("&&");
    }

    public boolean isArithmetic() {
        return binaryOp.equals("*") || binaryOp.equals("/") || binaryOp.equals("+") || binaryOp.equals("-");
    }
    
    public boolean isRelational() {
        return binaryOp.equals("<") || binaryOp.equals("<=") || binaryOp.equals(">") || binaryOp.equals(">=") || binaryOp.equals("==") || binaryOp.equals("<>");
    }
        
}