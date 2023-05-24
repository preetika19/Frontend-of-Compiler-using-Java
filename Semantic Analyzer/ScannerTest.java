import java.io.*;
import java_cup.runtime.*;

/****
 *
 * This is a simple stand-alone testing program for the lexer defined in
 * tokens.jflex.  The main method accepts an input file as its first
 * command-line argument.  It then calls the lexer's next_token method with an
 * input reader for that file.  The value of each Symbol returned by next_token
 * is printed to stanard output.
 *                                                                     
 * The following command-line invocation will read in the test program in the
 * file "scanner-test.p" and print out each token found in that file:
 *
 *     java ScannerTest scanner-test.p
 *
 */

public class ScannerTest {
  public static void main(String args[]) throws Exception {
    Reader reader = null;
    //If no file provided, take from the input stream
    if (args.length == 1) {
      File input = new File(args[0]);
      if (!input.canRead()) {
        System.out.println("Error: could not read [" + input + "]");
      } 
      reader = new FileReader(input);
    } 
    else {
      reader = new InputStreamReader(System.in);
    }
    
    Lexer lexer = new Lexer(reader);
    parser parser = new parser(lexer);
    Program program = (Program) parser.parse().value;

    System.out.println(program.toString(0));
    System.out.println();

    try { // type check
      program.typeCheck();
      System.out.println("Type checking complete. 0 Errors.");
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
    }
    System.out.println();
  }
}