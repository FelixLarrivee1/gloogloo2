package org.drawing;

import org.equipe45.domain.CNCController;
import org.equipe45.dto.CutDTO;
import org.equipe45.dto.InteractiveEntityDTO;
import org.equipe45.dto.PointReferenceDTO;

import java.awt.Graphics;

public class Afficheur {

    private CNCController cncController;
    private ViewDrawer viewDrawer;


    public Afficheur(CNCController cncController) {
        this.cncController = cncController;
        //this.viewDrawer = viewDrawer;
    }

    //public abstract void draw(Graphics g);

    /*public void draw(Graphics g, int currentGridSize)
    {
        AfficheurPanel afficheurPanel = new AfficheurPanel(this.cncController.getPanelDTO());
        afficheurPanel.draw(g, currentGridSize);
        //for (CutDTO cut : this.cncController.getHistoryInDTO()) {
        for (InteractiveEntityDTO cutDTO : this.cncController.getHistoryInDTO()) {
            AfficheurCut afficheurCut = AfficheurCutFactory.createAfficheurCut(cut);
            if (afficheurCut != null) {
                afficheurCut.draw(g, currentGridSize);
            }
        }
    }*/

    public void draw(Graphics g, int currentGridSize, int offsetX, int offsetY)
    {
        AfficheurPanel afficheurPanel = new AfficheurPanel(this.cncController.getPanelDTO());
        afficheurPanel.draw(g, currentGridSize, offsetX, offsetY);
        //for (CutDTO cut : this.cncController.getHistoryInDTO()) {
        for (InteractiveEntityDTO cut : this.cncController.getHistoryInDTO()) {
            if (cut instanceof CutDTO) {
                CutDTO cutDTO = (CutDTO) cut;
                AfficheurCut afficheurCut = AfficheurCutFactory.createAfficheurCut(cutDTO);
                if (afficheurCut != null) {
                    afficheurCut.draw(g, currentGridSize, offsetX, offsetY);
                }
            }
            else if (cut instanceof PointReferenceDTO) {
                //System.out.println("ADDED POINT");
                PointReferenceDTO pointReferenceDTO = (PointReferenceDTO) cut;
                AfficheurPointReference afficheurPointReference = new AfficheurPointReference(pointReferenceDTO);
                afficheurPointReference.draw(g, currentGridSize, offsetX, offsetY);
            }

            //AfficheurCut afficheurCut = AfficheurCutFactory.createAfficheurCut(cut);
            //if (afficheurCut != null) {
            //    afficheurCut.draw(g, currentGridSize, offsetX, offsetY);
            //}
        }
    }

}