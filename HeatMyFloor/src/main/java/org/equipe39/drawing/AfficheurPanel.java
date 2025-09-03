package org.equipe39.drawing;

import org.equipe39.domain.ConversionPiedMM;
import org.equipe39.dto.PanelDTO;

import java.awt.*;

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
    public void draw(Graphics g, int currentGridSize, int offsetX, int offsetY, double varVert, double varHor) {
        //x, y, widthFactor, heightFactor, no initialGridSize
        double adjustedX = (panelDTO.x / ConversionPiedMM.FACTEUR_CONVERSION) * currentGridSize - offsetX;
        double adjustedY = (panelDTO.y / ConversionPiedMM.FACTEUR_CONVERSION) * currentGridSize - offsetY;
        double adjustedWidth = (panelDTO.widthFactor / ConversionPiedMM.FACTEUR_CONVERSION) * currentGridSize;
        double adjustedHeight = (panelDTO.heightFactor / ConversionPiedMM.FACTEUR_CONVERSION) * currentGridSize;

        g.setColor(Color.decode("#ebb980"));
        g.fillRect((int)adjustedX, (int)adjustedY, (int)adjustedWidth, (int)adjustedHeight);

    }



}
