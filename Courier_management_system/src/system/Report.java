
package system;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.Duration;
import java.util.ArrayList;

public class Report implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String DIRECTORY_PATH = "serializ";
	private static final String REPORT = "report";
	private static Duration workingTimeForOrders; 
	private static ArrayList<Report> reports = new ArrayList<Report>();
	private static ArrayList<Order> uncompletedOrders = new ArrayList<Order>();
	private static ArrayList<Order> completedOrders = new ArrayList<Order>();
	private static ArrayList<Courier> couriers;

	/**
     * Constructor for creating a Report object.
     *
     * @param uncompletedOrders - List of uncompleted orders
     * @param completedOrders - List of completed orders
     * @param couriers - List of couriers
     */	
	public Report(ArrayList<Order> uncompletedOrders, ArrayList<Order> completedOrders, ArrayList<Courier> couriers) {
		this.uncompletedOrders = uncompletedOrders;
		this.completedOrders = completedOrders;
		this.couriers = CourierManager.getCourierList();
		this.workingTimeForOrders = calculateWorkingTimeOrder();
		sortingOrdersByReadyStatus();
		reports.add(this);
	}

	/**
     * Calculates the total working time for completed orders.
     *
     * @return total working time for completed orders
     */
	private Duration calculateWorkingTimeOrder() {
		workingTimeForOrders = Duration.ZERO;
		for (Order order : Order.getOrders())
			if (order.isStatusAccepted()) {
				workingTimeForOrders = workingTimeForOrders
						.plus(order.getTravelTime().plus(order.getLoadingTime()).plus(order.getDelayTime()));
			}
		return workingTimeForOrders;
	}

	/**
     * Calculates the total delay time for completed orders.
     *
     * @return total delay time for completed orders
     */
	private static Duration calculateDelaysOrder() {
		Duration delayTimeForAll = Duration.ZERO;
		for (Order order : Order.getOrders())
			if (order.isStatusAccepted()) {
				delayTimeForAll = delayTimeForAll
						.plus(order.getTravelTime().plus(order.getLoadingTime()).plus(order.getDelayTime()));
			}
		return delayTimeForAll;
	}

	/**
     * Sorts orders by their completion status and updates the respective lists.
     */
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

	/**
	 * Generates and prints a report containing various statistics about the orders and couriers.
	 * This method calculates and displays the total working time for completed orders, the number
	 * of completed and uncompleted orders, the total delay time for completed orders, and a 
	 * breakdown of couriers including their total earnings, total orders completed, and total working hours.
	 */
	public static void generateReport() {
	    StringBuilder report = new StringBuilder();

	    try {
	        // Ensure that necessary collections and variables are not null
	        if (workingTimeForOrders != null) {
	            report.append("Total Working Time for Completed Orders: ").append(workingTimeForOrders).append("\n\n");
	        }

	        if (completedOrders != null) {
	            report.append("Total number of completed orders: ").append(completedOrders.size()).append("\n");
	        }

	        if (uncompletedOrders != null) {
	            report.append("Total number of uncompleted orders: ").append(uncompletedOrders.size()).append("\n\n");
	        }

	        // Total delay time for completed orders
	        report.append("Total delay time for completed orders: ").append(calculateDelaysOrder()).append("\n\n");

	        // Breakdown by couriers and orders
	        if (couriers != null) {
	            for (Courier courier : couriers) {
	                report.append("Courier ID: ").append(courier.getId()).append("\n");
	                report.append("Total Earnings: ").append(courier.calculateSalary()).append("\n");
	                report.append("Total Orders Completed: ").append(CourierManager.getCourierListOfOrdersById(courier.getId()).size()).append("\n");
	                report.append("Total Working Hours: ").append(courier.getWorkingHours()).append("\n");
	            }
	        }

	        System.out.println(report.toString());
	    } catch (Exception e) {
	        System.err.println("Error generating report: " + e.getMessage());
	    }
	}


	/**
	 * Serializes the reports ArrayList to a file.
	 * 
	 * This method writes the reports ArrayList to a .ser file using ObjectOutputStream.
	 * 
	 * @return true if the serialization was successful, false otherwise.
	 */
	public static boolean serializeReports() {
	    File serFile = new File(DIRECTORY_PATH, REPORT + ".ser");

	    try (ObjectOutputStream fileReportsOut = new ObjectOutputStream(new FileOutputStream(serFile))) {
	        fileReportsOut.writeObject(reports);
	        System.out.println("Reports were serialized");
	        return true;  
	    } catch (IOException e) {
	        System.err.println("Error while serializing reports: " + e.getMessage());
	        return false; 
	    }
	}

	/**
	 * Deserializes the reports ArrayList from a specified file.
	 * 
	 * This method checks if the file exists and then reads the reports ArrayList 
	 * from the file using ObjectInputStream. If the file is not found or an error occurs 
	 * during deserialization, an error message is printed and false is returned.
	 * 
	 * @return true if the deserialization was successful, false otherwise.
	 */
	public static boolean deserializeReports() {
	    File serFile = new File(DIRECTORY_PATH, REPORT + ".ser");

	    if (!serFile.exists()) {
	        System.err.println("File not found: " + serFile.getPath());
	        return false; // Failure
	    }

	    try (ObjectInputStream fileReportsIn = new ObjectInputStream(new FileInputStream(serFile))) {
	        Object obj = fileReportsIn.readObject();
	        if (obj instanceof ArrayList<?>) {
	            reports = (ArrayList<Report>) obj;
	            System.out.println("Reports were deserialized");
	            return true; 
	        } else {
	            System.err.println("Deserialized object is not an ArrayList");
	            return false; 
	        }
	    } catch (IOException | ClassNotFoundException e) {
	        System.err.println("Error while deserializing reports: " + e.getMessage());
	        return false; 
	    }
	}


}
