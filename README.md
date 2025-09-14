# 🚢 Batalha Naval – Java CLI

Jogo clássico **Batalha Naval** implementado em **Java** no console.  
Você enfrenta uma **IA** em diferentes níveis de dificuldade, usando tiros simples e especiais para afundar a frota inimiga! ⚔️

---

## 🎮 Funcionalidades
- 3 níveis de dificuldade (Fácil, Médio, Difícil).
- Frota de embarcações variadas (Porta-Aviões, Cruzador, Fragata, Destróier, Submarino).
- Armas especiais:
  - 🔹 **Tiro Simples**
  - 🔹 **Tiro Duplo**
  - 🔹 **Tiro Explosivo**
- Sistema de eventos com atraso de radar no modo Difícil.
- IA básica (aleatória) + estrutura para IA mais avançada.

---

## 🗂️ Estrutura
- **bootstrap** → ponto de entrada (main).  
- **controlador** → controla o fluxo do jogo.  
- **aplicacao** → regras de turno, motor de regras e IA.  
- **dominio** → embarcações, armas, tabuleiro, eventos, jogador.  
- **infraestrutura** → configuração do jogo.  
- **visao** → interação via console.

---


## ▶️ Como Executar

# Compile todos os arquivos .java para a pasta bin
javac -d bin src/**/*.java

# Rode a classe principal
java -cp bin br.com.batalhanaval.bootstrap.Principal

💡 Certifique-se de estar usando o Java 17 ou superior.

---

## 👥 Integrantes

- Maria Eduarda Ferreira Bianchini RA: 081230001
- Ana Marta de Souza Santos RA: 082230041

---

## 📌 Observação

Este projeto foi desenvolvido para fins acadêmicos, na disciplina Linguagem de Programação II,
lecionada pelo professor Israel Florentino.

Foram aplicados conceitos de POO e padrões de projeto em Java:

- Strategy
- Observer
- Factory
- Singleton

---

## 📊 Documentação Adicional

Além deste README, o projeto também conta com um **Diagrama de Classes UML**, disponível na raiz do repositório.  
Esse diagrama detalha os principais pacotes, classes e relacionamentos do sistema, servindo como apoio para o entendimento da arquitetura e da modelagem orientada a objetos.


