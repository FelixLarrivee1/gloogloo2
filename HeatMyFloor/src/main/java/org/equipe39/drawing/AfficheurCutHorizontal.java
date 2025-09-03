/*package org.equipe39.drawing;

import java.awt.*;

import org.equipe39.dto.VerticalLineDTO;

public class AfficheurCutHorizontal extends AfficheurCut {
    private HorizontalLineDTO verticalLineDTO;

    public AfficheurCutHorizontal(VerticalLineDTO verticalLineDTO) {
        super(verticalLineDTO);
        this.verticalLineDTO = verticalLineDTO;
    }

    @Override
    public void draw(Graphics g, int currentGridSize) {

        int adjustedX = (verticalLineDTO.x / verticalLineDTO.initialGridSize) * currentGridSize;

        if (verticalLineDTO.isSelected) {
            g.setColor(Color.RED);
        } else {
            g.setColor(verticalLineDTO.color);
        }

        g.drawLine(adjustedX, 99999, adjustedX, currentGridSize);

    }

    @Override
    public void draw(Graphics g, int currentGridSize, int offsetX, int offsetY) {
        // Adjust X position based on the zoom and horizontal offset
        int adjustedX = ((verticalLineDTO.x / verticalLineDTO.initialGridSize) * currentGridSize) - offsetX;

        // Set the color based on whether the vertical line is selected
        if (verticalLineDTO.isSelected) {
            g.setColor(Color.RED);
        } else {
            g.setColor(verticalLineDTO.color);
        }

        // Draw the vertical line
        g.drawLine(adjustedX, 0, adjustedX, 99999);
    }



}
*/


package org.equipe39.drawing;

import java.awt.*;

import org.equipe39.domain.ConversionPiedMM;
import org.equipe39.dto.HorizontalLineDTO;

public class AfficheurCutHorizontal extends AfficheurCut {
    private HorizontalLineDTO horizontalLineDTO;

    public AfficheurCutHorizontal(HorizontalLineDTO horizontalLineDTO) {
        super(horizontalLineDTO);
        this.horizontalLineDTO = horizontalLineDTO;
    }

    @Override
    public void draw(Graphics g, int currentGridSize) {
/*
        int adjustedY = (horizontalLineDTO.y / horizontalLineDTO.initialGridSize) * currentGridSize;

        if (horizontalLineDTO.isSelected) {
            g.setColor(Color.RED);
        } else {
            g.setColor(horizontalLineDTO.color);
        }

        g.drawLine(0, adjustedY, 99999, adjustedY);*/

    }

    @Override
    public void draw(Graphics g, int currentGridSize, int offsetX, int offsetY, double varVert, double varHor) {
        Graphics2D g2d = (Graphics2D) g.create();

        double adjustedY = (((varVert - horizontalLineDTO.y) / ConversionPiedMM.FACTEUR_CONVERSION) * currentGridSize) - offsetY;
        double adjustedX = (varHor * currentGridSize / ConversionPiedMM.FACTEUR_CONVERSION);

        // Épaisseur en mm
        double thickness = (horizontalLineDTO.tool != null) ? horizontalLineDTO.tool.epaisseur : 1.0;
        // Convertir l'épaisseur en pixels en fonction du zoom
        float pixelThickness = (float)(thickness * (currentGridSize / ConversionPiedMM.FACTEUR_CONVERSION));
        double halfPixelThickness = pixelThickness / 2.0;

        double adjustedTopY = adjustedY - halfPixelThickness;
        //adjustedTopY = varVert - adjustedTopY;
        double rectHeight = pixelThickness;

        if (horizontalLineDTO.tool != null) {
            if (horizontalLineDTO.isSelected) {
                g2d.setColor(Color.RED);
            } else {
                g2d.setColor(Color.BLUE);
            }
        } else {
            // Si pas d'outil, choisir une couleur par défaut
            if (horizontalLineDTO.isSelected) {
                g2d.setColor(new Color(255, 0, 0, 100));
            } else {
                g2d.setColor(new Color(0, 0, 255, 100));
            }
        }

        // Dessin du rectangle représentant la ligne horizontale
        g2d.fillRect(-offsetX, (int)Math.round(adjustedTopY), (int)Math.round(adjustedX), (int)Math.round(rectHeight));

        if (horizontalLineDTO.isSelected) {
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(1.0f));
            g2d.drawLine(-offsetX + (int) Math.round(adjustedX/2), -offsetY,
                    -offsetX + (int) Math.round(adjustedX/2), (int) Math.round(adjustedY));
            g2d.drawString((int)(varVert - this.horizontalLineDTO.y) + " mm", -offsetX + (int)Math.round(adjustedX/2), (int) adjustedTopY);
        }

        g2d.dispose();
    }
}