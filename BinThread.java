import java.rmi.RemoteException;
import java.rmi.Naming;
import java.util.Date;

public class BinThread implements Runnable {

	final int IMG_SIZE = 512;
	private byte[][] pic;
	private byte rpic[][] = new byte[IMG_SIZE][IMG_SIZE];
	private int Xdim;
	private int Ydim;
	private int init_x;
	private int init_y;
	private BinServer server;

	public BinThread(String objName, byte[][] pic, int xdim, int ydim, int init_x, int init_y) {
		this.pic = pic;
		this.Xdim = xdim;
		this.Ydim = ydim;
		this.init_x = init_x;
		this.init_y = init_y;

		try {
			this.server = (BinServer) Naming.lookup(objName);
		} catch (Exception e) {
			System.out
					.println(Thread.currentThread().getName() + ": Caught an exception doing name lookup on " + objName
							+ ": " + e);
			System.exit(-1);
		}
	}

	@Override
	public void run() {
		double start = new Date().getTime();
		System.out.println(Thread.currentThread().getName() + " (Start)");
		try {
			rpic = server.bin(pic, rpic, Xdim, Ydim, init_x, init_y);
		} catch (RemoteException e) {
			System.err.println(Thread.currentThread().getName() + " (Error)");
			System.err.println(e);
		}
		System.out.println(
				Thread.currentThread().getName() + ": (End), Demorou " + (new Date().getTime() - start) + " ms");
	}

	public void updateRpic(byte[][] rpic) {
		for (int i = init_y; i < Ydim; i++) {
			for (int j = init_x; j < Xdim; j++) {
				rpic[i][j] = this.rpic[i][j];
			}
		}
	}
}
