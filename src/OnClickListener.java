import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;


/**
 * Created by eniml on 01.06.2016.
 */
class OnClickListener implements ActionListener {
    private static final Random RANDOM = new Random();
    private static JLabel[] dbg;
    private final int LENGTH = 8;
    private final int WIDTH = 8;
    private final Color[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.PINK, Color.YELLOW, Color.CYAN};
    private Cell[][] board;
    private Color firstCellColor, secondCellColor;
    private int score;
    private int fCX = 0;
    private int fCY = 0;
    private int sCX, sCY;
    private boolean secondCellPressed = false;

    private OnClickListener() {
        board = new Cell[LENGTH][WIDTH];
        int row, col;
        Random random = new Random();
        final JFrame myFrame = getjFrame();
        addCells(random, myFrame);
        destroyMatchedCellsAfterAdding(board, 20);
    }

    public static void main(String[] args) {
        OnClickListener qq = new OnClickListener();
        JFrame q = new JFrame();
    }

    private JFrame getjFrame() {
        final JFrame myFrame = new JFrame("test");
        myFrame.setLayout(new GridLayout(9, 8));
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Game");
        fileMenu.addSeparator();
        JMenuItem newGameItem = new JMenuItem("New Game");
        fileMenu.add(newGameItem);
        newGameItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reShuffleCells(board);
                destroyMatchedCellsAfterAdding(board, 20);
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
        score = 0;
        dbg = new JLabel[7];
        dbg[0] = new JLabel();
        myFrame.setLocationByPlatform(true);
        myFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        myFrame.pack();
        myFrame.setSize(600, 600);
        myFrame.setVisible(true);
        return myFrame;
    }

    private void addCells(Random random, JFrame myFrame) {
		//TODO please move declaration to cicle 
        int col;
        int row;
        for (col = LENGTH - 1; col >= 0; col--) {
            for (row = 0; row < WIDTH; row++) {
                board[row][col] = new Cell(row, col);
                int pos = random.nextInt(colors.length);
                board[row][col].setBackground(colors[pos]);
                board[row][col].setCondition("normal");
                board[row][col].setToolTipText(board[row][col].get_x() + " " + board[row][col].get_y() + board[row][col].getCondition());
                board[row][col].addActionListener(this);
                myFrame.add(board[row][col]);
            }
        }
        myFrame.validate();
    }

    void reShuffleCells(Cell[][] board) {
        Random r = new Random();
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < WIDTH; j++) {
                int pos = r.nextInt(colors.length);
                board[i][j].setBackground(colors[pos]);
            }
        }
    }

    int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    //TO DO + hascode equals.
    //TODO extract variables like cuurentColor
    //TODO extract methods/classes
    //TODO create maven module and move all libraries there
    //TO DO + remove extra fields (borders)
    public void actionPerformed(ActionEvent e) {
        int tempX, tempY;
        Object tempSrc = e.getSource();
        if (!(tempSrc instanceof Cell))
            return;
        Cell source = (Cell) e.getSource();
        tempX = source.get_x();
        tempY = source.get_y();
        sCX = tempX;
        sCY = tempY;
        secondCellColor = board[tempX][tempY].getBackground();
        boolean firstCellPressed = true;

        if (explosionPossible()) {
            swap();
            findLines(board);
            loweringCellsAfterDestroying(board);
            fillingDestroyedCells(board);
            destroyMatchedCellsAfterAdding(board, 3);
            dbg[0].setText(String.valueOf(getScore()));
            fCX = 0;
            fCY = 0;
            sCX = 0;
            sCY = 0;
            secondCellPressed = false;
        } else {
            firstCellColor = secondCellColor;
            fCX = sCX;
            fCY = sCY;
            secondCellPressed = true;
            firstCellPressed = false;
        }
    }

    private void swap() {
        board[sCX][sCY].setBackground(firstCellColor);
        board[fCX][fCY].setBackground(secondCellColor);
    }

    private boolean explosionPossible() {
        return (moveXtoRightIsUsefull(fCX, fCY, board) && (fCY == sCY) && ((sCX - fCX) == 1)) ||
                (moveXtoLeftIsUsefull(fCX, fCY, board) && (fCY == sCY) && ((fCX - sCX) == 1)) ||
                (moveYUpIsUsefull(fCX, fCY, board) && (fCX == sCX) && ((sCY - fCY) == 1)) ||
                (moveYDownIsUsefull(fCX, fCY, board) && (fCX == sCX) && ((fCY - sCY) == 1)) ||
                (moveXtoRightIsUsefull(sCX, sCY, board) && (fCY == sCY) && ((fCX - sCX) == 1)) ||
                (moveXtoLeftIsUsefull(sCX, sCY, board) && (fCY == sCY) && ((sCX - fCX) == 1)) ||
                (moveYUpIsUsefull(sCX, sCY, board) && (fCX == sCX) && ((fCY - sCY) == 1)) ||
                (moveYDownIsUsefull(sCX, sCY, board) && (fCX == sCX) && ((sCY - fCY) == 1))
                        && (secondCellPressed) && (sCX + sCY >= 0);
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

    void destroyMatchedCellsAfterAdding(Cell[][] board, int k) {
        int t = 0;
        while (t <= k) {
            findLines(board);
            loweringCellsAfterDestroying(board);
            fillingDestroyedCells(board);
            t++;
        }
    }


    private void fillingDestroyedCells(Cell[][] board) {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if (board[i][j].getBackground().equals(Color.WHITE)) {
                    int pos = RANDOM.nextInt(colors.length);
                    board[i][j].setBackground(colors[pos]);
                }
            }
        }

    }

    private void loweringCellsAfterDestroying(Cell[][] board) {
        for (int j = 0; j < WIDTH; j++) {
            for (int i = 0; i < LENGTH; i++) {
                int k;
                k = j + 1;
                while ((board[i][j].getBackground().equals(Color.WHITE)) && (k < LENGTH)) {
                    board[i][j].setBackground(board[i][k].getBackground());
                    board[i][k].setBackground(Color.WHITE);
                    k++;
                }
            }
        }
    }

    void findLines(Cell[][] board) {
        Color tempColor;
        Cell tempButton = null;
        MassBox[] xBox = new MassBox[20];
        MassBox[] yBox = new MassBox[20];
        int xLineCounter = 0;
        for (int j = 0; j < LENGTH; j++) {
            tempColor = board[0][j].getBackground();
            int rowCounter = 1;
            for (int i = 1; i < WIDTH; i++) {
                if (board[i][j].getBackground().equals(tempColor)) {
                    rowCounter++;
                    tempButton = board[i][j];
                } else {
						//TODO why 3??? 
                    if (rowCounter >= 3) {
                        xLineCounter++;
                        xBox[xLineCounter] = new MassBox(rowCounter, tempButton.get_x(), tempButton.get_y());
                        rowCounter = 1;
                        tempColor = board[i][j].getBackground();
                    } else {
                        tempColor = board[i][j].getBackground();
                        rowCounter = 1;
                    }
                }
            }
			//TODO why 3??? 
            if (rowCounter >= 3) {
                xLineCounter++;
                xBox[xLineCounter] = new MassBox(rowCounter, tempButton.get_x(), tempButton.get_y());
            }
        }
        int yLineCounter = 0;
        for (int i = 0; i < LENGTH; i++) {
            tempColor = board[i][0].getBackground();
            int colCounter = 1;
            for (int j = 1; j < LENGTH; j++) {
                if (board[i][j].getBackground().equals(tempColor)) {
                    colCounter++;
                    tempButton = board[i][j];
                } else {
                    if (colCounter >= 3) {
                        yLineCounter++;
                        yBox[yLineCounter] = new MassBox(colCounter, tempButton.get_x(), tempButton.get_y());
                        colCounter = 1;
                        tempColor = board[i][j].getBackground();
                    } else {
                        tempColor = board[i][j].getBackground();
                        colCounter = 1;
                    }
                }
            }
			//TODO why 3??? Please extract constant and give meaningfull name 
            if (colCounter >= 3) {
                yLineCounter++;
                yBox[yLineCounter] = new MassBox(colCounter, tempButton.get_x(), tempButton.get_y());
            }
        }
        while (xLineCounter > 0) {
            int x = xBox[xLineCounter].getLastBoxX();
            int y = xBox[xLineCounter].getLastBoxY();
            int counter = xBox[xLineCounter].getNumberOfCells();
			//TODO meaningless variable name
            int t = 0;
            while (counter > 0) {
                board[x - t][y].setCondition("toDestroy");
                t++;
                counter--;
            }
            xLineCounter--;
        }

        while (yLineCounter > 0) {
            int x = yBox[yLineCounter].getLastBoxX();
            int y = yBox[yLineCounter].getLastBoxY();
            int counter = yBox[yLineCounter].getNumberOfCells();
            int t = 0;
            while (counter > 0) {
                board[x][y - t].setCondition("toDestroy");
                t++;
                counter--;

            }
            yLineCounter--;
        }

        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < LENGTH; j++) {
				//TODO best practice is comparing string vs variable but not visa versa
				//anyway this code shall be replaced with enum
                if (board[i][j].getCondition().equals("toDestroy")) {
                    board[i][j].setBackground(Color.white);
                    board[i][j].setCondition("normal");
                }
            }
        }
    }


    boolean moveXtoLeftIsUsefull(int i, int j, Cell[][] m) {
        return (checkColor(m, i, j, 1, 0, 1, 1, -1, -1, 1, -1)) ||
                (checkColor(m, i, j, 3, 0, 0, 0, -2, -3, 0, 0)) ||
                (checkColor(m, i, j, 1, 0, 2, 0, -1, -1, -1, -2)) ||
                (checkColor(m, i, j, 1, 0, 0, 2, -1, -1, 1, 2));
    }

    boolean moveXtoRightIsUsefull(int i, int j, Cell[][] m) {
        return (checkColor(m, i, j, 0, 1, 1, 1, 1, 1, 1, -1)) ||
                (checkColor(m, i, j, 0, 3, 0, 0, 2, 3, 0, 0)) ||
                (checkColor(m, i, j, 0, 1, 0, 2, 1, 1, 1, 2)) ||
                (checkColor(m, i, j, 0, 1, 2, 0, 1, 1, -1, -2));
    }

    boolean moveYUpIsUsefull(int i, int j, Cell[][] m) {
        return (checkColor(m, i, j, 0, 0, 0, 3, 0, 0, 2, 3)) ||
                (checkColor(m, i, j, 1, 1, 0, 1, 1, -1, 1, 1)) ||
                (checkColor(m, i, j, 0, 2, 0, 1, 1, 2, 1, 1)) ||
                (checkColor(m, i, j, 2, 0, 0, 1, -1, -2, 1, 1));
    }

    boolean moveYDownIsUsefull(int i, int j, Cell[][] m) {
        return (checkColor(m, i, j, 0, 0, 3, 0, 0, 0, -2, -3)) ||
                (checkColor(m, i, j, 1, 1, 1, 0, -1, 1, -1, -1)) ||
                (checkColor(m, i, j, 0, 2, 1, 0, 1, 2, -1, -1)) ||
                (checkColor(m, i, j, 2, 0, 1, 0, -1, -2, -1, -1));
    }

    boolean checkColor(Cell[][] m, int i, int j, int lX, int rX, int lY, int rY, int x1, int x2, int y1, int y2) {                       //highwaytohell
        Color currentColor = m[i][j].getBackground();
        boolean flag = false;
        if ((i >= lX) && (i < WIDTH - rX) && (j >= lY) && (j < WIDTH - rY)) {
            flag = ((currentColor.equals(m[i + x1][j + y1].getBackground())) && (currentColor.equals(m[i + x2][j + y2].getBackground())));
        }
        return flag;
    }
}
