ALTER TABLE public.medical_records
    DROP COLUMN heart_rate;

ALTER TABLE public.medical_records
    ADD COLUMN heart_rate_bpm INTEGER;