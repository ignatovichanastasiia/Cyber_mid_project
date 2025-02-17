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
		int role = sc.nextInt();
		sc.nextLine();
		menuRegistration(role);
		int num = sc.nextInt();
		sc.nextLine();
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
		menuWithRoles(num);

//		}
	}

	private static void startDistribution() {
//– запуск второго периода (распределение заказов).
	}

	private static void generateFinalReport() {
//– запуск третьего периода (подведение итогов и создание отчетов).
	}


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
					\tTake orders - enter 2.
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

			break;
		case 2:
			//TODO 
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
					Wrong number. Enter only numbers from the menues. Try again!
					""");
			break;
		}

	}

	private static menuAdmin(int num) {
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

	private static int takeNum(){
		try {
			
		int num = sc.nextInt();
		sc.nextLine();
		} catch (NumberFormatException e) {
            System.out.println("Wrong enter");
        }catch(Exception e) {
        	System.out.println("Wrong number");
		}
	}
}

//	startRegistration() – запуск первого периода (регистрация заказов и курьеров).
//	startDistribution() – запуск второго периода (распределение заказов).
//	generateFinalReport() – запуск третьего периода (подведение итогов и создание отчетов).
