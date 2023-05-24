public class Program extends Token {
    String className;
    Memberdecls memberdecls;

    public Program(String className, Memberdecls memberdecls) {
      this.className = className;
      this.memberdecls = memberdecls;
      table = new SymbolTable();
    }

    public String toString(int line) {
      return "class " + className + " {\n" + memberdecls.toString(line+1) + "}";
    }

    public void typeCheck() throws Exception {
      memberdecls.typeCheck();
    }
  }
  