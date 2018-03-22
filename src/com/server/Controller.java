package com.server;

import com.socketfx.Constants;
import com.socketfx.FxSocketClient;
import com.socketfx.SocketListener;
import com.utils.CustomClock;
import com.utils.FxTimer;
import com.utils.Timer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

import java.net.URL;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private boolean configured;
    private boolean connected;
    private int clockNumber;

    @FXML
    private Label clockLabel;

    @FXML
    private Slider clockSlider;

    private FxSocketClient socket;
    private CustomClock clock = new CustomClock();
    private Timer timer;

    public Controller() {
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.connect();
    }

    class FxSocketListener implements SocketListener {
        @Override
        public void onMessage(String line) {
            if (line != null && !line.equals("")) {
                System.out.println("Server: " + line);
                if (!configured) {
                    socket.sendMessage("master");
                    configured = true;
                } else if (!connected && line.contains("master-")) {
                    clockNumber = Integer.valueOf(line.substring(line.lastIndexOf("-") + 1));
                    connected = true;
                    FxTimer.runPeriodically(Duration.ofMillis(250), () -> {
                        socket.sendMessage(generateProtocolTimeMessage());
                    });
                } else {
                    clockLabel.setText(line);
                }
            }
        }

        @Override
        public void onClosedStatus(boolean isClosed) {
        }
    }

    private void connect() {
        socket = new FxSocketClient(new FxSocketListener(),
                "localhost",
                6666,
                Constants.instance().DEBUG_NONE);
        socket.connect();
    }

    private String generateProtocolTimeMessage() {
        return "setTime-" + clockNumber + "/" + getClockTime();

    }

    private String getClockTime() {
        return String.valueOf(clock.getTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
    }

    @FXML
    private void handlePlusHour() {
        clock.plusHour();
    }

    @FXML
    private void handleMinusHour() {
        clock.minusHour();
    }

    @FXML
    private void handlePlusMinute() {
        clock.plusMinute();
    }

    @FXML
    private void handleMinusMinute() {
        clock.minusMinute();
    }

}
