/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apts;

import java.util.LinkedList;

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
        
        gate1 = new Gate(500);
        passengerQueue = new LinkedList<Passenger>();
        vehicleQueue = new LinkedList<Car>();
        vehicleQueue.add(new Car("A", 10));
        
        for(int i = 0; i < 500; i++){
            Update();
        }
        
    }
    
    public static void Update(){
        if(tick%5==0){
           passengerQueue.add(new Passenger("Mike" + tick,"A",tick)); 
        }
        
        if(!vehicleQueue.isEmpty()){
            for(Car c : vehicleQueue){
            if(c.idle){
                passengerQueue.pop();
                c.idle = false;

            }
            break;
        }
            
            
        }
        
        tick++;
    }
    
}
