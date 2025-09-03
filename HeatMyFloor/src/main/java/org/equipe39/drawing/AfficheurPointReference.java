package org.equipe39.drawing;

import org.equipe39.domain.ConversionPiedMM;
import org.equipe39.dto.PointReferenceDTO;

import java.awt.*;

public class AfficheurPointReference implements ViewDrawer {
    private PointReferenceDTO pointReferenceDTO;
    private static final int HITBOX_SIZE = 10;

    public AfficheurPointReference(PointReferenceDTO pointReferenceDTO) {
        super();
        this.pointReferenceDTO = pointReferenceDTO;
    }


    @Override
    public void draw(Graphics g, int currentGridSize) {

    }

    @Override
    public void draw(Graphics g, int currentGridSize, int offsetX, int offsetY, double varVert, double varHor) {
        Graphics2D g2d = (Graphics2D) g.create();
        double adjustedX = (pointReferenceDTO.point.getX() / ConversionPiedMM.FACTEUR_CONVERSION) * currentGridSize - offsetX;
        double adjustedY = ((varVert - pointReferenceDTO.point.getY()) / ConversionPiedMM.FACTEUR_CONVERSION) * currentGridSize - offsetY;
        double adjustedWidth = (HITBOX_SIZE / ConversionPiedMM.FACTEUR_CONVERSION) * currentGridSize;
        double adjustedHeight = (HITBOX_SIZE / ConversionPiedMM.FACTEUR_CONVERSION) * currentGridSize;

        // Adjust the X and Y to center the hitbox around the point
        adjustedX -= adjustedWidth / 2;
        adjustedY -= adjustedHeight / 2;

        if(this.pointReferenceDTO.isSelected)
        {
            g.setColor(Color.RED);
        }
        else
        {
            g.setColor(Color.BLACK);
        }

        g.fillRect((int) Math.round(adjustedX), (int) Math.round(adjustedY), (int) Math.round(adjustedWidth), (int) Math.round(adjustedHeight));

        if (this.pointReferenceDTO.isSelected) {
            g2d.setColor(Color.BLACK); // Couleur du texte
            g2d.setFont(new Font("Arial", Font.PLAIN, 12));
            g2d.drawString("( " +Integer.toString((int) this.pointReferenceDTO.point.getX())+ "mm ; " + Integer.toString((int) this.pointReferenceDTO.point.getY()) + "mm )", (int) Math.round(adjustedX) + 10, (int) Math.round(adjustedY));
        }
    }

}