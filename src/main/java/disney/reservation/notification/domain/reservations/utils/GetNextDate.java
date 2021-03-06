package disney.reservation.notification.domain.reservations.utils;

import static java.util.Locale.ENGLISH;

import disney.reservation.notification.domain.log.Logger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.function.BiFunction;

public class GetNextDate implements BiFunction<String, Integer, String> {

  private final Logger logger;

  public GetNextDate(Logger logger) {
    this.logger = logger;
  }

  @Override
  public String apply(String date, Integer interval) {
    final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", ENGLISH);
    final Date start;
    try {
      start = sdf.parse(date);
      final Calendar calendar = Calendar.getInstance();
      calendar.setTime(start);
      calendar.add(Calendar.DAY_OF_YEAR, interval);
      return sdf.format(calendar.getTime());
    } catch (ParseException e) {
      logger.info("Error with getting next date: ".concat(e.getMessage()));
      return null;
    }
  }
}
