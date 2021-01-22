package gitlet;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;


public class Repository implements Serializable {

    static final File CWD = new File(System.getProperty("user.dir"));
    static final File GITDIR = Utils.join(CWD, ".gitlet");
    static final File COMMITS = Utils.join(GITDIR, "/commits");
    static final File BLOBS = Utils.join(GITDIR, "/blobs");

    private static HashMap<String, String> _branches;
    //Make a hash map that has the name of the Branch and the top/Head of the Branch. example "master, ShaId"" "branch 2, shaid"
    private static HashMap<String, Blob> _staged;
    //Make a hashmap that has name and ShaID of blobs to add to commits;
    private static HashMap<String, Blob> _removed;
    //Make a hashmap that has name and ShaID of blobs to remove from commits and commit tree
    private static HashSet<String> _everyCommit;
    //make a hashmap that holds every commit by SHAID;
    //serialize after each commit;
    private static String _head;
    private static String _currBranch;

    public static void init() {
        if (GITDIR.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            return;
        } else {
            GITDIR.mkdir();
            COMMITS.mkdir();
            BLOBS.mkdir();
            HashMap c = new HashMap<>();
            _everyCommit = new HashSet<String>();
            Commit initial = new Commit("initial commit", null, null, c);
            initial._save();
            _currBranch = "master";
            _head = initial.get_shaID();
            _everyCommit.add(_head);
            _removed = new HashMap<>();
            _staged = new HashMap<>();
            _branches = new HashMap<>();
            _branches.put(_currBranch, _head);
            File head = Utils.join(Repository.GITDIR, "head");
            try {
                head.createNewFile();
            } catch (IOException e) {
                System.out.println("Can't track Head");
            }
            Utils.writeContents(head, _head);
            File eCommit = Utils.join(Repository.GITDIR, "everyCommit");
            try {
                eCommit.createNewFile();
            } catch (IOException e) {
                System.out.println("Can't add commit");
            }
            Utils.writeObject(eCommit, _everyCommit);
            File currB = Utils.join(Repository.GITDIR, "currbranch");
            try {
                currB.createNewFile();
            } catch (IOException e) {
                System.out.println("Can'track curr branch");
            }
            Utils.writeContents(currB, _currBranch);
            File stag = Utils.join(Repository.GITDIR, "staged");
            try {
                stag.createNewFile();
            } catch (IOException e) {
                System.out.println("Can't create Staged map");
            }
            Utils.writeObject(stag, _staged);
            File rem = Utils.join(Repository.GITDIR, "removed");
            try {
                rem.createNewFile();
            } catch (IOException e) {
                System.out.println("Can't create removed Map");
            }
            Utils.writeObject(rem, _removed);
            File bra = Utils.join(Repository.GITDIR, "branches");
            try {
                bra.createNewFile();
            } catch (IOException e) {
                System.out.println("Can't track branches");
            }
            Utils.writeObject(bra, _branches);
        }
    }

    public static void add(String name) {
        File f = new File(name);
        if (!f.exists()) {
            System.out.println("File does not exist.");
            return;
        }
        Blob b = new Blob(f);
        String currHead = Utils.readContentsAsString(Utils.join(Repository.GITDIR, "head"));
        Commit c = Utils.readObject(Utils.join(Repository.COMMITS, currHead), Commit.class);
        HashMap stage = Utils.readObject(Utils.join(Repository.GITDIR, "staged"), HashMap.class);
        if (c.getDirec().containsKey(name)) {
            Blob old = c.getDirec().get(name);
            if (b._shaID.equals(old._shaID)) {
                stage.remove(name, b);
            }
            else{
                stage.put(name,b);
                b._save();
            }
        }
        else{
            stage.put(name, b);
            b._save();
        }

        HashMap remove = Utils.readObject(Utils.join(Repository.GITDIR, "removed"), HashMap.class);
        remove.remove(name);
        Utils.writeObject(Utils.join(Repository.GITDIR, "removed"), remove);
        Utils.writeObject(Utils.join(Repository.GITDIR, "staged"), stage);
    }

    /** from Ed: For your first question, tracked means that
     a file with the same name exists in the most recent commit. This means that the contents of the
     file in the CWD do not have to be identical to the version in the current commit for it to be "tracked."
     This also means that rm is allowed to delete a modified file from the CWD and untrack it as long as
     there is some version of the file (some file with the same name) tracked in the current commit. */


    public static void rm(String name){
        String currHead = Utils.readContentsAsString(Utils.join(Repository.GITDIR, "head"));
        Commit c = Utils.readObject(Utils.join(Repository.COMMITS, currHead), Commit.class);
        HashMap stage = Utils.readObject(Utils.join(Repository.GITDIR, "staged"), HashMap.class);
        HashMap remove = Utils.readObject(Utils.join(Repository.GITDIR, "removed"), HashMap.class);
        if(!stage.containsKey(name) && !c.getDirec().containsKey(name)){
            System.out.println("No reason to remove the file.");
            return;
        }
        if(stage.containsKey(name)) {
            stage.remove(name, stage.get(name));
        }
        if(c.getDirec().containsKey(name)){
            remove.put(name, c.getDirec().get(name));
            Utils.restrictedDelete(name);
        }
        Utils.writeObject(Utils.join(Repository.GITDIR, "staged"), stage);
        Utils.writeObject(Utils.join(Repository.GITDIR, "removed"), remove);
    }
    public static void commit(String message){
        String currHead = Utils.readContentsAsString(Utils.join(Repository.GITDIR, "head"));
        Commit prev = Utils.readObject(Utils.join(Repository.COMMITS, currHead),Commit.class);
        HashMap tree = prev.getDirec();
        HashMap stage = Utils.readObject(Utils.join(Repository.GITDIR, "staged"), HashMap.class);
        HashMap<String, Blob> remove = Utils.readObject(Utils.join(Repository.GITDIR, "removed"), HashMap.class);

        if (stage.isEmpty() && remove.isEmpty()) {
            System.out.print("No changes added to the commit.");
            return;
        }
        if (message.equals("")) {
            System.out.println("Please enter a commit message.");
            return;
        }

        Iterator i = tree.keySet().iterator();
        while(i.hasNext()){
            Object s = i.next();
            if(remove.containsKey(s)){
                i.remove();
            }
        }

        for (Object key: stage.keySet()){
            if(!tree.containsKey(key)){

                tree.put(key, stage.get(key));
            }
            if(tree.containsKey(key)){
                tree.replace(key, stage.get(key));
            }
        }
        Commit c = new Commit(message, prev.get_shaID(), null, tree);
        c._save();
        _head = c.get_shaID();
        Utils.writeContents(Utils.join(Repository.GITDIR, "head") ,_head);
        HashSet eCommit = Utils.readObject(Utils.join(Repository.GITDIR, "everyCommit"), HashSet.class);
        eCommit.add(c.get_shaID());
        Utils.writeObject(Utils.join(Repository.GITDIR, "everyCommit"), eCommit);
        stage.clear();
        remove.clear();
        Utils.writeObject(Utils.join(Repository.GITDIR, "staged"), stage);
        Utils.writeObject(Utils.join(Repository.GITDIR, "removed"), remove);
        HashMap branch =  Utils.readObject(Utils.join(Repository.GITDIR, "branches"), HashMap.class);
        String currbranch = Utils.readContentsAsString(Utils.join(Repository.GITDIR, "currbranch"));
        branch.replace(currbranch, c.get_shaID());
        Utils.writeObject(Utils.join(Repository.GITDIR, "branches"), branch);
    }

    public static void log(){
        String currHead = Utils.readContentsAsString(Utils.join(Repository.GITDIR, "head"));
        Commit curr = Utils.readObject(Utils.join(Repository.COMMITS, currHead),Commit.class);
        while (curr.getParentID()!= null) {
            System.out.println("===");
            System.out.println("commit " + curr.get_shaID());
            System.out.println(String.format("Date: %1$ta %1$tb"
                    + " %1$te %1$tT %1$tY %1$tz",curr.getTimestamp()));
            System.out.println(curr.getLog_message());
            System.out.println("");
            curr = curr.fromFile(curr.getParentID());
        }
        System.out.println("===");
        System.out.println("commit " + curr.get_shaID());
        System.out.println(String.format("Date: %1$ta %1$tb"
                + " %1$te %1$tT %1$tY %1$tz",curr.getTimestamp()));
        System.out.println(curr.getLog_message());
        System.out.println("");
    }


    public static void globalLog(){
        HashSet<String> eCommit = Utils.readObject(Utils.join(Repository.GITDIR, "everyCommit"), HashSet.class);
        for (String id : eCommit){
            Commit curr = Utils.readObject(Utils.join(Repository.COMMITS, id), Commit.class);
            System.out.println("===");
            System.out.println("commit " + curr.get_shaID());
            System.out.println(String.format("Date: %1$ta %1$tb"
                    + " %1$te %1$tT %1$tY %1$tz",curr.getTimestamp()));
            System.out.println(curr.getLog_message());
            System.out.println("");
        }

    }


    public static void find(String message) {
        HashSet<String> eCommit = Utils.readObject(Utils.join(Repository.GITDIR, "everyCommit"), HashSet.class);
        String found = "no";
        for (String id : eCommit) {
            Commit curr = Utils.readObject(Utils.join(Repository.COMMITS, id), Commit.class);
            if (curr.getLog_message().equals(message)) {
                System.out.println(id);
                found = "yes";
            }
        }
        if (found.equals("no")) {
            System.out.println("Found no commit with that message.");
            return;
        }
    }



    public static void status() {
        String currBranch = Utils.readContentsAsString(Utils.join(Repository.GITDIR, "currbranch"));
        HashMap<String, String> branches = Utils.readObject(Utils.join(Repository.GITDIR, "branches"), HashMap.class);
        System.out.println("=== Branches ===");
        for (String b : branches.keySet()) {
            if (b.equals(currBranch)) {
                System.out.println('*' + b);
                continue;
            }
            System.out.println(b);

        }
        System.out.println();
        HashMap<String, Blob> staged = Utils.readObject(Utils.join(Repository.GITDIR, "staged"), HashMap.class);
        System.out.println("=== Staged Files ===");
        for (String b : staged.keySet()) {
            System.out.println(b);
        }
        System.out.println();
        HashMap<String, Blob> removed = Utils.readObject(Utils.join(Repository.GITDIR, "removed"), HashMap.class);
        System.out.println("=== Removed Files ===");
        for (String b : removed.keySet()) {
            System.out.println(b);
        }
        System.out.println();
        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println();
        System.out.println("=== Untracked Files ===");
        System.out.println();
    }


    public static void newBranch(String name){
        HashMap<String, String> branches = Utils.readObject(Utils.join(Repository.GITDIR,"branches"), HashMap.class);
        String currbranch = Utils.readContentsAsString(Utils.join(Repository.GITDIR,"currbranch"));
        String currHead = Utils.readContentsAsString(Utils.join(Repository.GITDIR, "head"));
        if(branches.containsKey(name)){
            System.out.println("A branch with that name already exists.");
            return;
        }
        branches.put(name, currHead);
        Utils.writeObject(Utils.join(Repository.GITDIR, "branches"), branches);

    }

    public static void rmBranch(String name) {
        HashMap<String, String> branches = Utils.readObject(Utils.join(Repository.GITDIR,"branches"), HashMap.class);
        String currbranch = Utils.readContentsAsString(Utils.join(Repository.GITDIR,"currbranch"));
        if(currbranch.equals(name)){
            System.out.println("Cannot remove the current branch.");
            return;
        }
        if(!branches.containsKey(name)){
            System.out.println("A branch with that name does not exist");
            return;
        }
        branches.remove(name, branches.get(name));
        Utils.writeObject(Utils.join(Repository.GITDIR, "branches"), branches);

    }

    public static void checkoutFile(String filename){
        String currHead = Utils.readContentsAsString(Utils.join(Repository.GITDIR, "head"));
        Commit curr = Utils.readObject(Utils.join(Repository.COMMITS, currHead),Commit.class);
        HashMap<String, Blob> currdir = curr.getDirec();
        if(!currdir.containsKey(filename)){
            System.out.println("File does not exist in that commit.");
            return;
        }
        File a = new File(filename);
        Blob b = currdir.get(filename);
        byte[] mem = b.mem;
        File f = new File(filename);
        try {
            f.createNewFile();
        } catch (IOException e) {
            System.out.println("Can't Create a new file in directory");
        }
        Utils.writeContents(f, mem);

    }

    public static String convertID(String commitID){
        HashSet<String> eCommit = Utils.readObject(Utils.join(Repository.GITDIR, "everyCommit"), HashSet.class);
        Iterator<String> i = eCommit.iterator();
        int count = 1;
        while (i.hasNext()) {
            String s = i.next();
            for (int j = 0; j < commitID.length(); j++) {
                if (s.charAt(j) != commitID.charAt(j)) {
                    break;
                }else{
                    count += 1;
                    if (count == commitID.length()) {
                        String searchID = s;
                        return searchID;
                    }
                }
            }
        }return commitID;
    }
    public static void checkoutID(String commitID, String filename) {
        HashSet<String> eCommit = Utils.readObject(Utils.join(Repository.GITDIR, "everyCommit"), HashSet.class);
        String ID;
        ID = commitID;
        if (commitID.length() < 40) {
            ID = convertID(commitID);
        }
        if(!eCommit.contains(ID)){
            System.out.println("No commit with that id exists.");
            return;
        }
        Commit curr = Utils.readObject(Utils.join(Repository.COMMITS, ID), Commit.class);
        HashMap<String, Blob> currdir = curr.getDirec();
        if (!currdir.containsKey(filename)) {
            System.out.println("File does not exist in that commit.");
            return;
        }
        File a = new File(filename);
        Blob b = currdir.get(filename);
        byte[] mem = b.mem;
        File f = new File(filename);
        try {
            f.createNewFile();
        } catch (IOException e) {
            System.out.println("Can't Create a new file in directory");
        }
        Utils.writeContents(f, mem);
    }



    public static void checkoutBranch(String branch){

        String currHead = Utils.readContentsAsString(Utils.join(Repository.GITDIR, "head"));
        Commit curr = Utils.readObject(Utils.join(Repository.COMMITS, currHead),Commit.class);
        HashMap<String, Blob> currdir = curr.getDirec();
        List<String> files = Utils.plainFilenamesIn(CWD);
        if(!files.isEmpty()) {
            for (int i = 0; i < files.size(); i++) {
                if (!currdir.containsKey(files.get(i))) {
                    System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                    return;
                }
            }
        }
        HashMap<String, String> branches = Utils.readObject(Utils.join(Repository.GITDIR,"branches"), HashMap.class);
        if(!branches.containsKey(branch)){
            System.out.println("No such branch exists.");
            return;
        }
        String currbranch = Utils.readContentsAsString(Utils.join(Repository.GITDIR,"currbranch"));
        if(currbranch.equals(branch)){
            System.out.println("No need to checkout the current branch.");
            return;
        }
        String newHead = branches.get(branch);
        Commit newCurr = Utils.readObject(Utils.join(Repository.COMMITS, newHead), Commit.class);
        HashMap <String, Blob> newdir = newCurr.getDirec();

        if(files != null){
            for(int i = 0; i < files.size(); i++){
                if(!newdir.containsKey(files.get(i))){
                    Utils.restrictedDelete(files.get(i));
                }
            }
        }

        for(String key: newdir.keySet()) {
            File a = new File(key);
            Blob b = newdir.get(key);
            byte[] mem = b.mem;
            File f = new File(key);
            try {
                f.createNewFile();
            } catch (IOException e) {
                System.out.println("Can't Create a new file in directory");
            }
            Utils.writeContents(f, mem);
        }

        _head = newCurr.get_shaID();
        String newBranch = branch;
        Utils.writeContents(Utils.join(Repository.GITDIR, "head") ,_head);
        Utils.writeContents(Utils.join(Repository.GITDIR, "currbranch") ,newBranch);


        HashMap<String, Blob> staged = Utils.readObject(Utils.join(Repository.GITDIR, "staged"), HashMap.class);
        HashMap<String, Blob> removed = Utils.readObject(Utils.join(Repository.GITDIR, "removed"), HashMap.class);
        staged.clear();
        removed.clear();
        Utils.writeObject(Utils.join(Repository.GITDIR, "staged"), staged);
        Utils.writeObject(Utils.join(Repository.GITDIR, "removed"), removed);

    }

    /**  Checks out all the files tracked by the given commit. Removes tracked files that are not present in that commit.
     Also moves the current branch’s head to that commit node.
     See the intro for an example of what happens to the head pointer after using reset. The [commit id] may be abbreviated as for checkout.
     The staging area is cleared. The command is essentially checkout of an arbitrary commit that also changes the current branch head. */
    /*Failure case: If no commit with the given id exists, print No commit with that id exists.
    If a working file is untracked in the current branch and would be overwritten by the reset,
    print There is an untracked file in the way; delete it, or add and commit it first. and exit; perform this check before doing anything else.
     */

    public static void reset(String commitID){
        String currHead = Utils.readContentsAsString(Utils.join(Repository.GITDIR, "head"));
        Commit curr = Utils.readObject(Utils.join(Repository.COMMITS, currHead),Commit.class);
        List<String> files = Utils.plainFilenamesIn(CWD);
        HashMap<String, Blob> currdir = curr.getDirec();
        Commit newHead = Utils.readObject(Utils.join(Repository.COMMITS, currHead),Commit.class);
        HashMap <String, Blob> newdir = newHead.getDirec();
     /*   if(!files.isEmpty()) {
            for (int i = 0; i < files.size(); i++) {
                if (!currdir.containsKey(files.get(i)) && !newdir.containsKey(files.get(i))) {
                    System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                    return;
                }
            }
        } */
        HashSet<String> s = Utils.readObject(Utils.join(Repository.GITDIR, "everyCommit"), HashSet.class);
        if(!s.contains(commitID)){
            System.out.print("No commit with that id exists.");
            return;
        }
        if(files != null){
            for(int i = 0; i < files.size(); i++){
                if(!newdir.containsKey(files.get(i))){
                    Utils.restrictedDelete(files.get(i));
                }
            }
        }

        for(String key: newdir.keySet()) {
            File a = new File(key);
            Blob b = newdir.get(key);
            byte[] mem = b.mem;
            File f = new File(key);
            try {
                f.createNewFile();
            } catch (IOException e) {
                System.out.println("Can't Create a new file in directory");
            }
            Utils.writeContents(f, mem);
        }

        _head = newHead.get_shaID();
        Utils.writeContents(Utils.join(Repository.GITDIR, "head") ,_head);

    }


    public static void merge(String branch){
        String currBranch = Utils.readContentsAsString(Utils.join(Repository.GITDIR, "currbranch"));
        HashMap<String, String> branches = Utils.readObject(Utils.join(Repository.GITDIR,"branches"), HashMap.class);
        if(!branches.containsKey(branch)){
            System.out.print("A branch with that name does not exist");
            return;
        }
        String givenheadID = branches.get(branch);
        String currheadID = branches.get(currBranch);
        Commit givenhead = Utils.readObject(Utils.join(Repository.COMMITS, givenheadID),Commit.class);
        Commit currHead = Utils.readObject(Utils.join(Repository.COMMITS, currheadID),Commit.class);
        String splitID = findSplit(branch, currBranch);
        Commit split = Utils.readObject(Utils.join(Repository.COMMITS, currheadID),Commit.class);
        List<String> files = Utils.plainFilenamesIn(CWD);
        if(files != null) {
            for (int i = 0; i < files.size(); i++) {
                if (!currHead.getDirec().containsKey(files.get(i))) {
                    System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                    return;
                }
            }
        }
        HashMap<String, Blob> staged = Utils.readObject(Utils.join(Repository.GITDIR, "staged"), HashMap.class);
        HashMap<String, Blob> removed = Utils.readObject(Utils.join(Repository.GITDIR, "removed"),HashMap.class);
        if(!staged.isEmpty() && !removed.isEmpty()){
            System.out.print("You have uncommitted changes.");
            System.exit(0);
        }

        if(branch.equals(currBranch)){
            System.out.print("Cannot merge a branch with itself");
            return;

        }

        if(splitID.equals(branch)){
            System.out.print("Given branch is an ancestor of the current branch.");
            return;
        }
        if(splitID.equals(currBranch)){
            checkoutBranch(branch);
            System.out.print("Current branch fast-forwarded.");
            return;
        }
        HashMap<String, Blob> currFiles = currHead.getDirec();
        HashMap<String, Blob> giveFiles = givenhead.getDirec();
        HashMap<String, Blob> splitFiles = split.getDirec();
        boolean conflicted = false;


        for(String name: giveFiles.keySet()) {
            if (splitFiles.containsKey(name)) {
                if (giveFiles.containsKey(name) && currFiles.containsKey(name)) {
                    Blob a = currFiles.get(name);
                    Blob b = giveFiles.get(name);
                    Blob c = splitFiles.get(name);
                    //Conflicted merge
                    if (!b._shaID.equals(a._shaID)) {
                        conflicted = true;
                        File newfile = new File(name);
                        String currfile = Utils.readContentsAsString(Utils.join(Repository.BLOBS, a._shaID));
                        String givefile= Utils.readContentsAsString(Utils.join(Repository.BLOBS, b._shaID));
                        Utils.writeContents(newfile,  "<<<<<<< HEAD" + currfile + "=======" +givefile + ">>>>>>>");
                        Blob f =  new Blob(newfile);
                        staged.put(name, f);

                    }
                    //Case 1
                    if (a._shaID.equals(c._shaID)) {
                        if (!b._shaID.equals(a._shaID)) {
                            checkoutID(givenheadID, name);
                            staged.put(name, b);
                        }
                    }
                }
                if (!giveFiles.containsKey(name) && currFiles.containsKey(name)) {
                    Blob a = currFiles.get(name);
                    Blob c = splitFiles.get(name);
                    if (a._shaID.equals(c._shaID)) {
                        rm(name);
                        Utils.restrictedDelete(name);
                    }
                }
            }
            else if(!currFiles.containsKey(name)) {
                Blob b = giveFiles.get(name);
                checkoutID(givenheadID, name);
                staged.put(name, b);
            }
        }
        if(conflicted == true){
            System.out.print("Encountered a merge conflict.");
        }
        String message = "Merged [" + branch +"] into [" + currBranch + "].";
        Commit merge = new Commit(message, currheadID, givenheadID, staged);

    }




    public static String findSplit(String Branch1, String Branch2){
        HashMap<String, String> branches = Utils.readObject(Utils.join(Repository.GITDIR,"branches"), HashMap.class);
        String currHead = branches.get(Branch1);
        Commit curr = Utils.readObject(Utils.join(Repository.COMMITS, currHead),Commit.class);
        ArrayList<String> B1 = new ArrayList<String>();
        while (curr.getParentID()!= null){
            B1.add(curr.get_shaID());
            curr = curr.fromFile(curr.getParentID());
        }
        String head2 = branches.get(Branch2);
        Commit b2 = Utils.readObject(Utils.join(Repository.COMMITS, head2),Commit.class);
        while(b2.getParentID()!= null){
            if(B1.contains(b2.get_shaID())){
                return b2.get_shaID();
            }
            b2 = b2.fromFile(b2.getParentID());
        }
        return b2.get_shaID();
    }
}