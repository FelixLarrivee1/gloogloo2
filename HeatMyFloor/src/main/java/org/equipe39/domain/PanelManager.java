package org.equipe39.domain;

//import java.util.List;

import org.equipe39.domain.Cut.Cut;

public class PanelManager {

    public Panel panel;
    public CNCController cncController;

    public PanelManager(CNCController cncController)
    {
        this.panel = new Panel(0, 0, ConversionPiedMM.convertirPiedEnMillimetres(8), ConversionPiedMM.convertirPiedEnMillimetres(4), 0.5);
        this.cncController = cncController;
    }




        

    //TODO: fix
    public void changePanelSize(Double adjustedWidth, Double adjustedHeight) {
        //cncController.adjustedWidth = ConversionPiedMM.convertirEnMillimetres(width);
        //cncController.adjustedHeight = ConversionPiedMM.convertirEnMillimetres(height);
        //double adjustedWidth = cncController.adjustedWidth;
        //double adjustedHeight = cncController.adjustedHeight;
        //double adjustedWidth=ConversionPiedMM.convertirEnMillimetres(width);
        //double adjustedHeight= ConversionPiedMM.convertirEnMillimetres(height);
        System.out.println("im here" + (adjustedWidth > 0 && adjustedHeight > 0));
        //if (width > 0 && height > 0) {
        if (adjustedWidth > 0 && adjustedHeight > 0 && ConversionPiedMM.MillimetreEnPied(adjustedWidth) <= 10 && ConversionPiedMM.MillimetreEnPied(adjustedHeight) <= 5) {
            //double adjustedWidth = ConversionPiedMM.convertirPiedEnMillimetres(width);
            //double adjustedHeight = ConversionPiedMM.convertirPiedEnMillimetres(height);
            System.out.println("im here");

            //truncate the decimals to 3
            //adjustedWidth = Math.round(adjustedWidth * 1000.0) / 1000.0;
            //adjustedHeight = Math.round(adjustedHeight * 1000.0) / 1000.0;
            this.panel.changeSize(adjustedWidth, adjustedHeight);


            for(Cut cut :  this.cncController.cutManager.getCuts())
            {
                if(!this.cncController.cutManager.isCutInsidePanel(cut))
                {
                    System.out.println("jsdlkajdlkjsalkdjsaldkjsalkdj");
                    this.cncController.cutManager.deleteCut(cut);
                }
            }

            this.cncController.pointReferenceManager.getReferencePoints();
            this.cncController.updateHistoryAfterClickRelease();
            this.cncController.pointReferenceManager.getReferencePoints();

                    // TODO: REWORK
                    /*for (Cut cut : actionHistory.getCuts()) {
                        if (cut instanceof VerticalLine) {
                            VerticalLine verticalLine = (VerticalLine) cut;
                            // If the line's X-coordinate is outside the panel, move it back inside
                            if (verticalLine.getX() < panel.getX()) {
                                verticalLine.setX((int) panel.getX());
                            } else if (verticalLine.getX() > panel.getX() + adjustedWidth) {
                                verticalLine.setX((int) (panel.getX() + adjustedWidth));
                            }
                        } else if (cut instanceof HorizontalLine) {
                            HorizontalLine horizontalLine = (HorizontalLine) cut;
                            // If the line's Y-coordinate is outside the panel, move it back inside
                            if (horizontalLine.getY() < panel.getY()) {
                                horizontalLine.setY((int) panel.getY());
                            } else if (horizontalLine.getY() > panel.getY() + adjustedHeight) {
                                horizontalLine.setY((int) (panel.getY() + adjustedHeight));
                            }
                        } else if (cut instanceof RestrictedArea) {
                            RestrictedArea restrictedArea = (RestrictedArea) cut;
                            // Adjust the position and size of the restricted area to fit within bounds
                            if (restrictedArea.getX() < panel.getX()) {
                                restrictedArea.setX((int) panel.getX());
                            }
                            if (restrictedArea.getY() < panel.getY()) {
                                restrictedArea.setY((int) panel.getY());
                            }
                            if (restrictedArea.getX() + restrictedArea.getWidthFactor() > panel.getX() + adjustedWidth) {
                                restrictedArea.setWidthFactor(adjustedWidth - (restrictedArea.getX() - panel.getX()));
                            }
                            if (restrictedArea.getY() + restrictedArea.getHeightFactor() > panel.getY() + adjustedHeight) {
                                restrictedArea.setHeightFactor(adjustedHeight - (restrictedArea.getY() - panel.getY()));
                            }
                        } else if (cut instanceof Square) {
                            Square square = (Square) cut;
                            // Adjust the position and size of the square to fit within bounds
                            if (square.getX() < panel.getX()) {
                                square.setX((int) panel.getX());
                            }
                            if (square.getY() < panel.getY()) {
                                square.setY((int) panel.getY());
                            }
                            if (square.getX() + square.getWidthFactor() > panel.getX() + adjustedWidth) {
                                square.setWidthFactor(adjustedWidth - (square.getX() - panel.getX()));
                            }
                            if (square.getY() + square.getHeightFactor() > panel.getY() + adjustedHeight) {
                                square.setHeightFactor(adjustedHeight - (square.getY() - panel.getY()));
                            }
                        } else if (cut instanceof L) {
                            L lCut = (L) cut;
                            // Adjust the L cut's position to fit within bounds
                            if (lCut.getX() < panel.getX()) {
                                lCut.setX((int) panel.getX());
                            }
                            if (lCut.getY() < panel.getY()) {
                                lCut.setY((int) panel.getY());
                            }
                            if (lCut.getX() > panel.getX() + adjustedWidth) {
                                lCut.setX((int) (panel.getX() + adjustedWidth));
                            }
                            if (lCut.getY() > panel.getY() + adjustedHeight) {
                                lCut.setY((int) (panel.getY() + adjustedHeight));
                            }
                        }
                    }*/

                            System.out.println("Panel size updated and cuts adjusted to fit within bounds.");
        }
    }



    public void changeEpaisseurPanel(String epaisseur) {
        double adjustedEppaiseur =  ConversionPiedMM.convertirEnMillimetres(epaisseur);
        this.panel.setEpaisseur(adjustedEppaiseur);
        //update the outil list to set their max epaisseur to the new panel epaisseur if it exceeds it
        for (Outil outil: this.cncController.getOutils()) {
            if(outil.getEpaisseur() > adjustedEppaiseur)
            {
                outil.setEpaisseur(adjustedEppaiseur);
            }
        }
        //todo: reajust every single cut that exceed it
    }


    public boolean isClickInsidePanel(double x, double y) {

        if (x >= panel.getX() && x <= panel.getX() + panel.getWidthFactor() && y >= panel.getY() && y <= panel.getY() + panel.getHeightFactor()) {
            return true;
        }
        return false;
    }
}
