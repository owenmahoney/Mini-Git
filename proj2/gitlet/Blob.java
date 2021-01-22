package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

public class Blob implements Serializable {


    public File name;
    public String _shaID;
    public byte[] mem;


    public Blob(File f){

        mem = Utils.readContents(f);
        _shaID = Utils.sha1(mem);

    }

    public void _save(){
        File sha1 = Utils.join(Repository.BLOBS, _shaID);
        try{
            sha1.createNewFile();
        } catch (IOException e) {
            System.out.println("Can't Create File");
        }
        Utils.writeObject(sha1, mem);

    }

    public Blob fromFile(String sha1){
        File d = Utils.join(Repository.BLOBS, sha1);
        if (d.exists()) {
            return Utils.readObject(d, Blob.class);
        }
        else{
            throw new IllegalArgumentException("File Doesn't Exist"); //FIXME
        }
    }

    }



