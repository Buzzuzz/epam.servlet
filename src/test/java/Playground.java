import lombok.extern.log4j.Log4j2;
import com.my.project.model.dao.DataSource;
import com.my.project.model.dao.impl.CourseDAO;
import com.my.project.model.entities.Course;

import java.sql.Connection;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Log4j2
public class Playground {
    public static void main(String[] args) {
        Connection con = DataSource.getConnection();

        Course c = CourseDAO.getInstance().get(con, 3).get();
        long millis = Duration.between(c.getStart_date().toLocalDateTime(), c.getEnd_date().toLocalDateTime()).toMillis();
        log.info(millis);
        log.info(TimeUnit.MILLISECONDS.toDays(millis));

    }

    public long diff(Timestamp t1, Timestamp t2, TimeUnit timeUnit) {
        return timeUnit.convert(t1.getTime() - t2.getTime(), TimeUnit.MILLISECONDS);
    }
}
