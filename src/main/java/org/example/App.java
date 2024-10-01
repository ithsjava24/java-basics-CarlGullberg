package org.example;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

public class App {
    private static final int HOURS_IN_DAY = 24;
    private static int[] elpriser = new int[HOURS_IN_DAY];

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            // Skriv ut menyn
            printMenu();

            // Läs användarens val
            String choice = scanner.next();

            switch (choice.toLowerCase()) {
                case "1":
                    // Inmatning av elpriser
                    inputElpriser(scanner);
                    break;
                case "2":
                    // Min, Max och Medel
                    printMinMaxMedel();
                    break;
                case "3":
                    // Sortera och skriv ut
                    sortAndPrintPrices();
                    break;
                case "4":
                    // Bästa laddningstid (4h)
                    cheapest4Hours();
                    break;
                case "5":
                    // Visualisering
                    visualizePrices();
                    break;
                case "e":
                    // Avsluta programmet
                    exit = true;
                    break;
                default:
                    System.out.println("Ogiltigt val, försök igen.");
                    break;
            }
        }

        scanner.close();
        System.out.println("Programmet avslutas.");
    }

    private static void printMenu() {
        System.out.println("""
                Elpriser
                ========
                1. Inmatning
                2. Min, Max och Medel
                3. Sortera
                4. Bästa Laddningstid (4h)
                5. Visualisering
                e. Avsluta
                """);

    }

    private static void inputElpriser(Scanner scanner) {
        for (int i = 0; i < HOURS_IN_DAY; i++) {
            System.out.print("Ange elpris (i öre) för timme " + i + ": ");
            elpriser[i] = scanner.nextInt();
        }
    }

    private static void printMinMaxMedel() {
        int minPris = Integer.MAX_VALUE;
        int maxPris = Integer.MIN_VALUE;
        int minTimme = 0;
        int maxTimme = 0;
        int totalPris = 0;

        for (int i = 0; i < elpriser.length; i++) {
            int pris = elpriser[i];
            totalPris += pris;
            if (pris < minPris) {
                minPris = pris;
                minTimme = i;
            }
            if (pris > maxPris) {
                maxPris = pris;
                maxTimme = i;
            }
        }


        Locale.setDefault(new Locale("sv", "SE"));

        double medelPris = (double) totalPris / HOURS_IN_DAY;

        System.out.print("Lägsta pris: " + formatTime(minTimme) +", "+ minPris + " öre/kWh\n");
        System.out.print("Högsta pris: " + formatTime(maxTimme)+ ", " +maxPris+" öre/kWh\n");
        System.out.printf("Medelpris: %.2f öre/kWh\n", medelPris);

    }

    private static void sortAndPrintPrices() {

        int[][] sortedPrices = new int[HOURS_IN_DAY][2];

        for (int i = 0; i < HOURS_IN_DAY; i++) {
            sortedPrices[i][0] = i;
            sortedPrices[i][1] = elpriser[i];
        }

        Arrays.sort(sortedPrices, (a, b) -> Integer.compare(b[1], a[1]));

        System.out.print("Timmar sorterade från dyrast till billigast:\n");
        for (int[] hourPrice : sortedPrices) {
            System.out.print(formatTime(hourPrice[0]) + " " + hourPrice[1] + " öre\n");
        }
    }

    private static void cheapest4Hours() {
        Locale.setDefault(new Locale("sv", "SE"));

        int minSum = Integer.MAX_VALUE;
        int bestStartHour = 0;

        // Loop through all possible 4-hour periods
        for (int i = 0; i <= HOURS_IN_DAY - 4; i++) {
            int sum = 0;
            for (int j = i; j < i + 4; j++) {
                sum += elpriser[j];
            }
            // Find the period with the lowest total price
            if (sum < minSum) {
                minSum = sum;
                bestStartHour = i;
            }
        }

        // Calculate the average price
        double medelPris = (double) minSum / 4;

        // Round the average price to 1 decimal
        medelPris = Math.round(medelPris * 10.0) / 10.0;

        // Print the best start hour and the average price with the specified format

        System.out.print("Påbörja laddning klockan " + bestStartHour+"\n");
        System.out.printf("Medelpris 4h: %.1f öre/kWh\n", medelPris);

    }



    private static void visualizePrices() {
         int maxPris = Arrays.stream(elpriser).max().orElse(1);
        int minPris = Arrays.stream(elpriser).min().orElse(1);
        final int HEIGHT = 6;
        final int COLUMN_COUNT = elpriser.length;
        final float DIFFERENCE = (maxPris - minPris) / (HEIGHT - 1f);

        System.out.println("Visualisering av elpriser: \n");


        for (int i = HEIGHT; i > 0; i--) {
            StringBuilder output = new StringBuilder();
            int lowerBound = (i == 1) ? minPris : (int) (maxPris - (HEIGHT - i) * DIFFERENCE);

            int maxLength = Integer.toString(maxPris).length();
            int minLength = Integer.toString(minPris).length();
            int longest = Math.max(maxLength, minLength);


            if (i == HEIGHT) {
                String spaces = maxLength < longest ? addSpaces(longest - maxLength) : "";
                output.append(spaces).append(maxPris).append("|");
            } else if (i == 1) {
                String spaces = minLength < longest ? addSpaces(longest - minLength) : "";
                output.append(spaces).append(minPris).append("|");
            } else {
                output.append(addSpaces(longest)).append("|");
            }


            for (int j = 0; j < COLUMN_COUNT; j++) {
                int currentPrice = elpriser[j];
                if (currentPrice >= lowerBound) {
                    output.append("  x");
                } else {
                    output.append("   ");
                }
            }
            System.out.println(output);
        }


        System.out.print("   |------------------------------------------------------------------------\n");
        System.out.print("   | 00 01 02 03 04 05 06 07 08 09 10 11 12 13 14 15 16 17 18 19 20 21 22 23\n");
    }


    private static String addSpaces(int count) {
        return " ".repeat(count);
    }




    private static String formatTime(int hour) {
        return String.format("%02d-%02d", hour, hour + 1);
    }
}
