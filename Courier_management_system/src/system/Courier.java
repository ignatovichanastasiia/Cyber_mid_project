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
	private static final String ID_TEXT = "courier";
	private static final String DIRECTORY_PATH = "serializ";
	private static Scanner sc = new Scanner(System.in);
	private String id;
	private String category;
	private long workingHours;
	private int penalty;
	private int blockStatus;
	
	/*
	 * Конструктор берет атрибутом категорию перевозки. Все остальные атрибуты генерируются в процессе 
	 * создания объекта. Частью id является дата и время регистрации курьера. Так же внутри конструктора 
	 * создающийся объект сразу попадает в лист курьеров в классе КурьерМенеджера
	 * 
	 * The constructor takes the transportation category as an attribute. All other attributes are 
	 * generated during the object creation process. Part of the id is the date and time the courier 
	 * was registered. Also, inside the constructor, the created object immediately goes to the list 
	 * of couriers in the CourierManager class
	 * 
	 */
	public Courier(String category) {

		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

		this.id = ID_TEXT + now.format(formatter);
		this.category = category;
		this.workingHours = 0;
		this.penalty = 0;
		this.blockStatus = 0;
		
		ArrayList<Courier> couriers = new ArrayList<Courier>(CourierManager.getCourierList());
		couriers.add(this);
		CourierManager.setCourierList(couriers);
	}

	/*
	 * Подтверждение принятия заказа. Метод берет список распределенных заказов, а
	 * возвращает список принятых. 
	 * 
	 * Confirmation of order acceptance. The method
	 * takes a list of distributed orders and returns a list of accepted orders.
	 */
	public ArrayList<Order> confirmOrder(ArrayList<Order> orders) {
		ArrayList<Order> confirmList = new ArrayList<Order>();
		orders.forEach(o -> {
			System.out.println("Add order? Y/N\n" + o.toString());
			String answer = sc.nextLine();
			if (answer.equalsIgnoreCase("Y")) {
				confirmList.add(o);
			}
		});
		return confirmList;

	}

	/*
	 * Расчет зарплаты с учетом штрафов и премий. 
	 * 
	 * Salary calculation taking into
	 * account fines and bonuses.
	 * 
	 */
	public double calculateSalary(double salary, double penaltyCost) {
		Duration duration = Duration.ofHours(workingHours);
		double hours = duration.toHours();
		double totalPenalty = penalty * penaltyCost;
		penalty = 0;
		return hours * salary - totalPenalty;
	}

	/*
	 * Добавляет единицу штрафа. Adds penalty.
	 */
	public int addPenalty() {
		return ++penalty;
	}

	/*
	 * Блокирует курьера на 2 дня из-за превышения лимита рабочих часов. 
	 * 
	 * Blocks the
	 * courier for 2 days due to exceeding the working hours limit.
	 */
	public void blockCourier() {
		if (blockStatus > 0) {
			throw new IllegalArgumentException(
					"Logic error: a blocked courier took over or the wrong courier received the block.");
		}
		blockStatus = 2;
	}

	/*
	 * Сериализация данных курьера. 
	 * 
	 * Serialization of courier data.
	 */
	public boolean serialize() {
		File directoryForSerialization = new File(DIRECTORY_PATH);
		File serFile = new File(DIRECTORY_PATH, id + ".ser");
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(serFile))) {
			if (!directoryForSerialization.exists()) {
				directoryForSerialization.mkdir();
			}
			serFile.createNewFile();
			System.out.println("Created file for serialization.");
			out.writeObject(this);
			System.out.println("Courier was serialized into file: " + serFile.getPath());
			return true;

		} catch (IOException e) {
			System.out.println("We have some problem with saving courier files: " + e.getMessage());
			return false;
		}
	}

	/*
	 * Десериализация данных курьера из файла. 
	 * 
	 * Deserialization of courier data from
	 * a file.
	 */
	public boolean deserialize() {
		File serFile = new File(DIRECTORY_PATH, id + ".ser");
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(serFile.getPath()))) {
			Courier deserializedEntry = (Courier) in.readObject();
			if (blockStatus > 0) {
				this.blockStatus = --blockStatus;
			}
			System.out.println("Courier was deserialized: " + deserializedEntry.id);
			System.out.println(deserializedEntry.toString());
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("We have troble with deserialization: " + e.getMessage());
			return false;
		}
		return true;
	}


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
		return Duration.ofHours(workingHours);
	}

	public void setWorkingHours(Duration workingHours) {
		this.workingHours = workingHours.toHours();
	}

	public int getPenalty() {
		return penalty;
	}

	public void setPenalty(int penalty) {
		this.penalty = penalty;
	}

	public boolean isBlockStatus() {
		return blockStatus > 0;
	}

	@Override
	public String toString() {
		return "Courier [id=" + id + ", category=" + category + ", workingHours=" + workingHours + ", penalty="
				+ penalty + ", blockStatus=" + blockStatus + "]";
	}
	

}

//o	Атрибуты: 
//	id (идентификатор курьера)
//	category (категория машины)
//	workingHours (время работы)
//	penalty (штрафы)
//	blockStatus (блокировка)
//o	Методы: 
//	confirmOrder() – подтверждение принятия заказа.
//	calculateSalary() – расчет зарплаты с учетом штрафов и премий.
//	addPenalty() – добавление штрафа.
//	blockCourier() – блокировка курьера.
//	serialize() – сериализация данных курьера.
//	deserialize() – десериализация данных курьера из файла.
