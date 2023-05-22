import java.util.*;

class Memberdecls extends Token {
    ArrayList<Fielddecl> fielddecls;
    ArrayList<Methoddecl> methoddecls;

    public Memberdecls(Fielddecl f, Memberdecls mds) {
        mds.fielddecls.add(0, f);
        this.fielddecls = mds.fielddecls;
        this.methoddecls = mds.methoddecls;
    }

    public Memberdecls(ArrayList<Fielddecl> fs, ArrayList<Methoddecl> ms) {
        fielddecls = fs;
        methoddecls = ms;
    }

    public Memberdecls(ArrayList<Methoddecl> ms) {
        fielddecls = new ArrayList<Fielddecl>();
        methoddecls = ms;
    }

    public String toString(int line) {
        String result = "";
        for (Fielddecl f: fielddecls) {
            result += f.toString(line) + "\n";
        }
        for (Methoddecl m: methoddecls) {
            result += m.toString(line) + "\n";
        }
        return result;
    }

  
}