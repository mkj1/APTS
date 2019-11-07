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
    public Car(String id, int speed){
        this.ID = id;
        this.idle = true; 
        this.direction = "GATE";
        this.atGate = false;
        this.speed = speed;
    }
    
    public void TakeNewPassenger(Passenger p){
        ticksToGate = ((p.gate.Distance/speed)*60*60)/1000;
        ticksToEntrance = ((p.gate.Distance/speed)*60*60)/1000;
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
    private int speed;
    public int ticksToGate;
    public int ticksToEntrance;
    private String direction;
    private Passenger passenger;
    
}
