/**
1. Hotel.java - A classe Hotel deve armazenar as informacoes basicas de
cada hotel como:
	- nome; 
	- endereco de localizacao;
	- telefone de contato.
	* O telefone e endereco podem ser representados por Strings. 
	- Alem disso, cada hotel possui um maximo de 100 quartos  em que os 10 primeiros 
	  deles sao quartos vip, e por isso possuem um preco maior. 
	A classe Hotel deve implementar metodos para: 
	- Consulta dos quartos disponveis; 
	- Preco da diaria de cada quarto; 
	- Impressao do estado atual do Hotel 
		- quantos e quais quartos estao disponiveis; 
		- preco da diaria
		- servicos oferecidos).
**/
package booking;

public class Hotel 
{
	private static TivialLog tlog = TivialLog.getInstance();
	private final int NUMBER_VIPS = 10;
	private final int NUMBER_OF_ROOMS = 100;
	private String name;
	private String address;
	private String phone;
	private Room[] rooms;
	private int priceVip;
	private int priceNotVip;
	private Boolean vipAcceptSmokers;
	private Boolean notVipAcceptSmokers;
	private Boolean vipHasAirConditioner;
	private Boolean notVipHasAirConditioner;
	
	public Hotel(String theName, 
                 String theAddress, 
                 String thePhone,
                 int thePriceVip,
                 Boolean theVipAcceptSmokers, 
                 Boolean theVipHasAirConditioner, 
                 int thePriceNotVip,
                 Boolean theNotVipAcceptSmokers, 
                 Boolean theNotVipHasAirConditioner)
	{
		this.name = theName;
		this.phone = thePhone;
		this.address = theAddress;
		this.rooms = new Room[NUMBER_OF_ROOMS];
		this.initRooms(thePriceVip, 
				       theVipAcceptSmokers, 
				       theVipHasAirConditioner, 
				       thePriceNotVip, 
				       theNotVipAcceptSmokers, 
				       theNotVipHasAirConditioner);
	}

	//
	// public methods
	//
			
	public int numberOfAvailableRooms()
	{
		int freeRooms = 0;
		for(int i = 0; i < NUMBER_OF_ROOMS; i++)
		{
			if(!this.rooms[i].isOccupied())
			{
				freeRooms++;
			}
		}
		return freeRooms;
	}
	
	public int roomPrice(int nRoom)
	{
		if(this.isValidRoom(nRoom))
		{
			if(this.rooms[nRoom].isVip())
			{
				return this.priceVip;
			}
			else
			{
				return this.priceNotVip;
			}
		}
		else 
		{
			tlog.Debug("-- invalid room number " + nRoom);
			//System.out.println("-- invalid room number " + nRoom);
		}
		return 0;
	}
	
	public String info()
	{
 		String info = "";
		info += this.name + " " + this.address + " Phone:" + this.phone + "\n" + 
				"Available Rooms:" + this.numberOfAvailableRooms() + "\n" +  
			    "List Available:[ ";
		for (int i = 0; i < NUMBER_OF_ROOMS; i++)
		{
			if(!this.rooms[i].isOccupied())
			{
				info += i + " ";
			}
		}
		info += "]\n";
		info += "    VIP: " + 
		        ((this.vipAcceptSmokers)? "accept smokers": "do not accept smokers") + ", " +
				((this.vipHasAirConditioner)? "has air conditioner" : "do not have air conditioner") + 
		        "\n";
		info += "Not VIP: " +
				 ((this.notVipAcceptSmokers)? "accept smokers": "do not accept smokers") + 
				 ((this.notVipHasAirConditioner)? "has air conditioner" : "do not have air conditioner");
		return info;
	}

	public Room getRoom(int nRoom)
	{
		if(this.isValidRoom(nRoom))
		{
			return this.rooms[nRoom];
		}
		else 
		{
			return null;
		}
	}
		
	
	//
	// private methods
	//
	
	private void initRooms(int thePriceVip, 
			              Boolean theVipAcceptSmokers, 
			              Boolean theVipHasAirConditioner, 
			              int thePriceNotVip,
			              Boolean theNotVipAcceptSmokers, 
			              Boolean theNotVipHasAirConditioner)
	{
		this.priceVip = thePriceVip;
		this.priceNotVip = thePriceNotVip;
		this.vipAcceptSmokers = theVipAcceptSmokers;
		this.notVipAcceptSmokers = theNotVipAcceptSmokers;
		this.vipHasAirConditioner = theVipHasAirConditioner;
		this.notVipHasAirConditioner = theVipHasAirConditioner;
		
		for(int i = 0; i < NUMBER_OF_ROOMS; i++)
		{
			Room newRoom;
			if(i < NUMBER_VIPS) // create a vip room
			{
				newRoom = new Room(true, theVipAcceptSmokers, theVipHasAirConditioner);
			}
			else // create a not vip room
			{
				newRoom = new Room(false, theNotVipAcceptSmokers, theNotVipHasAirConditioner);
			}
			this.rooms[i] = newRoom;
		}
	}
		
	private Boolean isValidRoom(int roomNumber)
	{
		if(roomNumber < 0 || roomNumber >= NUMBER_OF_ROOMS)
		{
			tlog.Debug("-- index provided is out of bounds: i=" + roomNumber + 
					   " expected i>0 i<" + NUMBER_OF_ROOMS);
			//System.out.println("-- index provided is out of bounds: i=" + roomNumber + 
			//		           " expected i>0 i<" + NUMBER_OF_ROOMS);
			return false;
		}
		else 
		{
			return true;
		}
	}

}
