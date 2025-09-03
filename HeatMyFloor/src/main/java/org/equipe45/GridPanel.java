package org.equipe45;



import org.equipe45.drawing.Afficheur;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GridPanel extends JPanel implements MouseWheelListener {

    public int gridSize = 30;
    private int hoverX = -1, hoverY = -1;
    private String selectedCuttingMethod = "cutterRectangle";
    private int depth = 5;
    private int thickness = 5;
    private boolean gridVisible = true;
    private MainFrame mainFrame;  // Reference to the MainFrame


    // Existing constructor (empty)
    public GridPanel() {
        this(null);  // Call the constructor that accepts MainFrame, with null
    }

    // Constructor that accepts MainFrame
    public GridPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        addMouseWheelListener(this);
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                hoverX = (e.getX() / gridSize) * gridSize;
                hoverY = (e.getY() / gridSize) * gridSize;
                repaint();
            }
            //mouse dragged
            @Override
            public void mouseDragged(MouseEvent e) {
                GridPanel.this.mouseDragged(e);
            }





        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                GridPanel.this.mouseClicked(e);
            }
            //mouse pressed
            @Override
            public void mousePressed(MouseEvent e) {
                GridPanel.this.mousePressed(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                GridPanel.this.mouseReleased(e);
            }
        });
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_E) {
                    GridPanel.this.handleBackspacePress(e);
                }
            }
        });

        setFocusable(true);

    }

    public void setSelectedCuttingMethod(String method) {
        this.selectedCuttingMethod = method;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public void setThickness(int thickness) {
        this.thickness = thickness;
    }

    public void setGridVisible(boolean visible) {
        this.gridVisible = visible;
        repaint();
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        super.paintComponent(g);
        if (gridVisible) {

            for (int x = 0; x <= getWidth(); x += gridSize) {
                for (int y = 0; y <= getHeight(); y += gridSize) {
                    if (x == hoverX && y == hoverY) {
                        g.setColor(Color.YELLOW);
                        g.fillRect(x, y, gridSize, gridSize);
                    }
                    g.setColor(Color.BLACK);
                    g.drawRect(x, y, gridSize, gridSize);
                }
            }
        }
        Afficheur afficheur = new Afficheur(this.mainFrame.cncController);
        afficheur.draw(g, gridSize);
    }


    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int notches = e.getWheelRotation();
        gridSize = notches < 0 ? Math.min(gridSize + 5, 100) : Math.max(gridSize - 5, 10);
        System.out.println(gridSize);
        repaint();
    }


    public void mouseClicked(MouseEvent e) {
        int x = (e.getX() / gridSize) * gridSize;
        int y = (e.getY() / gridSize) * gridSize;

        mainFrame.testClick(x, y);
    }

    public void mouseDragged(MouseEvent e) {
        int x = (e.getX() / gridSize) * gridSize;
        int y = (e.getY() / gridSize) * gridSize;

        mainFrame.testDrag(x, y);
    }

    public void mousePressed(MouseEvent e) {
        int x = (e.getX() / gridSize) * gridSize;
        int y = (e.getY() / gridSize) * gridSize;

        mainFrame.testPress(x, y);
    }

    public void mouseReleased(MouseEvent e) {
        int x = (e.getX() / gridSize) * gridSize;
        int y = (e.getY() / gridSize) * gridSize;

        mainFrame.testRelease(x, y);
    }

    public void handleBackspacePress(KeyEvent e)
    {
        mainFrame.handleBackspacePress();
    }


}
