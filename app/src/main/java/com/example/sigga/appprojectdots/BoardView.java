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
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BoardView extends View {

    private final int NUM_CELLS = 6;

    private int m_cellWidth;
    private int m_cellHeight;

    private Rect m_rect = new Rect();
    private Paint m_paintPath = new Paint();
    private Paint dotPaint = new Paint();
    boolean initialBoard = true;
    boolean isMoving = false;
    private Integer moves = 30; // auto moves per game

    private ArrayList<Integer> colorList = new ArrayList<Integer>(); // array to keep track of colors
    private List<ArrayList<Integer>> coordColor = new ArrayList<ArrayList<Integer>>(); // 2D array to keep coordinates and colors


    private RectF m_circle = new RectF();
    private Path m_path = new Path();
    private DotPath path = new DotPath();

    private ShapeDrawable circle = new ShapeDrawable(new OvalShape());

    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);

        pickColor();
        dotPaint.setColor(Color.BLACK);
        dotPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        dotPaint.setStrokeWidth(2);
        dotPaint.setAntiAlias(true);

        m_paintPath.setStyle(Paint.Style.STROKE);
        m_paintPath.setColor(Color.CYAN);
        m_paintPath.setStrokeWidth(15);
        m_paintPath.setStrokeCap(Paint.Cap.ROUND);
        m_paintPath.setStrokeJoin(Paint.Join.ROUND);
        m_paintPath.setAntiAlias(true);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        int height = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
        int size = Math.min(width, height);
        setMeasuredDimension(size + getPaddingLeft() + getPaddingRight(),
                size + getPaddingTop() + getPaddingBottom());
    }

    private int pickColor() {
        int[] colors = new int[5];

        colors[0] = Color.rgb(231, 219, 38);
        colors[1] = Color.rgb(155, 93, 181);
        colors[2] = Color.rgb(139, 232, 142);
        colors[3] = Color.rgb(237, 93, 96);
        colors[4] = Color.rgb(134, 189, 255);

        Random rand = new Random();
        int max = colors.length;
        int randomNum = rand.nextInt(max);
        int c = colors[randomNum];

        return c;


    }

    @Override
    protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld) {
        int boardWidth = (xNew - getPaddingLeft() - getPaddingRight());
        int boardHeight = (yNew - getPaddingTop() - getPaddingBottom());
        m_cellWidth = boardWidth / NUM_CELLS;
        m_cellHeight = boardHeight / NUM_CELLS;
        m_circle.set(0, 0, m_cellWidth / 2, m_cellHeight / 2);
        m_circle.inset(m_cellWidth * 0.1f, m_cellHeight * 0.1f);
        m_circle.offset(getPaddingLeft(), getPaddingTop());

    }


    @Override
    protected void onDraw(Canvas canvas) {

        drawDots(canvas);

        drawPathS(canvas);

    }

    public void drawDots(Canvas canvas) {

        int i = 0;
        for (int row = 0; row < NUM_CELLS; ++row) {
            coordColor.add(new ArrayList<Integer>());
            for (int col = 0; col < NUM_CELLS; ++col) {

                int x = colToX(col);
                int y = rowToY(row);
                m_rect.set(x, y, x + m_cellWidth, y + m_cellHeight);
                m_rect.inset((int) (m_rect.width() * 0.2), (int) (m_rect.height() * 0.2));
                circle.setBounds(m_rect);

                if (initialBoard) {

                    // when making the initial board we need random colors
                    dotPaint.setColor(pickColor());
                    //litur.setColor(dotPaint.getColor());
                    circle.getPaint().setColor(dotPaint.getColor());

                    // store the colors of the grid
                    colorList.add(dotPaint.getColor());

                } else {

                    // getting correct colors for the board
                    dotPaint.setColor(colorList.get(i));
                    circle.getPaint().setColor(dotPaint.getColor());
                    i++;

                }

                // keeping track of the coordinates of the color
                coordColor.get(row).add(col, dotPaint.getColor());
                circle.draw(canvas);

            }
        }
        initialBoard = false;
    }


    protected void drawPathS(Canvas canvas) {

        m_path.reset();

        if (!path.isEmpty()) {
            ArrayList<Point> point = path.getCoordinates();
            Point index = point.get(0);

            int r = colToX(index.x) + m_cellWidth / 2;
            int f = rowToY(index.y) + m_cellHeight / 2;
            m_paintPath.setColor(coordColor.get(index.x).get(index.y));

            m_path.moveTo(r, f);
            for (int i = 1; i < point.size(); ++i) {

                index = point.get(i);
                r = colToX(index.x) + m_cellWidth / 2;
                f = rowToY(index.y) + m_cellHeight / 2;
                m_path.lineTo(r, f);
                m_paintPath.setColor(coordColor.get(index.x).get(index.y));

            }
        }

        //m_paintPath.setColor(dotPaint.getColor());
        canvas.drawPath(m_path, m_paintPath);

    }


    private int xToCol(int x) {
        return (x - getPaddingLeft()) / m_cellWidth;
    }

    private int yToRow(int y) {
        return (y - getPaddingTop()) / m_cellHeight;
    }

    private int colToX(int col) {
        return col * m_cellWidth + getPaddingLeft();
    }

    private int rowToY(int row) {
        return row * m_cellHeight + getPaddingTop();
    }

    private boolean areNeighbours(int c1, int r1, int c2, int r2) {

        int a = Math.abs(c1 - c2);
        int b = Math.abs(r1 - r2);

        return (a + b == 1);


    }

    void snapToGrid(RectF circle) {
        int col = xToCol((int) circle.left);
        int row = yToRow((int) circle.top);
        int x = colToX(col) + (int) (m_cellWidth - circle.width()) / 2;
        int y = rowToY(row) + (int) (m_cellHeight - circle.height()) / 2;
        circle.offsetTo(x, y);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int x = (int) event.getX();
        int y = (int) event.getY();

        int xMax = getPaddingLeft() + m_cellWidth * NUM_CELLS;
        int yMax = getPaddingTop() + m_cellHeight * NUM_CELLS;
        x = Math.max(getPaddingLeft(), Math.min(x, (int) (xMax - m_circle.width())));
        y = Math.max(getPaddingTop(), Math.min(y, (int) (yMax - m_circle.height())));

        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            isMoving = true;
            path.reset();
            path.append(new Point(xToCol(x), yToRow(y)));
            m_paintPath.setColor(coordColor.get(xToCol(x)).get(yToRow(y)));
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (isMoving) {
                if (!path.isEmpty()) {
                    int col = xToCol(x);
                    int row = yToRow(y);
                    Point last = path.getLast();

                    int dx = Math.abs(col - last.x);
                    int dy = Math.abs(row - last.y);


                    if (col != last.x || row != last.y) {

                        path.append(new Point(col, row));
                    }
                }

                invalidate();
            }


        } else if (event.getAction() == MotionEvent.ACTION_UP) {

            moves--; // after lifting a finger you only have x amount of moves left
            isMoving = false;
            path.reset();
            invalidate();

        }

        return true;
    }

}