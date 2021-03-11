package musicfy;

public class Musicfy {
	public static void main(String[] args) 
	{
		Boolean original = true;
		Boolean test = false;
		
		if(original) originalMain();
		if(test)     testMain();
		
	}
	public static void originalMain()
	{
		User user1 = new User("Marcos Paulo", "777.777.777-77");
		User user2 = new User("Cookiezi", "111.111.11-11");
		Song song1 = new Song("Seven Nation Army", "Rock", "The White Stripes");
		Song song2 = new Song("Crazy Train", "Rock", "Ozzy Osbourne");
		Song song3 = new Song("Feels", "Pop", "Calvin Harris");
		Song song4 = new Song("Roar", "Pop", "Katy Perry");
		Song song5 = new Song("Anima", "Hardcore", "Xi");
		Song song6 = new Song("Freedom Dive", "Hardcore", "Xi");
		Song song7 = new Song("Teo", "Hardcore", "Omoi");
		Playlist rockPlaylist = new Playlist("Awesome Rock Songs", "Rock");
		rockPlaylist.addSong(song1);
		rockPlaylist.addSong(song2);
		Playlist osuPlaylist = new Playlist("Osu Memories", "hardcore");
		osuPlaylist.addSong(song5);
		osuPlaylist.addSong(song6);
		osuPlaylist.addSong(song7);
		user1.addPlaylist(rockPlaylist);
		user2.addPlaylist(osuPlaylist);

		System.out.println(user1.getPlaylists().get(0).getDetails());
		System.out.println("");
		System.out.println(user2.getDetails());
		System.out.println(osuPlaylist.play().getName());
		System.out.println(osuPlaylist.play().getName());
		System.out.println(osuPlaylist.play(true).getName());		
	}
	
	public static void testMain()
	{
		User user1 = new User("Marcos Paulo", "777.777.777-77");
		User user2 = new User("Cookiezi", "111.111.11-11");
		Song song1 = new Song("Seven Nation Army", "Rock", "The White Stripes");
		Song song2 = new Song("Crazy Train", "Rock", "Ozzy Osbourne");
		Song song3 = new Song("Feels", "Pop", "Calvin Harris");
		Song song4 = new Song("Roar", "Pop", "Katy Perry");
		Song song5 = new Song("Anima", "Hardcore", "Xi");
		Song song6 = new Song("Freedom Dive", "Hardcore", "Xi");
		Song song7 = new Song("Teo", "Hardcore", "Omoi");
		Playlist rockPlaylist = new Playlist("Awesome Rock Songs", "Rock");
		rockPlaylist.addSong(song1);
		rockPlaylist.addSong(song2);
		rockPlaylist.addSong(song3);
		rockPlaylist.addSong(song4);
		rockPlaylist.addSong(song5);
		rockPlaylist.addSong(song6);
		rockPlaylist.addSong(song7);
		
		Playlist osuPlaylist = new Playlist("Osu Memories", "hardcore");
		osuPlaylist.addSong(song5);
		osuPlaylist.addSong(song6);
		osuPlaylist.addSong(song7);
		user1.addPlaylist(rockPlaylist);
		user2.addPlaylist(osuPlaylist);

		System.out.println("USER 1");
		System.out.println(user1.getPlaylists().get(0).getDetails());
		System.out.println(user1.getDetails());
		System.out.println("USER 2");
		System.out.println(user2.getPlaylists().get(0).getDetails());
		System.out.println(user2.getDetails());
		
		
		System.out.println("");
		System.out.println("PLAY SONGS rockPlaylist");
		System.out.println(rockPlaylist.play().getName()); // 1
		System.out.println(rockPlaylist.play().getName()); // 2
		System.out.println(rockPlaylist.play().getName()); // 3
		System.out.println(rockPlaylist.play().getName()); // 4
		System.out.println(rockPlaylist.play().getName()); // 5
		System.out.println(rockPlaylist.play().getName()); // 6
		System.out.println(rockPlaylist.play().getName()); // 7
		System.out.println(rockPlaylist.play().getName()); // 8
		System.out.println(rockPlaylist.play().getName()); // 9
		System.out.println(rockPlaylist.play().getName()); // 10
		
		System.out.println("");
		System.out.println("PLAY SONGS osuPlaylist");
		System.out.println(osuPlaylist.play(true).getName()); //  1
		System.out.println(osuPlaylist.play(true).getName()); //  2
		System.out.println(osuPlaylist.play(true).getName()); //  3
		System.out.println(osuPlaylist.play(true).getName()); //  4
		System.out.println(osuPlaylist.play(true).getName()); //  5
		System.out.println(osuPlaylist.play(true).getName()); //  6
		System.out.println(osuPlaylist.play(true).getName()); //  7
		System.out.println(osuPlaylist.play(true).getName()); //  8
		System.out.println(osuPlaylist.play(true).getName()); //  9
		System.out.println(osuPlaylist.play(true).getName()); //  10

		
		
	}	
	
}