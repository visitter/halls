package testb;

import java.io.Serializable;

public class AvailableResources implements Serializable{
	private static final long serialVersionUID = 1L;
	private String name;
	private Integer count;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public AvailableResources(String name, Integer count){
		setName(name);
		setCount(count);
	}
}
