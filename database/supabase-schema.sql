-- Run this in Supabase SQL Editor to create the CodeQuest cloud data table.
create table if not exists public.user_data (
  user_id text primary key,
  data jsonb not null default '{}'::jsonb,
  updated_at timestamptz not null default now()
);
alter table public.user_data enable row level security;
-- Access is through the server-side Vercel function only, using SUPABASE_SERVICE_ROLE_KEY.
-- Never expose the service-role key in the APK or frontend. Configure Supabase Auth before storing sensitive user data.
