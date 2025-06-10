Visão Geral
O PedidoFácil é um aplicativo mobile para tablets Android voltado a funcionários de lojas de roupas. Ele substitui o uso de papel no registro de pedidos e permite visualizar históricos e separar peças, tornando o processo mais eficiente e moderno.

Tecnologias Utilizadas
- Linguagem: Kotlin
- IDE: Android Studio
- Arquitetura: MVC
- Comunicação com API: Retrofit (GET, POST, PUT, DELETE)
- Persistência: Suporte a operação offline (com SQLite ou Room, se aplicável)

Estrutura de Pastas (padrão sugerido)
app/
├── java/
│   └── com.example.pedidofacil/
│       ├── model/
│       ├── view/
│       ├── controller/
│       ├── adapter/
│       ├── api/
│       └── utils/
├── res/
│   ├── layout/
│   ├── drawable/
│   ├── values/

Instalação e Execução
1. Abrir o projeto no Android Studio
2. Sincronizar o Gradle
3. Conectar o tablet ou usar um emulador
4. Executar clicando em Run > Run 'app'

 Gerenciamento de Dependências
Exemplo de dependências no build.gradle:
- implementation 'com.squareup.retrofit2:retrofit:2.9.0'
- implementation 'androidx.recyclerview:recyclerview:1.3.0'

 Estilo de Código
- Seguir a convenção oficial do Kotlin Android
- camelCase para variáveis, PascalCase para classes
- Separar responsabilidades por camada (MVC)
- Comentários para métodos e lógicas complexas

Telas e Componentes
| Tela                | Função                                        |
|---------------------|-----------------------------------------------|
| Tela de Registro    | Registrar novo pedido do cliente              |
| Tela de Histórico   | Listar pedidos anteriores                     |
| Tela de Separação   | Exibir itens do pedido e marcar como separados|
| Tela Inicial/Splash | Carregamento e redirecionamento               |

Navegação
A navegação é feita entre Activities com Intent.
Exemplo:
val intent = Intent(this, HistoricoActivity::class.java)
startActivity(intent)

 Integração com API
Utiliza Retrofit com métodos:
- GET /pedidos – Buscar histórico
- POST /pedidos – Criar novo pedido
- PUT /pedidos/{id} – Atualizar pedido
- DELETE /pedidos/{id} – Excluir pedido

Exemplo Retrofit:
@POST("pedidos")
fun criarPedido(@Body pedido: Pedido): Call<Pedido>

Boas Práticas
- Separar lógica de rede da UI
- Usar ViewModel ou Controller para regras de negócio
- Tratar falhas de rede com mensagens ao usuário
- Garantir usabilidade offline

Considerações Finais
O PedidoFácil moderniza o processo interno das lojas, tornando o registro de pedidos mais prático. Futuras melhorias podem incluir autenticação de funcionários e relatórios gerenciais.
