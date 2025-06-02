package backend.models;

import java.time.LocalDateTime;
import java.util.Objects;


public class Transaction {
    public enum Status {
        PENDING,
        COMPLETED,
        FAILED
    }

    private static long counter = 0;
    private final long id;
    private final int userId;
    private final int assetId;
    private final double amount;
    private final double pricePerUnit;
    private final LocalDateTime time = LocalDateTime.now();
    private Status status;
    public boolean isBuy;

    public boolean isBuy() {
        return isBuy;
    }

    public Transaction(int id, int userId, int assetId, double amount, double pricePerUnit, boolean isBuy) {
        this.id = id;
        this.userId = userId;
        this.assetId = assetId;
        this.amount = amount;
        this.pricePerUnit = pricePerUnit;
        this.status = Status.PENDING;
        this.isBuy = isBuy;
    }

    public Transaction(int userId, int assetId, double amount, double pricePerUnit, boolean isBuy) {
        this.id = counter++;
        this.userId = userId;
        this.assetId = assetId;
        this.amount = amount;
        this.pricePerUnit = pricePerUnit;
        this.status = Status.PENDING;
        this.isBuy = isBuy;
    }

    public void updateStatus(Status status) {
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getAssetId() {
        return assetId;
    }

    public double getAmount() {
        return amount;
    }

    public double getPricePerUnit() {
        return pricePerUnit;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return this.id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", userId=" + userId +
                ", assetId=" + assetId +
                ", amount=" + amount +
                ", pricePerUnit=" + pricePerUnit +
                ", time=" + time +
                ", status=" + status +
                ", isBuy=" + isBuy +
                '}';
    }
}
