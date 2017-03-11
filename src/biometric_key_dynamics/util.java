package biometric_key_dynamics;

import java.io.File;

/**
 * @author Chanuka Wijayakoon
 */

public class util {
    
    
    public static boolean checkUsername(String username) {
        File test = new File("identities/" + username + "-aft.ser");
        boolean exists = test.exists();
        return exists;
    }
}
