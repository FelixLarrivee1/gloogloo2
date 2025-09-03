package org.drawing;

import java.awt.Color;
import java.awt.Graphics;

import org.equipe45.domain.ConversionPiedMM;
import org.equipe45.dto.PanelDTO;

public class AfficheurPanel implements ViewDrawer {
    private PanelDTO panelDTO;

    public AfficheurPanel(PanelDTO panelDTO) {
        super();
        this.panelDTO = panelDTO;
    }


    @Override
    public void draw(Graphics g, int currentGridSize) {

    }

    @Override
    public void draw(Graphics g, int currentGridSize, int offsetX, int offsetY) {
        //x, y, widthFactor, heightFactor, no initialGridSize
        double adjustedX = (panelDTO.x / ConversionPiedMM.FACTEUR_CONVERSION) * currentGridSize - offsetX;
        double adjustedY = (panelDTO.y / ConversionPiedMM.FACTEUR_CONVERSION) * currentGridSize - offsetY;
        double adjustedWidth = (panelDTO.widthFactor / ConversionPiedMM.FACTEUR_CONVERSION) * currentGridSize;
        double adjustedHeight = (panelDTO.heightFactor / ConversionPiedMM.FACTEUR_CONVERSION) * currentGridSize;

        g.setColor(Color.ORANGE);
        g.fillRect((int)adjustedX, (int)adjustedY, (int)adjustedWidth, (int)adjustedHeight);









    }



}