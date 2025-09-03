package org.equipe39.dto;


import org.equipe39.domain.Cut.Cut;
import org.equipe39.domain.InteractiveEntity;

import java.util.UUID;

public abstract class CutDTO extends InteractiveEntityDTO {
    
    //public UUID id;
    //public boolean isSelected;


    //
    public CutDTO( UUID id, boolean isSelected) {
        super(id, isSelected);
    }



    public abstract Cut toCut();

    @Override
    public InteractiveEntity toInteractiveEntity() {
        return toCut();
    }
}





