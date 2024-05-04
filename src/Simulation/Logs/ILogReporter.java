package Simulation.Logs;

public interface ILogReporter {

    void prepareLogging(int dayNo);

    void log(Log log);
}
