package cn.edu.neusoft.lgx.gametest;

public class StatusFactory {
    private static int NUM = 100;

    private static final String STATUS_PULSAR = "Pulsar";//1*4
    private static final String STATUS_PENTADECATHLON = "Pentadecathlon ";//2*2
    private static final String STATUS_GLIDER = "Glider";//\Z
    private static final String STATUS_LWSS = "Lightweight spaceship";//Z
    private static final String STATUS_GOSPER_GLIDER_GUN = "Gosper glider gun";//L
    private static final String STATUS_DIEHARD = "Diehard";//\L
    private static final String STATUS_INFINITE_LINE = "Infinite line";//T

    public static final String[] SAMPLE_STATUS_ARRAY = {
            STATUS_PULSAR,
            STATUS_GLIDER,
            STATUS_LWSS,
            STATUS_PENTADECATHLON,
            STATUS_DIEHARD,
            STATUS_INFINITE_LINE,
            STATUS_GOSPER_GLIDER_GUN};

    private static int[][] sMatrix;

    /**
     * 绘制图像
     * @param mode 选择模板
     * @param x 模板横坐标
     * @param y 模板纵坐标
     * @param r 模板旋转角度 顺时针 1~4
     * @return 使用模板会绘制后的二维矩阵
     */
    public static int[][] getSampleStatus(String mode, int x, int y, int r) {
        int[][] matrix = null;
        switch (mode) {
            case STATUS_PULSAR:
                matrix = getStatusPulsar(x, y, r);
                break;
            case STATUS_GLIDER:
                matrix = getGlider(x, y, r);
                break;
            case STATUS_DIEHARD:
                matrix = getDiehard(x, y, r);
                break;
            case STATUS_PENTADECATHLON:
                matrix = getPentadecathlon(x, y, r);
                break;
            case STATUS_GOSPER_GLIDER_GUN:
                matrix = getGosperGliderGun(x, y, r);
                break;
            case STATUS_INFINITE_LINE:
                matrix = getInfiniteLine(x, y, r);
                break;
            case STATUS_LWSS:
                matrix = getLWSS(x, y, r);
                break;
        }
        return matrix;
    }

    private static void initMatrix() {
        sMatrix = new int[NUM][NUM];
        for (int row = 0; row < NUM; row++) {
            for (int col = 0; col < NUM; col++) {
                sMatrix[row][col] = 0;
            }
        }
    }

    /**
     * 更改矩阵大小
     * @param NUM 正方形矩阵边长
     */
    public static void setNUM(int NUM) {
        StatusFactory.NUM = NUM;
    }

    /**
     *L字型模板绘制二维矩阵
     * @param x 模板的横坐标
     * @param y 模板的纵坐标
     * @param r 旋转角度1~4
     * @return 坐标矩阵
     */
    private static int[][] getGosperGliderGun(int x, int y, int r) {

        initMatrix();

        switch (r%4){
            case 1:
                sMatrix[y + 1][x] = 1;
                sMatrix[y][x] = 1;
                sMatrix[y][x + 1] = 1;
                sMatrix[y][x + 2] = 1;
                break;
            case 2:
                sMatrix[y][x - 1] = 1;
                sMatrix[y][x] = 1;
                sMatrix[y + 1][x] = 1;
                sMatrix[y + 2][x] = 1;
                break;
            case 3:
                sMatrix[y - 1][x] = 1;
                sMatrix[y][x] = 1;
                sMatrix[y][x - 1] = 1;
                sMatrix[y][x - 2] = 1;
                break;
            case 0:
                sMatrix[y][x - 1] = 1;
                sMatrix[y][x] = 1;
                sMatrix[y - 1][x] = 1;
                sMatrix[y - 2][x] = 1;
                break;
        }

        return sMatrix;
    }

    /**
     * T字型模板绘制二维矩阵
     * @param x 模板的横坐标
     * @param y 模板的纵坐标
     * @param r 旋转角度1~4
     * @return 坐标矩阵
     */
    private static int[][] getInfiniteLine(int x, int y, int r) {

        initMatrix();
        switch (r%4){
            case 1:
                sMatrix[y][x - 1] = 1;
                sMatrix[y][x] = 1;
                sMatrix[y][x + 1] = 1;
                if(y-1>=0)
                    sMatrix[y - 1][x] = 1;
                break;
            case 2:
                sMatrix[y - 1][x] = 1;
                sMatrix[y][x] = 1;
                sMatrix[y + 1][x] = 1;
                sMatrix[y][x + 1] = 1;
                break;
            case 3:
                sMatrix[y][x + 1] = 1;
                sMatrix[y][x] = 1;
                sMatrix[y][x - 1] = 1;
                sMatrix[y + 1][x] = 1;
                break;
            case 0:
                sMatrix[y - 1][x] = 1;
                sMatrix[y][x] = 1;
                sMatrix[y + 1][x] = 1;
                sMatrix[y][x - 1] = 1;
                break;
        }

        return sMatrix;
    }

    /**
     * 反L字符模板绘制二维矩阵
     * @param x 模板的横坐标
     * @param y 模板的纵坐标
     * @param r 旋转角度1~4
     * @return 坐标矩阵
     */
    private static int[][] getDiehard(int x, int y, int r) {

        initMatrix();

        switch (r%4){
            case 1:
                if(y-1>=0)
                    sMatrix[y - 1][x] = 1;
                sMatrix[y][x] = 1;
                sMatrix[y][x + 1] = 1;
                sMatrix[y][x + 2] = 1;
                break;
            case 2:
                sMatrix[y][x + 1] = 1;
                sMatrix[y][x] = 1;
                sMatrix[y + 1][x] = 1;
                sMatrix[y + 2][x] = 1;
                break;
            case 3:
                sMatrix[y + 1][x] = 1;
                sMatrix[y][x] = 1;
                sMatrix[y][x - 1] = 1;
                sMatrix[y][x - 2] = 1;
                break;
            case 0:
                sMatrix[y][x - 1] = 1;
                sMatrix[y][x] = 1;
                sMatrix[y - 1][x] = 1;
                sMatrix[y - 2][x] = 1;
                break;
        }

        return sMatrix;
    }

    /**
     *田字型模板绘制二维矩阵
     * @param x 模板的横坐标
     * @param y 模板的纵坐标
     * @param r 旋转角度1~4
     * @return 坐标矩阵
     */
    private static int[][] getPentadecathlon(int x, int y, int r) {

        initMatrix();
        switch (r%4){
            case 1:
                sMatrix[y][x] = 1;
                sMatrix[y][x + 1] = 1;
                sMatrix[y + 1][x] = 1;
                sMatrix[y + 1][x + 1] = 1;
                break;
            case 2:
                sMatrix[y][x - 1] = 1;
                sMatrix[y][x] = 1;
                sMatrix[y + 1][x] = 1;
                sMatrix[y + 1][x - 1] = 1;
                break;
            case 3:
                sMatrix[y - 1][x] = 1;
                sMatrix[y][x] = 1;
                sMatrix[y][x - 1] = 1;
                sMatrix[y - 1][x - 1] = 1;
                break;
            case 0:
                sMatrix[y][x + 1] = 1;
                sMatrix[y][x] = 1;
                sMatrix[y - 1][x] = 1;
                sMatrix[y - 1][x + 1] = 1;
                break;
        }

        return sMatrix;
    }

    /**
     *Z字型模板
     * @param x 模板的横坐标
     * @param y 模板的纵坐标
     * @param r 旋转角度1~4
     * @return 坐标矩阵
     */
    private static int[][] getLWSS(int x, int y, int r) {
        initMatrix();

        switch (r%4){
            case 1:
                sMatrix[y][x - 1] = 1;
                sMatrix[y][x] = 1;
                sMatrix[y + 1][x] = 1;
                sMatrix[y + 1][x + 1] = 1;
                break;
            case 2:
                sMatrix[y - 1][x] = 1;
                sMatrix[y][x] = 1;
                sMatrix[y][x - 1] = 1;
                sMatrix[y + 1][x - 1] = 1;
                break;
            case 3:
                sMatrix[y][x + 1] = 1;
                sMatrix[y][x] = 1;
                sMatrix[y - 1][x] = 1;
                sMatrix[y - 1][x - 1] = 1;
                break;
            case 0:
                sMatrix[y + 1][x] = 1;
                sMatrix[y][x] = 1;
                sMatrix[y][x + 1] = 1;
                sMatrix[y - 1][x + 1] = 1;
                break;
        }

        return sMatrix;
    }

    /**
     * 反Z字型模板
     * @param x 模板的横坐标
     * @param y 模板的纵坐标
     * @param r 旋转角度1~4
     * @return 坐标矩阵
     */
    private static int[][] getGlider(int x, int y, int r) {
        initMatrix();
        switch (r%4){
            case 1:
                sMatrix[y][x + 1] = 1;
                sMatrix[y][x] = 1;
                sMatrix[y + 1][x] = 1;
                sMatrix[y + 1][x - 1] = 1;
                break;
            case 2:
                sMatrix[y + 1][x] = 1;
                sMatrix[y][x] = 1;
                sMatrix[y][x - 1] = 1;
                sMatrix[y - 1][x - 1] = 1;
                break;
            case 3:
                sMatrix[y][x - 1] = 1;
                sMatrix[y][x] = 1;
                sMatrix[y - 1][x] = 1;
                sMatrix[y - 1][x + 1] = 1;
                break;
            case 0:
                sMatrix[y - 1][x] = 1;
                sMatrix[y][x] = 1;
                sMatrix[y][x + 1] = 1;
                sMatrix[y + 1][x + 1] = 1;
                break;
        }
        return sMatrix;
    }

    /**
     * 长条型模板
     * @param x 模板的横坐标
     * @param y 模板的纵坐标
     * @param r 旋转角度1~4
     * @return 坐标矩阵
     */
    private static int[][] getStatusPulsar(int x, int y,int r) {
        initMatrix();
        switch (r%4){
            case 1:
                sMatrix[y][x-1] = 1;
                sMatrix[y][x] = 1;
                sMatrix[y][x + 1] = 1;
                sMatrix[y][x + 2] = 1;
                break;
            case 2:
                sMatrix[y - 1][x] = 1;
                sMatrix[y][x] = 1;
                sMatrix[y + 1][x] = 1;
                sMatrix[y + 2][x] = 1;
                break;
            case 3:
                sMatrix[y][x-2] = 1;
                sMatrix[y][x-1] = 1;
                sMatrix[y][x] = 1;
                sMatrix[y][x + 1] = 1;
                break;
            case 0:
                sMatrix[y - 2][x] = 1;
                sMatrix[y - 1][x] = 1;
                sMatrix[y][x] = 1;
                sMatrix[y + 1][x] = 1;
                break;
        }
        return sMatrix;
    }
}
