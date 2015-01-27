package testb;

import java.io.Serializable;
import java.util.ArrayList;

class Requirements implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer typeId;
	private Integer count;
	private String  positions;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getTypeId() {
		return typeId;
	}
	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public String getPositions() {
		return positions;
	}
	public void setPositions(String positions) {
		this.positions = positions;
	}
	
	public Requirements(Integer id, Integer typeId, Integer count, String positions){		
		setId(id);
		setTypeId(typeId);
		setCount(count);
		setPositions(positions);		
	}
}

public class Request implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String clientId;	
	private String name;
	private String startDate;
	private String endDate;
	
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	private ArrayList<Requirements> reqs;
	
	public ArrayList<Requirements> getReq() {
		return reqs;
	}
	public void setReq(ArrayList<Requirements> reqs) {
		this.reqs = reqs;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
		if( reqs!=null ){
			for(Requirements req:reqs)
			{
				req.setId(this.id);
			}
		}
		
	}	
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	
	public Request(){
		
	}
	public Request( Integer id, String clientId, String name, String startDate, String endDate){
		super();
		setId(id);
		setClientId(clientId);
		setName(name);		
		setStartDate(startDate);
		setEndDate(endDate);
	}
	
	@Override
	public String toString(){
		String res =" Id = "+getId()+
					" clientId = "+getClientId()+
					" name = "+getName()+
					" start = "+getStartDate()+
					" end = "+getEndDate();
		
		if( (reqs!=null)&&(reqs.size()>0) ){
		for(Requirements req:reqs)
		{
			res = res+ " Id = "+req.getId().toString()+
				  " TypeId= "+req.getTypeId().toString()+
				  " count = "+req.getCount().toString()+
				  " positions = "+req.getPositions()+		
				  "\n";
		}
		}
		return res;
	}
	
}
