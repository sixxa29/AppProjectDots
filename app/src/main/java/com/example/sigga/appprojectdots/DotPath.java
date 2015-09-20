package com.example.sigga.appprojectdots;

import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;

import java.security.PublicKey;
import java.util.ArrayList;

/**
 * Created by sigga on 15.9.2015.
 */
public class DotPath {

    private ArrayList<Point> m_path = new ArrayList<Point>();

    public void append(Point point) {
        int idx = m_path.indexOf(point);
        if (idx >= 0) {
            for (int i = m_path.size() - 1; i > idx; --i) {
                m_path.remove(i);
            }
        } else {
            m_path.add(point);
        }

    }

    public Point getLast() {
        return m_path.get(m_path.size() - 1);
    }

    public Integer lastInt(){
        return m_path.size() - 1;
    }

    public Point get(int i){
        return m_path.get(i);
    }

    public Integer getSize() { return m_path.size(); }

    public void reset() {
        m_path.clear();
    }

    public boolean contains(Point point) {
        return m_path.contains(point);
    }

    public boolean isEmpty() {
        return m_path.isEmpty();
    }
}