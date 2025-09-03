package org.equipe39;

import org.equipe39.domain.CNCController;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MenuPanel menuPanel;
    public GridPanel gridPanel;
    public CNCController cncController;
    private String selectedCuttingMethod = "cutterRectangle";
    private float depth = 1;
    private int thickness = 0;


    public MainFrame() {
        setTitle("Grid Mouse Interaction");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        gridPanel = new GridPanel(this);
        menuPanel = new MenuPanel(this);
        cncController = new CNCController();

        add(gridPanel, BorderLayout.CENTER);
        add(menuPanel, BorderLayout.EAST);

        //gridPanel.addMouseListener(this);
        // Add key listener to capture backspace key events


        setFocusable(true);  // Make sure the frame can receive key events

        setVisible(true);
    }

    public void setSelectedCuttingMethod(String method) {
        this.selectedCuttingMethod = method;
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame());
    }


    public void testClick(int x, int y) {
        cncController.controllerClick(x, y, this.selectedCuttingMethod, menuPanel.depth, menuPanel.thickness, gridPanel.gridSize);
        this.menuPanel.toggleEditArea(cncController.getSelectedCut());
        gridPanel.repaint();
    }

    //drag
    public void testDrag(int x, int y) {

        cncController.controllerDrag(x, y, this.selectedCuttingMethod, menuPanel.depth, menuPanel.thickness);
        gridPanel.repaint();
    }

    //press and release
    public void testPress(int x, int y) {
        cncController.controllerPress(x, y, this.selectedCuttingMethod, menuPanel.depth, menuPanel.thickness);
        gridPanel.repaint();
    }

    public void testRelease(int x, int y) {
        cncController.controllerRelease(x, y, this.selectedCuttingMethod, menuPanel.depth, menuPanel.thickness);
        gridPanel.repaint();
    }

    public void handleBackspacePress()
    {
        System.out.println("del");
        this.cncController.deleteSelectedCut();
    }

    public int getThickness() {
        return thickness;
    }

    public void setThickness(int thickness) {
        this.thickness = thickness;
    }

    public float getDepth() {
        return depth;
    }

    public void setDepth(float depth) {
        this.depth = depth;
    }

}
