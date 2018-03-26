package events;

import restaurant.Restaurant;

class SeatEvent implements Event{
    private int tableId;
    private int seatNumber;
    private EventType type;

    SeatEvent(int tableId) {
        this.tableId = tableId;
        type = EventType.ADDSEAT;
    }

    SeatEvent(int tableId, int seatNumber){
        this.tableId = tableId;
        this.seatNumber = seatNumber;
        type = EventType.REMOVESEAT;
    }

    @Override
    public EventType getType() {
        return type;
    }

    @Override
    public void doEvent() {
        if(type.equals(EventType.ADDSEAT)) Restaurant.getInstance().getTable(tableId).addSeat();
        else Restaurant.getInstance().getTable(tableId).removeSeat(seatNumber);
    }
}
