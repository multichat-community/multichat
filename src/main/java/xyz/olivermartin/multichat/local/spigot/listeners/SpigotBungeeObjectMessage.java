package xyz.olivermartin.multichat.local.spigot.listeners;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import xyz.olivermartin.multichat.local.common.listeners.LocalBungeeObjectMessage;

public class SpigotBungeeObjectMessage implements LocalBungeeObjectMessage {

	ObjectInputStream in;

	public SpigotBungeeObjectMessage(byte[] message) throws IOException {
		ByteArrayInputStream stream = new ByteArrayInputStream(message);
		this.in = new ObjectInputStream(stream);
	}

	@Override
	public String readUTF() throws IOException {
		return in.readUTF();
	}

	@Override
	public Object readObject() throws ClassNotFoundException, IOException {
		return in.readObject();
	}

	@Override
	public boolean readBoolean() throws IOException {
		return in.readBoolean();
	}

}
