import java.util.*;

class Methoddecl extends Token {
    ArrayList<Argdecl> argdecls;
    ArrayList<Fielddecl> fielddecls;
    ArrayList<Stmt> stmts;
    String type;
    String id;
    boolean hasSemi;

    public Methoddecl(String type, String id, ArrayList<Argdecl> as, ArrayList<Fielddecl> fs, ArrayList<Stmt> sts, boolean semi) {
        this.type = type;
        this.id = id;
        argdecls = as;
        fielddecls = fs;
        stmts = sts;
        hasSemi = semi;
    }

    public String toString(int line) {
        String args = "";
        String output;

        for (Argdecl a: argdecls) {
            args += a.toString() + ", ";
        } 
        args = args.substring(0, args.length() > 0 ? args.length() - 2 : 0);

        output = getTabs(line) + type + " " + id + "(" + args + ")" + " {\n";

        for (Fielddecl f: fielddecls) {
            output += f.toString(line+1) + "\n";
        }

        for (Stmt st: stmts) {
            output += st.toString(line+1) + "\n";
        } 
        output += getTabs(line) + "}" + ( hasSemi ? ";" : "" );
        
        return output;
    }

}