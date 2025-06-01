# Deploy no Railway - Palpite F1 Backend

## Configuração Atual

### Variáveis de Ambiente Necessárias no Railway

```
DATABASE_URL=mysql://user:password@host:3306/database
DATABASE_USERNAME=user
DATABASE_PASSWORD=password
JWT_SECRET=your-secret-key
```

### Configurações Importantes

1. **Porta**: O Railway fornece automaticamente a variável `PORT`. Nossa aplicação está configurada para usar esta variável.

2. **Health Check**: Configurado em `/api/health` (com context-path)

3. **Perfil Spring**: `prod` (definido no Dockerfile)

## Troubleshooting

### Se o healthcheck falhar:

1. **Verificar logs do Railway** para erros de inicialização
2. **Verificar variáveis de ambiente** - especialmente DATABASE_URL
3. **Verificar se o banco MySQL está acessível**

### Comandos úteis no Railway CLI:

```bash
# Ver logs
railway logs

# Ver variáveis de ambiente
railway variables

# Fazer deploy manual
railway up
```

## Arquivos de Configuração

- `Dockerfile`: Configuração do container
- `docker-entrypoint.sh`: Script de inicialização com logs
- `railway.json`: Configuração específica do Railway
- `application-prod.properties`: Configurações de produção

## Endpoints Importantes

- Health Check: `/api/health`
- Swagger UI: `/api/swagger-ui.html` (se habilitado)