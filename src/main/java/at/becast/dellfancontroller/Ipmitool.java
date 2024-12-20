package at.becast.dellfancontroller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Ipmitool {
    private static final Logger LOG = LoggerFactory.getLogger(Ipmitool.class);
    private boolean automatic = true;

    public void enableAutomaticControl(boolean auto) {
        if(auto) {
            automatic = true;
            sendcommand("raw 0x30 0x30 0x01 0x01");
        } else {
            automatic = false;
            sendcommand("raw 0x30 0x30 0x01 0x00");
        }
    }

    public void setSpeed(int percentage) {
        if(percentage <= 0) {
            if(!automatic) {
                enableAutomaticControl(true);
                return;
            }
        }else if(percentage > 100) {
            percentage = 100;
        }
        if(automatic) {
            enableAutomaticControl(false);
        }
        sendcommand("raw 0x30 0x30 0x02 0xff 0x" + String.format("%02X", percentage));
    }

    private void sendcommand(String scommand) {
        LOG.debug("IPMI Command ipmitool {}", scommand);
        try {
            Runtime.getRuntime().exec("ipmitool " + scommand);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
