package org.equipe39.drawing;

import java.awt.*;

import org.equipe39.domain.ConversionPiedMM;
import org.equipe39.dto.BordureDTO;

public class AfficheurCutBordure extends AfficheurCut {
    private BordureDTO bordureDTO;

    public AfficheurCutBordure(BordureDTO bordureDTO) {
        super(bordureDTO);
        this.bordureDTO = bordureDTO;
    }


    @Override
    public void draw(Graphics g, int currentGridSize) {

    }


    @Override
    public void draw(Graphics g, int currentGridSize, int offsetX, int offsetY, double varVert, double varHor) {
        Graphics2D g2d = (Graphics2D) g.create();

        try {
            double adjustedX = (this.bordureDTO.x / ConversionPiedMM.FACTEUR_CONVERSION) * currentGridSize - offsetX;
            double adjustedY = (this.bordureDTO.y / ConversionPiedMM.FACTEUR_CONVERSION) * currentGridSize - offsetY;
            double adjustedWidth = (this.bordureDTO.widthFactor / ConversionPiedMM.FACTEUR_CONVERSION) * currentGridSize;
            double adjustedHeight = (this.bordureDTO.heightFactor / ConversionPiedMM.FACTEUR_CONVERSION) * currentGridSize;

            double thickness = (this.bordureDTO.tool != null) ? this.bordureDTO.tool.epaisseur : 1.0;
            float pixelThickness = (float)(thickness * (currentGridSize / ConversionPiedMM.FACTEUR_CONVERSION));

            if (this.bordureDTO.tool != null) {
                if (this.bordureDTO.isSelected) {
                    g2d.setColor(Color.RED);
                } else {
                    g2d.setColor(Color.BLACK);
                }
            } else {
                if (this.bordureDTO.isSelected) {
                    g2d.setColor(Color.PINK);
                } else {
                    g2d.setColor(Color.RED);
                }
            }

            g2d.setStroke(new BasicStroke(pixelThickness));

            // Dessiner la bordure
            g2d.drawRect((int)Math.round(adjustedX), (int)Math.round(adjustedY),
                    (int)Math.round(adjustedWidth), (int)Math.round(adjustedHeight));

            if (this.bordureDTO.isSelected) {
                g2d.setColor(Color.BLACK);
                g2d.setFont(new Font("Arial", Font.PLAIN, 12));

                int textXwidth = (int) (adjustedX + adjustedWidth/2) - 15;
                int textYwidth = (int) adjustedY - 5;
                int textXheight = (int) (adjustedX + 7);
                int textYheight = (int) (adjustedY + adjustedHeight/2)+2;

                g2d.drawString((int)this.bordureDTO.widthFactor + " mm", textXwidth, textYwidth);
                g2d.drawString((int)this.bordureDTO.heightFactor + " mm", textXheight, textYheight);
            }
        } finally {
            g2d.dispose();
        }
    }
}
