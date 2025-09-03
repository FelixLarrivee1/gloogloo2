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


package org.drawing;

import org.equipe45.domain.ConversionPiedMM;
import org.equipe45.dto.HorizontalLineDTO;

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
    public void draw(Graphics g, int currentGridSize, int offsetX, int offsetY) {
        double adjustedY = ((horizontalLineDTO.y / ConversionPiedMM.FACTEUR_CONVERSION) * currentGridSize) - offsetY;
        // Set the color based on whether the horizontal line is selected
        if (horizontalLineDTO.isSelected) {
            g.setColor(Color.RED);
        } else {
            g.setColor(horizontalLineDTO.color);
        }

        // Draw the horizontal line
        g.drawLine(0, (int) adjustedY, 99999, (int) adjustedY);
    }
}