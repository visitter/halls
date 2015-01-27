package testb;

import java.io.Serializable;

public class BaseNomenclatureRow implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String desc;
	private String nomName;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getNomName() {
		return nomName;
	}
	public void setNomName(String nomName) {
		this.nomName = nomName;
	}
	public BaseNomenclatureRow(){
		super();
	}
	
	public BaseNomenclatureRow(Integer id, String desc){
		setId(id);
		setDesc(desc);
	}
}
