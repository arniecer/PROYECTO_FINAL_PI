#!/bin/bash

# backup incremental: guarda los cambios del dia
# se ejecuta de lunes a sabado a las 3 de la manana
# los cambios se guardan en archivos binlog

fecha=$(date +%Y-%m-%d)
pass="root"
carpeta="/home/ubuntu/backups"

mkdir -p $carpeta/incrementales

echo "[$(date)] Empezando backup incremental del dia $fecha"

binlogs=$(sudo docker exec grandspicy-db \
  mysql -u root -p$pass -N -e "SHOW BINARY LOGS;" | awk "{print \$1}")

for log in $binlogs; do
  if [ -f "$carpeta/incrementales/$log" ]; then
    continue
  fi
  sudo docker cp grandspicy-db:/var/lib/mysql/$log $carpeta/incrementales/$log
  if [ $? -eq 0 ]; then
    echo "  guardado: $log"
  fi
done

find $carpeta/incrementales -name "binlog.*" -mtime +28 -delete

echo "[$(date)] Backup incremental terminado"
