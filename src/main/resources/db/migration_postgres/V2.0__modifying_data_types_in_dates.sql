ALTER TABLE public.chronic_diseases ALTER COLUMN created_date TYPE timestamp USING created_date::timestamp;
ALTER TABLE public.chronic_diseases ALTER COLUMN updated_date TYPE timestamp USING updated_date::timestamp;
ALTER TABLE public.medical_records ALTER COLUMN created_date TYPE timestamp USING created_date::timestamp;
ALTER TABLE public.medical_records ALTER COLUMN updated_date TYPE timestamp USING updated_date::timestamp;