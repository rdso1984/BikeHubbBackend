-- Criação da tabela bike_images para armazenar imagens das bicicletas
-- Execute este script no seu banco de dados Supabase

CREATE TABLE IF NOT EXISTS bike_images (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    bicycle_id UUID NOT NULL,
    original_filename VARCHAR(255) NOT NULL,
    content_type VARCHAR(100),
    file_size BIGINT,
    image_data BYTEA NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_primary BOOLEAN DEFAULT FALSE,
    
    -- Foreign key para tabela bicycles
    CONSTRAINT fk_bike_images_bicycle 
        FOREIGN KEY (bicycle_id) 
        REFERENCES bicycles(id) 
        ON DELETE CASCADE
);

-- Índices para melhorar performance
CREATE INDEX IF NOT EXISTS idx_bike_images_bicycle_id ON bike_images(bicycle_id);
CREATE INDEX IF NOT EXISTS idx_bike_images_primary ON bike_images(bicycle_id, is_primary) WHERE is_primary = true;
CREATE INDEX IF NOT EXISTS idx_bike_images_created_at ON bike_images(created_at);

-- Comentários para documentação
COMMENT ON TABLE bike_images IS 'Tabela para armazenar imagens das bicicletas diretamente no banco de dados';
COMMENT ON COLUMN bike_images.id IS 'Identificador único da imagem';
COMMENT ON COLUMN bike_images.bicycle_id IS 'Referência para a bicicleta (FK)';
COMMENT ON COLUMN bike_images.original_filename IS 'Nome original do arquivo enviado';
COMMENT ON COLUMN bike_images.content_type IS 'Tipo MIME da imagem (ex: image/jpeg, image/png)';
COMMENT ON COLUMN bike_images.file_size IS 'Tamanho do arquivo em bytes';
COMMENT ON COLUMN bike_images.image_data IS 'Dados binários da imagem (BLOB)';
COMMENT ON COLUMN bike_images.created_at IS 'Data e hora de criação do registro';
COMMENT ON COLUMN bike_images.is_primary IS 'Indica se é a imagem principal da bicicleta';

-- Verificar se a tabela foi criada
SELECT 
    schemaname,
    tablename,
    tableowner
FROM pg_tables 
WHERE tablename = 'bike_images';

-- Verificar estrutura da tabela
SELECT 
    column_name,
    data_type,
    is_nullable,
    column_default
FROM information_schema.columns 
WHERE table_name = 'bike_images'
ORDER BY ordinal_position;
