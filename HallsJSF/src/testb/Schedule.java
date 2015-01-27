package testb;

import java.util.Date;

public class Schedule {
	private Integer id;
	private Integer hallId;
	private Integer meetingId;
	private Date startDate;
	private Date endDate;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getHallId() {
		return hallId;
	}
	public void setHallId(Integer hallId) {
		this.hallId = hallId;
	}
	public Integer getMeetingId() {
		return meetingId;
	}
	public void setMeetingId(Integer meetingId) {
		this.meetingId = meetingId;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public Schedule(  Integer id, Integer hallId, Integer meetingId, Date startDate, Date endDate ){
		setId(id);
		setHallId(hallId);
		setMeetingId(meetingId);
		setStartDate(startDate);
		setEndDate(endDate);
	}
}
