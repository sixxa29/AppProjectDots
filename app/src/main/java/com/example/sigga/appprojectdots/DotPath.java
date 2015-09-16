package com.example.sigga.appprojectdots;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by sigga on 15.9.2015.
 */
public class DotPath {

    private ArrayList<Point> m_path = new ArrayList<Point>();
    private Path drawPath = new Path();
    private Paint paintPath  = new Paint();
    private int color;


    public void append(Point point) {
        int idx = m_path.indexOf(point);
        if ( idx >= 0 ) {
            for ( int i=m_path.size()-1; i > idx; --i ) {
                m_path.remove(i);
            }
        }
        else {
            m_path.add(point);
        }
    }

    public ArrayList<Point> getCoordinates() {
        return m_path;
    }

    public void movePath(int x, int y) {

        drawPath.moveTo(x,y);
    }

    public void linePath(int x, int y){

        drawPath.lineTo(x, y);
    }

    public Path getPath(){
        return drawPath;
    }

    public Point getLast(){
        return m_path.get(m_path.size() - 1);
    }

    public void setPaintPath(int color){
        // Set Color
        this.color = color;
        paintPath.setColor(Color.CYAN);

        // Cell path paint settings
        paintPath.setStyle(Paint.Style.STROKE);
        paintPath.setStrokeWidth(32);

        paintPath.setStrokeCap(Paint.Cap.ROUND);
        paintPath.setStrokeJoin(Paint.Join.ROUND);
        paintPath.setAntiAlias(true);

    }

  /*  public void drawPaths(Canvas canvas, int x, int y){
        //ArrayList<Point> point = m_path.getCoordinates();
        Point index = m_path.get(0);

        if(m_path.isEmpty()){ return; }

        drawPath.moveTo(x, y);
        for(int i = 1; i < m_path.size(); ++i) {

            index = m_path.get(i);
            x = colToX(index.x) + m_cellWidth / 2;
            y = rowToY(index.y) + m_cellHeight / 2;

            drawPath.lineTo(x, y);
        }
    }*/

    public int getPaintPath(){
        return paintPath.getColor();
    }

    public void reset() {
        m_path.clear();
    }

    public boolean isEmpty() {
        return m_path.isEmpty();
    }
}
