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
    private String _GateID;
    private int _ArrivalTime;
    
    public Passenger(String ID, String GateID, int ArrivalTime){
        _ID = ID;
        _GateID = GateID;
        _ArrivalTime = ArrivalTime;
    }
}
