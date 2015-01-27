package testb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty; 
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;

public class NomsExporterView implements Serializable {
  
	private static final long serialVersionUID = 1L;
	
	private String nomName;
	public String getNomName() {
		return nomName;
	}
	public void setNomName(String nomName) {
		this.nomName = nomName;
	}

	private List<BaseNomenclatureRow> rows;
    public List<BaseNomenclatureRow> getRows() {
		return rows;
	}

	@ManagedProperty("#{baseNomManager}")
    private BaseNomManager manager;
    public BaseNomManager getManager() {
		return manager;
	}
    public void setManager(BaseNomManager manager){
    	this.manager = manager;
    }
    
    private BaseNomenclatureRow selectedRow;       
    public BaseNomenclatureRow getSelectedRow() {
		return selectedRow;
	}
	public void setSelectedRow(BaseNomenclatureRow selectedRow) {
		this.selectedRow = selectedRow;
	}

	private BaseNomenclatureRow newRow;
	public BaseNomenclatureRow getNewRow() {
		return newRow;
	}
	public void setNewRow(BaseNomenclatureRow newRow) {
		this.newRow = newRow;
	}
	
	@PostConstruct
    public void init() {
    	newRow = new BaseNomenclatureRow(0, "Нов");
    	manager = new BaseNomManager();
    }
	
	public void getAllRows(){		
		manager.setNomName(nomName);
		rows = manager.getAllNomRows(nomName);
		
		if( rows==null){
			rows = new ArrayList<BaseNomenclatureRow>();        	
        	if( selectedRow==null ){
        		selectedRow =  new BaseNomenclatureRow(0, "Нов");            	
            }        	
        	rows.add(selectedRow);        
        }else{
        
        }
		setSelectedRow(rows.get(0));
	}
	
	public void update(){
		selectedRow.setNomName(nomName);		
		manager.update(selectedRow);
		getAllRows();		
	}
	
	public void insert(){
		System.out.println("Inserting...");
		BaseNomenclatureRow row = new BaseNomenclatureRow( newRow.getId(), newRow.getDesc());
		setSelectedRow(row);
		row.setNomName(nomName);
		manager.save(row);
		getAllRows();		
	}
	
	public void delete(){		
		selectedRow.setNomName(nomName);
		manager.delete(selectedRow);
		getAllRows();		
	}
	
	public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);        
    }	
		
	public void onRowSelect(SelectEvent event) {
		setSelectedRow(((BaseNomenclatureRow) event.getObject()));
		selectedRow.setNomName(nomName);

    }   
}