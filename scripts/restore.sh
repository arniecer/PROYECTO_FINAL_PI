#!/bin/bash

# uso: ./restore.sh grandspicy_2026-06-07.sql.gz
# restaura el backup completo de esa fecha
# y pregunta si aplicar los incrementales

carpeta="/home/ubuntu/backups"

if [ -z "$1" ]; then
  echo "Tienes que decirme que backup quieres restaurar"
  echo ""
  echo "Backups disponibles:"
  ls -1 $carpeta/totales/*.sql.gz 2>/dev/null || echo "  (no hay ninguno)"
  exit 1
fi

backup="$carpeta/totales/$1"
if [ ! -f "$backup" ]; then
  echo "No encuentro el archivo $backup"
  exit 1
fi

fecha_backup=$(echo $1 | sed 's/grandspicy_//' | sed 's/\.sql\.gz//')

echo "Restaurando el backup completo de $fecha_backup ..."
gunzip -c "$backup" | sudo docker exec -i grandspicy-db mysql -u root -proot

if [ $? -ne 0 ]; then
  echo "ERROR: no se pudo restaurar la base de datos"
  exit 1
fi

echo "Base de datos restaurada correctamente"

echo ""
echo "Archivos incrementales disponibles:"
ls -1 $carpeta/incrementales/binlog.* 2>/dev/null || echo "  (no hay)"

echo ""
read -p "Quieres aplicar los cambios incrementales? (s/n): " respuesta

if [ "$respuesta" != "s" ] && [ "$respuesta" != "S" ]; then
  echo "Restauracion completada (sin incrementales)"
  exit 0
fi

for log in $(ls -1 $carpeta/incrementales/binlog.* 2>/dev/null | sort); do
  echo "  aplicando cambios de: $(basename $log)"
  sudo docker exec -i grandspicy-db mysqlbinlog $log | sudo docker exec -i grandspicy-db mysql -u root -proot
  if [ $? -ne 0 ]; then
    echo "  aviso: puede que algunos cambios ya estuvieran aplicados, seguimos"
  fi
done

echo "Restauracion completada con todos los cambios"
