package org.equipe39.drawing;

import org.equipe39.dto.CutDTO;

import java.awt.*;

public abstract class AfficheurCut implements ViewDrawer {
    protected CutDTO cutDTO;

    public AfficheurCut(CutDTO cutDTO) {
        this.cutDTO = cutDTO;
    }

    public abstract void draw(Graphics g, int currentGridSize);

    public abstract void draw(Graphics g, int currentGridSize, int offsetX, int offsetY, double varVert, double varHor);
}
