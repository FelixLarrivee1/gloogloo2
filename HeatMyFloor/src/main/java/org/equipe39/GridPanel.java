package org.equipe39;

import org.equipe39.domain.CNCController;
import org.equipe39.domain.ConversionPiedMM;
import org.equipe39.drawing.Afficheur;
import org.equipe39.dto.PanelDTO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import static java.lang.System.*;

public class GridPanel extends JPanel implements MouseWheelListener {

    public double gridSize = ConversionPiedMM.FACTEUR_CONVERSION;
    private double hoverX = -1, hoverY = -1;
    private boolean gridVisible = true;
    private int offsetX = 0;
    private int offsetY = 0;
    private int staticGridWidth = 100;
    private int staticGridHeight = 100;
    private boolean gridMagnified;

    private MainFrame mainFrame; // Reference to MainFrame

    public GridPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        out.println(mainFrame.cncController);
        addMouseWheelListener(this);

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                Point2D processedClickCoordinates = mouseClickCoordinatesHandler(new Point2D.Double(e.getX(), e.getY()));;
                hoverX = processedClickCoordinates.getX();
                hoverY = processedClickCoordinates.getY();
                mainFrame.testHover( hoverX,  hoverY);

                repaint();
            }

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

            @Override
            public void mousePressed(MouseEvent e) {
                GridPanel.this.mousePressed(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                GridPanel.this.mouseReleased(e);
            }
        });

        setFocusable(true);

    }

    public void setGridVisible(boolean visible) {
        this.gridVisible = visible;
        repaint();
    }


    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        //g2d.scale(1, -1);
        //g2d.translate(0, -mainFrame.cncController.adjustedHeight);
       /* AffineTransform originalTransform = g2d.getTransform();
        System.out.println(originalTransform);
        AffineTransform transform = new AffineTransform();
        System.out.println(transform);
        transform.translate(0, mainFrame.cncController.adjustedHeight);
        System.out.println(transform);
        transform.scale(1, -1);
        System.out.println(transform);
        //g2d.transform(transform);
*/
        Afficheur afficheur = new Afficheur(this.mainFrame.cncController);
        afficheur.draw(g2d, (int) gridSize, offsetX, offsetY, mainFrame.cncController.getPanelDTO().heightFactor, mainFrame.cncController.getPanelDTO().widthFactor);

        if (gridVisible) {

            if (this.staticGridWidth != 0 && this.staticGridHeight != 0) {
                PanelDTO panelDTO = this.mainFrame.cncController.getPanelDTO();

                int adjustedWidth = (int) ((panelDTO.widthFactor / ConversionPiedMM.FACTEUR_CONVERSION) * gridSize);
                int adjustedHeight = (int) ((panelDTO.heightFactor / ConversionPiedMM.FACTEUR_CONVERSION) * gridSize);
                int adjustedX = (int) ((panelDTO.x / ConversionPiedMM.FACTEUR_CONVERSION) * gridSize) - offsetX;
                int adjustedY = (int) ((panelDTO.y / ConversionPiedMM.FACTEUR_CONVERSION) * gridSize) - offsetY;
                int nbCarrWid = (int) (panelDTO.widthFactor / (staticGridWidth));
                int nbCarrVert = (int) (panelDTO.heightFactor / (staticGridHeight));
                int verticalSpacing = (int) (adjustedWidth / (panelDTO.widthFactor / (staticGridWidth)));
                int horizontalSpacing = (int) (adjustedHeight / (panelDTO.heightFactor / (staticGridHeight)));
                //g.setClip(adjustedX, adjustedY, adjustedWidth, adjustedHeight);

                /*System.out.println(adjustedWidth);
                System.out.println(panelDTO.widthFactor);
                System.out.println(adjustedX);
                System.out.println(verticalSpacing);
                System.out.println(staticGridWidth);*/

                for (int i = 1; i <= nbCarrWid; i++) {
                    int x = adjustedX + i * verticalSpacing;
                    g2d.setColor(Color.GRAY);

                    g2d.drawLine(x, adjustedY, x, adjustedY + adjustedHeight);
                }

                // Draw horizontal grid lines
                for (int i = 1; i <= nbCarrVert; i++) { // Start at 1 to skip the panel edge
                    int y = adjustedY + i * horizontalSpacing;
                    g2d.setColor(Color.GRAY);
                    g2d.drawLine(adjustedX, y, adjustedX + adjustedWidth, y);


                }

                //FIX LATER
                // Highlight hovered cell (if within the panel boundaries)
                /*if (hoverX != -1 && hoverY != -1) {
                    double cellX = (hoverX - offsetX - adjustedX) / verticalSpacing;
                    double cellY = (hoverY - offsetY - adjustedY) / horizontalSpacing;

                    int highlightX = (int) (adjustedX + cellX * verticalSpacing);
                    int highlightY = (int) (adjustedY + cellY * horizontalSpacing);

                    if (highlightX >= adjustedX && highlightX < adjustedX + adjustedWidth &&
                            highlightY >= adjustedY && highlightY < adjustedY + adjustedHeight) {
                        g.setColor(Color.YELLOW);
                        g.fillRect(highlightX, highlightY, verticalSpacing, horizontalSpacing);
                    }
                }*/
            }
        }
//        if (gridVisible) {
//            // Vérifiez que staticGridWidth et staticGridHeight sont définis
//            if (this.staticGridWidth != 0 && this.staticGridHeight != 0) {
//                PanelDTO panelDTO = this.mainFrame.cncController.getPanelDTO();
//
//                // Calcul des dimensions ajustées basées sur panelDTO et gridSize
//                int adjustedWidth = (int) (panelDTO.widthFactor * gridSize);
//                int adjustedHeight = (int) (panelDTO.heightFactor * gridSize);
//                int adjustedX = (int) (panelDTO.x * gridSize) - offsetX;
//                int adjustedY = (int) (panelDTO.y * gridSize) - offsetY;
//
//                // Calcul du nombre de cellules
//                int nbCarrWid = (int) (panelDTO.widthFactor / gridSize);
//                int nbCarrVert = (int) (panelDTO.heightFactor / gridSize);
//
//                // Calcul de l'espacement
//                int verticalSpacing = (int) gridSize;
//                int horizontalSpacing = (int) gridSize;
//
//                // Supprimez la définition de la zone de clipping
//                g2d.setClip(adjustedX, adjustedY, adjustedWidth, adjustedHeight);
//
//                System.out.println("adjustedWidth: " + adjustedWidth);
//                System.out.println("panelDTO.widthFactor: " + panelDTO.widthFactor);
//                System.out.println("adjustedX: " + adjustedX);
//                System.out.println("verticalSpacing: " + verticalSpacing);
//                System.out.println("staticGridWidth: " + staticGridWidth);
//
//                // Dessiner les lignes verticales
//                for (int i = 0; i <= nbCarrWid; i++) {
//                    int x = adjustedX + i * verticalSpacing;
//                    if (x >= 0 && x <= getWidth()) {
//                        g2d.setColor(Color.BLACK);
//                        g2d.drawLine(x, adjustedY, x, adjustedY + adjustedHeight);
//                    }
//                }
//
//                // Dessiner les lignes horizontales
//                for (int i = 0; i <= nbCarrVert; i++) {
//                    int y = adjustedY + i * horizontalSpacing;
//                    if (y >= 0 && y <= getHeight()) {
//                        g2d.setColor(Color.BLACK);
//                        g2d.drawLine(adjustedX, y, adjustedX + adjustedWidth, y);
//                    }
//
//                }
//
//                // Optionnel : Dessiner une grille supplémentaire si nécessaire
//            }
//        }
//


        /*if (this.mainFrame.getSelectedCuttingMethod() == "restrictedArea") {
            if (this.mainFrame.cncController.getCickedPointSequenceById(0) != null) {
                // Draw a rectangle from the first point hovered to the current point hovered
                Point2D firstPoint = this.mainFrame.cncController.getCickedPointSequenceById(0);
                Point2D currentPoint = new Point2D.Double( hoverX,  hoverY);

                // Adjust for offset and grid size
                double adjustedFirstX = (firstPoint.getX() / ConversionPiedMM.FACTEUR_CONVERSION) * this.gridSize - offsetX;
                double adjustedFirstY = (firstPoint.getY() / ConversionPiedMM.FACTEUR_CONVERSION) * this.gridSize - offsetY;
                double adjustedCurrentX = (currentPoint.getX() / ConversionPiedMM.FACTEUR_CONVERSION) * this.gridSize - offsetX;
                double adjustedCurrentY = (currentPoint.getY() / ConversionPiedMM.FACTEUR_CONVERSION) * this.gridSize - offsetY;

                g.setColor(Color.RED);
                g.drawRect((int)adjustedFirstX,(int) adjustedFirstY, (int)(adjustedCurrentX - adjustedFirstX), (int) (adjustedCurrentY - adjustedFirstY));
            }
        }

// Square
        if (this.mainFrame.getSelectedCuttingMethod() == "cutterRectangle") {
            if (this.mainFrame.cncController.getCickedPointSequenceById(0) != null && this.mainFrame.cncController.getCickedPointSequenceById(1) == null) {
                Point2D firstPoint = this.mainFrame.cncController.getCickedPointSequenceById(0);
                Point2D currentPoint = new Point2D.Double( hoverX,  hoverY);

                // Adjust for offset and grid size
                double adjustedFirstX = (firstPoint.getX() / ConversionPiedMM.FACTEUR_CONVERSION) * this.gridSize - offsetX;
                double adjustedFirstY = (firstPoint.getY() / ConversionPiedMM.FACTEUR_CONVERSION) * this.gridSize - offsetY;
                double adjustedCurrentX = (currentPoint.getX() / ConversionPiedMM.FACTEUR_CONVERSION) * this.gridSize - offsetX;
                double adjustedCurrentY = (currentPoint.getY() / ConversionPiedMM.FACTEUR_CONVERSION) * this.gridSize - offsetY;

                g.setColor(Color.BLACK);
                g.drawLine((int)adjustedFirstX, (int)adjustedFirstY, (int)adjustedCurrentX, (int) adjustedCurrentY);
            }
            if (this.mainFrame.cncController.getCickedPointSequenceById(1) != null) {
                // Draw a rectangle from the second point to the hover point
                Point2D secondPoint = this.mainFrame.cncController.getCickedPointSequenceById(1);
                Point2D currentPoint = new Point2D.Double(hoverX, hoverY);

                // Adjust for offset and grid size
                double adjustedSecondX = (secondPoint.getX() / ConversionPiedMM.FACTEUR_CONVERSION) * this.gridSize - offsetX;
                double adjustedSecondY = (secondPoint.getY() / ConversionPiedMM.FACTEUR_CONVERSION) * this.gridSize - offsetY;
                double adjustedCurrentX = (currentPoint.getX() / ConversionPiedMM.FACTEUR_CONVERSION) * this.gridSize - offsetX;
                double adjustedCurrentY = (currentPoint.getY() / ConversionPiedMM.FACTEUR_CONVERSION) * this.gridSize - offsetY;

                g.drawRect((int)adjustedSecondX, (int)adjustedSecondY, (int)(adjustedCurrentX - adjustedSecondX), (int)(adjustedCurrentY - adjustedSecondY));
            }
        }

// L method behaves like restricted area
        if (this.mainFrame.getSelectedCuttingMethod() == "cutterL") {
            if (this.mainFrame.cncController.getCickedPointSequenceById(0) != null) {
                // Draw a rectangle from the first point hovered to the current point hovered
                Point2D firstPoint = this.mainFrame.cncController.getCickedPointSequenceById(0);
                Point2D currentPoint = new Point2D.Double( hoverX,  hoverY);

                // Adjust for offset and grid size
                double adjustedFirstX = (firstPoint.getX() / ConversionPiedMM.FACTEUR_CONVERSION) * this.gridSize - offsetX;
                double adjustedFirstY = (firstPoint.getY() / ConversionPiedMM.FACTEUR_CONVERSION) * this.gridSize - offsetY;
                double adjustedCurrentX = (currentPoint.getX() / ConversionPiedMM.FACTEUR_CONVERSION) * this.gridSize - offsetX;
                double adjustedCurrentY = (currentPoint.getY() / ConversionPiedMM.FACTEUR_CONVERSION) * this.gridSize - offsetY;

                g.setColor(Color.RED);
                g.drawRect((int)adjustedFirstX, (int)adjustedFirstY, (int)(adjustedCurrentX - adjustedFirstX), (int)(adjustedCurrentY - adjustedFirstY));
            }
        }*/

        double varVert = mainFrame.cncController.adjustedHeight;

        if (this.mainFrame.getSelectedCuttingMethod() == "restrictedArea") {
            if (this.mainFrame.cncController.getCickedPointSequenceById(0) != null) {
                Point2D firstPoint = this.mainFrame.cncController.getCickedPointSequenceById(0);
                Point2D currentPoint = new Point2D.Double(hoverX, hoverY);

                double adjustedFirstX = (firstPoint.getX() / ConversionPiedMM.FACTEUR_CONVERSION) * this.gridSize - offsetX;
                double adjustedFirstY = ((varVert - firstPoint.getY()) / ConversionPiedMM.FACTEUR_CONVERSION) * this.gridSize - offsetY;

                double adjustedCurrentX = (currentPoint.getX() / ConversionPiedMM.FACTEUR_CONVERSION) * this.gridSize - offsetX;
                double adjustedCurrentY = ((varVert - currentPoint.getY()) / ConversionPiedMM.FACTEUR_CONVERSION) * this.gridSize - offsetY;

                double x = Math.min(adjustedFirstX, adjustedCurrentX);
                double y = Math.min(adjustedFirstY, adjustedCurrentY);
                double width = Math.abs(adjustedCurrentX - adjustedFirstX);
                double height = Math.abs(adjustedCurrentY - adjustedFirstY);

                g.setColor(Color.RED);
                g.drawRect((int) x, (int) y, (int) width, (int) height);
            }
        }

        if (this.mainFrame.getSelectedCuttingMethod() == "cutterRectangle") {
            if (this.mainFrame.cncController.getCickedPointSequenceById(0) != null && this.mainFrame.cncController.getCickedPointSequenceById(1) == null) {
                Point2D firstPoint = this.mainFrame.cncController.getCickedPointSequenceById(0);
                Point2D currentPoint = new Point2D.Double(hoverX, hoverY);

                double adjustedFirstX = (firstPoint.getX() / ConversionPiedMM.FACTEUR_CONVERSION) * this.gridSize - offsetX;
                double adjustedFirstY = ((varVert - firstPoint.getY()) / ConversionPiedMM.FACTEUR_CONVERSION) * this.gridSize - offsetY;

                double adjustedCurrentX = (currentPoint.getX() / ConversionPiedMM.FACTEUR_CONVERSION) * this.gridSize - offsetX;
                double adjustedCurrentY = ((varVert - currentPoint.getY()) / ConversionPiedMM.FACTEUR_CONVERSION) * this.gridSize - offsetY;

                g.setColor(Color.BLACK);
                g.drawLine((int) adjustedFirstX, (int) adjustedFirstY, (int) adjustedCurrentX, (int) adjustedCurrentY);
            }

            if (this.mainFrame.cncController.getCickedPointSequenceById(1) != null) {
                Point2D secondPoint = this.mainFrame.cncController.getCickedPointSequenceById(1);
                Point2D currentPoint = new Point2D.Double(hoverX, hoverY);

                double adjustedSecondX = (secondPoint.getX() / ConversionPiedMM.FACTEUR_CONVERSION) * this.gridSize - offsetX;
                double adjustedSecondY = ((varVert - secondPoint.getY()) / ConversionPiedMM.FACTEUR_CONVERSION) * this.gridSize - offsetY;

                double adjustedCurrentX = (currentPoint.getX() / ConversionPiedMM.FACTEUR_CONVERSION) * this.gridSize - offsetX;
                double adjustedCurrentY = ((varVert - currentPoint.getY()) / ConversionPiedMM.FACTEUR_CONVERSION) * this.gridSize - offsetY;

                double x = Math.min(adjustedSecondX, adjustedCurrentX);
                double y = Math.min(adjustedSecondY, adjustedCurrentY);
                double width = Math.abs(adjustedCurrentX - adjustedSecondX);
                double height = Math.abs(adjustedCurrentY - adjustedSecondY);

                g.drawRect((int) x, (int) y, (int) width, (int) height);
            }
        }

        if (this.mainFrame.getSelectedCuttingMethod() == "cutterL") {
            if (this.mainFrame.cncController.getCickedPointSequenceById(0) != null) {
                Point2D firstPoint = this.mainFrame.cncController.getCickedPointSequenceById(0);
                Point2D currentPoint = new Point2D.Double(hoverX, hoverY);

                double adjustedFirstX = (firstPoint.getX() / ConversionPiedMM.FACTEUR_CONVERSION) * this.gridSize - offsetX;
                double adjustedFirstY = ((varVert - firstPoint.getY()) / ConversionPiedMM.FACTEUR_CONVERSION) * this.gridSize - offsetY;

                double adjustedCurrentX = (currentPoint.getX() / ConversionPiedMM.FACTEUR_CONVERSION) * this.gridSize - offsetX;
                double adjustedCurrentY = ((varVert - currentPoint.getY()) / ConversionPiedMM.FACTEUR_CONVERSION) * this.gridSize - offsetY;

                double x = Math.min(adjustedFirstX, adjustedCurrentX);
                double y = Math.min(adjustedFirstY, adjustedCurrentY);
                double width = Math.abs(adjustedCurrentX - adjustedFirstX);
                double height = Math.abs(adjustedCurrentY - adjustedFirstY);

                g.setColor(Color.RED);
                g.drawRect((int) x, (int) y, (int) width, (int) height);
            }
        }



    }

    /*@Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int notches = e.getWheelRotation();
        int mouseX = e.getX();
        int mouseY = e.getY();

        int oldGridSize = gridSize;
        gridSize = notches < 0 ? Math.min(gridSize + 5, 100) : Math.max(gridSize - 5, 10);
        double scaleFactor = (double) gridSize / oldGridSize;

        // Adjust offsets to zoom centered on the mouse
        offsetX += (int) ((mouseX + offsetX) * (1 - scaleFactor));
        offsetY += (int) ((mouseY + offsetY) * (1 - scaleFactor));

        repaint();
    }*/

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
//        int notches = e.getWheelRotation(); // Get the direction of the scroll
//        int mouseX = e.getX(); // Get mouse X position
//        int mouseY = e.getY(); // Get mouse Y position
//
//        double oldGridSize = gridSize; // Store the current grid size
//        // Change grid size with limits
//        //gridSize = notches < 0 ? Math.min(gridSize + 5, 100) : Math.max(gridSize - 5, 10);
//
//        //rewrite the above line to allow for the grid to be zoomed in and out infinitely
//        gridSize = gridSize + (notches * -5);
//        //gridSize = Math.max(gridSize + (notches < 0 ? 5 : -5), 1);
//
//        // Calculate scale factor based on the change in grid size
//        double scaleFactor = (double) gridSize / oldGridSize;
//
//        // Adjust offsets to ensure zoom is centered around the mouse
//        offsetX = (int) ((mouseX + offsetX) * scaleFactor - mouseX);
//        offsetY = (int) ((mouseY + offsetY) * scaleFactor - mouseY);
//
//        repaint(); // Redraw the grid to reflect changes
        int notches = e.getWheelRotation(); // Direction du scroll
        int mouseX = e.getX(); // Position X de la souris
        int mouseY = e.getY(); // Position Y de la souris

        double oldGridSize = gridSize; // Taille actuelle de la grille
        gridSize += (notches * -5); // Zoom in/out


        double scaleFactor = gridSize / oldGridSize;

        // Ajustez les offsets pour centrer le zoom autour de la souris
        offsetX = (int) ((mouseX + offsetX) * scaleFactor - mouseX);
        offsetY = (int) ((mouseY + offsetY) * scaleFactor - mouseY);

        repaint();
    }



    //TODO: CONVERT THE POINT TO DOUBLE SOMEHOW? MAKE IT SO THAT THE ONLY PLACE
    public void setMagnifyGrid(boolean gridMag) {
        this.gridMagnified = gridMag;
    }


    //150 60, 30
    //500, 200

    //90 AND 45, 30, 0, 0
    //242 AND 251, 10, -212, -236

    //Point2D?
    public Point2D mouseClickCoordinatesHandler(Point2D e) {
        // Mouse coordinates
        double x = e.getX();
        double y = e.getY();

        PanelDTO panel = this.mainFrame.cncController.getPanelDTO();
        double panelScaleFactor = (double) gridSize / ConversionPiedMM.FACTEUR_CONVERSION;

        if (gridVisible && this.staticGridWidth != 0 && this.staticGridHeight != 0) {
            if (gridMagnified) {
                // Calculate normal coordinates relative to PanelDTO

                double adjustedX =  ((x + offsetX) / panelScaleFactor);
                double adjustedY =  ((y + offsetY) / panelScaleFactor);

                System.out.println("HERE " + adjustedX + ", " + adjustedY);

                // Calculate the spacing for equal division of lines
                double verticalSpacing = (staticGridWidth);
                double horizontalSpacing = (staticGridHeight);
                out.println("spacing" + horizontalSpacing + ", " + verticalSpacing);

                double Xcell = (adjustedX - panel.x) / verticalSpacing;
                double Ycell = (adjustedY - panel.y) / horizontalSpacing;

                int cellX = (int) Xcell;
                int cellY = (int) Ycell;

                out.println("cell " + cellX + ", " + cellY);

                //find out which line we are the closest to
                double xDiff = Math.abs(Xcell - cellX);
                double yDiff = Math.abs(Ycell - cellY);

                //if the difference is less than 0.5, we are closer to the line
                //if the difference is greater than 0.5, we are closer to the cell
                if (xDiff < 0.5) {
                    adjustedX =   (panel.x + (cellX * verticalSpacing));
                } else {
                    adjustedX = (panel.x + ((cellX + 1) * verticalSpacing));
                }

                if (yDiff < 0.5) {
                    adjustedY =  (panel.y + (cellY * horizontalSpacing));
                } else {
                    adjustedY =  (panel.y + ((cellY + 1) * horizontalSpacing));
                }

                return new Point2D.Double(adjustedX, mainFrame.cncController.adjustedHeight - adjustedY);
                //todo: idk, get rid of it?
            /*if (gridMagnified) {
                double adjustedX =  ((x + offsetX) / panelScaleFactor);
                double adjustedY =  ((y + offsetY) / panelScaleFactor);

                System.out.println("HERE " + adjustedX + ", " + adjustedY);

                double verticalSpacing = (panel.widthFactor / (staticGridWidth + 1));
                double horizontalSpacing = (panel.heightFactor / (staticGridHeight + 1));
                out.println("spacing" + horizontalSpacing + ", " + verticalSpacing);

                double Xcell = (adjustedX - panel.x) / verticalSpacing;
                double Ycell = (adjustedY - panel.y) / horizontalSpacing;

                double cellX =  Xcell;
                double cellY = Ycell;

                out.println("cell " + cellX + ", " + cellY);

                double xDiff = Math.abs(Xcell - cellX);
                double yDiff = Math.abs(Ycell - cellY);

                if (xDiff < 0.5) {
                    adjustedX =  (panel.x + (cellX * verticalSpacing));
                } else {
                    adjustedX =  (panel.x + ((cellX + 1) * verticalSpacing));
                }

                if (yDiff < 0.5) {
                    adjustedY = (panel.y + (cellY * horizontalSpacing));
                } else {
                    adjustedY = (panel.y + ((cellY + 1) * horizontalSpacing));
                }


                return new Point2D.Double(adjustedX, adjustedY);*/
            }
            double adjustedX =  ((x + offsetX) / panelScaleFactor);
            double adjustedY =  ((y + offsetY) / panelScaleFactor);

            return new Point2D.Double(adjustedX, mainFrame.cncController.adjustedHeight -  adjustedY);
        } else {
            double adjustedX =  ((x + offsetX) / panelScaleFactor);
            double adjustedY =  ((y + offsetY) / panelScaleFactor);
            System.out.println(adjustedY);
            return new Point2D.Double(adjustedX, mainFrame.cncController.adjustedHeight -  adjustedY);
        }
    }


    public void mouseClicked(MouseEvent e) {
        out.println("mouse clicked");
        Point2D processedClickCoordinates = this.mouseClickCoordinatesHandler(new Point2D.Double(e.getX(), e.getY()));
        out.println("HERE2 " + processedClickCoordinates.getX() + ", " + processedClickCoordinates.getY());
        if (processedClickCoordinates != null) {
            mainFrame.testClick(processedClickCoordinates.getX(), processedClickCoordinates.getY());
        }
    }

    public void mouseHover(MouseEvent e)
    {
        this.mainFrame.testHover((int) hoverX, (int) hoverY);
    }


    /*public void mouseDragged(MouseEvent e) {
        int x = ((e.getX() + offsetX) / gridSize) * gridSize;
        int y = ((e.getY() + offsetY) / gridSize) * gridSize;

        mainFrame.testDrag(x, y);
    }*/

    public void mouseDragged(MouseEvent e) {
        Point2D processedClickCoordinates = this.mouseClickCoordinatesHandler(new Point(e.getX(), e.getY()));
        if(processedClickCoordinates != null)
        {
            mainFrame.testDrag(processedClickCoordinates.getX(), processedClickCoordinates.getY());
        }
    }



    public void mousePressed(MouseEvent e) {
        Point2D processedClickCoordinates = this.mouseClickCoordinatesHandler(new Point2D.Double(e.getX(), e.getY()));
        if(processedClickCoordinates != null)
        {
            out.println("mouse pressed");
            mainFrame.testPress(processedClickCoordinates.getX(), processedClickCoordinates.getY());
        }
    }


    public void mouseReleased(MouseEvent e) {
        out.println("mouse released");
        Point2D processedClickCoordinates = this.mouseClickCoordinatesHandler(new Point2D.Double(e.getX(), e.getY()));
        if(processedClickCoordinates != null)
        {
            mainFrame.testRelease(processedClickCoordinates.getX(), processedClickCoordinates.getY());
        }
    }

    public int getStaticGridHeight() {
        return staticGridHeight;
    }

    public void setStaticGridHeight(int staticGridHeight) {
        this.staticGridHeight = staticGridHeight;
    }

    public int getStaticGridWidth() {
        return staticGridWidth;
    }

    public void setStaticGridWidth(int staticGridWidth) {
        this.staticGridWidth = staticGridWidth;
    }

    public void setStaticGridDimensions(int width)
    {
        this.staticGridHeight = width;
        this.staticGridWidth = width;
    }

    public void setPanelInTheMiddle(PanelDTO panel)
    {
        //this.gridSize = (int) (panel.widthFactor * this.gridSize);
        //this.offsetX = (this.getWidth() / -2) + ((int)(panel.widthFactor * this.gridSize) / 2);
        //this.offsetY = (this.getHeight() / -2) + ((int)(panel.heightFactor * this.gridSize) / 2);

    }
}
