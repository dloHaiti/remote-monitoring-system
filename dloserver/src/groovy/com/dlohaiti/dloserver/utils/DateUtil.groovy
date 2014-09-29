package com.dlohaiti.dloserver.utils
import com.dlohaiti.dloserver.Delivery
import com.dlohaiti.dloserver.Kiosk
import com.dlohaiti.dloserver.Product
import com.dlohaiti.dloserver.Reading
import com.dlohaiti.dloserver.Readings
import com.dlohaiti.dloserver.Receipt
import com.dlohaiti.dloserver.Region
import com.dlohaiti.dloserver.TableToChart
import org.joda.time.LocalDate

import java.math.RoundingMode

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
}