package domain;

import exceptions.InvalidDataException;

import java.util.Objects;

public class DateTime extends Date{
    private int hour;
    private int minutes;

    public DateTime(int day, int month, int year, int hour, int minutes) throws InvalidDataException {
        super(day, month, year);
        if (hour < 0 || hour > 23 || minutes < 0 || minutes > 59)
            throw new InvalidDataException("invalid time");
        this.hour = hour;
        this.minutes = minutes;
    }

    public DateTime() {
    }

    public DateTime(DateTime dateTime) {
        if (dateTime != null) {
            this.day = dateTime.day;
            this.month = dateTime.month;
            this.year = dateTime.year;
            this.hour = dateTime.hour;
            this.minutes = dateTime.minutes;
        }
    }

    @Override
    public String toString() {
        if (minutes < 10 && hour > 10)
            return super.toString() + ", time = " + hour + ":0" + minutes;
        if (minutes > 10 && hour < 10)
            return super.toString() + ", time = 0" + hour + ":" + minutes;
        if(minutes < 10 && hour < 10)
            return super.toString() + ", time = 0" + hour + ":0" + minutes;
//        return super.toString() + ", time = " + hour + ":" + minutes;
        return super.toString() + " " + hour + ":" + minutes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DateTime dateTime = (DateTime) o;
        return hour == dateTime.hour && minutes == dateTime.minutes;
    }

    public int compareTo(DateTime dateTime) {
        Date date = (Date)this;
        int result = date.compareTo((Date) dateTime);
        if (result == 0) {
            if (this.hour == dateTime.hour && this.minutes == dateTime.minutes) return 0;
            if (this.hour > dateTime.hour || (this.hour == dateTime.hour && this.minutes > dateTime.minutes)) return 1;
            return -1;
        }
        return result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), hour, minutes);
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }
}
