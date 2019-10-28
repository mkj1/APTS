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
public class Passenger {
    private String _ID;
    public Gate gate;
    private int _ArrivalTime;
    
    public Passenger(String ID, Gate g, int ArrivalTime){
        _ID = ID;
        gate = g;
        _ArrivalTime = ArrivalTime;
    }
}
