package cn.edu.neusoft.lgx.gametest;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    private DrawingBoardSurfaceView playview,preview;
    private Button game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView score = findViewById(R.id.textViewScore);
        TextView time = findViewById(R.id.textViewTime);
        //绑定游戏进程按键
        game = findViewById(R.id.button_GameStartOrStop);

        playview = findViewById(R.id.gameplayView);
        playview.setAliveHintTextView(score);
        playview.setGenerationTextView(time);
        playview.setmButton(game);


        //速度控制
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        radioGroup.check(R.id.radioButton2);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radioButton:
                        playview.setSleepTime(2000);
                        break;
                    case R.id.radioButton2:
                        playview.setSleepTime(1000);
                        break;
                    case R.id.radioButton3:
                        playview.setSleepTime(500);
                        break;
                }
            }
        });
    }

    private void makeToast(String string, int time){
        Toast.makeText(MainActivity.this,string,time).show();
    }

    public void OnButtonClick_up(View view){
        makeToast("up",Toast.LENGTH_SHORT);
    }
    public void OnButtonClick_down(View view){
        makeToast("down",Toast.LENGTH_SHORT);
    }
    public void OnButtonClick_left(View view){
        makeToast("left",Toast.LENGTH_SHORT);
    }
    public void OnButtonClick_right(View view){
        makeToast("right",Toast.LENGTH_SHORT);
    }
    public void OnButtonClick_start(View view){
        if("开始".contentEquals(game.getText())){
            playview.initGame(20);
            game.setText("暂停");
            playview.continueGame();
            Log.e(TAG, "OnButtonClick_start: 开始" );
            return;
        }
        if("暂停".contentEquals(game.getText())) {
            game.setText("继续");
            playview.pauseGame();
            Log.e(TAG, "OnButtonClick_start: 暂停" );
            return;
        }
        if("继续".contentEquals(game.getText())){
            game.setText("暂停");
            playview.continueGame();
            Log.e(TAG, "OnButtonClick_start: 继续" );
        }
    }

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        public void run() {
            this.update();
            /*handler.postDelayed(this, 500);// 间隔120秒*/
        }
        void update() {
            playview.initGame(20);
        }
    };
}