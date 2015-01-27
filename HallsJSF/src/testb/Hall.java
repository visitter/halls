package testb;

import java.io.Serializable;

public class Hall implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String name;
	private Integer floor;
	private Integer capacity;
		
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getFloor() {
		return floor;
	}
	public void setFloor(Integer floor) {
		this.floor = floor;
	}
	public Integer getCapacity() {
		return capacity;
	}
	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}
	
	public Hall( Integer id, String name, Integer floor, Integer capacity){
		setId(id);
		this.name=name; // setName(name);
		setFloor(floor);
		setCapacity(capacity);
	}
	
}
