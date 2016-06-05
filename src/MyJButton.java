import javax.swing.*;
import java.awt.*;

/**
 * Created by eniml on 05.06.2016.
 */
public class MyJButton extends JButton {
    static MyJButton[][] jButton;
    int ID;

    public MyJButton(int ID) {
        this.ID = ID;
    }

    public static void main(String[] args) {
        JFrame tframe = new JFrame();
        tframe.setLayout(new GridLayout(1, 2));
        jButton = new MyJButton[2][2];
        jButton[0][0] = new MyJButton(2);
        jButton[0][1] = new MyJButton(5);
        tframe.add(jButton[0][1]);
        tframe.add(jButton[0][0]);
        System.out.print(jButton[0][0].getID());
        tframe.setLocationByPlatform(true);
        tframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        tframe.pack();
        tframe.setSize(600, 600);
        tframe.setVisible(true);
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

}
