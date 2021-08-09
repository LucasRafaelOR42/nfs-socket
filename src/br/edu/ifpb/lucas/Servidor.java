package br.edu.ifpb.lucas;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Servidor {

    public static void main(String[] args) throws IOException {
        System.out.println("== Servidor ==");

        ServerSocket serverSocket = new ServerSocket(7001);
        Socket socket = serverSocket.accept();

        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        DataInputStream dis = new DataInputStream(socket.getInputStream());

        while (true) {
            System.out.println("Cliente: " + socket.getInetAddress());

            String mensagem = dis.readUTF();
            System.out.println(mensagem);

            String cmd = mensagem.split(" ")[0];

            String retorno = cmd + "\n";

            switch(cmd)
            {
                case "readdir": {
                    String arg = mensagem.split(" ")[1];
                    File[] arqs = new File(arg).listFiles();

                    if (arqs != null) {
                        for (File file : arqs) {
                            retorno += file.getName() + ", ";
                        }
                    }
                    retorno = retorno.substring(0, retorno.lastIndexOf(","));
                    break;
                }

                case "rename": {
                    String from = mensagem.split(" ")[1];
                    String to = mensagem.split(" ")[2];

                    File fileFrom = new File(from);
                    File fileTo = new File(to);

                    boolean flag = fileFrom.renameTo(fileTo);

                    if (flag)
                        System.out.println("Arquivo renomeado.");
                    break;
                }
                case "create": {
                    String filename = mensagem.split(" ")[1];
                    File arq = new File(filename);
                    if(arq.createNewFile())
                        System.out.println("Arquivo criado.");
                    break;
                }
                case "remove": {
                    String filename = mensagem.split(" ")[1];
                    File arq = new File(filename);
                    if(arq.delete())
                        System.out.println("Arquivo excluido.");
                    break;
                }
            }
            dos.writeUTF(retorno);
        }
    }
}
