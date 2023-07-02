package com.mygdx.soulknight.util;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Collision {
    public static boolean isLineCollideRect(Vector2 pos1, Vector2 pos2, Rectangle rectangle) {
        if (pos1.x < rectangle.x && pos2.x < rectangle.x) {
            return false;
        }
        if (pos1.x > rectangle.x + rectangle.width && pos2.x > rectangle.x + rectangle.width) {
            return false;
        }
        if (pos1.y < rectangle.y && pos2.y < rectangle.y) {
            return false;
        }
        if (pos1.y > rectangle.y + rectangle.height && pos2.y > rectangle.y + rectangle.height) {
            return false;
        }
        if (pos1.x == pos2.x) {
            return true;
        }
        float a = (pos2.y - pos1.y) / (pos2.x - pos1.x);
        float b = (pos2.x * pos1.y - pos1.x * pos2.y) / (pos2.x - pos1.x);
        float test = a * rectangle.x + b;
        if (rectangle.y <= test && test <= (rectangle.y + rectangle.height)) {
            return true;
        }
        test = a * (rectangle.x + rectangle.width) + b;
        if (rectangle.y <= test && test <= (rectangle.y + rectangle.height)) {
            return true;
        }
        test = (rectangle.y - b) / a;
        if (rectangle.x <= test && test <= rectangle.x + rectangle.width) {
            return true;
        }
        test = (rectangle.y + rectangle.height - b) / a;
        if (rectangle.x <= test && test <= rectangle.x + rectangle.width) {
            return true;
        }
        return false;
    }

    public static boolean isArcCollideRect(Vector2 center, float radius, float startDegree, float endDegree, Rectangle rectangle, float degreePerCheck) {
        for (float i = startDegree; i <= endDegree; i += degreePerCheck) {
            Vector2 endPoint = new Vector2(center.x + radius * MathUtils.cosDeg(i), center.y + radius * MathUtils.sinDeg(i));
            if (isLineCollideRect(center, endPoint, rectangle)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isArcCollideRect(Vector2 center, float radius, float startDegree, float endDegree, Rectangle rectangle) {
        return isArcCollideRect(center, radius, startDegree, endDegree, rectangle, 1);
    }

    public static void print(Object... objects) {
        for (Object object : objects) {
            System.out.println(object);
        }
    }
}
