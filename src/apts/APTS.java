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

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        passengerQueue = new LinkedList<Passenger>();
        vehicleQueue = new LinkedList<Car>();
        
        for(int i = 0; i < 500; i++){
            Update();
        }
        
    }
    
    public static void Update(){
        if(tick%5==0){
           passengerQueue.add(new Passenger("Mike" + tick,"A",tick)); 
        }
        
        tick++;
    }
    
}
