//package org.equipe39.domain;
//
//public class ConversionPiedMM {
//
//    // Constante pour le facteur de conversion de pieds en millimètres
//    public static final double FACTEUR_CONVERSION = 304.8;
//
//    public static double convertirPiedEnMillimetres(double pieds) {
//        return pieds * FACTEUR_CONVERSION;
//    }
//
//    public static double convertirMillimetresEnPied(double millimetres) {
//        return millimetres / FACTEUR_CONVERSION;
//    }
//
//
//    public static double convertirEnMillimetres(String valeur) {
//        valeur = convertirFractionEnDecimal(valeur);
//        System.out.println("valeur" + valeur);
//        double nombre = Double.parseDouble(valeur.replaceAll("[^\\d.]", ""));
//        //Math.round(adjustedWidth * 1000.0) / 1000.0;
//        System.out.println(valeur);
//        System.out.println("nombre " + nombre);
//        if (valeur.matches("[0-9]+(\\.[0-9]+)?")) {
//            return (double) nombre;
//        } else
//        {
//            if (valeur.endsWith("mm")) {
//                System.out.println("convert to mm");
//                return ((double) nombre);
//            } else if (valeur.endsWith("m")) {
//                System.out.println("convert from m");
//                return (double) (nombre * 1000);
//            } else if (valeur.endsWith("cm")) {
//                System.out.println("convert from cm ");
//                return ((double) (nombre * 10));
//            } else if (valeur.endsWith("dm")) {
//                System.out.println("convert from dm");
//                return (double) (nombre * 100);
//            } else if (valeur.endsWith("ft") || valeur.endsWith("pi") || valeur.endsWith("\'")) {
//                System.out.println("convert from foot");
//                return (double) (nombre * FACTEUR_CONVERSION);
//            } else if (valeur.endsWith("\"") || valeur.endsWith("po") || valeur.endsWith("in")) {
//                return Math.round((nombre * 25.4) * 1000.0) / 1000.0;
//            //if contains only numbers and no unit, then it is in mm
//
//            //} else if (valeur.endsWith("")) {
//            //    return Math.round(((double) convertirPiedEnMillimetres(nombre)) * 1000.0) / 1000.0;
//            } else {
//                throw new IllegalArgumentException("Unité inconnue : " + valeur);
//            }
//        }
//    }
//
//    public static double convertirMillimetresVers(double millimetres, String unite) {
//        //Math.round(adjustedWidth * 1000.0) / 1000.0;
//        switch (unite) {
//            case "m":
//                return millimetres / 1000;
//            case "cm":
//                return millimetres / 10;
//            case "mm":
//                return millimetres;
//            case "dm":
//                return millimetres / 100;
//            case "'":
//                return convertirMillimetresEnPied(millimetres);
//            default:
//                throw new IllegalArgumentException("Unité inconnue : " + unite);
//        }
//    }
//
//
//    public static String convertirFractionEnDecimal(String valeur) {
//        if (valeur.matches(".*\\d+/\\d+.*")) {
//            String[] parts = valeur.split("[^\\d./]+");
//            for (String part : parts) {
//                if (part.contains("/")) {
//                    String[] fraction = part.split("/");
//                    if (fraction.length == 2) {
//                        try {
//                            double numerator = Double.parseDouble(fraction[0]);
//                            double denominator = Double.parseDouble(fraction[1]);
//
//                            // Check for divide by zero
//                            if (denominator == 0) {
//                                throw new ArithmeticException("Cannot divide by zero in fraction: " + part);
//                            }
//
//
//                            // Calculate decimal value and replace fraction with decimal
//                            double decimalValue = numerator / denominator;
//                            valeur = valeur.replace(part, String.valueOf(decimalValue));
//                        } catch (NumberFormatException e) {
//                            // Handle the case where the fraction is not a valid number
//                            throw new IllegalArgumentException("Invalid fraction: " + part, e);
//                        } catch (ArithmeticException e) {
//                            // Handle divide by zero error
//                            throw new IllegalArgumentException("Divide by zero error in fraction: " + part, e);
//                        }
//                    }
//                }
//            }
//        }
//        return valeur;
//    }
//
//    //return in mm string (takes a double and turns it into a string that has mm at the end)
//    public static String convertirEnMillimetresString(double valeur) {
//        return valeur + "mm";
//    }
//
//
//    public static double PiedEnMillimetre(int graphUnit) {
//        return (graphUnit) * FACTEUR_CONVERSION;
//    }
//
//    public static double MillimetreEnPied(double mm) {
//        return (mm / FACTEUR_CONVERSION);
//    }
//}
package org.equipe39.domain;

public class ConversionPiedMM {

    // Constante pour le facteur de conversion de pieds en millimètres
    public static final double FACTEUR_CONVERSION = 304.8;

    public static double convertirPiedEnMillimetres(double pieds) {
        return pieds * FACTEUR_CONVERSION;
    }

    public static double convertirMillimetresEnPied(double millimetres) {
        return millimetres / FACTEUR_CONVERSION;
    }

    public static double convertirEnMillimetres(String valeur) {
        valeur = valeur.trim();

        // D'abord, on gère le cas des entrées de type 5"1/2 ou 3'3/4
        // On détecte la présence de ' ou " dans la chaîne.
        if (valeur.matches(".*\\d+'\\d*(/\\d+)?")) {
            // On a une valeur en pieds avec une partie fractionnaire potentielle, ex: 3'3/4
            return convertirMesurePied(valeur);
        } else if (valeur.matches(".*\\d+\"\\d*(/\\d+)?")) {
            // On a une valeur en pouces avec une partie fractionnaire potentielle, ex: 5\"1/2
            return convertirMesurePouce(valeur);
        } else {
            // Sinon, on applique l'ancienne logique.
            valeur = convertirFractionEnDecimal(valeur);

            double nombre = Double.parseDouble(valeur.replaceAll("[^\\d.]", ""));
            if (valeur.matches("[0-9]+(\\.[0-9]+)?")) {
                // Pas d'unité spécifiée, on considère que c'est en mm
                return nombre;
            } else {
                if (valeur.endsWith("mm")) {
                    return nombre;
                } else if (valeur.endsWith("cm")) {
                    return ((double) (nombre * 10));
                } else if (valeur.endsWith("dm")) {
                    return (double) (nombre * 100);
                } else if (valeur.endsWith("m")) {
                    return (double) (nombre * 1000);
                } else if (valeur.endsWith("ft") || valeur.endsWith("pi") || valeur.endsWith("'")) {
                    return (double) (nombre * FACTEUR_CONVERSION);
                } else if (valeur.endsWith("\"") || valeur.endsWith("po") || valeur.endsWith("in")) {
                    return Math.round((nombre * 25.4) * 1000.0) / 1000.0;
                } else {
                    throw new IllegalArgumentException("Unité inconnue : " + valeur);
                }
            }
        }
    }

    private static double convertirMesurePied(String valeur) {
        // Forme attendue: X'Y/Z ou X'Y ou juste X'
        // On sépare par le symbole '
        String[] parts = valeur.split("'");
        if (parts.length == 0) {
            throw new IllegalArgumentException("Format invalide pour la valeur en pieds: " + valeur);
        }

        // Partie pied entier
        double pieds = 0.0;
        try {
            pieds = Double.parseDouble(parts[0].replaceAll("[^\\d.]", ""));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Partie pieds invalide: " + parts[0]);
        }

        // Partie fractionnaire éventuelle
        double fraction = 0.0;
        if (parts.length > 1 && !parts[1].isEmpty()) {
            // par exemple '3/4'
            fraction = fractionToDecimal(parts[1]);
        }

        double totalPieds = pieds + fraction;
        return convertirPiedEnMillimetres(totalPieds);
    }

    private static double convertirMesurePouce(String valeur) {
        // Forme attendue: X"Y/Z ou X"Y ou juste X"
        // On sépare par le symbole "
        String[] parts = valeur.split("\"");
        if (parts.length == 0) {
            throw new IllegalArgumentException("Format invalide pour la valeur en pouces: " + valeur);
        }

        // Partie pouce entier
        double pouces = 0.0;
        try {
            pouces = Double.parseDouble(parts[0].replaceAll("[^\\d.]", ""));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Partie pouces invalide: " + parts[0]);
        }

        // Partie fractionnaire éventuelle
        double fraction = 0.0;
        if (parts.length > 1 && !parts[1].isEmpty()) {
            fraction = fractionToDecimal(parts[1]);
        }

        double totalPouces = pouces + fraction;
        // Conversion pouces en mm
        return Math.round((totalPouces * 25.4) * 1000.0) / 1000.0;
    }

    private static double fractionToDecimal(String fractionPart) {
        // Nettoyage, par ex: "3/4"
        fractionPart = fractionPart.trim();
        if (fractionPart.isEmpty()) {
            return 0.0;
        }
        String[] fraction = fractionPart.split("/");
        if (fraction.length == 2) {
            double num = Double.parseDouble(fraction[0]);
            double den = Double.parseDouble(fraction[1]);
            if (den == 0) {
                throw new ArithmeticException("Division par zéro dans la fraction: " + fractionPart);
            }
            return num / den;
        } else {
            // Pas de fraction, juste un entier. Ex: "3"
            return Double.parseDouble(fractionPart.replaceAll("[^\\d.]", ""));
        }
    }

    public static double convertirMillimetresVers(double millimetres, String unite) {
        switch (unite) {
            case "m":
                return millimetres / 1000;
            case "cm":
                return millimetres / 10;
            case "mm":
                return millimetres;
            case "dm":
                return millimetres / 100;
            case "'":
                return convertirMillimetresEnPied(millimetres);
            default:
                throw new IllegalArgumentException("Unité inconnue : " + unite);
        }
    }

    public static String convertirFractionEnDecimal(String valeur) {
        if (valeur.matches(".*\\d+/\\d+.*")) {
            String[] parts = valeur.split("[^\\d./]+");
            for (String part : parts) {
                if (part.contains("/")) {
                    String[] fraction = part.split("/");
                    if (fraction.length == 2) {
                        try {
                            double numerator = Double.parseDouble(fraction[0]);
                            double denominator = Double.parseDouble(fraction[1]);

                            if (denominator == 0) {
                                throw new ArithmeticException("Cannot divide by zero in fraction: " + part);
                            }

                            double decimalValue = numerator / denominator;
                            valeur = valeur.replace(part, String.valueOf(decimalValue));
                        } catch (NumberFormatException e) {
                            throw new IllegalArgumentException("Invalid fraction: " + part, e);
                        } catch (ArithmeticException e) {
                            throw new IllegalArgumentException("Divide by zero error in fraction: " + part, e);
                        }
                    }
                }
            }
        }
        return valeur;
    }

    public static String convertirEnMillimetresString(double valeur) {
        return valeur + "mm";
    }

    public static double PiedEnMillimetre(int graphUnit) {
        return (graphUnit) * FACTEUR_CONVERSION;
    }

    public static double MillimetreEnPied(double mm) {
        return (mm / FACTEUR_CONVERSION);
    }
}

