package com.example.sigga.appprojectdots;

import java.io.Serializable;

/**
 * Created by Kristin on 11/09/15.
 */
public class Score implements Serializable {
    private String m_name;
    private int m_points;

    Score(String name, int points) {
        m_name = name;
        m_points = points;
    }

    String getName() {
        return m_name;
    }

    int getPoints() {
        return m_points;
    }

    @Override
    public String toString() {
        return m_name + " <==> " + m_points;
    }
}
