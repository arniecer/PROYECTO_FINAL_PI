#!/bin/bash

# backup completo de la base de datos y los certificados
# se ejecuta los domingos a las 2 de la manana

fecha=$(date +%Y-%m-%d)
pass="root"
proyecto="/home/ubuntu/PROYECTO_FINAL_PI"
carpeta="/home/ubuntu/backups"

mkdir -p $carpeta/totales
mkdir -p $carpeta/incrementales

echo "[$(date)] Empezando backup completo del dia $fecha"

sudo docker exec grandspicy-db \
  mysqldump -u root -p$pass \
  --databases grandspicy \
  --routines --triggers --flush-logs \
  | gzip > $carpeta/totales/grandspicy_$fecha.sql.gz

if [ $? -eq 0 ]; then
  echo "[$(date)] Backup de la base de datos guardado"
else
  echo "[$(date)] ERROR: algo fallo en el volcado de la base de datos"
fi

sudo docker exec grandspicy-db \
  mysql -u root -p$pass -e "SHOW MASTER STATUS;" \
  > $carpeta/totales/posicion_binlog_$fecha.txt

sudo tar -czf $carpeta/totales/certificados_$fecha.tar.gz \
  -C $proyecto certbot/conf/ 2>/dev/null

find $carpeta/totales -name "grandspicy_*.sql.gz" -mtime +28 -delete
find $carpeta/totales -name "certificados_*.tar.gz" -mtime +28 -delete
find $carpeta/totales -name "posicion_binlog_*.txt" -mtime +28 -delete

echo "[$(date)] Backup completo terminado"
