import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BinServer extends Remote {
    byte[][] bin(byte[][] pic, byte[][] rpic, int Xdim, int Ydim, int init_x, int init_y) throws RemoteException;
}
