
public class BankFactory {

	public static Bank getBank(String nameOfBank) {

		Bank ref = null;
		
		if (nameOfBank.equals("kodnest")) {
			ref = KodnestBank.getInstance();
		} else if (nameOfBank.equals("city")) {
			ref = CityBank.getInstance();
		}

		return ref;

	}
}
