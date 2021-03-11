/**
2. Room.java - A classe Room deveria representar um quarto. Com 
informacoes como: 
	- se o quarto e vip;
	- se esta ocupado;
	- se aceita fumantes;
	- se possui ar condicionado. 
	- Metodos para verificar e alterar o status de ocupado. 
	* Considere que cada quarto e ocupado por apenas uma pessoa.
 **/

package booking;

public class Room 
{
	private Boolean isVip;
	private Boolean isRoomOccupied;
	private Boolean acceptSmokers;
	private Boolean hasAirConditioner;
	private String bookCpf;
	private int bookDays;
	
	public Room(Boolean roomIsVip, Boolean roomAcceptSmokers, Boolean roomHasAirConditioner)
	{
		this.isVip = roomIsVip;
		this.isRoomOccupied = false;
		this.acceptSmokers = roomAcceptSmokers;
		this.hasAirConditioner = roomHasAirConditioner;
		this.bookCpf = "";
		this.bookDays = 0;
	}

	public Boolean isVip()
	{
		return this.isVip;
	}
	
	public Boolean isOccupied()
	{
		return this.isRoomOccupied;
	}
	
	public void releaseRoom()
	{
		this.isRoomOccupied = false;
		this.bookCpf = "";
		this.bookDays = 0;
	}
	
	public Boolean bookRoom(User newGuest, int days)
	{
		if(this.isRoomOccupied)
		{
			return false;
		}
		else 
		{
			this.bookDays = days;
			this.bookCpf = newGuest.getCpf();
			this.isRoomOccupied = true;
			return true;
		}
	}
	
	public Boolean checkUser(User guest)
	{
		if(!this.isOccupied())
		{
			return false;
		}
		else 
		{
			if(guest.getCpf() == this.bookCpf)
			{
				return true;
			}
			System.out.println();
		}
		return false;
	}
	
	public Boolean roomAcceptSmokers()
	{
		return this.acceptSmokers;
	}
	
	public Boolean roomHasAirConditioner()
	{
		return this.hasAirConditioner;
	}

	public int bookDays()
	{
		return this.bookDays;
	}
}
