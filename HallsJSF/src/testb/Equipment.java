package testb;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;

@ManagedBean
public class Equipment implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer id;
	private BaseNomenclatureRow row;
	private Integer count;
		
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public BaseNomenclatureRow getRow() {
		return row;
	}
	public void setRow(BaseNomenclatureRow row) {
		this.row = row;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}

	public Equipment( Integer id, BaseNomenclatureRow row, Integer count){
		setId(id);
		setRow(row);
		setCount(count);
	}
}
