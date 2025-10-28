/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

// Define o pacote onde a classe está localizada
package ucb.sistema.view;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
// Importa as classes de modelo (dados) e controle (lógica de negócio)
import ucb.sistema.model.Paciente; 
import ucb.sistema.control.GerenciadorPaciente; 

public class SistemaHospital {

    // Instância do controlador de pacientes, responsável pela lógica de CRUD.
    private static GerenciadorPaciente gerenciador = new GerenciadorPaciente(); 
    
    // Formatador de data padrão para garantir que todas as datas sigam o formato "yyyy-MM-dd".
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    // Scanner global para capturar entrada do usuário.
    private static Scanner sc = new Scanner(System.in); 


    public static void main(String[] args) {
        
        // Configura a saída do console para usar UTF-8, garantindo a exibição correta de caracteres especiais.
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        
        // Chama o menu inicial do sistema.
        menuPrincipal();
        
        // Fecha o scanner ao finalizar a aplicação para liberar recursos.
        sc.close();
    }
    
    public static void menuPrincipal() {
        while(true) { // Loop infinito para manter o menu ativo até que o usuário escolha sair.
            try { 
                // Exibe menu.
                System.out.println("\n=============================");
                System.out.println("=== SISTEMA HOSPITALAR ===");
                System.out.println("=============================");
                System.out.println("1 - Acessar como ADMINISTRADOR");
                System.out.println("2 - Acessar como MÉDICO");
                System.out.println("3 - Sair do sistema");
                System.out.print("Escolha o perfil de acesso: ");
                
                int opcao = sc.nextInt(); // Lê a opção (espera um inteiro).
                sc.nextLine(); // Consome a quebra de linha pendente após nextInt().

                System.out.println();
                
                // Seleciona a opção escolhida.
                switch (opcao) {
                    case 1 -> menuAdministrador(); // Vai para o menu do Administrador.
                    case 2 -> System.out.println("Acesso como MÉDICO selecionado. Funcionalidade ainda não implementada.");
                    case 3 -> { 
                        System.out.println("Finalizando o sistema...");
                        return; 
                    }
                    default -> System.out.println("Opção inválida! Tente novamente.");
                }
                
            // Trata o erro se o usuário digitar algo que não seja um número para a opção.
            } catch (InputMismatchException e) {
                System.out.println("Erro: Entrada inválida. Por favor, digite apenas números para a opção.");
                sc.nextLine(); // Limpa o buffer do scanner para evitar loop infinito de erro.
                
            // Captura qualquer outro erro inesperado.
            } catch (Exception e) {
                System.err.println("ERRO INESPERADO: " + e.getMessage());
            }
        }
    }
    
    public static void menuAdministrador() {
        boolean rodando = true;
        while(rodando) {
            try { 
                // Exibe as opções do menu Administrador (CRUD de Pacientes).
                System.out.println("\n=== PERFIL: ADMINISTRADOR ===");
                System.out.println("1 - Cadastrar paciente");
                System.out.println("2 - Consultar paciente (por CPF)");
                System.out.println("3 - Listar pacientes");
                System.out.println("4 - Atualizar dados de Paciente (por CPF)"); 
                System.out.println("5 - Excluir paciente (por CPF)"); 
                System.out.println("6 - Sair do sistema");
                System.out.print("Escolha a opção: ");
                
                int opcao = sc.nextInt();
                sc.nextLine();

                System.out.println();
                
                switch (opcao) {
                    case 1 -> {
                        System.out.println("--- CADASTRO DE PACIENTE ---");
                        System.out.print("Nome: ");
                        String nome = sc.nextLine();
                        System.out.print("CPF (11 dígitos, apenas números): ");
                        String cpf = sc.nextLine();
                        
                        System.out.print("Data de Nascimento (yyyy-MM-dd): ");
                        String dataNascStr = sc.nextLine();
                        LocalDate dataNascimento;
                        try {
                            // Tenta fazer o parse da string para o objeto LocalDate
                            dataNascimento = LocalDate.parse(dataNascStr, DATE_FORMAT);
                        } catch (DateTimeParseException e) {
                            // Trata o erro se a data não estiver no formato correto.
                            throw new IllegalArgumentException("Formato de data inválido. Use yyyy-MM-dd.");
                        }
                        
                        System.out.print("Telefone: ");
                        String telefone = sc.nextLine();
                        System.out.print("Email: ");
                        String email = sc.nextLine();
                        
                        System.out.print("Cidade: ");
                        String cidade = sc.nextLine();
                        System.out.print("Rua: ");
                        String rua = sc.nextLine();
                        
                        int numero;
                        try {
                            System.out.print("Número da Casa: ");
                            numero = sc.nextInt();
                            sc.nextLine(); 
                        // Trata o erro se o número da casa não for um valor numérico.
                        } catch (InputMismatchException e) {
                            sc.nextLine();
                            throw new IllegalArgumentException("O número da casa deve ser um valor numérico.");
                        }
                        
                        System.out.print("Convênio (Ex: SUS, Particular, Unimed): ");
                        String convenio = sc.nextLine();
                        System.out.print("Prioridade (Ex: Normal, Urgente, Emergência): ");
                        String prioridade = sc.nextLine();
                        
                        // Cria um novo objeto Paciente com os dados coletados.
                        Paciente novoPaciente = new Paciente(
                            nome, cpf, dataNascimento, telefone, email,
                            cidade, numero, rua, convenio, prioridade
                        );
                        
                        // Envia o novo paciente para ser cadastrado pelo Gerenciador.
                        gerenciador.cadastrar(novoPaciente);
                        System.out.println("\nPaciente cadastrado com sucesso! ID Gerado: " + novoPaciente.getIdPaciente());
                    }
                    
                    case 2 -> {
                        System.out.println("--- CONSULTAR PACIENTE ---");
                        System.out.print("Buscar paciente por CPF: ");
                        String cpf = sc.nextLine();
                        
                        // Busca o paciente usando o CPF.
                        Paciente paciente = gerenciador.buscarPorCpf(cpf);
                        
                        if (paciente != null) {
                            // Exibe os detalhes se o paciente for encontrado.
                            System.out.println("--- DETALHES DO PACIENTE ---");
                            System.out.println("ID: " + paciente.getIdPaciente());
                            System.out.println("Nome: " + paciente.getNome());
                            System.out.println("CPF: " + paciente.getCpf());
                            System.out.println("Nascimento: " + paciente.getDataNascimento().format(DATE_FORMAT));
                            System.out.println("Telefone: " + paciente.getTelefone());
                            System.out.println("Email: " + paciente.getEmail());
                            System.out.println("Endereço: " + paciente.getRua() + ", " + paciente.getNumero() + " (" + paciente.getCidade() + ")");
                            System.out.println("Convênio: " + paciente.getConvenio());
                            System.out.println("Prioridade: " + paciente.getPrioridade());
                            System.out.println("----------------------------");
                        } else {
                            // Mensagem de erro se não for encontrado.
                            System.out.println("Paciente com CPF " + cpf + " não encontrado.");
                        }
                    }
                    
                    case 3 -> {
                        System.out.println("--- LISTA DE PACIENTES CADASTRADOS ---");
                        // Obtém a lista completa de pacientes.
                        List<Paciente> pacientes = gerenciador.listarTodos();
                        
                        if (pacientes.isEmpty()) {
                            System.out.println("Nenhum paciente cadastrado no sistema.");
                        } else {
                            // Itera sobre a lista e exibe um resumo de cada paciente.
                            for (Paciente p : pacientes) {
                                System.out.printf("ID: %d | Nome: %s | CPF: %s | Convênio: %s%n", 
                                    p.getIdPaciente(), 
                                    p.getNome(), 
                                    p.getCpf(),
                                    p.getConvenio()
                                );
                            }
                            System.out.println("----------------------------------------------");
                            System.out.println("Total de pacientes: " + pacientes.size());
                        }
                    }
                    
                    case 4 -> {
                        System.out.println("--- ATUALIZAR DADOS DO PACIENTE ---");
                        System.out.print("Digite o CPF do paciente para atualização: ");
                        String cpfBusca = sc.nextLine();
                        
                        // Busca o paciente pelo CPF.
                        Paciente paciente = gerenciador.buscarPorCpf(cpfBusca);

                        if (paciente == null) {
                            System.out.println("Paciente com CPF " + cpfBusca + " não encontrado.");
                            break; // Retorna ao menu do Administrador.
                        }

                        System.out.println("Paciente encontrado. Deixe em branco para manter o valor atual.");
                        
                        // Solicita novos dados, permitindo que campos em branco mantenham o valor existente.
                        
                        System.out.println("Nome atual: " + paciente.getNome());
                        System.out.print("Novo Nome: ");
                        String novoNome = sc.nextLine();
                        if (!novoNome.isEmpty()) {
                            paciente.setNome(novoNome); // Atualiza o nome se o novo valor não for vazio.
                        }

                        System.out.println("Telefone atual: " + paciente.getTelefone());
                        System.out.print("Novo Telefone: ");
                        String novoTelefone = sc.nextLine();
                        if (!novoTelefone.isEmpty()) {
                            paciente.setTelefone(novoTelefone);
                        }
                        
                        System.out.println("Convênio atual: " + paciente.getConvenio());
                        System.out.print("Novo Convênio: ");
                        String novoConvenio = sc.nextLine();
                        if (!novoConvenio.isEmpty()) {
                            paciente.setConvenio(novoConvenio);
                        }

                        System.out.println("Prioridade atual: " + paciente.getPrioridade());
                        System.out.print("Nova Prioridade: ");
                        String novaPrioridade = sc.nextLine();
                        if (!novaPrioridade.isEmpty()) {
                            paciente.setPrioridade(novaPrioridade);
                        }
                        
                        // Chama o método do gerenciador para persistir as alterações.
                        gerenciador.atualizar(paciente);
                        System.out.println("Paciente CPF " + paciente.getCpf() + " (ID: " + paciente.getIdPaciente() + ") atualizado com sucesso!");
                    }
                    
                    case 5 -> {
                        System.out.println("--- EXCLUIR PACIENTE ---");
                        System.out.print("Digite o CPF do paciente para EXCLUSÃO: ");
                        String cpfBusca = sc.nextLine();
                        
                        // Busca o paciente a ser excluído para confirmar a existência.
                        Paciente paciente = gerenciador.buscarPorCpf(cpfBusca);

                        if (paciente == null) {
                            System.out.println("Paciente com CPF " + cpfBusca + " não encontrado.");
                            break; // Retorna ao menu.
                        }
                        
                        // Pede confirmação antes de excluir.
                        System.out.println("\nTem certeza que deseja EXCLUIR o paciente: " + paciente.getNome() + " (CPF: " + paciente.getCpf() + ")? (S/N)");
                        String confirmacao = sc.nextLine().trim().toUpperCase();
                        
                        if (confirmacao.equals("S")) {
                            // Se confirmado, chama o método de exclusão usando o ID.
                            gerenciador.excluir(paciente.getIdPaciente()); 
                            System.out.println("Paciente CPF " + cpfBusca + " (ID: " + paciente.getIdPaciente() + ") excluído com sucesso.");
                        } else {
                            System.out.println("Exclusão cancelada.");
                        }
                    }
                    case 6 -> { // Opção de sair (volta ao menu principal ou encerra se estiver no main).
                        System.out.println("Finalizando o sistema...");
                        return; // Sai do menuAdministrador, voltando ao menuPrincipal.
                    }

                    default -> System.out.println("Opção inválida! Tente novamente.");
                }
            // Trata o erro se o usuário digitar algo que não seja um número para a opção.
            } catch (InputMismatchException e) {
                System.out.println("Erro: Entrada inválida. Por favor, digite apenas números para a opção.");
                sc.nextLine(); // Limpa o buffer.
            } catch (Exception e) {
                System.err.println("ERRO NA OPERAÇÃO: " + e.getMessage());
            }
        }
    }
}
