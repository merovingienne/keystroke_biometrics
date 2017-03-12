package biometric_key_dynamics;

import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

/**
 * @author Chanuka Wijayakoon
 */
public class keyDynamics {

    private static JFrame frame;

    private HashMap<Integer, Long> keyLog;
    private HashMap<Integer, Integer> keyCount;
    private HashMap<Integer, Long> keyDuration;
    private HashMap<ArrayList<Integer>, Long> keyFlights;
    private int lastKey = -1;
    private long keyDownTime = -1, keyUpTime = -1;
    private keyCombination kc;

    private boolean verified = false;

    private HashMap<Integer, Long> verifyKeyLog;
    private HashMap<Integer, Integer> verifyKeyCount;
    private HashMap<Integer, Long> verifyKeyDuration;
    private HashMap<ArrayList<Integer>, Long> verifyKeyFlights;
    private keyCombination vkc;

//    GUI
    private final int WIDTH = 640;
    private final int HEIGHT = 480;

    private static JPanel trainScreen, verifyScreen, loginScreen;

    public keyDynamics() {
        frame = new JFrame("Key Catcher");
        Container content = frame.getContentPane();
        frame.setLayout(null);
        frame.setSize(WIDTH, HEIGHT);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBackground(new Color(204, 204, 204));

        frame.add(loginScreen());

        frame.setVisible(true);
    }

    /**
     * login screen *
     */
    private JPanel loginScreen() {
        loginScreen = new JPanel(null);
        loginScreen.setSize(WIDTH, HEIGHT);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(40, 50, 100, 20);

        JTextField username = new JTextField();
        username.setBounds(200, 50, 100, 20);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(320, 50, 80, 25);

        JLabel loginFailure = new JLabel("No such account!");
        loginFailure.setBounds(420, 50, 120, 25);
        loginFailure.setVisible(false);

        JButton trainButton = new JButton("Train new user");
        trainButton.setBounds(260, 150, 120, 25);

        JLabel instructions = new JLabel("Instructions");
        instructions.setBounds(40, 200, 100, 30);

        JLabel instruction1 = new JLabel("*If you already have an account, enter username at top.");
        instruction1.setBounds(40, 240, 560, 20);

        JLabel instruction2 = new JLabel("*To register and train new account, click 'Train new user'.");
        instruction2.setBounds(40, 260, 560, 20);

        JLabel instruction3 = new JLabel("*This app uses key dwell time (time a"
                + " key is kept pressed down) to identify users.");
        instruction3.setBounds(40, 280, 560, 20);

        loginScreen.add(usernameLabel);
        loginScreen.add(username);
        loginScreen.add(loginButton);
        loginScreen.add(loginFailure);
        loginScreen.add(trainButton);
        loginScreen.add(instructions);
        loginScreen.add(instruction1);
        loginScreen.add(instruction2);
        loginScreen.add(instruction3);

        loginScreen.setVisible(true);

        trainButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                trainScreen = trainScreen();
                loginScreen.setVisible(false);

            }
        });

        loginButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                kc = keyCombination.open(username.getText());

                if (kc != null) {
                    verifyScreen = verifyScreen(true);
                    loginScreen.setVisible(false);
                } else {
                    loginFailure.setVisible(true);
                }
            }
        });

        return loginScreen;
    }

    /**
     * Training screen *
     */
    private JPanel trainScreen() {
        // Variable initialization
        if (keyLog != null) {
            keyLog.clear();
            keyCount.clear();
            keyDuration.clear();
            keyFlights.clear();
        } else {
            keyLog = new HashMap();
            keyCount = new HashMap();
            keyDuration = new HashMap();
            keyFlights = new HashMap();
        }

        kc = new keyCombination("default");

        trainScreen = new JPanel(null);
        trainScreen.setSize(WIDTH, HEIGHT);

        JLabel usernameLabel = new JLabel("New username");
        usernameLabel.setBounds(40, 50, 100, 20);

        JTextField username = new JTextField();
        username.setBounds(200, 50, 400, 20);
        username.setBackground(new Color(255, 255, 255));

        JLabel testTextLabel = new JLabel("Enter text here:");
        testTextLabel.setBounds(40, 80, 100, 20);

        JTextArea testText = new JTextArea();
        testText.setBounds(200, 80, 400, 100);
        testText.setBackground(new Color(255, 255, 255));
        testText.setLineWrap(true);

        JButton verifyB = new JButton("Verify yourself");
        verifyB.setBounds(260, 230, 120, 20);
        verifyB.setEnabled(false);

        JButton clearButton = new JButton("Clear");
        clearButton.setBounds(500, 230, 80, 20);

        JButton backButton = new JButton("Back");
        backButton.setBounds(40, 230, 80, 20);

        JLabel instructions = new JLabel("Instructions:");
        instructions.setBounds(40, 275, 100, 20);

        JLabel instruction1 = new JLabel("Please click clear button if you wish "
                + "to do a new training after a verification.");
        instruction1.setBounds(40, 295, 560, 40);

        trainScreen.add(usernameLabel);
        trainScreen.add(username);
        trainScreen.add(testTextLabel);
        trainScreen.add(testText);
        trainScreen.add(verifyB);
        trainScreen.add(clearButton);
        trainScreen.add(backButton);
        trainScreen.add(instructions);
        trainScreen.add(instruction1);

        trainScreen.setVisible(true);
        frame.add(trainScreen);

        /**
         * username field *
         */
        username.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent evt) {
                String un = username.getText();
                boolean exists = util.checkUsername(un);
                if (un.length() > 0 && (!exists)) {
                    verifyB.setEnabled(true);
                } else {
                    verifyB.setEnabled(false);
                }
            }
        });

        /**
         * training text key press *
         */
        testText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() > 59 && evt.getKeyCode() < 91) {
                    keyDownTime = System.nanoTime();

                    Runnable runner1 = new Runnable() {
                        @Override
                        public void run() {
                            int keyCode = evt.getKeyCode();
                            keyLog.put(keyCode, keyDownTime);
                            keyCount.put(keyCode,
                                    keyCount.containsKey(keyCode)
                                            ? keyCount.get(keyCode) + 1 : 1);
                        }
                    };

                    Thread keyPressed = new Thread(runner1);
                    keyPressed.start();
                }
            }

            /**
             * training text key up *
             */
            @Override
            public void keyReleased(KeyEvent evt) {
                // detect only alphabetic inputs
                if (59 < evt.getKeyCode() && evt.getKeyCode() < 91) {
                    keyUpTime = System.nanoTime();
                    int keyCode = evt.getKeyCode();
                    long dwellTime = keyUpTime - keyLog.get(keyCode);
                    keyDuration.put(keyCode, keyDuration.containsKey(keyCode)
                            ? keyDuration.get(keyCode) + dwellTime : dwellTime);

                    Runnable runner2 = new Runnable() {
                        @Override
                        public void run() {
                            kc.addKey(evt.getKeyCode(), (keyDuration.get(keyCode))
                                    / (keyCount.get(keyCode)));
                        }
                    };

                    Thread keyRel = new Thread(runner2);
                    keyRel.start();
                }
            }
        });

        /*Action listener for verify button */
        verifyB.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                kc.setUsername(username.getText());
                verifyScreen(false);
                trainScreen.setVisible(false);
            }
        });

        /* Action listener for clear button*/
        clearButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                keyLog.clear();
                keyCount.clear();
                keyDuration.clear();
                username.setText("");
                testText.setText("");
            }
        });

        /* Action listener for back button */
        backButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                keyLog.clear();
                keyCount.clear();
                keyDuration.clear();
                username.setText("");
                testText.setText("");
                loginScreen.setVisible(true);
                trainScreen.setVisible(false);
            }
        });

        return trainScreen;
    }

    /**
     * verification screen *
     */
    private JPanel verifyScreen(boolean isFromLogin) {
        verifyScreen = new JPanel(null);
        verifyScreen.setSize(WIDTH, HEIGHT);

        verifyKeyLog = new HashMap();
        verifyKeyCount = new HashMap();
        verifyKeyDuration = new HashMap();
        vkc = new keyCombination("runner");

        JButton backButton = new JButton();
        backButton.setBounds(40, 250, 80, 20);
        backButton.setText("Back");

        JButton verifyButton = new JButton();
        verifyButton.setBounds(280, 250, 80, 20);
        verifyButton.setText("Verify");

        JButton clearButton = new JButton();
        clearButton.setBounds(500, 250, 80, 20);
        clearButton.setText("Clear");

        JTextArea verifyText = new JTextArea();
        verifyText.setBounds(40, 40, 540, 200);

        JLabel verification = new JLabel();
        verification.setBounds(280, 280, 80, 20);
        verification.setVisible(false);

        JButton saveButton = new JButton("Save");
        saveButton.setBounds(500, 280, 80, 20);
        saveButton.setEnabled(false);

        JLabel instructions = new JLabel("Instructions");
        instructions.setBounds(40, 300, 100, 20);

        JLabel instruction1 = new JLabel("*Please press 'Clear' before trying"
                + " to verify again. Deleting text won't reset system.");
        instruction1.setBounds(40, 330, 560, 20);

        verifyScreen.add(verifyText);
        verifyScreen.add(backButton);
        verifyScreen.add(verifyButton);
        verifyScreen.add(verification);
        verifyScreen.add(clearButton);
        verifyScreen.add(saveButton);
        verifyScreen.add(instructions);
        verifyScreen.add(instruction1);

        verifyScreen.setVisible(true);

        frame.add(verifyScreen);

        /**
         * back button *
         */
        backButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                verifyScreen.setVisible(false);
                if (isFromLogin) {
                    loginScreen.setVisible(true);
                } else {
                    trainScreen.setVisible(true);
                }
            }
        });

        /**
         * verification button action *
         */
        verifyButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                HashMap<Integer, Long> okc = kc.getKeyMap();
                HashMap<Integer, Long> tkc = vkc.getKeyMap();
                HashMap<Integer, Long> deviations = new HashMap();
                boolean empty = true;

                if (okc.size() > 10 && tkc.size() > 10) {
                    empty = false;
                }

                for (int k : tkc.keySet()) {
                    if (okc.containsKey(k)) {
                        long dwellTimeDeviation = (Math.abs(okc.get(k) - tkc.get(k))) / 1000;
                        if (dwellTimeDeviation > 20000) {
                            deviations.put(k, dwellTimeDeviation);
                        }
                    }
                }

                if (deviations.size() > 10 || empty) {
                    verification.setText("Failed!");
                    verification.setVisible(true);
                } else {
                    verified = true;
                    saveButton.setEnabled(true);
                    verification.setText("Success!");
                    verification.setVisible(true);
                }

            }
        });

        /**
         * verification text *
         */
        verifyText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                keyDownTime = System.nanoTime();

                Runnable runner3 = new Runnable() {
                    @Override
                    public void run() {
                        int keyCode = evt.getKeyCode();
                        verifyKeyLog.put(keyCode, keyDownTime);
                        verifyKeyCount.put(keyCode,
                                verifyKeyCount.containsKey(keyCode)
                                        ? verifyKeyCount.get(keyCode) + 1 : 1);
                    }
                };

                Thread verifyKeyPressed = new Thread(runner3);
                verifyKeyPressed.start();
            }

            @Override
            public void keyReleased(KeyEvent evt) {
                keyUpTime = System.nanoTime();
                int keyCode = evt.getKeyCode();
                long dwellTime = keyUpTime - verifyKeyLog.get(keyCode);
                verifyKeyDuration.put(keyCode, verifyKeyDuration.containsKey(keyCode)
                        ? verifyKeyDuration.get(keyCode) + dwellTime : dwellTime);

                Runnable runner4 = new Runnable() {
                    @Override
                    public void run() {
                        vkc.addKey(keyCode,
                                (verifyKeyDuration.get(keyCode)
                                / verifyKeyCount.get(keyCode)));
                    }
                };

                Thread verifyKeyRel = new Thread(runner4);
                verifyKeyRel.start();
            }
        });

        /**
         * clear button *
         */
        clearButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                verifyKeyLog.clear();
                verifyKeyCount.clear();
                verifyKeyDuration.clear();
                verifyText.setText("");
                vkc = new keyCombination("runner");
                verification.setText("");
                verification.setVisible(false);
            }
        });

        saveButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (verified) {
                    kc.save();
                }
            }
        });

        return verifyScreen;

    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new keyDynamics();
            }
        });
    }

}
