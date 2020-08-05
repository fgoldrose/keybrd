package com.example.rotakey;

import java.util.HashMap;

public class MyKeyboard {

    // Define modes
    public static final int NORMAL = 0;
    public static final int SHIFTED = 1;
    public static final int SYMBOLS = 2;
    public static final int MORESYMBOLS = 3;

    // Define keycodes
    public static final int NONE = -1;

    public static final int INPUT = -2;
    public static final int CAPSLOCK = -3;
    public static final int MODE_CHANGE = -4;
    public static final int ENTER = -5;
    public static final int DELETE = -6;


    public int mode;
    public Key curkey;
    public Key secondkey; // second key being pressed if using multitouch
    public boolean capslock;

    private Key[] keys;
    private HashMap<KeyPair, Action> actions;

    MyKeyboard(){
        this.mode = NORMAL;
        this.curkey = null;
        this.secondkey = null;
        this.keys = new Key[9];
        this.actions = new HashMap<KeyPair, Action>();
        this.capslock = false;

        float angle = 1 / (float) Math.sqrt(2);

        keys[0] = new Key(this,0, 0, 0);
        keys[1] = new Key(this,1, 0, 1);
        keys[2] = new Key(this,2, angle,angle);
        keys[3] = new Key(this,3,1, 0);
        keys[4] = new Key(this,4,angle, -angle);
        keys[5] = new Key(this,5, 0, -1);
        keys[6] = new Key(this,6, -angle, -angle);
        keys[7] = new Key(this,7,-1, 0);
        keys[8] = new Key(this,8,-angle, angle);
    }

    public void reset(){
        this.mode = NORMAL;
        this.curkey = null;
        this.secondkey = null;
        this.capslock = false;
        for (int i = 1; i <= 8; i++) {
            keys[i].setLabel("");
        }
    }

    public Key getKey(int position){
        if (position >=0 && position <=8) {
            return keys[position];
        }
        else{
            return null;
        }
    }

    public Action getAction(Key k1, Key k2){
        if(k1 != null && k2 != null) {
            return actions.get(new KeyPair(k1, k2, mode));
        }
        else{
            return null;
        }
    }

    public void setCurkey(Key k){
        this.curkey = k;
        if(secondkey == null) {
            for (int i = 1; i <= 8; i++) {
                Action ac = getAction(keys[i], k);
                if (ac != null) {
                    keys[i].setLabel(ac.label);
                } else {
                    keys[i].setLabel("");
                }
            }
            if (k != null && capslock && k.position == 0) {
                keys[5].setLabel("lower");
            }
        }
    }

    public void setSecondkey(Key k) {
        this.secondkey = k;
        for (int i = 1; i <= 8; i++) {
            keys[i].setLabel("");
        }
    }

    class Action {
        int action;
        int arg;
        String text; //text to send if action is input

        String label;

        boolean repeatable;

        Action(int action, int arg, String label){
            this.action = action;
            this.arg = arg;
            this.label = label;
        }

        Action(int action, String text){
            this.action = action;
            this.text = text;
            this.label = text;
        }
    }

    class Key {

        int position;
        MyKeyboard parent;

        // offsets to get to center of key when multiplied by keyboard radius
        float xoffset;
        float yoffset;

        // Current key label to display
        String label = "";

        // Action on single key click, if any
        Action clickaction;

        Key(MyKeyboard parent, int p, float xoffset, float yoffset) {
            this.parent = parent;
            this.position = p;
            this.xoffset = xoffset;
            this.yoffset = yoffset;
        }

        public void setLabel(String l){
            this.label = l;
        }
    }

    class KeyPair {
        Key key1;
        Key key2;
        int m;

        KeyPair(Key k1, Key k2, int m){
            if(k1.position < k2.position){
                this.key1 = k1;
                this.key2 = k2;
            }
            else{
                this.key1 = k2;
                this.key2 = k1;
            }
            this.m = m;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof KeyPair)) return false;
            KeyPair kp = (KeyPair) o;
            return key1 == kp.key1 && key2 == kp.key2 && m == kp.m;
        }

        @Override
        public int hashCode() {
            int hash = 17;
            hash = 31 * hash + key1.hashCode();
            hash = 31 * hash + key2.hashCode();
            hash = 31 * hash + m;
            return hash;
        }

    }

    public void setupCenter(){
        // Center related
        keys[0].clickaction = new Action(INPUT, " ");
        actions.put(new KeyPair(keys[0], keys[1], NORMAL), new Action(INPUT, "."));
        actions.put(new KeyPair(keys[0], keys[2], NORMAL), new Action(INPUT, "?"));
        actions.put(new KeyPair(keys[0], keys[3], NORMAL), new Action(ENTER, NONE, "Enter"));
        actions.put(new KeyPair(keys[0], keys[4], NORMAL), new Action(MODE_CHANGE, SYMBOLS,"Sym"));
        actions.put(new KeyPair(keys[0], keys[5], NORMAL), new Action(MODE_CHANGE, SHIFTED, "Shift"));
        actions.put(new KeyPair(keys[0], keys[6], NORMAL), new Action(INPUT, "!"));
        actions.put(new KeyPair(keys[0], keys[7], NORMAL), new Action(DELETE, NONE, "<--"));
        actions.put(new KeyPair(keys[0], keys[8], NORMAL), new Action(INPUT, ","));

        actions.put(new KeyPair(keys[0], keys[1], SHIFTED), new Action(INPUT, "."));
        actions.put(new KeyPair(keys[0], keys[2], SHIFTED), new Action(INPUT, "?"));
        actions.put(new KeyPair(keys[0], keys[3], SHIFTED), new Action(ENTER, NONE, "Enter"));
        actions.put(new KeyPair(keys[0], keys[4], SHIFTED), new Action(MODE_CHANGE, SYMBOLS,"Sym"));
        actions.put(new KeyPair(keys[0], keys[5], SHIFTED), new Action(CAPSLOCK, NONE, "CAPS"));
        actions.put(new KeyPair(keys[0], keys[6], SHIFTED), new Action(INPUT, "!"));
        actions.put(new KeyPair(keys[0], keys[7], SHIFTED), new Action(DELETE, NONE, "<--"));
        actions.put(new KeyPair(keys[0], keys[8], SHIFTED), new Action(INPUT, ","));

        actions.put(new KeyPair(keys[0], keys[1], SYMBOLS), new Action(INPUT, "."));
        actions.put(new KeyPair(keys[0], keys[2], SYMBOLS), new Action(INPUT, "?"));
        actions.put(new KeyPair(keys[0], keys[3], SYMBOLS), new Action(ENTER, NONE, "Enter"));
        actions.put(new KeyPair(keys[0], keys[4], SYMBOLS), new Action(MODE_CHANGE, MORESYMBOLS,"More"));
        actions.put(new KeyPair(keys[0], keys[5], SYMBOLS), new Action(MODE_CHANGE, NORMAL, "Alph"));
        actions.put(new KeyPair(keys[0], keys[6], SYMBOLS), new Action(INPUT, "!"));
        actions.put(new KeyPair(keys[0], keys[7], SYMBOLS), new Action(DELETE, NONE, "<--"));
        actions.put(new KeyPair(keys[0], keys[8], SYMBOLS), new Action(INPUT, ","));
    }

    public void setupNormal(){
        // and also shifted
        //Normal mode
        actions.put(new KeyPair(keys[7], keys[2], NORMAL), new Action(INPUT, "'"));
        actions.put(new KeyPair(keys[3], keys[2], NORMAL), new Action(INPUT, "\""));

        actions.put(new KeyPair(keys[6], keys[7], NORMAL), new Action(INPUT, "y"));
        actions.put(new KeyPair(keys[4], keys[3], NORMAL), new Action(INPUT, "w"));
        actions.put(new KeyPair(keys[7], keys[3], NORMAL), new Action(INPUT, "q"));
        actions.put(new KeyPair(keys[6], keys[1], NORMAL), new Action(INPUT, "r"));
        actions.put(new KeyPair(keys[4], keys[2], NORMAL), new Action(INPUT, "z"));
        actions.put(new KeyPair(keys[3], keys[1], NORMAL), new Action(INPUT, "d"));
        actions.put(new KeyPair(keys[8], keys[5], NORMAL), new Action(INPUT, "s"));
        actions.put(new KeyPair(keys[6], keys[4], NORMAL), new Action(INPUT, "a"));
        actions.put(new KeyPair(keys[5], keys[2], NORMAL), new Action(INPUT, "k"));
        actions.put(new KeyPair(keys[7], keys[8], NORMAL), new Action(INPUT, "u"));
        actions.put(new KeyPair(keys[6], keys[8], NORMAL), new Action(INPUT, "o"));
        actions.put(new KeyPair(keys[3], keys[5], NORMAL), new Action(INPUT, "v"));
        actions.put(new KeyPair(keys[6], keys[2], NORMAL), new Action(INPUT, "p"));
        actions.put(new KeyPair(keys[1], keys[2], NORMAL), new Action(INPUT, "x"));
        actions.put(new KeyPair(keys[7], keys[1], NORMAL), new Action(INPUT, "l"));
        actions.put(new KeyPair(keys[8], keys[1], NORMAL), new Action(INPUT, "i"));
        actions.put(new KeyPair(keys[4], keys[5], NORMAL), new Action(INPUT, "h"));
        actions.put(new KeyPair(keys[6], keys[5], NORMAL), new Action(INPUT, "c"));
        actions.put(new KeyPair(keys[4], keys[8], NORMAL), new Action(INPUT, "t"));
        actions.put(new KeyPair(keys[6], keys[3], NORMAL), new Action(INPUT, "j"));
        actions.put(new KeyPair(keys[8], keys[2], NORMAL), new Action(INPUT, "m"));
        actions.put(new KeyPair(keys[4], keys[1], NORMAL), new Action(INPUT, "n"));
        actions.put(new KeyPair(keys[7], keys[5], NORMAL), new Action(INPUT, "b"));
        actions.put(new KeyPair(keys[4], keys[7], NORMAL), new Action(INPUT, "g"));
        actions.put(new KeyPair(keys[1], keys[5], NORMAL), new Action(INPUT, "e"));
        actions.put(new KeyPair(keys[3], keys[8], NORMAL), new Action(INPUT, "f"));

        // Shifted mode
        actions.put(new KeyPair(keys[7], keys[2], SHIFTED), new Action(INPUT, "'"));
        actions.put(new KeyPair(keys[3], keys[2], SHIFTED), new Action(INPUT, "\""));

        actions.put(new KeyPair(keys[6], keys[7], SHIFTED), new Action(INPUT, "Y"));
        actions.put(new KeyPair(keys[4], keys[3], SHIFTED), new Action(INPUT, "W"));
        actions.put(new KeyPair(keys[7], keys[3], SHIFTED), new Action(INPUT, "Q"));
        actions.put(new KeyPair(keys[6], keys[1], SHIFTED), new Action(INPUT, "R"));
        actions.put(new KeyPair(keys[4], keys[2], SHIFTED), new Action(INPUT, "Z"));
        actions.put(new KeyPair(keys[3], keys[1], SHIFTED), new Action(INPUT, "D"));
        actions.put(new KeyPair(keys[8], keys[5], SHIFTED), new Action(INPUT, "S"));
        actions.put(new KeyPair(keys[6], keys[4], SHIFTED), new Action(INPUT, "A"));
        actions.put(new KeyPair(keys[5], keys[2], SHIFTED), new Action(INPUT, "K"));
        actions.put(new KeyPair(keys[7], keys[8], SHIFTED), new Action(INPUT, "U"));
        actions.put(new KeyPair(keys[6], keys[8], SHIFTED), new Action(INPUT, "O"));
        actions.put(new KeyPair(keys[3], keys[5], SHIFTED), new Action(INPUT, "V"));
        actions.put(new KeyPair(keys[6], keys[2], SHIFTED), new Action(INPUT, "P"));
        actions.put(new KeyPair(keys[1], keys[2], SHIFTED), new Action(INPUT, "X"));
        actions.put(new KeyPair(keys[7], keys[1], SHIFTED), new Action(INPUT, "L"));
        actions.put(new KeyPair(keys[8], keys[1], SHIFTED), new Action(INPUT, "I"));
        actions.put(new KeyPair(keys[4], keys[5], SHIFTED), new Action(INPUT, "H"));
        actions.put(new KeyPair(keys[6], keys[5], SHIFTED), new Action(INPUT, "C"));
        actions.put(new KeyPair(keys[4], keys[8], SHIFTED), new Action(INPUT, "T"));
        actions.put(new KeyPair(keys[6], keys[3], SHIFTED), new Action(INPUT, "J"));
        actions.put(new KeyPair(keys[8], keys[2], SHIFTED), new Action(INPUT, "M"));
        actions.put(new KeyPair(keys[4], keys[1], SHIFTED), new Action(INPUT, "N"));
        actions.put(new KeyPair(keys[7], keys[5], SHIFTED), new Action(INPUT, "B"));
        actions.put(new KeyPair(keys[4], keys[7], SHIFTED), new Action(INPUT, "G"));
        actions.put(new KeyPair(keys[1], keys[5], SHIFTED), new Action(INPUT, "E"));
        actions.put(new KeyPair(keys[3], keys[8], SHIFTED), new Action(INPUT, "F"));
    }

    public void setupSymbols(){
        actions.put(new KeyPair(keys[1], keys[2], SYMBOLS), new Action(INPUT, "0"));
        actions.put(new KeyPair(keys[1], keys[3], SYMBOLS), new Action(INPUT, "1"));
        actions.put(new KeyPair(keys[1], keys[4], SYMBOLS), new Action(INPUT, "2"));
        actions.put(new KeyPair(keys[1], keys[5], SYMBOLS), new Action(INPUT, "3"));
        actions.put(new KeyPair(keys[1], keys[6], SYMBOLS), new Action(INPUT, "4"));
        actions.put(new KeyPair(keys[1], keys[7], SYMBOLS), new Action(INPUT, "5"));
        actions.put(new KeyPair(keys[1], keys[8], SYMBOLS), new Action(INPUT, "6"));
        actions.put(new KeyPair(keys[2], keys[3], SYMBOLS), new Action(INPUT, "7"));
        actions.put(new KeyPair(keys[2], keys[4], SYMBOLS), new Action(INPUT, "8"));
        actions.put(new KeyPair(keys[2], keys[5], SYMBOLS), new Action(INPUT, "9"));
        actions.put(new KeyPair(keys[2], keys[6], SYMBOLS), new Action(INPUT, "+"));
        actions.put(new KeyPair(keys[2], keys[7], SYMBOLS), new Action(INPUT, "-"));
        actions.put(new KeyPair(keys[2], keys[8], SYMBOLS), new Action(INPUT, "="));
        actions.put(new KeyPair(keys[3], keys[4], SYMBOLS), new Action(INPUT, "@"));
        actions.put(new KeyPair(keys[3], keys[5], SYMBOLS), new Action(INPUT, "#"));
        actions.put(new KeyPair(keys[3], keys[6], SYMBOLS), new Action(INPUT, "$"));
        actions.put(new KeyPair(keys[3], keys[7], SYMBOLS), new Action(INPUT, "%"));
        actions.put(new KeyPair(keys[3], keys[8], SYMBOLS), new Action(INPUT, "&"));
        actions.put(new KeyPair(keys[4], keys[5], SYMBOLS), new Action(INPUT, "*"));
        actions.put(new KeyPair(keys[4], keys[6], SYMBOLS), new Action(INPUT, "/"));
        actions.put(new KeyPair(keys[4], keys[7], SYMBOLS), new Action(INPUT, "("));
        actions.put(new KeyPair(keys[4], keys[8], SYMBOLS), new Action(INPUT, ")"));
        actions.put(new KeyPair(keys[5], keys[6], SYMBOLS), new Action(INPUT, "<"));
        actions.put(new KeyPair(keys[5], keys[7], SYMBOLS), new Action(INPUT, ">"));
        actions.put(new KeyPair(keys[5], keys[8], SYMBOLS), new Action(INPUT, "^"));
        actions.put(new KeyPair(keys[6], keys[7], SYMBOLS), new Action(INPUT, ":"));
        actions.put(new KeyPair(keys[6], keys[8], SYMBOLS), new Action(INPUT, ";"));
        actions.put(new KeyPair(keys[7], keys[8], SYMBOLS), new Action(INPUT, "_"));
    }
}


