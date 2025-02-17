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
		while (true) {
			menuRegistration(0);
			int num = sc.nextInt();
			sc.nextLine();
			menuRegistration(num);

		}
	}

	private static void startDistribution() {
//– запуск второго периода (распределение заказов).
	}

	private static void generateFinalReport() {
//– запуск третьего периода (подведение итогов и создание отчетов).
	}

	private static void menuRegistration(int num) {
		switch (num) {
		case 0:
		 System.out.println("""
		 		Welcome to registration to this day!
		 		/tCouries - enter 1;
		 		/tOrders - enter 2;	
		 		/tAdmin - enter 3;	 		
		 		""");
			break;
		case 1:
			System.out.println("""
					This is menu for couriers!
					/tNew courier
					/tTake orders.
					""");

			break;

		default:
			break;
		}

	}
}

//	startRegistration() – запуск первого периода (регистрация заказов и курьеров).
//	startDistribution() – запуск второго периода (распределение заказов).
//	generateFinalReport() – запуск третьего периода (подведение итогов и создание отчетов).
