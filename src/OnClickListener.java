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
    private final int mLength = 20;
    private final int mWidth = 20;
    private final Color[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.PINK, Color.YELLOW, Color.CYAN};
    private Color firstCellColor, secondCellColor;
    private Object source;
    private int score;
    //private String[] textColor = new String[]{"red", "green", "blue"/*, "pink", "yellow", "cyan", "white"*/};
    private int firstCellX = 4;
    private int firstCellY = 4;
    private int secondCellX, secondCellY;
    private boolean firstCellPressed = false, secondCellPressed = false;

    private board() {
        board = new JButton[mLength][mWidth];
        int row, col;
        Random random = new Random();
        JFrame.setDefaultLookAndFeelDecorated(false);
        final JFrame myFrame = new JFrame("test");
        myFrame.setLayout(new GridLayout(11, 10));
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Game");
        fileMenu.addSeparator();
        JMenuItem newGameItem = new JMenuItem("New Game");
        fileMenu.add(newGameItem);
        newGameItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reShuffleCells(board);
                destroyMatchedCellsAfterAdding(board);
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
        myFrame.setJMenuBar(menuBar);
        for (col = 0; col < mLength; col++) {
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
                    myFrame.add(board[row][col]);
                }
            }
        }
        destroyMatchedCellsAfterAdding(board);
        score = 0;
        dbg = new JLabel[7];
        dbg[0] = new JLabel();
        myFrame.add(dbg[0]);
        dbg[1] = new JLabel();
        myFrame.add(dbg[1]);
        dbg[2] = new JLabel();
        myFrame.add(dbg[2]);
        dbg[3] = new JLabel();
        myFrame.add(dbg[3]);
        dbg[4] = new JLabel();
        myFrame.add(dbg[4]);
        dbg[5] = new JLabel();
        myFrame.add(dbg[5]);
        myFrame.setLocationByPlatform(true);
        myFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        myFrame.pack();
        myFrame.setSize(600, 600);
        myFrame.setVisible(true);
    }

    public static void main(String[] args) {
        board qq = new board();
        JFrame q = new JFrame();
    }

    private void reShuffleCells(JButton[][] board) {
        Random r = new Random();
        for (int i = mWidth - 6; i >= 5; i--) {
            for (int j = 5; j <= mLength - 6; j++) {
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
        source = e.getSource();
        tempX = getX(source, board);
        tempY = getY(source, board);
        secondCellColor = board[tempX][tempY].getBackground();
        secondCellX = tempX;
        secondCellY = tempY;
        //dbg[1].setText(firstCellY + "    || ");
        //dbg[2].setText(secondCellX + "  ");
        //dbg[3].setText(secondCellY + "  ");
        firstCellPressed = true;

        if (isSwapPossible()) {
            board[secondCellX][secondCellY].setBackground(firstCellColor);
            board[firstCellX][firstCellY].setBackground(secondCellColor);
            destroyMatchedCells(firstCellX, firstCellY, board);
            destroyMatchedCells(secondCellX, secondCellY, board);
            loweringCellsAfterDestroying(board);
            fillingDestroyedCells(board);
            destroyMatchedCellsAfterAdding(board);
            dbg[0].setText(String.valueOf(getScore()));

            firstCellX = 4;
            firstCellY = 4;
            secondCellX = 0;
            secondCellY = 0;
            secondCellPressed = false;
        } else {
            firstCellColor = secondCellColor;
            firstCellX = secondCellX;
            firstCellY = secondCellY;
            secondCellPressed = true;
            firstCellPressed = false;
        }
    }

    private boolean isSwapPossible() {
        return (moveXtoRightIsUsefull(firstCellX, firstCellY, board) && (firstCellY == secondCellY) && ((secondCellX - firstCellX) == 1)) ||
                (moveXtoLeftIsUsefull(firstCellX, firstCellY, board) && (firstCellY == secondCellY) && ((firstCellX - secondCellX) == 1)) ||
                (moveYUpIsUsefull(firstCellX, firstCellY, board) && (firstCellX == secondCellX) && ((secondCellY - firstCellY) == 1)) ||
                (moveYDownIsUsefull(firstCellX, firstCellY, board) && (firstCellX == secondCellX) && ((firstCellY - secondCellY) == 1)) ||
                (moveXtoRightIsUsefull(secondCellX, secondCellY, board) && (firstCellY == secondCellY) && ((firstCellX - secondCellX) == 1)) ||
                (moveXtoLeftIsUsefull(secondCellX, secondCellY, board) && (firstCellY == secondCellY) && ((secondCellX - firstCellX) == 1)) ||
                (moveYUpIsUsefull(secondCellX, secondCellY, board) && (firstCellX == secondCellX) && ((firstCellY - secondCellY) == 1)) ||
                (moveYDownIsUsefull(secondCellX, secondCellY, board) && (firstCellX == secondCellX) && ((secondCellY - firstCellY) == 1))
                        && (secondCellPressed) && (secondCellX + secondCellY >= 0);
    }

// --Commented out by Inspection START (03.06.2016 7:50):
//    private boolean matchexistcheker(JButton[][] board) {
//        boolean check = false;
//        for (int i = mWidth - 6; i >= 5; i--) {
//            for (int j = 5; j <= mLength - 6; j++) {
//                if (moveXtoRightIsUsefull(j, i, board) || moveXtoLeftIsUsefull(j, i, board) || moveYUpIsUsefull(j, i, board) || moveYDownIsUsefull(j, i, board))
//                    check = true;
//                break;
//            }
//        }
//        return check;
//    }
// --Commented out by Inspection STOP (03.06.2016 7:50)

    private void destroyMatchedCellsAfterAdding(JButton[][] board) {
        int t = 0;
        while (t <= 20) {
            for (int i = mWidth - 6; i >= 5; i--) {
                for (int j = 5; j <= mLength - 6; j++) {
                    destroyMatchedCells(j, i, board);
                    fillingDestroyedCells(board);
                    t++;
                }
            }
        }
    }

    private void fillingDestroyedCells(JButton[][] board) {
        Random r = new Random();
        for (int i = mWidth - 6; i >= 5; i--) {
            for (int j = 5; j <= mLength - 6; j++) {
                if (board[j][i].getBackground().equals(Color.WHITE)) {
                    int pos = r.nextInt(colors.length);
                    board[j][i].setBackground(colors[pos]);
                }
            }
        }

    }

    private void loweringCellsAfterDestroying(JButton[][] board) {
        for (int i = mWidth - 6; i >= 5; i--) {
            for (int j = 5; j <= mLength - 6; j++) {
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

    private void destroyMatchedCells(int x1, int y1, JButton[][] m) {
        boolean flag = true;
        //x+
        if (flag && (m[x1][y1].getBackground().equals(m[x1 + 1][y1].getBackground())) && (m[x1][y1].getBackground().equals(m[x1 + 2][y1].getBackground())) && (0 <= x1) && (x1 < mWidth - 2)) {
            if (flag && (m[x1][y1].getBackground().equals(m[x1 - 1][y1].getBackground())) && (1 <= x1)) {
                if (flag && (m[x1][y1].getBackground().equals(m[x1 + 3][y1].getBackground())) && (x1 < mWidth - 3)) {    //+x+++
                    m[x1][y1].setBackground(Color.white);
                    m[x1 + 1][y1].setBackground(Color.white);
                    m[x1 + 2][y1].setBackground(Color.white);
                    m[x1 - 1][y1].setBackground(Color.white);
                    m[x1 + 3][y1].setBackground(Color.white);
                    score = score + 5;
                    flag = false;
                }
                if (flag && (m[x1][y1].getBackground().equals(m[x1 - 2][y1].getBackground())) && (2 <= x1)) {    //++x++
                    m[x1][y1].setBackground(Color.white);
                    m[x1 + 1][y1].setBackground(Color.white);
                    m[x1 + 2][y1].setBackground(Color.white);
                    m[x1 - 1][y1].setBackground(Color.white);
                    m[x1 - 2][y1].setBackground(Color.white);
                    score = score + 5;
                    flag = false;
                } else {
                    m[x1][y1].setBackground(Color.white);                                   //+x++
                    m[x1 + 1][y1].setBackground(Color.white);
                    m[x1 + 2][y1].setBackground(Color.white);
                    m[x1 - 1][y1].setBackground(Color.white);
                    score = score + 4;
                    flag = false;
                }
            }
            if (flag && (m[x1][y1].getBackground().equals(m[x1 + 3][y1].getBackground())) && (x1 <= mWidth - 3)) {
                if (flag && (m[x1][y1].getBackground().equals(m[x1 + 4][y1].getBackground())) && (x1 <= mWidth - 3)) {    //x++++
                    m[x1][y1].setBackground(Color.white);
                    m[x1 + 1][y1].setBackground(Color.white);
                    m[x1 + 2][y1].setBackground(Color.white);
                    m[x1 + 3][y1].setBackground(Color.white);
                    m[x1 + 4][y1].setBackground(Color.white);
                    score = score + 5;
                    flag = false;
                } else {
                    m[x1][y1].setBackground(Color.white);                                   //x+++
                    m[x1 + 1][y1].setBackground(Color.white);
                    m[x1 + 2][y1].setBackground(Color.white);
                    m[x1 + 3][y1].setBackground(Color.white);
                    score = score + 4;
                    flag = false;
                }
            } else {
                m[x1][y1].setBackground(Color.white);                                       //x++
                m[x1 + 1][y1].setBackground(Color.white);
                m[x1 + 2][y1].setBackground(Color.white);
                score = score + 3;
                flag = false;
            }
        }
        //-x
        if (flag && (m[x1][y1].getBackground().equals(m[x1 - 1][y1].getBackground())) && (m[x1][y1].getBackground().equals(m[x1 - 2][y1].getBackground())) && (2 <= x1) && (x1 < mWidth)) {
            if (flag && (m[x1][y1].getBackground().equals(m[x1 + 1][y1].getBackground())) && (x1 < mWidth - 1)) {
                if (flag && (m[x1][y1].getBackground().equals(m[x1 - 3][y1].getBackground())) && (3 <= x1)) {   //+++x+
                    m[x1][y1].setBackground(Color.white);
                    m[x1 - 1][y1].setBackground(Color.white);
                    m[x1 - 2][y1].setBackground(Color.white);
                    m[x1 - 3][y1].setBackground(Color.white);
                    m[x1 + 1][y1].setBackground(Color.white);
                    score = score + 5;
                    flag = false;
                } else {                                                                    //++x+
                    m[x1][y1].setBackground(Color.white);
                    m[x1 - 1][y1].setBackground(Color.white);
                    m[x1 - 2][y1].setBackground(Color.white);
                    m[x1 + 1][y1].setBackground(Color.white);
                    score = score + 4;
                    flag = false;
                }
            }
            if (flag && (m[x1][y1].getBackground().equals(m[x1 - 3][y1].getBackground())) && (3 <= x1)) {
                if (flag && (m[x1][y1].getBackground().equals(m[x1 - 4][y1].getBackground())) && (5 <= x1)) {    //++++x
                    m[x1][y1].setBackground(Color.white);
                    m[x1 - 1][y1].setBackground(Color.white);
                    m[x1 - 2][y1].setBackground(Color.white);
                    m[x1 - 3][y1].setBackground(Color.white);
                    m[x1 - 4][y1].setBackground(Color.white);
                    score = score + 5;
                    flag = false;
                } else {
                    m[x1][y1].setBackground(Color.white);                                   //+++x
                    m[x1 - 1][y1].setBackground(Color.white);
                    m[x1 - 2][y1].setBackground(Color.white);
                    m[x1 - 3][y1].setBackground(Color.white);
                    score = score + 4;
                    flag = false;
                }
            } else {                                                                        //++x
                m[x1][y1].setBackground(Color.white);
                m[x1 - 1][y1].setBackground(Color.white);
                m[x1 - 2][y1].setBackground(Color.white);
                score = score + 3;
                flag = false;
            }
        }
        //y+
        if (flag && (m[x1][y1].getBackground().equals(m[x1][y1 + 1].getBackground())) && (m[x1][y1].getBackground().equals(m[x1][y1 + 2].getBackground())) && (0 <= y1) && (y1 < mWidth - 2)) {
            if (flag && (m[x1][y1].getBackground().equals(m[x1][y1 - 1].getBackground())) && (1 <= y1)) {
                if (flag && (m[x1][y1].getBackground().equals(m[x1][y1 + 3].getBackground())) && (y1 < mWidth - 3)) {    //+x+++
                    m[x1][y1].setBackground(Color.white);
                    m[x1][y1 + 1].setBackground(Color.white);
                    m[x1][y1 + 2].setBackground(Color.white);
                    m[x1][y1 - 1].setBackground(Color.white);
                    m[x1][y1 + 3].setBackground(Color.white);
                    score = score + 5;
                    flag = false;
                }
                if (flag && (m[x1][y1].getBackground().equals(m[x1][y1 - 2].getBackground())) && (2 <= y1)) {    //++x++
                    m[x1][y1].setBackground(Color.white);
                    m[x1][y1 + 1].setBackground(Color.white);
                    m[x1][y1 + 2].setBackground(Color.white);
                    m[x1][y1 - 1].setBackground(Color.white);
                    m[x1][y1 - 2].setBackground(Color.white);
                    score = score + 5;
                    flag = false;
                } else {
                    m[x1][y1].setBackground(Color.white);                                   //+x++
                    m[x1][y1 + 1].setBackground(Color.white);
                    m[x1][y1 + 2].setBackground(Color.white);
                    m[x1][y1 - 1].setBackground(Color.white);
                    score = score + 4;
                    flag = false;
                }
            }
            if (flag && (m[x1][y1].getBackground().equals(m[x1][y1 + 3].getBackground())) && (y1 < mWidth - 3)) {
                if (flag && (m[x1][y1].getBackground().equals(m[x1][y1 + 4].getBackground())) && (y1 < mWidth - 4)) {    //x++++
                    m[x1][y1].setBackground(Color.white);
                    m[x1][y1 + 1].setBackground(Color.white);
                    m[x1][y1 + 2].setBackground(Color.white);
                    m[x1][y1 + 3].setBackground(Color.white);
                    m[x1][y1 + 4].setBackground(Color.white);
                    score = score + 5;
                    flag = false;
                } else {
                    m[x1][y1].setBackground(Color.white);                                   //x+++
                    m[x1][y1 + 1].setBackground(Color.white);
                    m[x1][y1 + 2].setBackground(Color.white);
                    m[x1][y1 + 3].setBackground(Color.white);
                    score = score + 4;
                    flag = false;
                }
            } else {
                m[x1][y1].setBackground(Color.white);                                       //x++
                m[x1][y1 + 1].setBackground(Color.white);
                m[x1][y1 + 2].setBackground(Color.white);
                score = score + 3;
                flag = false;
            }
        }
        //y-
        if (flag && (m[x1][y1].getBackground().equals(m[x1][y1 - 1].getBackground())) && (m[x1][y1].getBackground().equals(m[x1][y1 - 2].getBackground())) && (2 <= y1)) {
            if (flag && (m[x1][y1].getBackground().equals(m[x1][y1 + 1].getBackground())) && (y1 < mWidth - 1)) {
                if (flag && (m[x1][y1].getBackground().equals(m[x1][y1 - 3].getBackground())) && (3 <= y1)) {   //+++x+
                    m[x1][y1].setBackground(Color.white);
                    m[x1][y1 - 1].setBackground(Color.white);
                    m[x1][y1 - 2].setBackground(Color.white);
                    m[x1][y1 - 3].setBackground(Color.white);
                    m[x1][y1 + 1].setBackground(Color.white);
                    score = score + 5;
                    flag = false;
                } else {                                                                    //++x+
                    m[x1][y1].setBackground(Color.white);
                    m[x1][y1 - 1].setBackground(Color.white);
                    m[x1][y1 - 2].setBackground(Color.white);
                    m[x1][y1 + 1].setBackground(Color.white);
                    score = score + 4;
                    flag = false;
                }
            }
            if (flag && (m[x1][y1].getBackground().equals(m[x1][y1 - 3].getBackground())) && (3 <= y1)) {
                if (flag && (m[x1][y1].getBackground().equals(m[x1][y1 - 4].getBackground())) && (5 <= y1)) {    //++++x
                    m[x1][y1].setBackground(Color.white);
                    m[x1][y1 - 1].setBackground(Color.white);
                    m[x1][y1 - 2].setBackground(Color.white);
                    m[x1][y1 - 3].setBackground(Color.white);
                    m[x1][y1 - 4].setBackground(Color.white);
                    score = score + 5;
                    flag = false;
                } else {
                    m[x1][y1].setBackground(Color.white);                                   //+++x
                    m[x1][y1 - 1].setBackground(Color.white);
                    m[x1][y1 - 2].setBackground(Color.white);
                    m[x1][y1 - 3].setBackground(Color.white);
                    score = score + 4;
                    flag = false;
                }
            } else {                                                                        //++x
                m[x1][y1].setBackground(Color.white);
                m[x1][y1 - 1].setBackground(Color.white);
                m[x1][y1 - 2].setBackground(Color.white);
                score = score + 3;
                flag = false;
            }
        }
        //-x+
        if (flag && (m[x1][y1].getBackground().equals(m[x1 + 1][y1].getBackground())) && (m[x1][y1].getBackground().equals(m[x1 - 1][y1].getBackground())) && (1 <= x1) && (x1 < mWidth - 1)) {
            m[x1][y1].setBackground(Color.white);
            m[x1 + 1][y1].setBackground(Color.white);
            m[x1 - 1][y1].setBackground(Color.white);
            score = score + 3;
            flag = false;
        }
        //-y+
        if (flag && (m[x1][y1].getBackground().equals(m[x1][y1 + 1].getBackground())) && (m[x1][y1].getBackground().equals(m[x1][y1 - 1].getBackground())) && (1 <= y1) && (y1 < mWidth - 1)) {
            m[x1][y1].setBackground(Color.white);
            m[x1][y1 + 1].setBackground(Color.white);
            m[x1][y1 - 1].setBackground(Color.white);
            score = score + 3;
            flag = false;
        }
    }

    int getY(Object t, JButton[][] m) {
        int row, col;
        int col1 = 100;
        for (row = 0; row < mLength; row++) {
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
        for (row = 0; row < mLength; row++)
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
        if ((checkColor(m, i, j, 1, 0, 1, 1, -1, -1, 1, -1)) ||
                (checkColor(m, i, j, 3, 0, 0, 0, -2, -3, 0, 0)) ||
                (checkColor(m, i, j, 1, 0, 2, 0, -1, -1, -1, -2)) ||
                (checkColor(m, i, j, 1, 0, 0, 2, -1, -1, 1, 2))) {
            return true;
        } else return false;
    }

    boolean moveXtoRightIsUsefull(int i, int j, JButton[][] m) {
        if ((checkColor(m, i, j, 0, 1, 1, 1, 1, 1, 1, -1)) ||
                (checkColor(m, i, j, 0, 3, 0, 0, 2, 3, 0, 0)) ||
                (checkColor(m, i, j, 0, 1, 0, 2, 1, 1, 1, 2)) ||
                (checkColor(m, i, j, 0, 1, 2, 0, 1, 1, -1, -2))) {
            return true;
        } else return false;
    }

    boolean moveYUpIsUsefull(int i, int j, JButton[][] m) {
        if ((checkColor(m, i, j, 0, 0, 0, 3, 0, 0, 2, 3)) ||
                (checkColor(m, i, j, 1, 1, 0, 1, 1, -1, 1, 1)) ||
                (checkColor(m, i, j, 0, 2, 0, 1, 1, 2, 1, 1)) ||
                (checkColor(m, i, j, 2, 0, 0, 1, -1, -2, 1, 1))) {
            return true;
        } else return false;
    }

    boolean moveYDownIsUsefull(int i, int j, JButton[][] m) {
        if ((checkColor(m, i, j, 0, 0, 3, 0, 0, 0, -2, -3)) ||
                (checkColor(m, i, j, 1, 1, 1, 0, -1, 1, -1, -1)) ||
                (checkColor(m, i, j, 0, 2, 1, 0, 1, 2, -1, -1)) ||
                (checkColor(m, i, j, 2, 0, 1, 0, -1, -2, -1, -1))) {
            return true;
        } else return false;
    }

    boolean checkColor(JButton[][] m, int i, int j, int lX, int rX, int lY, int rY, int x1, int x2, int y1, int y2) {                       //highwaytohell
        Color currentColor = m[i][j].getBackground();
        boolean flag = false;
        if ((i >= lX) && (i < mWidth - rX) && (j >= lY) && (j < mWidth - rY)) {
            flag = ((currentColor.equals(m[i + x1][j + y1].getBackground())) && (currentColor.equals(m[i + x2][j + y2].getBackground())));
        }
        return flag;
    }
}


