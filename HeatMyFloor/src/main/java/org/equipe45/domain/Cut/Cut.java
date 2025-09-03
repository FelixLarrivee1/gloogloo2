package org.domain.Cut;




import org.domain.CutLine;
import org.equipe45.domain.InteractiveEntity;
import org.equipe45.dto.CutDTO;

import java.util.UUID;
import java.util.List;

public abstract class Cut extends InteractiveEntity {
    //public UUID id;
    //boolean isSelected = false;


    /*public void setSelection(boolean isSelected) {
        this.isSelected = isSelected;
    }*/



    public boolean isSelected() {
        return isSelected;
    }

    /*public void moveTo(int newX, int newY) {
        //this.x = newX;
        //this.y = newY;
    }*/

    //public abstract boolean isClicked(int mouseX, int mouseY);

    /*public void shapeModification(int x, int y)
    {

    }*/

    /*public void snapToGrid(int gridSize) {
        //this.x = (this.x / gridSize) * gridSize;
        //this.y = (this.y / gridSize) * gridSize;
    }*/

    /*public void draw(Graphics g, int currentGridSize) {
    }

    public void draw(Graphics g, int currentGridSize, int panelWidth) {
    }*/

    //public abstract CutDTO toDTO();


    public abstract List<CutLine> getCutLines();

}