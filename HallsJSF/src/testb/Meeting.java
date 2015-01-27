package testb;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Meeting implements java.io.Serializable {
	private static final long serialVersionUID = 8515453777462207047L;
	private Integer mId;	
	private String mDesc;
	private String mName;	
	private Date mSDate;
	private Date mEDate;
		
	private Integer hallId;
	public Integer getHallId() {
		return hallId;
	}
	public void setHallId(Integer hallId) {
		this.hallId = hallId;
	}


	private Integer typeId;
	public Integer getTypeId() {
		return typeId;
	}
	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}
	
	
	private Integer requestId;
	public Integer getRequestId() {
		return requestId;
	}
	public void setRequestId(Integer requestId) {
		this.requestId = requestId;
	}

	public static final String yearMonthDay = "yyyy-MM-dd"; 
	public static final String dayMonthYear = "dd-MM-yyyy";
		
	public Integer getmId() {
		return mId;
	}
	public void setmId(Integer mId) {
		this.mId = mId;
	}
	public String getmDesc() {
		return mDesc;
	}
	public void setmDesc(String mDesc) {
		this.mDesc = mDesc;
	}	
	public String getmName() {
		return mName;
	}
	public void setmName(String mName) {
		this.mName = mName;
	}
	
	public Date getmSDate() {
		return mSDate;
	}
	public void setmSDate(Date mSDate) {		
		this.mSDate =  mSDate;
	}
	public Date getmEDate() {
		return mEDate;
	}
	public void setmEDate(Date mEDate) {
		this.mEDate = mEDate;
	}
	
	public Meeting(){		
	}
	
	
    public Meeting( Integer mId, String mDesc, String mName, Date mSDate, Date mEDate, Integer typeId, Integer requestId, Integer hallId){
		this.mId = mId;
		this.mDesc = mDesc;
		this.mName = mName;
		setmSDate(mSDate);
		setmEDate(mEDate);
		setTypeId(typeId);
		setRequestId(requestId);
		setHallId(hallId);
	}	
	public static String dateFormat(String inputDate, String fromPattern, String toPattern){
		SimpleDateFormat fromFormat = new SimpleDateFormat(fromPattern);
		SimpleDateFormat toFormat   = new SimpleDateFormat(toPattern);
		try {
		    return toFormat.format(fromFormat.parse(inputDate));
		} catch (ParseException e) {
		    e.printStackTrace();
		    return null;
		}
	}
}
/*
SELECT
	MM.ME_DESC,
	MH.HALL_NAME,
	MS.ME_START_DATE,
	MS.ME_END_DATE,
	MS.ME_START_TIME,
	MS.ME_END_TIME
FROM MAIN_SCHEDULE AS MS 
	INNER JOIN MAIN_MEETINGS AS MM ON MS.SCH_ME_ID = MM.ME_ID 
		INNER JOIN MAIN_HALLS AS MH ON MS.SCH_HALL_ID = MH.HALL_ID
		*/