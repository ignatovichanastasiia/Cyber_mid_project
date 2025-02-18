package system;

import java.time.Duration;
import java.util.Scanner;

public class Main {
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

	//TODO
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
			break;
		case 2:
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
}

//	startRegistration() – запуск первого периода (регистрация заказов и курьеров).
//	startDistribution() – запуск второго периода (распределение заказов).
//	generateFinalReport() – запуск третьего периода (подведение итогов и создание отчетов).
