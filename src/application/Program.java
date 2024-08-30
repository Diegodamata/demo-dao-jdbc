package application;

import java.util.List;
import java.util.Scanner;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		SellerDao sellerDao = DaoFactory.createSellerDao();
		
		System.out.println("=== TEST 1: Seller findById ===");
		Seller seller = sellerDao.findById(3);
		System.out.println(seller);
		
		System.out.println("\n=== TEST 2: Seller findByDepartment ===");
		
		Department department = new Department(2, null);
		
		List<Seller> list = sellerDao.findByDepartment(department);
		
		for(Seller s : list) {
			System.out.println(s);
		}
		
		System.out.println("\n=== TEST 3: Seller findAll ===");
		
		list = sellerDao.findAll();
		
		for(Seller s : list) {
			System.out.println(s);
		}
		
		/*System.out.println("\n=== TEST 4: Insert ===");
		
		Seller newSeller = new Seller(7, "Marcos", "marcos@gmail.com", new Date(), 3500.0, department);
		sellerDao.insert(newSeller);
		System.out.println("Inserido! novo id: " + newSeller.getId());
		
		System.out.println("\n=== TEST 5: Update ===");
		seller = sellerDao.findById(2);
		seller.setName("Diego Pontes");
		sellerDao.update(seller);
		System.out.println("Update completado!");*/
		
		
		System.out.println("\n=== TEST 5: Delete ===");
		
		System.out.print("Informe o id: ");
		int id = sc.nextInt();
		sellerDao.deleteById(id);
		System.out.println("Delete completado!");
		
		sc.close();
	}
}
