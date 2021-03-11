package musicfy;

import java.sql.Time;

public class Song 
{
	private String name;
	private String gender;
	private String artist;
	private int durationSeconds;
	
	public Song(String theName, String theGender, String theArtist)
	{
		this.setGender(theGender);
		this.setName(theName);
		this.setArtist(theArtist);
		this.setDuration(0, 0);
	}
	
	public String getDetails()
	{
		return  this.name + "(" + this.artist + ")";
	}
	
	// setters
	public void setDuration(int min, int sec)
	{
		this.durationSeconds = 60*min + sec;	
	}
	public void setGender(String theGender)
	{
		this.gender = theGender;
	}
	public void setName(String theName)
	{
		this.name = theName;
	}
	public void setArtist(String theArtist)
	{
		this.artist = theArtist;
	}
	
	// getters
	public String getGender()
	{
		return this.gender;
	}
	public String getName()
	{
		return this.name;
	}
	public String getArtist()
	{
		return this.artist;
	}
	public int getDurationSeconds()
	{
		return this.durationSeconds;
	}
	
}
