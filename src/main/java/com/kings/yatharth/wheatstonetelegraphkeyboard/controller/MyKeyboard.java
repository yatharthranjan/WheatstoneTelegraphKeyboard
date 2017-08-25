package com.kings.yatharth.wheatstonetelegraphkeyboard.controller;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.kings.yatharth.wheatstonetelegraphkeyboard.R;

import java.util.List;

/**
 * Created by yatharth on 18/07/17.
 */

public class MyKeyboard extends InputMethodService
        implements KeyboardView.OnKeyboardActionListener {

    private KeyboardView kv;
    private Keyboard keyboard;

    private boolean caps = false;

    EditText et;

    Table<Integer,Integer,Character> charMapUpper;
    Table<Integer,Integer,Character> charMapLower;

    private int[] value = new int[5];


    @Override
    public View onCreateInputView() {
        charMapUpper = HashBasedTable.create();
        charMapUpper.put(1,2,'h');
        charMapUpper.put(1,3,'e');
        charMapUpper.put(1,4,'b');
        charMapUpper.put(1,5,'a');

        charMapUpper.put(2,3,'i');
        charMapUpper.put(2,4,'f');
        charMapUpper.put(2,5,'d');

        charMapUpper.put(3,4,'k');
        charMapUpper.put(3,5,'g');

        charMapUpper.put(4,5,'l');



        charMapLower = HashBasedTable.create();
        charMapLower.put(1,2,'m');
        charMapLower.put(1,3,'r');
        charMapLower.put(1,4,'v');
        charMapLower.put(1,5,'y');

        charMapLower.put(2,3,'n');
        charMapLower.put(2,4,'s');
        charMapLower.put(2,5,'w');

        charMapLower.put(3,4,'o');
        charMapLower.put(3,5,'t');

        charMapLower.put(4,5,'p');

        kv = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard, null);
        keyboard = new Keyboard(this, R.xml.wheatstone);
        kv.setKeyboard(keyboard);
        kv.setOnKeyboardActionListener(this);
        return kv;
    }

    private void playClick(int keyCode){
        AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
        switch(keyCode){
            case 32:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR, 1);
                break;
            case 10:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_RETURN, 1);
                break;
            case Keyboard.KEYCODE_DELETE:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_DELETE, 1);
                break;

            case 14000:
            case 14001:
            case 14002:
            case 14003:
            case 14004:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD, 1);
                break;
        }
    }

    @Override
    public void onPress(int i) {


    }

    @Override
    public void onRelease(int i) {

    }

    @Override
    public void onKey(int i, int[] ints) {

        int count = 0;

        for(int m=0 ; m< value.length; m++){
            if(value[m] == 1 || value[m] == -1)
                count++;
        }

            InputConnection ic = getCurrentInputConnection();
            playClick(i);
            switch (i) {
                case Keyboard.KEYCODE_DELETE:
                    ic.deleteSurroundingText(1, 0);
                    break;
                case Keyboard.KEYCODE_SHIFT:
                    caps = !caps;
                    keyboard.setShifted(caps);
                    kv.invalidateAllKeys();
                    break;
                case Keyboard.KEYCODE_DONE:
                    ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                    break;

                case 32:
                    ic.commitText(" ", 1);
                    break;


                // the logic for the 5 needles of the telegraph
                case 14000:
                    if (value[0] == 0 && count < 2) {
                        value[0] = 1;
                        changeLabel(14000, "/");
                    } else if (value[0] == 1) {
                        value[0] = -1;
                        changeLabel(14000, "\\");
                    } else if (value[0] == -1) {
                        value[0] = 0;
                        changeLabel(14000, "|");
                    } else
                        break;
                    updateKeyboard(ic);
                    break;
                case 14001:
                    if (value[1] == 0 && count < 2) {
                        value[1] = 1;
                        changeLabel(14001, "/");
                    } else if (value[1] == 1) {
                        value[1] = -1;
                        changeLabel(14001, "\\");
                    } else if (value[1] == -1) {
                        value[1] = 0;
                        changeLabel(14001, "|");
                    } else
                        break;
                    updateKeyboard(ic);
                    break;
                case 14002:
                    if (value[2] == 0 && count < 2) {
                        value[2] = 1;
                        changeLabel(14002, "/");
                    } else if (value[2] == 1) {
                        value[2] = -1;
                        changeLabel(14002, "\\");
                    } else if (value[2] == -1) {
                        value[2] = 0;
                        changeLabel(14002, "|");
                    } else
                        break;
                    updateKeyboard(ic);
                    break;
                case 14003:
                    if (value[3] == 0 && count < 2) {
                        value[3] = 1;
                        changeLabel(14003, "/");
                    } else if (value[3] == 1) {
                        value[3] = -1;
                        changeLabel(14003, "\\");
                    } else if (value[3] == -1) {
                        value[3] = 0;
                        changeLabel(14003, "|");
                    } else
                        break;
                    updateKeyboard(ic);
                    break;
                case 14004:
                    if (value[4] == 0 && count < 2) {
                        value[4] = 1;
                        changeLabel(14004, "/");
                    } else if (value[4] == 1) {
                        value[4] = -1;
                        changeLabel(14004, "\\");
                    } else if (value[4] == -1) {
                        value[4] = 0;
                        changeLabel(14004, "|");
                    } else
                        break;
                    updateKeyboard(ic);
                    break;

                default:
                    /*char code = (char) i;
                    if (Character.isLetter(code) && caps) {
                        code = Character.toUpperCase(code);
                    }
                    ic.commitText(String.valueOf(code), 1);*/

            }
    }

    public void changeLabel(int primaryCode, String label)
    {
        Keyboard currentKeyboard = kv.getKeyboard();
        List<Keyboard.Key> keys = currentKeyboard.getKeys();
        kv.invalidateKey(primaryCode);


        for(int i = 0; i < keys.size() - 1; i++ )
        {
            Keyboard.Key currentKey = keys.get(i);

            //If Key contains more than one code, then you will have to check if the codes array contains the primary code
            if(currentKey.codes[0] == primaryCode)
            {

                //currentKey.label = label;
                if(label.equals("|")){
                    currentKey.icon = getResources().getDrawable(R.mipmap.needle_straight);
                }else if(label.equals("/")){
                    currentKey.icon = getResources().getDrawable(R.mipmap.needle_clockwise);
                }else if(label.equals("\\")){
                    currentKey.icon = getResources().getDrawable(R.mipmap.needle_anti);
                }
                break; // leave the loop once match is found
            }
        }
    }

    public void updateKeyboard(InputConnection ic){
        String ch="";

        for(int i = 0; i < value.length - 1; i++){
            if(value[i] == 1){
                for(int j = i+1 ; j < value.length ; j++){
                    if(value[j] == -1){
                        ch = charMapUpper.get(new Integer(Math.abs(i)+1), new Integer(Math.abs(j)+1)).toString();
                        Log.i("Letter : ", ch);
                    }
                }
            }else if(value[i] == -1){
                for(int j = i+1 ; j < value.length ; j++){
                    if(value[j] == 1){
                        ch = charMapLower.get(new Integer(Math.abs(i)+1), new Integer(Math.abs(j)+1)).toString();
                    }
                }
            }
        }
        if(caps)
            ch = ch.toUpperCase();
        ic.commitText(ch, 1);

    }


    @Override
    public void onText(CharSequence charSequence) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }


}
