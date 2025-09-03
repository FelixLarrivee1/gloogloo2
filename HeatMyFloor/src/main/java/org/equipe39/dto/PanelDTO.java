package org.equipe39.dto;


import org.equipe39.domain.InteractiveEntity;
import org.equipe39.domain.Panel;

import java.util.UUID;

public class PanelDTO extends InteractiveEntityDTO{
    public double x, y;
    public double widthFactor;
    public double heightFactor;
    public double epaisseur;

    //public double initialGridSize;

    
    public PanelDTO( UUID id,
                     boolean isSelected,
                     double x,
                     double y,
                     double widthFactor,
                    double heightFactor,
                     double epaisseur) {
        super(id, isSelected);
        this.x = x;
        this.y = y;
        this.widthFactor = widthFactor;
        this.heightFactor = heightFactor;
        this.epaisseur = epaisseur;
    }

    public Panel toPanel()
    {
        return new Panel(id, isSelected, x, y, widthFactor, heightFactor, epaisseur);
    }

    @Override
    public InteractiveEntity toInteractiveEntity() {
        return toPanel();
    }
}
