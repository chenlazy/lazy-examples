package com.delin.flink.entity;

import com.delin.flink.utils.DataGenerator;
import com.delin.flink.utils.GeoUtils;
import org.apache.flink.annotation.VisibleForTesting;
import org.apache.flink.streaming.runtime.streamrecord.StreamRecord;

import javax.annotation.Nullable;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * TaxiRide
 *
 * @author: chendl
 * @date: Created in 2023/8/7 17:53
 * @description: com.delin.flink.entity.TaxiRide
 */
public class TaxiRide implements Comparable<TaxiRide>, Serializable {

    /** Creates a new TaxiRide with now as start and end time. */
    public TaxiRide() {
        this.eventTime = Instant.now();
    }

    /** Invents a TaxiRide. */
    public TaxiRide(long rideId, boolean isStart) {
        DataGenerator g = new DataGenerator(rideId);

        this.rideId = rideId;
        this.isStart = isStart;
        this.eventTime = isStart ? g.startTime() : g.endTime();
        this.startLon = g.startLon();
        this.startLat = g.startLat();
        this.endLon = g.endLon();
        this.endLat = g.endLat();
        this.passengerCnt = g.passengerCnt();
        this.taxiId = g.taxiId();
        this.driverId = g.driverId();
    }

    /** Creates a TaxiRide with the given parameters. */
    public TaxiRide(
            long rideId,
            boolean isStart,
            Instant eventTime,
            float startLon,
            float startLat,
            float endLon,
            float endLat,
            long passengerCnt,
            long taxiId,
            long driverId) {
        this.rideId = rideId;
        this.isStart = isStart;
        this.eventTime = eventTime;
        this.startLon = startLon;
        this.startLat = startLat;
        this.endLon = endLon;
        this.endLat = endLat;
        this.passengerCnt = passengerCnt;
        this.taxiId = taxiId;
        this.driverId = driverId;
    }

    public long rideId;
    public boolean isStart;
    public Instant eventTime;
    public float startLon;
    public float startLat;
    public float endLon;
    public float endLat;
    public long passengerCnt;
    public long taxiId;
    public long driverId;

    @Override
    public String toString() {

        return rideId
                + ","
                + (isStart ? "START" : "END")
                + ","
                + eventTime.toString()
                + ","
                + startLon
                + ","
                + startLat
                + ","
                + endLon
                + ","
                + endLat
                + ","
                + passengerCnt
                + ","
                + taxiId
                + ","
                + driverId;
    }

    /**
     * Compares this TaxiRide with the given one.
     *
     * <ul>
     *   <li>sort by timestamp,
     *   <li>putting START events before END events if they have the same timestamp
     * </ul>
     */
    public int compareTo(@Nullable TaxiRide other) {
        if (other == null) {
            return 1;
        }
        int compareTimes = this.eventTime.compareTo(other.eventTime);
        if (compareTimes == 0) {
            if (this.isStart == other.isStart) {
                return 0;
            } else {
                if (this.isStart) {
                    return -1;
                } else {
                    return 1;
                }
            }
        } else {
            return compareTimes;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TaxiRide taxiRide = (TaxiRide) o;
        return rideId == taxiRide.rideId
                && isStart == taxiRide.isStart
                && Float.compare(taxiRide.startLon, startLon) == 0
                && Float.compare(taxiRide.startLat, startLat) == 0
                && Float.compare(taxiRide.endLon, endLon) == 0
                && Float.compare(taxiRide.endLat, endLat) == 0
                && passengerCnt == taxiRide.passengerCnt
                && taxiId == taxiRide.taxiId
                && driverId == taxiRide.driverId
                && Objects.equals(eventTime, taxiRide.eventTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                rideId,
                isStart,
                eventTime,
                startLon,
                startLat,
                endLon,
                endLat,
                passengerCnt,
                taxiId,
                driverId);
    }

    /** Gets the ride's time stamp as a long in millis since the epoch. */
    public long getEventTimeMillis() {
        return eventTime.toEpochMilli();
    }

    /** Gets the distance from the ride location to the given one. */
    public double getEuclideanDistance(double longitude, double latitude) {
        if (this.isStart) {
            return GeoUtils.getEuclideanDistance(
                    (float) longitude, (float) latitude, this.startLon, this.startLat);
        } else {
            return GeoUtils.getEuclideanDistance(
                    (float) longitude, (float) latitude, this.endLon, this.endLat);
        }
    }

    /** Creates a StreamRecord, using the ride and its timestamp. Used in tests. */
    @VisibleForTesting
    public StreamRecord<TaxiRide> asStreamRecord() {
        return new StreamRecord<>(this, this.getEventTimeMillis());
    }

    /** Creates a StreamRecord from this taxi ride, using its id and timestamp. Used in tests. */
    @VisibleForTesting
    public StreamRecord<Long> idAsStreamRecord() {
        return new StreamRecord<>(this.rideId, this.getEventTimeMillis());
    }

    public long getRideId() {
        return rideId;
    }

    public void setRideId(long rideId) {
        this.rideId = rideId;
    }

    public boolean isStart() {
        return isStart;
    }

    public void setStart(boolean start) {
        isStart = start;
    }

    public Instant getEventTime() {
        return eventTime;
    }

    public void setEventTime(Instant eventTime) {
        this.eventTime = eventTime;
    }

    public float getStartLon() {
        return startLon;
    }

    public void setStartLon(float startLon) {
        this.startLon = startLon;
    }

    public float getStartLat() {
        return startLat;
    }

    public void setStartLat(float startLat) {
        this.startLat = startLat;
    }

    public float getEndLon() {
        return endLon;
    }

    public void setEndLon(float endLon) {
        this.endLon = endLon;
    }

    public float getEndLat() {
        return endLat;
    }

    public void setEndLat(float endLat) {
        this.endLat = endLat;
    }

    public long getPassengerCnt() {
        return passengerCnt;
    }

    public void setPassengerCnt(long passengerCnt) {
        this.passengerCnt = passengerCnt;
    }

    public long getTaxiId() {
        return taxiId;
    }

    public void setTaxiId(long taxiId) {
        this.taxiId = taxiId;
    }

    public long getDriverId() {
        return driverId;
    }

    public void setDriverId(long driverId) {
        this.driverId = driverId;
    }
}
