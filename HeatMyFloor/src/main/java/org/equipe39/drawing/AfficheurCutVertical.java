package org.equipe39.drawing;

import org.equipe39.domain.ConversionPiedMM;
import org.equipe39.dto.VerticalLineDTO;

import java.awt.*;

public class AfficheurCutVertical extends AfficheurCut {
    private VerticalLineDTO verticalLineDTO;

    public AfficheurCutVertical(VerticalLineDTO verticalLineDTO) {
        super(verticalLineDTO);
        this.verticalLineDTO = verticalLineDTO;
    }


    @Override
    public void draw(Graphics g, int currentGridSize) {

        //int adjustedX = (verticalLineDTO.x / verticalLineDTO.initialGridSize) * currentGridSize;
        double adjustedX = (verticalLineDTO.x / ConversionPiedMM.FACTEUR_CONVERSION) * currentGridSize;

        if (verticalLineDTO.isSelected) {
            g.setColor(Color.RED);
        } else {
            g.setColor(verticalLineDTO.color);
        }

        g.drawLine((int) adjustedX, 99999, (int) adjustedX, currentGridSize);

    }

    @Override
    public void draw(Graphics g, int currentGridSize, int offsetX, int offsetY, double varVert, double varHor) {
        Graphics2D g2d = (Graphics2D) g.create();

        double adjustedX = ((verticalLineDTO.x / ConversionPiedMM.FACTEUR_CONVERSION) * currentGridSize) - offsetX;
        double adjustedY = (varVert * currentGridSize / ConversionPiedMM.FACTEUR_CONVERSION);

        double thickness = (this.verticalLineDTO.tool != null) ? this.verticalLineDTO.tool.epaisseur : 1.0;
        float pixelThickness = (float)(thickness * (currentGridSize / ConversionPiedMM.FACTEUR_CONVERSION));
        double halfPixelThickness = pixelThickness / 2.0;

        double adjustedLeftX = adjustedX - halfPixelThickness;
        double rectWidth = pixelThickness;

        if (verticalLineDTO.tool != null) {
            if (verticalLineDTO.isSelected) {
                g2d.setColor(Color.RED);
            } else {
                g2d.setColor(Color.BLUE);
            }
        } else {
            if (verticalLineDTO.isSelected) {
                g2d.setColor(new Color(255, 0, 0, 100));
            } else {
                g2d.setColor(new Color(0, 0, 255, 100));
            }
        }

        // Dessin du rectangle représentant la ligne verticale
        g2d.fillRect((int)Math.round(adjustedLeftX), -offsetY, (int)Math.round(rectWidth), (int)(adjustedY - 1));

        if (verticalLineDTO.isSelected) {
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(1.0f));
            g2d.drawLine(-offsetX, -offsetY + (int)(adjustedY/2), (int)Math.round(adjustedLeftX), -offsetY + (int)(adjustedY/2));
            g2d.drawString((int)this.verticalLineDTO.x + " mm", (int)Math.round(adjustedLeftX/2), -offsetY + (int)(adjustedY/2));
        }

        g2d.dispose();
    }
}
