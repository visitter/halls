package testb;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class EquipmentManager implements Serializable{

	private static final long serialVersionUID = 1L;
	private ArrayList<Equipment> arrEq;
	private Equipment selectedEq = new Equipment(0, new BaseNomenclatureRow(), 0);
	public Equipment getSelectedEq() {
		return selectedEq;
	}
	public void setSelectedEq(Equipment selectedEq) {
		this.selectedEq = selectedEq;
	}
	public ArrayList<Equipment> getArrEq() {
		return arrEq;
	}
	public Integer selectedId;
	public Integer getSelectedId() {
		return selectedId;
	}
	public void setSelectedId(Integer selectedId) {
		this.selectedId = selectedId;
	}
	@PostConstruct
	public void Init(){
		getAllEquipment();
	}
	
	public void getAllEquipment(){
		try {
			JdbcConnector oCon = new JdbcConnector();
			arrEq = oCon.getAllEquipment(0);
			oCon.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void getAllEquipmentById(){
		try {
			System.out.println(getSelectedId());
			JdbcConnector oCon = new JdbcConnector();
			arrEq = oCon.getAllEquipment(getSelectedId());
			oCon.close();
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void update(){
		
	}
	public void insert(){
			
	}
	public void delete(){
	
	}
}
