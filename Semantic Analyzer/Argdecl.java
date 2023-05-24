public class Argdecl extends Token {
    String type;
    String id;
    boolean isArray;
    String array = "";

    public Argdecl(String type, String id, boolean isArray) {
        this.type = type;
        this.id = id;
        this.isArray = isArray;
    }
    
    public String toString() {
        if (isArray) array = "[]";
        return type + " " + id + array;
    }

    public void typeCheck() throws Exception {
        if (isArray) array = "array";
        if (!table.addToScope(id, array, getType(type), null)) throw new RedefinedVariable(id);
    }
    
}