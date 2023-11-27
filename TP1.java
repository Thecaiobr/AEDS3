import java.io.RandomAccessFile;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Scanner;
import java.util.ArrayList;

/*Sumário por linha
Classe Conta: 23
Create: 139
Read: 180
Update: 220
Delete: 296
Transferencia: 348
MAIN: 480
*/


//Começo da parte sobre o Objeto Conta do Banco
class Conta{
    private int idConta;
    private String nome;
    private int cpf;
    private String cidade;
    private int transferenciasRealizadas;
    private float saldoConta;
    private boolean lapide;

    public Conta(int a, String b, int c, String d, int e, float f){
        this.idConta = a;
        this.nome = b;
        this.cpf = c;
        this.cidade = d;
        this.transferenciasRealizadas = e;
        this.saldoConta = f;
        this.lapide = false;
    }

    public Conta(){
        this.idConta = -1;
        this.nome = "";
        this.cpf = -1;
        this.cidade = "";
        this.transferenciasRealizadas = -1;
        this.saldoConta = 0F;
        this.lapide = false;
    }

    
    public String toString(){//transforma tudo em string para a impressão na tela por meio do método read
        DecimalFormat df = new DecimalFormat("#,##0.00");

        return "\nID: " + this.idConta + "\nNome: " + this.nome + "\nCPF: " + this.cpf + "\nCidade: "+ this.cidade +
            "\nNº Transferencias: " + this.transferenciasRealizadas + "\nSaldo: " + df.format(this.saldoConta) + "\nLapide: " + this.lapide;
    } 
    
    public byte[] toByteArray() throws IOException{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(idConta);
        dos.writeUTF(nome);
        dos.writeInt(cpf);
        dos.writeUTF(cidade);
        dos.writeInt(transferenciasRealizadas);
        dos.writeFloat(saldoConta);
        dos.writeBoolean(lapide);
        return baos.toByteArray();   
    }

    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        idConta = dis.readInt();
        nome = dis.readUTF();
        cpf = dis.readInt();
        cidade = dis.readUTF();
        transferenciasRealizadas = dis.readInt();
        saldoConta = dis.readFloat();
        lapide = dis.readBoolean();
    }

    public int getId(){
        return idConta;
    }

    public String getNome(){
        return nome;
    }

    public int getCPF(){
        return cpf;
    }

    public String getCidade(){
        return cidade;
    }

    public Float getSaldo(){
        return saldoConta;
    }

    public boolean getLapide(){
        return lapide;
    }

    public int getTR(){
        return transferenciasRealizadas;
    }

    public void setLapide(boolean x){
        this.lapide = x; 
    }
    
}

//Começo da parte principal do CRUD
class App{
    static int idTotal = CheckTotalID(); //várivel usada globalmente no App para saber o ULTIMO ID da conta no banco.
    static Scanner sc = new Scanner(System.in);
    static ArrayList<Conta> contasTransferencia = new ArrayList<Conta>();

    public static int CheckTotalID(){ // na verdade checa qual o ultimo id no cabeçalho (primeiro byte do arquivo)
        RandomAccessFile arq;
        try{
            arq = new RandomAccessFile("dados/contas.db", "rw");
            int id = arq.readInt();
            System.out.println("(DEBUG) || IdTotal - "+ id);
            arq.close();
            return id;
        }catch(Exception e){
            return -1; // Se não tiver arquivo retorna -1 pra começar no 0.
        }
    }


    public static void Create(){
        ArrayList<Conta> contasWrite = new ArrayList<Conta>();
        RandomAccessFile arq;
        byte[] b;

        // Criação das Váriaveis da nova conta
        int idConta = idTotal + 1, cpf;
        idTotal++; // ja atualizando o IDTotal para escrever no arquivo depois.
        String nome, cidade;
        
    
        System.out.println("Escreva o nome: ");
        nome = sc.nextLine();
        System.out.println("Escreva o CPF: ");
        cpf = Integer.parseInt(sc.nextLine());
        System.out.println("Escreva a cidade: ");
        cidade = sc.nextLine();

        Conta a1 = new Conta(idConta, nome, cpf, cidade, 0, 0F); //Criando o objeto da conta que será criado
        contasWrite.add(a1);

        try{
            arq = new RandomAccessFile("dados/contas.db", "rw");
            arq.seek(0);
            arq.writeInt(idTotal); //Voltando ao final do arquivo para atualizar o IdTotal.

            for(int i = 0; i<contasWrite.size(); i++){
                b = contasWrite.get(i).toByteArray(); //Transformando em bytes para colocar no arquivo
                arq.seek(arq.length()); //Indo para o final do arquivo escrever.
                arq.writeInt(b.length); //Escrevendo o tamanho
                System.out.println("(DEBUG) BLENGHT" + b.length);
                arq.write(b); //E depois a conta em si.
            }

            arq.close();
        } catch(Exception e){
            e.printStackTrace();
        }
        
    }

    public static void Read(){
        ArrayList<Conta> contasRead = new ArrayList<Conta>(); 
        RandomAccessFile arq;
        byte[] b;
        int tam, idRead = -1;
        Boolean check = false; //Check para ver se foi possível encontrar alguma conta com o ID digitado

        System.out.println("Digite o id da Conta que será lida: ");
        idRead = Integer.parseInt(sc.nextLine());

        try{
            arq = new RandomAccessFile("dados/contas.db", "rw");
            arq.seek(0);//Indo ao inicio do arquivo
            System.out.println("(DEBUG) idtotal - " + arq.readInt());//pulando o cabeçalho.
             
            while(arq.getFilePointer() < arq.length()){ // Enquanto o arquivo não acabar
                Conta cRead = new Conta();
                tam = arq.readInt();
                System.out.println(" (DEBUG) TAM INT " + tam);
                b = new byte[tam];
                arq.read(b);    //Pegando o byte e Transformando em objeto
                cRead.fromByteArray(b);
                if(cRead.getId() == idRead && cRead.getLapide() == false){ // Verificando o ID e Lapide do Objeto
                    check = true;
                    contasRead.add(cRead); // Adicionando o objeto para leitura
                }
            }
            arq.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        if(check == false){ //
            System.out.println("Não foi possível encontrar a conta.");
        }else{
            for(int i = 0; i<contasRead.size(); i++){
                System.out.println(contasRead.get(i)); //Printando a conta de fato se foi encontrada
            }
        }
    }

    public static void Update(){
        ArrayList<Conta> contasUpdate = new ArrayList<Conta>();
        RandomAccessFile arq;
        byte[] b1,b2;
        int tam;
        Boolean check = false;//Variavel para checar se foi possível achar o ID.

        System.out.println("Digite o ID da conta a ser atualizada:");
        int idRead = Integer.parseInt(sc.nextLine());

        try{
            arq = new RandomAccessFile("dados/contas.db", "rw");
            arq.seek(0);
            arq.readInt();//pulando o cabeçalho.

            //Percorrendo o Arquivo tentando achar o ID digitado.
            while(arq.getFilePointer() < arq.length()){
                Conta cUpdate = new Conta();
                long contaPonteiro = arq.getFilePointer();
                tam = arq.readInt();
                b1 = new byte[tam];
                arq.read(b1);
                cUpdate.fromByteArray(b1);
                //Checando pra ver se não tem lápide e o mesmo ID pra começar o Update
                if(cUpdate.getId() == idRead && cUpdate.getLapide() == false){
                    check = true;
                    //Criando os dados pra fazer o Update
                    System.out.println("Escreva o novo nome: ");
                    String newNome = sc.nextLine();
                    System.out.println("Escreva o novo CPF: ");
                    int newCPF = Integer.parseInt(sc.nextLine());
                    System.out.println("Escreva a nova cidade: ");
                    String newCidade = sc.nextLine();
                    System.out.println("Escreva o saldo: ");
                    float newSaldo = Float.parseFloat(sc.nextLine());

                    cUpdate.setLapide(true);    //colocando a Lapide do original como true para "deletar"
                    b1 = cUpdate.toByteArray();
                    cUpdate.fromByteArray(b1);
                    System.out.println("(DEBUG)Lapide atualizada? " + cUpdate.getLapide());

                    Conta newConta = new Conta(cUpdate.getId(), newNome, newCPF, newCidade, cUpdate.getTR(), newSaldo);
                    contasUpdate.add(newConta);
                    
                    //Pegando o novo Update criado na lista para colocar no arquivo
                    for(int i = 0; i<contasUpdate.size(); i++){
                        b2 = contasUpdate.get(i).toByteArray();

                        //Caso o tamanho da nova Conta seja menor pode-se atualizar direto
                        if(b2.length < b1.length){
                            arq.seek(contaPonteiro);
                            arq.writeInt(b2.length);
                            arq.write(b2);
                        }else{// Caso contrario escreva a lapida como true e crie nova conta no final do arquivo.
                            
                            arq.seek(contaPonteiro);
                            arq.writeInt(b1.length);
                            arq.write(b1);
                            arq.seek(arq.length());
                            arq.writeInt(b2.length);
                            arq.write(b2);
                            System.out.println(b2);
                        }
                    }
                }
            }
            if(check == false){
                System.out.println("Não foi possível encontrar a conta.");
            }
            arq.close();
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public static void Delete(){
        ArrayList<Conta> contasDelete = new ArrayList<Conta>();
        RandomAccessFile arq;
        byte[] b1;
        int tam;
        Boolean check = false;
        
        System.out.println("Digite o ID da conta a ser deletada:");
        int idRead = Integer.parseInt(sc.nextLine());
  
        try{
            arq = new RandomAccessFile("dados/contas.db", "rw");
            arq.seek(0);
            arq.readInt();//pulando o cabeçalho.
    
            //Percorrendo o Arquivo tentando achar o ID digitado.
            while(arq.getFilePointer() < arq.length()){
                Conta cDelete = new Conta();
                long contaPosition = arq.getFilePointer();// Guardando a posição de onde está para mudar depois.
                tam = arq.readInt();
                b1 = new byte[tam];
                arq.read(b1);
                
                cDelete.fromByteArray(b1);
                contasDelete.add(cDelete);

                //Checando pra ver se é o mesmo ID para ser deletado
                if(cDelete.getId() == idRead && cDelete.getLapide() == false){
                    check = true;
                    for(int i = 0; i<contasDelete.size(); i++){
                        cDelete.setLapide(true);    //colocando a Lapide do original como true para "deletar"
                        b1 = cDelete.toByteArray();
                        cDelete.fromByteArray(b1);
                        arq.seek(contaPosition);
                        arq.writeInt(b1.length);
                        arq.write(b1);//Escrevendo a lápide como true para deletar o arquivo.
                    }
                    
                }

            }
            if(check == false){
                System.out.println("Não foi possível encontrar a conta.");
            }
            arq.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    
    } 
      

    public static void Transferencia(){
        ArrayList<Conta> contasUpdate = new ArrayList<Conta>();
        ArrayList<Conta> contasUpdate2 = new ArrayList<Conta>();
        RandomAccessFile arq;
        byte[] b1,b2;
        int tam;
        Boolean check = false, check2 = false;//Variavel para checar se foi possível achar o ID.

        System.out.println("Digite o ID da conta que fara a transferencia:");
        int idRead1 = Integer.parseInt(sc.nextLine());
        System.out.println("Digite o ID da conta que recebera a transferencia:");
        int idRead2 = Integer.parseInt(sc.nextLine());
        System.out.println("Digite a quantidade da transferencia:");
        float transferencia = Float.parseFloat(sc.nextLine());

        try{
            arq = new RandomAccessFile("dados/contas.db", "rw");
            arq.seek(0);
            arq.readInt();//pulando o cabeçalho.

            //Percorrendo o Arquivo tentando achar o PRIMEIRO ID digitado.
            while(arq.getFilePointer() < arq.length()){
                Conta cUpdate = new Conta();
                long contaPonteiro = arq.getFilePointer();
                tam = arq.readInt();
                b1 = new byte[tam];
                arq.read(b1);
                cUpdate.fromByteArray(b1);
                //Checando pra ver se não tem lápide e o mesmo ID pra fazer a Transferencia
                if(cUpdate.getId() == idRead1 && cUpdate.getLapide() == false){
                    check = true;

                    //Criando os dados pra fazer o Update depois do novo saldo
                    float newSaldo = cUpdate.getSaldo() - transferencia;

                    Conta newConta = new Conta(cUpdate.getId(), cUpdate.getNome(), cUpdate.getCPF(), cUpdate.getCidade(), cUpdate.getTR()+1, newSaldo);

                    b1 = newConta.toByteArray();
                    newConta.fromByteArray(b1);
                    System.out.println("(DEBUG)Saldo atualizado? " + newConta.getSaldo());

                    contasUpdate.add(newConta);
                    
                    //Pegando o novo Update criado na lista para colocar no arquivo
                    for(int i = 0; i<contasUpdate.size(); i++){
                        b2 = contasUpdate.get(i).toByteArray();

                        //Caso o tamanho da nova Conta seja menor pode-se atualizar direto
                        if(b2.length < b1.length){
                            arq.seek(contaPonteiro);
                            arq.writeInt(b2.length);
                            arq.write(b2);
                        }else{// Caso contrario escreva a lapida como true e crie nova conta no final do arquivo.
                            cUpdate.setLapide(true);    //colocando a Lapide do original como true para "deletar" e criar uma nova entrada com o saldo atualizado.
                            b1 = cUpdate.toByteArray();
                            cUpdate.fromByteArray(b1);
                            
                            arq.seek(contaPonteiro);
                            arq.writeInt(b1.length);
                            arq.write(b1);
                            arq.seek(arq.length());
                            arq.writeInt(b2.length);
                            arq.write(b2);
                            System.out.println(b2);
                        }
                    }
                }
            }
            if(check == false){
                System.out.println("Não foi possível encontrar a primeira conta.");
            }
            arq.seek(0);
            arq.readInt();
            //Percorrendo o arquivo para achar o SEGUNDO ID digitado
            while(arq.getFilePointer() < arq.length()){
                Conta cUpdate = new Conta();
                long contaPonteiro = arq.getFilePointer();
                tam = arq.readInt();
                b1 = new byte[tam];
                arq.read(b1);
                cUpdate.fromByteArray(b1);
                //Checando pra ver se não tem lápide e o mesmo ID pra fazer a Transferencia
                if(cUpdate.getId() == idRead2 && cUpdate.getLapide() == false){
                    check2 = true;

                    //Criando os dados pra fazer o Update depois da Transferencia
                    float newSaldo = cUpdate.getSaldo() + transferencia;

                    Conta newConta = new Conta(cUpdate.getId(), cUpdate.getNome(), cUpdate.getCPF(), cUpdate.getCidade(), cUpdate.getTR()+1, newSaldo);

                    b1 = newConta.toByteArray();
                    newConta.fromByteArray(b1);
                    System.out.println("(DEBUG)Saldo atualizado? " + newConta.getSaldo());

                    contasUpdate2.add(newConta);
                    
                    //Pegando o novo Update criado na lista para colocar no arquivo
                    for(int i = 0; i<contasUpdate2.size(); i++){
                        b2 = contasUpdate2.get(i).toByteArray();

                        //Caso o tamanho da nova Conta seja menor pode-se atualizar direto
                        if(b2.length < b1.length){
                            arq.seek(contaPonteiro);
                            arq.writeInt(b2.length);
                            arq.write(b2);
                        }else{// Caso contrario escreva a lapida como true e crie nova conta no final do arquivo.
                            cUpdate.setLapide(true);    //colocando a Lapide do original como true para "deletar" e criar uma nova entrada com o saldo atualizado.
                            b1 = cUpdate.toByteArray();
                            cUpdate.fromByteArray(b1);
                            
                            arq.seek(contaPonteiro);
                            arq.writeInt(b1.length);
                            arq.write(b1);
                            arq.seek(arq.length());
                            arq.writeInt(b2.length);
                            arq.write(b2);
                            System.out.println(b2);
                        }
                    }
                }
            }
            if(check2 == false){
                System.out.println("Não foi possível encontrar a segunda conta.");
            }
            arq.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }

    //Main com um menu simples para utilizar o CRUD criado.
    public static void main (String[] args) throws Exception {
        Boolean active = true;


        while(active == true){
            System.out.print("|\n|\nEscolha uma das opções:\n0 - Sair do Programa\n1 - Criar uma conta\n2 - Ler uma conta\n3 - Atualizar uma conta\n4 - Deletar uma conta\n5 - Realizar Transferencia\n:");
            int choice = Integer.parseInt(sc.nextLine());
    
            if(choice == 0){
                active = false;
            }else if(choice == 1){
                Create();
            }else if(choice == 2){
                Read();
            }else if(choice == 3){
                Update();
            }else if(choice == 4){
                Delete();
            }else if(choice == 5){
                Transferencia();
            }else{
                System.out.println("Digite um número válido como opção.");
            } 
        }
    
        sc.close();
    }

}