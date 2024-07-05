import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class XRectangle {

    public static void main(final String[] a) {
        final Rectangle rectangle = Rectangle.newRectangle(125, 75, 50, 50);
        final JFrame window = new JFrame();
        window.setLayout(new FlowLayout());
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setBounds(300, 300, 300, 300);

        final MyRectangleCanvas canvas = new MyRectangleCanvas(rectangle);
        window.getContentPane().add(canvas);

        final JButton widen = XRectangle.addButton(window, rectangle, "Widen!", RectangleAction.WIDEN);
        final JButton narrow = XRectangle.addButton(window, rectangle, "Narrow!", RectangleAction.NARROW);
        final JButton raise  = XRectangle.addButton(window, rectangle, "Raise!", RectangleAction.RAISE);
        final JButton lower = XRectangle.addButton(window, rectangle, "Lower!", RectangleAction.LOWER);

        final KeyListener kl = new RectangleKeyPressedListener() {
            @Override
            public void keyPressed(final KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        canvas.rotateLeft();
                        break;
                    case KeyEvent.VK_RIGHT:
                        canvas.rotateRight();
                        break;
                    default:
                        return;
                }
                window.repaint();
            }
        };
        for (final JComponent c : new JComponent[] { canvas, widen, narrow, raise, lower }) {
            c.addKeyListener(kl);
        }

        window.setVisible(true);
    }

    private static JButton addButton(
        final JFrame window,
        final Rectangle rectangle,
        final String label,
        final RectangleAction action
    ) {
        final JButton button = new MyRectangleButton(label);
        button.addMouseListener(new RectangleClickListener() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                rectangle.performAction(action);
                window.repaint();
            }
        });
        window.getContentPane().add(button);
        return button;
    }

    private XRectangle(){}

}

@SuppressWarnings("serial")
class MyRectangleButton extends JButton {

    MyRectangleButton(final String label) {
        super(label);
        this.setPreferredSize(new Dimension(120, 30));
    }
}

@SuppressWarnings("serial")
class MyRectangleCanvas extends JComponent {

    private final Rectangle rectangle;
    private double theta = 0;

    public MyRectangleCanvas(final Rectangle rectangle) {
        this.setPreferredSize(new Dimension(300, 200));
        this.rectangle = rectangle;
    }

    @Override
    public void paint(final Graphics g) {
        ((Graphics2D) g).rotate(this.theta, this.getWidth() / 2, this.getHeight() / 2);
        g.drawRect(this.rectangle.getX(), this.rectangle.getY(), this.rectangle.getWidth(), this.rectangle.getHeight());
    }

    void rotateLeft() {
        this.theta -= Math.PI / 45;
    }

    void rotateRight() {
        this.theta += Math.PI / 45;
    }
}

abstract class RectangleClickListener implements MouseListener {

    @Override
    public void mouseEntered(final MouseEvent e) {
    }

    @Override
    public void mouseExited(final MouseEvent e) {
    }

    @Override
    public void mousePressed(final MouseEvent e) {
    }

    @Override
    public void mouseReleased(final MouseEvent e) {
    }

}

abstract class RectangleKeyPressedListener implements KeyListener {

    @Override
    public void keyReleased(final KeyEvent e) {
    }

    @Override
    public void keyTyped(final KeyEvent e) {
    }

}
