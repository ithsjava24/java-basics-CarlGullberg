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
        int maxPris = Arrays.stream(elpriser).max().orElse(1);  // Se till att maxpris inte är 0
        int scale = Math.max(1, maxPris / 50);  // Bestäm en skala baserad på maxpris

        for (int i = 0; i < elpriser.length; i++) {
            int antalX = elpriser[i] / scale;
            System.out.printf("%02d-%02d | %s (%d öre)%n", i, i + 1, "x".repeat(antalX), elpriser[i]);
        }
    }

    private static String formatTime(int hour) {
        return String.format("%02d-%02d", hour, hour + 1);
    }
}
