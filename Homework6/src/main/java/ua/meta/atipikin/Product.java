package ua.meta.atipikin;

public class Product {
	@Id
	private String variety;
	private String kind;
	private String description;
	private int price;
	private int quantity;
	public Product() {
	}
	public Product(String variety, String kind, String description, int price, int quantity) {
		this.variety = variety;
		this.kind = kind;
		this.description = description;
		this.price = price;
		this.quantity = quantity;
	}
	public String getVariety() {return variety;}
	public void setVariety(String variety) {this.variety = variety;}
	public String getKind() {return kind;}
	public void setKind(String kind) {this.kind = kind;}
	public String getDescription() {return description;}
	public void setDescription(String description) {this.description = description;}
	public int getPrice() {return price;}
	public void setPrice(int price) {this.price = price;}
	public int getQuantity() {return quantity;}
	public void setQuantity(int quantity) {this.quantity = quantity;}
	@Override
	public String toString() {
		return variety + ", " + kind + ", " + description + ", price=" + price + ", quantity=" + quantity;
	}
}