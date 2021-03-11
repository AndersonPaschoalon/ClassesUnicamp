package musicfy;

import java.util.*; 
public class User 
{
	public enum Gender
	{
		MALE,
		FEMALE,
		NOT_DECLARED
	}
	
	// User infor
	private String name;
	private String cpf;
	private String birthDate;
	private Gender gender;
	private Boolean isSubscriber;
	// User playlists
	private final int NOT_SUBSCRIBER_MAX_PLAYLISTS = 3;
	private final int SUBSCRIBER_MAX_PLAYLISTS = 10;
	private List<Playlist> playlists;
	
	// public methods
	public User(String theName, 
		    String theCpf)
	{
		this.name = theName;
		this.cpf = theCpf;		
		
		this.birthDate = "01/01/2000";
		this.gender = User.Gender.NOT_DECLARED;
		this.isSubscriber = false;
		this.playlists = new LinkedList<Playlist>(); 		
	}
	public User(String theName, 
			    String theCpf, 
			    String theBirthDate, 
			    Gender theGender, 
			    Boolean userIsSubscriber)
	{
		this.birthDate = theBirthDate;
		this.cpf = theCpf;
		this.gender = theGender;
		this.isSubscriber = userIsSubscriber;
		this.name = theName;
		this.playlists = new LinkedList<Playlist>(); 
	}
	
	public String getDetails()
	{
		String details = this.name + ", Playlists: ";
		for (int i = 0; i < this.playlists.size(); i++)
		{
			details += "(" + this.playlists.get(i).getName() + 
					   ", "  + this.playlists.get(i).getGender() + 
					   ", " + " Songs:" + this.playlists.get(i).numberOfSongs() + 
					   ")";
			if(i != this.playlists.size() - 1)
			{
				details += ", ";
			}
		}
		return details;
	}

	public List<Playlist> getPlaylists()
	{
		return this.playlists;
	}
	
	public String getName()
	{
		return this.name;
	}
	public String getCpf()
	{
		return this.cpf;
	}
	public String getBirthDate()
	{
		return this.birthDate;
	}
	public Gender getGender()
	{
		return this.gender;
	}
	public Boolean getIsSubscriber()
	{
		return this.isSubscriber;
	}
	
	public Boolean addPlaylist(Playlist newPlayList)
	{
		newPlayList.changeOwner(this);
		if(this.isSubscriber)
		{
			if(this.playlists.size() >= this.SUBSCRIBER_MAX_PLAYLISTS)
			{
				System.out.println("-- cannot add more playlists for subscriber n:" + this.playlists.size());
				return false;
			}
			else 
			{
				this.playlists.add(newPlayList);
				System.out.println("-- subscriber playlists count: " + this.playlists.size());
				return true;
			}
		}
		else 
		{
			if(this.playlists.size() >= this.NOT_SUBSCRIBER_MAX_PLAYLISTS)
			{
				System.out.println("-- cannot add more playlists for non-subscriber n:" + this.playlists.size());
				return false;
			}
			else 
			{
				this.playlists.add(newPlayList);
				System.out.println("-- non-subscriber playlists count: " + this.playlists.size());
				return true;
			}			
		}
	}
	public Boolean removePlaylist(String playListName)
	{
		for(int i = 0; i < this.playlists.size(); i++)
		{
			if(this.playlists.get(i).getName() == playListName)
			{
				this.playlists.remove(i);
				return true;
			}
		}
		return false;		
	}
	public Boolean addPlaylistToUser(String playlistName, User friend)
	{
		for(int i = 0; i < this.playlists.size(); i++)
		{
			if(this.playlists.get(i).getName() == playlistName)
			{
				friend.addPlaylist(this.playlists.get(i));
				return true;
			}
		}
		return true;			
	}
	public void changeSubscriptionType()
	{
		// TODO
	}
	
	
	/*
	public void setName(String theName)
	{
		this.name = theName;
	} 
	public void setCpf(String theCpf)
	{
		this.name = theCpf;
	}		
	public void setBirthDate(String theBirthDate)
	{
		this.name = theBirthDate;
	}	
	public void setBirthDate(Gender theGender)
	{
		this.gender = theGender;
	}		
	public void setBirthDate(Boolean userIsSubscriber)
	{
		this.isSubscriber = userIsSubscriber;
	}		
	*/

}
