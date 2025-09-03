package org.equipe39.domain.Cut;




import org.equipe39.domain.CutLine;
import org.equipe39.domain.InteractiveEntity;

import java.util.List;

public abstract class Cut extends InteractiveEntity {
    private List<CutLine> cutLines;
    //public UUID id;
    //boolean isSelected = false;


    /*public void setSelection(boolean isSelected) {
        this.isSelected = isSelected;
    }*/


   //equals
   @Override
    public boolean equals(Object o) {
         if (this == o) return true;
         if (o == null || getClass() != o.getClass()) return false;
    
         Cut cut = (Cut) o;
    
         return id.equals(cut.id);
    }




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

    public abstract boolean isTouching(Cut cut);

    public abstract Cut clone();
}
