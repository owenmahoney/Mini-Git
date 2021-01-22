package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Formatter;

public class Commit implements Serializable {


    private String log_message;
    private Date timestamp;
    private String parentID;
    private String SecondParentID;
    private String _shaID;
    private HashMap<String, Blob> direc;
    private Blob blob;
    private HashMap<String, String> tocommit;
    private HashMap<String, String> toremove;



    public Commit(String message, String parent, String SecondParent, HashMap <String, Blob> d){
        this.log_message = message;
        this.parentID = parent;
        this.direc = d;
        this._shaID = Utils.sha1(Utils.serialize(this));
        this.SecondParentID = SecondParent;
        if(this.parentID == null) {
            this.timestamp = new Date(0);
        }
        else{
            this.timestamp = new Date();
        }
    }
    public void _save() { //need to add remove and clear staged for removal
        File sha1 = Utils.join(Repository.COMMITS, this._shaID);
        try{
            sha1.createNewFile();
        } catch (IOException e) {
            System.out.println("Can't Create File");
        }
        Utils.writeObject(sha1, this);
    }

    public Commit fromFile(String sha1){
        File d = Utils.join(Repository.COMMITS, sha1);
        if (d.exists()) {
            return Utils.readObject(d, Commit.class);
        }
        else{
            throw new IllegalArgumentException("File Doesn't Exist"); //FIXME
        }
    }

    public String getLog_message() {

        return this.log_message;
    }
    public Date getTimestamp() {
        return this.timestamp;

    }

    public String get_shaID() {
        return this._shaID;
    }

    public String getParentID() {
        return this.parentID;
    }

    public String getSecondParentID() {
        return this.SecondParentID;
    }

    public HashMap<String, Blob> getDirec() {
        return direc;
    }
}