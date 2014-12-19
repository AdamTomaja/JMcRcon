package org.adamtomaja.jmcrcon;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
/**
 * Represents packet
 * @author AdamTomaja
 */
public class Packet {
	protected int length;
	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getRequest() {
		return request;
	}

	public void setRequest(int request) {
		this.request = request;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	protected int type;
	protected int request;
	byte [] content;
	
	public Packet(byte [] bytes, int bytesRead)
	{
		ByteBuffer buff = ByteBuffer.wrap(bytes, 0, bytesRead);
		buff.order(ByteOrder.LITTLE_ENDIAN);
		
		length = buff.getInt();
		request = buff.getInt();
		type = buff.getInt();
		
		int contentLen = length - 4 - 4 - 2;
		
		content = new byte[contentLen];
		
		buff.get(content);
	}
	public Packet(int request, int type, byte [] content)
	{
		this.request = request;
		this.type = type;
		this.content = content;
		this.length = 4 + 4 + content.length + 2;
	}
	public String getContentString()
	{
		return new String(getContent(), StandardCharsets.US_ASCII);
	}
	public byte [] getPayload()
	{
 		byte [] payload = new byte[length + 4]; // Dodajemy jeden bajt, potrzebny na wysłanie długości pakietu
		ByteBuffer buff = ByteBuffer.wrap(payload);
		buff.order(ByteOrder.LITTLE_ENDIAN);
		buff.position(0);
		
		buff.putInt(length);
		buff.putInt(request);
		buff.putInt(type);
		buff.put(content);
		buff.put(new byte[]{0x00, 0x00});
		
		return payload;
	}
	@Override
	public String toString()
	{
		return String.format("Packet[len=%d, req=%d(%s), type=%d(%s) , content=%s]", length, request,PacketTypes.toString(request), type, PacketTypes.toString(type), getContentString());
	}
}
