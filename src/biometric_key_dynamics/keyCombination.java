package biometric_key_dynamics;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import static biometric_key_dynamics.util.*;

/**
 * @author Chanuka Wijayakoon
 */
public class keyCombination implements Serializable {

    private HashMap<Integer, Long> keyMap;
    private HashMap<ArrayList<Integer>, Long> digraphs;
    private String owner;
    private long avgFlightTime;

    public keyCombination(String owner) {
        this.owner = owner;
        this.keyMap = new HashMap(26);
        this.digraphs = new HashMap(650);
    }

    public void setUsername(String username) {
        this.owner = username;
    }

    public boolean addKey(int keyCode, long dwellTime) {
        try {
            if (keyMap.containsKey(keyCode)) {
                if (Math.abs(keyMap.get(keyCode) - dwellTime) > 500000000) {
                    System.out.println("too late for dwellTime.");
                    return false;
                }

                dwellTime = (dwellTime + keyMap.get(keyCode)) / 2;

                keyMap.put(keyCode, dwellTime);
//                System.out.println("key " + KeyEvent.getKeyText(keyCode) + " pressed with dwellTime " + dwellTime);
                return true;
            }

            keyMap.put(keyCode, dwellTime);
//            System.out.println("New key " + KeyEvent.getKeyText(keyCode) + " pressed with dwellTime " + dwellTime);
            return true;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
        }
        return false;

    }

    public boolean addKeyPair(ArrayList<Integer> keys, long flightTime) {
        if (digraphs.containsKey(keys)) {
            if (Math.abs(digraphs.get(keys) - flightTime) > 500000000) {
                System.out.println("too late for flightTime.");
                return false;
            }

            flightTime = (flightTime + digraphs.get(keys)) / 2;
            digraphs.put(keys, flightTime);
            return true;

        }

        flightTime = (flightTime + digraphs.get(keys)) / 2;
        digraphs.put(keys, flightTime);
        return true;

    }

    public void save() {
        writeFile(owner, "keymap", this.keyMap);
        writeFile(owner, "digraphs", this.digraphs);
        writeFile(owner, "aft", this.avgFlightTime);
    }

    public HashMap<Integer, Long> getKeyMap() {
        return keyMap;
    }

    public void clear() {
        this.keyMap.clear();
        this.digraphs.clear();
    }

    public void setKeyMap(HashMap<Integer, Long> keyMap) {
        this.keyMap = keyMap;
    }

    public void setDigraphs(HashMap<ArrayList<Integer>, Long> digraphs) {
        this.digraphs = digraphs;
    }

    public void setAft(long aft) {
        this.avgFlightTime = aft;
    }

    public static keyCombination open(String username) {
        boolean user_exists = util.checkUsername(username);

        if (user_exists) {
            long aft_loaded = (long) openFile(username, "aft");
            HashMap<Integer, Long> keyMap_loaded = (HashMap<Integer, Long>) openFile(username, "keymap");
            HashMap<ArrayList<Integer>, Long> digraphs_loaded = (HashMap<ArrayList<Integer>, Long>) openFile(username, "digraphs");
            keyCombination kc = new keyCombination(username);

            kc.setKeyMap(keyMap_loaded);
            kc.setDigraphs(digraphs_loaded);
            kc.setAft(aft_loaded);

            return kc;
        }

        return null;
    }

}
