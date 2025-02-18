package system;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.Duration;
import java.util.ArrayList;

public class Order {

    private static final long serialVersionUID = 1L;
    private static final String BASE_DIRECTORY = "C:\\Users\\IvanS\\git\\Cyber_mid_project\\Courier_management_system\\Orders";
    private static final String ORDERS = "\\orders";
    private static int idCounter = 1;
    private final String id;
    private String category;
    private Duration loadingTime;
    private Duration travelTime;
    private Duration delayTime; // Will be initialized by randomizeDelay method
    private boolean priority;
    private boolean statusComplete;
    private boolean statusAccepted;
    private String courierIDcomplete;
    private static ArrayList<Order> orders = new ArrayList<Order>();

    /**
     * Constructor to initialize an Order object with given parameters.
     * It also generates a unique id for the order and adds the order to the static orders list.
     *
     * @param category - Category of the order
     * @param loadingTime - Loading time duration
     * @param travelTime - Travel time duration
     * @param priority - Priority status of the order
     * @param status - Current status of the order
     */
    public Order(String category, Duration loadingTime, Duration travelTime) {
        this.category = category;
        this.loadingTime = loadingTime;
        this.travelTime = travelTime;
        this.delayTime = randomizeDelay(); // Initialize with a random delay
        this.priority = false;
        this.statusAccepted = false;
        this.statusComplete = false;
        this.courierIDcomplete = null;
        this.id = getClass().getSimpleName().charAt(0) + "-" + "0".repeat(nullsNumber(idCounter)) + idCounter++;
        orders.add(this);
    }

    /**
     * Generates a random delivery delay within the range of 0 to 59 minutes.
     *
     * @return delayTime
     */
    private Duration randomizeDelay() {
        long randomMinutes = (long) (Math.random() * 60);
        return Duration.ofMinutes(randomMinutes);
    }

    /**
     * Searches for an order in the static orders list by its ID.
     * @param orderId - ID of the order to find
     * @return the order if found, otherwise null
     */
    public static Order findOrderById(String orderId) {
        for (Order order : orders) {
            if (order.getId().equalsIgnoreCase(orderId)) {
                return order;
            }
        }
        return null;
    }

    /**
     * Creates the base directory and orders file if they do not exist.
     */
    private static void creatingDirAndFileOrders() {
        File directory = new File(BASE_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File usersDir = new File(BASE_DIRECTORY + ORDERS);
        if (!usersDir.exists()) {
            usersDir.mkdir();
            String filePath = BASE_DIRECTORY + ORDERS + "/" + "Orders.ser";
            File fileName = new File(filePath);
            try {
                fileName.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Serializes the static orders list to a specified file.
     *
     * @param orders - List of orders to serialize
     * @param fileName - Name of the file to save the serialized data
     * @throws IOException
     */
    public static void serializeOrders(ArrayList<Order> orders, String fileName) throws IOException {
        creatingDirAndFileOrders();
        try (ObjectOutputStream fileOrders = new ObjectOutputStream(new FileOutputStream(fileName))) {
            fileOrders.writeObject(orders);
        }
    }

    /**
     * Deserializes the orders list from a specified file.
     *
     * @param fileName - Name of the file to read the serialized data from
     * @return deserialized list of orders
     * @throws IOException, ClassNotFoundException
     */
    @SuppressWarnings("unchecked")
    public ArrayList<Order> deserializeOrders(String fileName) throws IOException, ClassNotFoundException {
        try (ObjectInputStream fileOrders = new ObjectInputStream(new FileInputStream(fileName))) {
            orders = (ArrayList<Order>) fileOrders.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error while finding an order: " + e.getMessage());
        }
        return orders;
    }

    /**
     * Helper method to determine the number of leading zeros required for the order ID.
     *
     * @param number - The current order number
     * @return number of leading zeros
     */
    private int nullsNumber(int number) {
        if (number < 10) {
            return 2;
        } else if (number < 100) {
            return 1;
        } else {
            return 0;
        }
    }

    // Getter and Setter methods

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Duration getLoadingTime() {
        return loadingTime;
    }

    public void setLoadingTime(Duration loadingTime) {
        this.loadingTime = loadingTime;
    }

    public Duration getExpectedTime() {
        return travelTime;
    }

    public void setExpectedTime(Duration expectedTime) {
        this.travelTime = expectedTime;
    }

    public Duration getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(Duration delayTime) {
        this.delayTime = delayTime;
    }

    public boolean isPriority() {
        return priority;
    }

    public void setPriority(boolean priority) {
        this.priority = priority;
    }

    public boolean isStatusComplete() {
		return statusComplete;
	}

	public void setStatusComplete(boolean statusComplete) {
		this.statusComplete = statusComplete;
	}

	public boolean isStatusAccepted() {
		return statusAccepted;
	}

	public void setStatusAccepted(boolean statusAccepted) {
		this.statusAccepted = statusAccepted;
	}

	public static ArrayList<Order> getOrders() {
        return orders;
    }

    public static void setOrders(ArrayList<Order> orders) {
        Order.orders = orders;
    }

    public String getId() {
        return id;
    }
    public String getCourierIDcomplete() {
		return courierIDcomplete;
	}

	public void setCourierIDcomplete(String courierIDcomplete) {
		this.courierIDcomplete = courierIDcomplete;
	}


}
