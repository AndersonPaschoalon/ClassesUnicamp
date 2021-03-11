/**
4. Booking.java - A classe Booking devera ser responsavel pela 
criacao e cancelamento de reservas.

 	[CRIACAO DE RESERVAS]
	* O metodo responsavel pela criacao de reservas devera receber 
		- o usuario,
	  	- o hotel, 
	  	- o numero do quarto desejado 
	  	- e numero de dias, 
	* Entao devera descontar do saldo atual do usuario o valor total 
	  da reserva. 
	* Para a criacao de reservas, o Hotel desejado devera ser con
	  sultado para: 
		- Verificar a disponibilidade dos quartos;
		- O valor da diaria;
		- A aceitacao ou nao de fumantes devera ser respeitada. 
		
	[CANCELAMENTO DE RESERVAS]
	* O metodo responsavel pelo cancelamento de reservas devera receber: 
		- o usuario;
		- o hoteL;
		- o numero do quarto;
	* Devera reembolsar o usuario em 70% do valor pago;
	* Alterar o status do quarto de ocupado para livre. 
	* Um cancelamento de reserva so podera ser efetivado caso antes seja
	  checada a existencia daquela reserva.
**/

package booking;

public class Booking 
{
	private static TivialLog tlog = TivialLog.getInstance();
	
	public static void main(String[] args)
	{
		// 1. Crie os seguintes Hoteis:
		// (a) Praia Tropical; Rua Tajuba, 201 - Florianopolis, SC; 3225-8997; R$100 a diaria normal e R$ 900 a diaria vip.
		// (b) Campo Florestal; Rua Monteiro, 456 - Goiania, GO; 3654-8974; R$50 a diaria normal e R$ 2000 a diaria vip.
		// 3. Crie dois tipos de quartos:
		// (a) Nao vip; nao aceita fumantes; nao possui ar condicionado.
		// (b) Vip; aceita fumantes; possui ar condicionado.		
		Hotel praiaTropical = new Hotel("Praia Tropical", 
				                        "Rua Tajuba, 201 - Florianopolis, SC", 
				                        "3225-8997", 
				                        900, true, true, 
				                        100,  false, false);
		Hotel campoFlorestal = new Hotel("Campo Florestal", 
                "Rua Monteiro, 456 - Goiania, GO", 
                "3654-8974", 
                2000, true, true, 
                50,  false, false);
		
		// 2. Crie os seguintes usuarios:
		// (a) Roberci Silva; 784245698-21; 12/04/1996; Masculino; R$ 1000; fumante.
		// (b) Marcela Domingues; 269784061-45; 22/07/1998; Feminino; R$ 2000; nao fumante.
		User roberciSilva = new User("Roberci Silva", 
				                     "784245698-21", 
				                     "12/04/1996", 
				                     "Masculino", 
				                     1000, true);
		User marcelaDomingues = new User("Marcela Domingues", 
                                         "269784061-45", 
                                         "22/07/1998", 
                                         "Feminino", 
                                         2000, false);		
		
		// 4. Crie as seguintes reservas:
		// (a) Roberci Silva; Hotel Praia Tropical; quarto numero 2; 1 dia.
		// (b) Marcela Domingues; Hotel Campo Florestal; quarto numero 13; 4 dias.
		msgBook(createBooking(roberciSilva, praiaTropical, 2, 1));
		msgBook(createBooking(marcelaDomingues, campoFlorestal, 13, 4));
		tlog.Debug("-- [USER] " + roberciSilva.info());
		tlog.Debug("-- [USER] " + marcelaDomingues.info());
		tlog.Debug("-- [HOTEL] " + praiaTropical.info());
		tlog.Debug("-- [HOTEL] " + campoFlorestal.info());	
		

		// 5. Tente reservar para Roberci Silva, no Hotel Praia Tropical, o quarto de numero 87 por 1 dia.
		msgBook(createBooking(roberciSilva, praiaTropical, 87, 1));
		
		// 6. Tente cancelar a reserva de Marcela Domingues no Hotel Praia Tropical, no quarto de numero 22.
		msgCancel(cancelBooking(marcelaDomingues, praiaTropical, 22));
		
		// 7. Reserve para Roberci Silva, no Hotel Campo Florestal, o quarto de numero 99 por 1 dia.
		msgBook(createBooking(roberciSilva, campoFlorestal, 99, 1));
		// 8. Cancele a reserva do item acima.
		msgCancel(cancelBooking(roberciSilva, campoFlorestal, 99));
		
		// 9. Tente reservar para Marcela Domingues, no Hotel Campo Florestal, o quarto de numero 87 por 1 dia.		
		msgBook(createBooking(marcelaDomingues, campoFlorestal, 87, 1));
		msgCancel(cancelBooking(marcelaDomingues, campoFlorestal, 87));
		msgBook(createBooking(marcelaDomingues, campoFlorestal, 87, 1));
	}
	
	

	public static void msgCancel(Boolean res)
	{
		System.out.println("Booking cancellation " + ((res)? "was wuccessful." : "FAILED") + "\n");
	}
	
	public static void msgBook(Boolean res)
	{
		System.out.println("Booking booking " + ((res)? "was wuccessful." : "FAILED") + "\n");
	}	
	
	
	
	/*
	* O metodo responsavel pelo cancelamento de reservas devera receber: 
		- o usuario;
		- o hoteL;
		- o numero do quarto;
	* Devera reembolsar o usuario em 70% do valor pago;
	* Alterar o status do quarto de ocupado para livre. 
	* Um cancelamento de reserva so podera ser efetivado caso antes seja
	  checada a existencia daquela reserva.
	 */
	public static Boolean cancelBooking(User guest, Hotel theHotel, int roomNumber)
	{
		int temp = 0;
		int backMoney = 0;
		if(guest == null || theHotel == null || roomNumber < 0 )
		{
			tlog.Info("-- CANT CANCEL invalid parameters @Booking.cancelBooking() ");
			//System.out.println("-- CANT CANCEL invalid parameters @Booking.cancelBooking() ");
			return false;
		}
		// check if is a valid room
		if(theHotel.getRoom(roomNumber) == null)
		{
			tlog.Info("-- CANT CANCEL invalid room number [" + roomNumber + "]");
			//System.out.println("-- CANT CANCEL invalid room number [" + roomNumber + "]");
			return false;
		}
		if(!theHotel.getRoom(roomNumber).isOccupied())
		{
			tlog.Info("-- CANT CANCEL not occupied [" + roomNumber + "]");
			//System.out.println("-- CANT CANCEL not occupied [" + roomNumber + "]");			
			return false;
		}
		else 
		{
			if(theHotel.getRoom(roomNumber).checkUser(guest))
			{
				temp = theHotel.roomPrice(roomNumber) * theHotel.getRoom(roomNumber).bookDays();
				backMoney = (int)( 0.7 * (double)temp);
				tlog.Info("--  back money:" + backMoney);
				//System.out.println("--  back money:" + backMoney);
				guest.addMoney(backMoney);
				theHotel.getRoom(roomNumber).releaseRoom();
				return true;
			}
			else 
			{
				tlog.Info("--  cannot cancel, wrong user");
				//System.out.println("--  cannot cancel, wrong user" );
			}
			
		}
		return false;
	}
	
	public static Boolean createBooking(User newGuest, Hotel theHotel, int desiredRoom, int numberOfDays)
	{
		// check input
		if(newGuest == null || theHotel == null || numberOfDays <= 0)
		{
			tlog.Info("--  invalid parameters @Booking.createBooking() ");
			//System.out.println();
			return false;
		}
		// check if is a valid room
		if(theHotel.getRoom(desiredRoom) == null)
		{
			tlog.Info("--  invalid room number [" + desiredRoom + "]");
			return false;
		}
		// verify if room is available
		if(theHotel.getRoom(desiredRoom).isOccupied())
		{
			tlog.Info("-- room [" + desiredRoom + "] is already occupied!");
			return false;
		}
		// verify smokers rule
		if(newGuest.userIsSmoker())
		{
			if(!theHotel.getRoom(desiredRoom).roomAcceptSmokers())
			{
				tlog.Info("-- cant book room [" + desiredRoom + 
						  "] to guest because room do not accept smokers!");
		        return false;				
			}
		}
		// calc value
		int bookingVal = theHotel.roomPrice(desiredRoom) * numberOfDays;
		// if guest has the money, book the room and sub the money
		if(bookingVal <= newGuest.getBalance())
		{
			theHotel.getRoom(desiredRoom).bookRoom(newGuest, numberOfDays);
			newGuest.subMoney(bookingVal);
			return true;
		}
		else 
		{
			tlog.Info("-- cant book the room, user does not have enough cash!");
			tlog.Info("user info: " + newGuest.info());
		}
		return false;
	}
}
