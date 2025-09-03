package org.equipe39.drawing;

import java.awt.Color;
import java.awt.Graphics;

import org.equipe39.domain.ConversionPiedMM;
import org.equipe39.dto.*;

public class AfficheurCutLine implements ViewDrawer {
    private CutLineDTO cutLineDTO;

    public AfficheurCutLine(CutLineDTO cutLineDTO) {
        super();
        this.cutLineDTO = cutLineDTO;
    }


    @Override
    public void draw(Graphics g, int currentGridSize) {

    }

    @Override
    public void draw(Graphics g, int currentGridSize, int offsetX, int offsetY, double varVert, double varHor) {

    }

    /*@Override
    public void draw(Graphics g, int currentGridSize, int offsetX, int offsetY) {

    }*/

    public void draw(Graphics g, int currentGridSize, int offsetX, int offsetY, CutDTO cutDTO, OutilDTO tool) {
    // Get the start and end points of the cut line
    double startX = cutLineDTO.start.getX();
    double startY = cutLineDTO.start.getY();
    double endX = cutLineDTO.end.getX();
    double endY = cutLineDTO.end.getY();

    // Adjust the start and end points based on the grid size and offset
    double adjustedStartX = (startX / ConversionPiedMM.FACTEUR_CONVERSION) * currentGridSize - offsetX;
    double adjustedStartY = (startY / ConversionPiedMM.FACTEUR_CONVERSION) * currentGridSize - offsetY;
    double adjustedEndX = (endX / ConversionPiedMM.FACTEUR_CONVERSION) * currentGridSize - offsetX;
    double adjustedEndY = (endY / ConversionPiedMM.FACTEUR_CONVERSION) * currentGridSize - offsetY;

    // Get the thickness of the line
    double thickness = tool.epaisseur;
    double halfThickness = thickness / 2;

    // For horizontal lines, we adjust Y; for vertical lines, we adjust X.
    double rectStart, rectEnd, rectHeight, rectWidth;
    if (startX == endX) { // Vertical line
        rectStart = adjustedStartY - halfThickness;
        rectEnd = adjustedEndY + halfThickness;
        rectWidth = thickness;
        rectHeight = adjustedEndY - adjustedStartY;
    } else { // Horizontal line
        rectStart = adjustedStartX - halfThickness;
        rectEnd = adjustedEndX + halfThickness;
        rectWidth = adjustedEndX - adjustedStartX;
        rectHeight = thickness;
    }

    // Set the color based on selection
    if (tool != null) {
        if (cutDTO.isSelected) {
            g.setColor(Color.RED);
        } else {
            g.setColor(Color.BLUE);
        }
    } else {
        if (cutDTO.isSelected) {
            g.setColor(new Color(255, 0, 0, 100));  // Semi-transparent red for selection
        } else {
            g.setColor(new Color(0, 0, 255, 100));  // Semi-transparent blue for non-selection
        }
    }

    // Draw the rectangle (cut line with thickness)
    g.fillRect((int) Math.round(rectStart), (int) Math.round(rectStart), (int) Math.round(rectWidth), (int) Math.round(rectHeight));
}

}
