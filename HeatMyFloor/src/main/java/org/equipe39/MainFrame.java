package org.equipe39;

import org.equipe39.domain.CNCController;
import org.equipe39.dto.PanelDTO;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MenuPanel menuPanel;
    public GridPanel gridPanel;
    public CNCController cncController;
    private String selectedCuttingMethod = "cutterRectangle";
    private double depth = 1;
    private double profondeur = 0;


    public MainFrame() {

        setTitle("Grid Mouse Interaction");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        cncController = new CNCController();
        gridPanel = new GridPanel(this);
        menuPanel = new MenuPanel(this);

        add(gridPanel, BorderLayout.CENTER);
        add(menuPanel, BorderLayout.EAST);

        //gridPanel.addMouseListener(this);
        // Add key listener to capture backspace key events


        setFocusable(true);  // Make sure the frame can receive key events

        setVisible(true);
        PanelDTO panel = this.cncController.getPanelDTO();
        this.gridPanel.setPanelInTheMiddle(panel);
    }

    public void setSelectedCuttingMethod(String method) {
        this.selectedCuttingMethod = method;
    }

    public String getSelectedCuttingMethod()
    {
        return selectedCuttingMethod;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame());
    }


    public void testClick(double x, double y) {
        //System.out.println(gridPanel.gridSize);
        if (this.selectedCuttingMethod == "restrictedArea" || this.selectedCuttingMethod == "select")
        {
            cncController.controllerClick(x, y, this.selectedCuttingMethod, null, menuPanel.profondeur);
        }
        else if (menuPanel.selectedOutil == null) {
            handleWarning("Vous devez choisir ou créer un outil avant de commencer à faire des coupes");
            return;
        }
        else if(this.selectedCuttingMethod == "cutterBorder")
        {
            this.cncController.cutManager.handleBorderCreation(this.menuPanel.bordureSize, this.menuPanel.selectedOutil.toOutil(), this.menuPanel.profondeur);
            return;
        }
        else
        {
            cncController.controllerClick(x, y, this.selectedCuttingMethod, menuPanel.selectedOutil.toOutil(), menuPanel.profondeur);
        }
        this.menuPanel.toggleEditArea(cncController.getSelectedCut());
        gridPanel.repaint();
    }

    //drag
    public void testDrag(double x, double y) {

        cncController.controllerDrag(x, y);
        gridPanel.repaint();
    }

    //press and release
    public void testPress(double x, double y) {
        if (menuPanel.selectedOutil != null) {
            cncController.controllerPress(x, y, this.selectedCuttingMethod, menuPanel.selectedOutil.toOutil(), menuPanel.profondeur);
        }

        gridPanel.repaint();
    }

    public void testRelease(double x, double y) {
        if (menuPanel.selectedOutil != null) {
            cncController.controllerRelease(x, y, this.selectedCuttingMethod, menuPanel.selectedOutil.toOutil(), menuPanel.profondeur);
        }
        gridPanel.repaint();
    }

    public void testHover(double x, double y) {
        this.menuPanel.setMouseCoordsPanel(x, y);
        //cncController.controllerHover(x, y, this.selectedCuttingMethod, menuPanel.depth, menuPanel.profondeur);
        gridPanel.repaint();
    }

    //vous devez creer un outil pop up warning
    public void handleWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Warning", JOptionPane.WARNING_MESSAGE);
    }


    /*public void handleBackspacePress()
    {
        System.out.println("del");
        this.cncController.deleteSelectedCut();
    }*/

    public double getProfondeur() {
        return profondeur;
    }

    public void setProfondeur(double profondeur) {
        this.profondeur = profondeur;
    }

    public double getDepth() {
        return depth;
    }

    public void setDepth(float depth) {
        this.depth = depth;
    }

}
