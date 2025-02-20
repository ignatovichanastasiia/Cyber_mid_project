package system;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class Courier implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final int ORDERS_FOR_BONUS = 5;
	private static final String ID_TEXT = "courier";
	private static final String DIRECTORY_PATH = "serializ";
	private static final String NULL = null;
	private static final double SALARY = 60;
	private static final double PENALTY_COST = 250;
	private static final double BONUS_COST = 500;
	private static int counter = 0;
	

	private static Scanner sc = new Scanner(System.in);
	private String id;
	private String category; //// REFRIGERATED, OVERSIZE, LIGHT
	private Duration workingHours;
	private int penalty;
	private int blockStatus;
	private boolean onlineStatus;
	private int bonusPoints;
	private ArrayList<Order> listOrders;

	/**
	 * The constructor takes the transportation category as an attribute. All other
	 * attributes are generated during the object creation process. Part of the id
	 * is the date and time the courier was registered. Also, inside the
	 * constructor, the created object immediately goes to the list of couriers in
	 * the CourierManager class.
	 */
	public Courier(String category) {
		this.id = genID();
		this.category = category;
		this.workingHours = Duration.ofHours(0);
		this.penalty = 0;
		this.blockStatus = 0;
		this.onlineStatus = false;
		this.listOrders = new ArrayList<Order>();

		ArrayList<Courier> couriers = new ArrayList<Courier>(CourierManager.getCourierList());
		couriers.add(this);
		CourierManager.setCourierList(couriers);
	}

	/**
	 * Salary calculation taking into account fines and bonuses. 
	 *@return double salary 
	 */
	public double calculateSalary() {
		Duration duration = workingHours;
		double hours = duration.toHours();
		double totalPenalty = penalty * PENALTY_COST;
		double totalBonus = bonusPoints * BONUS_COST;
		return hours * SALARY - totalPenalty + totalBonus;
	}
	
	/**
	 * Adds penalty and return number of penalties.
	 * @return number (int)
	 */
	public int addPenalty() {
		return ++penalty;
	}

	/**
	 * Blocks the courier for 2 days due to exceeding the working hours limit.
	 * 
	 */
	public void blockCourier() {
		if (blockStatus > 0) {
			throw new IllegalArgumentException(
					"Logic error: a blocked courier took over or the wrong courier received the block.");
		}
		blockStatus = 2;
	}	
	
	/**
	 * Serializes the static courier list to a specified file.
	 * 
	 *@return boolean about serilization (done or not)
	 */
	public static boolean serializeCouriers() {
		File directoryForSerialization = new File(DIRECTORY_PATH);
		File serFile = new File(DIRECTORY_PATH, "couriers.ser");
		if (!directoryForSerialization.exists()) {
			directoryForSerialization.mkdir();
		}
		if(!serFile.exists()) {
			try {
			serFile.createNewFile();
			System.out.println("Created file for serialization.");
			}catch(IOException e) {
				System.out.println("Problem with creating file for serialization: "+e.getMessage());
			}
		}
		try (ObjectOutputStream fileCouriersOut = new ObjectOutputStream(new FileOutputStream(serFile))) {
			fileCouriersOut.writeObject(CourierManager.getCourierList());
			System.out.println("Couriers were serialized into file: " + serFile.getPath());
			return true;
		} catch (IOException e) {
			System.out.println("We have some problem with saving courier files: " + e.getMessage());
			return false;
		}
	}

	/**
	 * Deserializes the couriers list from a specified file.
	 *
	 * @return deserialized list of couriers
	 * 
	 */
	public static ArrayList<Courier> deserializeCouriers() {
		File userFile = new File(DIRECTORY_PATH, "couriers.ser");
		ArrayList<Courier> couriers = new ArrayList<Courier>();
		if (userFile.exists()) {
			try (ObjectInputStream fileCourier = new ObjectInputStream(new FileInputStream(userFile))) {
				 couriers = (ArrayList<Courier>) fileCourier.readObject();
				 CourierManager.setCourierList(couriers);
			} catch (IOException | ClassNotFoundException e) {
				System.err.println("Error while finding an order: " + e.getMessage());
			}
		}
		return couriers;
	}
	
	/**
	 * Getters and Setters
	 * 
	 */

	public String getId() {
		return id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Duration getWorkingHours() {
		return workingHours;
	}

	public void setWorkingHours(Duration workingHours) {
		this.workingHours = workingHours;
	}

	public int getPenalty() {
		return penalty;
	}

	public void setPenalty(int penalty) {
		this.penalty = penalty;
	}

	public boolean isOnlineStatus() {
		return onlineStatus;
	}

	public void setOnlineStatus(boolean onlineStatus) {
		this.onlineStatus = onlineStatus;
		if(onlineStatus) System.out.println("Courier "+id+" is online! Welcome to this day!");
	}

	public boolean isBlockStatus() {
		return blockStatus > 0;
	}

	public int getBlockStatus() {
		return blockStatus;
	}

	public void setBlockStatus(int blockStatus) {
		this.blockStatus = blockStatus;
	}

	public ArrayList<Order> getListOrders() {
		return listOrders;
	}

	public void setListOrders(ArrayList<Order> listOrders) {
		this.listOrders = listOrders;
	}

	@Override
	public String toString() {
		return "Courier [id=" + id + ", category=" + category + ", workingHours=" + workingHours + ", penalty="
				+ penalty + ", blockStatus=" + blockStatus + ", onlineStatus=" + onlineStatus + "]\n";
	}
	
	public static String getDirectoryPath() {
		return DIRECTORY_PATH;
	}
	
	/**
	 * Utility method generates ID
	 * 
	 * @return ID
	 */
	private static String genID() {
		counter++;
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		return ID_TEXT + now.format(formatter)+counter;
	}

}