package at.becast.dellfancontroller;

import at.becast.dellfancontroller.config.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Checker implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(Checker.class);
    private List<Integer> temps;
    private List<Integer> speed;
    private int currentSpeed = 0;
    Checker() {
        Settings config = Settings.getInstance();
        temps = config.getIntList("dellfanspeed.temperatures");
        speed = config.getIntList("dellfanspeed.speed");
        if(temps.size() != speed.size()) {
            LOG.error("Temperatures not equal Speeds. Please check your fanspeed.conf. Temps: {}, Speeds: {}",temps.size(),speed.size());
        }
    }

    public void run() {
        int wantedSpeed = speedCalc(DellFanSpeed.temp.getAverageTemp());
        if(wantedSpeed != currentSpeed) {
            DellFanSpeed.ipmi.setSpeed(wantedSpeed);
            currentSpeed = wantedSpeed;
        }
    }

    private int speedCalc(Double temp) {
        int rTemp = (int) Math.round(temp);
        int speedKey = getClosestIndex(temps,rTemp);
        int wantedSpeed = speed.get(speedKey);
        LOG.debug("Temperature (rounded): {} Wanted speed: {}",rTemp,wantedSpeed);
        return wantedSpeed;
    }

    private int getClosestIndex(final List<Integer> values, int value) {
        int distance = Math.abs(values.get(0) - value);
        int idx = 0;
        for (int i = 1; i < values.size(); i++) {
            int cdistance = Math.abs(values.get(i) - value);
            if (cdistance < distance) {
                idx = i;
                distance = cdistance;
            }
        }
        return idx;
    }
}
