package Simulation.Logs;

public class ConsoleLogReporter implements ILogReporter {
    private IStatistic statistic;
    private int dayNo;
    
    public ConsoleLogReporter() {

    }

    @Override
    public void prepareLogging(int dayNo, IStatistic statistic) {

        this.dayNo=dayNo;
        this.statistic=statistic;
    }

    public void log(Log log){
        printMessage(log.getTime(), log.toString());
        log.updateStatistic(statistic);
    }
    
    private void printMessage(int time, String message){
        String sb = dayNo + ". " +
                getTimestamp(time) + " " +
                message;
        System.out.println(sb);
    }
    
    private String getTimestamp(int currentTime){
        int hours = currentTime / 60;
        int minutes = currentTime % 60;
        return String.format("%02d:%02d", hours, minutes);
    }
}
