package org.domain;

public class ConversionPiedMM {

    // Constante pour le facteur de conversion de pieds en millimètres
    private static final double FACTEUR_CONVERSION = 304.8;

    /**
     * Méthode pour convertir des pieds en millimètres.
     *
     * @param pieds La valeur en pieds à convertir.
     * @return La valeur convertie en millimètres.
     */
    public static double convertirPiedEnMillimetres(double pieds) {
        return pieds * FACTEUR_CONVERSION;
    }

    /**
     * Méthode pour convertir des millimètres en pieds.
     *
     * @param millimetres La valeur en millimètres à convertir.
     * @return La valeur convertie en pieds.
     */
    public static double convertirMillimetresEnPied(double millimetres) {
        return millimetres / FACTEUR_CONVERSION;
    }

    public static int convertirEnMillimetres(String valeur) {
        double nombre = Double.parseDouble(valeur.replaceAll("[^\\d.]", ""));
        if (valeur.endsWith("m")) {
            return (int) (nombre * 1000);
        } else if (valeur.endsWith("cm")) {
            return (int) (nombre * 10);
        } else if (valeur.endsWith("mm")) {
            return (int) nombre;
        } else if (valeur.endsWith("dm")) {
            return (int) (nombre * 100);
        } else if (valeur.endsWith("'")) {
            return (int) convertirPiedEnMillimetres(nombre);
        } else {
            throw new IllegalArgumentException("Unité inconnue : " + valeur);
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

    public static double GridUnitEnMillimetre(int graphUnit) {
        return (graphUnit / 30) * FACTEUR_CONVERSION;
    }

    public static double MillimetreEnGridUnit(double mm) {
        return (mm / FACTEUR_CONVERSION) * 30;
    }
}
