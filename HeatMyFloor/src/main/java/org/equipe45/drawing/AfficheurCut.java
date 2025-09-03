package org.drawing;

import org.equipe45.dto.CutDTO;

import java.awt.Graphics;

public abstract class AfficheurCut implements ViewDrawer {
    protected CutDTO cutDTO;

    public AfficheurCut(CutDTO cutDTO) {
        this.cutDTO = cutDTO;
    }

    public abstract void draw(Graphics g, int currentGridSize);
}