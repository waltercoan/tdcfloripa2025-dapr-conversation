# TDC Florianópolis 2025 - Integrando Microservices a LLM com o Dapr 🎩- Distributed Application Runtime

[![My Skills](https://skillicons.dev/icons?i=azure,aws,java,python,spring,docker,git,linux)](https://skillicons.dev)

## Arquitetura

![Diagrama](./diagramas/diagrama.png)

## Documentação

- [Conversation Overview](https://docs.dapr.io/developing-applications/building-blocks/conversation/conversation-overview/)
- [Conversation Components](https://docs.dapr.io/reference/components-reference/supported-conversation/)
- [Conversation API Ref](https://docs.dapr.io/reference/api/conversation_api/)

## Evento

![Diagrama](./diagramas/trilha-api-e-microservices-integrando-microservices-a-llm-com-o-dapr-distributed-application-runtime-rectangular.png)

[Download Slides](./diagramas/waltercoan-Integrando%20Microservices%20a%20LLM%20com%20o%20Dapr.pdf)

## Ambiente

- Criar um GitHub CodeSpace a partir de um fork da branch main
- criar um arquivo .env na raiz do projeto com as seguintes variáveis

```bash
openapikey=<CHAVE DA OPENAI>
DAPR_CONFIGURATION_STORE=configstore
AWS_ACCESS_KEY_ID=<AWS ACCESS KEY COM PERMISSAO PARA O BEDROCK>
AWS_SECRET_ACCESS_KEY=<AWS SECRET ACCESS KEY COM PERMISSAO PARA O BEDROCK>
AWS_DEFAULT_REGION=sa-east-1
```

- Teclar F5 para executar o arquivo .vscode/launch.json

- Utilizar o [arquivo teste.rest](./teste.rest) para executar as chamadas

## Instalação do Dapr

- No terminal digitar

```bash
dapr init
```
- Compilação dos projetos Java

```bash
mvn package -DskipTests
```

- Criação do Python Enviroment 

```bash
pip install fastapi[standard]
apt-get install python3-venv
python3 -m venv venv
source ./venv/bin/activate
pip install -r requirements.txt -t .
```
- Inclusão da chave-valor de configuração no Redis

```bash
docker exec dapr_redis redis-cli MSET CONVERSATION-NAME "openai-gpt"
```