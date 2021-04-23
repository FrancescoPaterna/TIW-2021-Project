package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import beans.Enroll;
import beans.Status;

public class EnrollsDAO {
private Connection connection;
	
	public EnrollsDAO(Connection connection) {
		this.connection = connection;
	}
	
	public List<Enroll> FindEnrollsOrderedByIDAsc(int exameDateId) throws SQLException {
		List<Enroll> enrolls = new ArrayList<>();
		
		String query = "SELECT user.ID, user.name, user.surname, user.email, user.coursedeg, enroll.mark, enroll.status "
				+ "FROM (user JOIN enroll ON user.ID = enroll.IDStudent) JOIN examdate ON enroll.IDExamDate = examdate.ID "
				+ "WHERE examedate.ID = ? ORDER BY user.ID ASC";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, exameDateId);
			try (ResultSet result = pstatement.executeQuery();) {
				while(result.next()) {
					Enroll enroll = new Enroll();
					enroll.setIDstudent(result.getInt("ID"));
					enroll.setName(result.getString("name"));
					enroll.setName(result.getString("surname"));
					enroll.setMail(result.getString("email"));
					enroll.setMark(result.getString("mark"));
					enroll.setStatus(Status.valueOf(result.getString("status")));
					enrolls.add(enroll);
				}
			}
		}
		return enrolls;
	}
	
	public List<Enroll> FindEnrollsOrderedByIDDesc(int exameDateId) throws SQLException {
		List<Enroll> enrolls = new ArrayList<>();
		
		String query = "SELECT user.ID, user.name, user.surname, user.email, user.coursedeg, enroll.mark, enroll.status "
				+ "FROM (user JOIN enroll ON user.ID = enroll.IDStudent) JOIN examdate ON enroll.IDExamDate = examdate.ID "
				+ "WHERE examedate.ID = ? ORDER BY user.ID DESC";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, exameDateId);
			try (ResultSet result = pstatement.executeQuery();) {
				while(result.next()) {
					Enroll enroll = new Enroll();
					enroll.setIDstudent(result.getInt("ID"));
					enroll.setName(result.getString("name"));
					enroll.setName(result.getString("surname"));
					enroll.setMail(result.getString("email"));
					enroll.setMark(result.getString("mark"));
					enroll.setStatus(Status.valueOf(result.getString("status")));
					enrolls.add(enroll);
				}
			}
		}
		return enrolls;
	}
	
	public List<Enroll> FindEnrollsOrderedByNameAsc(int exameDateId) throws SQLException {
		List<Enroll> enrolls = new ArrayList<>();
		
		String query = "SELECT user.ID, user.name, user.surname, user.email, user.coursedeg, enroll.mark, enroll.status "
				+ "FROM (user JOIN enroll ON user.ID = enroll.IDStudent) JOIN examdate ON enroll.IDExamDate = examdate.ID "
				+ "WHERE examedate.ID = ? ORDER BY user.name ASC";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, exameDateId);
			try (ResultSet result = pstatement.executeQuery();) {
				while(result.next()) {
					Enroll enroll = new Enroll();
					enroll.setIDstudent(result.getInt("ID"));
					enroll.setName(result.getString("name"));
					enroll.setName(result.getString("surname"));
					enroll.setMail(result.getString("email"));
					enroll.setMark(result.getString("mark"));
					enroll.setStatus(Status.valueOf(result.getString("status")));
					enrolls.add(enroll);
				}
			}
		}
		return enrolls;
	}
	
	public List<Enroll> FindEnrollsOrderedByNameDesc(int exameDateId) throws SQLException {
		List<Enroll> enrolls = new ArrayList<>();
		
		String query = "SELECT user.ID, user.name, user.surname, user.email, user.coursedeg, enroll.mark, enroll.status "
				+ "FROM (user JOIN enroll ON user.ID = enroll.IDStudent) JOIN examdate ON enroll.IDExamDate = examdate.ID "
				+ "WHERE examedate.ID = ? ORDER BY user.name DESC";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, exameDateId);
			try (ResultSet result = pstatement.executeQuery();) {
				while(result.next()) {
					Enroll enroll = new Enroll();
					enroll.setIDstudent(result.getInt("ID"));
					enroll.setName(result.getString("name"));
					enroll.setName(result.getString("surname"));
					enroll.setMail(result.getString("email"));
					enroll.setMark(result.getString("mark"));
					enroll.setStatus(Status.valueOf(result.getString("status")));
					enrolls.add(enroll);
				}
			}
		}
		return enrolls;
	}
	
	public List<Enroll> FindEnrollsOrderedBySurnameAsc(int exameDateId) throws SQLException {
		List<Enroll> enrolls = new ArrayList<>();
		
		String query = "SELECT user.ID, user.name, user.surname, user.email, user.coursedeg, enroll.mark, enroll.status "
				+ "FROM (user JOIN enroll ON user.ID = enroll.IDStudent) JOIN examdate ON enroll.IDExamDate = examdate.ID "
				+ "WHERE examedate.ID = ? ORDER BY user.surname ASC";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, exameDateId);
			try (ResultSet result = pstatement.executeQuery();) {
				while(result.next()) {
					Enroll enroll = new Enroll();
					enroll.setIDstudent(result.getInt("ID"));
					enroll.setName(result.getString("name"));
					enroll.setName(result.getString("surname"));
					enroll.setMail(result.getString("email"));
					enroll.setMark(result.getString("mark"));
					enroll.setStatus(Status.valueOf(result.getString("status")));
					enrolls.add(enroll);
				}
			}
		}
		return enrolls;
	}
	
	public List<Enroll> FindEnrollsOrderedBySurnameDesc(int exameDateId) throws SQLException {
		List<Enroll> enrolls = new ArrayList<>();
		
		String query = "SELECT user.ID, user.name, user.surname, user.email, user.coursedeg, enroll.mark, enroll.status "
				+ "FROM (user JOIN enroll ON user.ID = enroll.IDStudent) JOIN examdate ON enroll.IDExamDate = examdate.ID "
				+ "WHERE examedate.ID = ? ORDER BY user.surname DESC";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, exameDateId);
			try (ResultSet result = pstatement.executeQuery();) {
				while(result.next()) {
					Enroll enroll = new Enroll();
					enroll.setIDstudent(result.getInt("ID"));
					enroll.setName(result.getString("name"));
					enroll.setName(result.getString("surname"));
					enroll.setMail(result.getString("email"));
					enroll.setMark(result.getString("mark"));
					enroll.setStatus(Status.valueOf(result.getString("status")));
					enrolls.add(enroll);
				}
			}
		}
		return enrolls;
	}
	
	public List<Enroll> FindEnrollsOrderedByEmailAsc(int exameDateId) throws SQLException {
		List<Enroll> enrolls = new ArrayList<>();
		
		String query = "SELECT user.ID, user.name, user.surname, user.email, user.coursedeg, enroll.mark, enroll.status "
				+ "FROM (user JOIN enroll ON user.ID = enroll.IDStudent) JOIN examdate ON enroll.IDExamDate = examdate.ID "
				+ "WHERE examedate.ID = ? ORDER BY user.email ASC";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, exameDateId);
			try (ResultSet result = pstatement.executeQuery();) {
				while(result.next()) {
					Enroll enroll = new Enroll();
					enroll.setIDstudent(result.getInt("ID"));
					enroll.setName(result.getString("name"));
					enroll.setName(result.getString("surname"));
					enroll.setMail(result.getString("email"));
					enroll.setMark(result.getString("mark"));
					enroll.setStatus(Status.valueOf(result.getString("status")));
					enrolls.add(enroll);
				}
			}
		}
		return enrolls;
	}
	
	public List<Enroll> FindEnrollsOrderedByEmailDesc(int exameDateId) throws SQLException {
		List<Enroll> enrolls = new ArrayList<>();
		
		String query = "SELECT user.ID, user.name, user.surname, user.email, user.coursedeg, enroll.mark, enroll.status "
				+ "FROM (user JOIN enroll ON user.ID = enroll.IDStudent) JOIN examdate ON enroll.IDExamDate = examdate.ID "
				+ "WHERE examedate.ID = ? ORDER BY user.email DESC";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, exameDateId);
			try (ResultSet result = pstatement.executeQuery();) {
				while(result.next()) {
					Enroll enroll = new Enroll();
					enroll.setIDstudent(result.getInt("ID"));
					enroll.setName(result.getString("name"));
					enroll.setName(result.getString("surname"));
					enroll.setMail(result.getString("email"));
					enroll.setMark(result.getString("mark"));
					enroll.setStatus(Status.valueOf(result.getString("status")));
					enrolls.add(enroll);
				}
			}
		}
		return enrolls;
	}
	/*
	public List<Enroll> FindEnrollsOrderedByMarkAsc(int exameDateId) throws SQLException {
		List<Enroll> enrolls = new ArrayList<>();
		
		String query = "SELECT user.ID, user.name, user.surname, user.email, user.coursedeg, enroll.mark, enroll.status "
				+ "FROM (user JOIN enroll ON user.ID = enroll.IDStudent) JOIN examdate ON enroll.IDExamDate = examdate.ID "
				+ "WHERE examedate.ID = ? ORDER BY user.email ASC";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, exameDateId);
			try (ResultSet result = pstatement.executeQuery();) {
				while(result.next()) {
					Enroll enroll = new Enroll();
					enroll.setIDstudent(result.getInt("ID"));
					enroll.setName(result.getString("name"));
					enroll.setName(result.getString("surname"));
					enroll.setMail(result.getString("email"));
					enroll.setMark(result.getString("mark"));
					enroll.setStatus(Status.valueOf(result.getString("status")));
					enrolls.add(enroll);
				}
			}
		}
		return enrolls;
	}
	*/
	/*
	public List<Enroll> FindEnrollsOrderedByMarkDesc(int exameDateId) throws SQLException {
		List<Enroll> enrolls = new ArrayList<>();
		
		String query = "SELECT user.ID, user.name, user.surname, user.email, user.coursedeg, enroll.mark, enroll.status "
				+ "FROM (user JOIN enroll ON user.ID = enroll.IDStudent) JOIN examdate ON enroll.IDExamDate = examdate.ID "
				+ "WHERE examedate.ID = ? ORDER BY user.email ASC";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, exameDateId);
			try (ResultSet result = pstatement.executeQuery();) {
				while(result.next()) {
					Enroll enroll = new Enroll();
					enroll.setIDstudent(result.getInt("ID"));
					enroll.setName(result.getString("name"));
					enroll.setName(result.getString("surname"));
					enroll.setMail(result.getString("email"));
					enroll.setMark(result.getString("mark"));
					enroll.setStatus(Status.valueOf(result.getString("status")));
					enrolls.add(enroll);
				}
			}
		}
		return enrolls;
	}
	*/
	public List<Enroll> FindEnrollsOrderedByStatusAsc(int exameDateId) throws SQLException {
		List<Enroll> enrolls = new ArrayList<>();
		
		String query = "SELECT user.ID, user.name, user.surname, user.email, user.coursedeg, enroll.mark, enroll.status "
				+ "FROM (user JOIN enroll ON user.ID = enroll.IDStudent) JOIN examdate ON enroll.IDExamDate = examdate.ID "
				+ "WHERE examedate.ID = ? ORDER BY enroll.status ASC";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, exameDateId);
			try (ResultSet result = pstatement.executeQuery();) {
				while(result.next()) {
					Enroll enroll = new Enroll();
					enroll.setIDstudent(result.getInt("ID"));
					enroll.setName(result.getString("name"));
					enroll.setName(result.getString("surname"));
					enroll.setMail(result.getString("email"));
					enroll.setMark(result.getString("mark"));
					enroll.setStatus(Status.valueOf(result.getString("status")));
					enrolls.add(enroll);
				}
			}
		}
		return enrolls;
	}
	
	public List<Enroll> FindEnrollsOrderedByStatusDesc(int exameDateId) throws SQLException {
		List<Enroll> enrolls = new ArrayList<>();
		
		String query = "SELECT user.ID, user.name, user.surname, user.email, user.coursedeg, enroll.mark, enroll.status "
				+ "FROM (user JOIN enroll ON user.ID = enroll.IDStudent) JOIN examdate ON enroll.IDExamDate = examdate.ID "
				+ "WHERE examedate.ID = ? ORDER BY enroll.status DESC";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, exameDateId);
			try (ResultSet result = pstatement.executeQuery();) {
				while(result.next()) {
					Enroll enroll = new Enroll();
					enroll.setIDstudent(result.getInt("ID"));
					enroll.setName(result.getString("name"));
					enroll.setName(result.getString("surname"));
					enroll.setMail(result.getString("email"));
					enroll.setMark(result.getString("mark"));
					enroll.setStatus(Status.valueOf(result.getString("status")));
					enrolls.add(enroll);
				}
			}
		}
		return enrolls;
	}
}