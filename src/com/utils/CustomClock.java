package com.utils;

import java.time.*;


public class CustomClock extends Clock {
    private ZonedDateTime dateTime;
    private Double tickSpeed;
    private int hoursOffset = 0;
    private int minutesOffset = 0;

    public CustomClock() {
       dateTime = ZonedDateTime.now();
       tickSpeed = 1.0;
    }

    public CustomClock(String timeZone) {
        tickSpeed = 1.0;
        try {
            dateTime = ZonedDateTime.now(ZoneId.of(timeZone));
        } catch (DateTimeException ex) {
            // Zone not found... creating with local zone
            dateTime = ZonedDateTime.now();
        }
    }

    @Override
    public ZoneId getZone() {
        return dateTime.getZone();
    }

    @Override
    public Clock withZone(ZoneId zone) {
        return null;
    }

    @Override
    public Instant instant() {
        return dateTime.toInstant();
    }

    public void setTickSpeed(Double speed) {
        this.tickSpeed = speed;
    }

    public void plusHour() {
        this.hoursOffset++;
    }

    public void minusHour() {
        this.hoursOffset--;
    }

    public void plusMinute() {
        this.minutesOffset++;
    }

    public void minusMinute() {
        this.minutesOffset--;
    }

    public LocalTime getTime() {
        ZonedDateTime now = ZonedDateTime.now();
        long nowSeconds = now.toInstant().getEpochSecond();
        long beginSeconds = this.dateTime.toInstant().getEpochSecond();

        // Difference between the starting point of this clock and 'now'
        long delta = nowSeconds - beginSeconds;

        ZonedDateTime newDateTime = this.dateTime.plusHours(hoursOffset).plusMinutes(minutesOffset).plusSeconds(Math.round(delta * tickSpeed));

        return LocalTime.of(newDateTime.getHour(), newDateTime.getMinute(), newDateTime.getSecond());
    }
}
