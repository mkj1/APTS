/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apts;

/**
 *
 * @author gxv
 */
public class Car {
    Car(String id, int speed){
        ID = id;
        idle = true; 
        direction = "GATE";
        atGate = false;       
    }
    
    public void TakeNewPassenger(Passenger p){
        ticksToGate = p.gate.Distance;
        ticksToEntrance = p.gate.Distance;
        passenger = p;
    }
    
    public void Update(){
        switch(direction){
            case "GATE":
                if(ticksToGate<1){
                    atGate = true;
                    direction = "ENTRANCE";
                }
                else{
                    ticksToGate--;
                }
                break;
                
            case "ENTRANCE":
                if(ticksToEntrance<1){
                    idle = true;
                    direction = "GATE";
                    
                }
                else{
                    ticksToEntrance--;
                }
                break;
                
        }
        

    }
    
    public Boolean atGate;
    public Boolean idle;
    private String ID;
    private int BatteryLevel;
    private int Speed;
    private int ticksToGate;
    private int ticksToEntrance;
    private String direction;
    private Passenger passenger;
    
}
