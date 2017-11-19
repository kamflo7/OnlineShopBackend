package pl.kflorczyk.onlineshopbackend.dto;

public class OrderProductDTO {
    private long productID;
    private int amount;

    public OrderProductDTO() {
    }

    public OrderProductDTO(long productID, int amount) {
        this.productID = productID;
        this.amount = amount;
    }

    public long getProductID() {
        return productID;
    }

    public void setProductID(long productID) {
        this.productID = productID;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
