package com.biin.biin.Utils;

/**
 * Created by ramirezallan on 7/26/16.
 */
public class BeaconSort implements Comparable<BeaconSort> {

    private int position;
    private double distance;

    public BeaconSort(int position, double distance) {
        this.position = position;
        this.distance = distance;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public int compareTo(BeaconSort f) {

        if (distance > f.distance) {
            return 1;
        }
        else if (distance <  f.distance) {
            return -1;
        }
        else {
            return 0;
        }

    }

    @Override
    public String toString(){
        return String.valueOf(this.position);
    }
}
