import javax.swing.*;

/**
 * Created by eniml on 05.06.2016.
 */
public class Cell extends JButton {
    /*static MyJButton[][] jButton;*/
    int _x;

    public Cell(int _x, int _y) {
        this._x = _x;
        this._y = _y;
    }

    public int get_y() {
        return _y;
    }

    public void set_y(int _y) {
        this._y = _y;
    }

    public int get_x() {
        return _x;
    }

    public void set_x(int _x) {
        this._x = _x;
    }

    int _y;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cell myJButton = (Cell) o;

        if (_x != myJButton._x) return false;
        if (_y != myJButton._y) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = _x;
        result = 31 * result + _y;
        return result;
    }





}
