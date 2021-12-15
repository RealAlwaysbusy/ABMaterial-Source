package com.ab.abmaterial;

import anywheresoftware.b4a.BA.Hide;

@Hide
public class Entity implements Comparable<Entity> {
    String name;
    double num;
    Entity(String name, double num) {
        this.name = name;
        this.num = num;
    }
    @Override
    public int compareTo(Entity o) {
        if (this.num > o.num)
            return 1;
        else if (this.num < o.num)
            return -1;
        return 0;
    }
}
