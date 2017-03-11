package biometric_key_dynamics;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Chanuka Wijayakoon
 */

public class util {
    public static final String folderPath = "identities/";
    
    
    public static boolean checkUsername(String username) {
        File test = new File(folderPath + username + "-aft.ser");
        boolean exists = test.exists();
        return exists;
    }
    
    public static Object openFile(String owner, String fileName){
        FileInputStream fileInputStream = null;
        ObjectInputStream objectInputStream = null;
        
        try {
            fileInputStream = new FileInputStream(folderPath + owner + "-" + fileName + ".ser");
            objectInputStream = new ObjectInputStream(fileInputStream);
            
            Object obj = objectInputStream.readObject();
            return obj;
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        
        return true;
    }

    public static boolean writeFile(String owner, String fileName, Object obj) {
        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        
        File idDir = new File("identities");
        
        if (!idDir.exists()){
            idDir.mkdir();
        }
        
        try {
            fileOutputStream = new FileOutputStream(folderPath + owner + "-" +fileName + ".ser");
            objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(obj);
            return true;
        } catch (Exception ex) {
            Logger.getLogger(keyCombination.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
            return false;
        } finally {
            try {
                fileOutputStream.close();
                objectOutputStream.close();
            } catch (IOException ex) {
                Logger.getLogger(keyCombination.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println(ex.getMessage());
            }
        }
    }
}
