package com.delin.flink.entity;

import com.delin.flink.utils.DataGenerator;
import org.apache.flink.annotation.VisibleForTesting;
import org.apache.flink.streaming.runtime.streamrecord.StreamRecord;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * TaxiFare
 *
 * @author: chendl
 * @date: Created in 2023/8/7 18:00
 * @description: com.delin.flink.entity.TaxiFare
 */
public class TaxiFare implements Serializable {

    /** Creates a TaxiFare with now as the start time. */
    public TaxiFare() {
        this.startTime = Instant.now();
    }

    /** Invents a TaxiFare. */
    public TaxiFare(long rideId) {
        DataGenerator g = new DataGenerator(rideId);

        this.rideId = rideId;
        this.taxiId = g.taxiId();
        this.driverId = g.driverId();
        this.startTime = g.startTime();
        this.paymentType = g.paymentType();
        this.tip = g.tip();
        this.tolls = g.tolls();
        this.totalFare = g.totalFare();
    }

    /**
     * 每次车程的唯一id
     */
    public long rideId;

    /**
     * 每一辆出租车的唯一id
     */
    public long taxiId;

    /**
     * 每一位司机的唯一id
     */
    public long driverId;

    /**
     * 车程开始时间
     */
    public Instant startTime;

    /**
     * 现金(CASH)或刷卡(CARD)
     */
    public String paymentType;

    /**
     * 小费
     */
    public float tip;

    /**
     * 过路费
     */
    public float tolls;

    /**
     * 总计车费
     */
    public float totalFare;

    @Override
    public String toString() {

        return rideId
                + ","
                + taxiId
                + ","
                + driverId
                + ","
                + startTime.toString()
                + ","
                + paymentType
                + ","
                + tip
                + ","
                + tolls
                + ","
                + totalFare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TaxiFare taxiFare = (TaxiFare) o;
        return rideId == taxiFare.rideId
                && taxiId == taxiFare.taxiId
                && driverId == taxiFare.driverId
                && Float.compare(taxiFare.tip, tip) == 0
                && Float.compare(taxiFare.tolls, tolls) == 0
                && Float.compare(taxiFare.totalFare, totalFare) == 0
                && Objects.equals(startTime, taxiFare.startTime)
                && Objects.equals(paymentType, taxiFare.paymentType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                rideId, taxiId, driverId, startTime, paymentType, tip, tolls, totalFare);
    }

    /** Gets the fare's start time. */
    public long getEventTimeMillis() {
        return startTime.toEpochMilli();
    }

    /** Creates a StreamRecord, using the fare and its timestamp. Used in tests. */
    @VisibleForTesting
    public StreamRecord<TaxiFare> asStreamRecord() {
        return new StreamRecord<>(this, this.getEventTimeMillis());
    }

    public long getRideId() {
        return rideId;
    }

    public void setRideId(long rideId) {
        this.rideId = rideId;
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

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public float getTip() {
        return tip;
    }

    public void setTip(float tip) {
        this.tip = tip;
    }

    public float getTolls() {
        return tolls;
    }

    public void setTolls(float tolls) {
        this.tolls = tolls;
    }

    public float getTotalFare() {
        return totalFare;
    }

    public void setTotalFare(float totalFare) {
        this.totalFare = totalFare;
    }
}
