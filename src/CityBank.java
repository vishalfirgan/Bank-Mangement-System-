import java.sql.*;
import java.util.Scanner;

public class CityBank implements Bank {
	private Scanner scan = new Scanner(System.in);
	private Connection con = null;

	private static CityBank ref = null;

//making singleton class and making constructor private
	private CityBank() {
		String url = "jdbc:mysql://localhost:3306/myjdbcdb";
		String username = "root";
		String password = "root";
		try {
			con = DriverManager.getConnection(url, username, password);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static CityBank getInstance() {
		if (ref == null) {
			ref = new CityBank();
		}
		return ref;
	}

	public void register() {
		System.out.println("WelCome to registration page");

		String query = "insert into CityBank values(?,?,?,?,?,?,?)";

		System.out.println("Enter your name:");
		String name = scan.next();

		System.out.println("Enter your accno:");
		String AccNo = scan.next();

		System.out.println("Enter your password:");
		String password = scan.next();

		System.out.println("Confirm Passsword");
		String dummypassword = scan.next();

		System.out.println("Enter your email:");
		String email = scan.next();

//		System.out.println("Enter your phone no:");
//		int phone = scan.nextInt();

		System.out.println("Enter your phone no:");
		String phone = scan.next();

		System.out.println("Enter your age:");
		int age = scan.nextInt();

		System.out.println("Enter initial amount");
		int amount = scan.nextInt();

		boolean flag = password.equals(dummypassword);
		try {
			if (flag == true && AccNo.length() == 9 && password.length() >= 6 && phone.length() == 10 && amount >= 1000
					&& age >= 18) {
				PreparedStatement psmt = con.prepareStatement(query);
				psmt.setString(1, AccNo);
				psmt.setString(2, password);
				psmt.setString(3, name);
				psmt.setInt(4, amount);
				psmt.setInt(5, age);
				psmt.setString(6, email);
				psmt.setString(7, phone);

				try {
					psmt.execute();
				} catch (Exception e) {
					System.out.println("server problem try again later.");
				}

				System.out.println("Registration successfull.. welcome to CityBank.");

			} else {
				System.out.println("Please enter valid data");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void logIn() {
		System.out.println("Enter accno:");
		String enteredAccNo = scan.next();
		System.out.println("Enter password:");
		String enteredPassword = scan.next();

		String query1 = "select amount from CityBank where accno='" + enteredAccNo + "' and password='"
				+ enteredPassword + "'";
		try {
			Statement st = con.createStatement();

			ResultSet rs = st.executeQuery(query1);

			boolean flag = rs.next();

//			System.out.println(flag);
			if (flag == true) {
				int amt = rs.getInt(1);
				System.out.println("Logged In Successfully..");
				System.out.println("Balance is " + amt);
			} else {
				System.out.println("invalid password or accno.");
			}
		} catch (Exception e) {
			System.out.println("There is server issue. Please try again later.");
//			e.printStackTrace();
		}

	}

	public void checkBalance() {
		System.out.println("Enter accno:");
		String enteredAccNo = scan.next();
		System.out.println("Enter password:");
		String enteredPassword = scan.next();

		String query1 = "select amount from CityBank where accno='" + enteredAccNo + "' and password='"
				+ enteredPassword + "'";
		try {
			Statement st = con.createStatement();

			ResultSet rs = st.executeQuery(query1);

			boolean flag = rs.next();

//			System.out.println(flag);
			if (flag == true) {
				int amt = rs.getInt(1);

				System.out.println("Balance is " + amt);
			} else {
				System.out.println("Invalid password or accno.");
			}
		} catch (Exception e) {
			System.out.println("There is server issue. Please try again later.");
//			e.printStackTrace();
		}

	}

	public void Transfer() {

		System.out.println("Enter FromAccNo:");
		String FromAccNo = scan.next();
		System.out.println("Enter password :");
		String Fpassword = scan.next();
		System.out.println("Enter toAccNo:");
		String toAccNo = scan.next();
		System.out.println("Enter amount to be transfered:");
		int amt = scan.nextInt();

		String q1 = "update CityBank set amount=amount-? where accno=? and password=?";
		String q2 = "update CityBank set amount=amount+? where accno=?";
		String CheckBalanceQuery = "select (amount-?) from CityBank where accno=? and password=?";

		try {
			con.setAutoCommit(false);

			PreparedStatement ps = con.prepareStatement(CheckBalanceQuery);

			ps.setInt(1, amt);
			ps.setString(2, FromAccNo);
			ps.setString(3, Fpassword);

			int result = 0;
			ResultSet rs1 = ps.executeQuery();

			while (rs1.next()) {
				result = rs1.getInt(1);
				rs1.next();
			}
//			System.out.println(result);
			if (result < 0) {
				System.out.println("Insufficient balance.. you need " + (-result)
						+ " Rs more in you bank balance to do this transaction");
				con.rollback();
			} else {
				PreparedStatement ps1 = con.prepareStatement(q1);

				PreparedStatement ps2 = con.prepareStatement(q2);

				ps1.setInt(1, amt);
				ps1.setString(2, FromAccNo);
				ps1.setString(3, Fpassword);

				ps2.setInt(1, amt);
				ps2.setString(2, toAccNo);
				try {
					int rowsAffected1 = ps1.executeUpdate();
					int rowsAffected2 = ps2.executeUpdate();
					if (rowsAffected1 == 1 && rowsAffected2 == 1) {
						System.out.println("Amount trasfered successfully :)");
						con.commit();
					} else {
						System.out.println("Please enter valid credentials");
						con.rollback();
					}

				} catch (Exception e1) {
					System.out.println("There is server problem kindly try again..");
					con.rollback();
//					e1.printStackTrace();
				}

			}

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	public void ChangePassword() {
		System.out.println("Enter account number:");
		String eAccNo = scan.next();
		System.out.println("Enter your password:");
		String epassword = scan.next();
		boolean flag = false;

		String query1 = "select password from CityBank where accno=? and password=?";
		try {
			PreparedStatement pstmt = con.prepareStatement(query1);
			pstmt.setString(1, eAccNo);
			pstmt.setString(2, epassword);

			ResultSet rs = pstmt.executeQuery();

			flag = rs.next();
			System.out.println(flag);

		} catch (Exception e1) {
			e1.printStackTrace();
		}

		System.out.println("Enter new password");
		String s1 = scan.next();
		System.out.println("Confirm new password");
		String s2 = scan.next();

		boolean flag1 = s1.equals(s2);
//     System.out.println("ok1");
		if (flag == true && flag1 == true) {

//			System.out.println("Inside two flag true");
			String setQuery = "update CityBank set password=? where accno=? and password=?";
//			System.out.println("ok2");
			try {
				System.out.println("Inside try ps2");
				PreparedStatement pstmt1 = con.prepareStatement(setQuery);

				pstmt1.setString(1, s1);
				pstmt1.setString(2, eAccNo);
				pstmt1.setString(3, epassword);

				pstmt1.execute();
				System.out.println("Password updated successfully.");

			} catch (Exception e) {
				System.out.println("There is some server problem .kindly try again later.");
//				e.printStackTrace();
			}

		} else {
			System.out.println("Invalid credentials.");
		}

	}

	public void deleteAccount() {

		boolean confirm = false;
		System.out.println("DO YOU REALLY WANT TO DELETE ACCOUNT!!!!!!!");
		System.out.println("Type true to confirm deletion of account or false to not to delete account");
		confirm = scan.nextBoolean();
		if (confirm == true) {
			System.out.println("ok we will procede to delete");
			System.out.println("Enter your accountNo:");

			String eAccno = scan.next();
			System.out.println("Enter your password:");
			String ePassword = scan.next();
			String query1 = "delete from CityBank where accno=? and password=?";
			try {
				PreparedStatement ps1 = con.prepareStatement(query1);
				ps1.setString(1, eAccno);
				ps1.setString(2, ePassword);

				int affectedRows = ps1.executeUpdate();

				if (affectedRows == 1) {
					System.out.println("Account Deleted successfully.");
					System.out.println("Let us know if you need any other financial services.");
				} else {
					System.out.println("Please Enter valid credentails");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			return;
		}

	}

	public void viewProfile() {
		System.out.println("User Profile");
		System.out.println("Enter your AccNo:");
		String eAccNo = scan.next();
		System.out.println("Enter your password:");
		String ePassword = scan.next();

		String query1 = "select * from CityBank where accno=? and password=?";
		try {
			PreparedStatement ps1 = con.prepareStatement(query1);
			ps1.setString(1, eAccNo);
			ps1.setString(2, ePassword);

			ResultSet rs = ps1.executeQuery();

			boolean flag = rs.next();

			if (flag == true) {
				System.out.println();

				System.out.println("*************CityBank****************\n");
				System.out.println("Account No :> " + rs.getString(1));
				System.out.println("Password   :> " + rs.getString(2));
				System.out.println("Name       :> " + rs.getString(3));
				System.out.println("Balance    :> " + rs.getInt(4));
				System.out.println("Age        :> " + rs.getInt(5));
				System.out.println("Email      :> " + rs.getString(6));
				System.out.println("Phone No   :> " + rs.getString(7));
				System.out.println("\n**************************************");

				System.out.println();

			} else {
				System.out.println("Please enter valid credetails.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateProfile() {

	}

}
