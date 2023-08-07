package com.delin.flink.entity;

import java.io.Serializable;

/**
 * RideAndFare
 *
 * @author: chendl
 * @date: Created in 2023/8/7 18:01
 * @description: com.delin.flink.entity.RideAndFare
 */
public class RideAndFare implements Serializable {

    public TaxiRide ride;
    public TaxiFare fare;

    /** Default constructor. */
    public RideAndFare() {}

    /** Create a RideAndFare from the ride and fare provided. */
    public RideAndFare(TaxiRide ride, TaxiFare fare) {
        this.ride = ride;
        this.fare = fare;
    }

    @Override
    public String toString() {
        return "<" + ride.toString() + " / " + fare.toString() + ">";
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof RideAndFare)) {
            return false;
        }

        RideAndFare otherRandF = (RideAndFare) other;
        return this.ride.equals(otherRandF.ride) && this.fare.equals(otherRandF.fare);
    }
}
