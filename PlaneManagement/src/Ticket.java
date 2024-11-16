public class Ticket {
    private final String row;
    private final int seat;
    private final int price;
    private final Person person;

    // Constructor
    public Ticket(String row, int seat, int price, Person person) {
        this.row = row;
        this.seat = seat;
        this.price = price;
        this.person = person;
    }

    // Getters
    public String getRow() {
        return row;
    }



    public int getSeat() {
        return seat;
    }



    public int getPrice() {
        return price;
    }



    public Person getPerson() {
        return person;
    }



    // Method to print ticket information
    public void printTicketInfo() {
        System.out.println("Ticket for seat: " + row + seat + ", Price: £" + price);
        person.printInformation();
    }



}
