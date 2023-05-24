public class Name extends Token implements Checkable{
    String id;
    Expr e;
    public Name(String id){ // Name -> id
        this.id = id;
    }
    public Name(String id, Expr e){ // Name -> id [ Expr ]
        this.id = id;
        this.e = e;
    }

    public String toString() {
        return id + ( e != null ? "[" + e.toString() + "]" : "" );
    }

    private boolean isExpr() {
        return e != null;
    }

    public boolean isFinal() throws Exception {
        return table.getVariable(id).isFinal();
    }
    
    public boolean isArray() throws Exception {
        return table.getVariable(id).isArray() && !isExpr();
    }
    public boolean isFunction() throws Exception {
        return table.getVariable(id).isFunction();
    } 
      
    public SymbolTable.Type typeCheck() throws Exception {
        if (!isExpr() || e.typeCheck().equals(SymbolTable.Type.INT)) return table.getVariable(id).typeCheck();
        else throw new Exception("int expected for array index");
    }

}
