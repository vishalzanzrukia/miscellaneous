package miscellaneous;

public class SortPage {

	private String month;
	
	public SortPage(String month)
	{
		this.setMonth(month);
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.month;
	}
	
	
}
