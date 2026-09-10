-- Atualização da base antiga para o tema 13. Execute no saep_agendamento_db.
-- Preserva clientes e colunas legadas; pode ser executado novamente.
BEGIN;
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema='public' AND table_name='clientes' AND column_name='cpf')
       AND NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema='public' AND table_name='clientes' AND column_name='documento') THEN
        ALTER TABLE public.clientes RENAME COLUMN cpf TO documento;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema='public' AND table_name='agendamentos' AND column_name='sala_id') THEN
        ALTER TABLE public.agendamentos ALTER COLUMN sala_id DROP NOT NULL;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema='public' AND table_name='agendamentos' AND column_name='procedimento_id') THEN
        ALTER TABLE public.agendamentos ALTER COLUMN procedimento_id DROP NOT NULL;
    END IF;
END $$;
ALTER TABLE public.clientes ALTER COLUMN documento TYPE VARCHAR(255);
ALTER TABLE public.clientes ALTER COLUMN documento SET NOT NULL;
CREATE UNIQUE INDEX IF NOT EXISTS uk_clientes_documento ON public.clientes(documento);
COMMIT;
