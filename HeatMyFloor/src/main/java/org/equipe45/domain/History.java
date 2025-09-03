package org.domain;


import org.equipe45.domain.PointReference;

import java.util.ArrayList;
import java.util.List;

public class History {
    private List<InteractiveEntity> entities = new ArrayList<>();
    private List<Cut> cuts = new ArrayList<>();
    private List<InteractiveEntity> clusterSelection = new ArrayList<>();
    private List<PointReference> pointReference = new ArrayList<>();
    private List<CutLine> cutLines = new ArrayList<>();
    private Panel panel;

    public void setPanel(Panel panel) {
        this.panel = panel;
    }

    public List<Cut> getCuts() {
        return cuts;
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

    public void deleteCut(Cut cut) {
        entities.remove(cut);
        updateAllAfterAnAction();
    }

    public List <CutLine> appendEveryCutLine() {
        List <CutLine> cutLinesList = new ArrayList < > ();
        for (Cut cut: cuts) {
            cutLinesList.addAll(cut.getCutLines());
        }
        return cutLinesList;
    }

    public void updateAllAfterAnAction() {
        // Update cut lines, cuts, and reference points
        cutLines = appendEveryCutLine();
        updateCuts();
        pointReference = getReferencePoints();

        // Adjust entities to ensure no entity is outside the panel
        ensureEntitiesWithinPanel();
    }

    public void updateCuts() {
        cuts.clear();
        for (InteractiveEntity entity : entities) {
            if (entity instanceof Cut) {
                cuts.add((Cut) entity);
            }
        }
    }

    private void ensureEntitiesWithinPanel() {
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

    private void ensurePointReferenceWithinPanel(PointReference reference) {
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
    }

    public void changePanelSize(int width, int height) {
        if (width > 0 && height > 0) {
            double adjustedWidth = ConversionPiedMM.convertirPiedEnMillimetres(width);
            double adjustedHeight = ConversionPiedMM.convertirPiedEnMillimetres(height);
            panel.changeSize(adjustedWidth, adjustedHeight);
            updateAllAfterAnAction();
        }
    }

    public List<PointReference> getReferencePoints() {
        List<PointReference> updatedPointReferences = new ArrayList<>();
        for (Cut cut1 : cuts) {
            for (CutLine cutLine1 : cut1.getCutLines()) {
                for (Cut cut2 : cuts) {
                    if (cut1 == cut2) continue;
                    for (CutLine cutLine2 : cut2.getCutLines()) {
                        if (areCutLinesIntersecting(cutLine1, cutLine2)) {
                            Point point = calculateIntersection(cutLine1, cutLine2);
                            PointReference existingReference = findPointReference(cutLine1, cutLine2);
                            if (existingReference != null) {
                                if (!existingReference.getPoint().equals(point)) {
                                    existingReference.setPoint(point);
                                    updateRelatedEntities(existingReference);
                                }
                                updatedPointReferences.add(existingReference);
                            } else {
                                updatedPointReferences.add(new PointReference(point, cutLine1, cutLine2));
                            }
                        }
                    }
                }
            }
        }
        pointReference.clear();
        pointReference.addAll(updatedPointReferences);
        return pointReference;
    }

    private Point calculateIntersection(CutLine line1, CutLine line2) {
        return new Point(
                (int) (line1.getStart().getX() + line1.getEnd().getX()) / 2,
                (int) (line1.getStart().getY() + line1.getEnd().getY()) / 2
        );
    }

    private PointReference findPointReference(CutLine cutLine1, CutLine cutLine2) {
        for (PointReference reference : pointReference) {
            if ((reference.getCutLineOrigin1() == cutLine1 && reference.getCutLineOrigin2() == cutLine2) ||
                    (reference.getCutLineOrigin1() == cutLine2 && reference.getCutLineOrigin2() == cutLine1)) {
                return reference;
            }
        }
        return null;
    }

    //areCutLinesIntersecting
    private boolean areCutLinesIntersecting(CutLine cutLine1, CutLine cutLine2) {
        int x1 = (int) cutLine1.getStart().getX();
        int y1 = (int) cutLine1.getStart().getY();
        int x2 = (int) cutLine1.getEnd().getX();
        int y2 = (int) cutLine1.getEnd().getY();
        int x3 = (int) cutLine2.getStart().getX();
        int y3 = (int) cutLine2.getStart().getY();
        int x4 = (int) cutLine2.getEnd().getX();
        int y4 = (int) cutLine2.getEnd().getY();

        int denominator = ((x1 - x2) * (y3 - y4)) - ((y1 - y2) * (x3 - x4));
        if (denominator == 0) {
            return false;
        }

        int x = (((x1 * y2) - (y1 * x2)) * (x3 - x4) - (x1 - x2) * ((x3 * y4) - (y3 * x4))) / denominator;
        int y = (((x1 * y2) - (y1 * x2)) * (y3 - y4) - (y1 - y2) * ((x3 * y4) - (y3 * x4))) / denominator;

        return x >= Math.min(x1, x2) && x <= Math.max(x1, x2) && y >= Math.min(y1, y2) && y <= Math.max(y1, y2) &&
                x >= Math.min(x3, x4) && x <= Math.max(x3, x4) && y >= Math.min(y3, y4) && y <= Math.max(y3, y4);



    }
    private void updateRelatedEntities(PointReference reference) {
        // Update cuts that use this reference point
        for (Cut cut : cuts) {
            if (cut instanceof Square) {
                Square square = (Square) cut;
                if (square.getReferencePoint().equals(reference.getId())) {
                    // Update the position of the Square based on the reference point
                    square.moveTo(reference.getPoint().x, reference.getPoint().y);

                    // Recursively update any child reference points created by this Square
                    PointReference childReference = getPointReferenceByCut(square);
                    if (childReference != null) {
                        updateRelatedEntities(childReference);
                    }
                }
            } else if (cut instanceof L) {
                L l = (L) cut;
                if (l.getReferencePoint().equals(reference.getId())) {
                    // Update the position of the L cut based on the reference point
                    l.moveTo(reference.getPoint().x, reference.getPoint().y);

                    // Recursively update any child reference points created by this L cut
                    PointReference childReference = getPointReferenceByCut(l);
                    if (childReference != null) {
                        updateRelatedEntities(childReference);
                    }
                }
            }
        }

        // Update any other reference points that depend on this reference point
        for (PointReference otherReference : pointReference) {
            if (otherReference.getCutLineOrigin1Id().equals(reference.getCutLineOrigin1Id()) ||
                    otherReference.getCutLineOrigin1Id().equals(reference.getCutLineOrigin2Id()) ||
                    otherReference.getCutLineOrigin2Id().equals(reference.getCutLineOrigin1Id()) ||
                    otherReference.getCutLineOrigin2Id().equals(reference.getCutLineOrigin2Id())) {
                // Move the dependent reference point
                otherReference.setPoint(reference.getPoint());

                // Recursively update entities related to the dependent reference point
                updateRelatedEntities(otherReference);
            }
        }
    }

    /**
     * Finds the PointReference created by a specific cut, if any.
     */
    private PointReference getPointReferenceByCut(Cut cut) {
        for (PointReference reference : pointReference) {
            if (reference.getCutLineOrigin1Id().equals(cut.getCutLines().get(0).getId()) ||
                    reference.getCutLineOrigin2Id().equals(cut.getCutLines().get(0).getId())) {
                return reference;
            }
        }
        return null;
    }

}