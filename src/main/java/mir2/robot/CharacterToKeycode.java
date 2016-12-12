package mir2.robot;

import org.junit.Test;

import java.awt.event.KeyEvent;

/**
 * Created by yang on 2016/12/3.
 */
public class CharacterToKeycode {
    private static int letterKeyCodeTable[] = {KeyEvent.VK_A,KeyEvent.VK_B,KeyEvent.VK_C,KeyEvent.VK_D,KeyEvent.VK_E,KeyEvent.VK_F,KeyEvent.VK_G,
            KeyEvent.VK_H,KeyEvent.VK_I,KeyEvent.VK_J,KeyEvent.VK_K,KeyEvent.VK_L,KeyEvent.VK_M,KeyEvent.VK_N,KeyEvent.VK_O,KeyEvent.VK_P,
            KeyEvent.VK_Q, KeyEvent.VK_R,KeyEvent.VK_S,KeyEvent.VK_T,KeyEvent.VK_U,KeyEvent.VK_V,KeyEvent.VK_W,KeyEvent.VK_X,KeyEvent.VK_Y,
            KeyEvent.VK_Z};
    private static int digitKeyCodeTable[] = {KeyEvent.VK_0,KeyEvent.VK_1,KeyEvent.VK_2,KeyEvent.VK_3,KeyEvent.VK_4,KeyEvent.VK_5,KeyEvent.VK_6,
            KeyEvent.VK_7,KeyEvent.VK_8,KeyEvent.VK_9};

    public static int characterToKeyCode(char ch){
        if (Character.isDigit(ch)) {
            return digitKeyCodeTable[ch - '0'];
        }
        else if(Character.isLetter(ch)){
            if(Character.isUpperCase(ch))
                return letterKeyCodeTable[ch - 'A'];
            else
                return letterKeyCodeTable[ch - 'a'];
        }
        else {
            switch (ch){
                case '\t':
                    return KeyEvent.VK_TAB;
            }
        }
        return 0;//
    }

    @Test
    public void test(){
        System.out.println(CharacterToKeycode.characterToKeyCode('\t'));
    }
}
