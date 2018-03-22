package com.server;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ClockProtocol {
    private final Map clocksMap = Collections.synchronizedMap(new HashMap<String, String>());

    ClockProtocol() {}

    public String processInput(String theInput) {
        String theOutput = null;
        if (theInput == null) {
            theOutput = "Please type 'client', or 'master-#' to take a role...";
        } else if (theInput.equalsIgnoreCase("bye")) {
            theOutput = "Bye";
        } else {
            String operation;
            if (theInput.lastIndexOf("-") > 0) {
                operation = theInput.substring(0, theInput.lastIndexOf("-"));
            } else {
                operation = theInput;
            }
            switch (operation) {
                case "client":
                    theOutput = String.valueOf(Arrays.toString(clocksMap.keySet().toArray()));
                    break;
                case "master":
                    clocksMap.put(clocksMap.size() + 1, "");
                    System.out.println("New master added... #" + clocksMap.size());
                    theOutput = "master-" + clocksMap.size();
                    break;
                case "fetchTime":
                    int serverId = Integer.valueOf(theInput.substring(theInput.lastIndexOf("-") + 1));
                    if (clocksMap.containsKey(serverId)) {
                        theOutput = String.valueOf(clocksMap.get(serverId));
                    } else {
                        theOutput = "server " + serverId +" not found!";
                    }
                    break;
                case "setTime":
                    int masterId = Integer.valueOf(theInput.substring(theInput.lastIndexOf("-") + 1, theInput.lastIndexOf("/")));
                    String masterTime = theInput.substring(theInput.lastIndexOf("/") + 1);
                    clocksMap.put(masterId, masterTime);
                    theOutput = String.valueOf(clocksMap.get(masterId));
                    break;
                default:
                    theOutput = "WHAT?";
            }
        }
        return theOutput;
    }
}

