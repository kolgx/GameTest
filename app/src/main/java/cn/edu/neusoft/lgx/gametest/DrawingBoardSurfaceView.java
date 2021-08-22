package cn.edu.neusoft.lgx.gametest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class DrawingBoardSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = "DrawingBoardSurfaceView";
    private static final int MESSAGE_UPDATA_UI = 0;
    private int num = 100;

    Handler mHandler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_UPDATA_UI: {
                    int[] data = (int[]) msg.obj;
                    aliveHintTextView.setText(""+data[0]);
                    generationTextView.setText(String.valueOf(data[1]));
                    if(data[2] == 1) mButton.setText("开始");
                    break;
                }
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    };

    private float mWidth;
    private Creatures creatures;
    private TextView aliveHintTextView;
    private TextView generationTextView;
    private Button mButton;
    private float mGridUnit;

    private final AtomicLong sleepTime = new AtomicLong(1000);
    private final AtomicBoolean mIsPaused = new AtomicBoolean(true);

    Context context;
    SurfaceHolder mHolder;

    public DrawingBoardSurfaceView(Context context) {
        super(context);

        this.context = context;

        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    public DrawingBoardSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mHolder = getHolder();

        setZOrderOnTop(true);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mIsPaused.set(true);
    }

    public void setAliveHintTextView(TextView textView) {
        aliveHintTextView = textView;
    }

    public void setGenerationTextView(TextView textView) {
        generationTextView = textView;
    }

    public void setmButton(Button mButton) {
        this.mButton = mButton;
    }

    public boolean isGameStop() {
        return mIsPaused.get();
    }

    public int[][] getLastMode(){
        return creatures.getLastmode();
    }

    public void initGame(int num, int[][] mode) {
        this.num = num;
        initGame(new Creatures(num, mode));
    }

    public void initGame() {
        initGame(new Creatures(num));
    }

    public void initGame(int num) {
        this.num = num;
        initGame(new Creatures(num));
        textViewConnect();
    }

    private void initGame(Creatures c) {
        int viewHeight = getHeight(), viewWidth = getWidth();
        mWidth = Math.min(viewWidth, viewHeight);
        mGridUnit = mWidth / num;

        mIsPaused.set(true);
        creatures = c;
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        Canvas canvas = mHolder.lockCanvas();
        draw(canvas, paint);
        mHolder.unlockCanvasAndPost(canvas);
    }

    private void textViewConnect(){
        aliveHintTextView.setText(String.valueOf(creatures.getScore()));
        generationTextView.setText(String.valueOf(creatures.getGeneration()));
    }

    public void pauseGame() {
        mIsPaused.set(true);
    }

    public void continueGame() {
        mIsPaused.set(false);
        new Thread(new DrawThread()).start();
    }

    public void setSleepTime(long sleepTime) {
        this.sleepTime.getAndSet(sleepTime);
    }

    private void draw(Canvas canvas, Paint paint) {
        int[][] livingStatus = creatures.getFusion();

        paint.setColor(Color.BLACK);
        filledSquare(canvas, paint, mWidth / 2, mWidth / 2, mWidth / 2);

        // draw n-by-n grid
        for (int row = 0; row < num; row++) {
            for (int col = 0; col < num; col++) {
                if (livingStatus[row][col] == 0) {
                    continue;
                } else if (livingStatus[row][col] == 1) {
                    paint.setColor(Color.WHITE);
                } else if (livingStatus[row][col] == 2) {
                    paint.setColor(Color.CYAN);
                } else if (livingStatus[row][col] == 3) {
                    paint.setColor(Color.GREEN);
                } else {
                    paint.setColor(Color.YELLOW);
                }
                filledSquare(canvas, paint, (col + 0.5f) * mGridUnit,
                        (row + 0.5f) * mGridUnit, 0.45f * mGridUnit);
            }
        }
    }


    /**
     * 绘制方块点
     * @param canvas 画布
     * @param paint 绘制格式
     * @param x 中心点横坐标
     * @param y 中心点纵坐标
     * @param halfLength 绘制半径
     */
    private void filledSquare(Canvas canvas, Paint paint, float x, float y, float halfLength) {
        RectF rect = new RectF();
        rect.set(x - halfLength, y + halfLength,
                x + halfLength, y - halfLength);
        canvas.drawRect(rect, paint);
    }

    class DrawThread implements Runnable {
        @SuppressWarnings("BusyWait")
        @Override
        public void run() {

            Canvas canvas = null;
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            boolean autoStop = false;

            while (!mIsPaused.get()) {
                try {
                    autoStop = !creatures.gameDown();
                    if (mHolder.getSurface().isValid()) {
                        canvas = mHolder.lockCanvas();
                        if (canvas != null && canvas.getWidth() > 0) {
                            draw(canvas, paint);
                            Message msg = new Message();
                            msg.obj = new int[]{creatures.getScore(), creatures.getGeneration(), autoStop?1:0};
                            mHandler.sendMessage(msg);
                        }
                        mHolder.unlockCanvasAndPost(canvas);
                    }
                    if(autoStop) mIsPaused.set(autoStop);
                    Thread.sleep(sleepTime.get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean control_up() {
        Canvas canvas = null;
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        boolean statly = false;
        boolean showstatly = false;

        if (!mIsPaused.get()) {
            statly = creatures.gameUp();
            if (statly && mHolder.getSurface().isValid()) {
                canvas = mHolder.lockCanvas();
                if (canvas != null && canvas.getWidth() > 0) {
                    draw(canvas, paint);
                    showstatly = true;
                }
                mHolder.unlockCanvasAndPost(canvas);
            }
        }
        return statly && showstatly;
    }

    public boolean control_down() {
        Canvas canvas = null;
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        boolean statly = false;
        boolean showstatly = false;

        if (!mIsPaused.get()) {
            statly = creatures.gameDown();
            if (statly && mHolder.getSurface().isValid()) {
                canvas = mHolder.lockCanvas();
                if (canvas != null && canvas.getWidth() > 0) {
                    draw(canvas, paint);
                    showstatly = true;
                }
                mHolder.unlockCanvasAndPost(canvas);
            }
        }
        return statly && showstatly;
    }

    public boolean control_left() {
        Canvas canvas = null;
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        boolean statly = false;
        boolean showstatly = false;

        if (!mIsPaused.get()) {
            statly = creatures.gameLeft();
            if (statly && mHolder.getSurface().isValid()) {
                canvas = mHolder.lockCanvas();
                if (canvas != null && canvas.getWidth() > 0) {
                    draw(canvas, paint);
                    showstatly = true;
                }
                mHolder.unlockCanvasAndPost(canvas);
            }
        }
        return statly && showstatly;
    }

    public boolean control_right() {
        Canvas canvas = null;
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        boolean statly = false;
        boolean showstatly = false;

        if (!mIsPaused.get()) {
            statly = creatures.gameRight();
            if (statly && mHolder.getSurface().isValid()) {
                canvas = mHolder.lockCanvas();
                if (canvas != null && canvas.getWidth() > 0) {
                    draw(canvas, paint);
                    showstatly = true;
                }
                mHolder.unlockCanvasAndPost(canvas);
            }
        }
        return statly && showstatly;
    }
}
