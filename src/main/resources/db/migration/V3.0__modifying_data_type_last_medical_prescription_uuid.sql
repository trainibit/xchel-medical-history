ALTER TABLE public.medical_records
    ADD COLUMN temp_last_medical_prescription_uuid uuid;

ALTER TABLE public.medical_records
DROP COLUMN last_medical_prescription_uuid;

ALTER TABLE public.medical_records
    RENAME COLUMN temp_last_medical_prescription_uuid TO last_medical_prescription_uuid;