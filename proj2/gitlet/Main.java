
package gitlet;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @Owen Mahoney, Kate Miller
 *
 * Collaborators: I talked to former 61b students Kenny Workman and Klara Chisholm about the overall design for the project
 * and how to set it up, and some general algorithimic ways about how to implement certain methods.
 */

public class Main {


    /** Current Working Directory. */
    private static File CWD = new File(System.getProperty("user.dir"));


    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */
    public static void main(String... args) {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        switch (args[0]) {
            case "init":
                validateNumArgs("init", args, 1);
                Repository.init();
                break;
            case "add":
                checkGit();
                Repository.add(args[1]);
                break;
            case "rm":
                checkGit();
                Repository.rm(args[1]);
                break;
            case "commit":
                checkGit();
                Repository.commit(args[1]);
                break;
            case "log":
                checkGit();
                Repository.log();
                break;
            case "global-log":
                checkGit();
                Repository.globalLog();
                break;
            case "find":
                checkGit();
                validateNumArgs("find", args, 2);
                Repository.find(args[1]);
                break;
            case "status":
                checkGit();
                Repository.status();
                break;
            case "branch":
                checkGit();
                Repository.newBranch(args[1]);
                break;
            case "rm-branch":
                checkGit();
                Repository.rmBranch(args[1]);
                break;
            case "checkout":
                checkGit();
                if(args.length == 3) {
                    Repository.checkoutFile(args[2]);
                }
                if(args.length == 4){
                    if(!args[2].equals("--")){
                        System.out.println("Incorrect Operands.");
                        return;
                    }
                    Repository.checkoutID(args[1], args[3]);
                }
                if(args.length == 2){
                    Repository.checkoutBranch(args[1]);
                }
                break;
            case "reset":
                checkGit();
                Repository.reset(args[1]);
                break;
            case "merge":
                checkGit();
                Repository.merge(args[1]);
                break;

            default:
                System.out.println("No command with that name exists.");
                System.exit(0);
        }
        return;
    }

    public static void checkGit(){
        if(!Repository.GITDIR.exists()){
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
    }
    public static void validateNumArgs(String cmd, String[] args, int n) {
        if (args.length != n) {
            System.out.println("Incorrect Operands");
            System.exit(0);
        }
    }
}