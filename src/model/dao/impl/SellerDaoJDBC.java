package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao{
	
	//o DAO ira ter uma dependencia com a conexão, vou ter o conn a disposição em qualquer lugar da classe
	private Connection conn;
	
	//para formar a injeção de dependencia eu crio um construtor recebendo uma conexão com o DB
	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Seller department) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Seller department) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		
	}

	
	//implementando o metodo atraves de uma operação sql e encontrar o primeiro que corresponde ao id
	@Override
	public Seller findById(Integer id) {
		
		PreparedStatement ps = null; //serve para uma execução (comando) especifica no DB
		ResultSet rs = null; //ira receber o resultado do statment em formato de tabela
		//a conexão com o DB não posso fazer aqui pois outras classes tambem pode usar essa conexão, 
		
		try {
			
			ps = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "WHERE seller.Id = ?");
			
			ps.setInt(1, id); //configurando o "?"
			rs = ps.executeQuery(); //pedindo para executar a operação
			
			if(rs.next()) { //verificando se existe linha
				//resultSet tras os dados em forma de tabela porem estamos trabalhando com classe, essa classe é responsavel por pegar a nossa tabela e transformar em objeto
				
				//instanciando os objetos e atribuindo os valores chegando do banco para a minha classes
				Department dep = new Department();
				dep.setId(rs.getInt("DepartmentId"));
				dep.setName(rs.getString("DepName"));
				Seller seller = new Seller();
				seller.setId(rs.getInt("Id"));
				seller.setName(rs.getString("Name"));
				seller.setEmail(rs.getString("Email"));
				seller.setBirthDate(rs.getDate("BirthDate"));
				seller.setBaseSalary(rs.getDouble("BaseSalary"));
				seller.setDepartment(dep);
				return seller;
			}
			
			return null; //se não existir uma linha peço para retornar null
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Seller> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
