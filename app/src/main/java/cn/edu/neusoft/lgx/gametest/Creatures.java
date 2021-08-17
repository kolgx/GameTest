package cn.edu.neusoft.lgx.gametest;


import android.util.Log;
import android.widget.RadioGroup;

import java.util.Arrays;
import java.util.Random;

import static android.content.ContentValues.TAG;

public class Creatures {
    private static final double INTIAL_SURVIVAL_RATE = 0.50;

    private final int num;//边长
    private int[][] background;//背景图
    private int[][] foreground;//前景图
    private int[][] fusion;//合成图
    private int mode;//当前模板
    private int lastmode;//下一次模板
    private int x,y=0, r = 1;//坐标及角度
    private int generation;//回合数
    private int score;//消行数
    private final Random random = new Random();

    public Creatures(int num) {
        this.num = num;
        if(num!=100)
            StatusFactory.setNUM(num);
        background = new int[num][num];
        foreground = new int[num][num];
        fusion = new int[num][num];
        x = num / 2;

        mode = random.nextInt(StatusFactory.SAMPLE_STATUS_ARRAY.length);
        lastmode = random.nextInt(StatusFactory.SAMPLE_STATUS_ARRAY.length);
        generation = 0;
        score = 0;
        foremodeCreate();
        background = mapClear();
        if(!mapFusion(foreground, background))
            fusion = mapClear();
    }

    public boolean gameRight() {
        x++;
        if(foremodeCreate()&&mapFusion(foreground, background))
            return true;
        x--;
        Log.e(TAG, "gameRight: false");
        return false;
    }

    public boolean gameLeft() {
        x--;
        if(foremodeCreate()&&mapFusion(foreground, background))
            return true;
        x++;
        Log.e(TAG, "gameLeft: false");
        return false;
    }

    public boolean gameUp() {
        r++;
        if(foremodeCreate()&&mapFusion(foreground, background))
            return true;
        r--;
        Log.e(TAG, "gameUp: false");
        return false;
    }

    /**
     * 向下
     * @return 能否成功下降，false：游戏结束
     */
    public boolean gameDown() {
        y++;
        if (foremodeCreate()&&mapFusion(foreground, background)) {
            Log.e(TAG, "gameDown: 坐标: "+x+":"+y+":"+r );
            return true;
        }
        Log.e(TAG, "gameDown: 坐标: "+x+":"+y+":"+r );
        return foremodeEnd();
    }

    /**
     * 创建前景图
     * @return 是否成功创建
     */
    private boolean foremodeCreate() {
        if(foemodeChack()) {
            foreground = StatusFactory.getSampleStatus(StatusFactory.SAMPLE_STATUS_ARRAY[mode], x, y, r);
            return true;
        }
        return false;
    }

    /**
     * 方块规则检查，防止方块溢出边界
     * @return 是否安全
     */
    public boolean foemodeChack() {
        r %= 4;
        if (x < 0 || x >= num || y < 0 || y >= num) {
            return false;
        }
        switch (mode) {
            case 0:
                if (r == 1 && (x - 1 < 0 || x + 2 >= num))
                    return false;
                if (r == 2 && (y - 1 < 0 || y + 2 >= num))
                    return false;
                if (r == 3 && (x - 2 < 0 || x + 1 >= num))
                    return false;
                if (r == 0 && (y - 2 < 0 || y + 1 >= num))
                    return false;
                break;
            case 1:
            case 2:
                if (r == 1 && (y + 1 >= num || x + 1 >= num || x - 1 < 0))
                    return false;
                if (r == 2 && (y + 1 >= num || y - 1 < 0 || x - 1 < 0))
                    return false;
                if (r == 3 && (y - 1 < 0 || x + 1 >= num || x - 1 < 0))
                    return false;
                if (r == 0 && (y + 1 >= num || y - 1 < 0 || x + 1 >= num))
                    return false;
                break;
            case 3:
                if (r == 1 && (x + 1 >= num || y + 1 >= num))
                    return false;
                if (r == 2 && (x - 1 < 0 || y + 1 >= num))
                    return false;
                if (r == 3 && (x - 1 < 0 || y - 1 < 0))
                    return false;
                if (r == 0 && (x + 1 >= num || y - 1 < 0))
                    return false;
                break;
            case 4:
                if (r == 1 && (x + 2 >= num))
                    return false;
                if (r == 2 && (y + 2 >= num || x + 1 >= num))
                    return false;
                if (r == 3 && (y + 1 >= num || x - 2 < 0))
                    return false;
                if (r == 0 && (y - 2 < 0 || x - 1 < 0))
                    return false;
                break;
            case 5:
                if (r == 1 && (x + 1 >= num || x - 1 < 0))
                    return false;
                if (r == 2 && (y + 1 >= num || y - 1 < 0 || x + 1 >= num))
                    return false;
                if (r == 3 && (y + 1 >= num || x + 1 >= num || x - 1 < 0))
                    return false;
                if (r == 0 && (y + 1 >= num || y - 1 < 0 || x - 1 < 0))
                    return false;
                break;
            case 6:
                if (r == 1 && (y + 1 >= num || x + 2 >= num))
                    return false;
                if (r == 2 && (y + 2 >= num || x - 1 < 0))
                    return false;
                if (r == 3 && (y - 1 < 0 || x - 2 < 0))
                    return false;
                if (r == 0 && (y - 2 < 0 || x - 1 < 0))
                    return false;
                break;
        }
        return true;
    }

    /**
     * 方块结束，当前方块已经到达低端；
     * 更新背景图，
     * 生成新的方块，
     * 重置坐标,
     * 更新合成图.
     */
    private boolean foremodeEnd() {
        mode = lastmode;
        lastmode = random.nextInt(StatusFactory.SAMPLE_STATUS_ARRAY.length);
        background = mapCpoy(fusion);
        backgroundChack();
        x = num / 2;
        y = 0;
        r = 1;
        foremodeCreate();
        return mapFusion(foreground, background);
    }

    /**
     * 检查背景层，消除满行，加分
     */
    private void backgroundChack() {
        for (int i = num-1; i >= 0; i--) {
            boolean full = true;
            for (int j = 0; j < num; j++) {
                if (background[i][j] == 0) {
                    full = false;
                    break;
                }
            }
            if(full){
                for (int k = i; k - 1 >= 0; k--) {
                    if (num >= 0) System.arraycopy(background[i - 1], 0, background[i], 0, num);
                }
                for (int j = 0; j < num; j++)
                    background[0][j] = 0;
                score++;
                Log.e(TAG, "backgroundChack: 消行："+i );
                i++;
            }
        }
    }

    private int[][] mapCpoy(int[][] map) {
        return map;
    }

    /**
     * 生成合成图
     * @param fore 前景图
     * @param back 背景图
     * @return 如果发生重叠，则不会更改合成图，返回 false
     */
    private boolean mapFusion(int[][] fore, int[][] back){
        int max = fore.length;
        int[][] temporary = new int[max][max];
        for (int i = 0; i < max; i++) {
            for (int j = 0; j < max; j++) {
                if (back[i][j] == fore[i][j] && back[i][j] != 0) {
                    return false;
                }
                temporary[i][j] = fore[i][j] + back[i][j];
            }
        }
        fusion = mapCpoy(temporary);
        generation++;
        return true;
    }

    private int[][] mapClear(){
        return mapClear(num);}

    private int[][] mapClear(int max){
        int[][] map = new int[max][max];
        for (int i = 0; i < max; i++) {
            for (int j = 0; j < max; j++) {
                map[i][j] = 0;
            }
        }
        return map;
    }

    public int[][] getFusion() {
        return fusion;
    }

    public int getGeneration() {
        return generation;
    }

    public int getScore() {
        return score;
    }

    public int getLastmode() {
        return lastmode;
    }

}
