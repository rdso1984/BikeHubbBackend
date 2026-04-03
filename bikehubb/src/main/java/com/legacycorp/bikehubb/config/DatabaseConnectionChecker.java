package com.legacycorp.bikehubb.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

@Configuration
public class DatabaseConnectionChecker {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnectionChecker.class);

    @Bean
    public CommandLineRunner checkDatabaseConnection(DataSource dataSource) {
        return args -> {
            logger.info("========================================");
            logger.info("INICIANDO VERIFICAÇÃO DA CONEXÃO COM O BANCO DE DADOS");
            logger.info("========================================");

            try (Connection connection = dataSource.getConnection()) {
                if (connection != null && !connection.isClosed()) {
                    DatabaseMetaData metaData = connection.getMetaData();
                    
                    logger.info("✓ CONEXÃO COM O BANCO DE DADOS ESTABELECIDA COM SUCESSO!");
                    logger.info("  → Database: {}", metaData.getDatabaseProductName());
                    logger.info("  → Versão: {}", metaData.getDatabaseProductVersion());
                    logger.info("  → Driver: {}", metaData.getDriverName());
                    logger.info("  → URL: {}", metaData.getURL());
                    logger.info("  → Usuário: {}", metaData.getUserName());
                    
                    // Teste adicional: executar uma query simples
                    try (var statement = connection.createStatement();
                         var resultSet = statement.executeQuery("SELECT 1")) {
                        if (resultSet.next()) {
                            logger.info("✓ Teste de query executado com sucesso!");
                        }
                    }
                    
                    // Lista as tabelas disponíveis
                    logger.info("  → Listando tabelas no banco de dados:");
                    try (ResultSet tables = metaData.getTables(null, null, "%", new String[]{"TABLE"})) {
                        int tableCount = 0;
                        while (tables.next()) {
                            String tableName = tables.getString("TABLE_NAME");
                            logger.info("     • {}", tableName);
                            tableCount++;
                        }
                        logger.info("  → Total de tabelas encontradas: {}", tableCount);
                    }
                    
                    logger.info("========================================");
                    logger.info("BANCO DE DADOS PRONTO PARA USO!");
                    logger.info("========================================");
                    
                } else {
                    logger.error("✗ FALHA: Conexão com o banco de dados está fechada ou nula!");
                    throw new RuntimeException("Não foi possível estabelecer conexão com o banco de dados");
                }
                
            } catch (Exception e) {
                logger.error("========================================");
                logger.error("✗ ERRO AO CONECTAR COM O BANCO DE DADOS!");
                logger.error("========================================");
                logger.error("Mensagem de erro: {}", e.getMessage());
                logger.error("Tipo de exceção: {}", e.getClass().getName());
                logger.error("Detalhes completos:", e);
                logger.error("========================================");
                logger.error("VERIFIQUE:");
                logger.error("  1. As configurações de conexão no application-dev.properties");
                logger.error("  2. Se o banco de dados está acessível");
                logger.error("  3. Se as credenciais estão corretas");
                logger.error("  4. Se há firewall bloqueando a conexão");
                logger.error("========================================");
                
                // Descomente a linha abaixo se quiser que a aplicação PARE caso não consiga conectar
                // throw new RuntimeException("Falha na conexão com o banco de dados", e);
            }
        };
    }
}
