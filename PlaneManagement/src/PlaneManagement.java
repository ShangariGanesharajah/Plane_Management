import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;

public class PlaneManagement {
    private static final int ROWS = 4;
    private static final int SEATS_IN_ROW_A_D = 14;
    private static final int SEATS_IN_ROW_B_C = 12;
    private static final int[][] seatingPlan = new int[ROWS][SEATS_IN_ROW_A_D];
    private static Ticket[] ticketsSold;
    private static int ticketsCount = 0;

    // Main method - entry point
    public static void main(String[] args) {
        initializeSeatingPlan();
        ticketsSold = new Ticket[56]; // Maximum possible number of tickets

        System.out.println("Welcome to the Plane Management application");
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            displayMenu();
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next(); // Consume the non-integer input
            }
            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    buySeat(scanner);
                    break;
                case 2:
                    cancelSeat(scanner);
                    break;
                case 3:
                    findFirstAvailableSeat();
                    break;
                case 4:
                    showSeatingPlan();
                    break;
                case 5:
                    printTicketsInfo();
                    break;
                case 6:
                    searchTicket(scanner);
                    break;
                case 0:
                    System.out.println("Exiting the application...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        } while (choice != 0);

        scanner.close();
    }

    // Initialize the seating plan
    private static void initializeSeatingPlan() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < (i == 1 || i == 2 ? SEATS_IN_ROW_B_C : SEATS_IN_ROW_A_D); j++) {
                seatingPlan[i][j] = 0; // 0 indicates that the seat is available
            }
        }
    }

    // Display the main menu
    private static void displayMenu() {
        System.out.println("\nMENU OPTIONS");
        System.out.println("1) Buy a seat");
        System.out.println("2) Cancel a seat");
        System.out.println("3) Find first available seat");
        System.out.println("4) Show seating plan");
        System.out.println("5) Print tickets information and total sales");
        System.out.println("6) Search ticket");
        System.out.println("0) Quit");
        System.out.println("Please select an option:");
    }

    private static void buySeat(Scanner scanner) {
        System.out.println("Enter the row letter (A-D):");
        String row = scanner.next().toUpperCase();

        if (!(row.equals("A") || row.equals("B") || row.equals("C") || row.equals("D"))) {
            System.out.println("Invalid row. Rows are A to D.");
            return; // Exit if the row is not valid
        }

        System.out.println("Enter the seat number:");
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a number for the seat.");
            scanner.next(); // Consume the non-integer input
        }
        int seat = scanner.nextInt();

        // Now validate the seat number against specific row bounds
        if (isValidSeat(row, seat)) {
            System.out.println("Invalid seat number. Please try again.");
            return; // Exit if the seat number is not valid for the row
        }

        int rowIndex = row.charAt(0) - 'A';
        int seatIndex = seat - 1; // Convert seat number to 0-based index for array

        // Additional check to ensure seatIndex is within the bounds of the array
        if (seatIndex < 0 || seatIndex >= seatingPlan[rowIndex].length) {
            System.out.println("Seat number out of range. Please select a valid seat number.");
            return;
        }

        // Check if the seat is already booked
        if (seatingPlan[rowIndex][seatIndex] != 0) {
            System.out.println("This seat is already booked. Please choose another seat.");
            return;
        }

        // Mark the seat as booked
        seatingPlan[rowIndex][seatIndex] = 1;

        System.out.println("Enter passenger's name:");
        String name = scanner.next();
        System.out.println("Enter passenger's surname:");
        String surname = scanner.next();
        System.out.println("Enter passenger's email:");
        String email = scanner.next();
        Person person = new Person(name, surname, email);

        int price = getSeatPrice(row, seat);
        Ticket ticket = new Ticket(row, seat, price, person);

        ticketsSold[ticketsCount] = ticket;
        ticketsCount++;

        System.out.println("Seat successfully booked for " + name + " " + surname + ".");
        save(ticket);
    }


    private static void save(Ticket ticket) {
        String filename = "Seat_" + ticket.getRow() + ticket.getSeat() + ".txt";
        try {
            FileWriter writer = new FileWriter(filename);
            writer.write("Ticket for seat: " + ticket.getRow() + ticket.getSeat() + "\n");
            writer.write("Name: " + ticket.getPerson().getName() + "\n");
            writer.write("Surname: " + ticket.getPerson().getSurname() + "\n");
            writer.write("Email: " + ticket.getPerson().getEmail() + "\n");
            writer.write("Price: £" + ticket.getPrice());
            writer.close();
            System.out.println("Ticket information saved to " + filename);
        } catch (IOException e) {
            System.out.println("An error occurred while saving the ticket.");

        }
    }


    private static boolean isValidSeat(String row, int seat) {
        if (row.length() != 1 || seat < 1 || seat > 14) {
            return true;
        }
        char rowChar = row.charAt(0);
        return (rowChar < 'A' || rowChar > 'D') || (seat > (rowChar == 'B' || rowChar == 'C' ? SEATS_IN_ROW_B_C : SEATS_IN_ROW_A_D));
    }

    private static int getSeatPrice(String row, int seat) {
        // Assuming the price logic based on the row and seat number is implemented here
        // Returns the correct price for the seat
        final int i = seat >= 1 && seat <= 5 ? 200 : seat >= 6 && seat <= 9 ? 150 : 180;
        return switch (row) {
            case "A", "D", "B", "C" -> i;
            default -> 0;
        };
    }

    private static void cancelSeat(Scanner scanner) {
        System.out.println("Enter the row letter (A-D) to cancel:");
        String row = scanner.next().toUpperCase();

        // Validate row input
        if (!(row.equals("A") || row.equals("B") || row.equals("C") || row.equals("D"))) {
            System.out.println("Invalid row. Rows are A to D.");
            return; // Exit if the row is not valid
        }

        System.out.println("Enter the seat number to cancel:");
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a number for the seat.");
            scanner.next(); // Consume the invalid input
        }
        int seat = scanner.nextInt();

        // Validate the seat number based on the row
        if (isValidSeat(row, seat)) {
            System.out.println("Invalid seat number. Please try again.");
            return; // Exit if the seat number is not valid for the row
        }

        int rowIndex = row.charAt(0) - 'A';
        int seatIndex = seat - 1; // Convert seat number to 0-based index

        // Additional check to ensure seatIndex is within the array bounds
        if (seatIndex < 0 || seatIndex >= seatingPlan[rowIndex].length) {
            System.out.println("Seat number out of range. Please select a valid seat number.");
            return;
        }

        // Check if the seat is already booked
        if (seatingPlan[rowIndex][seatIndex] != 1) {
            System.out.println("This seat is not currently booked. Cannot cancel.");
            return;
        }

        // Mark the seat as available
        seatingPlan[rowIndex][seatIndex] = 0;

        // Remove the ticket from the ticketsSold array
        for (int i = 0; i < ticketsCount; i++) {
            if (ticketsSold[i].getRow().equalsIgnoreCase(row) && ticketsSold[i].getSeat() == seat) {
                // Shift subsequent tickets up in the array to fill the gap
                System.arraycopy(ticketsSold, i + 1, ticketsSold, i, ticketsCount - i - 1);
                ticketsSold[ticketsCount - 1] = null; // Nullify the last element
                ticketsCount--; // Decrease the ticket count
                System.out.println("The seat " + row + seat + " has been successfully canceled.");
                return;
            }
        }

        System.out.println("Ticket for the seat " + row + seat + " was not found.");
    }



    private static void findFirstAvailableSeat() {
        // Iterate over each row and seat to find the first available one.
        for (int i = 0; i < seatingPlan.length; i++) {
            for (int j = 0; j < seatingPlan[i].length; j++) {
                // Check if the seat is available
                if (seatingPlan[i][j] == 0) {
                    char row = (char) ('A' + i);
                    int seatNumber = j + 1;
                    System.out.println("The first available seat is " + row + seatNumber);
                    return; // Exit the method after finding the first available seat
                }
            }
        }

        // If all seats are booked
        System.out.println("There are no available seats.");
    }

    private static void showSeatingPlan() {
        System.out.println("Seating Plan:");

        for (int i = 0; i < seatingPlan.length; i++) {
            // Determine the row letter based on the index
            char rowLetter = (char) ('A' + i);
            System.out.print(rowLetter + " ");

            for (int j = 0; j < seatingPlan[i].length; j++) {
                // For rows B and C, skip printing the last two positions
                if ((i == 1 || i == 2) && j >= SEATS_IN_ROW_B_C) {
                    continue;
                }

                // Print an 'X' for sold seats and an 'O' for available seats
                char seatStatus = seatingPlan[i][j] == 1 ? 'X' : 'O';
                System.out.print(seatStatus + " ");
            }
            System.out.println(); // Move to the next line after each row
        }
    }

    private static void printTicketsInfo() {
        if (ticketsCount == 0) {
            System.out.println("No tickets have been sold.");
            return;
        }

        System.out.println("Tickets Information and Total Sales:");
        int totalSales = 0;

        // Loop through the array of sold tickets and print their info
        for (int i = 0; i < ticketsCount; i++) {
            Ticket ticket = ticketsSold[i];
            if (ticket != null) {
                ticket.printTicketInfo(); // Assuming printTicketInfo() is implemented in the Ticket class
                totalSales += ticket.getPrice();
            }
        }
        System.out.println("---------------------------------------------------------------" );
        System.out.println("Total sales amount: £" + totalSales);
    }

    private static void searchTicket(Scanner scanner) {
        System.out.println("Enter the row letter (A-D) to search:");
        String row = scanner.next().toUpperCase();

        // Validate row input
        if (!(row.equals("A") || row.equals("B") || row.equals("C") || row.equals("D"))) {
            System.out.println("Invalid row. Rows are A to D.");
            return; // Exit if the row is not valid
        }

        System.out.println("Enter the seat number to search:");
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a number for the seat.");
            scanner.next(); // Consume the invalid input
        }
        int seat = scanner.nextInt();

        // Correctly validate the seat number based on the row
        if (isValidSeat(row, seat)) {
            System.out.println("Invalid seat number. Please try again.");
            return; // Exit if the seat number is not valid for the row
        }

        // Adjust the check for seat availability based on your seating plan logic
        int rowIndex = row.charAt(0) - 'A';
        int seatIndex = seat - 1; // Convert to 0-based index for internal array use

        // Correct bounds check to prevent ArrayIndexOutOfBoundsException
        if (seatIndex < 0 || seatIndex >= seatingPlan[rowIndex].length) {
            System.out.println("Seat number out of range for the specified row.");
            return;
        }

        // Look for the ticket in the sold tickets
        for (int i = 0; i < ticketsCount; i++) {
            Ticket ticket = ticketsSold[i];
            if (ticket != null && ticket.getRow().equalsIgnoreCase(row) && ticket.getSeat() == seat) {
                System.out.println("Ticket found:");
                ticket.printTicketInfo(); // Assumes this method prints the ticket details
                return;
            }
        }

        // If no ticket is found for the seat
        System.out.println("No ticket found for seat " + row + seat + ".");
    }

}