import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

/**
 * Created by eniml on 01.06.2016.
 */
public class board implements ActionListener {
    private static JButton[][] board;
    private static JLabel[] dbg;
    Color fc;
    Color sc;
    int x, y;
    Object source;
    int mLenth = 16, mWidth = 16;
    private Color[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.PINK, Color.YELLOW, Color.CYAN, Color.WHITE};
    private String[] textColor = new String[]{"red", "green", "blue"/*, "pink", "yellow", "cyan", "white"*/};
    private int fx = 3, fy = 3, sx = 0, sy = 0;
    private boolean fpr = false, spr = false;

    public board() {
        board = new JButton[mLenth][mWidth];
        int row, col;
        Random random = new Random();
        JFrame.setDefaultLookAndFeelDecorated(false);
        final JFrame tframe = new JFrame("test");
        tframe.setLayout(new GridLayout(17, 16));
        for (col = 0; col < mLenth; col++) {
            for (row = 0; row < mWidth; row++) {
                board[row][col] = new JButton();
                int pos = random.nextInt(colors.length);
                board[row][col].setBackground(colors[pos]);
                if ((col == 0) | (col == 1) | (col == 2) | (row == 0) | (row == 1) | (row == 2) | (col == 13) | (col == 14) | (col == 15) | (row == 13) | (row == 14) | (row == 15)) {
                    board[row][col].setEnabled(false);
                    board[row][col].setBackground(Color.BLACK);
                    board[row][col].setVisible(true);

                }
                board[row][col].setToolTipText(String.valueOf(row + " " + col) + "   " + board[row][col].getBackground());
                board[row][col].addActionListener(this);
                tframe.add(board[row][col]);
            }
        }
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

    public void actionPerformed(ActionEvent e) {
        int tempX, tempY;
        Color tempC;
        source = e.getSource();
        tempX = getX(source, board);
        tempY = getY(source, board);
        tempC = board[tempX][tempY].getBackground();
        sx = tempX;
        sy = tempY;
        sc = tempC;
        dbg[0].setText(fx + "   ");
        dbg[1].setText(fy + "    || ");
        dbg[2].setText(sx + "  ");
        dbg[3].setText(sy + "  ");
        fpr = true;
//todo Add inverted moves;
        if ((moveXtoRightIsUsefull(fx, fy, board) && (fy == sy) && ((sx - fx) == 1)) ||
                (moveXtoLeftIsUsefull(fx, fy, board) && (fy == sy) && ((fx - sx) == 1)) ||
                (moveYUpIsUsefull(fx, fy, board) && (fx == sx) && ((sy - fy) == 1)) ||
                (moveYDownIsUsefull(fx, fy, board) && (fx == sx) && ((fy - sy) == 1))
                        && (spr) && (sx + sy >= 0)) {
            board[sx][sy].setBackground(fc);
            board[fx][fy].setBackground(tempC);
            fx = 3;
            fy = 3;
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

    public int getY(Object t, JButton[][] m) {
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

    public int getX(Object t, JButton[][] m) {
        int row, col = 0;
        int row1 = 100;
        for (row = 0; row < mLenth; row++)
            if (row1 != 100) {
                break;
            } else {
                for (col = 0; col < mWidth; col++) {
                    int temp = m[row][col].hashCode();
                    if (temp == t.hashCode()) {
                        row1 = row;
                        break;
                    }
                }
            }
        return row1;
    }

    public boolean moveXtoLeftIsUsefull(int i, int j, JButton[][] m) {
        if ((((m[i][j].getBackground()).equals(m[i - 1][j + 1].getBackground())) && ((m[i][j].getBackground()).equals(m[i - 1][j - 1].getBackground()))) ||         //16
                (((m[i][j].getBackground()).equals(m[i - 2][j].getBackground())) && ((m[i][j].getBackground()).equals(m[i - 3][j].getBackground()))) ||                 //10
                (((m[i][j].getBackground()).equals(m[i - 1][j - 1].getBackground())) && ((m[i][j].getBackground()).equals(m[i - 1][j - 2].getBackground()))) ||         //8
                (((m[i][j].getBackground()).equals(m[i - 1][j + 1].getBackground())) && ((m[i][j].getBackground()).equals(m[i - 1][j + 2].getBackground()))))      //12
        {
            return true;
        } else return false;
    }

    public boolean moveXtoRightIsUsefull(int i, int j, JButton[][] m) {

        if (((m[i][j].getBackground()).equals(m[i + 1][j + 1].getBackground())) && ((m[i][j].getBackground()).equals(m[i + 1][j - 1].getBackground())) ||               //14
                (((m[i][j].getBackground()).equals(m[i + 2][j].getBackground())) && ((m[i][j].getBackground()).equals(m[i + 3][j].getBackground()))) ||                 //4
                (((m[i][j].getBackground()).equals(m[i + 1][j + 1].getBackground())) && ((m[i][j].getBackground()).equals(m[i + 1][j + 2].getBackground()))) ||         //2
                (((m[i][j].getBackground()).equals(m[i + 1][j - 1].getBackground())) && ((m[i][j].getBackground()).equals(m[i + 1][j - 2].getBackground()))))           //6
        {
            return true;
        } else return false;
    }

    public boolean moveYUpIsUsefull(int i, int j, JButton[][] m) {
        if ((((m[i][j].getBackground()).equals(m[i][j + 2].getBackground())) && ((m[i][j].getBackground()).equals(m[i][j + 3].getBackground()))) ||                      //1
                (((m[i][j].getBackground()).equals(m[i + 1][j + 1].getBackground())) && ((m[i][j].getBackground()).equals(m[i - 1][j + 1].getBackground()))) ||          //13
                (((m[i][j].getBackground()).equals(m[i + 1][j + 1].getBackground())) && ((m[i][j].getBackground()).equals(m[i + 2][j + 1].getBackground()))) ||          //3
                (((m[i][j].getBackground()).equals(m[i - 1][j + 1].getBackground())) && ((m[i][j].getBackground()).equals(m[i - 2][j + 1].getBackground()))))          //11
        {
            return true;
        } else return false;
    }

    public boolean moveYDownIsUsefull(int i, int j, JButton[][] m) {
        if (
                (((m[i][j].getBackground()).equals(m[i][j - 2].getBackground())) && ((m[i][j].getBackground()).equals(m[i][j - 3].getBackground()))) ||                  //7
                        (((m[i][j].getBackground()).equals(m[i - 1][j - 1].getBackground())) && ((m[i][j].getBackground()).equals(m[i + 1][j - 1].getBackground()))) ||          //15
                        (((m[i][j].getBackground()).equals(m[i + 1][j - 1].getBackground())) && ((m[i][j].getBackground()).equals(m[i + 2][j - 1].getBackground()))) ||          //5
                        (((m[i][j].getBackground()).equals(m[i - 1][j - 1].getBackground())) && ((m[i][j].getBackground()).equals(m[i - 2][j - 1].getBackground()))))           //9
        {
            return true;
        } else return false;
    }
}