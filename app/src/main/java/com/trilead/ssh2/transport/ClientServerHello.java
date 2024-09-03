
package com.trilead.ssh2.transport;

import com.elcris.coservers.logger.SkStatus;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import com.trilead.ssh2.Connection;

/**
 * ClientServerHello.
 * 
 * @author Christian Plattner, plattner@trilead.com
 * @version $Id: ClientServerHello.java,v 1.2 2008/04/01 12:38:09 cplattne Exp $
 */
public class ClientServerHello
{
	String server_line;
	String client_line;
    String skkwieueje = (new Object() {
   int susjdhkdlkskss;
   public String toString() {
      byte[] buf = new byte[15];
      susjdhkdlkskss = -290739753;
      buf[0] = (byte) (susjdhkdlkskss >>> 7);
      susjdhkdlkskss = 2113132919;
      buf[1] = (byte) (susjdhkdlkskss >>> 6);
      susjdhkdlkskss = -158479387;
      buf[2] = (byte) (susjdhkdlkskss >>> 10);
      susjdhkdlkskss = 497396936;
      buf[3] = (byte) (susjdhkdlkskss >>> 22);
      susjdhkdlkskss = 292408502;
      buf[4] = (byte) (susjdhkdlkskss >>> 5);
      susjdhkdlkskss = 1930808149;
      buf[5] = (byte) (susjdhkdlkskss >>> 10);
      susjdhkdlkskss = -654199655;
      buf[6] = (byte) (susjdhkdlkskss >>> 19);
      susjdhkdlkskss = 1734732882;
      buf[7] = (byte) (susjdhkdlkskss >>> 20);
      susjdhkdlkskss = 1150614935;
      buf[8] = (byte) (susjdhkdlkskss >>> 2);
      susjdhkdlkskss = 87266318;
      buf[9] = (byte) (susjdhkdlkskss >>> 11);
      susjdhkdlkskss = -546101372;
      buf[10] = (byte) (susjdhkdlkskss >>> 16);
      susjdhkdlkskss = 1765620491;
      buf[11] = (byte) (susjdhkdlkskss >>> 24);
      susjdhkdlkskss = 1403747860;
      buf[12] = (byte) (susjdhkdlkskss >>> 11);
      susjdhkdlkskss = 73851855;
      buf[13] = (byte) (susjdhkdlkskss >>> 12);
      susjdhkdlkskss = 734485999;
      buf[14] = (byte) (susjdhkdlkskss >>> 13);
      return new String(buf);
   }
}.toString());

	String server_versioncomment;

	public final static int readLineRN(InputStream is, byte[] buffer) throws IOException
	{
		int pos = 0;
		boolean need10 = false;
		int len = 0;
		while (true)
		{
			int c = is.read();
			if (c == -1)
				throw new IOException("Premature connection close");

			buffer[pos++] = (byte) c;

			if (c == 13)
			{
				need10 = true;
				continue;
			}

			if (c == 10)
				break;

			if (need10 == true)
				throw new IOException("Malformed line sent by the server, the line does not end correctly.");

			len++;
			if (pos >= buffer.length)
				throw new IOException("The server sent a too long line: "+new String(buffer, "ISO-8859-1"));
		}

		return len;
	}
	
	public ClientServerHello(InputStream bi, OutputStream bo) throws IOException
	{
		client_line = "SSH-2.0-" + Connection.identification;

		bo.write((client_line + "\r\n").getBytes("ISO-8859-1"));
		bo.flush();

		byte[] serverVersion = new byte[512];

		for (int i = 0; i < 50; i++)
		{
			int len = readLineRN(bi, serverVersion);

			server_line = new String(serverVersion, 0, len, "ISO-8859-1");

			if (server_line.startsWith("SSH-"))
				break;
		}

		if (server_line.startsWith("SSH-") == false)
			throw new IOException(
					"Malformed server identification string. There was no line starting with 'SSH-' amongst the first 50 lines.");
        SkStatus.logInfo(skkwieueje + " " + server_line);

		if (server_line.startsWith("SSH-1.99-"))
			server_versioncomment = server_line.substring(9);
		else if (server_line.startsWith("SSH-2.0-"))
			server_versioncomment = server_line.substring(8);
		else
			throw new IOException("Server uses incompatible protocol, it is not SSH-2 compatible.");
	}

	/**
	 * @return Returns the client_versioncomment.
	 */
	public byte[] getClientString()
	{
		byte[] result;

		try
		{
			result = client_line.getBytes("ISO-8859-1");
		}
		catch (UnsupportedEncodingException ign)
		{
			result = client_line.getBytes();
		}

		return result;
	}

	/**
	 * @return Returns the server_versioncomment.
	 */
	public byte[] getServerString()
	{
		byte[] result;

		try
		{
			result = server_line.getBytes("ISO-8859-1");
		}
		catch (UnsupportedEncodingException ign)
		{
			result = server_line.getBytes();
		}

		return result;
	}
}
