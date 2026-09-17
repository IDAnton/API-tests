#!/bin/bash
set -e

JDK_25_PATH=$(ls -d "/c/Users/anton/.jdks/openjdk-25"* 2>/dev/null | tail -n 1)

export JAVA_HOME="$JDK_25_PATH"

IDEA_PATH=$(ls -d "/c/Program Files/JetBrains/IntelliJ IDEA "* 2>/dev/null | tail -n 1)
export PATH="$JAVA_HOME/bin:$IDEA_PATH/plugins/maven/lib/maven3/bin:$PATH"

java -version

echo "=== docker-compose up -d ==="
docker-compose up -d

echo "=== 2. PostgreSQL Healthcheck ==="
COUNTER=0
MAX_ATTEMPTS=15

until docker-compose ps postgres | grep -q "(healthy)"; do
    if [ $COUNTER -eq $MAX_ATTEMPTS ]; then
        echo "Ошибка: PostgreSQL не смог запуститься!"
        docker-compose down -v
        exit 1
    fi
    echo "PostgreSQL еще запускается... ожидание 2 секунды (Попытка $((COUNTER+1)) из $MAX_ATTEMPTS)"
    sleep 2
    COUNTER=$((COUNTER+1))
done
echo "PostgreSQL готов к работе!"

echo "=== Запуск тестов ==="
set +e
mvn clean test
TEST_EXIT_CODE=$?
set -e

echo "=== Генерация Allure отчета ==="
mvn allure:report

echo "=== Очистка volumens Docker ==="
docker-compose down -v

echo "=== Готово! ==="
exit $TEST_EXIT_CODE