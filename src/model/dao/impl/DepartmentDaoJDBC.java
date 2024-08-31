package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao{

	private Connection conn = null;
	
	public DepartmentDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert(Department department) {
		
		PreparedStatement ps = null;
		
		try {
			
			ps = conn.prepareStatement("INSERT INTO department(Id, Name) "
					+ "VALUES (?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			
			ps.setInt(1, department.getId());
			ps.setString(2, department.getName());
			
			int rows = ps.executeUpdate();
			
			ResultSet rs = ps.getGeneratedKeys();
			
			if(rows > 0){
				
				if(rs.next()) {
					int id = rs.getInt(1);
					department.setId(id);
					
					DB.closeResultSet(rs);
				}
			}
			else {
				throw new DbException("Erro inesperado, usuario não inserido: ");
			}
			
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(ps);
		}
	}

	@Override
	public void update(Department department) {
		
		PreparedStatement ps = null;
		
		try {
			
			ps = conn.prepareStatement("UPDATE department "
					+ "SET Name = ? "
					+ "WHERE Id = ?");
			ps.setString(1, department.getName());
			ps.setInt(2, department.getId());
			
			ps.executeUpdate();
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(ps);
		}
	}

	@Override
	public void deleteById(Integer id) {
		
		PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement("DELETE FROM department WHERE Id = ?");
			
			ps.setInt(1, id);
			
			ps.executeUpdate();
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(ps);
		}
	}

	@Override
	public Department findById(Integer id) {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			
			ps = conn.prepareStatement("SELECT * FROM department "
					+ "WHERE Id = ?");
			
			ps.setInt(1, id);
			
			rs = ps.executeQuery();
			
			if(rs.next()) {
				Department dep = instantiateDepartment(rs);
				return dep;
			}
			return null;
			
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(ps);
		}
	}
	
	public Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
		dep.setId(rs.getInt("Id"));
		dep.setName(rs.getString("Name"));
		return dep;
	}

	@Override
	public List<Department> findAll() {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			
			List<Department> list = new ArrayList<Department>();
			
			ps = conn.prepareStatement("SELECT * FROM department");
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				Department dep = instantiateDepartment(rs);
				list.add(dep);
			}
			
			return list;
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(ps);
		}
	}
}
