import java.util.Scanner; // Import the Scanner class
import java.io.File; // Import the File class
import java.io.IOException; // Import the IOException class to handle errors
import java.io.RandomAccessFile;
import java.io.FileNotFoundException; // Import this class to handle errors

public class SistemaCadastro {

    // decalaração das variaveis
    static String nome;
    static String cpf;
    static String dataNascimento;
    static String sexo;
    static String anotacoesMedicas;

    // iniciando o scanner
    static Scanner myObj = new Scanner(System.in); // Criando Scanner object

    // Profundadade dos arquivos
    static int p = 0;

    public static void criarArquivo() {
        long start = System.currentTimeMillis();
        System.out.println(" ");

        // Criar diretorio
        try {
            RandomAccessFile diretorioFile = new RandomAccessFile("diretorio.txt", "rw");

            if (diretorioFile.length() == 0) {

                diretorioFile.seek(0);
                diretorioFile.writeBytes(Integer.toString(p)); // primeira linha do arquivo recebe p = 0 (profundidade)
                diretorioFile.close();
                System.out.println("Diretório criado com sucesso");
            } else {
                System.out.println("Diretorio já existe");
            }
        } catch (IOException e) {
            System.out.println("Ocorreu um erro ao criar o Diretório");
        }

        System.out.println(" ");

        // Criar índice
        try {
            RandomAccessFile indiceFile = new RandomAccessFile("indice.txt", "rw");

            if (indiceFile.length() == 0) {

                indiceFile.seek(0);
                indiceFile.writeBytes(Integer.toString(p)); // primeira linha do arquivo recebe p = 0 (profundidade)
                String cabecalho = "Indice  CPF";
                indiceFile.writeBytes("\n" + cabecalho); // segunda linha recebe o cabecalho
                indiceFile.close();
                System.out.println("Índice criado com sucesso");

            } else {
                System.out.println("Índice já existe");
            }
        } catch (IOException e) {
            System.out.println("Ocorreu um erro ao criar o Índice");
        }

        System.out.println(" ");

        // Criar arquivo-mestre
        try {
            RandomAccessFile arquivoMestre = new RandomAccessFile("arquivoMestre.txt", "rw");

            if (arquivoMestre.length() == 0) {

                arquivoMestre.seek(0);
                arquivoMestre.writeBytes(Integer.toString(p)); // primeira linha do arquivo recebe p = 0 (profundidade)
                String cabecalho = "Indice Nome    CPF      Data de Nascimento  Sexo  Anotacoes Medicas";
                arquivoMestre.writeBytes("\n" + cabecalho); // segunda linha recebe o cabecalho
                arquivoMestre.close();
                System.out.println("Arquivo Mestre criado com sucesso");

            } else {
                System.out.println("Arquivo Mestre já existe");
            }
        } catch (IOException e) {
            System.out.println("Ocorreu um erro ao criar o Arquivo Mestre");
        }

        System.out.println(" ");

        long delay = System.currentTimeMillis() - start;
        System.out.println("Demorou " + delay + " milissegundos para criar os arquivos"); // medir o tempo do programa
    }

    public static void inserirRegistro() throws IOException {
        long start = System.currentTimeMillis();

        int contador1 = 0;
        int contadorIndice = 0;
        int j = 0;
        boolean achouCPF = false;

        // guardando as informações do usuário
        System.out.println("Insira as informações do paciente: ");

        do {
            System.out.print("Nome (até 30 caracteres): ");
            nome = myObj.nextLine(); // Ler valor do teclado
            if (nome.length() > 30) {
                System.out.println("ERRO - Nome possui mais de 30 caracteres.");
            } else {
                System.out.println(" ");
            }
        } while (nome.length() > 30); // nome tem que ter até 20 caracteres

        do {
            System.out.print("CPF (xxxxxxxxxxx): ");
            cpf = myObj.nextLine(); // Ler valor do teclado
            if (cpf.length() != 11) {
                System.out.println("ERRO - CPF INVÁLIDO!");
            } else {
                try {
                    RandomAccessFile indiceFile = new RandomAccessFile("indice.txt", "rw");

                    while (indiceFile.getFilePointer() < indiceFile.length()) { // Enquanto não acabar o arquivo
                        j = 0;
                        String line = indiceFile.readLine(); // Le linha

                        if (contadorIndice > 1) {

                            line = line.replace(" ", ""); // Tira os espaços da linha

                            for (int i = 1; i < line.length(); i++) { // for para comparar os CPFs

                                if (line.charAt(i) == cpf.charAt(j)) {
                                    contador1++; // Se posição i dos CPFs forem iguais, contador++
                                }
                                j++;
                            }
                        }
                        contadorIndice++;
                    }
                    if (contador1 == 11) { // se cont for 11, achou o CPF
                        achouCPF = true;
                    } else {
                        achouCPF = false;
                    }
                    indiceFile.close();
                } catch (IOException e) {
                }

                if (achouCPF == true) {
                    System.out.println("CPF já foi registrado");
                } else {
                    System.out.println(" ");
                }
            }
        } while (cpf.length() != 11 || achouCPF == true);

        do {
            System.out.print("Data de Nascimento (dd/mm/yyyy): ");
            dataNascimento = myObj.nextLine(); // Ler valor do teclado
            if (dataNascimento.charAt(2) != '/' && dataNascimento.charAt(5) != '/' && dataNascimento.length() != 9) {
                System.out.println("ERRO - DATA DE NASCIMENTO INVÁLIDA!");
            } else {
                System.out.println(" ");
            }
        } while (dataNascimento.charAt(2) != '/' && dataNascimento.charAt(5) != '/' && dataNascimento.length() != 9); // validar
                                                                                                                      // a
                                                                                                                      // data
                                                                                                                      // de
                                                                                                                      // nascimento

        System.out.print("Sexo: ");
        sexo = myObj.nextLine(); // Ler valor do teclado
        System.out.println(" ");

        do {
            System.out.print("Anotações Médicas (até 40 caracteres): ");
            anotacoesMedicas = myObj.nextLine(); // Ler valor do teclado
            if (anotacoesMedicas.length() > 40) {
                System.out.print("ERRO - Anotações Médicas possuem mais de 40 caracteres. ");
            } else {
                System.out.print(" ");
            }
        } while (anotacoesMedicas.length() > 40); // anotações podem ter até 40 caracteres

        System.out.println(" ");

        // Imprimindo as informações salvas
        System.out.println("Informações salvas: ");
        System.out.println("Nome do paciente: " + nome);
        System.out.println("CPF do paciente: " + cpf);
        System.out.println("Data de nascimento do paciente: " + dataNascimento);
        System.out.println("Sexo do paciente: " + sexo);
        System.out.println("Anotações medicas: " + anotacoesMedicas);

        System.out.println(" ");

        // Atualizando o diretorio
        try {
            RandomAccessFile diretorioFile = new RandomAccessFile("diretorio.txt", "rw");

            if (diretorioFile.length() <= 0) { // se os arquivos ainda não foram criados, chame o metodo Criar Arquivo
                System.out.println("Criando arquivos...");
                criarArquivo();
            }

            if (diretorioFile.length() > 0) { // se o arquivo existir

                String profundidade = diretorioFile.readLine();

                p = Integer.parseInt(profundidade.toString());
                p++; // atualizar a profundidade
            }
            diretorioFile.seek(0);
            diretorioFile.writeBytes(p + ""); // salvar informações no arquivo

            diretorioFile.seek(diretorioFile.length());
            diretorioFile.writeBytes("\n" + p); // salvar informações no arquivo
            diretorioFile.close();
        } catch (IOException e) {
        }

        // Atualizando o indice
        try {
            RandomAccessFile indiceFile = new RandomAccessFile("indice.txt", "rw");

            if (indiceFile.length() > 1) { // se o arquivo existir
                indiceFile.seek(0);
                String profundidade = indiceFile.readLine();
                p = Integer.parseInt(profundidade.toString());
                p++; // atualizar a profundidade
            }
            indiceFile.seek(0);
            indiceFile.writeBytes(p + "");

            indiceFile.seek(indiceFile.length());
            indiceFile.writeBytes("\n" + p + " " + cpf); // salvar informações no arquivo
            indiceFile.close();
        } catch (IOException e) {
        }

        // Atualizando o arquivo mestre
        try {
            RandomAccessFile arquivoMestre = new RandomAccessFile("arquivoMestre.txt", "rw");

            if (arquivoMestre.length() > 0) { // se o arquivo existir

                arquivoMestre.seek(0);
                String profundidade = arquivoMestre.readLine();
                p = Integer.parseInt(profundidade.toString());
                p++; // atualizar a profundidade
            }

            arquivoMestre.seek(0);
            arquivoMestre.writeBytes(p + ""); // salvar informações no arquivo

            arquivoMestre.seek(arquivoMestre.length());
            arquivoMestre.writeBytes( // salvar informações no arquivo
                    "\n" + p + " " + nome + " " + cpf + " " + dataNascimento + " " + sexo + " " + anotacoesMedicas
                            + "                                                                                                           ");
            arquivoMestre.close();
        } catch (IOException e) {
        }

        long delay = System.currentTimeMillis() - start;
        System.out.println("Demorou " + delay + " milissegundos para inserir os registros"); // Calcular tempo de
                                                                                             // programa
    }

    public static void editarRegistro() {
        long start = System.currentTimeMillis();
        String cpfEditar;

        do {
            System.out.print("Informe o CPF do registro que deseja editar: "); // armazena o cpf que a pessoa deseja
                                                                               // editar
            cpfEditar = myObj.nextLine();
            if (cpfEditar.length() != 11) {
                System.out.println("CPF Inválido!");
            }
        } while (cpfEditar.length() != 11);

        System.out.println("CPF que será editado: " + cpfEditar);

        int contadorIndice = 0;
        int contadorMestre = 0;
        int contador1 = 0;
        int j = 0;
        char id = 0;
        boolean achouIndice = false;
        boolean achouIndiceMestre = false;
        String[] tamanhoLinha;

        // Procurando o CPF no arquivo indice

        try {
            RandomAccessFile indiceFile = new RandomAccessFile("indice.txt", "rw");

            if (indiceFile.getFilePointer() < indiceFile.length()) { // Conferir se o arquivo possui registros

                // Guardando as novas informações
                System.out.println("Atualizando os dados: ");

                System.out.print("Nome: ");
                nome = myObj.nextLine(); // Ler valor do teclado

                System.out.print("CPF: ");
                cpf = myObj.nextLine(); // Ler valor do teclado

                System.out.print("Data de Nascimento: ");
                dataNascimento = myObj.nextLine(); // Ler valor do teclado

                System.out.print("Sexo: ");
                sexo = myObj.nextLine(); // Ler valor do teclado

                System.out.print("Anotações Médicas: ");
                anotacoesMedicas = myObj.nextLine(); // Ler valor do teclado

                while (achouIndice == false) { // enquanto não achar indice do CPF
                    j = 0;
                    String line = indiceFile.readLine(); // Ler linha do arquivo

                    if (contadorIndice > 1) { // Começar a procurar o CPF quando chegar na linha com os registros
                        line = line.replace(" ", ""); // Tirando os espaços das linhas

                        for (int i = 1; i < line.length(); i++) { // for para comparar o CPF da linha atual e do CPF
                                                                  // fornecido

                            if (line.charAt(i) == cpfEditar.charAt(j)) { // se a posicao i dos CPFs forem iguais,
                                                                         // contador++
                                contador1++;
                            }
                            j++;
                        }

                        if (contador1 == 11) { // Se contador == 11, CPF encontrado
                            id = line.charAt(0); // Indice da linha onde o CPF foi encontrado
                            achouIndice = true;

                            System.out.print("CPF do paciente: " + cpfEditar
                                    + "\n \nDeseja editar esse registro?: \n1 - SIM 2 - NÃO \n-->"); // confirmar a
                                                                                                     // edição

                            String confirmaRemocao = myObj.nextLine();

                            if (confirmaRemocao.equals("1")) {
                                id = line.charAt(0); // Pega o indice da linha onde o CPF foi encontrado

                                int ponteiro = (int) indiceFile.getFilePointer() - line.length() - 2;
                                indiceFile.seek(ponteiro);
                                indiceFile.writeBytes(id + " " + cpf + "\n"); // Apagar resgitro do arquivo

                                try {
                                    RandomAccessFile arquivoMestre = new RandomAccessFile("arquivoMestre.txt", "rw");

                                    if (arquivoMestre.getFilePointer() < arquivoMestre.length()) {
                                        while (achouIndiceMestre == false) {
                                            String lineMestre = arquivoMestre.readLine(); // Le linha do arquivo

                                            if (contadorMestre > 1) { // Começa quando chegar os resgitros

                                                tamanhoLinha = new String[lineMestre.length()];

                                                int tamanhoArray = tamanhoLinha.length;

                                                lineMestre = lineMestre.replace(" ", ""); // Tira os espaços da linha
                                                System.out.println(" ");

                                                int novoTamanho = nome.length() + cpf.length() + dataNascimento.length()
                                                        + sexo.length() + anotacoesMedicas.length();

                                                int quantidadeEspaco = tamanhoArray - lineMestre.length();
                                                int espacoCompletar = quantidadeEspaco - novoTamanho;

                                                if (id == lineMestre.charAt(0)) { // procura indice no arquivo mestre

                                                    for (int i = 0; i < espacoCompletar; i++) { // completar o espaço
                                                                                                // que tem a mais na
                                                                                                // linha
                                                        tamanhoLinha[i] = " ";
                                                    }

                                                    tamanhoLinha[tamanhoArray - 1] = "\n"; // pular de linha quando
                                                                                           // acabar de inserir

                                                    int ponteiroMestre = (int) arquivoMestre.getFilePointer()
                                                            - tamanhoArray;// calculando onde está o ponteiro da linha
                                                                           // do CPF

                                                    arquivoMestre.seek(ponteiroMestre);
                                                    arquivoMestre.writeBytes(" " + nome + " " + cpf + " "
                                                            + dataNascimento + " " + sexo + " " + anotacoesMedicas); // salvar
                                                                                                                     // novos
                                                                                                                     // dados

                                                    for (int t = 0; t < espacoCompletar; t++) {
                                                        arquivoMestre.writeBytes(tamanhoLinha[t]); // escrever no
                                                                                                   // arquivo
                                                    }
                                                    System.out.println("Dados salvos com sucesso!");
                                                    achouIndiceMestre = true;
                                                }
                                            }
                                            contadorMestre++;
                                        }
                                    }
                                    arquivoMestre.close();
                                } catch (IOException e) {
                                }
                            } else {
                                System.out.println("Registro não foi alterado.");
                            }
                        } else {
                            System.out.println(" ");
                        }
                    }
                    System.out.println(" ");
                    contadorIndice++;
                }
                indiceFile.close();
            } else {
                System.out.println("ERRO: Arquivo não possui registros.");
            }
        } catch (IOException e) {
        }

        System.out.println(" ");

        long delay = System.currentTimeMillis() - start;
        System.out.println("Demorou " + delay + " milissegundos para editar os registros"); // calcular tempo de
                                                                                            // programa
    }

    public static void removerRegistro() {
        long start = System.currentTimeMillis();
        String cpfRemover;
        int contadorIndice = 0;
        int contadorMestre = 0;
        int contador1 = 0;
        int j = 0;
        char id = 0;
        boolean achouIndice = false;
        boolean achouIndiceMestre = false;
        String[] tamanhoLinha;

        do {
            System.out.print("Informe o CPF do registro que deseja excluir: "); // armazenar cpf que será excluido
            cpfRemover = myObj.nextLine(); // Ler valor do teclado
            if (cpfRemover.length() != 11) {
                System.out.println("CPF Inválido!");
            }
        } while (cpfRemover.length() != 11); // validar CPF

        System.out.println("CPF que será excluido: " + cpfRemover);

        // Procurando o CPF no arquivo indice

        try {
            RandomAccessFile indiceFile = new RandomAccessFile("indice.txt", "rw");
            RandomAccessFile arquivoMestre = new RandomAccessFile("arquivoMestre.txt", "rw");

            if (indiceFile.getFilePointer() < indiceFile.length()) { // Conferir se o arquivo possui registros

                while (achouIndice == false) { // Enquanto não achar o indice do CPF le o arquivo
                    j = 0;
                    String line = indiceFile.readLine(); // Le linha

                    if (contadorIndice > 1) { // Começa quando chegar nas linhas com os resgitros
                        line = line.replace(" ", ""); // Tira os espaços da linha

                        for (int i = 1; i < line.length(); i++) { // for para comparar o CPF da linha e o CPF fornecido
                                                                  // pelo
                                                                  // usuário

                            if (line.charAt(i) == cpfRemover.charAt(j)) {
                                contador1++; // Se posição i dos CPFs forem iguais, contador++
                            }
                            j++;
                        }

                        if (contador1 == 11) {
                            achouIndice = true;

                            System.out.print("\nDeseja remover o registro do cpf: " + cpfRemover
                                    + " ?:\n\n1 - SIM 2 - NÃO \nInsira a opção --> "); // confirmar a remoção

                            String confirmaRemocao = myObj.nextLine();

                            if (confirmaRemocao.equals("1")) {
                                id = line.charAt(0); // Pega o indice da linha onde o CPF foi encontrado

                                int ponteiro = (int) indiceFile.getFilePointer() - line.length() - 2;

                                indiceFile.seek(ponteiro);
                                indiceFile.writeBytes(id + "            \n"); // Apagar resgitro do arquivo

                                if (arquivoMestre.getFilePointer() < arquivoMestre.length()) {
                                    while (achouIndiceMestre == false) {
                                        String lineMestre = arquivoMestre.readLine(); // Le linha do arquivo

                                        if (contadorMestre > 1) { // Começa quando chegar os resgitros

                                            tamanhoLinha = new String[lineMestre.length()];

                                            int tamanhoArray = tamanhoLinha.length;

                                            lineMestre = lineMestre.replace(" ", ""); // Tira os espaços da linha
                                            System.out.println(" ");

                                            if (id == lineMestre.charAt(0)) { // procura indice no arquivo mestre

                                                for (int i = 0; i < tamanhoArray; i++) { // preencher a linha que terá
                                                                                         // os registros excluidos com '
                                                                                         // '
                                                    tamanhoLinha[i] = " ";
                                                }

                                                tamanhoLinha[tamanhoArray - 1] = "\n";

                                                int ponteiroMestre = (int) arquivoMestre.getFilePointer()
                                                        - tamanhoArray; // calculando onde está o ponteiro da linha do
                                                                        // CPF

                                                arquivoMestre.seek(ponteiroMestre);

                                                for (int t = 0; t < tamanhoArray - 1; t++) {

                                                    arquivoMestre.writeBytes(tamanhoLinha[t]); // apagando registros
                                                }

                                                System.out.println("Dados removidos com sucesso!");
                                                achouIndiceMestre = true;
                                            }
                                        }
                                        contadorMestre++;
                                    }
                                }
                                arquivoMestre.close();

                            } else {
                                System.out.println("Dados não foram removidos do sistema.");
                            }
                        } else {
                            System.out.println(" ");

                        }
                    }
                    System.out.println(" ");
                    contadorIndice++;
                }
                indiceFile.close();
            } else {
                System.out.println("Erro: Arquivo não possui registros.");
            }
        } catch (IOException e) {
        }
        System.out.println(" ");

        long delay = System.currentTimeMillis() - start;
        System.out.println("Demorou " + delay + " milissegundos para remover os registros"); // calcular tempo de
                                                                                             // programa
    }

    public static void imprimirIrquivos() {
        long start = System.currentTimeMillis();

        System.out.println(" ");

        System.out.println("DIRETORIO: ");
        // Ler o diretorio
        try {
            File myObj = new File("diretorio.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) { // Enquanto tiver linha, imprime
                String data = myReader.nextLine(); // leia proxima linha
                System.out.println(data); // imprime linha
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Ocorreu um erro.");
            e.printStackTrace();
        }

        System.out.println(" ");

        System.out.println("INDICE: ");

        // Ler o indice
        try {
            File myObj = new File("indice.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) { // Enquanto tiver linha, imprime
                String data = myReader.nextLine();// leia proxima linha
                System.out.println(data);// imprime linha
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Ocorreu um erro.");
            e.printStackTrace();
        }

        System.out.println(" ");

        System.out.println("ARQUIVO MESTRE: ");

        // Ler o arquivo mestre
        try {
            File myObj = new File("arquivoMestre.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) { // Enquanto tiver linha, imprime
                String data = myReader.nextLine();// leia proxima linha
                System.out.println(data);// imprime linha
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Ocorreu um erro.");
            e.printStackTrace();
        }

        System.out.println(" ");

        long delay = System.currentTimeMillis() - start;
        System.out.println("Demorou " + delay + " milissegundos para imprimir os arquivos"); // Calcular tempo de
                                                                                             // programa
    }

    public static void simulação() {
        long start = System.currentTimeMillis();

        int[] valores;
        int valor;
        int tamanho;
        String tamanhoString;
        String valorString;
        int contadorMestre = 0;
        int numeroSimulacoes = 0;

        // Procurando o indice no arquivo mestre

        try {
            RandomAccessFile arquivoMestreFile = new RandomAccessFile("arquivoMestre.txt", "rw");

            String linha1 = arquivoMestreFile.readLine(); // mostrar a quantidade de registros que o sistema possui

            System.out.println("O sistema possui " + linha1.charAt(0) + " registros \n");

            do {
                System.out.print("Digite quantos indices deseja buscar: ");
                tamanhoString = myObj.nextLine();
                tamanho = Integer.parseInt(tamanhoString.toString()); // armazenar a quantidade de simulações que o
                                                                      // usuario deseja fazer

                int profundidade = Integer.parseInt(String.valueOf(linha1.charAt(0)));

                if (tamanho > profundidade) {
                    System.out.print("Valor inválido!");
                }
                valores = new int[tamanho];
            } while (tamanho >= linha1.charAt(0)); // se a quantidade armazenada for maior que a quantidade de
                                                   // registros, digite outra valida

            System.out.println(" ");

            for (int i = 0; i < tamanho; i++) { // for para preencher a array com quais indices serão mostrados
                System.out.print("Digite o indice que deseja ver do arquivo: ");
                valorString = myObj.nextLine();
                valor = Integer.parseInt(valorString.toString());
                valores[i] = valor;
                System.out.println(" ");
            }

            while (arquivoMestreFile.getFilePointer() < arquivoMestreFile.length()) { // percorre o arquivo enquanto não
                                                                                      // acabar

                if (numeroSimulacoes <= tamanho) {

                    String line = arquivoMestreFile.readLine(); // ler linha

                    if (contadorMestre >= 1) { // enquanto não chegar na linha com registros

                        for (int j = 0; j < tamanho; j++) {

                            int arranjo = valores[j];

                            int linha = Integer.parseInt(String.valueOf(line.charAt(0)));

                            if (arranjo == linha) { // se o indice do array for igual ao indice da linha
                                System.out.println(j + "  --->   " + line); // imprime registro
                                numeroSimulacoes++;
                            }
                        }
                    }
                    contadorMestre++;
                }
            }
            arquivoMestreFile.close();
        } catch (IOException e) {
        }

        System.out.println(" ");

        long delay = System.currentTimeMillis() - start;
        System.out.println("Demorou " + delay + " milissegundos para fazer a simulação"); // Calcular tempo de programa

    }

    public static void main(String[] args) throws Exception {

        String resp = "";
        String resp2 = "Sim";
        String resp1 = "sim";
        String resp3 = "SIM";
        // Mostrar as opções do menu
        do {
            System.out.println(
                    "Menu: \n 1 - Criar arquivo \n 2 - Inserir registro \n 3 - Editar registro \n 4 - Remover registro \n 5 - Imprimir arquivos \n 6 - Simulação \n ");

            System.out.print("Digite o número com a opção que você deseja: "); // Menu do usuário

            String menu = myObj.nextLine(); // Ler valor do teclado

            // Menu que chama a ação desejada
            switch (menu) {
                case "1":
                    criarArquivo();
                    break;
                case "2":
                    inserirRegistro();
                    break;
                case "3":
                    editarRegistro();
                    break;
                case "4":
                    removerRegistro();
                    break;
                case "5":
                    imprimirIrquivos();
                    break;
                case "6":
                    simulação();
                    break;
                default:
                    System.out.println("Opção invalida!");
                    break;
            }
            System.out.print("Deseja voltar ao menu? Digite 'Sim' para voltar e 'Não' para sair do menu:   ");
            resp = myObj.nextLine();
        } while (resp.equals(resp2) || resp.equals(resp1) || resp.equals(resp3)); // loop do menu
    }
}
