import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

//todo Reshuffle;

/**
 * Created by eniml on 01.06.2016.
 */
public class board implements ActionListener {
    private static JButton[][] board;
    private static JLabel[] dbg;
    private final int mLenth = 20;
    private final int mWidth = 20;
    private final Color[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.PINK, Color.YELLOW, Color.CYAN};
    private Color fc;
    private Color sc;
    private Object source;
    private int score;
    //private String[] textColor = new String[]{"red", "green", "blue"/*, "pink", "yellow", "cyan", "white"*/};
    private int fx = 4;
    private int fy = 4;
    private int sy = 0;
    private boolean fpr = false, spr = false;

    private board() {
        board = new JButton[mLenth][mWidth];
        int row, col;
        Random random = new Random();
        JFrame.setDefaultLookAndFeelDecorated(false);
        final JFrame tframe = new JFrame("test");
        tframe.setLayout(new GridLayout(11, 10));
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Game");
        fileMenu.addSeparator();
        JMenuItem newGameItem = new JMenuItem("New Game");
        fileMenu.add(newGameItem);
        newGameItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reshuffle(board);
                destroynewmatches(board);
                score = 0;
                dbg[0].setText(String.valueOf(getScore()));
            }
        });
        JMenuItem exitItem = new JMenuItem("Exit");
        fileMenu.add(exitItem);
        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        menuBar.add(fileMenu);
        tframe.setJMenuBar(menuBar);
        for (col = 0; col < mLenth; col++) {
            for (row = 0; row < mWidth; row++) {
                boolean bool = true;
                board[row][col] = new JButton();
                int pos = random.nextInt(colors.length);
                board[row][col].setBackground(colors[pos]);
                if ((col < 5) | (col > 14) | (row < 5) | (row > 14)) {

                    bool = false;
                    board[row][col].setEnabled(false);
                    board[row][col].setBackground(Color.WHITE);
                    board[row][col].setVisible(true);
                }
                board[row][col].setToolTipText(String.valueOf(row + " " + col) + "   " + board[row][col].getBackground());
                board[row][col].addActionListener(this);
                if (bool) {
                    tframe.add(board[row][col]);
                }
            }
        }
        destroynewmatches(board);
        score = 0;
        dbg = new JLabel[7];
        dbg[0] = new JLabel();
        tframe.add(dbg[0]);
        dbg[1] = new JLabel();
        tframe.add(dbg[1]);
        dbg[2] = new JLabel();
        tframe.add(dbg[2]);
        dbg[3] = new JLabel();
        tframe.add(dbg[3]);
        dbg[4] = new JLabel();
        tframe.add(dbg[4]);
        dbg[5] = new JLabel();
        tframe.add(dbg[5]);
        tframe.setLocationByPlatform(true);
        tframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        tframe.pack();
        tframe.setSize(600, 600);
        tframe.setVisible(true);
    }

    public static void main(String[] args) {
        board qq = new board();
        JFrame q = new JFrame();
    }

    private void reshuffle(JButton[][] board) {
        Random r = new Random();
        for (int i = mWidth - 6; i >= 5; i--) {
            for (int j = 5; j <= mLenth - 6; j++) {
                int pos = r.nextInt(colors.length);
                board[j][i].setBackground(colors[pos]);
            }
        }
    }

    int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void actionPerformed(ActionEvent e) {
        int tempX, tempY;
        Color tempC;
        source = e.getSource();
        tempX = getX(source, board);
        tempY = getY(source, board);
        tempC = board[tempX][tempY].getBackground();
        int sx = tempX;
        sy = tempY;
        sc = tempC;

        //dbg[1].setText(fy + "    || ");
        //dbg[2].setText(sx + "  ");
        //dbg[3].setText(sy + "  ");
        fpr = true;

        if ((moveXtoRightIsUsefull(fx, fy, board) && (fy == sy) && ((sx - fx) == 1)) ||
                (moveXtoLeftIsUsefull(fx, fy, board) && (fy == sy) && ((fx - sx) == 1)) ||
                (moveYUpIsUsefull(fx, fy, board) && (fx == sx) && ((sy - fy) == 1)) ||
                (moveYDownIsUsefull(fx, fy, board) && (fx == sx) && ((fy - sy) == 1)) ||
                (moveXtoRightIsUsefull(sx, sy, board) && (fy == sy) && ((fx - sx) == 1)) ||
                (moveXtoLeftIsUsefull(sx, sy, board) && (fy == sy) && ((sx - fx) == 1)) ||
                (moveYUpIsUsefull(sx, sy, board) && (fx == sx) && ((fy - sy) == 1)) ||
                (moveYDownIsUsefull(sx, sy, board) && (fx == sx) && ((sy - fy) == 1))
                        && (spr) && (sx + sy >= 0)) {
            board[sx][sy].setBackground(fc);
            board[fx][fy].setBackground(tempC);
            destroyaftermove(fx, fy, board);
            destroyaftermove(sx, sy, board);
            fillwhytes(board);
            filldestroeydwhytes(board);
            destroynewmatches(board);
            dbg[0].setText(String.valueOf(getScore()));

            fx = 4;
            fy = 4;
            sx = 0;
            sy = 0;
            spr = false;
        } else {
            fc = sc;
            fx = sx;
            fy = sy;
            spr = true;
            fpr = false;
        }
    }

    private boolean matchexistcheker(JButton[][] board) {
        boolean check = false;
        for (int i = mWidth - 6; i >= 5; i--) {
            for (int j = 5; j <= mLenth - 6; j++) {
                if (moveXtoRightIsUsefull(j, i, board) || moveXtoLeftIsUsefull(j, i, board) || moveYUpIsUsefull(j, i, board) || moveYDownIsUsefull(j, i, board))
                    check = true;
                break;
            }
        }
        return check;
    }

    private void destroynewmatches(JButton[][] board) {
        int t = 0;
        while (t <= 20) {
            for (int i = mWidth - 6; i >= 5; i--) {
                for (int j = 5; j <= mLenth - 6; j++) {
                    destroyaftermove(j, i, board);
                    filldestroeydwhytes(board);
                    t++;
                }
            }
        }
    }

    private void filldestroeydwhytes(JButton[][] board) {
        Random r = new Random();
        for (int i = mWidth - 6; i >= 5; i--) {
            for (int j = 5; j <= mLenth - 6; j++) {
                if (board[j][i].getBackground().equals(Color.WHITE)) {
                    int pos = r.nextInt(colors.length);
                    board[j][i].setBackground(colors[pos]);
                }
            }
        }

    }

    private void fillwhytes(JButton[][] board) {
        for (int i = mWidth - 6; i >= 5; i--) {
            for (int j = 5; j <= mLenth - 6; j++) {
                int k;
                k = i - 1;
                while ((board[j][i].getBackground().equals(Color.WHITE) && (k >= 5))) {
                    board[j][i].setBackground(board[j][k].getBackground());
                    board[j][k].setBackground(Color.WHITE);
                    k--;
                }
            }
        }
    }

    private void destroyaftermove(int x1, int y1, JButton[][] m) {
        //x+
        if ((m[x1][y1].getBackground().equals(m[x1 + 1][y1].getBackground())) && (m[x1][y1].getBackground().equals(m[x1 + 2][y1].getBackground())) && (0 <= x1) && (x1 < mWidth - 2)) {
            if ((m[x1][y1].getBackground().equals(m[x1 - 1][y1].getBackground())) && (1 <= x1)) {
                if ((m[x1][y1].getBackground().equals(m[x1 + 3][y1].getBackground())) && (x1 < mWidth - 3)) {    //+x+++
                    m[x1][y1].setBackground(Color.white);
                    m[x1 + 1][y1].setBackground(Color.white);
                    m[x1 + 2][y1].setBackground(Color.white);
                    m[x1 - 1][y1].setBackground(Color.white);
                    m[x1 + 3][y1].setBackground(Color.white);
                    score = score + 5;
                }
                if ((m[x1][y1].getBackground().equals(m[x1 - 2][y1].getBackground())) && (2 <= x1)) {    //++x++
                    m[x1][y1].setBackground(Color.white);
                    m[x1 + 1][y1].setBackground(Color.white);
                    m[x1 + 2][y1].setBackground(Color.white);
                    m[x1 - 1][y1].setBackground(Color.white);
                    m[x1 - 2][y1].setBackground(Color.white);
                    score = score + 5;
                } else {
                    m[x1][y1].setBackground(Color.white);                                   //+x++
                    m[x1 + 1][y1].setBackground(Color.white);
                    m[x1 + 2][y1].setBackground(Color.white);
                    m[x1 - 1][y1].setBackground(Color.white);
                    score = score + 4;
                }
            }
            if ((m[x1][y1].getBackground().equals(m[x1 + 3][y1].getBackground())) && (x1 <= mWidth - 3)) {
                if ((m[x1][y1].getBackground().equals(m[x1 + 4][y1].getBackground())) && (x1 <= mWidth - 3)) {    //x++++
                    m[x1][y1].setBackground(Color.white);
                    m[x1 + 1][y1].setBackground(Color.white);
                    m[x1 + 2][y1].setBackground(Color.white);
                    m[x1 + 3][y1].setBackground(Color.white);
                    m[x1 + 4][y1].setBackground(Color.white);
                    score = score + 5;
                } else {
                    m[x1][y1].setBackground(Color.white);                                   //x+++
                    m[x1 + 1][y1].setBackground(Color.white);
                    m[x1 + 2][y1].setBackground(Color.white);
                    m[x1 + 3][y1].setBackground(Color.white);
                    score = score + 4;
                }
            } else {
                m[x1][y1].setBackground(Color.white);                                       //x++
                m[x1 + 1][y1].setBackground(Color.white);
                m[x1 + 2][y1].setBackground(Color.white);
                score = score + 3;
            }
        }
        //-x
        if ((m[x1][y1].getBackground().equals(m[x1 - 1][y1].getBackground())) && (m[x1][y1].getBackground().equals(m[x1 - 2][y1].getBackground())) && (2 <= x1) && (x1 < mWidth)) {
            if ((m[x1][y1].getBackground().equals(m[x1 + 1][y1].getBackground())) && (x1 < mWidth - 1)) {
                if ((m[x1][y1].getBackground().equals(m[x1 - 3][y1].getBackground())) && (3 <= x1)) {   //+++x+
                    m[x1][y1].setBackground(Color.white);
                    m[x1 - 1][y1].setBackground(Color.white);
                    m[x1 - 2][y1].setBackground(Color.white);
                    m[x1 - 3][y1].setBackground(Color.white);
                    m[x1 + 1][y1].setBackground(Color.white);
                    score = score + 5;
                } else {                                                                    //++x+
                    m[x1][y1].setBackground(Color.white);
                    m[x1 - 1][y1].setBackground(Color.white);
                    m[x1 - 2][y1].setBackground(Color.white);
                    m[x1 + 1][y1].setBackground(Color.white);
                    score = score + 4;
                }
            }
            if ((m[x1][y1].getBackground().equals(m[x1 - 3][y1].getBackground())) && (3 <= x1)) {
                if ((m[x1][y1].getBackground().equals(m[x1 - 4][y1].getBackground())) && (5 <= x1)) {    //++++x
                    m[x1][y1].setBackground(Color.white);
                    m[x1 - 1][y1].setBackground(Color.white);
                    m[x1 - 2][y1].setBackground(Color.white);
                    m[x1 - 3][y1].setBackground(Color.white);
                    m[x1 - 4][y1].setBackground(Color.white);
                    score = score + 5;
                } else {
                    m[x1][y1].setBackground(Color.white);                                   //+++x
                    m[x1 - 1][y1].setBackground(Color.white);
                    m[x1 - 2][y1].setBackground(Color.white);
                    m[x1 - 3][y1].setBackground(Color.white);
                    score = score + 4;
                }
            } else {                                                                        //++x
                m[x1][y1].setBackground(Color.white);
                m[x1 - 1][y1].setBackground(Color.white);
                m[x1 - 2][y1].setBackground(Color.white);
                score = score + 3;
            }
        }
        //y+
        if ((m[x1][y1].getBackground().equals(m[x1][y1 + 1].getBackground())) && (m[x1][y1].getBackground().equals(m[x1][y1 + 2].getBackground())) && (0 <= y1) && (y1 < mWidth - 2)) {
            if ((m[x1][y1].getBackground().equals(m[x1][y1 - 1].getBackground())) && (1 <= y1)) {
                if ((m[x1][y1].getBackground().equals(m[x1][y1 + 3].getBackground())) && (y1 < mWidth - 3)) {    //+x+++
                    m[x1][y1].setBackground(Color.white);
                    m[x1][y1 + 1].setBackground(Color.white);
                    m[x1][y1 + 2].setBackground(Color.white);
                    m[x1][y1 - 1].setBackground(Color.white);
                    m[x1][y1 + 3].setBackground(Color.white);
                    score = score + 5;
                }
                if ((m[x1][y1].getBackground().equals(m[x1][y1 - 2].getBackground())) && (2 <= y1)) {    //++x++
                    m[x1][y1].setBackground(Color.white);
                    m[x1][y1 + 1].setBackground(Color.white);
                    m[x1][y1 + 2].setBackground(Color.white);
                    m[x1][y1 - 1].setBackground(Color.white);
                    m[x1][y1 - 2].setBackground(Color.white);
                    score = score + 5;
                } else {
                    m[x1][y1].setBackground(Color.white);                                   //+x++
                    m[x1][y1 + 11].setBackground(Color.white);
                    m[x1][y1 + 2].setBackground(Color.white);
                    m[x1][y1 - 1].setBackground(Color.white);
                    score = score + 4;
                }
            }
            if ((m[x1][y1].getBackground().equals(m[x1][y1 + 3].getBackground())) && (y1 < mWidth - 3)) {
                if ((m[x1][y1].getBackground().equals(m[x1][y1 + 4].getBackground())) && (y1 < mWidth - 4)) {    //x++++
                    m[x1][y1].setBackground(Color.white);
                    m[x1][y1 + 1].setBackground(Color.white);
                    m[x1][y1 + 2].setBackground(Color.white);
                    m[x1][y1 + 3].setBackground(Color.white);
                    m[x1][y1 + 4].setBackground(Color.white);
                    score = score + 5;
                } else {
                    m[x1][y1].setBackground(Color.white);                                   //x+++
                    m[x1][y1 + 1].setBackground(Color.white);
                    m[x1][y1 + 2].setBackground(Color.white);
                    m[x1][y1 + 3].setBackground(Color.white);
                    score = score + 4;
                }
            } else {
                m[x1][y1].setBackground(Color.white);                                       //x++
                m[x1][y1 + 1].setBackground(Color.white);
                m[x1][y1 + 2].setBackground(Color.white);
                score = score + 3;
            }
        }
        //y-
        if ((m[x1][y1].getBackground().equals(m[x1][y1 - 1].getBackground())) && (m[x1][y1].getBackground().equals(m[x1][y1 - 2].getBackground())) && (2 <= y1)) {
            if ((m[x1][y1].getBackground().equals(m[x1][y1 + 1].getBackground())) && (y1 < mWidth - 1)) {
                if ((m[x1][y1].getBackground().equals(m[x1][y1 - 3].getBackground())) && (3 <= y1)) {   //+++x+
                    m[x1][y1].setBackground(Color.white);
                    m[x1][y1 - 1].setBackground(Color.white);
                    m[x1][y1 - 2].setBackground(Color.white);
                    m[x1][y1 - 3].setBackground(Color.white);
                    m[x1][y1 + 1].setBackground(Color.white);
                    score = score + 5;
                } else {                                                                    //++x+
                    m[x1][y1].setBackground(Color.white);
                    m[x1][y1 - 1].setBackground(Color.white);
                    m[x1][y1 - 2].setBackground(Color.white);
                    m[x1][y1 + 1].setBackground(Color.white);
                    score = score + 4;
                }
            }
            if ((m[x1][y1].getBackground().equals(m[x1][y1 - 3].getBackground())) && (3 <= y1)) {
                if ((m[x1][y1].getBackground().equals(m[x1][y1 - 4].getBackground())) && (5 <= y1)) {    //++++x
                    m[x1][y1].setBackground(Color.white);
                    m[x1][y1 - 1].setBackground(Color.white);
                    m[x1][y1 - 2].setBackground(Color.white);
                    m[x1][y1 - 3].setBackground(Color.white);
                    m[x1][y1 - 4].setBackground(Color.white);
                    score = score + 5;
                } else {
                    m[x1][y1].setBackground(Color.white);                                   //+++x
                    m[x1][y1 - 1].setBackground(Color.white);
                    m[x1][y1 - 2].setBackground(Color.white);
                    m[x1][y1 - 3].setBackground(Color.white);
                    score = score + 4;
                }
            } else {                                                                        //++x
                m[x1][y1].setBackground(Color.white);
                m[x1][y1 - 1].setBackground(Color.white);
                m[x1][y1 - 2].setBackground(Color.white);
                score = score + 3;
            }
        }
        //-x+
        if ((m[x1][y1].getBackground().equals(m[x1 + 1][y1].getBackground())) && (m[x1][y1].getBackground().equals(m[x1 - 1][y1].getBackground())) && (1 <= x1) && (x1 < mWidth - 1)) {
            m[x1][y1].setBackground(Color.white);
            m[x1 + 1][y1].setBackground(Color.white);
            m[x1 - 1][y1].setBackground(Color.white);
            score = score + 3;
        }
        //-y+
        if ((m[x1][y1].getBackground().equals(m[x1][y1 + 1].getBackground())) && (m[x1][y1].getBackground().equals(m[x1][y1 - 1].getBackground())) && (1 <= y1) && (y1 < mWidth - 1)) {
            m[x1][y1].setBackground(Color.white);
            m[x1][y1 + 1].setBackground(Color.white);
            m[x1][y1 - 1].setBackground(Color.white);
            score = score + 3;
        }
    }

    int getY(Object t, JButton[][] m) {
        int row, col;
        int col1 = 100;
        for (row = 0; row < mLenth; row++) {
            if (col1 != 100) {
                break;
            } else {
                for (col = 0; col < mWidth; col++) {
                    int temp = m[row][col].hashCode();
                    if (temp == t.hashCode()) {
                        col1 = col;
                        break;
                    }
                }
            }
        }
        return col1;
    }

    int getX(Object t, JButton[][] m) {
        int row;
        int row1 = 100;
        for (row = 0; row < mLenth; row++)
            if (row1 != 100) {
                break;
            } else {
                for (int col = 0; col < mWidth; col++) {
                    int temp = m[row][col].hashCode();
                    if (temp == t.hashCode()) {
                        row1 = row;
                        break;
                    }
                }
            }
        return row1;
    }

    boolean moveXtoLeftIsUsefull(int i, int j, JButton[][] m) {
        if ((((m[i][j].getBackground()).equals(m[i - 1][j + 1].getBackground())) && ((m[i][j].getBackground()).equals(m[i - 1][j - 1].getBackground())) && ((1 <= i) && (i < mWidth) && (1 <= j) && (j < mWidth - 1))) ||         //16
                (((m[i][j].getBackground()).equals(m[i - 2][j].getBackground())) && ((m[i][j].getBackground()).equals(m[i - 3][j].getBackground())) && ((3 <= i) && (i < mWidth) && (0 <= j) && (j < mWidth))) ||                 //10
                (((m[i][j].getBackground()).equals(m[i - 1][j - 1].getBackground())) && ((m[i][j].getBackground()).equals(m[i - 1][j - 2].getBackground())) && ((1 <= i) && (i < mWidth) && (2 <= j) && (j < mWidth))) ||         //8
                (((m[i][j].getBackground()).equals(m[i - 1][j + 1].getBackground())) && ((m[i][j].getBackground()).equals(m[i - 1][j + 2].getBackground()))) && ((1 <= i) && (i < mWidth) && (0 <= j) && (j < mWidth - 2)))      //12
        {
            return true;
        } else return false;
    }

    boolean moveXtoRightIsUsefull(int i, int j, JButton[][] m) {

        if (((m[i][j].getBackground()).equals(m[i + 1][j + 1].getBackground())) && ((m[i][j].getBackground()).equals(m[i + 1][j - 1].getBackground()) && ((0 <= i) && (i < mWidth - 1) && (1 <= j) && (j < mWidth - 1))) ||               //14
                (((m[i][j].getBackground()).equals(m[i + 2][j].getBackground())) && ((m[i][j].getBackground()).equals(m[i + 3][j].getBackground())) && ((0 <= i) && (i < mWidth - 3) && (0 <= j) && (j < mWidth))) ||                 //4
                (((m[i][j].getBackground()).equals(m[i + 1][j + 1].getBackground())) && ((m[i][j].getBackground()).equals(m[i + 1][j + 2].getBackground())) && ((0 <= i) && (i < mWidth - 1) && (0 <= j) && (j < mWidth - 2))) ||         //2
                (((m[i][j].getBackground()).equals(m[i + 1][j - 1].getBackground())) && ((m[i][j].getBackground()).equals(m[i + 1][j - 2].getBackground()))) && ((0 <= i) && (i < mWidth - 1) && (2 <= j) && (j < mWidth)))           //6
        {
            return true;
        } else return false;
    }

    boolean moveYUpIsUsefull(int i, int j, JButton[][] m) {
        if ((((m[i][j].getBackground()).equals(m[i][j + 2].getBackground())) && ((m[i][j].getBackground()).equals(m[i][j + 3].getBackground())) && ((0 <= i) && (i < mWidth) && (0 <= j) && (j < mWidth - 3))) ||                      //1
                (((m[i][j].getBackground()).equals(m[i + 1][j + 1].getBackground())) && ((m[i][j].getBackground()).equals(m[i - 1][j + 1].getBackground())) && ((1 <= i) && (i < mWidth - 1) && (0 <= j) && (j < mWidth - 1))) ||          //13
                (((m[i][j].getBackground()).equals(m[i + 1][j + 1].getBackground())) && ((m[i][j].getBackground()).equals(m[i + 2][j + 1].getBackground())) && ((0 <= i) && (i < mWidth - 2) && (0 <= j) && (j < mWidth - 1))) ||          //3
                (((m[i][j].getBackground()).equals(m[i - 1][j + 1].getBackground())) && ((m[i][j].getBackground()).equals(m[i - 2][j + 1].getBackground())) && ((2 <= i) && (i < mWidth) && (0 <= j) && (j < mWidth - 1))))          //11
        {
            return true;
        } else return false;
    }

    boolean moveYDownIsUsefull(int i, int j, JButton[][] m) {
        if (
                (((m[i][j].getBackground()).equals(m[i][j - 2].getBackground())) && ((m[i][j].getBackground()).equals(m[i][j - 3].getBackground())) && ((0 <= i) && (i < mWidth) && (3 <= j) && (j < mWidth))) ||                  //7
                        (((m[i][j].getBackground()).equals(m[i - 1][j - 1].getBackground())) && ((m[i][j].getBackground()).equals(m[i + 1][j - 1].getBackground())) && ((1 <= i) && (i < mWidth - 1) && (1 <= j) && (j < mWidth))) ||          //15
                        (((m[i][j].getBackground()).equals(m[i + 1][j - 1].getBackground())) && ((m[i][j].getBackground()).equals(m[i + 2][j - 1].getBackground())) && ((0 <= i) && (i < mWidth - 2) && (1 <= j) && (j < mWidth))) ||          //5
                        (((m[i][j].getBackground()).equals(m[i - 1][j - 1].getBackground())) && ((m[i][j].getBackground()).equals(m[i - 2][j - 1].getBackground())) && ((2 <= i) && (i < mWidth) && (1 <= j) && (j < mWidth))))           //9
        {
            return true;
        } else return false;
    }
}