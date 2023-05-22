public class Name extends Token{
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
        return id + ( e!=null ? "[" + e.toString() + "]" : "" );
      }
}
