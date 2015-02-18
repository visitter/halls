package testb;

import java.io.Serializable;

public class BaseNomenclatureRow implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String desc;
	private String nomName;
	private String iconURL;
	
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
	public String getIconURL() {
		return iconURL;
	}
	public void setIconURL(String iconURL) {
		this.iconURL = iconURL;
	}
	
	public BaseNomenclatureRow(){
		super();
	}
	
	public BaseNomenclatureRow(Integer id, String desc, String iconURL){
		setId(id);
		setDesc(desc);
		setIconURL(iconURL);
	}
}
