package system;

import java.util.Scanner;

public class Main {
	private static Scanner sc;

	public static void main(String[] args) {
		sc = new Scanner(System.in);
		startRegistration();
		sc.close();

	}

	private static void startRegistration() {
//		while (true) {
		menuRegistration(10);
		int role = takeNum();
		menuRegistration(role);
		int num = takeNum();
		switch (role) {
		case 1:
			menuCourier(num);
			break;
		case 2:
			menuOrders(num);
			break;
		case 3:
			menuAdmin(num);
			break;
		}

//		}
	}

	private static void startDistribution() {
//– запуск второго периода (распределение заказов).
	}

	private static void generateFinalReport() {
//– запуск третьего периода (подведение итогов и создание отчетов).
	}

	// Switch 1round(default 10) - role enter
	// Switch 2round(with role) - choose in menu
	private static void menuRegistration(int num) {
		switch (num) {
		case 10:
			System.out.println("""
					Welcome to registration to this day!\n
					\tCouries - enter 1.
					\tOrders - enter 2.
					\tAdmin - enter 3.
					""");
			break;
		case 1:
			System.out.println("""
					This is menu for couriers!\n
					\tNew courier - enter 1.
					\tTake orders for this day (get online status) - enter 2.
					""");
			break;
		case 2:
//			public Order(String category, Duration loadingTime, Duration travelTime)
//			REFRIGERATED, OVERSIZE, LIGHT
			System.out.println("""
					This is menu for orders!\n
					Let's do new order!\n
					Choose category:\n
					\tRefrigerated - enter 1.
					\tOversize - enter 2.
					\tLight - enter 3.
					""");
		case 3:
			System.out.println("""
					This is admin's menu!\n
					\tEdit courier - enter 1.
					\tEdit order - enter 2.
					\tAll couriers - enter 3.
					\tAll orders - enter 4.
					\tTake buf of couriers and orders for test
					\tDelete all couriers.
					\tDelete all orders.
					""");
			break;
		default:
			System.out.println("""
					Wrong number. Enter only numbers from the menues. Try again!
					""");
			break;
		}

	}

	private static void menuCourier(int num) {
		switch (num) {
		case 1:
			System.out.println("""
					Let's registration a new courier:
					Choose category:\n
					\tRefrigerated - enter 1.
					\tOversize - enter 2.
					\tLight - enter 3.
					""");
			Courier courier = CourierManager.addCourier(takeCategory(takeNum()));
			System.out.println("ID of new courier: " + courier.getId() + "\nTake orders for this day?Y/N");
			String agree = sc.nextLine();
			if(agree.trim().equalsIgnoreCase("Y")) {
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

	private static void menuOrders(int num) {
		switch (num) {
		case 1:
			break;
		case 2:
			break;
		default:
			System.out.println("""
					Wrong number. Enter only numbers from the menus. Try again!
					""");
			break;
		}

	}

	private static void menuAdmin(int num) {
		switch (num) {
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
}

//	startRegistration() – запуск первого периода (регистрация заказов и курьеров).
//	startDistribution() – запуск второго периода (распределение заказов).
//	generateFinalReport() – запуск третьего периода (подведение итогов и создание отчетов).
