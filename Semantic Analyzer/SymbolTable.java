import java.util.*;
import java.util.HashMap;
public class SymbolTable {
    enum Type {
        VOID, BOOL, INT, FLOAT, CHAR, STRING
    }
    LinkedList<HashMap<String, Variable>> scopes;
    

    public SymbolTable() {
        scopes = new LinkedList<HashMap<String, Variable>>();
    }

    public void insertScope()   { 
        scopes.addFirst(new HashMap<String, Variable>()); 
    }

    public void removeScope()  { 
        scopes.removeFirst(); 
    }

    public boolean addToScope(String name, String reference, Type type, ArrayList args) {
        if (scopes.getFirst().containsKey(name)) return false;
        scopes.getFirst().put(name, new Variable(name, reference, type, args));
        return true;
      }

    public Variable getVariable(String name) throws Exception {
        for (HashMap<String, Variable> scope: scopes)
          if (scope.containsKey(name))
            return scope.get(name);
        throw new Exception(name + " not found in SymbolTable");
    }

    public Args newArgs(Type type, boolean array) {
        return new Args(type, array);
    }


    class Args implements Checkable {
        Type type;
        boolean isArray;
        public Args(Type type, boolean isArray) {
            this.type = type;
            this.isArray = isArray;
        }
        public Type typeCheck() { 
            return type;
        }
        public boolean isArray() { 
            return isArray; 
        }
        public boolean isFinal() { 
            return false; 
        }
        public boolean isFunction() { 
            return false; 
        }
    }

    class Variable implements Checkable {
        ArrayList<Args> arguments;
        String referenceType, name;
        Type type;
        public Variable(String name, String referenceType, Type type, ArrayList arguments) {
            this.name = name;
            this.referenceType = referenceType;
            this.type = type;
            this.arguments = arguments;
        }
        public boolean isFinal() {
          return referenceType.equals("final");
        }
        public boolean isArray() {
          return referenceType.equals("array");
        }
        public boolean isFunction() {
          return referenceType.equals("function");
        }
        public Type typeCheck() {
          return type;
        }
    }
}