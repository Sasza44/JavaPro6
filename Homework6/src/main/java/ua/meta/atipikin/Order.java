package ua.meta.atipikin;

import java.time.LocalDate;
import java.util.List;

public class Order {
	@Id
	private int number;
	private String varieties; // список назв сортів
	private int clientId;
	private LocalDate date;
	private int sum;
	public Order() {
	}
	public Order(int number, String varieties, int clientId, LocalDate date, int sum) {
		super();
		this.number = number;
		this.varieties = varieties;
		this.clientId = clientId;
		this.date = date;
		this.sum = sum;
	}
	public int getNumber() {return number;}
	public void setNumber(int number) {this.number = number;}
	public String getVarieties() {return varieties;}
	public void setVarieties(String varieties) {this.varieties = varieties;}
	public int getClientId() {return clientId;}
	public void setClientId(int clientId) {this.clientId = clientId;}
	public LocalDate getDate() {return date;}
	public void setDate(LocalDate date) {this.date = date;}
	public int getSum() {return sum;}
	public void setSum(int sum) {this.sum = sum;}
	@Override
	public String toString() {
		return "Order [number=" + number + ", varieties=" + varieties + ", clientId=" + clientId + ", date=" + date+ ", sum=" + sum + "]";
	}
}