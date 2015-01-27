package testb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class JdbcConnector {
   
   private Connection connection = null;
   public static final String MyDB  = "jdbc:derby://localhost:1527/C:\\Program Files (x86)\\Apache-Tomcat-7\\Derby SQL\\databases\\MyDB";   
   private String dbConn = MyDB;
   
   //requests
   /*
   private PreparedStatement selectRequest   = null;
   private Statement insertRequest   = null;
   private Statement insertRequestEQ = null;
   private PreparedStatement deleteRequestEQ = null;
   */
   //meetings
   private PreparedStatement selectSchMeeting = null;
   private PreparedStatement updateSchMeeting = null;
   
   //halls
   private PreparedStatement selectHalls= null;
   private PreparedStatement insertHall = null;
   private PreparedStatement updateHall = null;
   private PreparedStatement deleteHall = null;   
   
   //noms
   private Statement statementNom    = null;  
   private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
   private SimpleDateFormat formatHour0 = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
   private SimpleDateFormat formatHour23 = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
   
   private ResultSet executeSelect(String sqlString){
	   if( ( sqlString == null )||( sqlString.isEmpty() ) )
		   return null;
	   
	   try {		   
		   return statementNom.executeQuery(sqlString);
	   } catch (SQLException e) {
		   e.printStackTrace();
		   return null;
	   }
   }

   public String driver = "org.apache.derby.jdbc.ClientDriver";
   
   public JdbcConnector() throws ClassNotFoundException, SQLException{	
         try{  	  				
	          Class.forName(driver);         									   
	          connection = DriverManager.getConnection(dbConn);
	          
	          selectSchMeeting = connection.prepareStatement("SELECT MS.SCH_ID, MM.ME_DESC, MM.ME_NAME, MH.HALL_NAME, MS.ME_START_DATE, MS.ME_END_DATE, MM.ME_TYPE_ID, MM.ME_REQ_ID, MS.SCH_HALL_ID FROM MAIN_SCHEDULE AS MS INNER JOIN MAIN_MEETINGS AS MM ON MS.SCH_ME_ID = MM.ME_ID INNER JOIN MAIN_HALLS AS MH ON MS.SCH_HALL_ID = MH.HALL_ID");
	          updateSchMeeting = connection.prepareStatement("UPDATE MAIN_SCHEDULE SET ME_START_DATE=?, ME_END_DATE = ? WHERE SCH_ID=?");
	          
	          selectHalls = connection.prepareStatement("SELECT * FROM MAIN_HALLS ORDER BY HALL_CAPACITY DESC");	          
	          insertHall  = connection.prepareStatement("INSERT INTO MAIN_HALLS (HALL_NAME, HALL_FLOOR, HALL_CAPACITY) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
	          updateHall  = connection.prepareStatement("UPDATE MAIN_HALLS  SET HALL_NAME = ?, HALL_FLOOR = ?, HALL_CAPACITY = ? WHERE HALL_ID = ?", Statement.RETURN_GENERATED_KEYS);
	          deleteHall  = connection.prepareStatement("DELETE FROM MAIN_HALLS WHERE HALL_ID = ?");
	          
	          statementNom = connection.createStatement();	          
	          
	       }catch ( SQLException sqlException ){    	  
	          sqlException.printStackTrace();
	          throw sqlException;
	       }catch( ClassNotFoundException cnfe){
	     	  throw cnfe;
	       }

   }
   public void simulateWork(){
	   try {
		   Thread.sleep(500);
	   	} catch (InterruptedException e) {
			e.printStackTrace();
	   	}
   }
   
   
   public ArrayList<String> getAll(){
	   ResultSet resultSet = null;
	   
	   try {
		   
		   resultSet = executeSelect("SELECT * FROM MAIN_HALLS");
		   				   
		   if( resultSet.next()){
			   ArrayList<String> arr=  new ArrayList<String>();
			   while(resultSet.next()){
				   arr.add( resultSet.getString("DESCRIPTION")	);
			   }
			   return arr;
		   }
		   
	   } catch (SQLException e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
	   }
	   return null;
   }
   public ArrayList<Meeting> getAllMeetings(){
	   ResultSet resultSet= null;
	   try {
		   
		   resultSet = selectSchMeeting.executeQuery();
		   
		   ArrayList<Meeting> arr=  new ArrayList<Meeting>();
			   
		   while(resultSet.next()){
				arr.add( new Meeting(
										resultSet.getInt("SCH_ID"),
										resultSet.getString("ME_DESC"),
										resultSet.getString("ME_NAME"),										
										resultSet.getTimestamp("ME_START_DATE"),
										resultSet.getTimestamp("ME_END_DATE"),										
										resultSet.getInt("ME_TYPE_ID"),
										resultSet.getInt("ME_REQ_ID"),
										resultSet.getInt("SCH_HALL_ID")
									)		 
						);
		   }
		   if( arr.isEmpty() )
			   return null;
		   else
			   return arr;		   
	   } catch (SQLException e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
	   }
	   return null;
   }
   
   public Boolean updateSchMeeting( Meeting me ){
	   Boolean res = false;
	  
	   try{
		   String d1 = format.format(me.getmSDate());
		   String d2 = format.format(me.getmEDate());
		   		   
		   updateSchMeeting.setString(1, d1);
		   updateSchMeeting.setString(2, d2);
		   updateSchMeeting.setInt(3, me.getmId());
		   
		   res = updateSchMeeting.executeUpdate()==1;			
	   }catch( SQLException e){
		   e.printStackTrace();
		   res = false;
	   }	   
	   return res;	   
   }

   public ArrayList<Hall> getAllHalls(){
	   ResultSet resultSet= null;
	   
	   try {
		   resultSet = selectHalls.executeQuery();	   
		
		   ArrayList<Hall> arr=  new ArrayList<Hall>();
			   
		   while(resultSet.next()){
				arr.add( new Hall(
										resultSet.getInt("HALL_ID"),
										resultSet.getString("HALL_NAME"),
										resultSet.getInt("HALL_FLOOR"),
										resultSet.getInt("HALL_CAPACITY")
									)		 
						);
		   }
		   if( arr.isEmpty() )
			   return null;
		   else
			   return arr;		   
	   } catch (SQLException e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
	   }
	   return null; 
   }
   public Boolean updateHall(Hall hall){	   
	   try {
		   updateHall.setString(1, hall.getName());
		   updateHall.setInt(2, hall.getFloor());
		   updateHall.setInt(3, hall.getCapacity());
		   updateHall.setInt(4, hall.getId());
		   
		   return updateHall.executeUpdate()>0;
	   } catch (SQLException e) {
		   e.printStackTrace();		   
	   }
	   return false; 
   }
   public Boolean insertHall(Hall hall){	   
	   try {
		   insertHall.setString(1, hall.getName());
		   insertHall.setInt(2, hall.getFloor());
		   insertHall.setInt(3, hall.getCapacity());		   
		   
		   return insertHall.executeUpdate()>0;
	   } catch (SQLException e) {
		   e.printStackTrace();		   
	   }
	   return false; 
   }
   public Boolean deleteHall(Hall hall){	   
	   try {
		   deleteHall.setInt(1, hall.getId());		   		   
		   return deleteHall.executeUpdate()>0;
	   } catch (SQLException e) {
		   e.printStackTrace();		   
	   }
	   return false; 
   }   
   
   public ArrayList<BaseNomenclatureRow> getAllNomRows(String nomname){
	   ResultSet resultSet= null;
	   try {
		   String sql = String.format("SELECT ID, DESCRIPTION FROM %s", nomname);
		   resultSet = statementNom.executeQuery(sql);
		   		   
		   ArrayList<BaseNomenclatureRow> arr=  new ArrayList<BaseNomenclatureRow>();
			   
		   while(resultSet.next()){
				arr.add( new BaseNomenclatureRow(
										resultSet.getInt("ID"),
										resultSet.getString("DESCRIPTION")
									)		 
						);
		   }
		   if( arr.isEmpty() )
			   return null;
		   else
			   return arr;		   
	   } catch (SQLException e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
	   }
	   return null;
   }
   public Boolean insertIntoNom(BaseNomenclatureRow row){
	   String sql = String.format("INSERT INTO %s (DESCRIPTION) VALUES('%s')", row.getNomName(), row.getDesc());
	   try {
		   return statementNom.executeUpdate(sql)>0;
	   } catch (SQLException e) {
		   e.printStackTrace();		
	   }
	   return false;
   }
   public Boolean updateNom(BaseNomenclatureRow row){
	   String sql = String.format("UPDATE %s SET DESCRIPTION='%s' WHERE Id = %d", row.getNomName(), row.getDesc(), row.getId());
	   try {
		   return statementNom.executeUpdate(sql)>0;
	   } catch (SQLException e) {
		   e.printStackTrace();		
	   }
	   return false;
   }
   public Boolean deleteFromNom(BaseNomenclatureRow row){
	   String sql = String.format("DELETE FROM %s WHERE Id = %d", row.getNomName(), row.getId());
	   try {
		   return statementNom.executeUpdate(sql)>0;
	   } catch (SQLException e) {
		   e.printStackTrace();		
	   }
	   return false;
   }
   
   public Boolean insertRequest(Request request){
	   try{
		   String sql = String.format("INSERT INTO MAIN_REQUESTS (REQ_NAME, REQ_START_DATE, REQ_END_DATE,  REQ_CU_ID) VALUES ('%s', '%s', '%s', '%s')",
				   						request.getName(),				   						
				   						request.getStartDate(),
				   						request.getEndDate(),
				   						request.getClientId()
				   						);
		   
		   if( statementNom.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS )>0){
			   ResultSet res = statementNom.getGeneratedKeys();
			   if( res.next()){
				   ResultSetMetaData rsmd = res.getMetaData();
				   int columnsNumber = rsmd.getColumnCount();
				   request.setId( res.getInt(columnsNumber) );
			   }
			   
			   if( (request.getReq()!= null)&&(request.getReq().size()>0) ){
				   for(Requirements req:request.getReq()){
					   sql = String.format("INSERT INTO MAIN_REQUESTS_EQUIPMENT (REQ_ID, REQ_EQ_TYPE_ID, REQ_EQ_COUNT, REQ_EQ_POSITIONS) VALUES (%d, %d, %d, '%s')",
		 									req.getId(),
		 									req.getTypeId(),
		 									req.getCount(),
		 									req.getPositions()
							   				);
					   if( statementNom.executeUpdate(sql)<1 ){
						   sql = String.format("DELETE FROM MAIN_REQUESTS WHERE REQ_ID=%d",request.getId());
						   if( statementNom.executeUpdate(sql)>0){
							   return false;
						   }
					   }
				   }
			   	   return true;
			   }else return request.getId()>0;
		   }
	   } catch (SQLException e) {
		   e.printStackTrace();		   
	   }
	   return false;	   
   }
   public ArrayList<Request> getAllRequests(){
	   ResultSet resultSet= null;
	   try {
		   String sql = "SELECT REQ_ID, REQ_NAME, REQ_CU_ID, REQ_START_DATE, REQ_END_DATE FROM MAIN_REQUESTS";
				   
		   resultSet = statementNom.executeQuery(sql);
		   
		   ArrayList<Request> arr=  new ArrayList<Request>();
		   
		   while(resultSet.next()){
				arr.add( new Request(
										resultSet.getInt("REQ_ID"),
										resultSet.getString("REQ_CU_ID"),
										resultSet.getString("REQ_NAME"),
										Meeting.dateFormat( resultSet.getString("REQ_START_DATE"), "yyyy-MM-dd HH:mm:ss", "dd-MM-yyyy HH:mm:ss"),
										Meeting.dateFormat( resultSet.getString("REQ_END_DATE")  , "yyyy-MM-dd HH:mm:ss", "dd-MM-yyyy HH:mm:ss")
									)		 
						);
		   }
		   
		   if( arr.isEmpty() )
			   return null;
		   else{
			   for(Request req:arr){
				   
				   sql = String.format("SELECT REQ_ID, REQ_EQ_TYPE_ID, REQ_EQ_COUNT, REQ_EQ_POSITIONS FROM MAIN_REQUESTS_EQUIPMENT WHERE REQ_ID = %d",
						   				req.getId()
					   					);   
				   resultSet = statementNom.executeQuery(sql);
				   ArrayList<Requirements> reqs=  new ArrayList<Requirements>();				   
				   while(resultSet.next()){
						reqs.add( new Requirements(
												resultSet.getInt("REQ_ID"),
												resultSet.getInt("REQ_EQ_TYPE_ID"),
												resultSet.getInt("REQ_EQ_COUNT"),
												resultSet.getString("REQ_EQ_POSITIONS")
											)
								);
				   }
				   req.setReq(reqs);
			   }
			   
			   return arr;
		   }
	   } catch (SQLException e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
	   }
	   return null; 
	   
   }
   
   public HashMap<String, Integer> checkAvailableResources(Date start, Date end){
	   try{
		   String sql = String.format("SELECT REQ_EQ_TYPE_ID, NET.DESCRIPTION, Sum(REQ_EQ_COUNT), (EQ_COUNT_AVAILABLE-Sum(REQ_EQ_COUNT)) FROM MAIN_SCHEDULE AS MS "+		   
				   						" INNER JOIN MAIN_MEETINGS AS MM "+ 
				   						" ON MS.SCH_ME_ID = MM.ME_ID "+
				   							" INNER JOIN MAIN_REQUESTS_EQUIPMENT AS MRQ "+ 
				   							" ON MRQ.REQ_ID = MM.ME_REQ_ID "+
				   								" INNER JOIN NOM_EQ_TYPES AS NET "+
				   								" ON NET.ID = MRQ.REQ_EQ_TYPE_ID "+
				   									" INNER JOIN MAIN_EQUIPMENT_INVENTORY AS MEI "+ 
				   									" ON NET.ID = MEI.EQ_TYPE_ID "+
				   						" WHERE MS.ME_START_DATE >= '%s' AND MS.ME_END_DATE<='%s' "+
				   						" GROUP BY REQ_EQ_TYPE_ID, NET.DESCRIPTION, EQ_COUNT_AVAILABLE",
				   formatHour0.format(start),
				   formatHour23.format(end)
				   );
		   		
		   ResultSet res = statementNom.executeQuery(sql);
		   HashMap<String, Integer> map = new HashMap<String, Integer>();
		   
		   while( res.next() ){			   
			   map.put(res.getString("DESCRIPTION"), res.getInt("4"));			   
		   }
		   		   
		   
		   if(map.isEmpty()){
			   sql = "SELECT NET.DESCRIPTION, MEI.EQ_COUNT_AVAILABLE FROM MAIN_EQUIPMENT_INVENTORY AS MEI"
			   			+ " INNER JOIN NOM_EQ_TYPES AS NET ON NET.ID = MEI.EQ_TYPE_ID";
			   
			   res = statementNom.executeQuery(sql);
			   while( res.next() ){
				   map.put(res.getString("DESCRIPTION"), res.getInt("EQ_COUNT_AVAILABLE"));
			   }
		   }
		   return map; 
	   } catch (SQLException e) {
		   e.printStackTrace();
		  
	   }
	   return null;	
	   
   }
   public Boolean insertMeeting( Meeting me, Schedule sch) throws SQLException{
	   try{		   
		   connection.setAutoCommit(false);
		   String sql = String.format("INSERT INTO MAIN_MEETINGS (ME_NAME, ME_TYPE_ID, ME_DESC, ME_REQ_ID) VALUES ('%s', %d, '%s', %d )",
				   						me.getmName(),				   						
				   						me.getTypeId(),
				   						me.getmDesc(),
				   						me.getRequestId()
				   					);
		   
		   if( statementNom.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS )>0){
			   ResultSet res = statementNom.getGeneratedKeys();
			   if( res.next()){
				   ResultSetMetaData rsmd = res.getMetaData();
				   int columnsNumber = rsmd.getColumnCount();
				   me.setmId( res.getInt(columnsNumber) );
				   sch.setMeetingId(me.getmId());
			   }
			   
			   
			   
			   sql = String.format("INSERT INTO MAIN_SCHEDULE( SCH_HALL_ID, SCH_ME_ID, ME_START_DATE, ME_END_DATE) VALUES( %d, %d, '%s', '%s')",
		 									sch.getHallId(),
		 									sch.getMeetingId(),
		 									format.format(sch.getStartDate()),
		 									format.format(sch.getEndDate())
							   				);
			   		   
			   if( statementNom.executeUpdate(sql)<1 ){
				   connection.rollback();
				   connection.setAutoCommit(true);
				   return false;
				   /*
				     sql = String.format("DELETE FROM MAIN_MEETINGS WHERE REQ_ID=%d",me.getmId());
				    
				   if( statementNom.executeUpdate(sql)>0){
					   return false;
				   }*/
				}else{
					connection.commit();	
					connection.setAutoCommit(true);
				}
			    return true;
		   }
	   } catch (SQLException e) {
		   e.printStackTrace();
		   connection.rollback();
		   connection.setAutoCommit(true);
		   throw e;
	   }
	   return false;	   
   }
   
   public Boolean isHallAvailable(Schedule sch) throws SQLException{
	   try{		   		   
		   String sql = String.format("SELECT SCH_ME_ID FROM MAIN_SCHEDULE WHERE SCH_HALL_ID =%d AND ( ME_START_DATE>='%s' AND ME_START_DATE<='%s')OR( ME_END_DATE>='%s' AND ME_END_DATE<='%s')",
				   						sch.getHallId(),
				   						format.format(sch.getStartDate()),
				   						format.format(sch.getEndDate()),
				   						format.format(sch.getStartDate()),
				   						format.format(sch.getEndDate())
				   						
				   					);
		   	
		  ResultSet res = statementNom.executeQuery(sql);
		  return !res.next();
	   }catch (SQLException e) {
		   e.printStackTrace();		   
		   throw e;
	   }   
   }
   
   public Boolean isUserAvailable(String username) throws SQLException{
	   try{		   		   
		   String sql = String.format("SELECT user_name FROM users WHERE user_name='%s'",
				   						username
				   					);
		  ResultSet res = statementNom.executeQuery(sql);
		  return !res.next();
	   }catch (SQLException e) {
		   e.printStackTrace();		   
		   throw e;
	   }   
   }
   public Boolean insertClient( UserClient uc ) throws SQLException{
	   try{		   
		   connection.setAutoCommit(false); 
		   String sql = String.format("INSERT INTO users (user_name, user_pass, user_mail) VALUES ('%s', '%s', '%s')",
				   						uc.getUsername(),				   						
				   						uc.getPassword(),
				   						uc.getEmail()
				   					);
		   
		   if( statementNom.executeUpdate(sql)>0){
			   			   
			   
			   sql = String.format("INSERT INTO user_roles (user_name, role_name) VALUES('%s', '%s')",
		 									uc.getUsername(),
		 									uc.getUserRole()
							   				);
			   		   
			   if( statementNom.executeUpdate(sql)<1 ){
				   connection.rollback();
				   connection.setAutoCommit(true);
				   return false;				   
				}else{
					connection.commit();	
					connection.setAutoCommit(true);
				}
			    return true;
		   }
	   } catch (SQLException e) {
		   e.printStackTrace();
		   connection.rollback();
		   connection.setAutoCommit(true);
		   throw e;
	   }
	   return false;	
		   
  }
   
   public Boolean deleteScheduledMeeting(Meeting me){	   
	   try {
		   connection.setAutoCommit(false);
		   String sql = String.format("DELETE FROM MAIN_SCHEDULE WHERE SCH_ME_ID = %d",
				   						me.getmId()
				   					);   
		   int resA = statementNom.executeUpdate(sql);
		   
		   sql = String.format("DELETE FROM MAIN_MEETINGS WHERE ME_ID = %d",
				   				me.getmId()
							);   
		   int resB = statementNom.executeUpdate(sql);
		   
		   Boolean lRes = (resA * resB)>0; 
		   if( lRes ){
			   connection.commit();
		   } else {
			   connection.rollback();			   
		   }
		   connection.setAutoCommit(true);
		   return lRes;
	   } catch (SQLException e) {
		   e.printStackTrace();		   
	   }
	   return false; 
   }
   public Boolean updateScheduledMeeting(Meeting me){	   
	   try {
		   connection.setAutoCommit(false);
		   String sql = String.format("UPDATE MAIN_SCHEDULE SET SCH_HALL_ID = %d, ME_START_DATE = '%s', ME_END_DATE = '%s' WHERE SCH_ME_ID = %d",
				   						me.getHallId(),
				   						format.format(me.getmSDate()),
				   						format.format(me.getmEDate()),
				   						me.getmId()
				   					);   
		   int resA = statementNom.executeUpdate(sql);		   
		   
		   sql = String.format("UPDATE MAIN_MEETINGS SET ME_NAME='%s' WHERE ME_ID = %d",
				   				me.getmName(),
				   				me.getmId()
							);   
		   int resB = statementNom.executeUpdate(sql);
		   Boolean lRes = (resA * resB)>0; 
		   if( lRes ){
			   connection.commit();
		   } else {
			   connection.rollback();
		   }
		   connection.setAutoCommit(true);
		   return lRes;
	   } catch (SQLException e) {
		   e.printStackTrace();		   
	   }
	   return false; 
   }
   
   public ArrayList<Equipment> getAllEquipment(Integer id){
	   String sql;
	   
	   if(id>0){
		  sql = String.format("SELECT MEI.EQ_ID, MEI.EQ_TYPE_ID, NQT.DESCRIPTION, MEI.EQ_COUNT_AVAILABLE FROM MAIN_EQUIPMENT_INVENTORY AS MEI INNER JOIN NOM_EQ_TYPES AS NQT ON NQT.ID = MEI.EQ_TYPE_ID WHERE NQT.ID = %d", id);	   
	   }else {
		  sql = "SELECT MEI.EQ_ID, MEI.EQ_TYPE_ID, NQT.DESCRIPTION, MEI.EQ_COUNT_AVAILABLE FROM MAIN_EQUIPMENT_INVENTORY AS MEI INNER JOIN NOM_EQ_TYPES AS NQT ON NQT.ID = MEI.EQ_TYPE_ID";
	   }
	  
	   try {
		   ResultSet res =  statementNom.executeQuery(sql);
		   
		   ArrayList<Equipment> arr=  new ArrayList<Equipment>();
		   
		   while(res.next()){
				arr.add( new Equipment(
										res.getInt("EQ_ID"),
									 	new BaseNomenclatureRow(res.getInt("EQ_TYPE_ID"), res.getString("DESCRIPTION")),
										res.getInt("EQ_COUNT_AVAILABLE")
									)		 
						);
		   }
		   
		   if( arr.isEmpty() )
			   return null;
		   else
			   return arr;		 
	   } catch (SQLException e) {
		// TODO Auto-generated catch block
		   e.printStackTrace();
		   return null;
	   }
   }
   
   public Boolean updateEquipment(Equipment eq){
	   try {
		   connection.setAutoCommit(false);
		   String sql = String.format("UPDATE MAIN_EQUIPMENT_INVENTORY SET EQ_TYPE_ID = %d, EQ_COUNT_AVAILABLE = %d WHERE EQ_ID = %d",
				   						eq.getRow().getId(),
				   						eq.getCount(),
				   						eq.getId()
				   					);   
		   int resA = statementNom.executeUpdate(sql);		   
		   
		    
		   if( resA>0 ){
			   connection.commit();			   
		   } else {
			   connection.rollback();
		   }
		   connection.setAutoCommit(true);
		   return resA>0;
	   } catch (SQLException e) {
		   e.printStackTrace();		   
	   }	
	   return false; 
   }
   public Boolean insertEquipment(Equipment eq){
	   try {
		   connection.setAutoCommit(false);
		   String sql = String.format("SELECT EQ_TYPE_ID FROM MAIN_EQUIPMENT_INVENTORY WHERE EQ_TYPE_ID=%d",eq.getRow().getId());
		   ResultSet res=statementNom.executeQuery(sql);
		   int resA = 0;
		   if( !res.next()){			   
			   sql = String.format("INSERT INTO MAIN_EQUIPMENT_INVENTORY (EQ_TYPE_ID, EQ_COUNT_AVAILABLE) VALUES (%d, %d)",
				  						eq.getRow().getId(),
				   						eq.getCount()
				   					);   
			   resA = statementNom.executeUpdate(sql);		   
			   		    
			   if( resA>0 ){
				   connection.commit();			   
			   } else {
				   connection.rollback();
			   }
		   }
		   connection.setAutoCommit(true);
		   return resA>0;
	   } catch (SQLException e) {
		   e.printStackTrace();		   
	   }
	   return false; 
   }
   
   public Boolean deleteEquipment(Equipment eq){
	   try {
		   connection.setAutoCommit(false);
		   String sql = String.format("DELETE FROM MAIN_EQUIPMENT_INVENTORY WHERE EQ_ID = %d",
				   						eq.getId()
				   					);   
		   int resA = statementNom.executeUpdate(sql);		   
		   		    
		   if( resA>0 ){
			   connection.commit();			   
		   } else {
			   connection.rollback();
		   }
		   connection.setAutoCommit(true);
		   return resA>0;
	   } catch (SQLException e) {
		   e.printStackTrace();		   
	   }
	   return false; 
   }
   
   public void close()
   {
      try 
      {
         connection.close();
      }
      catch ( SQLException sqle )
      {
    	  sqle.printStackTrace();
      } 
   }
}
