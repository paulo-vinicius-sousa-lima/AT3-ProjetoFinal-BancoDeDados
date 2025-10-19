/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ucb.sistema.dao;

import java.sql.Connection;

public interface ConexaoBD {
    Connection obterConexao() throws Exception;
    
    void fecharConexao(Connection conexao);
}

