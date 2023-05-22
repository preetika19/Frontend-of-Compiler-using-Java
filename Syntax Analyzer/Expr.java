import java.util.*;

public class Expr extends Token {
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
    String binaryOp;

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

    public Expr(Expr e1, String binaryOp, Expr e2) { // Expr -> Expr BinaryOp Expr
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
        else if (index == 3) { // incomplete
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
            return "(" + expr[0].toString() + " " + binaryOp + " " + expr[1].toString() + ")";
        }
        else if (index == 16) {
            return "( " + expr[0].toString() + " ? " + expr[1].toString() + " : " + expr[2].toString() + " )";
        }

        return "";

    }
    
}
