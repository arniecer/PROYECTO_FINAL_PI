#!/bin/bash
# Configura SSL con Certbot para GrandS.yatat.es
# Ejecutar en el EC2 tras el primer docker compose up

DOMAIN="GrandS.yatat.es"
EMAIL="admin@grandspicy.com"

echo "=== Paso 1: Verificar que nginx responde en :80 ==="
curl -sI http://$DOMAIN/.well-known/acme-challenge/test > /dev/null 2>&1
if [ $? -ne 0 ]; then
    echo "ERROR: No se puede acceder a http://$DOMAIN/.well-known/acme-challenge/"
    echo "Asegurate de que el DNS apunte a la IP elastica y el contenedor nginx este corriendo."
    exit 1
fi

echo "=== Paso 2: Obtener certificado SSL con Certbot ==="
sudo docker compose run --rm certbot certonly --webroot \
    --webroot-path=/var/www/certbot \
    -d $DOMAIN \
    --email $EMAIL \
    --agree-tos \
    --non-interactive

if [ $? -ne 0 ]; then
    echo "ERROR: Certbot fallo. Revisa los logs."
    exit 1
fi

echo "=== Paso 3: Copiar config SSL ==="
cp nginx/default-ssl.conf nginx/default.conf

echo "=== Paso 4: Recargar nginx ==="
sudo docker compose exec nginx nginx -s reload

echo "=== SSL configurado correctamente ==="
echo "Visita https://$DOMAIN"
