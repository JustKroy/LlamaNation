# GITHUB

## BRANCH
Como se fosse a sua área de trabalho dentro do repositório do **Github**
Cada nova funcionalidade ou correção deve ser feita em uma branch separada.

CADA PARTICIPANTE TRABALHA NA PRÓPRIA BRANCH
DEPOIS ABRE UM PULL REQUEST PARA JUNTAR AO MAIN

### NOMES DE BRANCH
- feature/login
- feature/cadastro-aluno
- feature/relatorio-pdf
- fix/erro-login
- refactor/tela-inicial

**feat:** nova funcionalidade
**fix:** correção de erro
**refactor:** reorganização de código sem mudar funcionalidade
**docs:** documentação
**style:** alteração visual ou formatação
**test:** criação ou ajuste de testes
**chore:** tarefa técnica ou manutenção

### PADRÃO PARA COMMIT
- feat: adiciona tela de login
- feat: cria cadastro de alunos
- fix: corrige validação de senha
- fix: resolve erro ao exportar pdf
- refactor: reorganiza classe usuario
- docs: atualiza readme do projeto
- style: melhora layout da dashboard

#### Práticas RUINS
- arrumei
- mudanças
- teste
- coisas do projeto
- aaa

## PULL REQUEST
Solicitação para juntar o seu branch ao main.
Toda alteração entra na main por revisão do grupo ou pelo menos com conferência de outra pessoa.

### FLUXO
Quem vai desenvolver algo novo:

1. Atualizar a main
2. Criar uma branch para sua tarefa
3. Fazer as alterações no projeto
4. Verificar o que mudou
5. Adicionar os arquivos
6. Criar um commit
7. Enviar a branch
8. Abrir Pull Request no GitHub
9. Revisão
10. Merge na main
11. Atualizar a main local

### WORKFLOW
**Antes de começar, atualizar a main**
git checkout main
git pull origin main

**Criar sua branch**
git checkout -b nome_branch

**Trabalhar normalmente**
git add .
git commit -m "feat: adicionado formulário de login"

**Enviar branch pro GitHub**
git push -u origin nome_branch

**Abrir Pull Request no GitHub**
Aí o grupo revisa e faz o merge

# BOAS PRÁTICAS
1. Não sair programando diretamente no Main
2. Cada pessoa pega uma parte diferente do sistema
3. Avisem quem está mexendo em qual arquivo (Ainda mais quando tratamos de arquivos centrais)
4. Façam commits pequenos e frequentes (Nunca acumulem alterações para fazer uma gigante no fim)
5. Antes de começar uma tarefa nova, sempre atualizar a main
6. Depois de atualizar a main, criar uma branch nova
7. Cada branch deve ter um único objetivo
8. Commits devem ter mensagem clara
9. Antes de abrir Pull Request, testar o que foi feito
10. Não deixem muitos dias sem enviar alterações


## COMANDOS MAIS UTILIZADOS
**Clonar o projeto (Antes de clonar, acesse a pasta onde quer clonar)**
git clone URL_DO_REPOSITORIO

**Entrar na Pasta**
cd nome-do-projeto

**Ver em qual branch está**
git branch

**Baixar alterações da main**
git checkout main
git pull origin main

**Criar uma branch nova**
git checkout -b nome-da-branch

**Ver status dos arquivos**
git status

**Enviar alterações**
git add .
git commit -m "mensagem"
git push -u origin nome-da-branch

**Baixar alterações do repositório**
git pull

**Trazer mudanças da main para sua branch**
git checkout nome_da_branch
git merge main

**Trocar de branch**
git checkout main

**Ver histórico de commits**
git log --oneline