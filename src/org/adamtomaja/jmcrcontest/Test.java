package org.adamtomaja.jmcrcontest;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.logging.Logger;

import org.adamtomaja.jmcrcon.InvalidReqestIdException;
import org.adamtomaja.jmcrcon.NotLoggedException;
import org.adamtomaja.jmcrcon.Server;

/**
 * Test klasy Server
 * @author AdamTomaja
 *
 */
public class Test {
	public static void main(String[] args)  
	{
		//Tworzymy nowy obiekt serwera z jego parametrami
		Server server = new Server("127.0.0.1", 25575, "1234ASD!@#");
		try {
			//Logujemy się na wcześniej podane dane
			server.login();
			try {
				//Wywołujemy jakąś komendę
				server.exec("say HelloWorld!");
			} catch (InvalidReqestIdException e) {
 				System.err.println("Invalid request id returned");
			} catch (NotLoggedException e) {
				System.err.println("Not logged");
			}
			//Dobrym nawykiem jest zamknięcie połączenia
			server.close();
		} catch (UnknownHostException e) {
			System.err.println("Unable to find host");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Input/output error");
			e.printStackTrace();
		}
	}

}
