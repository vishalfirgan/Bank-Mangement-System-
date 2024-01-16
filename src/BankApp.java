
import java.sql.*;
import java.util.Scanner;

public class BankApp {

	public static void main(String[] args) {

		System.out.println("WelCome to Online Banking. Thank You For Choossing Us.\n");

		Scanner scan = new Scanner(System.in);

		BankFactory factory = new BankFactory();
		
		System.out.println("Enter your bank name:");
		String bankName = scan.next();
		
		Bank ref = null;
		
		if (bankName.equals("kodnest"))
		{
			ref = (KodnestBank) factory.getBank(bankName);
		} 
		else if (bankName.equals("city"))
		{
			ref = (CityBank) factory.getBank(bankName);
		}

//		KodnestBank kb = KodnestBank.getInstance();

		while (true) {
			System.out.println("<><><><><><><><><><><><><><><><><><><><><>");
			System.out.println();
			System.out.println("---> Press 1 To Register");
			System.out.println("---> Press 2 To Login");
			System.out.println("---> Press 3 To Check Balance");
			System.out.println("---> Press 4 To Transfer Amount");
			System.out.println("---> Press 5 To Change password");
			System.out.println("---> Press 6 To Delete Account");
			System.out.println("---> Press 7 To View Profile");
			System.out.println("---> Press 8 To STOP");

			System.out.println();
//			System.out.println("LeAvE YoUr VaLuEaBle FeEdBaCk :)");
			System.out.println("<><><><><><><><><><><><><><><><><><><><><>");
			System.out.println();
			int n = scan.nextInt();

			switch (n) {
			case 1:
				ref.register();// done
				break;
			case 2:
				ref.logIn();// done
				break;
			case 3:
				ref.checkBalance();// done
				break;
			case 4:
				ref.Transfer();// done
				break;
			case 5:
				ref.ChangePassword();// done
				break;
			case 6:
				ref.deleteAccount();
				break;
			case 7:
				ref.viewProfile();
				break;
			case 8:
				ref.updateProfile();
			default:
				System.out.println(" Thank You  For Using Kodnest Bank.\n LeAvE YoUr VaLuEaBle FeEdBaCk :)");
				System.exit(0);
				break;
			}
		}

	}

}
