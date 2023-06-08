public class Item implements Comparable<Item> {
    private String name;
    private String store;
    private String description;
    private int quantity;
    private double price;
    private int id;
    private String sellerEmail;

    public Item(int id, String name, String store, String description, int quantity, double price, String sellerEmail) {
        this.id = id;
        this.name = name;
        this.store = store;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.sellerEmail = sellerEmail;
    }

    public String getSellerEmail() {
        return sellerEmail;
    }

    public void setSellerEmail(String sellerEmail) {
        this.sellerEmail = sellerEmail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String toString() {
        return String.format("%d,%s,%s,%s,%d,%.2f,%s", id, name, store, description, quantity, price, sellerEmail);
    }

    public String fancyToString() {
        return String.format("ID:%d,Name:%s,Store:%s,Description:%s,Quantity:%d,Price:%.2f,Email:%s", id, name, store, description, quantity, price, sellerEmail);
    }

    public String toStringNoDQ() {
        return String.format("ID:%d,Store:%s,Name:%s,Price:%.2f", id, store, name, price);
    }

    @Override
    public int compareTo(Item o) {
        if(this.getPrice() > o.getPrice()){
            return 1;
        }
        else if(this.getPrice() < o.getPrice()){
            return -1;
        }else{
            return 0;
        }

    }
    public int compareQuantity(Item o){
        if(this.getQuantity() > o.getQuantity()){
            return 1;
        }
        else if(this.getQuantity() < o.getQuantity()){
            return -1;
        }else{
            return 0;
        }
    }
}
