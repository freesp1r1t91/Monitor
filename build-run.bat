@echo off
set DOCKER_USER=freesp1r1t91

cd EventOrder
call mvn clean package
::Eliminar los siguientes comentarios para hacer push
::docker build -t %DOCKER_USER%/event-service:latest .
::docker push %DOCKER_USER%/event-service:latest

::Eliminar los siguientes comentarios para hacer push
cd ..\EventDelivery
call mvn clean package
::docker build -t %DOCKER_USER%/order-service:latest .
::docker push %DOCKER_USER%/order-service:latest

::Modificar comentarios en docker compose para descargar de hub
cd ..
docker compose build
docker compose up