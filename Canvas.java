/*
 * HINWEIS:
 *
 * Sie brauchen den Java-Code, der in dieser Datei steht, nicht zu lesen oder zu
 * verstehen. Alle Hinweise, die zur Verwendung der Datei nötig sind, können Sie
 * aus den jeweiligen Aufgabentexten entnehmen.
 */

import java.awt.*;
import java.awt.event.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.util.*;

import javax.swing.*;

public class Canvas {

    private static class ChooseColor extends GraphicAction {

        private final Color color;

        ChooseColor(final Color c) {
            this.color = c;
        }

        @Override
        public boolean drawsSomething() {
            return false;
        }

        @Override
        void replay(final Graphics2D g, final java.awt.Rectangle boundsParam) {
            g.setColor(this.color);
        }

    }

    private static class DrawForward extends MoveForward {

        DrawForward(final int length) {
            super(length);
        }

        @Override
        void replay(final Graphics2D g, final java.awt.Rectangle boundsParam) {
            g.drawLine(0, 0, 0, this.length);
            super.replay(g, boundsParam);
        }

    }

    private static class DrawSquare extends GraphicAction {

        private final double length;

        DrawSquare(final double length) {
            this.length = length;
        }

        @Override
        void replay(final Graphics2D g, final java.awt.Rectangle boundsParam) {
            final double hl = this.length / 2;
            final Rectangle2D r = new Rectangle2D.Double();
            r.setFrame(-hl, -hl, this.length, this.length);
            g.fill(r);
            if (boundsParam != null) {
                Canvas.addToBounds(r, boundsParam, g);
            }
        }

    }

    private static class DrawTextLabel extends GraphicAction {

        private final String text;

        DrawTextLabel(final String text) {
            this.text = text;
        }

        @Override
        void replay(final Graphics2D g, final java.awt.Rectangle boundsParam) {
            final AffineTransform origTrans = g.getTransform();
            final Point2D center = origTrans.transform(new Point2D.Double(0, 0), null);
            final FontRenderContext frc = g.getFontRenderContext();
            final Font f = g.getFont();
            final TextLayout tl = new TextLayout(this.text, f, frc);
            final double w = tl.getBounds().getWidth();
            final double h = tl.getBounds().getHeight();
            this.moveTextToCenterOfBox(g, center, w, h);
            this.drawBox(g, w, h, tl, boundsParam);
            g.setTransform(origTrans);
        }

        private void drawBox(
            final Graphics2D g,
            final double w,
            final double h,
            final TextLayout tl,
            final java.awt.Rectangle boundsParam
        ) {
            final java.awt.Rectangle r = new java.awt.Rectangle(0, (int)-h, (int)w, (int)h);
            final Shape textShape = tl.getOutline(new AffineTransform());
            g.setColor(Color.BLACK);
            r.grow(4, 4);
            g.setColor(Color.WHITE);
            g.fill(r);
            g.setColor(Color.BLACK);
            g.draw(r);
            g.fill(textShape);
            if (boundsParam != null) {
                Canvas.addToBounds(r, boundsParam, g);
            }
        }

        private void moveTextToCenterOfBox(final Graphics2D g, final Point2D center, final double w, final double h) {
            final AffineTransform trans = new AffineTransform();
            trans.setToTranslation(Math.round(center.getX() - w / 2), Math.round(center.getY() + h / 2));
            g.setTransform(trans);
        }

    }

    private static abstract class GraphicAction {

        protected final Canvas canvas = Canvas.INSTANCE;

        public boolean drawsSomething() {
            return true;
        }

        abstract void replay(Graphics2D g, java.awt.Rectangle boundsParam);

    }

    private static class Move extends GraphicAction {

        private final double x;

        private final double y;

        Move(final double x, final double y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean drawsSomething() {
            return false;
        }

        @Override
        void replay(final Graphics2D g, final java.awt.Rectangle boundsParam) {
            g.translate(this.x, this.y);
            if (boundsParam != null) {
                Canvas.addToBounds(new Point2D.Double(this.x, this.y), boundsParam, g);
            }
        }

    }

    private static class MoveForward extends GraphicAction {

        final int length;

        MoveForward(final int length) {
            this.length = length;
        }

        @Override
        public boolean drawsSomething() {
            return false;
        }

        @Override
        void replay(final Graphics2D g, final java.awt.Rectangle boundsParam) {
            Canvas.addCurrentPos(g, boundsParam);
            final AffineTransform t = g.getTransform();
            t.translate(0, this.length);
            g.setTransform(t);
            Canvas.addCurrentPos(g, boundsParam);
        }

    }

    private static class Pop extends GraphicAction {

        @Override
        void replay(final Graphics2D g, final java.awt.Rectangle boundsParam) {
            g.setTransform(this.canvas.transformations.pop());
        }

    }

    private static class Push extends GraphicAction {

        @Override
        void replay(final Graphics2D g, final java.awt.Rectangle boundsParam) {
            this.canvas.transformations.push(g.getTransform());
        }

    }

    private static class Rotate extends GraphicAction {

        final int degree;

        Rotate(final int degree) {
            this.degree = degree;
        }

        @Override
        public boolean drawsSomething() {
            return false;
        }

        @Override
        void replay(final Graphics2D g, final java.awt.Rectangle boundsParam) {
            final AffineTransform t = g.getTransform();
            t.rotate(this.degree / 180.0 * Math.PI);
            g.setTransform(t);
        }

    }

    static final Color BROWN = new Color(160, 82, 45);

    static final Color GREEN = Color.GREEN;

    static final Canvas INSTANCE = new Canvas();

    static void chooseColor(final Color c) {
        Canvas.INSTANCE.addAction(new ChooseColor(c));
    }

    static void drawForward(final int length) {
        Canvas.INSTANCE.addAction(new DrawForward(length));
    }

    static void drawTextLabel(final String text) {
        Canvas.INSTANCE.addAction(new DrawTextLabel(text));
    }

    static void move(final double x, final double y) {
        Canvas.INSTANCE.addAction(new Move(x, y));
    }

    static void moveForward(final int length) {
        Canvas.INSTANCE.addAction(new MoveForward(length));
    }

    static void pop() {
        Canvas.INSTANCE.addAction(new Pop());
    }

    static void push() {
        Canvas.INSTANCE.addAction(new Push());
    }

    static void refresh() {
        synchronized (Canvas.INSTANCE) {}
        Canvas.INSTANCE.drawing.revalidate();
        Canvas.INSTANCE.ui.revalidate();
        Canvas.INSTANCE.ui.repaint();
    }

    static void rotate(final int degrees) {
        Canvas.INSTANCE.addAction(new Rotate(degrees));
    }

    static void square(final double length) {
        Canvas.INSTANCE.addAction(new DrawSquare(length));
    }

    private static void addCurrentPos(final Graphics2D g, final java.awt.Rectangle boundsParam) {
        if (boundsParam != null) {
            Canvas.addToBounds(new Point2D.Float(0f, 0f), boundsParam, g);
        }
    }

    private static void addToBounds(final Point2D point, final java.awt.Rectangle boundsParam, final Graphics2D g) {
        boundsParam.add(g.getTransform().transform(point, null));
    }

    private static void addToBounds(final Rectangle2D r, final java.awt.Rectangle boundsParam, final Graphics2D g) {
        final double maxX = r.getMaxX();
        final double maxY = r.getMaxY();
        final double minX = r.getMinX();
        final double minY = r.getMinY();
        Canvas.addToBounds(new Point2D.Double(maxX, maxY), boundsParam, g);
        Canvas.addToBounds(new Point2D.Double(maxX, minY), boundsParam, g);
        Canvas.addToBounds(new Point2D.Double(minX, maxY), boundsParam, g);
        Canvas.addToBounds(new Point2D.Double(minX, minY), boundsParam, g);
    }

    protected java.awt.Rectangle bounds;

    private final ArrayList<Canvas.GraphicAction> actions = new ArrayList<>();

    private final JComponent drawing = new JPanel() {

        private static final long serialVersionUID = -1665573331455268961L;

        @Override
        public Dimension getPreferredSize() {
            synchronized (Canvas.this) {
                return Canvas.this.bounds == null ? new Dimension(100, 100) : Canvas.this.bounds.getSize();
            }
        }

        @Override
        public void paintComponent(final Graphics g) {
            super.paintComponent(g);
            synchronized (Canvas.this) {
                final Graphics2D g2 = (Graphics2D)g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                final java.awt.Rectangle clipBounds = g.getClipBounds();
                g2.setColor(Color.WHITE);
                g2.fill(clipBounds);
                g2.setColor(Color.BLACK);
                final AffineTransform t = g2.getTransform();
                this.initBoundsIfNeeded(g2);
                final GraphicAction init = this.getMoveDrawingInsideScreenAction();
                this.drawActions(g2, t, init);
            }
        }

        private void drawActions(final Graphics2D g2, final AffineTransform t, final Canvas.GraphicAction init) {
            g2.setTransform(t);
            Canvas.this.transformations.clear();
            int drawCounter = 0;
            init.replay(g2, null);
            for (final GraphicAction a : Canvas.this.actions) {
                if (drawCounter >= Canvas.this.renderMaxDraws) {
                    break;
                }
                a.replay(g2, null);
                if (a.drawsSomething()) {
                    drawCounter++;
                }
            }
        }

        private Canvas.GraphicAction getMoveDrawingInsideScreenAction() {
            return new Move(20 - Canvas.this.bounds.x, 20 - Canvas.this.bounds.y);
        }

        private void initBoundsIfNeeded(final Graphics2D g2) {
            if (Canvas.this.bounds == null) {
                final java.awt.Rectangle newBounds = new java.awt.Rectangle();
                Canvas.this.transformations.clear();
                for (final GraphicAction a : Canvas.this.actions) {
                    a.replay(g2, newBounds);
                }
                Canvas.this.bounds = new java.awt.Rectangle(newBounds);
            }
        }

    };

    private int draws;

    private final ActionListener handler = new ActionListener() {

        @Override
        public void actionPerformed(final ActionEvent e) {
            switch (e.getActionCommand()) {
                case "start":
                    Canvas.this.renderMaxDraws = 0;
                    break;
                case "next":
                    if (Canvas.this.renderMaxDraws < Canvas.this.draws) {
                        Canvas.this.renderMaxDraws++;
                    }
                    break;
                case "back":
                    if (Canvas.this.renderMaxDraws > 0) {
                        Canvas.this.renderMaxDraws--;
                    }
                    break;
                case "end":
                    Canvas.this.renderMaxDraws = Canvas.this.draws;
                    break;
            }
            Canvas.this.step.setText("Schritt: " + Canvas.this.renderMaxDraws);
            Canvas.this.drawing.repaint();
        }

    };

    private int renderMaxDraws = 0;

    private final JLabel step = new JLabel("Schritt: " + this.renderMaxDraws);

    private final Stack<AffineTransform> transformations = new Stack<>();

    private final JFrame ui = new JFrame() {

        private static final long serialVersionUID = 8620900696432559397L;

        {
            final JScrollPane scrollPane = new JScrollPane(Canvas.this.drawing);
            scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
            scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            final JPanel panel = new JPanel();
            final JButton startButton = new JButton("Anfang");
            startButton.setActionCommand("start");
            startButton.addActionListener(Canvas.this.handler);
            final JButton backButton = new JButton("Zurück");
            backButton.setActionCommand("back");
            backButton.addActionListener(Canvas.this.handler);
            final JButton forwardButton = new JButton("Vor");
            forwardButton.setActionCommand("next");
            forwardButton.addActionListener(Canvas.this.handler);
            final JButton endButton = new JButton("Ende");
            endButton.setActionCommand("end");
            endButton.addActionListener(Canvas.this.handler);
            this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            final GroupLayout jPanel1Layout = new GroupLayout(panel);
            panel.setLayout(jPanel1Layout);
            jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(
                    GroupLayout.Alignment.LEADING
                ).addGap(0, 158, Short.MAX_VALUE)
            );
            jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(
                    GroupLayout.Alignment.LEADING
                ).addGap(0, 36, Short.MAX_VALUE)
            );
            final GroupLayout layout = new GroupLayout(this.getContentPane());
            this.getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(
                    GroupLayout.Alignment.LEADING
                ).addGroup(
                    GroupLayout.Alignment.TRAILING,
                    layout.createSequentialGroup().addContainerGap().addGroup(
                        layout.createParallelGroup(
                            GroupLayout.Alignment.TRAILING
                        ).addComponent(
                            scrollPane,
                            GroupLayout.Alignment.LEADING,
                            GroupLayout.DEFAULT_SIZE,
                            458,
                            Short.MAX_VALUE
                        ).addGroup(
                            GroupLayout.Alignment.LEADING,
                            layout.createSequentialGroup().addComponent(
                                startButton
                            ).addGap(
                                6,
                                6,
                                6
                            ).addComponent(
                                backButton
                            ).addGap(
                                6,
                                6,
                                6
                            ).addComponent(
                                forwardButton
                            ).addGap(
                                6,
                                6,
                                6
                            ).addComponent(
                                endButton
                            ).addGap(
                                18,
                                18,
                                18
                            ).addComponent(
                                Canvas.this.step
                            ).addPreferredGap(
                                LayoutStyle.ComponentPlacement.RELATED
                            ).addComponent(
                                panel,
                                GroupLayout.DEFAULT_SIZE,
                                GroupLayout.DEFAULT_SIZE,
                                Short.MAX_VALUE
                            )
                        )
                    ).addContainerGap()
                )
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(
                    GroupLayout.Alignment.LEADING
                ).addGroup(
                    GroupLayout.Alignment.TRAILING,
                    layout.createSequentialGroup().addContainerGap().addComponent(
                        scrollPane,
                        GroupLayout.DEFAULT_SIZE,
                        400,
                        Short.MAX_VALUE
                    ).addPreferredGap(
                        LayoutStyle.ComponentPlacement.RELATED
                    ).addGroup(
                        layout.createParallelGroup(
                            GroupLayout.Alignment.TRAILING
                        ).addComponent(
                            panel,
                            GroupLayout.PREFERRED_SIZE,
                            GroupLayout.DEFAULT_SIZE,
                            GroupLayout.PREFERRED_SIZE
                        ).addGroup(
                            layout.createParallelGroup(
                                GroupLayout.Alignment.BASELINE
                            ).addComponent(
                                startButton
                            ).addComponent(
                                backButton
                            ).addComponent(
                                forwardButton
                            ).addComponent(
                                endButton
                            ).addComponent(
                                Canvas.this.step
                            )
                        )
                    ).addContainerGap()
                )
            );
            this.pack();
            this.setVisible(true);
            Canvas.this.drawing.revalidate();
            Canvas.this.drawing.repaint();
            this.setExtendedState(this.getExtendedState() | Frame.MAXIMIZED_BOTH);
        }

    };

    private void addAction(final GraphicAction action) {
        synchronized (this) {
            this.actions.add(action);
            if (action.drawsSomething()) {
                this.draws++;
            }
            this.bounds = null;
        }
    }

}
