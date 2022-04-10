import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;

public class Bin1 extends UnicastRemoteObject implements BinServer {

	public Bin1() throws RemoteException {
		// super() is allways called!
	}

	public byte[][] bin(byte[][] pic, byte[][] rpic, int Xdim, int Ydim, int init_x, int init_y)
			throws RemoteException {
		float desvio;
		int tot, media, sum2, dif, pixeis_mask, mask_d;
		int i, j, i1, j1;
		float K = 2.0f;
		int sob = 10;

		mask_d = 2 * sob + 1;
		pixeis_mask = mask_d * mask_d * 2; /* pixeis da mascara do threshold */

		/* calcular o threshold */
		/* Para todos os pontos reais da frame ... */
		for (i = init_y; i < Ydim - sob; i++) {
			// System.out.print("[" + i + "]");
			for (j = init_x; j < Xdim - sob; j++) {

				System.out.print("[" + i + "]" + "[" + j + "] --");
				/* calcular a media */
				tot = 0;
				for (i1 = -sob; i1 < mask_d - sob; i1++)
					for (j1 = -sob; j1 < mask_d - sob; j1++)
						tot += pic[i + i1][j + j1];
				media = tot / pixeis_mask;

				/* calcular o modulo dos desvios */
				sum2 = 0;
				for (i1 = -sob; i1 < mask_d - sob; i1++)
					for (j1 = -sob; j1 < mask_d - sob; j1++) {
						dif = pic[i + i1][j + j1] - media;
						if (dif < 0)
							sum2 += -dif;
						else
							sum2 += +dif;
					}
				desvio = sum2 / pixeis_mask;

				/* poe o pixel a preto ou a branco. */
				/* pode-se mudar o K de acordo com os testes feitos. */
				if (pic[i][j] >= media - K * desvio)
					rpic[i][j] = 0;
				else
					rpic[i][j] = 1;
			}
		}
		return (rpic);
	}
}
