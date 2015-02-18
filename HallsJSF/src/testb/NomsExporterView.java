package testb;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty; 
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.UploadedFile;

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
	
	private UploadedFile fileName;
	public UploadedFile getFileName() {
		return fileName;
	}
	public void setFileName(UploadedFile fileName) {
		this.fileName = fileName;
	}
	@PostConstruct
    public void init() {
    	newRow = new BaseNomenclatureRow(0, "Нов", "");
    	manager = new BaseNomManager();
    }
	
	public void getAllRows(){		
		manager.setNomName(nomName);
		rows = manager.getAllNomRows(nomName);
				
		if( rows==null){
			rows = new ArrayList<BaseNomenclatureRow>();        	
        	if( selectedRow==null ){
        		selectedRow =  new BaseNomenclatureRow(0, "Нов", "");            	
            }        	
        	rows.add(selectedRow);        
        }else{
        
        }
		setSelectedRow(rows.get(0));		
	}
	
	public void update(){
		String fileDBName = "";
		if(fileName != null){
			fileDBName = upload(fileName);
		 }else System.out.println("filename is null");
		
		selectedRow.setIconURL(fileDBName);
		selectedRow.setNomName(nomName);		
		manager.update(selectedRow);		
		getAllRows();		
	}
	
	public void insert(){
		System.out.println("Inserting...");
		if(fileName != null){			 
			 newRow.setIconURL(upload(fileName));
		}else System.out.println("filename is null");
		
		BaseNomenclatureRow row = new BaseNomenclatureRow( newRow.getId(), newRow.getDesc(), newRow.getIconURL());
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
	
	public String upload(UploadedFile file) {
		System.out.println("uploading...");
		FacesMessage message = null;
		String fileName = null;
		try {			
			Path p = Paths.get(file.getFileName());
    		fileName = p.getFileName().toString();
    		
			if( copyFile( fileName, file.getInputstream()) ){
				message = new FacesMessage("Succesful", fileName + " is uploaded.");			
			}else{
				message = new FacesMessage("Failed", fileName + " failed to upload.");
				fileName=null;
			}
		} catch (IOException e) {
			e.printStackTrace();
			message = new FacesMessage("Failed", e.getMessage() + " failed to upload.");
			fileName=null;
		}
        FacesContext.getCurrentInstance().addMessage(null, message);
    	return fileName;
    }
	public Boolean copyFile(String fileName, InputStream in) {
        try {        	
        	if(nomName!=null){
        		String path = "C:\\Program Files (x86)\\Apache-Tomcat-7\\webapps\\HallsJSF\\img\\equip\\";        		
        		OutputStream out = new FileOutputStream(new File( path + fileName));
        		
	            int read = 0;
	            byte[] bytes = new byte[1024];	           
	            
	            while ((read = in.read(bytes)) != -1) {
	            	out.write(bytes, 0, read);
	            }
	           
	            in.close();
	            out.flush();
	            out.close();
	            return true;
	        }else return false;
        	
        } catch (IOException e) {
        	System.out.println(e.getMessage());
            return false;
        }
	}
}