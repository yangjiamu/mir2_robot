import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by yang on 2016/11/9.
 */
public class Log4jTest {
    private static final Logger logger = LoggerFactory.getLogger(Log4jTest.class);

    public static void main(String[] args) {
        logger.info("info level");
        logger.error("error level");
        logger.debug("debug level");
    }
    @Test
    public void test(){

    }
}
