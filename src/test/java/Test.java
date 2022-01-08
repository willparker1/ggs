import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;

public class Test {

    public static void main( String[] args ) {

        Date startTime = new Date();
        startTime.setHours(12);
        startTime.setMinutes(34);
        startTime.setSeconds(29);
        Date now = new Date();
        startTime.setHours(12);
        startTime.setMinutes(35);
        startTime.setSeconds(0);
        System.out.println( DateUtils.addSeconds(startTime, -35) );
        System.out.println( DateUtils.addSeconds(startTime, -35).compareTo(new Date()) );
    }
}
