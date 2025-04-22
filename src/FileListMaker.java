import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class FileListMaker {
    private static ArrayList<String> list = new ArrayList<>();
    private static String currentFileName = null;
    private static boolean needsToBeSaved = false;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        boolean done = false;

        while (!done) {
            displayMenu();
            String cmd = in.nextLine().trim().toUpperCase();

            try {
                switch (cmd) {
                    case "A" -> addItem(in);
                    case "D" -> deleteItem(in);
                    case "I" -> insertItem(in);
                    case "M" -> moveItem(in);
                    case "V" -> viewList();
                    case "S" -> saveFile();
                    case "O" -> openFile(in);
                    case "C" -> clearList(in);
                    case "Q" -> done = quitPrompt(in);
                    default -> System.out.println("Invalid option. Try again.");
                }
            } catch (IOException e) {
                System.out.println("File operation failed: " + e.getMessage());
            }
        }
        System.out.println("Cya!");
    }

    private static void displayMenu() {
        System.out.println("\n=== File List Maker Menu ===");
        System.out.println("A - Add Item");
        System.out.println("D - Delete Item");
        System.out.println("I - Insert Item");
        System.out.println("M - Move Item");
        System.out.println("V - View List");
        System.out.println("S - Save List");
        System.out.println("O - Open List");
        System.out.println("C - Clear List");
        System.out.println("Q - Quit");
        System.out.print("Enter command: ");
    }

    private static void addItem(Scanner in) {
        System.out.print("Enter item to add: ");
        String item = in.nextLine();
        list.add(item);
        needsToBeSaved = true;
    }

    private static void deleteItem(Scanner in) {
        viewList();
        System.out.print("Enter index to delete: ");
        int index = Integer.parseInt(in.nextLine());
        if (index >= 0 && index < list.size()) {
            list.remove(index);
            needsToBeSaved = true;
        } else {
            System.out.println("Invalid index.");
        }
    }

    private static void insertItem(Scanner in) {
        System.out.print("Enter item to insert: ");
        String item = in.nextLine();
        System.out.print("Enter index to insert at: ");
        int index = Integer.parseInt(in.nextLine());
        if (index >= 0 && index <= list.size()) {
            list.add(index, item);
            needsToBeSaved = true;
        } else {
            System.out.println("Invalid index.");
        }
    }

    private static void moveItem(Scanner in) {
        viewList();
        System.out.print("Enter index of item to move: ");
        int fromIndex = Integer.parseInt(in.nextLine());
        System.out.print("Enter new index to move to: ");
        int toIndex = Integer.parseInt(in.nextLine());
        if (fromIndex >= 0 && fromIndex < list.size() && toIndex >= 0 && toIndex <= list.size()) {
            String item = list.remove(fromIndex);
            list.add(toIndex, item);
            needsToBeSaved = true;
        } else {
            System.out.println("Invalid index(es).");
        }
    }

    private static void viewList() {
        System.out.println("\n=== Current List ===");
        for (int i = 0; i < list.size(); i++) {
            System.out.printf("%d: %s%n", i, list.get(i));
        }
    }

    private static void saveFile() throws IOException {
        if (currentFileName == null) {
            Scanner in = new Scanner(System.in);
            System.out.print("Enter file name to save (without .txt): ");
            currentFileName = in.nextLine().trim() + ".txt";
        }
        Files.write(Paths.get(currentFileName), list);
        needsToBeSaved = false;
        System.out.println("List saved to: " + currentFileName);
    }

    private static void openFile(Scanner in) throws IOException {
        if (needsToBeSaved && !confirmAbandonChanges(in)) {
            return;
        }
        System.out.print("Enter filename to open (without .txt): ");
        currentFileName = in.nextLine().trim() + ".txt";
        list = new ArrayList<>(Files.readAllLines(Paths.get(currentFileName)));
        needsToBeSaved = false;
        System.out.println("Loaded " + list.size() + " items from " + currentFileName);
    }

    private static void clearList(Scanner in) {
        if (!list.isEmpty() && confirmClearList(in)) {
            list.clear();
            needsToBeSaved = true;
            System.out.println("List cleared.");
        }
    }

    private static boolean quitPrompt(Scanner in) throws IOException {
        if (needsToBeSaved) {
            System.out.print("You have unsaved changes. Save before quitting? (Y/N): ");
            String response = in.nextLine().trim().toUpperCase();
            if (response.equals("Y")) {
                saveFile();
            }
        }
        return true;
    }

    private static boolean confirmAbandonChanges(Scanner in) {
        System.out.print("Unsaved changes will be lost. Continue? (Y/N): ");
        return in.nextLine().trim().equalsIgnoreCase("Y");
    }

    private static boolean confirmClearList(Scanner in) {
        System.out.print("Are you sure you want to clear the list? (Y/N): ");
        return in.nextLine().trim().equalsIgnoreCase("Y");
    }
}
