import java.util.*;

public class Expr extends Token implements Checkable{
    int index;
    Name name;
    String id;
    ArrayList<Expr> args;
    int intlit;
    String charstrlit;
    float floatlit;
    boolean bool;
    Expr expr[];
    String unaryOp;
    String type;
    BinaryOperator binaryOp;

    public Expr(Name name) { // Expr -> Name
        this.name = name;
        index = 1;
    }

    public Expr(String id, boolean isFunc) { // Expr -> id ()
        this.id = id;
        index = 2;
    }

    public Expr(String id, ArrayList<Expr> args) { // Expr -> id (Args) // incomplete
        this.id = id;
        this.args = args;
        index = 3; 
    }

    public Expr(int intlit) { // Expr -> intlit
        this.intlit = intlit;
        index = 4;
    }

    public Expr(String charstrlit, char c) { // Expr -> charlit|strlit
        this.charstrlit = charstrlit;
        if (c == 'c') index = 5;
        else index = 6;
    }

    public Expr(float floatlit) { // Expr -> floatlit
        this.floatlit = floatlit;
        index = 7;
    }

    public Expr(boolean bool) { // Expr -> true|false
        this.bool = bool;
        index = 8;
    }

    public Expr(Expr expr) { // Expr -> (Expr)
        this.expr = new Expr[]{expr};
        index = 10;
    }

    public Expr(Expr e, String unaryOp) { // Expr -> ~Expr | -Expr | +Expr
        this.unaryOp = unaryOp;
        expr = new Expr[]{e};
        index = 11;
    }

    public Expr(String type, Expr e) { // Expr -> (Type)Expr
        this.type = type;
        expr = new Expr[]{e};
        index = 14;
    }

    public Expr(Expr e1, BinaryOperator binaryOp, Expr e2) { // Expr -> Expr BinaryOperator Expr
        this.binaryOp = binaryOp;
        expr = new Expr[]{e1, e2};
        index = 15;
    }

    public Expr(Expr e1, Expr e2, Expr e3) { // Expr -> ( Expr ? Expr : Expr )
        expr = new Expr[]{e1, e2, e3};
        index = 16;
    }

    public String toString() {
        if (index == 1) {
            return name.toString();
        }
        else if (index == 2) {
            return id + "()";
        }
        else if (index == 3) { 
            String output = "";
            for (Expr e: args) {
                output += e.toString() + ", ";
            } 
            output = output.substring(0, output.length() > 0 ? output.length() - 2 : 0 );
            return id + "(" + output + ")";
        }
        else if (index == 4) {
            return Integer.toString(intlit);
        }
        else if (index == 5 || index == 6) {
            return charstrlit;
        }
        else if (index == 7) {
            return Float.toString(floatlit);
        }
        else if (index == 8) {
            if (bool == true) return "true";
            else return "false";
        }
        else if (index == 10) {
            return expr[0].toString();
        }
        else if (index == 11) {
            return "(" + unaryOp + " " + expr[0].toString() + ")";
        }
        else if (index == 14) {
            return "(" + type + ")" + expr[0].toString();
        }
        else if (index == 15) {
            return "(" + expr[0].toString() + " " + binaryOp.toString() + " " + expr[1].toString() + ")";
        }
        else if (index == 16) {
            return "( " + expr[0].toString() + " ? " + expr[1].toString() + " : " + expr[2].toString() + " )";
        }

        return "";

    }

    public boolean isArray() throws Exception {
        return name != null && name.isArray() || index == 10 && expr[0].isArray();
    }

    public boolean isFinal() throws Exception {
        return name != null && name.isFinal() || index == 10 && expr[0].isFinal();
    }

    public boolean isFunction() throws Exception {
        return name != null && name.isFunction() || index == 10 && expr[0].isFunction();
    }

    public SymbolTable.Type typeCheck() throws Exception {
        if(index == 1) {
            return name.typeCheck();
        }

        else if(index == 2) {
            if (!table.getVariable(id).isFunction()) throw new UndefinedFunction(id);
            return table.getVariable(id).typeCheck();
        }

        else if(index == 3) {
            SymbolTable.Variable function = table.getVariable(id);
            if (!function.isFunction()) throw new UndefinedFunction(id);
            if (function.arguments.size() != args.size()) throw new ArgumentListInconsistency(id);
            for (int i = 0; i < args.size(); i++) {
            if (!assignment(args.get(i), function.arguments.get(i)))
                throw new ArgumentListInconsistency(id, i,function.arguments.get(i).typeCheck(),function.arguments.get(i).isArray(),
                args.get(i).typeCheck(),args.get(i).isArray());
            }
            return table.getVariable(id).typeCheck();
        }

        else if(index == 4) {
            return SymbolTable.Type.INT;
        }

        else if(index == 5) {
            return SymbolTable.Type.CHAR;
        }

        else if(index == 6) {
            return SymbolTable.Type.STRING;
        }

        else if(index == 7) {
            return SymbolTable.Type.FLOAT;
        }

        else if(index == 8) {
            return SymbolTable.Type.BOOL;
        }

        else if(index == 10) {
            return expr[0].typeCheck();
        }

        else if(index == 11) {
            if (unaryOp.equals("~")) {
                if (!isBool(expr[0])) throw new Exception("unary logical operators require type:{"+SymbolTable.Type.BOOL+"}");
                return SymbolTable.Type.BOOL;
            } 
            else {
                if (!isFloat(expr[0])) throw new Exception("prefix of signed operators expect type:{"+SymbolTable.Type.INT+"|"+SymbolTable.Type.INT+"}");
                return expr[0].typeCheck();
            }   
        }
        else if(index == 14) {
            return getType(type);
        }

        else if(index == 15) {
            if (expr[0].typeCheck().equals(SymbolTable.Type.STRING) || expr[1].typeCheck().equals(SymbolTable.Type.STRING)) {
                if (!binaryOp.binaryOp.equals("+")) throw new Exception("invalid string operation");
                return SymbolTable.Type.STRING;
            }
            if (binaryOp.isLogical()) {
                if ( !isBool(expr[0]) && !isBool(expr[1])) throw new BinaryTypeExpression(binaryOp.binaryOp, 
                    ( isBool(expr[0]) ? expr[0].typeCheck() : expr[1].typeCheck() ),
                    ( isBool(expr[0]) ? expr[0].isArray() : expr[1].isArray()));
                return SymbolTable.Type.BOOL;
            }
            if (binaryOp.isArithmetic()) {
                if (!isFloat(expr[0]) && !isFloat(expr[1])) throw new BinaryTypeExpression(binaryOp.binaryOp,
                    (isFloat(expr[0]) ? expr[0].typeCheck() : expr[1].typeCheck()),
                    (isFloat(expr[0]) ? expr[0].isArray() : expr[1].isArray()));
                return expr[0].typeCheck().equals(SymbolTable.Type.FLOAT) || expr[1].typeCheck().equals(SymbolTable.Type.FLOAT)
                    ? SymbolTable.Type.FLOAT : SymbolTable.Type.INT;
            }
            if (binaryOp.isRelational()) {
                if (!isFloat(expr[0]) && !isFloat(expr[1])) throw new BinaryTypeExpression(binaryOp.binaryOp, 
                    (isFloat(expr[0]) ? expr[0].typeCheck() : expr[1].typeCheck()),
                    (isFloat(expr[0]) ? expr[0].isArray() : expr[1].isArray()));
                return SymbolTable.Type.BOOL;
            }
        }

        else if(index == 16) {
            if (!isBool(expr[0])) throw new ConditionExpected(expr[0].typeCheck(), expr[0].isArray());
            if ( !expr[1].typeCheck().equals(expr[2].typeCheck())) throw new Exception("three part expression outputs should be of the same type");
            return expr[1].typeCheck();
        }
        
        return null;

    }
    
}
