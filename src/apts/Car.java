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
        Speed = speed;
        idle = true;       
    }
    
    public void SetPassenger(Passenger p){
        _passenger = p;
    }
    
    public Passenger GetPassenger(Passenger p){
        return _passenger;
    }
    
    public Boolean idle;
    private Passenger _passenger;
    private String ID;
    private int BatteryLevel;
    private int Speed;
}
