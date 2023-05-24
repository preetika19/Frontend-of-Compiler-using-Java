import java.util.*;

class Memberdecls extends Token {
    ArrayList<Fielddecl> fielddecls;
    ArrayList<Methoddecl> methoddecls;

    public Memberdecls(Fielddecl fielddecl, Memberdecls memberdecl) {
        memberdecl.fielddecls.add(0, fielddecl);
        this.fielddecls = memberdecl.fielddecls;
        this.methoddecls = memberdecl.methoddecls;
    }

    public Memberdecls(ArrayList<Fielddecl> fielddecls, ArrayList<Methoddecl> methoddecls) {
        this.fielddecls = fielddecls;
        this.methoddecls = methoddecls;
    }

    public Memberdecls(ArrayList<Methoddecl> methoddecls) {
        fielddecls = new ArrayList<Fielddecl>();
        this.methoddecls = methoddecls;
    }

    public String toString(int line) {
        String result = "";
        for (Fielddecl fielddecl: fielddecls) {
            result += fielddecl.toString(line) + "\n";
        }
        for (Methoddecl methoddecl: methoddecls) {
            result += methoddecl.toString(line) + "\n";
        }
        return result;
    }

    public void typeCheck() throws Exception {
        table.insertScope();
        for (Fielddecl fielddecl: fielddecls) {
            fielddecl.typeCheck();
        }
        for (Methoddecl methoddecl: methoddecls) {
            methoddecl.typeCheck();
        }
        table.removeScope();
      }

  
}