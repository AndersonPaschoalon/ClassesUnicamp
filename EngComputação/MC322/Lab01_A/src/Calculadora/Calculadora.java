package Calculadora;

import java.util.Scanner;

public class Calculadora 
{
	public static void main(String args[])
	{
		Calculadora.calculadoraJava();
	}
	
	public static void calculadoraJava()
	{
		System.out.println("###################################");
		System.out.println("# Lab  01 (A): Calculadora Java");
		System.out.println("###################################");
		
		Scanner scanner = new Scanner(System.in);  
		boolean exitProgram = false;
		
		while(!exitProgram)
		{
			System.out.println("Escolha a Operação:");
			System.out.println("1 SOMAR");
			System.out.println("2 SUBTRAIR");
			System.out.println("3 MULTIPLICAR");
			System.out.println("4 DIVIDIR");
			System.out.println("5 CAULCULAR FATORIAL");
			System.out.println("6 VERIFICAR SE É PRIMO");
			System.out.println("# QUALQUER OUTRO NÚMERO PARA SAIR");
			System.out.print("op>");
			int inputOp = scanner.nextInt();
			switch(inputOp)
			{
				case 1:
				{
					System.out.println("SOM");
					int a = scanner.nextInt();
					int b = scanner.nextInt();
					int soma = a + b;
					System.out.println("SOM> " + soma);
					break;
				}
				case 2:
				{
					System.out.println("SUB");
					int a = scanner.nextInt();
					int b = scanner.nextInt();
					int sub = a - b;
					System.out.println("SUB> " + sub);					
					break;
				}
				case 3:
				{
					System.out.println("MUL");
					int a = scanner.nextInt();
					int b = scanner.nextInt();
					int mul = a * b;
					System.out.println("MUL> " + mul);							
					break;
				}
				case 4:
				{
					System.out.println("DIV");
					int a = scanner.nextInt();
					int b = scanner.nextInt();
					if(b != 0)
					{
						int div = a / b;
						System.out.println("DIV> " + div);							
					}
					else 
					{
						System.out.println("DIV> NaN");	
					}
						
					break;
				}
				case 5:
				{
					System.out.println("FAT");
					int a = scanner.nextInt();
					if(a < 0)
					{
						System.out.println("FAT> not defined");	
					}
					else
					{
						int fat = 1;
						for(int i = 1; i <= a; i++)
						{
							fat = fat*i;
						}
						System.out.println("FAT> " + fat);	
					}

					break;
				}
				case 6:
				{
					System.out.println("PRI");
					int a = scanner.nextInt();
					boolean isPrime = true;
					if(a == 0 || a == 1)
					{
						System.out.println("PRI> FALSE");	
					}
					else if(a == 2)
					{
						System.out.println("PRI> TRUE");	
					}
					else 
					{
						for(int i = 2; i <= (a / 2); i++)
						{
							if(a % i == 0)
							{
								isPrime = false;
								break;
							}
						}
						if(isPrime)
						{
							System.out.println("PRI> TRUE");
						}
						else 
						{
							System.out.println("PRI> FALSE");
						}
					}
					break;
				}
				default:
				{
					System.out.println("Saindo da Calculadora...");
					exitProgram = true;
				}
					
			}
			
		}
		scanner.close();
	}
	

}
