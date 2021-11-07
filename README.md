# Projeto PA 2021/22 - Época Normal

O enunciado relativamente ao projeto encontra-se no Moodle.

Este template *IntelliJ* deve servir de base de desenvolvimento para este trabalho e o seu versionamento interno dentro de cada grupo feito através do *Git*.

## Estrutura de ficheiros/pastas

- `/dataset` - contém os *datasets* a importar pela aplicação
- `/src` - contém o código-fonte da aplicação
    - `com.brunomnsilva.smartgraph` - *package* que contém a biblioteca [JavaFXSmartGraph](https://github.com/brunomnsilva/JavaFXSmartGraph). Não é esperado que tenha de fazer modificações a estas classes.
    - `pt.pa` - *package* principal da aplicação.
        - `*.graph` - *package* que contém o ADT Graph e uma implementação funcional baseada em "lista de arestas".
        - `*.model` - *package* que contém o modelo de classes da aplicação desenvolvida.
        - `*.view` - *package* que contém as classes gráficas da aplicação desenvolvida.
    - `Main.java` - classe que contém o ponto de entrada para a aplicação.
- `/test` - (a criar) contém os testes unitários da aplicação
- `smartgraph.css` - *stylesheet* utilizado pela biblioteca JavaFXSmartGraph (pode alterar)
- `smartgraph.properties` - *propriedades* utilizadas pela biblioteca JavaFXSmartGraph (não é necessário alterar, já contém definições adequadas)
- `README.md` - este ficheiro. Podem alterar o conteúdo, se entenderem.

## Dados para importação

Os ficheiros estão em formato de texto, emboram possam conter números; poderão existir comentários - essas linhas começam pelo caractere `"#"` e devem ser ignoradas durante a leitura.

Os *datasets* encontram-se na pasta local `dataset`, portanto qualquer ficheiro localizado nessa pasta pode ser aberto com, e.g., `new FileReader("dataset/<folder>/<file>.txt")`, sendo `<file>` o ficheiro respetivo a abrir.

Existem dois *datasets*, nomeadamente `sbg32` e `sbg128`, contendo 32 e 128 hubs respetivamente.

Cada dataset contem os seguintes ficheiros:

- `name.txt` - Nomes das cidades onde estão localizados os *hubs*;
- `weight.txt` - População das cidades onde estão localizados os *hubs*;
- `xy.txt` - Coordenadas (de ecrã) das cidades onde estão localizados os *hubs*;

- `routes_*.txt` - Matriz de distâncias das rotas entre todos os hubs - pode haver diferentes versões (sufixo) deste ficheiro, e.g., `"_1"` e `"_2"`.

### Relação da informação entre ficheiros

Os ficheiros `name.txt`, `weight.txt` e `xy.txt` contêm o **mesmo número N de entradas válidas**; cada linha respetiva contém informação de uma cidade/hub. Ou seja, a informação na linha (válida) L de um ficheiro relaciona-se com a informação da linha L de outro ficheiro.

Os ficheiros `routes_*.txt` contêm uma "matriz de distâncias" entre cidades/hubs, i.e., se houve N cidades, então esta matriz têm a dimensão NxN (e.g., ver [wikipedia](https://pt.wikipedia.org/wiki/Matriz_de_dist%C3%A2ncias)). Uma distância de 0 (zero) significa que não existe rota direta entre duas cidades/hubs.

### Exemplo de modelo importado

A título de exemplo, mostra-se o resultado esperado da importação dos dois datasets, podendo variar o 
ficheiro das rotas. Note que a disposição relativa das cidades (vértices) é um mapeamento geograficamente realista. 

### sgb32

Rotas em `routes_1.txt` (grafo bipartido - 2 componentes):

![](sgb32_1.png)

### sgb128

Rotas em `routes_1.txt` (1 componente):

![](sgb128_1.png)

Rotas em `routes_2.txt` (grafo bipartido - 3 componentes):

![](sgb128_2.png)
