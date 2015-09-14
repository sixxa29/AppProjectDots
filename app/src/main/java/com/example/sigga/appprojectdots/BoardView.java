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

    private RectF m_circle = new RectF();
    private Paint m_paintCircle = new Paint();

    private Path m_path = new Path();
    private Paint m_paintPath = new Paint();

    private final int NUM_CELLS = 6;
    private static final int POINT_PADDING = 25;

    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        m_paint.setColor(Color.WHITE);
        m_paint.setStyle(Paint.Style.STROKE);
        m_paint.setStrokeWidth(2);
        m_paint.setAntiAlias(true);

        m_paintCircle.setStyle(Paint.Style.FILL_AND_STROKE);
        m_paintCircle.setAntiAlias(true);

        m_paintPath.setColor(Color.BLACK);
        m_paintPath.setStrokeWidth(32);
        m_paintPath.setStrokeJoin(Paint.Join.ROUND);
        m_paintPath.setStrokeCap(Paint.Cap.ROUND);
        m_paintPath.setStyle(Paint.Style.STROKE);
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
        m_circle.set(0, 0, m_cellWidth/2, m_cellHeight/2);
        m_circle.inset(m_cellWidth * 0.1f, m_cellHeight * 0.1f);
        m_circle.offset(getPaddingLeft(), getPaddingTop());

    }

    @Override
    protected void onDraw(Canvas canvas ) {
        canvas.drawRect(m_rect, m_paint);

        for ( int row = 0; row < NUM_CELLS; ++row ) {
            for ( int col = 0; col < NUM_CELLS; ++col ) {
                int x = colToX(col);
                int y = rowToY(row);
                m_rect.set(x, y, x + m_cellWidth, y + m_cellHeight);
                m_paintCircle.setColor(pickColor());
                
                canvas.drawCircle(x + getPaddingLeft(), y + getPaddingTop(), 20, m_paintCircle);
                canvas.drawRect(m_rect, m_paint);

            }
        }

        if ( !m_cellPath.isEmpty() ) {
            m_path.reset();
            Point point = m_cellPath.get(0);
            m_path.moveTo( colToX(point.x) + m_cellWidth/2,
                    rowToY(point.y) + m_cellHeight/2 );

            for ( int i=1; i<m_cellPath.size(); ++i ) {
                point = m_cellPath.get(i);
                m_path.lineTo( colToX(point.x) + m_cellWidth/2,
                        rowToY(point.y) + m_cellHeight/2 );
            }
            canvas.drawPath( m_path, m_paintPath );
        }
    }

    boolean m_moving = false;

    private int xToCol( int x ) { return (x - getPaddingLeft()) / m_cellWidth; }
    private int yToRow( int y ) { return (y - getPaddingTop()) / m_cellHeight; }

    private int colToX( int col ) { return  col * m_cellWidth + getPaddingLeft(); }
    private int rowToY( int row ) { return  row * m_cellHeight + getPaddingTop(); }


    void snapToGrid( RectF circle ) {
        int col = xToCol( (int) circle.left );
        int row = yToRow((int) circle.top);
        float x = colToX(col) + (m_cellWidth - circle.width())/2.0f;
        float y = rowToY(row) + (m_cellHeight - circle.height())/2.0f;
        //circle.offsetTo( x, y );
        m_path.moveTo(x,y);
    }

    private List<Point> m_cellPath = new ArrayList<Point>();

    @Override
    public boolean onTouchEvent( MotionEvent event ) {

        int x = (int) event.getX();
        int y = (int) event.getY();
        int col = xToCol(x);
        int row = yToRow(y);

        int xMax = getPaddingLeft() + m_cellWidth * NUM_CELLS;
        int yMax = getPaddingTop() + m_cellHeight * NUM_CELLS;
        x = Math.max(getPaddingLeft(), Math.min(x, (int) (xMax - m_circle.width())));
        y = Math.max(getPaddingTop(), Math.min(y, (int) (yMax - m_circle.height())));

        if ( event.getAction() == MotionEvent.ACTION_DOWN ) {
            //if ( m_rect.contains(x,y) ) {
                //m_moving = true;
                //m_cellPath.add( new Point(xToCol(x), yToRow(y)) );
                m_cellPath.clear();
                m_cellPath.add( new Point(col, row) );
           // }
           // else {
           //     animateMovement(m_circle.left, m_circle.top,
           //             x - m_circle.width() / 2, y - m_circle.height() / 2);
           // }
           // invalidate();
        }
         if ( event.getAction() == MotionEvent.ACTION_MOVE ) {
            //if ( m_moving ) {
                if ( !m_cellPath.isEmpty( ) ) {

                    Point last = m_cellPath.get(m_cellPath.size() - 1);
                    if (col != last.x || row != last.y) {
                        m_cellPath.add(new Point(col, row));
                    }
          //      }
                invalidate();
            }

        }
     /*   else if ( event.getAction() == MotionEvent.ACTION_UP ) {
            m_moving = false;
            snapToGrid(m_circle);
            m_cellPath.clear();
            invalidate();
        }*/


        return true;
    }
    ValueAnimator animator = new ValueAnimator();

    private void animateMovement( final float xFrom, final float yFrom, final float xTo, final float yTo ) {
        animator.removeAllUpdateListeners();
        animator.setDuration(3000);
        animator.setFloatValues(0.0f, 1.0f);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float ratio = (float) animation.getAnimatedValue();
                int x = (int)( (1.0-ratio) * xFrom + ratio * xTo );
                int y = (int)( (1.0-ratio) * yFrom + ratio * yTo );
                m_path.lineTo(x,y);
                invalidate();
            }
        });
        animator.start();

    }
}