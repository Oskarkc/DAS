import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class DAS {
    private final int port;
    private final int number;
    List<Integer> cyfry = new ArrayList<>();
    public DAS(int port,int number) {
        this.port = port;
        this.number = number;
    }
    public void run(){
        try(DatagramSocket socket = new DatagramSocket(port)){
            if(number!=0)
                cyfry.add(number);
            masterMode(socket);
        } catch (SocketException e) {
            slaveMode();
        }
    }

    private void masterMode(DatagramSocket socket) {
        while(true){
            try {
                byte[] receiveBuffer = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                socket.receive(receivePacket);
                int temp = Integer.parseInt(new String(receivePacket.getData(), 0, receivePacket.getLength()));
                switch (temp) {
                    case 0: {
                        double avg;
                        avg = cyfry
                                .stream()
                                .filter(x -> x != 0)
                                .mapToInt(Integer::intValue)
                                .average()
                                .orElse(0);
                        DatagramPacket sendpacket = new DatagramPacket(String.valueOf((int) avg).getBytes(), String.valueOf(temp).getBytes().length, InetAddress.getByName("255.255.255.255"), port);
                        socket.send(sendpacket);

                        break;
                    }
                    case -1: {
                        System.out.println(temp);
                        DatagramPacket sendpacket = new DatagramPacket(String.valueOf(temp).getBytes(), String.valueOf(temp).getBytes().length, InetAddress.getByName("255.255.255.255"), port);
                        socket.send(sendpacket);
                        socket.close();
                        return;
                    }
                    default:
                        System.out.println(temp);
                        cyfry.add(temp);

                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }

    }

    private void slaveMode() {
        try(DatagramSocket socket = new DatagramSocket(0)){
            DatagramPacket sendpacket = new DatagramPacket(String.valueOf(number).getBytes(),String.valueOf(number).getBytes().length, InetAddress.getLocalHost(),port);
            socket.send(sendpacket);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java DAS <port> <number>");
        }
        DAS das = new DAS(Integer.parseInt(args[0]),Integer.parseInt(args[1]));
        das.run();
    }

}