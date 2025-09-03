package org.equipe39.domain;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.*;




import java.util.*;
import java.awt.geom.Point2D;

public class RectangleFinder {

    /**
     * Given a list of PointReference objects, find all sets of four points that form rectangles.
     * @param points the list of points
     * @return a list of rectangle solutions, each rectangle is a list of 4 points (corners)
     */
    public static List<List<PointReference>> findAllRectangles(List<PointReference> points) {
        List<List<PointReference>> rectangles = new ArrayList<>();
        System.out.println("etered");
        // Generate all combinations of 4 distinct points
        int n = points.size();
        for (int i = 0; i < n; i++) {
            for (int j = i+1; j < n; j++) {
                for (int k = j+1; k < n; k++) {
                    for (int l = k+1; l < n; l++) {
                        PointReference p1 = points.get(i);
                        PointReference p2 = points.get(j);
                        PointReference p3 = points.get(k);
                        PointReference p4 = points.get(l);

                        if (isRectangle(p1, p2, p3, p4)) {

                            System.out.println("found rect");
                            // Add this combination as a potential rectangle
                            rectangles.add(Arrays.asList(p1, p2, p3, p4));
                        }
                    }
                }
            }
        }

        // Sort rectangles by their area
        rectangles.sort(Comparator.comparingDouble(RectangleFinder::rectangleArea));

        return rectangles;
    }

    /**
     * Check if four points form a rectangle by comparing their distances.
     *
     * @param A point 1
     * @param B point 2
     * @param C point 3
     * @param D point 4
     * @return true if ABCD form a rectangle, false otherwise
     */
    private static boolean isRectangle(PointReference A, PointReference B, PointReference C, PointReference D) {
        //System.out.println("check for rectangles");
        double[] dist = new double[6];
        dist[0] = squaredDistance(A, B);
        dist[1] = squaredDistance(A, C);
        dist[2] = squaredDistance(A, D);
        dist[3] = squaredDistance(B, C);
        dist[4] = squaredDistance(B, D);
        dist[5] = squaredDistance(C, D);

        Arrays.sort(dist);

        // For a rectangle, the first four distances (the sides) must be equal and non-zero
        // and the last two distances (the diagonals) must be equal.
        // Distances:
        //   sides: dist[0], dist[1], dist[2], dist[3]
        //   diagonals: dist[4], dist[5]

        return dist[0] > 0 && // no zero-length sides
                dist[0] == dist[1] && dist[1] == dist[2] && dist[2] == dist[3] &&
                dist[4] == dist[5];
    }

    /**
     * Compute the squared distance between two PointReferences to avoid floating point errors with sqrt.
     */
    private static double squaredDistance(PointReference p1, PointReference p2) {
        double dx = p1.getX() - p2.getX();
        double dy = p1.getY() - p2.getY();
        return dx*dx + dy*dy;
    }

    /**
     * Compute the area of the rectangle formed by the given four points.
     * We know it's a rectangle, so we can find two adjacent sides.
     *
     * One simple approach:
     * - Sort the points by x and then by y to get some order.
     * - The rectangle can be defined by adjacent points after sorting.
     *
     * Alternatively, since we know the first four smallest distances are the sides and they come in pairs:
     * - Extract two distinct side lengths from the four smallest distances (in isRectangle we have that)
     *   and then take the product of their square roots.
     */
    public static double rectangleArea(List<PointReference> rectanglePoints) {
        // rectanglePoints has exactly 4 points.
        // Let's pick one approach: use a convex hull or just find the bounding box after verifying it's a rectangle.

        // If it's a perfect rectangle aligned in any orientation (not necessarily axis-aligned),
        // we can rely on the fact we have two sets of equal sides.
        // Let's find the sides by picking one point as a reference:

        PointReference A = rectanglePoints.get(0);
        PointReference B = rectanglePoints.get(1);
        PointReference C = rectanglePoints.get(2);
        PointReference D = rectanglePoints.get(3);

        // We have confirmed this is a rectangle. Let's find two adjacent sides.
        // A trick: Identify which point pairs represent sides meeting at a corner.
        // For this, find a point with two equal minimal distances to two other points.

        // Distances from A to others:
        double dAB = squaredDistance(A,B);
        double dAC = squaredDistance(A,C);
        double dAD = squaredDistance(A,D);

        // We'll try A as a reference corner.
        // We know that at a corner, there should be exactly two equal smallest distances (the sides).
        // After sorting these three distances, the first two should be the sides.
        double[] dA = {dAB, dAC, dAD};
        Arrays.sort(dA);

        // dA[0] and dA[1] are side lengths squared
        double side1 = Math.sqrt(dA[0]);
        double side2 = Math.sqrt(dA[1]);

        return side1 * side2;
    }
}





/*public class RectangleFinder {

    public static List<double[]> findRectanglesFromCutlines(List<CutLine> cutLines) {
        List<double[]> rectangles = new ArrayList<>();

        // Collect all unique x and y coordinates from cutlines
        Set<Double> xCoords = new HashSet<>();
        Set<Double> yCoords = new HashSet<>();

        for (CutLine cutLine : cutLines) {
            Point2D.Double start = cutLine.getStart();
            Point2D.Double end = cutLine.getEnd();

            xCoords.add(start.getX());
            xCoords.add(end.getX());
            yCoords.add(start.getY());
            yCoords.add(end.getY());
        }

        // Convert sets to sorted lists
        List<Double> xList = new ArrayList<>(xCoords);
        List<Double> yList = new ArrayList<>(yCoords);

        Collections.sort(xList);
        Collections.sort(yList);

        // For each cell defined by adjacent x and y coordinates
        for (int i = 0; i < xList.size() - 1; i++) {
            double x1 = xList.get(i);
            double x2 = xList.get(i + 1);

            for (int j = 0; j < yList.size() - 1; j++) {
                double y1 = yList.get(j);
                double y2 = yList.get(j + 1);

                // Check if the rectangle is fully enclosed by cutlines
                if (isRectangleEnclosed(cutLines, x1, y1, x2, y2)) {
                    rectangles.add(new double[]{x1, y1, x2, y2});
                }
            }
        }

        System.out.println("Found " + rectangles.size() + " rectangles");
        for (double[] rect : rectangles) {
            System.out.println(Arrays.toString(rect));
        }
        
        return rectangles;
    }

    // Helper method to check if a rectangle is enclosed by cutlines
    private static boolean isRectangleEnclosed(List<CutLine> cutLines, double x1, double y1, double x2, double y2) {
        boolean leftEdge = isVerticalCutLinePresent(cutLines, x1, y1, y2);
        boolean rightEdge = isVerticalCutLinePresent(cutLines, x2, y1, y2);
        boolean topEdge = isHorizontalCutLinePresent(cutLines, y1, x1, x2);
        boolean bottomEdge = isHorizontalCutLinePresent(cutLines, y2, x1, x2);

        return leftEdge && rightEdge && topEdge && bottomEdge;
    }

    // Check for vertical cutline at x between y1 and y2
    private static boolean isVerticalCutLinePresent(List<CutLine> cutLines, double x, double y1, double y2) {
        for (CutLine cutLine : cutLines) {
            Point2D.Double start = cutLine.getStart();
            Point2D.Double end = cutLine.getEnd();

            if (approxEqual(start.getX(), end.getX()) && approxEqual(start.getX(), x)) {
                double minY = Math.min(start.getY(), end.getY());
                double maxY = Math.max(start.getY(), end.getY());

                if (minY <= y1 + epsilon() && maxY >= y2 - epsilon()) {
                    return true;
                }
            }
        }
        return false;
    }

    // Check for horizontal cutline at y between x1 and x2
    private static boolean isHorizontalCutLinePresent(List<CutLine> cutLines, double y, double x1, double x2) {
        for (CutLine cutLine : cutLines) {
            Point2D.Double start = cutLine.getStart();
            Point2D.Double end = cutLine.getEnd();

            if (approxEqual(start.getY(), end.getY()) && approxEqual(start.getY(), y)) {
                double minX = Math.min(start.getX(), end.getX());
                double maxX = Math.max(start.getX(), end.getX());

                if (minX <= x1 + epsilon() && maxX >= x2 - epsilon()) {
                    return true;
                }
            }
        }
        return false;
    }

    // Helper method to compare doubles with a small tolerance
    private static boolean approxEqual(double a, double b) {
        return Math.abs(a - b) < epsilon();
    }

    private static double epsilon() {
        return 1e-6;
    }
}*/