package system;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Optional;
import java.util.Scanner;

public class Main {
	private static final Object NULL = null;
	private static Scanner sc;
	private static int enterNum;
	private static boolean done;

	/**
	 * The main method contains the primary methods that control the activation of
	 * different stages of the program's operation. The main stages are: 1.
	 * registration of couriers and orders; 2. assignment of orders to available
	 * couriers; 3. generating a report on the results of the work.
	 * 
	 * (RUS)В методе main находятся основные методы, управляющие включением разных
	 * этапов работы программы. Основные этапы: 1. регистрация курьеров и заказов;
	 * 2. распределение заказов по имеющимся курьерам; 3. составление отчета по
	 * итогам работы.
	 */
	public static void main(String[] args) {
		sc = new Scanner(System.in);
		startRegistration();
		startDistribution();
		generateFinalReport();
		sc.close();
		CourierManager.sc.close();

	}

	/**
	 * The method starts the first stage of the program's operation – registration.
	 * Logically, the first step is the deserialization of already existing objects:
	 * these are the couriers in the database and the orders that are already in the
	 * database. Then, the menu is activated, offering various options for working
	 * with couriers, handling orders, as well as administration tasks: making
	 * changes to various objects, printing lists of all objects, and preparing
	 * random files for the program's operation in test mode.
	 * 
	 * (RUS)Метод запускает первый этап работы программы - регистрацию По логике,
	 * сначала идет десериализация уже имеющихся объектов: это курьеры, которые есть
	 * в базе и заказы, которые уже есть в базе. Далее включается меню, в котором
	 * предлагаются различные меню для работы курьеров, для работы с заказами, а так
	 * же для администрирования: внесения изменений в различные объекты,
	 * распечатывания списков всех объектов, а так же подготовки рандомных файлов
	 * для работы программы в тестовом режиме.
	 * 
	 */
	private static void startRegistration() {
		deserializationForStart();
		while (true) {
			RegistrationFirstStep();
			done = false;
			if (CourierManager.getCourierList().size() > 1 && Order.getOrders().size() > 4) {
				CourierManager.getCourierList().forEach(c -> {
					if (c.isOnlineStatus()) {
						done = true;
					}
				});
				if (done) {
					System.out.println("Start distribution for this day? Y (for yes)/ANY KEY");
					if (takeAnswer()) {
						return;
					}
				}
			}
			System.out.println("We back to registration!");
		}
	}

	/**
	 * This method triggers the order distribution process from the database to the
	 * couriers available in the database and currently in online status. Couriers
	 * who receive orders must sequentially confirm their workload.
	 * 
	 * (RUS) Этот метод запускает метод распределения заказов из базы по курьерам,
	 * имеющимся в базе и находящихся в онлайн статусе. Курьеры, получившие
	 * распределение должны поочередно подтвердить нагрузку.
	 */
	private static void startDistribution() {
		OrderManager.distributeOrders();
	}

	/**
	 * This method starts the third stage of the program's operation: summarizing
	 * the results and generating reports.
	 * 
	 * (RUS) Этот метод запускает третий этап работы программы: подведение итогов и
	 * создание отчетов.
	 */
	private static void generateFinalReport() {
		Report.generateReport();
		try {
			CourierManager.serializeCouriers();
			Order.serializeOrders();
		} catch (Exception e) {
			System.out.println("Problem with serialization: " + e.getMessage());
		}
		System.out.println("This day is over.");
	}

	/**
	 * First logis step.
	 * Actually restoring couriers and orders from the file.
	 */
	private static void deserializationForStart() {
		try {
			CourierManager.deserializeCouriers();
			Order.deserializeOrders();
		} catch (Exception e) {
			System.out.println("Problem with deserialization: " + e.getMessage());
		}
	}

	/**
	 * The first part of the customer's interaction with the menu
	 */
	private static void RegistrationFirstStep() {
		do {
			System.out.println("""
					Welcome to registration to this day!\n
					\tCouries - enter 1.
					\tOrders - enter 2.
					\tAdmin - enter 3.
					\tExit registration - enter 0.
					""");
			enterNum = takeNum();
			RegistrationSecondStep(enterNum);
		} while (enterNum != 0);
	}
	
	/**
	 * The second part of the customer's interaction with the menu.
	 * Scattering of the menu after entering data about the client’s role.
	 */
	private static void RegistrationSecondStep(int role) {
		if (role == 0) {
			System.out.println("\n");
			return;
		}
		switch (role) {
		case 1:
			menuCourier();
			break;
		case 2:
			menuOrders();
			break;
		case 3:
			menuAdmin();
			break;
		default:
			System.out.println("""
					Wrong number. Enter only numbers from the menues. Try again!
					""");
			break;
		}

	}
	
	/**
	 * Menu for couriers. Registration, access to the line (online status).
	 */
	private static void menuCourier() {
		System.out.println("""
				This is menu for couriers!\n
				\tNew courier - enter 1.
				\tTake orders for this day (get online status) - enter 2.
				\tBack - enter 0.
				""");
		enterNum = takeNum();
		if (enterNum == 0) {
			return;
		}
		switch (enterNum) {
		case 1:
			System.out.println("""
					Let's registration a new courier:
					Choose category:\n
					\tRefrigerated - enter 1.
					\tOversize - enter 2.
					\tLight - enter 3.
					\tExit - enter 0.
					""");
			enterNum = takeNum();
			if (enterNum == 0) {
				break;
			}
			Courier courier = CourierManager.addCourier(takeCategory(enterNum));
			System.out.println("ID of new courier: " + courier.getId() + "\nTake orders for this day?Y (for yes)/ANY KEY");
			if (takeAnswer()) {
				CourierManager.changeStatusToOnline(courier.getId());
			}
			break;
		case 2:
			System.out.println("""
					Let's try to change your status to online and take some orders for this day.
					Ender your ID:
					""");
			String clientId = sc.nextLine();
			CourierManager.changeStatusToOnline(clientId);
			break;
		default:
			System.out.println("""
					Wrong number. Enter only numbers from the menues. Try again!
					""");
			break;
		}
	}

	/**
	 * Takes parameters for creating an order from client input
	 * (Order(String category, Duration loadingTime, Duration travelTime)).
	 * 
	 * @param category
	 * @param loadingTime
	 * @param travelTime
	 */

	private static void menuOrders() {
		System.out.println("""
				This is menu for orders!\n
				Let's do new order!\n
				Choose category:\n
				\tRefrigerated - enter 1.
				\tOversize - enter 2.
				\tLight - enter 3.
				\tBack - enter 0.
				""");
		enterNum = takeNum();
		if (enterNum == 0) {
			return;
		}
		String category = takeCategory(enterNum);
		System.out.println("Enter loadingTime in minutes: ");
		long minutes = (long) takeNum();
		Duration loadingTime = Duration.ofMinutes(minutes);
		System.out.println("Enter travelTime in minutes: ");
		minutes = (long) takeNum();
		Duration travelTime = Duration.ofMinutes(minutes);
		Order order = new Order(category, loadingTime, travelTime);
		System.out.println("Order was created: " + order.getId());
		System.out.println("Do you want to create one more order?Y (for yes)/ANY KEY");
		if (takeAnswer()) {
			menuOrders();
		}
	}

	/**
	 * The administrator menu allows you to obtain and manage all data. 
	 * It is also possible to create random couriers and random orders for the program to work in test mode.
	 * 
	 */
	private static void menuAdmin() {
		System.out.println("""
				This is admin's menu!\n
				\tEdit courier - enter 1.
				\tEdit order - enter 2.
				\tAll couriers - enter 3.
				\tAll orders - enter 4.
				\tTake buf of couriers and orders for test - enter 5
				\tDelete all couriers - enter 6.
				\tDelete all orders - enter 7.
				\tBack - enter 0.
				""");
		enterNum = takeNum();
		if (enterNum == 0) {
			return;
		}
		switch (enterNum) {
		case 1:
			System.out.println(CourierManager.getCourierList().toString());
			System.out.println("\nEnter courier's ID: ");
			String clientID = sc.nextLine().trim();
			Optional<Courier> clientCourier = CourierManager.getCourierFromID(clientID);
			if (clientCourier.isPresent()) {
				changeCourierParam(clientCourier.get());
			}
			break;
		case 2:
			System.out.println(Order.getOrders().toString());
			System.out.println("\nEnter order's ID: ");
			clientID = sc.nextLine().trim();
			Order order = Order.findOrderById(clientID);
			if (order.equals(NULL)) {
				System.out.println("This order is null.");
				break;
			}
			changeOrderParam(order);
			break;
		case 3:
			System.out.println(CourierManager.getCourierList().toString());
			break;
		case 4:
			System.out.println(Order.getOrders().toString());
			break;
		case 5:
			bufOrdersAndCouriers();
			break;
		case 6:
			deleteAllCouriers();
			break;
		case 7:
			deleteAllOrders();
			break;
		case 0:
			break;
		default:
			System.out.println("""
					Wrong number. Enter only numbers from the menues. Try again!
					""");
			break;
		}
	}

	/**
	 * Utility method to get the Category parameter from client input
	 * 
	 * @param intCategory
	 * @return String - Category
	 */
	private static String takeCategory(int intCategory) {
		String strCategory = "null";
		switch (intCategory) {
		case 1:
			strCategory = "refrigerated";
			break;
		case 2:
			strCategory = "oversize";
			break;
		case 3:
			strCategory = "light";
			break;
		default:
			System.out.println("Wrong number. Enter 1 or 2 or 3: ");
			strCategory = takeCategory(intCategory);
			break;
		}
		return strCategory;
	}

	/**
	 * Utility method to get number
	 * @return number (int)
	 */
	private static int takeNum() {
		int num = 0;
		try {
			num = sc.nextInt();
			sc.nextLine();
		} catch (NumberFormatException e) {
			System.out.println("Wrong enter");
			num = 0;
		}catch(InputMismatchException e) {
			System.out.println("Wrong enter");
			num = 0;
		}
		return num;
	}

	/**
	 * Utility method to get boolean from client input
	 * @return boolean - client's answer
	 */
	private static boolean takeAnswer() {
		String answer = sc.nextLine().trim();
		return answer.equalsIgnoreCase("Y");
	}

	/**
	 * The method allows the administrator to change the courier data by entering data into the console.
	 * @param courier
	 */
	private static void changeCourierParam(Courier courier) {
		System.out.println("""
				Enter number of changed parameter:
						\tcategory - enter 1,
						\tworkingHours - enter 2,
						\tpenalty - enter 3,
						\tblockStatus - enter 4,
						\tonlineStatus - enter 5.
						""");
		enterNum = takeNum();
		System.out.println("Enter new value: ");
		String value = sc.nextLine();
		try {
			switch (enterNum) {
			case 1:
				courier.setCategory(value);
				break;
			case 2:
				Duration workingHours = Duration.ofHours(Long.parseLong(value.trim()));
				courier.setWorkingHours(workingHours);
				System.out.println("Woking hours was changed: " + courier.toString());
				break;
			case 3:
				int pen = Integer.parseInt(value.trim());
				courier.setPenalty(pen);
				System.out.println("Penalty was changed: " + courier.toString());
				break;
			case 4:
				int bStatus = Integer.parseInt(value.trim());
				courier.setBlockStatus(bStatus);
				System.out.println("Block status was changed: " + courier.toString());
				break;
			case 5:
				boolean isOnline = Boolean.valueOf(value);
				courier.setOnlineStatus(isOnline);
				System.out.println("Online status was changed: " + courier.toString());
				break;
			}

		} catch (Exception e) {
			System.out.println("Cast problem: " + e.getMessage());
		}
	}

	/**
	 * The method allows the administrator to change the order data by entering data into the console.
	 * @param order
	 */
	private static void changeOrderParam(Order order) {
		System.out.println("""
				Enter number of changed parameter:
						\tcategory - enter 1,
						\tloadingTime - enter 2,
						\ttravelTime - enter 3,
						\tpriority - enter 4,
						\tstatusComplete - enter 5,
						\tstatusAccepted - enter 6.
						""");

		enterNum = takeNum();
		System.out.println("Enter new value: ");
		String value = sc.nextLine();
		try {
			switch (enterNum) {
			case 1:
				order.setCategory(value);
				break;
			case 2:
				Duration loadingTime = Duration.ofHours(Long.parseLong(value.trim()));
				order.setLoadingTime(loadingTime);
				System.out.println("Loading time was changed: " + order.toString());
				break;
			case 3:
				Duration travelTime = Duration.ofHours(Long.parseLong(value.trim()));
				order.setTravelTime(travelTime);
				System.out.println("Travel time was changed: " + order.toString());
				break;
			case 4:
				boolean priority = Boolean.valueOf(value.trim());
				order.setPriority(priority);
				System.out.println("Priority was changed: " + order.toString());
				break;
			case 5:
				boolean statusComplete = Boolean.valueOf(value);
				order.setStatusComplete(statusComplete);
				System.out.println("Status complete was changed: " + order.toString());
				break;
			case 6:
				boolean statusAccepted = Boolean.valueOf(value);
				order.setStatusAccepted(statusAccepted);
				System.out.println("Status accepted was changed: " + order.toString());
				break;
			}

		} catch (Exception e) {
			System.out.println("Cast problem: " + e.getMessage());
		}
	}

	/**
	 * Method creates 10 couriers and 100 orders for testing program
	 */
	private static void bufOrdersAndCouriers() {
//		10 couriers
		for (int x = 0; x <= 10; x++) {
			int random = 1 + (int) ((Math.random() * 3));
			Courier courier = new Courier(takeCategory(random));
			courier.setOnlineStatus(true);
		}
//		100 orders
		for (int y = 0; y <= 100; y++) {
			int chooseCategory = 1 + (int) ((Math.random() * 3));
			long loadingTime = 1 + (long) ((Math.random() * 20));
			Duration lTime = Duration.ofMinutes(loadingTime);
			long travelTime = 1 + (long) ((Math.random() * 240));
			Duration tTime = Duration.ofMinutes(travelTime);
			Order order = new Order(takeCategory(chooseCategory), lTime, tTime);
		}

	}

	/**
	 * The method allows the administrator to erase all data about 
	 * couriers (both in the list and serialized files in the folder)
	 */
	public static void deleteAllCouriers() {
		File directory = new File("couriers");
		// Delete the file or directory
		if (directory.exists()) {
			if (directory.isDirectory()) {
				File[] files = directory.listFiles();
				if (files != null) {
					for (File file : files) {
						if (!file.isDirectory()) {
							// Delete individual files
							file.delete();
						}
					}
				}
			}
		}
		CourierManager.setCourierList(new ArrayList<Courier>());
	}

	/**
	 * The method allows the administrator to erase all data about orders 
	 * (both in the list and serialized files in the folder)
	 * 
	 */
	private static void deleteAllOrders() {
		File directory = new File("orders");
		// Delete the file or directory
		if (directory.exists()) {
			if (directory.isDirectory()) {
				File[] files = directory.listFiles();
				if (files != null) {
					for (File file : files) {
						if (!file.isDirectory()) {
							// Delete individual files
							file.delete();
						}
					}
				}
			}
		}
		Order.setOrders(new ArrayList<Order>());
	}
}

