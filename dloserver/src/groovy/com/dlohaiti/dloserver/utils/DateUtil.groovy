package com.dlohaiti.dloserver.utils

import org.joda.time.LocalDate

class DateUtil {

    public static Date oneWeekAgoMidnight() {
        return new LocalDate().minusWeeks(1).toDateMidnight().toDate()
    }

    public static List<LocalDate> previousWeek() {
        def days = []
        for (int i = 6; i >= 0; i--) {
            days.add(new LocalDate().minusDays(i))
        }
        return days
    }

    /**
     * Processes the given week and returns the week dates in {@List} of {@LocalDate} objects.
     * @param fromDate
     *              the Start Date
     * @param toDate
     *              the End Date
     * @return Week dates in {@List} of {@LocalDate} objects
     */
    public static List<LocalDate> getWeekDataByFromDate(LocalDate fromDate, LocalDate toDate) {
        def days = []
        for (int i = 0; i <= 6; i++) {
            days.add(fromDate.plusDays(i))
        }
        return days
    }

    /**
     * Processes the given week and returns the from Date for the given week.
     * If no Week matches It returns the current date.
     *
     * @param weekString The Week String either <b> currentWeek </b> or <b> lastWeek </b>.
     * @return To Date as {@LocalDate}
     */
    public static LocalDate getFromDateByWeekString(def weekString) {
        LocalDate fromDate;
        // For Current Week
        if (weekString.equalsIgnoreCase("currentWeek")) {
            fromDate = new LocalDate().minusWeeks(1).toDateMidnight().toLocalDate();
        }
        //For Earlier Week
        else if (weekString.equalsIgnoreCase("lastWeek")) {
            fromDate = new LocalDate().minusWeeks(2).toDateMidnight().toLocalDate();
        } else {
            fromDate = new LocalDate();
        }
        fromDate = fromDate.plusDays(1);
        return fromDate;
    }

    /**
     * Processes the given week and returns the to Date for the given week.
     * If no Week matches It returns the current date.
     *
     * @param weekString The Week String either <b> currentWeek </b> or <b> lastWeek </b>.
     * @return To Date as {@LocalDate}
     */
    public static LocalDate getToDateByWeekString(def weekString) {
        LocalDate toDate;
        // For Current Week
        if (weekString.equalsIgnoreCase("currentWeek")) {
            toDate = new LocalDate().minusWeeks(0).toDateMidnight().toLocalDate();
        }
        // For Earlier Week
        else if (weekString.equalsIgnoreCase("lastWeek")) {
            toDate = new LocalDate().minusWeeks(1).toDateMidnight().toLocalDate();
        } else {
            toDate = new LocalDate();
        }
        return toDate;
    }
}