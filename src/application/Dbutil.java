package application;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Dbutil {
	private String dbUrl = "jdbc:mysql://localhost:3306/registermanager?useSSL=false&serverTimezone=UTC";
	private String userName = "root";
	private String passWord = "1027lj99!@03LJX";
	//private String jdbcName = "com.mysql.cj.jdbc.Driver";
	private Connection conn = null;

	
	public Dbutil() throws SQLException {
		//Class.forName(jdbcName)
;		conn = DriverManager.getConnection(dbUrl,userName,passWord);
	}
	/*
	 *Connect database 
	 **/
	public Connection getConnection() throws Exception {
		return DriverManager.getConnection(dbUrl,userName,passWord);
	}
	
	public void closeConnection() throws Exception {
		if(conn != null) {
			conn.close();
		}
	}
		
	public ObservableList<String> getsoName() throws Exception {
		//conn = getConnection();
		ObservableList<String> data = FXCollections.observableArrayList();
		Statement stmt =  conn.createStatement();
		String strSelect = "select soName from t_soinfo;";
		ResultSet rset = stmt.executeQuery(strSelect);
		
		while(rset.next()) {
			data.add(rset.getString("soName"));
		}
		return data;
	}
	
	public String getPwdofptID(String ID) throws Exception {

		String strSelect  = "select Word from t_ptinfo where ptID = ?;";
		PreparedStatement stmt = conn.prepareStatement(strSelect);
		stmt.setString(1, ID);
		ResultSet rset = stmt.executeQuery();
		if(rset.next())
		{
			return rset.getString("Word");
		}
		return "";
	}
	
	public String getPwdofdtID(String ID) throws Exception {
		//conn = getConnection();
		String strSelect  = "select Word from t_dtinfo where dtID = ?;";
		PreparedStatement stmt = conn.prepareStatement(strSelect);
		stmt.setString(1, ID);
		// return the password 
		//closeConnection();
		ResultSet rset = stmt.executeQuery();
		if(rset.next())
		{
			return rset.getString("Word");
		}
		return "";
	}
	/*
	 * get doctors in a department
	 */
	public ObservableList<String> getDtofSo(String soName) throws Exception {
		//conn = getConnection();
		ObservableList<String> data = FXCollections.observableArrayList();
		
		String strSelect = "select dtName from t_soinfo a INNER JOIN t_dtinfo b ON a.soID = b.soID where soName = ?;";
		
		PreparedStatement stmt = conn.prepareStatement(strSelect);
		stmt.setString(1, soName);
		
		ResultSet rset = stmt.executeQuery();
		while(rset.next()) {
			data.add(rset.getString("dtName"));
		}
		return data;
	}
	public ObservableList<String> getDtName() throws Exception {
		ObservableList<String> data = FXCollections.observableArrayList();
		
		String strSelect = "select dtName from t_dtinfo;";
		
		PreparedStatement stmt = conn.prepareStatement(strSelect);
		
		ResultSet rset = stmt.executeQuery();
		
		while(rset.next()) {
			data.add(rset.getString("dtName"));
		}
		return data;
	}
	
	public ObservableList<String> getNumofSo(String soName) throws Exception {
		ObservableList<String> data = FXCollections.observableArrayList();
		
		String strSelect = "select numName from t_soinfo a INNER JOIN t_numinfo b ON a.soID = b.soID where soName = ?;";
		
		PreparedStatement stmt = conn.prepareStatement(strSelect);
		stmt.setString(1, soName);
		
		ResultSet rset = stmt.executeQuery();
		while(rset.next()) {
			data.add(rset.getString("numName"));
		}
		return data;
	}
	
	public ObservableList<String> getNumName() throws SQLException {
		ObservableList<String> data = FXCollections.observableArrayList();
		
		String strSelect = "select numName from t_soinfo a INNER JOIN t_numinfo b ON a.soID = b.soID;";
		
		PreparedStatement stmt = conn.prepareStatement(strSelect);
		
		ResultSet rset = stmt.executeQuery();
		while(rset.next()) {
			data.add(rset.getString("numName"));
		}
		return data;
	}
	public boolean getExofDt(String dtName) throws SQLException {
		String strSelect = "select Expert from t_dtinfo where dtName = ?;";
		
		PreparedStatement stmt = conn.prepareStatement(strSelect);
		stmt.setString(1,dtName);
		
		ResultSet rset = stmt.executeQuery();
		if(rset.next()) {
			if(rset.getInt("Expert") == 1) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}
	/*
	 * get the cost of numName
	 */
	public double getCostofNum(String numName) throws SQLException {
		
		String strSelect = "select Cost from t_numinfo where numName = ?;";
		
		PreparedStatement stmt = conn.prepareStatement(strSelect);
		stmt.setString(1, numName);
		
		ResultSet rset = stmt.executeQuery();
		
		if(rset.next()) {
			return rset.getDouble("Cost");
		} else {
			return 0.0;
		}
	}

	public double getDepAmountOfPt(String ptID) throws SQLException {
		String strSelect = "select depAmount from t_ptinfo where ptID = ?;";

		PreparedStatement stmt = conn.prepareStatement(strSelect);
		stmt.setString(1,ptID);

		ResultSet rset = stmt.executeQuery();

		if(rset.next()) {
			return rset.getDouble("depAmount");
		}
		return 0.0;
	}

	public boolean UpdateDepAmountOfPt(String ptID, double opAmount) throws SQLException {
		String strUpdate = "update t_ptinfo set depAmount = ? where ptID = ?;";

		PreparedStatement stmt = conn.prepareStatement(strUpdate);
		stmt.setDouble(1,getDepAmountOfPt(ptID) + opAmount);
		stmt.setString(2,"ptID");

		return stmt.execute();
	}
	
	public int getRegNum() throws SQLException {
		//get sum of number from t_reginfo
		String strSelect = "select regID from t_reginfo a where regID = (select max(regID) from t_reginfo b);";

		String strLock = "LOCK TABLE t_reginfo as a write, t_reginfo as b write;";
		String strUnlock = "unlock tables;";
		PreparedStatement stmt1 = conn.prepareStatement(strLock);
		//insert reginfo
		PreparedStatement stmt = conn.prepareStatement(strSelect);
		stmt1.executeQuery();
		ResultSet rset = stmt.executeQuery();
		stmt1 = conn.prepareStatement(strUnlock);
		stmt1.executeQuery();
		if(rset.next()) {
			System.out.println(rset.getString("regID"));
			return Integer.valueOf(rset.getString("regID"));

		} else {
			return 0;		
		}
	}
	public int getNumCount(String numID) throws SQLException {
		String strSelect = "select numCount from t_reginfo where numID = ?;";

		PreparedStatement stmt = conn.prepareStatement(strSelect);
		stmt.setString(1, numID);
		ResultSet rset = stmt.executeQuery();
		if(rset.next()) {
			return rset.getInt("numCount");
		}
		return 0;
	}
	
	public int UpdateRegNum(String dtName, String ptID, String numName, double Amount) throws SQLException {


		String numID = getIDofNum(numName);


        String str = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        Timestamp time = Timestamp.valueOf(str);

		String strLock = "LOCK TABLE t_reginfo write, t_dtinfo read;";
		String strUnlock = "unlock tables;";
		PreparedStatement stmt1 = conn.prepareStatement(strLock);
		//insert reginfo
		stmt1.executeQuery();
		PreparedStatement stmt;
		String strInsert = "insert into t_reginfo values (?,?,?,?,?,?,?,?);";
		int regnum = getRegNum() + 1;
		String numString = String.format("%06d", regnum);

		stmt = conn.prepareStatement(strInsert);
		stmt.setString(1, numString);
		stmt.setString(2, numID);
		stmt.setString(3, getIDofDt(dtName));
		stmt.setString(4, ptID);
		stmt.setInt(5, getNumCount(numID) + 1);
		stmt.setInt(6, 0);
		stmt.setDouble(7, Amount);
		stmt.setTimestamp(8, time);
		
		stmt.execute();
		stmt1 = conn.prepareStatement(strUnlock);
		stmt1.executeQuery();
		//update numcount
		String strSelect = "select * from t_reginfo where numID = ?;";
		stmt = conn.prepareStatement(strSelect);
		stmt.setString(1,numID);
		ResultSet rset = stmt.executeQuery();
		if(rset.next()) {
			String strUpdate = "update t_reginfo set numCount = ? where numID = ?;";
			stmt = conn.prepareStatement(strUpdate);
			stmt.setInt(1, getNumCount(numID));
			stmt.setString(2, numID);
			
			stmt.execute();
		}
		return regnum;
	}
	
	public String getIDofNum(String numName) throws SQLException {
		String strSelect = "select numID from t_numinfo where numName = ?;";
		
		PreparedStatement stmt = conn.prepareStatement(strSelect);
		stmt.setString(1, numName);
		
		ResultSet rset = stmt.executeQuery();
		if(rset.next()) {
			return rset.getString("numID");
		}
		return "";
	}
	
	public String getIDofDt(String dtName) throws SQLException {
		String strSelect = "select dtID from t_dtinfo where dtName = ?;";
		
		PreparedStatement stmt = conn.prepareStatement(strSelect);
		stmt.setString(1, dtName);
		
		ResultSet rset = stmt.executeQuery();
		if(rset.next()) {
			return rset.getString("dtID");
		}
		return "";
	}
	
	public String getIDofPt(String ptName) throws SQLException {
		String strSelect = "select ptID from t_ptinfo where ptName = ?;";
		
		PreparedStatement stmt = conn.prepareStatement(strSelect);
		stmt.setString(1, ptName);
		
		ResultSet rset = stmt.executeQuery();
		if(rset.next()) {
			return rset.getString("ptID");
		}
		return "";
	}

	public ObservableList<RegData> getRegInfo(String dtID) throws SQLException {
        ObservableList<RegData> list = FXCollections.observableArrayList();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		//String strSelect = "select * from t_reginfo a INNER JOIN t_ptinfo b ON a.ptID = b.ptID;";
        String strSelect = "select regID, ptName, regDate, Expert from t_reginfo a, t_ptinfo b, t_numinfo c where a.ptID = b.ptID and a.numID = c.numID " +
				"and dtID = ?;";
		PreparedStatement stmt = conn.prepareStatement(strSelect);
		stmt.setString(1,dtID);
		ResultSet rset = stmt.executeQuery();
		while(rset.next()) {
            RegData data = new RegData();
			data.setRegID(rset.getString("regID"));
			data.setPtName(rset.getString("ptName"));
			data.setRegDate(df.format(rset.getTimestamp("regDate")));
			if(rset.getInt("Expert") == 1) {
                data.setNumType("专家号");
            } else {
			    data.setNumType("普通号");
            }
            list.add(data);
		}
		return list;
	}

	//Income Table Data Query
	public ObservableList<IncomeData> getIncome(LocalDate start, LocalDate end) throws SQLException {
		ObservableList<IncomeData> list = FXCollections.observableArrayList();

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ZoneId zoneId = ZoneId.systemDefault();

		String strSelect = "select soName,t_reginfo.dtID,dtName, t_numinfo.Expert, count(*) AS count , sum(t_numinfo.cost) as sum " +
				"from t_reginfo,t_dtinfo,t_numinfo,t_soinfo where t_reginfo.dtID = t_dtinfo.dtID " +
				"and t_dtinfo.soID = t_soinfo.soID and t_reginfo.numID = t_numinfo.numID and regDate >= ? " +
				"and regDate <= ? group by soName, t_reginfo.dtID,dtName,t_numinfo.Expert order by dtID ASC;";

		PreparedStatement stmt = conn.prepareStatement(strSelect);
		stmt.setTimestamp(1,Timestamp.valueOf(start.atStartOfDay()));
		stmt.setTimestamp(2,Timestamp.valueOf(end.atTime(23,59,59)));

		ResultSet rset = stmt.executeQuery();
		while(rset.next()) {
			IncomeData data = new IncomeData();
			data.setSoName(rset.getString("soName"));
			data.setDtID(rset.getString("dtID"));
			data.setDtName(rset.getString("dtName"));
			if(rset.getInt("Expert") == 1) {
                data.setNumType("专家号");
            } else {
			    data.setNumType("普通号");
            }
			data.setRegCount(rset.getString("count"));
			data.setIncome(rset.getString("sum"));
			list.add(data);
		}
		return list;
	}

	public void UpdatePtLoginDate(String ptID) throws SQLException {
        Timestamp time = new Timestamp(new Date().getTime());

	    String strUpdate = "update t_ptinfo set lastDate = ? where ptID = ?;";

	    PreparedStatement stmt = conn.prepareStatement(strUpdate);
	    stmt.setTimestamp(1,time);
	    stmt.setString(2,ptID);

	    stmt.execute();
    }
    public void UpdateDtLoginDate(String dtID) throws SQLException {
        String str = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        Timestamp time = Timestamp.valueOf(str);
		String strUpdate = "update t_dtinfo set lastDate = ? where dtID = ?;";

		PreparedStatement stmt = conn.prepareStatement(strUpdate);
		stmt.setTimestamp(1,time);
		stmt.setString(2,dtID);

		stmt.execute();
	}

	public boolean CheckCountofNum(String numName) throws SQLException {
		String strSelectCt = "select count(*) as count from t_reginfo,t_numinfo where t_reginfo.numID = t_numinfo.numID " +
				"and numName = ?;";
		String strSelect = "select numLimit from t_numinfo;";
		PreparedStatement stmtCt = conn.prepareStatement(strSelectCt);
		stmtCt.setString(1,numName);
		PreparedStatement stmt = conn.prepareStatement(strSelect);

		int Count = 0;
		int Limit = 0;

		ResultSet rset  = stmtCt.executeQuery();

		if(rset.next()) {
			Count = rset.getInt("count");
		}

		rset = stmt.executeQuery();
		if(rset.next()) {
			Limit = rset.getInt("numLimit");
		}

		if(Count >= Limit) {
			return false;
		} else {
			return true;
		}
	}
}
