import java.util.ArrayList;
import java.util.Scanner;

class Component {
    String code;
    String name;
    int quantity;

    Component(String code, String name, int quantity) {
        this.code = code;
        this.name = name;
        this.quantity = quantity;
    }
}

class IssueRequest {
    int id;
    String student;
    String code;
    int quantity;
    String status;

    IssueRequest(int id, String student, String code, int quantity) {
        this.id = id;
        this.student = student;
        this.code = code;
        this.quantity = quantity;
        this.status = "PENDING";
    }
}

public class Main {
    static Scanner sc = new Scanner(System.in);
    static ArrayList<Component> components = new ArrayList<>();
    static ArrayList<IssueRequest> requests = new ArrayList<>();
    static int nextId = 1;

    public static void main(String[] args) {
        components.add(new Component("C101", "Resistor", 50));
        components.add(new Component("C102", "Capacitor", 30));
        components.add(new Component("C103", "LED", 25));

        while (true) {
            System.out.println("\n=== COMPONENT ISSUE SYSTEM ===");
            System.out.println("1. Admin");
            System.out.println("2. Lab Staff");
            System.out.println("3. Student");
            System.out.println("0. Exit");
            System.out.print("Choose role: ");

            int role = readInt();

            switch (role) {
                case 1:
                    admin();
                    break;
                case 2:
                    staff();
                    break;
                case 3:
                    student();
                    break;
                case 0:
                    System.out.println("Thank you!");
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid role. Try again.");
            }
        }
    }

    static int readInt() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Enter a valid number: ");
            }
        }
    }

    static void showInventory() {
        System.out.println("\n--- Component Inventory ---");

        for (Component c : components) {
            System.out.println(c.code + " | " + c.name
                    + " | Available: " + c.quantity);
        }
    }

    static void admin() {
        while (true) {
            System.out.println("\n--- ADMIN MENU ---");
            System.out.println("1. Add Component");
            System.out.println("2. View Inventory");
            System.out.println("0. Back");
            System.out.print("Choose: ");

            int choice = readInt();

            if (choice == 0) {
                return;
            } else if (choice == 1) {
                System.out.print("Component code: ");
                String code = sc.nextLine().trim();

                boolean exists = false;
                for (Component c : components) {
                    if (c.code.equalsIgnoreCase(code)) {
                        exists = true;
                        break;
                    }
                }

                if (code.isEmpty() || exists) {
                    System.out.println("Invalid or duplicate component code.");
                    continue;
                }

                System.out.print("Component name: ");
                String name = sc.nextLine().trim();

                if (name.isEmpty()) {
                    System.out.println("Name cannot be empty.");
                    continue;
                }

                System.out.print("Quantity: ");
                int quantity = readInt();

                if (quantity < 0) {
                    System.out.println("Quantity cannot be negative.");
                    continue;
                }

                components.add(new Component(code, name, quantity));
                System.out.println("Component added successfully.");

            } else if (choice == 2) {
                showInventory();
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }

    static void student() {
        System.out.print("Enter student name: ");
        String name = sc.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("Student name cannot be empty.");
            return;
        }

        showInventory();

        System.out.print("Enter component code: ");
        String code = sc.nextLine().trim();

        System.out.print("Enter required quantity: ");
        int quantity = readInt();

        if (quantity <= 0) {
            System.out.println("Quantity must be greater than zero.");
            return;
        }

        for (Component c : components) {
            if (c.code.equalsIgnoreCase(code)) {
                if (c.quantity == 0) {
                    System.out.println("Component is out of stock.");
                    return;
                }

                requests.add(new IssueRequest(
                        nextId++, name, c.code, quantity));

                System.out.println("Request submitted successfully.");
                System.out.println("Request ID: " + (nextId - 1));
                System.out.println("Status: PENDING");
                return;
            }
        }

        System.out.println("Component code not found.");
    }

    static void staff() {
        while (true) {
            System.out.println("\n--- LAB STAFF MENU ---");
            System.out.println("1. View Requests");
            System.out.println("2. Approve and Issue Component");
            System.out.println("0. Back");
            System.out.print("Choose: ");

            int choice = readInt();

            if (choice == 0) {
                return;
            }

            if (choice == 1) {
                if (requests.isEmpty()) {
                    System.out.println("No requests available.");
                }

                for (IssueRequest r : requests) {
                    System.out.println("ID: " + r.id
                            + " | Student: " + r.student
                            + " | Code: " + r.code
                            + " | Quantity: " + r.quantity
                            + " | Status: " + r.status);
                }

            } else if (choice == 2) {
                System.out.print("Enter request ID: ");
                int id = readInt();
                IssueRequest request = null;

                for (IssueRequest r : requests) {
                    if (r.id == id) {
                        request = r;
                        break;
                    }
                }

                if (request == null) {
                    System.out.println("Request not found.");
                    continue;
                }

                if (!request.status.equals("PENDING")) {
                    System.out.println("Request already processed.");
                    continue;
                }

                Component selected = null;

                for (Component c : components) {
                    if (c.code.equalsIgnoreCase(request.code)) {
                        selected = c;
                        break;
                    }
                }

                if (selected == null) {
                    System.out.println("Component not found.");
                } else if (selected.quantity < request.quantity) {
                    System.out.println("Insufficient stock.");
                } else {
                    selected.quantity -= request.quantity;
                    request.status = "ISSUED";
                    System.out.println("Component issued successfully.");
                    System.out.println("Remaining stock: "
                            + selected.quantity);
                }

            } else {
                System.out.println("Invalid choice.");
            }
        }
    }
}
