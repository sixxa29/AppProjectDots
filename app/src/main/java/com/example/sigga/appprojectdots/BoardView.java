package com.example.sigga.appprojectdots;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BoardView extends View {

    private int m_cellWidth;
    private int m_cellHeight;

    private Rect m_rect = new Rect();
    private Paint m_paint = new Paint();
    private Paint m_paintPath  = new Paint();
    private Paint dotPaint = new Paint();

    private DotPath dots = new DotPath();


    private ArrayList<Integer> colorList = new ArrayList<Integer>();

    private RectF m_circle = new RectF();

    private final int NUM_CELLS = 6;

    private Path m_path = new Path();
    private DotPath path = new DotPath();

    private ShapeDrawable circle = new ShapeDrawable( new OvalShape());

    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        m_paint.setColor(Color.WHITE);
        m_paint.setStyle(Paint.Style.STROKE);
        m_paint.setStrokeWidth(2);
        m_paint.setAntiAlias(true);

        m_paintPath.setStyle(Paint.Style.STROKE);
        m_paintPath.setColor(Color.CYAN);
        m_paintPath.setStrokeWidth(32);
        m_paintPath.setStrokeCap(Paint.Cap.ROUND);
        m_paintPath.setStrokeJoin(Paint.Join.ROUND);
        m_paintPath.setAntiAlias(true);
    }



    @Override
    protected void onMeasure( int widthMeasureSpec, int heightMeasureSpec ) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width  = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        int height = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
        int size = Math.min(width, height);
        setMeasuredDimension(size + getPaddingLeft() + getPaddingRight(),
                size + getPaddingTop() + getPaddingBottom());
    }

    private int pickColor(){
        int[] colors = new int[5];

        colors[0] = Color.rgb(231,219,38);
        colors[1] = Color.rgb(155,93,181);
        colors[2] = Color.rgb(139,232,142);
        colors[3] = Color.rgb(237,93,96);
        colors[4] = Color.rgb(134,189,255);

        Random rand = new Random();
        int max = colors.length;
        int randomNum = rand.nextInt(max);
        int c = colors[randomNum];

        return c;


    }

    @Override
    protected void onSizeChanged( int xNew, int yNew, int xOld, int yOld ) {
        int   boardWidth = (xNew - getPaddingLeft() - getPaddingRight());
        int   boardHeight = (yNew - getPaddingTop() - getPaddingBottom());
        m_cellWidth = boardWidth / NUM_CELLS;
        m_cellHeight = boardHeight / NUM_CELLS;
        m_circle.set(0, 0, m_cellWidth / 2, m_cellHeight / 2);
        m_circle.inset(m_cellWidth * 0.1f, m_cellHeight * 0.1f);
        m_circle.offset(getPaddingLeft(), getPaddingTop());

    }


    @Override
    protected void onDraw(Canvas canvas ) {


        if(colorList.isEmpty()){
            drawDots(canvas);
        }
        else{

            int index = 0;

            for ( int row = 0; row < NUM_CELLS; ++row ) {
                for ( int col = 0; col < NUM_CELLS; ++col ){
                    int x = colToX(col);
                    int y = rowToY(row);
                    m_rect.set(x, y, x + m_cellWidth, y + m_cellHeight);
                    m_rect.inset((int) (m_rect.width() * 0.2), (int) (m_rect.height() * 0.2));

                    //dotPaint.setColor(pickColor());

                    circle.setBounds(m_rect);
                    circle.getPaint().setColor(colorList.get(index));
                    circle.draw(canvas);
                    index++;

                }
            }


        }



        m_path.reset();

        if(!path.isEmpty()) {
            ArrayList<Point> point = path.getCoordinates();
            Point index = point.get(0);

            int x = colToX(index.x) + m_cellWidth/2;
            int y = rowToY(index.y) + m_cellHeight/2;

            m_path.moveTo(x, y);
            for (int i = 1; i < point.size(); ++i) {

                index = point.get(i);
                x = colToX(index.x) + m_cellWidth / 2;
                y = rowToY(index.y) + m_cellHeight / 2;
               m_path.lineTo(x, y);

            }
        }

        canvas.drawPath(m_path, m_paintPath);


    }

    public void drawDots(Canvas canvas) {


        for ( int row = 0; row < NUM_CELLS; ++row ) {
            for ( int col = 0; col < NUM_CELLS; ++col ){
                int x = colToX(col);
                int y = rowToY(row);
                m_rect.set(x, y, x + m_cellWidth, y + m_cellHeight);
                m_rect.inset((int) (m_rect.width() * 0.2), (int) (m_rect.height() * 0.2));
                dotPaint.setColor(pickColor());

                circle.setBounds(m_rect);
                circle.getPaint().setColor(dotPaint.getColor());
                circle.draw(canvas);

                // store the colors of the grid
                colorList.add(dotPaint.getColor());
            }
        }

    }

/*
    protected void drawPathS(Canvas canvas) {

        ArrayList<Point> point = path.getCoordinates();
        Point index = point.get(0);

        int x = colToX(index.x) + m_cellWidth/2;
        int y = rowToY(index.y) + m_cellHeight/2;

        m_path.reset();

        if(path.isEmpty()){ return; }

        m_path.moveTo(x, y);
        for(int i = 1; i < point.size(); ++i){

            index = point.get(i);
            x = colToX(index.x) + m_cellWidth/2;
            y = rowToY(index.y) + m_cellHeight/2;

            m_path.lineTo(x, y);

        }

       // path.setPaintPath(circle.getPaint().getColor());
        //path.setPaintPath(Color.CYAN);

        canvas.drawPath(m_path, m_paintPath);




    }
*/

    private int xToCol( int x ) { return (x - getPaddingLeft()) / m_cellWidth; }
    private int yToRow( int y ) { return (y - getPaddingTop()) / m_cellHeight; }

    private int colToX( int col ) { return  col * m_cellWidth + getPaddingLeft(); }
    private int rowToY( int row ) { return  row * m_cellHeight + getPaddingTop(); }



    @Override
    public boolean onTouchEvent( MotionEvent event ) {

        int x = (int) event.getX();
        int y = (int) event.getY();
      //  int col = xToCol(x);
       // int row = yToRow(y);

        int xMax = getPaddingLeft() + m_cellWidth * NUM_CELLS;
        int yMax = getPaddingTop() + m_cellHeight * NUM_CELLS;
        x = Math.max(getPaddingLeft(), Math.min(x, (int) (xMax - m_circle.width())));
        y = Math.max(getPaddingTop(), Math.min(y, (int) (yMax - m_circle.height())));

        if ( event.getAction() == MotionEvent.ACTION_DOWN ) {

            path.reset();
            path.append(new Point(xToCol(x), yToRow(y)));

            invalidate();
        }
         if ( event.getAction() == MotionEvent.ACTION_MOVE ) {
                if ( !path.isEmpty( ) ) {
                    int col = xToCol(x);
                    int row = yToRow(y);
                    Point last = path.getLast();
                    if (col != last.x || row != last.y) {
                        path.append(new Point(col, row));
                    }
                }
                invalidate();

        }



        return true;
    }

}