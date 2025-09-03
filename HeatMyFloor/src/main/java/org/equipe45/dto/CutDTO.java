package org.dto;

import org.equipe45.domain.Cut.Cut;
import org.equipe45.domain.InteractiveEntity;

import java.util.UUID;

public abstract class CutDTO extends InteractiveEntityDTO {

    //public UUID id;
    //public boolean isSelected;



    public CutDTO(UUID id, boolean isSelected) {
        super(id, isSelected);
    }



    public abstract Cut toCut();

    @Override
    public InteractiveEntity toInteractiveEntity() {
        return toCut();
    }
}





/*
package org.equipe39.dto;

import org.equipe39.domain.Cut.Cut;

import java.util.UUID;

public abstract class CutDTO extends InteractiveEntityDTO {
    public UUID id;
    public boolean isSelected;

    public CutDTO(UUID id, boolean isSelected) {
        this.id = id;
        this.isSelected = isSelected;
    }

    public abstract Cut toCut();

}
 */