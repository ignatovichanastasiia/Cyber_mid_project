package system;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.Duration;
import java.util.ArrayList;

public class Report implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String BASE_DIRECTORY = "Reports";
	private static final String REPORT = "report";
	private static Duration workingTimeForOrders; // это все данные с заказа: грузим, едем, проблемы в дороге
	private static ArrayList<Report> reports = new ArrayList<Report>();
	private static ArrayList<Order> uncompletedOrders = new ArrayList<Order>();
	private static ArrayList<Order> completedOrders = new ArrayList<Order>();
	private static ArrayList<Courier> couriers;

	public Report(ArrayList<Order> uncompletedOrders, ArrayList<Order> completedOrders, ArrayList<Courier> couriers) {
		sortingOrdersByReadyStatus();
		this.uncompletedOrders = uncompletedOrders;
		this.completedOrders = completedOrders;
		this.couriers = CourierManager.getCourierList();
		this.workingTimeForOrders = calculateWorkingTimeOrder();
		reports.add(this);
	}

	private Duration calculateWorkingTimeOrder() {
		workingTimeForOrders = Duration.ZERO;
		for (Order order : Order.getOrders())
			if (order.isStatusAccepted()) {
				workingTimeForOrders = workingTimeForOrders
						.plus(order.getExpectedTime().plus(order.getLoadingTime()).plus(order.getDelayTime()));
			}
		return workingTimeForOrders;
	}

	private static Duration calculateDelaysOrder() {
		Duration delayTimeForAll = Duration.ZERO;
		for (Order order : Order.getOrders())
			if (order.isStatusAccepted()) {
				delayTimeForAll = delayTimeForAll
						.plus(order.getExpectedTime().plus(order.getLoadingTime()).plus(order.getDelayTime()));
			}
		return delayTimeForAll;
	}

	public static void sortingOrdersByReadyStatus() {
		StringBuilder str = new StringBuilder("");
		if (Order.getOrders().isEmpty()) {
			System.out.println("There's no orders exist");
			return;
		}
		for (Order order : Order.getOrders()) {
			if (!order.isStatusAccepted()) {
				uncompletedOrders.add(order);
				str.append("Order " + order.getId() + " added to list of uncompleted tasks");
			} else {
				completedOrders.add(order);
				str.append("Order " + order.getId() + " added to list of completed tasks");
			}
		}
		System.out.println(str);
	}

	public static void generateReport() {
	    StringBuilder report = new StringBuilder();
	    // Общие рабочие часы для завершенных заказов
	    report.append("Total Working Time for Completed Orders: ").append(workingTimeForOrders).append("\n\n");
	    
	    // Общее количество выполненных и невыполненных заказов
	    report.append("Total number of completed orders: ").append(completedOrders.size()).append("\n");
	    report.append("Total number of uncompleted orders: ").append(uncompletedOrders.size()).append("\n\n");
	    // Общее время задержек по выполненным заказам
	    report.append("Total delay time for completed orders: ").append(calculateDelaysOrder()).append("\n\n");

	    // Разбивка по курьерам и заказам
	    for (Courier courier : couriers) {
	        report.append("Courier ID: ").append(courier.getId()).append("\n");
	        report.append("Total Earnings: ").append().append("\n");
	        report.append("Total Orders Completed: ").append(countCompletedOrders(courier)).append("\n");
	        report.append("Total Working Hours: ").append(calculateWorkingHours(courier)).append("\n");
	        report.append("Bonuses: ").append(calculateBonuses(courier)).append("\n");
	        report.append("Penalties: ").append(calculatePenalties(courier)).append("\n\n");
	    }

	    System.out.println(report.toString());
	}

	/**
	 * Creates the base directory and orders file if they do not exist.
	 */
	private static void creatingDirAndFileReport() {
		try {
			// Использую Path и Paths для создания пути
			Path directory = Paths.get(System.getProperty("user.home"), "git", "Cyber_mid_project",
					"Courier_management_system", BASE_DIRECTORY);
			if (Files.notExists(directory)) {
				Files.createDirectories(directory);
			}
			Path usersDir = directory.resolve(REPORT);
			if (Files.notExists(usersDir)) {
				Files.createDirectories(usersDir);
				Path filePath = usersDir.resolve(REPORT + ".ser");
				Files.createFile(filePath);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Serializes the static reports list to a specified file.
	 *
	 * @param orders   - List of reports to serialize
	 * @param fileName - Name of the file to save the serialized data
	 * @throws IOException
	 */
	public static void serializeReports(ArrayList<Report> reports, String fileName) throws IOException {
		creatingDirAndFileReport();
		Path filePath = Paths.get(System.getProperty("user.home"), "git", "Cyber_mid_project",
								 "Courier_management_system", REPORT, fileName);
		try (ObjectOutputStream fileReports = new ObjectOutputStream(new FileOutputStream(filePath.toFile()))) {
			fileReports.writeObject(reports);
		}
	}

	/**
	 * Deserializes the reports list from a specified file.
	 *
	 * @param fileName - Name of the file to read the serialized data from
	 * @return deserialized list of reports
	 * @throws IOException, ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Report> deserializeOrders(String fileName) throws IOException, ClassNotFoundException {
		Path filePath = Paths.get(System.getProperty("user.home"), "git", "Cyber_mid_project",
				 "Courier_management_system", REPORT, fileName);
		try (ObjectInputStream fileReports = new ObjectInputStream(new FileInputStream(filePath.toFile()))) {
			reports = (ArrayList<Report>) fileReports.readObject();
		} catch (IOException | ClassNotFoundException e) {
			System.err.println("Error while finding an order: " + e.getMessage());
		}
		return reports;
	}

}
