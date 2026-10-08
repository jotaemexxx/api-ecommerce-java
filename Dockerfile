# syntax=docker/dockerfile:1

### Stage 1: build do jar usando o Maven Wrapper ###
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Copia o modulo da API (pom.xml, wrapper mvnw e codigo-fonte)
COPY api/ ./

# Da permissao de execucao ao wrapper e empacota.
# Pulamos os testes aqui porque o build da imagem nao deve depender de banco/ambiente;
# os testes rodam no fluxo de CI/local (./mvnw test).
RUN chmod +x mvnw && ./mvnw -q -DskipTests package

### Stage 2: imagem final enxuta, apenas com o JRE ###
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copia somente o jar gerado no stage de build
COPY --from=build /app/target/api-0.0.1-SNAPSHOT.jar app.jar

# Porta padrao local; em PaaS a env PORT sobrescreve (ver application.properties)
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
