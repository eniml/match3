import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;


/**
 * Created by eniml on 01.06.2016.
 */
public class OnClickListener implements ActionListener {
    private Cell[][] board;
    private static JLabel[] dbg;
    private final int LENGTH = 20;
    private final int WIDTH = 20;
    private final Color[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.PINK, Color.YELLOW, Color.CYAN};
    private Color firstCellColor, secondCellColor;
    private Cell source;
    private int score;
    //private String[] textColor = new String[]{"red", "green", "blue"/*, "pink", "yellow", "cyan", "white"*/};
    private int firstCellX = 4;
    private int firstCellY = 4;
    private int secondCellX, secondCellY;
    private boolean firstCellPressed = false, secondCellPressed = false;
    public static final Random RANDOM = new Random();

    private OnClickListener() {
        board = new Cell[LENGTH][WIDTH];
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
        for (col = 0; col < LENGTH; col++) {
            for (row = 0; row < WIDTH; row++) {
                boolean bool = true;
                board[row][col] = new Cell(row, col);
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
        OnClickListener qq = new OnClickListener();
        JFrame q = new JFrame();
    }

    private void reShuffleCells(JButton[][] board) {
        Random r = new Random();
        for (int i = WIDTH - 6; i >= 5; i--) {
            for (int j = 5; j <= LENGTH - 6; j++) {
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

    //TODO hascode equals.
    //extract variables like cuurentColor
    //extract methods/classes
    //create maven module and move all libraries there
    //remove extra fields (borders)
    public void actionPerformed(ActionEvent e) {
        int tempX, tempY;
        Object tempSrc = e.getSource();

        if (!(tempSrc instanceof Cell))
            return;

        source = (Cell)e.getSource();

        tempX = source.get_x();//getX(source, board);
        tempY = source.get_y(); /*getY(source, board);*/
        secondCellX = tempX;
        secondCellY = tempY;
        secondCellColor = board[tempX][tempY].getBackground();

        //dbg[1].setText(firstCellY + "    || ");
        //dbg[2].setText(secondCellX + "  ");
        //dbg[3].setText(secondCellY + "  ");
        firstCellPressed = true;

        if (explosionPossible()) {
            swap();
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

    private void swap() {
        board[secondCellX][secondCellY].setBackground(firstCellColor);
        board[firstCellX][firstCellY].setBackground(secondCellColor);
    }

    private boolean explosionPossible() {
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
            for (int i = WIDTH - 6; i >= 5; i--) {
                for (int j = 5; j <= LENGTH - 6; j++) {
                    destroyMatchedCells(j, i, board);
                    fillingDestroyedCells(board);
                    t++;
                }
            }
        }
    }

    private void fillingDestroyedCells(JButton[][] board) {
        for (int i = WIDTH - 6; i >= 5; i--) {
            for (int j = 5; j <= LENGTH - 6; j++) {
                if (board[j][i].getBackground().equals(Color.WHITE)) {
                    int pos = RANDOM.nextInt(colors.length);
                    board[j][i].setBackground(colors[pos]);
                }
            }
        }

    }

    private void loweringCellsAfterDestroying(JButton[][] board) {
        for (int i = WIDTH - 6; i >= 5; i--) {
            for (int j = 5; j <= LENGTH - 6; j++) {
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
        Color currentColor = m[x1][y1].getBackground();
        if (flag && (currentColor.equals(m[x1 + 1][y1].getBackground())) && (currentColor.equals(m[x1 + 2][y1].getBackground())) && (0 <= x1) && (x1 < WIDTH - 2)) {
            if (flag && (currentColor.equals(m[x1 - 1][y1].getBackground())) && (1 <= x1)) {
                if (flag && (currentColor.equals(m[x1 + 3][y1].getBackground())) && (x1 < WIDTH - 3)) {    //+x+++
                    m[x1][y1].setBackground(Color.white);
                    m[x1 + 1][y1].setBackground(Color.white);
                    m[x1 + 2][y1].setBackground(Color.white);
                    m[x1 - 1][y1].setBackground(Color.white);
                    m[x1 + 3][y1].setBackground(Color.white);
                    score = score + 5;
                    flag = false;
                }
                if (flag && (currentColor.equals(m[x1 - 2][y1].getBackground())) && (2 <= x1)) {    //++x++
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
            if (flag && (currentColor.equals(m[x1 + 3][y1].getBackground())) && (x1 <= WIDTH - 3)) {
                if (flag && (currentColor.equals(m[x1 + 4][y1].getBackground())) && (x1 <= WIDTH - 3)) {    //x++++
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
        if (flag && (currentColor.equals(m[x1 - 1][y1].getBackground())) && (currentColor.equals(m[x1 - 2][y1].getBackground())) && (2 <= x1) && (x1 < WIDTH)) {
            if (flag && (currentColor.equals(m[x1 + 1][y1].getBackground())) && (x1 < WIDTH - 1)) {
                if (flag && (currentColor.equals(m[x1 - 3][y1].getBackground())) && (3 <= x1)) {   //+++x+
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
            if (flag && (currentColor.equals(m[x1 - 3][y1].getBackground())) && (3 <= x1)) {
                if (flag && (currentColor.equals(m[x1 - 4][y1].getBackground())) && (5 <= x1)) {    //++++x
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
        if (flag && (currentColor.equals(m[x1][y1 + 1].getBackground())) && (currentColor.equals(m[x1][y1 + 2].getBackground())) && (0 <= y1) && (y1 < WIDTH - 2)) {
            if (flag && (currentColor.equals(m[x1][y1 - 1].getBackground())) && (1 <= y1)) {
                if (flag && (currentColor.equals(m[x1][y1 + 3].getBackground())) && (y1 < WIDTH - 3)) {    //+x+++
                    m[x1][y1].setBackground(Color.white);
                    m[x1][y1 + 1].setBackground(Color.white);
                    m[x1][y1 + 2].setBackground(Color.white);
                    m[x1][y1 - 1].setBackground(Color.white);
                    m[x1][y1 + 3].setBackground(Color.white);
                    score = score + 5;
                    flag = false;
                }
                if (flag && (currentColor.equals(m[x1][y1 - 2].getBackground())) && (2 <= y1)) {    //++x++
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
            if (flag && (currentColor.equals(m[x1][y1 + 3].getBackground())) && (y1 < WIDTH - 3)) {
                if (flag && (currentColor.equals(m[x1][y1 + 4].getBackground())) && (y1 < WIDTH - 4)) {    //x++++
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
        if (flag && (currentColor.equals(m[x1][y1 - 1].getBackground())) && (currentColor.equals(m[x1][y1 - 2].getBackground())) && (2 <= y1)) {
            if (flag && (currentColor.equals(m[x1][y1 + 1].getBackground())) && (y1 < WIDTH - 1)) {
                if (flag && (currentColor.equals(m[x1][y1 - 3].getBackground())) && (3 <= y1)) {   //+++x+
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
            if (flag && (currentColor.equals(m[x1][y1 - 3].getBackground())) && (3 <= y1)) {
                if (flag && (currentColor.equals(m[x1][y1 - 4].getBackground())) && (5 <= y1)) {    //++++x
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

                score = score + 3;
                flag = false;
            }
        }
        //-x+
        if (flag && (currentColor.equals(m[x1 + 1][y1].getBackground())) && (currentColor.equals(m[x1 - 1][y1].getBackground())) && (1 <= x1) && (x1 < WIDTH - 1)) {
            m[x1][y1].setBackground(Color.white);
            m[x1 + 1][y1].setBackground(Color.white);
            m[x1 - 1][y1].setBackground(Color.white);
            score = score + 3;
            flag = false;
        }
        //-y+
        if (flag && (currentColor.equals(m[x1][y1 + 1].getBackground())) && (currentColor.equals(m[x1][y1 - 1].getBackground())) && (1 <= y1) && (y1 < WIDTH - 1)) {
            m[x1][y1].setBackground(Color.white);
            m[x1][y1 + 1].setBackground(Color.white);
            m[x1][y1 - 1].setBackground(Color.white);
            score = score + 3;
            flag = false;
        }
    }







    boolean moveXtoLeftIsUsefull(int i, int j, JButton[][] m) {
        Color currentColor = m[i][j].getBackground();
        if (((currentColor.equals(m[i - 1][j + 1].getBackground())) && (currentColor.equals(m[i - 1][j - 1].getBackground())) && ((1 <= i) && (i < WIDTH) && (1 <= j) && (j < WIDTH - 1))) ||         //16
                ((currentColor.equals(m[i - 2][j].getBackground())) && (currentColor.equals(m[i - 3][j].getBackground())) && ((3 <= i) && (i < WIDTH) && (0 <= j) && (j < WIDTH))) ||                 //10
                ((currentColor.equals(m[i - 1][j - 1].getBackground())) && (currentColor.equals(m[i - 1][j - 2].getBackground())) && ((1 <= i) && (i < WIDTH) && (2 <= j) && (j < WIDTH))) ||         //8
                ((currentColor.equals(m[i - 1][j + 1].getBackground())) && (currentColor.equals(m[i - 1][j + 2].getBackground()))) && ((1 <= i) && (i < WIDTH) && (0 <= j) && (j < WIDTH - 2)))      //12
        {
            return true;
        } else return false;
    }

    boolean moveXtoRightIsUsefull(int i, int j, JButton[][] m) {

        Color currentClolor = m[i][j].getBackground();
        if ((currentClolor.equals(m[i + 1][j + 1].getBackground())) && (currentClolor.equals(m[i + 1][j - 1].getBackground()) && ((0 <= i) && (i < WIDTH - 1) && (1 <= j) && (j < WIDTH - 1))) ||               //14
                ((currentClolor.equals(m[i + 2][j].getBackground())) && (currentClolor.equals(m[i + 3][j].getBackground())) && ((0 <= i) && (i < WIDTH - 3) && (0 <= j) && (j < WIDTH))) ||                 //4
                ((currentClolor.equals(m[i + 1][j + 1].getBackground())) && (currentClolor.equals(m[i + 1][j + 2].getBackground())) && ((0 <= i) && (i < WIDTH - 1) && (0 <= j) && (j < WIDTH - 2))) ||         //2
                ((currentClolor.equals(m[i + 1][j - 1].getBackground())) && (currentClolor.equals(m[i + 1][j - 2].getBackground()))) && ((0 <= i) && (i < WIDTH - 1) && (2 <= j) && (j < WIDTH)))           //6
        {
            return true;
        } else return false;
    }

    boolean moveYUpIsUsefull(int i, int j, JButton[][] m) {
        Color currentColor = m[i][j].getBackground();
        if (((currentColor.equals(m[i][j + 2].getBackground())) && (currentColor.equals(m[i][j + 3].getBackground())) && ((0 <= i) && (i < WIDTH) && (0 <= j) && (j < WIDTH - 3))) ||                      //1
                ((currentColor.equals(m[i + 1][j + 1].getBackground())) && (currentColor.equals(m[i - 1][j + 1].getBackground())) && ((1 <= i) && (i < WIDTH - 1) && (0 <= j) && (j < WIDTH - 1))) ||          //13
                ((currentColor.equals(m[i + 1][j + 1].getBackground())) && (currentColor.equals(m[i + 2][j + 1].getBackground())) && ((0 <= i) && (i < WIDTH - 2) && (0 <= j) && (j < WIDTH - 1))) ||          //3
                ((currentColor.equals(m[i - 1][j + 1].getBackground())) && (currentColor.equals(m[i - 2][j + 1].getBackground())) && ((2 <= i) && (i < WIDTH) && (0 <= j) && (j < WIDTH - 1))))          //11
        {
            return true;
        } else return false;
    }

    boolean moveYDownIsUsefull(int i, int j, JButton[][] m) {
        Color currentColor = m[i][j].getBackground();
        if (
                ((currentColor.equals(m[i][j - 2].getBackground())) && (currentColor.equals(m[i][j - 3].getBackground())) && ((0 <= i) && (i < WIDTH) && (3 <= j) && (j < WIDTH))) ||                  //7
                        ((currentColor.equals(m[i - 1][j - 1].getBackground())) && (currentColor.equals(m[i + 1][j - 1].getBackground())) && ((1 <= i) && (i < WIDTH - 1) && (1 <= j) && (j < WIDTH))) ||          //15
                        ((currentColor.equals(m[i + 1][j - 1].getBackground())) && (currentColor.equals(m[i + 2][j - 1].getBackground())) && ((0 <= i) && (i < WIDTH - 2) && (1 <= j) && (j < WIDTH))) ||          //5
                        ((currentColor.equals(m[i - 1][j - 1].getBackground())) && (currentColor.equals(m[i - 2][j - 1].getBackground())) && ((2 <= i) && (i < WIDTH) && (1 <= j) && (j < WIDTH))))           //9
        {
            return true;
        } else return false;
    }
}