package app.statzapps.quotebook;

public class Quote {
	// Instance variables
	private String name;
	private String quote;
	private long rowId;
	
	//Empty constructor
	public Quote(){}
	
	/*
	 * Constructs a quote.
	 * @param n: the name of the person who said the quote.
	 * @param q: the text of the quote.
	 * @param rowID: the rowId where the quote is stored in the database.
	 */
	public Quote(String n, String q, long rowID)
	{
		name = n;
		quote = q;
		rowId = rowID;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getQuote()
	{
		return quote;
	}
	
	public long getId()
	{
		return rowId;
	}
	
	public String toString()
	{
	  return "Name: " +  name + "\n" +
		"Quote: " + quote + "\n" +
		"Id:" + rowId;		
	}

}
