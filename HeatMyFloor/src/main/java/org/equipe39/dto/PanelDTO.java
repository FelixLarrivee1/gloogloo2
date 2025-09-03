package org.equipe39.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.equipe39.domain.InteractiveEntity;
import org.equipe39.domain.Panel;

import java.util.UUID;

public class PanelDTO extends InteractiveEntityDTO{
    public double x, y;
    public double widthFactor;
    public double heightFactor;
    public double epaisseur;

    //public double initialGridSize;

    @JsonCreator
    public PanelDTO(@JsonProperty("id") UUID id,
                    @JsonProperty("isSelected") boolean isSelected,
                    @JsonProperty("x") double x,
                    @JsonProperty("y") double y,
                    @JsonProperty("widthFactor") double widthFactor,
                    @JsonProperty("heightFactor") double heightFactor,
                    @JsonProperty("epaisseur") double epaisseur) {
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
