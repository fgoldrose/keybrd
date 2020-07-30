package com.example.keybird;

import java.util.HashMap;

public class Keyboard {

    // Define modes
    public static final int NORMAL = 0;
    public static final int SHIFTED = 1;
    public static final int SHIFTLOCK = 2;
    public static final int SYMBOLS = 3;

    // Define keycodes
    public static final int NONE = -1;

    public static final int INPUT = -2;
    public static final int SHIFT = -3;
    public static final int MODE_CHANGE = -4;
    public static final int ENTER = -5;
    public static final int DELETE = -6;


    public int mode;
    public Key curkey;
    private Key[] keys;
    private HashMap<KeyPair, Action> actions;

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
        for(int i = 0; i < 9; i++){
            Action ac = getAction(keys[i], k);
            if (ac != null) {
                keys[i].setLabel(ac.label);
            }
            else{
                keys[i].setLabel("");
            }
        }
    }

    class Action {
        int action;
        int arg;
        String text; //Maybe send text instead of a key code.

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

    class Key {

        int position;
        Keyboard parent;

        // offsets to get to center of key when multiplied by keyboard radius
        float xoffset;
        float yoffset;

        // Current key label to display
        String label = "";

        Action clickaction;

        HashMap<Key, KeyPair> keypairs;

        Key(Keyboard parent, int p, float xoffset, float yoffset) {
            this.parent = parent;
            this.position = p;
            this.xoffset = xoffset;
            this.yoffset = yoffset;
        }
        public KeyPair to(Key k){
            return keypairs.get(k);
        }

        public void setKeymap(HashMap<Key, KeyPair> kp) {
            this.keypairs = kp;
        }

        public void setLabel(String l){
            this.label = l;
        }
    }

    Keyboard(){
        this.mode = NORMAL;
        this.curkey = null;
        this.keys = new Key[9];
        this.actions = new HashMap<KeyPair, Action>();

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

    public void setupDefault(){
        // for testing
        keys[0].clickaction = new Action(INPUT, " ");
        actions.put(new KeyPair(keys[0], keys[1], NORMAL), new Action(INPUT, "."));
        actions.put(new KeyPair(keys[0], keys[2], NORMAL), new Action(INPUT, "?"));
        actions.put(new KeyPair(keys[0], keys[3], NORMAL), new Action(ENTER, NONE, "Enter"));
        actions.put(new KeyPair(keys[0], keys[5], NORMAL), new Action(SHIFT, NONE, "Shift"));
        actions.put(new KeyPair(keys[0], keys[6], NORMAL), new Action(INPUT, "!"));
        actions.put(new KeyPair(keys[0], keys[7], NORMAL), new Action(DELETE, NONE, "<--"));
        actions.put(new KeyPair(keys[0], keys[8], NORMAL), new Action(INPUT, ","));
        actions.put(new KeyPair(keys[1], keys[3], NORMAL), new Action(INPUT, "y"));
        actions.put(new KeyPair(keys[2], keys[4], NORMAL), new Action(INPUT, "w"));
        actions.put(new KeyPair(keys[3], keys[4], NORMAL), new Action(INPUT, "q"));
        actions.put(new KeyPair(keys[1], keys[6], NORMAL), new Action(INPUT, "r"));
        actions.put(new KeyPair(keys[2], keys[8], NORMAL), new Action(INPUT, "z"));
        actions.put(new KeyPair(keys[4], keys[6], NORMAL), new Action(INPUT, "d"));
        actions.put(new KeyPair(keys[5], keys[7], NORMAL), new Action(INPUT, "s"));
        actions.put(new KeyPair(keys[1], keys[2], NORMAL), new Action(INPUT, "a"));
        actions.put(new KeyPair(keys[7], keys[8], NORMAL), new Action(INPUT, "k"));
        actions.put(new KeyPair(keys[3], keys[5], NORMAL), new Action(INPUT, "u"));
        actions.put(new KeyPair(keys[1], keys[5], NORMAL), new Action(INPUT, "o"));
        actions.put(new KeyPair(keys[4], keys[7], NORMAL), new Action(INPUT, "v"));
        actions.put(new KeyPair(keys[1], keys[8], NORMAL), new Action(INPUT, "p"));
        actions.put(new KeyPair(keys[6], keys[8], NORMAL), new Action(INPUT, "x"));
        actions.put(new KeyPair(keys[3], keys[6], NORMAL), new Action(INPUT, "l"));
        actions.put(new KeyPair(keys[5], keys[6], NORMAL), new Action(INPUT, "i"));
        actions.put(new KeyPair(keys[2], keys[7], NORMAL), new Action(INPUT, "h"));
        actions.put(new KeyPair(keys[1], keys[7], NORMAL), new Action(INPUT, "c"));
        actions.put(new KeyPair(keys[2], keys[5], NORMAL), new Action(INPUT, "t"));
        actions.put(new KeyPair(keys[1], keys[4], NORMAL), new Action(INPUT, "j"));
        actions.put(new KeyPair(keys[5], keys[8], NORMAL), new Action(INPUT, "m"));
        actions.put(new KeyPair(keys[2], keys[6], NORMAL), new Action(INPUT, "n"));
        actions.put(new KeyPair(keys[3], keys[7], NORMAL), new Action(INPUT, "b"));
        actions.put(new KeyPair(keys[2], keys[3], NORMAL), new Action(INPUT, "g"));
        actions.put(new KeyPair(keys[6], keys[7], NORMAL), new Action(INPUT, "e"));
        actions.put(new KeyPair(keys[4], keys[5], NORMAL), new Action(INPUT, "f"));
    }
}


