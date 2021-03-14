package com.pv.neliritta;

public class Utilities {

    public static boolean isPointInsideTriangle(float x1, float y1, float x2, float y2, float x3, float y3, float x, float y) {
        double ABC = Math.abs (x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2));
        double ABP = Math.abs (x1 * (y2 - y) + x2 * (y - y1) + x * (y1 - y2));
        double APC = Math.abs (x1 * (y - y3) + x * (y3 - y1) + x3 * (y1 - y));
        double PBC = Math.abs (x * (y2 - y3) + x2 * (y3 - y) + x3 * (y - y2));

        // Room for error of 1.0f pixel
        return Math.abs((ABP + APC + PBC) - ABC) < 0.1f;
    }

    public static boolean isMouseInsidePerspectiveRectangle(Main main, float x1, float y1, float x2, float y2) {
        return Utilities.isPointInsideTriangle(
                    main.getGame().screenX(x2, y1),
                    main.getGame().screenY(x2, y1),

                    main.getGame().screenX(x1, y2),
                    main.getGame().screenY(x1, y2),

                    main.getGame().screenX(x2, y2),
                    main.getGame().screenY(x2, y2),

                    main.getGame().mouseX, main.getGame().mouseY
            ) || Utilities.isPointInsideTriangle(
                    main.getGame().screenX(x1, y1),
                    main.getGame().screenY(x1, y1),

                    main.getGame().screenX(x2, y1),
                    main.getGame().screenY(x2, y1),

                    main.getGame().screenX(x1, y2),
                    main.getGame().screenY(x1, y2),

                    main.getGame().mouseX, main.getGame().mouseY
            );
    }
}
