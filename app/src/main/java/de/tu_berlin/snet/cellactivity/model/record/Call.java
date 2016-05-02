package de.tu_berlin.snet.cellactivity.model.record;

import java.util.ArrayList;

import de.tu_berlin.snet.cellactivity.util.CellInfo;
import de.tu_berlin.snet.cellactivity.util.validation.Check;

/**
 * Created by giraffe on 4/16/16.
 */
public class Call extends AbstractCallOrText {
    private CellInfo startCell;
    private ArrayList<Handover> handovers;
    private long startTime, endTime;

    public Call(CellInfo startCell, String direction, String address, ArrayList<Handover> handovers, long startTime, long endTime) {
        setHandovers(handovers);
        setDirection(direction);
        setAddress(address);
        setStartCell(startCell);
        setStartTime(startTime);
        setEndTime(endTime);
    }

    // TODO: POSSIBLY REMOVE handovers FROM CONSTRUCTOR, AS IT WILL ALMOST ALWAYS BE EMPTY - OR CREATE ADDITIONAL CONSTRUCTOR
    public Call(CellInfo startCell, String direction, String address, ArrayList<Handover> handovers) {
        this(startCell, direction, address, handovers, System.currentTimeMillis()/1000, System.currentTimeMillis()/1000);
    }

    public CellInfo getStartCell() {
        return startCell;
    }

    private boolean startCellMatchesHandoverStart(CellInfo startCell, ArrayList<Handover> handovers) {
        if(handovers.size() > 0) {
            if(Check.Network.isSameCell(startCell, handovers.get(0).getStartCell())) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    public void setStartCell(CellInfo startCell) {
        if(startCellMatchesHandoverStart(startCell, getHandovers())) {
            this.startCell = startCell;
        } else {
            throw new IllegalArgumentException("startCell must match first startCell in handovers");
        }
    }

    public ArrayList<Handover> getHandovers() {
        return handovers;
    }
    private void setHandovers(ArrayList<Handover> handovers) {
        if(startCellMatchesHandoverStart(getStartCell(), handovers)) {
            this.handovers = handovers;
        } else {
            throw new IllegalArgumentException("startCell must match first startCell in handovers");
        }

    }

    public void addHandover(Handover handover) {
        this.handovers.add(handover);
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        if (!Check.Time.isBetween2016and2025(startTime)) {
            throw new IllegalArgumentException("not a valid epoch timestamp between 2016 and 2026");
        }
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        if (Check.Time.isBefore(endTime, getStartTime())) {
            throw new IllegalArgumentException("endTime cannot be before startTime");
        }
        this.endTime = endTime;
    }

    public void endCall() {
        setEndTime(System.currentTimeMillis()/1000);
    }
}