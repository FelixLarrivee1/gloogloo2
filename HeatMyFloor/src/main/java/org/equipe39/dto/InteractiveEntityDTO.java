package org.equipe39.dto;


import org.equipe39.domain.InteractiveEntity;

import java.util.UUID;



public abstract class InteractiveEntityDTO {
    public UUID id;
    public boolean isSelected;

    
    public InteractiveEntityDTO( UUID id,  boolean isSelected) {
        this.id = id;
        this.isSelected = isSelected;
    }

    public abstract InteractiveEntity toInteractiveEntity();

}