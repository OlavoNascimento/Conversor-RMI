import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class ServidorRMI {

    public static void main(String[] args) {
        try {
            // Cria um objeto para se tornar acessível via rede.
            BinServer bin = new Bin1();
            // String objName = "rmi://localhost/Bin";
            String objName = args[0];
            int port = Integer.parseInt(objName.split(":")[2].split("/")[0]);

            System.out.println("Registrando o objeto no RMIRegistry...");
            LocateRegistry.createRegistry(port);
            Naming.rebind(objName, bin);

            System.out.println("Aguardando Clientes!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
