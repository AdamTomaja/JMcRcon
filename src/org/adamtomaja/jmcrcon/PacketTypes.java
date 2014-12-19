package org.adamtomaja.jmcrcon;

/**
 * Zawiera stałe reprezentujące typy pakietów
 * @author unborn
 *
 */
public class PacketTypes
{
	/**
	 * Logowanie do konsoli
	 */
	public static final int LOGIN = 3;
	/**
	 * Komenda tekstowa
	 */
	public static final int COMMAND = 2;
	/**
	 * Odpwowiedź serwera na komendę
	 */
	public static final int COMMAND_RESPONSE = 0;
	/**
	 * Tłumaczy wartość stałej typu <b>int</b> na tekst
	 * @param type
	 * @return
	 */
	public static String toString(int type)
	{
		switch(type)
		{
		case LOGIN:
			return "LOGIN";
		case COMMAND:
			return "COMMAND";
		case COMMAND_RESPONSE:
			return "COMMAND_RESPONSE";
		}
		return "Unknown";
	}
}
