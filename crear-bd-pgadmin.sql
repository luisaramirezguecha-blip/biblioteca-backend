-- Para pgAdmin (Query Tool). NO usa comandos de psql, así que se ejecuta completo con F5.
-- Antes: crear la base biblioteca_db (clic derecho en Databases > Create > Database...) y abrir el Query Tool SOBRE esa base.
-- Se puede repetir sin errores: crea el usuario o le actualiza la clave si ya existe.

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

-- Lo hace dueño de la base: así Flyway puede crear las tablas y los datos
ALTER DATABASE biblioteca_db OWNER TO biblioteca;
GRANT ALL PRIVILEGES ON DATABASE biblioteca_db TO biblioteca;
-- Desde PostgreSQL 15 el esquema public ya no es escribible por cualquiera: sin esto Flyway falla.
GRANT ALL ON SCHEMA public TO biblioteca;
