package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	//agora meu metodo esta bem mais inchuto
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
				
				//criando variaveis e chamando os metodos para instanciar
				Department dep = instantiateDepartment(rs);
				Seller seller = instantiateSeller(rs, dep);
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

	//metodo para instanciar o seller pegando os valores retornados no resultSet
	//e passando o dep tambem, como ja tratei a exception em findById eu apenas propago 
	//a exception que pode gerar ao consultar o resultSet
	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
		Seller seller = new Seller();
		seller.setId(rs.getInt("Id"));
		seller.setName(rs.getString("Name"));
		seller.setEmail(rs.getString("Email"));
		seller.setBirthDate(rs.getDate("BirthDate"));
		seller.setBaseSalary(rs.getDouble("BaseSalary"));
		seller.setDepartment(dep);
		return seller;
	}

	//metodos para instanciar os meus objetos (department, seller)
	//o acesso do meu resultSet pode gerar uma exception
	//na minha classe findById ja esta tratando da exception entao eu simplesmente propago a exception
	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
		dep.setId(rs.getInt("DepartmentId"));
		dep.setName(rs.getString("DepName"));
		return dep;
	}

	@Override
	public List<Seller> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Seller> findByDepartment(Department department) {
		
		PreparedStatement ps = null; //serve para uma execução (comando) especifica no DB
		ResultSet rs = null; //ira receber o resultado do statment em formato de tabela
		//a conexão com o DB não posso fazer aqui pois outras classes tambem pode usar essa conexão, 
		
		try {
			
			ps = conn.prepareStatement(
					" SELECT seller.*,department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "WHERE DepartmentId = ? "
					+ "ORDER BY Name");
			
			ps.setInt(1, department.getId()); //configurando o "?"
			
			rs = ps.executeQuery(); //pedindo para executar a operação
			
			//e como são varios valores que pode retornar do banco então criei uma lista
			List<Seller> list = new ArrayList<Seller>();
			
			//criei uma estrutura map vazia, ela não permite repetições de chave
			Map<Integer, Department> map = new HashMap<>();
			
			//meu resultado pode ser 0 ou mais valor então uso a estrutura while para percorrer
			//e adicionar todos os valores na minha lista
			while(rs.next()) { 
				
				//porem dessa forma é uma aplicação ruim, pois cada vendedor vai estar
				//apontando para departamentos direfentes, sendo que é o mesmo departamento
				//Department dep = instantiateDepartment(rs);
				//Seller seller = instantiateSeller(rs, dep);
				//list.add(seller);
				
				//forma correta
				
				//crio uma variavel do tipo department chamo o map e verifico se no meu map ja contem o id que esta na coluna DepatmentId
				//se não existir ira me retornal null
				//ai sim eu instancio o meu objeto
				Department dep = map.get(rs.getInt("DepartmentId"));
				
				if(dep == null) { //se o dep for null quer dizer que não conten esse valor no meu map
					
					dep = instantiateDepartment(rs); //se o dep não estiver vazio, estao atribuo o resultado da instancia do department
					map.put(rs.getInt("DepartmentId"), dep); //entao eu adiciono no meu map o id e o department
					//porem se estiver valor repedido o mep não aceitara, dessa forma os sellers estara apontando pro mesmo department
				}
				
				Seller seller = instantiateSeller(rs, dep);
				list.add(seller);
				
			}
			
			return list; //se não existir uma linha peço para retornar null
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
	}

}
