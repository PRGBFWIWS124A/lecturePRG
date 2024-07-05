import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class XCircle {

    public static void main(final String[] a) {
        final Circle circle = Circle.newCircle(150, 115, 50);
        final JFrame window = new JFrame();
        window.setLayout(new FlowLayout());
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setBounds(300, 300, 300, 300);

        final JComponent canvas = new MyCircleCanvas(circle);
        window.getContentPane().add(canvas);

        final JButton bigger = XCircle.addButton(window, circle, "Bigger!", CircleAction.BIGGER);
        final JButton narrower = XCircle.addButton(window, circle, "Smaller!", CircleAction.SMALLER);

        final KeyListener kl = new CircleKeyPressedListener() {
            @Override
            public void keyPressed(final KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        circle.performAction(CircleAction.UP);
                        break;
                    case KeyEvent.VK_DOWN:
                        circle.performAction(CircleAction.DOWN);
                        break;
                    case KeyEvent.VK_LEFT:
                        circle.performAction(CircleAction.LEFT);
                        break;
                    case KeyEvent.VK_RIGHT:
                        circle.performAction(CircleAction.RIGHT);
                        break;
                    default:
                        return;
                }
                window.repaint();
            }
        };
        for (final JComponent c : new JComponent[] { canvas, bigger, narrower }) {
            c.addKeyListener(kl);
        }

        window.setVisible(true);
    }

    private static JButton addButton(
        final JFrame window,
        final Circle circle,
        final String label,
        final CircleAction action
    ) {
        final JButton button = new MyCircleButton(label);
        button.addMouseListener(new CircleClickListener() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                circle.performAction(action);
                window.repaint();
            }
        });
        window.getContentPane().add(button);
        return button;
    }

    private XCircle(){}

}

abstract class CircleClickListener implements MouseListener {

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

abstract class CircleKeyPressedListener implements KeyListener {

    @Override
    public void keyReleased(final KeyEvent e) {
    }

    @Override
    public void keyTyped(final KeyEvent e) {
    }

}

@SuppressWarnings("serial")
class MyCircleButton extends JButton {

    MyCircleButton(final String label) {
        super(label);
        this.setPreferredSize(new Dimension(120, 30));
    }
}

@SuppressWarnings("serial")
class MyCircleCanvas extends JComponent {

    private final Circle circle;

    public MyCircleCanvas(final Circle circle) {
        this.setPreferredSize(new Dimension(300, 230));
        this.circle = circle;
    }

    @Override
    public void paint(final Graphics g) {
        g.drawOval(this.circle.getX() - this.circle.getRadius() / 2,
                this.circle.getY() - this.circle.getRadius() / 2,
                this.circle.getRadius(),
                this.circle.getRadius());
    }
}
