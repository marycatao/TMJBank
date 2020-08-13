package Principal;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import ContasBancarias.Conta;
import ContasBancarias.SeguroDeVida;
import Exceptions.NumeroNegativoException;
import Pessoal.Pessoa;
import relatorios.Relatorios;

public class Menu {

		Scanner input = new Scanner(System.in);
		Relatorios relatorio = new Relatorios();
		SeguroDeVida seguroVida = new SeguroDeVida();
	
	public void displayMain(List<Pessoa> listaP, List<Conta> listaC) throws IOException {
		do {
		imprimeLogo();
		System.out.print("                                CPF: ");
		String cpf = input.next();
		
		String tipo = "";
		boolean cpfCadastrado = false; //faz verifica��o do cpf para testar no final do "for"
		
		for(int i = 0; i<listaP.size(); i++) {
			if(listaP.get(i).getCpf().equals(cpf)) { //Testa o cpf
				cpfCadastrado = true;
				System.out.print("                                PASSWORD:    ");
				int password = input.nextInt();
 				if(listaP.get(i).getSenha() == password) { //Testa a senha
					limpaConsole();
 					tipo = listaP.get(i).getTipo();
					Conta conta = VerificaListas.econtraConta(listaP.get(i).getCpf(), listaC);

					switch(tipo) {
					case("CLIENTE"):
						displayCliente(conta, listaC, listaP.get(i), listaP);
					    limpaConsole();
					break;
					case("GERENTE"):
						displayGerente(conta, listaC, listaP.get(i), listaP);
						limpaConsole();
					break;
					case("DIRETOR"):
						displayDiretor(conta, listaP.get(i), listaC, listaP);
						limpaConsole();
					break;
					case("PRESIDENTE"):
						displayPresidente(listaC, conta, listaP.get(i), listaP);
						limpaConsole();
					}
				} 
				else
					System.out.println("\n                                  Senha Inv�lida!");
			}
		}
		
		if(!cpfCadastrado) // Caso cpf n�o esteja cadastrado
			System.out.println("                                CPF N�O CADASTRADO!");
		}while(true);
	}
	
	public boolean displayCliente(Conta conta, List<Conta> listaConta, Pessoa pessoa, List<Pessoa> listaPessoa) throws IOException {
		if(conta == null) return false;
		
		System.out.println("");
		System.out.println("                                  MENU CLIENTE!");

		int opcao;
		int opcao1;
		int opcao2;
		String resp;

		resp = "S";

		
		while(resp.equalsIgnoreCase("S")){
			limpaConsole();
			imprimeLogo();
			do{
				System.out.println("                  *********************************************");
				System.out.println("                           1 - Movimenta��es da Conta");
				System.out.println("                           2 - Extrato das Opera��es");
				System.out.println("                  *********************************************");
				System.out.print("                                 Escolha uma op��o: ");
				opcao = input.nextInt();
				limpaConsole();
				imprimeLogo();
				switch(opcao) {
					case(1):
						do{
							System.out.println("                  *********************************************");
							System.out.println("                                1 - Saque");
							System.out.println("                                2 - Dep�sito");
							System.out.println("                                3 - Transfer�ncia");
							System.out.println("                                4 - Seguro de Vida");
							System.out.println("                  *********************************************");
							System.out.print("                                Escolha uma op��o: ");
							opcao1 = input.nextInt();
							if(opcao1 == 1){
                                boolean sacou = false;
                                do {
                                    try {
                                        System.out.print("                     Digite o valor desejado para saque: ");
                                        double valor = input.nextDouble();
                                        if(valor <= 0)
                                            throw new NumeroNegativoException(valor);
                                            sacou = conta.Sacar(valor);
                                            if(sacou)
                                                EscritorDeTransacoes.escritorSaque(pessoa, conta, valor);
                                    } catch(NumeroNegativoException e) {
                                        System.err.println("                 Ocorreu um erro! Informe um valor positivo.");
                                        
                                    }
                                }while(!sacou);
                            }
                            else if(opcao1 == 2){
								System.out.print("                        Digite o valor para dep�sito: ");
								double valor = input.nextDouble();
								boolean depositou = conta.Depositar(valor);
								if(depositou)
									EscritorDeTransacoes.escritorDeposito(pessoa, conta, valor);
							} else if(opcao1 == 3){
								System.out.print("                  Digite o valor para transfer�ncia: ");
								double valor = input.nextDouble();
								System.out.print("                  Digite o CPF da conta destino: ");
								String cpf = input.next();
								Conta destino = VerificaListas.econtraConta(cpf, listaConta);
								boolean transferiu = conta.Transferir(destino, valor);
									if(transferiu)
										System.out.println("                            Valor transferido para "+VerificaListas.encontraPessoa(cpf, listaPessoa));
										EscritorDeTransacoes.escritorTransferencia(pessoa, conta, valor, destino, listaPessoa);
							} else if(opcao1 == 4){
								System.out.println("                  *********************************************");
								System.out.println("                          Contrata��o de Seguro de Vida");
								System.out.println("                          Essa opera��o tem incid�ncia ");
								System.out.println("                      de 20% de tributos sobre o valor pago.");
								System.out.print("                          Informe o valor segurado: ");
								Double valor = input.nextDouble();
								boolean contratou = seguroVida.contrata(conta, valor);
								if(contratou)
									EscritorDeTransacoes.seguroDeVida(pessoa, conta, valor);
							} 
							else {
								System.out.println("                  *********************************************");
								System.out.println("                                  Op��o Inv�lida!");
								System.out.println("                             Escolha uma op��o V�lida: ");
							}
						  } while(opcao1 > 4 || opcao1 < 1);
					break;
		
				case(2):
					do{
						System.out.println("                  *********************************************");
						System.out.println("                         1 - Saldo");
						System.out.println("                         2 - Tributa��o da Conta corrente");
						System.out.println("                         3 - Rendimento da Conta Poupan�a");
						System.out.println("                  *********************************************");
						System.out.print("                               Escolha uma op��o: ");
						opcao2 = input.nextInt();
						limpaConsole();
						imprimeLogo();
						if(opcao2 == 1){
							System.out.println("                       Seu saldo atualizado � de R$ "+conta.getSaldo());
							EscritorDeTransacoes.saldo(pessoa, conta);
						} else if(opcao2 == 2){
							double totalGastos = relatorio.tributacao(conta);
							EscritorDeTransacoes.tributacao(pessoa, conta, totalGastos);
						} else if(opcao2 == 3){
							System.out.println("                  *********************************************");
							System.out.println("                      Simula��o de investimento em Poupan�a");
							System.out.println("");
							System.out.print("                    Digite o valor desejado: ");
							double valor = input.nextDouble();
							System.out.print("                    Digite o prazo do investimento em dias: ");
							int prazo = input.nextInt();
							
							relatorio.rendimento(valor, prazo);
							EscritorDeTransacoes.Rendimento(pessoa, conta, valor, prazo);
						}
						else{
						System.out.println("                  *********************************************");
						System.out.println("                                  Op��o Inv�lida!");
						System.out.println("                             Escolha uma op��o V�lida: ");
						}
					}while(opcao2 > 3 || opcao2 < 1);
				break;
				
				default:
				System.out.println("                  *********************************************");	
				System.out.println("                                  Op��o Inv�lida!");
				System.out.println("                             Escolha uma op��o V�lida: ");
				}
			}while(opcao != 1 && opcao != 2);
			System.out.println("                  *********************************************");
			System.out.print("                        Deseja fazer outra opera��o? (S/N) ");
			resp = input.next();
		}
		System.out.println("                  *********************************************");
		System.out.println("                           Foi um prazer atend�-lo(a)!");
		System.out.println("                                 Tenha um bom dia!");
		
		return true;
	}
	public void displayGerente(Conta conta, List<Conta> listaConta, Pessoa pessoa, List<Pessoa> listaPessoa) throws IOException {
		
		int opcao;
		String opcao1 = "";
		do {
		do {
		limpaConsole();
		imprimeLogo();	
		System.out.println("                  *********************************************");
		System.out.println("                          Como deseja acessar o sistema?");
		System.out.println("                                  1 - Cliente");
		System.out.println("                                  2 - Gerente");
		System.out.println("                  *********************************************");
		System.out.print("                                Escolha uma op��o: ");
		opcao = input.nextInt();
		
		if(opcao == 1)
			displayCliente(conta, listaConta, pessoa, listaPessoa);
		else if(opcao == 2) {
			int contaAgencia = relatorio.gerente(listaConta, conta );
			EscritorDeTransacoes.relatorioGerente(pessoa, conta, contaAgencia);
			System.out.println("                  *********************************************");
			System.out.print("                        Deseja fazer outra opera��o? (S/N) ");
			opcao1 = input.next();
		} 
		 else {
			System.out.println("                  *********************************************");
			System.out.println("                                  Op��o Inv�lida");
			System.out.println("                             Escolha uma op��o V�lida: ");
		}	
		}while(opcao > 2 || opcao < 1);
		}while(opcao1.equalsIgnoreCase("S"));
	}
		
	public void displayDiretor(Conta conta, Pessoa pessoa, List<Conta> listaConta, List<Pessoa> listaPessoa) throws IOException {
		int opcao;
		String opcao1 = "";
		do {
		do {
		limpaConsole();
		imprimeLogo();		
		System.out.println("                  *********************************************");
		System.out.println("                          Como deseja acessar o sistema?");
		System.out.println("                                  1 - Cliente");
		System.out.println("                                  2 - Diretor");
		System.out.println("                  *********************************************");
		System.out.print("                                Escolha uma op��o: ");
		opcao = input.nextInt();
		
		if(opcao == 1)
			displayCliente(conta, listaConta, pessoa, listaPessoa);
		else if(opcao == 2) {
			List<String> lista = relatorio.diretor(listaConta, listaPessoa);
			EscritorDeTransacoes.relatorioDiretor(pessoa, conta, lista);
			System.out.println("                  *********************************************");
			System.out.print("                        Deseja fazer outra opera��o? (S/N) ");
			opcao1 = input.next();
		} else {
		System.out.println("                  *********************************************");
		System.out.println("                                  Op��o Inv�lida");
		System.out.println("                             Escolha uma op��o V�lida: ");
		}
		}while(opcao > 2 || opcao < 1);
		}while(opcao1.equalsIgnoreCase("S"));
		
		
	}
	public void displayPresidente(List<Conta> listaConta, Conta conta, Pessoa pessoa, List<Pessoa> listaPessoa) throws IOException {
		int opcao;
		String opcao1 = "";
		do {
		do {
		limpaConsole();
		imprimeLogo();	
		System.out.println("                  *********************************************");
		System.out.println("                          Como deseja acessar o sistema?");
		System.out.println("                                  1 - Cliente");
		System.out.println("                                  2 - Presidente");
		System.out.println("                  *********************************************");
		System.out.print("                                Escolha uma op��o: ");
		opcao = input.nextInt();
		
		if(opcao == 1)
			displayCliente(conta, listaConta, pessoa, listaPessoa);
		else if(opcao == 2) {
			double totalPassivo = relatorio.presidente(listaConta);
			EscritorDeTransacoes.relatorioPresidente(pessoa, conta, totalPassivo);
			System.out.println("                  *********************************************");
			System.out.print("                        Deseja fazer outra opera��o? (S/N) ");
			opcao1 = input.next();
		} else {
		System.out.println("                  *********************************************");
		System.out.println("                                  Op��o Inv�lida");
		System.out.println("                             Escolha uma op��o V�lida: ");
		}
		}while(opcao > 2 || opcao < 1);
	    }while(opcao1.equalsIgnoreCase("S"));
		
	
	}
	public void limpaConsole()
    {
        try
        {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            //Runtime.getRuntime().exec("cmd /c cls");
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
    }
	
	public void imprimeLogo() {
		System.out.println("\n******************************************************************************************");

		System.out.println("    /$$$$$$$$ /$$      /$$    /$$$$$        /$$$$$$$   /$$$$$$  /$$   /$$ /$$   /$$");       
		System.out.println("   |__  $$__/| $$$    /$$$   |__  $$       | $$__  $$ /$$__  $$| $$$ | $$| $$  /$$/");       
		System.out.println("      | $$   | $$$$  /$$$$      | $$       | $$  \\ $$| $$  \\ $$| $$$$| $$| $$ /$$/ ");     
		System.out.println("      | $$   | $$ $$/$$ $$      | $$       | $$$$$$$ | $$$$$$$$| $$ $$ $$| $$$$$/  ");       
		System.out.println("      | $$   | $$  $$$| $$ /$$  | $$       | $$__  $$| $$__  $$| $$  $$$$| $$  $$  ");       
		System.out.println("      | $$   | $$\\  $ | $$| $$  | $$       | $$  \\ $$| $$  | $$| $$\\  $$$| $$\\  $$ ");     
		System.out.println("      | $$   | $$ \\/  | $$|  $$$$$$/       | $$$$$$$/| $$  | $$| $$ \\  $$| $$ \\  $$");      
		System.out.println("      |__/   |__/     |__/ \\______/        |_______/ |__/  |__/|__/  \\__/|__/  \\__/");      
		System.out.println("\n******************************************************************************************");
	}
	}
	
	



