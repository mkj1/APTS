/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apts;

import java.util.LinkedList;
import java.util.Random;
import org.knowm.xchart.*;

/**
 *
 * @author gxv
 */
public class APTS {

    static int tick = 0;
    static LinkedList<Passenger> passengerQueue;
    static LinkedList<Car> vehicleQueue;

    static LinkedList<Passenger> arrivedPassengers;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        final int duration = 300 * 60;
        Initialize();

        // Arrays for chart
        double[] xData = new double[duration];
        double[] yData = new double[duration];

        for (int i = 0; i < duration; i++) {            
            xData[i] = i;
            yData[i] = passengerQueue.size();
            Update();
        }

        // Create Chart
        XYChart chart = QuickChart.getChart("APTS", "seconds", "queue length", "queue length", xData, yData);

        // Show it
        new SwingWrapper(chart).displayChart();
    }

    public static void Initialize() {

        passengerQueue = new LinkedList<Passenger>();
        vehicleQueue = new LinkedList<Car>();
        vehicleQueue.add(new Car("a", 6));
        vehicleQueue.add(new Car("a", 7));
        vehicleQueue.add(new Car("a", 8));

        for (int i = 0; i < 10; i++) {
            passengerQueue.add(new Passenger("P" + tick + i, GetRandomGate(), tick));
        }

    }

    public static Gate GetRandomGate() {
        Random rand = new Random();
        int n = rand.nextInt(5);
        Gate gate = null;

        switch (n) {
            case 0:
                gate = new Gate(720, "A");
                break;
            case 1:
                gate = new Gate(660, "B");
                break;
            case 2:
                gate = new Gate(690, "C");
                break;
            case 3:
                gate = new Gate(320, "D");
                break;
            case 4:
                gate = new Gate(760, "F");
                break;
        }

        return gate;
    }

    public static void Update() {

        Random rand = new Random();
        int n = rand.nextInt(2);
        n += 1;

        if (tick % 208 == 0 && tick != 0) {
            passengerQueue.add(new Passenger("P" + tick, GetRandomGate(), tick));
        }

        if (!vehicleQueue.isEmpty()) {
            for (Car c : vehicleQueue) {
                if (c.idle == true && !passengerQueue.isEmpty()) {
                    c.idle = false;
                    c.TakeNewPassenger(passengerQueue.pop());
                }
                c.Update();
            }
        }

        tick++;
    }
}
