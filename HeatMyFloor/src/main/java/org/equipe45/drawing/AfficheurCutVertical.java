package org.drawing;

import org.equipe45.domain.ConversionPiedMM;
import org.equipe45.dto.VerticalLineDTO;

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

        g.drawLine((int) adjustedX, 99999, (int)adjustedX, currentGridSize);

    }

    @Override
    public void draw(Graphics g, int currentGridSize, int offsetX, int offsetY) {

        // Adjust X position based on the zoom and horizontal offset
        double adjustedX = ((verticalLineDTO.x / ConversionPiedMM.FACTEUR_CONVERSION) * currentGridSize) - offsetX;

        // Set the color based on whether the vertical line is selected
        if (verticalLineDTO.isSelected) {
            g.setColor(Color.RED);
        } else {
            g.setColor(verticalLineDTO.color);
        }

        // Draw the vertical line
        g.drawLine((int)adjustedX, 0, (int)adjustedX, 99999);
    }
}