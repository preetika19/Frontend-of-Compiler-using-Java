import java.util.*;

public class Stmt extends Token {
    int index;
    Expr expr;
    Stmt stmt;
    Stmt elsestmt;
    Name name;
    ArrayList funclist;
    String id;
    ArrayList<Expr> args;
    String operation;
    ArrayList<Fielddecl> fielddecls;
    ArrayList<Stmt> stmts;
    boolean isSemi;
    
    public Stmt(Expr expr, Stmt stmt, Stmt elsestmt) { // Stmt -> if (Expr) Stmt IfEnd
        this.expr = expr;
        this.stmt = stmt;
        this.elsestmt = elsestmt;
        index = 1;
    }

    public Stmt(Expr expr, Stmt stmt) { // Stmt -> while (Expr) Stmt
        this.expr = expr;
        this.stmt = stmt;
        index = 2;
    }

    public Stmt(Name name, Expr expr) { // Stmt -> Name = Expr;
        this.name = name;
        this.expr = expr;
        index = 3;
    }

    public Stmt(String id, ArrayList funclist) { // Stmt -> read (Readlist) | print (Printlist) | printline (PrintlineList);
        this.id = id;
        this.funclist = funclist;
        if (id == "read") index = 4;
        else if( id == "print") index = 5;
        else index = 5;
    }

    public Stmt(String id) { // Stmt -> id();
        this.id = id;
        index = 7;
    }

    public Stmt(String id, ArrayList<Expr> args, boolean func) { // Stmt -> id(args);
        this.id = id;
        this.args = args;
        index = 8;
    }

    public Stmt() { // Stmt -> return; 
        index = 9;
    }

    public Stmt(Expr expr) { // Stmt -> return Expr;
        this.expr = expr;
        index = 10;
    }

    public Stmt(Name name, String operation) { // Stmt -> Name++; | Name--;
        this.name = name;
        this.operation = operation;
        index = 11;
    }

    public Stmt(ArrayList<Fielddecl> fielddecls, ArrayList<Stmt> stmts, boolean isSemi) { // Stmt -> {Fielddecls Stmts} OptionalSemi
        this.fielddecls = fielddecls;
        this.stmts = stmts;
        this.isSemi = isSemi;
        index = 12;
    }

    public String toString(int line) {
        if (index == 1) {
            return getTabs(line) +
            "if (" + expr.toString() + ")\n" +
            ( stmt.index == 12 ? stmt.toString(line) : getTabs(line) +"{\n" + stmt.toString(line+1) + "\n"+ getTabs(line) + "}" ) +
            ( elsestmt != null ? "\n" + getTabs(line) + "else\n" + ( elsestmt.index == 12 ? elsestmt.toString(line) : getTabs(line) +"{\n" + elsestmt.toString(line+1) + "\n"+ getTabs(line) + "}") : "" );
        }

        else if(index == 2){
            return getTabs(line) + "while (" + expr.toString() + ")\n" + stmt.toString(stmt.index == 12 ? line : line+1) + "\n";
        }

        else if(index == 3){
            return getTabs(line) + name.toString() + " = " + expr.toString() + ";"; 
        }

        else if(index == 4){
            String list = "";
            for (Name n: (ArrayList<Name>)funclist) {
                list += n.toString() + ", ";
            } 
            list = list.substring(0, list.length() > 0 ? list.length() - 2 : 0);
            return getTabs(line) + id + "(" + list + ");";
        }

        else if(index == 5){
            String list = "";
            for (Expr e: (ArrayList<Expr>)funclist) {
                list += e.toString() + ", ";
            } 
            list = list.substring(0, list.length() > 0 ? list.length() - 2 : 0);
            return getTabs(line) + id + "(" + list + ");";
        }

        else if(index == 7){
            return getTabs(line) + id + "();";
        }
        
        else if(index == 8){
            String list = "";
            for (Expr e: args) {
                list += e.toString() + ", ";
            } 
            list = list.substring(0, list.length() > 0 ? list.length() - 2 : 0);
            return getTabs(line) + id + "(" + list + ");";
        }

        else if(index == 9){
            return getTabs(line) + "return;";
        }

        else if(index == 10){
            return getTabs(line) + "return " + expr.toString() + ";"; 
        }

        else if(index == 11){
            return getTabs(line) + name.toString() + operation + ";";
        }
  
        else if(index == 12){ 
            String result = "";
            for (Fielddecl f: fielddecls) {
                result += f.toString(line+1) + "\n";
            }
            for (Stmt st: stmts) { 
                result += st.toString(line+1) + "\n";
            }
            return getTabs(line) + "{\n" + result + getTabs(line) + "}" + (isSemi ? ";" : "") ;
        }
        
        return "";
        
    }

    public boolean isReturn() {
        return this.index == 9 | this.index == 10;
    }

    public SymbolTable.Type typeCheck() throws Exception {
        if (index == 1) {
            if (!isBool(expr)) throw new ConditionExpected(expr.typeCheck(), expr.isArray());
            table.insertScope();
            stmt.typeCheck();
            table.removeScope();
            if(elsestmt != null) {
                table.insertScope();
                stmt.typeCheck();
                table.removeScope();
            }
        }

        else if(index == 2){
            if (!isBool(expr)) throw new ConditionExpected(expr.typeCheck(), expr.isArray());
            table.insertScope();
            stmt.typeCheck();
            table.removeScope();
        }

        else if(index == 3){
            if (!assignment(this.expr, this.name)) throw new AssignmentTypeInconsistency(
                name.typeCheck(),name.isArray(),expr.typeCheck(),expr.isArray());
            if (table.getVariable(name.id).isFinal()) throw new Exception("variable:{"+name.id+"} is final. No modifications allowed");
        }

        else if(index == 4) {
            for (Name nme: (ArrayList<Name>)funclist) {
                if (nme.isArray() || nme.isFinal()) throw new Exception("final/array cannot be read");
              }
        }

        else if(index == 5) {
            for (Expr epr: (ArrayList<Expr>)funclist) {
                epr.typeCheck();
                if (epr.isArray() || epr.isFunction()) throw new Exception("array/function cannot be printed");
            }
        }

        else if(index == 7) {
            if (!table.getVariable(id).isFunction()) throw new UndefinedFunction(id);
        }

        else if(index == 8) {
            SymbolTable.Variable function = table.getVariable(id);
            if (!function.isFunction()) throw new UndefinedFunction(id);
            if (function.arguments.size() != args.size()) throw new ArgumentListInconsistency(id);
            for (int i = 0; i < args.size(); i++) {
            if (!assignment(args.get(i), function.arguments.get(i)))throw new ArgumentListInconsistency(
                id, i,function.arguments.get(i).typeCheck(),function.arguments.get(i).isArray(),
                args.get(i).typeCheck(),args.get(i).isArray());
            }
        }

        else if(index == 9) {
            return SymbolTable.Type.VOID;
        }

        else if(index == 10) {
            return expr.typeCheck();
        }

        else if(index == 11) {
            if (!isFloat(name)) throw new Exception("unary arithmetic operators require type:{"+SymbolTable.Type.INT+"|"+SymbolTable.Type.FLOAT+"}");
            if (table.getVariable(name.id).isFinal()) throw new Exception("variable:{"+name.id+"} is final. No modifications allowed");
        }

        else if(index == 12) {
            table.insertScope();
            for (Fielddecl fielddecl: fielddecls) fielddecl.typeCheck();
            for (Stmt stmt: stmts) stmt.typeCheck();
            table.removeScope();
        }

        return null;

    }
 
}