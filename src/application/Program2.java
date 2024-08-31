package application;

import java.util.List;
import java.util.Scanner;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class Program2 {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		DepartmentDao departmentDao = DaoFactory.createDepartmentDao();
		
		System.out.println("=== TESTE 1: findAll ====");
		List<Department> list = departmentDao.findAll();
		
		for(Department d : list) {
			System.out.println(d);
		}
		
		System.out.println("\n=== TESTE 2: findById ====");
		System.out.println("\nInforme o ID do Departamento: ");
		int id = sc.nextInt();
		
		Department dep = departmentDao.findById(id);
		System.out.println(dep);
		
		System.out.println("\n=== TESTE 3: insert ====");
		
		Department department = new Department(8, "Pagode");
		departmentDao.insert(department);
		System.out.println("Novo id inserido: " + department.getId());
		
		System.out.println("\n=== TESTE 4: update ====");
		
		Department depa = new Department();
		depa = departmentDao.findById(7);
		depa.setName("SAMBA");
		departmentDao.update(depa);
		System.out.println("Update feito com sucesso!");
		
		System.out.println("\n=== TESTE 5: delete ====");
		
		System.out.print("Informe o Id que deseja deletar: ");
		int idDelete = sc.nextInt();
		departmentDao.deleteById(idDelete);
		System.out.println("Deletado com sucesso!");
		
		sc.close();
	}
}
