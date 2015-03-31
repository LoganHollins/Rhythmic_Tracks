package com.rhythmictracks.rhythmictracks;

/**
 * Created by Logan on 31/03/2015.
 */
public class RunStats {
    private int _id;
    private Double runMaxSpeed;
    private Double runAvgSpeed;
    private Double runDistance;
    private Double runTime;

    public RunStats(){

    }

    public RunStats(int id, Double mSpeed, Double aSpeed, Double rDistance, Double rTime){
        this._id = id;
        this.runMaxSpeed = mSpeed;
        this.runAvgSpeed = aSpeed;
        this.runDistance = rDistance;
        this.runTime = rTime;
    }
    public RunStats(Double mSpeed, Double aSpeed, Double rDistance, Double rTime){
        this.runMaxSpeed = mSpeed;
        this.runAvgSpeed = aSpeed;
        this.runDistance = rDistance;
        this.runTime = rTime;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public Double getRunMaxSpeed() {
        return runMaxSpeed;
    }

    public void setRunMaxSpeed(Double runMaxSpeed) {
        this.runMaxSpeed = runMaxSpeed;
    }

    public Double getRunAvgSpeed() {
        return runAvgSpeed;
    }

    public void setRunAvgSpeed(Double runAvgSpeed) {
        this.runAvgSpeed = runAvgSpeed;
    }

    public Double getRunDistance() {
        return runDistance;
    }

    public void setRunDistance(Double runDistance) {
        this.runDistance = runDistance;
    }

    public Double getRunTime() {
        return runTime;
    }

    public void setRunTime(Double runTime) {
        this.runTime = runTime;
    }

}
