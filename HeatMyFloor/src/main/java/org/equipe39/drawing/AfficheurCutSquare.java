package org.equipe39.drawing;

import org.equipe39.domain.ConversionPiedMM;
import org.equipe39.dto.SquareDTO;

import java.awt.*;

public class AfficheurCutSquare extends AfficheurCut {
    private SquareDTO squareDTO;

    public AfficheurCutSquare(SquareDTO squareDTO) {
        super(squareDTO);
        this.squareDTO = squareDTO;
    }


    @Override
    public void draw(Graphics g, int currentGridSize) {

    }


    @Override
    public void draw(Graphics g, int currentGridSize, int offsetX, int offsetY, double varVert, double varHor) {
        Graphics2D g2d = (Graphics2D) g.create();

        double adjustedX = (this.squareDTO.x / ConversionPiedMM.FACTEUR_CONVERSION) * currentGridSize - offsetX;
        double adjustedY = ((varVert - this.squareDTO.y) / ConversionPiedMM.FACTEUR_CONVERSION) * currentGridSize - offsetY;
        double adjustedWidth = (this.squareDTO.widthFactor / ConversionPiedMM.FACTEUR_CONVERSION) * currentGridSize;
        double adjustedHeight = (this.squareDTO.heightFactor / ConversionPiedMM.FACTEUR_CONVERSION) * currentGridSize;

        double thickness = (this.squareDTO.tool != null) ? this.squareDTO.tool.epaisseur : 1.0;
        float pixelThickness = (float)(thickness * (currentGridSize / ConversionPiedMM.FACTEUR_CONVERSION));

        if (this.squareDTO.isSelected) {
            g2d.setColor(Color.RED);
        } else {
            g2d.setColor(Color.BLUE);
        }

        g2d.setStroke(new BasicStroke(pixelThickness));

        // Dessin du carré
        g2d.drawRect((int)adjustedX, ((int)adjustedY-(int)adjustedHeight), (int)adjustedWidth, (int)adjustedHeight);

        if (this.squareDTO.isSelected) {
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.PLAIN, 12));

            int textXwidth = (int) (adjustedX + adjustedWidth/2) - 15;
            int textYwidth = (int) adjustedY - 5;
            int textXheight = (int) (adjustedX + 7);
            int textYheight = (int) (adjustedY - adjustedHeight/2) + 2;

            g2d.drawString((int)this.squareDTO.widthFactor + " mm", textXwidth, textYwidth);
            g2d.drawString((int)this.squareDTO.heightFactor + " mm", textXheight, textYheight);
        }

        g2d.dispose();
    }
}
