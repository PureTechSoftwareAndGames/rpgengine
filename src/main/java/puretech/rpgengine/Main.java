package puretech.rpgengine;

import puretech.rpgengine.display.ScreenBooter;

import java.awt.*;

public class Main {
    public static void main(String[] args) {
        ScreenBooter.makeScreenBooter().disp(); // Windowed 500x500
//        ScreenBooter.makeScreenBooter(true).disp(); // Fullscreen
//        ScreenBooter.makeScreenBooter(new int[] {1000,1000}).disp(); // Windowed ?x? (in this case 1000x1000)
//        ScreenBooter.makeScreenBooter(Color.BLACK).disp(); // Black Windowed 500x500 (Can also pass in RGB, but if any value is 255 it will rainbow)
//        ScreenBooter.makeScreenBooter(new int[] {1000,1000}, Color.BLACK).disp(); // Black Windowed ?x? (in this case 1000x1000)
//        ScreenBooter.makeScreenBooter(true, Color.BLACK).disp(); // Black Fullscreen
    }
}
