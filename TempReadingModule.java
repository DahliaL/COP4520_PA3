/*
*
* You are tasked with the design of the module responsible for measuring the atmospheric
* temperature of the next generation Mars Rover, equipped with a multi-core CPU and 8 temperature
* sensors. The sensors are responsible for collecting temperature readings at regular intervals and
* storing them in shared memory space. The atmospheric temperature module has to compile a report at
* the end of every hour, comprising the top 5 highest temperatures recorded for that hour, the top 5
* lowest temperatures recorded for that hour, and the 10-minute interval of time when the largest temperature
*  difference was observed. The data storage and retrieval of the shared memory region must be carefully handled,
* as we do not want to delay a sensor and miss the interval of time when it is supposed to conduct temperature reading.

Design and implement a solution using 8 threads that will offer a solution for this task. Assume that the temperature
* readings are taken every 1 minute. In your solution, simulate the operation of the temperature reading sensor by
* generating a random number from -100F to 70F at every reading. In your report, discuss the efficiency, correctness,
* and progress guarantee of your program.
*
* */
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class TempReadingModule {
    private static volatile int curr;
    private static int[] temps;
    private static int[] reportReady;
    private static int currentHour;
    private static int maxDiff = Integer.MIN_VALUE;
    private static int indexS = 0;
    private static int indexE = 0;

    public static void generateReport() {
        System.out.println("---------- Report for Hour " + currentHour + " -------------");
        biggestTempDiff();
        Arrays.sort(reportReady);
        System.out.println("Top five highest recorded temperatures: ");
        for(int i = reportReady.length-1; i > reportReady.length-7; i--) {
            System.out.print(reportReady[i] + " ");
        }
        System.out.println("\nTop five lowest recorded temperatures: ");
        for(int i = 0; i < 6; i++) {
            System.out.print(reportReady[i] + " ");
        }
        System.out.print("\n");
        System.out.print("\n");
        System.out.print("\n");
    }

    public static class sensorThread implements Runnable {
        @Override
        public synchronized void run() {
            if(curr == 60) {
                reportReady = Arrays.copyOf(temps, 60);
                generateReport();
                curr = 0;
            }
            temps[curr] = generateRand();
            curr++;
        }
    }

    public static int generateRand() {
        return ThreadLocalRandom.current().nextInt(-100, 71);
    }

    public static void biggestTempDiff() {
        maxDiff = Integer.MIN_VALUE;
        indexS = 0;
        indexE = 0;

        // must be 10 min intervals
        for(int i = 0; i < reportReady.length; i++) {
            int j = i+10;
            if(j >= reportReady.length)
                break;

            if(Math.abs(reportReady[i] - reportReady[j]) > maxDiff) {
                maxDiff = Math.abs(reportReady[i] - reportReady[j]);
                indexS = i;
                indexE = j;
            }
        }

        System.out.println("10-minute interval of time when the largest temperature difference was observed was between " + indexS + " and " + indexE + " with a difference of " + maxDiff);
        maxDiff = Integer.MIN_VALUE;
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("How many hours would you like to print for?");
        Scanner sc = new Scanner(System.in);
        int hours = sc.nextInt();
        hours++;
        temps = new int[60];
        int[] arrHolder;

        // the 8 sensors
        Thread[] sensors = new Thread[8];
        sensorThread sensorThread = new sensorThread();
        System.out.print("\n Starting the temperature module!\n\n");
        currentHour = 0;
        while( currentHour < hours ) {
            int k = 0; // simulating the minutes in an hour

            while( k < 60) {
                try {
                    sensors[k%sensors.length].join();
                } catch (NullPointerException | InterruptedException ignored) {
                }

                sensors[k% sensors.length] = new Thread(sensorThread);
                sensors[k% sensors.length].start();

                k++;
            }

            currentHour++;
        }

    }
}
