package cn.edu.neusoft.lgx.gametest;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    private DrawingBoardSurfaceView playview,preview;
    private Button game, gameRestart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView score = findViewById(R.id.textViewScore);
        TextView time = findViewById(R.id.textViewTime);
        //绑定游戏进程按键
        game = findViewById(R.id.button_GameStartOrStop);
        gameRestart = findViewById(R.id.button_GameRestart);

        playview = findViewById(R.id.gameplayView);
        playview.setAliveHintTextView(score);
        playview.setGenerationTextView(time);
        playview.setmButton(game);

        preview = findViewById(R.id.previewView);

        //速度控制
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        radioGroup.check(R.id.radioButton2);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radioButton:
                        playview.setSleepTime(800);
                        break;
                    case R.id.radioButton2:
                        playview.setSleepTime(500);
                        break;
                    case R.id.radioButton3:
                        playview.setSleepTime(200);
                        break;
                }
            }
        });

    }

    public void OnButtonClick_up(View view) {
        if (!playview.control_up())
            Log.e(TAG, "OnButtonClick_up: failed");
    }

    public void OnButtonClick_down(View view) {
        if (!playview.control_down()) {
            Log.e(TAG, "OnButtonClick_down: failed");
        }
    }

    public void OnButtonClick_left(View view) {
        if (!playview.control_left())
            Log.e(TAG, "OnButtonClick_left: failed");
    }

    public void OnButtonClick_right(View view) {
        if (!playview.control_right())
            Log.e(TAG, "OnButtonClick_right: failed");
    }

    public void OnButtonClick_start(View view){
        if("开始".contentEquals(game.getText())){
            playview.initGame(20);
            game.setText("暂停");
            playview.continueGame();
            return;
        }
        if("暂停".contentEquals(game.getText())) {
            game.setText("继续");
            playview.pauseGame();
            gameRestart.setEnabled(true);
            return;
        }
        if("继续".contentEquals(game.getText())){
            game.setText("暂停");
            playview.continueGame();
            gameRestart.setEnabled(false);
        }
    }

    public void OnButtonClick_restart(View view) {
        if(playview.isNotGameStop()) return;
        playview.initGame(20);
        game.setText("暂停");
        playview.continueGame();
        gameRestart.setEnabled(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        handler.postDelayed(runnable, 1000);
    }

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        public void run() {
            this.update();
            handler.postDelayed(this, 1000);
        }
        void update() {
            if(playview.isNotGameStop())
                preview.initGame(6,playview.getLastMode());
        }
    };
}