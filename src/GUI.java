import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by eniml on 06.06.2016.
 */
class GUI {
    private final int LENGTH = 8;
    private final int WIDTH = 8;
    private Cell[][] board;

    public GUI() {
        // final OnClickListener a = new OnClickListener();
        JFrame myFrame = new JFrame("test");
        JFrame.setDefaultLookAndFeelDecorated(false);
        myFrame.setLayout(new GridLayout(9, 8));
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Game");
        fileMenu.addSeparator();
        JMenuItem newGameItem = new JMenuItem("New Game");
        fileMenu.add(newGameItem);
       /* newGameItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                a.reShuffleCells();
                a.destroyMatchedCellsAfterAdding(board, 20);
            }
        });*/
        JMenuItem exitItem = new JMenuItem("Exit");
        fileMenu.add(exitItem);
        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        menuBar.add(fileMenu);
        myFrame.setJMenuBar(menuBar);
        myFrame.setLocationByPlatform(true);
        myFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        myFrame.pack();
        myFrame.setSize(600, 600);
        myFrame.setVisible(true);
    }
}
