package Simulation.Logs;

public interface ILogReporter {

    void prepareLogging(int dayNo, IStatistic statistic);

    void log(Log log);
}
