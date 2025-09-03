package org.drawing;

import java.awt.Color;
import java.awt.Graphics;

import org.equipe45.domain.ConversionPiedMM;
import org.equipe45.dto.PanelDTO;
import org.equipe45.dto.RestrictedAreaDTO;

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
    public void draw(Graphics g, int currentGridSize, int offsetX, int offsetY) {
        //x, y, widthFactor, heightFactor, no initialGridSize
        double adjustedX = (this.restrictedAreaDTO.x / ConversionPiedMM.FACTEUR_CONVERSION) * currentGridSize - offsetX;
        double adjustedY = (this.restrictedAreaDTO.y / ConversionPiedMM.FACTEUR_CONVERSION) * currentGridSize - offsetY;
        double adjustedWidth = (this.restrictedAreaDTO.widthFactor / ConversionPiedMM.FACTEUR_CONVERSION) * currentGridSize;
        double adjustedHeight = (this.restrictedAreaDTO.heightFactor / ConversionPiedMM.FACTEUR_CONVERSION) * currentGridSize;

        g.setColor(Color.RED);
        g.fillRect((int)adjustedX, (int)adjustedY, (int)adjustedWidth, (int)adjustedHeight);

    }



}
