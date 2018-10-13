package disney.reservation.notification.WebPageEssentials.Reservation.DataMapper.Parser;


import disney.reservation.notification.Adapter.Logger.Logger;
import disney.reservation.notification.WebPageEssentials.Reservation.DataMapper.Parser.Exception.ReservationParserException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.nio.file.FileSystems;
import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;


public class ReservationParserImplTest {


    @Test
    public void testParseEquals() throws Exception, ReservationParserException {
        String directoryPath = FileSystems.getDefault().getPath("").toAbsolutePath().toString() + "/src/test/java/disney/reservation/notification/WebPageEssentials/reservation/DataMapper/Parser";
        Logger stubLogger = mock(Logger.class);
        ReservationParserImpl tester = new ReservationParserImpl(stubLogger);
        ArrayList<JSONObject> mockObjects = tester.parse(directoryPath);


        assertEquals("https://ohana-reservation-url-testing.com", mockObjects.get(0).get("url"));
        JSONArray mockDates = (JSONArray) mockObjects.get(0).get("dates");
        assertEquals("11/10/2018", mockDates.get(0));
        assertEquals("4", mockObjects.get(0).get("partySize"));

        assertEquals("https://ohana-reservation-url.com", mockObjects.get(1).get("url"));
        JSONArray secondEventMockDates = (JSONArray) mockObjects.get(1).get("dates");
        assertEquals("11/19/2018", secondEventMockDates.get(0));
        assertEquals("https://ohana-reservation-url.com", mockObjects.get(1).get("url"));
        assertEquals("3", mockObjects.get(1).get("partySize"));

    }


    @Test
    public void testParseNotEquals() throws Exception, ReservationParserException {
        String directoryPath = FileSystems.getDefault().getPath("").toAbsolutePath().toString() + "/src/test/java/disney/reservation/notification/WebPageEssentials/reservation/DataMapper/Parser";
        Logger stubLogger = mock(Logger.class);
        ReservationParserImpl tester = new ReservationParserImpl(stubLogger);
        ArrayList<JSONObject> mockObjects = tester.parse(directoryPath);


        assertNotSame("NOT_EQUALS", mockObjects.get(0).get("url"));
        assertNotSame("NOT_EQUALS", mockObjects.get(0).get("startDate"));
        assertNotSame("NOT_EQUALS", mockObjects.get(0).get("endDate"));
        assertNotSame("NOT_EQUALS", mockObjects.get(0).get("seating"));
    }


    @Test
    public void testParseException() {
        String directoryPath = "C:\\NOT_REAL_DIRECTORY";
        Logger stubLogger = mock(Logger.class);
        ReservationParserImpl tester = new ReservationParserImpl(stubLogger);

            ArrayList<JSONObject> mockJSONObjects = tester.parse(directoryPath);
            Assertions.assertNull(mockJSONObjects);
    }


}