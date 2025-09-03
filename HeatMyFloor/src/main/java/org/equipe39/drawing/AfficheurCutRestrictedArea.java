package org.equipe39.drawing;

import java.awt.*;

import org.equipe39.domain.ConversionPiedMM;
import org.equipe39.dto.RestrictedAreaDTO;

public class AfficheurCutRestrictedArea extends AfficheurCut {
    private RestrictedAreaDTO restrictedAreaDTO;

    public AfficheurCutRestrictedArea(RestrictedAreaDTO restrictedAreaDTO) {
        super(restrictedAreaDTO);
        this.restrictedAreaDTO = restrictedAreaDTO;
    }


    @Override
    public void draw(Graphics g, int currentGridSize) {

    }

    @Override
    public void draw(Graphics g, int currentGridSize, int offsetX, int offsetY, double varVert, double varHor) {
        //x, y, widthFactor, heightFactor, no initialGridSize
        Graphics2D g2d = (Graphics2D) g.create();
        double adjustedX = (this.restrictedAreaDTO.x / ConversionPiedMM.FACTEUR_CONVERSION) * currentGridSize - offsetX;
        double adjustedY = ((varVert - this.restrictedAreaDTO.y) / ConversionPiedMM.FACTEUR_CONVERSION) * currentGridSize - offsetY;
        double adjustedWidth = (this.restrictedAreaDTO.widthFactor / ConversionPiedMM.FACTEUR_CONVERSION) * currentGridSize;
        //double adjustedHeight = (this.restrictedAreaDTO.heightFactor / ConversionPiedMM.FACTEUR_CONVERSION) * currentGridSize;
        double adjustedHeight = ((this.restrictedAreaDTO.heightFactor / ConversionPiedMM.FACTEUR_CONVERSION) * currentGridSize);

        if(this.restrictedAreaDTO.isSelected)
        {
            g.setColor(new Color(0,0,255,100));

        }
        else
        {
            g.setColor(new Color(255,0,0,100));
        }
        g.fillRect((int)Math.round(adjustedX), (int)Math.round(adjustedY - adjustedHeight), (int)Math.round(adjustedWidth), (int)Math.round(adjustedHeight));

        if (this.restrictedAreaDTO.isSelected){
            g2d.setColor(Color.BLACK); // Couleur du texte
            g2d.setFont(new Font("Arial", Font.PLAIN, 12)); // Police du texte

            // Calculer la position du texte (par exemple, au-dessus du coin supérieur gauche)
            int textXwidth = (int) (adjustedX + adjustedWidth/2) - 15;
            int textYwidth = (int) adjustedY -10;

            int textXheight = (int) (adjustedX + 7);
            int textYheight = (int) (adjustedY - adjustedHeight/2)+2;

            // Dessiner un fond semi-transparent pour améliorer la lisibilité
            FontMetrics metrics = g2d.getFontMetrics();


            // Dessiner le texte des dimensions
            g2d.setColor(Color.BLACK); // Couleur du texte
            g2d.drawString(Integer.toString( (int) this.restrictedAreaDTO.widthFactor) + " mm", textXwidth, textYwidth);
            g2d.drawString(Integer.toString( (int) this.restrictedAreaDTO.heightFactor)+ " mm", textXheight, textYheight);
        }

    }



}
