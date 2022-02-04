package puretech.rpgengine;

import puretech.rpgengine.display.RainbowScreen;

import java.awt.*;

public class Main {
    public static void main(String[] args) {
        new RainbowScreen("Rainbow", new int[]{1,1},Color.RED,true).disp();
    }
}
