# spring-ai-rag-example
Пример использования RAG (retrieval augmented generation) в **Spring AI**.

## Настройка векторного хранилища
На ubuntu-подобные системы расширение можно установить командой:

```bash
sudo apt install postgresql-XX-pgvector
# где XX - номер версии postgres, которую вы используете
```

А затем в самой БД выполнить скрипт, создающий таблицу **vector_store**.

```sql
create extension if not exists vector;
create extension if not exists hstore;
create extension if not exists "uuid-ossp";

create table if not exists vector_store (
    id uuid default uuid_generate_v4() primary key,
    content text,
    metadata json,
    embedding vector(1536) -- 1536 is the default embedding dimension
);

create index on vector_store using hnsw (embedding vector_cosine_ops);
```

## Переменные окружения
Для запуска приложения требуется определить следующие переменные окружения:
- `JDBC_URL` - url для подключения к БД **postgres** с установленным расширением **pgVector**
- `JDBC_USER` и `JDBC_PASSWORD` - имя пользователя и пароль для подключения к БД
- `OPEN_AI_API_KEY` - ключ для выполнения запросов к Open AI
- `OPEN_AI_BASE_URL` (опционально) - позволяет переопределить эндпоинт LLM, к которому происходит подключение (при использовании другой LLM с совместимым протоколом)

Данный проект содержит `Dockerfile`, поэтому его можно легко развернуть в облачном хостинге.

### Полезные ссылки
* [Spring AI: retrieval augmented generation](https://devmark.ru/article/spring-ai-rag)
* [dockhost.ru](https://dockhost.ru/?utm_source=devmark&utm_medium=cpa&utm_campaign=devmark&p=z8i9gexg) - облачный хостинг по технологии `Push-to-Deploy`.
* [Новости проекта](https://t.me/+RjrPWNUEwf8wZTMy) и короткие заметки.
* Ещё больше статей по разработке ПО вы можете найти на моём сайте [devmark.ru](https://devmark.ru/).
