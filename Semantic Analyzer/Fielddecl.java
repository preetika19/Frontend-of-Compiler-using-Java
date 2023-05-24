public class Fielddecl extends Token {
    int index;
    String type;
    String id;
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

    public void typeCheck() throws Exception {
        if(index == 1) { 
            if (!table.addToScope(id, "array", getType(type), null)) throw new RedefinedVariable(id);
        }
        else if(index == 2) {
            if (optionalex != null) {
                if (!conversion(optionalex.typeCheck(), getType(type))) throw new AssignmentTypeInconsistency(getType(type), optionalex.typeCheck());
            }  
            if (!table.addToScope(id, (varFinal), getType(type), null)) throw new RedefinedVariable(id);
        }
    }
}
