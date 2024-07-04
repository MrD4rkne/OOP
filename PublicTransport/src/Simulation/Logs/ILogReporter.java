package Simulation.Logs;

import Simulation.Statistic.IStatisticService;

public interface ILogReporter {

    /**
     * Prepares the logging for the given day.
     * @param dayNo  the number of the day
     */
    void prepareLogging(int dayNo, IStatisticService statistic);
    
    /**
     * Logs the given log.
     * @param log  the log to log
     */
    void log(Log log);
}
