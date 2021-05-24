package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;


public class RecordDAO {
	private Connection connection;
	
	public RecordDAO(Connection connection) {
		this.connection = connection;
	}

	

	public void writeRecordOnDb(int id_exam) throws SQLException {
		String query = "INSERT INTO projectdb.record (IDExamDate) VALUES ( ? )";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, id_exam);
			pstatement.executeUpdate();
		}
	}
	
	
	public int getCurrentID(int id_exam) throws SQLException {
		int ID;
		String query = "SELECT MAX(ID) FROM projectdb.record WHERE projectdb.record.IDExamDate = ?";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, id_exam);
			try (ResultSet result = pstatement.executeQuery();) {
				result.next();
				ID = result.getInt("MAX(ID)");

			}
		}
		return ID;
	}

	public Timestamp getCurrentTimestamp(int id_record) throws SQLException{
		Timestamp timestamp;
		String query = "SELECT projectdb.record.time FROM projectdb.record WHERE projectdb.record.ID = ?";
		try( PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, id_record);
			try (ResultSet result = pstatement.executeQuery();) {
				result.next();
				timestamp = result.getTimestamp("time");
			}
		}
		return timestamp;
	}
	
}
