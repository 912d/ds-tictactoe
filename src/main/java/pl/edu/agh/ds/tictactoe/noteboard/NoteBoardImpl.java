package pl.edu.agh.ds.tictactoe.noteboard;

import java.rmi.RemoteException;

public class NoteBoardImpl implements INoteBoard {
	private StringBuffer buf;

	public NoteBoardImpl() {
		buf = new StringBuffer();
	}

	@Override
	public String getText() throws RemoteException {
		return buf.toString();
	}

	@Override
	public void appendText(String newNote) throws RemoteException {
		buf.append("\n").append(newNote);
	}

	@Override
	public void clean() throws RemoteException {
		buf = new StringBuffer();
	}
}
