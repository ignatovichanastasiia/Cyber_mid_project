package system;

import java.io.Serializable;
import java.time.Duration;
import java.util.ArrayList;

public class Report implements Serializable{

	private static final long serialVersionUID = 1L;
	private static Duration workingTime; // это все данные с заказа: грузим, едем, проблемы в дороге
	private ArrayList <Order> uncompletedOrders = new ArrayList<Order>();
	private ArrayList <Order> completedOrders = new ArrayList<Order>();
	private ArrayList <Courier> couriers;
	
	public Report(ArrayList<Order> uncompletedOrders, ArrayList<Order> completedOrders, ArrayList<Courier> couriers) {
		this.uncompletedOrders = uncompletedOrders;
		this.completedOrders = completedOrders;
		this.couriers = CourierManager.getCourierList();
		calculateTravelTime();
	}

	private void calculateTravelTime() {
        workingTime = Duration.ZERO;
    	for(Order order: Order.getOrders())
        if (order.isStatusAccepted()) {
        	workingTime = order.getExpectedTime().plus(order.getLoadingTime()).plus(order.getDelayTime());
        } 
	}


	public static void generateReport() {
		//Отчет по общим рабочим часам (подборка из выполненных заказов)
		// Общее число выполненных и не выполненных заказов.
		// Разбивка по курьерам и заказам, с указанимем, кто сколько заработал, 
		//сколько заказов выполнил, сколько часов отработ, премия и штрафы
	}

	
	
//	Методы: 
//	 generateReport() – создание итогового отчета.
//	serializeReport() – сериализация отчета.
//	deserializeReport() – десериализация отчета.  

	
	
}
