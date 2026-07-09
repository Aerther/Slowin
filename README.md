# Slowin

Projeto desenvolvido para a disciplina de Desenvolvimento Web do **4º ano do Curso Técnico em Informática**.

## Integrantes

* Arthur Lassem
* Leonardo

**Turma:** 4º TI

## Repositório

https://github.com/Aerther/Slowin

---

# Sobre o Projeto

O **Slowin** é uma aplicação web desenvolvida em **Java** utilizando **Spring Boot**, inspirada no universo da Fórmula 1.

O projeto foi criado para aplicar, na prática, os conhecimentos adquiridos durante o curso por meio do desenvolvimento de uma aplicação completa, utilizando conceitos como arquitetura MVC, desenvolvimento web, persistência de dados e consumo de APIs REST.

---

# Objetivo

O principal objetivo do projeto é consolidar os conhecimentos adquiridos ao longo do curso por meio da criação de uma aplicação funcional. Além disso, busca proporcionar uma experiência interativa para os usuários, permitindo o gerenciamento de pilotos e corridas, bem como a simulação de eventos inspirados na Fórmula 1.

O nome **Slowin** surgiu da junção das palavras **Slow** e **Win**, representando a ideia de que persistência, dedicação e evolução constante são fundamentais para alcançar bons resultados.

---

# Tecnologias Utilizadas

* Java
* Spring Boot
* Spring MVC
* Spring Data JPA
* Thymeleaf
* HTML5
* CSS3
* JavaScript
* Maven
* H2 Database

---

# Funcionalidades

* Gerenciamento de pilotos;
* Gerenciamento de corridas;
* Consulta de pistas previamente cadastradas;
* Simulação de corridas;
* Consumo de APIs externas para obtenção de informações complementares.

---

# APIs Utilizadas

### Open-Meteo

API utilizada para obter dados meteorológicos relacionados às pistas de corrida.

https://open-meteo.com/

### ApiCountries

API utilizada para obter informações dos países, como nome e bandeira.

https://www.apicountries.com/

---

# Como Executar

## Pré-requisitos

* Java 21 (ou versão compatível)
* Maven

Clone o repositório:

```bash
git clone https://github.com/Aerther/Slowin.git
```

Acesse a pasta do projeto:

```bash
cd Slowin
```

Execute a aplicação:

```bash
./mvnw spring-boot:run
```

Depois, acesse:

```text
http://localhost:8080
```
