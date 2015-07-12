package se.lundakarnevalen.extern.util;

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
