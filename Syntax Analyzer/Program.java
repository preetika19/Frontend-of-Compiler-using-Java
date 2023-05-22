public class Program extends Token {
    String className;
    Memberdecls memberdecls;

    public Program(String id, Memberdecls m) {
      className = id;
      memberdecls = m;
    }

    public String toString(int line) {
      return "class " + className + " {\n" + memberdecls.toString(line+1) + "}";
    }
  }
  