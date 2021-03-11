package booking;

public class TivialLog {
	
	enum Loglevel
	{
		DEBUG,
		INFO,
		WARN,
		ERROR
	}
	private static Loglevel loglevel;
	private static TivialLog logInstance = null;
	
	private TivialLog()
	{
		// set loglevel
		TivialLog.loglevel = TivialLog.Loglevel.DEBUG;
	}
	
	public static TivialLog getInstance()
	{
		if(TivialLog.logInstance == null)
		{
			TivialLog.logInstance = new TivialLog();
		}
		return TivialLog.logInstance;
	}
	
	public void Error(String val)
	{
		if(loglevel == Loglevel.DEBUG || 
		   loglevel == Loglevel.INFO  || 
		   loglevel == Loglevel.WARN  || 
		   loglevel == Loglevel.ERROR)
		{
			System.out.println("[DEBUG] " + val);
		}
	}
	
	public void Warn(String val)
	{
		if(loglevel == Loglevel.DEBUG || 
		   loglevel == Loglevel.INFO  || 
		   loglevel == Loglevel.WARN  )
		{
			System.out.println("[WARN]  " + val);
		}
	}	
	
	public void Info(String val)
	{
		if( loglevel == Loglevel.INFO  || 
			loglevel == Loglevel.DEBUG)
			{
				System.out.println("[INFO]  " + val);
			}
	}	
	
	public void Debug(String val)
	{
		if(loglevel == Loglevel.DEBUG)
			{
				System.out.println("[DEBUG] " + val);
			}
	}	



	
	
}
