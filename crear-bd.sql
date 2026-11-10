-- SOLO para psql (usa \gexec y \connect). En pgAdmin NO sirve: usa crear-bd-pgadmin.sql.
-- Ejecutar como superusuario (postgres). Se puede repetir sin errores: crea lo que falta y actualiza lo demás.
--   psql -U postgres -h localhost -p 5432 -f crear-bd.sql

-- Crea el usuario (o le actualiza la clave si ya existe)
DO $$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_roles WHERE rolname = 'biblioteca') THEN
        CREATE USER biblioteca WITH PASSWORD 'biblioteca';
    ELSE
        ALTER USER biblioteca WITH PASSWORD 'biblioteca';
    END IF;
END
$$;

-- Crea la base solo si no existe (CREATE DATABASE no puede ir dentro de un bloque DO)
SELECT 'CREATE DATABASE biblioteca_db OWNER biblioteca'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'biblioteca_db')\gexec

\connect biblioteca_db

-- Lo hace dueño de la base: así Flyway puede crear las tablas y los datos
ALTER DATABASE biblioteca_db OWNER TO biblioteca;
GRANT ALL PRIVILEGES ON DATABASE biblioteca_db TO biblioteca;
-- Desde PostgreSQL 15 el esquema public ya no es escribible por cualquiera: sin esto Flyway falla.
GRANT ALL ON SCHEMA public TO biblioteca;
