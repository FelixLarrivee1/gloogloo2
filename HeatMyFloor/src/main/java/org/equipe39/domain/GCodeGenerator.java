package org.equipe39.domain;


import org.equipe39.domain.Cut.*;
import org.equipe39.domain.CutLine;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GCodeGenerator {

    public static List<String> convertCutLinesToGCode(List<CutLine> cutLines,int laserPower, int feedRate) {
//        double panelHeight = 0, panelWidth = 0;
//        for (CutLine cutLine : cutLines) {
//            if (cutLine.getCut() instanceof Panel) {
//                panelHeight = ((Panel) cutLine.getCut()).getHeightFactor();
//                panelWidth = ((Panel) cutLine.getCut()).getWidthFactor();
//                break;
//            }
//        }
        List<String> gcodeInstructions = new ArrayList<>();
        gcodeInstructions.add("G21 ; Set units to millimeters");
        gcodeInstructions.add("G90 ; Use absolute positioning");
        gcodeInstructions.add(String.format("G1 F%d ; Set default feed rate", feedRate));
        for (CutLine cutLine : cutLines) {
//            if (cutLine.getCut() instanceof VerticalLine) {
//                cutLine.setEnd(new Point2D.Double(cutLine.getEnd().getX(), panelHeight));
//            }
//            if (cutLine.getCut() instanceof HorizontalLine) {
//                cutLine.setEnd(new Point2D.Double(panelWidth, cutLine.getEnd().getY()));
//            }
            Point2D.Double start = cutLine.getStart();
            Point2D.Double end = cutLine.getEnd();
            InteractiveEntity cut = cutLine.getCut();
            //NEW LINE YOU NEED TO USE
            Point2D.Double startPoint = cutLine.getStart();
            double profondeur = getCutLineProfondeur(cutLine);

            gcodeInstructions.add(String.format("G0 X%.3f Y%.3f", start.getX(), start.getY()));
            gcodeInstructions.add(String.format("M3 S%d", laserPower));
            gcodeInstructions.add(String.format("G1 X%.3f Y%.3f F%d", end.getX(), end.getY(), feedRate));
            gcodeInstructions.add("M5");
        }
        //cutLines.get(0).
        gcodeInstructions.add("G0 X0 Y0 ; Return to origin");
        return gcodeInstructions;
    }
    public static void saveGCodeToFile(List<String> gcodeInstructions, File outputFile) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            for (String line : gcodeInstructions) {
                writer.write(line);
                writer.newLine();
            }
            System.out.println("G-code saved to " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


/*
   //TODO: rework a bit
   public static List<String> convertCutLinesToGCode(List<CutLine> cutLines, Panel panel) {
        List<String> gcodeInstructions = new ArrayList<>();

        gcodeInstructions.add("G21 ; Set units to millimeters");
        gcodeInstructions.add("G90 ; Use absolute positioning");


        double safeZ = 5.0;
        double plungeFeedRate = 300;
        double defaultFeedRate = 1000;

        for (CutLine cutLine : cutLines) {
            Point2D start = cutLine.getStart();
            Point2D end = cutLine.getEnd();
            Cut cut = cutLine.getCut();

            double thickness = getCutLineEppaisseur(cutLine);
            double depth = getCutLineProfondeur(cutLine, panel);

            if (thickness <= 0) {
                System.out.println("Skipping CutLine with non-positive thickness.");
                continue;
            }

            double toolDiameter = thickness;
            int numPasses = 1;

            gcodeInstructions.add(String.format("G0 Z%.3f", safeZ));
            gcodeInstructions.add(String.format("G0 X%.3f Y%.3f", start.getX(), start.getY()));
            gcodeInstructions.add(String.format("G1 Z%.3f F%.1f", -depth, plungeFeedRate));
            gcodeInstructions.add(String.format("G1 X%.3f Y%.3f F%.1f", end.getX(), end.getY(), defaultFeedRate));
            gcodeInstructions.add(String.format("G0 Z%.3f", safeZ));
        }

        gcodeInstructions.add("G0 X0 Y0 ; Return to origin");

        return gcodeInstructions;
    }*/


       /*public static List<String> convertCutLinesToGCode(List<CutLine> cutLines, Panel panel) {
        List<String> gcodeInstructions = new ArrayList<>();

        gcodeInstructions.add("G21 ; Set units to millimeters");
        gcodeInstructions.add("G90 ; Use absolute positioning");

        double safeZ = 5.0; // Safe height above the material
        double plungeFeedRate = 300; // Feed rate for plunging (Z-axis movement)
        double defaultFeedRate = 1000; // Default feed rate for cutting moves

        for (CutLine cutLine : cutLines) {
            Point2D start = cutLine.getStart();
            Point2D end = cutLine.getEnd();
            InteractiveEntity cut = cutLine.getCut();

            double thickness = getCutLineEppaisseur(cutLine);
            double depth = getCutLineProfondeur(cutLine, panel);

            if (thickness <= 0) {
                System.out.println("Skipping CutLine with non-positive thickness.");
                continue;
            }

            // Calculate rectangle around the line based on 'profondeur'
            // For this example, we'll assume the rectangle width is proportional to the 'profondeur'

            double rectangleWidth = thickness; // You can adjust this as needed

            // Calculate the vector along the line
            double dx = end.getX() - start.getX();
            double dy = end.getY() - start.getY();
            double length = Math.sqrt(dx * dx + dy * dy);

            // Normalize perpendicular vector
            double nx = -dy / length;
            double ny = dx / length;

            // Scale the perpendicular vector by half the rectangle width
            double offsetX = nx * rectangleWidth / 2;
            double offsetY = ny * rectangleWidth / 2;

            // Calculate the four corners of the rectangle
            Point2D p1 = new Point2D.Double(start.getX() + offsetX, start.getY() + offsetY);
            Point2D p2 = new Point2D.Double(end.getX() + offsetX, end.getY() + offsetY);
            Point2D p3 = new Point2D.Double(end.getX() - offsetX, end.getY() - offsetY);
            Point2D p4 = new Point2D.Double(start.getX() - offsetX, start.getY() - offsetY);

            // Draw the rectangle four times (you can adjust the number of repetitions)
            int repetitions = 4;

            for (int i = 0; i < repetitions; i++) {
                // Optional: Adjust the rectangle dimensions slightly for each pass if needed
                // For example, you can incrementally increase the depth or adjust the offset

                // Move to safe height
                gcodeInstructions.add(String.format("G0 Z%.3f", safeZ));

                // Rapid move to the starting point (first corner)
                gcodeInstructions.add(String.format("G0 X%.3f Y%.3f", p1.getX(), p1.getY()));

                // Plunge to cutting depth
                gcodeInstructions.add(String.format("G1 Z%.3f F%.1f", -depth, plungeFeedRate));

                // Cut along the rectangle perimeter
                gcodeInstructions.add(String.format("G1 X%.3f Y%.3f F%.1f", p2.getX(), p2.getY(), defaultFeedRate));
                gcodeInstructions.add(String.format("G1 X%.3f Y%.3f", p3.getX(), p3.getY()));
                gcodeInstructions.add(String.format("G1 X%.3f Y%.3f", p4.getX(), p4.getY()));
                gcodeInstructions.add(String.format("G1 X%.3f Y%.3f", p1.getX(), p1.getY()));

                // Retract to safe height
                gcodeInstructions.add(String.format("G0 Z%.3f", safeZ));
            }
        }

        // Return to origin
        gcodeInstructions.add("G0 X0 Y0 ; Return to origin");

        return gcodeInstructions;
    }*/

    public static double getCutLineEppaisseur(CutLine cutLine){
        if(cutLine.getCut() == null){
            return 1;
        }

        if(cutLine.getCut() instanceof Square){
            return ((Square) cutLine.getCut()).getTool().getEpaisseur();
        }
        else if(cutLine.getCut() instanceof L){
            return ((L) cutLine.getCut()).getTool().getEpaisseur();
        }
        else if(cutLine.getCut() instanceof HorizontalLine){
            return ((HorizontalLine) cutLine.getCut()).getTool().getEpaisseur();
        }
        else if(cutLine.getCut() instanceof VerticalLine){
            return ((VerticalLine) cutLine.getCut()).getTool().getEpaisseur();
        }
        else if(cutLine.getCut() instanceof Bordure){
            return ((Bordure) cutLine.getCut()).getTool().getEpaisseur();
        } else {
            return 0;
        }
    }

    public static double getCutLineProfondeur(CutLine cutLine){
        if(cutLine.getCut() instanceof Panel){
            return ((Panel) cutLine.getCut()).getEpaisseur();
        }
        if(cutLine.getCut() instanceof Square){
            return ((Square) cutLine.getCut()).getProfondeur();
        }
        else if(cutLine.getCut() instanceof L){
            return ((L) cutLine.getCut()).getProfondeur();
        }
        else if(cutLine.getCut() instanceof HorizontalLine){
            return ((HorizontalLine) cutLine.getCut()).getProfondeur();
        }
        else if(cutLine.getCut() instanceof VerticalLine){
            return ((VerticalLine) cutLine.getCut()).getProfondeur();
        }
        else if(cutLine.getCut() instanceof Bordure){
            return ((Bordure) cutLine.getCut()).getProfondeur();
        } else {
            return 0;
        }
    }

}