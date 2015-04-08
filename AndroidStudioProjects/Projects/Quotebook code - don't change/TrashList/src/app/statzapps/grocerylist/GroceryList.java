package app.statzapps.grocerylist;

public class GroceryList {
	private String name;
	private String tableName;
	private long id;
		
	public GroceryList(String n)
	{
		name = n;
	}
	
	public GroceryList(String n, String tn, long ID)
	{
		name = n;
		tableName = tn;
		id = ID;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getTableName()
	{
		return tableName;
	}
	
	public long getID()
	{
		return id;
	}
}