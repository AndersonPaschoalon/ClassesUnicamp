/**
3. User.java - A classe User deveria armazenar as informacoes do usuario,
como: 
	- nome; 
	- cpf;
	- data de nascimento;
	- genero;
	- saldo atual;
	- se e fumante ou nao. 
	Devera, tambem:
	- possuir metodos para consulta e alteracao de saldo;
	- impressao das informacoes do usuario.
**/

package booking;

public class User 
{
	private String name;
	private String cpf;
	private String birthDate;
	private String gender;
	private int balance;
	private Boolean isSmoker;
	
	public User(String theName, String theCpf, String theBirthDate, String theGender, int theBalance, Boolean userIsSmoker)
	{
		this.name = theName;
		this.cpf = theCpf;
		this.birthDate = theBirthDate;
		this.gender = theGender;
		this.balance = theBalance;
		this.isSmoker = userIsSmoker;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public String getCpf()
	{
		return this.cpf;
	}

	public int getBalance()
	{
		return this.balance;
	}
	
	public void addMoney(int val)
	{
		this.balance += val;
	}
	
	public void subMoney(int val)
	{
		this.balance -= val;
	}	

	public Boolean userIsSmoker()
	{
		return this.isSmoker;
	}
	
	public String info()
	{
		return "Name:" + this.name + ", " + 
		       "Cpf:" + this.cpf + ", " + 
		       "Birth Date:" + this.birthDate + ", " + 
		       "Gender:" + this.gender + ", " + 
		       "Balance:" + this.balance + ", " + 
		       "Is Smoker:" + this.isSmoker ;
	}
	
}
