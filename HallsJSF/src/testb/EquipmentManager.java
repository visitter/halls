package testb;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@ViewScoped
public class EquipmentManager implements Serializable{
	private static final long serialVersionUID = 1L;
	private ArrayList<Equipment> arrEq;
	private Equipment selectedEq = new Equipment(0, new BaseNomenclatureRow(), 0);
	
	public Equipment getSelectedEq() {
		System.out.println(getSelectedId());
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
		
	private ArrayList<BaseNomenclatureRow> arrEqTypes;
	public ArrayList<BaseNomenclatureRow> getArrEqTypes() {
		return arrEqTypes;
	}
	public void setArrEqTypes(ArrayList<BaseNomenclatureRow> arrEqTypes) {
		this.arrEqTypes = arrEqTypes;
	}
	
	@PostConstruct
	public void Init(){
		getAllEquipment();
		if(arrEq.size()>0){
			setSelectedEq(arrEq.get(0));
		}
		getAllEquipmentTypes();
	}
	
	public void getAllEquipment(){
		try {
			JdbcConnector oCon = new JdbcConnector();
			arrEq = oCon.getAllEquipment(0);
			oCon.close();
		} catch (ClassNotFoundException|SQLException e) {
			e.printStackTrace();
			addMessage("Error in database",e.getMessage(),FacesMessage.SEVERITY_ERROR);
		}
	}
	
	public void getAllEquipmentTypes(){
		try {
			System.out.println(getSelectedId());
			JdbcConnector oCon = new JdbcConnector();
			arrEqTypes = oCon.getAllNomRows("NOM_EQ_TYPES");
			oCon.close();
			
		} catch (ClassNotFoundException|SQLException e) {
			e.printStackTrace();
			addMessage("Error in database",e.getMessage(),FacesMessage.SEVERITY_ERROR);
		}
	}
	
	public void update(){
		JdbcConnector oCon;
		try {
			oCon = new JdbcConnector();
			System.out.println(selectedEq.getId()+" "+selectedEq.getRow().getId());
			oCon.updateEquipment(selectedEq);
			oCon.close();
			addMessage("Update", selectedEq.getRow().getDesc()+" Successful",FacesMessage.SEVERITY_INFO);
		} catch (ClassNotFoundException|SQLException e) {
			e.printStackTrace();
			addMessage("Error in database",e.getMessage(),FacesMessage.SEVERITY_ERROR);
		}
	}
	public void insert(){
		JdbcConnector oCon;
		try {
			oCon = new JdbcConnector();
			selectedEq.getRow().setId(getSelectedId());
			System.out.println( selectedEq.getRow().getId());
			if( oCon.insertEquipment(selectedEq)){
				addMessage("Adding","Successful",FacesMessage.SEVERITY_INFO);
			} else {
				addMessage("Adding","Failed, item already exists",FacesMessage.SEVERITY_WARN);
			}
			oCon.close();
			
		} catch (ClassNotFoundException|SQLException e) {
			e.printStackTrace();
			addMessage("Error in database",e.getMessage(),FacesMessage.SEVERITY_ERROR);
		}	
	}
	public void delete(){
		JdbcConnector oCon;
		try {
			oCon = new JdbcConnector();
			System.out.println(selectedEq.getId());
			oCon.deleteEquipment(selectedEq);
			oCon.close();
			addMessage("Delete",selectedEq.getRow().getDesc()+" Successful",FacesMessage.SEVERITY_INFO);
		} catch (ClassNotFoundException|SQLException e) {
			e.printStackTrace();
			addMessage("Error in database",e.getMessage(),FacesMessage.SEVERITY_ERROR);
		}
	}
	public static void addMessage(String summary, String detail, FacesMessage.Severity severity) {
        FacesMessage message = new FacesMessage(severity, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}
