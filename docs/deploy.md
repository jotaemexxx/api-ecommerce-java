# Deploy do backend em dev/hmg (Supabase + Render)

Objetivo: subir a API + banco na web para testar o frontend contra uma API real, sem
depender da sua máquina ligada. Estratégia mais simples e gratuita:

- **Banco:** Supabase (Postgres hospedado) — perfil `supabase` já existe no projeto.
- **Backend:** Render (Web Service via Docker) — usa o `Dockerfile` da raiz.

---

## 1. Banco de dados no Supabase

1. Crie uma conta em https://supabase.com e um **novo projeto** (região **São Paulo /
   sa-east-1** para menor latência).
2. Defina uma senha de banco forte quando solicitado — **guarde-a** (será `DB_PASSWORD`).
3. Em **Project Settings → Database → Connection pooling**, copie os dados da conexão
   **Transaction pooler** (porta `6543`). O `application-supabase.properties` já aponta
   para o host do pooler `aws-0-sa-east-1.pooler.supabase.com:6543`.
   - **Usuário** (`DB_USERNAME`): tem o formato `postgres.<referencia-do-projeto>`.
   - **Senha** (`DB_PASSWORD`): a que você definiu no passo 2.

> O perfil supabase usa `ddl-auto=update`: o Hibernate cria/atualiza as tabelas sozinho
> no primeiro boot. Não precisa rodar SQL manual.

---

## 2. Backend no Render

### Opção A — via Blueprint (recomendada, usa o `render.yaml`)

1. Suba este repositório para o GitHub (se ainda não estiver).
2. Em https://render.com → **New → Blueprint**, conecte o repositório. O Render lê o
   `render.yaml` e propõe criar o serviço `ecommerce-api`.
3. Preencha as variáveis marcadas como "segredo" (o Render pergunta):
   - `DB_USERNAME` → usuário do Supabase (`postgres.<ref>`)
   - `DB_PASSWORD` → senha do Supabase
   - `APP_CORS_ALLOWED_ORIGINS` → por ora `http://localhost:4200`; depois acrescente a
     URL do front hospedado, separando por vírgula.
   - `JWT_SECRET` → já é gerado automaticamente (`generateValue`).
   - `SPRING_PROFILES_ACTIVE` → já vem como `supabase`.
4. **Create** e aguarde o build do Docker + deploy (primeira vez leva alguns minutos).

### Opção B — manual (sem Blueprint)

1. **New → Web Service**, conecte o repositório.
2. **Runtime:** Docker (o Render detecta o `Dockerfile` da raiz).
3. **Plan:** Free.
4. Em **Environment**, adicione as variáveis:
   `SPRING_PROFILES_ACTIVE=supabase`, `JWT_SECRET=<gere um valor aleatório longo>`,
   `DB_USERNAME`, `DB_PASSWORD`, `APP_CORS_ALLOWED_ORIGINS`.
5. **Create Web Service**.

---

## 3. Validar

Após o deploy, o Render dá uma URL pública `https://<nome>.onrender.com`. Teste o
endpoint público de produtos:

```bash
curl -i https://<nome>.onrender.com/api/products
```

Esperado: `200 OK` com um array JSON (vazio `[]` se ainda não há produtos).

Para criar o primeiro produto você precisa de um usuário **ADMIN**. Fluxo rápido:

```bash
# 1. cadastrar um usuario (vem como USER)
curl -X POST https://<nome>.onrender.com/api/users \
  -H "Content-Type: application/json" \
  -d '{"name":"Dono","email":"dono@loja.com","number":"11999999999","password":"senha123"}'

# 2. promover para ADMIN: so outro ADMIN pode. No primeiro deploy, promova direto no
#    banco (Supabase → Table editor → tabela users → coluna role = ADMIN), ou crie um
#    seed inicial. Depois disso, use PATCH /api/users/{id}/promote autenticado como ADMIN.

# 3. logar e pegar o token
curl -X POST https://<nome>.onrender.com/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"dono@loja.com","password":"senha123"}'
```

---

## Notas

- **Cold start:** no free tier, o serviço "dorme" após ~15 min sem tráfego; a primeira
  request depois disso demora alguns segundos. Normal para dev/hmg.
- **Trocar de provedor:** o `Dockerfile` é padrão; Railway, Koyeb e Fly.io aceitam o mesmo
  container. Só muda onde você configura as variáveis de ambiente.
- **Segredos:** nunca commite `JWT_SECRET`, `DB_USERNAME` ou `DB_PASSWORD`. Eles vivem só
  nas variáveis de ambiente do provedor e no seu `.env` local (que está no `.gitignore`).
- **Rodar o container localmente** (teste rápido com H2, sem banco externo):
  ```bash
  docker build -t ecommerce-api .
  docker run -p 8080:8080 -e SPRING_PROFILES_ACTIVE=h2 ecommerce-api
  ```
