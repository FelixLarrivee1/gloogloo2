package org.equipe39.domain;


import org.equipe39.domain.Cut.Cut;
import org.equipe39.domain.Cut.L;
import org.equipe39.domain.Cut.RestrictedArea;
import org.equipe39.domain.Cut.Square;

import java.awt.geom.Point2D;
import java.util.*;

public class History {
    private List<InteractiveEntity> entities = new ArrayList<>();
    private List<Cut> cuts = new ArrayList<>();
    private List<PointReference> pointReference = new ArrayList<>();
    private List<CutLine> cutLines = new ArrayList<>();
    private List<List<InteractiveEntity>> actionHistory = new ArrayList<>();

    private int currentHistoryIndex = 0;
    
    private Panel panel;

    public void setPanel(Panel panel) {
        this.panel = panel;
    }

    public List<Cut> getCuts() {
        return cuts;
    }


    public History() {
        this.actionHistory = new ArrayList<>();
        this.actionHistory.add(new ArrayList<>());
        //this.entities = new ArrayList<>();
        //this.cuts = new ArrayList<>();
        //this.clusterSelection = new ArrayList<>();
        //this.pointReference = new ArrayList<>();
        //this.cutLines = new ArrayList<>();
        //this.actionHistory = new ArrayList<>();
        //this.currentHistoryIndex = 0;
        
    }

    public void addCut(InteractiveEntity cut) {
        entities.add(cut);
        updateAllAfterAnAction();
    }

    public void updateInteractiveEntity(InteractiveEntity cut) {
        for (int i = 0; i < entities.size(); i++) {
            if (entities.get(i).isSelected()) {
                entities.set(i, cut);
            }
        }
        updateAllAfterAnAction();
    }

    public List<InteractiveEntity> getInteractiveEntities()
    {
        return this.entities;
    }

    public void deleteCut(Cut cut) {
        entities.remove(cut);
        updateAllAfterAnAction();
    }

    public List <CutLine> appendEveryCutLine() {
        List <CutLine> cutLinesList = new ArrayList < > ();
        
        for (Cut cut: cuts) {
            cutLinesList.addAll(cut.getCutLines());
        }

        // Add the panel's cut lines
        cutLinesList.addAll(panel.getCutLines());


        System.out.println("CutLinesList: {");
        for (CutLine cutLine: cutLinesList) {
            System.out.println(cutLine);
        }
        System.out.println("}");

        
        return cutLinesList;
    }

    public List<PointReference> getPointReference() {
        return pointReference;
    }

    public void updateAllAfterAnAction() {
        updateCuts();
        cutLines = appendEveryCutLine();
        getReferencePoints();

    }

    public void updateCuts() {
        cuts.clear();
        for (InteractiveEntity entity : entities) {
            if (entity instanceof Cut) {
                cuts.add((Cut) entity);
            }
        }
    }


    public InteractiveEntity getSelectedInteractiveEntity() {
        //check to see if its a reference point first
        for (PointReference pointReference: pointReference) {
            if (pointReference.isSelected()) {
                return pointReference;
            }
        }
        for (InteractiveEntity entity: entities) {
            System.out.println(entity);
            if (entity.isSelected()) {
                System.out.println("currently selected: " + entity);
                return entity;
            }
        }
        return null;
    }


    //get point refernce associated with cuts
    public void getPointReferenceAssociatedWithCuts() {
        for (Cut cut: cuts) {
            if (cut instanceof L) {
                L lCut = (L) cut;
                for (PointReference pointReference: pointReference) {
                    if (pointReference.equals(lCut.getReferencePoint())) {
                        lCut.setReferencePoint(pointReference);
                    }
                }
            }
            else if (cut instanceof Square) {
                Square square = (Square) cut;
                for (PointReference pointReference: pointReference) {
                    if (pointReference.equals(square.getReferencePoint())) {
                        square.setReferencePoint(pointReference);
                    }
                }
            }
        }
    }


    /*private void ensureEntitiesWithinPanel() {
        for (InteractiveEntity entity : entities) {
            if (entity instanceof Cut) {
                ensureCutWithinPanel((Cut) entity);
            } else if (entity instanceof PointReference) {
                ensurePointReferenceWithinPanel((PointReference) entity);
            }
        }
    }

    private void ensureCutWithinPanel(Cut cut) {
        if (cut instanceof VerticalLine) {
            VerticalLine verticalLine = (VerticalLine) cut;
            if (verticalLine.getX() < panel.getX()) {
                verticalLine.setX((int) panel.getX());
            } else if (verticalLine.getX() > panel.getX() + panel.getWidthFactor()) {
                verticalLine.setX((int) (panel.getX() + panel.getWidthFactor()));
            }
        } else if (cut instanceof HorizontalLine) {
            HorizontalLine horizontalLine = (HorizontalLine) cut;
            if (horizontalLine.getY() < panel.getY()) {
                horizontalLine.setY((int) panel.getY());
            } else if (horizontalLine.getY() > panel.getY() + panel.getHeightFactor()) {
                horizontalLine.setY((int) (panel.getY() + panel.getHeightFactor()));
            }
        } else if (cut instanceof Square) {
            Square square = (Square) cut;
            ensureSquareWithinPanel(square);
        } else if (cut instanceof L) {
            L lCut = (L) cut;
            ensureLCutWithinPanel(lCut);
        }
    }

    private void ensureSquareWithinPanel(Square square) {
        if (square.getX() < panel.getX()) {
            square.setX((int) panel.getX());
        }
        if (square.getY() < panel.getY()) {
            square.setY((int) panel.getY());
        }
        if (square.getX() + square.getWidthFactor() > panel.getX() + panel.getWidthFactor()) {
            square.setWidthFactor(panel.getWidthFactor() - (square.getX() - panel.getX()));
        }
        if (square.getY() + square.getHeightFactor() > panel.getY() + panel.getHeightFactor()) {
            square.setHeightFactor(panel.getHeightFactor() - (square.getY() - panel.getY()));
        }
    }

    private void ensureLCutWithinPanel(L lCut) {
        if (lCut.getX() < panel.getX()) {
            lCut.setX((int) panel.getX());
        }
        if (lCut.getY() < panel.getY()) {
            lCut.setY((int) panel.getY());
        }
        if (lCut.getX() > panel.getX() + panel.getWidthFactor()) {
            lCut.setX((int) (panel.getX() + panel.getWidthFactor()));
        }
        if (lCut.getY() > panel.getY() + panel.getHeightFactor()) {
            lCut.setY((int) (panel.getY() + panel.getHeightFactor()));
        }
    }

    /*private void ensurePointReferenceWithinPanel(PointReference reference) {
        Point point = reference.getPoint();
        if (point.x < panel.getX()) {
            point.x = (int) panel.getX();
        }
        if (((Point) point).y < panel.getY()) {
            point.y = (int) panel.getY();
        }
        if (point.x > panel.getX() + panel.getWidthFactor()) {
            point.x = (int) (panel.getX() + panel.getWidthFactor());
        }
        if (point.y > panel.getY() + panel.getHeightFactor()) {
            point.y = (int) (panel.getY() + panel.getHeightFactor());
        }
        reference.setPoint(point);

        // Update all related entities

        updateRelatedEntities(reference);
    }*/

    public void changePanelSize(int width, int height) {
        if (width > 0 && height > 0) {
            double adjustedWidth = ConversionPiedMM.convertirPiedEnMillimetres(width);
            double adjustedHeight = ConversionPiedMM.convertirPiedEnMillimetres(height);
            panel.changeSize(adjustedWidth, adjustedHeight);
            updateAllAfterAnAction();
        }
    }

    //getIntersection: do the two lines touch each other at any point?
    //public boolean getIntersection(CutLine cutLine1, CutLine cutLine2) {
    //    return cutLine1.getIntersection(cutLine2) != null;
    //}


    /*public InteractiveEntity isClickingSomething(Point point) {
        int x = point.x;
        int y = point.y;

        //prioritize the point reference
        for (InteractiveEntity entity : pointReference) {
            if (entity.isClicked(x, y)) {
                System.out.println("CLICKED: " + entity);
                return entity;
            }
        }
        for (InteractiveEntity entity : entities) {
            if (entity.isClicked(x, y)) {
                System.out.println("CLICKED: " + entity);
                return entity;
            }
        }
        return null;
    }*/


    public InteractiveEntity isClickingSomething(Point2D.Double point) {
        double x = point.getX();
        double y = point.getY();

        //prioritize the point reference
        for (InteractiveEntity entity : pointReference) {
            if (entity.isClicked(x, y)) {
                System.out.println("CLICKED: " + entity);
                return entity;
            }
        }
        for (InteractiveEntity entity : entities) {
            if (entity.isClicked(x, y)) {
                System.out.println("CLICKED: " + entity);
                return entity;
            }
        }
        return null;
    }



    /*

    public void updateHistoryAfterClickRelease()
    {
        if(this.actionHistory.isEmpty())
        {
            System.out.println("is empoty");
            //add an empty list to the history
            this.actionHistory = new ArrayList<>();
            this.actionHistory.add(new ArrayList<>());
        }
        if(!this.actionHistory.get(0).isEmpty())
        {
            System.out.println("not empty");
            this.actionHistory.set(0, new ArrayList<>());
        }
        if(this.currentHistoryIndex < this.actionHistory.size() - 1)
        {
            System.out.println("currently greater");
        //    System.out.println("removing everything ahead of current history index");
            //this.actionHistory = this.actionHistory.subList(0, this.currentHistoryIndex + 1);
            System.out.println(this.actionHistory.subList(0, this.currentHistoryIndex + 1));
            for(int i = this.actionHistory.size() - 1; i > this.currentHistoryIndex; i--)
            {
                this.actionHistory.remove(i);
            }
            //this.actionHistory = this.actionHistory.subList(0, 0);
        }
        List<InteractiveEntity> entitiesCopy = new ArrayList<>();
        for (InteractiveEntity entity : this.entities) {
            entitiesCopy.add(entity.clone());
        }
        System.out.println("History (currently at index " + this.currentHistoryIndex + "): {");
        int counter = 0;
        this.actionHistory.add(entitiesCopy);
        for (List<InteractiveEntity> action : this.actionHistory) {
            System.out.println(counter + ": " + action);
            counter++;
        }
        System.out.println("}");
        this.currentHistoryIndex = this.actionHistory.size() - 1;
        System.out.println("updated history list, current index: " + this.currentHistoryIndex + " out of " + this.actionHistory.size());

    }

    public void undo()
    {

        if(this.currentHistoryIndex - 1 >= 0)
        {
            this.currentHistoryIndex -= 1;
            //this.cuts = this.actionHistory.get(this.currentHistoryIndex);
            this.entities = this.actionHistory.get(this.currentHistoryIndex);
            System.out.println("entities after undoing " + this.entities);
            //this.updateAllAfterAnAction();
            System.out.println("Undoing " + this.currentHistoryIndex);
            this.updateAllAfterAnAction();
        }
        
    }


    public void redo()
    {
        if(this.currentHistoryIndex < this.actionHistory.size() - 1)
        {
            this.currentHistoryIndex += 1;
            //this.cuts = this.actionHistory.get(this.currentHistoryIndex);
            this.entities = this.actionHistory.get(this.currentHistoryIndex);
            //this.updateAllAfterAnAction();
            System.out.println("Redoing " + this.currentHistoryIndex);
            this.updateAllAfterAnAction();
        }
        
    }*/



public void updateHistoryAfterClickRelease() {
    if (this.actionHistory == null) {
        this.actionHistory = new ArrayList<>();
        this.currentHistoryIndex = -1;
    }

    if (this.currentHistoryIndex < this.actionHistory.size() - 1) {
        this.actionHistory = new ArrayList<>(this.actionHistory.subList(0, this.currentHistoryIndex + 1));
        //this.actionHistory.add(0, new ArrayList<>());
    }

    List<InteractiveEntity> entitiesCopy = new ArrayList<>();
    System.out.println("Entities before updating history: " + this.entities);
    for (InteractiveEntity entity : this.entities) {
        entitiesCopy.add(entity.clone());
    }

    this.actionHistory.add(entitiesCopy);

    this.currentHistoryIndex = this.actionHistory.size() - 1;

    System.out.println("History (currently at index " + this.currentHistoryIndex + "): {");
    int counter = 0;
    for (List<InteractiveEntity> action : this.actionHistory) {
        System.out.println(counter + ": " + action);
        counter++;
    }
    System.out.println("}");
    System.out.println("Updated history list, current index: " + this.currentHistoryIndex + " out of " + this.actionHistory.size());
}





public void undo() {
    if (this.currentHistoryIndex - 1 >= 0) {
        this.currentHistoryIndex -= 1;

        List<InteractiveEntity> entitiesCopy = new ArrayList<>();
        for (InteractiveEntity entity : this.actionHistory.get(this.currentHistoryIndex)) {
            entitiesCopy.add(entity.clone());
        }
        this.entities = entitiesCopy;

        System.out.println("entities after undoing " + this.entities);
        System.out.println("Undoing " + this.currentHistoryIndex);
        this.updateAllAfterAnAction();
    }
}





public void redo() {
    if (this.currentHistoryIndex < this.actionHistory.size() - 1) {
        this.currentHistoryIndex += 1;

        // Clone entities from history
        List<InteractiveEntity> entitiesCopy = new ArrayList<>();
        for (InteractiveEntity entity : this.actionHistory.get(this.currentHistoryIndex)) {
            entitiesCopy.add(entity.clone());
        }
        this.entities = entitiesCopy;

        System.out.println("Redoing " + this.currentHistoryIndex);
        this.updateAllAfterAnAction();
    }
}










    /*public void getReferencePoints() {
        List<PointReference> updatedPointReferences = new ArrayList<>();
        //loop through the list once
        for (CutLine cutLine1 : cutLines)
        {
            for(CutLine cutLine2 : cutLines)
            {
                //make sure the two cutlines are not the same
                if (cutLine1 != cutLine2)
                {
                    //see if they intersect
                    Point intersection = cutLine1.getIntersection(cutLine2);
                    PointReference potentialPointReference = new PointReference(intersection, cutLine1, cutLine2);
                    System.out.println("Intersection: " + intersection);
                    if (intersection != null) {
                        boolean alreadyExist = false;
                        //loop through the list of point references
                        updatedPointReferences.add(potentialPointReference);
                        //if exists, update it in the list, but keep the original id, just update the cutlines
                        for (PointReference pointReference : pointReference) {
                            if (pointReference.equalsWithCutlines(potentialPointReference)) {
                                //seems like the point reference already exists
                                //update and keep the original id
                                pointReference.setCutLineOrigin1(cutLine1);
                                pointReference.setCutLineOrigin2(cutLine2);
                                alreadyExist = true;
                                //updatedPointReferences.add(pointReference);
                            }
                            else {
                                updatedPointReferences.add(potentialPointReference);
                            }
                        }
                    }
                }
                else {
                    System.out.println("Skipping cutLine1: " + cutLine1 + " and cutLine2: " + cutLine1 + " because they are the same");
                }
            }

        }
        this.pointReference = updatedPointReferences;
        //filter the updated point references to update the

}*/


    //EXTREMEMENT PLUS EFFICACE QUE L'AUTRE MAIS MARCHE A MOITIER
    //OK IL MARCHE MAINTENANT
    public void getReferencePoints() {
        List<PointReference> updatedPointReferences = new ArrayList<>();
        Set<String> processedPairs = new HashSet<>();

        int n = cutLines.size();
        for (int i = 0; i < n; i++) {
            CutLine cutLine1 = cutLines.get(i);
            for (int j = i + 1; j < n; j++) {
                CutLine cutLine2 = cutLines.get(j);

                if (cutLinesAreSame(cutLine1, cutLine2)) {
                    //System.out.println("Skipping pair: " + cutLine1 + " and " + cutLine2 + " because they are the same");
                    continue;
                }

                String pairKey = generatePairKey(cutLine1, cutLine2);
                if (processedPairs.contains(pairKey)) {
                    System.out.println("Skipping pair: " + pairKey + " because it was already processed");
                    continue;
                }
                processedPairs.add(pairKey);

                // Check for intersection
                Point2D.Double intersection = cutLine1.getIntersection(cutLine2);
                if (intersection != null) {
                    //if point reference already exists, update it by keeping the original id
                    boolean pointRefExists = false;
                    //TODO: rework this so its more effitient
                    for (PointReference pointReference : pointReference) {
                        //if (pointReference.equalsWithCutlines(new PointReference(intersection, cutLine1, cutLine2))) {
                        if (pointReference.equalsWithCutlines(new PointReference(intersection, cutLine1, cutLine2))) {
                            pointReference.setCutLineOrigin1(cutLine1);
                            pointReference.setCutLineOrigin2(cutLine2);
                            pointReference.setPoint(intersection);
                            updatedPointReferences.add(pointReference);

                            pointRefExists = true;
                            break;
                        }
                    }
                    if (!pointRefExists) {
                        PointReference pointRef = new PointReference(intersection, cutLine1, cutLine2);
                        updatedPointReferences.add(pointRef);
                    }
                    //PointReference pointRef = new PointReference(intersection, cutLine1, cutLine2);
                    //updatedPointReferences.add(pointRef);
                }
            }
        }

        this.pointReference = updatedPointReferences;
        this.referencePointRelatedEntityUpdate();
    }

/*
    public void referencePointRelatedEntityUpdate() {
        //check for deletion
        ArrayList<Cut> toDelete = new ArrayList<>();
        for(Cut cut : this.cuts)
        {
            //if the cut is an L or a square, check if the reference points are still present in the point reference list
            if(cut instanceof L)
            {
                L lCut = (L) cut;
                if(!this.pointReference.contains(lCut.getReferencePoint()))
                {
                    //print all the ref points
                    int counter = 0;
                    //System.out.println("Point reference size: " + this.pointReference.size());
                    for(PointReference pointReference : this.pointReference)
                    {
                        System.out.println("Point reference " + counter + ": " + pointReference);
                        //System.out.println(pointReference);
                    }    
                    System.out.println("Deleting " + lCut + " because point reference was deleted");
                    toDelete.add(lCut);
                }
                else
                {
                    //update the reference point
                    //lCut.setReferencePoint(this.pointReference.get(this.pointReference.indexOf(lCut.getReferencePoint())));
                }

            }
            else if(cut instanceof Square)
            {
                Square square = (Square) cut;
                if(!this.pointReference.contains(square.getReferencePoint()))
                {
                    System.out.println("Deleting " + square + " because point reference was deleted");
                    toDelete.add(square);
                }
                else
                {
                    //update the reference point
                    //square.setReferencePoint(this.pointReference.get(this.pointReference.indexOf(square.getReferencePoint())));
                }

            }
        }
        for(Cut cut : toDelete)
        {
            this.entities.remove(cut);
        }
        if(toDelete.isEmpty())
        {
            //updateHistoryAfterClickRelease();
            return;
        }
        this.updateAllAfterAnAction();
    }*/


    public void referencePointRelatedEntityUpdate() {
        //check for deletion
        ArrayList<Cut> toDelete = new ArrayList<>();
        for(Cut cut : this.cuts)
        {
            //if the cut is an L or a square, check if the reference points are still present in the point reference list
            if(cut instanceof L)
            {
                L lCut = (L) cut;
                toDelete.add(lCut);
                for(PointReference pointReference : this.pointReference)
                {
                    if(pointReference.equals(lCut.getReferencePoint()))
                    {
                        toDelete.remove(lCut);
                        System.out.println("WE CAN KEEP " + lCut);
                        lCut.setReferencePoint(pointReference.clone());
                        break;
                    }
                    //else
                    //{
                    //    System.out.println("Deleting " + lCut + " because point reference was deleted");
                    //    toDelete.add(lCut);
                    //}
                }
                //{
                //    
                //    toDelete.add(lCut);
                //}
                //else
                //{
                //    //update the reference point
                //    //lCut.setReferencePoint(this.pointReference.get(this.pointReference.indexOf(lCut.getReferencePoint())));
                //}

            }
            else if(cut instanceof Square)
            {
                Square square = (Square) cut;
                if(!this.pointReference.contains(square.getReferencePoint()))
                {
                    System.out.println("Deleting " + square + " because point reference was deleted");
                    toDelete.add(square);
                }
                else
                {
                    //update the reference point
                    //square.setReferencePoint(this.pointReference.get(this.pointReference.indexOf(square.getReferencePoint())));
                }

            }
        }
        for(Cut cut : toDelete)
        {
            this.entities.remove(cut);
        }
        if(toDelete.isEmpty())
        {
            //updateHistoryAfterClickRelease();
            return;
        }
        this.updateAllAfterAnAction();
    }


    //get every restricted area
    public List<RestrictedArea> getRestrictedAreas() {
        List<RestrictedArea> restrictedAreas = new ArrayList<>();
        for (Cut cut : cuts) {
            if (cut instanceof RestrictedArea) {
                restrictedAreas.add((RestrictedArea) cut);
            }
        }
        return restrictedAreas;
    }



    //get all related to the point reference
    //public void updateRelatedEntities(PointReference pointReference) {
    //    for (CutLine cutLine : cutLines) {
    //        if (cutLine.equals(pointReference.getCutLineOrigin1()) || cutLine.equals(pointReference.getCutLineOrigin2())) {
    //            cutLine.update();
    //        }
    //    }
    //}

    // Helper method to determine if two CutLines are the same
    private boolean cutLinesAreSame(CutLine cl1, CutLine cl2) {
        return cl1.getId() == cl2.getId() && Objects.equals(cl1.getCut(), cl2.getCut());
    }

    private String generatePairKey(CutLine cl1, CutLine cl2) {
        int hash1 = cl1.hashCode();
        int hash2 = cl2.hashCode();
        if (hash1 < hash2) {
            return hash1 + "_" + hash2;
        } else {
            return hash2 + "_" + hash1;
        }
    }



    //check if every cut is contained within a restricted area
    public void checkIfCutIsContainedWithinRestrictedArea() {
        List<RestrictedArea> restrictedAreas = getRestrictedAreas();
        for (Cut cut : cuts) {
            for (RestrictedArea restrictedArea : restrictedAreas) {
                if(cut.isTouching(restrictedArea))
                {
                    System.out.println("Cut " + cut + " is touching " + restrictedArea);
                }
            }
        }
    }

    //check if one cut is touching another a restricted area
    public boolean isCutTouchingRestrictedArea(Cut cut) {
        List<RestrictedArea> restrictedAreas = getRestrictedAreas();
        for (RestrictedArea restrictedArea : restrictedAreas) {
            if(restrictedArea.isTouching(cut))
            {
                System.out.println("Cut " + cut + " is touching " + restrictedArea);
                return true;
            }
        }
        return false;
    }







    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    













}