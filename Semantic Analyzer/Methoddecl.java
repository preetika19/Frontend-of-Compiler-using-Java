import java.util.*;

class Methoddecl extends Token {
    ArrayList<Argdecl> argdecls;
    ArrayList<Fielddecl> fielddecls;
    ArrayList<Stmt> stmts;
    String type;
    String id;
    boolean hasSemi;

    public Methoddecl(String type, String id, ArrayList<Argdecl> argdecls, ArrayList<Fielddecl> fielddecls, ArrayList<Stmt> stmts, boolean hasSemi) {
        this.type = type;
        this.id = id;
        this.argdecls = argdecls;
        this.fielddecls = fielddecls;
        this.stmts = stmts;
        this.hasSemi = hasSemi;
    }

    public String toString(int line) {
        String args = "";
        String output;

        for (Argdecl ad: argdecls) {
            args += ad.toString() + ", ";
        } 
        args = args.substring(0, args.length() > 0 ? args.length() - 2 : 0);

        output = getTabs(line) + type + " " + id + "(" + args + ")" + " {\n";
        for (Fielddecl fielddecl: fielddecls) {
            output += fielddecl.toString(line + 1) + "\n";
        }
        for (Stmt stmt: stmts) {
            output += stmt.toString(line + 1) + "\n";
        } 
        output += getTabs(line) + "}" + ( hasSemi ? ";" : "" );
        
        return output;
    }

    public void typeCheck() throws Exception {
        ArrayList<SymbolTable.Args> parameters = new ArrayList<SymbolTable.Args>();
        for (Argdecl arg: argdecls) {
            parameters.add(table.newArgs(getType(arg.type), arg.isArray));
        } 
        if (!table.addToScope(id, "function", getType(type), parameters)) throw new RedefinedVariable(id);
        table.insertScope();
        for (Argdecl argDecl: argdecls) argDecl.typeCheck();
        for (Fielddecl fielddecl: fielddecls) fielddecl.typeCheck();
        boolean needsReturn = !getType(type).equals(SymbolTable.Type.VOID);
        for (Stmt stmt: stmts) {
            if (stmt.isReturn()) {
                needsReturn = false;
                if (!conversion(stmt.typeCheck(), getType(type))) throw new ReturnTypeInconsistency(id, stmt.typeCheck(), getType(type));
            } 
            else stmt.typeCheck();
        }
        if (needsReturn) throw new Exception("function is missing a return statement:{"+id+"}");
        table.removeScope();
    }


}