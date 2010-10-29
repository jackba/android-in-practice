package com.manning.aip.portfolio;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * A data structure class representing a stock. This implements the
 * {@link http://developer.android.com/reference/android/os/Parcelable.html Parcelable}
 * interface so that it can be use for IPC between a background <code>Service</code>
 * and an <code>Activity</code>.
 * 
 * @author Michael Galpin
 *
 */
public class Stock implements Parcelable{
	// user defined
	private String symbol;
	private double maxPrice;
	private double minPrice;
	private double pricePaid;
	private int quantity;
	
	// dynamic retrieved
	private String name ="";
	private double currentPrice=0D;
	
	// db assigned
	private int id;
	
	// constructors
	public Stock(String symbol, double pricePaid, int quantity) {
		this(symbol, pricePaid, quantity, 0);
	}
	public Stock(String symbol, double pricePaid, int quantity, int id) {
		this.symbol = symbol;
		this.pricePaid = pricePaid;
		this.quantity = quantity;
		this.id = id;
	}
	public Stock(Stock old, int id){
		this.symbol = old.symbol;
		this.maxPrice = old.maxPrice;
		this.minPrice = old.minPrice;
		this.pricePaid = old.pricePaid;
		this.quantity = old.quantity;
		this.name = old.name;
		this.currentPrice = old.currentPrice;
		this.id = id;
	}
	private Stock(Parcel parcel){
		this.readFromParcel(parcel);
	}
	
	// getters and setters
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getCurrentPrice() {
		return currentPrice;
	}
	public void setCurrentPrice(double currentPrice) {
		this.currentPrice = currentPrice;
	}
	public String getSymbol() {
		return symbol;
	}
	public double getMaxPrice() {
		return maxPrice;
	}
	public double getMinPrice() {
		return minPrice;
	}
	public double getPricePaid() {
		return pricePaid;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setMaxPrice(double maxPrice) {
		this.maxPrice = maxPrice;
	}
	public void setMinPrice(double minPrice) {
		this.minPrice = minPrice;
	}
	public int getId() {
		return id;
	}
	@Override
	public String toString() {
		if (name != null && name.trim().length() > 0){
			return name + '(' + symbol + ')';
		} else {
			return symbol;
		}
	}
	/**
	 * Any <code>Parcelable</code> needs a static field called CREATOR that
	 * acts as a factory class for the <code>Parcelable</code>.
	 */
	public static final Parcelable.Creator<Stock> CREATOR = new Parcelable.Creator<Stock>() {

		public Stock createFromParcel(Parcel source) {
			return new Stock(source);
		}

		public Stock[] newArray(int size) {
			return new Stock[size];
		}
	};
	
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeString(symbol);
		parcel.writeDouble(maxPrice);
		parcel.writeDouble(minPrice);
		parcel.writeDouble(pricePaid);
		parcel.writeInt(quantity);
		parcel.writeDouble(currentPrice);
		parcel.writeString(name);
	}
	
	/**
	 * Method for creating a <code>Stock</code> from a <code>Parcelable</code>.
	 * This is not required by the <code>Parcelable</code> interface, you can
	 * instead defer this to <code>Parcelable.Creator</code>'s 
	 * <code>createFromParcel</code> method.
	 * 
	 * @param 	parcel			The <code>Parcelable</code> being used to create
	 * 							a <code>Stock</code> object, presumably this is
	 * 							a <code>Stock</code> object that has been 
	 * 							serialized using the {@link #writeToParcel(Parcel, int) writeToParcel}
	 * 							method
	 */
	public void readFromParcel(Parcel parcel){
		symbol = parcel.readString();
		maxPrice = parcel.readDouble();
		minPrice = parcel.readDouble();
		pricePaid = parcel.readDouble();
		quantity = parcel.readInt();
		currentPrice = parcel.readDouble();
		name = parcel.readString();
	}
}
