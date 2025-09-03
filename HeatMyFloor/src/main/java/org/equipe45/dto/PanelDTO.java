package org.dto;

import org.equipe45.domain.Panel;

public class PanelDTO {
    public int x;
    public int y;
    public double widthFactor;
    public double heightFactor;

    //public double initialGridSize;

    public PanelDTO(int x, int y, double widthFactor, double heightFactor) {
        this.x = x;
        this.y = y;
        this.widthFactor = widthFactor;
        this.heightFactor = heightFactor;
    }

    public Panel toPanel()
    {
        return new Panel(x, y, widthFactor, heightFactor);
    }
}