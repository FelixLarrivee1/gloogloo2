package org.equipe39.drawing;

import java.awt.*;

import org.equipe39.domain.ConversionPiedMM;
import org.equipe39.dto.LDTO;

public class AfficheurCutL extends AfficheurCut {
    private LDTO lDTO;

    public AfficheurCutL(LDTO lDTO) {
        super(lDTO);
        this.lDTO = lDTO;
    }


    @Override
    public void draw(Graphics g, int currentGridSize) {

    }

    @Override
    public void draw(Graphics g, int currentGridSize, int offsetX, int offsetY, double varVert, double varHor) {
        Graphics2D g2d = (Graphics2D) g.create();

        double adjustedX1 = (this.lDTO.x / ConversionPiedMM.FACTEUR_CONVERSION) * currentGridSize - offsetX;
        double adjustedY1 = ((varVert - this.lDTO.y) / ConversionPiedMM.FACTEUR_CONVERSION) * currentGridSize - offsetY;
        double adjustedX2 = (this.lDTO.referencePoint.point.getX() / ConversionPiedMM.FACTEUR_CONVERSION) * currentGridSize - offsetX;
        double adjustedY2 = ((varVert - this.lDTO.referencePoint.point.getY()) / ConversionPiedMM.FACTEUR_CONVERSION) * currentGridSize - offsetY;

        // Couleur selon la sélection
        if (lDTO.isSelected) {
            g2d.setColor(Color.RED);
        } else {
            g2d.setColor(Color.BLUE);
        }

        // Récupération de l'épaisseur en mm depuis l'outil
        double thickness = 1.0;
        if (lDTO.tool != null) {
            thickness = this.lDTO.tool.epaisseur;
        }

        // Convertir l'épaisseur en pixels selon le zoom
        // thickness (mm) * (currentGridSize px/grille) / FACTEUR_CONVERSION (mm->grid unit)
        float pixelThickness = (float) (thickness * (currentGridSize / ConversionPiedMM.FACTEUR_CONVERSION));
        g2d.setStroke(new BasicStroke(pixelThickness));

        // Dessin des segments du L
        g2d.drawLine((int) adjustedX1, (int) adjustedY1, (int) adjustedX1, (int) adjustedY2);
        g2d.drawLine((int) adjustedX1, (int) adjustedY1, (int) adjustedX2, (int) adjustedY1);

        // Affichage des dimensions si sélectionné
        if (this.lDTO.isSelected) {
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.PLAIN, 12));

            int textXwidth = (int) ((adjustedX1 + adjustedX2) / 2);
            int textYwidth = (int) adjustedY1 - 5;

            int textXheight = (int) (adjustedX1 - 5);
            int textYheight = (int) ((adjustedY1 + adjustedY2) / 2);

            // Dessiner les dimensions
            g2d.drawString(Integer.toString(Math.abs((int) this.lDTO.widthFactor)) + " mm", textXwidth, textYwidth);
            g2d.drawString(Integer.toString(Math.abs((int) this.lDTO.heightFactor)) + " mm", textXheight + 7, textYheight);
        }

        g2d.dispose();
    }


    /*@Override
    public void draw(Graphics g, int currentGridSize, int offsetX, int offsetY) {
        // Convert the L-shape coordinates from "L" units to screen units
        double adjustedX1 = (this.lDTO.x / ConversionPiedMM.FACTEUR_CONVERSION) * currentGridSize - offsetX;
        double adjustedY1 = (this.lDTO.y / ConversionPiedMM.FACTEUR_CONVERSION) * currentGridSize - offsetY;

        double adjustedX2 = ((this.lDTO.x - this.lDTO.widthFactor) / ConversionPiedMM.FACTEUR_CONVERSION) * currentGridSize - offsetX;
        double adjustedY2 = ((this.lDTO.y - this.lDTO.heightFactor) / ConversionPiedMM.FACTEUR_CONVERSION) * currentGridSize - offsetY;

        // Determine the thickness of the line
        double thickness = (this.lDTO.tool != null) ? this.lDTO.tool.epaisseur : 1.0;
        double adjustedThickness = (thickness / ConversionPiedMM.FACTEUR_CONVERSION) * currentGridSize;
        double halfAdjustedThickness = adjustedThickness / 2.0;

        // Set the color based on selection and tool presence
        if (this.lDTO.tool != null) {
            g.setColor(this.lDTO.isSelected ? Color.RED : Color.BLACK);
        } else {
            g.setColor(this.lDTO.isSelected ? new Color(255, 0, 0, 100) : new Color(0, 0, 255, 100));
        }

        // Draw the vertical segment of the L
        double verticalRectX = adjustedX1 - halfAdjustedThickness;
        double verticalRectY = Math.min(adjustedY1, adjustedY2);
        double verticalRectWidth = adjustedThickness;
        double verticalRectHeight = Math.abs(adjustedY2 - adjustedY1);

        g.fillRect(
                (int) Math.round(verticalRectX),
                (int) Math.round(verticalRectY),
                (int) Math.round(verticalRectWidth),
                (int) Math.round(verticalRectHeight)
        );

        // Draw the horizontal segment of the L
        double horizontalRectX = Math.min(adjustedX1, adjustedX2);
        double horizontalRectY = adjustedY1 - halfAdjustedThickness;
        double horizontalRectWidth = Math.abs(adjustedX2 - adjustedX1);
        double horizontalRectHeight = adjustedThickness;

        g.fillRect(
                (int) Math.round(horizontalRectX),
                (int) Math.round(horizontalRectY),
                (int) Math.round(horizontalRectWidth),
                (int) Math.round(horizontalRectHeight)
        );
    }*/



    /*@Override
    public void draw(Graphics g, int currentGridSize, int offsetX, int offsetY) {
        double adjustedX1 = (this.lDTO.x / ConversionPiedMM.FACTEUR_CONVERSION) * currentGridSize - offsetX;
        double adjustedY1 = (this.lDTO.y / ConversionPiedMM.FACTEUR_CONVERSION) * currentGridSize - offsetY;
        double adjustedX2 = (this.lDTO.referencePoint.point.getX() / ConversionPiedMM.FACTEUR_CONVERSION) * currentGridSize - offsetX;
        double adjustedY2 = (this.lDTO.referencePoint.point.getY() / ConversionPiedMM.FACTEUR_CONVERSION) * currentGridSize - offsetY;

        //double adjustedX1 = (this.lDTO.x / ConversionPiedMM.FACTEUR_CONVERSION) * currentGridSize - offsetX;
        //double adjustedY2 = ((this.lDTO.heightFactor - this.lDTO.y) / ConversionPiedMM.FACTEUR_CONVERSION) * currentGridSize - offsetY;
        //double adjustedY1 = (this.lDTO.y / ConversionPiedMM.FACTEUR_CONVERSION) * currentGridSize - offsetY;
        //double adjustedX2 = ((this.lDTO.widthFactor - this.lDTO.x)/ ConversionPiedMM.FACTEUR_CONVERSION) * currentGridSize - offsetX;


        double thickness = this.lDTO.tool != null ? this.lDTO.tool.epaisseur : 1.0;
        double adjustedThickness = (thickness / ConversionPiedMM.FACTEUR_CONVERSION) * currentGridSize;
        double halfAdjustedThickness = adjustedThickness / 2.0;

        if (this.lDTO.tool != null) {
            if (this.lDTO.isSelected) {
                g.setColor(Color.RED);
            } else {
                g.setColor(Color.BLACK);
            }
        } else {
            if (this.lDTO.isSelected) {
                g.setColor(new Color(255, 0, 0, 100));
            } else {
                g.setColor(new Color(0, 0, 255, 100));
            }
        }

        double adjustedLeftX = adjustedX1 - halfAdjustedThickness;
        double verticalRectX = adjustedLeftX;
        double verticalRectY = Math.min(adjustedY1, adjustedY2);
        double verticalRectWidth = adjustedThickness;
        double verticalRectHeight = Math.abs(adjustedY2 - adjustedY1);

        g.fillRect(
                (int) Math.round(verticalRectX),
                (int) Math.round(verticalRectY),
                (int) Math.round(verticalRectWidth),
                (int) Math.round(verticalRectHeight)
        );

        double adjustedTopY = adjustedY1 - halfAdjustedThickness;
        double horizontalRectX = Math.min(adjustedX1, adjustedX2);
        double horizontalRectY = adjustedTopY;
        double horizontalRectWidth = Math.abs(adjustedX2 - adjustedX1);
        double horizontalRectHeight = adjustedThickness;

        g.fillRect(
                (int) Math.round(horizontalRectX),
                (int) Math.round(horizontalRectY),
                (int) Math.round(horizontalRectWidth),
                (int) Math.round(horizontalRectHeight)
        );
    }*/




}
