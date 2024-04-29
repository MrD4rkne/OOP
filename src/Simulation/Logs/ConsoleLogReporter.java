package Simulation.Logs;

public class ConsoleLogReporter implements ILogReporter {
    private final int dayNo;
    
    public ConsoleLogReporter(int dayNo) {
        this.dayNo = dayNo;
    }
    
    public void log(Log log){
        printMessage(log.getTime(), log.toString());
    }
    
    private void printMessage(int time, String message){
        StringBuilder sb = new StringBuilder();
        sb.append(dayNo).append(". ");
        sb.append(getTimestamp(time)).append(" ");
        sb.append(message);
        System.out.println(sb.toString());
    }
    
    private String getTimestamp(int currentTime){
        int hours = currentTime / 60;
        int minutes = currentTime % 60;
        return String.format("%02d:%02d", hours, minutes);
    }
}
