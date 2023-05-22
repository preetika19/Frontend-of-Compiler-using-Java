public class Fielddecl extends Token {
    int index;
    String type, id;
    Integer intlit;
    String varFinal = "";
    Expr optionalex;

    public Fielddecl (String type, String id, Integer intlit) {
        this.type = type;
        this.id = id;
        this.intlit = intlit;
        index = 1;
    }

    public Fielddecl (boolean isFinal, String type, String id, Expr optionalex) {
        if (isFinal) this.varFinal = "final";
        this.type = type;
        this.id = id;
        this.optionalex = optionalex;
        index = 2;
    }

    public String toString(int line) {
        if(index == 1) {
            return getTabs(line) + type + " " + id + "[" + intlit + "]" + ";";
        }
        else if(index == 2) {
            return getTabs(line) + varFinal + type + " " + id  + ( optionalex != null ? " = "+ optionalex.toString() : "" ) + ";";
        }
        return "";
    }
}
