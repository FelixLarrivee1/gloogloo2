package org.equipe39.domain;

import org.equipe39.dto.InteractiveEntityDTO;
import org.equipe39.dto.PanelDTO;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Panel extends InteractiveEntity {
    private double x, y;
    private double widthFactor, heightFactor;
    private double epaisseur;
    //private double initialGridSize;
    public Panel(double x, double y, double widthFactor, double heightFactor, double epaisseur) {
        this.id = UUID.randomUUID();
        this.x = x;
        this.y = y;
        this.widthFactor = widthFactor;
        this.heightFactor = heightFactor;
        this.epaisseur = epaisseur;
    }

    public Panel(UUID id, boolean isSelected, double x, double y, double widthFactor, double heightFactor, double epaisseur) {
        //super(id, isSelected);
        this.id = id;
        this.isSelected = isSelected;
        this.x = x;
        this.y = y;
        this.widthFactor = widthFactor;
        this.heightFactor = heightFactor;
        this.epaisseur = epaisseur;
    }





    public List<CutLine> getCutLines() {
        List<CutLine> panelLines = new ArrayList<>();
        Point2D.Double start = new Point2D.Double(x, y);
        Point2D.Double end = new Point2D.Double(x + widthFactor, y);
        Point2D.Double start2 = new Point2D.Double(x, y);
        Point2D.Double end2 = new Point2D.Double(x, y + heightFactor);
        Point2D.Double start3 = new Point2D.Double(x + widthFactor, y);
        Point2D.Double end3 = new Point2D.Double(x + widthFactor, y +  heightFactor);
        Point2D.Double start4 = new Point2D.Double(x, y +  heightFactor);
        Point2D.Double end4 = new Point2D.Double(x +  widthFactor, y + heightFactor);

        panelLines.add(new CutLine(1, start, end, this));
        panelLines.add(new CutLine(2, start2, end2, this));
        panelLines.add(new CutLine(3, start3, end3, this));
        panelLines.add(new CutLine(4, start4, end4, this));

        return panelLines;
    }



    public double getEpaisseur()
    {
        return this.epaisseur;
    }


    public void setEpaisseur(double epaisseur)
    {
        this.epaisseur = epaisseur;
    }

    public void changeSize(double width, double height)
    {
        System.out.println("new dim: " + width + " " + height);
        this.widthFactor = width;
        this.heightFactor = height;
    }

    public double getHeightFactor() {
        return heightFactor;
    }

    public double getWidthFactor() {
        return widthFactor;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setHeightFactor(double heightFactor) {
        this.heightFactor = heightFactor;
    }


    @Override
    public InteractiveEntityDTO toDTO() {
        return new PanelDTO(this.id, this.isSelected, this.getX(), this.getY(), this.getWidthFactor(), this.getHeightFactor() , this.getEpaisseur() );
    }

    @Override
    public boolean isClicked(double mouseX, double mouseY) {
        return false;
    }

    @Override
    public InteractiveEntity clone() {
        return new Panel(this.id, this.isSelected, this.x, this.y, this.widthFactor, this.heightFactor, this.epaisseur);
    }

    public PanelDTO getPanelDTO() {
        return new PanelDTO(this.id, this.isSelected, this.x, this.y, this.widthFactor, this.heightFactor, this.epaisseur);
    }   
}


