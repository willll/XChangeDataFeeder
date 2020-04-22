package features;

import java.lang.invoke.MethodHandles;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*
 * https://mkyong.com/logging/apache-log4j-2-tutorials/
 */

public interface ILogger {
	static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());
}
