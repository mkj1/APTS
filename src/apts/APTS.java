/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apts;

import java.util.LinkedList;
import org.knowm.xchart.*;


/**
 *
 * @author gxv
 */
public class APTS {
    static int tick = 0;
    static LinkedList<Passenger> passengerQueue;
    static LinkedList<Car> vehicleQueue;
    static Gate gate1;
    
    static LinkedList<Passenger> arrivedPassengers;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        final int duration = 300;               
        Initialize();
        
        // Arrays for chart
        double[] xData = new double[duration];
        double[] yData = new double[duration];
        
        for(int i = 0; i < duration; i++){
            Update();
            xData[i] = i;
            yData[i] = passengerQueue.size();
        }
        


        // Create Chart
        XYChart chart = QuickChart.getChart("APTS", "minutes", "queue length", "y(x)", xData, yData);

        // Show it
        new SwingWrapper(chart).displayChart();    
    }
    
    
    public static void Initialize(){
        gate1 = new Gate(12);
        passengerQueue = new LinkedList<Passenger>();
        vehicleQueue = new LinkedList<Car>();
        vehicleQueue.add(new Car("A", 10));
        vehicleQueue.add(new Car("B", 10));
        vehicleQueue.add(new Car("C", 10));
        vehicleQueue.add(new Car("D", 10));
        vehicleQueue.add(new Car("E", 10));
        vehicleQueue.add(new Car("F", 10));
        
        for(int i = 0; i<10; i++){
            passengerQueue.add(new Passenger("P" + tick + i, gate1,tick));
        }
        
    }
    
    public static void Update(){
        if(tick%4==0){
           passengerQueue.add(new Passenger("P" + tick, gate1,tick));
        }
        
        if(!vehicleQueue.isEmpty()){
            for(Car c : vehicleQueue){
                if(c.idle==true && !passengerQueue.isEmpty()){
                    c.idle = false;
                    c.TakeNewPassenger(passengerQueue.pop());

                }
                c.Update(); 
            }
        }
        
        tick++;
    }    
}
