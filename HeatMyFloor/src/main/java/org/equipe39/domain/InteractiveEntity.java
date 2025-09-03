package org.equipe39.domain;

import org.equipe39.dto.InteractiveEntityDTO;

import java.util.UUID;

public abstract class InteractiveEntity
{
    public UUID id;
    public boolean isSelected;

    /*public InteractiveEntity(UUID id, boolean isSelected)
    {
        this.id = id;
        this.isSelected = isSelected;
    }*/

    public UUID getId()
    {
        return id;
    }

    public void setId(UUID id)
    {
        this.id = id;
    }

    public boolean isSelected()
    {
        return isSelected;
    }

    public void setSelected(boolean selected)
    {
        isSelected = selected;
    }

    //to dto
    public abstract InteractiveEntityDTO toDTO();

    public abstract boolean isClicked(double mouseX, double mouseY);

    public void toggleSelection() {
        System.out.println("toggled!");
        isSelected = !isSelected;
    }


    //clone
    public abstract InteractiveEntity clone();


    



}
