package biletsistemi.database;

public class Ticket {

    private int id;
    private String firm;
    private String fromCity;
    private String toCity;
    private String tripType;
    private String flightDate;
    private String flightTime;
    private String returnDate;
    private String seat;
    private String fullName;
    private String passengerEmail;
    private double price;

    public Ticket(
            int id,
            String firm,
            String fromCity,
            String toCity,
            String tripType,
            String flightDate,
            String flightTime,
            String returnDate,
            String seat,
            String fullName,
            String passengerEmail,
            double price
    ) {
        this.id = id;
        this.firm = firm;
        this.fromCity = fromCity;
        this.toCity = toCity;
        this.tripType = tripType;
        this.flightDate = flightDate;
        this.flightTime = flightTime;
        this.returnDate = returnDate;
        this.seat = seat;
        this.fullName = fullName;
        this.passengerEmail = passengerEmail;
        this.price = price;
    }

    public int getId() { return id; }
    public String getFirm() { return firm; }
    public String getFromCity() { return fromCity; }
    public String getToCity() { return toCity; }
    public String getTripType() { return tripType; }
    public String getFlightDate() { return flightDate; }
    public String getFlightTime() { return flightTime; }
    public String getReturnDate() { return returnDate; }
    public String getSeat() { return seat; }
    public String getFullName() { return fullName; }
    public String getPassengerEmail() { return passengerEmail; }
    public double getPrice() { return price; }
}
