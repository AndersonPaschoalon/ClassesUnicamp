package musicfy;
import java.util.*;  
import java.sql.Time;

public class Playlist 
{
	// playlist info
	private String name;
	private String gender;
	private User owner;
	// playlist properties
	private final int SUBSCRIBER_LIMIT = 100;
	private final int NON_SUBSCRIBER_LIMIT = 10;
	private List<Song> songs;
	private int currentPlayedSong = 0;
	

	public Playlist(String theName, String theGender)
	{
		this.name = theName;
		this.gender = theGender;
		this.owner = null;
		this.songs = new ArrayList<Song>(); 
	}	
	
	public Playlist(User theOwner)
	{
		this.songs = new ArrayList<Song>(); 
		this.changeOwner(theOwner);
	}
	
	public String getDetails()
	{
		String details = "Playlist:" + this.getName() + " Songs: [";
		for(int i = 0; i < this.songs.size(); i++)
		{
			details += this.songs.get(i).getName();
			details += " (" + this.songs.get(i).getArtist() + ")";
			if(i != this.songs.size() - 1)
			{
				details += ", ";
			}
		}
		details += "]";
		return details;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public String getGender()
	{
		return this.gender;
	}
	
	public void changeOwner(User theOwner)
	{
		this.owner = theOwner;
	}
	
	public Boolean addSong(Song theSong)
	{
		if(this.isOwnerSubscriber())
		{
			System.out.println("-- user is a subscriver");
			if(this.songs.size() >= this.SUBSCRIBER_LIMIT)
			{
				System.out.println("-- cannot add more songs n:" + this.songs.size());
				return false;
			}
			else  
			{
				this.helperAddSong(theSong);
				//this.songs.add(theSong);
				return true;
			}
		}
		else 
		{
			System.out.println("-- user is a non-subscriver");
			if(this.songs.size() >= this.NON_SUBSCRIBER_LIMIT)
			{
				System.out.println("-- cannot add more songs n:" + this.songs.size());
				return false;
			}
			else  
			{
				this.helperAddSong(theSong);
				//this.songs.add(theSong);
				return true;
			}			
		}
	}
	
	public Boolean deleteSong(String songName)
	{
		for(int i = 0; i < this.songs.size(); i++)
		{
			if( this.songs.get(i).getName() == songName)
			{
				this.songs.remove(i);
				return true;
			}
		}
		return false;
	}
	
	public Song shorterSong()
	{
		Song shorter = null;
		if( this.songs.size() > 0)
		{
			shorter = this.songs.get(0);
			for (int i = 1; i < this.songs.size(); i++)
			{
				if(shorter.getDurationSeconds() > this.songs.get(i).getDurationSeconds())
				{
					shorter = this.songs.get(i);
				}
			}			
		}
		return shorter;
	}

	public Song largerSong()
	{
		Song larger = null;
		if( this.songs.size() > 0)
		{
			larger = this.songs.get(0);
			for (int i = 1; i < this.songs.size(); i++)
			{
				if(larger.getDurationSeconds() < this.songs.get(i).getDurationSeconds())
				{
					larger = this.songs.get(i);
				}
			}			
		}
		return larger;		
	}

	public int averageDurationSeconds()
	{
		if(this.songs.size() == 0)
		{
			return 0;
		}
		else 
		{
			return this.playlistDurationSeconds()/this.songs.size();
		}
	}

	public int playlistDurationSeconds()
	{
		int total = 0;
		for (int i = 1; i < this.songs.size(); i++)
		{
			total += this.songs.get(i).getDurationSeconds();
		}
		return total;
		
	}

	public String artistWithMoreSongs()
	{
		Hashtable<String, Integer> artistDic = new Hashtable<String, Integer>(); 
		String currentArtist = "";
		Integer nSongs = 0;
		int largerValue = 0;
		String theArtist = "";
		for (int i = 1; i < this.songs.size(); i++)
		{
			currentArtist = this.songs.get(i).getArtist();
			nSongs = (int)artistDic.get(currentArtist);
			nSongs++;
			System.out.println("-- currentArtist:" + currentArtist + ", nSongs:" + nSongs);
			artistDic.put(currentArtist, nSongs);
			if(largerValue < nSongs)
			{
				largerValue = nSongs;
				theArtist = currentArtist;
			}
		}
		return theArtist;
	}
	
	public int numberOfSongs()
	{
		if(this.songs != null)
		{
			return this.songs.size();			
		}
		else 
		{
			return 0;
		}

	}
	
	public Song play()
	{
		return this.play(false);
	}
	
	public Song play(Boolean shuffle)
	{
		Song songToRet = null;
		int randomPos = 0;
		
		if(shuffle)
		{
			randomPos = (int)(Math.random() * ((double)this.songs.size()) );
			while(randomPos == this.currentPlayedSong)
			{
				randomPos = (int)(Math.random() * ((double)this.songs.size()) );
			}
			//System.out.println("-- randomPos:" + randomPos + ", currentPlayedSong:" + this.currentPlayedSong);	
			
			songToRet = this.songs.get(randomPos);
			//System.out.println("-- songToRet:" + songToRet.getName());
			this.currentPlayedSong = randomPos;
		}
		else 
		{
			if(this.currentPlayedSong >= this.songs.size() - 1)
			{
				this.currentPlayedSong = 0;
			}
			this.currentPlayedSong++;
			songToRet = this.songs.get(this.currentPlayedSong);
		}
		return songToRet;
	}
	
	
	// private methods
	private Boolean isOwnerSubscriber()
	{
		if( this.owner == null)
		{
			return false;
		}
		return this.owner.getIsSubscriber();
	}
	
	private void helperAddSong(Song aNewSong)
	{
		if(aNewSong == null)
		{
			return;
		}
		
		List<Song> orderedSongList = new ArrayList<Song>();
		int i = 0;
		int compare = 0;
		int originalSizeSongList = 0;
		Boolean isInserted = false;
		// prevent null String
		if(aNewSong.getName() == null)
		{
			aNewSong.setName("");
		}
		
		// no songs on the list
		if(this.songs.size() == 0)
		{
			this.songs.add(aNewSong);
			this.currentPlayedSong = 0;
		}
		else 
		{
			originalSizeSongList = this.songs.size();
			// insert in the alphabetical order
			for(i = 0; i < originalSizeSongList; i++)
			{
				if(isInserted == false)
				{
					compare = aNewSong.getName().compareTo(this.songs.get(i).getName());
					if (compare > 0)  // the new song is grater, insert the current song
					{  
						orderedSongList.add(this.songs.get(i));
					}
					else // the new song is equals or samaller, insert the new song and the current
					{
						orderedSongList.add(aNewSong);
						orderedSongList.add(this.songs.get(i));
						isInserted = true;
					}				
				}
				else // already inserted, insert the next song to the end
				{
					orderedSongList.add(this.songs.get(i));
				}

			}
			if(isInserted == false)
			{
				orderedSongList.add(aNewSong);
			}
			// update the song list to a ordered version
			this.songs = orderedSongList;	
			this.currentPlayedSong = 0;
		}
	}
	
}
