package system;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

public class CourierManager {
	private static final int MAX_WORK_HOURS = 11;
	private static final int NORMAL_WORK_HOURS = 8;
	private static final ArrayList NULL = null;
	private static ArrayList<Courier> courierList = new ArrayList<Courier>();
//	private static Map<String, ArrayList<Order>> courierOrders = new HashMap();
	private static boolean normal;
	private static boolean done;
	public static Scanner sc;

	/**
	 * The method sends the Category parameter to the constructor to create the courier and then returns the created courier.
	 * 
	 * @param category
	 * @return Courier
	 */
	public static Courier addCourier(String category) {
		System.out.println("Creating new Courier.");
		Courier courier = new Courier(category);
		return courier;
	}

	/**
	 * Changing status online - going online. Permission to distribute orders.
	 * 
	 * @param id (courier's ID)
	 */
	public static void changeStatusToOnline(String id) {
		Optional<Courier> courier = getCourierFromID(id);
		if (courier.isPresent()) {
			if (!courier.get().isBlockStatus()) {
				courier.get().setOnlineStatus(true);
				System.out.println("Courier id: " + courier.get().getId() + " online today.");
			} else {
				System.out.println("Courier " + id + " is block");
			}
		}
	}

	/**
	 * The method searches for a courier by ID string. Returns Optional for working with null
	 * 
	 * @param id
	 * @return Optional<Courier>
	 */
	public static Optional<Courier> getCourierFromID(String id) {
		done = false;
		Optional<Courier> courier = Optional.empty();
		courierList.forEach(c -> {
			if (c.getId().equalsIgnoreCase(id)) {
				courier.of(c);
				done = true;
			}
		});
		if (!done)
			System.out.println("Courier with id: " + id + " not found");
		return courier;
	}

	/**
	 * The method accesses the List orders by ID
	 * @param ID
	 * @return ArrayList <Order>
	 */
	public static ArrayList<Order> getCourierListOfOrdersById(String ID) {
		Optional c = getCourierFromID(ID);
		if(c.isPresent()) {
			Courier courier = (Courier)c.get();
			return courier.getListOrders();
		}
		return new ArrayList<Order>();
	}

	/**
	 * The method receives a list of distributed orders and offers them to the courier. 
	 * The courier can accept all or one at a time.
	 * @param id courier
	 * @param orders - list orders
	 */
	public static void assignCourierToOrder(String id, ArrayList<Order> orders) {
		sc = new Scanner(System.in);
		ArrayList<Order> newOne = new ArrayList<Order>();
		courierList.forEach(c -> {
			if (c.getId().equalsIgnoreCase(id)) {
				System.out.println("\nCourier " + id + " assign orders:\n");
				orders.forEach(o -> {
					System.out.println("Order " + o.getId() + ": category=" + o.getCategory()
							+ ", loading time=" + o.getLoadingTime().toMinutes() + ", travelTime="
							+ o.getTravelTime().toMinutes() + ", priority=" + o.isPriority() + "\n");
				});
				System.out.println("\nAssign all this orders? Y (for yes)/ANY KEY\n");
				if (takeAnswer()) {
					c.setListOrders(orders);
					return;
				} else {
					orders.forEach(o -> {
						System.out.println("Order " + o.getId() + "\nAssign this order? Y (for yes)/ANY KEY\n");
						if (takeAnswer()) {
							newOne.add(o);
						} else {
							o.setStatusAccepted(false);
						}
					});
					c.setListOrders(newOne);
				}
			}
		});
	}

	/**
	 * The method checks the courier's working hours and answers whether the limit has been exceeded or not.
	 * @param id
	 * @return boolean (true - more than limit)
	 */
	public static boolean checkWorkTime(String id) {
		normal = true;
		courierList.forEach(c -> {
			if (c.getId().equals(id)) {
				Duration duration = c.getWorkingHours();
				if (duration.toHours() > NORMAL_WORK_HOURS) {
					c.addPenalty();
					normal = false;
				}
				if (duration.toHours() > MAX_WORK_HOURS) {
					c.blockCourier();
				}
			}
		});
		return normal;
	}

	/**
	 * Utility method for working with client input
	 * @return boolean - as client answer
	 */
	private static boolean takeAnswer() {
		String answer = sc.nextLine().trim();
		return answer.equalsIgnoreCase("Y");
	}
	
	/**
	 * Passing method from logic (main) to class
	 */
	public static void serializeCouriers() {
		Courier.serializeCouriers();
	}


	/**
	 * Passing method from logic (main) to class
	 */
	public static void deserializeCouriers() {
		Courier.deserializeCouriers();
	}

	/**
	 * Getters and Setters
	 * 
	 */
	public static ArrayList<Courier> getCourierList() {
		return courierList;
	}

	public static void setCourierList(ArrayList<Courier> courierList) {
		CourierManager.courierList = courierList;
	}
//
//	public static Map<String, ArrayList<Order>> getCourierOrders() {
//		return courierOrders;
//	}
//
//	public static void setCourierOrders(Map<String, ArrayList<Order>> courierOrders) {
//		CourierManager.courierOrders = courierOrders;
//	}
//	

}

