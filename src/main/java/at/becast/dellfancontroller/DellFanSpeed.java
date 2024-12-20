package at.becast.dellfancontroller;

import at.becast.dellfancontroller.config.Settings;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DellFanSpeed {
    private static final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    static Ipmitool ipmi = new Ipmitool();
    static Sensors temp = new Sensors();
    private static final Checker check = new Checker();
    private static final Settings config = Settings.getInstance();
    public static void main(String[] args) {
        //Main Code
        final Thread mainThread = Thread.currentThread();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                System.out.println("Shutting Down, resuming automatic control");
                ipmi.enableAutomaticControl(true);
                mainThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }));
        executor.scheduleAtFixedRate(check, 0, config.getInt("dellfanspeed.refresh"), TimeUnit.SECONDS);
    }
}

