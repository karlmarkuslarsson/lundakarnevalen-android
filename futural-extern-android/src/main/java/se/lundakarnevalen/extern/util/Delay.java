package se.lundakarnevalen.extern.util;

/**
 * Created by Filip on 2014-03-27.
 */
public class Delay {
    public static boolean ms(int ms){
        try {
            Thread.sleep(ms);
            return true;
        } catch (InterruptedException e) {
            return false;
        }
    }
}
