package org.adamtomaja.jmcrcon;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * Reprezentuje serwer. Służy do łączenia, wykonywania komend i odbierania wyników.
 * Wykorzystuje protokół RCON
 * @author AdamTomaja
 *
 */
public class Server 
{
	protected Logger log = Logger.getLogger(this.getClass().getName()); 
	
	/**
	 * Nazwa hosta, z którym nastąpi połączenie
	 */
	protected String host;
	/**
	 * Port na którym wystąpi połączenie
	 */
	protected int port;
	/**
	 * Hasło, które zostanie wykorzystane do autentykacji
	 */
	protected String password;
	
	private Socket sock = null;
	/**
	 * Typ ostatnio wysłanego pakietu. Służy do późniejszego sprawdzenia, w odpowiedzi serwera
	 */
	private int lastType = -1;
	protected boolean logged = false;

	/**
	 * Tworzy nowy obiekt serwera
	 * @param host - nazwa hosta, z którym nastąpi połączenie
	 * @param port - port, na którym nastąpi połączenie
	 * @param password - hasło, które zostanie wykorzystane do autentykacji
	 */
	public Server(String host, int port, String password)
	{
		this.host = host;
		this.port = port;
		this.password = password;
	}
	/**
	 * Wysyła pakiet z tekstem do serwera.
	 * @param type - typ pakietu. Jedna ze stałych klasy {@link PacketTypes}
	 * @param content - komenda
	 * @throws IOException
	 */
	protected void sendPacket(int type, String content) throws IOException
	{
		sendPacket(type, content.getBytes(StandardCharsets.US_ASCII));
	}
	/**
	 * Wysyła pakiet z tablicą bajtów
	 * @param type - typ pakietu. Jedna ze stałych klasy {@link PacketTypes}
	 * @param content - treść pakietu, tablica znaków ascii
	 * @throws IOException
	 */
	protected void sendPacket(int type, byte [] content) throws IOException
	{	 
		Packet packet = new Packet(type, type, content);
		OutputStream out = sock.getOutputStream();
		log.info("Sending bytes: " + packet.getPayload());
		out.write(packet.getPayload());
		out.flush();
		lastType = type;
	}
	/**
	 * Odbiera pakiet i zwraca go w zintepretowanej formie klasy {@link Packet}
	 * @return obiekt pakietu
	 * @throws IOException
	 */
	public Packet receivePacket() throws IOException
	{
		byte [] bytes = new byte[2048];
		int bytesRead = sock.getInputStream().read(bytes);
		
		Packet pack = new Packet(bytes, bytesRead);
		log.info("Packet received: " + pack);
		return pack;
	}
	/**
	 * Wykonuje komendę na serwerze
	 * @param command - komenda do wykonania
	 * @return tekst zwrócony przez serwer w wyniku wykonania komendy
	 * @throws IOException
	 * @throws InvalidReqestIdException - w przypadku gdy serwer zwróci inny idenetyfikator wywołania niż typ wcześniej wysłanego pakietu
	 * @throws NotLoggedException - w przypadku braku wcześniejszego zalogowania na serwerze 
	 */
	public String exec(String command) throws IOException, InvalidReqestIdException, NotLoggedException
	{
		if(!logged)
			throw new NotLoggedException("You must login, to use this function");
		sendPacket(PacketTypes.COMMAND, command);
		Packet receivedPacket = receivePacket();
		if(receivedPacket.getRequest() != lastType)
			throw new InvalidReqestIdException();
		return receivedPacket.getContentString();
	}
	/**
	 * Loguje na serwerze.
	 * Musi być pierwszym krokiem wykonanym przed użyciem innych funkcji.
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public void login() throws UnknownHostException, IOException
	{
		sock = new Socket(host, port);
		log.info("Connected with " + this);
		sendPacket(PacketTypes.LOGIN, password );
		Packet receivedPacket = receivePacket();
		logged = true;
	}
	/**
	 * Closes the connection
	 */
	public void close()
	{
		try {
			sock.close();
			log.info("Connection closed");
		} catch (IOException e) {
			log.warning("Socket close error" + e.getMessage());
		}
	}
	
	@Override
	public 
	String toString()
	{
		return "Server[" + host + ":" + port + "]";
	}
	
}
