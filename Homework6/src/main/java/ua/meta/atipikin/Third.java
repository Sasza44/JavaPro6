package ua.meta.atipikin;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Third {
	
	public static void main(String[] args) {
		try (Connection con = ConnectionFactory.getConnection()) {
			ProductDAO productDAO = new ProductDAO(con, "product");
			try {
				System.out.println("The connection is successful"); // перевірка підключення до бази даних
				productDAO.getAll(Product.class, "1.txt"); // записуємо список наявних товарів у файл
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
				e.printStackTrace();
			}
			selectAction(con, productDAO); // вибір дії
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void selectAction(Connection con, ProductDAO productDAO) { // вибір дії
		Scanner sc = new Scanner(System.in);
		String select = "0";
		System.out.println("1: add client"); // додати покупця
		System.out.println("2: update client"); // змінити дані покупця
		System.out.println("3: add order (select varieties in 1.txt file)"); // додати замовлення
		System.out.println("-> ");
		select = sc.nextLine();
		switch(select) {
		case "1":
			try {
				addClient(con); // додати покупця
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
			break;
		case "2":
			try {
				updateClient(con); // змінити дані покупця
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException
					| SQLException e) {
				e.printStackTrace();
			}
			break;
		case "3":
			try {
				addOrder(con, productDAO);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException
					| SQLException e) {
				e.printStackTrace();
			} // додати замовлення
			break;
		default:
			break;
		}
	}

	public static void addClient(Connection con) throws IllegalArgumentException, IllegalAccessException { // додати покупця
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter client name: ");
		String name = sc.nextLine();
		System.out.println("Enter client phone: ");
		String phone = sc.nextLine();
		
		Client client = new Client();
		client.setName(name);
		client.setPhone(phone);
		
		ClientDAO clientDAO = new ClientDAO(con, "client");
		clientDAO.add(client);
	}
	
	public static void updateClient(Connection con) throws InstantiationException, IllegalAccessException, IllegalArgumentException,
	InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException, SQLException { // змінити дані покупця
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter client ID: "); // вводимо id покупця
		int id = sc.nextInt();
		
		System.out.println("Enter client name: "); // вводимо змінене ім'я покупця
		String name = sc.nextLine(); // хтозна в чому справа, але тут при одному записі не дає ввести name
		name = sc.nextLine();
		
		System.out.println("Enter client phone: "); // вводимо змінений телефон покупця
		String phone = sc.nextLine();
		
		ClientDAO clientDAO = new ClientDAO(con, "client");
		Client client = clientDAO.read(Client.class, Integer.toString(id)); // шукаємо покупця з даним id, щоб можна залишити деякі дані за замовчуванням
		System.out.println(client);
		if(name != "") client.setName(name); // можливість не змінювати ім'я за замовчуванням
		if(phone != "") client.setPhone(phone); // можливість не змінювати номер телефону за замовчуванням
		clientDAO.update(client);
	}
	
	private static void addOrder(Connection con, ProductDAO productDAO) throws InstantiationException, IllegalAccessException,
	IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException, SQLException { // додати замовлення
		Scanner sc = new Scanner(System.in);
		OrderDAO orderDAO = new OrderDAO(con, "orders.order"); // саме в цю таблицю не вставляє дані без вказання назви бази
		Order order = new Order(); // створюємо нове замовлення
		StringBuilder varieties = new StringBuilder(); // перелік назв сортів для опису замовлення
		List<Product> productList = new ArrayList<>(); // список сортів для розрахунку ціни замовлення
		
		String addSelect = "add select";
		while(addSelect != "") {
			System.out.println("Оберіть сорт саджанця");
			addSelect = sc.nextLine();
			Product product = (Product) productDAO.read(Product.class, addSelect); // зчитуємо дані про обраний сорт
			if(product.getQuantity() > 0) { // додаємо, якщо є в наявності
				product.setQuantity(product.getQuantity() - 1);
				productDAO.update(product);
				varieties.append(product.getKind()).append(" ").append(product.getVariety()).append(", ");
				productList.add(product);
			}
			else if(product.getQuantity() > 0) System.out.println("Такий сорт закінчився");
		}
		varieties.delete(varieties.length() - 2, varieties.length() - 1); // видаляємо кому з пробілом у кінці
		
		order.setVarieties(varieties.toString()); // оформлюємо замовлення
		System.out.println("Enter client's id :");
		int clientId = sc.nextInt(); // вказуємо id покупця
		order.setClientId(clientId);
		order.setDate(LocalDate.now()); // автоматично ставиться сьогоднішня дата
		int sum = 0; // сума замовлення
		for(Product p: productList) {
			sum += p.getPrice();
		}
		order.setSum(sum);
		System.out.println(order);
		orderDAO.add(order); // додаємо об'єкт замовлення в таблицю бази
	}
}