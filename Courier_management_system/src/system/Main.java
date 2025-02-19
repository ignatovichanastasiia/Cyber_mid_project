package system;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

public class Main {
	private static final Object NULL = null;
	private static Scanner sc;
	private static int enterNum;

	public static void main(String[] args) {
		sc = new Scanner(System.in);
		startRegistration();
		startDistribution();
		generateFinalReport();
		sc.close();

	}

	private static void startRegistration() {
		while (true) {
			RegistrationFirstStep();
			if (CourierManager.getCourierList().size() > 1) {
				if ((Order.getOrders().size()) > 4) {
					System.out.println("Start distribution for this day? Y/N");
					if (takeAnswer()) {
						return;
					}
				}
			}
		}
	}

	private static void startDistribution() {
//		TODO
//– запуск второго периода (распределение заказов).
	}

	private static void generateFinalReport() {
//		TODO
//– запуск третьего периода (подведение итогов и создание отчетов).
	}

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

	private static void RegistrationSecondStep(int role) {
		if (role == 0) {
			System.out.println("Bye.");
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
			System.out.println("ID of new courier: " + courier.getId() + "\nTake orders for this day?Y/N");
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

//	public Order(String category, Duration loadingTime, Duration travelTime)
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
		System.out.println("Do you want to create one more order?Y/N");
		if (takeAnswer()) {
			menuOrders();
		}
	}

	// TODO
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

	private static int takeNum() {
		int num = 0;
		try {
			num = sc.nextInt();
			sc.nextLine();
		} catch (NumberFormatException e) {
			System.out.println("Wrong enter");
			num = 0;
		}
		return num;
	}

	private static boolean takeAnswer() {
		String answer = sc.nextLine().trim();
		return answer.equalsIgnoreCase("Y");
	}

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
				order.setExpectedTime(travelTime);
//				TODO
//				order.setTravelTime(travelTime);
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

	private static void bufOrdersAndCouriers() {
//		10 couriers
		for(int x = 0; x<=10;x++) {
			int random = 1+(int)((Math.random() * 3));
			Courier courier = new Courier(takeCategory(random));
		}
//		100 orders
		for(int y = 0;y<=100;y++) {
			int chooseCategory = 1+(int)((Math.random() * 3));
			long loadingTime = 1+(long)((Math.random() * 20));
			Duration lTime = Duration.ofMinutes(loadingTime);
			long travelTime = 1+(long)((Math.random() * 240));
			Duration tTime = Duration.ofMinutes(travelTime);
			Order order = new Order(takeCategory(chooseCategory),lTime,tTime);
		}

	}

	public static void deleteAllCouriers() {
		File directory = new File(Courier.getDirectoryPath());
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
		ArrayList<Courier> courierList = new ArrayList<Courier>();
	}

	//TODO
	private static void deleteAllOrders() {
//		File directory = new File(Order.getBasePath(),Order.getOrders();
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

//	startRegistration() – запуск первого периода (регистрация заказов и курьеров).
//	startDistribution() – запуск второго периода (распределение заказов).
//	generateFinalReport() – запуск третьего периода (подведение итогов и создание отчетов).
